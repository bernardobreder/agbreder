package com.agbreder.compiler.disassembler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.exception.BytecodeDesassemblerException;
import com.agbreder.compiler.exception.DesassemblerException;
import com.agbreder.compiler.exception.HeaderDesassemblerException;
import com.agbreder.compiler.link.OpcodeOutputStream;

/**
 * Classe responsável por realizar o desassembler
 * 
 * @author bernardobreder
 */
public class AGBDesassembler extends DataInputStream {

	/** Constantes de Numeros */
	private List<String> numbers;

	/** Constantes de Strings */
	private List<String> strings;

	/** Lista de Classes */
	private List<AGBClass> classes;

	/** Lista de bytecodes */
	private List<AGBInstruction> bytecodes;

	/** Número de bytecodes */
	private int opcodes;

	/**
	 * Construtor
	 * 
	 * @param url
	 * @throws IOException
	 */
	public AGBDesassembler(URL url) throws IOException {
		this(url.openStream());
	}

	/**
	 * Construtor
	 * 
	 * @param input
	 * @throws IOException
	 */
	public AGBDesassembler(InputStream input) throws IOException {
		super(input);
	}

	/**
	 * Realiza a leitura
	 * 
	 * @throws IOException
	 * @throws DesassemblerException
	 */
	public void readHeader() throws IOException, DesassemblerException {
		{
			if (this.readByte() != (byte) 0xBB) {
				throw new HeaderDesassemblerException();
			}
		}
		{
			this.numbers = new ArrayList<String>();
			this.strings = new ArrayList<String>();
			this.classes = new ArrayList<AGBClass>();
		}
		{
			int size = this.data();
			for (int n = 0; n < size; n++) {
				String value = this.readUTF();
				this.numbers.add(value);
			}
		}
		{
			int size = this.data();
			for (int n = 0; n < size; n++) {
				String value = this.readUTF();
				this.strings.add(value);
			}
		}
		{
			int size = this.data();
			for (int n = 0; n < size; n++) {
				String name = this.readUTF();
				int extendIndex = this.data();
				int fields = this.data();
				AGBClass item = new AGBClass(name, extendIndex, fields);
				this.classes.add(item);
			}
		}
		{
			int classSize = this.data();
			for (int m = 0; m < classSize; m++) {
				int msize = this.data();
				for (int n = 0; n < msize; n++) {
					String name = this.readUTF();
					boolean isStatic = this.data() != 0;
					int pc = this.data();
					int pcs = this.data();
					AGBMethod item = new AGBMethod(name, isStatic, pc, pcs);
					this.classes.get(m).getMethods().add(item);
				}
			}
		}
		if (this.readByte() != (byte) 255) {
			throw new HeaderDesassemblerException();
		}
	}

	/**
	 * Realiza a leitura do corpo do bytecode
	 * 
	 * @throws IOException
	 * @throws DesassemblerException
	 */
	public void readBody() throws IOException, DesassemblerException {
		int len = this.data();
		this.opcodes = 0;
		this.bytecodes = new ArrayList<AGBInstruction>(len);
		for (; this.opcodes < len;) {
			switch (this.data()) {
			case OpcodeOutputStream._AGB_GP_VM: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_VM_HALF:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_HALF));
					break;
				case OpcodeOutputStream._AGB_OP_VM_GC:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_GC));
					break;
				case OpcodeOutputStream._AGB_OP_VM_ALLOC:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_ALLOC));
					break;
				case OpcodeOutputStream._AGB_OP_VM_PAINT:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_PAINT));
					break;
				case OpcodeOutputStream._AGB_OP_VM_EVENT_KEY:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_EVENT_KEY));
					break;
				case OpcodeOutputStream._AGB_OP_VM_EVENT_MOUSE:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_EVENT_MOUSE));
					break;
				case OpcodeOutputStream._AGB_OP_VM_EVENT_WHEEL:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_EVENT_WHEEL));
					break;
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_STACK: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_STACK_PUSH:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_PUSH, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_POP:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_POP, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_LOAD:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_LOAD, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_STORE:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_STORE, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_PRE_INC:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_PRE_INC, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_PRE_DEC:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_PRE_DEC, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_POS_INC:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_POS_INC, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_POS_DEC:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_POS_DEC, this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_FOR_INC_BEGIN:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_FOR_INC_BEGIN, this.data(), this.data()));
					break;
				case OpcodeOutputStream._AGB_OP_STACK_FOR_INC_END:
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STACK, OpcodeOutputStream._AGB_OP_STACK_FOR_INC_END, this.data(), this.data()));
					break;
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_LOAD: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_LOAD_TRUE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_LOAD, OpcodeOutputStream._AGB_OP_LOAD_TRUE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_LOAD_FALSE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_LOAD, OpcodeOutputStream._AGB_OP_LOAD_FALSE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_LOAD_NUM: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_LOAD, OpcodeOutputStream._AGB_OP_LOAD_NUM, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_LOAD_STR: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_LOAD, OpcodeOutputStream._AGB_OP_LOAD_STR, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_LOAD_NULL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_LOAD, OpcodeOutputStream._AGB_OP_LOAD_NULL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_LOAD_NEW: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_LOAD, OpcodeOutputStream._AGB_OP_LOAD_NEW, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_LOAD_FUNC: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_LOAD, OpcodeOutputStream._AGB_OP_LOAD_FUNC, this.data(), this.data()));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_JUMP: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_JUMP_INT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_INT, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_TRUE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_TRUE, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_FALSE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_FALSE, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_CALL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_CALL, this.data(), this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_STATIC_CALL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_STATIC_CALL, this.data(), this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_RETURN: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_RETURN, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_TRUE_DUP: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_TRUE_DUP, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_FALSE_DUP: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_FALSE_DUP, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_JUMP_FUNC_CALL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_JUMP, OpcodeOutputStream._AGB_OP_JUMP_FUNC_CALL));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_OBJECT: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_OBJECT_CAST_CLASS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_CAST_CLASS, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_OBJECT_GET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_GET, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_OBJECT_SET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_SET, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_OBJECT_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_EQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_OBJECT_NOTEQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_NOTEQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_OBJECT_HASHCODE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_HASHCODE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_OBJECT_CLASSNAME: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_CLASSNAME));
					break;
				}
				case OpcodeOutputStream._AGB_OP_OBJECT_CAST_NATIVE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_CAST_NATIVE, this.data()));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_BOOL: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_BOOL_OR: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOL, OpcodeOutputStream._AGB_OP_BOOL_OR));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOL_AND: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOL, OpcodeOutputStream._AGB_OP_BOOL_AND));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOL_NOT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOL, OpcodeOutputStream._AGB_OP_BOOL_NOT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOL_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOL, OpcodeOutputStream._AGB_OP_BOOL_EQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOL_NEQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOL, OpcodeOutputStream._AGB_OP_BOOL_NEQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOL_TO_STR: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOL, OpcodeOutputStream._AGB_OP_BOOL_TO_STR));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_NUM: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_NUM_NEG: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_NEG));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_SUM: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_SUM));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_SUB: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_SUB));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_MUL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_MUL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_DIV: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_DIV));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_MOD: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_MOD));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_EQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_NEQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_NEQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_GT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_GT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_EGT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_EGT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_LT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_LT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_ELT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_ELT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_TO_STR: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_TO_STR));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_AND_BIT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_AND_BIT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_OR_BIT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_OR_BIT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_SHIFT_LEFT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_SHIFT_LEFT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_SHIFT_RIGHT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_SHIFT_RIGHT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUM_TO_INT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_TO_INT));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_STR: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_STR_LEN: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_LEN));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_SUM: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_SUM));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_EQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_NEQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_NEQUAL));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_GT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_GT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_EGT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_EGT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_LT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_LT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_ELT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_ELT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_SUBSTRING: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_SUBSTRING));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_CODE_TO_CHAR: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_SUBSTRING));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_CHAR_TO_CODE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_SUBSTRING));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_TRIM: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_TRIM));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_CHAR_AT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_CHAR_AT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_START_WITH: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_START_WITH));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_END_WITH: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_END_WITH));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_INDEX_OF: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_INDEX_OF));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_LAST_INDEX_OF: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_LAST_INDEX_OF));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_REPLACE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_REPLACE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_TO_LOWER_CASE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_TO_LOWER_CASE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_TO_UPPER_CASE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_TO_UPPER_CASE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_INDEX_OF_N: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_INDEX_OF_N));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_BASE64_ENCODE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_BASE64_ENCODE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_BASE64_DECODE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_BASE64_DECODE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_UTF_ENCODE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_UTF_ENCODE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_STR_UTF_DECODE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_UTF_DECODE));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_THROW: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_THROW_TRY: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_THROW, OpcodeOutputStream._AGB_OP_THROW_TRY, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_THROW_OBJECT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_THROW, OpcodeOutputStream._AGB_OP_THROW_OBJECT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_THROW_TRUE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_THROW, OpcodeOutputStream._AGB_OP_THROW_TRUE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_THROW_FALSE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_THROW, OpcodeOutputStream._AGB_OP_THROW_FALSE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_THROW_STORE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_THROW, OpcodeOutputStream._AGB_OP_THROW_STORE, this.data()));
					break;
				}
				case OpcodeOutputStream._AGB_OP_THROW_JUMP: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_THROW, OpcodeOutputStream._AGB_OP_THROW_JUMP, this.data(), this.data()));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_ARRAY: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_ARRAY_NEW: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_NEW));
					break;
				}
				case OpcodeOutputStream._AGB_OP_ARRAY_LEN: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_LEN));
					break;
				}
				case OpcodeOutputStream._AGB_OP_ARRAY_ADD: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_ADD));
					break;
				}
				case OpcodeOutputStream._AGB_OP_ARRAY_INSERT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_INSERT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_ARRAY_GET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_GET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_ARRAY_SET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_SET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_ARRAY_REM: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_REM));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_INTS: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_INTS_NEW: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_INTS, OpcodeOutputStream._AGB_OP_INTS_NEW));
					break;
				}
				case OpcodeOutputStream._AGB_OP_INTS_SIZE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_INTS, OpcodeOutputStream._AGB_OP_INTS_SIZE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_INTS_GET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_INTS, OpcodeOutputStream._AGB_OP_INTS_GET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_INTS_SET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_INTS, OpcodeOutputStream._AGB_OP_INTS_SET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_INTS_SETS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_INTS, OpcodeOutputStream._AGB_OP_INTS_SETS));
					break;
				}
				case OpcodeOutputStream._AGB_OP_INTS_COPY: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_INTS, OpcodeOutputStream._AGB_OP_INTS_COPY));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_NUMS: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_NUMS_NEW: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_NEW));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUMS_SIZE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_SIZE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUMS_GET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_GET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUMS_SET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_SET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUMS_SETS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_SETS));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUMS_COPY: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_COPY));
					break;
				}
				case OpcodeOutputStream._AGB_OP_NUMS_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_EQUAL));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_BOOLS: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_BOOLS_NEW: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_NEW));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOLS_SIZE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_SIZE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOLS_GET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_GET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOLS_SET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_SET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOLS_SETS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_SETS));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOLS_COPY: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_COPY));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BOOLS_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_EQUAL));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_BYTES: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_BYTES_NEW: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_NEW));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BYTES_SIZE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_SIZE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BYTES_GET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_GET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BYTES_SET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_SET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BYTES_SETS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_SETS));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BYTES_COPY: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_COPY));
					break;
				}
				case OpcodeOutputStream._AGB_OP_BYTES_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_EQUAL));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_UINT2D: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_UINT2D_NEW: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_NEW));
					break;
				}
				case OpcodeOutputStream._AGB_OP_UINT2D_LINS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_LINS));
					break;
				}
				case OpcodeOutputStream._AGB_OP_UINT2D_COLS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_COLS));
					break;
				}
				case OpcodeOutputStream._AGB_OP_UINT2D_GET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_GET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_UINT2D_SET: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_SET));
					break;
				}
				case OpcodeOutputStream._AGB_OP_UINT2D_SETS: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_SETS));
					break;
				}
				case OpcodeOutputStream._AGB_OP_UINT2D_COPY: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_COPY));
					break;
				}
				case OpcodeOutputStream._AGB_OP_UINT2D_EQUAL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_EQUAL));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_CONSOLE: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_STR: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_CONSOLE, OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_STR));
					break;
				}
				case OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_NUM: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_CONSOLE, OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_NUM));
					break;
				}
				case OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_BOOL: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_CONSOLE, OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_BOOL));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_SDL: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_SDL_ERROR: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_ERROR));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_INIT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_INIT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_QUIT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_QUIT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_VIDEO: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_VIDEO));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_CONSTANT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_CONSTANT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_VIDEO_WIDTH: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_VIDEO_WIDTH));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_VIDEO_HEIGHT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_VIDEO_HEIGHT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_SCREEN_WIDTH: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_SCREEN_WIDTH));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_SCREEN_HEIGHT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_SCREEN_HEIGHT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_POLL_EVENT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_POLL_EVENT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_WAIT_EVENT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_WAIT_EVENT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_TYPE_EVENT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_TYPE_EVENT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_KEYCODE_EVENT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_KEYCODE_EVENT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_KEYCHAR_EVENT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_KEYCHAR_EVENT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_LOCK: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_LOCK));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_UNLOAD: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_UNLOAD));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_UPDATE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_UPDATE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_TICK: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_TICK));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_DELAY: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DELAY));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_REPAINT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_REPAINT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_USERCODE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_USERCODE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_X: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_X));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_Y: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_Y));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_X: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_X));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_Y: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_Y));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_X: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_X));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_Y: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_Y));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_DRAW_RECT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DRAW_RECT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_DRAW_CIRCLE: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DRAW_CIRCLE));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_DRAW_STRING: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DRAW_STRING));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_FONT_WIDTH: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_FONT_WIDTH));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_FONT_HEIGHT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_FONT_HEIGHT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_FILL_RECT: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_FILL_RECT));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_PUSH_GRAPHIC: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_PUSH_GRAPHIC));
					break;
				}
				case OpcodeOutputStream._AGB_OP_SDL_POP_GRAPHIC: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_POP_GRAPHIC));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			case OpcodeOutputStream._AGB_GP_NET: {
				switch (this.data()) {
				case OpcodeOutputStream._AGB_OP_NET_REQUEST: {
					this.bytecodes.add(new AGBInstruction(OpcodeOutputStream._AGB_GP_NET, OpcodeOutputStream._AGB_OP_NET_REQUEST));
					break;
				}
				default: {
					throw new BytecodeDesassemblerException();
				}
				}
				break;
			}
			default: {
				throw new BytecodeDesassemblerException();
			}
			}
		}
		if (this.readByte() != (byte) 255) {
			throw new HeaderDesassemblerException();
		}
	}

	/**
	 * Realiza a leitura de um inteiro de 32 bits
	 * 
	 * @return valor da leitura
	 * @throws IOException
	 */
	public int data() throws IOException {
		int value = this.readInt();
		this.opcodes++;
		return value;
	}

	/**
	 * @return the numbers
	 */
	public List<String> getNumbers() {
		return numbers;
	}

	/**
	 * @return the strings
	 */
	public List<String> getStrings() {
		return strings;
	}

	/**
	 * @return the classes
	 */
	public List<AGBClass> getClasses() {
		return classes;
	}

	/**
	 * @return the bytecodes
	 */
	public List<AGBInstruction> getBytecodes() {
		return bytecodes;
	}

}
