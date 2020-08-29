package me.mrletsplay.mrcore.misc.classfile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class ByteCode {

	private byte[] bytes;
	
	public ByteCode(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public List<Instruction> getInstructions() {
		List<Instruction> is = new ArrayList<>();
		for(int i = 0; i < bytes.length; i++) {
			Instruction in = Instruction.getInstruction(bytes[i] & 0xff);
			i += in.getByteCountFunction().apply(bytes, i);
			is.add(in);
		}
		return is;
	}
	
	public List<InstructionInformation> parseCode() {
		List<InstructionInformation> is = new ArrayList<>();
		for(int i = 0; i < bytes.length; i++) {
			Instruction in = Instruction.getInstruction(bytes[i] & 0xff);
			if(in == null) throw new FriendlyException("Unknown instruction with byte code " + (bytes[i] & 0xff) + " at offset " + i);
			int j = in.getByteCountFunction().apply(bytes, i);
			byte[] inf = new byte[j < 0 ? 12 : j];
			if(j < 0 || j > 500) {
				byte[] inf2 = new byte[12];
				System.arraycopy(bytes, i, inf2, 0, 12);
			}
			System.arraycopy(bytes, i + 1, inf, 0, j);
			i += j;
			is.add(new InstructionInformation(in, inf));
		}
		return is;
	}
	
	public void replace(ByteCode code) {
		bytes = code.getBytes();
	}
	
	public void append(ByteCode code) {
		byte[] nBytes = new byte[bytes.length + code.getBytes().length];
		System.arraycopy(bytes, 0, nBytes, 0, bytes.length);
		System.arraycopy(code.getBytes(), 0, nBytes, bytes.length, code.getBytes().length);
		bytes = nBytes;
	}
	
	public static ByteCode of(byte[] bytes) {
		return new ByteCode(bytes);
	}
	
	public static ByteCode of(List<InstructionInformation> code) {
		int sz = code.stream().mapToInt(InstructionInformation::getSize).sum();
		byte[] bytes = new byte[sz];
		int i = 0;
		for(InstructionInformation inf : code) {
			bytes[i++] = (byte) inf.getInstruction().getByteValue();
			System.arraycopy(inf.getInformation(), 0, bytes, i, inf.getInformation().length);
			i += inf.getInformation().length;
		}
		return of(bytes);
	}
	
	@Override
	public String toString() {
		return parseCode().stream().map(i -> i.toString()).collect(Collectors.joining("\n"));
	}
	
}
