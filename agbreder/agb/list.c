#include "agb.h"

agb_list_t* agb_list_new() {
	agb_list_t* list = (agb_list_t*) agb_memory_alloc(sizeof(agb_list_t));
	if (!list) {
		return 0;
	}
	list->data = 0;
	list->size = 0;
	list->max = 10;
	return list;
}

int agb_list_add(agb_list_t* list, void* data) {
	if (!list) {
		return 1;
	}
	if (!list->data) {
		list->data = (void**) agb_memory_alloc(list->max * sizeof(void*));
		if (!list->data) {
			return 1;
		}
	} else if (list->size == list->max) {
		void** aux = (void**) agb_memory_realloc(list->data, list->max * 2 * sizeof(void*));
		if (!aux) {
			return 1;
		}
		list->data = aux;
		list->max *= 2;
	}
	list->data[list->size++] = data;
	return 0;
}

void* agb_list_get(agb_list_t* list, u_int32_t index) {
	if (index < 0 || index >= list->size) {
		return 0;
	}
	return list->data[index];
}

void agb_list_remove(agb_list_t* list, u_int32_t index) {
	if (index < 0 || index >= list->size) {
		return;
	}
	list->data[index] = list->data[--list->size];
}

void* agb_list_set(agb_list_t* list, u_int32_t index, void* data) {
	if (index < 0 || index >= list->size) {
		return 0;
	}
	void* aux = list->data[index];
	list->data[index] = data;
	return aux;
}

u_int32_t agb_list_size(agb_list_t* list) {
	return list->size;
}

void agb_list_free(agb_list_t* list) {
	if (list->data) {
		agb_memory_free(list->data);
	}
	agb_memory_free(list);
}

void agb_list_free_func(agb_list_t* list, void (*func)(void*)) {
	if (list->data) {
		int n;
		for (n = 0; n < list->size; n++) {
			func(list->data[n]);
		}
		agb_memory_free(list->data);
	}
	agb_memory_free(list);
}
