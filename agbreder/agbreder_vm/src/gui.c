#include "agb.h"
#include "agbi.h"
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "SDL/SDL.h"
#include "SDL/SDL_ttf.h"

#define PITCH(S) ((S)->pitch / 4)
#define SDL_KEY_FIRST 1024

static SDL_Surface* createSurface(int width, int height) {
	SDL_Surface* surface = SDL_CreateRGBSurface(SDL_SWSURFACE, width, height, 32, 0x00ff0000, 0x0000ff00, 0x000000ff,
			0x00000000);
	SDL_FillRect(surface, 0, 0xff000000);
	return surface;
}

static TTF_Font* arialFont(agb_thread_t* thread) {
	TTF_Font* font = thread->vm->guiData->arial14;
	if (!font && thread->vm->parentVm) {
		font = thread->vm->guiData->arial14 = thread->vm->parentVm->guiData->arial14;
	}
	return font;
}

wchar_t* agb_library_sdl_error(agb_thread_t* thread) {
	return 0;
}

int agb_library_sdl_init(agb_thread_t* thread) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!thread->vm->parentVm) {
		{
			putenv("SDL_VIDEO_WINDOW_POS");
			putenv("SDL_VIDEO_CENTERED=1");
			if (SDL_Init(SDL_INIT_EVERYTHING) < 0) {
				return 0;
			}
		}
		{
			if (TTF_Init() == -1) {
				return 0;
			}
			guiData->arial14 = TTF_OpenFont("font/arial.ttf", 14);
		}
		{
			SDL_EnableUNICODE(1);
			SDL_EnableKeyRepeat(200, 10);
		}
		thread->vm->guiLock = SDL_CreateMutex();
	} else {
		thread->vm->guiLock = thread->vm->parentVm->guiLock;
	}
	return 1;
}

void agb_library_sdl_quit(agb_thread_t* thread) {
	agb_library_vm_free(thread);
	AGB_GuiData* guiData = thread->vm->guiData;
	agb_memory_free(guiData->surface->userdata);
	if (!thread->vm->parentVm) {
		SDL_DestroyMutex(thread->vm->guiLock);
		SDL_Quit();
		TTF_Quit();
	} else {
		SDL_FreeSurface(guiData->surface);
	}
}

int agb_library_sdl_video(agb_thread_t* thread, int width, int height) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!thread->vm->parentVm) {
		const SDL_VideoInfo* info = SDL_GetVideoInfo();
		guiData->surface = SDL_SetVideoMode(info->current_w - 100, info->current_h - 100, 32, SDL_SWSURFACE);
	} else {
		AGB_GuiData* guiData = thread->vm->guiData;
		guiData->surface = createSurface(guiData->width, guiData->height);
	}
	int* size = agb_memory_alloc(2 * sizeof(int));
	size[0] = 0;
	size[1] = 0;
	guiData->surface->userdata = size;
	return 1;
}

int agb_library_sdl_screen_width(agb_thread_t* thread) {
	return thread->vm->guiData->surface->w;
}

int agb_library_sdl_screen_height(agb_thread_t* thread) {
	return thread->vm->guiData->surface->h;
}

int agb_library_sdl_pool_event(agb_thread_t* thread) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!thread->vm->parentVm) {
		int flag = SDL_PollEvent(&guiData->event) == 1;
		if (guiData->event.type == SDL_MOUSEWHEEL) {
			guiData->event.motion.xrel = guiData->event.wheel.x;
			guiData->event.motion.yrel = guiData->event.wheel.y;
			SDL_GetMouseState(&guiData->event.motion.x, &guiData->event.motion.y);
		}
		return flag;
	} else {
		SDL_mutexP(thread->vm->guiLock);
		int flag = guiData->eventSize > 0;
		if (guiData->eventSize > 0) {
			guiData->event = guiData->events[--guiData->eventSize];
		}
		SDL_mutexV(thread->vm->guiLock);
		return flag;
	}
}

void agb_library_sdl_wait_event(agb_thread_t* thread) {
	for (;;) {
		if (agb_library_sdl_pool_event(thread)) {
			return;
		}
		SDL_Delay(10);
	}
}

int agb_library_sdl_type_event(agb_thread_t* thread) {
	return thread->vm->guiData->event.type;
}

int agb_library_sdl_keycode_event(agb_thread_t* thread) {
	return thread->vm->guiData->event.key.keysym.sym;
}

int agb_library_sdl_keychar_event(agb_thread_t* thread) {
	return thread->vm->guiData->event.key.keysym.unicode;
}

int agb_library_sdl_lock(agb_thread_t* thread) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!thread->vm->parentVm) {
		if (SDL_MUSTLOCK(guiData->surface)) {
			if (SDL_LockSurface(guiData->surface) < 0) {
				return 0;
			}
		}
	}
	SDL_mutexP(thread->vm->guiLock);
	return 1;
}

void agb_library_sdl_unlock(agb_thread_t* thread) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!thread->vm->parentVm) {
		if (SDL_MUSTLOCK(guiData->surface)) {
			SDL_UnlockSurface(guiData->surface);
		}
	}
	SDL_mutexV(thread->vm->guiLock);
}

void agb_library_sdl_update(agb_thread_t* thread, int x, int y) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!thread->vm->parentVm) {
		SDL_UpdateRect(guiData->surface, x, y, guiData->surface->w, guiData->surface->h);
	} else {
		SDL_Event event;
		event.type = SDL_USEREVENT;
		event.user.code = 1;
		SDL_PushEvent(&event);
	}
}

void agb_library_sdl_push_event_key(agb_thread_t* thread, u_int32_t type, u_int32_t button) {
	if (!thread->vm->parentVm) {
		if (thread->vm->vmChild) {
			AGB_GuiData* guiData = thread->vm->vmChild->guiData;
			SDL_Event event;
			event.type = type;
			event.key.keysym.unicode = button;
			event.key.keysym.sym = button;
			SDL_mutexP(thread->vm->guiLock);
			if (guiData->eventSize < 1024) {
				guiData->events[guiData->eventSize++] = event;
			}
			SDL_mutexV(thread->vm->guiLock);
		}
	}
}

void agb_library_sdl_push_event_mouse(agb_thread_t* thread, u_int32_t type, u_int32_t x, u_int32_t y) {
	if (!thread->vm->parentVm) {
		if (thread->vm->vmChild) {
			AGB_GuiData* guiData = thread->vm->vmChild->guiData;
			SDL_Event event;
			event.type = type;
			event.motion.x = x;
			event.motion.y = y;
			SDL_mutexP(thread->vm->guiLock);
			if (guiData->eventSize < 1024) {
				guiData->events[guiData->eventSize++] = event;
			}
			SDL_mutexV(thread->vm->guiLock);
		}
	}
}

void agb_library_sdl_push_event_wheel(agb_thread_t* thread, u_int32_t type, u_int32_t x, u_int32_t y, u_int32_t xrel, u_int32_t yrel) {
	if (!thread->vm->parentVm) {
		if (thread->vm->vmChild) {
			AGB_GuiData* guiData = thread->vm->vmChild->guiData;
			SDL_Event event;
			event.type = type;
			event.motion.x = x;
			event.motion.y = y;
			event.motion.xrel = xrel;
			event.motion.yrel = yrel;
			SDL_mutexP(thread->vm->guiLock);
			if (guiData->eventSize < 1024) {
				guiData->events[guiData->eventSize++] = event;
			}
			SDL_mutexV(thread->vm->guiLock);
		}
	}
}

int agb_library_sdl_tick(agb_thread_t* thread) {
	return SDL_GetTicks();
}

void agb_library_sdl_delay(int miliseg) {
	SDL_Delay(miliseg);
}

int agb_library_sdl_usercode(agb_thread_t* thread) {
	return thread->vm->guiData->event.user.code = 1;
}

void agb_library_sdl_fillrect(agb_thread_t* thread, int x, int y, int width, int height, uint32_t c) {
	AGB_GuiData* guiData = thread->vm->guiData;
	SDL_Surface* surface = guiData->screensSize == 0 ? guiData->surface : guiData->screens[guiData->screensSize - 1];
	int* size = (int*) surface->userdata;
	SDL_Rect rect = { x - size[0], y - size[1], width, height };
	SDL_FillRect(surface, &rect, c);
}

void agb_library_sdl_drawrect(agb_thread_t* thread, int x, int y, int width, int height, uint32_t c) {
	agb_library_sdl_fillrect(thread, x, y, width, 1, c);
	agb_library_sdl_fillrect(thread, x, y, 1, height, c);
	agb_library_sdl_fillrect(thread, x + width - 1, y, 1, height, c);
	agb_library_sdl_fillrect(thread, x, y + height - 1, width, 1, c);
}

void agb_library_sdl_drawstring(agb_thread_t* thread, int x, int y, wchar_t* text, uint32_t c) {
	AGB_GuiData* guiData = thread->vm->guiData;
	SDL_Surface* surface = guiData->screensSize == 0 ? guiData->surface : guiData->screens[guiData->screensSize - 1];
	int* size = (int*) surface->userdata;
	TTF_Font* font = arialFont(thread);
	int w, h;
	int n, len = wcslen(text);
	char chars[len + 1];
	for (n = 0; n < len; n++) {
		chars[n] = text[n];
	}
	chars[len] = 0;
	TTF_SizeText(font, chars, &w, &h);
	SDL_Rect rect = { x - size[0], y - size[1], 0, 0 };
	if (rect.y + h > 0 && rect.y < surface->h) {
		if (rect.x + w > 0 && rect.x < surface->w) {
			SDL_Color foregroundColor = { (c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF, 0x00 };
			SDL_Surface* textSurface = TTF_RenderText_Solid(font, chars, foregroundColor);
			SDL_BlitSurface(textSurface, 0, surface, &rect);
			SDL_FreeSurface(textSurface);
		}
	}
}

void agb_library_sdl_push_graphic(agb_thread_t* thread, int x, int y, int width, int height) {
	if (width < 0) {
		width = 1;
	}
	if (height < 0) {
		height = 1;
	}
	AGB_GuiData* guiData = thread->vm->guiData;
	SDL_Surface* surface = createSurface(width, height);
	int* size = agb_memory_alloc(2 * sizeof(int));
	size[0] = x;
	size[1] = y;
	surface->userdata = size;
	guiData->screens[guiData->screensSize++] = surface;
}

void agb_library_sdl_pop_graphic(agb_thread_t* thread, int x, int y) {
	AGB_GuiData* guiData = thread->vm->guiData;
	if (!guiData->screensSize) {
		return;
	}
	SDL_Surface* src = guiData->screens[--guiData->screensSize];
	SDL_Surface* dst = guiData->screensSize == 0 ? guiData->surface : guiData->screens[guiData->screensSize - 1];
	int* dstSize = dst->userdata;
	int srcX = 0;
	int srcX2 = src->w;
	int dstX = x - dstSize[0];
	if (dstX < 0) {
		srcX -= dstX;
		dstX = 0;
	}
	if (dstX + srcX2 > dst->w) {
		srcX2 = dst->w;
	}
	int srcY = 0;
	int srcY2 = src->h;
	int dstY = y - dstSize[1];
	if (dstY < 0) {
		srcY -= dstY;
		dstY = 0;
	}
	if (dstY + srcY2 > dst->h) {
		srcY2 = dst->h;
	}
	unsigned int* srcPixels = (unsigned int*) src->pixels;
	unsigned int* dstPixels = (unsigned int*) dst->pixels;
	int i, j;
	for (i = srcY; i < srcY2; i++, dstY++) {
		if (dstY < dst->h) {
			register unsigned int* srcPixel = srcPixels + i * PITCH(src) + srcX;
			register unsigned int* dstPixel = dstPixels + dstY * PITCH(dst) + dstX;
			for (j = srcX; j < srcX2; j++) {
				if (dstX < dst->w) {
					register uint32_t c = *srcPixel++;
					if ((c & 0xff000000) == 0) {
						*dstPixel = c;
					}
					dstPixel++;
				}
			}
		}
	}
	agb_memory_free(src->userdata);
	SDL_FreeSurface(src);
}

int agb_library_sdl_font_width(agb_thread_t* thread, wchar_t* text) {
	if (!text) {
		return 0;
	}
	TTF_Font* font = arialFont(thread);
	if (!font) {
		return 0;
	}
	int n, size = wcslen(text);
	char chars[size + 1];
	for (n = 0; n < size; n++) {
		chars[n] = text[n];
	}
	chars[size] = 0;
	SDL_mutexP(thread->vm->guiLock);
	TTF_SizeUTF8(font, chars, &n, 0);
	SDL_mutexV(thread->vm->guiLock);
	return n;
}

int agb_library_sdl_font_height(agb_thread_t* thread, wchar_t* text) {
	if (!text) {
		return 0;
	}
	TTF_Font* font = arialFont(thread);
	if (!font) {
		return 0;
	}
	int n, size = wcslen(text);
	char chars[size + 1];
	for (n = 0; n < size; n++) {
		chars[n] = text[n];
	}
	chars[size] = 0;
	SDL_mutexP(thread->vm->guiLock);
	TTF_SizeUTF8(font, chars, 0, &n);
	SDL_mutexV(thread->vm->guiLock);
	return n;
}

int agb_library_sdl_mouse_motion_x(agb_thread_t* thread) {
	AGB_GuiData* guiData = thread->vm->guiData;
	return guiData->event.motion.x;
}

int agb_library_sdl_mouse_motion_y(agb_thread_t* thread) {
	AGB_GuiData* guiData = thread->vm->guiData;
	return guiData->event.motion.y;
}

int agb_library_sdl_mouse_action_x(agb_thread_t* thread) {
	return thread->vm->guiData->event.button.x;
}

int agb_library_sdl_mouse_action_y(agb_thread_t* thread) {
	return thread->vm->guiData->event.button.y;
}

int agb_library_sdl_mouse_wheel_x(agb_thread_t* thread) {
	return thread->vm->guiData->event.motion.xrel;
}

int agb_library_sdl_mouse_wheel_y(agb_thread_t* thread) {
	return thread->vm->guiData->event.motion.yrel;
}

int agb_library_sdl_mouse_action_button(agb_thread_t* thread) {
	return thread->vm->guiData->event.button.button;
}

int agb_library_sdl_constant(agb_thread_t* thread, int value) {
	switch (value) {
	case 1:
		return SDL_QUIT;
	case 2:
		return SDL_KEYDOWN;
	case 3:
		return SDL_KEYUP;
	case 4:
		return SDL_MOUSEMOTION;
	case 5:
		return SDL_MOUSEBUTTONDOWN;
	case 6:
		return SDL_MOUSEBUTTONUP;
	case 7:
		return SDL_MOUSEWHEEL;
	case 8:
		return SDL_USEREVENT;
	case 9:
		return SDL_RESIZABLE;
	case SDL_KEY_FIRST + 0:
		return SDLK_BACKSPACE;
	case SDL_KEY_FIRST + 1:
		return SDLK_TAB;
	case SDL_KEY_FIRST + 2:
		return SDLK_CLEAR;
	case SDL_KEY_FIRST + 3:
		return SDLK_RETURN;
	case SDL_KEY_FIRST + 4:
		return SDLK_PAUSE;
	case SDL_KEY_FIRST + 5:
		return SDLK_ESCAPE;
	case SDL_KEY_FIRST + 6:
		return SDLK_SPACE;
	case SDL_KEY_FIRST + 7:
		return SDLK_EXCLAIM;
	case SDL_KEY_FIRST + 8:
		return SDLK_QUOTEDBL;
	case SDL_KEY_FIRST + 9:
		return SDLK_HASH;
	case SDL_KEY_FIRST + 10:
		return SDLK_DOLLAR;
	case SDL_KEY_FIRST + 11:
		return SDLK_AMPERSAND;
	case SDL_KEY_FIRST + 12:
		return SDLK_QUOTE;
	case SDL_KEY_FIRST + 13:
		return SDLK_LEFTPAREN;
	case SDL_KEY_FIRST + 14:
		return SDLK_RIGHTPAREN;
	case SDL_KEY_FIRST + 15:
		return SDLK_ASTERISK;
	case SDL_KEY_FIRST + 16:
		return SDLK_PLUS;
	case SDL_KEY_FIRST + 17:
		return SDLK_COMMA;
	case SDL_KEY_FIRST + 18:
		return SDLK_MINUS;
	case SDL_KEY_FIRST + 19:
		return SDLK_PERIOD;
	case SDL_KEY_FIRST + 20:
		return SDLK_SLASH;
	case SDL_KEY_FIRST + 21:
		return SDLK_0;
	case SDL_KEY_FIRST + 22:
		return SDLK_1;
	case SDL_KEY_FIRST + 23:
		return SDLK_2;
	case SDL_KEY_FIRST + 24:
		return SDLK_3;
	case SDL_KEY_FIRST + 25:
		return SDLK_4;
	case SDL_KEY_FIRST + 26:
		return SDLK_5;
	case SDL_KEY_FIRST + 27:
		return SDLK_6;
	case SDL_KEY_FIRST + 28:
		return SDLK_7;
	case SDL_KEY_FIRST + 29:
		return SDLK_8;
	case SDL_KEY_FIRST + 30:
		return SDLK_9;
	case SDL_KEY_FIRST + 31:
		return SDLK_COLON;
	case SDL_KEY_FIRST + 32:
		return SDLK_SEMICOLON;
	case SDL_KEY_FIRST + 33:
		return SDLK_LESS;
	case SDL_KEY_FIRST + 34:
		return SDLK_EQUALS;
	case SDL_KEY_FIRST + 35:
		return SDLK_GREATER;
	case SDL_KEY_FIRST + 36:
		return SDLK_QUESTION;
	case SDL_KEY_FIRST + 37:
		return SDLK_AT;
	case SDL_KEY_FIRST + 38:
		return SDLK_LEFTBRACKET;
	case SDL_KEY_FIRST + 39:
		return SDLK_BACKSLASH;
	case SDL_KEY_FIRST + 40:
		return SDLK_RIGHTBRACKET;
	case SDL_KEY_FIRST + 41:
		return SDLK_CARET;
	case SDL_KEY_FIRST + 42:
		return SDLK_UNDERSCORE;
	case SDL_KEY_FIRST + 43:
		return SDLK_BACKQUOTE;
	case SDL_KEY_FIRST + 44:
		return SDLK_a;
	case SDL_KEY_FIRST + 45:
		return SDLK_b;
	case SDL_KEY_FIRST + 46:
		return SDLK_c;
	case SDL_KEY_FIRST + 47:
		return SDLK_d;
	case SDL_KEY_FIRST + 48:
		return SDLK_e;
	case SDL_KEY_FIRST + 49:
		return SDLK_f;
	case SDL_KEY_FIRST + 50:
		return SDLK_g;
	case SDL_KEY_FIRST + 51:
		return SDLK_h;
	case SDL_KEY_FIRST + 52:
		return SDLK_i;
	case SDL_KEY_FIRST + 53:
		return SDLK_j;
	case SDL_KEY_FIRST + 54:
		return SDLK_k;
	case SDL_KEY_FIRST + 55:
		return SDLK_l;
	case SDL_KEY_FIRST + 56:
		return SDLK_m;
	case SDL_KEY_FIRST + 57:
		return SDLK_n;
	case SDL_KEY_FIRST + 58:
		return SDLK_o;
	case SDL_KEY_FIRST + 59:
		return SDLK_p;
	case SDL_KEY_FIRST + 60:
		return SDLK_q;
	case SDL_KEY_FIRST + 61:
		return SDLK_r;
	case SDL_KEY_FIRST + 62:
		return SDLK_s;
	case SDL_KEY_FIRST + 63:
		return SDLK_t;
	case SDL_KEY_FIRST + 64:
		return SDLK_u;
	case SDL_KEY_FIRST + 65:
		return SDLK_v;
	case SDL_KEY_FIRST + 66:
		return SDLK_w;
	case SDL_KEY_FIRST + 67:
		return SDLK_x;
	case SDL_KEY_FIRST + 68:
		return SDLK_y;
	case SDL_KEY_FIRST + 69:
		return SDLK_z;
	case SDL_KEY_FIRST + 70:
		return SDLK_DELETE;
	case SDL_KEY_FIRST + 167:
		return SDLK_KP0;
	case SDL_KEY_FIRST + 168:
		return SDLK_KP1;
	case SDL_KEY_FIRST + 169:
		return SDLK_KP2;
	case SDL_KEY_FIRST + 170:
		return SDLK_KP3;
	case SDL_KEY_FIRST + 171:
		return SDLK_KP4;
	case SDL_KEY_FIRST + 172:
		return SDLK_KP5;
	case SDL_KEY_FIRST + 173:
		return SDLK_KP6;
	case SDL_KEY_FIRST + 174:
		return SDLK_KP7;
	case SDL_KEY_FIRST + 175:
		return SDLK_KP8;
	case SDL_KEY_FIRST + 176:
		return SDLK_KP9;
	case SDL_KEY_FIRST + 177:
		return SDLK_KP_PERIOD;
	case SDL_KEY_FIRST + 178:
		return SDLK_KP_DIVIDE;
	case SDL_KEY_FIRST + 179:
		return SDLK_KP_MULTIPLY;
	case SDL_KEY_FIRST + 180:
		return SDLK_KP_MINUS;
	case SDL_KEY_FIRST + 181:
		return SDLK_KP_PLUS;
	case SDL_KEY_FIRST + 182:
		return SDLK_KP_ENTER;
	case SDL_KEY_FIRST + 183:
		return SDLK_KP_EQUALS;
	case SDL_KEY_FIRST + 184:
		return SDLK_UP;
	case SDL_KEY_FIRST + 185:
		return SDLK_DOWN;
	case SDL_KEY_FIRST + 186:
		return SDLK_RIGHT;
	case SDL_KEY_FIRST + 187:
		return SDLK_LEFT;
	case SDL_KEY_FIRST + 188:
		return SDLK_INSERT;
	case SDL_KEY_FIRST + 189:
		return SDLK_HOME;
	case SDL_KEY_FIRST + 190:
		return SDLK_END;
	case SDL_KEY_FIRST + 191:
		return SDLK_PAGEUP;
	case SDL_KEY_FIRST + 192:
		return SDLK_PAGEDOWN;
	case SDL_KEY_FIRST + 193:
		return SDLK_F1;
	case SDL_KEY_FIRST + 194:
		return SDLK_F2;
	case SDL_KEY_FIRST + 195:
		return SDLK_F3;
	case SDL_KEY_FIRST + 196:
		return SDLK_F4;
	case SDL_KEY_FIRST + 197:
		return SDLK_F5;
	case SDL_KEY_FIRST + 198:
		return SDLK_F6;
	case SDL_KEY_FIRST + 199:
		return SDLK_F7;
	case SDL_KEY_FIRST + 200:
		return SDLK_F8;
	case SDL_KEY_FIRST + 201:
		return SDLK_F9;
	case SDL_KEY_FIRST + 202:
		return SDLK_F10;
	case SDL_KEY_FIRST + 203:
		return SDLK_F11;
	case SDL_KEY_FIRST + 204:
		return SDLK_F12;
	case SDL_KEY_FIRST + 205:
		return SDLK_F13;
	case SDL_KEY_FIRST + 206:
		return SDLK_F14;
	case SDL_KEY_FIRST + 207:
		return SDLK_F15;
	case SDL_KEY_FIRST + 208:
		return SDLK_NUMLOCK;
	case SDL_KEY_FIRST + 209:
		return SDLK_CAPSLOCK;
	case SDL_KEY_FIRST + 210:
		return SDLK_SCROLLOCK;
	case SDL_KEY_FIRST + 211:
		return SDLK_RSHIFT;
	case SDL_KEY_FIRST + 212:
		return SDLK_LSHIFT;
	case SDL_KEY_FIRST + 213:
		return SDLK_RCTRL;
	case SDL_KEY_FIRST + 214:
		return SDLK_LCTRL;
	case SDL_KEY_FIRST + 215:
		return SDLK_RALT;
	case SDL_KEY_FIRST + 216:
		return SDLK_LALT;
	case SDL_KEY_FIRST + 217:
		return SDLK_RMETA;
	case SDL_KEY_FIRST + 218:
		return SDLK_LMETA;
	case SDL_KEY_FIRST + 219:
		return SDLK_LSUPER;
	case SDL_KEY_FIRST + 220:
		return SDLK_RSUPER;
	case SDL_KEY_FIRST + 221:
		return SDLK_COMPOSE;
	case SDL_KEY_FIRST + 222:
		return SDLK_HELP;
	case SDL_KEY_FIRST + 223:
		return SDLK_PRINT;
	case SDL_KEY_FIRST + 224:
		return SDLK_SYSREQ;
	case SDL_KEY_FIRST + 225:
		return SDLK_BREAK;
	case SDL_KEY_FIRST + 226:
		return SDLK_MENU;
	case SDL_KEY_FIRST + 227:
		return SDLK_POWER;
	case SDL_KEY_FIRST + 228:
		return SDLK_EURO;
	case SDL_KEY_FIRST + 229:
		return SDLK_UNDO;
	}
	return -1;
}
