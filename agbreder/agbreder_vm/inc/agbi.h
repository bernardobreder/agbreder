#ifndef AGBI
#define AGBI

#include <assert.h>
#include "agb_platform.h"
#include "agb.h"
#include "agb_so.h"
#include "agb_opcode.h"
#include "agb_object.h"
#include "agb_number.h"
#include "agb_string.h"
#include "agb_array.h"
#include "agb_map.h"
#include "agb_gui.h"
#include "agb_net.h"
#include "agb_vm.h"
#include <SDL/SDL.h>
#include <SDL/SDL_ttf.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

#define agb_object_data(O) ((agb_ref_t*)((O)+1))
#define agb_object_datai(O,I) agb_object_data(O)[I]

#define AGB_LIBRARY_THROW_ID -1
#define AGB_LIBRARY_STRING_ID -2
#define AGB_LIBRARY_ARRAY_ID -3
#define AGB_LIBRARY_NET_SOCKET_ID -4
#define AGB_LIBRARY_FUNC_ID -5
#define AGB_LIBRARY_BOOLEAN_ID -6
#define AGB_LIBRARY_NUMBER_ID -7
#define AGB_LIBRARY_INTS_ID -8
#define AGB_LIBRARY_NUMS_ID -9
#define AGB_LIBRARY_BOOLS_ID -10
#define AGB_LIBRARY_BYTES_ID -11
#define AGB_LIBRARY_UINT2D_ID -12
#define AGB_LIBRARY_MAX -11

typedef struct {
	TTF_Font* arial14;
	uint32_t width;
	uint32_t height;
	SDL_Surface* surface;
	SDL_Event event;
	SDL_Event events[1024];
	uint32_t eventSize;
	SDL_Surface* screens[1024];
	uint32_t screensSize;
} AGB_GuiData;

struct agb_vm_t {
	int numberSize;
	double* numbers;
	int stringSize;
	agb_object_t** strings;
	int classSize;
	int methodSize;
	agb_class_t** classes;
	// Vetor de threads em uso
	int threadSize;
	// Vetor de threads alocados
	int threadMax;
	// Vetor de threads
	agb_thread_t** threads;
	// Indica o pc do método de methodid considerando o classid
	int* opcs;
	// Indica o pc do método de methodid
	int* spcs;
	// Indica quantos campos cada classid tem
	int* fields;
	// Cache de cast para classid
	unsigned char* cast;
	// Vetor de opcodes
	int* bytes;
	// Vm aberto
	agb_vm_t* vmChild;
	SDL_Thread* sdlThread;
	AGB_GuiData* guiData;
	agb_vm_t* parentVm;
	SDL_mutex* guiLock; // Somente o Root pois a referencia
};

struct agb_method_t {
	int id;
	wchar_t* name;
	int pc;
	int isStatic;
};

struct agb_field_t {
	int id;
	wchar_t* name;
};

struct agb_class_t {
	int id;
	wchar_t* name;
	int extendIndex;
	int fieldSize;
	int methodSize;
	agb_method_t** methods;
};

struct agb_object_field_t {
	int classid;
	int used;
	void* native;
	agb_object_t* data1;
	double number1;
	agb_object_t* data2;
	double number2;
};

struct agb_number_t {
	int classid;
	num native;
};

struct agb_string_t {
	int classid;
	wchar_t* native;
};

struct agb_boolean_t {
	int classid;
	num native;
};

// Memory

void agb_memory_free(void* data);

void agb_memory_set_size(long size);

size_t agb_memory_get_size();

void* agb_memory_alloc(size_t size);

void* agb_memory_realloc(void* old, size_t size);

// File

void* agb_file_open(const char* file);

void agb_file_close(void* file);

int agb_bytecode_read_int1(unsigned char** file);

unsigned char agb_bytecode_read_byte(unsigned char** file);

int agb_bytecode_read_int(unsigned char** file);

short agb_bytecode_read_short(unsigned char** file);

wchar_t* agb_bytecode_read_utf(unsigned char** file);

void* agb_object_new(int fields);

void* agb_object_vm_new(agb_thread_t* thread, agb_ref_t* cos, int stacksize, int fields);

void* agb_vm_alloc(agb_thread_t* thread, agb_ref_t* cos, int stacksize, int size);

void* agb_vm_realloc(agb_thread_t* thread, agb_ref_t* cos, int stacksize, void* old, int size);

void agb_object_free(agb_object_t* object);

void agb_vm_gc_object(agb_vm_t* vm, agb_object_t* object);

void agb_vm_gc(agb_thread_t* thread, agb_ref_t* cos, int size);

unsigned int agb_vm_cast(agb_vm_t* vm, unsigned int index, agb_class_t* clazz);

wchar_t* agb_base64_encode(wchar_t* data, size_t input_length, size_t* output_length);

wchar_t* agb_base64_decode(wchar_t *data, size_t input_length, size_t *output_length);

unsigned char* agb_base64_decode_chars(unsigned char* data, size_t input_length, size_t *output_length);

agb_object_t* agb_library_bools_new(agb_thread_t* thread, unsigned char* bytes, long size);

// Bytes

agb_object_t* agb_library_bytes_new(agb_thread_t* thread, unsigned char* bytes, long size);

unsigned char* agb_library_bytes_to_uchars(agb_object_t* object);

long agb_library_bytes_size(agb_object_t* object);

// Numbers

agb_object_t* agb_library_nums_new(agb_thread_t* thread, double* bytes, long size);

int code(int encode, FILE* InFile, FILE* SrcFile, FILE* OutFile, int BufSize);

#define _AGB_GP_VM 1
#define _AGB_GP_STACK 2
#define _AGB_GP_OBJECT 3
#define _AGB_GP_LOAD 4
#define _AGB_GP_JUMP 5
#define _AGB_GP_BOOL 6
#define _AGB_GP_INT 7
#define _AGB_GP_NUM 8
#define _AGB_GP_THROW 9
#define _AGB_GP_USER 32
#define _AGB_GP_CONSOLE _AGB_GP_USER + 0
#define _AGB_GP_STR _AGB_GP_USER + 1
#define _AGB_GP_ARRAY _AGB_GP_USER + 2
#define _AGB_GP_MAP _AGB_GP_USER + 3
#define _AGB_GP_SDL _AGB_GP_USER + 4
#define _AGB_GP_NET _AGB_GP_USER + 5
#define _AGB_GP_INTS _AGB_GP_USER + 6
#define _AGB_GP_NUMS _AGB_GP_USER + 7
#define _AGB_GP_BOOLS _AGB_GP_USER + 8
#define _AGB_GP_BYTES _AGB_GP_USER + 9
#define _AGB_GP_UINT2D _AGB_GP_USER + 10
#define _AGB_OP_VM_HALF 1
#define _AGB_OP_VM_GC 2
#define _AGB_OP_VM_ALLOC 3
#define _AGB_OP_VM_PAINT 4
#define _AGB_OP_VM_EVENT_KEY 5
#define _AGB_OP_VM_EVENT_MOUSE 6
#define _AGB_OP_VM_EVENT_WHEEL 7
#define _AGB_OP_STACK_PUSH 1
#define _AGB_OP_STACK_POP 2
#define _AGB_OP_STACK_LOAD 3
#define _AGB_OP_STACK_STORE 4
#define _AGB_OP_STACK_PRE_INC 5
#define _AGB_OP_STACK_PRE_DEC 6
#define _AGB_OP_STACK_POS_INC 7
#define _AGB_OP_STACK_POS_DEC 8
#define _AGB_OP_STACK_FOR_INC_BEGIN 9
#define _AGB_OP_STACK_FOR_INC_END 10
#define _AGB_OP_STACK_FOR_DEC_BEGIN 11
#define _AGB_OP_STACK_FOR_DEC_END 12
#define _AGB_OP_OBJECT_CAST_CLASS 1
#define _AGB_OP_OBJECT_GET_FIELD 2
#define _AGB_OP_OBJECT_SET_FIELD 3
#define _AGB_OP_OBJECT_EQUAL 4
#define _AGB_OP_OBJECT_NOTEQUAL 5
#define _AGB_OP_OBJECT_HASHCODE 6
#define _AGB_OP_OBJECT_CLASSNAME 7
#define _AGB_OP_OBJECT_CAST_NATIVE 8
#define _AGB_OP_LOAD_TRUE 1
#define _AGB_OP_LOAD_FALSE 2
#define _AGB_OP_LOAD_NUM 4
#define _AGB_OP_LOAD_STR 5
#define _AGB_OP_LOAD_NULL 6
#define _AGB_OP_LOAD_FUNC 7
#define _AGB_OP_LOAD_NEW 8
#define _AGB_OP_JUMP_STACK 1
#define _AGB_OP_JUMP_INT 2
#define _AGB_OP_JUMP_TRUE 3
#define _AGB_OP_JUMP_FALSE 4
#define _AGB_OP_JUMP_CALL 5
#define _AGB_OP_JUMP_STATIC_CALL 6
#define _AGB_OP_JUMP_RETURN 7
#define _AGB_OP_JUMP_TRUE_DUP 8
#define _AGB_OP_JUMP_FALSE_DUP 9
#define _AGB_OP_JUMP_FUNC_CALL 10
#define _AGB_OP_BOOL_NOT 1
#define _AGB_OP_BOOL_OR 2
#define _AGB_OP_BOOL_AND 3
#define _AGB_OP_BOOL_EQUAL 4
#define _AGB_OP_BOOL_NEQUAL 5
#define _AGB_OP_BOOL_TO_STR 6
#define _AGB_OP_NUM_NEG 1
#define _AGB_OP_NUM_SUM 2
#define _AGB_OP_NUM_SUB 3
#define _AGB_OP_NUM_MUL 4
#define _AGB_OP_NUM_DIV 5
#define _AGB_OP_NUM_EQUAL 6
#define _AGB_OP_NUM_NEQUAL 7
#define _AGB_OP_NUM_GT 8
#define _AGB_OP_NUM_EGT 9
#define _AGB_OP_NUM_LT 10
#define _AGB_OP_NUM_ELT 11
#define _AGB_OP_NUM_TO_STR 12
#define _AGB_OP_NUM_MOD 13
#define _AGB_OP_NUM_AND_BIT 14
#define _AGB_OP_NUM_OR_BIT 15
#define _AGB_OP_NUM_SHIFT_LEFT 16
#define _AGB_OP_NUM_SHIFT_RIGHT 17
#define _AGB_OP_NUM_TO_INT 18
#define _AGB_OP_STR_LEN 1
#define _AGB_OP_STR_SUM 2
#define _AGB_OP_STR_EQUAL 3
#define _AGB_OP_STR_NEQUAL 4
#define _AGB_OP_STR_GT 5
#define _AGB_OP_STR_EGT 6
#define _AGB_OP_STR_LT 7
#define _AGB_OP_STR_ELT 8
#define _AGB_OP_STR_SUBSTRING 9
#define _AGB_OP_STR_CODE_TO_CHAR 10
#define _AGB_OP_STR_CHAR_TO_CODE 11
#define _AGB_OP_STR_TRIM 12
#define _AGB_OP_STR_CHAR_AT 13
#define _AGB_OP_STR_START_WITH 14
#define _AGB_OP_STR_END_WITH 15
#define _AGB_OP_STR_INDEX_OF 16
#define _AGB_OP_STR_LAST_INDEX_OF 17
#define _AGB_OP_STR_REPLACE 18
#define _AGB_OP_STR_TO_LOWER_CASE 19
#define _AGB_OP_STR_TO_UPPER_CASE 20
#define _AGB_OP_STR_INDEX_OF_N 21
#define _AGB_OP_STR_BASE64_ENCODE 22
#define _AGB_OP_STR_BASE64_DECODE 23
#define _AGB_OP_STR_UTF_ENCODE 24
#define _AGB_OP_STR_UTF_DECODE 25
#define _AGB_OP_THROW_TRY 1
#define _AGB_OP_THROW_OBJECT 2
#define _AGB_OP_THROW_TRUE 3
#define _AGB_OP_THROW_FALSE 4
#define _AGB_OP_THROW_STORE 5
#define _AGB_OP_THROW_JUMP 6
#define _AGB_OP_ARRAY_NEW 1
#define _AGB_OP_ARRAY_LEN 2
#define _AGB_OP_ARRAY_ADD 3
#define _AGB_OP_ARRAY_INSERT 4
#define _AGB_OP_ARRAY_GET 5
#define _AGB_OP_ARRAY_SET 6
#define _AGB_OP_ARRAY_REM 7
#define _AGB_OP_CONSOLE_PRINT_STR 1
#define _AGB_OP_CONSOLE_PRINT_NUM 2
#define _AGB_OP_CONSOLE_PRINT_BOOL 3
#define _AGB_OP_SDL_ERROR 1
#define _AGB_OP_SDL_INIT 2
#define _AGB_OP_SDL_QUIT 3
#define _AGB_OP_SDL_VIDEO 4
#define _AGB_OP_SDL_EVENT_POOL 5
#define _AGB_OP_SDL_EVENT_WAIT 6
#define _AGB_OP_SDL_LOCK 7
#define _AGB_OP_SDL_UNLOCK 8
#define _AGB_OP_SDL_UPDATE 9
#define _AGB_OP_SDL_TICK 10
#define _AGB_OP_SDL_DELAY 11
#define _AGB_OP_SDL_VIDEO_WIDTH 12
#define _AGB_OP_SDL_VIDEO_HEIGHT 13
#define _AGB_OP_SDL_SCREEN_WIDTH 14
#define _AGB_OP_SDL_SCREEN_HEIGHT 15
#define _AGB_OP_SDL_CONSTANT 16
#define _AGB_OP_SDL_EVENT_TYPE 20
#define _AGB_OP_SDL_EVENT_KEYCODE 21
#define _AGB_OP_SDL_EVENT_REPAINT 22
#define _AGB_OP_SDL_EVENT_USERCODE 23
#define _AGB_OP_SDL_EVENT_MOUSE_MOTION_X 24
#define _AGB_OP_SDL_EVENT_MOUSE_MOTION_Y 25
#define _AGB_OP_SDL_EVENT_MOUSE_ACTION_X 26
#define _AGB_OP_SDL_EVENT_MOUSE_ACTION_Y 27
#define _AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON 28
#define _AGB_OP_SDL_EVENT_KEYCHAR 29
#define _AGB_OP_SDL_EVENT_MOUSE_WHEEL_X 30
#define _AGB_OP_SDL_EVENT_MOUSE_WHEEL_Y 31
#define _AGB_OP_SDL_DRAW_RECT 1024
#define _AGB_OP_SDL_DRAW_CIRCLE 1025
#define _AGB_OP_SDL_DRAW_STRING 1026
#define _AGB_OP_SDL_FONT_WIDTH 1027
#define _AGB_OP_SDL_FONT_HEIGHT 1028
#define _AGB_OP_SDL_FILL_RECT 1029
#define _AGB_OP_SDL_PUSH_GRAPHIC 1030
#define _AGB_OP_SDL_POP_GRAPHIC 1031
#define _AGB_OP_SDL_MAX 2048
#define _AGB_OP_NET_REQUEST 1
#define _AGB_OP_INTS_NEW 1
#define _AGB_OP_INTS_SIZE 2
#define _AGB_OP_INTS_GET 3
#define _AGB_OP_INTS_SET 4
#define _AGB_OP_INTS_SETS 5
#define _AGB_OP_INTS_COPY 6
#define _AGB_OP_NUMS_NEW 1
#define _AGB_OP_NUMS_SIZE 2
#define _AGB_OP_NUMS_GET 3
#define _AGB_OP_NUMS_SET 4
#define _AGB_OP_NUMS_SETS 5
#define _AGB_OP_NUMS_COPY 6
#define _AGB_OP_NUMS_EQUAL 7
#define _AGB_OP_BOOLS_NEW 1
#define _AGB_OP_BOOLS_SIZE 2
#define _AGB_OP_BOOLS_GET 3
#define _AGB_OP_BOOLS_SET 4
#define _AGB_OP_BOOLS_SETS 5
#define _AGB_OP_BOOLS_COPY 6
#define _AGB_OP_BOOLS_EQUAL 7
#define _AGB_OP_BYTES_NEW 1
#define _AGB_OP_BYTES_SIZE 2
#define _AGB_OP_BYTES_GET 3
#define _AGB_OP_BYTES_SET 4
#define _AGB_OP_BYTES_SETS 5
#define _AGB_OP_BYTES_COPY 6
#define _AGB_OP_BYTES_EQUAL 7
#define _AGB_OP_UINT2D_NEW 1
#define _AGB_OP_UINT2D_LINS 2
#define _AGB_OP_UINT2D_COLS 3
#define _AGB_OP_UINT2D_GET 4
#define _AGB_OP_UINT2D_SET 5
#define _AGB_OP_UINT2D_SETS 6
#define _AGB_OP_UINT2D_COPY 7
#define _AGB_OP_UINT2D_EQUAL 8

#ifdef __cplusplus
}
#endif

#endif
