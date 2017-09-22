#ifndef AGB_STRING
#define AGB_STRING

#include "agb.h"

#ifdef __cplusplus
extern "C" {
#endif

agb_object_t* agb_library_string_new(agb_thread_t* thread, long len, long hash, wchar_t* data);

extern DECLSPEC void agb_library_string_free(agb_object_t* object);

extern DECLSPEC agb_object_t* agb_library_string_from_number(agb_thread_t* thread, double value);

extern DECLSPEC long agb_library_string_length(agb_object_t* object);

wchar_t* agb_library_string_chars(agb_object_t* object);

extern DECLSPEC agb_object_t* agb_library_string_concat(agb_thread_t* thread, agb_object_t* left, agb_object_t* right);

extern DECLSPEC int agb_library_string_equal(agb_object_t* left, agb_object_t* right);

extern DECLSPEC int agb_library_string_notequal(agb_object_t* left, agb_object_t* right);

extern DECLSPEC int agb_library_string_compare(agb_object_t* left, agb_object_t* right);

extern DECLSPEC agb_object_t* agb_library_string_substring(agb_thread_t* thread, agb_object_t* left, long begin, long end);

extern DECLSPEC agb_object_t* agb_library_string_code_to_char(agb_thread_t* thread, long code);

extern DECLSPEC long agb_library_string_char_to_code(agb_object_t* left);

extern DECLSPEC agb_object_t* agb_library_string_trim(agb_thread_t* thread, agb_object_t* left);

extern DECLSPEC wchar_t agb_library_string_charat(agb_object_t* left, long at);

extern DECLSPEC int agb_library_string_startwith(agb_object_t* left, agb_object_t* text);

extern DECLSPEC int agb_library_string_endwith(agb_object_t* left, agb_object_t* text);

extern DECLSPEC long agb_library_string_indexof(agb_object_t* left, agb_object_t* text, long index);

extern DECLSPEC long agb_library_string_lastindexof(agb_object_t* left, agb_object_t* text);

extern DECLSPEC agb_object_t* agb_library_string_replace(agb_thread_t* thread, agb_object_t* left, agb_object_t* lastvalue, agb_object_t* newvalue);

extern DECLSPEC agb_object_t* agb_library_string_tolowercase(agb_thread_t* thread, agb_object_t* left);

extern DECLSPEC agb_object_t* agb_library_string_touppercase(agb_thread_t* thread, agb_object_t* left);

agb_object_t* agb_library_string_base64_encode(agb_thread_t* thread, agb_object_t* left);

agb_object_t* agb_library_string_base64_decode(agb_thread_t* thread, agb_object_t* left);

agb_object_t* agb_library_string_utf_encode(agb_thread_t* thread, agb_object_t* left);

agb_object_t* agb_library_string_utf_decode(agb_thread_t* thread, agb_object_t* left);

#ifdef __cplusplus
}
#endif

#endif
