package me.mrletsplay.mrcore.extensions;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.configuration.file.YamlConfiguration;

import me.mrletsplay.mrcore.bukkitimpl.MrCorePlugin;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

public class JARLoader extends ClassLoader implements Closeable {

	private static HashMap<String, byte[]> fakeClasses = new HashMap<>();
	public static long totalBytesLoaded = 0;
	
	public static void registerFakeClass(String className, File fake) {
		try {
			InputStream in = new FileInputStream(fake);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			byte[] objp = out.toByteArray();
			fakeClasses.put(className, objp);
		}catch(IOException e) {
			throw new FriendlyException("Failed to register fake class", e);
		}
	}
	
	private URL jarURL;
	private ClassLoader parent;
	private File file;
	private HashMap<String, byte[]> loadedClasses = new HashMap<>();
	
	public JARLoader(File file) throws IOException, URISyntaxException {
		this.jarURL = new URL("jar", "", file.toURI().toURL() + "!/");
		JarURLConnection jarConnection = (JarURLConnection) jarURL.openConnection();
		jarConnection.setDefaultUseCaches(false);
		JarFile jarFile = jarConnection.getJarFile();
		this.file = file;
		this.parent = JARLoader.class.getClassLoader();
		fakeClasses.forEach((name,data) -> defineClass(name, data, 0, data.length));
		loadClasses(jarFile);
		jarFile.close();
	}
	
	private void loadClasses(JarFile jarFile) throws IOException {
		Enumeration<JarEntry> ent = jarFile.entries();
		while(ent.hasMoreElements()) {
			JarEntry e = ent.nextElement();
			if(e.getName().endsWith(".class")) {
				ByteArrayOutputStream o = new ByteArrayOutputStream();
				InputStream in = jarFile.getInputStream(e);
				byte[] buf = new byte[4096];
				int len;
				while((len = in.read(buf)) > 0) {
					o.write(buf, 0, len);
				}
				String className = e.getName().replace('/', '.').substring(0, e.getName().length()-".class".length());
				definePackage(className);
				loadedClasses.put(className, o.toByteArray());
			}/*else {
				ByteArrayOutputStream o = new ByteArrayOutputStream();
				InputStream in = jarFile.getInputStream(e);
				byte[] buf = new byte[4096];
				int len;
				while((len = in.read(buf)) > 0) {
					o.write(buf, 0, len);
				}
				loadedResources.put(e.getName(), o.toByteArray());
			}*/
		}
		long bytesLoaded = loadedClasses.values().stream().mapToInt(i -> i.length).sum();
		totalBytesLoaded += bytesLoaded;
		MrCorePlugin.pl.getLogger().info(bytesLoaded/1024+" kb of classes loaded");
	}
	
	private void definePackage(String className) {
		String pName = className.substring(0, className.lastIndexOf('.'));
		if(getPackage(pName) != null) return;
		definePackage(pName, null, null, null, null, null, null, null);
	}
	
	@Override
	protected URL findResource(String name) {
		try {
			return new URL(jarURL+name);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Class<?> getJavaMainClass() throws IOException, ClassNotFoundException {
		JarURLConnection con = (JarURLConnection) jarURL.openConnection();
		con.setDefaultUseCaches(false);
		Attributes attr = con.getMainAttributes();
		if(attr == null) {
			con.getJarFile().close();
			return null;
		}
		String name = attr.getValue(Attributes.Name.MAIN_CLASS);
		if(name == null) {
			con.getJarFile().close();
			return null;
		}
		con.getJarFile().close();
		return loadClass(name);
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(loadedClasses.containsKey(name)) {
			byte[] classBuf = loadedClasses.get(name);
			return defineClass(name, classBuf, 0, classBuf.length);
		}else {
			throw new ClassNotFoundException("Couldn't find class: "+name);
		}
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
				return parent.loadClass(name); // If class is not found in the jar, return it from this classloader
			}catch(ClassNotFoundException e1) {
				if(!usePluginClasses) throw e1;
				return loadClassFromPlugins(name); // If class is not found in this classloader, try to load it from other plugins
			}
		}
	}
	
	public Class<?> loadClassFromPlugins(String name) {
		Class<?> clazz = null;
		for(MrCoreExtension ex : MrCorePluginLoader.getInstance().getAllPlugins()) {
			try {
				clazz = ex.getLoader().loadClassInternally(name, false);
			}catch(ClassNotFoundException ignored) {}
		}
		return clazz;
	}
	
	public Class<?> getBukkitMainClass() throws IOException, ClassNotFoundException {
		URL url = getResource("plugin.yml");
		InputStream in = url.openStream();
		if(in == null) return null;
		InputStreamReader r = new InputStreamReader(in);
		YamlConfiguration c = YamlConfiguration.loadConfiguration(r);
		r.close();
		if(c == null) throw new FriendlyException("Failed to load "+file.getName()+". Couldn't locate plugin.yml");
		return loadClass(c.getString("main"));
	}

	@Override
	public void close() throws IOException {
		
	}
	
//	@Override
//	public void close() throws IOException {
//		super.close();
//		jarFile.close();
//		try {
//			Class<?> clazz = java.net.URLClassLoader.class;
//			java.lang.reflect.Field ucp = clazz.getDeclaredField("ucp");
//			ucp.setAccessible(true);
//			Object sun_misc_URLClassPath = ucp.get(this);
//			java.lang.reflect.Field loaders = sun_misc_URLClassPath.getClass().getDeclaredField("loaders");
//			loaders.setAccessible(true);
//			Object java_util_Collection = loaders.get(sun_misc_URLClassPath);
//			for (Object sun_misc_URLClassPath_JarLoader : ((java.util.Collection<?>) java_util_Collection)) {
//				System.out.println(sun_misc_URLClassPath_JarLoader.getClass());
//				try {
//					java.lang.reflect.Field loader = sun_misc_URLClassPath_JarLoader.getClass().getDeclaredField("jar");
//					loader.setAccessible(true);
//					Object java_util_jar_JarFile = loader.get(sun_misc_URLClassPath_JarLoader);
//					System.out.println(((JarFile) java_util_jar_JarFile).getName());
//					System.out.println(((JarFile) java_util_jar_JarFile).getEntry("plugin.yml"));
//					((java.util.jar.JarFile) java_util_jar_JarFile).close();
//				} catch (Throwable t) {
//					// if we got this far, this is probably not a JAR loader so skip it
//					t.printStackTrace();
//				}
//			}
//		} catch (Throwable t) {
//			// probably not a SUN VM
//			t.printStackTrace();
//		}
//		return;
//	}
	
}
