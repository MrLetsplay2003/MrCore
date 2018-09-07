package me.mrletsplay.mrcore.misc;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.FlagCompound.Flag;

public class EnumFlagCompound<E extends Enum<E> & Flag> extends FlagCompound {

	public Class<E> clazz;
	
	@SafeVarargs
	private EnumFlagCompound(Class<E> clazz, E... es) {
		super(Arrays.stream(es).toArray(Flag[]::new));
		this.clazz = clazz;
	}
	
	private EnumFlagCompound(Class<E> clazz, long compound) {
		super(compound);
		this.clazz = clazz;
	}
	
	public List<E> getApplicable() {
		return Arrays.stream(getEnumValues(clazz)).filter(f -> hasFlag(f)).collect(Collectors.toList());
	}
	
	public List<E> getNonApplicable() {
		return Arrays.stream(getEnumValues(clazz)).filter(f -> !hasFlag(f)).collect(Collectors.toList());
	}
	
	public static <E extends Enum<E> & Flag> EnumFlagCompound<E> noneOf(Class<E> clazz) {
		return new EnumFlagCompound<>(clazz);
	}
	
	public static <E extends Enum<E> & Flag> EnumFlagCompound<E> allOf(Class<E> clazz) {
		return new EnumFlagCompound<>(clazz, getEnumValues(clazz));
	}
	
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <E extends Enum<E> & Flag> EnumFlagCompound<E> of(E first, E... rest) {
		EnumFlagCompound<E> compound = new EnumFlagCompound<>((Class<E>) first.getClass(), rest);
		compound.addFlag((Flag) first);
		return compound;
	}
	
	public static <E extends Enum<E> & Flag> EnumFlagCompound<E> of(Class<E> clazz, FlagCompound compound) {
		return new EnumFlagCompound<>(clazz, compound.getCompound());
	}
	
	public static <E extends Enum<E> & Flag> EnumFlagCompound<E> of(Class<E> clazz, long compound) {
		return new EnumFlagCompound<>(clazz, compound);
	}
	
	@SuppressWarnings("unchecked")
	private static <E extends Enum<E>> E[] getEnumValues(Class<E> enumClass) {
		try {
			return (E[]) enumClass.getMethod("values").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new FriendlyException(e);
		}
	}
	
}
