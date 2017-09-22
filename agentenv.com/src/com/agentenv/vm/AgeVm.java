package com.agentenv.vm;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.agentenv.compiler.linker.AgeOpcode;

/**
 * Máquina virtual da linguagem Age
 * 
 * @author bernardobreder
 * 
 */
public class AgeVm {

	/** Publico */
	private final AgeVmInterface library;
	/** Instruções */
	private final int[] opcodes;
	/** Números */
	private final Double[] numbers;

	/**
	 * Construtor
	 * 
	 * @param input
	 * @throws IOException
	 */
	public AgeVm(InputStream input, AgeVmInterface library) throws IOException {
		this.library = library;
		DataInputStream in = new DataInputStream(input);
		{
			int size = in.readInt();
			this.numbers = new Double[size];
			for (int n = 0; n < size; n++) {
				String utf = in.readUTF();
				this.numbers[n] = new Double(utf);
			}
		}
		{
			int size = in.readInt();
			this.opcodes = new int[size];
			for (int n = 0; n < size; n++) {
				this.opcodes[n] = in.readInt();
			}
		}
		in.close();
	}

	/**
	 * Executa a máquina virtual
	 * 
	 * @param pc
	 * @return
	 */
	public Object execute(int pc) {
		// Object regReturn = null;
		Object regA, regB, regC, regD, regE;
		Object regF, regG, regH, regI, regJ;
		Object regK, regL, regM, regN, regO;
		Object regP, regQ, regR, regS, regT;
		Object regU, regV, regX, regY, regZ;
		Object regLeft, regRight;
		Object regLeftA = null;
		Object regLeftB = null;
		Object regRightA = null;
		Object regRightB = null;
		int[] opcodes = this.opcodes;
		Double[] numbers = this.numbers;
		for (;;) {
			switch (opcodes[pc++]) {
			case AgeOpcode.LOAD_NUMBER_LEFT_A: {
				regLeftA = numbers[opcodes[pc++]];
				break;
			}
			case AgeOpcode.LOAD_NUMBER_LEFT_B: {
				regLeftB = numbers[opcodes[pc++]];
				break;
			}
			case AgeOpcode.LOAD_NUMBER_RIGHT_A: {
				regRightA = numbers[opcodes[pc++]];
				break;
			}
			case AgeOpcode.LOAD_NUMBER_RIGHT_B: {
				regRightB = numbers[opcodes[pc++]];
				break;
			}
			case AgeOpcode.SUM_NUMBER_LEFT_A: {
				regLeftA = (Double) regLeftA + (Double) regLeftB;
				break;
			}
			case AgeOpcode.SUM_NUMBER_LEFT_B: {
				regLeftB = (Double) regLeftA + (Double) regLeftB;
				break;
			}
			case AgeOpcode.SUM_NUMBER_RIGHT_A: {
				regRightA = (Double) regRightA + (Double) regRightB;
				break;
			}
			case AgeOpcode.SUM_NUMBER_RIGHT_B: {
				regRightB = (Double) regRightA + (Double) regRightB;
				break;
			}
			case AgeOpcode.MUL_NUMBER_LEFT_A: {
				regLeftA = (Double) regLeftA * (Double) regLeftB;
				break;
			}
			case AgeOpcode.MUL_NUMBER_LEFT_B: {
				regLeftB = (Double) regLeftA * (Double) regLeftB;
				break;
			}
			case AgeOpcode.MUL_NUMBER_RIGHT_A: {
				regRightA = (Double) regRightA * (Double) regRightB;
				break;
			}
			case AgeOpcode.MUL_NUMBER_RIGHT_B: {
				regRightB = (Double) regRightA * (Double) regRightB;
				break;
			}
			case AgeOpcode.RETURN_LEFT_A: {
				return regLeftA;
			}
			case AgeOpcode.RETURN_LEFT_B: {
				return regLeftB;
			}
			case AgeOpcode.CONSOLE_PRINT_NUMBER: {
				String text = ((Double) regLeftA).toString();
				for (int n = 0; n < text.length(); n++) {
					this.library.log(text.charAt(n));
				}
				break;
			}
			default: {
				throw new IllegalStateException("wrong opcode exception");
			}
			}
		}
	}
}
