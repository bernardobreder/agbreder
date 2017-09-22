#ifndef AGB
#define AGB

#include <fcntl.h>
#include <stdint.h>
#include <limits.h>
#include <stdio.h>
#include <wchar.h>
#include <locale.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include "agb_so.h"
#include "agb_platform.h"

#ifdef __cplusplus
extern "C" {
#endif

#undef main

typedef double num;

/**
 * Struct of method
 */
typedef struct agb_object_t {
	int classid;
	unsigned char used;
	int nativeid;
	void* native;
} agb_object_t;

typedef struct {
	int classid;
	unsigned char used;
	unsigned int size;
	int* array;
} agb_object_ints_t;

typedef struct {
	int classid;
	unsigned char used;
	unsigned int size;
	double* array;
} agb_object_nums_t;

typedef struct {
	int classid;
	unsigned char used;
	unsigned int size;
	unsigned char* array;
} agb_object_bools_t;

typedef struct {
	int classid;
	unsigned char used;
	unsigned int size;
	unsigned char* array;
} agb_object_bytes_t;

typedef struct {
	int classid;
	unsigned char used;
	unsigned int lins;
	unsigned int cols;
	unsigned int* array;
} agb_object_uint2d_t;

typedef struct {
	int classid;
	unsigned char used;
	double value;
} agb_object_number_t;

typedef struct {
	int classid;
	unsigned char used;
	int nativeid;
	void* native;
} agb_object_string_t;

typedef struct {
	int classid;
	unsigned char used;
	unsigned char flag;
} agb_object_boolean_t;

typedef struct {
	int classid;
	unsigned char used;
	unsigned int max;
	unsigned int size;
	agb_object_t** native;
} agb_object_array_t;

typedef struct {
	int classid;
	unsigned char used;
	void* native;
} agb_object_socket_t;

typedef struct {
	int classid;
	unsigned char used;
} agb_object_func_t;

typedef struct agb_ref_t {
	agb_object_t* data;
	double number;
} agb_ref_t;

/**
 * Struct of virtual machine
 */
typedef struct agb_vm_t agb_vm_t;

typedef struct {
	int size;
	int dataMax;
	int dataSize;
	unsigned char** data;
} agb_segment_t;

/**
 * Struct of Thread
 */
typedef struct agb_thread_t {
	// Referencia para a máquina virtual
	agb_vm_t* vm;
	// Indica quantos objectos estão em uso
	unsigned int memorySize;
	// Indica quantos objectos estão alocados
	unsigned int memoryMax;
	// Vetor de objectos
	agb_object_t** memory;
	// Pilha de Objetos
	agb_ref_t* cobjstack;
	// Indica quantos objetos estão pronto para uma classid
	unsigned int* objectSize;
	// Indica quantos objetos estão alocados para uma classid
	unsigned int* objectMax;
	// Objetos de cada classid
	agb_object_t** objects;
	agb_segment_t* objs;
} agb_thread_t;

/**
 * Struct of class
 */
typedef struct agb_class_t agb_class_t;

/**
 * Struct of field
 */
typedef struct agb_field_t agb_field_t;

/**
 * Struct of method
 */
typedef struct agb_method_t agb_method_t;

/**
 * Struct of method
 */
typedef struct agb_object_field_t agb_object_field_t;

/**
 * Struct of method
 */
typedef struct agb_number_t agb_number_t;

/**
 * Struct of method
 */
typedef struct agb_string_t agb_string_t;

/**
 * Struct of method
 */
typedef struct agb_boolean_t agb_boolean_t;

/**
 * Função de Opcode
 */
typedef agb_object_t* (*agb_func_t)(agb_thread_t*, void*);

/**
 * Retorna a class struct
 */
agb_class_t* agb_class_name(agb_vm_t* vm, wchar_t* name);

/**
 * Retorna a method struct of a class
 */
extern DECLSPEC agb_method_t* agb_method_name(agb_vm_t* vm, agb_class_t* class_def, wchar_t* name, int isStatic);

/**
 * Retorna a method struct of a class
 */
extern DECLSPEC agb_method_t* agb_method_classname(agb_vm_t* vm, wchar_t* classname, wchar_t* methodname, int isStatic);

/**
 * Return the program counter of the method
 */
extern DECLSPEC int agb_method_pc(agb_method_t* method);

/**
 * Create a virtual machine
 */
extern DECLSPEC agb_vm_t* agb_vm_new(unsigned char* buffer);

/**
 * Free the virtual machine
 */
extern DECLSPEC void agb_vm_free(agb_vm_t* vm);

/**
 * Free the thread
 */
extern DECLSPEC void agb_thread_free(agb_thread_t* vm);

/**
 * Create a new Thread
 */
extern DECLSPEC agb_thread_t* agb_thread_new(agb_vm_t* vm);

/**
 * Adiciona um ponteiro na memoria. Retorna 1 se tudo der certo.
 */
int agb_thread_malloc(agb_thread_t* thread, void* data);

/**
 * Carrega o buffer através de um arquivo
 */
unsigned char* agb_file_read(const char* filename, long* size);

/**
 * Carrega o buffer através de um arquivo
 */
extern DECLSPEC unsigned char* agb_base64_read(const char* base64, int* size);

/**
 * Execute the virtual machine initing by a program counter
 */
extern DECLSPEC int agb_vm_execute(agb_thread_t* thread, int pc);

wchar_t* agb_wchar_encode(unsigned char* bytes, long byteSize, long* size);

bool agb_wchar_alloced(unsigned char* bytes, long byteSize, wchar_t* data, long* size);

unsigned char* agb_wchar_decode(wchar_t* wchars, long wcharsSize, long* byteSize);

wchar_t* agb_wchar_dup(wchar_t* value, long size);

char* agb_wchar_to_chars(wchar_t* text);

void agb_wchar_copy_to_chars(wchar_t* text, char* value);

/**
 * Executa os testes da máquina virtual
 */
void agb_test();

#ifdef __cplusplus
}
#endif

#endif
