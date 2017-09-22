#ifndef AGB_OBJECT
#define AGB_OBJECT

#include "agb.h"
#include "agbi.h"

int agb_library_object_hashcode ( agb_thread_t* thread , agb_object_t* object ) ;

agb_object_t* agb_library_object_classname ( agb_thread_t* thread , agb_object_t* object ) ;

#endif
