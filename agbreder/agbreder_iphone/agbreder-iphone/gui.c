#include "agb.h"
#include "agbi.h"
#include "agb_gui.h"

const char* agb_library_sdl_error ( ) {
    return 0 ;
}

int agb_library_sdl_init ( ) {
	return 1 ;
}

void agb_library_sdl_quit ( ) {
}

int agb_library_sdl_video ( int width , int height ) {
	return 1 ;
}

int agb_library_sdl_pool_event ( ) {
    return 1 ;
}

void agb_library_sdl_wait_event ( ) {
}

int agb_library_sdl_type_event ( ) {
	return 0 ;
}

int agb_library_sdl_keycode_event ( ) {
	return 0 ;
}

int agb_library_sdl_lock ( ) {
	return 1 ;
}

void agb_library_sdl_unlock ( ) {
}

void agb_library_sdl_update ( ) {
}

int agb_library_sdl_tick ( ) {
	return 1 ;
}

void agb_library_sdl_delay ( int miliseg ) {
}

void agb_library_sdl_repaint ( ) {
}

int agb_library_sdl_usercode( ) {
	return 1 ;
}

void agb_library_sdl_drawrect ( int x , int y , int width , int height , int c ) {
}

void agb_library_sdl_drawcircle ( int x , int y , int r , int c ) {
}

int agb_library_sdl_width ( const char* text ) {
    return 0 ;
}

int agb_library_sdl_height ( const char* text ) {
	return 0 ;
}

void agb_library_sdl_drawstring ( int x , int y , int c , const char* text ) {
}
