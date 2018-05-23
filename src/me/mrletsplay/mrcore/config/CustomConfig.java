package me.mrletsplay.mrcore.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

/**
 * - This is a config format based on two HashMaps, one for the properties and one for the comments<br>
 * - The file formatting is based on YAML, but is easier to read/edit for humans as it supports UTF-8 encoding, so you don't need to escape special characters<br>
 * - Every value is treated like a String or a List of Strings until further processing is requested using the get[type] methods<br>
 * - This config should be able to load most yaml-based configs without too much errors<br>
 * - This config system is based on a line-by-line basis, so every line is interpreted by itself and placed in the correct context<br>
 * <br>
 * Feel free to use this in your projects however you want (You don't need to credit me)
 * @author MrLetsplay2003
 */
public class CustomConfig {
	
	private static final String VERSION = "1.0";
	
	/**
	 * Returns this CustomConfig class's config version<br>
	 * This value might not be the newest version nor the value returned by {@link #getInstanceVersion()}<br>
	 * This value is used to tell the config loader what version this is
	 * @return This CustomConfig class's config version
	 */
	public static String getVersion() {
		return VERSION;
	}
	
	public static final String
			DEFAULT_SPACE = "  ",
			DEFAULT_SPL_STRING = ": ",
			DEFAULT_ENTRY_STRING = "- ",
			DEFAULT_COMMENT_STRING = "# ",
			DEFAULT_HEADER_COMMENT_STRING = "## ",
			DEFAULT_OBJECT_START_STRING = "{",
			DEFAULT_OBJECT_END_STRING = "}",
			DEFAULT_CUSTOMCONFIG_VERSION_STRING = "### CustomConfig version: ";
	
	private String 
			space = DEFAULT_SPACE,
			splString = DEFAULT_SPL_STRING,
			entryString = DEFAULT_ENTRY_STRING,
			commentString = DEFAULT_COMMENT_STRING,
			headerCommentString = DEFAULT_HEADER_COMMENT_STRING,
			objectStartString = DEFAULT_OBJECT_START_STRING,
			objectEndString = DEFAULT_OBJECT_END_STRING,
			customConfigVersionString = DEFAULT_CUSTOMCONFIG_VERSION_STRING;

	private File configFile;
	private URL configURL;
	private boolean isExternal;
	private HashMap<String, Object> defaults;
	private ConfigSection parentSection;
	private List<ConfigSaveProperty> defaultSaveProps;
	private long lastEdited;
	private ConfigFormatter formatter;
	
	/**
	 * Creates a CustomConfig instance with the given config file
	 * @param configFile The config file to be used
	 * @param defaultSaveProperties The default {@link ConfigSaveProperty} options for this config
	 */
	public CustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
		this.configFile = configFile;
		isExternal = false;
		defaultSaveProps = Arrays.asList(defaultSaveProperties);
		defaults = new HashMap<>();
		parentSection = new ConfigSection(this, null, null);
		formatter = new DefaultConfigFormatter(this);
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
	 * Creates a CustomConfig instance with the given config url<br>
	 * Note: External CustomConfig instances can <b>not</b> be saved
	 * @param configURL The config url to be used
	 * @param defaultSaveProperties The default {@link ConfigSaveProperty} options for this config
	 */
	public CustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
		this.configURL = configURL;
		isExternal = true;
		defaultSaveProps = Arrays.asList(defaultSaveProperties);
		defaults = new HashMap<>();
		parentSection = new ConfigSection(this, null, null);
		formatter = new DefaultConfigFormatter(this);
	}
	
	/**
	 * Sets the default save options
	 * @param defaultSaveProps The default {@link ConfigSaveProperty ConfigSaveProperty} options to use
	 */
	public void setDefaultSaveProperties(ConfigSaveProperty... defaultSaveProps) {
		this.defaultSaveProps = Arrays.asList(defaultSaveProps);
	}
	
	/**
	 * @return The default save properties of this CustomConfig instance as specified by {@link #setDefaultSaveProperties(ConfigSaveProperty...)}
	 */
	public List<ConfigSaveProperty> getDefaultSaveProperties() {
		return defaultSaveProps;
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
	 * Sets the property splitter for this CustomConfig (default: ": ")
	 * @param splString The splitter to use
	 * @return This CustomConfig instance
	 */
	public CustomConfig usePropertySplitter(String splString) {
		this.splString = splString;
		return this;
	}

	/**
	 * Sets the header comment prefix for this CustomConfig (default: "## ")
	 * @param headerCommentString The prefix to use
	 * @return This CustomConfig instance
	 */
	public CustomConfig useHeaderCommentPrefix(String headerCommentString) {
		this.headerCommentString = headerCommentString;
		return this;
	}

	/**
	 * Sets the entry prefix for this CustomConfig (default: "- ")
	 * @param entryString The prefix to use
	 * @return This CustomConfig instance
	 */
	public CustomConfig useEntryPrefix(String entryString) {
		this.entryString = entryString;
		return this;
	}

	/**
	 * Sets the comment prefix for this CustomConfig (default: "# ")
	 * @param commentString The prefix to use
	 * @return This CustomConfig instance
	 */
	public CustomConfig useCommentPrefix(String commentString) {
		this.commentString = commentString;
		return this;
	}
	
	/**
	 * Sets the formatter for this config instance
	 * @param formatter The formatter to use
	 */
	public void setFormatter(ConfigFormatter formatter) {
		this.formatter = formatter;
	}
	
	public ConfigFormatter getFormatter() {
		return formatter;
	}
	
	/**
	 * Saves the config with the given save properties<br>
	 * This method ignores the default save properties if a non-null value is given
	 * @param saveProperties The {@link ConfigSaveProperty} options to be used when saving the config
	 * @throws IOException If an IO error occurs while saving the config
	 */
	public void saveConfig(List<ConfigSaveProperty> saveProperties) throws IOException {
		if(isExternal) throw new UnsupportedOperationException("External configs cannot be saved");
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
	 * @throws UnsupportedOperationException If this config is external ({@link #isExternal()})
	 */
	public void saveConfig(OutputStream fOut, List<ConfigSaveProperty> saveProperties) throws IOException{
		if(isExternal) throw new UnsupportedOperationException("External (url) configs cannot be saved!");
		List<ConfigSaveProperty> props = saveProperties!=null?saveProperties:defaultSaveProps;
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(fOut, StandardCharsets.UTF_8));
		w.write(customConfigVersionString + getVersion());
		w.write(ConfigFormatter.newLine());
		w.write(parentSection.saveToString(props, 0));
		w.close();
		if(configFile != null) lastEdited = configFile.lastModified();
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
		BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		LineByLineReader lr = new LineByLineReader(this, r);
		ParsedLine ln = lr.readLine();
		if(ln == null) return this;
		if(ln.getType().equals(LineType.CUSTOMCONFIG_VERSION)) {
			String version = ln.getValue().val;
			if(!getVersion().equals(version)) {
				throw new InvalidConfigVersionException(version);
			}
		}else {
			lr.jumpBack();
		}
		parentSection = loadSubsection(lr, 0);
		r.close();
		return this;
	}
	
	public HashMap<String, Object> loadMap(LineByLineReader reader) throws IOException {
		HashMap<String, Object> map = new HashMap<>();
		ParsedLine line;
		int stage = reader.getPreviousLine().getStage();
		outer: while((line = reader.readLine()) != null) {
			if(line.getStage() < stage) {
				reader.jumpBack();
				break;
			}
			switch(line.type) {
				case PROPERTY:
					map.put(line.key, loadValue(line.value, reader));
					break;
				case LIST_START:
					map.put(line.key, loadList(reader));
					break;
				case OBJECT_END:
					break outer;
				default:
					reader.jumpBack();
					break outer;
			}
		}
		return map;
	}
	
	public Object loadGenericObject(LineByLineReader reader) throws IOException {
		ParsedLine line = reader.readLine();
		switch(line.type) {
			case LIST_START:
			case PROPERTY:
				reader.jumpBack();
				return loadMap(reader);
			case LIST_ENTRY:
				reader.jumpBack();
				return loadList(reader);
			default:
				throw new InvalidConfigException("Invalid generic object, got "+line.type, reader.lineNum);
		}
	}
	
	public Object loadListOrSubsection(LineByLineReader reader) throws IOException {
		ParsedLine line;
		ParsedLine l = reader.getPreviousLine();
		int startStage = l.getStage();
		while((line = reader.readLine()) != null) {
			if(line.getStage() <= startStage) {
				reader.jumpBack();
				return null;
			}
			if(line.getStage() - startStage != 1)
				throw new InvalidConfigException("Invalid stage change ("+(line.getStage() - startStage)+")", line.lineNum);
			switch(line.type) {
				case LIST_START:
				case PROPERTY:
					reader.jumpBack();
					return loadSubsection(reader, line.getStage());
				case LIST_ENTRY:
					reader.jumpBack();
					return loadList(reader);
				default:
					throw new InvalidConfigException("Invalid list or subsection, got "+line.type, reader.lineNum);
			}
		}
		return null;
	}
	
	public ConfigSection loadSubsection(LineByLineReader reader, int startStage) throws IOException {
		ConfigSection section = new ConfigSection(this, null, null);
		List<String> tmpComment = null;
		ParsedLine line;
		while((line = reader.readLine()) != null) {
			if(line.getStage() == -1) throw new InvalidConfigException("Invalid stage", line.lineNum);
			if(line.getStage() < startStage) {
				reader.jumpBack();
				return section;
			}
			switch(line.type) {
				case COMMENT:
					reader.jumpBack();
					tmpComment = loadComment(reader);
					break;
				case HEADER_COMMENT:
					reader.jumpBack();
					section.setComment(null, loadComment(reader).stream().collect(Collectors.joining("\n")));
					tmpComment = null;
					break;
				case LIST_START:
					if(tmpComment != null) {
						section.setComment(line.getKey(), tmpComment.stream().collect(Collectors.joining("\n")));
						tmpComment = null;
					}
					Object o = loadListOrSubsection(reader);
					if(o == null) break;
					if(o instanceof ConfigSection) {
						section.subsections.put(line.key, new ConfigSection((ConfigSection) o, section, line.key));
					}else if(o instanceof List<?>) {
						section.set(line.key, new Property(PropertyType.LIST, o));
					}
					break;
				case PROPERTY:
					if(tmpComment != null) {
						setComment(line.getKey(), tmpComment.stream().collect(Collectors.joining("\n")));
						tmpComment = null;
					}
					section.set(line.getKey(), formatter.formatObject(loadValue(line.value, reader)).toProperty());
					break;
				default:
					throw new InvalidConfigException("Unexpected "+line.type, line.lineNum);
			}
		}
		return section;
	}

	public List<Object> loadList(LineByLineReader reader) throws IOException {
		List<Object> list = new ArrayList<>();
		ParsedLine line;
		outer: while((line = reader.readLine()) != null) {
			switch(line.type) {
				case LIST_ENTRY:
					list.add(loadValue(line.value, reader));
					break;
				default:
					reader.jumpBack();
					break outer;
			}
		}
		return list;
	}

	public List<String> loadComment(LineByLineReader reader) throws IOException {
		List<String> comments = new ArrayList<>();
		ParsedLine line;
		outer: while((line = reader.readLine()) != null) {
			switch(line.type) {
				case COMMENT:
					comments.add(line.value.val);
					break;
				default:
					reader.jumpBack();
					break outer;
			}
		}
		return comments;
	}

	public List<String> loadHeaderComment(LineByLineReader reader) throws IOException {
		List<String> comments = new ArrayList<>();
		ParsedLine line;
		outer: while((line = reader.readLine()) != null) {
			switch(line.type) {
				case HEADER_COMMENT:
					comments.add(line.value.val);
					break;
				default:
					reader.jumpBack();
					break outer;
			}
		}
		return comments;
	}
	
	public Object loadValue(ParsedValue value, LineByLineReader reader) throws IOException {
		switch(value.type) {
			case DEFAULT:
				return value.val;
			case OBJECT_START:
				return loadGenericObject(reader);
		}
		return null;
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
		for(Map.Entry<String, Property> en : cfg.getProperties().entrySet()) {
			if(override || !properties.containsKey(en.getKey())) {
				set(en.getKey(), en.getValue().getValue());
			}
		}
		for(Map.Entry<String, String> en : cfg.getComments().entrySet()) {
			if(override || !comments.containsKey(en.getKey())) {
				setComment(en.getKey(), en.getValue());
			}
		}
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
		CustomConfig cfg = new CustomConfig((File) null).loadConfig(clazz.getClassLoader().getResourceAsStream(path));
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
	 * Sets this CustomConfig's (raw) properties.<br>
	 * This will not delete any existing properties but will override them if specified in the HashMap
	 * @param properties The property map to load
	 * @return This CustomConfig instance
	 */
	public CustomConfig setRawProperties(Map<String, Object> properties) {
		properties.entrySet().forEach(en -> {
			set(en.getKey(), en.getValue());
		});
		return this;
	}

	/**
	 * Sets this CustomConfig's comments.<br>
	 * This will not delete any existing comments but will override them if specified in the HashMap
	 * @param properties The comment map to load
	 * @return This CustomConfig instance
	 */
	public CustomConfig setComments(HashMap<String, String> comments) {
		comments.entrySet().forEach(en -> {
			parentSection.setComment(en.getKey(), en.getValue());
		});
		return this;
	}
	
	/**
	 * @return This CustomConfig's (raw) property map
	 */
	public Map<String, Object> getRawProperties() {
		return getProperties().entrySet().stream()
				.collect(Collectors.toMap(en -> (String) en.getKey(), en -> en.getValue().getValue()));
	}
	
	/**
	 * @return This config's property HashMap
	 */
	public HashMap<String, Property> getProperties(){
		return parentSection.getProperties(true);
	}

	/**
	 * @return This config's comment HashMap
	 */
	public HashMap<String, String> getComments(){
		return parentSection.getComments(true);
	}

	private ParsedLine parseLine(int line, String s) throws InvalidConfigException{
		int indents = getFileStage(s);
		String formattedLine = s.replaceAll("^\\s+","").replaceAll("^\\t+","");
		if(formattedLine.isEmpty()) return null;
		
		boolean tmpBool = false;
		
		if(tmpBool = formattedLine.startsWith(customConfigVersionString) || formattedLine.startsWith(customConfigVersionString.trim())){
			return new ParsedLine(line, indents, null, LineType.CUSTOMCONFIG_VERSION, new ParsedValue(ValueType.DEFAULT,
					formattedLine.substring(tmpBool?customConfigVersionString.length():customConfigVersionString.trim().length())));
		}
		
		if(tmpBool = formattedLine.startsWith(commentString) || formattedLine.startsWith(commentString.trim())){
			return new ParsedLine(line, indents, null, LineType.COMMENT, new ParsedValue(ValueType.DEFAULT,
					formattedLine.substring(tmpBool?commentString.length():commentString.trim().length())));
		}
		
		if(tmpBool = formattedLine.startsWith(headerCommentString) || formattedLine.startsWith(headerCommentString.trim())) {
			return new ParsedLine(line, indents, null, LineType.COMMENT, new ParsedValue(ValueType.DEFAULT,
					formattedLine.substring(tmpBool?headerCommentString.length():headerCommentString.trim().length())));
		}
		
		if(formattedLine.startsWith(entryString)) {
			return new ParsedLine(line, indents, null, LineType.LIST_ENTRY, parseValue(formattedLine.substring(entryString.length())));
		}
		
		if(formattedLine.equals(entryString.trim())) {
			return new ParsedLine(line, indents, null, LineType.LIST_ENTRY, new ParsedValue(ValueType.DEFAULT, ""));
		}
		
		String[] p = formattedLine.split(splString, 2);
		if(p.length==2){
			if(p[1].isEmpty()){
				return new ParsedLine(line, indents, p[0], LineType.LIST_START, new ParsedValue(ValueType.DEFAULT, null));
			}else{
				return new ParsedLine(line, indents, p[0], LineType.PROPERTY, parseValue(p[1]));
			}
		}
		
		if(formattedLine.equals(objectEndString)) return new ParsedLine(line, indents, null, LineType.OBJECT_END, null);
		
		
		throw new InvalidConfigException("Invalid property \""+formattedLine+"\"",line);
	}
	
	private ParsedValue parseValue(String value) {
		if(value.equals(objectStartString)) {
			return new ParsedValue(ValueType.OBJECT_START, value);
		}
		return new ParsedValue(ValueType.DEFAULT, value);
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
	public void set(String key, Object val, boolean save, List<ConfigSaveProperty> props) {
		FormattedProperty fProp = formatter.formatObject(val);
		parentSection.set(key, fProp.toProperty());
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
	
	/**
	 * Adds a default value to the config<br>
	 * <br>
	 * These values need to be applied using {@link #applyDefaults(boolean, boolean, List)}
	 * @param key The key
	 * @param defaultVal The default value
	 */
	public void addDefault(String key, Object defaultVal) {
		defaults.put(key, defaultVal);
	}

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
		return getGeneric(key, String.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<String> getStringList(String key) {
		return getGenericList(key, String.class, new ArrayList<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<String> getStringList(String key, List<String> defaultVal, boolean applyDefault) {
		return getGenericList(key, String.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Integer getInt(String key) {
		return getGeneric(key, Integer.class, null, false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Integer getInt(String key, Integer defaultVal, boolean applyDefault) {
		return getGeneric(key, Integer.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Integer> getIntegerList(String key) {
		return getGenericList(key, Integer.class, new ArrayList<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Integer> getIntegerList(String key, List<Integer> defaultVal, boolean applyDefault) {
		return getGenericList(key, Integer.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Double getDouble(String key) {
		return getGeneric(key, Double.class, null, false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Double getDouble(String key, Double defaultVal, boolean applyDefault) {
		return getGeneric(key, Double.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Double> getDoubleList(String key) {
		return getGenericList(key, Double.class, new ArrayList<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Double> getDoubleList(String key, List<Double> defaultVal, boolean applyDefault) {
		return getGenericList(key, Double.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public boolean getBoolean(String key) {
		return getGeneric(key, Boolean.class, false, false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public boolean getBoolean(String key, boolean defaultVal, boolean applyDefault) {
		return getGeneric(key, Boolean.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Boolean> getBooleanList(String key) {
		return getGenericList(key, Boolean.class, new ArrayList<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Boolean> getBooleanList(String key, List<Boolean> defaultVal, boolean applyDefault) {
		return getGenericList(key, Boolean.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Object get(String key) {
		return getGeneric(key, Object.class, null, false);
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
		return getGeneric(key, Object.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Long getLong(String key) {
		return getGeneric(key, Long.class, null, false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Long getLong(String key, Long defaultVal, boolean applyDefault) {
		return getGeneric(key, Long.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Long> getLongList(String key) {
		return getGenericList(key, Long.class, new ArrayList<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<Long> getLongList(String key, List<Long> defaultVal, boolean applyDefault) {
		return getGenericList(key, Long.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigInteger getBigInteger(String key) {
		return getGeneric(key, BigInteger.class, null, false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigInteger getBigInteger(String key, BigInteger defaultVal, boolean applyDefault) {
		return getGeneric(key, BigInteger.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigInteger> getBigIntegerList(String key) {
		return getGenericList(key, BigInteger.class, new ArrayList<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigInteger> getBigIntegerList(String key, List<BigInteger> defaultVal, boolean applyDefault) {
		return getGenericList(key, BigInteger.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigDecimal getBigDecimal(String key) {
		return getGeneric(key, BigDecimal.class, null, false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public BigDecimal getBigDecimal(String key, BigDecimal defaultVal, boolean applyDefault) {
		return getGeneric(key, BigDecimal.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigDecimal> getBigDecimalList(String key) {
		return getGenericList(key, BigDecimal.class, new ArrayList<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public List<BigDecimal> getBigDecimalList(String key, List<BigDecimal> defaultVal, boolean applyDefault) {
		return getGenericList(key, BigDecimal.class, defaultVal, applyDefault);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	public Map<String, Object> getMap(String key) {
		return getGenericMap(key, Object.class, new HashMap<>(), false);
	}

	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> getMapList(String key) {
		List<Map> maps = getGenericList(key, Map.class);
		return maps.stream().map(e -> (Map<String, Object>)e).collect(Collectors.toList());
	}
	
	/**
	 * See {@link #get(String, Object, boolean)}
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String key, Map<String, Object> defaultVal, boolean applyDefault) {
		Property p = parentSection.get(key);
		if(p!=null && p.getType().equals(PropertyType.MAP)) {
			return (Map<String, Object>) p.getValue();
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}
	
	public <T> T getGeneric(String key, Class<T> clazz) {
		Property prop = parentSection.get(key);
		if(prop == null) return null;
		if(!prop.type.equals(PropertyType.VALUE)) throw new InvalidTypeException(key, "Invalid property type "+prop.type);
		try {
			return castGeneric(prop.value, clazz);
		}catch(Exception e) {
			throw new InvalidTypeException(key, "Failed to cast type", e);
		}
	}
	
	public <T> T getGeneric(String key, Class<T> clazz, T defaultVal, boolean applyDefault) {
		T obj = getGeneric(key, clazz);
		if(obj != null) return obj;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
	public <T> List<T> getGenericList(String key, Class<T> clazz) {
		Property prop = parentSection.get(key);
		if(prop == null) return null;
		if(!prop.type.equals(PropertyType.LIST)) throw new InvalidTypeException(key, "Invalid property type"+prop.type);
		try {
			return castGenericList((List<?>) prop.value, clazz);
		}catch(Exception e) {
			throw new InvalidTypeException(key, "Failed to cast type", e);
		}
	}
	
	public <T> List<T> getGenericList(String key, Class<T> clazz, List<T> defaultVal, boolean applyDefault) {
		List<T> obj = getGenericList(key, clazz);
		if(obj != null) return obj;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getGenericMap(String key, Class<T> clazz) {
		Property prop = parentSection.get(key);
		if(prop == null) return null;
		if(!prop.type.equals(PropertyType.MAP)) throw new InvalidTypeException(key, "Invalid property type"+prop.type);
		try {
			return castGenericMap((Map<String, ?>) prop.value, clazz);
		}catch(Exception e) {
			throw new InvalidTypeException(key, "Failed to cast type", e);
		}
	}
	
	public <T> Map<String, T> getGenericMap(String key, Class<T> clazz, Map<String, T> defaultVal, boolean applyDefault) {
		Map<String, T> obj = getGenericMap(key, clazz);
		if(obj != null) return obj;
		if(applyDefault) set(key, defaultVal);
		return defaultVal;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T castGeneric(Object obj, Class<T> clazz) {
		if(clazz.equals(Object.class)) return (T) obj;
		if(clazz.equals(Map.class)) return (T) obj;
		if(clazz.equals(List.class)) return (T) obj;
		String val = (String) obj;
		if(clazz.equals(String.class)) return (T) val;
		if(clazz.equals(Boolean.class)) return (T) Boolean.valueOf(val);
		if(clazz.equals(Integer.class)) return (T) Integer.valueOf(val);
		if(clazz.equals(Double.class)) return (T) Double.valueOf(val);
		if(clazz.equals(Long.class)) return (T) Long.valueOf(val);
		if(clazz.equals(BigInteger.class)) return (T) new BigInteger(val);
		if(clazz.equals(BigDecimal.class)) return (T) new BigDecimal(val);
		throw new InvalidTypeException("Unsupported type: "+clazz.getName());
	}
	
	private <T> List<T> castGenericList(List<?> list, Class<T> clazz) {
		return list.stream().map(o -> castGeneric(o, clazz)).collect(Collectors.toList());
	}
	
	private <T> Map<String, T> castGenericMap(Map<String, ?> map, Class<T> clazz) {
		return map.entrySet().stream()
				.map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), castGeneric(e.getValue(), clazz)))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
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

		private Object value;
		private PropertyType type;

		public Property(PropertyType type, Object value){
			this.value = value;
			this.type = type;
		}

		public PropertyType getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return ""+value;
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
		
		VALUE,
		LIST,
		MAP;

	}
	
	public static enum LineType {
		
		CUSTOMCONFIG_VERSION,
		COMMENT,
		HEADER_COMMENT,
		PROPERTY,
		LIST_START,
		LIST_ENTRY,
		OBJECT_END
		
	}
	
	public static enum ValueType {
		
		DEFAULT,
		OBJECT_START
		
	}
	
	public static class ParsedLine {
		
		private int stage, lineNum;
		private String key;
		private ParsedValue value;
		private LineType type;

		public ParsedLine(int lineNum, int stage, String key, LineType type, ParsedValue value){
			this.lineNum = lineNum;
			this.stage = stage;
			this.value = value;
			this.type = type;
			this.key = key;
		}
		
		public int getLineNumber() {
			return lineNum;
		}
		
		public int getStage() {
			return stage;
		}

		public LineType getType() {
			return type;
		}

		public ParsedValue getValue() {
			return value;
		}

		public String getKey() {
			return key;
		}
		
		@Override
		public String toString() {
			return "[("+type+")"+key+" => "+value+"]";
		}
		
	}
	
	public static class ParsedValue {
		
		private ValueType type;
		private String val;
		
		public ParsedValue(ValueType type, String val) {
			this.type = type;
			this.val = val;
		}
		
		public ValueType getType() {
			return type;
		}
		
		public Object getValue() {
			return val;
		}
		
		@Override
		public String toString() {
			return "Parsed[("+type+") "+val+"]";
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
		 * When saving the config, properties with comments will have one empty line added before and after them<br>
		 * Currently does nothing
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
		
		private static final long serialVersionUID = -3103267125846837725L;

		public InvalidConfigException(String reason, int line){
			super("Failed to load config: "+reason+(line!=-1?" (Line "+line+")":""));
		}

		public InvalidConfigException(String reason, int line, Throwable cause){
			super("Failed to load config: "+reason+(line!=-1?" (Line "+line+")":""), cause);
		}

	}

	/**
	 * An exception thrown when the given config is incompatible with the newest version<br>
	 * Can be caught to convert it to the newest version
	 * @author MrLetsplay2003
	 */
	public static class InvalidConfigVersionException extends RuntimeException{
		
		private static final long serialVersionUID = -3103267125846837725L;
		private String version;

		public InvalidConfigVersionException(String message, String version){
			super(message);
			this.version = version;
		}

		public InvalidConfigVersionException(String version){
			super("Incompatible version: "+version);
			this.version = version;
		}
		
		public String getVersion() {
			return version;
		}

	}

	/**
	 * An exception thrown when the given config is not in the correct format
	 * @author MrLetsplay2003
	 */
	public static class InvalidTypeException extends RuntimeException{
		
		private static final long serialVersionUID = -329316730029087146L;

		public InvalidTypeException(String reason){
			super(reason);
		}

		public InvalidTypeException(String key, String reason){
			super("Failed to get property \""+key+"\": "+reason);
		}

		public InvalidTypeException(String key, String reason, Throwable cause){
			super("Failed to get property \""+key+"\": "+reason, cause);
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
		
		public ConfigSection(ConfigSection from, ConfigSection parent, String name) {
			this.config = from.config;
			this.parent = parent;
			this.name = name;
			subsections = from.subsections;
			properties = from.properties;
			comments = from.comments;
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
				if(property == null || property.value == null) {
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
			List<String> keys = new ArrayList<>(properties.keySet()).stream().map(k -> fullKeys && name!=null ? name + "." + k : k).collect(Collectors.toList());
			keys.addAll(subsections.keySet().stream().map(k -> fullKeys && name!=null ? name + "." + k : k).collect(Collectors.toList()));
			if(deep) {
				subsections.values().forEach(s -> s.getKeys(deep, fullKeys).forEach(k -> keys.add(fullKeys && name != null ? name + "." + k : k)));
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
			HashMap<String, Property> ps = new HashMap<>();
			properties.entrySet().stream()
					.map(en -> new AbstractMap.SimpleEntry<String, Property>(name != null ? name + "." + en.getKey() : en.getKey(), en.getValue()))
					.forEach(en -> ps.put(en.getKey(), en.getValue()));
			
			if(deep) subsections.values().forEach(s -> s.getProperties(true).entrySet().stream()
					.map(en -> new AbstractMap.SimpleEntry<>((name==null?"":name + ".") + en.getKey(), en.getValue()))
					.forEach(en -> ps.put(en.getKey(), en.getValue())));
			return ps;
		}
		
		public HashMap<String, String> getComments(boolean deep) {
			HashMap<String, String> cs = new HashMap<>();
			comments.entrySet().stream()
				.map(en -> new AbstractMap.SimpleEntry<String, String>(name != null ? name + "." + en.getKey() : en.getKey(), en.getValue()))
				.forEach(en -> cs.put(en.getKey(), en.getValue()));
			if(deep) subsections.values().forEach(s -> s.getComments(true).entrySet().stream()
					.map(en -> new AbstractMap.SimpleEntry<>((name==null?"":name + ".") + en.getKey(), en.getValue()))
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
			String indent = config.formatter.space(indents);
			String lineSeparator = System.getProperty("line.separator");
			StringBuilder section = new StringBuilder();
			for(String key : getSortedKeys(props)) {
				String comment = getComment(key);
				if(comment != null) {
					section.append(indent).append(config.commentString).append(comment).append(lineSeparator);
				}
				Property p = properties.get(key);
				if(p != null) {
					config.formatter.formatProperty(key, config.formatter.formatObject(p.value), indents).forEach(section::append);
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
			if(parent != null) parent.subsections.remove(name);
		}
		
	}
	
	public static abstract class ConfigFormatter {
		
		private CustomConfig config;
		
		public ConfigFormatter(CustomConfig config) {
			this.config = config;
		}
		
		public CustomConfig getConfig() {
			return config;
		}
		
		public abstract FormattedProperty formatObject(Object o);
		
		public abstract List<String> formatProperty(String key, FormattedProperty p, int indents);
		
		public abstract List<String> formatValue(String key, Object val, int indents);

		public abstract List<String> formatListFully(String key, List<?> list, int indents);

		public abstract List<String> formatList(List<?> list, int indents);
		
		public abstract List<String> formatMapFully(String key, Map<String, ?> map, int indents);
		
		public abstract List<String> formatMap(Map<String, ?> map, int indents);
		
		public String space(int length) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < length; i++) sb.append(config.space);
			return sb.toString();
		}
		
		public static String newLine() {
			return System.getProperty("line.separator");
		}
		
		public static String escapeString(String s) {
			return s.replace("\n", "\\n").replace("\r", "\\r");
		}
		
		public static String unescapeString(String s) {
			return s.replace("\\n", "\n").replace("\\r", "\r");
		}
		
	}
	
	public static class FormattedProperty {
		
		private Object val;
		private PropertyType type;
		
		private FormattedProperty(PropertyType type, Object val) {
			this.type = type;
			this.val = val;
		}
		
		public static FormattedProperty other(Object o) {
			return new FormattedProperty(PropertyType.VALUE, o);
		}
		
		public static FormattedProperty list(List<?> list) {
			return new FormattedProperty(PropertyType.LIST, list);
		}
		
		public static FormattedProperty map(Map<String,?> map) {
			return new FormattedProperty(PropertyType.MAP, map);
		}
		
		public PropertyType getType() {
			return type;
		}
		
		public boolean isSpecific() {
			return !type.equals(PropertyType.VALUE);
		}
		
		public Object getValue() {
			return val;
		}
		
		public Property toProperty() {
			return new Property(type, val != null ? (type.equals(PropertyType.VALUE) ? val.toString() : val) : null);
		}
		
	}
	
	public static class DefaultConfigFormatter extends ConfigFormatter {

		public DefaultConfigFormatter(CustomConfig config) {
			super(config);
		}

		@SuppressWarnings("unchecked")
		@Override
		public FormattedProperty formatObject(Object o) {
			if(o instanceof List<?>) {
				List<?> l = (List<?>) o;
				return FormattedProperty.list(l);
			}
			if(o instanceof Map<?,?>) {
				Map<?,?> mp = (Map<?,?>) o;
				if(!mp.isEmpty() && mp.keySet().stream().findFirst().get() instanceof String) {
					Map<String, ?> map = (Map<String, ?>) o;
					return FormattedProperty.map(map);
				}
			}
			return FormattedProperty.other(o);
		}

		@Override
		public List<String> formatListFully(String key, List<?> list, int indents) {
			List<String> lines = new ArrayList<>();
			lines.addAll(formatValue(key, null, indents));
			lines.addAll(formatList(list, indents + 1));
			return lines;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public List<String> formatList(List<?> list, int indents) {
			List<String> lines = new ArrayList<>();
			String space = space(indents);
			for(Object o : list) {
				StringBuilder b = new StringBuilder()
					.append(space)
					.append(getConfig().entryString);
				FormattedProperty prop = formatObject(o);
				switch(prop.type) {
					case VALUE:
						b.append(escapeString(o.toString()));
						break;
					case LIST:
						b.append(getConfig().objectStartString).append(newLine());
						lines.add(b.toString());
						b = new StringBuilder();
						lines.addAll(formatList((List<?>) prop.val, indents + 1));
						lines.add(new StringBuilder(space).append(getConfig().objectEndString).toString());
						break;
					case MAP:
						b.append(getConfig().objectStartString).append(newLine());
						lines.add(b.toString());
						b = new StringBuilder();
						lines.addAll(formatMap((Map<String, ?>) prop.val, indents + 1));
						lines.add(new StringBuilder(space).append(getConfig().objectEndString).toString());
						break;
				}
				b.append(newLine());
				lines.add(b.toString());
			}
			return lines;
		}

		@Override
		public List<String> formatValue(String key, Object val, int indents) {
			return Arrays.asList(new StringBuilder()
						.append(space(indents))
						.append(key)
						.append(getConfig().splString)
						.append(val!=null?escapeString(val.toString()):"")
						.append(newLine())
					.toString());
		}

		@Override
		public List<String> formatMapFully(String key, Map<String,?> map, int indents) {
			List<String> lines = new ArrayList<>();
			lines.addAll(formatValue(key, getConfig().objectStartString, indents));
			lines.addAll(formatMap(map, indents + 1));
			lines.add(new StringBuilder().append(space(indents)).append(getConfig().objectEndString).append(newLine()).toString());
			return lines;
		}
		
		@Override
		public List<String> formatMap(Map<String, ?> map, int indents) {
			List<String> lines = new ArrayList<>();
			for(Map.Entry<String,?> en : map.entrySet()) {
				FormattedProperty prop = formatObject(en.getValue());
				lines.addAll(formatProperty((String) en.getKey(), prop, indents));
			}
			return lines;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> formatProperty(String key, FormattedProperty prop, int indents) {
			List<String> lines = new ArrayList<>();
			Property p = prop.toProperty();
			switch(p.type) {
				case VALUE:
					lines.addAll(formatValue(key, p.value, indents));
					break;
				case LIST:
					formatListFully(key, (List<?>) p.value, indents).forEach(lines::add);
					break;
				case MAP:
					formatMapFully(key, (Map<String,?>) p.value, indents).forEach(lines::add);
					break;
				default:
					break;
			}
			return lines;
		}
		
	}
	
	public static class LineByLineReader {
		
		private int lineNum;
		private int overrideIndex;
		private List<ParsedLine> lastLines;
		private CustomConfig config;
		private BufferedReader reader;
		
		public LineByLineReader(CustomConfig cfg, BufferedReader reader) {
			this.config = cfg;
			this.reader = reader;
			this.lineNum = 0;
			this.overrideIndex = -1;
			this.lastLines = new ArrayList<>();
		}
		
		private String supplyLine() throws IOException {
			String line = "";
			while(line.trim().isEmpty()) {
				line = reader.readLine();
				lineNum++;
				if(line == null) return null;
			}
			return line;
		}
		
		public ParsedLine getPreviousLine() {
			return overrideIndex == -1 ? (lastLines.isEmpty() ? null : lastLines.get(lastLines.size() - 1)) : lastLines.get(overrideIndex - 1);
		}
		
		public ParsedLine getLastLine() {
			return !lastLines.isEmpty() ? lastLines.get(lastLines.size()-1) : null;
		}
		
		public ParsedLine readLine() throws IOException {
			if(overrideIndex != -1 && overrideIndex < lastLines.size()) {
				ParsedLine tmp = lastLines.get(overrideIndex++);
				if(overrideIndex >= lastLines.size()) overrideIndex = -1;
				return tmp;
			}
			overrideIndex = -1;
			String line = supplyLine();
			if(line == null) return null;
			ParsedLine pLine = config.parseLine(lineNum, line);
			lastLines.add(pLine);
			return pLine;
		}
		
		public void jumpBack() {
			overrideIndex = (overrideIndex == -1 ? lastLines.size()-1 : overrideIndex--);
		}
		
	}
	
}
