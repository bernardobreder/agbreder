#include "agbreder.h"

Thread::Thread ( ) {
	this->thread = 0 ;
	this->running = false ;
}

Thread::~Thread ( ) {
}

static int callthread ( void* self ) {
	Thread* thread = ( Thread* ) self ;
	while ( ! thread->thread ) {
		SDL_Delay ( 1 ) ;
	}
	thread->running = true ;
	return thread->run ( ) ;
}

Thread* Thread::start ( ) {
	static int n = 0 ;
	n ++ ;
#ifdef __MACOSX__
	this->thread = SDL_CreateThread ( callthread , "Vm" , this ) ;
#else
	this->thread = SDL_CreateThread ( callthread , this ) ;
#endif
	while ( ! this->running ) {
		SDL_Delay ( 1 ) ;
	}
	return this ;
}

int Thread::run ( ) {
	return 0 ;
}
