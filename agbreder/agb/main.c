#include "agb.h"

int main() {
	setlocale(LC_ALL, "");
//	long size;
//	unsigned char* bytes = agb_file_read("main.c", &size);
//	wchar_t* content = agb_utf8(bytes, size, &size);
//	free(bytes);
//	wchar_t** words = agb_lexical(content, size, &size);
//	free(content);
//	agb_token_t** tokens = agb_token(words, size);
//	free(words);
//	agb_node_t* nodes = agb_syntax(tokens, size);
	agb_test();
	return 0;
}
