#include "math.h"
#include "agb.h"
#include "agbi.h"

int agb_library_object_hashcode ( agb_thread_t* thread , agb_object_t* object ) {
	return abs ( ( ( void* ) object - ( void* ) thread->vm ) ) ;
}

agb_object_t* agb_library_object_classname ( agb_thread_t* thread , agb_object_t* object ) {
	int classid = object->classid ;
	wchar_t* name ;
	if ( classid < 0 ) {
		if ( classid == AGB_LIBRARY_STRING_ID ) {
			name = L"<string>" ;
		} else if ( classid == AGB_LIBRARY_ARRAY_ID ) {
			name = L"<array>" ;
		} else if ( classid == AGB_LIBRARY_NUMS_ID ) {
			name = L"num[]" ;
		} else if ( classid == AGB_LIBRARY_BOOLS_ID ) {
			name = L"bool[]" ;
		} else if ( classid == AGB_LIBRARY_INTS_ID ) {
			name = L"int[]" ;
		} else {
			name = L"<primitive>" ;
		}
	} else {
		name = thread->vm->classes [ classid ]->name ;
	}
	return agb_library_string_new ( thread , - 1 , - 1 , name ) ;
}
