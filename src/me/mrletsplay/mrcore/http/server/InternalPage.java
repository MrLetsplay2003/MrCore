package me.mrletsplay.mrcore.http.server;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.http.server.html.HTMLDocument.HttpSiteAccessedEvent;
import me.mrletsplay.mrcore.misc.JSON.JSONObject;
import me.mrletsplay.mrcore.misc.OtherTools.FriendlyException;

@FunctionalInterface
public interface InternalPage {

	public InternalResult call(HttpSiteAccessedEvent event);
	
	public static class InternalResult {
		
		private HttpStatusCode statusCode;
		private String contentType;
		private byte[] content;
		
		private InternalResult(HttpStatusCode statusCode, String contentType, byte[] content) {
			this.statusCode = statusCode;
			this.contentType = contentType;
			this.content = content;
		}
		
		public HttpStatusCode getStatusCode() {
			return statusCode;
		}
		
		public String getContentType() {
			return contentType;
		}
		
		public byte[] getContent() {
			return content;
		}
		
		public static InternalResult of(String contentType, String content) {
			return new InternalResult(HttpStatusCode.OKAY_200, contentType, content.getBytes(StandardCharsets.UTF_8));
		}
		
		public static InternalResult of(String contentType, byte[] content) {
			return new InternalResult(HttpStatusCode.OKAY_200, contentType, content);
		}
		
		public static InternalResult ofJSON(JSONObject json) {
			return of("application/json", json.toString());
		}
		
		public static InternalResult ofImage(RenderedImage image) {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "PNG", bOut);
			} catch (IOException e) {
				throw new FriendlyException(e);
			}
			return of("image/png", bOut.toByteArray());
		}
		
		public static InternalResult error(HttpStatusCode statusCode) {
			return new InternalResult(statusCode, "text/html", new byte[0]);
		}
		
	}
	
}
