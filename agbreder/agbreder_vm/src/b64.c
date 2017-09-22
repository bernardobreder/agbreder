#include "agb.h"
#include "agbi.h"

static char encoding_table[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
static char* decoding_table = 0;

wchar_t* agb_base64_encode(wchar_t* data, size_t input_length, size_t* output_length) {
	static int mod_table[] = { 0, 2, 1 };
	int size = (size_t) (4.0 * ceil((double) input_length / 3.0));
	wchar_t* result = agb_memory_alloc((size + 1) * sizeof(wchar_t));
	if (result == 0)
		return 0;
	int i, j;
	for (i = 0, j = 0; i < input_length;) {
		uint32_t octet_a = i < input_length ? data[i++] : 0;
		uint32_t octet_b = i < input_length ? data[i++] : 0;
		uint32_t octet_c = i < input_length ? data[i++] : 0;
		uint32_t triple = (octet_a << 0x10) + (octet_b << 0x08) + octet_c;
		result[j++] = encoding_table[(triple >> 3 * 6) & 0x3F];
		result[j++] = encoding_table[(triple >> 2 * 6) & 0x3F];
		result[j++] = encoding_table[(triple >> 1 * 6) & 0x3F];
		result[j++] = encoding_table[(triple >> 0 * 6) & 0x3F];
	}
	for (i = 0; i < mod_table[input_length % 3]; i++) {
		result[size - 1 - i] = '=';
	}
	result[size] = 0;
	if (output_length) {
		*output_length = size;
	}
	return (wchar_t*) result;
}

wchar_t* agb_base64_decode(wchar_t *data, size_t input_length, size_t *output_length) {
	int i, j, size;
	size = input_length / 4 * 3;
	if (data[input_length - 1] == '=')
		size--;
	if (data[input_length - 2] == '=')
		size--;
	wchar_t* result = agb_memory_alloc((size + 1) * sizeof(wchar_t));
	if (result == 0) {
		return 0;
	}
	if (input_length % 4 != 0) {
		result[0] = 0;
		return result;
	}
	if (!decoding_table) {
		decoding_table = agb_memory_alloc(256);
		for (i = 0; i < 0x40; i++) {
			decoding_table[(int) encoding_table[i]] = i;
		}
	}
	for (i = 0, j = 0; i < input_length;) {
		uint32_t a = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t b = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t c = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t d = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t all = (a << 3 * 6) + (b << 2 * 6) + (c << 1 * 6) + (d << 0 * 6);
		if (j < size)
			result[j++] = (all >> 2 * 8) & 0xFF;
		if (j < size)
			result[j++] = (all >> 1 * 8) & 0xFF;
		if (j < size)
			result[j++] = (all >> 0 * 8) & 0xFF;
	}
	result[size] = 0;
	if (output_length) {
		*output_length = size;
	}
	return result;
}

unsigned char* agb_base64_decode_chars(unsigned char* data, size_t input_length, size_t *output_length) {
	int i, j, size;
	size = input_length / 4 * 3;
	if (data[input_length - 1] == '=')
		size--;
	if (data[input_length - 2] == '=')
		size--;
	unsigned char* result = agb_memory_alloc((size + 1) * sizeof(unsigned char));
	if (result == 0) {
		return 0;
	}
	if (input_length % 4 != 0) {
		result[0] = 0;
		return result;
	}
	if (!decoding_table) {
		decoding_table = agb_memory_alloc(256);
		for (i = 0; i < 0x40; i++) {
			decoding_table[(int) encoding_table[i]] = i;
		}
	}
	for (i = 0, j = 0; i < input_length;) {
		uint32_t a = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t b = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t c = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t d = data[i] == '=' ? 0 & i++ : decoding_table[(int) data[i++]];
		uint32_t all = (a << 3 * 6) + (b << 2 * 6) + (c << 1 * 6) + (d << 0 * 6);
		if (j < size)
			result[j++] = (all >> 2 * 8) & 0xFF;
		if (j < size)
			result[j++] = (all >> 1 * 8) & 0xFF;
		if (j < size)
			result[j++] = (all >> 0 * 8) & 0xFF;
	}
	result[size] = 0;
	if (output_length) {
		*output_length = size;
	}
	return result;
}
