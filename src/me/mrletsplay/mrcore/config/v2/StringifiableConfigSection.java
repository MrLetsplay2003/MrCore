package me.mrletsplay.mrcore.config.v2;

public interface StringifiableConfigSection extends ConfigSection {

	/**
	 * Returns a string representation of this section.<br>
	 * This representation can vary for each implementation and usually returns the string in a format similar to what would be produced by the {@link CustomConfig#save(java.io.OutputStream)} method
	 * @return A string representation of this section
	 */
	public String saveToString();
	
}
