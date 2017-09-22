package com.agbreder.compiler.link;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.agbreder.compiler.exception.AGBCastException;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.parser.node.AGBRValueNode;
import com.agbreder.compiler.parser.node.type.AGBStringTypeNode;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Indica todos os opcodes
 * 
 * @author bernardobreder
 */
public class OpcodeOutputStream extends DataOutputStream {

	/** Opcode */
	public static final int _AGB_GP_VM = 1;

	/** Opcode */
	public static final int _AGB_GP_STACK = 2;

	/** Opcode */
	public static final int _AGB_GP_OBJECT = 3;

	/** Opcode */
	public static final int _AGB_GP_LOAD = 4;

	/** Opcode */
	public static final int _AGB_GP_JUMP = 5;

	/** Opcode */
	public static final int _AGB_GP_BOOL = 6;

	/** Opcode */
	public static final int _AGB_GP_INT = 7;

	/** Opcode */
	public static final int _AGB_GP_NUM = 8;

	/** Opcode */
	public static final int _AGB_GP_THROW = 9;

	/** Opcode */
	public static final int _AGB_GP_USER = 32;

	/** Opcode */
	public static final int _AGB_GP_CONSOLE = 32 + 0;

	/** Opcode */
	public static final int _AGB_GP_STR = _AGB_GP_USER + 1;

	/** Opcode */
	public static final int _AGB_GP_ARRAY = _AGB_GP_USER + 2;

	/** Opcode */
	public static final int _AGB_GP_MAP = _AGB_GP_USER + 3;

	/** Opcode */
	public static final int _AGB_GP_SDL = _AGB_GP_USER + 4;

	/** Opcode */
	public static final int _AGB_GP_NET = _AGB_GP_USER + 5;

	/** Opcode */
	public static final int _AGB_GP_INTS = _AGB_GP_USER + 6;

	/** Opcode */
	public static final int _AGB_GP_NUMS = _AGB_GP_USER + 7;

	/** Opcode */
	public static final int _AGB_GP_BOOLS = _AGB_GP_USER + 8;

	/** Opcode */
	public static final int _AGB_GP_BYTES = _AGB_GP_USER + 9;

	/** Opcode */
	public static final int _AGB_GP_UINT2D = _AGB_GP_USER + 10;

	/** Opcode */
	public static final int _AGB_OP_VM_HALF = 1;

	/** Opcode */
	public static final int _AGB_OP_VM_GC = 2;

	/** Opcode */
	public static final int _AGB_OP_VM_ALLOC = 3;

	/** Opcode */
	public static final int _AGB_OP_VM_PAINT = 4;

	/** Opcode */
	public static final int _AGB_OP_VM_EVENT_KEY = 5;

	/** Opcode */
	public static final int _AGB_OP_VM_EVENT_MOUSE = 6;

	/** Opcode */
	public static final int _AGB_OP_VM_EVENT_WHEEL = 7;

	/** Opcode */
	public static final int _AGB_OP_STACK_PUSH = 1;

	/** Opcode */
	public static final int _AGB_OP_STACK_POP = 2;

	/** Opcode */
	public static final int _AGB_OP_STACK_LOAD = 3;

	/** Opcode */
	public static final int _AGB_OP_STACK_STORE = 4;

	/** Opcode */
	public static final int _AGB_OP_STACK_PRE_INC = 5;

	/** Opcode */
	public static final int _AGB_OP_STACK_PRE_DEC = 6;

	/** Opcode */
	public static final int _AGB_OP_STACK_POS_INC = 7;

	/** Opcode */
	public static final int _AGB_OP_STACK_POS_DEC = 8;

	/** Opcode */
	public static final int _AGB_OP_STACK_FOR_INC_BEGIN = 9;

	/** Opcode */
	public static final int _AGB_OP_STACK_FOR_INC_END = 10;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_CAST_CLASS = 1;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_GET = 2;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_SET = 3;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_EQUAL = 4;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_NOTEQUAL = 5;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_HASHCODE = 6;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_CLASSNAME = 7;

	/** Opcode */
	public static final int _AGB_OP_OBJECT_CAST_NATIVE = 8;

	/** Opcode */
	public static final int _AGB_OP_LOAD_TRUE = 1;

	/** Opcode */
	public static final int _AGB_OP_LOAD_FALSE = 2;

	/** Opcode */
	public static final int _AGB_OP_LOAD_NUM = 4;

	/** Opcode */
	public static final int _AGB_OP_LOAD_STR = 5;

	/** Opcode */
	public static final int _AGB_OP_LOAD_NULL = 6;

	/** Opcode */
	public static final int _AGB_OP_LOAD_FUNC = 7;

	/** Opcode */
	public static final int _AGB_OP_LOAD_NEW = 8;

	/** Opcode */
	public static final int _AGB_OP_JUMP_INT = 2;

	/** Opcode */
	public static final int _AGB_OP_JUMP_TRUE = 3;

	/** Opcode */
	public static final int _AGB_OP_JUMP_FALSE = 4;

	/** Opcode */
	public static final int _AGB_OP_JUMP_CALL = 5;

	/** Opcode */
	public static final int _AGB_OP_JUMP_STATIC_CALL = 6;

	/** Opcode */
	public static final int _AGB_OP_JUMP_RETURN = 7;

	/** Opcode */
	public static final int _AGB_OP_JUMP_TRUE_DUP = 8;

	/** Opcode */
	public static final int _AGB_OP_JUMP_FALSE_DUP = 9;

	/** Opcode */
	public static final int _AGB_OP_JUMP_FUNC_CALL = 10;

	/** Opcode */
	public static final int _AGB_OP_BOOL_NOT = 1;

	/** Opcode */
	public static final int _AGB_OP_BOOL_OR = 2;

	/** Opcode */
	public static final int _AGB_OP_BOOL_AND = 3;

	/** Opcode */
	public static final int _AGB_OP_BOOL_EQUAL = 4;

	/** Opcode */
	public static final int _AGB_OP_BOOL_NEQUAL = 5;

	/** Opcode */
	public static final int _AGB_OP_BOOL_TO_STR = 6;

	/** Opcode */
	public static final int _AGB_OP_NUM_NEG = 1;

	/** Opcode */
	public static final int _AGB_OP_NUM_SUM = 2;

	/** Opcode */
	public static final int _AGB_OP_NUM_SUB = 3;

	/** Opcode */
	public static final int _AGB_OP_NUM_MUL = 4;

	/** Opcode */
	public static final int _AGB_OP_NUM_DIV = 5;

	/** Opcode */
	public static final int _AGB_OP_NUM_EQUAL = 6;

	/** Opcode */
	public static final int _AGB_OP_NUM_NEQUAL = 7;

	/** Opcode */
	public static final int _AGB_OP_NUM_GT = 8;

	/** Opcode */
	public static final int _AGB_OP_NUM_EGT = 9;

	/** Opcode */
	public static final int _AGB_OP_NUM_LT = 10;

	/** Opcode */
	public static final int _AGB_OP_NUM_ELT = 11;

	/** Opcode */
	public static final int _AGB_OP_NUM_TO_STR = 12;

	/** Opcode */
	public static final int _AGB_OP_NUM_MOD = 13;

	/** Opcode */
	public static final int _AGB_OP_NUM_AND_BIT = 14;

	/** Opcode */
	public static final int _AGB_OP_NUM_OR_BIT = 15;

	/** Opcode */
	public static final int _AGB_OP_NUM_SHIFT_LEFT = 16;

	/** Opcode */
	public static final int _AGB_OP_NUM_SHIFT_RIGHT = 17;

	/** Opcode */
	public static final int _AGB_OP_NUM_TO_INT = 18;

	/** Opcode */
	public static final int _AGB_OP_STR_LEN = 1;

	/** Opcode */
	public static final int _AGB_OP_STR_SUM = 2;

	/** Opcode */
	public static final int _AGB_OP_STR_EQUAL = 3;

	/** Opcode */
	public static final int _AGB_OP_STR_NEQUAL = 4;

	/** Opcode */
	public static final int _AGB_OP_STR_GT = 5;

	/** Opcode */
	public static final int _AGB_OP_STR_EGT = 6;

	/** Opcode */
	public static final int _AGB_OP_STR_LT = 7;

	/** Opcode */
	public static final int _AGB_OP_STR_ELT = 8;

	/** Opcode */
	public static final int _AGB_OP_STR_SUBSTRING = 9;

	/** Opcode */
	public static final int _AGB_OP_STR_CODE_TO_CHAR = 10;

	/** Opcode */
	public static final int _AGB_OP_STR_CHAR_TO_CODE = 11;

	/** Opcode */
	public static final int _AGB_OP_STR_TRIM = 12;

	/** Opcode */
	public static final int _AGB_OP_STR_CHAR_AT = 13;

	/** Opcode */
	public static final int _AGB_OP_STR_START_WITH = 14;

	/** Opcode */
	public static final int _AGB_OP_STR_END_WITH = 15;

	/** Opcode */
	public static final int _AGB_OP_STR_INDEX_OF = 16;

	/** Opcode */
	public static final int _AGB_OP_STR_LAST_INDEX_OF = 17;

	/** Opcode */
	public static final int _AGB_OP_STR_REPLACE = 18;

	/** Opcode */
	public static final int _AGB_OP_STR_TO_LOWER_CASE = 19;

	/** Opcode */
	public static final int _AGB_OP_STR_TO_UPPER_CASE = 20;

	/** Opcode */
	public static final int _AGB_OP_STR_INDEX_OF_N = 21;

	/** Opcode */
	public static final int _AGB_OP_STR_BASE64_ENCODE = 22;

	/** Opcode */
	public static final int _AGB_OP_STR_BASE64_DECODE = 23;

	/** Opcode */
	public static final int _AGB_OP_STR_UTF_ENCODE = 24;

	/** Opcode */
	public static final int _AGB_OP_STR_UTF_DECODE = 25;

	/** Opcode */
	public static final int _AGB_OP_THROW_TRY = 1;

	/** Opcode */
	public static final int _AGB_OP_THROW_OBJECT = 2;

	/** Opcode */
	public static final int _AGB_OP_THROW_TRUE = 3;

	/** Opcode */
	public static final int _AGB_OP_THROW_FALSE = 4;

	/** Opcode */
	public static final int _AGB_OP_THROW_STORE = 5;

	/** Opcode */
	public static final int _AGB_OP_THROW_JUMP = 6;

	/** Opcode */
	public static final int _AGB_OP_ARRAY_NEW = 1;

	/** Opcode */
	public static final int _AGB_OP_ARRAY_LEN = 2;

	/** Opcode */
	public static final int _AGB_OP_ARRAY_ADD = 3;

	/** Opcode */
	public static final int _AGB_OP_ARRAY_INSERT = 4;

	/** Opcode */
	public static final int _AGB_OP_ARRAY_GET = 5;

	/** Opcode */
	public static final int _AGB_OP_ARRAY_SET = 6;

	/** Opcode */
	public static final int _AGB_OP_ARRAY_REM = 7;

	/** Opcode */
	public static final int _AGB_OP_PRINT_STR = 1;

	/** Opcode */
	public static final int _AGB_OP_PRINT_NUM = 2;

	/** Opcode */
	public static final int _AGB_OP_PRINT_BOOL = 3;

	/** Grupo */
	public static final int _AGB_OP_CONSOLE_PRINT_STR = 1;

	/** Opcode */
	public static final int _AGB_OP_CONSOLE_PRINT_NUM = 2;

	/** Opcode */
	public static final int _AGB_OP_CONSOLE_PRINT_BOOL = 3;

	/** Opcode */
	public static final int _AGB_OP_SDL_ERROR = 1;

	/** Opcode */
	public static final int _AGB_OP_SDL_INIT = 2;

	/** Opcode */
	public static final int _AGB_OP_SDL_QUIT = 3;

	/** Opcode */
	public static final int _AGB_OP_SDL_VIDEO = 4;

	/** Opcode */
	public static final int _AGB_OP_SDL_POLL_EVENT = 5;

	/** Opcode */
	public static final int _AGB_OP_SDL_WAIT_EVENT = 6;

	/** Opcode */
	public static final int _AGB_OP_SDL_LOCK = 7;

	/** Opcode */
	public static final int _AGB_OP_SDL_UNLOAD = 8;

	/** Opcode */
	public static final int _AGB_OP_SDL_UPDATE = 9;

	/** Opcode */
	public static final int _AGB_OP_SDL_TICK = 10;

	/** Opcode */
	public static final int _AGB_OP_SDL_DELAY = 11;

	/** Opcode */
	public static final int _AGB_OP_SDL_VIDEO_WIDTH = 12;

	/** Opcode */
	public static final int _AGB_OP_SDL_VIDEO_HEIGHT = 13;

	/** Opcode */
	public static final int _AGB_OP_SDL_SCREEN_WIDTH = 14;

	/** Opcode */
	public static final int _AGB_OP_SDL_SCREEN_HEIGHT = 15;

	/** Opcode */
	public static final int _AGB_OP_SDL_CONSTANT = 16;

	/** Opcode */
	public static final int _AGB_OP_SDL_TYPE_EVENT = 20;

	/** Opcode */
	public static final int _AGB_OP_SDL_KEYCODE_EVENT = 21;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_REPAINT = 22;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_USERCODE = 23;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_MOUSE_MOTION_X = 24;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_MOUSE_MOTION_Y = 25;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_MOUSE_ACTION_X = 26;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_MOUSE_ACTION_Y = 27;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON = 28;

	/** Opcode */
	public static final int _AGB_OP_SDL_KEYCHAR_EVENT = 29;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_MOUSE_WHEEL_X = 30;

	/** Opcode */
	public static final int _AGB_OP_SDL_EVENT_MOUSE_WHEEL_Y = 31;

	/** Opcode */
	public static final int _AGB_OP_SDL_DRAW_RECT = 1024;

	/** Opcode */
	public static final int _AGB_OP_SDL_DRAW_CIRCLE = 1025;

	/** Opcode */
	public static final int _AGB_OP_SDL_DRAW_STRING = 1026;

	/** Opcode */
	public static final int _AGB_OP_SDL_FONT_WIDTH = 1027;

	/** Opcode */
	public static final int _AGB_OP_SDL_FONT_HEIGHT = 1028;

	/** Opcode */
	public static final int _AGB_OP_SDL_FILL_RECT = 1029;

	/** Opcode */
	public static final int _AGB_OP_SDL_PUSH_GRAPHIC = 1030;

	/** Opcode */
	public static final int _AGB_OP_SDL_POP_GRAPHIC = 1031;

	/** Opcode */
	public static final int _AGB_OP_SDL_MAX = 1030;

	/** Opcode */
	public static final int _AGB_OP_NET_REQUEST = 1;

	/** Opcode */
	public static final int _AGB_OP_INTS_NEW = 1;

	/** Opcode */
	public static final int _AGB_OP_INTS_SIZE = 2;

	/** Opcode */
	public static final int _AGB_OP_INTS_GET = 3;

	/** Opcode */
	public static final int _AGB_OP_INTS_SET = 4;

	/** Opcode */
	public static final int _AGB_OP_INTS_SETS = 5;

	/** Opcode */
	public static final int _AGB_OP_INTS_COPY = 6;

	/** Opcode */
	public static final int _AGB_OP_INTS_EQUAL = 7;

	/** Opcode */
	public static final int _AGB_OP_NUMS_NEW = 1;

	/** Opcode */
	public static final int _AGB_OP_NUMS_SIZE = 2;

	/** Opcode */
	public static final int _AGB_OP_NUMS_GET = 3;

	/** Opcode */
	public static final int _AGB_OP_NUMS_SET = 4;

	/** Opcode */
	public static final int _AGB_OP_NUMS_SETS = 5;

	/** Opcode */
	public static final int _AGB_OP_NUMS_COPY = 6;

	/** Opcode */
	public static final int _AGB_OP_NUMS_EQUAL = 7;

	/** Opcode */
	public static final int _AGB_OP_BOOLS_NEW = 1;

	/** Opcode */
	public static final int _AGB_OP_BOOLS_SIZE = 2;

	/** Opcode */
	public static final int _AGB_OP_BOOLS_GET = 3;

	/** Opcode */
	public static final int _AGB_OP_BOOLS_SET = 4;

	/** Opcode */
	public static final int _AGB_OP_BOOLS_SETS = 5;

	/** Opcode */
	public static final int _AGB_OP_BOOLS_COPY = 6;

	/** Opcode */
	public static final int _AGB_OP_BOOLS_EQUAL = 7;

	/** Opcode */
	public static final int _AGB_OP_BYTES_NEW = 1;

	/** Opcode */
	public static final int _AGB_OP_BYTES_SIZE = 2;

	/** Opcode */
	public static final int _AGB_OP_BYTES_GET = 3;

	/** Opcode */
	public static final int _AGB_OP_BYTES_SET = 4;

	/** Opcode */
	public static final int _AGB_OP_BYTES_SETS = 5;

	/** Opcode */
	public static final int _AGB_OP_BYTES_COPY = 6;

	/** Opcode */
	public static final int _AGB_OP_BYTES_EQUAL = 7;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_NEW = 1;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_LINS = 2;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_COLS = 3;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_GET = 4;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_SET = 5;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_SETS = 6;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_COPY = 7;

	/** Opcode */
	public static final int _AGB_OP_UINT2D_EQUAL = 8;

	/** Contador */
	private int counter;

	/** Contador */
	private int pc;

	/**
	 * Construtor
	 * 
	 * @param out
	 */
	public OpcodeOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param count
	 * @throws IOException
	 */
	public void opStackPush(int count) throws IOException {
		if (count > 0) {
			this.writeData(_AGB_GP_STACK);
			this.writeData(_AGB_OP_STACK_PUSH);
			this.writeData(count);
			this.counter += count;
		}
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param count
	 * @throws IOException
	 */
	public void opStackPop(int count) throws IOException {
		if (count > 0) {
			this.writeData(_AGB_GP_STACK);
			this.writeData(_AGB_OP_STACK_POP);
			this.writeData(count);
			this.counter -= count;
		}
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackLoad(int index) throws IOException {
		this.opStackLoadAbs(index + this.counter);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackLoadAbs(int index) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_LOAD);
		this.writeData(index);
		this.counter++;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackSave(int index) throws IOException {
		this.opStackSaveAbs(index + this.counter);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackSaveAbs(int index) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_STORE);
		this.writeData(index);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackDupAbs(int index) throws IOException {
		this.opStackLoadAbs(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackPreDec(int index) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_PRE_DEC);
		this.writeData(index + this.counter);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackPreInc(int index) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_PRE_INC);
		this.writeData(index + this.counter);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackPosDec(int index) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_POS_DEC);
		this.writeData(index + this.counter);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opStackPosInc(int index) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_POS_INC);
		this.writeData(index + this.counter);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @param pc
	 * @throws IOException
	 */
	public void opStackForIncBegin(int index, int pc) throws IOException {
		this.opStackForIncBeginAbs(index + this.counter, pc);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @param pc
	 * @throws IOException
	 */
	public void opStackForIncBeginAbs(int index, int pc) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_FOR_INC_BEGIN);
		this.writeData(index);
		this.writeData(pc);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @param pc
	 * @throws IOException
	 */
	public void opStackForIncEnd(int index, int pc) throws IOException {
		this.opStackForIncEndAbs(index + this.counter, pc);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @param pc
	 * @throws IOException
	 */
	public void opStackForIncEndAbs(int index, int pc) throws IOException {
		this.writeData(_AGB_GP_STACK);
		this.writeData(_AGB_OP_STACK_FOR_INC_END);
		this.writeData(index);
		this.writeData(pc);
	}

	// /**
	// * Opcode Stack Load
	// *
	// * @param index
	// * @param pc
	// * @throws IOException
	// */
	// public void opStackForDecBegin(int index, int pc) throws IOException {
	// this.opStackForDecBeginAbs(index + this.counter, pc + this.getPc() + 4);
	// }
	//
	// /**
	// * Opcode Stack Load
	// *
	// * @param index
	// * @param pc
	// * @throws IOException
	// */
	// public void opStackForDecBeginAbs(int index, int pc) throws IOException {
	// this.writeData(_AGB_GP_STACK);
	// this.writeData(_AGB_OP_STACK_FOR_DEC_BEGIN);
	// this.writeData(index);
	// this.writeData(pc);
	// }
	//
	// /**
	// * Opcode Stack Load
	// *
	// * @param index
	// * @param pc
	// * @throws IOException
	// */
	// public void opStackForDecEnd(int index, int pc) throws IOException {
	// this.opStackForDecEndAbs(index + this.counter, pc);
	// }
	//
	// /**
	// * Opcode Stack Load
	// *
	// * @param index
	// * @param pc
	// * @throws IOException
	// */
	// public void opStackForDecEndAbs(int index, int pc) throws IOException {
	// this.writeData(_AGB_GP_STACK);
	// this.writeData(_AGB_OP_STACK_FOR_DEC_END);
	// this.writeData(index);
	// this.writeData(pc);
	// }

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opLoadTrue() throws IOException {
		this.writeData(_AGB_GP_LOAD);
		this.writeData(_AGB_OP_LOAD_TRUE);
		this.counter++;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opLoadFalse() throws IOException {
		this.writeData(_AGB_GP_LOAD);
		this.writeData(_AGB_OP_LOAD_FALSE);
		this.counter++;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opObjectCast(int index) throws IOException {
		this.writeData(_AGB_GP_OBJECT);
		this.writeData(_AGB_OP_OBJECT_CAST_CLASS);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opObjectCastNative(int index) throws IOException {
		this.writeData(_AGB_GP_OBJECT);
		this.writeData(_AGB_OP_OBJECT_CAST_NATIVE);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opObjectGet(int index) throws IOException {
		this.writeData(_AGB_GP_OBJECT);
		this.writeData(_AGB_OP_OBJECT_GET);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opObjectSet(int index) throws IOException {
		this.writeData(_AGB_GP_OBJECT);
		this.writeData(_AGB_OP_OBJECT_SET);
		this.writeData(index);
		this.counter -= 2;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opObjectEqual() throws IOException {
		this.writeData(_AGB_GP_OBJECT);
		this.writeData(_AGB_OP_OBJECT_EQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opObjectNotEqual() throws IOException {
		this.writeData(_AGB_GP_OBJECT);
		this.writeData(_AGB_OP_OBJECT_NOTEQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpInt(int index) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_INT);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpIntNext(int index) throws IOException {
		this.opJumpInt(index + this.pc + 3);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpTrue(int index) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_TRUE);
		this.writeData(index);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpTrueNext(int index) throws IOException {
		this.opJumpTrue(index + this.getPc() + 3);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpFalse(int index) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_FALSE);
		this.writeData(index);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpFalseDup(int index) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_FALSE_DUP);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpTrueDup(int index) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_TRUE_DUP);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpFalseNext(int index) throws IOException {
		this.opJumpFalse(index + this.getPc() + 3);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @param paramSize
	 * @throws IOException
	 */
	public void opJumpCall(int index, int paramSize) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_CALL);
		this.writeData(index);
		this.writeData(paramSize);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opJumpCallFunc() throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_FUNC_CALL);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpStaticCall(int index, int paramSize) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_STATIC_CALL);
		this.writeData(index);
		this.writeData(paramSize);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opJumpReturn(int index) throws IOException {
		this.writeData(_AGB_GP_JUMP);
		this.writeData(_AGB_OP_JUMP_RETURN);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param flag
	 * @throws IOException
	 */
	public void opLoadBoolean(boolean flag) throws IOException {
		if (flag) {
			this.opLoadTrue();
		} else {
			this.opLoadFalse();
		}
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opLoadNum(int index) throws IOException {
		this.writeData(_AGB_GP_LOAD);
		this.writeData(_AGB_OP_LOAD_NUM);
		this.writeData(index);
		this.counter++;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @param objects
	 * @throws IOException
	 */
	public void opLoadFunc(int index, int objects) throws IOException {
		this.writeData(_AGB_GP_LOAD);
		this.writeData(_AGB_OP_LOAD_FUNC);
		this.writeData(objects);
		this.writeData(index);
		this.counter -= objects - 1;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opLoadStr(int index) throws IOException {
		this.writeData(_AGB_GP_LOAD);
		this.writeData(_AGB_OP_LOAD_STR);
		this.writeData(index);
		this.counter++;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opLoadNull() throws IOException {
		this.writeData(_AGB_GP_LOAD);
		this.writeData(_AGB_OP_LOAD_NULL);
		this.counter++;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opLoadNew(int index) throws IOException {
		this.writeData(_AGB_GP_LOAD);
		this.writeData(_AGB_OP_LOAD_NEW);
		this.writeData(index);
		this.counter++;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opBooleanNot() throws IOException {
		this.writeData(_AGB_GP_BOOL);
		this.writeData(_AGB_OP_BOOL_NOT);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opBooleanOr() throws IOException {
		this.writeData(_AGB_GP_BOOL);
		this.writeData(_AGB_OP_BOOL_OR);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opBooleanAnd() throws IOException {
		this.writeData(_AGB_GP_BOOL);
		this.writeData(_AGB_OP_BOOL_AND);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opBooleanEqual() throws IOException {
		this.writeData(_AGB_GP_BOOL);
		this.writeData(_AGB_OP_BOOL_EQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opBooleanNotEqual() throws IOException {
		this.writeData(_AGB_GP_BOOL);
		this.writeData(_AGB_OP_BOOL_NEQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opBooleanToStr() throws IOException {
		this.writeData(_AGB_GP_BOOL);
		this.writeData(_AGB_OP_BOOL_TO_STR);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberNeg() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_NEG);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberSum() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_SUM);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberSub() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_SUB);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberMul() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_MUL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberDiv() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_DIV);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberMod() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_MOD);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberEqual() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_EQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberNotEqual() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_NEQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberGreater() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_GT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberGreaterEqual() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_EGT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberLower() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_LT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberLowerEqual() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_ELT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberToStr() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_TO_STR);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberAndBit() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_AND_BIT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberOrBit() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_OR_BIT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberShiftLeft() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_SHIFT_LEFT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opNumberShiftRight() throws IOException {
		this.writeData(_AGB_GP_NUM);
		this.writeData(_AGB_OP_NUM_SHIFT_RIGHT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opStringSum() throws IOException {
		this.writeData(_AGB_GP_STR);
		this.writeData(_AGB_OP_STR_SUM);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opStringEqual() throws IOException {
		this.writeData(_AGB_GP_STR);
		this.writeData(_AGB_OP_STR_EQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opStringNotEqual() throws IOException {
		this.writeData(_AGB_GP_STR);
		this.writeData(_AGB_OP_STR_NEQUAL);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opStringGreater() throws IOException {
		this.writeData(_AGB_GP_STR);
		this.writeData(_AGB_OP_STR_GT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opStringGreaterEqual() throws IOException {
		this.writeData(_AGB_GP_STR);
		this.writeData(_AGB_OP_STR_EGT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opStringLower() throws IOException {
		this.writeData(_AGB_GP_STR);
		this.writeData(_AGB_OP_STR_LT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opStringLowerEqual() throws IOException {
		this.writeData(_AGB_GP_STR);
		this.writeData(_AGB_OP_STR_ELT);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opThrowTry(int index) throws IOException {
		this.writeData(_AGB_GP_THROW);
		this.writeData(_AGB_OP_THROW_TRY);
		this.writeData(index);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opThrowObject() throws IOException {
		this.writeData(_AGB_GP_THROW);
		this.writeData(_AGB_OP_THROW_OBJECT);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opThrowTrue() throws IOException {
		this.writeData(_AGB_GP_THROW);
		this.writeData(_AGB_OP_THROW_TRUE);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @throws IOException
	 */
	public void opThrowFalse() throws IOException {
		this.writeData(_AGB_GP_THROW);
		this.writeData(_AGB_OP_THROW_FALSE);
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void opThrowStore(int index) throws IOException {
		this.writeData(_AGB_GP_THROW);
		this.writeData(_AGB_OP_THROW_STORE);
		this.writeData(index);
		this.counter--;
	}

	/**
	 * Opcode Stack Load
	 * 
	 * @param classIndex
	 * @param index
	 * @throws IOException
	 */
	public void opThrowJump(int index, int classIndex) throws IOException {
		this.writeData(_AGB_GP_THROW);
		this.writeData(_AGB_OP_THROW_JUMP);
		this.writeData(index);
		this.writeData(classIndex);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opArrayListNew() throws IOException {
		this.writeData(_AGB_GP_ARRAY);
		this.writeData(_AGB_OP_ARRAY_NEW);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opArrayListSize() throws IOException {
		this.writeData(_AGB_GP_ARRAY);
		this.writeData(_AGB_OP_ARRAY_LEN);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opArrayListGet() throws IOException {
		this.writeData(_AGB_GP_ARRAY);
		this.writeData(_AGB_OP_ARRAY_GET);
		this.counter--;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opArrayListSet() throws IOException {
		this.writeData(_AGB_GP_ARRAY);
		this.writeData(_AGB_OP_ARRAY_SET);
		this.counter -= 2;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opArrayListRemove() throws IOException {
		this.writeData(_AGB_GP_ARRAY);
		this.writeData(_AGB_OP_ARRAY_REM);
		this.counter--;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opIntsNew() throws IOException {
		this.writeData(_AGB_GP_INTS);
		this.writeData(_AGB_OP_INTS_NEW);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opIntsSize() throws IOException {
		this.writeData(_AGB_GP_INTS);
		this.writeData(_AGB_OP_INTS_SIZE);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opIntsGet() throws IOException {
		this.writeData(_AGB_GP_INTS);
		this.writeData(_AGB_OP_INTS_GET);
		this.counter--;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opIntsSet() throws IOException {
		this.writeData(_AGB_GP_INTS);
		this.writeData(_AGB_OP_INTS_SET);
		this.counter -= 2;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opNumsNew() throws IOException {
		this.writeData(_AGB_GP_NUMS);
		this.writeData(_AGB_OP_NUMS_NEW);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opNumsSize() throws IOException {
		this.writeData(_AGB_GP_NUMS);
		this.writeData(_AGB_OP_NUMS_SIZE);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opNumsGet() throws IOException {
		this.writeData(_AGB_GP_NUMS);
		this.writeData(_AGB_OP_NUMS_GET);
		this.counter--;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opNumsSet() throws IOException {
		this.writeData(_AGB_GP_INTS);
		this.writeData(_AGB_OP_INTS_SET);
		this.counter -= 2;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opBoolsNew() throws IOException {
		this.writeData(_AGB_GP_NUMS);
		this.writeData(_AGB_OP_NUMS_NEW);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opBoolsSize() throws IOException {
		this.writeData(_AGB_GP_BOOLS);
		this.writeData(_AGB_OP_BOOLS_SIZE);
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opBoolsGet() throws IOException {
		this.writeData(_AGB_GP_BOOLS);
		this.writeData(_AGB_OP_BOOLS_GET);
		this.counter--;
	}

	/**
	 * Opcode
	 * 
	 * @throws IOException
	 */
	public void opBoolsSet() throws IOException {
		this.writeData(_AGB_GP_BOOLS);
		this.writeData(_AGB_OP_BOOLS_SET);
		this.counter -= 2;
	}

	/**
	 * @param value
	 * @throws IOException
	 */
	public void opSum(AGBRValueNode value) throws IOException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberSum();
		} else if (type.isString()) {
			this.opStringSum();
		} else {
			throw new RuntimeException();
		}
	}

	/**
	 * @param value
	 * @throws IOException
	 * @throws AGBException
	 */
	public void opToString(AGBRValueNode value) throws IOException, AGBException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberToStr();
		} else if (type.isString()) {
		} else if (type.isBoolean()) {
			this.opBooleanToStr();
		} else {
			throw new AGBCastException(AGBStringTypeNode.getInstance(), value.getType(), value.getToken());
		}
	}

	/**
	 * @param value
	 * @throws IOException
	 */
	public void opEqual(AGBRValueNode value) throws IOException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberEqual();
		} else if (type.isString()) {
			this.opStringEqual();
		} else if (type.isBoolean()) {
			this.opBooleanEqual();
		} else if (type.isObject()) {
			this.opObjectEqual();
		} else {
			this.opObjectEqual();
		}
	}

	/**
	 * @param value
	 * @throws IOException
	 */
	public void opNotEqual(AGBRValueNode value) throws IOException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberNotEqual();
		} else if (type.isString()) {
			this.opStringNotEqual();
		} else if (type.isBoolean()) {
			this.opBooleanNotEqual();
		} else if (type.isObject()) {
			this.opObjectNotEqual();
		} else {
			this.opObjectNotEqual();
		}
	}

	/**
	 * @param value
	 * @throws IOException
	 */
	public void opGreater(AGBRValueNode value) throws IOException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberGreater();
		} else if (type.isString()) {
			this.opStringGreater();
		} else {
			throw new RuntimeException();
		}
	}

	/**
	 * @param value
	 * @throws IOException
	 */
	public void opGreaterEqual(AGBRValueNode value) throws IOException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberGreaterEqual();
		} else if (type.isString()) {
			this.opStringGreaterEqual();
		} else {
			throw new RuntimeException();
		}
	}

	/**
	 * @param value
	 * @throws IOException
	 */
	public void opLower(AGBRValueNode value) throws IOException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberLower();
		} else if (type.isString()) {
			this.opStringLower();
		} else {
			throw new RuntimeException();
		}
	}

	/**
	 * @param value
	 * @throws IOException
	 */
	public void opLowerEqual(AGBRValueNode value) throws IOException {
		AGBTypeNode type = value.getType();
		if (type.isNumber()) {
			this.opNumberLowerEqual();
		} else if (type.isString()) {
			this.opStringLowerEqual();
		} else {
			throw new RuntimeException();
		}
	}

	/**
	 * Escreve um conteúdo
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void writeData(int data) throws IOException {
		this.pc++;
		this.writeInt(data);
	}

	// /**
	// * Calcula quantos opcodes é usado
	// *
	// * @param node
	// * @return quantos opcodes
	// * @throws IOException
	// * @throws AGBException
	// */
	// public int op(AGBNode node) throws IOException, AGBException {
	// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	// OpcodeOutputStream output = new OpcodeOutputStream(bytes);
	// node.build(output);
	// return bytes.toByteArray().length / 4;
	// }

	/**
	 * Instrução de biblioteca
	 * 
	 * @param group
	 * @param opcode
	 * @param returns
	 * @throws IOException
	 */
	public void op(int group, int opcode, int returns) throws IOException {
		this.writeData(group);
		this.writeData(opcode);
		this.counter += returns;
	}

	/**
	 * Retorna o pc
	 * 
	 * @return pc
	 */
	public int getPc() {
		return this.pc;
	}

	/**
	 * Incrementa a pilha
	 * 
	 * @param count
	 */
	public void incCounter(int count) {
		this.counter += count;
	}

	/**
	 * Decrementa a pilha
	 * 
	 * @param count
	 */
	public void decCounter(int count) {
		this.counter -= count;
	}

	/**
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Atribui a pilha
	 * 
	 * @param counter
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

}
