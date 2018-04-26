package me.mrletsplay.mrcore.config;

public class ConfigExpansions {

	public static class ConfigCustomizer {

		private String 
				space = CustomConfig.DEFAULT_SPACE,
				splString = CustomConfig.DEFAULT_SPL_STRING,
				entryString = CustomConfig.DEFAULT_ENTRY_STRING,
				commentString = CustomConfig.DEFAULT_COMMENT_STRING,
				headerCommentString = CustomConfig.DEFAULT_HEADER_COMMENT_STRING;
		
		public ConfigCustomizer withEntryPrefix(String entryPrefix) {
			this.entryString = entryPrefix;
			return this;
		}
		
		public ConfigCustomizer withCommentPrefix(String commentPrefix) {
			this.commentString = commentPrefix;
			return this;
		}
		
		public ConfigCustomizer withHeaderCommentPrefix(String headerCommentPrefix) {
			this.headerCommentString = headerCommentPrefix;
			return this;
		}
		
		public ConfigCustomizer withIndentation(String indentation) {
			this.space = indentation;
			return this;
		}
		
		public ConfigCustomizer withPropertySplitter(String propertySplitter) {
			this.splString = propertySplitter;
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
		
		public String getIndentation() {
			return space;
		}
		
		public String getPropertySplitter() {
			return splString;
		}
		
	}
	
}
