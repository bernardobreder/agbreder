#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>
#include <stdarg.h>
#include <stdlib.h>
#include <stdio.h>
#include <wchar.h>
#include <locale.h>
#include <unistd.h>
#include <sys/types.h>
#include <assert.h>

typedef struct {
	wchar_t* text;
	long size;
	u_char type;
} agb_token_t;

typedef struct {
	u_int8_t type;
} agb_node_t;

typedef struct {
	u_int16_t type;
	agb_token_t* token;
} agb_type_t;

typedef struct {
	u_int8_t type;
} agb_class_node_t;

typedef struct {
	long size;
	long max;
	void** data;
} agb_list_t;

typedef agb_list_t agb_list_node_t;

// Define

#define AGB_CLASS_NODE 1
#define AGB_TYPE_NUM_NODE 2
#define AGB_TYPE_BOOL_NODE 3
#define AGB_TYPE_STR_NODE 4
#define AGB_TYPE_OBJ_NODE 5
#define AGB_TYPE_FUNC_NODE 6
#define AGB_TYPE_NUMS_NODE 7
#define AGB_TYPE_BOOLS_NODE 8

// Memory

void* agb_memory_alloc(u_int64_t size);

void* agb_memory_realloc(void* old, u_int64_t size);

void agb_memory_set_size(int64_t size);

void agb_memory_free(void* data);

int64_t agb_memory_get_size();

// File Read

u_char* agb_file_read(const char* filename, long* size);

// UTF8

wchar_t* agb_wchar(unsigned char* bytes, long byteSize, long* size);

wchar_t* agb_wchar_dup(wchar_t* value, long size);

// Lexical

wchar_t** agb_lexical(wchar_t* text, long len, long* size);

void agb_lexical_free(wchar_t** words, long size);

// Token

#define AGB_TOKEN_ID 1
#define AGB_TOKEN_NUMBER 2
#define AGB_TOKEN_STRING 3
#define AGB_TOKEN_SYMBOL 4
#define AGB_TOKEN_CLASS 5
#define AGB_TOKEN_EXTENDS 6
#define AGB_TOKEN_THIS 7
#define AGB_TOKEN_DO 8
#define AGB_TOKEN_END 9
#define AGB_TOKEN_NUM 10
#define AGB_TOKEN_STR 11
#define AGB_TOKEN_BOOL 12
#define AGB_TOKEN_OBJ 13
#define AGB_TOKEN_FUNCTION 14

agb_token_t** agb_token(wchar_t** words, int len);

agb_token_t* agb_token_dup(agb_token_t* token);

void agb_tokens_free(agb_token_t** tokens, int size);

void agb_token_free(agb_token_t* token);

// Syntax

agb_list_node_t* agb_syntax(agb_token_t** tokens, long size);

// Array

agb_list_t* agb_list_new();

int agb_list_add(agb_list_t* list, void* data);

void* agb_list_get(agb_list_t* list, u_int32_t index);

void* agb_list_set(agb_list_t* list, u_int32_t index, void* data);

u_int32_t agb_list_size(agb_list_t* list);

void agb_list_free(agb_list_t* list);

void agb_list_free_func(agb_list_t* list, void (*func)(void*));

void agb_test();

// Node

void agb_node_free(agb_node_t* node);
