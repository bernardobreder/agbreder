#include "agb.h"
#include "agbi.h"

#define osize(O) (O)->size
#define omax(O) (O)->max
#define odata(O) (O)->native

void agb_library_array_free(agb_object_array_t* object) {
	agb_memory_free(object->native);
}

agb_object_t* agb_library_array_new(agb_thread_t* thread, agb_ref_t* cos, int stacksize, int max) {
	agb_object_array_t* object = (agb_object_array_t*) agb_memory_alloc(sizeof(agb_object_array_t));
	if (!object) {
		return 0;
	}
	max = max <= 0 ? 1 : max;
	agb_object_t** aux = agb_vm_alloc(thread, cos, stacksize, max * sizeof(agb_object_t*));
	if (!aux) {
		agb_memory_free(object);
		return 0;
	}
	object->native = aux;
	object->max = max;
	object->classid = AGB_LIBRARY_ARRAY_ID;
	if (thread) {
		if (!agb_thread_malloc(thread, object)) {
			agb_object_free((agb_object_t*) object);
			return 0;
		}
	}
	return (agb_object_t*) object;
}

int agb_library_array_size(agb_object_array_t* object) {
	if (!object) {
		return 0;
	}
	return object->size;
}

int agb_library_array_add(agb_thread_t* thread, agb_ref_t* cos, int stacksize, agb_object_array_t* object, agb_object_t* item) {
	if (!object) {
		return 1;
	}
	if (object->size == object->max) {
		object->max *= 2;
		object->native = agb_vm_realloc(thread, cos, stacksize, object->native, object->max * sizeof(agb_object_t*));
		if (!object->native) {
			return 0;
		}
	}
	object->native[object->size++] = item;
	return 1;
}

int agb_library_array_insert(agb_thread_t* thread, agb_ref_t* cos, int stacksize, agb_object_array_t* object, int index, agb_object_t* item) {
	if (!object) {
		return 1;
	}
	if (object->size == object->max) {
		object->max *= 2;
		object->native = agb_vm_realloc(thread, cos, stacksize, object->native, object->max * sizeof(agb_object_t*));
		if (!object->native) {
			return 0;
		}
	}
	index--;
	int rightSize = object->size - index;
	if (rightSize) {
		memcpy( object->native + index + 1, object->native + index, rightSize * sizeof(agb_object_t*));
	}
	object->native[index] = item;
	object->size++;
	return 1;
}

agb_object_t* agb_library_array_get(agb_object_array_t* object, int index) {
	if (!object) {
		return 0;
	}
	if (index < 1 || index > object->size) {
		return 0;
	}
	index--;
	return object->native[index];
}

agb_object_t* agb_library_array_set(agb_object_array_t* object, int index, agb_object_t* value) {
	if (!object) {
		return 0;
	}
	if (index < 1 || index > object->size) {
		return 0;
	}
	index--;
	agb_object_t* old = object->native[index];
	object->native[index] = value;
	return old;
}

agb_object_t* agb_library_array_remove(agb_object_array_t* object, int index) {
	if (index < 1 || index > object->size) {
		return 0;
	}
	index--;
	agb_object_t* old = object->native[index];
	int rightSize = object->size - index;
	if (rightSize) {
		memcpy( object->native + index, object->native + index + 1, rightSize * sizeof(agb_object_t*));
	}
	object->size--;
	return old;
}

void agb_library_array_gc(agb_vm_t* vm, agb_object_array_t* object) {
	int n, size = object->size;
	agb_object_t** datas = object->native;
	for (n = 0; n < size; n++) {
		agb_object_t* item = *datas;
		if (item) {
			agb_vm_gc_object(vm, item);
		}
		datas++;
	}
}
