#include "agb.h"
#include "agbi.h"
#include "ctype.h"

#define olen(O) ((long*)(O)->native)[0]
#define ohash(O) ((long*)(O)->native)[1]
#define odata(O) ((wchar_t*)(((long*)(O)->native)+2))

void agb_library_string_free(agb_object_t* object) {
	agb_memory_free(object->native);
}

agb_object_t* agb_library_string_new(agb_thread_t* thread, long len, long hash, wchar_t* data) {
	agb_object_t* object = agb_object_new(0);
	if (!object) {
		return 0;
	}
	if (len < 0) {
		if (!data) {
			return 0;
		}
		len = wcslen(data);
	}
	long* header = agb_memory_alloc(2 * sizeof(long) + (len + 1) * sizeof(wchar_t));
	if (!header) {
		agb_object_free(object);
		return 0;
	}
	header[0] = len;
	header[1] = hash;
	if (len > 0 && data) {
		wcsncpy((wchar_t*) (header + 2), data, len);
	}
	object->classid = AGB_LIBRARY_STRING_ID;
	object->native = header;
	if (thread) {
		if (!agb_thread_malloc(thread, object)) {
			agb_object_free(object);
			return 0;
		}
	}
	return object;
}

long agb_library_string_length(agb_object_t* object) {
	if (!object) {
		return 0;
	}
	long len = olen(object);
	if (len == -1) {
		len = wcslen(odata(object));
		olen(object) = len;
	}
	return len;
}

agb_object_t* agb_library_string_from_number(agb_thread_t* thread, double value) {
	wchar_t num2str[64];
	long len;
	if (value == (long) value) {
		len = swprintf(num2str, 64, L"%d", (long) value);
	} else {
		len = swprintf(num2str, 64, L"%lf", value);
		wchar_t* aux = num2str + len - 1;
		while (*aux == L'0') {
			aux--;
		}
		aux[1] = 0;
	}
	if (len < 0) {
		return 0;
	}
	num2str[len] = 0;
	return agb_library_string_new(thread, wcslen(num2str), -1, num2str);
}

agb_object_t* agb_library_string_concat(agb_thread_t* thread, agb_object_t* left, agb_object_t* right) {
	if (!left) {
		return right;
	} else if (!right) {
		return left;
	}
	long leftlen = agb_library_string_length(left);
	long rightlen = agb_library_string_length(right);
	wchar_t* leftdata = odata(left);
	wchar_t* rightdata = odata(right);
	long len = leftlen + rightlen;
	agb_object_t* object = agb_library_string_new(thread, len, -1, 0);
	if (!object) {
		return 0;
	}
	wcsncpy(odata(object), leftdata, leftlen);
	wcsncpy(odata(object) + leftlen, rightdata, rightlen);
	return object;
}

int agb_library_string_equal(agb_object_t* left, agb_object_t* right) {
	if (!left || !right) {
		return left == right;
	}
	if (olen(left) != olen(right)) {
		return 0;
	}
	if (odata(left)[0] != odata(right)[0]) {
		return 0;
	}
	wchar_t* wleft = odata(left);
	wchar_t* wright = odata(right);
	return !wcscmp(wleft, wright);
}

int agb_library_string_compare(agb_object_t* left, agb_object_t* right) {
	if (!left || !right) {
		return left == right;
	}
	if (olen(left) != olen(right)) {
		return olen(left) - olen(right);
	}
	if (odata(left)[0] != odata(right)[0]) {
		return odata(left)[0] - odata(right)[0];
	}
	return wcscmp(odata(left), odata(right));
}

agb_object_t* agb_library_string_substring(agb_thread_t* thread, agb_object_t* left, long begin, long end) {
	if (!left) {
		return 0;
	}
	if (end < 1) {
		return agb_library_string_new(thread, 0, -1, L"");
	}
	if (begin < 1) {
		begin = 1;
	}
	long leftlen = agb_library_string_length(left);
	if (begin > leftlen) {
		return agb_library_string_new(thread, 0, -1, L"");
	}
	if (end > leftlen) {
		end = leftlen;
	}
	long len = end - begin + 1;
	if (len < 0) {
		return agb_library_string_new(thread, 0, -1, L"");
	}
	wchar_t* leftdata = odata(left);
	return agb_library_string_new(thread, len, -1, leftdata + begin - 1);
}

wchar_t* agb_library_string_chars(agb_object_t* object) {
	if (!object) {
		return 0;
	}
	return odata(object);
}

agb_object_t* agb_library_string_code_to_char(agb_thread_t* thread, long code) {
	if (code < 0 || code > WCHAR_MAX) {
		return 0;
	}
	wchar_t c = (wchar_t) code;
	return agb_library_string_new(thread, 1, -1, &c);
}

long agb_library_string_char_to_code(agb_object_t* left) {
	if (!left) {
		return 0;
	}
	wchar_t* text = agb_library_string_chars(left);
	return (long) text[0];
}

agb_object_t* agb_library_string_trim(agb_thread_t* thread, agb_object_t* left) {
	if (!left) {
		return 0;
	}
	wchar_t* text = agb_library_string_chars(left);
	long len = agb_library_string_length(left);
	if (len == 0 || (text[0] > ' ' && text[len - 1] > ' ')) {
		return left;
	}
	while (len > 0 && text[len - 1] <= ' ') {
		len--;
	}
	if (len > 0) {
		wchar_t c = *text;
		while (c != 0 && c <= L' ') {
			text++;
			len--;
			c = *text;
		}
	}
	return agb_library_string_new(thread, len, -1, text);
}

wchar_t agb_library_string_charat(agb_object_t* left, long at) {
	if (!left) {
		return -1;
	}
	if (at < 1) {
		return -1;
	}
	long len = agb_library_string_length(left);
	if (len == 0) {
		return -1;
	}
	if (at > len) {
		return -1;
	}
	return agb_library_string_chars(left)[at - 1];
}

int agb_library_string_startwith(agb_object_t* left, agb_object_t* text) {
	if (!left || !text) {
		return 0;
	}
	long leftLen = agb_library_string_length(left);
	long textLen = agb_library_string_length(text);
	wchar_t* leftChars = agb_library_string_chars(left);
	wchar_t* textChars = agb_library_string_chars(text);
	if (textLen > leftLen) {
		return 0;
	}
	return !wcsncmp(leftChars, textChars, textLen);
}

int agb_library_string_endwith(agb_object_t* left, agb_object_t* text) {
	if (!left || !text) {
		return 0;
	}
	long leftLen = agb_library_string_length(left);
	long textLen = agb_library_string_length(text);
	wchar_t* leftChars = agb_library_string_chars(left);
	wchar_t* textChars = agb_library_string_chars(text);
	if (textLen > leftLen) {
		return 0;
	}
	return !wcsncmp(leftChars + (leftLen - textLen), textChars, textLen);
}

long agb_library_string_indexof(agb_object_t* left, agb_object_t* text, long index) {
	if (!left || !text) {
		return -1;
	}
	if (index < 1) {
		index = 1;
	}
	long leftLen = agb_library_string_length(left);
	long textLen = agb_library_string_length(text);
	wchar_t* leftChars = agb_library_string_chars(left);
	wchar_t* textChars = agb_library_string_chars(text);
	if (textLen > leftLen || textLen == 0) {
		return -1;
	}
	if (index > leftLen) {
		return -1;
	}
	index--;
	char c = textChars[0];
	leftChars += index;
	wchar_t* auxChars = wcschr(leftChars, c);
	while (auxChars) {
		if (!wcsncmp(auxChars + 1, textChars + 1, textLen - 1)) {
			return auxChars - leftChars + 1 + index;
		}
		if (textLen - (auxChars - leftChars) + 1 >= leftLen) {
			return -1;
		}
		auxChars = wcschr(auxChars + 1, c);
	}
	return -1;
}

long agb_library_string_lastindexof(agb_object_t* left, agb_object_t* text) {
	if (!left || !text) {
		return -1;
	}
	long leftLen = agb_library_string_length(left);
	long textLen = agb_library_string_length(text);
	wchar_t* leftChars = agb_library_string_chars(left);
	wchar_t* textChars = agb_library_string_chars(text);
	if (textLen > leftLen || textLen == 0) {
		return -1;
	}
	wchar_t c = textChars[0];
	wchar_t* auxChars = leftChars + leftLen - 1;
	while (leftLen--) {
		if (*auxChars == c) {
			if (!wcsncmp(auxChars + 1, textChars + 1, textLen - 1)) {
				return auxChars - leftChars + 1;
			}
		}
		auxChars--;
	}
	return -1;
}

agb_object_t* agb_library_string_replace(agb_thread_t* thread, agb_object_t* left, agb_object_t* lastvalue, agb_object_t* newvalue) {
	if (!left || !lastvalue || !newvalue) {
		return 0;
	}
	return 0;
}

agb_object_t* agb_library_string_tolowercase(agb_thread_t* thread, agb_object_t* left) {
	if (!left) {
		return 0;
	}
	long leftLen = agb_library_string_length(left);
	wchar_t* leftChars = agb_library_string_chars(left);
	wchar_t* chars = agb_memory_alloc((leftLen + 1) * sizeof(wchar_t));
	long auxLen = leftLen;
	wchar_t* aux = chars;
	while (auxLen--) {
		*aux++ = tolower(*leftChars++);
	}
	agb_object_t* result = agb_library_string_new(thread, leftLen, -1, chars);
	agb_memory_free(chars);
	return result;
}

agb_object_t* agb_library_string_touppercase(agb_thread_t* thread, agb_object_t* left) {
	if (!left) {
		return 0;
	}
	long leftLen = agb_library_string_length(left);
	wchar_t* leftChars = agb_library_string_chars(left);
	wchar_t* chars = agb_memory_alloc((leftLen + 1) * sizeof(wchar_t));
	long auxLen = leftLen;
	wchar_t* aux = chars;
	while (auxLen--) {
		*aux++ = toupper(*leftChars++);
	}
	agb_object_t* result = agb_library_string_new(thread, leftLen, -1, chars);
	agb_memory_free(chars);
	return result;
}

agb_object_t* agb_library_string_base64_encode(agb_thread_t* thread, agb_object_t* left) {
	long len = left ? agb_library_string_length(left) : 0;
	wchar_t* chars = left ? agb_library_string_chars(left) : L"";
	size_t size;
	// TODO : Resolver o cast
	wchar_t* bytes = agb_base64_encode(chars, len, &size);
	if (!bytes) {
		return 0;
	}
	agb_object_t* result = agb_library_string_new(thread, size, -1, bytes);
	agb_memory_free(bytes);
	return result;
}

agb_object_t* agb_library_string_base64_decode(agb_thread_t* thread, agb_object_t* left) {
	long len = left ? agb_library_string_length(left) : 0;
	wchar_t* chars = left ? agb_library_string_chars(left) : L"";
	size_t size;
	// TODO : Resolver o cast
	wchar_t* decoded = agb_base64_decode(chars, len, &size);
	if (!decoded) {
		return 0;
	}
	agb_object_t* result = agb_library_string_new(thread, size, -1, decoded);
	agb_memory_free(decoded);
	return result;
}

agb_object_t* agb_library_string_utf_encode(agb_thread_t* thread, agb_object_t* left) {
	unsigned char* bytes = left ? agb_library_bytes_to_uchars(left) : (unsigned char*) "";
	long byteSize = left ? agb_library_bytes_size(left) : 0l;
	agb_object_t* object = agb_library_string_new(thread, byteSize, -1, 0);
	if (!object) {
		return 0;
	}
	wchar_t* wchars = odata(object);
	agb_wchar_alloced(bytes, byteSize, wchars, &olen(object));
	return object;
}

agb_object_t* agb_library_string_utf_decode(agb_thread_t* thread, agb_object_t* left) {
	wchar_t* wchars = left ? agb_library_string_chars(left) : L"";
	long wcharsSize = left ? agb_library_string_length(left) : 0l;
	long byteSize;
	unsigned char* bytes = agb_wchar_decode(wchars, wcharsSize, &byteSize);
	if (!bytes) {
		return 0;
	}
	return agb_library_bytes_new(thread, bytes, byteSize);
}
