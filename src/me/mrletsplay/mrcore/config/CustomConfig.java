package me.mrletsplay.mrcore.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.config.ConfigExpansions.ConfigCustomizer;

/**
 * - This is a config format based on two HashMaps, one for the properties and one for the comments<br>
 * - The file formatting is based on YAML, but is easier to read/edit for humans as it supports UTF-8 encoding, so you don't need to escape special characters<br>
 * - Every value is treated like a String or a List of Strings until further processing is requested using the get[type] methods<br>
 * - This config should be able to load most yaml-based configs without too much errors<br>
 * <br>
 * Feel free to use this in your projects however you want (You don't need to credit me)
 * @author MrLetsplay2003
 */
public class CustomConfig {
	
	public static final String
			DEFAULT_SPACE = "  ",
			DEFAULT_SPL_STRING = ": ",
			DEFAULT_ENTRY_STRING = "- ",
			DEFAULT_COMMENT_STRING = "# ",
			DEFAULT_HEADER_COMMENT_STRING = "## ";
	
	private String 
			space = DEFAULT_SPACE,
			splString = DEFAULT_SPL_STRING,
			entryString = DEFAULT_ENTRY_STRING,
			commentString = DEFAULT_COMMENT_STRING,
			headerCommentString = DEFAULT_HEADER_COMMENT_STRING;

	private File configFile;
	private URL configURL;
	private boolean isExternal, isCompact;
	private HashMap<String, Object> defaults;
	private ConfigSection parentSection;
	private List<ConfigSaveProperty> defaultSaveProps;
	private long lastEdited;

	/**
	 * See {@link #CustomConfig(File, boolean, ConfigSaveProperty...)}
	 */
	public CustomConfig(File configFile, boolean compact) {
		this(configFile, compact, new ConfigSaveProperty[0]);
	}
	
	
	/**
	 * Creates a CustomConfig instance with the given config file
	 * @param configFile The config file to be used
	 * @param compact Whether to use the compact format or not
	 * @param defaultSaveProperties The default {@link ConfigSaveProperty} options for this config
	 */
	public CustomConfig(File configFile, boolean compact, ConfigSaveProperty... defaultSaveProperties) {
		this.configFile = configFile;
		isExternal = false;
		isCompact = compact;
		defaultSaveProps = Arrays.asList(defaultSaveProperties);
		parentSection = new ConfigSection(this, null, null);
	}
	
	/**
	 * Returns the config file if not external, returns <b>null</b> otherwise<br>
	 * Check whether the config is external using {@link #isExternal() isExternal()}
	 * @return The config file, if set, null otherwise
	 */
	public File getConfigFile() {
		return configFile;
	}

	/**
	 * See {@link #CustomConfig(URL, boolean, ConfigSaveProperty...)}
	 */
	public CustomConfig(URL configURL, boolean compact) {
		this(configURL, compact, new ConfigSaveProperty[0]);
	}

	/**
	 * Creates a CustomConfig instance with the given config url<br>
	 * Note: External CustomConfig instances can <b>not</b> be saved
	 * @param configURL The config url to be used
	 * @param compact Whether to use the compact format or not
	 * @param defaultSaveProperties The default {@link ConfigSaveProperty} options for this config
	 */
	public CustomConfig(URL configURL, boolean compact, ConfigSaveProperty... defaultSaveProperties) {
		this.configURL = configURL;
		isExternal = true;
		isCompact = compact;
		defaultSaveProps = Arrays.asList(defaultSaveProperties);
		parentSection = new ConfigSection(this, null, null);
	}
	
	/**
	 * Sets the default save options
	 * @param defaultSaveProps The default {@link ConfigSaveProperty ConfigSaveProperty} options to use
	 */
	public void setDefaultSaveProperties(ConfigSaveProperty... defaultSaveProps) {
		this.defaultSaveProps = Arrays.asList(defaultSaveProps);
	}

	/**
	 * Returns the config url if external, returns <b>null</b> otherwise<br>
	 * Check whether the config is external using {@link CustomConfig#isExternal() isExternal()}
	 * @return The config url, if set, null otherwise
	 */
	public URL getConfigURL() {
		return configURL;
	}
	
	/**
	 * Checks whether the config is external (loaded from a url) or not
	 * @return True if external, false otherwise
	 */
	public boolean isExternal() {
		return isExternal;
	}
	
	/**
	 * Adds a {@link ConfigCustomizer} to this config instance.<br>
	 * This will determine the layout of a (non-compact) saved config and can also load configs with the given layout
	 * @param custom The customizer to use
	 * @return This config instance
	 */
	public CustomConfig useCustomizer(ConfigCustomizer customizer) {
		entryString = customizer.getEntryPrefix();
		commentString = customizer.getCommentPrefix();
		headerCommentString = customizer.getHeaderCommentPrefix();
		space = customizer.getIndentation();
		splString = customizer.getPropertySplitter();
		return this;
	}

	/**
	 * Saves the config with the given save properties<br>
	 * This method ignores the default save properties if a non-null value is given
	 * @param saveProperties The {@link me.mrletsplay.mrcore.CustomConfigv1.CustomConfig.ConfigSaveProperty ConfigSaveProperty} options to be used when saving the config
	 * @throws IOException If an IO error occurs while saving the config
	 */
	public void saveConfig(List<ConfigSaveProperty> saveProperties) throws IOException {
		if(isExternal) return;
		configFile.getParentFile().mkdirs();
		saveConfig(new FileOutputStream(configFile), saveProperties);
	}
	
	/**
	 * See {@link #saveConfig(List)}
	 */
	public void saveConfig() throws IOException {
		saveConfig((List<ConfigSaveProperty>)null);
	}
	
	/**
	 * Saves the config with the given save properties<br>
	 * This method ignores the default save properties if a non-null value is given<br>
	 * <br>
	 * Note: This method does <b>not</b> hide errors. Any errors that occur will be printed to the console
	 * @param saveProperties The {@link ConfigSaveProperty} options to be used when saving the config
	 */
	public void saveConfigSafely(List<ConfigSaveProperty> saveProperties) {
		try {
			saveConfig(saveProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * See {@link #saveConfigSafely(List)}
	 */
	public void saveConfigSafely() {
		saveConfigSafely(null);
	}
	
	/**
	 * See {@link #saveConfig(OutputStream, List)}
	 */
	public void saveConfig(OutputStream fOut) throws IOException {
		saveConfig(fOut, null);
	}
	
	/**
	 * Saves the config to the given {@link OutputStream}<br>
	 * This method ignores the default save properties if a non-null value is given
	 * @param fOut The OutputStream to be used
	 * @param saveProperties The save properties to be used
	 * @throws IOException If an IO error occurs while saving the config
	 */
	public void saveConfig(OutputStream fOut, List<ConfigSaveProperty> saveProperties) throws IOException{
		List<ConfigSaveProperty> props = saveProperties!=null?saveProperties:defaultSaveProps;
		if(!isCompact) {
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(fOut, StandardCharsets.UTF_8));
			w.write(parentSection.saveToString(props, 0));
			w.close();
		}else {
			//Compact format
			DataOutputStream out = new DataOutputStream(fOut);
			HashMap<String, Short> cKs = new HashMap<>();
			
			short i = 0;
			for(String key : getProperties().keySet()) {
				String[] spl = key.split("\\.");
				for(String part : spl) {
					if(!cKs.containsKey(part)) {
						cKs.put(part, i++);
					}
				}
			}
			
			//Keys
			for(Map.Entry<String, Short> en : cKs.entrySet()) {
				out.writeShort(en.getValue());
				out.writeUTF(en.getKey());
			}
			out.writeShort(-1);
			
			//Properties
			for(Map.Entry<String, Property> en : getProperties().entrySet()) {
				String key = en.getKey();
				Property prop = en.getValue();
				String[] splK = key.split("\\.");
				out.writeByte(splK.length);
				for(String part : splK) {
					out.writeShort(cKs.get(part));
				}
				if(!prop.getType().IS_LIST) {
					out.writeByte(0);
					out.writeUTF(prop.getValue().toString());
				}else {
					out.writeByte(1);
					List<?> lst = (List<?>)prop.getValue();
					out.writeShort(lst.size());
					for(Object o : lst) {
						out.writeUTF(o.toString());
					}
				}
			}
			out.writeByte(-1);
			
			//Comments
			for(Map.Entry<String, String> en : getComments().entrySet()) {
				String key = en.getKey();
				String comment = en.getValue();
				String[] splK = key.split("\\.");
				out.writeByte(splK.length);
				for(String part : splK) {
					out.writeShort(cKs.get(part));
				}
				out.writeUTF(comment);
			}
			out.writeByte(-1);
			out.close();
		}
		if(configFile!=null) lastEdited = configFile.lastModified();
	}
	
	private String space(int length) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) sb.append(space);
		return sb.toString();
	}

	/**
	 * Loads the config from the set config file/url
	 * @return The same CustomConfig instance
	 * @throws IOException If an IO error occurs while loading the config
	 * @throws InvalidConfigException If the config to be loaded is in an invalid format
	 */
	public CustomConfig loadConfig() throws IOException{
		if(!isExternal && !configFile.exists()){
			configFile.getParentFile().mkdirs();
			configFile.createNewFile();
		}
		loadConfig(isExternal?configURL.openStream():new FileInputStream(configFile));
		return this;
	}
	
	/**
	 * Loads the config from the set config file/url<br>
	 * <br>
	 * Note: This method does <b>not</b> hide errors. Any errors that occur will be printed to the console
	 * @return The same CustomConfig instance
	 * @throws InvalidConfigException If the config to be loaded is in an invalid format
	 */
	public CustomConfig loadConfigSafely() {
		try {
			return loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads the CustomConfig from the given {@link InputStream}
	 * @param in The InputStream to be used
	 * @return The same CustomConfig instance
	 * @throws IOException If an IO error occurs while loading the config
	 * @throws InvalidConfigException If the config to be loaded is in an invalid format
	 */
	public CustomConfig loadConfig(InputStream in) throws IOException {
		parentSection = new ConfigSection(this, null, null);
		if(!isExternal && configFile != null) lastEdited = configFile.lastModified();
		if(isCompact) return loadConfig_Compact(in);
		BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		List<String> stages = new ArrayList<>();
		int lastStage = 0;
		String line;
		Property lastP = null;
		String tmpKey = null;
		List<Object> tmpList = null;
		List<String> tmpComments = null,
					 tmpHeader = null;
		int l = 0;
		while((line=r.readLine())!=null) {
			l++;
			if(line.isEmpty() || line.trim().isEmpty()) continue;
			int stage = getFileStage(line);
			if(stage==-1) {
				r.close();
				throw new InvalidConfigException("Invalid stage", l);
			}
			List<String> tmpStages = new ArrayList<>(stages);
			Property p = loadProperty(l,line);
			if(p == null) {
				continue;
			}
			if(stage!=lastStage) {
				if(p.getType().equals(PropertyType.LIST_ENTRY)) {
					if(!((lastP.getType().equals(PropertyType.LIST_START) && stage-lastStage == 1) || (lastP.getType().equals(PropertyType.LIST_ENTRY) && stage == lastStage))) {
						r.close();
						throw new InvalidConfigException("Stage changed on LIST_ENTRY", l);
					}
				}else if(lastP==null) {
					r.close();
					throw new InvalidConfigException("Stage > 0 in first line", l);
				}else if(tmpComments!=null && p.getType().equals(PropertyType.COMMENT)){
					r.close();
					throw new InvalidConfigException("Stage changed on COMMENT: "+stage+", previously "+lastStage, l);
				}else {
					addOrRemove(stages, lastP.getKey().split("\\."), stage - lastStage + (lastP.getType().equals(PropertyType.LIST_ENTRY)?1:0), l);
				}
			}
			if(!p.getType().equals(PropertyType.COMMENT) && tmpComments!=null) {
				if (p.getKey() != null) {
					setLoadedComment(lastStage, tmpStages, p.getKey(), tmpComments.stream().collect(Collectors.joining("\n")));
					tmpComments = null;
				} else {
					r.close();
					throw new InvalidConfigException("No key for comment", l);
				}
			}
			if(!p.getType().equals(PropertyType.HEADER_COMMENT) && tmpHeader!=null) {
				if(!parentSection.containsCommentKey(null)){
					parentSection.setComment(null, tmpHeader.stream().collect(Collectors.joining("\n")));
					tmpHeader = null;
				}
			}
			if(p.getType().equals(PropertyType.COMMENT)){
				if(tmpComments==null) {
					tmpComments = new ArrayList<>();
				}
				tmpComments.add((String)p.getValue());
			}else if(p.getType().equals(PropertyType.HEADER_COMMENT)){
				if(tmpHeader==null) {
					tmpHeader = new ArrayList<>();
				}
				tmpHeader.add((String)p.getValue());
			}else if(!p.getType().IS_LIST){
				if(tmpList!=null){
					if(!tmpList.isEmpty()) setLoadedProperty(lastStage-1, tmpStages, new Property(tmpKey, PropertyType.LIST, tmpList));
					tmpList = null;
					tmpKey = null;
				}
				setLoadedProperty(stage, stages, p);
				lastP = p;
			}else if(p.getType().equals(PropertyType.LIST_START)){
				if(tmpList!=null){
					if(!tmpList.isEmpty()) setLoadedProperty(lastStage-1, tmpStages, new Property(tmpKey, PropertyType.LIST, tmpList));
				}
				tmpList = new ArrayList<>();
				tmpKey = p.getKey();
				lastP = p;
			}else if(p.getType().equals(PropertyType.LIST_ENTRY)){
				if(tmpList == null || tmpKey == null){
					r.close();
					throw new IllegalArgumentException("Tried to add list value without list start (Line "+l+")");
				}
				tmpList.add(p.getValue());
				lastP = new Property(lastP.getKey(), p.getType(), p.getValue());
			}
			lastStage = stage;
		}
		if(tmpList!=null && !tmpList.isEmpty()){
			setLoadedProperty(lastStage-1, stages, new Property(tmpKey, PropertyType.LIST, tmpList));
		}
		r.close();
		return this;
	}
	
	private CustomConfig loadConfig_Compact(InputStream oIn) throws IOException {
		DataInputStream in = new DataInputStream(oIn);
		try {
			HashMap<Short, String> cKs = new HashMap<>();
			
			if(in.available()==0) {
				in.close();
				return this;
			}
			
			//Keys
			short id;
			while((id = in.readShort())>=0) {
				cKs.put(id, in.readUTF());
			}
			
			//Properties
			short len;
			while((len = in.readByte())>=0) {
				List<Short> parts = new ArrayList<>();
				for(int i = 0; i < len; i++) {
					parts.add(in.readShort());
				}
				
				String key = parts.stream().map(kp -> cKs.get(kp)).collect(Collectors.joining("."));
				boolean isList = in.readByte() == 1;
				if(!isList) {
					String prop = in.readUTF();
					parentSection.set(key, new Property(key, PropertyType.VALUE, prop));
				}else {
					List<String> lst = new ArrayList<>();
					short sz = in.readShort();
					for(int i = 0; i < sz; i++) {
						lst.add(in.readUTF());
					}
					parentSection.set(key, new Property(key, PropertyType.LIST, lst));
				}
			}
			
			while((len = in.readByte())>=0) {
				List<Short> parts = new ArrayList<>();
				for(int i = 0; i < len; i++) {
					parts.add(in.readShort());
				}
				
				String key = parts.stream().map(kp -> cKs.get(kp)).collect(Collectors.joining("."));
				parentSection.setComment(key, in.readUTF());
			}
			in.close();
		}catch(EOFException e) {
			in.close();
			throw new InvalidConfigException("Invalid compact config", -1);
		}
		return this;
	}

	/**
	 * Loads the given CustomConfig as default (If given properties don't exist, they will be created)
	 * @param cfg The CustomConfig to load the defaults from
	 * @param override Whether currently existing properties should be overridden
	 * @return The same CustomConfig instance
	 * @throws IOException If an IO error occurs while loading the config
	 * @throws InvalidConfigException If this config needs to be loaded and is in an invalid format
	 */
	public CustomConfig loadDefault(CustomConfig cfg, boolean override) throws IOException{
		HashMap<String, Property> properties = getProperties();
		HashMap<String, String> comments = getComments();
		HashMap<String, Property> properties2 = cfg.getProperties();
		HashMap<String, String> comments2 = cfg.getComments();
		comments2.keySet().stream().filter(k -> override || !comments.containsKey(k)).forEach(k -> comments.put(k,comments2.get(k)));
		properties2.keySet().stream().filter(k -> override || !properties.containsKey(k)).forEach(k -> properties.put(k,properties2.get(k)));
		return this;
	}

	/**
	 * Loads the config defaults (see {@link #loadDefault(CustomConfig, boolean)}) from a given class path. This will use this classes {@link ClassLoader} to find the given path
	 * @param path The class path
	 * @param override Whether currently existing properties should be overriden
	 * @param clazz The class to use the classloader from
	 * @return The same CustomConfig instance
	 * @throws IOException If an IO error occurs while loading the config
	 * @throws InvalidConfigException If this config needs to be loaded and is in an invalid format or the config from the class path is in an invalid format
	 */
	public CustomConfig loadDefaultFromClassPath(String path, boolean override, Class<?> clazz) throws IOException{
		CustomConfig cfg = new CustomConfig((File) null, false).loadConfig(clazz.getClassLoader().getResourceAsStream(path));
		loadDefault(cfg, override);
		return this;
	}

	/**
	 * See {@link #loadDefaultFromClassPath(String, boolean)}
	 * @param path The class path
	 * @param override Whether currently existing properties should be overridden
	 * @param clazz The class to use the classloader from
	 * @return The same CustomConfig instance
	 */
	public CustomConfig loadDefaultFromClassPathSafely(String path, boolean override, Class<?> clazz){
		try {
			loadDefaultFromClassPath(path, override, clazz);
		} catch (IOException | InvalidConfigException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Returns this config's property HashMap<br>
	 * Any changes to this HashMap will also affect this config
	 * @return This config's property HashMap
	 */
	public HashMap<String, Property> getProperties(){
		return parentSection.getProperties(true);
	}

	/**
	 * Returns this config's comment HashMap<br>
	 * Any changes to this HashMap will also affect this config
	 * @return This config's comment HashMap
	 */
	public HashMap<String, String> getComments(){
		return parentSection.getComments(true);
	}

	private void setLoadedProperty(int currStage, List<String> stages, Property p){
		if(currStage>0) {
			parentSection.set(getKey(stages, currStage)+"."+p.getKey(), p);
		}else {
			parentSection.set(p.getKey(), p);
		}
	}

	private void setLoadedComment(int currStage, List<String> stages, String key, String comment){
		if(currStage>0) {
			parentSection.setComment(getKey(stages, currStage)+"."+key, comment);
		}else {
			parentSection.setComment(key, comment);
		}
	}

	private Property loadProperty(int line, String s) throws InvalidConfigException{
		String formattedLine = s.replaceAll("^\\s+","").replaceAll("^\\t+","");
		if(formattedLine.startsWith(commentString)){
			return new Property(null, PropertyType.COMMENT, formattedLine.substring(commentString.length()));
		}
		if(formattedLine.startsWith(headerCommentString)) {
			return new Property(null, PropertyType.HEADER_COMMENT, formattedLine.substring(headerCommentString.length()));
		}
		if(formattedLine.startsWith(entryString)) {
			return new Property(null, PropertyType.LIST_ENTRY, formattedLine.substring(entryString.length()));
		}
		if(formattedLine.equals(entryString.trim())) {
			return new Property(null, PropertyType.LIST_ENTRY, "");
		}
		String[] p = formattedLine.split(splString, 2);
		if(p.length==2){
			if(p[1].isEmpty()){
				return new Property(p[0], PropertyType.LIST_START,true);
			}else{
				return new Property(p[0], PropertyType.VALUE, p[1]);
			}
		}else if(p.length==1){
			if(p[0].endsWith(splString.trim())){
				String k = p[0].substring(0, p[0].length()-splString.trim().length());
				return new Property(k, PropertyType.LIST_START, true);
			}
		}
		throw new InvalidConfigException("Invalid property \""+formattedLine+"\"",line);
	}

	private void addOrRemove(List<String> s, String[] spl, int c, int l) {
		if(c>0) {
			if(c > spl.length) throw new InvalidConfigException("Invalid stage change", l);
			s.addAll(Arrays.asList(spl).subList(0, c));
		}else {
			if(Math.abs(c) > s.size()) throw new InvalidConfigException("Invalid stage change", l);
			for(int i = 0; i < Math.abs(c); i++) {
				s.remove(s.size()-1);
			}
		}
	}

	private String getKey(List<String> stages, int currStage) {
		return stages.stream().limit(currStage).collect(Collectors.joining("."));
	}

	private int getFileStage(String s) {
		int indents = 0;
		while(s.startsWith(space)) {
			s = s.substring(space.length());
			indents++;
		}
		return indents;
	}
	
	/**
	 * See {@link #set(String, Object, boolean, List)}
	 * @param key The key
	 * @param val The value
	 */
	public void set(String key, Object val) {
		set(key, val, false, null);
	}
	
	/**
	 * See {@link #set(String, Object, boolean, List)}
	 * @param key
	 * @param val
	 * @param save
	 */
	public void set(String key, Object val, boolean save) {
		set(key, val, save, null);
	}

	/**
	 * Sets a key to a value
	 * @param key The key
	 * @param val The value
	 * @param save Whether this config should be saved
	 * @param props The saveProperties to be used when saving the config (See {@link #saveConfig(List)})
	 */
	@SuppressWarnings("unchecked")
	public void set(String key, Object val, boolean save, List<ConfigSaveProperty> props) {
		PropertyType type;
		if(val instanceof List<?>) {
			type = PropertyType.LIST;
			List<?> l = (List<?>)val;
			if(l.size()>0) {
				if(l.get(0) instanceof String) {
					List<String> s = (List<String>)val;
					for(int i = 0; i < s.size(); i++) {
						s.set(i, verifyString(s.get(i)));
					}
					val = s;
				}
			}
		}else{
			type = PropertyType.VALUE;
			if(val instanceof String) {
				val = verifyString((String)val);
			}
		}
		parentSection.set(key, new Property(key, type, val));
		if(save) try {
			saveConfig(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * See {@link #unset(String, boolean, boolean, List)}
	 * @param key
	 */
	public void unset(String key) {
		unset(key, false);
	}

	/**
	 * See {@link #unset(String, boolean, boolean, List)}
	 * @param key
	 */
	public void unset(String key, boolean deep) {
		unset(key, deep, false);
	}
	
	/**
	 * See {@link #unset(String, boolean, boolean, List)}
	 * @param key
	 * @param deep
	 * @param save
	 */
	public void unset(String key, boolean deep, boolean save) {
		unset(key, deep, save, null);
	}

	/**
	 * Unsets a specific key/set of keys
	 * @param key The key to be unset
	 * @param deep If true, all subproperties (and their subproperties etc.) will also be unset
	 * @param save Whether or not this config should be saved
	 * @param props The save properties to be used when saving the config (See {@link #saveConfig(List)})
	 */
	public void unset(String key, boolean deep, boolean save, List<ConfigSaveProperty> props) {
		parentSection.unset(key, deep);
		if(save) {
			try {
				saveConfig(props);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String verifyString(String s) {
		return s.replace("\n", "\\n").replace("\r", "\\r");
	}
	
	/**
	 * Adds a default value to the config<br>
	 * <br>
	 * These values need to be applied using {@link #applyDefaults(boolean, boolean, List)}
	 * @param key The key
	 * @param defaultVal The default value
	 */
	public void addDefault(String key, Object defaultVal) {
//		addDefault(key, defaultVal, false);
		defaults.put(key, defaultVal);
	}
	
//	public void addDefault(String key, Object defaultVal, boolean save) {
//		addDefault(key, defaultVal, save, null);
//	}
	
//	public void addDefault(String key, Object defaultVal, boolean save, List<ConfigSaveProperty> props) {
//		if(!properties.containsKey(key)) {
//			set(key, defaultVal, save, props);
//		}
//	}
//	
//	public void addDefault(String forKey, String key, Object defaultVal) {
//		addDefault(forKey, key, defaultVal, false);
//	}
//	
//	public void addDefault(String forKey, String key, Object defaultVal, boolean save) {
//		addDefault(forKey, key, defaultVal, save, null);
//	}
//	
//	public void addDefault(String forKey, String key, Object defaultVal, boolean save, List<ConfigSaveProperty> props) {
//		if(getKeys(forKey, false, true).isEmpty()) {
//			set(key, defaultVal, save, props);
//		}
//	}

	/**
	 * See {@link #applyDefaults(boolean, boolean, List)}
	 */
	public void applyDefaults(boolean save) {
		applyDefaults(save, false);
	}
	
	/**
	 * See {@link #applyDefaults(boolean, boolean, List)}
	 */
	public void applyDefaults(boolean save, boolean alwaysAdd) {
		applyDefaults(save, alwaysAdd, null);
	}

	/**
	 * Applies all default values set using {@link #addDefault(String, Object)}
	 * @param save Whether this config should be saved or not
	 * @param alwaysAdd Whether currently existing properties should be overridden
	 * @param props The save properties to use when saving the config (See {@link #saveConfig(List)})
	 */
	public void applyDefaults(boolean save, boolean alwaysAdd, List<ConfigSaveProperty> props) {
		if(!parentSection.isEmpty() && !alwaysAdd) return;
		for(Map.Entry<String, Object> en : defaults.entrySet()) {
			if(!parentSection.containsKey(en.getKey())) {
				set(en.getKey(), en.getValue());
			}
		}
		if(save) {
			try {
				saveConfig(props);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * See {@link #setComment(String, String, boolean, List)}
	 */
	public void setComment(String key, String comment) {
		setComment(key, comment, false);
	}

	/**
	 * See {@link #setComment(String, String, boolean, List)}
	 */
	public void setComment(String key, String comment, boolean save) {
		setComment(key, comment, save, null);
	}

	/**
	 * Sets a comment for a specific key<br>
	 * If a comment is set for a key that doesn't exist and the config is saved, the comment will be ignored
	 * @param key The key
	 * @param comment The comment for that key
	 * @param save Whether this config should be saved
	 * @param props The save properties to be used when saving the config (See {@link #saveConfig(List)})
	 */
	public void setComment(String key, String comment, boolean save, List<ConfigSaveProperty> props) {
		parentSection.setComment(key, comment);
		if(save) try {
			saveConfig(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * See {@link #setHeader(String, boolean, List)}
	 */
	public void setHeader(String header) {
		setHeader(header, false);
	}

	/**
	 * See {@link #setHeader(String, boolean, List)}
	 */
	public void setHeader(String header, boolean save) {
		setHeader(header, save, null);
	}

	/**
	 * Sets the header comment (The comment above the config)
	 * @param header The header comment
	 * @param save Whether the config should be saved
	 * @param props The save properties to be used when saving the config (See {@link #saveConfig(List)})
	 */
	public void setHeader(String header, boolean save, List<ConfigSaveProperty> props) {
		parentSection.setComment(null, header);
		if(save) try {
			saveConfig(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public String getString(String key) {
		return getString(key, null, false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public String getString(String key, String defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null) {
			return ((String)p.getValue()).replace("\\n", "\n").replace("\\r", "\r");
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<String> getStringList(String key) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			return castAllString((List<?>) p.getValue());
		}
		return new ArrayList<>();
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<String> getStringList(String key, List<String> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			return castAllString((List<?>) p.getValue());
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Integer getInt(String key) {
		Property p = parentSection.get(key);
		if(p!=null){
			return Integer.parseInt(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Integer getInt(String key, Integer defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null) {
			return Integer.parseInt(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Integer> getIntegerList(String key) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Integer> getIntegerList(String key, List<Integer> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Double getDouble(String key) {
		Property p = parentSection.get(key);
		if(p!=null){
			return Double.valueOf(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Double getDouble(String key, Double defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null) {
			return Double.valueOf(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Double> getDoubleList(String key) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllDouble((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Double> getDoubleList(String key, List<Double> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllDouble((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public boolean getBoolean(String key) {
		Property p = parentSection.get(key);
		if(p!=null){
			return Boolean.valueOf(String.valueOf(p.getValue()));
		}else {
			return false;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public boolean getBoolean(String key, boolean defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null) {
			return Boolean.valueOf(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Boolean> getBooleanList(String key) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBoolean((List<?>) p.getValue());
			} catch (IllegalArgumentException ignored){}
		}
		return new ArrayList<>();
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Boolean> getBooleanList(String key, List<Boolean> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBoolean((List<?>) p.getValue());
			} catch (IllegalArgumentException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Object get(String key) {
		Property p = parentSection.get(key);
		if(p==null) return null;
		return p.getValue();
	}

	/**
	 * Get an object by a specific key<br>
	 * The <b>get[type]</b> functions return their type respectively<br>
	 * If the type is invalid, these functions will throw a {@link ClassCastException} or a {@link NumberFormatException}<br>
	 * <br>
	 * this specific function will always either return a {@link List}<{@link String}> or a {@link String}
	 * @param key The key to the property
	 * @param defaultVal If there is no value stored in the CustomConfig, this value will be returned
	 * @param applyDefault Whether the default value should be added to the config, if it's not set
	 * @return The property value
	 */
	public Object get(String key, Object defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null){
			return p.getValue();
		}else{
			if(applyDefault){
				set(key, defaultVal, false);
			}
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Long getLong(String key) {
		Property p = parentSection.get(key);
		if(p!=null){
			return Long.parseLong(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Long getLong(String key, Long defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null) {
			return Long.parseLong(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Long> getLongList(String key) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllLong((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Long> getLongList(String key, List<Long> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllLong((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigInteger getBigInteger(String key) {
		Property p = parentSection.get(key);
		if(p!=null){
			return new BigInteger(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigInteger getBigInteger(String key, BigInteger defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null) {
			return new BigInteger(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigInteger> getBigIntegerList(String key) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBigInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigInteger> getBigIntegerList(String key, List<BigInteger> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBigInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigDecimal getBigDecimal(String key) {
		Property p = parentSection.get(key);
		if(p!=null){
			return new BigDecimal(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigDecimal getBigDecimal(String key, BigDecimal defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null) {
			return new BigDecimal(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigDecimal> getBigDecimalList(String key) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBigDecimal((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigDecimal> getBigDecimalList(String key, List<BigDecimal> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBigDecimal((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	private List<String> castAllString(List<?> in){
		List<String> out = new ArrayList<>();
		for(Object o : in){
			out.add(String.valueOf(o));
		}
		return out;
	}

	private List<Integer> castAllInteger(List<?> in) throws NumberFormatException{
		List<Integer> out = new ArrayList<>();
		for(Object o : in){
			out.add(Integer.parseInt(String.valueOf(o)));
		}
		return out;
	}

	private List<Long> castAllLong(List<?> in) throws NumberFormatException{
		List<Long> out = new ArrayList<>();
		for(Object o : in){
			out.add(Long.parseLong(String.valueOf(o)));
		}
		return out;
	}

	private List<Double> castAllDouble(List<?> in) throws NumberFormatException{
		List<Double> out = new ArrayList<>();
		for(Object o : in){
			out.add(Double.parseDouble(String.valueOf(o)));
		}
		return out;
	}

	private List<Boolean> castAllBoolean(List<?> in) throws IllegalArgumentException{
		List<Boolean> out = new ArrayList<>();
		for(Object o : in){
			String v = String.valueOf(o);
			if(v.equalsIgnoreCase("true")){
				out.add(true);
			}else if(v.equalsIgnoreCase("false")){
				out.add(false);
			}else{
				throw new IllegalArgumentException("Invalid boolean list");
			}
		}
		return out;
	}
	
	private List<BigInteger> castAllBigInteger(List<?> in) throws NumberFormatException{
		List<BigInteger> out = new ArrayList<>();
		for(Object o : in){
			out.add(new BigInteger(String.valueOf(o)));
		}
		return out;
	}
	
	private List<BigDecimal> castAllBigDecimal(List<?> in) throws NumberFormatException{
		List<BigDecimal> out = new ArrayList<>();
		for(Object o : in){
			out.add(new BigDecimal(String.valueOf(o)));
		}
		return out;
	}

	/**
	 * Returns the list of keys for the subproperties for the given key
	 * @param key The key
	 * @param deep Whether the subproperties of subproperties (and theirs as well etc.) should also be included
	 * @param fullKeys Whether the given key should be removed from the front
	 * @return A list of property keys
	 */
	public List<String> getKeys(String key, boolean deep, boolean fullKeys){
		return parentSection.getKeys(key, deep, fullKeys);
	}

	/**
	 * See {@link #getKeys(String, boolean, boolean)}
	 */
	public List<String> getKeys(boolean deep){
		return parentSection.getKeys(deep, true);
	}
	
	/**
	 * See {@link #reloadConfig(boolean, List)}
	 */
	public void reloadConfig(boolean save) throws IOException {
		reloadConfig(save, null);
	}

	/**
	 * Reloads the config<br>
	 * See {@link #saveConfig(List)} and {@link #loadConfig()}
	 * @param save Whether the config should be saved
	 */
	public void reloadConfig(boolean save, List<ConfigSaveProperty> props) throws IOException{
		clearConfig(save, props);
		loadConfig();
	}
	
	/**
	 * See {@link #clearConfig(boolean, List)}
	 */
	public void clearConfig(boolean save) throws IOException {
		clearConfig(save, null);
	}

	/**
	 * Clears all properties from the config
	 * @param save Whether the config should be saved first (See {@link #saveConfig(List)}
	 */
	public void clearConfig(boolean save, List<ConfigSaveProperty> props) throws IOException {
		if(save){
			saveConfig(props);
		}
		parentSection.clear();
	}

	/**
	 * Deletes the config file (If the config is not external, as stated by {@link #isExternal()})
	 */
	public void deleteConfig(){
		if(isExternal)return;
		configFile.delete();
	}
	
	/**
	 * @return whether the config file has been modified since it was last saved/loaded
	 * @throws UnsupportedOperationException if the config is external
	 */
	public boolean hasBeenModified() throws UnsupportedOperationException {
		if(isExternal) throw new UnsupportedOperationException("Can't use hasBeenModified() on an external config");
		return lastEdited != configFile.lastModified();
	}
	
	public boolean hasProperty(ConfigSaveProperty prop) {
		return defaultSaveProps.contains(prop);
	}

	/**
	 * The config property class used for internal handling of config properties
	 * @author MrLetsplay2003
	 */
	public static class Property {

		private String key;
		private Object value;
		private PropertyType type;

		public Property(String key, PropertyType type, Object value){
			this.value = value;
			this.type = type;
			this.key = key;
		}

		public PropertyType getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}

		public String getKey() {
			return key;
		}
		
		@Override
		public String toString() {
			return "["+key+" => "+value+"]";
		}
	}
	
	/**
	 * Returns the parent section containing all the properties of this CustomConfig instance.<br>
	 * Beware: All the methods inside this class are to be used with caution, nothing is documented yet
	 * @return The parent section
	 */
	public ConfigSection getParentSection() {
		return parentSection;
	}

	/**
	 * The config property type enum used for internal handling of config properties
	 * @author MrLetsplay2003
	 */
	public static enum PropertyType {

		COMMENT(false),
		HEADER_COMMENT(false),
		LIST_START(true), LIST_ENTRY(true),
		VALUE(false),
		LIST(true);

		public final boolean IS_LIST;

		PropertyType(boolean isList) {
			IS_LIST = isList;
		}

	}

	/**
	 * All config save properties that can be used. These provide additional features or change the behaviour when saving the config
	 * @author MrLetsplay2003
	 */
	public static enum ConfigSaveProperty{
		
		/**
		 * When saving the config, all config properties will be sorted alphabetically<br>
		 * This is only available to non-external configs (as stated by {@link CustomConfig#isExternal()})
		 */
		SORT_ALPHABETICALLY,
		
		/**
		 * When saving the config, null values will be kept in the config<br>
		 * <br>
		 * <b>Note:<br>
		 * This is currently not working correctly. This feature will be reimplemented soon</b>
		 */
		INCLUDE_NULL,
		
		/**
		 * When saving the config, properties with comments will have one empty line added before and after them
		 */
		SPACE_COMMENTED_PROPERTIES,
		
		/**
		 * When loading/saving the config, the properties will stay in the order as they're set. <br>
		 * <b>This option can only be used in the constructor</b>
		 */
		KEEP_CONFIG_SORTING,
	}

	/**
	 * An exception thrown when the given config is not in the correct format
	 * @author MrLetsplay2003
	 */
	public static class InvalidConfigException extends RuntimeException{
		
		private static final long serialVersionUID = 1L;

		InvalidConfigException(String reason, int line){
			super("Failed to load config: "+reason+(line!=-1?" (Line "+line+")":""));
		}

	}
	
	public static class ConfigSection {
		
		private CustomConfig config;
		private ConfigSection parent;
		private String name;
		private HashMap<String, ConfigSection> subsections;
		private HashMap<String, Property> properties;
		private HashMap<String, String> comments;
		
		public ConfigSection(CustomConfig config, ConfigSection parent, String name) {
			this.config = config;
			this.parent = parent;
			this.name = name;
			boolean keepOrder = config.hasProperty(ConfigSaveProperty.KEEP_CONFIG_SORTING);
			subsections = keepOrder ? new LinkedHashMap<>() : new HashMap<>();
			properties = keepOrder ? new LinkedHashMap<>() : new HashMap<>();
			comments = new HashMap<>();
		}
		
		public ConfigSection getSubsection(String key) {
			String[] spl = key.split("\\.");
			if(spl.length > 1) {
				ConfigSection sub = subsections.get(spl[0]);
				if(sub == null) return null;
				return sub.getSubsection(key.substring(spl[0].length() + 1));
			}else {
				return subsections.get(key);
			}
		}
		
		public ConfigSection getOrCreateSubsection(String key) {
			String[] spl = key.split("\\.");
			if(spl.length > 1) {
				ConfigSection sub = subsections.get(spl[0]);
				if(sub == null) return null;
				return sub.getSubsection(key.substring(spl[0].length() + 1));
			}else {
				if(subsections.containsKey(key)) return subsections.get(key);
				ConfigSection sub = new ConfigSection(config, this, key);
				subsections.put(key, sub);
				return sub;
			}
		}
		
		public void set(String key, Property property) {
			String[] spl = key.split("\\.");
			if(spl.length > 1) {
				ConfigSection sub = getOrCreateSubsection(spl[0]);
				sub.set(key.substring(spl[0].length() + 1), property);
			}else {
				if(property == null) {
					properties.remove(key);
				}else {
					properties.put(key, property);
				}
			}
		}
		
		public Property get(String key) {
			String[] spl = key.split("\\.");
			if(spl.length > 1) {
				ConfigSection sub = getSubsection(spl[0]);
				if(sub == null) return null;
				return sub.get(key.substring(spl[0].length() + 1));
			}else {
				return properties.get(key);
			}
		}
		
		public void setComment(String key, String comment) {
			if(key == null) comments.put(null, comment);
			String[] spl = key.split("\\.");
			if(spl.length > 1) {
				ConfigSection sub = getOrCreateSubsection(spl[0]);
				sub.setComment(key.substring(spl[0].length() + 1), comment);
			}else {
				if(comment == null) {
					comments.remove(key);
				}else {
					comments.put(key, comment);
				}
			}
		}
		
		public String getComment(String key) {
			if(key == null) return comments.get(null);
			String[] spl = key.split("\\.");
			if(spl.length > 1) {
				ConfigSection sub = getSubsection(spl[0]);
				if(sub == null) return null;
				return sub.getComment(key.substring(spl[0].length() + 1));
			}else {
				return comments.get(key);
			}
		}
		
		public boolean containsKey(String key) {
			return properties.containsKey(key) || subsections.values().stream().anyMatch(s -> s.containsKey(key));
		}
		
		public List<String> getKeys(String key, boolean deep, boolean fullKeys){
			ConfigSection section = getSubsection(key);
			if(section == null) return new ArrayList<>();
			return section.getKeys(deep, fullKeys);
		}
		
		public List<String> getKeys(boolean deep, boolean fullKeys) {
			List<String> keys = new ArrayList<>(properties.keySet());
			if(deep) {
				subsections.values().forEach(s -> s.getKeys(deep, fullKeys).forEach(k -> keys.add(fullKeys ? name + k : k)));
			}
			return keys;
		}
		
		public void unset(String key, boolean deep) {
			String[] spl = key.split("\\.");
			if(spl.length > 1) {
				ConfigSection sub = getSubsection(spl[0]);
				if(sub == null) return;
				sub.unset(key.substring(spl[0].length() + 1), deep);
			}else {
				properties.remove(key);
				if(deep) subsections.remove(key);
			}
		}
		
		public boolean containsCommentKey(String key) {
			return comments.containsKey(key);
		}
		
		public ConfigSection getParent() {
			return parent;
		}
		
		public String getName() {
			return name;
		}
		
		public String getFullName() {
			if(parent == null || parent.getFullName() == null) return name;
			return parent.getFullName() + name;
		}
		
		public HashMap<String, ConfigSection> getSubsections() {
			return subsections;
		}
		
		public HashMap<String, Property> getProperties() {
			return getProperties(false);
		}
		
		public HashMap<String, String> getComments() {
			return getComments(false);
		}
		
		public HashMap<String, Property> getProperties(boolean deep) {
			HashMap<String, Property> ps = new HashMap<>(properties);
			if(deep) subsections.values().forEach(s -> s.getProperties(true).entrySet().stream()
					.map(en -> new AbstractMap.SimpleEntry<>(name + "." + en.getKey(), en.getValue()))
					.forEach(en -> ps.put(en.getKey(), en.getValue())));
			return ps;
		}
		
		public HashMap<String, String> getComments(boolean deep) {
			HashMap<String, String> cs = new HashMap<>(comments);
			if(deep) subsections.values().forEach(s -> s.getComments(true).entrySet().stream()
					.map(en -> new AbstractMap.SimpleEntry<>(name + "." + en.getKey(), en.getValue()))
					.forEach(en -> cs.put(en.getKey(), en.getValue())));
			return cs;
		}
		
		public boolean isEmpty() {
			return properties.isEmpty() && subsections.isEmpty();
		}
		
		public String saveToString() {
			return saveToString(new ArrayList<>(), 0);
		}
		
		private List<String> getSortedKeys(List<ConfigSaveProperty> props) {
			List<String> keys = new ArrayList<>();
			properties.keySet().forEach(keys::add);
			subsections.keySet().forEach(s -> { if(!keys.contains(s)) keys.add(s);});
			return keys;
		}
		
		public String saveToString(List<ConfigSaveProperty> props, int indents) {
			String indent = config.space(indents);
			String lineSeparator = System.getProperty("line.separator");
			StringBuilder section = new StringBuilder();
			for(String key : getSortedKeys(props)) {
				String comment = getComment(key);
				if(comment != null) {
					section.append(indent).append(config.commentString).append(comment).append(lineSeparator);
				}
				Property p = properties.get(key);
				if(p != null) {
					section.append(indent).append(key).append(config.splString).append(p.value.toString()).append(lineSeparator);
				}else {
					section.append(indent).append(key).append(config.splString).append(lineSeparator);
				}
				
				ConfigSection sub = subsections.get(key);
				if(sub != null) {
					section.append(sub.saveToString(props, indents + 1));
				}
			}
			return section.toString();
		}
		
		public void clear() {
			properties.clear();
			subsections.clear();
			comments.clear();
		}
		
		public void delete() {
			parent.subsections.remove(name);
		}
		
	}

}
