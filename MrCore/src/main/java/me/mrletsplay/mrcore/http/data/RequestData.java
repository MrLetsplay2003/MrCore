package me.mrletsplay.mrcore.http.data;

public interface RequestData {
	
	public byte[] createPayload();
	
	public String getMimeType();

}
