#include "agb.h"
#include "agbi.h"

typedef struct {
	int width;
	int height;
	unsigned char* bytes;
	agb_vm_t* vm;
	SDL_Thread* thread;
} agb_request_t;

static int callthread(void* self) {
	agb_request_t* request = (agb_request_t*) self;
	while (!request->thread) {
		SDL_Delay(1);
	}
	agb_vm_t* vm = agb_vm_new(request->bytes);
	if (!vm) {
		return EXIT_FAILURE;
	}
	vm->guiData->width = request->width;
	vm->guiData->height = request->height;
	vm->parentVm = request->vm;
	request->vm->vmChild = vm;
	agb_thread_t* thread = agb_thread_new(vm);
	if (!thread) {
		return EXIT_FAILURE;
	}
	agb_method_t* method = agb_method_classname(vm, L"Main", L"main()", 1);
	if (!method) {
		return EXIT_FAILURE;
	}
	int pc = agb_method_pc(method);
	int flag = agb_vm_execute(thread, pc);
	SDL_Delay(1);
	request->vm->vmChild = 0;
	return flag;
}

void agb_library_vm_free(agb_thread_t* thread) {
	agb_vm_t* vm = thread->vm->vmChild;
	if (vm) {
		AGB_GuiData* guiData = vm->guiData;
		SDL_Event event = { SDL_QUIT };
		SDL_mutexP(thread->vm->guiLock);
		if (guiData->eventSize >= 1024) {
			guiData->events[1024 - 1] = event;
		} else {
			guiData->events[guiData->eventSize++] = event;
		}
		SDL_mutexV(thread->vm->guiLock);
		while (thread->vm->vmChild) {
			SDL_Delay(1);
		}
		agb_vm_free(vm);
	}
}

int agb_library_vm_alloc(agb_thread_t* thread, unsigned char* bytecodes, int width, int height) {
	if (!thread->vm->parentVm) {
		agb_library_vm_free(thread);
		// Cria um novo Processo
		{
			agb_request_t* request = agb_memory_alloc(sizeof(agb_request_t));
			if (!request) {
				return 1;
			}
			request->bytes = bytecodes;
			request->width = width;
			request->height = height;
			request->vm = thread->vm;
			request->thread = SDL_CreateThread(callthread, "Vm", request);
			while (!thread->vm->vmChild) {
				SDL_Delay(1);
			}
		}
	}
	return EXIT_SUCCESS;
}

void agb_library_vm_paint(agb_thread_t* thread, int x, int y) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!thread->vm->parentVm) {
		SDL_Surface* surface =
				guiData->screensSize == 0 ? guiData->surface : guiData->screens[guiData->screensSize - 1];
		SDL_Rect rect = { x, y, 0, 0 };
		SDL_BlitSurface(thread->vm->vmChild->guiData->surface, 0, surface, &rect);
	}
}
