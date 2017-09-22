#include "agb.h"

wchar_t** agb_lexical(wchar_t* text, long len, long* psize) {
	if (!text) {
		return 0;
	}
	wchar_t** data = 0;
	wchar_t* token = (wchar_t*) agb_memory_alloc(len * sizeof(wchar_t));
	if (!token) {
		return 0;
	}
	wchar_t** tokens = (wchar_t**) agb_memory_alloc(len * sizeof(wchar_t*));
	if (!tokens) {
		agb_memory_free(token);
		return 0;
	}
	wchar_t* aux = text;
	int n, i = 0, size = 0;
	for (;;) {
		wchar_t c = *aux++;
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
			do {
				token[i++] = c;
				c = *aux++;
			} while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c == '$');
			{
				wchar_t* tmp = (wchar_t*) agb_memory_alloc((i + 1) * sizeof(wchar_t));
				if (!tmp) {
					goto end;
				}
				wcsncpy(tmp, token, i);
				tmp[i] = 0;
				tokens[size++] = tmp;
			}
			aux--;
			i = 0;
		} else if (c >= '0' && c <= '9') {
			int dot = 0;
			do {
				if ((!dot && c == '.') || c != '.') {
					token[i++] = c;
				}
				if (c == '.') {
					dot = 1;
				}
				c = *aux++;
			} while ((c >= '0' && c <= '9') || c == '.');
			{
				wchar_t* tmp = (wchar_t*) agb_memory_alloc((i + 1) * sizeof(wchar_t));
				if (!tmp) {
					goto end;
				}
				wcsncpy(tmp, token, i);
				tmp[i] = 0;
				tokens[size++] = tmp;
			}
			aux--;
			i = 0;
		} else if (c == '\"') {
			do {
				token[i++] = c;
				c = *aux++;
			} while (c != '\"' && c != 0);
			if (c == 0) {
				c = '\"';
				aux--;
			}
			token[i++] = c;
			{
				wchar_t* tmp = (wchar_t*) agb_memory_alloc((i + 1) * sizeof(wchar_t));
				if (!tmp) {
					goto end;
				}
				wcsncpy(tmp, token, i);
				tmp[i] = 0;
				tokens[size++] = tmp;
			}
			i = 0;
		} else if (c >= 1 && c <= ' ') {
		} else if (c == 0) {
			if (i > 0) {
				wchar_t* tmp = (wchar_t*) agb_memory_alloc((i + 1) * sizeof(wchar_t));
				if (!tmp) {
					goto end;
				}
				wcsncpy(tmp, token, i);
				tmp[i] = 0;
				tokens[size++] = tmp;
			}
			break;
		} else if (c == '/' && *aux == '/') {
			do {
				aux++;
			} while (*aux != '\n' && *aux != 0);
		} else if (c == '/' && *aux == '*') {
			do {
				aux++;
			} while (aux[0] != 0 && aux[0] != '*' && aux[1] != '/');
			if (*aux != 0) {
				aux += 2;
			}
		} else {
			if (i > 0) {
				wchar_t* tmp = (wchar_t*) agb_memory_alloc((i + 1) * sizeof(wchar_t));
				if (!tmp) {
					goto end;
				}
				wcsncpy(tmp, token, i);
				tmp[i] = 0;
				tokens[size++] = tmp;
			}
			{
				wchar_t* tmp = (wchar_t*) agb_memory_alloc(2 * sizeof(wchar_t));
				if (!tmp) {
					goto end;
				}
				tmp[0] = c;
				tmp[1] = 0;
				tokens[size++] = tmp;
			}
			i = 0;
		}
	}
	data = (wchar_t**) agb_memory_alloc((size + 1) * sizeof(wchar_t*));
	if (!data) {
		goto end;
	}
	for (n = 0; n < size; n++) {
		data[n] = tokens[n];
	}
	data[size] = 0;
	if (psize) {
		*psize = size;
	}
	agb_memory_free(token);
	agb_memory_free(tokens);
	return data;
	end: {
		for (n = 0; n < size; n++) {
			agb_memory_free(tokens[n]);
		}
		if (data) {
			agb_memory_free(data);
		}
		agb_memory_free(token);
		agb_memory_free(tokens);
		return 0;
	}
}

void agb_lexical_free(wchar_t** words, long size) {
	long n;
	for (n = 0; n < size; n++) {
		agb_memory_free(words[n]);
	}
	agb_memory_free(words);
}
