#include "agb.h"

#define CLASS 1

typedef struct agb_context_t {
	agb_token_t** tokens;
	long index;
	long size;
} agb_context_t;

agb_node_t* agb_syntax_file_cmd(agb_context_t* c);

int agb_syntax_is_throw_class(agb_context_t* c);

agb_node_t* agb_syntax_do_throw_class(agb_context_t* c);

agb_class_node_t* agb_syntax_do_class(agb_context_t* c);

agb_token_t* agb_syntax_is(agb_context_t* c, int type);

agb_token_t* agb_syntax_can(agb_context_t* c, int type);

agb_token_t* agb_syntax_is_symbol(agb_context_t* c, wchar_t type);

agb_token_t* agb_syntax_can_symbol(agb_context_t* c, wchar_t type);

agb_token_t* agb_syntax_is_next(register agb_context_t* c, int type, int next);

agb_token_t* agb_syntax_can_next(register agb_context_t* c, int type, int next);

agb_token_t* agb_syntax_is_symbol_next(register agb_context_t* c, wchar_t type, int next);

agb_token_t* agb_syntax_can_symbol_next(register agb_context_t* c, wchar_t type, int next);

agb_node_t* agb_syntax_do_class_cmd(agb_context_t* c);

int agb_syntax_is_method(agb_context_t* c);

agb_node_t* agb_syntax_do_method(agb_context_t* c);

agb_node_t* agb_syntax_do_field(agb_context_t* c);

agb_type_t* agb_syntax_do_type(agb_context_t* c);

agb_type_t* agb_syntax_do_type_know(agb_context_t* c);

#define check(A) if (!(A)) { goto end; }

agb_list_node_t* agb_syntax(agb_token_t** tokens, long size) {
	if (!tokens) {
		return 0;
	}
	agb_list_t* list = agb_list_new();
	if (!list) {
		return 0;
	}
	agb_context_t c = { tokens, 0, size };
	while (c.index < size) {
		agb_node_t* node = agb_syntax_file_cmd(&c);
		check(node);
		if (agb_list_add(list, node)) {
			agb_node_free(node);
			goto end;
		}
	}
	return list;
	end: {
		agb_list_free_func(list, (void (*)(void*)) agb_node_free);
		return 0;
	}
}

agb_node_t* agb_syntax_file_cmd(agb_context_t* c) {
	if (agb_syntax_is_throw_class(c)) {
		return (agb_node_t*) agb_syntax_do_throw_class(c);
	} else {
		return (agb_node_t*) agb_syntax_do_class(c);
	}
}

int agb_syntax_is_throw_class(agb_context_t* c) {
	return 0;
}

agb_node_t* agb_syntax_do_throw_class(agb_context_t* c) {
	return 0;
}

agb_class_node_t* agb_syntax_do_class(agb_context_t* c) {
	agb_class_node_t* node = agb_memory_alloc(sizeof(agb_class_node_t));
	check(node);
	node->type = AGB_CLASS_NODE;
	check(agb_syntax_can(c, AGB_TOKEN_CLASS));
	check(agb_syntax_can(c, AGB_TOKEN_ID));
	if (agb_syntax_can(c, AGB_TOKEN_EXTENDS)) {
		check(agb_syntax_can(c, AGB_TOKEN_ID))
	}
	check(agb_syntax_can(c, AGB_TOKEN_DO));
	while (!agb_syntax_is(c, AGB_TOKEN_END)) {
		agb_node_t* child = agb_syntax_do_class_cmd(c);
		check(child);
		break;
	}
	check(agb_syntax_can(c, AGB_TOKEN_END));
	return node;
	end: {
		agb_node_free((agb_node_t*) node);
		return 0;
	}
}

agb_node_t* agb_syntax_do_class_cmd(agb_context_t* c) {
	if (agb_syntax_is_method(c)) {
		return agb_syntax_do_method(c);
	} else {
		return agb_syntax_do_field(c);
	}
	return 0;
}

int agb_syntax_is_method(agb_context_t* c) {
	return 0;
}

agb_node_t* agb_syntax_do_method(agb_context_t* c) {
	return 0;
}

agb_node_t* agb_syntax_do_field(agb_context_t* c) {
	agb_type_t* type = agb_syntax_do_type(c);
	check(type);
	agb_token_t* name = agb_syntax_can(c, AGB_TOKEN_ID);
	check(name);
	return 0;
	end: {
		return 0;
	}
}

agb_type_t* agb_syntax_do_type(agb_context_t* c) {
	agb_type_t* type = agb_syntax_do_type_know(c);
	if (type) {
		return type;
	}
	agb_token_t* name = agb_syntax_can(c, AGB_TOKEN_ID);
	check(name);
	return 0;
	end: {
		return 0;
	}
}

agb_type_t* agb_syntax_do_type_know(agb_context_t* c) {
	if (agb_syntax_is(c, AGB_TOKEN_NUM)) {
		agb_token_t* name = agb_syntax_can(c, AGB_TOKEN_NUM);
		check(name);
		if (agb_syntax_is_symbol(c, L'[') && agb_syntax_is_symbol_next(c, L']', 1)) {
			check(agb_syntax_is_symbol(c, L'['));
			check(agb_syntax_is_symbol(c, L']'));
			agb_type_t* type = agb_memory_alloc(sizeof(agb_type_t));
			check(type);
			type->type = AGB_TYPE_NUMS_NODE;
			return type;
		} else {
			agb_type_t* type = agb_memory_alloc(sizeof(agb_type_t));
			check(type);
			type->type = AGB_TYPE_NUM_NODE;
			return type;
		}
	} else if (agb_syntax_is(c, AGB_TOKEN_BOOL)) {
		agb_token_t* name = agb_syntax_can(c, AGB_TOKEN_BOOL);
		check(name);
		if (agb_syntax_is_symbol(c, L'[') && agb_syntax_is_symbol_next(c, L']', 1)) {
			check(agb_syntax_is_symbol(c, L'['));
			check(agb_syntax_is_symbol(c, L']'));
			agb_type_t* type = agb_memory_alloc(sizeof(agb_type_t));
			check(type);
			type->type = AGB_TYPE_BOOLS_NODE;
			return type;
		} else {
			agb_type_t* type = agb_memory_alloc(sizeof(agb_type_t));
			check(type);
			type->type = AGB_TYPE_BOOL_NODE;
			type->token = agb_token_dup(name);
			check(type->token);
			return type;
		}
	} else if (agb_syntax_is(c, AGB_TOKEN_STR)) {
		agb_token_t* name = agb_syntax_can(c, AGB_TOKEN_NUM);
		check(name);
		agb_type_t* type = agb_memory_alloc(sizeof(agb_type_t));
		check(type);
		type->type = AGB_TYPE_STR_NODE;
		return type;
	} else if (agb_syntax_is(c, AGB_TOKEN_OBJ)) {
		agb_token_t* name = agb_syntax_can(c, AGB_TOKEN_NUM);
		check(name);
		agb_type_t* type = agb_memory_alloc(sizeof(agb_type_t));
		check(type);
		type->type = AGB_TYPE_OBJ_NODE;
		return type;
	} else if (agb_syntax_is(c, AGB_TOKEN_FUNCTION)) {
		agb_type_t* type = agb_memory_alloc(sizeof(agb_type_t));
		check(type);
		type->type = AGB_TYPE_FUNC_NODE;
		return type;
	}
	return 0;
	end: {
		return 0;
	}
}

agb_token_t* agb_syntax_is(register agb_context_t* c, int type) {
	return agb_syntax_is_next(c, type, 0);
}

agb_token_t* agb_syntax_can(register agb_context_t* c, int type) {
	return agb_syntax_can_next(c, type, 0);
}

agb_token_t* agb_syntax_is_symbol(register agb_context_t* c, wchar_t type) {
	return agb_syntax_is_symbol_next(c, type, 0);
}

agb_token_t* agb_syntax_can_symbol(register agb_context_t* c, wchar_t type) {
	return agb_syntax_can_symbol_next(c, type, 0);
}

agb_token_t* agb_syntax_is_next(register agb_context_t* c, int type, int next) {
	if (c->index < c->size && c->tokens[c->index]->type == type) {
		return c->tokens[c->index];
	} else {
		return 0;
	}
}

agb_token_t* agb_syntax_can_next(register agb_context_t* c, int type, int next) {
	if (c->index < c->size && c->tokens[c->index]->type == type) {
		return c->tokens[c->index++];
	} else {
		return 0;
	}
}

agb_token_t* agb_syntax_is_symbol_next(register agb_context_t* c, wchar_t type, int next) {
	if (c->index < c->size && c->tokens[c->index]->text[0] == type) {
		return c->tokens[c->index];
	} else {
		return 0;
	}
}

agb_token_t* agb_syntax_can_symbol_next(register agb_context_t* c, wchar_t type, int next) {
	if (c->index < c->size && c->tokens[c->index]->text[0] == type) {
		return c->tokens[c->index++];
	} else {
		return 0;
	}
}
