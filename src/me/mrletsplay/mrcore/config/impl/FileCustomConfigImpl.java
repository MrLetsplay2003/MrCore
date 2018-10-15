package me.mrletsplay.mrcore.config.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import me.mrletsplay.mrcore.config.impl.DefaultConfigParser.Marker;
import me.mrletsplay.mrcore.config.v2.ConfigException;
import me.mrletsplay.mrcore.config.v2.ConfigSection;
import me.mrletsplay.mrcore.config.v2.FileCustomConfig;

public class FileCustomConfigImpl implements FileCustomConfig {

	private File configFile;
	private ConfigSection mainSection;
	
	public FileCustomConfigImpl(File configFile) {
		this.configFile = configFile;
		this.mainSection = new DefaultConfigSectionImpl(this, null, null);
	}
	
	@Override
	public ConfigSection getMainSection() {
		return mainSection;
	}

	@Override
	public void load(InputStream in) throws ConfigException {
		try(BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
			List<String> lines = new ArrayList<>();
			String s;
			while((s = r.readLine()) != null) {
				lines.add(s + "\n");
			}
			r.close();
			DefaultConfigParser p = new DefaultConfigParser(lines.toArray(new String[lines.size()]));
			DefaultConfigSectionImpl sc = new DefaultConfigSectionImpl(null, null, null);
			sc.loadFromMap(p.readSubsection(new Marker(0, 0), 0).getProperties());
		}catch(IOException e) {
			throw new ConfigException("Unexpected IO exception", e);
		}
	}

	@Override
	public void save(OutputStream out) {
		try(BufferedWriter o = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("TEST/abc_s.yml")))) {
			DefaultConfigFormatter f = new DefaultConfigFormatter(o);
			f.writeSubsection(0, getMainSection());
			o.close();
		}catch(IOException e) {
			throw new ConfigException("Unexpected IO exception", e);
		}
	}

	@Override
	public File getConfigFile() {
		return configFile;
	}
	
	
	
}
