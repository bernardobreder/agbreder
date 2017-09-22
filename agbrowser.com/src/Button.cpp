#include "agbreder.h"

Button::Button ( ) :
		Label ( ) {
	this->setPadding ( 3 ) ;
	this->setBackColor ( 0x00ccccff ) ;
}

Button::~Button ( ) {
}

void Button::paint ( Graphic* g ) {
	int padding = this->getPadding ( ) ;
	g->fillRect ( 1 , 1 , this->getWidth ( ) - 2 , this->getHeight ( ) - 2 , this->getBackColor ( ) ) ;
	g->drawRect ( 0 , 0 , this->getWidth ( ) , this->getHeight ( ) , this->getDarkBackColor ( ) ) ;
	g->drawText ( padding , padding , this->getText ( ) , this->getForeColor ( ) ) ;
}

void Button::layout ( ) {
}

void Button::fireMouseDownEvent ( SDL_Event* e ) {
	this->getFrame ( )->setFocus ( this ) ;
	this->fireActionEvent ( e ) ;
}

void Button::fireActionEvent ( SDL_Event* e ) {
}

int Button::getMinimumWidth ( ) {
	return this->Label::getMinimumWidth ( ) + this->getPadding ( ) * 2 ;
}

int Button::getMinimumHeight ( ) {
	return this->Label::getMinimumHeight ( ) + this->getPadding ( ) * 2 ;
}
