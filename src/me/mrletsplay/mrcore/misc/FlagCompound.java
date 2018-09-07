package me.mrletsplay.mrcore.misc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlagCompound {
	
	private long compound;
	
	public FlagCompound(long... compound) {
		for(long l : compound) {
			this.compound |= l;
		}
	}
	
	public FlagCompound(Flag... compound) {
		for(Flag l : compound) {
			this.compound |= l.getValue();
		}
	}
	
	public FlagCompound(FlagCompound... compound) {
		for(FlagCompound l : compound) {
			this.compound |= l.compound;
		}
	}
	
	public FlagCompound(long compound) {
		this.compound = compound;
	}
	
	public FlagCompound addFlag(long flag) {
		this.compound |= flag;
		return this;
	}
	
	public FlagCompound addFlag(Flag flag) {
		this.compound |= flag.getValue();
		return this;
	}
	
	public FlagCompound addFlags(FlagCompound flag) {
		this.compound |= flag.compound;
		return this;
	}
	
	public boolean hasFlag(long flag) {
		return (compound & flag) == flag;
	}
	
	public boolean hasFlag(Flag flag) {
		return (compound & flag.getValue()) == flag.getValue();
	}
	
	public List<Flag> getApplicableFlags(List<Flag> flags) {
		return flags.stream().filter(f -> hasFlag(f)).collect(Collectors.toList());
	}
	
	public List<Flag> getApplicableFlags(Flag... flags) {
		return Arrays.stream(flags).filter(f -> hasFlag(f)).collect(Collectors.toList());
	}
	
	public void setCompound(long compound) {
		this.compound = compound;
	}
	
	public long getCompound() {
		return compound;
	}
	
	/**
	 * @deprecated Use <code>{@link #combine(FlagCompound, FlagCompound, FlagCombinationMode) combine}(flag1, flag2, {@link CombinationMode#AND})</code> instead
	 */
	@Deprecated
	public static FlagCompound combineIfBoth(FlagCompound flag1, FlagCompound flag2) {
		return new FlagCompound(flag1.compound & flag2.compound);
	}

	/**
	 * @deprecated Use <code>{@link #combine(FlagCompound, FlagCompound, FlagCombinationMode) combine}(flag1, flag2, {@link CombinationMode#OR})</code> instead
	 */
	@Deprecated
	public static FlagCompound combineIfOne(FlagCompound flag1, FlagCompound flag2) {
		return new FlagCompound(flag1.compound | flag2.compound);
	}
	
	public static FlagCompound combine(FlagCompound f1, FlagCompound f2, FlagCombinationMode mode) {
		return new FlagCompound(mode.combineBulk(f1.compound, f2.compound));
	}

	public static interface Flag {
		
		public String getName();
		
		public long getValue();
		
	}
	
	public static enum CombinationMode implements FlagCombinationMode {
		
		AND((BulkFlagCombinationMode) (a, b) -> a & b),
		OR((BulkFlagCombinationMode) (a, b) -> a | b);
		
		private FlagCombinationMode mode;
		
		private CombinationMode(FlagCombinationMode mode) {
			this.mode = mode;
		}

		@Override
		public boolean combineBit(long flag1, long flag2, boolean flag1Value, boolean flag2Value) {
			return mode.combineBit(flag1, flag2, flag1Value, flag2Value);
		}

		@Override
		public long combineBulk(long bulkFlag1, long bulkFlag2) {
			return mode.combineBulk(bulkFlag1, bulkFlag2);
		}
		
	}

	public static interface FlagCombinationMode {
		
		public long combineBulk(long bulkFlag1, long bulkFlag2);
		
		public boolean combineBit(long flag1, long flag2, boolean flag1Value, boolean flag2Value);
		
	}
	
	@FunctionalInterface
	public static interface BitwiseFlagCombinationMode extends FlagCombinationMode {
		
		@Override
		public default long combineBulk(long bulkFlag1, long bulkFlag2){
			long newFlag = 0;
			for(int i = 0; i < 64; i++) {
				byte b1 = (byte) ((bulkFlag1 >> i) & 1);
				byte b2 = (byte) ((bulkFlag2 >> i) & 1);
				newFlag |= combineBit(b1 << i, b2 << i, b1 == 1, b2 == 1) ? 1 : 0;
			}
			return newFlag;
		}
		
		@Override
		public boolean combineBit(long flag1, long flag2, boolean flag1Value, boolean flag2Value);
		
	}
	
	@FunctionalInterface
	public static interface BulkFlagCombinationMode extends FlagCombinationMode {
		
		@Override
		public long combineBulk(long bulkFlag1, long bulkFlag2);
		
		@Override
		public default boolean combineBit(long flag1, long flag2, boolean flag1Value, boolean flag2Value) {
			return combineBulk(flag1, flag2) == 1;
		}
		
	}
	
}
