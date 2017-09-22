#ifndef _AGB_SO_SO_
#define _AGB_SO_SO_

#include "agb_so.h"

#ifdef __LINUX__
void agb_file_temp(char* path) {
	mkstemp(path) ;
}
#endif

#ifdef __MACOSX__
#include <stdlib.h>
void agb_file_temp ( char* path ) {
	mkstemp ( path ) ;
}
#endif

#ifdef __WIN32__
#include <io.h>
void agb_file_temp(char* path) {
	tmpnam (path) ;
}
#endif

#endif
