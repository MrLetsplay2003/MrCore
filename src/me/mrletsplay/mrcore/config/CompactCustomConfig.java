package me.mrletsplay.mrcore.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompactCustomConfig extends CustomConfig {
	
	private static final String VERSION = "1.0";
	private String instanceVersion;
	
	public CompactCustomConfig(File configFile, ConfigSaveProperty... defaultSaveProperties) {
		super(configFile, defaultSaveProperties);
	}

	public CompactCustomConfig(URL configURL, ConfigSaveProperty... defaultSaveProperties) {
		super(configURL, defaultSaveProperties);
	}
	
	public static String getVersion() {
		return VERSION;
	}
	
	@Override
	public String getInstanceVersion() {
		return instanceVersion;
	}
	
	@Override
	public void saveConfig(OutputStream fOut, List<ConfigSaveProperty> saveProperties) throws IOException {
		if(isExternal()) throw new UnsupportedOperationException("External (url) configs cannot be saved");
		DataOutputStream out = new DataOutputStream(fOut);
		
		out.writeUTF(getVersion());
		
		HashMap<String, Short> cKs = new HashMap<>();
		
		HashMap<String, Property> properties = getProperties();
		HashMap<String, String> comments = getComments();
		
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
			writeKey(key, cKs, out);
			writeValue(prop.getValue(), out);
		}
		out.writeByte(-1);
		
		//Comments
		for(Map.Entry<String, String> en : comments.entrySet()) {
			String key = en.getKey();
			String comment = en.getValue();
			writeKey(key, cKs, out);
			out.writeUTF(comment);
		}
		out.writeByte(-1);
		out.close();
	}
	
	@SuppressWarnings("unchecked")
	private void writeValue(Object o, DataOutputStream out) throws IOException {
		FormattedProperty form = getFormatter().formatObject(o);
		switch(form.getType()) {
			case VALUE:
				out.writeByte(0);
				out.writeUTF(form.getValue().toString());
				break;
			case LIST:
				out.writeByte(1);
				writeList((List<?>) form.getValue(), out);
				break;
			case MAP:
				out.writeByte(2);
				writeMap((Map<String, ?>) form.getValue(), out);
				break;
		}
	}
	
	private void writeList(List<?> list, DataOutputStream out) throws IOException {
		out.writeInt(list.size());
		for(Object o : list) {
			writeValue(o, out);
		}
	}
	
	private void writeMap(Map<String, ?> map, DataOutputStream out) throws IOException {
		out.writeInt(map.size());
		for(Map.Entry<String, ?> en : map.entrySet()) {
			out.writeUTF(en.getKey());
			writeValue(en.getValue(), out);
		}
	}
	
	private void writeKey(String key, HashMap<String, Short> lookupTable, DataOutputStream out) throws IOException {
		String[] splK = key.split("\\.");
		out.writeByte(splK.length);
		for(String part : splK) {
			out.writeShort(lookupTable.get(part));
		}
	}
	
	@Override
	public CustomConfig loadConfig(InputStream oIn) throws IOException {
		DataInputStream in = new DataInputStream(oIn);
		try {
			HashMap<Short, String> cKs = new HashMap<>();
			
			if(in.available() == 0) {
				in.close();
				return this;
			}
			
			instanceVersion = in.readUTF();
			
			//Keys
			short id;
			while((id = in.readShort())>=0) {
				cKs.put(id, in.readUTF());
			}
			
			//Properties
			String key;
			while((key = readKey(cKs, in)) != null) {
				getParentSection().set(key, getFormatter().formatObject(readValue(in)).toProperty());
			}
			
			while((key = readKey(cKs, in)) != null) {
				getParentSection().setComment(key, in.readUTF());
			}
			in.close();
			return this;
		}catch(EOFException e) {
			in.close();
			e.printStackTrace();
			throw new InvalidConfigException("Invalid compact config", -1);
		}
	}
	
	private Object readValue(DataInputStream in) throws IOException {
		byte type = in.readByte();
		switch(type) {
			case 0: //value
				return in.readUTF();
			case 1: //list
				return readList(in);
			case 2: //map
				return readMap(in);
		}
		throw new UnsupportedOperationException("Invalid property type: "+type);
	}
	
	private List<?> readList(DataInputStream in) throws IOException {
		int size = in.readInt();
		List<Object> list = new ArrayList<>();
		while(size-- > 0) {
			list.add(readValue(in));
		}
		return list;
	}
	
	private Map<String, ?> readMap(DataInputStream in) throws IOException {
		int size = in.readInt();
		Map<String, Object> map = new HashMap<>();
		while(size-- > 0) {
			map.put(in.readUTF(), readValue(in));
		}
		return map;
	}
	
	private String readKey(HashMap<Short, String> lookupTable, DataInputStream in) throws IOException {
		List<Short> parts = new ArrayList<>();
		short len = in.readByte();
		if(len <= 0) return null;
		for(int i = 0; i < len; i++) {
			parts.add(in.readShort());
		}
		return parts.stream().map(kp -> lookupTable.get(kp)).collect(Collectors.joining("."));
	}

}
