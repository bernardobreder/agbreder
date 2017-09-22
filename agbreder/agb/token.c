#include "agb.h"

static u_int32_t TOKENS_SIZE = 5;
static wchar_t* TOKENS[] = { L"class", L"extends", L"this", L"do", L"end", L"num", L"str", L"bool", L"obj", L"function" };

agb_token_t** agb_token(wchar_t** words, int len) {
	if (!words) {
		return 0;
	}
	agb_token_t** data = (agb_token_t**) agb_memory_alloc(len * sizeof(agb_token_t*));
	if (!data) {
		return 0;
	}
	int n, m;
	wchar_t** aux = words;
	for (n = 0; n < len; n++) {
		wchar_t* word = *aux++;
		wchar_t c = word[0];
		u_int8_t type;
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
			type = 1;
			for (m = 0; m < TOKENS_SIZE; m++) {
				if (!wcscmp(TOKENS[m], word)) {
					type = 5 + m;
				}
			}
		} else if (c >= '0' && c <= '9') {
			type = 2;
		} else if (c == '\"') {
			type = 3;
		} else {
			type = 4;
		}
		agb_token_t* token = (agb_token_t*) agb_memory_alloc(sizeof(agb_token_t));
		if (!token) {
			goto end;
		}
		int size = wcslen(word);
		wchar_t* text = agb_wchar_dup(word, size);
		if (!text) {
			agb_memory_free(token);
			goto end;
		}
		token->size = size;
		token->text = text;
		token->type = type;
		data[n] = token;
	}
	return data;
	end: {
		agb_tokens_free(data, n);
		return 0;
	}
}

agb_token_t* agb_token_dup(agb_token_t* token) {
	agb_token_t* data = (agb_token_t*) agb_memory_alloc(sizeof(agb_token_t));
	if (!data) {
		return 0;
	}
	wchar_t* text = agb_wchar_dup(token->text, token->size);
	if (!text) {
		agb_memory_free(data);
		return 0;
	}
	data->text = text;
	data->size = token->size;
	data->type = token->type;
	return data;
}

void agb_tokens_free(agb_token_t** tokens, int size) {
	long n;
	for (n = 0; n < size; n++) {
		agb_token_free(tokens[n]);
	}
	agb_memory_free(tokens);
}

void agb_token_free(agb_token_t* token) {
	agb_memory_free(token->text);
	agb_memory_free(token);
}
