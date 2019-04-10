package me.mrletsplay.mrcore.json.converter;

public interface JSONPrimitiveStringConvertible extends JSONPrimitiveConvertible {

	public String toJSONPrimitive();
	
	public void deserialize(String primitive);
	
	@Override
	public default void deserialize(Object primitive) {
		deserialize((String) primitive);
	}
	
}
