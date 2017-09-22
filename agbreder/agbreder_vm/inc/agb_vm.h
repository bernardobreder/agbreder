#ifndef AGB_VM
#define AGB_VM

#include "agb.h"
#include "agbi.h"

int agb_library_vm_alloc(agb_thread_t* thread, unsigned char* request, int width, int height);

void agb_library_vm_free(agb_thread_t* thread);

void agb_library_vm_paint(agb_thread_t* thread, int x, int y);

#endif
