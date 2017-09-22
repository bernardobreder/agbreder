#include "agbreder.h"

AppThread::AppThread ( agb_library_sdl_object* data ) :
		Thread ( ) {
	this->data = data ;
}

AppThread::~AppThread ( ) {
}

int AppThread::run ( ) {
	this->data->thread = this->thread ;
	unsigned char* buffer = this->load ( ) ;
	if ( ! buffer ) {
		buffer = agb_file_read ( ( const char* ) "../agbreder_bdk/bin/binary.agbc" ) ;
	}
	return this->vm ( buffer ) ;
}

unsigned char* AppThread::load ( ) {
	string host = "localhost" ;
	string url = "/" ;
	int port = 9889 ;
	IPaddress address ;
	if ( SDLNet_ResolveHost ( & address , host.c_str ( ) , port ) != 0 ) {
		return 0 ;
	}
	TCPsocket socket = SDLNet_TCP_Open ( & address ) ;
	if ( ! socket ) {
		return 0 ;
	}
	const char* request = "GET /index.html AGB/1.0\nHost: localhost\n\n" ;
	int len = strlen ( request ) ;
	if ( SDLNet_TCP_Send ( socket , request , len ) != len ) {
		SDLNet_TCP_Close ( socket ) ;
		return 0 ;
	}
	int max = 1024 ;
	int size = 0 ;
	unsigned char* buffer = ( unsigned char* ) calloc ( max , sizeof(unsigned char) ) ;
	if ( ! buffer ) {
		return 0 ;
	}
	for ( ; ; ) {
		if ( SDLNet_TCP_Recv ( socket , buffer + size , 1 ) == 0 ) {
			break ;
		}
		if ( ++ size == max ) {
			max *= 2 ;
			unsigned char* aux = ( unsigned char* ) realloc ( buffer , max * sizeof(unsigned char) ) ;
			if ( ! aux ) {
				free ( buffer ) ;
				return 0 ;
			}
			buffer = aux ;
			buffer [ size ] = 0 ;
		}
	}
	SDLNet_TCP_Close ( socket ) ;
	return buffer ;
}
int AppThread::vm ( unsigned char* buffer ) {
	agb_vm_t* vm = agb_vm_new ( buffer ) ;
	free ( buffer ) ;
	if ( ! vm ) {
		return EXIT_FAILURE ;
	}
	agb_vm_set_opcode_gui ( vm , agb_virtual_library_sdl ( data ) ) ;
	agb_thread_t * thread = agb_thread_new ( vm ) ;
	if ( ! thread ) {
		return EXIT_FAILURE ;
	}
	agb_method_t* method = agb_method_classname ( vm , "Main" , "main()" , 1 ) ;
	if ( ! method ) {
		return EXIT_FAILURE ;
	}
	int pc = agb_method_pc ( method ) ;
	int flag = agb_vm_execute ( thread , pc ) ;
	agb_vm_free ( vm ) ;
	return flag ;
}

