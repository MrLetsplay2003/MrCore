package me.mrletsplay.mrcore.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

import org.bukkit.configuration.file.YamlConfiguration;

public class JARLoader extends URLClassLoader {

	private URL url, jarURL;
	
	public JARLoader(File file) throws MalformedURLException {
		super(new URL[] {file.toURI().toURL()});
		this.url = file.toURI().toURL();
		this.jarURL = new URL("jar", "", url + "!/");
	}
	
	public Class<?> getJavaMainClass() throws IOException, ClassNotFoundException {
		JarURLConnection con = (JarURLConnection) jarURL.openConnection();
		Attributes attr = con.getMainAttributes();
		if(attr == null) return null;
		String name = attr.getValue(Attributes.Name.MAIN_CLASS);
		if(name == null) return null;
		return loadClass(name);
	}
	
	public Class<?> getBukkitMainClass() throws IOException, ClassNotFoundException {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("plugin.yml").openStream()));
		return loadClass(c.getString("main"));
	}
	
}
