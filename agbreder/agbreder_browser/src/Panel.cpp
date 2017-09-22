#include "agbreder.h"

Panel::Panel ( ) :
		Component ( ) {
	this->margin = 2 ;
}

Panel::~Panel ( ) {
}

void Panel::init ( ) {
	for ( vector < Component* >::iterator it = list.begin ( ) ; it < list.end ( ) ; it ++ ) {
		Component* c = * it ;
		c->init ( ) ;
	}
}

void Panel::paint ( Graphic* g ) {
}

void Panel::layout ( ) {
}

void Panel::fireMouseDownEvent ( SDL_Event* event ) {
	int x = event->motion.x ;
	int y = event->motion.y ;
	for ( vector < Component* >::iterator it = list.begin ( ) ; it < list.end ( ) ; it ++ ) {
		Component* c = * it ;
		int cx = c->getX ( ) ;
		if ( x >= cx && cx + c->getWidth ( ) >= x ) {
			int cy = c->getY ( ) ;
			if ( y >= cy && cy + c->getHeight ( ) >= y ) {
				event->motion.x -= c->getX ( ) ;
				event->motion.y -= c->getY ( ) ;
				c->fireMouseDownEvent ( event ) ;
			}
		}
	}
}

void Panel::fireMouseUpEvent ( SDL_Event* event ) {
	int x = event->motion.x ;
	int y = event->motion.y ;
	for ( vector < Component* >::iterator it = list.begin ( ) ; it < list.end ( ) ; it ++ ) {
		Component* c = * it ;
		int cx = c->getX ( ) ;
		if ( x >= cx && cx + c->getWidth ( ) >= x ) {
			int cy = c->getY ( ) ;
			if ( y >= cy && cy + c->getHeight ( ) >= y ) {
				event->motion.x -= c->getX ( ) ;
				event->motion.y -= c->getY ( ) ;
				c->fireMouseUpEvent ( event ) ;
			}
		}
	}
}

void Panel::fireKeyDownEvent ( SDL_Event* event ) {
}

void Panel::fireKeyUpEvent ( SDL_Event* event ) {
}

int Panel::getMargin ( ) {
	return this->margin ;
}

void Panel::setMargin ( int value ) {
	this->margin = value ;
}

Component* Panel::getComponent ( int index ) {
	return this->list [ index ] ;
}

int Panel::size ( ) {
	return this->list.size ( ) ;
}

