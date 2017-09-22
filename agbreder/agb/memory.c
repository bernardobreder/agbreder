#include "agb.h"

typedef struct {
	void* data;
	u_int64_t size;
} agb_memory_data;

static int64_t memory_flag = 0;
static int64_t memory_size = -1;
static agb_memory_data** list = 0;
static u_int64_t listIndex = 0;

void* agb_memory_alloc(u_int64_t size) {
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

void* agb_memory_realloc(void* old, u_int64_t size) {
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

void agb_memory_set_size(int64_t size) {
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
	}
	listIndex = 0;
	list = calloc(size, sizeof(agb_memory_data));
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

int64_t agb_memory_get_size() {
	return memory_size;
}
