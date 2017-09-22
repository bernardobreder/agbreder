#include "agb.h"
#include "agbi.h"

unsigned char* agb_file_read(const char* filename, long* size) {
	if (!filename) {
		return 0;
	}
	FILE* f = fopen(filename, "r");
	if (!f) {
		return 0;
	}
	fseek(f, 0, SEEK_END);
	long len = ftell(f);
	fseek(f, 0, SEEK_SET);
	unsigned char* data = (unsigned char*) agb_memory_alloc((len + 1) * sizeof(unsigned char));
	if (!data) {
		fclose(f);
		return 0;
	}
	unsigned char* aux = data;
	long n;
	for (n = 0; n < len; n++) {
		int c = fgetc(f);
		if (c < 0) {
			agb_memory_free(data);
			fclose(f);
			return 0;
		} else {
			*aux++ = c;
		}
	}
	*aux = 0;
	data[n] = 0;
	if (size) {
		*size = n;
	}
	fclose(f);
	return data;
}

void* agb_file_open(const char* file) {
	return fopen(file, "rb");
}

void agb_file_close(void* file) {
	fclose(file);
}

int agb_bytecode_read_int1(unsigned char** file) {
	int c = **file;
	*file += 1;
	return c;
}

unsigned char agb_bytecode_read_byte(unsigned char** file) {
	return agb_bytecode_read_int1(file);
}

int agb_bytecode_read_int(unsigned char** file) {
	int c1 = agb_bytecode_read_int1(file);
	int c2 = agb_bytecode_read_int1(file);
	int c3 = agb_bytecode_read_int1(file);
	int c4 = agb_bytecode_read_int1(file);
	if (c1 < 0 || c2 < 0 || c3 < 0 || c4 < 0) {
		return -2147483647;
	}
	return (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
}

short agb_bytecode_read_short(unsigned char** file) {
	int c1 = agb_bytecode_read_int1(file);
	int c2 = agb_bytecode_read_int1(file);
	if (c1 < 0 || c2 < 0) {
		return -1;
	}
	return (c1 << 8) + c2;
}

wchar_t* agb_bytecode_read_utf(unsigned char** file) {
	short len = agb_bytecode_read_short(file);
	if (len < 0) {
		return 0;
	}
	long size;
	wchar_t* result = agb_wchar_encode(*file, len, &size);
	*file += len;
	return result;
}
