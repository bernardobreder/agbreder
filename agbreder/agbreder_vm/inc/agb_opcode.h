//#ifndef _AGB_OPCODE_
//#define _AGB_OPCODE_
//
//#ifdef __cplusplus
//extern "C" {
//#endif
//
//#define _AGB_GP_VM 1
//#define _AGB_GP_STACK 2
//#define _AGB_GP_OBJECT 3
//#define _AGB_GP_LOAD 4
//#define _AGB_GP_JUMP 5
//#define _AGB_GP_BOOL 6
//#define _AGB_GP_INT 7
//#define _AGB_GP_NUM 8
//#define _AGB_GP_THROW 9
//#define _AGB_GP_USER 32
//#define _AGB_GP_CONSOLE _AGB_GP_USER + 0
//#define _AGB_GP_STR _AGB_GP_USER + 1
//#define _AGB_GP_ARRAY _AGB_GP_USER + 2
//#define _AGB_GP_MAP _AGB_GP_USER + 3
//#define _AGB_GP_SDL _AGB_GP_USER + 4
//#define _AGB_GP_NET _AGB_GP_USER + 5
//#define _AGB_OP_VM_HALF 1
//#define _AGB_OP_VM_GC 2
//#define _AGB_OP_STACK_PUSH 1
//#define _AGB_OP_STACK_POP 2
//#define _AGB_OP_STACK_LOAD 3
//#define _AGB_OP_STACK_STORE 4
//#define _AGB_OP_STACK_PRE_INC 5
//#define _AGB_OP_STACK_PRE_DEC 6
//#define _AGB_OP_STACK_POS_INC 7
//#define _AGB_OP_STACK_POS_DEC 8
//#define _AGB_OP_STACK_FOR_INC_BEGIN 9
//#define _AGB_OP_STACK_FOR_INC_END 10
//#define _AGB_OP_STACK_FOR_DEC_BEGIN 11
//#define _AGB_OP_STACK_FOR_DEC_END 12
//#define _AGB_OP_OBJECT_CAST_CLASS 1
//#define _AGB_OP_OBJECT_GET_FIELD 2
//#define _AGB_OP_OBJECT_SET_FIELD 3
//#define _AGB_OP_OBJECT_EQUAL 4
//#define _AGB_OP_OBJECT_NOTEQUAL 5
//#define _AGB_OP_OBJECT_HASHCODE 6
//#define _AGB_OP_OBJECT_CLASSNAME 7
//#define _AGB_OP_OBJECT_CAST_NATIVE 8
//#define _AGB_OP_LOAD_TRUE 1
//#define _AGB_OP_LOAD_FALSE 2
//#define _AGB_OP_LOAD_NUM 4
//#define _AGB_OP_LOAD_STR 5
//#define _AGB_OP_LOAD_NULL 6
//#define _AGB_OP_LOAD_FUNC 7
//#define _AGB_OP_LOAD_NEW 8
//#define _AGB_OP_JUMP_STACK 1
//#define _AGB_OP_JUMP_INT 2
//#define _AGB_OP_JUMP_TRUE 3
//#define _AGB_OP_JUMP_FALSE 4
//#define _AGB_OP_JUMP_CALL 5
//#define _AGB_OP_JUMP_STATIC_CALL 6
//#define _AGB_OP_JUMP_RETURN 7
//#define _AGB_OP_JUMP_TRUE_DUP 8
//#define _AGB_OP_JUMP_FALSE_DUP 9
//#define _AGB_OP_JUMP_FUNC_CALL 10
//#define _AGB_OP_BOOL_NOT 1
//#define _AGB_OP_BOOL_OR 2
//#define _AGB_OP_BOOL_AND 3
//#define _AGB_OP_BOOL_EQUAL 4
//#define _AGB_OP_BOOL_NEQUAL 5
//#define _AGB_OP_BOOL_TO_STR 6
//#define _AGB_OP_NUM_NEG 1
//#define _AGB_OP_NUM_SUM 2
//#define _AGB_OP_NUM_SUB 3
//#define _AGB_OP_NUM_MUL 4
//#define _AGB_OP_NUM_DIV 5
//#define _AGB_OP_NUM_EQUAL 6
//#define _AGB_OP_NUM_NEQUAL 7
//#define _AGB_OP_NUM_GT 8
//#define _AGB_OP_NUM_EGT 9
//#define _AGB_OP_NUM_LT 10
//#define _AGB_OP_NUM_ELT 11
//#define _AGB_OP_NUM_TO_STR 12
//#define _AGB_OP_NUM_MOD 13
//#define _AGB_OP_NUM_AND_BIT 14
//#define _AGB_OP_NUM_OR_BIT 15
//#define _AGB_OP_NUM_SHIFT_LEFT 16
//#define _AGB_OP_NUM_SHIFT_RIGHT 17
//#define _AGB_OP_STR_LEN 1
//#define _AGB_OP_STR_SUM 2
//#define _AGB_OP_STR_EQUAL 3
//#define _AGB_OP_STR_NEQUAL 4
//#define _AGB_OP_STR_GT 5
//#define _AGB_OP_STR_EGT 6
//#define _AGB_OP_STR_LT 7
//#define _AGB_OP_STR_ELT 8
//#define _AGB_OP_STR_SUBSTRING 9
//#define _AGB_OP_STR_CODE_TO_CHAR 10
//#define _AGB_OP_STR_CHAR_TO_CODE 11
//#define _AGB_OP_STR_TRIM 12
//#define _AGB_OP_STR_CHAR_AT 13
//#define _AGB_OP_STR_START_WITH 14
//#define _AGB_OP_STR_END_WITH 15
//#define _AGB_OP_STR_INDEX_OF 16
//#define _AGB_OP_STR_LAST_INDEX_OF 17
//#define _AGB_OP_STR_REPLACE 18
//#define _AGB_OP_STR_TO_LOWER_CASE 19
//#define _AGB_OP_STR_TO_UPPER_CASE 20
//#define _AGB_OP_THROW_TRY 1
//#define _AGB_OP_THROW_OBJECT 2
//#define _AGB_OP_THROW_TRUE 3
//#define _AGB_OP_THROW_FALSE 4
//#define _AGB_OP_THROW_STORE 5
//#define _AGB_OP_THROW_JUMP 6
//#define _AGB_OP_ARRAY_NEW 1
//#define _AGB_OP_ARRAY_LEN 2
//#define _AGB_OP_ARRAY_ADD 3
//#define _AGB_OP_ARRAY_INSERT 4
//#define _AGB_OP_ARRAY_GET 5
//#define _AGB_OP_ARRAY_SET 6
//#define _AGB_OP_ARRAY_REM 7
//#define _AGB_OP_CONSOLE_PRINT_STR 1
//#define _AGB_OP_CONSOLE_PRINT_NUM 2
//#define _AGB_OP_CONSOLE_PRINT_BOOL 3
//#define _AGB_OP_SDL_ERROR 1
//#define _AGB_OP_SDL_INIT 2
//#define _AGB_OP_SDL_QUIT 3
//#define _AGB_OP_SDL_VIDEO 4
//#define _AGB_OP_SDL_EVENT_POOL 5
//#define _AGB_OP_SDL_EVENT_WAIT 6
//#define _AGB_OP_SDL_LOCK 7
//#define _AGB_OP_SDL_UNLOCK 8
//#define _AGB_OP_SDL_UPDATE 9
//#define _AGB_OP_SDL_TICK 10
//#define _AGB_OP_SDL_DELAY 11
//#define _AGB_OP_SDL_VIDEO_WIDTH 12
//#define _AGB_OP_SDL_VIDEO_HEIGHT 13
//#define _AGB_OP_SDL_SCREEN_WIDTH 14
//#define _AGB_OP_SDL_SCREEN_HEIGHT 15
//#define _AGB_OP_SDL_CONSTANT 16
//#define _AGB_OP_SDL_EVENT_TYPE 20
//#define _AGB_OP_SDL_EVENT_KEYCODE 21
//#define _AGB_OP_SDL_EVENT_REPAINT 22
//#define _AGB_OP_SDL_EVENT_USERCODE 23
//#define _AGB_OP_SDL_EVENT_MOUSE_MOTION_X 24
//#define _AGB_OP_SDL_EVENT_MOUSE_MOTION_Y 25
//#define _AGB_OP_SDL_EVENT_MOUSE_ACTION_X 26
//#define _AGB_OP_SDL_EVENT_MOUSE_ACTION_Y 27
//#define _AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON 28
//#define _AGB_OP_SDL_EVENT_KEYCHAR 29
//#define _AGB_OP_SDL_DRAW_RECT 1024
//#define _AGB_OP_SDL_DRAW_CIRCLE 1025
//#define _AGB_OP_SDL_DRAW_STRING 1026
//#define _AGB_OP_SDL_FONT_WIDTH 1027
//#define _AGB_OP_SDL_FONT_HEIGHT 1028
//#define _AGB_OP_SDL_FILL_RECT 1029
//#define _AGB_OP_SDL_MAX 1030
//#define _AGB_OP_NET_REQUEST 1
//
//#ifdef __cplusplus
//}
//#endif
//
//#endif
