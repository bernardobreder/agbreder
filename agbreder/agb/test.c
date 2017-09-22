#include "agb.h"

#define test(NAME, FUNC) printf("Testing '%s': ", NAME); FUNC; printf("Ok\n");

static void assert_notnull(void* data) {
	if (!data) {
		printf("expected not null\n");
		exit(-1);
	}
}

static void assert_null(void* data) {
	if (data) {
		printf("expected null\n");
		exit(-1);
	}
}

static void assert_long(long expected, long atual) {
	if (atual != expected) {
		printf("expected '%ld' and not '%ld'\n", expected, atual);
		exit(-1);
	}
}

static void assert_void(void* expected, void* atual) {
	if (atual != expected) {
		printf("expected '%p' and not '%p'\n", expected, atual);
		exit(-1);
	}
}

static void assert_size(size_t expected, size_t atual) {
	if (atual != expected) {
		printf("expected '%ld' and not '%ld'\n", expected, atual);
		exit(-1);
	}
}

static void assert_wchar(wchar_t expected, wchar_t atual) {
	if (atual != expected) {
		fwprintf(stderr, L"expected '%ld' and not '%ld'\n", expected, atual);
		exit(-1);
	}
}

static void assert_pwchar(wchar_t* expected, wchar_t* atual) {
	if (wcscmp(expected, atual)) {
		fwprintf(stderr, L"expected '%ls' and not '%ls'\n", expected, atual);
		exit(-1);
	}
}

static void assert_uchar(unsigned char expected, unsigned char atual) {
	if (atual != expected) {
		printf("expected '%c(%d)' and not '%c(%d)'\n", expected, (int) expected, atual, (int) atual);
		exit(-1);
	}
}

static void test$agb_file_read() {
	{
		agb_memory_set_size(0);
		assert_null(agb_file_read(0, 0));
		assert_size(0, agb_memory_get_size());
		agb_memory_set_size(-1);
	}
	{
		FILE* f = fopen("a.txt", "w");
		fputs(" abc ", f);
		fclose(f);
		long size;
		u_char* data = agb_file_read("a.txt", &size);
		remove("a.txt");
		assert_notnull(data);
		assert_long(5, size);
		assert_uchar(' ', data[0]);
		assert_uchar('a', data[1]);
		assert_uchar('b', data[2]);
		assert_uchar('c', data[3]);
		assert_uchar(' ', data[4]);
		assert_uchar(0, data[5]);
		agb_memory_free(data);
	}
	{
		FILE* f = fopen("a.txt", "w");
		fputs(" abc ", f);
		fclose(f);
		long n, size;
		for (n = 0; n < INT32_MAX; n++) {
			agb_memory_set_size(n);
			u_char* data = agb_file_read("a.txt", &size);
			if (data) {
				agb_memory_free(data);
				assert_size(n, agb_memory_get_size());
				break;
			}
			assert_size(n, agb_memory_get_size());
		}
		remove("a.txt");
		agb_memory_set_size(-1);
	}
}

static void test$agb_wchar() {
	{
		agb_memory_set_size(0);
		assert_null(agb_wchar(0, 0, 0));
		assert_size(0, agb_memory_get_size());
		agb_memory_set_size(-1);
	}
	{
		u_char bytes[] = { 1, 2, 0xC0 + 31, 0x80 + 63, 0xE0 + 15, 0x80 + 63, 0x80 + 62 };
		long size = 7;
		wchar_t* data = agb_wchar(bytes, size, &size);
		assert_notnull(data);
		assert_long(4, size);
		assert_wchar(1, data[0]);
		assert_wchar(2, data[1]);
		assert_wchar(0x7FF, data[2]);
		assert_wchar(0xFFFE, data[3]);
		assert_wchar(0, data[4]);
	}
	{
		u_char bytes[] = { 1, 2, 0xC0 + 31, 0x80 + 63, 0xE0 + 15, 0x80 + 63, 0x80 + 62 };
		long n, size = 7;
		for (n = 0; n < INT32_MAX; n++) {
			agb_memory_set_size(n);
			wchar_t* data = agb_wchar(bytes, size, &size);
			if (data) {
				agb_memory_free(data);
				assert_size(n, agb_memory_get_size());
				break;
			}
			assert_size(n, agb_memory_get_size());
		}
		agb_memory_set_size(-1);
	}
}

static void test$agb_lexical() {
	long size;
	{
		{
			wchar_t** tokens = agb_lexical(0, 0, 0);
			assert_null(tokens);
		}
	}
	{
		{
			wchar_t text[] = L"a";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"a", tokens[0]);
		}
		{
			wchar_t text[] = L"abc";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"abc", tokens[0]);
		}
	}
	{
		{
			wchar_t text[] = L".";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L".", tokens[0]);
		}
		{
			wchar_t text[] = L"a.b.c";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(5, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L".", tokens[1]);
			assert_pwchar(L"b", tokens[2]);
			assert_pwchar(L".", tokens[3]);
			assert_pwchar(L"c", tokens[4]);
		}
		{
			wchar_t text[] = L" a . b . c ";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(5, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L".", tokens[1]);
			assert_pwchar(L"b", tokens[2]);
			assert_pwchar(L".", tokens[3]);
			assert_pwchar(L"c", tokens[4]);
		}
	}
	{
		{
			wchar_t text[] = L"12";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"12", tokens[0]);
		}
		{
			wchar_t text[] = L"12.";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"12.", tokens[0]);
		}
		{
			wchar_t text[] = L"a12b";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(3, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"12", tokens[1]);
			assert_pwchar(L"b", tokens[2]);
		}
		{
			wchar_t text[] = L"a1.2b";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(3, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"1.2", tokens[1]);
			assert_pwchar(L"b", tokens[2]);
		}
		{
			wchar_t text[] = L"a1.2.3b";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(3, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"1.23", tokens[1]);
			assert_pwchar(L"b", tokens[2]);
		}
		{
			wchar_t text[] = L"a1.2.3b";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(3, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"1.23", tokens[1]);
			assert_pwchar(L"b", tokens[2]);
		}
		{
			wchar_t text[] = L"a1.2.,3b";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(5, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"1.2", tokens[1]);
			assert_pwchar(L",", tokens[2]);
			assert_pwchar(L"3", tokens[3]);
			assert_pwchar(L"b", tokens[4]);
		}
		{
			wchar_t text[] = L" a 1.2. , 3 b ";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(5, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"1.2", tokens[1]);
			assert_pwchar(L",", tokens[2]);
			assert_pwchar(L"3", tokens[3]);
			assert_pwchar(L"b", tokens[4]);
		}
		{
			wchar_t text[] = L" a 1.2 . , 3 b ";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(6, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"1.2", tokens[1]);
			assert_pwchar(L".", tokens[2]);
			assert_pwchar(L",", tokens[3]);
			assert_pwchar(L"3", tokens[4]);
			assert_pwchar(L"b", tokens[5]);
		}
	}
	{
		{
			wchar_t text[] = L"\"aa\"";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"\"aa\"", tokens[0]);
		}
		{
			wchar_t text[] = L"\"aa";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"\"aa\"", tokens[0]);
		}
		{
			wchar_t text[] = L".\"aa\",";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(3, size);
			assert_pwchar(L".", tokens[0]);
			assert_pwchar(L"\"aa\"", tokens[1]);
			assert_pwchar(L",", tokens[2]);
		}
	}
	{
		{
			wchar_t text[] = L"1\n2";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(2, size);
			assert_pwchar(L"1", tokens[0]);
			assert_pwchar(L"2", tokens[1]);
		}
		{
			wchar_t text[] = L"1//abc\n2";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(2, size);
			assert_pwchar(L"1", tokens[0]);
			assert_pwchar(L"2", tokens[1]);
		}
		{
			wchar_t text[] = L"1//";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"1", tokens[0]);
		}
		{
			wchar_t text[] = L"//";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(0, size);
		}
	}
	{
		{
			wchar_t text[] = L"/**/";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(0, size);
		}
		{
			wchar_t text[] = L"/*";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(0, size);
		}
		{
			wchar_t text[] = L"a/**/b";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(2, size);
			assert_pwchar(L"a", tokens[0]);
			assert_pwchar(L"b", tokens[1]);
		}
		{
			wchar_t text[] = L"///**/a";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(0, size);
		}
		{
			wchar_t text[] = L"///**/\na";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"a", tokens[0]);
		}
	}
	{
		{
			wchar_t text[] = L"\"ç\"";
			wchar_t** tokens = agb_lexical(text, wcslen(text), &size);
			assert_notnull(tokens);
			assert_long(1, size);
			assert_pwchar(L"\"ç\"", tokens[0]);
		}
	}
	{
		long n, size;
		u_char* bytes = agb_file_read("main.c", &size);
		wchar_t* data = agb_wchar(bytes, size, &size);
		long len = wcslen(data);
		for (n = 0; n < INT32_MAX; n++) {
			agb_memory_set_size(n);
			wchar_t** words = agb_lexical(data, len, &size);
			if (words) {
				agb_lexical_free(words, size);
				assert_size(n, agb_memory_get_size());
				break;
			}
			assert_size(n, agb_memory_get_size());
		}
		agb_memory_set_size(-1);
	}
}

static void test$agb_token() {
	{
		wchar_t* words[] = { };
		agb_token_t** tokens = agb_token(words, 0);
		assert_notnull(tokens);
	}
	{
		wchar_t* words[] = { L"a" };
		agb_token_t** tokens = agb_token(words, 1);
		assert_notnull(tokens);
		assert_pwchar(L"a", tokens[0]->text);
		assert_long(1, tokens[0]->size);
		assert_uchar(1, tokens[0]->type);
	}
	{
		wchar_t* words[] = { L"1" };
		agb_token_t** tokens = agb_token(words, 1);
		assert_notnull(tokens);
		assert_pwchar(L"1", tokens[0]->text);
		assert_long(1, tokens[0]->size);
		assert_uchar(2, tokens[0]->type);
	}
	{
		wchar_t* words[] = { L"\"a\"" };
		agb_token_t** tokens = agb_token(words, 1);
		assert_notnull(tokens);
		assert_pwchar(L"\"a\"", tokens[0]->text);
		assert_long(3, tokens[0]->size);
		assert_uchar(3, tokens[0]->type);
	}
	{
		wchar_t* words[] = { L"." };
		agb_token_t** tokens = agb_token(words, 1);
		assert_notnull(tokens);
		assert_pwchar(L".", tokens[0]->text);
		assert_long(1, tokens[0]->size);
		assert_uchar(4, tokens[0]->type);
	}
	{
		wchar_t* words[] = { L"class", L"extends" };
		agb_token_t** tokens = agb_token(words, 2);
		assert_notnull(tokens);
		assert_pwchar(L"class", tokens[0]->text);
		assert_long(5, tokens[0]->size);
		assert_uchar(AGB_TOKEN_CLASS, tokens[0]->type);
		assert_pwchar(L"extends", tokens[1]->text);
		assert_long(7, tokens[1]->size);
		assert_uchar(AGB_TOKEN_EXTENDS, tokens[1]->type);
	}
	{
		wchar_t* words[] = { L"a", L"12", L"\"abc\"", L"." };
		agb_token_t** tokens = agb_token(words, 4);
		assert_notnull(tokens);
		assert_pwchar(L"a", tokens[0]->text);
		assert_long(1, tokens[0]->size);
		assert_uchar(1, tokens[0]->type);
		assert_pwchar(L"12", tokens[1]->text);
		assert_long(2, tokens[1]->size);
		assert_uchar(2, tokens[1]->type);
		assert_pwchar(L"\"abc\"", tokens[2]->text);
		assert_long(5, tokens[2]->size);
		assert_uchar(3, tokens[2]->type);
		assert_pwchar(L".", tokens[3]->text);
		assert_long(1, tokens[3]->size);
		assert_uchar(4, tokens[3]->type);
	}
	{
		long n, size;
		u_char* bytes = agb_file_read("main.c", &size);
		wchar_t* data = agb_wchar(bytes, size, &size);
		long len = wcslen(data);
		wchar_t** words = agb_lexical(data, len, &size);
		for (n = 0; n < INT32_MAX; n++) {
			agb_memory_set_size(n);
			agb_token_t** tokens = agb_token(words, size);
			if (tokens) {
				agb_tokens_free(tokens, size);
				assert_size(n, agb_memory_get_size());
				break;
			}
			assert_size(n, agb_memory_get_size());
		}
		agb_memory_set_size(-1);
	}
}

static void test$agb_list() {
	{
		agb_list_t * list = agb_list_new();
		assert_notnull(list);
		assert_long(0, agb_list_size(list));
		assert_null(agb_list_get(list, 0));
		assert_null(agb_list_get(list, 1));
		assert_null(agb_list_set(list, 0, list));
	}
	{
		agb_list_t * list = agb_list_new();
		agb_list_add(list, list);
		assert_long(1, agb_list_size(list));
		assert_void(list, agb_list_get(list, 0));
		assert_null(agb_list_get(list, 1));
	}
	{
		agb_list_t * list = agb_list_new();
		agb_list_add(list, list);
		agb_list_add(list, list);
		agb_list_add(list, list);
		agb_list_add(list, list);
		agb_list_add(list, list);
		assert_long(5, agb_list_size(list));
		assert_void(list, agb_list_get(list, 0));
		assert_void(list, agb_list_get(list, 1));
		assert_void(list, agb_list_get(list, 2));
		assert_void(list, agb_list_get(list, 3));
		assert_void(list, agb_list_get(list, 4));
		assert_null(agb_list_get(list, 5));
	}
	{
		int n;
		agb_list_t * list = agb_list_new();
		for (n = 0; n < 1024; n++) {
			agb_list_add(list, list);
		}
		assert_long(1024, agb_list_size(list));
		for (n = 0; n < 1024; n++) {
			assert_void(list, agb_list_get(list, n));
		}
		assert_null(agb_list_get(list, 1024));
		for (n = 0; n < 1024; n++) {
			assert_void(list, agb_list_set(list, n, &n));
		}
		for (n = 0; n < 1024; n++) {
			assert_void(&n, agb_list_set(list, n, list));
		}
		assert_null(agb_list_get(list, 1024));
	}
	{
		int n;
		agb_list_t * list = agb_list_new();
		for (n = 0; n < 1024; n++) {
			agb_list_add(list, list);
		}
		assert_long(1024, agb_list_size(list));
		agb_list_free(list);
	}
	{
		long n;
		for (n = 0; n < INT32_MAX; n++) {
			agb_memory_set_size(n);
			agb_list_t * list = agb_list_new();
			if (list) {
				agb_list_free(list);
				assert_size(n, agb_memory_get_size());
				break;
			}
			assert_size(n, agb_memory_get_size());
		}
		agb_memory_set_size(-1);
	}
}

static void test$agb_syntax() {
	long n;
	{
		agb_list_node_t* nodes = agb_syntax(0, 0);
		assert_null(nodes);
	}
	{
		wchar_t* words[] = { };
		agb_token_t** tokens = agb_token(words, 0);
		agb_list_node_t* nodes = agb_syntax(tokens, 0);
		assert_notnull(nodes);
		assert_long(0, agb_list_size(nodes));
	}
	{
		wchar_t * words[] = { L"class", L"a", L"do", L"end" };
		agb_token_t** tokens = agb_token(words, 4);
		for (n = 0; n < INT32_MAX; n++) {
			agb_memory_set_size(n);
			agb_list_node_t* nodes = agb_syntax(tokens, 4);
			if (nodes) {
				assert_notnull(nodes);
				assert_long(1, agb_list_size(nodes));
				agb_class_node_t* node = agb_list_get(nodes, 0);
				assert_notnull(node);
				assert_uchar(AGB_CLASS_NODE, node->type);
				agb_list_free_func(nodes, (void (*)(void*)) agb_node_free);
				assert_size(n, agb_memory_get_size());
				break;
			}
			assert_size(n, agb_memory_get_size());
		}
		agb_memory_set_size(-1);
	}
}

void agb_test() {
	test("file.input", test$agb_file_read());
	test("utf8", test$agb_wchar());
	test("lexical", test$agb_lexical());
	test("token", test$agb_token());
	test("list", test$agb_list());
	test("syntax", test$agb_syntax());
}
