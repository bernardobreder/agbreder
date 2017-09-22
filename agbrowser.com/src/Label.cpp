#include "agbreder.h"

Label::Label ( ) :
		Component ( ) {
	this->setText ( "" ) ;
	this->setFont ( new ArialFont ( ) ) ;
	this->setBackColor ( 0xffffffff ) ;
	this->setForeColor ( 0x00000000 ) ;
}

Label::~Label ( ) {
}

void Label::paint ( Graphic* g ) {
	if ( this->getBackColor ( ) >> 24 == 0 ) {
		g->fillRect ( 0 , 0 , this->getWidth ( ) , this->getHeight ( ) , this->getBackColor ( ) ) ;
	}
	g->drawText ( 0 , 0 , this->getText ( ) , this->getForeColor ( ) ) ;
}

void Label::setText ( string text ) {
	this->text = text ;
}

string Label::getText ( ) {
	return this->text ;
}

void Label::setFont ( Font* font ) {
	this->font = font ;
}

Font* Label::getFont ( ) {
	return this->font ;
}

void Label::setBackColor ( int value ) {
	this->backColor = value ;
}

int Label::getBackColor ( ) {
	return this->backColor ;
}

int Label::getDarkBackColor ( ) {
	unsigned int c = this->backColor ;
	int r = ( ( c >> 16 ) & 0xff ) ;
	int g = ( ( c >> 8 ) & 0xff ) ;
	int b = ( c & 0xff ) ;
	r = r * 0.5 ;
	g = g * 0.5 ;
	b = b * 0.5 ;
	return ( r << 16 ) + ( g << 8 ) + b ;
}

int Label::getLightBackColor ( ) {
	unsigned int c = this->backColor ;
	int r = ( ( c >> 16 ) & 0xff ) ;
	int g = ( ( c >> 8 ) & 0xff ) ;
	int b = ( c & 0xff ) ;
	r = r * 1.5 ;
	g = g * 1.5 ;
	b = b * 1.5 ;
	r = min ( r , 255 ) ;
	g = min ( g , 255 ) ;
	b = min ( b , 255 ) ;
	return ( r << 16 ) + ( g << 8 ) + b ;
}

void Label::setForeColor ( int value ) {
	this->foreColor = value ;
}

int Label::getForeColor ( ) {
	return this->foreColor ;
}

int Label::getMinimumWidth ( ) {
	return this->getFont ( )->getWidth ( this->getText ( ) ) ;
}

int Label::getMinimumHeight ( ) {
	return this->getFont ( )->getHeight ( this->getText ( ) ) ;
}
