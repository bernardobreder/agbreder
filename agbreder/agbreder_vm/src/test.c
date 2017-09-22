#include "agb.h"
#include "agbi.h"

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
		assert_null(agb_file_read(0, 0));
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

static void test$agb_wchar_encode() {
	{
		assert_notnull(agb_wchar_encode(0, 0, 0));
	}
	{
		u_char bytes[] = { 1, 2, 0xC0 + 31, 0x80 + 63, 0xE0 + 15, 0x80 + 63, 0x80 + 62 };
		long n, wcharSize, byteSize = 7;
		wchar_t* data = agb_wchar_encode(bytes, byteSize, &wcharSize);
		assert_notnull(data);
		assert_long(4, wcharSize);
		assert_wchar(1, data[0]);
		assert_wchar(2, data[1]);
		assert_wchar(0x7FF, data[2]);
		assert_wchar(0xFFFE, data[3]);
		assert_wchar(0, data[4]);
		u_char* other = agb_wchar_decode(data, wcharSize, 0);
		assert_long(7, byteSize);
		assert_notnull(other);
		for (n = 0; n < byteSize; n++) {
			assert_uchar(bytes[n], other[n]);
		}
	}
	{
		u_char bytes[] = { 1, 2, 0xC0 + 31, 0x80 + 63, 0xE0 + 15, 0x80 + 63, 0x80 + 62 };
		long n, size = 7;
		for (n = 0; n < INT32_MAX; n++) {
			agb_memory_set_size(n);
			wchar_t* data = agb_wchar_encode(bytes, size, &size);
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

void agb_test() {
	test("file.input", test$agb_file_read());
	test("utf8", test$agb_wchar_encode());
}
