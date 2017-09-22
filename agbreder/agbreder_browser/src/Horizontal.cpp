#include "agbreder.h"

Horizontal::Horizontal ( ) :
		Panel ( ) {
}

Horizontal::~Horizontal ( ) {
}

void Horizontal::addComponent ( Component* c , bool expandable ) {
	this->list.push_back ( c ) ;
	this->expandables.push_back ( expandable ) ;
	c->setParent ( this ) ;
}

void Horizontal::layout ( ) {
	int w = this->getWidth ( ) ;
	int h = this->getHeight ( ) ;
	int margin = this->getMargin ( ) ;
	for ( int n = 0 ; n < this->size ( ) ; n ++ ) {
		Component* c = list [ n ] ;
		if ( c->getPreferenceWidth ( ) >= 0 ) {
			c->setWidth ( c->getPreferenceWidth ( ) ) ;
		} else {
			c->setWidth ( c->getMinimumWidth ( ) ) ;
		}
		if ( c->getPreferenceHeight ( ) >= 0 ) {
			c->setHeight ( c->getPreferenceHeight ( ) ) ;
		} else {
			c->setHeight ( c->getMinimumHeight ( ) ) ;
		}
		h = max ( h , c->getHeight ( ) ) ;
		w -= c->getWidth ( ) ;
		if ( n != this->size ( ) - 1 ) {
			w -= margin ;
		}
		if ( w < 0 ) {
			w = 0 ;
		}
	}
	this->setHeight ( h ) ;
	int count = 0 ;
	for ( int n = 0 ; n < this->size ( ) ; n ++ ) {
		if ( expandables [ n ] ) {
			count ++ ;
		}
	}
	int delta = w / count ;
	if ( count > 0 ) {
		int x = 0 ;
		for ( int n = 0 ; n < this->size ( ) ; n ++ ) {
			Component* c = list [ n ] ;
			bool expandable = expandables [ n ] ;
			c->setX ( x ) ;
			c->setY ( ( h - c->getHeight ( ) ) / 2 ) ;
			if ( expandable ) {
				c->setWidth ( c->getWidth ( ) + delta ) ;
			}
			if ( n == this->size ( ) - 1 && ( ( float ) w / count ) != ( w / count ) ) {
				c->setWidth ( c->getWidth ( ) + 1 ) ;
			}
			x += c->getWidth ( ) ;
			if ( n != this->size ( ) - 1 ) {
				x += margin ;
			}
		}
	}
	for ( int n = 0 ; n < this->size ( ) ; n ++ ) {
		Component* c = list [ n ] ;
		if ( dynamic_cast < Panel* > ( c ) ) {
			( ( Panel* ) c )->layout ( ) ;
		}
	}
}

void Horizontal::paint ( Graphic* g ) {
	for ( vector < Component* >::iterator it = list.begin ( ) ; it < list.end ( ) ; it ++ ) {
		Component* c = * it ;
		g->translate ( c->getX ( ) , c->getY ( ) ) ;
		c->paint ( g ) ;
		g->translate ( - c->getX ( ) , - c->getY ( ) ) ;
	}
}

