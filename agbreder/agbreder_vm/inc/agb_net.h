#ifndef AGB_NET
#define AGB_NET

#include "agb.h"
#include "agbi.h"

/**
 * Estrutura do Objeto
 */
struct agb_library_net_socket_t {
	int port ;
} ;

typedef struct agb_library_net_socket_t agb_library_net_socket_t ;

/**
 * Libere a mem�ria do Socket
 */
void agb_library_net_socket_free ( agb_library_net_socket_t* data ) ;

/**
 * Cria um Socket atrav�s de um Host e uma Porta
 */
agb_library_net_socket_t* agb_library_net_socket_new ( agb_thread_t* thread , agb_ref_t* cos , int stacksize , char* host , int port ) ;

/**
 * Finaliza um Socket
 */
void agb_library_net_socket_close ( agb_library_net_socket_t* data ) ;

/**
 * Leia do Socket um byte (0-255).
 * Caso ocorra um erro, retorne um valor negativo
 * Se tudo ocorrer corretamente, retorne um byte (0-255)
 */
char* agb_library_net_socket_read ( agb_library_net_socket_t* data ) ;

/**
 * Escreve no Socket um byte (0-255).
 * Caso ocorra um erro, retorne 1.
 * Se tudo ocorrer corretamente, retorne 0.
 */
int agb_library_net_socket_write ( agb_library_net_socket_t* data , char* chars , int len ) ;

agb_object_t* agb_library_net_request ( agb_thread_t* thread , wchar_t* host , unsigned short port , wchar_t* request ) ;

#endif
