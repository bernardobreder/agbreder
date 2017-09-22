package com.agbreder.vm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.AGBCompiler;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.util.Base64;

public class AGBVm {

	private static final int object_stack_max = 1024;

	private double[] numbers;

	private String[] strings;

	private AGBClass[] classes;

	private int methodCount;

	private int[] opcs;

	private int[] spcs;

	private int[] bytes;

	private int[] fields;

	private boolean[] cast;

	private ByteArrayOutputStream output = new ByteArrayOutputStream();

	public AGBVm(InputStream input) throws IOException {
		DataInputStream data = new DataInputStream(input);
		loadMagic(data);
		loadNumber(data);
		loadString(data);
		loadClasses(data);
		loadMethods(data);
		loadBodySeparator(data);
		loadBytes(data);
		loadEof(data);
		data.close();
		{
			{
				int classCount = this.classes.length;
				this.opcs = new int[classCount * this.methodCount];
				for (int classIndex = 0; classIndex < classCount; classIndex++) {
					AGBClass c = this.classes[classIndex];
					this.loadObjectJump(c, c);
				}
			}
			{
				int classCount = this.classes.length;
				this.spcs = new int[this.methodCount];
				for (int n = 0, m = 0; n < classCount; n++) {
					AGBClass c = this.classes[n];
					for (int p = 0; p < c.methodCount; p++) {
						AGBMethod method = c.methods[p];
						this.spcs[m++] = method.pc;
					}
				}
			}
			{
				int classCount = this.classes.length;
				this.fields = new int[classCount];
				for (int n = 0; n < classCount; n++) {
					AGBClass c = this.classes[n];
					this.fields[n] = 0;
					for (;;) {
						this.fields[n] += c.fieldCount;
						if (c.extendIndex >= 0) {
							c = this.classes[c.extendIndex];
						} else {
							break;
						}
					}
				}
			}
			{
				int classCount = this.classes.length;
				this.cast = new boolean[classCount * classCount];
				for (int ifrom = 0; ifrom < classCount; ifrom++) {
					AGBClass cfrom = this.classes[ifrom];
					AGBClass cto = cfrom;
					while (cto != null) {
						this.cast[ifrom * classCount + cto.index] = true;
						cto = cto.extendIndex >= 0 ? this.classes[cto.extendIndex] : null;
					}
				}
			}
		}
	}

	private void loadObjectJumpAux(AGBClass bclass, AGBClass clazz, AGBMethod method) {
		if (clazz.extendIndex >= 0) {
			AGBClass extend = this.classes[clazz.extendIndex];
			loadObjectJumpAux(bclass, extend, method);
		}
		int methods = this.methodCount;
		int sizem = clazz.methodCount;
		for (int m = 0; m < sizem; m++) {
			AGBMethod smethod = clazz.methods[m];
			if (smethod.name.equals(method.name)) {
				if (!smethod.isStatic) {
					this.opcs[bclass.index * methods + smethod.index] = method.pc;
					break;
				}
			}
		}
	}

	private void loadObjectJump(AGBClass bclass, AGBClass clazz) {
		if (clazz.extendIndex >= 0) {
			AGBClass extend = this.classes[clazz.extendIndex];
			this.loadObjectJump(bclass, extend);
		}
		int sizem = clazz.methodCount;
		for (int m = 0; m < sizem; m++) {
			AGBMethod method = clazz.methods[m];
			if (!method.isStatic) {
				loadObjectJumpAux(bclass, clazz, method);
			} else {
				// TODO : Reaproveitar a memoria para colocar os statics
			}
		}
	}

	private void loadMagic(DataInputStream data) throws IOException {
		if (data.read() != 0xBB) {
			throw new IllegalArgumentException("magic symbol");
		}
	}

	private void loadNumber(DataInputStream data) throws IOException {
		int size = data.readInt();
		this.numbers = new double[size];
		for (int n = 0; n < size; n++) {
			String utf = data.readUTF();
			this.numbers[n] = Double.parseDouble(utf);
		}
	}

	private void loadString(DataInputStream data) throws IOException {
		int size = data.readInt();
		this.strings = new String[size];
		for (int n = 0; n < size; n++) {
			String utf = data.readUTF();
			this.strings[n] = utf;
		}
	}

	private void loadClasses(DataInputStream data) throws IOException {
		int size = data.readInt();
		this.classes = new AGBClass[size];
		for (int n = 0; n < size; n++) {
			AGBClass c = new AGBClass();
			c.index = n;
			c.name = data.readUTF();
			c.extendIndex = data.readInt();
			c.fieldCount = data.readInt();
			this.classes[n] = c;
		}
	}

	private void loadMethods(DataInputStream data) throws IOException {
		int classCount = data.readInt();
		int methodGlobalIndex = 0;
		for (int classIndex = 0; classIndex < classCount; classIndex++) {
			AGBClass c = this.classes[classIndex];
			int methodCount = data.readInt();
			this.methodCount += methodCount;
			c.methodCount = methodCount;
			c.methods = new AGBMethod[methodCount];
			for (int methodIndex = 0; methodIndex < methodCount; methodIndex++) {
				AGBMethod method = new AGBMethod();
				method.index = methodGlobalIndex++;
				method.name = data.readUTF();
				method.isStatic = data.readInt() != 0;
				method.pc = data.readInt();
				method.pcs = data.readInt();
				c.methods[methodIndex] = method;
			}
		}
	}

	private void loadBodySeparator(DataInputStream data) throws IOException {
		if (data.read() != 0xFF) {
			throw new IllegalArgumentException("body separator");
		}
	}

	private void loadBytes(DataInputStream data) throws IOException {
		int size = data.readInt();
		this.bytes = new int[size];
		for (int n = 0; n < size; n++) {
			this.bytes[n] = data.readInt();
		}
	}

	private void loadEof(DataInputStream data) throws IOException {
		if (data.read() != 0xFF) {
			throw new IllegalArgumentException("eof");
		}
	}

	public static class AGBClass {

		private int index;

		private String name;

		public int extendIndex;

		public int fieldCount;

		public int methodCount;

		public AGBMethod[] methods;

	}

	public static class AGBMethod {

		private int index;

		private String name;

		public boolean isStatic;

		public int pc;

		public int pcs;

	}

	public static class AGBThread {

	}

	public static class AGBThrow {
		public int co;
		public int pc;
		public int st;
	}

	public static final int _AGB_GP_VM = 1;
	public static final int _AGB_GP_STACK = 2;
	public static final int _AGB_GP_OBJECT = 3;
	public static final int _AGB_GP_LOAD = 4;
	public static final int _AGB_GP_JUMP = 5;
	public static final int _AGB_GP_BOOL = 6;
	public static final int _AGB_GP_INT = 7;
	public static final int _AGB_GP_NUM = 8;
	public static final int _AGB_GP_THROW = 9;
	public static final int _AGB_GP_USER = 32;
	public static final int _AGB_GP_CONSOLE = _AGB_GP_USER + 0;
	public static final int _AGB_GP_STR = _AGB_GP_USER + 1;
	public static final int _AGB_GP_ARRAY = _AGB_GP_USER + 2;
	public static final int _AGB_GP_MAP = _AGB_GP_USER + 3;
	public static final int _AGB_GP_SDL = _AGB_GP_USER + 4;
	public static final int _AGB_GP_NET = _AGB_GP_USER + 5;
	public static final int _AGB_GP_INTS = _AGB_GP_USER + 6;
	public static final int _AGB_GP_NUMS = _AGB_GP_USER + 7;
	public static final int _AGB_GP_BOOLS = _AGB_GP_USER + 8;
	public static final int _AGB_GP_BYTES = _AGB_GP_USER + 9;
	public static final int _AGB_GP_UINT2D = _AGB_GP_USER + 10;
	public static final int _AGB_OP_VM_HALF = 1;
	public static final int _AGB_OP_VM_GC = 2;
	public static final int _AGB_OP_VM_ALLOC = 3;
	public static final int _AGB_OP_VM_PAINT = 4;
	public static final int _AGB_OP_VM_EVENT_KEY = 5;
	public static final int _AGB_OP_VM_EVENT_MOUSE = 6;
	public static final int _AGB_OP_VM_EVENT_WHEEL = 7;
	public static final int _AGB_OP_STACK_PUSH = 1;
	public static final int _AGB_OP_STACK_POP = 2;
	public static final int _AGB_OP_STACK_LOAD = 3;
	public static final int _AGB_OP_STACK_STORE = 4;
	public static final int _AGB_OP_STACK_PRE_INC = 5;
	public static final int _AGB_OP_STACK_PRE_DEC = 6;
	public static final int _AGB_OP_STACK_POS_INC = 7;
	public static final int _AGB_OP_STACK_POS_DEC = 8;
	public static final int _AGB_OP_STACK_FOR_INC_BEGIN = 9;
	public static final int _AGB_OP_STACK_FOR_INC_END = 10;
	public static final int _AGB_OP_STACK_FOR_DEC_BEGIN = 11;
	public static final int _AGB_OP_STACK_FOR_DEC_END = 12;
	public static final int _AGB_OP_OBJECT_CAST_CLASS = 1;
	public static final int _AGB_OP_OBJECT_GET_FIELD = 2;
	public static final int _AGB_OP_OBJECT_SET_FIELD = 3;
	public static final int _AGB_OP_OBJECT_EQUAL = 4;
	public static final int _AGB_OP_OBJECT_NOTEQUAL = 5;
	public static final int _AGB_OP_OBJECT_HASHCODE = 6;
	public static final int _AGB_OP_OBJECT_CLASSNAME = 7;
	public static final int _AGB_OP_OBJECT_CAST_NATIVE = 8;
	public static final int _AGB_OP_LOAD_TRUE = 1;
	public static final int _AGB_OP_LOAD_FALSE = 2;
	public static final int _AGB_OP_LOAD_NUM = 4;
	public static final int _AGB_OP_LOAD_STR = 5;
	public static final int _AGB_OP_LOAD_NULL = 6;
	public static final int _AGB_OP_LOAD_FUNC = 7;
	public static final int _AGB_OP_LOAD_NEW = 8;
	public static final int _AGB_OP_JUMP_STACK = 1;
	public static final int _AGB_OP_JUMP_INT = 2;
	public static final int _AGB_OP_JUMP_TRUE = 3;
	public static final int _AGB_OP_JUMP_FALSE = 4;
	public static final int _AGB_OP_JUMP_CALL = 5;
	public static final int _AGB_OP_JUMP_STATIC_CALL = 6;
	public static final int _AGB_OP_JUMP_RETURN = 7;
	public static final int _AGB_OP_JUMP_TRUE_DUP = 8;
	public static final int _AGB_OP_JUMP_FALSE_DUP = 9;
	public static final int _AGB_OP_JUMP_FUNC_CALL = 10;
	public static final int _AGB_OP_BOOL_NOT = 1;
	public static final int _AGB_OP_BOOL_OR = 2;
	public static final int _AGB_OP_BOOL_AND = 3;
	public static final int _AGB_OP_BOOL_EQUAL = 4;
	public static final int _AGB_OP_BOOL_NEQUAL = 5;
	public static final int _AGB_OP_BOOL_TO_STR = 6;
	public static final int _AGB_OP_NUM_NEG = 1;
	public static final int _AGB_OP_NUM_SUM = 2;
	public static final int _AGB_OP_NUM_SUB = 3;
	public static final int _AGB_OP_NUM_MUL = 4;
	public static final int _AGB_OP_NUM_DIV = 5;
	public static final int _AGB_OP_NUM_EQUAL = 6;
	public static final int _AGB_OP_NUM_NEQUAL = 7;
	public static final int _AGB_OP_NUM_GT = 8;
	public static final int _AGB_OP_NUM_EGT = 9;
	public static final int _AGB_OP_NUM_LT = 10;
	public static final int _AGB_OP_NUM_ELT = 11;
	public static final int _AGB_OP_NUM_TO_STR = 12;
	public static final int _AGB_OP_NUM_MOD = 13;
	public static final int _AGB_OP_NUM_AND_BIT = 14;
	public static final int _AGB_OP_NUM_OR_BIT = 15;
	public static final int _AGB_OP_NUM_SHIFT_LEFT = 16;
	public static final int _AGB_OP_NUM_SHIFT_RIGHT = 17;
	public static final int _AGB_OP_NUM_TO_INT = 18;
	public static final int _AGB_OP_STR_LEN = 1;
	public static final int _AGB_OP_STR_SUM = 2;
	public static final int _AGB_OP_STR_EQUAL = 3;
	public static final int _AGB_OP_STR_NEQUAL = 4;
	public static final int _AGB_OP_STR_GT = 5;
	public static final int _AGB_OP_STR_EGT = 6;
	public static final int _AGB_OP_STR_LT = 7;
	public static final int _AGB_OP_STR_ELT = 8;
	public static final int _AGB_OP_STR_SUBSTRING = 9;
	public static final int _AGB_OP_STR_CODE_TO_CHAR = 10;
	public static final int _AGB_OP_STR_CHAR_TO_CODE = 11;
	public static final int _AGB_OP_STR_TRIM = 12;
	public static final int _AGB_OP_STR_CHAR_AT = 13;
	public static final int _AGB_OP_STR_START_WITH = 14;
	public static final int _AGB_OP_STR_END_WITH = 15;
	public static final int _AGB_OP_STR_INDEX_OF = 16;
	public static final int _AGB_OP_STR_LAST_INDEX_OF = 17;
	public static final int _AGB_OP_STR_REPLACE = 18;
	public static final int _AGB_OP_STR_TO_LOWER_CASE = 19;
	public static final int _AGB_OP_STR_TO_UPPER_CASE = 20;
	public static final int _AGB_OP_STR_INDEX_OF_N = 21;
	public static final int _AGB_OP_STR_BASE64_ENCODE = 22;
	public static final int _AGB_OP_STR_BASE64_DECODE = 23;
	public static final int _AGB_OP_STR_UTF_ENCODE = 24;
	public static final int _AGB_OP_STR_UTF_DECODE = 25;
	public static final int _AGB_OP_THROW_TRY = 1;
	public static final int _AGB_OP_THROW_OBJECT = 2;
	public static final int _AGB_OP_THROW_TRUE = 3;
	public static final int _AGB_OP_THROW_FALSE = 4;
	public static final int _AGB_OP_THROW_STORE = 5;
	public static final int _AGB_OP_THROW_JUMP = 6;
	public static final int _AGB_OP_ARRAY_NEW = 1;
	public static final int _AGB_OP_ARRAY_LEN = 2;
	public static final int _AGB_OP_ARRAY_ADD = 3;
	public static final int _AGB_OP_ARRAY_INSERT = 4;
	public static final int _AGB_OP_ARRAY_GET = 5;
	public static final int _AGB_OP_ARRAY_SET = 6;
	public static final int _AGB_OP_ARRAY_REM = 7;
	public static final int _AGB_OP_CONSOLE_PRINT_STR = 1;
	public static final int _AGB_OP_CONSOLE_PRINT_NUM = 2;
	public static final int _AGB_OP_CONSOLE_PRINT_BOOL = 3;
	public static final int _AGB_OP_NET_REQUEST = 1;
	public static final int _AGB_OP_INTS_NEW = 1;
	public static final int _AGB_OP_INTS_SIZE = 2;
	public static final int _AGB_OP_INTS_GET = 3;
	public static final int _AGB_OP_INTS_SET = 4;
	public static final int _AGB_OP_INTS_SETS = 5;
	public static final int _AGB_OP_INTS_COPY = 6;
	public static final int _AGB_OP_NUMS_NEW = 1;
	public static final int _AGB_OP_NUMS_SIZE = 2;
	public static final int _AGB_OP_NUMS_GET = 3;
	public static final int _AGB_OP_NUMS_SET = 4;
	public static final int _AGB_OP_NUMS_SETS = 5;
	public static final int _AGB_OP_NUMS_COPY = 6;
	public static final int _AGB_OP_NUMS_EQUAL = 7;
	public static final int _AGB_OP_BOOLS_NEW = 1;
	public static final int _AGB_OP_BOOLS_SIZE = 2;
	public static final int _AGB_OP_BOOLS_GET = 3;
	public static final int _AGB_OP_BOOLS_SET = 4;
	public static final int _AGB_OP_BOOLS_SETS = 5;
	public static final int _AGB_OP_BOOLS_COPY = 6;
	public static final int _AGB_OP_BOOLS_EQUAL = 7;
	public static final int _AGB_OP_BYTES_NEW = 1;
	public static final int _AGB_OP_BYTES_SIZE = 2;
	public static final int _AGB_OP_BYTES_GET = 3;
	public static final int _AGB_OP_BYTES_SET = 4;
	public static final int _AGB_OP_BYTES_SETS = 5;
	public static final int _AGB_OP_BYTES_COPY = 6;
	public static final int _AGB_OP_BYTES_EQUAL = 7;
	public static final int _AGB_OP_UINT2D_NEW = 1;
	public static final int _AGB_OP_UINT2D_LINS = 2;
	public static final int _AGB_OP_UINT2D_COLS = 3;
	public static final int _AGB_OP_UINT2D_GET = 4;
	public static final int _AGB_OP_UINT2D_SET = 5;
	public static final int _AGB_OP_UINT2D_SETS = 6;
	public static final int _AGB_OP_UINT2D_COPY = 7;
	public static final int _AGB_OP_UINT2D_EQUAL = 8;

	public static final int AGB_LIBRARY_THROW_ID = -1;
	public static final int AGB_LIBRARY_STRING_ID = -2;
	public static final int AGB_LIBRARY_ARRAY_ID = -3;
	public static final int AGB_LIBRARY_NET_SOCKET_ID = -4;
	public static final int AGB_LIBRARY_FUNC_ID = -5;
	public static final int AGB_LIBRARY_BOOLEAN_ID = -6;
	public static final int AGB_LIBRARY_NUMBER_ID = -7;
	public static final int AGB_LIBRARY_INTS_ID = -8;
	public static final int AGB_LIBRARY_NUMS_ID = -9;
	public static final int AGB_LIBRARY_BOOLS_ID = -10;
	public static final int AGB_LIBRARY_BYTES_ID = -11;
	public static final int AGB_LIBRARY_UINT2D_ID = -12;
	public static final int AGB_LIBRARY_MAX = -11;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(int opcodeIndex) throws IOException {
		AGBThrow[] exs = new AGBThrow[1024];
		for (int n = 0; n < 1024; n++) {
			exs[n] = new AGBThrow();
		}
		int ex = 0;
		AGBVm vm = this;
		int[] bytecodes = vm.bytes;
		int[] opcs = vm.opcs;
		int[] spcs = vm.spcs;
		int[] fields = vm.fields;
		int classeSize = vm.classes.length;
		int methodSize = vm.methodCount;
		boolean[] cast = vm.cast;
		double[] numbers = vm.numbers;
		// agb_object_t** stringPoll = vm.strings;
		// agb_object_t* auxobj;
		// agb_object_nums_t* auxnums, *auxnums2;
		// agb_object_uint2d_t* auxuint2d, *auxuint2d2;
		// agb_object_bools_t* auxbools, *auxbools2;
		// double auxnum;
		// unsigned char auxbool;
		Object[] o = new Object[object_stack_max];
		int co = 0;
		// agb_ref_t* coslim = objectStack + objstack_count;
		// struct throw_t* cthrow = throw;
		int[] pcs = new int[1024];
		int cpcs = 0;
		int pc = opcodeIndex;
		for (;;) {
			switch (bytecodes[pc++]) {
			case _AGB_GP_STACK: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_STACK_PUSH: {
					int index = bytecodes[pc++];
					if (co + index >= object_stack_max) {
						throw new StackOverflowError();
					}
					co += index;
					break;
				}
				case _AGB_OP_STACK_POP: {
					int index = bytecodes[pc++];
					co -= index;
					break;
				}
				case _AGB_OP_STACK_LOAD: {
					int index = bytecodes[pc++];
					co++;
					o[co] = o[co - index - 1];
					break;
				}
				case _AGB_OP_STACK_STORE: {
					int index = bytecodes[pc++];
					o[co - index] = o[co];
					co--;
					break;
				}
				case _AGB_OP_STACK_PRE_INC: {
					int index = bytecodes[pc++];
					o[co] = ((Double) o[co]) + 1;
					o[co - index] = ((Double) o[co - index]) + 1;
					break;
				}
				case _AGB_OP_STACK_PRE_DEC: {
					int index = bytecodes[pc++];
					o[co] = ((Double) o[co]) - 1;
					o[co - index] = ((Double) o[co - index]) - 1;
					break;
				}
				case _AGB_OP_STACK_POS_INC: {
					int index = bytecodes[pc++];
					o[co - index] = ((Double) o[co - index]) + 1;
					break;
				}
				case _AGB_OP_STACK_POS_DEC: {
					int index = bytecodes[pc++];
					o[co - index] = ((Double) o[co - index]) - 1;
					break;
				}
				case _AGB_OP_STACK_FOR_INC_BEGIN: {
					int index = bytecodes[pc];
					if (((Double) o[co - index]) <= ((Double) o[co])) {
						pc += 2;
					} else {
						pc = bytecodes[pc + 1];
					}
					break;
				}
				case _AGB_OP_STACK_FOR_INC_END: {
					int index = bytecodes[pc];
					o[co - index] = ((Double) o[co - index]) + 1;
					pc = bytecodes[pc + 1];
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_LOAD: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_LOAD_NUM: {
					co++;
					o[co] = numbers[bytecodes[pc++]];
					break;
				}
				case _AGB_OP_LOAD_STR: {
					co++;
					o[co] = strings[bytecodes[pc++]];
					break;
				}
				case _AGB_OP_LOAD_TRUE: {
					co++;
					o[co] = Boolean.TRUE;
					break;
				}
				case _AGB_OP_LOAD_FALSE: {
					co++;
					o[co] = Boolean.FALSE;
					break;
				}
				case _AGB_OP_LOAD_NULL: {
					co++;
					o[co] = null;
					break;
				}
				case _AGB_OP_LOAD_FUNC: {
					int auxint = bytecodes[pc++];
					Object[] auxobj = new Object[auxint + 3];
					auxobj[0] = AGB_LIBRARY_FUNC_ID;
					auxobj[1] = bytecodes[pc++];
					auxobj[2] = auxint;
					co -= auxint - 1;
					System.arraycopy(o, co, auxobj, 3, auxint);
					o[co] = auxobj;
					break;
				}
				case _AGB_OP_LOAD_NEW: {
					int cindex = bytecodes[pc++];
					Object[] array = new Object[fields[cindex] + 1];
					array[0] = cindex;
					o[++co] = array;
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_JUMP: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_JUMP_STACK: {
					break;
				}
				case _AGB_OP_JUMP_INT: {
					pc = bytecodes[pc];
					break;
				}
				case _AGB_OP_JUMP_TRUE: {
					if (o[co] == Boolean.TRUE) {
						pc = bytecodes[pc];
					} else {
						pc++;
					}
					co--;
					break;
				}
				case _AGB_OP_JUMP_FALSE: {
					if (o[co] == Boolean.FALSE) {
						pc = bytecodes[pc];
					} else {
						pc++;
					}
					co--;
					break;
				}
				case _AGB_OP_JUMP_CALL: {
					int index = bytecodes[pc++];
					if (o[co] == null || cpcs >= object_stack_max) {
						index = bytecodes[pc++];
						o[co - index] = o[co];
						co += index;
					} else {
						pcs[cpcs++] = pc + 1;
						int cindex = ((Integer) (((Object[]) o[co])[0]));
						pc = opcs[cindex * methodSize + index];
					}
					break;
				}
				case _AGB_OP_JUMP_STATIC_CALL: {
					int index = bytecodes[pc++];
					if (cpcs >= object_stack_max) {
						index = bytecodes[pc++] - 1;
						co += index;
						o[co] = null;
					} else {
						pcs[cpcs++] = pc + 1;
						pc = spcs[index];
					}
					break;
				}
				case _AGB_OP_JUMP_RETURN: {
					co -= bytecodes[pc];
					if (cpcs <= 0) {
						return o[co];
					}
					o[co] = o[co + bytecodes[pc]];
					pc = pcs[--cpcs];
					break;
				}
				case _AGB_OP_JUMP_TRUE_DUP: {
					if (o[co] == Boolean.TRUE) {
						pc = bytecodes[pc];
					} else {
						pc++;
					}
					break;
				}
				case _AGB_OP_JUMP_FALSE_DUP: {
					if (o[co] == Boolean.TRUE) {
						pc++;
					} else {
						pc = bytecodes[pc];
					}
					break;
				}
				case _AGB_OP_JUMP_FUNC_CALL: {
					if (cpcs >= object_stack_max) {
						throw new StackOverflowError();
					}
					int auxint = (Integer) ((Object[]) o[co])[1];
					pcs[cpcs++] = pc;
					pc = spcs[auxint];
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_OBJECT: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_OBJECT_CAST_CLASS: {
					if (o[co] != null) {
						int cindex = ((Integer) (((Object[]) o[co])[0]));
						if (cindex < 0 || !cast[cindex * classeSize + bytecodes[pc]]) {
							throw new ClassCastException();
						}
					}
					pc++;
					break;
				}
				case _AGB_OP_OBJECT_CAST_NATIVE: {
					if (o[co] != null) {
						int cindex = ((Integer) (((Object[]) o[co])[0]));
						if (cindex != -bytecodes[pc]) {
							throw new ClassCastException();
						}
					}
					pc++;
					break;
				}
				case _AGB_OP_OBJECT_GET_FIELD: {
					if (o[co] != null) {
						o[co] = (((Object[]) o[co])[bytecodes[pc++] + 1]);
					}
					break;
				}
				case _AGB_OP_OBJECT_SET_FIELD: {
					if (o[co] != null) {
						(((Object[]) o[co])[bytecodes[pc++] + 1]) = o[co - 1];
						co -= 2;
					}
					break;
				}
				case _AGB_OP_OBJECT_EQUAL: {
					co--;
					o[co] = o[co] == o[co + 1] ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_OBJECT_NOTEQUAL: {
					co--;
					o[co] = o[co] == o[co + 1] ? Boolean.FALSE : Boolean.TRUE;
					break;
				}
				case _AGB_OP_OBJECT_HASHCODE: {
					if (o[co] != null) {
						o[co] = Double.valueOf(o[co].hashCode());
					} else {
						o[co] = 0;
					}
					break;
				}
				case _AGB_OP_OBJECT_CLASSNAME: {
					if (o[co] != null) {
						int cindex = ((Integer) (((Object[]) o[co])[0]));
						o[co] = classes[cindex].name;
					} else {
						o[co] = null;
					}
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_BOOL: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_BOOL_NOT: {
					o[co] = o[co] == Boolean.TRUE ? Boolean.FALSE : Boolean.TRUE;
					break;
				}
				case _AGB_OP_BOOL_OR: {
					co--;
					o[co] = o[co] == Boolean.TRUE || o[co + 1] == Boolean.TRUE;
					break;
				}
				case _AGB_OP_BOOL_AND: {
					co--;
					o[co] = o[co] == Boolean.TRUE && o[co + 1] == Boolean.TRUE;
					break;
				}
				case _AGB_OP_BOOL_EQUAL: {
					co--;
					o[co] = o[co] == o[co + 1];
					break;
				}
				case _AGB_OP_BOOL_NEQUAL: {
					co--;
					o[co] = o[co] != o[co + 1];
					break;
				}
				case _AGB_OP_BOOL_TO_STR: {
					if (o[co] == Boolean.TRUE) {
						o[co] = "true";
					} else {
						o[co] = "false";
					}
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_NUM: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_NUM_NEG: {
					o[co] = -((Double) o[co]);
					break;
				}
				case _AGB_OP_NUM_SUM: {
					co--;
					o[co] = ((Double) o[co]) + ((Double) o[co + 1]);
					break;
				}
				case _AGB_OP_NUM_SUB: {
					co--;
					o[co] = ((Double) o[co]) - ((Double) o[co + 1]);
					break;
				}
				case _AGB_OP_NUM_MUL: {
					co--;
					o[co] = ((Double) o[co]) * ((Double) o[co + 1]);
					break;
				}
				case _AGB_OP_NUM_DIV: {
					co--;
					o[co] = ((Double) o[co]) / ((Double) o[co + 1]);
					break;
				}
				case _AGB_OP_NUM_EQUAL: {
					co--;
					o[co] = ((Double) o[co]).equals(((Double) o[co + 1])) ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_NUM_NEQUAL: {
					co--;
					o[co] = ((Double) o[co]).equals(((Double) o[co + 1])) ? Boolean.FALSE : Boolean.TRUE;
					break;
				}
				case _AGB_OP_NUM_GT: {
					co--;
					o[co] = ((Double) o[co]).compareTo(((Double) o[co + 1])) > 0 ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_NUM_EGT: {
					co--;
					o[co] = ((Double) o[co]).compareTo(((Double) o[co + 1])) >= 0 ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_NUM_LT: {
					co--;
					o[co] = ((Double) o[co]).compareTo(((Double) o[co + 1])) < 0 ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_NUM_ELT: {
					co--;
					o[co] = ((Double) o[co]).compareTo(((Double) o[co + 1])) <= 0 ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_NUM_TO_STR: {
					Double value = ((Double) o[co]);
					if (value == value.intValue()) {
						o[co] = Integer.valueOf(value.intValue()).toString();
					} else {
						o[co] = value.toString();
					}
					break;
				}
				case _AGB_OP_NUM_MOD: {
					co--;
					o[co] = Double.valueOf(((Double) o[co]) % ((Double) o[co + 1]));
					break;
				}
				case _AGB_OP_NUM_AND_BIT: {
					co--;
					o[co] = Double.valueOf(((Double) o[co]).intValue() & ((Double) o[co + 1]).intValue());
					break;
				}
				case _AGB_OP_NUM_OR_BIT: {
					co--;
					o[co] = Double.valueOf(((Double) o[co]).intValue() | ((Double) o[co + 1]).intValue());
					break;
				}
				case _AGB_OP_NUM_SHIFT_LEFT: {
					co--;
					o[co] = Double.valueOf(((Double) o[co]).intValue() << ((Double) o[co + 1]).intValue());
					break;
				}
				case _AGB_OP_NUM_SHIFT_RIGHT: {
					co--;
					o[co] = Double.valueOf(((Double) o[co]).intValue() >> ((Double) o[co + 1]).intValue());
					break;
				}
				case _AGB_OP_NUM_TO_INT: {
					o[co] = Double.valueOf(((Double) o[co]).intValue());
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_STR: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_STR_LEN: {
					o[co] = Double.valueOf(((String) o[co]).length());
					break;
				}
				case _AGB_OP_STR_SUM: {
					co--;
					o[co] = ((String) o[co]) + ((String) o[co + 1]);
					break;
				}
				case _AGB_OP_STR_EQUAL: {
					co--;
					o[co] = ((String) o[co]).equals(((String) o[co + 1])) ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_STR_NEQUAL: {
					co--;
					o[co] = ((String) o[co]).equals(((String) o[co + 1])) ? Boolean.FALSE : Boolean.TRUE;
					break;
				}
				case _AGB_OP_STR_GT: {
					co--;
					o[co] = ((String) o[co]).compareTo(((String) o[co + 1])) > 0 ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_STR_EGT: {
					co--;
					o[co] = ((String) o[co]).compareTo(((String) o[co + 1])) >= 0 ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_STR_LT: {
					co--;
					o[co] = ((String) o[co]).compareTo(((String) o[co + 1])) < 0 ? Boolean.TRUE : Boolean.FALSE;
					break;
				}
				case _AGB_OP_STR_ELT: {
					co--;
					if (o[co] != null) {
						o[co] = ((String) o[co]).compareTo(((String) o[co + 1])) <= 0 ? Boolean.TRUE : Boolean.FALSE;
					} else {
						o[co] = o[co + 1] == null;
					}
					break;
				}
				case _AGB_OP_STR_SUBSTRING: {
					co -= 2;
					if (o[co] != null) {
						String string = (String) o[co];
						int beginIndex = ((Double) o[co + 1]).intValue() - 1;
						int endIndex = ((Double) o[co + 2]).intValue();
						if (beginIndex < 0) {
							beginIndex = 0;
						}
						if (endIndex > string.length()) {
							endIndex = string.length();
						}
						if (beginIndex >= string.length()) {
							o[co] = "";
						} else {
							o[co] = string.substring(beginIndex, endIndex);
						}
					}
					break;
				}
				case _AGB_OP_STR_CODE_TO_CHAR: {
					if (o[co] != null) {
						int index = ((Double) o[co]).intValue();
						if (index < 0 || index > 255) {
							o[co] = null;
						} else {
							o[co] = "" + (char) index;
						}
					}
					break;
				}
				case _AGB_OP_STR_CHAR_TO_CODE: {
					if (o[co] != null) {
						o[co] = Double.valueOf(((String) o[co]).charAt(0));
					} else {
						o[co] = Double.valueOf(0.);
					}
					break;
				}
				case _AGB_OP_STR_TRIM: {
					if (o[co] != null) {
						o[co] = ((String) o[co]).trim();
					}
					break;
				}
				case _AGB_OP_STR_CHAR_AT: {
					co--;
					if (o[co] != null) {
						String string = (String) o[co];
						int index = ((Double) o[co + 1]).intValue() - 1;
						if (index < 0 || index >= string.length()) {
							o[co] = Double.valueOf(-1);
						} else {
							o[co] = Double.valueOf(string.charAt(index));
						}
					} else {
						o[co] = Double.valueOf(-1);
					}
					break;
				}
				case _AGB_OP_STR_START_WITH: {
					co--;
					if (o[co] != null && o[co + 1] != null) {
						o[co] = ((String) o[co]).startsWith((String) o[co + 1]) ? Boolean.TRUE : Boolean.FALSE;
					} else {
						o[co] = Boolean.FALSE;
					}
					break;
				}
				case _AGB_OP_STR_END_WITH: {
					co--;
					if (o[co] != null && o[co + 1] != null) {
						o[co] = ((String) o[co]).endsWith((String) o[co + 1]) ? Boolean.TRUE : Boolean.FALSE;
					} else {
						o[co] = Boolean.FALSE;
					}
					break;
				}
				case _AGB_OP_STR_INDEX_OF: {
					co--;
					if (o[co] != null && o[co + 1] != null) {
						String string = (String) o[co];
						String str = (String) o[co + 1];
						if (str.length() == 0) {
							o[co] = Double.valueOf(-1);
						} else {
							int index = string.indexOf(str);
							if (index >= 0) {
								o[co] = Double.valueOf(index + 1);
							} else {
								o[co] = Double.valueOf(-1);
							}
						}
					} else {
						o[co] = Double.valueOf(-1);
					}
					break;
				}
				case _AGB_OP_STR_INDEX_OF_N: {
					co -= 2;
					if (o[co] != null && o[co + 1] != null) {
						String string = (String) o[co];
						String str = (String) o[co + 1];
						if (str.length() == 0) {
							o[co] = Double.valueOf(-1);
						} else {
							int index = ((Double) o[co + 2]).intValue() - 1;
							index = string.indexOf(str, index);
							if (index >= 0) {
								o[co] = Double.valueOf(index + 1);
							} else {
								o[co] = Double.valueOf(-1);
							}
						}
					} else {
						o[co] = Double.valueOf(-1);
					}
					break;
				}
				case _AGB_OP_STR_LAST_INDEX_OF: {
					co--;
					if (o[co] != null && o[co + 1] != null) {
						String string = (String) o[co];
						String str = (String) o[co + 1];
						if (str.length() == 0) {
							o[co] = Double.valueOf(-1);
						} else {
							int index = string.lastIndexOf(str);
							if (index >= 0) {
								o[co] = Double.valueOf(index + 1);
							} else {
								o[co] = Double.valueOf(-1);
							}
						}
					} else {
						o[co] = Double.valueOf(-1);
					}
					break;
				}
				// case _AGB_OP_STR_REPLACE: {
				// auxobj = agb_library_string_replace(thread, oobj(-2),
				// oobj(-1), oobj(0));
				// cosdecn(2);
				// oobj(0) = auxobj;
				// break;
				// }
				case _AGB_OP_STR_TO_LOWER_CASE: {
					if (o[co] != null) {
						o[co] = ((String) o[co]).toLowerCase();
					}
					break;
				}
				case _AGB_OP_STR_TO_UPPER_CASE: {
					if (o[co] != null) {
						o[co] = ((String) o[co]).toUpperCase();
					}
					break;
				}
				case _AGB_OP_STR_BASE64_ENCODE: {
					if (o[co] != null) {
						o[co] = Base64.encode(((String) o[co]).getBytes("utf-8"));
					} else {
						o[co] = "";
					}
					break;
				}
				case _AGB_OP_STR_BASE64_DECODE: {
					if (o[co] != null) {
						try {
							o[co] = new String(Base64.decode((String) o[co]), "utf-8");
						} catch (Exception e) {
							o[co] = "";
						}
					} else {
						o[co] = "";
					}
					break;
				}
				case _AGB_OP_STR_UTF_ENCODE: {
					break;
				}
				case _AGB_OP_STR_UTF_DECODE: {
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_THROW: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_THROW_TRY: {
					AGBThrow t = exs[++ex];
					t.co = co;
					t.pc = bytecodes[pc++];
					t.st = cpcs;
					break;
				}
				case _AGB_OP_THROW_TRUE: {
					ex--;
					break;
				}
				case _AGB_OP_THROW_FALSE: {
					if (ex == 0) {
						throw new RuntimeException("uncatched exception");
					}
					AGBThrow agbThrow = exs[ex];
					int index = agbThrow.co + 1;
					o[index] = o[co];
					co = index;
					pc = agbThrow.pc;
					cpcs = agbThrow.st;
					ex--;
					break;
				}
				case _AGB_OP_THROW_JUMP: {
					Object value = o[co];
					int cindex = (Integer) ((Object[]) value)[0];
					if (value != null && cast[cindex * classeSize + bytecodes[pc++]]) {
						pc = bytecodes[pc];
					} else {
						pc++;
					}
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_ARRAY: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_ARRAY_NEW: {
					int index = ((Double) o[co]).intValue();
					if (index < 0) {
						index = 0;
					}
					o[co] = new ArrayList<Object>(index);
					break;
				}
				case _AGB_OP_ARRAY_LEN: {
					o[co] = Double.valueOf(((List<?>) o[co]).size());
					break;
				}
				case _AGB_OP_ARRAY_ADD: {
					co--;
					((List) o[co]).add(o[co + 1]);
					break;
				}
				case _AGB_OP_ARRAY_INSERT: {
					co -= 2;
					List<Object> list = (List) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index < 0) {
						index = 0;
					} else if (index > list.size()) {
						index = list.size();
					}
					list.add(index, o[co + 2]);
					break;
				}
				case _AGB_OP_ARRAY_GET: {
					co--;
					List list = (List) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index < 0 || index >= list.size()) {
						o[co] = null;
					} else {
						o[co] = list.get(index);
					}
					break;
				}
				case _AGB_OP_ARRAY_SET: {
					co -= 2;
					((List) o[co]).set(((Double) o[co + 1]).intValue() - 1, o[co + 2]);
					break;
				}
				case _AGB_OP_ARRAY_REM: {
					co--;
					((List) o[co]).remove(((Double) o[co + 1]).intValue() - 1);
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_NUMS: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_NUMS_NEW: {
					int auxint = ((Double) o[co]).intValue();
					Double[] object = new Double[auxint];
					Double value = Double.valueOf(0.);
					for (int n = 0; n < auxint; n++) {
						object[n] = value;
					}
					o[co] = object;
					break;
				}
				case _AGB_OP_NUMS_SIZE: {
					o[co] = Double.valueOf(((Double[]) o[co]).length);
					break;
				}
				case _AGB_OP_NUMS_GET: {
					co--;
					Double[] object = (Double[]) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index < 0 || index >= object.length) {
						o[co] = Double.valueOf(0.);
					} else {
						o[co] = object[index];
					}
					break;
				}
				case _AGB_OP_NUMS_SET: {
					co -= 2;
					Double[] object = (Double[]) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index >= 0 && index < object.length) {
						object[index] = (Double) o[co + 2];
					}
					break;
				}
				case _AGB_OP_NUMS_SETS: {
					co -= 3;
					Double[] object = (Double[]) o[co];
					int begin = ((Double) o[co + 1]).intValue() - 1;
					int end = ((Double) o[co + 2]).intValue() - 1;
					Double value = (Double) o[co + 3];
					if (begin < 0) {
						begin = 0;
					}
					if (end >= object.length) {
						end = object.length - 1;
					}
					if (begin <= end) {
						if (begin <= object.length - 1) {
							int size = end - begin;
							for (int n = 0; n <= size; n++) {
								object[begin + n] = value;
							}
						}
					}
					break;
				}
				case _AGB_OP_NUMS_COPY: {
					co -= 4;
					Double[] auxnums = (Double[]) o[co];
					int begin = ((Double) o[co + 1]).intValue() - 1;
					int end = ((Double) o[co + 2]).intValue() - 1;
					Double[] auxnums2 = (Double[]) o[co + 3];
					Double auxint3 = (Double) o[co + 4];
					if (auxint3 < 1) {
						begin += 1 - auxint3;
						auxint3 = 1.;
					}
					if (begin < 1) {
						begin = 1;
					}
					if (end > auxnums.length) {
						end = auxnums.length;
					}
					if (begin <= end) {
						if (begin <= auxnums.length) {
							int size = end - begin + 1;
							int size2 = (int) (auxnums2.length - auxint3 + 1);
							size = size < size2 ? size : size2;
							if (size > 0) {
								for (int n = 0; n < size; n++) {
									auxnums2[(int) (auxint3 - 1 + n)] = auxnums[begin - 1 + n];
								}
							}
						}
					}
					break;
				}
				case _AGB_OP_NUMS_EQUAL: {
					co--;
					Double[] auxnums = (Double[]) o[co];
					Double[] auxnums2 = (Double[]) o[co + 1];
					if (auxnums == null) {
						o[co] = auxnums2 == null ? Boolean.TRUE : Boolean.FALSE;
					} else if (auxnums2 == null) {
						o[co] = Boolean.FALSE;
					} else if (auxnums.length != auxnums2.length) {
						o[co] = Boolean.FALSE;
					} else {
						o[co] = Boolean.TRUE;
						for (int n = 0; n <= auxnums.length; n++) {
							if (auxnums2[n].equals(auxnums[n])) {
								o[co] = Boolean.FALSE;
								break;
							}
						}
						o[co] = Boolean.FALSE;
					}
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_BOOLS: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_BOOLS_NEW: {
					int auxint = ((Double) o[co]).intValue();
					Boolean[] object = new Boolean[auxint];
					Boolean value = Boolean.FALSE;
					for (int n = 0; n < auxint; n++) {
						object[n] = value;
					}
					o[co] = object;
					break;
				}
				case _AGB_OP_BOOLS_SIZE: {
					o[co] = Double.valueOf(((Boolean[]) o[co]).length);
					break;
				}
				case _AGB_OP_BOOLS_GET: {
					co--;
					Boolean[] object = (Boolean[]) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index < 0 || index >= object.length) {
						o[co] = Boolean.FALSE;
					} else {
						o[co] = object[index];
					}
					break;
				}
				case _AGB_OP_BOOLS_SET: {
					co -= 2;
					Boolean[] object = (Boolean[]) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index >= 0 && index < object.length) {
						object[index] = (Boolean) o[co + 2];
					}
					break;
				}
				case _AGB_OP_BOOLS_SETS: {
					co -= 3;
					Boolean[] object = (Boolean[]) o[co];
					int begin = ((Double) o[co + 1]).intValue() - 1;
					int end = ((Double) o[co + 2]).intValue() - 1;
					Boolean value = (Boolean) o[co + 3];
					if (begin < 0) {
						begin = 0;
					}
					if (end >= object.length) {
						end = object.length - 1;
					}
					if (begin <= end) {
						if (begin <= object.length - 1) {
							int size = end - begin;
							for (int n = 0; n <= size; n++) {
								object[begin + n] = value;
							}
						}
					}
					break;
				}
				case _AGB_OP_BOOLS_COPY: {
					co -= 4;
					Boolean[] auxnums = (Boolean[]) o[co];
					int begin = ((Double) o[co + 1]).intValue() - 1;
					int end = ((Double) o[co + 2]).intValue() - 1;
					Boolean[] auxnums2 = (Boolean[]) o[co + 3];
					Double auxint3 = (Double) o[co + 4];
					if (auxint3 < 1) {
						begin += 1 - auxint3;
						auxint3 = 1.;
					}
					if (begin < 1) {
						begin = 1;
					}
					if (end > auxnums.length) {
						end = auxnums.length;
					}
					if (begin <= end) {
						if (begin <= auxnums.length) {
							int size = end - begin + 1;
							int size2 = (int) (auxnums2.length - auxint3 + 1);
							size = size < size2 ? size : size2;
							if (size > 0) {
								for (int n = 0; n < size; n++) {
									auxnums2[(int) (auxint3 - 1 + n)] = auxnums[begin - 1 + n];
								}
							}
						}
					}
					break;
				}
				case _AGB_OP_BOOLS_EQUAL: {
					co--;
					Boolean[] auxnums = (Boolean[]) o[co];
					Boolean[] auxnums2 = (Boolean[]) o[co + 1];
					if (auxnums == null) {
						o[co] = auxnums2 == null ? Boolean.TRUE : Boolean.FALSE;
					} else if (auxnums2 == null) {
						o[co] = Boolean.FALSE;
					} else if (auxnums.length != auxnums2.length) {
						o[co] = Boolean.FALSE;
					} else {
						o[co] = Boolean.TRUE;
						for (int n = 0; n <= auxnums.length; n++) {
							if (auxnums2[n] == auxnums[n]) {
								o[co] = Boolean.FALSE;
								break;
							}
						}
						o[co] = Boolean.FALSE;
					}
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_BYTES: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_BYTES_NEW: {
					int auxint = ((Double) o[co]).intValue();
					Double[] object = new Double[auxint];
					Double value = Double.valueOf(0.);
					for (int n = 0; n < auxint; n++) {
						object[n] = value;
					}
					o[co] = object;
					break;
				}
				case _AGB_OP_BYTES_SIZE: {
					o[co] = Double.valueOf(((Double[]) o[co]).length);
					break;
				}
				case _AGB_OP_BYTES_GET: {
					co--;
					Double[] object = (Double[]) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index < 0 || index >= object.length) {
						o[co] = Double.valueOf(0.);
					} else {
						o[co] = object[index];
					}
					break;
				}
				case _AGB_OP_BYTES_SET: {
					co -= 2;
					Double[] object = (Double[]) o[co];
					int index = ((Double) o[co + 1]).intValue() - 1;
					if (index >= 0 && index < object.length) {
						object[index] = (Double) o[co + 2];
					}
					break;
				}
				case _AGB_OP_BYTES_SETS: {
					co -= 3;
					Double[] object = (Double[]) o[co];
					int begin = ((Double) o[co + 1]).intValue() - 1;
					int end = ((Double) o[co + 2]).intValue() - 1;
					Double value = (Double) o[co + 3];
					if (begin < 0) {
						begin = 0;
					}
					if (end >= object.length) {
						end = object.length - 1;
					}
					if (begin <= end) {
						if (begin <= object.length - 1) {
							int size = end - begin;
							for (int n = 0; n <= size; n++) {
								object[begin + n] = value;
							}
						}
					}
					break;
				}
				case _AGB_OP_BYTES_COPY: {
					co -= 4;
					Double[] auxnums = (Double[]) o[co];
					int begin = ((Double) o[co + 1]).intValue() - 1;
					int end = ((Double) o[co + 2]).intValue() - 1;
					Double[] auxnums2 = (Double[]) o[co + 3];
					Double auxint3 = (Double) o[co + 4];
					if (auxint3 < 1) {
						begin += 1 - auxint3;
						auxint3 = 1.;
					}
					if (begin < 1) {
						begin = 1;
					}
					if (end > auxnums.length) {
						end = auxnums.length;
					}
					if (begin <= end) {
						if (begin <= auxnums.length) {
							int size = end - begin + 1;
							int size2 = (int) (auxnums2.length - auxint3 + 1);
							size = size < size2 ? size : size2;
							if (size > 0) {
								for (int n = 0; n < size; n++) {
									auxnums2[(int) (auxint3 - 1 + n)] = auxnums[begin - 1 + n];
								}
							}
						}
					}
					break;
				}
				case _AGB_OP_BYTES_EQUAL: {
					co--;
					Double[] auxnums = (Double[]) o[co];
					Double[] auxnums2 = (Double[]) o[co + 1];
					if (auxnums == null) {
						o[co] = auxnums2 == null ? Boolean.TRUE : Boolean.FALSE;
					} else if (auxnums2 == null) {
						o[co] = Boolean.FALSE;
					} else if (auxnums.length != auxnums2.length) {
						o[co] = Boolean.FALSE;
					} else {
						o[co] = Boolean.TRUE;
						for (int n = 0; n <= auxnums.length; n++) {
							if (auxnums2[n].equals(auxnums[n])) {
								o[co] = Boolean.FALSE;
								break;
							}
						}
						o[co] = Boolean.FALSE;
					}
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			case _AGB_GP_CONSOLE: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_CONSOLE_PRINT_STR: {
					if (o[co] == null) {
						output.write("null".getBytes());
					} else {
						output.write(((String) o[co]).getBytes("utf-8"));
					}
					break;
				}
				case _AGB_OP_CONSOLE_PRINT_NUM: {
					double value = ((Double) o[co]);
					if (value == (int) value) {
						output.write(Integer.valueOf((int) value).toString().getBytes());
					} else {
						output.write(Double.valueOf(value).toString().getBytes());
					}
					break;
				}
				case _AGB_OP_CONSOLE_PRINT_BOOL: {
					if (o[co] == Boolean.TRUE) {
						output.write("true".getBytes());
					} else {
						output.write("false".getBytes());
					}
					break;
				}
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			// case _AGB_GP_NET: {
			// switch (bytecodes[pc++]) {
			// case _AGB_OP_NET_REQUEST: {
			// oobj(-2) = agb_library_net_request(thread,
			// agb_library_string_chars(oobj(-2)), onum(-1),
			// agb_library_string_chars(oobj(0)));
			// cosdecn(2);
			// break;
			// }
			// default: {
			// throw new IllegalArgumentException("unknow bytecode");
			// }
			// }
			// break;
			// }
			case _AGB_GP_VM: {
				switch (bytecodes[pc++]) {
				case _AGB_OP_VM_HALF: {
					return null;
				}
				case _AGB_OP_VM_GC: {
					System.gc();
					break;
				}
				// case _AGB_OP_VM_ALLOC: {
				// obool(-2) = agb_library_vm_alloc(thread,
				// agb_library_bytes_to_uchars(oobj(-2)), onum(-1), onum(0));
				// cosdecn(2);
				// break;
				// }
				// case _AGB_OP_VM_PAINT: {
				// agb_library_vm_paint(thread, onum(-2), onum(-1));
				// obool(-2) = 1;
				// cosdecn( 2);
				// break;
				// }
				// case _AGB_OP_VM_EVENT_KEY: {
				// agb_library_sdl_push_event_key(thread, onum(-1), onum(0));
				// obool(-1) = 1;
				// co--;
				// break;
				// }
				// case _AGB_OP_VM_EVENT_MOUSE: {
				// agb_library_sdl_push_event_mouse(thread, onum(-2), onum(-1),
				// onum(0));
				// obool(-2) = 1;
				// cosdecn( 2);
				// break;
				// }
				// case _AGB_OP_VM_EVENT_WHEEL: {
				// agb_library_sdl_push_event_wheel(thread, onum(-4), onum(-3),
				// onum(-2), onum(-1), onum(0));
				// obool(-4) = 1;
				// cosdecn( 4);
				// break;
				// }
				default: {
					throw new IllegalArgumentException("unknow bytecode");
				}
				}
				break;
			}
			default: {
				throw new IllegalArgumentException("unknow bytecode");
			}
			}
		}
	}

	public int getOpcodeIndex(String classname, String methodname) {
		for (int n = 0; n < this.classes.length; n++) {
			AGBClass c = this.classes[n];
			if (c.name.equals(classname)) {
				for (int m = 0; m < c.methodCount; m++) {
					AGBMethod method = c.methods[m];
					if (method.name.equals(methodname)) {
						return method.pc;
					}
				}
				break;
			}
		}
		return -1;
	}

	/**
	 * @return the output
	 */
	public ByteArrayOutputStream getOutputStream() {
		return output;
	}

	/**
	 * Testador
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws AGBException
	 */
	public static void main(String[] args) throws IOException, AGBException {
		String code = "class Main do static this main() do new Main() return this end this Main () do |log.prints,\"a\"| return this end end";
		InputStream input = new ByteArrayInputStream(code.getBytes("utf8"));
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		AGBCompiler.compile(bytes, input);
		InputStream binaryInput = new ByteArrayInputStream(bytes.toByteArray());
		AGBVm vm = new AGBVm(binaryInput);
		vm.execute(0);
		System.out.println(new String(vm.getOutputStream().toByteArray(), "utf-8"));
	}
}
