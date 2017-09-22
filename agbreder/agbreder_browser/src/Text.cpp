#include "agbreder.h"

Text::Text ( ) :
		Label ( ) {
	this->cursor = 0 ;
	this->cursorX = 0 ;
}

Text::~Text ( ) {
}

void Text::paint ( Graphic* g ) {
	g->drawText ( 2 , 0 , this->getText ( ) , 0x00000000 ) ;
	g->drawRect ( 0 , 0 , this->getWidth ( ) , this->getHeight ( ) , 0x00aaaaaa ) ;
	if ( this->getFrame ( )->getFocus ( ) == this ) {
		g->drawRect ( 1 + this->cursorX , 2 , 1 , this->getHeight ( ) - 4 , 0x00000000 ) ;
	}
}

void Text::fireMouseDownEvent ( SDL_Event* e ) {
	this->getFrame ( )->setFocus ( this ) ;
	this->getFrame ( )->repaint ( ) ;
}

void Text::fireKeyDownEvent ( SDL_Event* e ) {
	int key = e->key.keysym.sym ;
	if ( key == SDLK_LEFT ) {
		if ( this->cursor > 0 ) {
			this->cursor = this->cursor - 1 ;
			this->cursorX = this->getFont ( )->getWidth ( this->getText ( ).substr ( 0 , this->cursor ) ) ;
		}
	} else if ( key == SDLK_RIGHT ) {
		if ( this->cursor < this->getText ( ).size ( ) ) {
			this->cursor = this->cursor + 1 ;
			this->cursorX = this->getFont ( )->getWidth ( this->getText ( ).substr ( 0 , this->cursor ) ) ;
		}
	} else if ( key == SDLK_DELETE ) {
		if ( this->cursor < this->getText ( ).size ( ) ) {
			string text = this->getText ( ).substr ( 0 , this->cursor ) ;
			text = text + this->getText ( ).substr ( this->cursor + 1 , this->getText ( ).size ( ) ) ;
			this->setText ( text ) ;
		}
	} else if ( key == SDLK_BACKSPACE ) {
		if ( this->cursor > 0 ) {
			string text = this->getText ( ).substr ( 0 , this->cursor - 1 ) ;
			text = text + this->getText ( ).substr ( this->cursor , this->getText ( ).size ( ) ) ;
			this->setText ( text ) ;
			this->cursor = this->cursor - 1 ;
			this->cursorX = this->getFont ( )->getWidth ( this->getText ( ).substr ( 0 , this->cursor ) ) ;
		}
	} else if ( key == SDLK_RETURN ) {
		Button* c = this->getFrame ( )->getButton ( ) ;
		if ( c ) {
			c->fireActionEvent ( e ) ;
		}
	} else if ( ( key > 32 and key < 256 ) or key == SDLK_SPACE ) {
		string text = this->getText ( ).substr ( 0 , this->cursor ) ;
		text = text + ( char ) e->key.keysym.unicode ;
		text = text + this->getText ( ).substr ( this->cursor , this->getText ( ).size ( ) ) ;
		this->setText ( text ) ;
		this->cursor = this->cursor + 1 ;
		this->cursorX = this->getFont ( )->getWidth ( this->getText ( ).substr ( 0 , this->cursor ) ) ;
	}
	this->getFrame ( )->repaint ( ) ;
}

int Text::getMinimumWidth ( ) {
	return this->Label::getMinimumWidth ( ) + 4 ;
}

int Text::getMinimumHeight ( ) {
	return this->Label::getMinimumHeight ( ) ;
}
