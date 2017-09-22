#include "agb.h"
#include "agbi.h"

//#define AGB_TRACE
#ifdef AGB_TRACE
#define dprintf2(O) printf(O, pc - vm->bytes - 2, (void*)cobjectStack , (void*)cpcStack , *pc)
#define dprintf1(O) printf(O, pc - vm->bytes - 2, (void*)cobjectStack , (void*)cpcStack )
#define address(O) (O)/*((O)?(void*)((void*)(O)-(void*)vm):0)*/
#else
#define dprintf2(O) ;
#define dprintf1(O) ;
#endif

#define onum(I) (ostack(I)->number)
#define oint(I) ((long)(ostack(I)->number))
#define ostr(I) (ostack(I)->data)
#define ostrlen(I) ((int*)(cos+(I))->data->native)[0]
#define ostrhash(I) ((int*)(cos+(I))->data->native)[1]
#define ostrdata(I) ((char*)(((int*)(cos+(I))->data->native)+2))
#define obool(I) ostack(I)->number
#define oobj(I) ostack(I)->data
#define ostack(I) (cobjectStack+(I))
//#define ostr(I) ((agb_object_string_t*)oobj(I))
#define oarray(I) ((agb_object_array_t*)oobj(I))
#define oints(I) ((agb_object_ints_t*)oobj(I))
#define onums(I) ((agb_object_nums_t*)oobj(I))
#define ouint2d(I) ((agb_object_uint2d_t*)oobj(I))
#define obools(I) ((agb_object_bools_t*)oobj(I))
#define cosdecn(n) cobjectStack -= n
#define cosdec() cobjectStack--
#define cosinc() cobjectStack++; \
									if ( cobjectStack >= coslim ) { \
										printf ( "Stack Overflow Exception\n" ) ; \
										return EXIT_FAILURE ; \
									}

int agb_method_pc(agb_method_t* method) {
	return method->pc;
}

agb_class_t* agb_class_name(agb_vm_t* vm, wchar_t* name) {
	int n;
	agb_class_t** classes = vm->classes;
	for (n = 0; n < vm->classSize; n++) {
		if (!wcscmp((*classes)->name, name)) {
			return *classes;
		}
		classes++;
	}
	return 0;
}

agb_method_t* agb_method_name(agb_vm_t* vm, agb_class_t* class_def, wchar_t* name, int isStatic) {
	int n;
	agb_method_t** methods = class_def->methods;
	for (n = 0; n < class_def->methodSize; n++) {
		if ((*methods)->isStatic == isStatic) {
			if (!wcscmp((*methods)->name, name)) {
				return *methods;
			}
		}
		methods++;
	}
	return 0;
}

agb_method_t* agb_method_classname(agb_vm_t* vm, wchar_t* classname, wchar_t* methodName, int isStatic) {
	agb_class_t* class_def = agb_class_name(vm, classname);
	return class_def == 0 ? 0 : agb_method_name(vm, class_def, methodName, isStatic);
}

void agb_object_free(agb_object_t* object) {
	if (object) {
		int classid = object->classid;
		if (classid < 0) {
			switch (classid) {
			case AGB_LIBRARY_STRING_ID: {
				agb_library_string_free(object);
				break;
			}
			case AGB_LIBRARY_ARRAY_ID: {
				agb_memory_free(((agb_object_array_t*) object)->native);
				break;
			}
			case AGB_LIBRARY_UINT2D_ID: {
				agb_memory_free(((agb_object_uint2d_t*) object)->array);
				break;
			}
			case AGB_LIBRARY_NUMS_ID: {
				agb_memory_free(((agb_object_nums_t*) object)->array);
				break;
			}
			case AGB_LIBRARY_BOOLS_ID: {
				agb_memory_free(((agb_object_bools_t*) object)->array);
				break;
			}
			case AGB_LIBRARY_BYTES_ID: {
				agb_memory_free(((agb_object_bytes_t*) object)->array);
				break;
			}
			case AGB_LIBRARY_FUNC_ID: {
				break;
			}
			}
		}
		agb_memory_free(object);
	}
}

void agb_thread_free(agb_thread_t* thread) {
	if (thread) {
		{
			int n, size = thread->memorySize;
			for (n = 0; n < size; n++) {
				agb_object_t* object = thread->memory[n];
				agb_object_free(object);
			}
		}
		agb_memory_free(thread->memory);
//		agb_memory_free ( thread->objectMax ) ;
//		agb_memory_free ( thread->objectSize ) ;
//		{
//			int n , size = thread->vm->classSize ;
//			for ( n = 0 ; n < size ; n ++ ) {
//				agb_memory_free ( thread->objects [ n ] ) ;
//			}
//		}
//		agb_memory_free ( thread->objects ) ;
		agb_memory_free(thread);
	}
}

static void b_vm_init_cache_method_aux_aux(agb_vm_t* vm, agb_class_t* bclass, agb_class_t* clazz, agb_method_t* method) {
	if (clazz->extendIndex >= 0) {
		agb_class_t* extend = vm->classes[clazz->extendIndex];
		b_vm_init_cache_method_aux_aux(vm, bclass, extend, method);
	}
	int methods = vm->methodSize;
	int m, sizem = clazz->methodSize;
	for (m = 0; m < sizem; m++) {
		agb_method_t* smethod = clazz->methods[m];
		if (!wcscmp(smethod->name, method->name)) {
			if (!smethod->isStatic) {
				vm->opcs[bclass->id * methods + smethod->id] = method->pc;
				break;
			}
		}
	}
}

static void b_vm_init_cache_method_aux(agb_vm_t* vm, agb_class_t* bclass, agb_class_t* clazz) {
	if (clazz->extendIndex >= 0) {
		agb_class_t* extend = vm->classes[clazz->extendIndex];
		b_vm_init_cache_method_aux(vm, bclass, extend);
	}
	int m, sizem = clazz->methodSize;
	for (m = 0; m < sizem; m++) {
		agb_method_t* method = clazz->methods[m];
		if (!method->isStatic) {
			b_vm_init_cache_method_aux_aux(vm, bclass, clazz, method);
		}
	}
}

agb_thread_t* agb_thread_new(agb_vm_t* vm) {
	agb_thread_t* thread = agb_memory_alloc(sizeof(agb_thread_t));
	if (!thread) {
		return 0;
	}
	thread->memoryMax = 1024;
	thread->memory = agb_memory_alloc(thread->memoryMax * sizeof(void*));
	if (!thread->memory) {
		goto end;
	}
	thread->vm = vm;
	vm->threads[vm->threadSize++] = thread;
//	agb_memory_object_init ( thread ) ;
	return thread;
	end: {
		agb_thread_free(thread);
		return 0;
	}
}

/**
 * Armazena o objeto dentro da memoria da thread corrente para que possa ser analisado pelo coletor de lixo.
 * Retorna 0 se não houve memória suficiente para colocar o objeto na thread. Se retornar 1, houve memória.
 */
int agb_thread_malloc(agb_thread_t* thread, void* data) {
	if (thread->memorySize == thread->memoryMax) {
		void* aux = agb_memory_realloc(thread->memory, thread->memoryMax * 2 * sizeof(void*));
		if (!aux) {
			return 0;
		}
		thread->memoryMax *= 2;
		thread->memory = aux;
	}
	thread->memory[thread->memorySize++] = data;
	return 1;
}

void agb_vm_free(agb_vm_t* vm) {
	if (vm) {
		int n, m;
		if (vm->threads) {
			for (n = 0; n < vm->threadSize; n++) {
				agb_thread_t* thread = vm->threads[n];
				agb_vm_gc(thread, 0, 0);
				agb_thread_free(thread);
			}
			agb_memory_free(vm->threads);
		}
		agb_memory_free(vm->bytes);
		if (vm->numbers) {
			agb_memory_free(vm->numbers);
		}
		if (vm->strings) {
			for (n = 0; n < vm->stringSize; n++) {
				agb_object_free(vm->strings[n]);
			}
			agb_memory_free(vm->strings);
		}
		if (vm->classes) {
			for (n = 0; n < vm->classSize; n++) {
				agb_class_t* class_def = vm->classes[n];
				if (class_def) {
					agb_memory_free(class_def->name);
					if (class_def->methods) {
						for (m = 0; m < class_def->methodSize; m++) {
							agb_method_t* method_def = class_def->methods[m];
							if (method_def) {
								agb_memory_free(method_def->name);
								agb_memory_free(method_def);
							}
						}
						agb_memory_free(class_def->methods);
					}
					agb_memory_free(class_def);
				}
			}
			agb_memory_free(vm->classes);
		}
		agb_memory_free(vm->cast);
		agb_memory_free(vm->opcs);
		agb_memory_free(vm->fields);
		agb_memory_free(vm->spcs);
		agb_memory_free(vm->guiData);
		agb_memory_free(vm);
	}
}

void agb_vm_gc_object(agb_vm_t* vm, agb_object_t* object) {
	if (object && !object->used) {
		object->used = 1;
		int classid = object->classid;
		assert( classid < vm->classSize);
		if (classid >= 0) {
			agb_ref_t* field = agb_object_data(object);
			int n, size = vm->fields[classid];
			for (n = 0; n < size; n++) {
				if (field->data) {
					agb_object_t* data = field->data;
					if (!data->used) {
						agb_vm_gc_object(vm, field->data);
					}
				}
				field++;
			}
		} else {
			if (classid == AGB_LIBRARY_ARRAY_ID) {
				agb_library_array_gc(vm, (agb_object_array_t*) object);
			} else if (classid == AGB_LIBRARY_FUNC_ID) {
				int n, size = agb_object_datai(object, 1).number;
				for (n = 0; n < size; n++) {
					agb_object_t* child = agb_object_datai(object, n + 2).data;
					if (child) {
						agb_vm_gc_object(vm, child);
					}
				}
			}
		}
	}
}

void agb_vm_gc(agb_thread_t* thread, agb_ref_t* cos, int size) {
	int n;
	if (size > 0) {
		agb_ref_t* index = cos - size + 1;
		for (n = 0; n < size; n++) {
			agb_ref_t* ref = index++;
			if (ref->data) {
				agb_vm_gc_object(thread->vm, ref->data);
			}
		}
	}
	for (n = 0; n < thread->memorySize; n++) {
		agb_object_t* object = thread->memory[n];
		if (!object->used) {
			thread->memory[n--] = thread->memory[--thread->memorySize];
			agb_object_free(object);
		} else {
			object->used = 0;
		}
	}
}

unsigned int agb_vm_cast(agb_vm_t* vm, unsigned int index, agb_class_t* clazz) {
	if (index == clazz->id) {
		return 1;
	}
	if (clazz->extendIndex >= 0) {
		agb_class_t* extend = vm->classes[clazz->extendIndex];
		if (extend->id == index) {
			return 1;
		}
		if (agb_vm_cast(vm, index, extend) == 1) {
			return 1;
		}
	}
	return 0;
}

void* agb_vm_alloc(agb_thread_t* thread, agb_ref_t* cos, int stacksize, int size) {
	void* data = agb_memory_alloc(size);
	if (!data) {
		agb_vm_gc(thread, cos, stacksize);
		data = agb_memory_alloc(size);
		if (!data) {
			return 0;
		}
	}
	return data;
}

void* agb_vm_realloc(agb_thread_t* thread, agb_ref_t* cos, int stacksize, void* old, int size) {
	void* data = agb_memory_realloc(old, size);
	if (!data) {
		agb_vm_gc(thread, cos, stacksize);
		data = agb_memory_realloc(old, size);
		if (!data) {
			return 0;
		}
	}
	return data;
}

void* agb_object_new(int fields) {
	return agb_memory_alloc(sizeof(agb_object_t) + fields * sizeof(agb_ref_t));
}

void* agb_object_vm_new(agb_thread_t* thread, agb_ref_t* cos, int stacksize, int fields) {
	return agb_vm_alloc(thread, cos, stacksize, sizeof(agb_object_t) + fields * sizeof(agb_ref_t));
}

agb_vm_t* agb_vm_new(unsigned char* buffer) {
	unsigned char** file = &buffer;
	if (agb_bytecode_read_byte(file) != 0xBB) {
		goto end;
	}
	agb_vm_t* vm = agb_memory_alloc(sizeof(agb_vm_t));
	if (!vm) {
		goto end;
	}
	vm->threadMax = 1024;
	vm->threads = agb_memory_alloc(vm->threadMax * sizeof(agb_thread_t));
	if (!vm->threads) {
		goto end;
	}
	{
		vm->guiData = agb_memory_alloc(sizeof(AGB_GuiData));
		if (!vm->guiData) {
			goto end;
		}
	}
	{
		int n, size = agb_bytecode_read_int(file);
		if (size < 0) {
			goto end;
		}
		vm->numberSize = size;
		vm->numbers = agb_memory_alloc(size * sizeof(double));
		if (!vm->numbers) {
			goto end;
		}
		for (n = 0; n < size; n++) {
			wchar_t* str = agb_bytecode_read_utf(file);
			if (!str) {
				goto end;
			}
			num value = 0;
			int result = swscanf(str, L"%lf", &value);
			agb_memory_free(str);
			if (result == 0) {
				goto end;
			}
			vm->numbers[n] = value;
		}
	}
	{
		int n, size = agb_bytecode_read_int(file);
		if (size < 0) {
			goto end;
		}
		vm->stringSize = size;
		vm->strings = agb_memory_alloc(size * sizeof(agb_object_t*));
		if (!vm->strings) {
			goto end;
		}
		for (n = 0; n < size; n++) {
			wchar_t* str = agb_bytecode_read_utf(file);
			if (!str) {
				goto end;
			}
			agb_object_t* object = agb_library_string_new(0, -1, -1, str);
			if (!object) {
				agb_memory_free(str);
				goto end;
			}
			agb_memory_free(str);
			vm->strings[n] = object;
		}
	}
	{
		int n, classSize = agb_bytecode_read_int(file);
		if (classSize < 0) {
			goto end;
		}
		vm->classSize = classSize;
		vm->classes = agb_memory_alloc(classSize * sizeof(agb_class_t*));
		if (!vm->classes) {
			goto end;
		}
		for (n = 0; n < classSize; n++) {
			agb_class_t* class_def = (agb_class_t*) agb_memory_alloc(sizeof(agb_class_t));
			if (!class_def) {
				goto end;
			}
			vm->classes[n] = class_def;
			class_def->id = n;
			class_def->name = agb_bytecode_read_utf(file);
			if (!class_def->name) {
				goto end;
			}
			class_def->extendIndex = agb_bytecode_read_int(file);
			if (class_def->extendIndex < -1) {
				goto end;
			}
			class_def->fieldSize = agb_bytecode_read_int(file);
			if (class_def->fieldSize < 0) {
				goto end;
			}
		}
	}
	{
		int n, index = 0, classSize = agb_bytecode_read_int(file);
		if (classSize < 0) {
			goto end;
		}
		for (n = 0; n < classSize; n++) {
			int m, methodSize = agb_bytecode_read_int(file);
			if (methodSize < 0) {
				goto end;
			}
			agb_class_t* class_def = vm->classes[n];
			class_def->methodSize = methodSize;
			vm->methodSize += methodSize;
			class_def->methods = agb_memory_alloc(methodSize * sizeof(agb_method_t*));
			if (!class_def->methods) {
				goto end;
			}
			for (m = 0; m < methodSize; m++, index++) {
				agb_method_t* method_def = (agb_method_t*) agb_memory_alloc(sizeof(agb_method_t));
				if (!method_def) {
					goto end;
				}
				class_def->methods[m] = method_def;
				method_def->id = index;
				method_def->name = agb_bytecode_read_utf(file);
				if (!method_def->name) {
					goto end;
				}
				method_def->isStatic = agb_bytecode_read_int(file);
				if (method_def->isStatic < 0) {
					goto end;
				}
				method_def->pc = agb_bytecode_read_int(file);
				if (method_def->pc < 0) {
					goto end;
				}
				int pcs = agb_bytecode_read_int(file);
				if (pcs < 0) {
					goto end;
				}
			}
		}
	}
	{
		{
			int n, sizec = vm->classSize;
			vm->opcs = agb_memory_alloc(vm->classSize * vm->methodSize * sizeof(int));
			if (!vm->opcs) {
				goto end;
			}
			for (n = 0; n < sizec; n++) {
				agb_class_t* aux_clazz = vm->classes[n];
				b_vm_init_cache_method_aux(vm, aux_clazz, aux_clazz);
			}
		}
		{
			int n, m, p;
			vm->spcs = agb_memory_alloc(vm->methodSize * sizeof(int));
			if (!vm->spcs) {
				goto end;
			}
			for (n = 0, m = 0; n < vm->classSize; n++) {
				agb_class_t* classnode = vm->classes[n];
				for (p = 0; p < classnode->methodSize; p++) {
					agb_method_t* methodnode = classnode->methods[p];
					vm->spcs[m++] = methodnode->pc;
				}
			}
		}
		{
			int n, size = vm->classSize;
			vm->fields = agb_memory_alloc(size * sizeof(int));
			if (!vm->fields) {
				goto end;
			}
			for (n = 0; n < size; n++) {
				agb_class_t* classnode = vm->classes[n];
				vm->fields[n] = 0;
				for (;;) {
					vm->fields[n] += classnode->fieldSize;
					if (classnode->extendIndex >= 0) {
						classnode = vm->classes[classnode->extendIndex];
					} else {
						break;
					}
				}
			}
		}
		{
			int ifrom, sizec = vm->classSize;
			if (!(vm->cast = agb_memory_alloc(sizec * sizec * sizeof(unsigned char)))) {
				goto end;
			}
			for (ifrom = 0; ifrom < sizec; ifrom++) {
				agb_class_t* cfrom = vm->classes[ifrom];
				agb_class_t* cto = cfrom;
				while (cto) {
					vm->cast[ifrom * sizec + cto->id] = 1;
					cto = cto->extendIndex >= 0 ? vm->classes[cto->extendIndex] : 0;
				}
			}
		}
	}
	{
		if (agb_bytecode_read_byte(file) != 0xFF) {
			goto end;
		}
	}
	int n;
	{
		int byteSize = agb_bytecode_read_int(file);
		if (byteSize < 0) {
			goto end;
		}
		vm->bytes = (int*) agb_memory_alloc(byteSize * sizeof(int));
		if (!vm->bytes) {
			goto end;
		}
		for (n = 0; n < byteSize; n++) {
			int bytecode = agb_bytecode_read_int(file);
			if (bytecode < 0) {
				goto end;
			}
			vm->bytes[n] = bytecode;
		}
	}
	{
		if (agb_bytecode_read_int1(file) != 255) {
			goto end;
		}
	}
	return vm;
	end: {
		agb_vm_free(vm);
		return 0;
	}
}

#define objstack_count 1024
int agb_vm_execute(agb_thread_t* thread, int opcodeIndex) {
	struct throw_t {
		agb_ref_t* cos;
		int* pc;
		int** stack;
	} throw[1024];
	int n, m, size, size2, index = 0;
	agb_vm_t* vm = thread->vm;
	int* bytecodes = vm->bytes;
	int* opcs = vm->opcs;
	int* spcs = vm->spcs;
	int* fields = vm->fields;
	int classeSize = vm->classSize;
	int methodSize = vm->methodSize;
	unsigned char* cast = vm->cast;
	double* numbers = vm->numbers;
	agb_object_t** stringPoll = vm->strings;
	agb_object_t* auxobj;
	agb_object_nums_t* auxnums, *auxnums2;
	agb_object_uint2d_t* auxuint2d, *auxuint2d2;
	agb_object_bools_t* auxbools, *auxbools2;
	long auxint, auxint2, auxint3, auxint4, auxint5, auxint6;
	void* auxvoid;
	double auxnum;
	unsigned char auxbool;
	agb_ref_t objectStack[objstack_count];
	agb_ref_t* coslim = objectStack + objstack_count;
	struct throw_t* cthrow = throw;
//	num* numbers = objstack ;
// Current Pc Stack
	int* pcStack[1024];
	int** cpcStack = pcStack;
// Current Object Stack
	register agb_ref_t* cobjectStack = objectStack;
// Pc
	register int* pc = bytecodes + opcodeIndex;
	for (;;) {
		switch (*(pc++)) {
		case _AGB_GP_STACK: {
			switch (*(pc++)) {
			case _AGB_OP_STACK_PUSH: {
				dprintf2( "[%ld][%p][%p] stack.push %d\n");
				index = *pc++;
				if (cobjectStack + index >= coslim) {
					printf("Stack Overflow Exception\n");
					return EXIT_FAILURE;
				}
				for (n = 0; n < index; n++) {
					cobjectStack++;
					cobjectStack->data = 0;
				}
				break;
			}
			case _AGB_OP_STACK_POP: {
				dprintf2( "[%ld][%p][%p] stack.pop %d\n");
				cobjectStack -= *pc++;
				break;
			}
			case _AGB_OP_STACK_LOAD: {
				dprintf2( "[%ld][%p][%p] stack.load %d\n");
				cosinc();
				*cobjectStack = *(cobjectStack - *pc - 1);
				pc++;
				break;
			}
			case _AGB_OP_STACK_STORE: {
				dprintf2( "[%ld][%p][%p] stack.store %d\n");
				cobjectStack[-(*pc++)] = *cobjectStack;
				cobjectStack--;
				break;
			}
			case _AGB_OP_STACK_PRE_INC: {
				dprintf2( "[%ld][%p][%p] stack.pre.inc %d\n");
				onum(0)++;
				onum(-(*pc++))++;
				break;
			}
			case _AGB_OP_STACK_PRE_DEC: {
				dprintf2( "[%ld][%p][%p] stack.pre.dec %d\n");
				onum(0)--;
				onum(-(*pc++))--;
				break;
			}
			case _AGB_OP_STACK_POS_INC: {
				dprintf2( "[%ld][%p][%p] stack.pos.inc %d\n");
				onum(-(*pc++))++;
				break;
			}
			case _AGB_OP_STACK_POS_DEC: {
				dprintf2( "[%ld][%p][%p] stack.pos.dec %d\n");
				onum(-(*pc++))--;
				break;
			}
			case _AGB_OP_STACK_FOR_INC_BEGIN: {
				dprintf2( "[%ld][%p][%p] stack.for.inc.begin %d\n");
				if (onum(-*pc) <= onum(0)) {
					pc += 2;
				} else {
					pc = bytecodes + pc[1];
				}
				break;
			}
			case _AGB_OP_STACK_FOR_INC_END: {
				dprintf2( "[%ld][%p][%p] stack.for.inc.end %d\n");
				onum(-*pc) += 1;
				pc = bytecodes + pc[1];
				break;
			}
			case _AGB_OP_STACK_FOR_DEC_BEGIN: {
				dprintf2( "[%ld][%p][%p] stack.for.dec.begin %d\n");
				if (onum(-*pc) >= onum(0)) {
					pc += 2;
				} else {
					pc = bytecodes + pc[1];
				}
				break;
			}
			case _AGB_OP_STACK_FOR_DEC_END: {
				dprintf2( "[%ld][%p][%p] stack.for.dec.end %d\n");
				onum(-*pc) -= 1;
				pc = bytecodes + pc[1];
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_LOAD: {
			switch (*(pc++)) {
			case _AGB_OP_LOAD_NUM: {
				dprintf2( "[%ld][%p][%p] load.num %d\n");
				cosinc();
				onum(0) = numbers[*pc++];
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_LOAD_STR: {
				dprintf2( "[%ld][%p][%p] load.str %d\n");
				cosinc();
				ostr(0) = stringPoll[*pc++];
				break;
			}
			case _AGB_OP_LOAD_TRUE: {
				dprintf1( "[%ld][%p][%p] load.true\n");
				cosinc();
				obool(0) = 1;
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_LOAD_FALSE: {
				dprintf1( "[%ld][%p][%p] load.false\n");
				cosinc();
				obool(0) = 0;
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_LOAD_NULL: {
				dprintf1( "[%ld][%p][%p] load.null\n");
				cosinc();
				cobjectStack->data = 0;
				break;
			}
			case _AGB_OP_LOAD_FUNC: {
				dprintf1( "[%ld][%p][%p] load.func\n");
				auxint = *pc++;
				auxobj = agb_vm_alloc(thread, cobjectStack, cobjectStack - objectStack,
						sizeof(agb_object_t) + (auxint + 2) * sizeof(agb_ref_t));
				if (!auxobj) {
					printf("OutOfMemoryException\n");
					return EXIT_FAILURE;
				}
				if (!agb_thread_malloc(thread, auxobj)) {
					agb_object_free(auxobj);
					printf("OutOfMemoryException\n");
					return EXIT_FAILURE;
				}
				agb_object_datai(auxobj, 0).number = *pc++;
				agb_object_datai(auxobj, 1).number = auxint;
				cosdecn( auxint - 1);
				memcpy( agb_object_data(auxobj) + 2, cobjectStack, auxint * sizeof(agb_ref_t));
				cobjectStack->data = auxobj;
				cobjectStack->data->classid = AGB_LIBRARY_FUNC_ID;
				break;
			}
			case _AGB_OP_LOAD_NEW: {
				dprintf2( "[%ld][%p][%p] load.new %d\n");
				cosinc();
				auxobj = agb_vm_alloc(thread, cobjectStack, cobjectStack - objectStack,
						sizeof(agb_object_t) + fields[*pc] * sizeof(agb_ref_t));
				if (auxobj) {
					agb_thread_malloc(thread, auxobj);
					oobj(0) = auxobj;
					oobj(0)->classid = *pc;
				}
				pc++;
				agb_vm_gc(thread, cobjectStack, cobjectStack - objectStack);
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_JUMP: {
			switch (*(pc++)) {
			case _AGB_OP_JUMP_STACK: {
				dprintf2( "[%ld][%p][%p] jump.stack %d\n");
				break;
			}
			case _AGB_OP_JUMP_INT: {
				dprintf2( "[%ld][%p][%p] jump.int %d\n");
				pc = bytecodes + *pc;
				break;
			}
			case _AGB_OP_JUMP_TRUE: {
				dprintf2( "[%ld][%p][%p] jump.true %d\n");
				if (obool(0)) {
					pc = bytecodes + *pc;
				} else {
					pc++;
				}
				cobjectStack--;
				break;
			}
			case _AGB_OP_JUMP_FALSE: {
				dprintf2( "[%ld][%p][%p] jump.false %d\n");
				if (obool(0)) {
					pc++;
				} else {
					pc = bytecodes + *pc;
				}
				cobjectStack--;
				break;
			}
			case _AGB_OP_JUMP_CALL: {
				dprintf2( "[%ld][%p][%p] jump.object.call %d\n");
				index = *pc++;
				if (!oobj(0) || cpcStack - pcStack >= 1024) {
					index = *pc++;
					cobjectStack[-index] = cobjectStack[0];
					cosdecn(-index);
				} else {
					*cpcStack++ = pc + 1;
					pc = bytecodes + opcs[oobj(0)->classid * methodSize + index];
				}
				break;
			}
			case _AGB_OP_JUMP_STATIC_CALL: {
				dprintf2( "[%ld][%p][%p] jump.static.call %d ");
				index = *pc++;
				if (cpcStack - pcStack >= 1024) {
					index = *pc++ - 1;
					// TODO Verificar a troca e o StackOverFlow
					cosdecn(-index);
					cobjectStack->data = 0;
					cobjectStack->number = 0;
				} else {
					*cpcStack++ = pc + 1;
					pc = bytecodes + spcs[index];
				}
				break;
			}
			case _AGB_OP_JUMP_RETURN: {
				dprintf2( "[%ld][%p][%p] jump.return %d\n");
				cobjectStack -= *pc;
				if (cpcStack == pcStack) {
					assert( cobjectStack == objectStack + 1);
					cobjectStack--;
					agb_vm_gc(thread, cobjectStack, 0);
					if (thread->memorySize > 0) {
						return EXIT_FAILURE;
					}
					return EXIT_SUCCESS;
				}
				cobjectStack[0] = cobjectStack[*pc];
				pc = *--cpcStack;
				break;
			}
			case _AGB_OP_JUMP_TRUE_DUP: {
				dprintf2( "[%ld][%p][%p] jump.true.dup %d\n");
				if (obool(0)) {
					pc = bytecodes + *pc;
				} else {
					pc++;
				}
				break;
			}
			case _AGB_OP_JUMP_FALSE_DUP: {
				dprintf2( "[%ld][%p][%p] jump.true.dup %d\n");
				if (obool(0)) {
					pc++;
				} else {
					pc = bytecodes + *pc;
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			case _AGB_OP_JUMP_FUNC_CALL: {
				dprintf2( "[%ld][%p][%p] jump.func.call %d ");
				if (cpcStack - pcStack >= 1024) {
					printf("StackOverflowException\n");
					return EXIT_FAILURE;
				}
				auxint = agb_object_datai( oobj(0) , 0 ).number;
				*cpcStack++ = pc;
				pc = bytecodes + spcs[auxint];
				break;
			}
			}
			break;
		}
		case _AGB_GP_OBJECT: {
			switch (*(pc++)) {
			case _AGB_OP_OBJECT_CAST_CLASS: {
				dprintf2( "[%ld][%p][%p] object.cast.class %d\n");
				if (oobj(0)) {
					if (oobj(0)->classid < 0 || !cast[oobj(0)->classid * classeSize + *pc]) {
						printf("Class Cast Exception\n");
						return EXIT_FAILURE;
					}
				}
				pc++;
				break;
			}
			case _AGB_OP_OBJECT_CAST_NATIVE: {
				dprintf2( "[%ld][%p][%p] object.cast.native %d\n");
				if (oobj(0)) {
					if (oobj(0)->classid != -*pc) {
						printf("Class Cast Exception\n");
						return EXIT_FAILURE;
					}
				}
				pc++;
				break;
			}
			case _AGB_OP_OBJECT_GET_FIELD: {
				dprintf2( "[%ld][%p][%p] object.getfield %d\n");
				if (oobj(0)) {
					*cobjectStack = agb_object_datai(oobj(0), *pc++);
				}
				break;
			}
			case _AGB_OP_OBJECT_SET_FIELD: {
				dprintf2( "[%ld][%p][%p] object.setfield %d\n");
				if (oobj(0)) {
					agb_object_datai(oobj(0), *pc++) = cobjectStack[-1];
					cobjectStack -= 2;
				}
				break;
			}
			case _AGB_OP_OBJECT_EQUAL: {
				dprintf1( "[%ld][%p][%p] object.equal\n");
				cobjectStack--;
				obool(0) = oobj(0) == oobj(1);
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_OBJECT_NOTEQUAL: {
				dprintf1( "[%ld][%p][%p] object.notequal\n");
				cobjectStack--;
				obool(0) = oobj(0) != oobj(1);
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_OBJECT_HASHCODE: {
				dprintf1( "[%ld][%p][%p] object.hashcode\n");
				if (oobj(0)) {
					onum(0) = agb_library_object_hashcode(thread, oobj(0));
				} else {
					onum(0) = 0;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_OBJECT_CLASSNAME: {
				dprintf1( "[%ld][%p][%p] object.classname\n");
				if (oobj(0)) {
					ostr(0) = agb_library_object_classname(thread, oobj(0));
				} else {
					oobj(0) = 0;
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_BOOL: {
			switch (*(pc++)) {
			case _AGB_OP_BOOL_NOT: {
				dprintf1( "[%ld][%p][%p] bool.not\n");
				obool(0) = !obool(0);
				break;
			}
			case _AGB_OP_BOOL_OR: {
				dprintf1( "[%ld][%p][%p] bool.or\n");
				cobjectStack--;
				obool(0) = obool(0) || obool(1);
				break;
			}
			case _AGB_OP_BOOL_AND: {
				dprintf1( "[%ld][%p][%p] bool.and\n");
				cobjectStack--;
				obool(0) = obool(0) && obool(1);
				break;
			}
			case _AGB_OP_BOOL_EQUAL: {
				dprintf1( "[%ld][%p][%p] bool.equal\n");
				cobjectStack--;
				obool(0) = obool(0) == obool(1);
				break;
			}
			case _AGB_OP_BOOL_NEQUAL: {
				dprintf1( "[%ld][%p][%p] bool.notequal\n");
				cobjectStack--;
				obool(0) = obool(0) != obool(1);
				break;
			}
			case _AGB_OP_BOOL_TO_STR: {
				dprintf1( "[%ld][%p][%p] bool.bool2str\n");
				if (obool(0)) {
					ostr(0) = agb_library_string_new(thread, 4, -1, L"true");
				} else {
					ostr(0) = agb_library_string_new(thread, 5, -1, L"false");
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_NUM: {
			switch (*(pc++)) {
			case _AGB_OP_NUM_NEG: {
				dprintf1( "[%ld][%p][%p] num.neg\n");
				onum(0) = -onum(0);
				break;
			}
			case _AGB_OP_NUM_SUM: {
				dprintf1( "[%ld][%p][%p] num.sum\n");
				cobjectStack--;
				onum(0) = onum(0) + onum(1);
				break;
			}
			case _AGB_OP_NUM_SUB: {
				dprintf1( "[%ld][%p][%p] num.sub\n");
				cobjectStack--;
				onum(0) = onum(0) - onum(1);
				break;
			}
			case _AGB_OP_NUM_MUL: {
				dprintf1( "[%ld][%p][%p] num.mul\n");
				cobjectStack--;
				onum(0) = onum(0) * onum(1);
				break;
			}
			case _AGB_OP_NUM_DIV: {
				dprintf1( "[%ld][%p][%p] num.div\n");
				cobjectStack--;
				onum(0) = onum(0) / onum(1);
				break;
			}
			case _AGB_OP_NUM_EQUAL: {
				dprintf1( "[%ld][%p][%p] num.equal\n");
				cobjectStack--;
				obool(0) = onum(0) == onum(1);
				break;
			}
			case _AGB_OP_NUM_NEQUAL: {
				dprintf1( "[%ld][%p][%p] num.nequal\n");
				cobjectStack--;
				obool(0) = onum(0) != onum(1);
				break;
			}
			case _AGB_OP_NUM_GT: {
				dprintf1( "[%ld][%p][%p] num.gt\n");
				cobjectStack--;
				obool(0) = onum(0) > onum(1);
				break;
			}
			case _AGB_OP_NUM_EGT: {
				dprintf1( "[%ld][%p][%p] num.egt\n");
				cobjectStack--;
				obool(0) = onum(0) >= onum(1);
				break;
			}
			case _AGB_OP_NUM_LT: {
				dprintf1( "[%ld][%p][%p] num.lt\n");
				cobjectStack--;
				obool(0) = onum(0) < onum(1);
				break;
			}
			case _AGB_OP_NUM_ELT: {
				dprintf1( "[%ld][%p][%p] num.elt\n");
				cobjectStack--;
				obool(0) = onum(0) <= onum(1);
				break;
			}
			case _AGB_OP_NUM_TO_STR: {
				dprintf1( "[%ld][%p][%p] num.to.str\n");
				ostr(0) = agb_library_string_from_number(thread, onum(0));
				break;
			}
			case _AGB_OP_NUM_MOD: {
				dprintf1( "[%ld][%p][%p] num.mod\n");
				cobjectStack--;
				onum(0) = (int) onum(0) % (int) onum(1);
				break;
			}
			case _AGB_OP_NUM_AND_BIT: {
				dprintf1( "[%ld][%p][%p] num.and.bit\n");
				cosdec();
				onum(0) = ((uint64_t) onum(0)) & ((uint64_t) onum(1));
				break;
			}
			case _AGB_OP_NUM_OR_BIT: {
				dprintf1( "[%ld][%p][%p] num.or.bit\n");
				cosdec();
				onum(0) = ((uint64_t) onum(0)) | ((uint64_t) onum(1));
				break;
			}
			case _AGB_OP_NUM_SHIFT_LEFT: {
				dprintf1( "[%ld][%p][%p] num.shift.left\n");
				cosdec();
				onum(0) = ((uint64_t) onum(0)) << ((uint64_t) onum(1));
				break;
			}
			case _AGB_OP_NUM_SHIFT_RIGHT: {
				dprintf1( "[%ld][%p][%p] num.shift.right\n");
				cosdec();
				onum(0) = ((uint64_t) onum(0)) >> ((uint64_t) onum(1));
				break;
			}
			case _AGB_OP_NUM_TO_INT: {
				dprintf1( "[%ld][%p][%p] num.to.int\n");
				onum(0) = (uint64_t) onum(0);
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_STR: {
			switch (*(pc++)) {
			case _AGB_OP_STR_LEN: {
				dprintf1( "[%ld][%p][%p] str.len\n");
				onum(0) = agb_library_string_length(oobj(0));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_SUM: {
				dprintf1( "[%ld][%p][%p] str.sum\n");
				oobj(-1) = agb_library_string_concat(thread, oobj(-1), oobj(0));
				cosdec();
				break;
			}
			case _AGB_OP_STR_EQUAL: {
				dprintf1( "[%ld][%p][%p] str.equal\n");
				cobjectStack--;
				obool(0) = agb_library_string_equal(oobj(0), oobj(1));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_NEQUAL: {
				dprintf1( "[%ld][%p][%p] str.notequal\n");
				cobjectStack--;
				obool(0) = !agb_library_string_equal(oobj(0), oobj(1));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_GT: {
				dprintf1( "[%ld][%p][%p] str.gt\n");
				cobjectStack--;
				obool(0) = agb_library_string_compare(oobj(0), oobj(1)) > 0;
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_EGT: {
				dprintf1( "[%ld][%p][%p] str.egt\n");
				cosdec();
				obool(0) = agb_library_string_compare(oobj(0), oobj(1)) >= 0;
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_LT: {
				dprintf1( "[%ld][%p][%p] str.lt\n");
				cosdec();
				obool(0) = agb_library_string_compare(oobj(0), oobj(1)) < 0;
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_ELT: {
				dprintf1("[%ld][%p][%p] str.elt\n");
				cosdec();
				obool(0) = agb_library_string_compare(oobj(0), oobj(1)) <= 0;
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_SUBSTRING: {
				dprintf1( "[%ld][%p][%p] str.substring\n");
				auxobj = agb_library_string_substring(thread, oobj(-2), onum(-1), onum(0));
				cosdecn(2);
				oobj(0) = auxobj;
				if (!oobj(0)) {
					printf("StringException\n");
					return EXIT_FAILURE;
				}
				break;
			}
			case _AGB_OP_STR_CODE_TO_CHAR: {
				dprintf1( "[%ld][%p][%p] str.code_to_char\n");
				oobj(0) = agb_library_string_code_to_char(thread, onum(0));
				break;
			}
			case _AGB_OP_STR_CHAR_TO_CODE: {
				dprintf1( "[%ld][%p][%p] str.char_to_code\n");
				onum(0) = agb_library_string_char_to_code(oobj(0));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_TRIM: {
				dprintf1( "[%ld][%p][%p] str.trim\n");
				oobj(0) = agb_library_string_trim(thread, oobj(0));
				break;
			}
			case _AGB_OP_STR_CHAR_AT: {
				dprintf1( "[%ld][%p][%p] str.charat\n");
				cosdec();
				onum(0) = agb_library_string_charat(oobj(0), onum(1));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_STR_START_WITH: {
				dprintf1( "[%ld][%p][%p] str.startwith\n");
				auxnum = agb_library_string_startwith(oobj(-1), oobj(0));
				cosdec();
				oobj(0) = 0;
				onum(0) = auxnum;
				break;
			}
			case _AGB_OP_STR_END_WITH: {
				dprintf1( "[%ld][%p][%p] str.endwith\n");
				auxnum = agb_library_string_endwith(oobj(-1), oobj(0));
				cosdec();
				oobj(0) = 0;
				onum(0) = auxnum;
				break;
			}
			case _AGB_OP_STR_INDEX_OF: {
				dprintf1( "[%ld][%p][%p] str.indexof\n");
				auxnum = agb_library_string_indexof(oobj(-1), oobj(0), 1);
				cosdec();
				oobj(0) = 0;
				onum(0) = auxnum;
				break;
			}
			case _AGB_OP_STR_INDEX_OF_N: {
				dprintf1( "[%ld][%p][%p] str.indexof.begin\n");
				auxnum = agb_library_string_indexof(oobj(-2), oobj(-1), onum(0));
				cosdecn(2);
				oobj(0) = 0;
				onum(0) = auxnum;
				break;
			}
			case _AGB_OP_STR_LAST_INDEX_OF: {
				dprintf1( "[%ld][%p][%p] str.lastindexof\n");
				auxnum = agb_library_string_lastindexof(oobj(-1), oobj(0));
				cosdec();
				oobj(0) = 0;
				onum(0) = auxnum;
				break;
			}
			case _AGB_OP_STR_REPLACE: {
				dprintf1( "[%ld][%p][%p] str.replace\n");
				auxobj = agb_library_string_replace(thread, oobj(-2), oobj(-1), oobj(0));
				cosdecn(2);
				oobj(0) = auxobj;
				break;
			}
			case _AGB_OP_STR_TO_LOWER_CASE: {
				dprintf1( "[%ld][%p][%p] str.tolowercase\n");
				oobj(0) = agb_library_string_tolowercase(thread, oobj(0));
				break;
			}
			case _AGB_OP_STR_TO_UPPER_CASE: {
				dprintf1( "[%ld][%p][%p] str.touppercase\n");
				oobj(0) = agb_library_string_touppercase(thread, oobj(0));
				break;
			}
			case _AGB_OP_STR_BASE64_ENCODE: {
				dprintf1( "[%ld][%p][%p] str.base64.encode\n");
				ostr(0) = agb_library_string_base64_encode(thread, ostr(0));
				break;
			}
			case _AGB_OP_STR_BASE64_DECODE: {
				dprintf1( "[%ld][%p][%p] str.base64.decode\n");
				ostr(0) = agb_library_string_base64_decode(thread, ostr(0));
				break;
			}
			case _AGB_OP_STR_UTF_ENCODE: {
				dprintf1( "[%ld][%p][%p] str.utf.encode\n");
				ostr(0) = agb_library_string_utf_encode(thread, ostr(0));
				break;
			}
			case _AGB_OP_STR_UTF_DECODE: {
				dprintf1( "[%ld][%p][%p] str.utf.decode\n");
				ostr(0) = agb_library_string_utf_decode(thread, ostr(0));
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_THROW: {
			switch (*(pc++)) {
			case _AGB_OP_THROW_TRY: {
				dprintf2( "[%ld][%p][%p] throw.try %d\n");
				cthrow++;
				cthrow->cos = cobjectStack;
				cthrow->pc = bytecodes + *pc++;
				cthrow->stack = cpcStack;
				break;
			}
			case _AGB_OP_THROW_TRUE: {
				cthrow--;
				dprintf1( "[%ld][%p][%p] throw.true\n");
				break;
			}
			case _AGB_OP_THROW_FALSE: {
				dprintf1( "[%ld][%p][%p] throw.false\n");
				if (cthrow == throw) {
					printf("?Exception\n");
					return EXIT_FAILURE;
				}
				cthrow->cos[1].data = cobjectStack->data;
				cobjectStack = cthrow->cos + 1;
				pc = cthrow->pc;
				cpcStack = cthrow->stack;
				cthrow--;
				break;
			}
			case _AGB_OP_THROW_JUMP: {
				dprintf1( "[%ld][%p][%p] throw.jump\n");
				auxobj = cobjectStack->data;
				if (auxobj && cast[auxobj->classid * classeSize + *pc++]) {
					pc = bytecodes + *pc;
				} else {
					pc++;
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_ARRAY: {
			switch (*(pc++)) {
			case _AGB_OP_ARRAY_NEW: {
				dprintf1( "[%ld][%p][%p] array.new\n");
				oobj(0) = agb_library_array_new(thread, cobjectStack, cobjectStack - objectStack, onum(0));
				break;
			}
			case _AGB_OP_ARRAY_LEN: {
				dprintf1( "[%ld][%p][%p] array.len\n");
				onum(0) = agb_library_array_size(oarray(0));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_ARRAY_ADD: {
				dprintf1( "[%ld][%p][%p] array.add\n");
				agb_library_array_add(thread, cobjectStack, cobjectStack - objectStack, oarray(-1), oobj ( 0 ));
				cosdec();
				break;
			}
			case _AGB_OP_ARRAY_INSERT: {
				dprintf1( "[%ld][%p][%p] array.insert\n");
				agb_library_array_insert(thread, cobjectStack, cobjectStack - objectStack, oarray(-2), onum(-1),
						oobj( 0 ));
				cosdecn(2);
				break;
			}
			case _AGB_OP_ARRAY_GET: {
				dprintf1( "[%ld][%p][%p] array.get\n");
				cosdec();
				oobj(0) = agb_library_array_get(oarray(0), onum(1));
				break;
			}
			case _AGB_OP_ARRAY_SET: {
				dprintf1( "[%ld][%p][%p] array.set\n");
				cosdecn(2);
				oobj(0) = agb_library_array_set(oarray ( 0 ), onum(1), oobj(2));
				break;
			}
			case _AGB_OP_ARRAY_REM: {
				dprintf1( "[%ld][%p][%p] array.rem\n");
				cosdec();
				oobj(0) = agb_library_array_remove(oarray ( 0 ), onum(1));
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_NUMS: {
			switch (*(pc++)) {
			case _AGB_OP_NUMS_NEW: {
				dprintf1( "[%ld][%p][%p] nums.new\n");
				auxint = oint(0);
				auxvoid = agb_memory_alloc(auxint * sizeof(double));
				if (auxvoid) {
					oobj(0) = agb_library_nums_new(thread, (double*) auxvoid, auxint);
				} else {
					oobj(0) = 0;
				}
				break;
			}
			case _AGB_OP_NUMS_SIZE: {
				dprintf1( "[%ld][%p][%p] nums.size\n");
				auxnums = onums(0);
				if (!auxnums) {
					onum(0) = 0;
				} else {
					onum(0) = auxnums->size;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_NUMS_GET: {
				dprintf1( "[%ld][%p][%p] nums.get\n");
				auxnums = onums(-1);
				auxint = oint(0);
				cosdec();
				if (auxint < 1 || auxint > auxnums->size) {
					onum(0) = 0;
				} else {
					onum(0) = auxnums->array[auxint - 1];
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_NUMS_SET: {
				dprintf1( "[%ld][%p][%p] nums.set\n");
				auxnums = onums(-2);
				auxint = oint(-1);
				auxnum = onum(0);
				cosdecn( 2);
				if (auxint < 1 || auxint > auxnums->size) {
					onum(0) = 0;
				} else {
					onum(0) = auxnums->array[auxint - 1];
					auxnums->array[auxint - 1] = auxnum;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_NUMS_SETS: {
				dprintf1( "[%ld][%p][%p] nums.sets\n");
				auxnums = onums(-3);
				auxint = oint(-2);
				auxint2 = oint(-1);
				auxnum = onum(0);
				cosdecn( 3);
				if (auxint < 1) {
					auxint = 1;
				}
				if (auxint2 > auxnums->size) {
					auxint2 = auxnums->size;
				}
				if (auxint <= auxint2) {
					if (auxint <= auxnums->size) {
						int n, size = auxint2 - auxint;
						double* aux = auxnums->array + auxint - 1;
						for (n = 0; n <= size; n++) {
							*aux++ = auxnum;
						}
					}
				}
				break;
			}
			case _AGB_OP_NUMS_COPY: {
				dprintf1( "[%ld][%p][%p] nums.copy\n");
				auxnums = onums(-4);
				auxint = oint(-3);
				auxint2 = oint(-2);
				auxnums2 = onums(-1);
				auxint3 = oint(0);
				cosdecn( 4);
				if (auxint3 < 1) {
					auxint += 1 - auxint3;
					auxint3 = 1;
				}
				if (auxint < 1) {
					auxint = 1;
				}
				if (auxint2 > auxnums->size) {
					auxint2 = auxnums->size;
				}
				if (auxint <= auxint2) {
					if (auxint <= auxnums->size) {
						size = auxint2 - auxint + 1;
						size2 = auxnums2->size - auxint3 + 1;
						size = size < size2 ? size : size2;
						if (size > 0) {
							double* src = auxnums->array + auxint - 1;
							double* dst = auxnums2->array + auxint3 - 1;
							for (n = 0; n < size; n++) {
								*dst++ = *src++;
							}
						}
					}
				}
				break;
			}
			case _AGB_OP_NUMS_EQUAL: {
				dprintf1( "[%ld][%p][%p] nums.equal\n");
				auxnums = onums(-1);
				auxnums2 = onums(0);
				cosdecn( 1);
				if (!auxnums) {
					obool(0) = auxnums2 == 0;
				} else if (!auxnums2) {
					obool(0) = 0;
				} else if (auxnums->size != auxnums2->size) {
					obool(0) = 0;
				} else {
					auxint = auxnums->size;
					double* aux = auxnums->array;
					double* aux2 = auxnums2->array;
					obool(0) = 1;
					for (n = 0; n <= auxint; n++) {
						if (*aux2++ != *aux++) {
							obool(0) = 0;
							break;
						}
					}
					oobj(0) = 0;
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_BOOLS: {
			switch (*(pc++)) {
			case _AGB_OP_BOOLS_NEW: {
				dprintf1( "[%ld][%p][%p] bools.new\n");
				auxint = oint(0);
				auxvoid = agb_memory_alloc(auxint * sizeof(unsigned char));
				if (auxvoid) {
					oobj(0) = agb_library_bools_new(thread, (unsigned char*) auxvoid, auxint);
				} else {
					oobj(0) = 0;
				}
				break;
			}
			case _AGB_OP_BOOLS_SIZE: {
				dprintf1( "[%ld][%p][%p] bools.size\n");
				auxbools = obools(0);
				if (!auxbools) {
					onum(0) = 0;
				} else {
					onum(0) = auxbools->size;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_BOOLS_GET: {
				dprintf1( "[%ld][%p][%p] bools.get\n");
				auxbools = obools(-1);
				auxint = oint(0);
				cosdec();
				if (auxint < 1 || auxint > auxbools->size) {
					obool(0) = 0;
				} else {
					obool(0) = auxbools->array[auxint - 1];
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_BOOLS_SET: {
				dprintf1( "[%ld][%p][%p] bools.set\n");
				auxbools = obools(-2);
				auxint = oint(-1);
				auxbool = obool(0) != 0;
				cosdecn( 2);
				if (auxint < 1 || auxint > auxbools->size) {
					obool(0) = 0;
				} else {
					obool(0) = auxbools->array[auxint - 1];
					auxbools->array[auxint - 1] = auxbool;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_BOOLS_SETS: {
				dprintf1( "[%ld][%p][%p] bools.sets\n");
				auxbools = obools(-3);
				auxint = oint(-2);
				auxint2 = oint(-1);
				auxbool = obool(0) != 0;
				cosdecn( 3);
				if (auxint < 1) {
					auxint = 1;
				}
				if (auxint2 > auxbools->size) {
					auxint2 = auxbools->size;
				}
				if (auxint <= auxint2) {
					if (auxint <= auxbools->size) {
						int n, size = auxint2 - auxint;
						unsigned char* aux = auxbools->array + auxint - 1;
						for (n = 0; n <= size; n++) {
							*aux++ = auxbool;
						}
					}
				}
				break;
			}
			case _AGB_OP_BOOLS_COPY: {
				dprintf1( "[%ld][%p][%p] bools.copy\n");
				auxbools = obools(-4);
				auxint = oint(-3);
				auxint2 = oint(-2);
				auxbools2 = obools(-1);
				auxint3 = oint(0);
				cosdecn( 4);
				if (auxint3 < 1) {
					auxint += 1 - auxint3;
					auxint3 = 1;
				}
				if (auxint < 1) {
					auxint = 1;
				}
				if (auxint2 > auxbools->size) {
					auxint2 = auxbools->size;
				}
				if (auxint <= auxint2) {
					if (auxint <= auxbools->size) {
						size = auxint2 - auxint + 1;
						size2 = auxbools2->size - auxint3 + 1;
						size = size < size2 ? size : size2;
						unsigned char* aux = auxbools->array + auxint - 1;
						unsigned char* aux2 = auxbools2->array + auxint3 - 1;
						for (n = 0; n < size; n++) {
							*aux2++ = *aux++;
						}
					}
				}
				break;
			}
			case _AGB_OP_BOOLS_EQUAL: {
				dprintf1( "[%ld][%p][%p] bools.equal\n");
				auxbools = obools(-1);
				auxbools2 = obools(0);
				cosdecn( 1);
				if (!auxbools) {
					obool(0) = auxbools2 == 0;
				} else if (!auxbools2) {
					obool(0) = 0;
				} else if (auxbools->size != auxbools2->size) {
					obool(0) = 0;
				} else {
					auxint = auxbools->size;
					unsigned char* aux = auxbools->array;
					unsigned char* aux2 = auxbools2->array;
					obool(0) = 1;
					for (n = 0; n <= auxint; n++) {
						if (*aux2++ != *aux++) {
							obool(0) = 0;
							break;
						}
					}
					oobj(0) = 0;
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_BYTES: {
			switch (*(pc++)) {
			case _AGB_OP_BYTES_NEW: {
				dprintf1( "[%ld][%p][%p] bytes.new\n");
				auxint = oint(0);
				auxvoid = agb_memory_alloc(auxint * sizeof(unsigned char));
				if (auxvoid) {
					oobj(0) = agb_library_bytes_new(thread, (unsigned char*) auxvoid, auxint);
				} else {
					oobj(0) = 0;
				}
				break;
			}
			case _AGB_OP_BYTES_SIZE: {
				dprintf1( "[%ld][%p][%p] bytes.size\n");
				auxbools = obools(0);
				if (!auxbools) {
					onum(0) = 0;
				} else {
					onum(0) = auxbools->size;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_BYTES_GET: {
				dprintf1( "[%ld][%p][%p] bytes.get\n");
				auxbools = obools(-1);
				auxint = oint(0);
				cosdec();
				if (auxint < 1 || auxint > auxbools->size) {
					onum(0) = 0;
				} else {
					onum(0) = auxbools->array[auxint - 1];
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_BYTES_SET: {
				dprintf1( "[%ld][%p][%p] bytes.set\n");
				auxbools = obools(-2);
				auxint = oint(-1);
				auxbool = onum(0);
				cosdecn( 2);
				if (auxint < 1 || auxint > auxbools->size) {
					onum(0) = 0;
				} else {
					onum(0) = auxbools->array[auxint - 1];
					auxbools->array[auxint - 1] = auxbool;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_BYTES_SETS: {
				dprintf1( "[%ld][%p][%p] bytes.sets\n");
				auxbools = obools(-3);
				auxint = oint(-2);
				auxint2 = oint(-1);
				auxbool = onum(0);
				cosdecn( 3);
				if (auxint < 1) {
					auxint = 1;
				}
				if (auxint2 > auxbools->size) {
					auxint2 = auxbools->size;
				}
				if (auxint <= auxint2) {
					if (auxint <= auxbools->size) {
						int n, size = auxint2 - auxint;
						unsigned char* aux = auxbools->array + auxint - 1;
						for (n = 0; n <= size; n++) {
							*aux++ = auxbool;
						}
					}
				}
				break;
			}
			case _AGB_OP_BYTES_COPY: {
				dprintf1( "[%ld][%p][%p] bytes.copy\n");
				auxbools = obools(-4);
				auxint = oint(-3);
				auxint2 = oint(-2);
				auxbools2 = obools(-1);
				auxint3 = oint(0);
				cosdecn( 4);
				if (auxint3 < 1) {
					auxint += 1 - auxint3;
					auxint3 = 1;
				}
				if (auxint < 1) {
					auxint = 1;
				}
				if (auxint2 > auxbools->size) {
					auxint2 = auxbools->size;
				}
				if (auxint <= auxint2) {
					if (auxint <= auxbools->size) {
						size = auxint2 - auxint + 1;
						size2 = auxbools2->size - auxint3 + 1;
						size = size < size2 ? size : size2;
						unsigned char* aux = auxbools->array + auxint - 1;
						unsigned char* aux2 = auxbools2->array + auxint3 - 1;
						for (n = 0; n < size; n++) {
							*aux2++ = *aux++;
						}
					}
				}
				break;
			}
			case _AGB_OP_BYTES_EQUAL: {
				dprintf1( "[%ld][%p][%p] bytes.equal\n");
				auxbools = obools(-1);
				auxbools2 = obools(0);
				cosdecn( 1);
				if (!auxbools) {
					obool(0) = auxbools2 == 0;
				} else if (!auxbools2) {
					obool(0) = 0;
				} else if (auxbools->size != auxbools2->size) {
					obool(0) = 0;
				} else {
					auxint = auxbools->size;
					unsigned char* aux = auxbools->array;
					unsigned char* aux2 = auxbools2->array;
					obool(0) = 1;
					for (n = 0; n <= auxint; n++) {
						if (*aux2++ != *aux++) {
							obool(0) = 0;
							break;
						}
					}
					oobj(0) = 0;
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_UINT2D: {
			switch (*(pc++)) {
			case _AGB_OP_UINT2D_NEW: {
				dprintf1( "[%ld][%p][%p] uint2d.new\n");
				auxint = oint(-1); // Lines
				auxint2 = oint(0); // Columns
				cosdecn( 1);
				if (auxint < 0) {
					auxint = 0;
				}
				if (auxint2 < 0) {
					auxint2 = 0;
				}
				auxuint2d = agb_memory_alloc(sizeof(agb_object_uint2d_t));
				if (!auxuint2d) {
					printf("OutOfMemoryException\n");
					return EXIT_FAILURE;
				}
				auxuint2d->array = agb_memory_alloc(auxint * auxint2 * sizeof(unsigned int));
				if (!auxuint2d->array && auxint * auxint2 > 0) {
					printf("OutOfMemoryException\n");
					return EXIT_FAILURE;
				}
				auxuint2d->cols = auxint2;
				auxuint2d->lins = auxint;
				auxuint2d->classid = AGB_LIBRARY_UINT2D_ID;
				if (!agb_thread_malloc(thread, auxuint2d)) {
					agb_object_free((agb_object_t*) auxuint2d);
					printf("OutOfMemoryException\n");
					return EXIT_FAILURE;
				}
				oobj(0) = (agb_object_t*) auxuint2d;
				break;
			}
			case _AGB_OP_UINT2D_LINS: {
				dprintf1( "[%ld][%p][%p] uint2d.lins\n");
				auxuint2d = ouint2d(0);
				if (!auxuint2d) {
					onum(0) = 0;
				} else {
					onum(0) = auxuint2d->lins;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_UINT2D_COLS: {
				dprintf1( "[%ld][%p][%p] uint2d.size\n");
				auxuint2d = ouint2d(0);
				if (!auxuint2d) {
					onum(0) = 0;
				} else {
					onum(0) = auxuint2d->cols;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_UINT2D_GET: {
				dprintf1( "[%ld][%p][%p] uint2d.get\n");
				auxuint2d = ouint2d(-2); // Matrix
				auxint = oint(-1); // Line
				auxint2 = oint(0); // Column
				cosdecn( 2);
				if (auxint < 1 || auxint > auxuint2d->lins) {
					onum(0) = 0;
				} else if (auxint2 < 1 || auxint2 > auxuint2d->cols) {
					onum(0) = 0;
				} else {
					onum(0) = auxuint2d->array[(auxint - 1) * auxuint2d->cols + (auxint2 - 1)];
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_UINT2D_SET: {
				dprintf1( "[%ld][%p][%p] uint2d.set\n");
				auxuint2d = ouint2d(-3); // Matrix
				auxint = oint(-2); // Line
				auxint2 = oint(-1); // Column
				auxnum = onum(0); // Valor
				cosdecn( 3);
				if (auxnum > INT_MAX) {
					auxnum = INT_MAX;
				} else if (auxnum < 0) {
					auxnum = 0;
				}
				if (auxint < 1 || auxint > auxuint2d->lins) {
					onum(0) = 0;
				} else if (auxint2 < 1 || auxint2 > auxuint2d->cols) {
					onum(0) = 0;
				} else {
					auxint = (auxint - 1) * auxuint2d->cols + (auxint2 - 1);
					onum(0) = auxuint2d->array[auxint];
					auxuint2d->array[auxint] = auxnum;
				}
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_UINT2D_SETS: {
				dprintf1( "[%ld][%p][%p] uint2d.sets\n");
				auxuint2d = ouint2d(-5); // Matrix
				auxint = oint(-4); // Begin Line
				auxint2 = oint(-3); // End Line
				auxint3 = oint(-2); // Begin Column
				auxint4 = oint(-1); // End Column
				auxnum = onum(0); // Valor
				cosdecn( 5);
				if (auxnum > INT_MAX) {
					auxnum = INT_MAX;
				} else if (auxnum < 0) {
					auxnum = 0;
				}
				if (auxint < 1) {
					auxint = 1;
				}
				if (auxint2 > auxuint2d->lins) {
					auxint2 = auxuint2d->lins;
				}
				if (auxint3 < 1) {
					auxint3 = 1;
				}
				if (auxint4 > auxuint2d->cols) {
					auxint4 = auxuint2d->cols;
				}
				if (auxint <= auxint2 && auxint3 <= auxint4) {
					auxint--;
					auxint2--;
					auxint3--;
					auxint4--;
					size = auxint2 - auxint + 1;
					size2 = auxint4 - auxint3 + 1;
					if (size > 0 && size2 > 0) {
						unsigned int* aux = auxuint2d->array + auxint * auxuint2d->cols + auxint3;
						for (n = 0; n < size; n++) {
							for (m = 0; m < size2; m++) {
								*aux++ = auxnum;
							}
							aux += (auxuint2d->cols - (auxint4 + 1)) + auxint3;
						}
					}
				}
				break;
			}
			case _AGB_OP_UINT2D_COPY: {
				dprintf1( "[%ld][%p][%p] uint2d.copy\n");
				auxuint2d = ouint2d(-7); // Dest Matrix
				auxint = oint(-6); // Line Dest
				auxint2 = oint(-5); // Column Dest
				auxuint2d2 = ouint2d(-4); // Source
				auxint3 = oint(-3); // Begin Source Line
				auxint4 = oint(-2); // End Source Line
				auxint5 = oint(-1); // Begin Source Column
				auxint6 = oint(0); // End Source Column
				cosdecn( 7);
				if (auxint3 < 1) {
					auxint3 = 1;
				}
				if (auxint4 > auxuint2d2->lins) {
					auxint4 = auxuint2d2->lins;
				}
				if (auxint5 < 1) {
					auxint5 = 1;
				}
				if (auxint6 > auxuint2d2->cols) {
					auxint6 = auxuint2d2->cols;
				}
				if (auxint < 1) {
					auxint3 += 1 - auxint;
					auxint = 1;
				}
				if (auxint2 < 1) {
					auxint5 += 1 - auxint2;
					auxint2 = 1;
				}
				if (auxint3 <= auxint4 && auxint5 <= auxint6) {
					if (auxint <= auxuint2d->lins && auxint2 <= auxuint2d->cols) {
						auxint--;
						auxint2--;
						auxint3--;
						auxint4--;
						auxint5--;
						auxint6--;
						size = fmin(auxint4 - auxint3 + 1, auxuint2d->lins - auxint);
						size2 = fmin(auxint6 - auxint5 + 1, auxuint2d->cols - auxint2);
						if (size > 0 && size2 > 0) {
							for (n = 0; n < size; n++) {
								unsigned int* src = auxuint2d2->array + (n + auxint3) * auxuint2d2->cols + auxint5;
								unsigned int* dst = auxuint2d->array + (n + auxint) * auxuint2d->cols + auxint2;
								for (m = 0; m < size2; m++) {
									*dst++ = *src++;
								}
							}
						}
					}
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_SDL: {
			auxint = *pc++;
			switch (auxint) {
			case _AGB_OP_SDL_ERROR: {
				dprintf1( "[%ld][%p][%p] sdl.error\n");
				oobj(1) = 0;
				cosinc()
				break;
			}
			case _AGB_OP_SDL_INIT: {
				dprintf1( "[%ld][%p][%p] sdl.init\n");
				oobj(1) = 0;
				cosinc();
				obool(0) = agb_library_sdl_init(thread);
				break;
			}
			case _AGB_OP_SDL_QUIT: {
				dprintf1( "[%ld][%p][%p] sdl.quit\n");
				agb_library_sdl_quit(thread);
				oobj(1) = 0;
				cosinc();
				break;
			}
			case _AGB_OP_SDL_VIDEO: {
				dprintf1( "[%ld][%p][%p] sdl.video\n");
				oobj(1) = 0;
				cosinc();
				obool(0) = agb_library_sdl_video(thread, 0, 0);
				break;
			}
			case _AGB_OP_SDL_CONSTANT: {
				dprintf1( "[%ld][%p][%p] sdl.constant\n");
				onum(0) = agb_library_sdl_constant(thread, onum(0));
				break;
			}
			case _AGB_OP_SDL_EVENT_POOL: {
				dprintf1( "[%ld][%p][%p] sdl.event.pool\n");
				oobj(1) = 0;
				cosinc();
				obool(0) = agb_library_sdl_pool_event(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_WAIT: {
				dprintf1( "[%ld][%p][%p] sdl.event.wait\n");
				agb_library_sdl_wait_event(thread);
				oobj(1) = 0;
				cosinc();
				break;
			}
			case _AGB_OP_SDL_EVENT_TYPE: {
				dprintf1( "[%ld][%p][%p] sdl.event.type\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_type_event(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_KEYCODE: {
				dprintf1( "[%ld][%p][%p] sdl.event.keycode\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_keycode_event(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_KEYCHAR: {
				dprintf1( "[%ld][%p][%p] sdl.event.keychar\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_keychar_event(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_USERCODE: {
				dprintf1( "[%ld][%p][%p] sdl.event.usercode\n");
				onum(1) = agb_library_sdl_usercode(thread);
				oobj(1) = 0;
				cosinc();
				break;
			}
			case _AGB_OP_SDL_EVENT_MOUSE_MOTION_X: {
				dprintf1( "[%ld][%p][%p] sdl.event.mouse.motion.x\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_mouse_motion_x(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_MOUSE_MOTION_Y: {
				dprintf1( "[%ld][%p][%p] sdl.event.mouse.motion.y\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_mouse_motion_y(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_MOUSE_ACTION_X: {
				dprintf1( "[%ld][%p][%p] sdl.event.mouse.action.x\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_mouse_action_x(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_MOUSE_ACTION_Y: {
				dprintf1( "[%ld][%p][%p] sdl.event.mouse.action.y\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_mouse_action_y(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON: {
				dprintf1( "[%ld][%p][%p] sdl.event.mouse.action.button\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_mouse_action_button(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_MOUSE_WHEEL_X: {
				dprintf1( "[%ld][%p][%p] sdl.event.mouse.wheel.x\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_mouse_wheel_x(thread);
				break;
			}
			case _AGB_OP_SDL_EVENT_MOUSE_WHEEL_Y: {
				dprintf1( "[%ld][%p][%p] sdl.event.mouse.wheel.y\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_mouse_wheel_y(thread);
				break;
			}
			case _AGB_OP_SDL_LOCK: {
				dprintf1( "[%ld][%p][%p] sdl.lock\n");
				oobj(1) = 0;
				cosinc();
				obool(0) = agb_library_sdl_lock(thread);
				break;
			}
			case _AGB_OP_SDL_UNLOCK: {
				dprintf1( "[%ld][%p][%p] sdl.unlock\n");
				agb_library_sdl_unlock(thread);
				oobj(1) = 0;
				cosinc();
				break;
			}
			case _AGB_OP_SDL_UPDATE: {
				dprintf1( "[%ld][%p][%p] sdl.update\n");
				agb_library_sdl_update(thread, 0, 0);
				oobj(1) = 0;
				cosinc();
				break;
			}
			case _AGB_OP_SDL_TICK: {
				dprintf1( "[%ld][%p][%p] sdl.tick\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_tick(thread);
				break;
			}
			case _AGB_OP_SDL_DELAY: {
				dprintf1( "[%ld][%p][%p] sdl.delay\n");
				agb_library_sdl_delay(onum(0));
				oobj(1) = 0;
				break;
			}
			case _AGB_OP_SDL_SCREEN_WIDTH: {
				dprintf1( "[%ld][%p][%p] sdl.screen.width\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_screen_width(thread);
				break;
			}
			case _AGB_OP_SDL_SCREEN_HEIGHT: {
				dprintf1( "[%ld][%p][%p] sdl.screen.height\n");
				oobj(1) = 0;
				cosinc();
				onum(0) = agb_library_sdl_screen_height(thread);
				break;
			}
			case _AGB_OP_SDL_DRAW_RECT: {
				dprintf1( "[%ld][%p][%p] sdl.draw.rect\n");
				agb_library_sdl_drawrect(thread, onum(-4), onum(-3), onum(-2), onum(-1), onum(0));
				cosdecn( 4);
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_SDL_FILL_RECT: {
				dprintf1( "[%ld][%p][%p] sdl.fill.rect\n");
				agb_library_sdl_fillrect(thread, onum(-4), onum(-3), onum(-2), onum(-1), onum(0));
				cosdecn( 4);
				oobj(0) = 0;
				break;
			}
//						case _AGB_OP_SDL_DRAW_CIRCLE : {
//							dprintf1( "[%ld][%p][%p] sdl.draw.circle\n" );
//							agb_library_sdl_drawcircle ( onum(-9) , onum(-8) , onum(-7) , onum(-6) , onum(-5) , onum(-4) , onum(-3) , onum(-2) , onum(-1) , onum(0) ) ;
//							cosdecn( 9 );
//							oobj(0) = 0 ;
//							break ;
//						}
			case _AGB_OP_SDL_DRAW_STRING: {
				dprintf1( "[%ld][%p][%p] sdl.draw.string\n");
				auxobj = oobj(-1);
				if (auxobj) {
					agb_library_sdl_drawstring(thread, onum(-3), onum(-2), agb_library_string_chars(auxobj), onum(0));
				}
				cosdecn( 3);
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_SDL_FONT_WIDTH: {
				dprintf1( "[%ld][%p][%p] sdl.font.width\n");
				onum(0) = agb_library_sdl_font_width(thread, agb_library_string_chars(oobj(0)));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_SDL_FONT_HEIGHT: {
				dprintf1( "[%ld][%p][%p] sdl.font.height\n");
				onum(0) = agb_library_sdl_font_height(thread, agb_library_string_chars(oobj(0)));
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_SDL_PUSH_GRAPHIC: {
				dprintf1( "[%ld][%p][%p] sdl.push_graphic\n");
				agb_library_sdl_push_graphic(thread, onum(-3), onum(-2), onum(-1), onum(0));
				cosdecn( 3);
				oobj(0) = 0;
				break;
			}
			case _AGB_OP_SDL_POP_GRAPHIC: {
				dprintf1( "[%ld][%p][%p] sdl.pop_graphic\n");
				agb_library_sdl_pop_graphic(thread, onum(-1), onum(0));
				cosdecn( 1);
				oobj(0) = 0;
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_CONSOLE: {
			switch (*(pc++)) {
			case _AGB_OP_CONSOLE_PRINT_STR: {
				dprintf1( "[%ld][%p][%p] log.prints\n");
				if (!oobj(0)) {
					wprintf(L"null");
				} else {
					wprintf(L"%ls", agb_library_string_chars(oobj(0)));
				}
				break;
			}
			case _AGB_OP_CONSOLE_PRINT_NUM: {
				dprintf1( "[%ld][%p][%p] log.printn\n");
				agb_library_number_print(onum(0));
				break;
			}
			case _AGB_OP_CONSOLE_PRINT_BOOL: {
				dprintf1( "[%ld][%p][%p] log.printb\n");
				if (obool(0)) {
					printf("true");
				} else {
					printf("false");
				}
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_NET: {
			switch (*(pc++)) {
			case _AGB_OP_NET_REQUEST: {
				dprintf1( "[%ld][%p][%p] net.request\n");
				oobj(-2) = agb_library_net_request(thread, agb_library_string_chars(oobj(-2)), onum(-1),
						agb_library_string_chars(oobj(0)));
				cosdecn(2);
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		case _AGB_GP_VM: {
			switch (*(pc++)) {
			case _AGB_OP_VM_HALF: {
				dprintf1( "[%ld][%p][%p] vm.half\n");
				return onum(0);
				break;
			}
			case _AGB_OP_VM_GC: {
				dprintf1( "[%ld][%p][%p] vm.gc\n");
				agb_vm_gc(thread, cobjectStack, cobjectStack - objectStack);
				cosinc();
				oobj ( 0 ) = 0;
				break;
			}
			case _AGB_OP_VM_ALLOC: {
				dprintf1( "[%ld][%p][%p] vm.alloc\n");
				obool(-2) = agb_library_vm_alloc(thread, agb_library_bytes_to_uchars(oobj(-2)), onum(-1), onum(0));
				cosdecn(2);
				break;
			}
			case _AGB_OP_VM_PAINT: {
				dprintf1( "[%ld][%p][%p] vm.alloc\n");
				agb_library_vm_paint(thread, onum(-2), onum(-1));
				obool(-2) = 1;
				cosdecn( 2);
				break;
			}
			case _AGB_OP_VM_EVENT_KEY: {
				dprintf1( "[%ld][%p][%p] vm.event_key\n");
				agb_library_sdl_push_event_key(thread, onum(-1), onum(0));
				obool(-1) = 1;
				cosdec();
				break;
			}
			case _AGB_OP_VM_EVENT_MOUSE: {
				dprintf1( "[%ld][%p][%p] vm.event_mouse\n");
				agb_library_sdl_push_event_mouse(thread, onum(-2), onum(-1), onum(0));
				obool(-2) = 1;
				cosdecn( 2);
				break;
			}
			case _AGB_OP_VM_EVENT_WHEEL: {
				dprintf1( "[%ld][%p][%p] vm.event_wheel\n");
				agb_library_sdl_push_event_wheel(thread, onum(-4), onum(-3), onum(-2), onum(-1), onum(0));
				obool(-4) = 1;
				cosdecn( 4);
				break;
			}
			default: {
				return EXIT_FAILURE;
			}
			}
			break;
		}
		default: {
			return EXIT_FAILURE;
		}
		}
#ifdef AGB_TRACE
		agb_ref_t* refs = cobjectStack;
		for (n = 0; n < cobjectStack - objectStack; n++) {
			if (refs->data) {
				agb_object_t* object = refs->data;
				if (object->classid < 0) {
					if (object->classid == AGB_LIBRARY_STRING_ID) {
						wprintf(L"\t<string> address: %p, value: %ls\n", address(object),
								agb_library_string_chars(object));
					} else if (object->classid == AGB_LIBRARY_FUNC_ID) {
						wprintf(L"\t<func> address: %p, index: %d, fields: {", address(object),
								(int) agb_object_datai(object, 0).number);
						int m, msize = agb_object_datai(object, 1).number;
						for (m = 0; m < msize; m++) {
							agb_ref_t ref = agb_object_datai(object , m + 2 );
							printf("[%p,%0.0lf]", address(ref.data), ref.number);
							if (m != msize - 1) {
								printf(",");
							}
						}
						wprintf(L"}\n");
					} else if (object->classid == AGB_LIBRARY_ARRAY_ID) {
						wprintf(L"\t<array> address: %p, size: %d, fields: {", address(object),
								agb_library_array_size((agb_object_array_t*) object));
						int m, msize = agb_library_array_size((agb_object_array_t*) object);
						for (m = 0; m < msize; m++) {
							wprintf(L"%p", address(agb_library_array_get ( (agb_object_array_t*) object , m + 1 )));
							if (m != msize - 1) {
								wprintf(L",");
							}
						}
						wprintf(L"}\n");
					} else if (object->classid == AGB_LIBRARY_UINT2D_ID) {
						wprintf(L"\t<uint2d> address: %p\n", address(object));
					}
				} else {
					wprintf(L"\t%ls address: %p, fields: {", vm->classes[object->classid]->name, address(object));
					int m, msize = vm->fields[object->classid];
					for (m = 0; m < msize; m++) {
						agb_ref_t ref = agb_object_datai(object, m);
						wprintf(L"[%p,%0.0lf],", address(ref.data), ref.number);
					}
					wprintf(L"}\n");
				}
			} else {
				wprintf(L"\t<primitive> value: %0.0lf\n", refs->number);
			}
			refs--;
		}
#endif
		// agb_vm_gc ( thread , cobjectStack , cobjectStack - objectStack ) ;
	}
	return EXIT_SUCCESS;
}
