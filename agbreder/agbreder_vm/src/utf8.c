#include "agb.h"
#include "agbi.h"

#define MASKBYTE 0x80
#define MASKBYTE_ONE 0x7F
#define MASK2BYTES 0xC0
#define MASK3BYTES 0xE0
#define MASK4BYTES 0xF0
#define MASK5BYTES 0xF8
#define MASK6BYTES 0xFC

bool agb_wchar_alloced(unsigned char* bytes, long byteSize, wchar_t* data, long* size) {
	if (!bytes || !data) {
		return 0;
	}
	long n = 0, i = 0;
	unsigned char c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0;
	for (; n < byteSize;) {
		c1 = bytes[n++];
		if ((c1 & MASK6BYTES) == MASK6BYTES) {
			if (n >= byteSize) {
				return 0;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c3 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c4 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c5 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c6 = bytes[n++];
			data[i++] = ((c1 - MASK4BYTES) << 30) + ((c2 - MASKBYTE) << 24) + ((c3 - MASKBYTE) << 18) + ((c4 - MASKBYTE) << 12) + ((c5 - MASKBYTE) << 6) + (c6 - MASKBYTE);
		} else if ((c1 & MASK5BYTES) == MASK5BYTES) {
			if (n >= byteSize) {
				return 0;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c3 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c4 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c5 = bytes[n++];
			data[i++] = ((c1 - MASK4BYTES) << 24) + ((c2 - MASKBYTE) << 18) + ((c3 - MASKBYTE) << 12) + ((c4 - MASKBYTE) << 6) + (c5 - MASKBYTE);
		} else if ((c1 & MASK4BYTES) == MASK4BYTES) {
			if (n >= byteSize) {
				return 0;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c3 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c4 = bytes[n++];
			data[i++] = ((c1 - MASK4BYTES) << 18) + ((c2 - MASKBYTE) << 12) + ((c3 - MASKBYTE) << 6) + (c4 - MASKBYTE);
		} else if ((c1 & MASK3BYTES) == MASK3BYTES) {
			if (n >= byteSize) {
				return 0;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				return 0;
			}
			c3 = bytes[n++];
			data[i++] = ((c1 - MASK3BYTES) << 12) + ((c2 - MASKBYTE) << 6) + (c3 - MASKBYTE);
		} else if ((c1 & MASK2BYTES) == MASK2BYTES) {
			if (n >= byteSize) {
				return 0;
			}
			c2 = bytes[n++];
			data[i++] = ((c1 - MASK2BYTES) << 6) + (c2 - MASKBYTE);
		} else {
			data[i++] = c1;
		}
	}
	data[i] = 0;
	if (size) {
		*size = i;
	}
	return 1;
}

unsigned char* agb_wchar_decode(wchar_t* wchars, long wcharsSize, long* byteSize) {
	unsigned char* data = (unsigned char*) agb_memory_alloc((wcharsSize * 6 + 1) * sizeof(unsigned char));
	if (!data) {
		return 0;
	}
	unsigned char* aux = data;
	long n = 0;
	for (n = 0; n < wcharsSize; n++) {
		wchar_t wc = wchars[n];
		if (wc <= 0x7F) {
			*aux++ = wc;
		} else if (wc <= 0x7FF) {
			*aux++ = (wc >> 6) + MASK2BYTES;
			*aux++ = (wc & 0x3F) + MASKBYTE;
		} else if (wc <= 0xFFFF) {
			*aux++ = (wc >> 12) + MASK3BYTES;
			*aux++ = ((wc >> 6) & 0x3F) + MASKBYTE;
			*aux++ = (wc & 0x3F) + MASKBYTE;
		} else if (wc <= 0x1FFFFF) {
			*aux++ = ((wc >> 18)) + MASK4BYTES;
			*aux++ = (((wc >> 12) & 0x3F)) + MASKBYTE;
			*aux++ = (((wc >> 6) & 0x3F)) + MASKBYTE;
			*aux++ = ((wc & 0x3F)) + MASKBYTE;
		} else if (wc <= 0x3FFFFFF) {
			*aux++ = ((wc >> 24)) + MASK5BYTES;
			*aux++ = (((wc >> 18) & 0x3F)) + MASKBYTE;
			*aux++ = (((wc >> 12) & 0x3F)) + MASKBYTE;
			*aux++ = (((wc >> 6) & 0x3F)) + MASKBYTE;
			*aux++ = ((wc & 0x3F)) + MASKBYTE;
		} else if (wc <= 0x7FFFFFFF) {
			*aux++ = ((wc >> 30)) + MASK6BYTES;
			*aux++ = (((wc >> 24) & 0x3F)) + MASKBYTE;
			*aux++ = (((wc >> 18) & 0x3F)) + MASKBYTE;
			*aux++ = (((wc >> 12) & 0x3F)) + MASKBYTE;
			*aux++ = (((wc >> 6) & 0x3F)) + MASKBYTE;
			*aux++ = ((wc & 0x3F)) + MASKBYTE;
		}
	}
	*aux = 0;
	long size = aux - data;
	unsigned char* value = (unsigned char*) agb_memory_realloc(data, (size + 1) * sizeof(unsigned char));
	if (!value) {
		agb_memory_free(data);
		return 0;
	}
	if (byteSize) {
		*byteSize = size;
	}
	return value;
}

wchar_t* agb_wchar_encode(unsigned char* bytes, long byteSize, long* size) {
	if (!bytes) {
		bytes = (unsigned char*) "";
		byteSize = 0;
	}
	wchar_t* data = (wchar_t*) agb_memory_alloc((byteSize + 1) * sizeof(wchar_t));
	if (!data) {
		return 0;
	}
	if (!agb_wchar_alloced(bytes, byteSize, data, size)) {
		agb_memory_free(data);
		return 0;
	} else {
		return data;
	}
}

wchar_t* agb_wchar_dup(wchar_t* value, long size) {
	wchar_t* data = (wchar_t*) agb_memory_alloc((size + 1) * sizeof(wchar_t));
	if (!data) {
		return 0;
	}
	wcscpy(data, value);
	return data;
}

char* agb_wchar_to_chars(wchar_t* text) {
	int n, size = wcslen(text);
	char* data = agb_memory_alloc((size + 1) * sizeof(char));
	char* aux = data;
	for (n = 0; n < size; n++) {
		*aux++ = *text++;
	}
	data[size] = 0;
	return data;
}

void agb_wchar_copy_to_chars(wchar_t* text, char* value) {
	int n, size = wcslen(text);
	char* aux = value;
	for (n = 0; n < size; n++) {
		*aux++ = *text++;
	}
	*aux++ = 0;
}
