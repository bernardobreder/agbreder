#include "agbreder.h"

#undef main

using namespace std ;

int main_execute ( int argc , char* argv [ ] ) {
	setbuf ( stdout , 0 ) ;
	string filename = "binary.agbc" ;
	if ( argc > 1 ) {
		filename = argv [ 1 ] ;
	}
	string mainClass = "Main" ;
	if ( argc > 2 ) {
		mainClass = argv [ 2 ] ;
	}
	unsigned char* buffer = agb_file_read ( filename.c_str ( ) ) ;
	if ( ! buffer ) {
		fprintf ( stderr , "Not correct file : %s\n" , filename.c_str ( ) ) ;
		return EXIT_FAILURE ;
	}
	if ( 0 ) {
		for ( ; ; ) {
			agb_vm_t* vm = agb_vm_new ( buffer ) ;
			if ( vm ) {
				agb_thread_t* thread = agb_thread_new ( vm ) ;
				if ( thread ) {
					agb_method_t* method = agb_method_classname ( vm , mainClass.c_str ( ) , "main()" , 1 ) ;
					if ( method ) {
						int pc = agb_method_pc ( method ) ;
						agb_vm_execute ( thread , pc ) ;
					}
				}
				agb_vm_free ( vm ) ;
			}
		}
	} else {
		agb_vm_t* vm = agb_vm_new ( buffer ) ;
		free ( buffer ) ;
		if ( ! vm ) {
			return EXIT_FAILURE ;
		}
		agb_thread_t* thread = agb_thread_new ( vm ) ;
		if ( ! thread ) {
			return EXIT_FAILURE ;
		}
		agb_method_t* method = agb_method_classname ( vm , mainClass.c_str ( ) , "main()" , 1 ) ;
		if ( ! method ) {
			return EXIT_FAILURE ;
		}
		int pc = agb_method_pc ( method ) ;
		int flag = agb_vm_execute ( thread , pc ) ;
		if ( flag != EXIT_SUCCESS ) {
			return flag ;
		}
		agb_vm_free ( vm ) ;
	}
	return EXIT_SUCCESS ;
}

int main ( int argc , char *argv [ ] ) {
	if ( argc > 1 && ! strcmp ( argv [ 1 ] , "-e" ) ) {
		return main_execute ( argc - 1 , argv + 1 ) ;
	} else {
		AppFrame frame ;
		frame.loop ( ) ;
		return 0 ;
	}
}
