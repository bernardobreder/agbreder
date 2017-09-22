function agb_eval(source) {
	var bytecode = null;
	if (typeof source == 'object') {
		bytecode = agbc(source);
	} else {
		bytecode = agbc([ source ]);
	}
	return agb(bytecode);
}

/**
 * Compila os fontes
 * 
 * @param sources
 *            string[]
 */
function agbc(sources) {
	if (typeof sources == 'string') {
		sources = [ sources ];
	}
	var blocks = [];
	for ( var n = 0; n < sources.length; n++) {
		var source = sources[n];
		var tokens = _agb_lexical(source);
		var nodes = _agb_syntax(tokens);
		blocks.push(new _agb_block(nodes));
	}
	var context = new _agb_context();
	for ( var n = 0; n < blocks.length; n++) {
		var block = blocks[n];
		block.head(context);
	}
	for ( var n = 0; n < blocks.length; n++) {
		var block = blocks[n];
		block.body(context);
	}
	for ( var n = 0; n < blocks.length; n++) {
		var block = blocks[n];
		block.link(context);
	}
	var opcodes = null;
	for ( var p = 0; p < 2; p++) {
		opcodes = new _agb_opcodes();
		opcodes.op_def_num_len(context.numbers.length);
		opcodes.op_def_str_len(context.strings.length);
		for ( var n = 0; n < context.numbers.length; n++) {
			var value = context.numbers[n];
			opcodes.op_def_num(value);
		}
		for ( var n = 0; n < context.strings.length; n++) {
			var value = context.strings[n];
			opcodes.op_def_str(value);
		}
		opcodes.op_def_class_len(context.defclasses.length);
		for ( var n = 0; n < context.defclasses.length; n++) {
			var value = context.defclasses[n];
			opcodes.op_def_class(value);
		}
		opcodes.op_stack_push(1);
		opcodes.count--;
		for ( var n = 0; n < blocks.length; n++) {
			var block = blocks[n];
			block.build(opcodes);
			opcodes.count = 0;
		}
		opcodes.op_vm_half();
		for ( var n = 0; n < context.defmethods.length; n++) {
			var method = context.defmethods[n];
			method.build(opcodes);
		}
	}
	return _agb_compress(opcodes.bytecodes);
}

/**
 * Executa os bytecodes
 * 
 * @param bytecode
 *            int[]
 */
function agb(base16) {
	base16 = _agb_decompress(base16);
	var irs = [];
	for ( var n = 0, m = 0; n < base16.length; m++) {
		irs.push(((base16.charCodeAt(n++) - 65) << 28)
				+ ((base16.charCodeAt(n++) - 65) << 24)
				+ ((base16.charCodeAt(n++) - 65) << 20)
				+ ((base16.charCodeAt(n++) - 65) << 16)
				+ ((base16.charCodeAt(n++) - 65) << 12)
				+ ((base16.charCodeAt(n++) - 65) << 8)
				+ ((base16.charCodeAt(n++) - 65) << 4)
				+ (base16.charCodeAt(n++) - 65));
	}
	var numPool = [];
	var strPool = [];
	var defclass = [];
	var sir = [];
	var ir = 0;
	var pc = 0;
	var os = [];
	var ios = -1;
	var c1, c2, c3, c4, pcs = 1024 * 1024;
	for (; true;) {
		switch (irs[pc++]) {
		case _AGB_GP_VM: {
			switch (irs[pc++]) {
			case _AGB_OP_VM_HALF:
				if (ios < 0) {
					return null;
				} else {
					return os[ios];
				}
			case _AGB_OP_VM_GC:
				break;
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_STACK: {
			switch (irs[pc++]) {
			case _AGB_OP_STACK_PUSH:
				ios += irs[pc++];
				break;
			case _AGB_OP_STACK_POP:
				ios -= irs[pc++];
				break;
			case _AGB_OP_STACK_LOAD:
				os[ios + 1] = os[ios - irs[pc++]];
				ios++;
				break;
			case _AGB_OP_STACK_STORE:
				os[ios - irs[pc++]] = os[ios];
				ios--;
				break;
			case _AGB_OP_STACK_INT:
				ios++;
				os[ios] = irs[pc++];
				break;
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_DEF: {
			switch (irs[pc++]) {
			case _AGB_OP_DEF_NUM:
				pc++;
				pc++;
				var sb = "";
				do {
					ir = irs[pc++];
					c1 = ((ir & 0xFF000000) >> 24);
					c2 = ((ir & 0xFF0000) >> 16);
					c3 = ((ir & 0xFF00) >> 8);
					c4 = (ir & 0xFF);
					if (c1 != 0) {
						sb += String.fromCharCode(c1);
					}
					if (c2 != 0) {
						sb += String.fromCharCode(c2);
					}
					if (c3 != 0) {
						sb += String.fromCharCode(c3);
					}
					if (c4 != 0) {
						sb += String.fromCharCode(c4);
					}
				} while (c4 != 0);
				numPool.push(parseFloat(sb));
				break;
			case _AGB_OP_DEF_STR:
				pc++;
				var sb = "";
				do {
					ir = irs[pc++];
					c1 = ((ir & 0xFF000000) >> 24);
					c2 = ((ir & 0xFF0000) >> 16);
					c3 = ((ir & 0xFF00) >> 8);
					c4 = (ir & 0xFF);
					if (c1 != 0) {
						sb += String.fromCharCode(c1);
					}
					if (c2 != 0) {
						sb += String.fromCharCode(c2);
					}
					if (c3 != 0) {
						sb += String.fromCharCode(c3);
					}
					if (c4 != 0) {
						sb += String.fromCharCode(c4);
					}
				} while (c4 != 0);
				strPool.push(sb);
				break;
			case _AGB_OP_DEF_CLASS:
				var id = irs[pc++];
				var fields = irs[pc++];
				defclass[id] = {
					fieldlen : fields
				};
				break;
			case _AGB_OP_DEF_CAST:
				break;
			case _AGB_OP_DEF_NUM_LEN:
				pc++;
				break;
			case _AGB_OP_DEF_STR_LEN:
				pc++;
				break;
			case _AGB_OP_DEF_CLASS_LEN:
				pc++;
				break;
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_LOAD: {
			switch (irs[pc++]) {
			case _AGB_OP_LOAD_TRUE: {
				os[++ios] = true;
				break;
			}
			case _AGB_OP_LOAD_FALSE: {
				os[++ios] = false;
				break;
			}
			case _AGB_OP_LOAD_NUM: {
				os[++ios] = numPool[irs[pc++]];
				break;
			}
			case _AGB_OP_LOAD_STR: {
				os[++ios] = strPool[irs[pc++]];
				break;
			}
			case _AGB_OP_LOAD_NULL: {
				os[++ios] = null;
				break;
			}
			case _AGB_OP_LOAD_NEW: {
				var id = irs[pc++];
				var array = [];
				array[-1] = id;
				os[++ios] = array;
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_JUMP: {
			switch (irs[pc++]) {
			case _AGB_OP_JUMP_STACK: {
				break;
			}
			case _AGB_OP_JUMP_INT: {
				pc = irs[pc++];
				break;
			}
			case _AGB_OP_JUMP_TRUE: {
				if (os[ios] == true) {
					pc = irs[pc];
				} else {
					pc++;
				}
				ios--;
				break;
			}
			case _AGB_OP_JUMP_FALSE: {
				if (os[ios] == false) {
					pc = irs[pc];
				} else {
					pc++;
				}
				ios--;
				break;
			}
			case _AGB_OP_JUMP_CALL: {
				sir.push(pc + 1);
				pc = irs[pc++];
				break;
			}
			case _AGB_OP_JUMP_RETURN: {
				if (sir.length == 0) {
					if (ios < 0) {
						return null;
					} else {
						return os[0];
					}
				}
				ios -= irs[pc++];
				pc = sir.pop();
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_BOOL: {
			switch (irs[pc++]) {
			case _AGB_OP_BOOL_OR: {
				ios--;
				os[ios] = os[ios] || os[ios + 1];
				break;
			}
			case _AGB_OP_BOOL_AND: {
				ios--;
				os[ios] = os[ios] && os[ios + 1];
				break;
			}
			case _AGB_OP_BOOL_NOT: {
				os[ios] = !os[ios];
				break;
			}
			case _AGB_OP_BOOL_EQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case _AGB_OP_BOOL_NEQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case _AGB_OP_BOOL_TO_STR: {
				os[ios] = '' + os[ios];
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_NUM: {
			switch (irs[pc++]) {
			case _AGB_OP_NUM_NEG: {
				os[ios] = os[ios] * -1;
				break;
			}
			case _AGB_OP_NUM_SUM: {
				ios--;
				os[ios] = os[ios] + os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_SUB: {
				ios--;
				os[ios] = os[ios] - os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_MUL: {
				ios--;
				os[ios] = os[ios] * os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_DIV: {
				ios--;
				os[ios] = os[ios] / os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_EQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_NEQUAL: {
				ios--;
				os[ios] = os[ios] != os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_GT: {
				ios--;
				os[ios] = os[ios] > os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_EGT: {
				ios--;
				os[ios] = os[ios] >= os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_LT: {
				ios--;
				os[ios] = os[ios] < os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_ELT: {
				ios--;
				os[ios] = os[ios] <= os[ios + 1];
				break;
			}
			case _AGB_OP_NUM_TO_STR: {
				os[ios] = '' + os[ios];
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_STR: {
			switch (irs[pc++]) {
			case _AGB_OP_STR_LEN: {
				os[ios] = os[ios].length;
				break;
			}
			case _AGB_OP_STR_SUM: {
				ios--;
				os[ios] = os[ios] + os[ios + 1];
				break;
			}
			case _AGB_OP_STR_EQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case _AGB_OP_STR_NEQUAL: {
				ios--;
				os[ios] = os[ios] != os[ios + 1];
				break;
			}
			case _AGB_OP_STR_GT: {
				ios--;
				os[ios] = os[ios] > os[ios + 1];
				break;
			}
			case _AGB_OP_STR_EGT: {
				ios--;
				os[ios] = os[ios] >= os[ios + 1];
				break;
			}
			case _AGB_OP_STR_LT: {
				ios--;
				os[ios] = os[ios] < os[ios + 1];
				break;
			}
			case _AGB_OP_STR_ELT: {
				ios--;
				os[ios] = os[ios] <= os[ios + 1];
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case _AGB_GP_ARRAY: {
			switch (irs[pc++]) {
			case _AGB_OP_ARRAY_NEW: {
				os[ios] = [];
				break;
			}
			case _AGB_OP_ARRAY_LEN: {
				os[ios] = os[ios].length;
				break;
			}
			case _AGB_OP_ARRAY_GET: {
				ios--;
				os[ios] = os[ios][os[ios + 1] - 1];
				break;
			}
			case _AGB_OP_ARRAY_SET: {
				ios -= 3;
				os[ios + 2][os[ios + 3] - 1] = os[ios + 1];
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		default: {
			return null;
		}
		}
	}
	return null;
}

var _AGB_GP_VM = 1;
var _AGB_GP_STACK = 2;
var _AGB_GP_DEF = 3;
var _AGB_GP_LOAD = 4;
var _AGB_GP_JUMP = 5;
var _AGB_GP_BOOL = 6;
var _AGB_GP_INT = 7;
var _AGB_GP_NUM = 8;
var _AGB_GP_STR = 9;
var _AGB_GP_ARRAY = 10;
var _AGB_GP_USER = 32;
var _AGB_OP_VM_HALF = 1;
var _AGB_OP_VM_GC = 2;
var _AGB_OP_STACK_PUSH = 1;
var _AGB_OP_STACK_POP = 2;
var _AGB_OP_STACK_LOAD = 3;
var _AGB_OP_STACK_STORE = 4;
var _AGB_OP_STACK_INT = 5;
var _AGB_OP_DEF_NUM = 2;
var _AGB_OP_DEF_STR = 3;
var _AGB_OP_DEF_CLASS = 5;
var _AGB_OP_DEF_CAST = 6;
var _AGB_OP_DEF_NUM_LEN = 8;
var _AGB_OP_DEF_STR_LEN = 9;
var _AGB_OP_DEF_CLASS_LEN = 10;
var _AGB_OP_DEF_CAST_LEN = 11;
var _AGB_OP_LOAD_TRUE = 1;
var _AGB_OP_LOAD_FALSE = 2;
var _AGB_OP_LOAD_NUM = 4;
var _AGB_OP_LOAD_STR = 5;
var _AGB_OP_LOAD_NULL = 6;
var _AGB_OP_LOAD_NEW = 7;
var _AGB_OP_JUMP_STACK = 1;
var _AGB_OP_JUMP_INT = 2;
var _AGB_OP_JUMP_TRUE = 3;
var _AGB_OP_JUMP_FALSE = 4;
var _AGB_OP_JUMP_CALL = 5;
var _AGB_OP_JUMP_RETURN = 6;
var _AGB_OP_BOOL_NOT = 1;
var _AGB_OP_BOOL_OR = 2;
var _AGB_OP_BOOL_AND = 3;
var _AGB_OP_BOOL_EQUAL = 4;
var _AGB_OP_BOOL_NEQUAL = 5;
var _AGB_OP_BOOL_TO_STR = 6;
var _AGB_OP_NUM_NEG = 1;
var _AGB_OP_NUM_SUM = 2;
var _AGB_OP_NUM_SUB = 3;
var _AGB_OP_NUM_MUL = 4;
var _AGB_OP_NUM_DIV = 5;
var _AGB_OP_NUM_EQUAL = 6;
var _AGB_OP_NUM_NEQUAL = 7;
var _AGB_OP_NUM_GT = 8;
var _AGB_OP_NUM_EGT = 9;
var _AGB_OP_NUM_LT = 10;
var _AGB_OP_NUM_ELT = 11;
var _AGB_OP_NUM_TO_STR = 12;
var _AGB_OP_STR_LEN = 1;
var _AGB_OP_STR_SUM = 2;
var _AGB_OP_STR_EQUAL = 3;
var _AGB_OP_STR_NEQUAL = 4;
var _AGB_OP_STR_GT = 5;
var _AGB_OP_STR_EGT = 6;
var _AGB_OP_STR_LT = 7;
var _AGB_OP_STR_ELT = 8;
var _AGB_OP_ARRAY_NEW = 1;
var _AGB_OP_ARRAY_LEN = 2;
var _AGB_OP_ARRAY_GET = 3;
var _AGB_OP_ARRAY_SET = 4;

/**
 * Tokens do Lexical
 */
var _AGB_TOKEN = {
	ID : [ 1, "<id>" ],
	SYMBOL : [ 2, "<symbol>" ],
	NUMBER : [ 3, "<number>" ],
	STRING : [ 4, "<string>" ],
	NUM : [ 5, "num" ],
	STR : [ 6, "str" ],
	BOOL : [ 7, "bool" ],
	OBJ : [ 8, "obj" ],
	VOID : [ 9, "void" ],
	CLASS : [ 10, "class" ],
	DOT : [ 11, "." ],
	COMMA : [ 12, "," ],
	SEMICOMMA : [ 13, ";" ],
	DOTDOT : [ 14, ":" ],
	LPARAM : [ 15, "(" ],
	RPARAM : [ 16, ")" ],
	LBLOCK : [ 17, "{" ],
	RBLOCK : [ 18, "}" ],
	LARRAY : [ 19, "[" ],
	RARRAY : [ 20, "]" ],
	NATIVE : [ 21, "|" ],
	IF : [ 22, "if" ],
	ELSE : [ 23, "else" ],
	SWITCH : [ 24, "switch" ],
	CASE : [ 25, "case" ],
	FOR : [ 26, "for" ],
	WHILE : [ 27, "while" ],
	REPEAT : [ 28, "repeat" ],
	RETURN : [ 29, "return" ],
	DEF : [ 30, "def" ],
	NEW : [ 31, "new" ],
	SUM : [ 32, "+" ],
	SUB : [ 33, "-" ],
	MUL : [ 34, "*" ],
	DIV : [ 35, "/" ],
	NOT : [ 36, "!" ],
	AND : [ 37, "and" ],
	OR : [ 38, "or" ],
	EQUAL : [ 39, "=" ],
	GREATER : [ 40, '>' ],
	LOWER : [ 41, "<" ],
	ASK : [ 42, "?" ],
	TRUE : [ 43, "true" ],
	FALSE : [ 44, "false" ],
	NULL : [ 45, "null" ],
	STRUCT : [ 46, "struct" ],
	INTERFACE : [ 47, "interface" ]
};

/**
 * Realiza o analisador lexical
 * 
 * @param source
 * @returns
 */
function _agb_lexical(source) {
	var list = [];
	var c, len = source.length;
	var lin = 1, col = 1;
	for ( var n = 0; n < len; n++, col++) {
		if ((c = source.charCodeAt(n)) <= 32) {
			if (c == '10') {
				lin++;
				col = 0;
			}
			continue;
		}
		if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
			var begin = n;
			do {
				c = source.charCodeAt(++n);
				col++;
			} while (n < len
					&& ((c >= 97 && c <= 122) || (c >= 65 && c <= 90) || (c == 95)));
			var image = source.substring(begin, n);
			var type = _AGB_TOKEN.ID[0];
			for ( var name in _AGB_TOKEN) {
				var map = _AGB_TOKEN[name];
				if (image == map[1]) {
					type = map[0];
					break;
				}
			}
			list.push(new _agb_token(image, type, lin, col - image.length));
			n--;
			col--;
		} else if (c == 39) {
			var begin = n;
			do {
				c = source.charCodeAt(++n);
				col++;
			} while (n < len && c != 39);
			var image = source.substring(begin + 1, n);
			list.push(new _agb_token(image, _AGB_TOKEN.STRING[0], lin, col
					- image.length - 1));
		} else if (c >= 48 && c <= 57) {
			var begin = n++;
			var dot = false;
			while (n < len && ((c >= 48 && c <= 57) || c == 46)) {
				c = source.charCodeAt(++n);
				col++;
				if (c == 46) {
					if (dot) {
						break;
					} else {
						dot = true;
					}
				}
			}
			var image = source.substring(begin, n);
			list.push(new _agb_token(image, _AGB_TOKEN.NUMBER[0], lin, col
					- (n - begin)));
			n--;
			col--;
		} else {
			var image = source.substring(n, n + 1);
			var type = _AGB_TOKEN.SYMBOL[0];
			for ( var name in _AGB_TOKEN) {
				var map = _AGB_TOKEN[name];
				if (map[1].length == 1) {
					if (image == map[1]) {
						type = map[0];
						break;
					}
				}
			}
			list.push(new _agb_token(image, type, lin, col));
		}
	}
	return list;
}

/**
 * Realiza a compressão
 * 
 * @param text
 */
function _agb_compress(text) {
	var len = text.length;
	if (len == 0) {
		return "";
	}
	var result = "";
	var c = text[0];
	var count = 0;
	for ( var n = 0; n < len; n++) {
		var char = text[n];
		if (char == c) {
			if (count < 9) {
				count++;
			} else {
				result += count + char;
				count = 1;
			}
		} else {
			if (count > 1) {
				result += count + c;
			} else {
				result += c;
			}
			c = char;
			count = 1;
		}
	}
	if (count == 1) {
		result += char;
	} else if (count > 1) {
		result += count + char;
	}
	return result;
}

/**
 * Realiza a decompressão
 * 
 * @param text
 */
function _agb_decompress(text) {
	var len = text.length;
	if (len == 0) {
		return "";
	}
	var result = "";
	for ( var n = 0; n < len; n++) {
		var char = text[n];
		if (char >= '0' && char <= '9') {
			var count = parseInt(char);
			char = text[++n];
			for ( var m = 0; m < count; m++) {
				result += char;
			}
		} else {
			result += char;
		}
	}
	return result;
}

/**
 * Realiza o analisador sintático
 * 
 * @param tokens
 */
function _agb_syntax(tokens) {
	var struct = {
		index : 0,
		tokens : tokens
	};
	var list = [];
	while (!_agb_syntax_eof(struct)) {
		var left = _agb_syntax_do_cmd(struct);
		list.push(left);
	}
	return list;
}

/**
 * @param tokens
 */
function _agb_syntax_do_cmd(struct) {
	if (_agb_syntax_is_struct(struct)) {
		return _agb_syntax_do_struct(struct);
	} else if (_agb_syntax_is_class(struct)) {
		return _agb_syntax_do_class(struct);
	} else if (_agb_syntax_is_block(struct)) {
		return _agb_syntax_do_block(struct);
	} else if (_agb_syntax_is_if(struct)) {
		return _agb_syntax_do_if(struct);
	} else if (_agb_syntax_is_while(struct)) {
		return _agb_syntax_do_while(struct);
	} else if (_agb_syntax_is_for(struct)) {
		return _agb_syntax_do_for(struct);
	} else if (_agb_syntax_is_repeat(struct)) {
		return _agb_syntax_do_repeat(struct);
	} else if (_agb_syntax_is_switch(struct)) {
		return _agb_syntax_do_switch(struct);
	} else if (_agb_syntax_is_return(struct)) {
		return _agb_syntax_do_return(struct);
	} else if (_agb_syntax_is_method(struct)) {
		return _agb_syntax_do_method(struct);
	} else {
		return new _agb_expression(_agb_syntax_do_exp(struct));
	}
}

/**
 * @param tokens
 */
function _agb_syntax_do_struct_cmd(struct) {
	return _agb_syntax_do_field(struct);
}

/**
 * @param tokens
 */
function _agb_syntax_do_class_cmd(struct) {
	if (_agb_syntax_is_method(struct)) {
		return _agb_syntax_do_method(struct);
	} else {
		return _agb_syntax_do_field(struct);
	}
}

/**
 * @param tokens
 */
function _agb_syntax_do_interface_cmd(struct) {
	return _agb_syntax_do_head_method(struct);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_is_struct(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.STRUCT);
}

/**
 * Realiza a leitura de um bloco
 * 
 * @param struct
 */
function _agb_syntax_do_struct(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.STRUCT);
	var name = _agb_syntax_do_id(struct);
	_agb_syntax_do(struct, _AGB_TOKEN.LBLOCK);
	var cmds = [];
	while (!_agb_syntax_is(struct, _AGB_TOKEN.RBLOCK)) {
		cmds.push(_agb_syntax_do_struct_cmd(struct));
	}
	_agb_syntax_do(struct, _AGB_TOKEN.RBLOCK);
	return new _agb_struct(cmds);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_is_class(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.CLASS);
}

/**
 * Realiza a leitura de um bloco
 * 
 * @param struct
 */
function _agb_syntax_do_class(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.CLASS);
	var name = _agb_syntax_do_id(struct);
	_agb_syntax_do(struct, _AGB_TOKEN.LBLOCK);
	var cmds = [];
	while (!_agb_syntax_is(struct, _AGB_TOKEN.RBLOCK)) {
		cmds.push(_agb_syntax_do_class_cmd(struct));
	}
	_agb_syntax_do(struct, _AGB_TOKEN.RBLOCK);
	return new _agb_class(name, cmds);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_is_interface(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.INTERFACE);
}

/**
 * Realiza a leitura de um bloco
 * 
 * @param struct
 */
function _agb_syntax_do_interface(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.INTERFACE);
	var name = _agb_syntax_do_id(struct);
	_agb_syntax_do(struct, _AGB_TOKEN.LBLOCK);
	var cmds = [];
	while (!_agb_syntax_is(struct, _AGB_TOKEN.RBLOCK)) {
		cmds.push(_agb_syntax_do_interface_cmd(struct));
	}
	_agb_syntax_do(struct, _AGB_TOKEN.RBLOCK);
	return new _agb_interface(cmds);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_is_field(struct) {
	return _agb_syntax_is_type(struct) && _agb_syntax_is_id_next(struct, 1);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_is_method(struct) {
	return _agb_syntax_is_typevoid(struct) && _agb_syntax_is_id_next(struct, 1)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.LPARAM, 2);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_do_field(struct) {
	var type = _agb_syntax_do_type(struct);
	var name = _agb_syntax_do_id(struct);
	return new _agb_field(type, name);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_do_head_method(struct) {
	var type = _agb_syntax_do_typevoid(struct);
	var token = _agb_syntax_do_id(struct);
	_agb_syntax_do(struct, _AGB_TOKEN.LPARAM);
	var params = [];
	while (_agb_syntax_is_type(struct)) {
		var ptype = _agb_syntax_do_type(struct);
		var ptoken = _agb_syntax_do_id(struct);
		params.push(new _agb_variable(ptoken, ptype));
		if (!_agb_syntax_can(struct, _AGB_TOKEN.COMMA)) {
			break;
		}
	}
	_agb_syntax_do(struct, _AGB_TOKEN.RPARAM);
	return new _agb_method(token, type, params);
}

/**
 * Verifica se é um bloco
 * 
 * @param struct
 */
function _agb_syntax_is_block(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.LBLOCK);
}

/**
 * Realiza a leitura de um bloco
 * 
 * @param struct
 */
function _agb_syntax_do_block(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.LBLOCK);
	var cmds = [];
	while (!_agb_syntax_is(struct, _AGB_TOKEN.RBLOCK)) {
		cmds.push(_agb_syntax_do_cmd(struct));
	}
	_agb_syntax_do(struct, _AGB_TOKEN.RBLOCK);
	return new _agb_block(cmds);
}

/**
 * Verifica se é um if
 * 
 * @param struct
 */
function _agb_syntax_is_if(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.IF);
}

/**
 * Realiza a leitura de um if
 * 
 * @param struct
 */
function _agb_syntax_do_if(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.IF);
	var cond = _agb_syntax_do_exp(struct);
	var cmd = _agb_syntax_do_cmd(struct);
	var ecmd = null;
	if (_agb_syntax_can(struct, _AGB_TOKEN.ELSE)) {
		ecmd = _agb_syntax_do_cmd(struct);
	}
	return new _agb_if(cond, cmd, ecmd);
}

/**
 * Verifica se é um while
 * 
 * @param struct
 */
function _agb_syntax_is_while(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.WHILE);
}

/**
 * Realiza a leitura de um while
 * 
 * @param struct
 */
function _agb_syntax_do_while(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.WHILE);
	var cond = _agb_syntax_do_exp(struct);
	var cmd = _agb_syntax_do_cmd(struct);
	return new _agb_while(cond, cmd);
}

/**
 * Verifica se é um if
 * 
 * @param struct
 */
function _agb_syntax_is_repeat(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.REPEAT);
}

/**
 * Realiza a leitura de um if
 * 
 * @param struct
 */
function _agb_syntax_do_repeat(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.REPEAT);
	var cmd = _agb_syntax_do_cmd(struct);
	_agb_syntax_do(struct, _AGB_TOKEN.WHILE);
	var cond = _agb_syntax_do_exp(struct);
	return new _agb_repeat(cond, cmd);
}

/**
 * Verifica se é um switch
 * 
 * @param struct
 * @returns
 */
function _agb_syntax_is_switch(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.SWITCH);
}

/**
 * Realiza a leitura de um switch
 * 
 * @param struct
 */
function _agb_syntax_do_switch(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.SWITCH);
	var cond = _agb_syntax_do_exp(struct);
	var cases = [];
	do {
		_agb_syntax_do(struct, _AGB_TOKEN.CASE);
		var val = _agb_syntax_do_exp(struct);
		var cmd = _agb_syntax_do_cmd(struct);
		cases.push({
			cond : val,
			cmd : cmd
		});
	} while (_agb_syntax_is(struct, _AGB_TOKEN.CASE));
	var elcmd = null;
	if (_agb_syntax_can(struct, _AGB_TOKEN.ELSE)) {
		elcmd = _agb_syntax_do_cmd(struct);
	}
	return new _agb_switch(cond, cases, elcmd);
}

/**
 * Verifica se é um for
 * 
 * @param struct
 * @returns
 */
function _agb_syntax_is_for(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.FOR);
}

/**
 * Realiza a leitura de um for
 * 
 * @param struct
 */
function _agb_syntax_do_for(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.FOR);
	_agb_syntax_do(struct, _AGB_TOKEN.LPARAM);
	var inits = [];
	while (!_agb_syntax_is(struct, _AGB_TOKEN.SEMICOMMA)) {
		inits.push(_agb_syntax_do_cmd(struct));
	}
	var token = _agb_syntax_do(struct, _AGB_TOKEN.SEMICOMMA);
	var cond = null;
	if (_agb_syntax_is(struct, _AGB_TOKEN.SEMICOMMA)) {
		cond = new _agb_boolean(token, true);
	} else {
		cond = _agb_syntax_do_exp(struct);
	}
	_agb_syntax_do(struct, _AGB_TOKEN.SEMICOMMA);
	var nexts = [];
	while (!_agb_syntax_is(struct, _AGB_TOKEN.RPARAM)) {
		nexts.push(_agb_syntax_do_cmd(struct));
	}
	_agb_syntax_do(struct, _AGB_TOKEN.RPARAM);
	var cmd = _agb_syntax_do_cmd(struct);
	return new _agb_for(inits, cond, nexts, cmd);
}

/**
 * Verifica se é um return
 * 
 * @param struct
 * @returns
 */
function _agb_syntax_is_return(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.RETURN);
}

/**
 * Realiza a leitura de um return
 * 
 * @param struct
 */
function _agb_syntax_do_return(struct) {
	_agb_syntax_do(struct, _AGB_TOKEN.RETURN);
	var left = _agb_syntax_do_exp(struct);
	return new _agb_return(left);
}

/**
 * Realiza a leitura de um return
 * 
 * @param struct
 */
function _agb_syntax_do_method(struct) {
	var method = _agb_syntax_do_head_method(struct);
	method.cmd = _agb_syntax_do_cmd(struct);
	return method;
}

/**
 * Retorna o Expression
 * 
 * @param tokens
 */
function _agb_syntax_do_exp(struct) {
	return _agb_syntax_do_ternary(struct);
}

/**
 * Retorna o Ternary
 * 
 * @param tokens
 */
function _agb_syntax_do_ternary(struct) {
	var left = _agb_syntax_do_or(struct);
	if (_agb_syntax_is(struct, _AGB_TOKEN.ASK)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.ASK);
		var center = _agb_syntax_do_exp(struct);
		_agb_syntax_do(struct, _AGB_TOKEN.DOTDOT);
		var right = _agb_syntax_do_exp(struct);
		left = new _agb_ternary(token, left, right, center);
	}
	return left;
}

/**
 * Retorna o Or
 * 
 * @param tokens
 */
function _agb_syntax_do_or(struct) {
	var left = _agb_syntax_do_equal(struct);
	while (_agb_syntax_is(struct, _AGB_TOKEN.AND)
			|| _agb_syntax_is(struct, _AGB_TOKEN.OR)) {
		if (_agb_syntax_is(struct, _AGB_TOKEN.AND)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.AND);
			var right = _agb_syntax_do_equal(struct);
			left = new _agb_and(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.OR)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.OR);
			var right = _agb_syntax_do_equal(struct);
			left = new _agb_or(token, left, right);
		}
	}
	return left;
}

/**
 * Retorna o Equal
 * 
 * @param tokens
 */
function _agb_syntax_do_equal(struct) {
	var left = _agb_syntax_do_sum(struct);
	while (_agb_syntax_is(struct, _AGB_TOKEN.EQUAL)
			|| _agb_syntax_is(struct, _AGB_TOKEN.NOT)
			|| _agb_syntax_is(struct, _AGB_TOKEN.GREATER)
			|| _agb_syntax_is(struct, _AGB_TOKEN.LOWER)) {
		if (_agb_syntax_is(struct, _AGB_TOKEN.EQUAL)
				&& _agb_syntax_is_next(struct, _AGB_TOKEN.EQUAL, 1)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.EQUAL);
			_agb_syntax_do(struct, _AGB_TOKEN.EQUAL);
			var right = _agb_syntax_do_sum(struct);
			left = new _agb_equal(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.NOT)
				&& _agb_syntax_is_next(struct, _AGB_TOKEN.EQUAL, 1)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.NOT);
			_agb_syntax_do(struct, _AGB_TOKEN.EQUAL);
			var right = _agb_syntax_do_sum(struct);
			left = new _agb_notequal(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.GREATER)
				&& _agb_syntax_is_next(struct, _AGB_TOKEN.EQUAL, 1)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.GREATER);
			_agb_syntax_do(struct, _AGB_TOKEN.EQUAL);
			var right = _agb_syntax_do_sum(struct);
			left = new _agb_greaterequal(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.LOWER)
				&& _agb_syntax_is_next(struct, _AGB_TOKEN.EQUAL, 1)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.LOWER);
			_agb_syntax_do(struct, _AGB_TOKEN.EQUAL);
			var right = _agb_syntax_do_sum(struct);
			left = new _agb_lowerequal(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.GREATER)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.GREATER);
			var right = _agb_syntax_do_sum(struct);
			left = new _agb_greater(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.LOWER)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.LOWER);
			var right = _agb_syntax_do_sum(struct);
			left = new _agb_lower(token, left, right);
		}
	}
	return left;
}

/**
 * Retorna o Sum
 * 
 * @param tokens
 */
function _agb_syntax_do_sum(struct) {
	var left = _agb_syntax_do_mul(struct);
	while (_agb_syntax_is(struct, _AGB_TOKEN.SUM)
			|| _agb_syntax_is(struct, _AGB_TOKEN.SUB)) {
		if (_agb_syntax_is(struct, _AGB_TOKEN.SUM)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.SUM);
			var right = _agb_syntax_do_mul(struct);
			left = new _agb_sum(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.SUB)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.SUB);
			var right = _agb_syntax_do_mul(struct);
			left = new _agb_sub(token, left, right);
		}
	}
	return left;
}
/**
 * Retorna o Mul
 * 
 * @param tokens
 */
function _agb_syntax_do_mul(struct) {
	var left = _agb_syntax_do_unary(struct);
	while (_agb_syntax_is(struct, _AGB_TOKEN.MUL)
			|| _agb_syntax_is(struct, _AGB_TOKEN.DIV)) {
		if (_agb_syntax_is(struct, _AGB_TOKEN.MUL)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.MUL);
			var right = _agb_syntax_do_unary(struct);
			left = new _agb_mul(token, left, right);
		} else if (_agb_syntax_is(struct, _AGB_TOKEN.DIV)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.DIV);
			var right = _agb_syntax_do_unary(struct);
			left = new _agb_div(token, left, right);
		}
	}
	return left;
}

/**
 * Retorna o Unary
 * 
 * @param tokens
 */
function _agb_syntax_do_unary(struct) {
	if (_agb_syntax_is(struct, _AGB_TOKEN.SUB)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.SUB, 1)) {
		_agb_syntax_do(struct, _AGB_TOKEN.SUB);
		_agb_syntax_do(struct, _AGB_TOKEN.SUB);
		var token = _agb_syntax_do_id(struct);
		return new _agb_predec(token, new _agb_ridentify(token));
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.SUM)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.SUM, 1)) {
		_agb_syntax_do(struct, _AGB_TOKEN.SUM);
		_agb_syntax_do(struct, _AGB_TOKEN.SUM);
		var token = _agb_syntax_do_id(struct);
		return new _agb_preinc(token, new _agb_ridentify(token));
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.SUB)) {
		_agb_syntax_do(struct, _AGB_TOKEN.SUB);
		var left = _agb_syntax_do_literal(struct);
		return new _agb_negative(left.token, left);
	} else if (_agb_syntax_is_lvalue(struct)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.SUB, 1)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.SUB, 2)) {
		var token = _agb_syntax_do_id(struct);
		_agb_syntax_do(struct, _AGB_TOKEN.SUB);
		_agb_syntax_do(struct, _AGB_TOKEN.SUB);
		return new _agb_posdec(token, new _agb_ridentify(token));
	} else if (_agb_syntax_is_lvalue(struct)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.SUM, 1)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.SUM, 2)) {
		var token = _agb_syntax_do_id(struct);
		_agb_syntax_do(struct, _AGB_TOKEN.SUM);
		_agb_syntax_do(struct, _AGB_TOKEN.SUM);
		return new _agb_posinc(token, new _agb_ridentify(token));
	} else {
		return _agb_syntax_do_literal(struct);
	}
}

/**
 * Retorna o literal
 * 
 * @param tokens
 */
function _agb_syntax_do_literal(struct) {
	if (_agb_syntax_can(struct, _AGB_TOKEN.LPARAM)) {
		var left = _agb_syntax_do_exp(struct);
		_agb_syntax_can(struct, _AGB_TOKEN.RPARAM);
		return left;
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.NUMBER)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.NUMBER);
		return new _agb_number(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.STRING)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.STRING);
		return new _agb_string(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.TRUE)
			|| _agb_syntax_is(struct, _AGB_TOKEN.FALSE)) {
		if (_agb_syntax_is(struct, _AGB_TOKEN.TRUE)) {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.TRUE);
			return new _agb_boolean(token, true);
		} else {
			var token = _agb_syntax_do(struct, _AGB_TOKEN.FALSE);
			return new _agb_boolean(token, false);
		}
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.NULL)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.NULL);
		return new _agb_null(token);
	} else if (_agb_syntax_is_new(struct)) {
		return _agb_syntax_do_new(struct);
	} else {
		return _agb_syntax_do_identify(struct);
	}
}

/**
 * Retorna o identificador
 * 
 * @param tokens
 */
function _agb_syntax_do_identify(struct) {
	if (_agb_syntax_is_type(struct) && _agb_syntax_is_id_next(struct, 1)) {
		var type = _agb_syntax_do_type(struct);
		var token = _agb_syntax_do_id(struct);
		_agb_syntax_do(struct, _AGB_TOKEN.EQUAL);
		var value = _agb_syntax_do_exp(struct);
		return new _agb_declare(token, type, new _agb_lidentify(token), value);
	} else if (_agb_syntax_is_lvalue(struct)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.EQUAL, 1)
			&& !_agb_syntax_is_next(struct, _AGB_TOKEN.EQUAL, 2)) {
		var left = _agb_syntax_do_lvalue(struct);
		_agb_syntax_do(struct, _AGB_TOKEN.EQUAL);
		var value = _agb_syntax_do_exp(struct);
		return new _agb_assign(left.token, left, value);
	} else {
		return _agb_syntax_do_rvalue(struct);
	}
}

/**
 * Recupera o identificador ou a chamada de método
 * 
 * @param tokens
 */
function _agb_syntax_do_rvalue(struct) {
	if (_agb_syntax_is_id(struct)
			&& _agb_syntax_is_next(struct, _AGB_TOKEN.LPARAM, 1)) {
		var token = _agb_syntax_do_id(struct);
		_agb_syntax_do(struct, _AGB_TOKEN.LPARAM);
		var params = [];
		if (!_agb_syntax_is(struct, _AGB_TOKEN.RPARAM)) {
			params.push(_agb_syntax_do_exp(struct));
			while (_agb_syntax_can(struct, _AGB_TOKEN.COMMA)) {
				params.push(_agb_syntax_do_exp(struct));
			}
		}
		_agb_syntax_do(struct, _AGB_TOKEN.RPARAM);
		return new _agb_call(token, params);
	} else {
		var token = _agb_syntax_do_id(struct);
		var left = new _agb_ridentify(token);
		while (_agb_syntax_is(struct, _AGB_TOKEN.DOT)) {
			if (_agb_syntax_can(struct, _AGB_TOKEN.DOT)) {
				var token = _agb_syntax_do_id(struct);
				left = new _agb_getfield(left, token);
			}
		}
		return left;
	}
}

function _agb_syntax_is_new(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.NEW);
}

function _agb_syntax_do_new(struct) {
	var token = _agb_syntax_do(struct, _AGB_TOKEN.NEW);
	var type = _agb_syntax_do_type(struct);
	_agb_syntax_do(struct, _AGB_TOKEN.LPARAM);
	var params = [];
	while (!_agb_syntax_is(struct, _AGB_TOKEN.RPARAM)) {
		params.push(_agb_syntax_do_exp(struct));
		if (!_agb_syntax_can(struct, _AGB_TOKEN.COMMA)) {
			break;
		}
	}
	_agb_syntax_do(struct, _AGB_TOKEN.RPARAM);
	return new _agb_new(token, type, params);
}

/**
 * Indica se tem um LValue
 * 
 * @param tokens
 * @returns boolean
 */
function _agb_syntax_is_lvalue(struct) {
	return _agb_syntax_is_id(struct);
}

/**
 * Realiza a leitura do LValue
 * 
 * @param tokens
 */
function _agb_syntax_do_lvalue(struct) {
	var token = _agb_syntax_do_id(struct);
	return new _agb_lidentify(token);
}

/**
 * Indica se tem um Type
 * 
 * @param tokens
 * @returns boolean
 */
function _agb_syntax_is_type(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.NUM)
			|| _agb_syntax_is(struct, _AGB_TOKEN.STR)
			|| _agb_syntax_is(struct, _AGB_TOKEN.BOOL)
			|| _agb_syntax_is(struct, _AGB_TOKEN.OBJ)
			|| _agb_syntax_is_id(struct);
}

/**
 * Indica se tem um Type
 * 
 * @param tokens
 * @returns boolean
 */
function _agb_syntax_is_typevoid(struct) {
	return _agb_syntax_is_type(struct)
			|| _agb_syntax_is(struct, _AGB_TOKEN.VOID);
}

/**
 * Reliza a leitura do Type
 * 
 * @param tokens
 */
function _agb_syntax_do_type(struct) {
	if (_agb_syntax_is(struct, _AGB_TOKEN.NUM)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.NUM);
		return new _agb_number_type(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.STR)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.STR);
		return new _agb_string_type(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.BOOL)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.BOOL);
		return new _agb_boolean_type(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.OBJ)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.OBJ);
		return new _agb_obj_type(token);
	} else {
		var token = _agb_syntax_do_id(struct);
		return new _agb_type(token);
	}
}

/**
 * Realiza a leitura do TypeVoid
 * 
 * @param tokens
 */
function _agb_syntax_do_typevoid(struct) {
	if (_agb_syntax_is(struct, _AGB_TOKEN.NUM)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.NUM);
		return new _agb_number_type(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.STR)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.STR);
		return new _agb_string_type(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.BOOL)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.BOOL);
		return new _agb_boolean_type(token);
	} else if (_agb_syntax_is(struct, _AGB_TOKEN.VOID)) {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.VOID);
		return new _agb_void_type(token);
	} else {
		var token = _agb_syntax_do(struct, _AGB_TOKEN.OBJ);
		return new _agb_type(token);
	}
}

/**
 * Verifica se chegou no final dos tokens
 * 
 * @param tokens
 * @returns boolean
 */
function _agb_syntax_eof(struct) {
	return _agb_syntax_eof_next(struct, 0);
}

/**
 * Verifica se chegou no final dos tokens
 * 
 * @param tokens
 * @returns boolean
 */
function _agb_syntax_eof_next(struct, index) {
	return struct.index + index >= struct.tokens.length;
}

/**
 * Indica se o token corrente é do tipo especificado pelo parametro
 * 
 * @param struct
 * @param type
 * @returns boolean
 */
function _agb_syntax_is(struct, type) {
	return _agb_syntax_is_next(struct, type, 0);
}

/**
 * Indica se o token corrente é do tipo especificado pelo parametro
 * 
 * @param struct
 * @param type
 * @returns boolean
 */
function _agb_syntax_is_next(struct, type, index) {
	return !_agb_syntax_eof_next(struct, index)
			&& _agb_syntax_get_next(struct, index).type == type[0];
}

/**
 * Indica se o token corrente é do tipo especificado pelo parametro
 * 
 * @param struct
 * @param type
 * @returns boolean
 */
function _agb_syntax_get_next(struct, index) {
	return struct.tokens[struct.index + index];
}

/**
 * Indica se o token corrente é do tipo especificado pelo parametro
 * 
 * @param struct
 * @param type
 * @returns boolean
 */
function _agb_syntax_is_id(struct) {
	return _agb_syntax_is(struct, _AGB_TOKEN.ID);
}

/**
 * Indica se o token corrente é do tipo especificado pelo parametro
 * 
 * @param struct
 * @param type
 * @returns boolean
 */
function _agb_syntax_is_id_next(struct, index) {
	return _agb_syntax_is_next(struct, _AGB_TOKEN.ID, index);
}

/**
 * Indica se o token corrente é do tipo especificado pelo parametro. Se for,
 * será incrementado o contador de token
 * 
 * @param struct
 * @param type
 * @returns boolean
 */
function _agb_syntax_can(struct, type) {
	return _agb_syntax_can_next(struct, type, 0);
}

/**
 * Indica se o token corrente é do tipo especificado pelo parametro. Se for,
 * será incrementado o contador de token
 * 
 * @param struct
 * @param type
 * @returns boolean
 */
function _agb_syntax_can_next(struct, type, index) {
	if (_agb_syntax_is_next(struct, type, index)) {
		struct.index++;
		return true;
	} else {
		return false;
	}
}

/**
 * Exige que o token corrente seja do tipo id
 * 
 * @param tokens
 * @returns token
 */
function _agb_syntax_do_id(struct) {
	return _agb_syntax_do(struct, _AGB_TOKEN.ID);
}

/**
 * Exige que um token esteja corrente
 * 
 * @param tokens
 * @returns token
 */
function _agb_syntax_do(struct, type) {
	if (!_agb_syntax_can(struct, type)) {
		var token = _agb_syntax_get_next(struct, 0);
		var expected = type[1];
		var atual = token;
		throw new _agb_syntax_error(expected, atual).msg;
	}
	return struct.tokens[struct.index - 1];
}

/**
 * Objeto de erro
 */
function _agb_syntax_error(expected, atual) {
	this.line = atual.lin;
	this.column = atual.col;
	this.msg = "Expected '" + expected + "' and not '" + atual.text + "'"
			+ "\n" + "at line " + this.line + " and column " + this.column;
}

/**
 * Objeto de erro
 */
function _agb_semantic_error(token, text) {
	this.line = token.lin;
	this.column = token.col;
	this.msg = text + "\n" + "at line " + this.line + " and column "
			+ this.column;
}

/**
 * Objeto de erro
 */
function _agb_link_error(token, text) {
	this.line = token.lin;
	this.column = token.col;
	this.msg = text + "\n" + "at line " + this.line + " and column "
			+ this.column;
}

/**
 * Construtor do node Context
 */
function _agb_context() {
	this.blocks = [];
	this.numbers = [];
	this.strings = [];
	this.methods = [];
	this.defmethods = [];
	this.defclasses = [];
	this.current_class = null;
	this.class_sequence = 1;
	this.method_sequence = 1;
}

/**
 * Indica quantas instruções o node gera
 * 
 * @param node
 */
function _agb_node_counter(node) {
	var opcodes = new _agb_opcodes();
	node.build(opcodes);
	return opcodes.pc;
}

/**
 * Construtor do node Struct
 */
function _agb_opcodes() {
	this.bytecodes = "";
	this.count = 0;
	this.pc = 0;
	this.op_vm_half = function() {
		this.op(_AGB_GP_VM, _AGB_OP_VM_HALF);
	};
	this.op_vm_gc = function() {
		this.op(_AGB_GP_VM, _AGB_OP_VM_GC);
	};
	this.op_stack_push = function(count) {
		if (count > 0) {
			this.opd(_AGB_GP_STACK, _AGB_OP_STACK_PUSH, count);
			this.count += count;
		}
	};
	this.op_stack_pop = function(count) {
		if (count > 0) {
			this.opd(_AGB_GP_STACK, _AGB_OP_STACK_POP, count);
			this.count -= count;
		}
	};
	this.op_stack_dup = function(index) {
		this.op_stack_dup_abs(index + this.count);
	};
	this.op_stack_dup_abs = function(index) {
		this.op_stack_load_abs(index);
	};
	this.op_stack_load = function(index) {
		this.op_stack_load_abs(index + this.count);
	};
	this.op_stack_load_abs = function(index) {
		this.opd(_AGB_GP_STACK, _AGB_OP_STACK_LOAD, index);
		this.count++;
	};
	this.op_stack_store = function(index) {
		this.op_stack_store_abs(index + this.count);
	};
	this.op_stack_store_abs = function(index) {
		this.opd(_AGB_GP_STACK, _AGB_OP_STACK_STORE, index);
		this.count--;
	};
	this.op_stack_int = function(index) {
		this.opd(_AGB_GP_STACK, _AGB_OP_STACK_INT, index);
		this.count++;
	};
	this.op_def_num = function(value) {
		var text = value.toString();
		this.opd(_AGB_GP_DEF, _AGB_OP_DEF_NUM, text);
		this.op1(text.indexOf(".") - 1);
		this.opstr(text);
	};
	this.op_def_str = function(value) {
		this.opd(_AGB_GP_DEF, _AGB_OP_DEF_STR, value.length);
		this.opstr(value);
	};
	this.op_def_class = function(c) {
		this.op(_AGB_GP_DEF, _AGB_OP_DEF_CLASS);
		this.op1(c.id);
		this.op1(c.fields.length);
	};
	this.op_def_cast = function() {
		this.op(_AGB_GP_DEF, _AGB_OP_DEF_CAST);
	};
	this.op_def_num_len = function(count) {
		this.opd(_AGB_GP_DEF, _AGB_OP_DEF_NUM_LEN, count);
	};
	this.op_def_str_len = function(count) {
		this.opd(_AGB_GP_DEF, _AGB_OP_DEF_STR_LEN, count);
	};
	this.op_def_cast_len = function(count) {
		this.opd(_AGB_GP_DEF, _AGB_OP_DEF_CAST_LEN, count);
	};
	this.op_def_class_len = function(count) {
		this.opd(_AGB_GP_DEF, _AGB_OP_DEF_CLASS_LEN, count);
	};
	this.op_load_true = function() {
		this.op(_AGB_GP_LOAD, _AGB_OP_LOAD_TRUE);
		this.count++;
	};
	this.op_load_false = function() {
		this.op(_AGB_GP_LOAD, _AGB_OP_LOAD_FALSE);
		this.count++;
	};
	this.op_load_num = function(index) {
		this.opd(_AGB_GP_LOAD, _AGB_OP_LOAD_NUM, index);
		this.count++;
	};
	this.op_load_str = function(index) {
		this.opd(_AGB_GP_LOAD, _AGB_OP_LOAD_STR, index);
		this.count++;
	};
	this.op_load_null = function() {
		this.op(_AGB_GP_LOAD, _AGB_OP_LOAD_NULL);
		this.count++;
	};
	this.op_load_new = function(id) {
		this.op(_AGB_GP_LOAD, _AGB_OP_LOAD_NEW);
		this.op1(id);
		this.count++;
	};
	this.op_jump_stack = function() {
		this.op(_AGB_GP_JUMP, _AGB_OP_JUMP_STACK);
	};
	this.op_jump_int_next = function(index) {
		this.op_jump_int(index + this.pc + 3);
	};
	this.op_jump_int = function(index) {
		this.opd(_AGB_GP_JUMP, _AGB_OP_JUMP_INT, index);
	};
	this.op_jump_true_next = function(index) {
		this.op_jump_true(index + this.pc + 3);
	};
	this.op_jump_false_next = function(index) {
		this.op_jump_false(index + this.pc + 3);
	};
	this.op_jump_true = function(index) {
		this.opd(_AGB_GP_JUMP, _AGB_OP_JUMP_TRUE, index);
		this.count--;
	};
	this.op_jump_false = function(index) {
		this.opd(_AGB_GP_JUMP, _AGB_OP_JUMP_FALSE, index);
		this.count--;
	};
	this.op_jump_call = function(index) {
		this.opd(_AGB_GP_JUMP, _AGB_OP_JUMP_CALL, index);
	};
	this.op_jump_return = function(count) {
		this.opd(_AGB_GP_JUMP, _AGB_OP_JUMP_RETURN, count);
	};
	this.op_equal = function(left) {
		var type = left.type;
		if (type.is_number()) {
			this.op_num_equal();
		} else if (type.is_string()) {
			this.op_str_equal();
		} else if (type.is_boolean()) {
			this.op_bool_equal();
		} else {
			throw new _agb_semantic_error(left.token, "illegal operator");
		}
	};
	this.op_nequal = function(left) {
		var type = left.type;
		if (type.is_number()) {
			this.op_num_nequal();
		} else if (type.is_string()) {
			this.op_str_nequal();
		} else if (type.is_boolean()) {
			this.op_bool_nequal();
		} else {
			throw new _agb_semantic_error(left.token, "illegal operator");
		}
	};
	this.op_gt = function(left) {
		var type = left.type;
		if (type.is_number()) {
			this.op_num_gt();
		} else if (type.is_string()) {
			this.op_str_gt();
		} else {
			throw new _agb_semantic_error(left.token, "illegal operator");
		}
	};
	this.op_egt = function(left) {
		var type = left.type;
		if (type.is_number()) {
			this.op_num_egt();
		} else if (type.is_string()) {
			this.op_str_egt();
		} else {
			throw new _agb_semantic_error(left.token, "illegal operator");
		}
	};
	this.op_lt = function(left) {
		var type = left.type;
		if (type.is_number()) {
			this.op_num_lt();
		} else if (type.is_string()) {
			this.op_str_lt();
		} else {
			throw new _agb_semantic_error(left.token, "illegal operator");
		}
	};
	this.op_elt = function(left) {
		var type = left.type;
		if (type.is_number()) {
			this.op_num_elt();
		} else if (type.is_string()) {
			this.op_str_elt();
		} else {
			throw new _agb_semantic_error(left.token, "illegal operator");
		}
	};
	this.op_bool_not = function() {
		this.op(_AGB_GP_BOOL, _AGB_OP_BOOL_NOT);
	};
	this.op_bool_or = function() {
		this.op(_AGB_GP_BOOL, _AGB_OP_BOOL_OR);
		this.count--;
	};
	this.op_bool_and = function() {
		this.op(_AGB_GP_BOOL, _AGB_OP_BOOL_AND);
		this.count--;
	};
	this.op_bool_equal = function() {
		this.op(_AGB_GP_BOOL, _AGB_OP_BOOL_EQUAL);
		this.count--;
	};
	this.op_bool_nequal = function() {
		this.op(_AGB_GP_BOOL, _AGB_OP_BOOL_NEQUAL);
		this.count--;
	};
	this.op_bool_to_str = function() {
		this.op(_AGB_GP_BOOL, _AGB_OP_BOOL_TO_STR);
	};
	this.op_num_neg = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_NEG);
	};
	this.op_num_sum = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_SUM);
		this.count--;
	};
	this.op_num_sub = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_SUB);
		this.count--;
	};
	this.op_num_mul = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_MUL);
		this.count--;
	};
	this.op_num_div = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_DIV);
		this.count--;
	};
	this.op_num_equal = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_EQUAL);
		this.count--;
	};
	this.op_num_nequal = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_NEQUAL);
		this.count--;
	};
	this.op_num_gt = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_GT);
		this.count--;
	};
	this.op_num_egt = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_EGT);
		this.count--;
	};
	this.op_num_lt = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_LT);
		this.count--;
	};
	this.op_num_elt = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_ELT);
		this.count--;
	};
	this.op_num_to_str = function() {
		this.op(_AGB_GP_NUM, _AGB_OP_NUM_TO_STR);
	};
	this.op_str_len = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_LEN);
	};
	this.op_str_sum = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_SUM);
		this.count--;
	};
	this.op_str_equal = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_EQUAL);
		this.count--;
	};
	this.op_str_nequal = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_NEQUAL);
		this.count--;
	};
	this.op_str_gt = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_GT);
		this.count--;
	};
	this.op_str_egt = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_EGT);
		this.count--;
	};
	this.op_str_lt = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_LT);
		this.count--;
	};
	this.op_str_elt = function() {
		this.op(_AGB_GP_STR, _AGB_OP_STR_ELT);
		this.count--;
	};
	this.op1 = function(value) {
		this.bytecodes += this.write32(value);
		this.pc++;
	};
	this.opstr = function(text) {
		var result = "";
		var len = text.length;
		for ( var n = 0; n < len; n++) {
			var c1 = null, c2 = null, c3 = null, c4 = null;
			if (n * 4 < len) {
				c1 = text[n * 4];
			}
			if (n * 4 + 1 < len) {
				c2 = text[n * 4 + 1];
			}
			if (n * 4 + 2 < len) {
				c3 = text[n * 4 + 2];
			}
			if (n * 4 + 3 < len) {
				c4 = text[n * 4 + 3];
			}
			this.op1(c1 == null ? 0 : c1.charCodeAt(0));
			if (c1 == null) {
				break;
			}
			this.op1(c2 == null ? 0 : c2.charCodeAt(0));
			if (c2 == null) {
				break;
			}
			this.op1(c3 == null ? 0 : c3.charCodeAt(0));
			if (c3 == null) {
				break;
			}
			this.op1(c4 == null ? 0 : c4.charCodeAt(0));
			if (c4 == null) {
				break;
			}
		}
		return result;
	};
	this.op = function(group, bytecode) {
		this.op1(group);
		this.op1(bytecode);
	};
	this.opd = function(group, bytecode, data) {
		this.op(group, bytecode);
		this.bytecodes += this.write32(data);
		this.pc += 1;
	};
	this.write8 = function(byte) {
		var hex = 'ABCDEFGHIJKLMNOP';
		return (hex[(byte & 240) >> 4] + hex[byte & 15]);
	};
	this.write16 = function(value) {
		return this.write8((value & 65280) >> 8) + this.write8(value & 255);
	};
	this.write32 = function(value) {
		return this.write16((value & 4294901760) >> 16)
				+ this.write16(value & 65535);
	};
}

/**
 * Construtor do node Struct
 */
function _agb_basic() {
	this.save = function(output) {
	};
}

/**
 * Construtor do Token
 */
function _agb_token(text, type, lin, col) {
	_agb_basic.call(this);
	this.text = text;
	this.type = type;
	this.lin = lin;
	this.col = col;
}

/**
 * Type
 */
function _agb_type(token) {
	_agb_node.call(this);
	this.token = token;
	this.is_primitive = function() {
		return this.is_number() || this.is_string() || this.is_boolean()
				|| this.is_void();
	};
	this.is_number = function() {
		return false;
	};
	this.is_string = function() {
		return false;
	};
	this.is_boolean = function() {
		return false;
	};
	this.is_object = function() {
		return !this.is_primitive() && !this.is_class();
	};
	this.is_class = function() {
		return false;
	};
	this.is_void = function() {
		return false;
	};
	this.can_cast = function(type) {
		if (this.is_number() && type.is_number) {
			return true;
		} else if (this.is_string() && type.is_string) {
			return true;
		}
		if (this.is_boolean() && type.is_boolean) {
			return true;
		}
		return false;
	};
	this.head = function(context) {
	};
	this.body = function(context) {
	};
	this.link = function(context) {
		if (this.is_object) {
			this.id = -1;
			var text = this.token.text;
			for ( var n = 0; n < context.defclasses.length; n++) {
				var defclass = context.defclasses[n];
				if (text == defclass.name.text) {
					this.id = defclass.id;
				}
			}
			if (this.id == -1) {
				// throw new _agb_link_error(this.token,
				// "can not found the class of the object");
			}
		}
	};
	this.build = function(opcodes) {
	};
}

/**
 * Node
 */
function _agb_null_type(token) {
	_agb_type.call(this, token);
}

/**
 * Node
 */
function _agb_number_type(token) {
	_agb_type.call(this, token);
	this.is_number = function() {
		return true;
	};
}

/**
 * Node
 */
function _agb_string_type(token) {
	_agb_type.call(this, token);
	this.is_string = function() {
		return true;
	};
}

/**
 * Node
 */
function _agb_boolean_type(token) {
	_agb_type.call(this, token);
	this.is_boolean = function() {
		return true;
	};
}

/**
 * Node
 */
function _agb_void_type(token) {
	_agb_type.call(this, token);
	this.is_void = function() {
		return true;
	};
}

/**
 * Node
 */
function _agb_object_type(token) {
	_agb_type.call(this, token);
	this.is_object = function() {
		return true;
	};
}

/**
 * Node
 */
function _agb_node() {
	_agb_basic.call(this);
}

function _agb_command() {
	_agb_node.call(this);
	this.isheader = function() {
		return false;
	};
}

function _agb_struct(name, fields) {
	_agb_command.call(this);
	this.name = name;
	this.fields = fields;
	this.isheader = function() {
		return true;
	};
	this.head = function(context) {
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.head(context);
		}
	};
	this.body = function(context) {
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.body(context);
		}
	};
	this.link = function(context) {
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.link(context);
		}
	};
	this.build = function(opcodes) {
	};
}

function _agb_class(name, cmds) {
	_agb_command.call(this);
	this.name = name;
	this.cmds = cmds;
	this.fields = [];
	this.methods = [];
	this.isheader = function() {
		return true;
	};
	this.head = function(context) {
		context.defclasses.push(this);
		context.current_class = this;
		this.id = context.class_sequence++;
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.head(context);
		}
		context.current_class = null;
	};
	this.body = function(context) {
		context.current_class = this;
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.body(context);
		}
		context.current_class = null;
	};
	this.link = function(context) {
		context.current_class = this;
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.link(context);
		}
		context.current_class = null;
	};
	this.build = function(opcodes) {
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.body(context);
		}
	};
}

function _agb_interface(name, cmds) {
	_agb_command.call(this);
	this.name = name;
	this.cmds = cmds;
	this.isheader = function() {
		return true;
	};
	this.head = function(context) {
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.head(context);
		}
	};
	this.body = function(context) {
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.body(context);
		}
	};
	this.link = function(context) {
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.link(context);
		}
	};
	this.build = function(opcodes) {
	};
}

function _agb_field(type, name) {
	_agb_command.call(this);
	this.type = type;
	this.name = name;
	this.isheader = function() {
		return true;
	};
	this.head = function(context) {
		if (context.current_class != null) {
			context.current_class.fields.push(this);
		}
	};
	this.body = function(context) {
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
	};
}

function _agb_block(cmds) {
	_agb_command.call(this);
	this.cmds = cmds;
	this.vars = [];
	this.head = function(context) {
		context.blocks.push(this);
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.head(context);
		}
		context.blocks.pop();
	};
	this.body = function(context) {
		context.blocks.push(this);
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.body(context);
		}
		context.blocks.pop();
	};
	this.link = function(context) {
		context.blocks.push(this);
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			cmd.link(context);
		}
		context.blocks.pop();
	};
	this.build = function(opcodes) {
		var size = this.vars.length;
		opcodes.count -= size;
		opcodes.op_stack_push(size);
		for ( var n = 0; n < this.cmds.length; n++) {
			var cmd = this.cmds[n];
			if (!cmd.isheader()) {
				opcodes.count = 0;
				cmd.build(opcodes);
			}
		}
		opcodes.count += size;
		opcodes.op_stack_pop(size);
	};
}

function _agb_expression(left) {
	_agb_command.call(this);
	this.left = left;
	this.head = function(context) {
		this.left.head(context);
	};
	this.body = function(context) {
		this.left.body(context);
	};
	this.link = function(context) {
		this.left.link(context);
	};
	this.build = function(opcodes) {
		this.left.build(opcodes);
		opcodes.op_stack_pop(1);
	};
}

function _agb_for(inits, cond, nexts, cmd) {
	_agb_command.call(this);
	this.inits = inits;
	this.cond = cond;
	this.nexts = nexts;
	this.cmd = cmd;
	this.head = function(context) {
		for ( var n = 0; n < this.inits.length; n++) {
			var init = this.inits[n];
			init.head(context);
		}
		this.cond.head(context);
		for ( var n = 0; n < this.nexts.length; n++) {
			var next = this.nexts[n];
			next.head(context);
		}
		this.cmd.head(context);
	};
	this.body = function(context) {
		for ( var n = 0; n < this.inits.length; n++) {
			var init = this.inits[n];
			init.body(context);
		}
		this.cond.body(context);
		for ( var n = 0; n < this.nexts.length; n++) {
			var next = this.nexts[n];
			next.body(context);
		}
		this.cmd.body(context);
	};
	this.link = function(context) {
		for ( var n = 0; n < this.inits.length; n++) {
			var init = this.inits[n];
			init.link(context);
		}
		this.cond.link(context);
		for ( var n = 0; n < this.nexts.length; n++) {
			var next = this.nexts[n];
			next.link(context);
		}
		this.cmd.link(context);
	};
	this.build = function(opcodes) {
		var index = _agb_node_counter(this.cmd);
		for ( var n = 0; n < this.nexts.length; n++) {
			var next = this.nexts[n];
			index += _agb_node_counter(next);
		}
		index += 3;
		for ( var n = 0; n < this.inits.length; n++) {
			var init = this.inits[n];
			init.build(opcodes);
		}
		var pc = opcodes.pc;
		this.cond.build(opcodes);
		opcodes.op_jump_false_next(index);
		this.cmd.build(opcodes);
		for ( var n = 0; n < this.nexts.length; n++) {
			var next = this.nexts[n];
			next.build(opcodes);
		}
		opcodes.op_jump_int(pc);
	};
}

function _agb_if(cond, cmd, ecmd) {
	_agb_command.call(this);
	this.cond = cond;
	this.cmd = cmd;
	this.ecmd = ecmd;
	this.head = function(context) {
		this.cond.head(context);
		this.cmd.head(context);
		if (this.ecmd != null) {
			this.ecmd.head(context);
		}
	};
	this.body = function(context) {
		this.cond.body(context);
		this.cmd.body(context);
		if (this.ecmd != null) {
			this.ecmd.body(context);
		}
	};
	this.link = function(context) {
		this.cond.link(context);
		this.cmd.link(context);
		if (this.ecmd != null) {
			this.ecmd.link(context);
		}
	};
	this.build = function(opcodes) {
		var index = _agb_node_counter(this.cmd);
		var elseIndex = 0;
		if (this.ecmd != null) {
			elseIndex = _agb_node_counter(this.ecmd);
		}
		this.cond.build(opcodes);
		if (this.ecmd != null) {
			opcodes.op_jump_false_next(index + 3);
		} else {
			opcodes.op_jump_false_next(index);
		}
		this.cmd.build(opcodes);
		if (this.ecmd != null) {
			opcodes.op_jump_int_next(elseIndex);
			this.ecmd.build(opcodes);
		}
	};
}

function _agb_native() {
	_agb_command.call(this);
	this.head = function(context) {
	};
	this.body = function(context) {
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
	};
}

function _agb_repeat(cond, cmd) {
	_agb_command.call(this);
	this.cond = cond;
	this.cmd = cmd;
	this.head = function(context) {
		this.cond.head(context);
		this.cmd.head(context);
	};
	this.body = function(context) {
		this.cond.body(context);
		this.cmd.body(context);
	};
	this.link = function(context) {
		this.cond.link(context);
		this.cmd.link(context);
	};
	this.build = function(opcodes) {
		var index = opcodes.pc;
		this.cmd.build(opcodes);
		this.cond.build(opcodes);
		opcodes.op_jump_true(index);
	};
}

function _agb_return(left) {
	_agb_command.call(this);
	this.left = left;
	this.head = function(context) {
		this.left.head(context);
	};
	this.body = function(context) {
		this.left.body(context);
		var block = context.blocks.peek();
		this.index = 1 + block.vars.length;
		this.pops = 0;
		var method = context.methods.peek();
		if (method != null) {
			var paramsize = method.params.length;
			this.pops = paramsize + method.vars.length;
			this.index += paramsize;
		}
	};
	this.link = function(context) {
		this.left.link(context);
	};
	this.build = function(opcodes) {
		this.left.build(opcodes);
		opcodes.op_stack_store_abs(this.index);
		opcodes.op_jump_return(this.pops);
	};
}

function _agb_switch(cond, cases, elcmd) {
	_agb_command.call(this);
	this.cond = cond;
	this.cases = cases;
	this.elcmd = elcmd;
	this.head = function(context) {
		this.cond.head(context);
		for ( var n = 0; n < this.cases.length; n++) {
			var caseitem = this.cases[n];
			caseitem.cond.head(context);
			caseitem.cmd.head(context);
		}
		if (this.elcmd != null) {
			this.elcmd.head(context);
		}
	};
	this.body = function(context) {
		this.cond.body(context);
		for ( var n = 0; n < this.cases.length; n++) {
			var caseitem = this.cases[n];
			caseitem.cond.body(context);
			caseitem.cmd.body(context);
		}
		if (this.elcmd != null) {
			this.elcmd.body(context);
		}
	};
	this.link = function(context) {
		this.cond.link(context);
		for ( var n = 0; n < this.cases.length; n++) {
			var caseitem = this.cases[n];
			caseitem.cond.link(context);
			caseitem.cmd.link(context);
			var type = caseitem.cond.type;
			if (!(type.is_string() || type.is_number() || type.is_boolean())) {
				throw new _agb_semantic_error(caseitem.cond.token,
						"illegal type");
			}
			if (!type.can_cast(this.cond.type)) {
				throw new _agb_semantic_error(caseitem.cond.token,
						"illegal type");
			}
		}
		if (this.elcmd != null) {
			this.elcmd.link(context);
		}
	};
	this.build = function(opcodes) {
		var len = this.cases.length;
		var cmdcounter = [];
		var condcounter = [];
		for ( var n = 0; n < len; n++) {
			var caseitem = this.cases[n];
			cmdcounter.push(_agb_node_counter(caseitem.cmd) + 3);
			condcounter.push(_agb_node_counter(caseitem.cond));
		}
		var elcmdcounter = 0;
		if (this.elcmd != null) {
			elcmdcounter = _agb_node_counter(this.elcmd) + 3;
		}
		this.cond.build(opcodes);
		for ( var n = 0; n < len; n++) {
			var caseitem = this.cases[n];
			opcodes.op_stack_dup_abs(0);
			caseitem.cond.build(opcodes);
			opcodes.op_equal(caseitem.cond);
			{
				var index = 3;
				if (this.elcmd == null) {
					index += 3;
				}
				for ( var m = n + 1; m < len; m++) {
					index += 3 + 2 + 3 + condcounter[m];
				}
				for ( var m = 0; m < n; m++) {
					index += cmdcounter[m] + 3;
				}
				opcodes.op_jump_true_next(index);
			}
		}
		if (this.elcmd != null) {
			var index = 0;
			for ( var n = 0; n < len; n++) {
				index += cmdcounter[n] + 3;
			}
			opcodes.op_jump_int_next(index);
		} else {
			var index = 0;
			for ( var n = 0; n < len; n++) {
				index += cmdcounter[n] + 3;
			}
			opcodes.op_stack_pop(1);
			opcodes.op_jump_int_next(index);
		}
		for ( var n = 0; n < len; n++) {
			opcodes.count = 1;
			opcodes.op_stack_pop(1);
			this.cases[n].cmd.build(opcodes);
			{
				var index = 0;
				for ( var m = len - 1; m > n; m--) {
					index += cmdcounter[m] + 3;
				}
				if (this.elcmd != null) {
					index += elcmdcounter;
				}
				opcodes.op_jump_int_next(index);
			}
		}
		if (this.elcmd != null) {
			opcodes.count = 1;
			opcodes.op_stack_pop(1);
			this.elcmd.build(opcodes);
		}
	};
}

function _agb_while(cond, cmd) {
	_agb_command.call(this);
	this.cond = cond;
	this.cmd = cmd;
	this.head = function(context) {
		this.cond.head(context);
		this.cmd.head(context);
	};
	this.body = function(context) {
		this.cond.body(context);
		this.cmd.body(context);
	};
	this.link = function(context) {
		this.cond.link(context);
		this.cmd.link(context);
	};
	this.build = function(opcodes) {
		var index = _agb_node_counter(this.cmd);
		var beginPc = opcodes.pc;
		this.cond.build(opcodes);
		opcodes.op_jump_false_next(index + 3);
		this.cmd.build(opcodes);
		opcodes.op_jump_int(beginPc);
	};
}

function _agb_method(token, type, params, cmd) {
	_agb_command.call(this);
	this.token = token;
	this.type = type;
	this.params = params;
	this.cmd = cmd;
	this.vars = [];
	this.pc = -1;
	this.isheader = function() {
		return true;
	};
	this.head = function(context) {
		if (context.current_class != null) {
			context.current_class.methods.push(this);
		}
		context.defmethods.push(this);
		context.methods.push(this);
		this.id = context.method_sequence++;
		this.cmd.head(context);
		context.methods.pop(this);
	};
	this.body = function(context) {
		context.methods.push(this);
		this.cmd.body(context);
		context.methods.pop(this);
	};
	this.link = function(context) {
		context.methods.push(this);
		this.cmd.link(context);
		context.methods.pop(this);
	};
	this.build = function(opcodes) {
		if (this.pc < 0) {
			this.pc = opcodes.pc;
		}
		this.cmd.build(opcodes);
	};
}

function _agb_variable(token, type) {
	_agb_node.call(this);
	this.token = token;
	this.type = type;
	this.head = function(context) {
	};
	this.body = function(context) {
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
	};
}

function _agb_value(token) {
	_agb_node.call(this);
	this.token = token;
	this.type = null;
}

function _agb_lvalue(token) {
	_agb_value.call(this, token);
}

function _agb_lidentify(token) {
	_agb_lvalue.call(this, token);
	this.head = function(context) {
	};
	this.body = function(context) {
		var name = this.token.text;
		var blocks = context.blocks;
		var blocksize = blocks.length;
		for ( var m = blocksize - 1; m >= 0; m--) {
			var block = blocks[m];
			var vars = block.vars;
			var varsize = vars.length;
			for ( var n = varsize - 1; n >= 0; n--) {
				var node = vars[n];
				if (node.token.text == name) {
					this.node = node;
					this.index = varsize - n - 1;
					break;
				}
			}
			if (this.index != null) {
				break;
			}
		}
		if (this.index == null) {
			throw new _agb_semantic_error(this.token, "lidentify not found");
		}
	};
	this.link = function(context) {
		this.type = this.node.type;
	};
	this.build = function(opcodes) {
		opcodes.op_stack_store(this.index);
	};
}

function _agb_rvalue(token) {
	_agb_value.call(this, token);
}

function _agb_assign(token, left, right) {
	_agb_rvalue.call(this, token);
	this.left = left;
	this.right = right;
	this.head = function(context) {
		this.left.head(context);
		this.right.head(context);
	};
	this.body = function(context) {
		this.left.body(context);
		this.right.body(context);
	};
	this.link = function(context) {
		this.left.link(context);
		this.right.link(context);
		this.type = this.right.type;
	};
	this.build = function(opcodes) {
		this.right.build(opcodes);
		opcodes.op_stack_dup_abs(0);
		this.left.build(opcodes);
	};
}

function _agb_declare(token, type, left, value) {
	_agb_assign.call(this, token, left, value);
	this.type = type;
	this._agb_assign_head = this.head;
	this.head = function(context) {
		var node = new _agb_variable(this.token, this.type);
		context.blocks.peek().vars.push(node);
		if (context.methods.length > 0) {
			context.methods.peek().vars.push(node);
		}
		this._agb_assign_head(context);
	};
}

function _agb_call(token, params) {
	_agb_rvalue.call(this, token);
	this.params = params;
	this.head = function(context) {
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.head(context);
		}
	};
	this.body = function(context) {
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.body(context);
		}
		var name = this.token.text;
		for ( var n = 0; n < context.defmethods.length; n++) {
			var method = context.defmethods[n];
			if (name == method.token.text) {
				this.method = method;
				break;
			}
		}
		if (this.method == null) {
			throw new _agb_semantic_error(this.token, "not found the method");
		}
	};
	this.link = function(context) {
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.link(context);
		}
		this.type = this.method.type;
	};
	this.build = function(opcodes) {
		opcodes.op_stack_push(1);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.build(opcodes);
		}
		opcodes.op_jump_call(this.method.pc);
	};
}

function _agb_ridentify(token) {
	_agb_rvalue.call(this, token);
	this.head = function(context) {
	};
	this.body = function(context) {
		var name = this.token.text;
		for ( var m = context.blocks.length - 1; m >= 0; m--) {
			var block = context.blocks[m];
			var vars = block.vars;
			var size = vars.length;
			for ( var n = size - 1; n >= 0; n--) {
				var node = vars[n];
				if (node.token.text == name) {
					this.vari = size - n - 1;
					this.node = node;
					break;
				}
			}
			if (this.node != null) {
				break;
			}
		}
		if (this.node == null) {
			var method = context.methods.peek();
			if (method != null) {
				var params = method.params;
				var paramCount = params.length;
				for ( var n = 0; n < paramCount; n++) {
					var param = params[n];
					if (name == param.token.text) {
						this.vari = paramCount + method.vars.length - n - 1;
						this.node = param;
						break;
					}
				}
			}
		}
	};
	this.link = function(context) {
		this.type = this.node.type;
		this.type.link(context);
		if (this.node == null) {
			var name = this.token.text;
			var params = method.params;
			var paramCount = params.length;
			for ( var n = 0; n < context.defclasses.length; n++) {
				var defclass = context.defclasses[n];
				if (name == defclass.name.text) {
					this.vari = defclass.id;
					this.node = defclass;
					break;
				}
			}
		}
		if (this.node == null) {
			throw new _agb_semantic_error(this.token, "ridentify not found");
		}
	};
	this.build = function(opcodes) {
		opcodes.op_stack_load(this.vari);
	};
}

function _agb_rgetfield(left, token) {
	_agb_unary.call(this, token, left);
	this._agb_unary_link = this.head;
	this._agb_unary_body = this.body;
	this._agb_unary_link = this.link;
	this.head = function(context) {
		this._agb_unary_head(context);
	};
	this.body = function(context) {
		this._agb_unary_body(context);
	};
	this.link = function(context) {
		this._agb_unary_link(context);

	};
	this.build = function(opcodes) {
		opcodes.op_load_new(this.type.id);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			// param.build(context);
		}
	};
}

function _agb_rsetfield(token, type, params) {
	_agb_rvalue.call(this, token);
	this.type = type;
	this.params = params;
	this.head = function(context) {
		this.type.head(context);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.head(context);
		}
	};
	this.body = function(context) {
		this.type.body(context);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.body(context);
		}
		if (!this.type.is_object()) {
			throw new _agb_semantic_error(this.type.token,
					"new only for class type");
		}
	};
	this.link = function(context) {
		this.type.link(context);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.link(context);
		}
	};
	this.build = function(opcodes) {
		opcodes.op_load_new(this.type.id);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			// param.build(context);
		}
	};
}

function _agb_new(token, type, params) {
	_agb_rvalue.call(this, token);
	this.type = type;
	this.params = params;
	this.head = function(context) {
		this.type.head(context);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.head(context);
		}
	};
	this.body = function(context) {
		this.type.body(context);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.body(context);
		}
		if (!this.type.is_object()) {
			throw new _agb_semantic_error(this.type.token,
					"new only for class type");
		}
	};
	this.link = function(context) {
		this.type.link(context);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			param.link(context);
		}
	};
	this.build = function(opcodes) {
		opcodes.op_load_new(this.type.id);
		for ( var n = 0; n < this.params.length; n++) {
			var param = params[n];
			// param.build(context);
		}
	};
}

function _agb_unary(token, left) {
	_agb_rvalue.call(this, token);
	this.left = left;
	this.head = function(context) {
		this.left.head(context);
	};
	this.body = function(context) {
		this.left.body(context);
	};
	this.link = function(context) {
		this.left.link(context);
	};
	this.build = function(opcodes) {
		this.left.build(context);
	};
}

function _agb_cast(token, left) {
	_agb_unary.call(this, token, left);
	this.head = function(context) {
	};
	this.body = function(context) {
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
	};
}

function _agb_negative(token, left) {
	_agb_unary.call(this, token, left);
	this.head = function(context) {
	};
	this.body = function(context) {
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
	};
}

function _agb_leftcall(token, left) {
	_agb_unary.call(this, token, left);
	this.head = function(context) {
	};
	this.body = function(context) {
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
	};
}

function _agb_posdec(token, left) {
	_agb_unary.call(this, token, left);
	this._agb_unary_head = this.head;
	this.head = function(context) {
		this._agb_unary_head(context);
		this.lvalue = new _agb_lidentify(left.token);
		this.number = new _agb_number(new _agb_token("1", -1, -1, -1));
		this.lvalue.head(context);
		this.number.head(context);
	};
	this._agb_unary_body = this.body;
	this.body = function(context) {
		this._agb_unary_body(context);
		this.lvalue.body(context);
		this.number.body(context);
	};
	this._agb_unary_link = this.link;
	this.link = function(context) {
		this._agb_unary_link(context);
		this.lvalue.link(context);
		this.number.link(context);
		if (!this.left.type.is_number()) {
			throw new _agb_semantic_error(this.token, "only a number identify");
		}
		this.type = this.left.type;
	};
	this.build = function(opcodes) {
		this.left.build(opcodes);
		opcodes.op_stack_dup_abs(0);
		this.number.build(opcodes);
		opcodes.op_num_sub();
		this.lvalue.build(opcodes);
	};
}

function _agb_posinc(token, left) {
	_agb_unary.call(this, token, left);
	this._agb_unary_head = this.head;
	this.head = function(context) {
		this._agb_unary_head(context);
		this.lvalue = new _agb_lidentify(left.token);
		this.number = new _agb_number(new _agb_token("1", -1, -1, -1));
		this.lvalue.head(context);
		this.number.head(context);
	};
	this._agb_unary_body = this.body;
	this.body = function(context) {
		this._agb_unary_body(context);
		this.lvalue.body(context);
		this.number.body(context);
	};
	this._agb_unary_link = this.link;
	this.link = function(context) {
		this._agb_unary_link(context);
		this.lvalue.link(context);
		this.number.link(context);
		if (!this.left.type.is_number()) {
			throw new _agb_semantic_error(this.token, "only a number identify");
		}
		this.type = this.left.type;
	};
	this.build = function(opcodes) {
		this.left.build(opcodes);
		opcodes.op_stack_dup_abs(0);
		this.number.build(opcodes);
		opcodes.op_num_sum();
		this.lvalue.build(opcodes);
	};
}

function _agb_predec(token, left) {
	_agb_unary.call(this, token, left);
	this._agb_unary_head = this.head;
	this.head = function(context) {
		this._agb_unary_head(context);
		this.lvalue = new _agb_lidentify(left.token);
		this.number = new _agb_number(new _agb_token("1", -1, -1, -1));
		this.lvalue.head(context);
		this.number.head(context);
	};
	this._agb_unary_body = this.body;
	this.body = function(context) {
		this._agb_unary_body(context);
		this.lvalue.body(context);
		this.number.body(context);
	};
	this._agb_unary_link = this.link;
	this.link = function(context) {
		this._agb_unary_link(context);
		this.lvalue.link(context);
		this.number.link(context);
		if (!this.left.type.is_number()) {
			throw new _agb_semantic_error(this.token, "only a number identify");
		}
		this.type = this.left.type;
	};
	this.build = function(opcodes) {
		this.left.build(opcodes);
		this.number.build(opcodes);
		opcodes.op_num_sub();
		opcodes.op_stack_dup_abs(0);
		this.lvalue.build(opcodes);
	};
}

function _agb_preinc(token, left) {
	_agb_unary.call(this, token, left);
	this._agb_unary_head = this.head;
	this.head = function(context) {
		this._agb_unary_head(context);
		this.lvalue = new _agb_lidentify(left.token);
		this.number = new _agb_number(new _agb_token("1", -1, -1, -1));
		this.lvalue.head(context);
		this.number.head(context);
	};
	this._agb_unary_body = this.body;
	this.body = function(context) {
		this._agb_unary_body(context);
		this.lvalue.body(context);
		this.number.body(context);
	};
	this._agb_unary_link = this.link;
	this.link = function(context) {
		this._agb_unary_link(context);
		this.lvalue.link(context);
		this.number.link(context);
		if (!this.left.type.is_number()) {
			throw new _agb_semantic_error(this.token, "only a number identify");
		}
		this.type = this.left.type;
	};
	this.build = function(opcodes) {
		this.left.build(opcodes);
		this.number.build(opcodes);
		opcodes.op_num_sum();
		opcodes.op_stack_dup_abs(0);
		this.lvalue.build(opcodes);
	};
}

function _agb_binary(token, left, right) {
	_agb_unary.call(this, token, left);
	this.right = right;
	this.head = function(context) {
		this.left.head(context);
		this.right.head(context);
	};
	this.body = function(context) {
		this.left.body(context);
		this.right.body(context);
	};
	this.link = function(context) {
		this.left.link(context);
		this.right.link(context);
	};
	this.build = function(opcodes) {
		this.left.build(opcodes);
		this.right.build(opcodes);
	};
}

function _agb_and(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		this._agb_binary_build(opcodes);
		opcodes.op_bool_and();
	};
}

function _agb_or(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		this._agb_binary_build(opcodes);
		opcodes.op_bool_or();
	};
}

function _agb_sum(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		if (this.left.type.is_number() && this.right.type.is_number()) {
		} else if (this.left.type.is_string() && !this.right.type.is_object()) {
		} else {
			throw new _agb_semantic_error(this.token, "illegal operator");
		}
		this.type = this.left.type;
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		this._agb_binary_build(opcodes);
		if (this.left.type.is_number()) {
			opcodes.op_num_sum();
		} else if (this.left.type.is_string()) {
			if (this.right.type.is_boolean()) {
				opcodes.op_bool_to_str();
			} else if (this.right.type.is_number()) {
				opcodes.op_num_to_str();
			}
			opcodes.op_str_sum();
		}
	};
}

function _agb_sub(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		if (this.left.type.is_number() && this.right.type.is_number()) {
		} else {
			throw new _agb_semantic_error(this.token, "illegal operator");
		}
		this.type = this.left.type;
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		this._agb_binary_build(opcodes);
		if (this.left.type.is_number()) {
			opcodes.op_num_sub();
		}
	};
}

function _agb_mul(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		if (this.left.type.is_number() && this.right.type.is_number()) {
		} else {
			throw new _agb_semantic_error(this.token, "illegal operator");
		}
		this.type = this.left.type;
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		this._agb_binary_build(opcodes);
		if (this.left.type.is_number()) {
			opcodes.op_num_mul();
		}
	};
}

function _agb_div(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		if (this.left.type.is_number() && this.right.type.is_number()) {
		} else {
			throw new _agb_semantic_error(this.token, "illegal operator");
		}
		this.type = this.left.type;
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		this._agb_binary_build(opcodes);
		if (this.left.type.is_number()) {
			opcodes.op_num_div();
		}
	};
}

function _agb_equal(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		if (this.left.type.is_number()) {
			if (!this.right.type.is_number()) {
				this.value = false;
			}
		} else if (this.left.type.is_string()) {
			if (!this.right.type.is_string()) {
				this.value = false;
			}
		} else if (this.left.type.is_boolean()) {
			if (!this.right.type.is_boolean()) {
				this.value = false;
			}
		} else {
			this.value = false;
		}
		this.type = new _agb_boolean_type(this.token);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		if (this.value != null) {
			if (this.value) {
				opcodes.op_load_true();
			} else {
				opcodes.op_load_false();
			}
		} else {
			this._agb_binary_build(opcodes);
			opcodes.op_equal(this.left);
		}
	};
}

function _agb_notequal(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		if (this.left.type.is_number()) {
			if (!this.right.type.is_number()) {
				this.value = true;
			}
		} else if (this.left.type.is_string()) {
			if (!this.right.type.is_string()) {
				this.value = true;
			}
		} else if (this.left.type.is_boolean()) {
			if (!this.right.type.is_boolean()) {
				this.value = true;
			}
		} else {
			this.value = true;
		}
		this.type = new _agb_boolean_type(this.token);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		if (this.value != null) {
			if (this.value) {
				opcodes.op_load_true();
			} else {
				opcodes.op_load_false();
			}
		} else {
			this._agb_binary_build(opcodes);
			opcodes.op_nequal(this.left);
		}
	};
}

function _agb_greater(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		if (this.left.type.is_number()) {
			if (!this.right.type.is_number()) {
				this.value = false;
			}
		} else if (this.left.type.is_string()) {
			if (!this.right.type.is_string()) {
				this.value = false;
			}
		} else {
			this.value = false;
		}
		this.type = new _agb_boolean_type(this.token);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		if (this.value != null) {
			if (this.value) {
				opcodes.op_load_true();
			} else {
				opcodes.op_load_false();
			}
		} else {
			this._agb_binary_build(opcodes);
			opcodes.op_gt(this.left);
		}
	};
}

function _agb_greaterequal(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		this._agb_binary_link(context);
		if (this.left.type.is_number()) {
			if (!this.right.type.is_number()) {
				this.value = false;
			}
		} else if (this.left.type.is_string()) {
			if (!this.right.type.is_string()) {
				this.value = false;
			}
		} else {
			this.value = false;
		}
		this.type = new _agb_boolean_type(this.token);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		if (this.value != null) {
			if (this.value) {
				opcodes.op_load_true();
			} else {
				opcodes.op_load_false();
			}
		} else {
			this._agb_binary_build(opcodes);
			opcodes.op_egt(this.left);
		}
	};
}

function _agb_lower(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		this._agb_binary_link(context);
		if (this.left.type.is_number()) {
			if (!this.right.type.is_number()) {
				this.value = false;
			}
		} else if (this.left.type.is_string()) {
			if (!this.right.type.is_string()) {
				this.value = false;
			}
		} else {
			this.value = false;
		}
		this.type = new _agb_boolean_type(this.token);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		if (this.value != null) {
			if (this.value) {
				opcodes.op_load_true();
			} else {
				opcodes.op_load_false();
			}
		} else {
			this._agb_binary_build(opcodes);
			opcodes.op_lt(this.left);
		}
	};
}

function _agb_lowerequal(token, left, right) {
	_agb_binary.call(this, token, left, right);
	this._agb_binary_link = this.link;
	this.link = function(context) {
		this._agb_binary_link(context);
		this._agb_binary_link(context);
		if (this.left.type.is_number()) {
			if (!this.right.type.is_number()) {
				this.value = false;
			}
		} else if (this.left.type.is_string()) {
			if (!this.right.type.is_string()) {
				this.value = false;
			}
		} else {
			this.value = false;
		}
		this.type = new _agb_boolean_type(this.token);
	};
	this._agb_binary_build = this.build;
	this.build = function(opcodes) {
		if (this.value != null) {
			if (this.value) {
				opcodes.op_load_true();
			} else {
				opcodes.op_load_false();
			}
		} else {
			this._agb_binary_build(opcodes);
			opcodes.op_elt(this.left);
		}
	};
}

function _agb_ternary(token, left, right, center) {
	_agb_binary.call(this, token, left, right);
	this.center = center;
}

function _agb_primitive(token) {
	_agb_rvalue.call(this, token);
}

function _agb_number(token) {
	_agb_primitive.call(this, token);
	this.head = function(context) {
	};
	this.body = function(context) {
		this.value = parseFloat(this.token.text);
		this.type = new _agb_number_type(this.token);
	};
	this.link = function(context) {
		this.index = context.numbers.indexOf(this.value);
		if (this.index < 0) {
			this.index = context.numbers.length;
			context.numbers.push(this.value);
		}
	};
	this.build = function(opcodes) {
		opcodes.op_load_num(this.index);
	};
}

function _agb_string(token) {
	_agb_primitive.call(this, token);
	this.head = function(context) {
	};
	this.body = function(context) {
		this.value = this.token.text;
		this.type = new _agb_string_type(this.token);
	};
	this.link = function(context) {
		this.index = context.strings.indexOf(this.value);
		if (this.index < 0) {
			this.index = context.strings.length;
			context.strings.push(this.value);
		}
	};
	this.build = function(opcodes) {
		opcodes.op_load_str(this.index);
	};
}

function _agb_boolean(token, value) {
	_agb_primitive.call(this, token);
	this.value = value;
	this.head = function(context) {
	};
	this.body = function(context) {
		this.type = new _agb_boolean_type(this.token);
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
		if (this.value) {
			opcodes.op_load_true();
		} else {
			opcodes.op_load_false();
		}
	};
}

function _agb_null(token) {
	_agb_primitive.call(this, token);
	this.head = function(context) {
	};
	this.body = function(context) {
		this.type = new _agb_null_type(this.token);
	};
	this.link = function(context) {
	};
	this.build = function(opcodes) {
		opcodes.op_load_null();
	};
}

function _agb_dec2hex(dec) {
	var hex = '0123456789ABCDEF';
	return (hex[dec >> 4] + hex[dec & 15]);
}

function _agb_int2hex(dec) {
	var hex = '0123456789ABCDEF';
	return (hex[dec >> 4] + hex[dec & 15]);
}

function _agb_hex2dec(hex) {
	return (parseInt(hex, 16))
}

function _agb_encode(string) {
	return escape(this._utf8_encode(string));
}

function _agb_decode(string) {
	return this._utf8_decode(unescape(string));
}

function _agb_utf8_encode(string) {
	string = string.replace(/\r\n/g, "\n");
	var utftext = "";
	for ( var n = 0; n < string.length; n++) {
		var c = string.charCodeAt(n);
		if (c < 128) {
			utftext += String.fromCharCode(c);
		} else if ((c > 127) && (c < 2048)) {
			utftext += String.fromCharCode((c >> 6) | 192);
			utftext += String.fromCharCode((c & 63) | 128);
		} else {
			utftext += String.fromCharCode((c >> 12) | 224);
			utftext += String.fromCharCode(((c >> 6) & 63) | 128);
			utftext += String.fromCharCode((c & 63) | 128);
		}
	}
	return utftext;
}

function _agb_utf8_decode(utftext) {
	var string = "";
	var i = 0;
	var c = c1 = c2 = 0;
	while (i < utftext.length) {
		c = utftext.charCodeAt(i);
		if (c < 128) {
			string += String.fromCharCode(c);
			i++;
		} else if ((c > 191) && (c < 224)) {
			c2 = utftext.charCodeAt(i + 1);
			string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
			i += 2;
		} else {
			c2 = utftext.charCodeAt(i + 1);
			c3 = utftext.charCodeAt(i + 2);
			string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6)
					| (c3 & 63));
			i += 3;
		}
	}
	return string;
}

Array.prototype.indexOf = function(v) {
	var len = this.length;
	for ( var n = 0; n < len; n++) {
		if (this[n] == v) {
			return n;
		}
	}
	return -1;
};

Array.prototype.peek = function() {
	if (this.length == 0) {
		return null;
	} else {
		return this[this.length - 1];
	}
};