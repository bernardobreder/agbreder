package com.agbreder.compiler.parser.node;

import java.util.Arrays;
import java.util.List;

import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.node.type.AGBBooleanArrayTypeNode;
import com.agbreder.compiler.parser.node.type.AGBBooleanTypeNode;
import com.agbreder.compiler.parser.node.type.AGBByteArrayTypeNode;
import com.agbreder.compiler.parser.node.type.AGBByteTypeNode;
import com.agbreder.compiler.parser.node.type.AGBNumberArrayTypeNode;
import com.agbreder.compiler.parser.node.type.AGBNumberTypeNode;
import com.agbreder.compiler.parser.node.type.AGBObjectTypeNode;
import com.agbreder.compiler.parser.node.type.AGBStringTypeNode;
import com.agbreder.compiler.parser.node.type.AGBThisTypeNode;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Tipos de operaóóes nativas
 * 
 * @author bernardobreder
 */
public enum AGBNativeEnum {

	/**
	 * Imprime uma String
	 */
	VM_HALF("vm.half", OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_HALF, AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime uma String
	 */
	VM_GC("vm.gc", OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_GC, AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime uma String
	 */
	VM_ALLOC("vm.alloc", OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_ALLOC, AGBBooleanTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime uma String
	 */
	VM_PAINT("vm.paint", OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_PAINT, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime uma String
	 */
	VM_EVENT_KEY("vm.event_key", OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_EVENT_KEY, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime uma String
	 */
	VM_EVENT_MOUSE("vm.event_mouse", OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_EVENT_MOUSE, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime uma String
	 */
	VM_EVENT_WHEEL("vm.event_wheel", OpcodeOutputStream._AGB_GP_VM, OpcodeOutputStream._AGB_OP_VM_EVENT_WHEEL, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime uma String
	 */
	LOG_PRINTS("log.prints", OpcodeOutputStream._AGB_GP_CONSOLE, OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_STR, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um nómero
	 */
	LOG_PRINTN("log.printn", OpcodeOutputStream._AGB_GP_CONSOLE, OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_NUM, AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	LOG_PRINTB("log.printb", OpcodeOutputStream._AGB_GP_CONSOLE, OpcodeOutputStream._AGB_OP_CONSOLE_PRINT_BOOL, AGBBooleanTypeNode.getInstance(), AGBBooleanTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	OBJECT_HASHCODE("object.hashcode", OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_HASHCODE, AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	OBJECT_CLASSNAME("object.classname", OpcodeOutputStream._AGB_GP_OBJECT, OpcodeOutputStream._AGB_OP_OBJECT_CLASSNAME, AGBStringTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_LEN("string.len", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_LEN, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_CONCAT("string.concat", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_SUM, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_EQUAL("string.equal", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_EQUAL, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_NOTEQUAL("string.nequal", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_NEQUAL, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_GREATER("string.gt", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_GT, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_GREATEREQUAL("string.egt", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_EGT, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_LOWER("string.lt", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_LT, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_LOWEREQUAL("string.elt", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_ELT, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_SUBSTRING("string.substring", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_SUBSTRING, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_CODE_TO_CHAR("string.code_to_char", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_CODE_TO_CHAR, AGBStringTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_CHAR_T0_CODE("string.char_to_code", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_CHAR_TO_CODE, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_TRIM("string.trim", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_TRIM, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_CHAR_AT("string.charat", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_CHAR_AT, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_START_WITH("string.startwith", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_START_WITH, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_END_WITH("string.endwith", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_END_WITH, AGBBooleanTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_INDEX_OF("string.indexof", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_INDEX_OF, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_INDEX_OF_N("string.indexofn", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_INDEX_OF_N, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_LAST_INDEX_OF("string.lastindexof", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_LAST_INDEX_OF, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_REPLACE("string.replace", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_REPLACE, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_TO_LOWER_CASE("string.tolowercase", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_TO_LOWER_CASE, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_TO_UPPER_CASE("string.touppercase", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_TO_UPPER_CASE, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_BASE64_ENCODE("string.base64_encode", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_BASE64_ENCODE, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_BASE64_DECODE("string.base64_decode", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_BASE64_DECODE, AGBStringTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_UTF_ENCODE("string.utf_encode", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_UTF_ENCODE, AGBStringTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	STRING_UTF_DECODE("string.utf_decode", OpcodeOutputStream._AGB_GP_STR, OpcodeOutputStream._AGB_OP_STR_UTF_DECODE, AGBByteArrayTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLEAN_TO_STRING("boolean.tostr", OpcodeOutputStream._AGB_GP_BOOL, OpcodeOutputStream._AGB_OP_BOOL_TO_STR, AGBStringTypeNode.getInstance(), AGBBooleanTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMBER_TO_STRING("number.tostr", OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_TO_STR, AGBStringTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMBER_TO_INTEGER("number.toint", OpcodeOutputStream._AGB_GP_NUM, OpcodeOutputStream._AGB_OP_NUM_TO_STR, AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	ARRAY_NEW("array.alloc", OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_NEW, AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	ARRAY_LEN("array.size", OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_LEN, AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	ARRAY_ADD("array.add", OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_ADD, AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	ARRAY_INSERT("array.insert", OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_INSERT, AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	ARRAY_GET("array.get", OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_GET, AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	ARRAY_SET("array.set", OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_SET, AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	ARRAY_REM("array.remove", OpcodeOutputStream._AGB_GP_ARRAY, OpcodeOutputStream._AGB_OP_ARRAY_REM, AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	// /**
	// * Imprime um valor Lógico
	// */
	// INTS_NEW( "ints.alloc",
	// OpcodeOutputStream._AGB_GP_INTS,
	// OpcodeOutputStream._AGB_OP_INTS_NEW,
	// AGBIntegerArrayTypeNode.getInstance(),
	// AGBNumberTypeNode.getInstance()),
	//
	// /**
	// * Imprime um valor Lógico
	// */
	// INTS_SIZE("ints.size",
	// OpcodeOutputStream._AGB_GP_INTS,
	// OpcodeOutputStream._AGB_OP_INTS_SIZE,
	// AGBNumberTypeNode.getInstance(),
	// AGBIntegerArrayTypeNode.getInstance()),
	//
	// /**
	// * Imprime um valor Lógico
	// */
	// INTS_GET( "ints.get",
	// OpcodeOutputStream._AGB_GP_INTS,
	// OpcodeOutputStream._AGB_OP_INTS_GET,
	// AGBNumberTypeNode.getInstance(),
	// AGBIntegerArrayTypeNode.getInstance(),
	// AGBNumberTypeNode.getInstance()),
	//
	// /**
	// * Imprime um valor Lógico
	// */
	// INTS_SET( "ints.set",
	// OpcodeOutputStream._AGB_GP_INTS,
	// OpcodeOutputStream._AGB_OP_INTS_SET,
	// AGBNumberTypeNode.getInstance(),
	// AGBIntegerArrayTypeNode.getInstance(),
	// AGBNumberTypeNode.getInstance(),
	// AGBNumberTypeNode.getInstance()),
	//
	// /**
	// * Imprime um valor Lógico
	// */
	// INTS_SETS( "ints.setS",
	// OpcodeOutputStream._AGB_GP_INTS,
	// OpcodeOutputStream._AGB_OP_INTS_SET,
	// AGBIntegerArrayTypeNode.getInstance(),
	// AGBIntegerArrayTypeNode.getInstance(),
	// AGBNumberTypeNode.getInstance(),
	// AGBNumberTypeNode.getInstance(),
	// AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMS_NEW("nums.alloc", OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_NEW, AGBNumberArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMS_SIZE("nums.size", OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_SIZE, AGBNumberTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMS_GET("nums.get", OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_GET, AGBNumberTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMS_SET("nums.set", OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_SET, AGBNumberTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMS_SETS("nums.sets", OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_SETS, AGBNumberArrayTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMS_COPY("nums.copy", OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_COPY, AGBNumberArrayTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NUMS_EQUAL("nums.equal", OpcodeOutputStream._AGB_GP_NUMS, OpcodeOutputStream._AGB_OP_NUMS_EQUAL, AGBBooleanTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance(), AGBNumberArrayTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLS_NEW("bools.alloc", OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_NEW, AGBBooleanArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLS_SIZE("bools.size", OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_SIZE, AGBNumberTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLS_GET("bools.get", OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_GET, AGBBooleanTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLS_SET("bools.set", OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_SET, AGBBooleanTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBBooleanTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLS_SETS("bools.sets", OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_SETS, AGBBooleanArrayTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBBooleanTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLS_COPY("bools.copy", OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_COPY, AGBBooleanArrayTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BOOLS_EQUAL("bools.equal", OpcodeOutputStream._AGB_GP_BOOLS, OpcodeOutputStream._AGB_OP_BOOLS_EQUAL, AGBBooleanTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance(), AGBBooleanArrayTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BYTES_ALLOC("bytes.alloc", OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_NEW, AGBByteArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BYTES_SIZE("bytes.size", OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_SIZE, AGBNumberTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BYTES_GET("bytes.get", OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_GET, AGBByteTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BYTES_SET("bytes.set", OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_SET, AGBByteTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBByteTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BYTES_SETS("bytes.sets", OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_SETS, AGBByteArrayTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBByteTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BYTES_COPY("bytes.copy", OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_COPY, AGBByteArrayTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	BYTES_EQUAL("bytes.equal", OpcodeOutputStream._AGB_GP_BYTES, OpcodeOutputStream._AGB_OP_BYTES_EQUAL, AGBBooleanTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance(), AGBByteArrayTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	UINT2D_NEW("uint2d.alloc", OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_NEW, AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	UINT2D_LINS("uint2d.lins", OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_LINS, AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	UINT2D_COLS("uint2d.cols", OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_COLS, AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	UINT2D_GET("uint2d.get", OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_GET, AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	UINT2D_SET("uint2d.set", OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_SET, AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	UINT2D_SETS("uint2d.sets", OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_SETS, AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	UINT2D_COPY("uint2d.copy", OpcodeOutputStream._AGB_GP_UINT2D, OpcodeOutputStream._AGB_OP_UINT2D_COPY, AGBObjectTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBObjectTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_ERROR("sdl.error", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_ERROR, AGBStringTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_INIT("sdl.init", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_INIT, AGBBooleanTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_QUIT("sdl.quit", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_QUIT, AGBThisTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_VIDEO("sdl.video", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_VIDEO, AGBBooleanTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_CONSTANT("sdl.constant", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_CONSTANT, AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_WIDTH("sdl.width", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_SCREEN_WIDTH, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_HEIGHT("sdl.height", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_SCREEN_HEIGHT, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_POOL_EVENT("sdl.event_poll", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_POLL_EVENT, AGBBooleanTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_WAIT_EVENT("sdl.event_wait", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_WAIT_EVENT, AGBThisTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_TYPE_EVENT("sdl.event_type", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_TYPE_EVENT, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_KEYCODE_EVENT("sdl.event_keycode", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_KEYCODE_EVENT, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_KEYCHAR_EVENT("sdl.event_keychar", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_KEYCHAR_EVENT, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_USERCODE_EVENT("sdl.event_usercode", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_USERCODE, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_EVENT_MOUSE_MOTION_X("sdl.event_mouse_motion_x", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_X, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_EVENT_MOUSE_MOTION_Y("sdl.event_mouse_motion_y", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_MOTION_Y, AGBNumberTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_EVENT_MOUSE_ACTION_X("sdl.event_mouse_action_x", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_X, AGBNumberTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_EVENT_MOUSE_ACTION_Y("sdl.event_mouse_action_y", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_Y, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_EVENT_MOUSE_WHEEL_X("sdl.event_mouse_wheel_x", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_X, AGBNumberTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_EVENT_MOUSE_WHEEL_Y("sdl.event_mouse_wheel_y", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_WHEEL_Y, AGBNumberTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_EVENT_MOUSE_ACTION_BUTTON("sdl.event_mouse_action_button", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON, AGBNumberTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_LOCK("sdl.lock", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_LOCK, AGBBooleanTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_UNLOCK("sdl.unlock", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_UNLOAD, AGBThisTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_UPDATE("sdl.update", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_UPDATE, AGBThisTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_TICK("sdl.tick", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_TICK, AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_DELAY("sdl.delay", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DELAY, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_DRAW_RECT("sdl.draw_rect", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DRAW_RECT, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_FILL_RECT("sdl.fill_rect", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_FILL_RECT, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_DRAW_CIRCLE("sdl.draw_circle", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DRAW_CIRCLE, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),
	/**
	 * Imprime um valor Lógico
	 */
	SDL_DRAW_STRING("sdl.draw_string", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_DRAW_STRING, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_FONT_WIDTH("sdl.font_width", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_FONT_WIDTH, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_FONT_HEIGHT("sdl.font_height", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_FONT_HEIGHT, AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_PUSH_GRAPHIC("sdl.push_graphic", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_PUSH_GRAPHIC, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	SDL_POP_GRAPHIC("sdl.pop_graphic", OpcodeOutputStream._AGB_GP_SDL, OpcodeOutputStream._AGB_OP_SDL_POP_GRAPHIC, AGBThisTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBNumberTypeNode.getInstance()),

	/**
	 * Imprime um valor Lógico
	 */
	NET_REQUEST("net.request", OpcodeOutputStream._AGB_GP_NET, OpcodeOutputStream._AGB_OP_NET_REQUEST, AGBByteArrayTypeNode.getInstance(), AGBStringTypeNode.getInstance(), AGBNumberTypeNode.getInstance(), AGBStringTypeNode.getInstance()),

	;

	/** Opcode do Grupo */
	private final int group;

	/** Opcode do Elemento */
	private final int opcode;

	/** Retorno */
	private final AGBTypeNode type;

	/** Parametros */
	private final List<AGBTypeNode> parameters;

	/** Nome */
	private final String name;

	/**
	 * Construtor
	 * 
	 * @param name
	 * @param group
	 * @param opcode
	 * @param returns
	 * @param params
	 */
	private AGBNativeEnum(String name, int group, int opcode, AGBTypeNode returns, AGBTypeNode... params) {
		this.name = name;
		this.group = group;
		this.opcode = opcode;
		this.type = returns;
		this.parameters = Arrays.asList(params);
	}

	/**
	 * @return the image
	 */
	public String getName() {
		return name;
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
	 * @return the returns
	 */
	public AGBTypeNode getType() {
		return type;
	}

	/**
	 * @return the types
	 */
	public List<AGBTypeNode> getParameters() {
		return parameters;
	}

}