package me.mrletsplay.mrcore.main;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomConfig {
	
	private static final String SPACE = "  ";
	private static final String SPL_STRING = ": ";
	private static final String
			ENTRY_STRING = "- ",
			COMMENT_STRING = "# ",
			HEADER_COMMENT_STRING = "## ";

	private File configFile;
	private URL configURL;
	private boolean isExternal, isCompact;
	private HashMap<String, Property> properties;
	private HashMap<String, String> comments;
	private List<ConfigSaveProperty> defaultSaveProps;

	public CustomConfig(File configFile, boolean compact) {
		this(configFile, compact, new ConfigSaveProperty[0]);
	}
	
	public CustomConfig(File configFile, boolean compact, ConfigSaveProperty... defaultSaveProperties) {
		this.configFile = configFile;
		isExternal = false;
		isCompact = compact;
		properties = new HashMap<>();
		comments = new HashMap<>();
		defaultSaveProps = Arrays.asList(defaultSaveProperties);
	}
	
	public File getConfigFile() {
		return configFile;
	}

	public CustomConfig(URL configURL, boolean compact) {
		this(configURL, compact, new ConfigSaveProperty[0]);
	}
	
	public CustomConfig(URL configURL, boolean compact, ConfigSaveProperty... defaultSaveProperties) {
		this.configURL = configURL;
		isExternal = true;
		isCompact = compact;
		properties = new HashMap<>();
		comments = new HashMap<>();
		defaultSaveProps = Arrays.asList(defaultSaveProperties);
	}
	
	public void setDefaultSaveProperties(ConfigSaveProperty... defaultSaveProps) {
		this.defaultSaveProps = Arrays.asList(defaultSaveProps);
	}
	
	public URL getConfigURL() {
		return configURL;
	}
	
	public boolean isExternal() {
		return isExternal;
	}

	public void saveConfig(List<ConfigSaveProperty> saveProperties) throws IOException {
		if(isExternal) return;
		configFile.getParentFile().mkdirs();
		saveConfig(new FileOutputStream(configFile), saveProperties);
	}
	
	public void saveConfig() throws IOException {
		saveConfig((List<ConfigSaveProperty>)null);
	}
	
	public void saveConfig_Safe(List<ConfigSaveProperty> saveProperties) {
		try {
			saveConfig(saveProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig_Safe() {
		saveConfig_Safe(null);
	}
	
	public void saveConfig(OutputStream fOut) throws IOException {
		saveConfig(fOut, null);
	}
	
	public void saveConfig(OutputStream fOut, List<ConfigSaveProperty> saveProperties) throws IOException{
		List<ConfigSaveProperty> props = saveProperties!=null?saveProperties:defaultSaveProps;
		if(!isCompact) {
			List<String> so = sortProperties(props.contains(ConfigSaveProperty.SORT_ALPHABETICALLY));
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(fOut, StandardCharsets.UTF_8));
			if(comments.containsKey(null)) {
				String[] splc = comments.get(null).replace("\r", "\n").split("\n", -1);
				if(splc.length>0) {
					if(props.contains(ConfigSaveProperty.SPACE_COMMENTED_PROPERTIES)) w.newLine();
					for (String s : splc) {
						w.write(HEADER_COMMENT_STRING + s);
						w.newLine();
					}
				}
			}
			String lKey = null;
			for (String key : so) {
				String ktw = key;
				StringBuilder lsb = new StringBuilder();
				if (lKey != null) {
					int hk = getHighestKey(lKey, key);
					for (int i = 0; i < hk; i++) {
						lsb.append(SPACE);
					}
					String tK = getKeyFrom(key.split("\\."), hk);
					String[] splK = tK.split("\\.");
					for(int i = hk; i < splK.length-1; i++) {
						w.write(lsb.toString()+splK[i]+SPL_STRING);
						lsb.append(SPACE);
						w.newLine();
					}
					ktw = splK[splK.length-1];
				}
				String ls = lsb.toString();
				Property p = properties.get(key);
				if((p!=null && p.getValue()!=null) || !getKeys(key, false, false).isEmpty() || props.contains(ConfigSaveProperty.INCLUDE_NULL)) {
					if(comments.containsKey(key)){
						String[] splc = comments.get(key).split("\n", -1);
						if(splc.length>0) {
							if(props.contains(ConfigSaveProperty.SPACE_COMMENTED_PROPERTIES)) w.newLine();
							for(String s : splc){
								w.write(ls + COMMENT_STRING + s);
								w.newLine();
							}
						}
					}
					w.write(ls+ktw + SPL_STRING + (p!=null&&p.getValue()!=null?(p.getType().equals(PropertyType.VALUE) ? p.getValue() : ""):""));
					if(comments.containsKey(key)) {
						if(props.contains(ConfigSaveProperty.SPACE_COMMENTED_PROPERTIES)) w.newLine();
					}
					w.newLine();
					if (p!=null && p.getType().IS_LIST) {
						String sp = "";
						if (lKey != null) {
							sp = space(getHighestKey(lKey, key));
						}
						for (Object s : (List<?>) p.getValue()) {
							w.write(sp + ENTRY_STRING + s);
							w.newLine();
						}
					}
				}
				lKey = key;
			}
			w.close();
		}else {
			//Compact format
			DataOutputStream out = new DataOutputStream(fOut);
			HashMap<String, Short> cKs = new HashMap<>();
			
			short i = 0;
			for(String key : properties.keySet()) {
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
			for(Map.Entry<String, Property> en : properties.entrySet()) {
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
			for(Map.Entry<String, String> en : comments.entrySet()) {
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
	}
	
	private String space(int length) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) sb.append(SPACE);
		return sb.toString();
	}
	
	private int getHighestKey(String lastKey, String currKey){
		String[] spl = lastKey.split("\\.");
		String[] spl2 = currKey.split("\\.");
		int a = 1;
		while(getKey(spl, a).equals(getKey(spl2, a)) && a <= spl.length && a <= spl2.length) {
			a++;
		}
		return a-1;
	}

	public CustomConfig loadConfig() throws IOException{
		if(!isExternal && !configFile.exists()){
			configFile.getParentFile().mkdirs();
			configFile.createNewFile();
		}
		loadConfig(isExternal?configURL.openStream():new FileInputStream(configFile));
		return this;
	}
	
	public CustomConfig loadConfig_Safe() {
		try {
			return loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public CustomConfig loadConfig(InputStream in) throws IOException {
		properties = new HashMap<>();
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
					r.close();
					throw new InvalidConfigException("Stage changed on LIST_ENTRY", l);
				}else if(lastP==null) {
					r.close();
					throw new InvalidConfigException("Stage > 0 in first line", l);
				}else if(tmpComments!=null && p.getType().equals(PropertyType.COMMENT)){
					r.close();
					throw new InvalidConfigException("Stage changed on COMMENT: "+stage+", previously "+lastStage, l);
				}else {
					addOrRemove(stages, lastP.getKey().split("\\."), stage - lastStage);
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
				if(!comments.containsKey(null)){
					comments.put(null, tmpHeader.stream().collect(Collectors.joining("\n")));
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
					if(!tmpList.isEmpty()) setLoadedProperty(lastStage, tmpStages, new Property(tmpKey, PropertyType.LIST, tmpList));
					tmpList = null;
					tmpKey = null;
				}
				setLoadedProperty(stage, stages, p);
				lastP = p;
			}else if(p.getType().equals(PropertyType.LIST_START)){
				if(tmpList!=null){
					if(!tmpList.isEmpty()) setLoadedProperty(lastStage, tmpStages, new Property(tmpKey, PropertyType.LIST, tmpList));
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
			setLoadedProperty(lastStage, stages, new Property(tmpKey, PropertyType.LIST, tmpList));
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
					properties.put(key, new Property(key, PropertyType.VALUE, prop));
				}else {
					List<String> lst = new ArrayList<>();
					short sz = in.readShort();
					for(int i = 0; i < sz; i++) {
						lst.add(in.readUTF());
					}
					properties.put(key, new Property(key, PropertyType.LIST, lst));
				}
			}
			
			while((len = in.readByte())>=0) {
				List<Short> parts = new ArrayList<>();
				for(int i = 0; i < len; i++) {
					parts.add(in.readShort());
				}
				
				String key = parts.stream().map(kp -> cKs.get(kp)).collect(Collectors.joining("."));
				comments.put(key, in.readUTF());
			}
			in.close();
			return this;
		}catch(EOFException e) {
			in.close();
			throw new InvalidConfigException("Invalid compact config", -1);
		}
	}

	public CustomConfig loadDefault(CustomConfig cfg, boolean override) throws IOException{
		if(properties==null){
			loadConfig();
		}
		cfg.comments.keySet().stream().filter(k -> override || !comments.containsKey(k)).forEach(k -> comments.put(k,cfg.comments.get(k)));
		cfg.properties.keySet().stream().filter(k -> override || !properties.containsKey(k)).forEach(k -> properties.put(k,cfg.properties.get(k)));
		return this;
	}

	public CustomConfig loadDefaultFromClassPath(String path, boolean override) throws IOException{
		if(properties==null){
			loadConfig();
		}
		CustomConfig cfg = new CustomConfig((File)null, false).loadConfig(getClass().getClassLoader().getResourceAsStream(path));
		loadDefault(cfg, override);
		return this;
	}

	public CustomConfig loadDefaultFromClassPath_Safe(String path, boolean override){
		try {
			loadDefaultFromClassPath(path, override);
		} catch (IOException | InvalidConfigException e) {
			e.printStackTrace();
		}
		return this;
	}

	public HashMap<String, Property> getProperties(){
		return properties;
	}

	public HashMap<String, String> getComments(){
		return comments;
	}

	private void setLoadedProperty(int currStage, List<String> stages, Property p){
		if(currStage>0) {
			properties.put(getKey(stages, currStage)+"."+p.getKey(), p);
		}else {
			properties.put(p.getKey(), p);
		}
	}

	private void setLoadedComment(int currStage, List<String> stages, String key, String comment){
		if(currStage>0) {
			comments.put(getKey(stages, currStage)+"."+key, comment);
		}else {
			comments.put(key, comment);
		}
	}

	private Property loadProperty(int line, String s) throws InvalidConfigException{
		String formattedLine = s.replaceAll("^\\s+","").replaceAll("^\\t+","");
		if(formattedLine.startsWith(COMMENT_STRING)){
			return new Property(null, PropertyType.COMMENT, formattedLine.substring(COMMENT_STRING.length()));
		}
		if(formattedLine.startsWith(HEADER_COMMENT_STRING)) {
			return new Property(null, PropertyType.HEADER_COMMENT, formattedLine.substring(COMMENT_STRING.length()));
		}
		String[] p = formattedLine.split(SPL_STRING, 2);
		if(p.length==2){
			if(p[1].isEmpty()){
				return new Property(p[0], PropertyType.LIST_START,true);
			}else{
				return new Property(p[0], PropertyType.VALUE, p[1]);
			}
		}else if(p.length==1){
			if(p[0].startsWith(ENTRY_STRING)) {
				return new Property(null, PropertyType.LIST_ENTRY, p[0].substring(ENTRY_STRING.length()));
			}else if(p[0].endsWith(SPL_STRING.trim())){
				String k = p[0].substring(0, p[0].length()-SPL_STRING.trim().length());
				return new Property(k, PropertyType.LIST_START, true);
			}
		}
		throw new InvalidConfigException("Invalid property \""+formattedLine+"\"",line);
	}

	private void addOrRemove(List<String> s, String[] spl, int c) {
		if(c>0) {
			s.addAll(Arrays.asList(spl).subList(0, c));
		}else {
			for(int i = 0; i < Math.abs(c); i++) {
				s.remove(s.size()-1);
			}
		}
	}

	private String getKey(List<String> stages, int currStage) {
		return stages.stream().limit(currStage).collect(Collectors.joining("."));
	}
	
	private List<String> sortProperties(boolean alphabetically){
		List<String> tR = new ArrayList<>();
		List<String> keys = getKeys(false);
		if(alphabetically) Collections.sort(keys);
		for(String s : keys) {
			tR.add(s);
			if(getKeys(s, false, true).size()>0) {
				tR.addAll(sortSubsection(s, alphabetically));
			}
		}
		return tR;
	}
	
	private List<String> sortSubsection(String key, boolean alphabetically){
		List<String> ks = new ArrayList<>();
		List<String> keys = getKeys(key, false, true);
		if(alphabetically) Collections.sort(keys);
		for(String s : keys) {
			ks.add(s);
			if(getKeys(s, false, true).size()>0) {
				ks.addAll(sortSubsection(s, alphabetically));
			}
		}
		return ks;
	}

	private String getKeyFrom(String[] stages, int from) {
		return Arrays.stream(stages).skip(from).collect(Collectors.joining("."));
	}

	private String getKey(String[] stages, int until) {
		return Arrays.stream(stages).limit(until).collect(Collectors.joining("."));
	}

	private int getFileStage(String s) {
		int sSpaces = s.length()-s.replaceAll("^\\s+","").length();
		int sTabs = s.length()-s.replaceAll("^\\t+","").length();
		if(sSpaces % SPACE.length()!=0) {
			return -1;
		}else {
			return (sSpaces/SPACE.length())+(sTabs*(4/SPACE.length()));
		}
	}
	
	public void set(String key, Object val) {
		set(key, val, false, null);
	}
	
	public void set(String key, Object val, boolean save) {
		set(key, val, save, null);
	}

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
		if(val==null) {
			properties.remove(key);
		}else {
			properties.put(key, new Property(key, type, val));
		}
		if(save) try {
			saveConfig(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unset(String key) {
		unset(key, false);
	}
	
	public void unset(String key, boolean deep) {
		unset(key, deep, false);
	}
	
	public void unset(String key, boolean deep, boolean save) {
		unset(key, deep, save, null);
	}
	
	public void unset(String key, boolean deep, boolean save, List<ConfigSaveProperty> props) {
		properties.remove(key);
		if(deep) {
			for(String k : getKeys(key, deep, true)) {
				properties.remove(k);
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
	
	private static String verifyString(String s) {
		return s.replace("\n", "\\n").replace("\r", "\\r");
	}
	
	public void resetWithSubkeys(String key) {
		for(String k : getKeys(key, true, true)) {
			properties.remove(k);
		}
	}
	
	public void addDefault(String key, Object defaultVal) {
		addDefault(key, defaultVal, false);
	}
	
	public void addDefault(String key, Object defaultVal, boolean save) {
		addDefault(key, defaultVal, save, null);
	}
	
	public void addDefault(String key, Object defaultVal, boolean save, List<ConfigSaveProperty> props) {
		if(!properties.containsKey(key)) {
			set(key, defaultVal, save, props);
		}
	}
	
	public void addDefault(String forKey, String key, Object defaultVal) {
		addDefault(forKey, key, defaultVal, false);
	}
	
	public void addDefault(String forKey, String key, Object defaultVal, boolean save) {
		addDefault(forKey, key, defaultVal, save, null);
	}
	
	public void addDefault(String forKey, String key, Object defaultVal, boolean save, List<ConfigSaveProperty> props) {
		if(getKeys(forKey, false, true).isEmpty()) {
			set(key, defaultVal, save, props);
		}
	}

	public void setComment(String key, String comment, boolean save) {
		setComment(key, comment, save, null);
	}
	
	public void setComment(String key, String comment, boolean save, List<ConfigSaveProperty> props) {
		comments.put(key, comment);
		if(save) try {
			saveConfig(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setComment(String key, String comment) {
		setComment(key, comment, false);
	}
	
	public void setHeader(String header, boolean save) {
		setHeader(header, save, null);
	}

	public void setHeader(String header, boolean save, List<ConfigSaveProperty> props) {
		comments.put(null, header);
		if(save) try {
			saveConfig(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setHeader(String header) {
		setHeader(header, false);
	}

	public String getString(String key) {
		return getString(key, null, false);
	}

	public String getString(String key, String defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null) {
			return ((String)p.getValue()).replace("\\n", "\n").replace("\\r", "\r");
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	public List<String> getStringList(String key) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			return castAllString((List<?>) p.getValue());
		}
		return new ArrayList<>();
	}

	public List<String> getStringList(String key, List<String> defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			return castAllString((List<?>) p.getValue());
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	public Integer getInt(String key) {
		Property p = properties.get(key);
		if(p!=null){
			return Integer.parseInt(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	public Integer getInt(String key, Integer defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null) {
			return Integer.parseInt(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	public List<Integer> getIntegerList(String key) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	public List<Integer> getIntegerList(String key, List<Integer> defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	public Double getDouble(String key) {
		Property p = properties.get(key);
		if(p!=null){
			return Double.valueOf(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	public Double getDouble(String key, Double defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null) {
			return Double.valueOf(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	public List<Double> getDoubleList(String key) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllDouble((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	public List<Double> getDoubleList(String key, List<Double> defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllDouble((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	public boolean getBoolean(String key) {
		Property p = properties.get(key);
		if(p!=null){
			return Boolean.valueOf(String.valueOf(p.getValue()));
		}else {
			return false;
		}
	}

	public boolean getBoolean(String key, boolean defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null) {
			return Boolean.valueOf(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	public List<Boolean> getBooleanList(String key) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBoolean((List<?>) p.getValue());
			} catch (IllegalArgumentException ignored){}
		}
		return new ArrayList<>();
	}
	
	public List<Boolean> getBooleanList(String key, List<Boolean> defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBoolean((List<?>) p.getValue());
			} catch (IllegalArgumentException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}

	public Object get(String key) {
		Property p = properties.get(key);
		if(p==null) return null;
		return p.getValue();
	}

	public Object get(String key, Object defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null){
			return p.getValue();
		}else{
			if(applyDefault){
				set(key, defaultVal, false);
			}
			return defaultVal;
		}
	}

	public Long getLong(String key) {
		Property p = properties.get(key);
		if(p!=null){
			return Long.parseLong(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	public Long getLong(String key, Long defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null) {
			return Long.parseLong(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	public List<Long> getLongList(String key) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllLong((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	public List<Long> getLongList(String key, List<Long> defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllLong((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}
	
	public BigInteger getBigInteger(String key) {
		Property p = properties.get(key);
		if(p!=null){
			return new BigInteger(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	public BigInteger getBigInteger(String key, BigInteger defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null) {
			return new BigInteger(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	public List<BigInteger> getBigIntegerList(String key) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBigInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	public List<BigInteger> getBigIntegerList(String key, List<BigInteger> defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBigInteger((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		if(applyDefault) set(key, defaultVal, false);
		return defaultVal;
	}
	
	public BigDecimal getBigDecimal(String key) {
		Property p = properties.get(key);
		if(p!=null){
			return new BigDecimal(String.valueOf(p.getValue()));
		}else {
			return null;
		}
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
		if(p!=null) {
			return new BigDecimal(String.valueOf(p.getValue()));
		}else {
			if(applyDefault) set(key, defaultVal, false);
			return defaultVal;
		}
	}

	public List<BigDecimal> getBigDecimalList(String key) {
		Property p = properties.get(key);
		if(p!=null && p.getType().equals(PropertyType.LIST)) {
			try {
				return castAllBigDecimal((List<?>) p.getValue());
			} catch (NumberFormatException ignored){}
		}
		return new ArrayList<>();
	}

	public List<BigDecimal> getBigDecimalList(String key, List<BigDecimal> defaultVal, boolean applyDefault) {
		Property p = properties.get(key);
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

	public List<String> getKeys(String key, boolean deep, boolean fullKeys){
		List<String> keys = new ArrayList<>();
		for(String kk : properties.keySet()) {
			if(kk.startsWith(key+".")){
				if (!deep) {
					kk = Arrays.stream(kk.split("\\.")).limit(key.split("\\.").length+1).collect(Collectors.joining("."));
					if((kk.split("\\.").length - key.split("\\.").length)!=1) continue;
				}
				if(!fullKeys){
					kk = kk.substring(key.length()+1);
				}
				if(!keys.contains(kk)) {
					keys.add(kk);
				}
			}
		}
		return keys;
	}

	public List<String> getKeys(boolean deep){
		List<String> keys = new ArrayList<>();
		for(String kk : properties.keySet()) {
			if (!deep) {
				kk = Arrays.stream(kk.split("\\.")).limit(1).collect(Collectors.joining("."));
				if(kk.split("\\.").length!=1) continue;
			}
			if(!keys.contains(kk)) {
				keys.add(kk);
			}
		}
		return keys;
	}
	
	public void reloadConfig(boolean save) throws IOException {
		reloadConfig(save, null);
	}

	public void reloadConfig(boolean save, List<ConfigSaveProperty> props) throws IOException{
		clearConfig(save, props);
		loadConfig();
	}
	
	public void clearConfig(boolean save) throws IOException {
		clearConfig(save, null);
	}

	public void clearConfig(boolean save, List<ConfigSaveProperty> props) throws IOException {
		if(save){
			saveConfig(props);
		}
		properties.clear();
	}

	public void deleteConfig(){
		if(isExternal)return;
		configFile.delete();
	}

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
	}

	public enum PropertyType {

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

	public enum ConfigSaveProperty{
		SORT_ALPHABETICALLY,
		INCLUDE_NULL,
		SPACE_COMMENTED_PROPERTIES,
		ADVANCED_COMPACT_FORMAT,
	}

	public class InvalidConfigException extends RuntimeException{
		
		private static final long serialVersionUID = 1L;

		InvalidConfigException(String reason, int line){
			super("Failed to load config: "+reason+(line!=-1?" (Line "+line+")":""));
		}

	}

}
