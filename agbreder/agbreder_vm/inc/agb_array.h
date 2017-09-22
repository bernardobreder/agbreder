#ifndef AGB_ARRAY
#define AGB_ARRAY

#include "agb.h"
#include "agbi.h"

void agb_library_array_free ( agb_object_array_t* object ) ;

agb_object_t* agb_library_array_new ( agb_thread_t* thread , agb_ref_t* cos , int stacksize , int max ) ;

int agb_library_array_size ( agb_object_array_t* object ) ;

int agb_library_array_add ( agb_thread_t* thread , agb_ref_t* cos , int stacksize , agb_object_array_t* object , agb_object_t* item ) ;

int agb_library_array_insert ( agb_thread_t* thread , agb_ref_t* cos , int stacksize , agb_object_array_t* object , int index , agb_object_t* item ) ;

agb_object_t* agb_library_array_get ( agb_object_array_t* object , int index ) ;

agb_object_t* agb_library_array_set ( agb_object_array_t* object , int index , agb_object_t* value ) ;

agb_object_t* agb_library_array_remove ( agb_object_array_t* object , int index ) ;

void agb_library_array_gc ( agb_vm_t* vm , agb_object_array_t* object ) ;

#endif
