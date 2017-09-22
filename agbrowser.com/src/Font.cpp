#include "agbreder.h"

Font::Font ( ) {
}

Font::~Font ( ) {
}

int Font::getWidth ( string text ) {
	return 0 ;
}

int Font::getHeight ( string text ) {
	return 0 ;
}

ArialFont::ArialFont ( ) {
	static TTF_Font* font = TTF_OpenFont ( "font/arial.ttf" , 14 ) ;
	this->font = font ;
}

ArialFont::~ArialFont ( ) {
	TTF_CloseFont ( this->font ) ;
}

int ArialFont::getWidth ( string text ) {
	int size ;
	TTF_SizeUTF8 ( this->font , text.c_str ( ) , & size , 0 ) ;
	return size ;
}

int ArialFont::getHeight ( string text ) {
	int size ;
	TTF_SizeUTF8 ( this->font , text.c_str ( ) , 0 , & size ) ;
	return size ;
}
