package me.mrletsplay.mrcore.mysql.protocol.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.mrletsplay.mrcore.mysql.protocol.MySQLServerConnection;

public class MySQLNative41PasswordAuth implements MySQLAuthPluginBase {

	@Override
	public byte[] getInitialAuthResponse(String password, byte[] authData) {
		return createNative41AuthResponse(authData, password);
	}

	@Override
	public void handleFurtherProcessing(MySQLServerConnection con) throws IOException {} // No further processing required
	
	private byte[] createNative41AuthResponse(byte[] randomChallenge, String password) {
        // SHA1( password ) XOR SHA1( "20-bytes random data from server" <concat> SHA1( SHA1( password ) ) )
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] pwBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			byte[] rightSeed = new byte[40];
			System.arraycopy(randomChallenge, 0, rightSeed, 0, 20);
			System.arraycopy(digest.digest(pwBytes), 0, rightSeed, 20, 20);
			byte[] right = digest.digest(rightSeed);
			for(int i = 0; i < pwBytes.length; i++) {
				pwBytes[i] ^= right[i];
			}
			return pwBytes;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MySQLAuthPlugin getType() {
		return MySQLAuthPlugin.MYSQL_NATIVE_PASSWORD;
	}

}
