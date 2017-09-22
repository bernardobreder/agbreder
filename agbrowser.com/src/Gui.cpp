#include "agbreder.h"

#define PITCH (screen->pitch / 4)

#define SDL_KEY_FIRST 1024

static int agb_virtual_library_sdl_error ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_init ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	static TTF_Font* arial12 = TTF_OpenFont ( "font/arial.ttf" , 14 ) ;
	data->arial12 = arial12 ;
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = 1 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_quit ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	SDL_FreeSurface ( data->surface ) ;
	data->surface = 0 ;
	while ( ! data->events.empty ( ) ) {
		data->events.pop ( ) ;
	}
	return 0 ;
}

static int agb_virtual_library_sdl_video ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	SDL_Surface* surface =
	    SDL_CreateRGBSurface ( SDL_SWSURFACE , data->width , data->height , 32 , 0x00ff0000 , 0x0000ff00 , 0x000000ff , 0x00000000 ) ;
	if ( surface == NULL ) {
		fprintf ( stderr , "CreateRGBSurface failed: %s\n" , SDL_GetError ( ) ) ;
		exit ( 1 ) ;
	}
	data->surface = surface ;
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = 1 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_screen_width ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->width ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_screen_height ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->height ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_pool_event ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	SDL_mutexP ( data->eventLock ) ;
	data->event.type = 0 ;
	if ( ! data->events.empty ( ) ) {
		data->event = data->events.front ( ) ;
		data->events.pop ( ) ;
	}
	SDL_mutexV ( data->eventLock ) ;
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.type != 0 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_wait_event ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	agb_virtual_library_sdl_pool_event ( thread , data ) ;
	while ( ! thread->cobjstack->number ) {
		SDL_Delay ( 10 ) ;
		thread->cobjstack -- ;
		agb_virtual_library_sdl_pool_event ( thread , data ) ;
	}
	return 0 ;
}

static int agb_virtual_library_sdl_type_event ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.type ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_keycode_event ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.key.keysym.sym ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_keychar_event ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.key.keysym.unicode ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_lock ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	int flag = 1 ;
	SDL_Surface* screen = data->surface ;
	if ( SDL_MUSTLOCK(screen) ) {
		if ( SDL_LockSurface ( screen ) < 0 ) {
			flag = 0 ;
		}
	}
	SDL_mutexP ( data->guiLock ) ;
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = flag ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_unlock ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	SDL_Surface* screen = data->surface ;
	if ( SDL_MUSTLOCK(screen) ) {
		SDL_UnlockSurface ( screen ) ;
	}
	SDL_mutexV ( data->guiLock ) ;
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = 0 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_update ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	SDL_Event user_event ;
	user_event.type = SDL_USEREVENT ;
	user_event.user.code = 1 ;
	user_event.user.data1 = NULL ;
	user_event.user.data2 = NULL ;
	SDL_PushEvent ( & user_event ) ;
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = 0 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_tick ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = SDL_GetTicks ( ) ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_delay ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = 0 ;
	return 0 ;
}

static int agb_virtual_library_sdl_repaint ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	SDL_Event user_event ;
	user_event.type = SDL_USEREVENT ;
	user_event.user.code = 1 ;
	user_event.user.data1 = NULL ;
	user_event.user.data2 = NULL ;
	SDL_PushEvent ( & user_event ) ;
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = 0 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_usercode ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.user.code = 1 ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_fillrect ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	int x = thread->cobjstack [ - 4 ].number ;
	int y = thread->cobjstack [ - 3 ].number ;
	int width = thread->cobjstack [ - 2 ].number ;
	int height = thread->cobjstack [ - 1 ].number ;
	int c = thread->cobjstack [ 0 ].number ;
	SDL_Surface* screen = data->surface ;
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
			int ofs = ( i + y ) * PITCH + xofs ;
			unsigned int* row = ( unsigned int* ) screen->pixels + ofs ;
			for ( j = 0 ; j < len ; j ++ ) {
				* row ++ = c ;
			}
		}
	}
	thread->cobjstack -= 4 ;
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = 0 ;
	return 0 ;
}

void agb_virtual_library_sdl_drawrect ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	int x = thread->cobjstack [ - 4 ].number ;
	int y = thread->cobjstack [ - 3 ].number ;
	int width = thread->cobjstack [ - 2 ].number ;
	int height = thread->cobjstack [ - 1 ].number ;
	int c = thread->cobjstack [ 0 ].number ;
	SDL_Surface* screen = data->surface ;
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
			int ofs = ( i + y ) * PITCH + xofs ;
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
	thread->cobjstack -= 4 ;
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = 0 ;
}

void agb_virtual_library_sdl_drawcircle ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	int x = thread->cobjstack [ - 3 ].number ;
	int y = thread->cobjstack [ - 2 ].number ;
	int r = thread->cobjstack [ - 1 ].number ;
	int c = thread->cobjstack [ 0 ].number ;
	SDL_Surface* screen = data->surface ;
	int i , j ;
	int h = screen->h ;
	int w = screen->w ;
	for ( i = 0 ; i < 2 * r ; i ++ ) {
		if ( ( y - r + i ) >= 0 && ( y - r + i ) < h ) {
			int len = ( int ) sqrt ( ( float ) ( r * r - ( r - i ) * ( r - i ) ) ) * 2 ;
			int xofs = x - len / 2 ;
			if ( xofs < 0 ) {
				len += xofs ;
				xofs = 0 ;
			}
			if ( xofs + len >= w ) {
				len -= ( xofs + len ) - w ;
			}
			int ofs = ( y - r + i ) * PITCH + xofs ;
			unsigned int* row = ( unsigned int* ) screen->pixels + ofs ;
			for ( j = 0 ; j < len ; j ++ ) {
				* row ++ = c ;
			}
		}
	}
	thread->cobjstack -= 3 ;
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = 0 ;
}

static int agb_virtual_library_sdl_font_width ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	agb_object_t* arg1 = thread->cobjstack->data ;
	if ( ! arg1 ) {
		thread->cobjstack->data = 0 ;
		thread->cobjstack->number = 0 ;
		return 0 ;
	}
	const char* text = agb_library_string_chars ( arg1 ) ;
	int w ;
	TTF_SizeUTF8 ( data->arial12 , text , & w , 0 ) ;
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = w ;
	return 0 ;
}

static int agb_virtual_library_sdl_font_height ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	agb_object_t* arg1 = thread->cobjstack->data ;
	if ( ! arg1 ) {
		thread->cobjstack->data = 0 ;
		thread->cobjstack->number = 0 ;
		return 0 ;
	}
	const char* text = agb_library_string_chars ( arg1 ) ;
	int h ;
	TTF_SizeUTF8 ( data->arial12 , text , 0 , & h ) ;
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = h ;
	return 0 ;
}

static int agb_virtual_library_sdl_drawstring ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	int x = thread->cobjstack [ - 3 ].number ;
	int y = thread->cobjstack [ - 2 ].number ;
	int c = thread->cobjstack [ - 1 ].number ;
	agb_object_t* otext = thread->cobjstack [ 0 ].data ;
	const char* text = agb_library_string_chars ( otext ) ;
	if ( ! text ) {
		text = "" ;
	}
	SDL_Color foregroundColor = { ( c >> 16 ) & 0xFF , ( c >> 8 ) & 0xFF , c & 0xFF } ;
	SDL_Surface* textSurface = TTF_RenderText_Blended ( data->arial12 , text , foregroundColor ) ;
	SDL_Rect textLocation = { x , y , 0 , 0 } ;
	SDL_BlitSurface ( textSurface , NULL , data->surface , & textLocation ) ;
	SDL_FreeSurface ( textSurface ) ;
	thread->cobjstack -= 3 ;
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = 0 ;
	return 0 ;
}

static int agb_virtual_library_sdl_video_width ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->surface->w ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_video_height ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->surface->h ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_mouse_motion_x ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.motion.x ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_mouse_motion_y ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.motion.y ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_mouse_action_x ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.button.x ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_mouse_action_y ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.button.y ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_mouse_action_button ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	thread->cobjstack [ 1 ].data = 0 ;
	thread->cobjstack [ 1 ].number = data->event.button.button ;
	thread->cobjstack ++ ;
	return 0 ;
}

static int agb_virtual_library_sdl_constant ( agb_thread_t* thread , agb_library_sdl_object* data ) {
	int value = thread->cobjstack->number ;
	int result = - 1 ;
	switch ( value ) {
		case 1 :
			result = SDL_QUIT ;
			break ;
		case 2 :
			result = SDL_KEYDOWN ;
			break ;
		case 3 :
			result = SDL_KEYUP ;
			break ;
		case 4 :
			result = SDL_MOUSEMOTION ;
			break ;
		case 5 :
			result = SDL_MOUSEBUTTONDOWN ;
			break ;
		case 6 :
			result = SDL_MOUSEBUTTONUP ;
			break ;
		case 7 :
			result = - 1 ;
			break ;
		case 8 :
			result = SDL_USEREVENT ;
			break ;
		case 9 :
			result = SDL_RESIZABLE ;
			break ;
		case SDL_KEY_FIRST + 0 :
			result = SDLK_BACKSPACE ;
			break ;
		case SDL_KEY_FIRST + 1 :
			result = SDLK_TAB ;
			break ;
		case SDL_KEY_FIRST + 2 :
			result = SDLK_CLEAR ;
			break ;
		case SDL_KEY_FIRST + 3 :
			result = SDLK_RETURN ;
			break ;
		case SDL_KEY_FIRST + 4 :
			result = SDLK_PAUSE ;
			break ;
		case SDL_KEY_FIRST + 5 :
			result = SDLK_ESCAPE ;
			break ;
		case SDL_KEY_FIRST + 6 :
			result = SDLK_SPACE ;
			break ;
		case SDL_KEY_FIRST + 7 :
			result = SDLK_EXCLAIM ;
			break ;
		case SDL_KEY_FIRST + 8 :
			result = SDLK_QUOTEDBL ;
			break ;
		case SDL_KEY_FIRST + 9 :
			result = SDLK_HASH ;
			break ;
		case SDL_KEY_FIRST + 10 :
			result = SDLK_DOLLAR ;
			break ;
		case SDL_KEY_FIRST + 11 :
			result = SDLK_AMPERSAND ;
			break ;
		case SDL_KEY_FIRST + 12 :
			result = SDLK_QUOTE ;
			break ;
		case SDL_KEY_FIRST + 13 :
			result = SDLK_LEFTPAREN ;
			break ;
		case SDL_KEY_FIRST + 14 :
			result = SDLK_RIGHTPAREN ;
			break ;
		case SDL_KEY_FIRST + 15 :
			result = SDLK_ASTERISK ;
			break ;
		case SDL_KEY_FIRST + 16 :
			result = SDLK_PLUS ;
			break ;
		case SDL_KEY_FIRST + 17 :
			result = SDLK_COMMA ;
			break ;
		case SDL_KEY_FIRST + 18 :
			result = SDLK_MINUS ;
			break ;
		case SDL_KEY_FIRST + 19 :
			result = SDLK_PERIOD ;
			break ;
		case SDL_KEY_FIRST + 20 :
			result = SDLK_SLASH ;
			break ;
		case SDL_KEY_FIRST + 21 :
			result = SDLK_0 ;
			break ;
		case SDL_KEY_FIRST + 22 :
			result = SDLK_1 ;
			break ;
		case SDL_KEY_FIRST + 23 :
			result = SDLK_2 ;
			break ;
		case SDL_KEY_FIRST + 24 :
			result = SDLK_3 ;
			break ;
		case SDL_KEY_FIRST + 25 :
			result = SDLK_4 ;
			break ;
		case SDL_KEY_FIRST + 26 :
			result = SDLK_5 ;
			break ;
		case SDL_KEY_FIRST + 27 :
			result = SDLK_6 ;
			break ;
		case SDL_KEY_FIRST + 28 :
			result = SDLK_7 ;
			break ;
		case SDL_KEY_FIRST + 29 :
			result = SDLK_8 ;
			break ;
		case SDL_KEY_FIRST + 30 :
			result = SDLK_9 ;
			break ;
		case SDL_KEY_FIRST + 31 :
			result = SDLK_COLON ;
			break ;
		case SDL_KEY_FIRST + 32 :
			result = SDLK_SEMICOLON ;
			break ;
		case SDL_KEY_FIRST + 33 :
			result = SDLK_LESS ;
			break ;
		case SDL_KEY_FIRST + 34 :
			result = SDLK_EQUALS ;
			break ;
		case SDL_KEY_FIRST + 35 :
			result = SDLK_GREATER ;
			break ;
		case SDL_KEY_FIRST + 36 :
			result = SDLK_QUESTION ;
			break ;
		case SDL_KEY_FIRST + 37 :
			result = SDLK_AT ;
			break ;
		case SDL_KEY_FIRST + 38 :
			result = SDLK_LEFTBRACKET ;
			break ;
		case SDL_KEY_FIRST + 39 :
			result = SDLK_BACKSLASH ;
			break ;
		case SDL_KEY_FIRST + 40 :
			result = SDLK_RIGHTBRACKET ;
			break ;
		case SDL_KEY_FIRST + 41 :
			result = SDLK_CARET ;
			break ;
		case SDL_KEY_FIRST + 42 :
			result = SDLK_UNDERSCORE ;
			break ;
		case SDL_KEY_FIRST + 43 :
			result = SDLK_BACKQUOTE ;
			break ;
		case SDL_KEY_FIRST + 44 :
			result = SDLK_a ;
			break ;
		case SDL_KEY_FIRST + 45 :
			result = SDLK_b ;
			break ;
		case SDL_KEY_FIRST + 46 :
			result = SDLK_c ;
			break ;
		case SDL_KEY_FIRST + 47 :
			result = SDLK_d ;
			break ;
		case SDL_KEY_FIRST + 48 :
			result = SDLK_e ;
			break ;
		case SDL_KEY_FIRST + 49 :
			result = SDLK_f ;
			break ;
		case SDL_KEY_FIRST + 50 :
			result = SDLK_g ;
			break ;
		case SDL_KEY_FIRST + 51 :
			result = SDLK_h ;
			break ;
		case SDL_KEY_FIRST + 52 :
			result = SDLK_i ;
			break ;
		case SDL_KEY_FIRST + 53 :
			result = SDLK_j ;
			break ;
		case SDL_KEY_FIRST + 54 :
			result = SDLK_k ;
			break ;
		case SDL_KEY_FIRST + 55 :
			result = SDLK_l ;
			break ;
		case SDL_KEY_FIRST + 56 :
			result = SDLK_m ;
			break ;
		case SDL_KEY_FIRST + 57 :
			result = SDLK_n ;
			break ;
		case SDL_KEY_FIRST + 58 :
			result = SDLK_o ;
			break ;
		case SDL_KEY_FIRST + 59 :
			result = SDLK_p ;
			break ;
		case SDL_KEY_FIRST + 60 :
			result = SDLK_q ;
			break ;
		case SDL_KEY_FIRST + 61 :
			result = SDLK_r ;
			break ;
		case SDL_KEY_FIRST + 62 :
			result = SDLK_s ;
			break ;
		case SDL_KEY_FIRST + 63 :
			result = SDLK_t ;
			break ;
		case SDL_KEY_FIRST + 64 :
			result = SDLK_u ;
			break ;
		case SDL_KEY_FIRST + 65 :
			result = SDLK_v ;
			break ;
		case SDL_KEY_FIRST + 66 :
			result = SDLK_w ;
			break ;
		case SDL_KEY_FIRST + 67 :
			result = SDLK_x ;
			break ;
		case SDL_KEY_FIRST + 68 :
			result = SDLK_y ;
			break ;
		case SDL_KEY_FIRST + 69 :
			result = SDLK_z ;
			break ;
		case SDL_KEY_FIRST + 70 :
			result = SDLK_DELETE ;
			break ;
		case SDL_KEY_FIRST + 167 :
			result = SDLK_KP0 ;
			break ;
		case SDL_KEY_FIRST + 168 :
			result = SDLK_KP1 ;
			break ;
		case SDL_KEY_FIRST + 169 :
			result = SDLK_KP2 ;
			break ;
		case SDL_KEY_FIRST + 170 :
			result = SDLK_KP3 ;
			break ;
		case SDL_KEY_FIRST + 171 :
			result = SDLK_KP4 ;
			break ;
		case SDL_KEY_FIRST + 172 :
			result = SDLK_KP5 ;
			break ;
		case SDL_KEY_FIRST + 173 :
			result = SDLK_KP6 ;
			break ;
		case SDL_KEY_FIRST + 174 :
			result = SDLK_KP7 ;
			break ;
		case SDL_KEY_FIRST + 175 :
			result = SDLK_KP8 ;
			break ;
		case SDL_KEY_FIRST + 176 :
			result = SDLK_KP9 ;
			break ;
		case SDL_KEY_FIRST + 177 :
			result = SDLK_KP_PERIOD ;
			break ;
		case SDL_KEY_FIRST + 178 :
			result = SDLK_KP_DIVIDE ;
			break ;
		case SDL_KEY_FIRST + 179 :
			result = SDLK_KP_MULTIPLY ;
			break ;
		case SDL_KEY_FIRST + 180 :
			result = SDLK_KP_MINUS ;
			break ;
		case SDL_KEY_FIRST + 181 :
			result = SDLK_KP_PLUS ;
			break ;
		case SDL_KEY_FIRST + 182 :
			result = SDLK_KP_ENTER ;
			break ;
		case SDL_KEY_FIRST + 183 :
			result = SDLK_KP_EQUALS ;
			break ;
		case SDL_KEY_FIRST + 184 :
			result = SDLK_UP ;
			break ;
		case SDL_KEY_FIRST + 185 :
			result = SDLK_DOWN ;
			break ;
		case SDL_KEY_FIRST + 186 :
			result = SDLK_RIGHT ;
			break ;
		case SDL_KEY_FIRST + 187 :
			result = SDLK_LEFT ;
			break ;
		case SDL_KEY_FIRST + 188 :
			result = SDLK_INSERT ;
			break ;
		case SDL_KEY_FIRST + 189 :
			result = SDLK_HOME ;
			break ;
		case SDL_KEY_FIRST + 190 :
			result = SDLK_END ;
			break ;
		case SDL_KEY_FIRST + 191 :
			result = SDLK_PAGEUP ;
			break ;
		case SDL_KEY_FIRST + 192 :
			result = SDLK_PAGEDOWN ;
			break ;
		case SDL_KEY_FIRST + 193 :
			result = SDLK_F1 ;
			break ;
		case SDL_KEY_FIRST + 194 :
			result = SDLK_F2 ;
			break ;
		case SDL_KEY_FIRST + 195 :
			result = SDLK_F3 ;
			break ;
		case SDL_KEY_FIRST + 196 :
			result = SDLK_F4 ;
			break ;
		case SDL_KEY_FIRST + 197 :
			result = SDLK_F5 ;
			break ;
		case SDL_KEY_FIRST + 198 :
			result = SDLK_F6 ;
			break ;
		case SDL_KEY_FIRST + 199 :
			result = SDLK_F7 ;
			break ;
		case SDL_KEY_FIRST + 200 :
			result = SDLK_F8 ;
			break ;
		case SDL_KEY_FIRST + 201 :
			result = SDLK_F9 ;
			break ;
		case SDL_KEY_FIRST + 202 :
			result = SDLK_F10 ;
			break ;
		case SDL_KEY_FIRST + 203 :
			result = SDLK_F11 ;
			break ;
		case SDL_KEY_FIRST + 204 :
			result = SDLK_F12 ;
			break ;
		case SDL_KEY_FIRST + 205 :
			result = SDLK_F13 ;
			break ;
		case SDL_KEY_FIRST + 206 :
			result = SDLK_F14 ;
			break ;
		case SDL_KEY_FIRST + 207 :
			result = SDLK_F15 ;
			break ;
		case SDL_KEY_FIRST + 208 :
			result = SDLK_NUMLOCK ;
			break ;
		case SDL_KEY_FIRST + 209 :
			result = SDLK_CAPSLOCK ;
			break ;
		case SDL_KEY_FIRST + 210 :
			result = SDLK_SCROLLOCK ;
			break ;
		case SDL_KEY_FIRST + 211 :
			result = SDLK_RSHIFT ;
			break ;
		case SDL_KEY_FIRST + 212 :
			result = SDLK_LSHIFT ;
			break ;
		case SDL_KEY_FIRST + 213 :
			result = SDLK_RCTRL ;
			break ;
		case SDL_KEY_FIRST + 214 :
			result = SDLK_LCTRL ;
			break ;
		case SDL_KEY_FIRST + 215 :
			result = SDLK_RALT ;
			break ;
		case SDL_KEY_FIRST + 216 :
			result = SDLK_LALT ;
			break ;
		case SDL_KEY_FIRST + 217 :
			result = SDLK_RMETA ;
			break ;
		case SDL_KEY_FIRST + 218 :
			result = SDLK_LMETA ;
			break ;
		case SDL_KEY_FIRST + 219 :
			result = SDLK_LSUPER ;
			break ;
		case SDL_KEY_FIRST + 220 :
			result = SDLK_RSUPER ;
			break ;
		case SDL_KEY_FIRST + 221 :
			result = SDLK_COMPOSE ;
			break ;
		case SDL_KEY_FIRST + 222 :
			result = SDLK_HELP ;
			break ;
		case SDL_KEY_FIRST + 223 :
			result = SDLK_PRINT ;
			break ;
		case SDL_KEY_FIRST + 224 :
			result = SDLK_SYSREQ ;
			break ;
		case SDL_KEY_FIRST + 225 :
			result = SDLK_BREAK ;
			break ;
		case SDL_KEY_FIRST + 226 :
			result = SDLK_MENU ;
			break ;
		case SDL_KEY_FIRST + 227 :
			result = SDLK_POWER ;
			break ;
		case SDL_KEY_FIRST + 228 :
			result = SDLK_EURO ;
			break ;
		case SDL_KEY_FIRST + 229 :
			result = SDLK_UNDO ;
			break ;
	}
	thread->cobjstack->data = 0 ;
	thread->cobjstack->number = result ;
	return 0 ;
}

void** agb_virtual_library_sdl ( agb_library_sdl_object* data ) {
	void** funcs = ( void** ) calloc ( _AGB_OP_SDL_MAX , sizeof(void*) ) ;
	funcs [ 0 ] = ( void* ) data ;
	funcs [ _AGB_OP_SDL_ERROR ] = ( void* ) agb_virtual_library_sdl_error ;
	funcs [ _AGB_OP_SDL_INIT ] = ( void* ) agb_virtual_library_sdl_init ;
	funcs [ _AGB_OP_SDL_QUIT ] = ( void* ) agb_virtual_library_sdl_quit ;
	funcs [ _AGB_OP_SDL_VIDEO ] = ( void* ) agb_virtual_library_sdl_video ;
	funcs [ _AGB_OP_SDL_EVENT_POOL ] = ( void* ) agb_virtual_library_sdl_pool_event ;
	funcs [ _AGB_OP_SDL_EVENT_WAIT ] = ( void* ) agb_virtual_library_sdl_wait_event ;
	funcs [ _AGB_OP_SDL_LOCK ] = ( void* ) agb_virtual_library_sdl_lock ;
	funcs [ _AGB_OP_SDL_UNLOCK ] = ( void* ) agb_virtual_library_sdl_unlock ;
	funcs [ _AGB_OP_SDL_UPDATE ] = ( void* ) agb_virtual_library_sdl_update ;
	funcs [ _AGB_OP_SDL_TICK ] = ( void* ) agb_virtual_library_sdl_tick ;
	funcs [ _AGB_OP_SDL_DELAY ] = ( void* ) agb_virtual_library_sdl_delay ;
	funcs [ _AGB_OP_SDL_VIDEO_WIDTH ] = ( void* ) agb_virtual_library_sdl_video_width ;
	funcs [ _AGB_OP_SDL_VIDEO_HEIGHT ] = ( void* ) agb_virtual_library_sdl_video_height ;
	funcs [ _AGB_OP_SDL_SCREEN_WIDTH ] = ( void* ) agb_virtual_library_sdl_screen_width ;
	funcs [ _AGB_OP_SDL_SCREEN_HEIGHT ] = ( void* ) agb_virtual_library_sdl_screen_height ;
	funcs [ _AGB_OP_SDL_CONSTANT ] = ( void* ) agb_virtual_library_sdl_constant ;
	funcs [ _AGB_OP_SDL_EVENT_TYPE ] = ( void* ) agb_virtual_library_sdl_type_event ;
	funcs [ _AGB_OP_SDL_EVENT_KEYCODE ] = ( void* ) agb_virtual_library_sdl_keycode_event ;
	funcs [ _AGB_OP_SDL_EVENT_REPAINT ] = ( void* ) agb_virtual_library_sdl_repaint ;
	funcs [ _AGB_OP_SDL_EVENT_USERCODE ] = ( void* ) agb_virtual_library_sdl_usercode ;
	funcs [ _AGB_OP_SDL_EVENT_MOUSE_MOTION_X ] = ( void* ) agb_virtual_library_sdl_mouse_motion_x ;
	funcs [ _AGB_OP_SDL_EVENT_MOUSE_MOTION_Y ] = ( void* ) agb_virtual_library_sdl_mouse_motion_y ;
	funcs [ _AGB_OP_SDL_EVENT_MOUSE_ACTION_X ] = ( void* ) agb_virtual_library_sdl_mouse_action_x ;
	funcs [ _AGB_OP_SDL_EVENT_MOUSE_ACTION_Y ] = ( void* ) agb_virtual_library_sdl_mouse_action_y ;
	funcs [ _AGB_OP_SDL_EVENT_MOUSE_ACTION_BUTTON ] = ( void* ) agb_virtual_library_sdl_mouse_action_button ;
	funcs [ _AGB_OP_SDL_EVENT_KEYCHAR ] = ( void* ) agb_virtual_library_sdl_keychar_event ;
	funcs [ _AGB_OP_SDL_DRAW_RECT ] = ( void* ) agb_virtual_library_sdl_drawrect ;
	funcs [ _AGB_OP_SDL_DRAW_CIRCLE ] = ( void* ) agb_virtual_library_sdl_drawcircle ;
	funcs [ _AGB_OP_SDL_DRAW_STRING ] = ( void* ) agb_virtual_library_sdl_drawstring ;
	funcs [ _AGB_OP_SDL_FONT_WIDTH ] = ( void* ) agb_virtual_library_sdl_font_width ;
	funcs [ _AGB_OP_SDL_FONT_HEIGHT ] = ( void* ) agb_virtual_library_sdl_font_height ;
	funcs [ _AGB_OP_SDL_FILL_RECT ] = ( void* ) agb_virtual_library_sdl_fillrect ;
	return funcs ;
}

