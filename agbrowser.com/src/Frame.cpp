#include "agbreder.h"

Frame::Frame ( ) {
	putenv ( ( char* ) "SDL_VIDEO_CENTERED=1" ) ;
	if ( SDL_Init ( SDL_INIT_EVERYTHING ) < 0 || TTF_Init ( ) < 0 || SDLNet_Init ( ) < 0 ) {
		fprintf ( stderr , "Unable to init SDL: %s\n" , SDL_GetError ( ) ) ;
		exit ( 1 ) ;
	}
	atexit ( SDL_Quit ) ;
	atexit ( TTF_Quit ) ;
	SDL_WM_SetCaption ( "Agent Breder Browser" , 0 ) ;
	const SDL_VideoInfo* info = SDL_GetVideoInfo ( ) ;
	SDL_Surface* screen = SDL_SetVideoMode ( info->current_w - 100 , info->current_h - 100 , 32 , SDL_SWSURFACE ) ;
	if ( screen == NULL ) {
		fprintf ( stderr , "Unable to set up video: %s\n" , SDL_GetError ( ) ) ;
		exit ( 1 ) ;
	}
	SDL_EnableUNICODE ( 1 ) ;
	SDL_EnableKeyRepeat ( 1 , 1 ) ;
	SDL_EventState ( SDL_MOUSEMOTION , SDL_IGNORE ) ;
	this->component = 0 ;
	this->focus = this ;
	this->g.setSurface ( screen ) ;
	this->g.fillRect ( 0 , 0 , screen->w , screen->h , 0xffffffff ) ;
}

void Frame::loop ( ) {
	this->repaint ( ) ;
	while ( true ) {
		SDL_Event event ;
		while ( SDL_WaitEvent ( & event ) ) {
			switch ( event.type ) {
				case SDL_MOUSEBUTTONDOWN :
					this->dispatchMouseDownEvent ( & event ) ;
					break ;
				case SDL_MOUSEBUTTONUP :
					this->dispatchMouseUpEvent ( & event ) ;
					break ;
				case SDL_KEYDOWN :
					this->dispatchKeyDownEvent ( & event ) ;
					break ;
				case SDL_KEYUP :
					this->dispatchKeyUpEvent ( & event ) ;
					break ;
				case SDL_USEREVENT : {
					switch ( event.user.code ) {
						case 1 : {
							this->dispatchRepaintEvent ( & event ) ;
							break ;
						}
					}
					break ;
				}
				case SDL_QUIT :
					return ;
			}
		}
	}
}

void Frame::paint ( Graphic* g ) {
	this->g.lock ( ) ;
	Component* c = this->getComponent ( ) ;
	if ( c ) {
		g->translate ( c->getX ( ) , c->getY ( ) ) ;
		this->g.fillRect ( 0 , 0 , this->getWidth ( ) , this->getHeight ( ) , 0x00ffffff ) ;
		c->paint ( & this->g ) ;
		g->translate ( - c->getX ( ) , - c->getY ( ) ) ;
	}
	this->g.unlock ( ) ;
	this->g.flush ( ) ;
}

void Frame::layout ( ) {
	Component* c = this->getComponent ( ) ;
	if ( c ) {
		c->setX ( this->getMargin ( ) ) ;
		c->setY ( this->getMargin ( ) ) ;
		c->setWidth ( this->getWidth ( ) - 2 * this->getMargin ( ) ) ;
		c->setHeight ( this->getHeight ( ) - 2 * this->getMargin ( ) ) ;
		if ( dynamic_cast < Panel* > ( c ) ) {
			( ( Panel* ) c )->layout ( ) ;
		}
	}
}

void Frame::repaint ( ) {
	SDL_Event user_event ;
	user_event.type = SDL_USEREVENT ;
	user_event.user.code = 1 ;
	user_event.user.data1 = NULL ;
	user_event.user.data2 = NULL ;
	SDL_PushEvent ( & user_event ) ;
}

void Frame::dispatchMouseDownEvent ( SDL_Event* event ) {
	Component* c = this->getComponent ( ) ;
	if ( c ) {
		c->fireMouseDownEvent ( event ) ;
	}
}

void Frame::dispatchMouseUpEvent ( SDL_Event* event ) {
	Component* c = this->getComponent ( ) ;
	if ( c ) {
		c->fireMouseUpEvent ( event ) ;
	}
}

void Frame::dispatchKeyDownEvent ( SDL_Event* event ) {
	Component* c = this->getFocus ( ) ;
	if ( c ) {
		c->fireKeyDownEvent ( event ) ;
	}
}

void Frame::dispatchKeyUpEvent ( SDL_Event* event ) {
	Component* c = this->getFocus ( ) ;
	if ( c ) {
		c->fireKeyUpEvent ( event ) ;
	}
}

void Frame::dispatchRepaintEvent ( SDL_Event* event ) {
	this->layout ( ) ;
	this->paint ( & g ) ;
}

void Frame::setComponent ( Component* c ) {
	this->component = c ;
	c->setWidth ( this->getWidth ( ) ) ;
	c->setHeight ( this->getHeight ( ) ) ;
	c->setParent ( this ) ;
}

Component* Frame::getComponent ( ) {
	return this->component ;
}

void Frame::setFocus ( Component* c ) {
	this->focus = c ;
}

Component* Frame::getFocus ( ) {
	return this->focus ;
}

void Frame::setButton ( Button* c ) {
	this->button = c ;
}

Button* Frame::getButton ( ) {
	return this->button ;
}

int Frame::getWidth ( ) {
	return this->g.getSurface ( )->w ;
}

int Frame::getHeight ( ) {
	return this->g.getSurface ( )->h ;
}
