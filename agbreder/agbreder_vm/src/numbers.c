#include "agb.h"
#include "agbi.h"
#include "ctype.h"

agb_object_t* agb_library_nums_new(agb_thread_t* thread, double* bytes, long size) {
	agb_object_bytes_t* obj = agb_memory_alloc(sizeof(agb_object_nums_t));
	if (obj) {
		obj->array = bytes;
		obj->size = size;
		obj->classid = AGB_LIBRARY_NUMS_ID;
		if (!agb_thread_malloc(thread, obj)) {
			agb_object_free((agb_object_t*) obj);
			return 0;
		}
	}
	return (agb_object_t*) obj;
}
