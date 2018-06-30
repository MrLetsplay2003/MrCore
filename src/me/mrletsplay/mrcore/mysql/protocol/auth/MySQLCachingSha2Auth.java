package me.mrletsplay.mrcore.mysql.protocol.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLException;
import java.util.Base64;

import javax.crypto.Cipher;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;
import me.mrletsplay.mrcore.mysql.protocol.io.RawPacket;

public class MySQLCachingSha2Auth implements MySQLAuthPluginBase {

    private static final int CACHING_SHA2_DIGEST_LENGTH = 32;
//    private static final int SEED_LENGTH = 20;
    private String password;
    private byte[] authData;

	@Override
	public byte[] getInitialAuthResponse(String password, byte[] authData) {
		try {
			this.password = password;
			this.authData = authData;
			return scrambleCachingSha2(password.getBytes(StandardCharsets.UTF_8), authData);
		} catch (DigestException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void handleFurtherProcessing(MySQLServerConnection con) throws IOException {
		RawPacket raw = con.readPacket(); // Fast auth result
		switch(raw.getPayload()[1]) {
			case 3:
				// Auth completed
				return;
			case 4:
				//Full auth needed
				break;
		}
		con.sendPacket(RawPacket.of(new byte[] {0x02}));
		RawPacket r2 = con.readPacket();
		con.sendPacket(RawPacket.of(encryptPassword(password, new String(r2.getPayload(), 1, r2.getLength()-1, StandardCharsets.UTF_8), authData)));
	}
	
    public static byte[] scrambleCachingSha2(byte[] password, byte[] seed) throws DigestException {
        /*
         * Server does it in 4 steps (see sql/auth/sha2_password_common.cc Generate_scramble::scramble method):
         * 
         * SHA2(src) => digest_stage1
         * SHA2(digest_stage1) => digest_stage2
         * SHA2(digest_stage2, m_rnd) => scramble_stage1
         * XOR(digest_stage1, scramble_stage1) => scramble
         */
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }

        byte[] dig1 = new byte[CACHING_SHA2_DIGEST_LENGTH];
        byte[] dig2 = new byte[CACHING_SHA2_DIGEST_LENGTH];
        byte[] scramble1 = new byte[CACHING_SHA2_DIGEST_LENGTH];

        // SHA2(src) => digest_stage1
        md.update(password, 0, password.length);
        md.digest(dig1, 0, CACHING_SHA2_DIGEST_LENGTH);
        md.reset();

        // SHA2(digest_stage1) => digest_stage2
        md.update(dig1, 0, dig1.length);
        md.digest(dig2, 0, CACHING_SHA2_DIGEST_LENGTH);
        md.reset();

        // SHA2(digest_stage2, m_rnd) => scramble_stage1
        md.update(dig2, 0, dig1.length);
        md.update(seed, 0, seed.length);
        md.digest(scramble1, 0, CACHING_SHA2_DIGEST_LENGTH);

        // XOR(digest_stage1, scramble_stage1) => scramble
        byte[] mysqlScrambleBuff = new byte[CACHING_SHA2_DIGEST_LENGTH];
        xorString(dig1, mysqlScrambleBuff, scramble1, CACHING_SHA2_DIGEST_LENGTH);

        return mysqlScrambleBuff;
    }
	
    public static void xorString(byte[] from, byte[] to, byte[] scramble, int length) {
        int pos = 0;
        int scrambleLength = scramble.length;

        while (pos < length) {
            to[pos] = (byte) (from[pos] ^ scramble[pos % scrambleLength]);
            pos++;
        }
    }
    
    protected byte[] encryptPassword(String password, String publicKey, byte[] authData) {
        byte[] input = null;
        input = password != null ? password.concat("\0").getBytes() : new byte[] { 0 };
        byte[] mysqlScrambleBuff = new byte[input.length];
        xorString(input, mysqlScrambleBuff, authData, input.length);
        return encryptWithRSAPublicKey(mysqlScrambleBuff,
                decodeRSAPublicKey(publicKey), "RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    }

    
    public static RSAPublicKey decodeRSAPublicKey(String key) {

        try {
            if (key == null) {
                throw new SQLException("key parameter is null");
            }

            int offset = key.indexOf("\n") + 1;
            int len = key.indexOf("-----END PUBLIC KEY-----") - offset;

            byte[] b = new byte[len];
            System.arraycopy(key.getBytes(StandardCharsets.UTF_8), offset, b, 0, len);
            byte[] certificateData = Base64.getDecoder().decode(new String(b).replace("\n", "").getBytes());

            X509EncodedKeySpec spec = new X509EncodedKeySpec(certificateData);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] encryptWithRSAPublicKey(byte[] source, RSAPublicKey key, String transformation) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(source);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

	@Override
	public MySQLAuthPlugin getType() {
		return MySQLAuthPlugin.CACHING_SHA2_PASSWORD;
	}

}
