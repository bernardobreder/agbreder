#ifndef AGB_MAP
#define AGB_MAP

#include "agb.h"
#include "agbi.h"

/**
 * Tipo de Mapa
 */
typedef struct agb_library_map_t agb_library_map_t ;

/**
 * Tipo de Entidade
 */
typedef struct agb_library_map_entity_t agb_library_map_entity_t ;

/**
 * Estrutura do Objeto
 */
struct agb_library_map_entity_t {
	int len ;
	int hash ;
	char* key ;
	agb_object_t* object ;
} ;

/**
 * Estrutura do Objeto
 */
struct agb_library_map_t {
	int size ;
	int* sizes ;
	agb_library_map_entity_t** array ;
} ;

/**
 * Libere a memória do Socket
 */
void agb_library_map_free ( agb_library_map_t* data ) ;

/**
 * Cria um Socket através de um Host e uma Porta
 */
agb_library_map_t* agb_library_map_new ( agb_thread_t* thread , agb_ref_t* cos , int stacksize ) ;

/**
 * Finaliza um Socket
 */
agb_object_t* agb_library_map_get ( agb_library_map_t* data , const char* key , int len , int hash ) ;

/**
 * Leia do Socket um byte (0-255).
 * Caso ocorra um erro, retorne um valor negativo
 * Se tudo ocorrer corretamente, retorne um byte (0-255)
 */
agb_object_t* agb_library_map_set ( agb_thread_t* thread , agb_ref_t* cos , int stacksize , agb_library_map_t* data , const char* key , int len , int hash , agb_object_t* value ) ;

/**
 * Escreve no Socket um byte (0-255).
 * Caso ocorra um erro, retorne 1.
 * Se tudo ocorrer corretamente, retorne 0.
 */
agb_object_t* agb_library_map_remove ( agb_library_map_t* data , char* chars , int len , int hash ) ;

/**
 * Escreve no Socket um byte (0-255).
 * Caso ocorra um erro, retorne 1.
 * Se tudo ocorrer corretamente, retorne 0.
 */
int agb_library_map_size ( agb_library_map_t* data ) ;

#endif
