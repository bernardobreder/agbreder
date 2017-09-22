#include "agbreder.h"

AppPanel::AppPanel ( ) :
		Panel ( ) {
	this->data = 0 ;
	this->setPreferenceHeight ( 500 ) ;
}

AppPanel::~AppPanel ( ) {
}

void AppPanel::init ( ) {
}

void AppPanel::open ( ) {
	if ( this->data ) {
		SDL_mutexP ( this->data->eventLock ) ;
		this->data->event.type = SDL_QUIT ;
		this->data->events.push ( this->data->event ) ;
		SDL_mutexV ( this->data->eventLock ) ;
		SDL_WaitThread ( data->thread , 0 ) ;
		SDL_KillThread(data->thread) ;
		SDL_DestroyMutex ( this->data->eventLock ) ;
		SDL_DestroyMutex ( this->data->guiLock ) ;
		if ( this->data->surface ) {
			SDL_FreeSurface ( this->data->surface ) ;
		}
		delete this->thread ;
		delete this->data ;
	}
	this->data = new agb_library_sdl_object ( ) ;
	data->thread = 0 ;
	data->surface = 0 ;
	data->width = this->getWidth ( ) ;
	data->height = this->getHeight ( ) ;
	data->eventLock = SDL_CreateMutex ( ) ;
	data->guiLock = SDL_CreateMutex ( ) ;
	this->thread = new AppThread ( data ) ;
	thread->start ( ) ;
}

void AppPanel::paint ( Graphic* g ) {
	g->fillRect ( 0 , 0 , this->getWidth ( ) , this->getHeight ( ) , 0xeeeeeeff ) ;
	if ( this->data && this->data->surface ) {
		int x = 0 ;
		int y = 0 ;
		int w = this->getWidth ( ) ;
		int h = this->getHeight ( ) ;
		SDL_Rect src = { x , y , w , h } ;
		SDL_Rect dest = { 0 , 0 , 0 , 0 } ;
		SDL_mutexP ( data->guiLock ) ;
		g->drawImage ( this->data->surface , & src , & dest ) ;
		SDL_mutexV ( data->guiLock ) ;
	}
}

void AppPanel::fireMouseDownEvent ( SDL_Event* event ) {
	this->getFrame ( )->setFocus ( this ) ;
	if ( this->data ) {
		SDL_mutexP ( this->data->eventLock ) ;
		this->data->events.push ( * event ) ;
		SDL_mutexV ( this->data->eventLock ) ;
	}
}

void AppPanel::fireMouseUpEvent ( SDL_Event* event ) {
	if ( this->data ) {
		SDL_mutexP ( this->data->eventLock ) ;
		this->data->events.push ( * event ) ;
		SDL_mutexV ( this->data->eventLock ) ;
	}
}

void AppPanel::fireKeyDownEvent ( SDL_Event* event ) {
	if ( this->data ) {
		SDL_mutexP ( this->data->eventLock ) ;
		this->data->events.push ( * event ) ;
		SDL_mutexV ( this->data->eventLock ) ;
	}
}

void AppPanel::fireKeyUpEvent ( SDL_Event* event ) {
	if ( this->data ) {
		SDL_mutexP ( this->data->eventLock ) ;
		this->data->events.push ( * event ) ;
		SDL_mutexV ( this->data->eventLock ) ;
	}
}
