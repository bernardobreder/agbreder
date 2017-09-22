package com.agentenv.compiler.linker;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe que possui todos os opcodes para fazer a geração
 * 
 * @author bernardobreder
 * 
 */
public class AgeLinker {

	/** OutputStream */
	private final DataOutputStream output;
	/** */
	private boolean isFirstRegister = true;
	/** */
	private boolean isLeftRegister = true;

	/**
	 * Construtor
	 * 
	 * @param output
	 */
	public AgeLinker(DataOutputStream output) {
		this.output = output;
	}

	/**
	 * Opcode de carga de constante numérico
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void loadConstant(int index) throws IOException {
		if (this.isLeftRegister) {
			if (this.isFirstRegister) {
				this.write(AgeOpcode.LOAD_NUMBER_LEFT_A);
			} else {
				this.write(AgeOpcode.LOAD_NUMBER_LEFT_B);
			}
		} else {
			if (this.isFirstRegister) {
				this.write(AgeOpcode.LOAD_NUMBER_RIGHT_A);
			} else {
				this.write(AgeOpcode.LOAD_NUMBER_RIGHT_B);
			}
		}
		this.write(index);
	}

	public void sumNumber() throws IOException {
		if (this.isLeftRegister) {
			if (this.isFirstRegister) {
				this.write(AgeOpcode.SUM_NUMBER_LEFT_A);
			} else {
				this.write(AgeOpcode.SUM_NUMBER_LEFT_B);
			}
		} else {
			if (this.isFirstRegister) {
				this.write(AgeOpcode.SUM_NUMBER_RIGHT_A);
			} else {
				this.write(AgeOpcode.SUM_NUMBER_RIGHT_B);
			}
		}
	}

	public void mulNumber() throws IOException {
		if (this.isLeftRegister) {
			if (this.isFirstRegister) {
				this.write(AgeOpcode.MUL_NUMBER_LEFT_A);
			} else {
				this.write(AgeOpcode.MUL_NUMBER_LEFT_B);
			}
		} else {
			if (this.isFirstRegister) {
				this.write(AgeOpcode.MUL_NUMBER_RIGHT_A);
			} else {
				this.write(AgeOpcode.MUL_NUMBER_RIGHT_B);
			}
		}
	}

	/**
	 * Opcode de return
	 * 
	 * @throws IOException
	 */
	public void returnLeft() throws IOException {
		this.write(AgeOpcode.RETURN_LEFT_A);
	}

	/**
	 * Escreve na saida
	 * 
	 * @param value
	 * @throws IOException
	 */
	private void write(int value) throws IOException {
		this.output.writeInt(value);
	}

	/**
	 * @return the isFirstRegister
	 */
	public boolean isFirstRegister() {
		return isFirstRegister;
	}

	/**
	 * @param isFirstRegister
	 *            the isFirstRegister to set
	 */
	public AgeLinker setFirstRegister(boolean isFirstRegister) {
		this.isFirstRegister = isFirstRegister;
		return this;
	}

	/**
	 * @return the isLeftRegister
	 */
	public boolean isLeftRegister() {
		return isLeftRegister;
	}

	/**
	 * @param isLeftRegister
	 *            the isLeftRegister to set
	 */
	public AgeLinker setLeftRegister(boolean isLeftRegister) {
		this.isLeftRegister = isLeftRegister;
		return this;
	}

}
