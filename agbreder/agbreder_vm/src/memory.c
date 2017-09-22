#include "agb.h"
#include "agbi.h"

typedef struct {
	void* data;
	size_t size;
} agb_memory_data;

static size_t memory_flag = 0;
static size_t memory_size = -1;
static agb_memory_data** list = 0;
static size_t listIndex = 0;

void* agb_memory_alloc(size_t size) {
	if (!memory_flag) {
		return calloc(1, size);
	} else {
		if (memory_size >= size) {
			memory_size -= size;
			void* data = calloc(1, size);
			agb_memory_data* cell = (agb_memory_data*) calloc(1, sizeof(agb_memory_data));
			assert(cell);
			cell->data = data;
			cell->size = size;
			if (data) {
				list[listIndex++] = cell;
			}
			return data;
		} else {
			return 0;
		}
	}
}

void* agb_memory_realloc(void* old, size_t size) {
	if (!memory_flag) {
		return realloc(old, size);
	} else {
		if (memory_size >= size) {
			long n, size = -1, found = 0;
			for (n = 0; n < listIndex; n++) {
				agb_memory_data* cell = list[n];
				if (cell->data == old) {
					memory_size += cell->size;
					memory_size -= size;
					cell->size = size;
					found = 1;
					break;
				}
			}
			assert(found == 1);
			return realloc(old, size);
		} else {
			return 0;
		}
	}
}

void agb_memory_set_size(long size) {
	memory_flag = size < 0 ? 0 : 1;
	memory_size = size;
	if (list) {
		u_int64_t n;
		for (n = 0; n < listIndex; n++) {
			agb_memory_data* cell = list[n];
			free(cell->data);
			free(cell);
		}
		free(list);
		list = 0;
	}
	if (memory_flag) {
		listIndex = 0;
		list = calloc(size, sizeof(agb_memory_data));
	}
}

void agb_memory_free(void* data) {
	free(data);
	if (memory_flag) {
		long n, size = -1;
		for (n = 0; n < listIndex; n++) {
			agb_memory_data* cell = list[n];
			if (cell->data == data) {
				size = cell->size;
				list[n] = list[--listIndex];
				break;
			}
		}
		if (size < 0) {
			size = 0;
		}
		assert(size >= 0);
		memory_size += size;
	}
}

size_t agb_memory_get_size() {
	return memory_size;
}

/*
 #define agb_object_class_t(FIELDS) struct { agb_object_t object ; agb_object_t fields [ FIELDS ] ; }
 int agb_memory_object_realloc(agb_thread_t* thread, int classid, unsigned int max) {
 int n;
 unsigned int uclassid = classid - AGB_LIBRARY_MAX;
 switch (classid) {
 case AGB_LIBRARY_NUMBER_ID: {
 agb_object_number_t* aux = (agb_object_number_t*) agb_memory_realloc(thread->objects[uclassid],
 max * sizeof(agb_object_number_t));
 if (!aux) {
 return 1;
 }
 thread->objects[uclassid] = (agb_object_t*) aux;
 for (n = 0; n < max; n++) {
 aux++->classid = AGB_LIBRARY_NUMBER_ID;
 }
 break;
 }
 case AGB_LIBRARY_BOOLEAN_ID: {
 agb_object_t* aux = (agb_object_t*) agb_memory_realloc(thread->objects[uclassid],
 max * sizeof(agb_object_boolean_t));
 if (!aux) {
 return 1;
 }
 thread->objects[uclassid] = aux;
 break;
 }
 case AGB_LIBRARY_STRING_ID: {
 agb_object_t* aux = (agb_object_t*) agb_memory_realloc(thread->objects[uclassid],
 max * sizeof(agb_object_string_t));
 if (!aux) {
 return 1;
 }
 thread->objects[uclassid] = aux;
 break;
 }
 case AGB_LIBRARY_ARRAY_ID: {
 agb_object_t* aux = (agb_object_t*) agb_memory_realloc(thread->objects[uclassid],
 max * sizeof(agb_object_array_t));
 if (!aux) {
 return 1;
 }
 thread->objects[uclassid] = aux;
 break;
 }
 case AGB_LIBRARY_NET_SOCKET_ID: {
 agb_object_t* aux = (agb_object_t*) agb_memory_realloc(thread->objects[uclassid],
 max * sizeof(agb_object_array_t));
 if (!aux) {
 return 1;
 }
 thread->objects[uclassid] = aux;
 break;
 }
 case AGB_LIBRARY_FUNC_ID: {
 agb_object_t* aux = (agb_object_t*) agb_memory_realloc(thread->objects[uclassid],
 max * sizeof(agb_object_func_t));
 if (!aux) {
 return 1;
 }
 thread->objects[uclassid] = aux;
 break;
 }
 default: {
 int fields = thread->vm->fields[uclassid + AGB_LIBRARY_MAX];
 void* aux = (agb_object_t*) agb_memory_realloc(thread->objects[uclassid],
 max * sizeof(agb_object_class_t ( fields ) ));
 if (!aux) {
 return 1;
 }
 thread->objects[uclassid] = aux;
 break;
 }
 }
 return 0;
 }

 unsigned int agb_memory_object_sizeof(agb_thread_t* thread, int classid) {
 switch (classid) {
 case AGB_LIBRARY_NUMBER_ID: {
 return sizeof(agb_object_number_t);
 }
 case AGB_LIBRARY_BOOLEAN_ID: {
 return sizeof(agb_object_boolean_t);
 }
 case AGB_LIBRARY_STRING_ID: {
 return sizeof(agb_object_string_t);
 }
 case AGB_LIBRARY_ARRAY_ID: {
 return sizeof(agb_object_array_t);
 }
 case AGB_LIBRARY_NET_SOCKET_ID: {
 return sizeof(agb_object_socket_t);
 }
 case AGB_LIBRARY_FUNC_ID: {
 return sizeof(agb_object_func_t);
 }
 default: {
 unsigned int uclassid = classid - AGB_LIBRARY_MAX;
 return sizeof(agb_object_t) + thread->vm->fields[uclassid];
 }
 }
 }

 agb_object_t* agb_memory_object_alloc(agb_thread_t* thread, int classid) {
 unsigned int uclassid = classid - AGB_LIBRARY_MAX;
 agb_segment_t* seg = thread->objs + uclassid;
 int index = seg->size;
 int segIndex = index / 1024;
 int pagIndex = index % 1024;
 if (pagIndex == 0 && segIndex % 1024 == 0) {
 if (seg->data) {
 seg->data = (unsigned char**) agb_memory_realloc(seg->data, ++seg->dataMax * 1024 * sizeof(unsigned char*));
 } else {
 seg->data = (unsigned char**) agb_memory_alloc(++seg->dataMax * 1024 * sizeof(unsigned char*));
 }
 }
 int sizeOf = agb_memory_object_sizeof(thread, classid);
 unsigned char* data = seg->data[segIndex];
 if (!data) {
 int n, size = 1024;
 data = agb_memory_alloc(size * sizeOf);
 unsigned char* aux = data;
 for (n = 0; n < 1024; n++) {
 ((agb_object_t*) aux)->classid = classid;
 aux += sizeOf;
 }
 seg->data[seg->dataSize++] = data;
 }
 agb_object_t* item = (agb_object_t*) (data + pagIndex * sizeOf);
 seg->size++;
 return item;
 }

 agb_object_number_t* agb_memory_object_number_alloc(agb_thread_t* thread) {
 return (agb_object_number_t*) agb_memory_object_alloc(thread, AGB_LIBRARY_NUMBER_ID);
 }

 void agb_memory_object_free(agb_thread_t* thread, agb_object_t* object) {
 int classid = object->classid;
 unsigned int uclassid = classid - AGB_LIBRARY_MAX;
 agb_segment_t* seg = thread->objs + uclassid;
 int index = seg->size - 1;
 int segIndex = index / 1024;
 int pagIndex = index % 1024;
 int sizeOf = agb_memory_object_sizeof(thread, classid);
 unsigned char* data = seg->data[segIndex];
 agb_object_t* item = (agb_object_t*) (data + pagIndex * sizeOf);
 memcpy( object, item, sizeOf);
 seg->size--;
 if (pagIndex == 0) {
 if (seg->data[segIndex + 1]) {
 agb_memory_free(data);
 seg->data[segIndex + 1] = 0;
 }
 if (segIndex % 1024 == 0) {
 int blockIndex = segIndex / 1024;
 if (seg->dataMax > blockIndex + 1) {
 seg->data = (unsigned char**) agb_memory_realloc(seg->data,
 --seg->dataMax * 1024 * sizeof(unsigned char*));
 }
 }
 }
 }

 int agb_memory_object_init(agb_thread_t* thread) {
 int size = thread->vm->classSize - AGB_LIBRARY_MAX;
 thread->objs = agb_memory_alloc(size * sizeof(agb_segment_t));
 {
 int max = 4 * 1024 * 1024 + 2 * 1024;
 agb_object_t * * objects = agb_memory_alloc(max * sizeof(agb_object_t*));
 int n, size = max;
 printf("a");
 for (n = 0; n < size; n++) {
 objects[n] = (agb_object_t *) agb_memory_object_number_alloc(thread);
 assert( objects [ n ]->used==0);
 objects[n]->used = 1;
 }
 printf("b");
 for (n = 0; n < size; n++) {
 agb_memory_object_free(thread, objects[n]);
 }
 printf("c");
 }
 return 0;
 }
 */
