package me.mrletsplay.mrcore.mysql.protocol.flag;

public class FlagCompound {

	private long compound;
	
	public FlagCompound(long... compound) {
		for(long l : compound) {
			this.compound |= l;
		}
	}
	
	public FlagCompound(long compound) {
		this.compound = compound;
	}
	
	public FlagCompound addFlag(long flag) {
		this.compound |= flag;
		return this;
	}
	
	public boolean hasFlag(long flag) {
		return (compound & flag) == flag;
	}
	
	public void setCompound(long compound) {
		this.compound = compound;
	}
	
	public long getCompound() {
		return compound;
	}
	
	public static FlagCompound combineIfBoth(FlagCompound o1, FlagCompound o2) {
		return new FlagCompound(o1.compound & o2.compound);
	}
	
	public static FlagCompound combineIfOne(FlagCompound o1, FlagCompound o2) {
		return new FlagCompound(o1.compound | o2.compound);
	}
	
}
