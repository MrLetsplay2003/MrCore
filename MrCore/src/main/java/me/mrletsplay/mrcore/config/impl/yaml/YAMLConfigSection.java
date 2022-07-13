package me.mrletsplay.mrcore.config.impl.yaml;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import me.mrletsplay.mrcore.config.CustomConfig;
import me.mrletsplay.mrcore.config.StringifiableConfigSection;
import me.mrletsplay.mrcore.config.impl.AbstractConfigSection;

public class YAMLConfigSection extends AbstractConfigSection implements StringifiableConfigSection {

	public YAMLConfigSection(CustomConfig config) {
		super(config);
	}

	@Override
	public String saveToString() {
		return saveToString(0);
	}

	public String saveToString(int indents) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(bOut));
		YAMLConfigFormatter f = new YAMLConfigFormatter(w);
		try {
			f.writeSubsection(indents, toMap(), commentsToMap());
			w.close();
		} catch (IOException e) {}
		return new String(bOut.toByteArray(), StandardCharsets.UTF_8);
	}

}
