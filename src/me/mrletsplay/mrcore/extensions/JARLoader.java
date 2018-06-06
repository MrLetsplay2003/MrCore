package me.mrletsplay.mrcore.extensions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

import org.bukkit.configuration.file.YamlConfiguration;

import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class JARLoader extends URLClassLoader {

	private URL url, jarURL;
	private ClassLoader parent;
	private JarURLConnection jarConnection;
	private File file;
	
	public JARLoader(File file) throws IOException {
		super(new URL[] {file.toURI().toURL()});
		this.url = file.toURI().toURL();
		this.file = file;
		this.jarURL = new URL("jar", "", url + "!/");
		this.parent = JARLoader.class.getClassLoader();
		jarConnection = (JarURLConnection) jarURL.openConnection();
		
		try {
			InputStream in = new FileInputStream(new File("plugins/MrCore_BukkitImpl/JavaPlugin.class"));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			byte[] objp = out.toByteArray();
			defineClass("org.bukkit.plugin.java.JavaPlugin", objp, 0, objp.length); // TODO: Download/Include
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Class<?> getJavaMainClass() throws IOException, ClassNotFoundException {
		JarURLConnection con = (JarURLConnection) jarURL.openConnection();
		Attributes attr = con.getMainAttributes();
		if(attr == null) return null;
		String name = attr.getValue(Attributes.Name.MAIN_CLASS);
		if(name == null) return null;
		return loadClass(name);
	}
	
	public JarURLConnection getJarConnection() {
		return jarConnection;
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return loadClassInternally(name, true);
	}
	
	public Class<?> loadClassInternally(String name, boolean usePluginClasses) throws ClassNotFoundException {
		try {
			return super.loadClass(name);
		}catch(ClassNotFoundException e) {
			try {
				return parent.loadClass(name); //If class is not found in the jar, return it from this classloader
			}catch(ClassNotFoundException e1) {
				if(!usePluginClasses) throw e1;
				return loadClassFromPlugins(name);
			}
		}
	}
	
	public Class<?> loadClassFromPlugins(String name) {
		Class<?> clazz = null;
		for(MrCoreExtension ex : MrCorePluginLoader.getInstance().getAllPlugins()) {
			try {
				clazz = ex.getLoader().loadClassInternally(name, false);
			}catch(ClassNotFoundException e2) {}
		}
		return clazz;
	}
	
	public Class<?> getBukkitMainClass() throws IOException, ClassNotFoundException {
		URL url = getResource("plugin.yml");
		if(url == null) return null;
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new InputStreamReader(url.openStream()));
		if(c == null) throw new FriendlyException("Failed to load "+file.getName()+". Couldn't locate plugin.yml");
		return loadClass(c.getString("main"));
	}
	
}
