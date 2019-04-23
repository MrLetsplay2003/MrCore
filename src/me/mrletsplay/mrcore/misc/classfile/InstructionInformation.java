package me.mrletsplay.mrcore.misc.classfile;

import java.util.Arrays;

public class InstructionInformation {

	private Instruction instruction;
	private byte[] information;
	
	public InstructionInformation(Instruction instruction, byte[] information) {
		this.instruction = instruction;
		this.information = information;
	}
	
	public InstructionInformation(Instruction instruction, byte information) {
		this(instruction, new byte[] {information});
	}
	
	public InstructionInformation(Instruction instruction) {
		this(instruction, new byte[0]);
	}
	
	public Instruction getInstruction() {
		return instruction;
	}
	
	public byte[] getInformation() {
		return information;
	}
	
	public int getSize() {
		return information.length + 1;
	}
	
	@Override
	public String toString() {
		return instruction.toString() + " " + Arrays.toString(information);
	}
	
}
