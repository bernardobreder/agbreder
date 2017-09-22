#include "agbreder.h"

Vertical::Vertical ( ) :
		Panel ( ) {
}

Vertical::~Vertical ( ) {
}

void Vertical::addComponent ( Component* c ) {
	this->list.push_back ( c ) ;
	c->setParent ( this ) ;
}

void Vertical::layout ( ) {
	if ( list.size ( ) > 0 ) {
		int y = 0 ;
		int h = this->getHeight ( ) ;
		for ( unsigned int n = 0 ; n < list.size ( ) ; n ++ ) {
			Component* c = list [ n ] ;
			c->setX ( 0 ) ;
			c->setY ( y ) ;
			if ( c->getPreferenceWidth ( ) >= 0 ) {
				c->setWidth ( c->getPreferenceWidth ( ) ) ;
			} else {
				c->setWidth ( this->getWidth ( ) ) ;
			}
			if ( c->getPreferenceHeight ( ) >= 0 ) {
				c->setHeight ( c->getPreferenceHeight ( ) ) ;
			} else {
				c->setHeight ( c->getMinimumHeight ( ) ) ;
			}
			if ( dynamic_cast < Panel* > ( c ) ) {
				( ( Panel* ) c )->layout ( ) ;
			}
			y += c->getHeight ( ) ;
			h -= c->getHeight ( ) ;
			if ( n != list.size ( ) - 1 ) {
				h -= this->getMargin ( ) ;
				y += this->getMargin ( ) ;
			}
		}
		{
			Component* c = list [ list.size ( ) - 1 ] ;
			c->setHeight ( c->getHeight ( ) + h ) ;
		}
	}
}

void Vertical::paint ( Graphic* g ) {
	for ( vector < Component* >::iterator it = list.begin ( ) ; it < list.end ( ) ; it ++ ) {
		Component* c = * it ;
		g->translate ( 0 , c->getY ( ) ) ;
		c->paint ( g ) ;
		g->translate ( 0 , - c->getY ( ) ) ;
	}
}
