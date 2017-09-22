var GP_VM = 1;
var GP_STACK = 2;
var GP_DEF = 3;
var GP_LOAD = 4;
var GP_JUMP = 5;
var GP_BOOL = 6;
var GP_INT = 7;
var GP_NUM = 8;
var GP_STR = 9;
var GP_ARRAY = 10;
var GP_USER = 32;
var OP_VM_HALF = 1;
var OP_VM_GC = 2;
var OP_STACK_PUSH = 1;
var OP_STACK_POP = 2;
var OP_STACK_LOAD = 3;
var OP_STACK_STORE = 4;
var OP_STACK_INT = 5;
var OP_DEF_NUM = 2;
var OP_DEF_STR = 3;
var OP_DEF_CLASS = 5;
var OP_DEF_CAST = 6;
var OP_DEF_NUM_LEN = 8;
var OP_DEF_STR_LEN = 9;
var OP_DEF_CLASS_LEN = 10;
var OP_DEF_CAST_LEN = 11;
var OP_LOAD_TRUE = 1;
var OP_LOAD_FALSE = 2;
var OP_LOAD_NUM = 4;
var OP_LOAD_STR = 5;
var OP_LOAD_NULL = 6;
var OP_JUMP_STACK = 1;
var OP_JUMP_INT = 2;
var OP_JUMP_TRUE = 3;
var OP_JUMP_FALSE = 4;
var OP_JUMP_CALL = 5;
var OP_JUMP_RETURN = 6;
var OP_BOOL_NOT = 1;
var OP_BOOL_OR = 2;
var OP_BOOL_AND = 3;
var OP_BOOL_EQUAL = 4;
var OP_BOOL_NEQUAL = 5;
var OP_BOOL_TO_STR = 6;
var OP_NUM_NEG = 1;
var OP_NUM_SUM = 2;
var OP_NUM_SUB = 3;
var OP_NUM_MUL = 4;
var OP_NUM_DIV = 5;
var OP_NUM_EQUAL = 6;
var OP_NUM_NEQUAL = 7;
var OP_NUM_GT = 8;
var OP_NUM_EGT = 9;
var OP_NUM_LT = 10;
var OP_NUM_ELT = 11;
var OP_NUM_TO_STR = 12;
var OP_STR_LEN = 1;
var OP_STR_SUM = 2;
var OP_STR_EQUAL = 3;
var OP_STR_NEQUAL = 4;
var OP_STR_GT = 5;
var OP_STR_EGT = 6;
var OP_STR_LT = 7;
var OP_STR_ELT = 8;
var OP_ARRAY_NEW = 1;
var OP_ARRAY_LEN = 2;
var OP_ARRAY_GET = 3;
var OP_ARRAY_SET = 4;
var MAX_OBJECT_STACK = 32;

function vm(base16, context) {
	var irs;
	if (context == null) {
		context = {
			irs : [],
			numPool : [],
			strPool : [],
			sir : [],
			ir : 0,
			pc : 0,
			os : [],
			ios : -1
		};
		irs = [];
		for ( var n = 0, m = 0; n < base16.length; m++) {
			irs[m] = ((base16.charCodeAt(n++) - 97) << 28)
					+ ((base16.charCodeAt(n++) - 97) << 24)
					+ ((base16.charCodeAt(n++) - 97) << 20)
					+ ((base16.charCodeAt(n++) - 97) << 16)
					+ ((base16.charCodeAt(n++) - 97) << 12)
					+ ((base16.charCodeAt(n++) - 97) << 8)
					+ ((base16.charCodeAt(n++) - 97) << 4)
					+ (base16.charCodeAt(n++) - 97);
		}
	} else {
		irs = context.irs;
	}
	var numPool = context.numPool;
	var strPool = context.strPool;
	var sir = context.sir;
	var ir = context.ir
	var pc = context.pc;
	var os = context.os;
	var ios = context.ios;
	var c1, c2, c3, c4, pcs = 1024 * 1024;
	for (;;) {
		{
			if (pcs == 0) {
				return {
					irs : irs,
					numPool : numPool,
					strPool : strPool,
					sir : sir,
					ir : ir,
					pc : pc,
					os : os,
					ios : ios
				};
			}
			pcs--;
		}
		switch (irs[pc++]) {
		case GP_VM: {
			switch (irs[pc++]) {
			case OP_VM_HALF:
				if (ios < 0) {
					return null;
				} else {
					return os[ios];
				}
			case OP_VM_GC:
				break;
			default: {
				return null;
			}
			}
			break;
		}
		case GP_STACK: {
			switch (irs[pc++]) {
			case OP_STACK_PUSH:
				ios += irs[pc++];
				break;
			case OP_STACK_POP:
				ios -= irs[pc++];
				break;
			case OP_STACK_LOAD:
				os[ios + 1] = os[ios - irs[pc++]];
				ios++;
				break;
			case OP_STACK_STORE:
				os[ios - irs[pc++]] = os[ios];
				ios--;
				break;
			case OP_STACK_INT:
				ios++;
				os[ios] = irs[pc++];
				break;
			default: {
				return null;
			}
			}
			break;
		}
		case GP_DEF: {
			switch (irs[pc++]) {
			case OP_DEF_NUM:
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
				numPool.push(Number(sb).valueOf());
				break;
			case OP_DEF_STR:
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
			case OP_DEF_CLASS:
				break;
			case OP_DEF_CAST:
				break;
			case OP_DEF_NUM_LEN:
				pc++;
				break;
			case OP_DEF_STR_LEN:
				pc++;
				break;
			default: {
				return null;
			}
			}
			break;
		}
		case GP_LOAD: {
			switch (irs[pc++]) {
			case OP_LOAD_TRUE: {
				ios++;
				os[ios] = true;
				break;
			}
			case OP_LOAD_FALSE: {
				ios++;
				os[ios] = false;
				break;
			}
			case OP_LOAD_NUM: {
				ios++;
				os[ios] = numPool[irs[pc++]];
				break;
			}
			case OP_LOAD_STR: {
				ios++;
				os[ios] = strPool[irs[pc++]];
				break;
			}
			case OP_LOAD_NULL: {
				ios++;
				os[ios] = null;
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case GP_JUMP: {
			switch (irs[pc++]) {
			case OP_JUMP_STACK: {
				break;
			}
			case OP_JUMP_INT: {
				pc = irs[pc++];
				break;
			}
			case OP_JUMP_TRUE: {
				if (os[ios] == true) {
					pc = irs[pc];
				} else {
					pc++;
				}
				ios--;
				break;
			}
			case OP_JUMP_FALSE: {
				if (os[ios] == false) {
					pc = irs[pc];
				} else {
					pc++;
				}
				ios--;
				break;
			}
			case OP_JUMP_CALL: {
				sir.push(pc + 1);
				pc = irs[pc++];
				break;
			}
			case OP_JUMP_RETURN: {
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
		case GP_BOOL: {
			switch (irs[pc++]) {
			case OP_BOOL_OR: {
				ios--;
				os[ios] = os[ios] || os[ios + 1];
				break;
			}
			case OP_BOOL_AND: {
				ios--;
				os[ios] = os[ios] && os[ios + 1];
				break;
			}
			case OP_BOOL_NOT: {
				os[ios] = !os[ios];
				break;
			}
			case OP_BOOL_EQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case OP_BOOL_NEQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case OP_BOOL_TO_STR: {
				os[ios] = '' + os[ios];
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case GP_NUM: {
			switch (irs[pc++]) {
			case OP_NUM_NEG: {
				os[ios] = os[ios] * -1;
				break;
			}
			case OP_NUM_SUM: {
				ios--;
				os[ios] = os[ios] + os[ios + 1];
				break;
			}
			case OP_NUM_SUB: {
				ios--;
				os[ios] = os[ios] - os[ios + 1];
				break;
			}
			case OP_NUM_MUL: {
				ios--;
				os[ios] = os[ios] * os[ios + 1];
				break;
			}
			case OP_NUM_DIV: {
				ios--;
				os[ios] = os[ios] / os[ios + 1];
				break;
			}
			case OP_NUM_EQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case OP_NUM_NEQUAL: {
				ios--;
				os[ios] = os[ios] != os[ios + 1];
				break;
			}
			case OP_NUM_GT: {
				ios--;
				os[ios] = os[ios] > os[ios + 1];
				break;
			}
			case OP_NUM_EGT: {
				ios--;
				os[ios] = os[ios] >= os[ios + 1];
				break;
			}
			case OP_NUM_LT: {
				ios--;
				os[ios] = os[ios] < os[ios + 1];
				break;
			}
			case OP_NUM_ELT: {
				ios--;
				os[ios] = os[ios] <= os[ios + 1];
				break;
			}
			case OP_NUM_TO_STR: {
				os[ios] = '' + os[ios];
				break;
			}
			default: {
				return null;
			}
			}
			break;
		}
		case GP_STR: {
			switch (irs[pc++]) {
			case OP_STR_LEN: {
				os[ios] = os[ios].length;
				break;
			}
			case OP_STR_SUM: {
				ios--;
				os[ios] = os[ios] + os[ios + 1];
				break;
			}
			case OP_STR_EQUAL: {
				ios--;
				os[ios] = os[ios] == os[ios + 1];
				break;
			}
			case OP_STR_NEQUAL: {
				ios--;
				os[ios] = os[ios] != os[ios + 1];
				break;
			}
			case OP_STR_GT: {
				ios--;
				os[ios] = os[ios] > os[ios + 1];
				break;
			}
			case OP_STR_EGT: {
				ios--;
				os[ios] = os[ios] >= os[ios + 1];
				break;
			}
			case OP_STR_LT: {
				ios--;
				os[ios] = os[ios] < os[ios + 1];
				break;
			}
			case OP_STR_ELT: {
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
		case GP_ARRAY: {
			switch (irs[pc++]) {
			case OP_ARRAY_NEW: {
				os[ios] = [];
				break;
			}
			case OP_ARRAY_LEN: {
				os[ios] = os[ios].length;
				break;
			}
			case OP_ARRAY_GET: {
				ios--;
				os[ios] = os[ios][os[ios + 1] - 1];
				break;
			}
			case OP_ARRAY_SET: {
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
}
