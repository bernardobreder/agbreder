package com.agbreder.compiler.disassembler;

import com.agbreder.compiler.link.OpcodeOutputStream;

/**
 * Representa uma instrução
 * 
 * @author bernardobreder
 */
public class AGBInstruction {

	/** Conjunto vazio */
	private static final int[] EMPTY = new int[0];

	/** Group */
	private final int group;

	/** Opcode */
	private final int opcode;

	/** Data */
	private final int[] data;

	/**
	 * Construtor
	 * 
	 * @param group
	 * @param opcode
	 */
	public AGBInstruction(int group, int opcode) {
		super();
		this.group = group;
		this.opcode = opcode;
		this.data = EMPTY;
	}

	/**
	 * Construtor
	 * 
	 * @param group
	 * @param opcode
	 * @param data
	 */
	public AGBInstruction(int group, int opcode, int... data) {
		super();
		this.group = group;
		this.opcode = opcode;
		this.data = data;
	}

	/**
	 * Retorna o tamanho da instrução
	 * 
	 * @return tamanho
	 */
	public int size() {
		return 2 + this.data.length;
	}

	/**
	 * @return the group
	 */
	public int getGroup() {
		return group;
	}

	/**
	 * @return the opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * @return the data
	 */
	public int[] getData() {
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String opcode = "?";
		switch (this.getGroup()) {
		case OpcodeOutputStream._AGB_GP_VM: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_VM_HALF:
				opcode = "vm.half";
				break;
			case OpcodeOutputStream._AGB_OP_VM_GC:
				opcode = "vm.gc";
				break;
			case OpcodeOutputStream._AGB_OP_VM_ALLOC:
				opcode = "vm.alloc";
				break;
			case OpcodeOutputStream._AGB_OP_VM_PAINT:
				opcode = "vm.paint";
				break;
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_STACK: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_STACK_PUSH:
				opcode = "stack.push";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_POP:
				opcode = "stack.pop";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_LOAD:
				opcode = "stack.load";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_STORE:
				opcode = "stack.store";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_PRE_INC:
				opcode = "stack.preinc";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_PRE_DEC:
				opcode = "stack.predec";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_POS_INC:
				opcode = "stack.posinc";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_POS_DEC:
				opcode = "stack.posdec";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_FOR_INC_BEGIN:
				opcode = "stack.for.inc.begin";
				break;
			case OpcodeOutputStream._AGB_OP_STACK_FOR_INC_END:
				opcode = "stack.for.inc.end";
				break;
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_LOAD: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_LOAD_TRUE: {
				opcode = "load.true";
				break;
			}
			case OpcodeOutputStream._AGB_OP_LOAD_FALSE: {
				opcode = "load.false";
				break;
			}
			case OpcodeOutputStream._AGB_OP_LOAD_NUM: {
				opcode = "load.num";
				break;
			}
			case OpcodeOutputStream._AGB_OP_LOAD_STR: {
				opcode = "load.str";
				break;
			}
			case OpcodeOutputStream._AGB_OP_LOAD_NULL: {
				opcode = "load.null";
				break;
			}
			case OpcodeOutputStream._AGB_OP_LOAD_NEW: {
				opcode = "load.new";
				break;
			}
			case OpcodeOutputStream._AGB_OP_LOAD_FUNC: {
				opcode = "load.func";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_JUMP: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_JUMP_INT: {
				opcode = "jump.int";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_TRUE: {
				opcode = "jump.true";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_FALSE: {
				opcode = "jump.false";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_CALL: {
				opcode = "jump.object.call";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_STATIC_CALL: {
				opcode = "jump.static.call";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_RETURN: {
				opcode = "jump.return";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_TRUE_DUP: {
				opcode = "jump.true.dup";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_FALSE_DUP: {
				opcode = "jump.false.dup";
				break;
			}
			case OpcodeOutputStream._AGB_OP_JUMP_FUNC_CALL: {
				opcode = "jump.func.call";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_OBJECT: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_OBJECT_CAST_CLASS: {
				opcode = "object.cast.class";
				break;
			}
			case OpcodeOutputStream._AGB_OP_OBJECT_CAST_NATIVE: {
				opcode = "object.cast.native";
				break;
			}
			case OpcodeOutputStream._AGB_OP_OBJECT_GET: {
				opcode = "object.get";
				break;
			}
			case OpcodeOutputStream._AGB_OP_OBJECT_SET: {
				opcode = "object.set";
				break;
			}
			case OpcodeOutputStream._AGB_OP_OBJECT_EQUAL: {
				opcode = "object.equal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_OBJECT_NOTEQUAL: {
				opcode = "object.nequal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_OBJECT_HASHCODE: {
				opcode = "object.hashcode";
				break;
			}
			case OpcodeOutputStream._AGB_OP_OBJECT_CLASSNAME: {
				opcode = "object.classname";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_BOOL: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_BOOL_OR: {
				opcode = "bool.or";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOL_AND: {
				opcode = "bool.and";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOL_NOT: {
				opcode = "bool.not";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOL_EQUAL: {
				opcode = "bool.equal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOL_NEQUAL: {
				opcode = "bool.nequal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOL_TO_STR: {
				opcode = "bool.str";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_NUM: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_NUM_NEG: {
				opcode = "num.neg";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_SUM: {
				opcode = "num.sum";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_SUB: {
				opcode = "num.sub";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_MUL: {
				opcode = "num.mul";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_DIV: {
				opcode = "num.div";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_EQUAL: {
				opcode = "num.equal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_NEQUAL: {
				opcode = "num.nequal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_GT: {
				opcode = "num.gt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_EGT: {
				opcode = "num.egt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_LT: {
				opcode = "num.lt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_ELT: {
				opcode = "num.elt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_TO_STR: {
				opcode = "num.to.str";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_MOD: {
				opcode = "num.mod";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_AND_BIT: {
				opcode = "num.and.bir";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_OR_BIT: {
				opcode = "num.or.bit";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_SHIFT_LEFT: {
				opcode = "num.shift.left";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_SHIFT_RIGHT: {
				opcode = "num.shift.right";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUM_TO_INT: {
				opcode = "num.to.int";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_STR: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_STR_LEN: {
				opcode = "str.len";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_SUM: {
				opcode = "str.sum";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_EQUAL: {
				opcode = "str.equal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_NEQUAL: {
				opcode = "str.nequal";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_GT: {
				opcode = "str.gt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_EGT: {
				opcode = "str.egt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_LT: {
				opcode = "str.lt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_ELT: {
				opcode = "str.elt";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_SUBSTRING: {
				opcode = "str.substring";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_CODE_TO_CHAR: {
				opcode = "str.code_to_char";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_CHAR_TO_CODE: {
				opcode = "str.char_to_code";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_TRIM: {
				opcode = "str.trim";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_CHAR_AT: {
				opcode = "str.charat";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_START_WITH: {
				opcode = "str.startwith";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_END_WITH: {
				opcode = "str.endwith";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_INDEX_OF: {
				opcode = "str.indexof";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_LAST_INDEX_OF: {
				opcode = "str.lastindexof";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_REPLACE: {
				opcode = "str.replace";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_TO_LOWER_CASE: {
				opcode = "str.tolowercase";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_TO_UPPER_CASE: {
				opcode = "str.touppercase";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_INDEX_OF_N: {
				opcode = "str.indexof.begin";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_BASE64_ENCODE: {
				opcode = "str.base64.encode";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_BASE64_DECODE: {
				opcode = "str.base64.decode";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_UTF_ENCODE: {
				opcode = "str.utf.encode";
				break;
			}
			case OpcodeOutputStream._AGB_OP_STR_UTF_DECODE: {
				opcode = "str.utf.decode";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_ARRAY: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_ARRAY_NEW: {
				opcode = "array.new";
				break;
			}
			case OpcodeOutputStream._AGB_OP_ARRAY_LEN: {
				opcode = "array.length";
				break;
			}
			case OpcodeOutputStream._AGB_OP_ARRAY_ADD: {
				opcode = "array.add";
				break;
			}
			case OpcodeOutputStream._AGB_OP_ARRAY_INSERT: {
				opcode = "array.insert";
				break;
			}
			case OpcodeOutputStream._AGB_OP_ARRAY_GET: {
				opcode = "array.get";
				break;
			}
			case OpcodeOutputStream._AGB_OP_ARRAY_SET: {
				opcode = "array.set";
				break;
			}
			case OpcodeOutputStream._AGB_OP_ARRAY_REM: {
				opcode = "array.remove";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_INTS: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_INTS_NEW: {
				opcode = "ints.alloc";
				break;
			}
			case OpcodeOutputStream._AGB_OP_INTS_SIZE: {
				opcode = "ints.size";
				break;
			}
			case OpcodeOutputStream._AGB_OP_INTS_GET: {
				opcode = "ints.get";
				break;
			}
			case OpcodeOutputStream._AGB_OP_INTS_SET: {
				opcode = "ints.set";
				break;
			}
			case OpcodeOutputStream._AGB_OP_INTS_SETS: {
				opcode = "ints.sets";
				break;
			}
			case OpcodeOutputStream._AGB_OP_INTS_COPY: {
				opcode = "ints.copy";
				break;
			}
			case OpcodeOutputStream._AGB_OP_INTS_EQUAL: {
				opcode = "ints.equal";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_NUMS: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_NUMS_NEW: {
				opcode = "nums.alloc";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUMS_SIZE: {
				opcode = "nums.size";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUMS_GET: {
				opcode = "nums.get";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUMS_SET: {
				opcode = "nums.set";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUMS_SETS: {
				opcode = "nums.sets";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUMS_COPY: {
				opcode = "nums.copy";
				break;
			}
			case OpcodeOutputStream._AGB_OP_NUMS_EQUAL: {
				opcode = "nums.equal";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_BOOLS: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_BOOLS_NEW: {
				opcode = "bools.alloc";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOLS_SIZE: {
				opcode = "bools.size";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOLS_GET: {
				opcode = "bools.get";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOLS_SET: {
				opcode = "bools.set";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOLS_SETS: {
				opcode = "bools.sets";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOLS_COPY: {
				opcode = "bools.copy";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BOOLS_EQUAL: {
				opcode = "bools.equal";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_BYTES: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_BYTES_NEW: {
				opcode = "bytes.alloc";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BYTES_SIZE: {
				opcode = "bytes.size";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BYTES_GET: {
				opcode = "bytes.get";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BYTES_SET: {
				opcode = "bytes.set";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BYTES_SETS: {
				opcode = "bytes.sets";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BYTES_COPY: {
				opcode = "bytes.copy";
				break;
			}
			case OpcodeOutputStream._AGB_OP_BYTES_EQUAL: {
				opcode = "bytes.equal";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_UINT2D: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_UINT2D_NEW: {
				opcode = "uint2d.alloc";
				break;
			}
			case OpcodeOutputStream._AGB_OP_UINT2D_LINS: {
				opcode = "uint2d.lins";
				break;
			}
			case OpcodeOutputStream._AGB_OP_UINT2D_COLS: {
				opcode = "uint2d.cols";
				break;
			}
			case OpcodeOutputStream._AGB_OP_UINT2D_GET: {
				opcode = "uint2d.get";
				break;
			}
			case OpcodeOutputStream._AGB_OP_UINT2D_SET: {
				opcode = "uint2d.set";
				break;
			}
			case OpcodeOutputStream._AGB_OP_UINT2D_SETS: {
				opcode = "uint2d.sets";
				break;
			}
			case OpcodeOutputStream._AGB_OP_UINT2D_COPY: {
				opcode = "uint2d.copy";
				break;
			}
			case OpcodeOutputStream._AGB_OP_UINT2D_EQUAL: {
				opcode = "uint2d.equal";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_THROW: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_THROW_TRY: {
				opcode = "throw.try";
				break;
			}
			case OpcodeOutputStream._AGB_OP_THROW_OBJECT: {
				opcode = "throw.object";
				break;
			}
			case OpcodeOutputStream._AGB_OP_THROW_TRUE: {
				opcode = "throw.true";
				break;
			}
			case OpcodeOutputStream._AGB_OP_THROW_FALSE: {
				opcode = "throw.false";
				break;
			}
			case OpcodeOutputStream._AGB_OP_THROW_STORE: {
				opcode = "throw.store";
				break;
			}
			case OpcodeOutputStream._AGB_OP_THROW_JUMP: {
				opcode = "throw.jump";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_CONSOLE: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_STR: {
				opcode = "log.prints";
				break;
			}
			case OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_NUM: {
				opcode = "log.printn";
				break;
			}
			case OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_BOOL: {
				opcode = "log.printb";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_SDL: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_SDL_ERROR: {
				opcode = "sdl.error";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_INIT: {
				opcode = "sdl.init";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_QUIT: {
				opcode = "sdl.quit";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_VIDEO: {
				opcode = "sdl.video";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_CONSTANT: {
				opcode = "sdl.constant";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_VIDEO_WIDTH: {
				opcode = "sdl.width";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_VIDEO_HEIGHT: {
				opcode = "sdl.height";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_SCREEN_WIDTH: {
				opcode = "sdl.screen_width";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_SCREEN_HEIGHT: {
				opcode = "sdl.screen_height";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_POLL_EVENT: {
				opcode = "sdl.event.pool";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_WAIT_EVENT: {
				opcode = "sdl.event.wait";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_TYPE_EVENT: {
				opcode = "sdl.event.type";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_KEYCODE_EVENT: {
				opcode = "sdl.event.keycode";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_KEYCHAR_EVENT: {
				opcode = "sdl.event.keychar";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_LOCK: {
				opcode = "sdl.lock";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_UNLOAD: {
				opcode = "sdl.unlock";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_UPDATE: {
				opcode = "sdl.update";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_TICK: {
				opcode = "sdl.tick";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_DELAY: {
				opcode = "sdl.delay";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_REPAINT: {
				opcode = "sdl.repaint";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_USERCODE: {
				opcode = "sdl.event.usercode";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_X: {
				opcode = "sdl.event.mouse.motion.x";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_Y: {
				opcode = "sdl.event.mouse.motion.y";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_X: {
				opcode = "sdl.event.mouse.action.x";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_Y: {
				opcode = "sdl.event.mouse.action.y";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON: {
				opcode = "sdl.event.mouse.action.button";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_X: {
				opcode = "sdl.event.mouse.wheel.x";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_Y: {
				opcode = "sdl.event.mouse.wheel.y";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_DRAW_RECT: {
				opcode = "sdl.draw.rect";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_DRAW_CIRCLE: {
				opcode = "sdl.draw.circle";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_DRAW_STRING: {
				opcode = "sdl.draw.string";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_FONT_WIDTH: {
				opcode = "sdl.font.width";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_FONT_HEIGHT: {
				opcode = "sdl.font.height";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_FILL_RECT: {
				opcode = "sdl.fill.rect";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_PUSH_GRAPHIC: {
				opcode = "sdl.push_graphic";
				break;
			}
			case OpcodeOutputStream._AGB_OP_SDL_POP_GRAPHIC: {
				opcode = "sdl.pop_graphic";
				break;
			}
			}
			break;
		}
		case OpcodeOutputStream._AGB_GP_NET: {
			switch (this.getOpcode()) {
			case OpcodeOutputStream._AGB_OP_NET_REQUEST: {
				opcode = "net.request";
				break;
			}
			}
			break;
		}
		}
		sb.append(opcode);
		if (data.length > 0) {
			sb.append(" ");
			for (int n = 0; n < data.length; n++) {
				sb.append(data[n]);
				if (n != data.length - 1) {
					sb.append(", ");
				}
			}
		}
		return sb.toString();
	}

}
