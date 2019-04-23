package me.mrletsplay.mrcore.misc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlagCompound {
	
	private long compound;
	
	/**
	 * Creates an empty flag compound
	 */
	public FlagCompound() {
		this.compound = 0;
	}
	
	/**
	 * Creates a flag compound including all the given compounds (combined using OR)
	 * @param compound The compounds to be included in this compound
	 */
	public FlagCompound(long... compound) {
		for(long l : compound) {
			this.compound |= l;
		}
	}
	
	/**
	 * Creates a flag compound including all the given flags
	 * @param flags The flags to be included in this compound
	 */
	public FlagCompound(Flag... flags) {
		for(Flag l : flags) {
			this.compound |= l.getValue();
		}
	}

	/**
	 * Creates a flag compound including all the given flags (combined using OR)
	 * @param flags The flags to be included in this compound
	 */
	public FlagCompound(FlagCompound... compound) {
		for(FlagCompound l : compound) {
			this.compound |= l.compound;
		}
	}
	
	/**
	 * Creates a flag compound from the given compound
	 * @param compound The compound to include in this compound
	 */
	public FlagCompound(long compound) {
		this.compound = compound;
	}
	
	/**
	 * Adds a flag (represented by a long) to this compound
	 * @param flag The flag to add
	 * @return This flag compound
	 */
	public FlagCompound addFlag(long flag) {
		this.compound |= flag;
		return this;
	}

	/**
	 * Adds a flag to this compound
	 * @param flag The flag to add
	 * @return This flag compound
	 */
	public FlagCompound addFlag(Flag flag) {
		this.compound |= flag.getValue();
		return this;
	}

	/**
	 * Adds all flags of another flag compound to this compound
	 * @param flag The flag to add
	 * @return This flag compound
	 */
	public FlagCompound addFlags(FlagCompound flag) {
		this.compound |= flag.compound;
		return this;
	}
	
	/**
	 * Removes a flag (represented by a long) from this compound
	 * @param flag The flag to remove
	 * @return This flag compound
	 */
	public FlagCompound removeFlag(long flag) {
		this.compound ^= (this.compound & flag);
		return this;
	}

	/**
	 * Removes a flag from this compound
	 * @param flag The flag to remove
	 * @return This flag compound
	 */
	public FlagCompound removeFlag(Flag flag) {
		this.compound ^= (this.compound & flag.getValue());
		return this;
	}

	/**
	 * Removes all flags of another flag compound from this compound
	 * @param flag The flag to remove
	 * @return This flag compound
	 */
	public FlagCompound removeFlags(FlagCompound flag) {
		this.compound ^= (this.compound | flag.compound);
		return this;
	}
	
	/**
	 * Checks whether a flag (represented by a long) is set in this flag compound
	 * @param flag The flag to check
	 * @return Whether that flag is set
	 */
	public boolean hasFlag(long flag) {
		return (compound & flag) == flag;
	}

	/**
	 * Checks whether a flag is set in this flag compound
	 * @param flag The flag to check
	 * @return Whether that flag is set
	 */
	public boolean hasFlag(Flag flag) {
		return (compound & flag.getValue()) == flag.getValue();
	}

	/**
	 * Checks whether all of the specified flags are set in this flag compound
	 * @param flags The flags to check
	 * @return Whether all of the flags are set
	 */
	public boolean hasFlags(Flag... flags) {
		return Arrays.stream(flags).allMatch(this::hasFlags);
	}
	
	/**
	 * Returns all flags from a given list that are set in this compound
	 * @param flags The flags to check against
	 * @return A sub list of all the flags that are set in this compound
	 */
	public List<? extends Flag> getApplicableFlags(List<? extends Flag> flags) {
		return flags.stream().filter(this::hasFlag).collect(Collectors.toList());
	}

	/**
	 * Returns all flags from a given array that are set in this compound
	 * @param flags The flags to check against
	 * @return A sub list of all the flags that are set in this compound
	 */
	public List<Flag> getApplicableFlags(Flag... flags) {
		return Arrays.stream(flags).filter(this::hasFlag).collect(Collectors.toList());
	}
	
	/**
	 * Sets the compound for this flag compound
	 * @param compound The compound to set to
	 */
	public void setCompound(long compound) {
		this.compound = compound;
	}
	
	/**
	 * Returns the compound for this flag compound
	 * @return The compound for this flag compound
	 */
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
	
	/**
	 * Combines two flags with the given combination mode
	 * @param f1 A flag
	 * @param f2 Another flag
	 * @param mode The combination mode
	 * @return The flags combined according to the given combination code
	 */
	public static FlagCompound combine(FlagCompound f1, FlagCompound f2, FlagCombinationMode mode) {
		return new FlagCompound(mode.combineBulk(f1.compound, f2.compound));
	}

	public static interface Flag {
		
		public String getName();
		
		public long getValue();
		
		public default boolean hasName() {
			return getName() != null;
		}
		
	}
	
	public static interface EnumFlag<E extends Enum<E> & Flag> extends Flag {
		
		public E get();
		
		@Override
		public default String getName() {
			return get().name();
		}
		
		@Override
		default long getValue() {
			return 1 << get().ordinal();
		}
		
	}
	
	/**
	 * Defines default {@link FlagCombinationMode}s
	 * @author MrLetsplay2003
	 */
	public static enum CombinationMode implements FlagCombinationMode {
		
		AND((BulkFlagCombinationMode) (a, b) -> a & b),
		OR((BulkFlagCombinationMode) (a, b) -> a | b),
		XOR((BulkFlagCombinationMode) (a, b) -> a ^ b),
		;
		
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
