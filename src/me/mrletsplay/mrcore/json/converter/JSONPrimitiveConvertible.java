package me.mrletsplay.mrcore.json.converter;

public interface JSONPrimitiveConvertible {

	public Object toJSONPrimitive();
	
	public void deserialize(Object primitive);
	
}
