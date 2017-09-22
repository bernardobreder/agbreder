#include "agb.h"

#define MASKBYTE 0x80
#define MASK2BYTES 0xC0
#define MASK3BYTES 0xE0
#define MASK4BYTES 0xF0
#define MASK5BYTES 0xF8
#define MASK6BYTES 0xFC

wchar_t* agb_wchar(unsigned char* bytes, long byteSize, long* size) {
	if (!bytes) {
		return 0;
	}
	wchar_t* data = (wchar_t*) agb_memory_alloc((byteSize + 1) * sizeof(wchar_t));
	if (!data) {
		return 0;
	}
	long n = 0, i = 0;
	unsigned char c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0;
	for (; n < byteSize;) {
		c1 = bytes[n++];
		if ((c1 & MASK6BYTES) == MASK6BYTES) {
			if (n >= byteSize) {
				goto end;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c3 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c4 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c5 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c6 = bytes[n++];
			data[i++] = ((c1 - MASK4BYTES) << 30) + ((c2 - MASKBYTE) << 24) + ((c3 - MASKBYTE) << 18) + ((c4 - MASKBYTE) << 12) + ((c5 - MASKBYTE) << 6) + (c6 - MASKBYTE);
		} else if ((c1 & MASK5BYTES) == MASK5BYTES) {
			if (n >= byteSize) {
				goto end;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c3 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c4 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c5 = bytes[n++];
			data[i++] = ((c1 - MASK4BYTES) << 24) + ((c2 - MASKBYTE) << 18) + ((c3 - MASKBYTE) << 12) + ((c4 - MASKBYTE) << 6) + (c5 - MASKBYTE);
		} else if ((c1 & MASK4BYTES) == MASK4BYTES) {
			if (n >= byteSize) {
				goto end;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c3 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c4 = bytes[n++];
			data[i++] = ((c1 - MASK4BYTES) << 18) + ((c2 - MASKBYTE) << 12) + ((c3 - MASKBYTE) << 6) + (c4 - MASKBYTE);
		} else if ((c1 & MASK3BYTES) == MASK3BYTES) {
			if (n >= byteSize) {
				goto end;
			}
			c2 = bytes[n++];
			if (n >= byteSize) {
				goto end;
			}
			c3 = bytes[n++];
			data[i++] = ((c1 - MASK3BYTES) << 12) + ((c2 - MASKBYTE) << 6) + (c3 - MASKBYTE);
		} else if ((c1 & MASK2BYTES) == MASK2BYTES) {
			if (n >= byteSize) {
				goto end;
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
	return data;
	end: {
		agb_memory_free(data);
		return 0;
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
