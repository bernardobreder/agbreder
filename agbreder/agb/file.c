#include "agb.h"

u_char* agb_file_read(const char* filename, long* size) {
	FILE* f = fopen(filename, "r");
	if (!f) {
		return 0;
	}
	fseek(f, 0, SEEK_END);
	long len = ftell(f);
	fseek(f, 0, SEEK_SET);
	unsigned char* data = (unsigned char*) agb_memory_alloc((len + 1) * sizeof(u_char));
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
