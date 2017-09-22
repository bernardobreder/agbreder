#include "agb.h"
#include "agbi.h"

static const int MAX = 15 ;

agb_library_map_t* agb_library_map_new ( agb_thread_t* thread , agb_ref_t* cos , int stacksize ) {
	agb_library_map_t* data = ( agb_library_map_t* ) agb_vm_alloc ( thread , cos , stacksize , sizeof(agb_library_map_t) ) ;
	if ( ! data ) {
		return 0 ;
	}
	return data ;
}

void agb_library_map_free ( agb_library_map_t* data ) {
	agb_memory_free ( data ) ;
}

agb_object_t* agb_library_map_get ( agb_library_map_t* data , const char* key , int len , int hash ) {
	if ( ! data->array ) {
		return 0 ;
	}
	int index = hash % MAX ;
	agb_library_map_entity_t** rows = data->array + index ;
	if ( ! rows ) {
		return 0 ;
	}
	int n , size = data->sizes [ index ] ;
	for ( n = 0 ; n < size ; n ++ ) {
		agb_library_map_entity_t* row = * rows ;
		if ( row ) {
			if ( row->len == len ) {
				if ( ! strcmp ( row->key , key ) ) {
					return row->object ;
				}
			}
		}
		rows ++ ;
	}
	return 0 ;
}

agb_object_t* agb_library_map_set ( agb_thread_t* thread , agb_ref_t* cos , int stacksize , agb_library_map_t* data , const char* key , int len , int hash , agb_object_t* value ) {
	if ( ! data->array ) {
		data->array = ( agb_library_map_entity_t** ) agb_vm_alloc ( thread , cos , stacksize , MAX * sizeof(agb_library_map_entity_t*) ) ;
		if ( ! data->array ) {
			return 0 ;
		}
	}
	int index = hash % MAX ;
	agb_library_map_entity_t* row = data->array [ index ] ;
	if ( ! row ) {
		row = ( agb_library_map_entity_t* ) agb_vm_alloc ( thread , cos , stacksize , MAX * sizeof(agb_library_map_entity_t) ) ;
		if ( ! row ) {
			return 0 ;
		}
		data->array [ index ] = row ;
	}
	int n , size = data->sizes [ index ] ;
	for ( n = 0 ; n < size ; n ++ ) {
		if ( row->len == len ) {
			if ( ! strcmp ( row->key , key ) ) {
				agb_object_t* old = row->object ;
				row->object = data->array [ index ] [ size - 1 ].object ;
				data->sizes [ index ] -- ;
				return old ;
			}
		}
		row ++ ;
	}
	// Falta completar
	return 0 ;
}

agb_object_t* agb_library_map_remove ( agb_library_map_t* data , char* key , int len , int hash ) {
	if ( ! data->array ) {
		return 0 ;
	}
	int index = hash % MAX ;
	agb_library_map_entity_t* row = data->array [ index ] ;
	if ( ! row ) {
		return 0 ;
	}
	int n , size = data->sizes [ index ] ;
	for ( n = 0 ; n < size ; n ++ ) {
		if ( row->len == len ) {
			if ( ! strcmp ( row->key , key ) ) {
				agb_object_t* old = row->object ;
				row->object = data->array [ index ] [ size - 1 ].object ;
				data->sizes [ index ] -- ;
				return old ;
			}
		}
		row ++ ;
	}
	return 0 ;
}

int agb_library_map_size ( agb_library_map_t* data ) {
	return data->size ;
}
