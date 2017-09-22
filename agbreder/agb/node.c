#include "agb.h"

void agb_node_free(agb_node_t* node) {
	if (!node) {
		return;
	}
	switch (node->type) {
	case AGB_CLASS_NODE: {
		break;
	}
	case AGB_TYPE_NUM_NODE: {
		agb_type_t* type = (agb_type_t*) node;
		agb_token_free(type->token);
		break;
	}
	case AGB_TYPE_BOOL_NODE: {
		agb_type_t* type = (agb_type_t*) node;
		agb_token_free(type->token);
		break;
	}
	case AGB_TYPE_STR_NODE: {
		agb_type_t* type = (agb_type_t*) node;
		agb_token_free(type->token);
		break;
	}
	case AGB_TYPE_OBJ_NODE: {
		agb_type_t* type = (agb_type_t*) node;
		agb_token_free(type->token);
		break;
	}
	}
	agb_memory_free(node);
}
