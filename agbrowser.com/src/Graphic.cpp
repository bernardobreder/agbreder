#include "agbreder.h"

// Screen pitch
#define PITCH(screen) (screen->pitch / 4)
// Definition of PI
#define PI 3.1415926535897932384626433832795f

Graphic::Graphic ( ) {
	this->screen = 0 ;
	this->x = 0 ;
	this->y = 0 ;
}

void Graphic::drawImage ( SDL_Surface* src , SDL_Rect* srcRect , SDL_Rect* destRect ) {
	destRect->x += this->x ;
	destRect->y += this->y ;
	SDL_BlitSurface ( src , srcRect , this->getSurface ( ) , destRect ) ;
	destRect->x -= this->x ;
	destRect->y -= this->y ;
}

void Graphic::setSurface ( SDL_Surface* surface ) {
	this->screen = surface ;
}

SDL_Surface* Graphic::getSurface ( ) {
	return this->screen ;
}

void Graphic::drawCircle ( int x , int y , int r , int c ) {
	x += this->x ;
	y += this->y ;
	int i , j ;
	for ( i = 0 ; i < 2 * r ; i ++ ) {
		if ( ( y - r + i ) >= 0 && ( y - r + i ) < screen->h ) {
			int len = ( int ) sqrt ( ( float ) ( r * r - ( r - i ) * ( r - i ) ) ) * 2 ;
			int xofs = x - len / 2 ;
			if ( xofs < 0 ) {
				len += xofs ;
				xofs = 0 ;
			}
			if ( xofs + len >= screen->w ) {
				len -= ( xofs + len ) - screen->w ;
			}
			int ofs = ( y - r + i ) * PITCH(screen) + xofs ;
			for ( j = 0 ; j < len ; j ++ )
				( ( unsigned int* ) screen->pixels ) [ ofs + j ] = c ;
		}
	}
}

void Graphic::drawRect ( int x , int y , int width , int height , int c ) {
	x += this->x ;
	y += this->y ;
	int i , j ;
	int h = screen->h ;
	int w = screen->w ;
	for ( i = 0 ; i < height ; i ++ ) {
		if ( ( y + i ) >= 0 && ( y + i ) < h ) {
			int len = width ;
			int xofs = x ;
			if ( xofs < 0 ) {
				len += xofs ;
				xofs = 0 ;
			}
			if ( xofs + len >= w ) {
				len -= ( xofs + len ) - w ;
			}
			int ofs = ( i + y ) * PITCH(screen) + xofs ;
			unsigned int* row = ( unsigned int* ) screen->pixels + ofs ;
			if ( i > 0 && i < height - 1 ) {
				row [ 0 ] = c ;
				row [ len - 1 ] = c ;
			} else {
				for ( j = 0 ; j < len ; j ++ ) {
					* row ++ = c ;
				}
			}
		}
	}
}

void Graphic::fillRect ( int x , int y , int width , int height , int c ) {
	x += this->x ;
	y += this->y ;
	int i , j ;
	int h = screen->h ;
	int w = screen->w ;
	for ( i = 0 ; i < height ; i ++ ) {
		if ( ( y + i ) >= 0 && ( y + i ) < h ) {
			int len = width ;
			int xofs = x ;
			if ( xofs < 0 ) {
				len += xofs ;
				xofs = 0 ;
			}
			if ( xofs + len >= w ) {
				len -= ( xofs + len ) - w ;
			}
			int ofs = ( i + y ) * PITCH(screen) + xofs ;
			unsigned int* row = ( unsigned int* ) screen->pixels + ofs ;
			for ( j = 0 ; j < len ; j ++ ) {
				* row ++ = c ;
			}
		}
	}
}

void Graphic::drawText ( int x , int y , string text , int c ) {
	x += this->x ;
	y += this->y ;
	static TTF_Font* arial12 = TTF_OpenFont ( "font/arial.ttf" , 14 ) ;
	SDL_Color foregroundColor = { ( c >> 16 ) & 0xFF , ( c >> 8 ) & 0xFF , c & 0xFF } ;
	SDL_Surface* textSurface = TTF_RenderText_Blended ( arial12 , text.c_str ( ) , foregroundColor ) ;
	SDL_Rect textLocation = { x , y , 0 , 0 } ;
	SDL_BlitSurface ( textSurface , NULL , screen , & textLocation ) ;
	SDL_FreeSurface ( textSurface ) ;
}

void Graphic::translate ( int x , int y ) {
	this->x += x ;
	this->y += y ;
}

bool Graphic::lock ( ) {
	if ( SDL_MUSTLOCK(screen) ) {
		if ( SDL_LockSurface ( screen ) < 0 ) {
			return false ;
		}
	}
	return true ;
}

void Graphic::unlock ( ) {
	if ( SDL_MUSTLOCK(screen) ) {
		SDL_UnlockSurface ( screen ) ;
	}
}

void Graphic::flush ( ) {
	SDL_UpdateRect ( screen , 0 , 0 , screen->w , screen->h ) ;
}
