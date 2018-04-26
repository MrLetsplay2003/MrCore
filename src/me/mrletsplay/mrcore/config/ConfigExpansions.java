package me.mrletsplay.mrcore.config;

public class ConfigExpansions {

	public static class ConfigCustomizer {

		private String 
				entryString = CustomConfig.DEFAULT_ENTRY_STRING,
				commentString = CustomConfig.DEFAULT_COMMENT_STRING,
				headerCommentString = CustomConfig.DEFAULT_HEADER_COMMENT_STRING;
		
		public ConfigCustomizer withEntryPrefix(String entryString) {
			this.entryString = entryString;
			return this;
		}
		
		public ConfigCustomizer withCommentPrefix(String commentString) {
			this.commentString = commentString;
			return this;
		}
		
		public ConfigCustomizer withHeaderCommentPrefix(String headerCommentString) {
			this.headerCommentString = headerCommentString;
			return this;
		}
		
		public String getEntryPrefix() {
			return entryString;
		}
		
		public String getCommentPrefix() {
			return commentString;
		}
		
		public String getHeaderCommentPrefix() {
			return headerCommentString;
		}
		
	}
	
}
