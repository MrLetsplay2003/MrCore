package me.mrletsplay.mrcore.misc;

import me.mrletsplay.mrcore.json.converter.JSONConvertible;

public interface Updateable extends JSONConvertible {

	/**
	 * Sets the "changed" state of this object to <code>true</code><br>
	 * Any subsequent calls to {@link #hasChanged()} will return true until {@link #resetChanged()} is called
	 */
	public void update();
	
	/**
	 * @return Whether or not the object's "changed" state has been set to <code>true</code> (e.g. via a call to {@link #update()}) since the last call to {@link #resetChanged()}
	 */
	public boolean hasChanged();
	
	/**
	 * Reset's the object's "changed" state to <code>false</code><br>
	 * Any subsequent calls to {@link #hasChanged()} will return false until the object's "changed" state is set to <code>true</code> (e.g. via a call to {@link #update()})
	 */
	public void resetChanged();
	
}
