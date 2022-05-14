package me.mrletsplay.mrcore.http.data;

public class RawData implements RequestData {
	
	private String mimeType;
	private byte[] data;

	public RawData(String mimeType, byte[] data) {
		this.mimeType = mimeType;
		this.data = data;
	}

	@Override
	public byte[] createPayload() {
		return data;
	}
	
	@Override
	public String getMimeType() {
		return mimeType;
	}

}
