#ifndef _AGB_BROWSER_
#define _AGB_BROWSER_

class Frame ;
class Panel ;
class Button ;

#include <string>
#include <vector>
#include <queue>
#include <SDL2/SDL.h>
#include <SDL2/SDL_ttf.h>
#include <SDL2/SDL_net.h>
#include "agb.h"
#include "agbi.h"

using namespace std ;

typedef struct agb_library_sdl_object {
		SDL_Thread* thread ;
		SDL_Event event ;
		SDL_Surface* surface ;
		TTF_Font* arial12 ;
		int width , height ;
		SDL_mutex* eventLock ;
		SDL_mutex* guiLock ;
		queue < SDL_Event > events ;
} agb_library_sdl_object ;

void** agb_virtual_library_sdl ( agb_library_sdl_object* data ) ;

class Thread {
	public:
		SDL_Thread* thread ;bool running ;
	public:
		Thread ( ) ;

		virtual ~Thread ( ) ;
		virtual Thread* start ( ) ;
		virtual int run ( ) ;
} ;

class Graphic {
	protected:
		SDL_Surface* screen ;
		int x , y ;
	public:
		Graphic ( ) ;

		bool lock ( ) ;
		void unlock ( ) ;
		void flush ( ) ;
		void drawRect ( int x , int y , int w , int h , int c ) ;
		void fillRect ( int x , int y , int w , int h , int c ) ;
		void drawCircle ( int x , int y , int r , int c ) ;
		void drawText ( int x , int y , string text , int c ) ;
		void translate ( int x , int y ) ;
		void drawImage ( SDL_Surface* src , SDL_Rect* srcRect , SDL_Rect* destRect ) ;
		void setSurface ( SDL_Surface* surface ) ;
		SDL_Surface* getSurface ( ) ;
} ;

class Font {
	protected:
	public:
		Font ( ) ;
		virtual ~Font ( ) ;

		virtual int getWidth ( string text ) ;
		virtual int getHeight ( string text ) ;
} ;

class ArialFont : public Font {
	protected:
		TTF_Font* font ;
	public:
		ArialFont ( ) ;
		virtual ~ArialFont ( ) ;

		virtual int getWidth ( string text ) ;
		virtual int getHeight ( string text ) ;
} ;

class Component {
	private:
		int x ;
		int y ;
		int width ;
		int height ;
		int preferenceWidth ;
		int preferenceHeight ;
		int padding ;
		Panel* parent ;
	public:
		Component ( ) ;
		virtual ~Component ( ) ;

		virtual void init ( ) ;
		virtual void paint ( Graphic* g ) ;

		virtual void fireMouseDownEvent ( SDL_Event* ) ;
		virtual void fireMouseUpEvent ( SDL_Event* ) ;
		virtual void fireKeyDownEvent ( SDL_Event* ) ;
		virtual void fireKeyUpEvent ( SDL_Event* ) ;

		virtual Panel* getParent ( ) ;
		virtual Frame* getFrame ( ) ;
		virtual void setParent ( Panel* value ) ;
		virtual int getWidth ( ) ;
		virtual void setWidth ( int value ) ;
		virtual int getHeight ( ) ;
		virtual void setHeight ( int value ) ;
		virtual int getPreferenceWidth ( ) ;
		virtual void setPreferenceWidth ( int value ) ;
		virtual int getPreferenceHeight ( ) ;
		virtual void setPreferenceHeight ( int value ) ;

		virtual int getMinimumWidth ( ) ;
		virtual int getMinimumHeight ( ) ;

		virtual int getX ( ) ;
		virtual void setX ( int value ) ;
		virtual int getY ( ) ;
		virtual void setY ( int value ) ;
		virtual int getPadding ( ) ;
		virtual void setPadding ( int value ) ;
		virtual int getAbsoluteX ( ) ;
		virtual int getAbsoluteY ( ) ;
} ;

class Panel : public Component {
	protected:
		vector < Component* > list ;
		int margin ;
	public:
		Panel ( ) ;
		virtual ~Panel ( ) ;

		virtual void init ( ) ;
		virtual void paint ( Graphic* g ) ;
		virtual void layout ( ) ;

		virtual void fireMouseDownEvent ( SDL_Event* ) ;
		virtual void fireMouseUpEvent ( SDL_Event* ) ;
		virtual void fireKeyDownEvent ( SDL_Event* ) ;
		virtual void fireKeyUpEvent ( SDL_Event* ) ;

		virtual int getMargin ( ) ;
		virtual void setMargin ( int value ) ;

		virtual Component* getComponent ( int index ) ;
		virtual int size ( ) ;
} ;

class Frame : public Panel {
	private:
		Graphic g ;
		Component* component ;
		Component* focus ;
		Button* button ;
	public:
		Frame ( ) ;

		agb_library_sdl_object* create ( ) ;
		virtual void loop ( ) ;
		virtual void repaint ( ) ;
		virtual void paint ( Graphic* g ) ;
		virtual void layout ( ) ;

		virtual void dispatchMouseDownEvent ( SDL_Event* ) ;
		virtual void dispatchMouseUpEvent ( SDL_Event* ) ;
		virtual void dispatchKeyDownEvent ( SDL_Event* ) ;
		virtual void dispatchKeyUpEvent ( SDL_Event* ) ;
		virtual void dispatchRepaintEvent ( SDL_Event* ) ;

		void setComponent ( Component* value ) ;
		Component* getComponent ( ) ;
		void setFocus ( Component* value ) ;
		Component* getFocus ( ) ;
		void setButton ( Button* value ) ;
		Button* getButton ( ) ;

		virtual int getWidth ( ) ;
		virtual int getHeight ( ) ;
} ;

class Label : public Component {
	private:
		string text ;
		Font* font ;
		unsigned int backColor ;
		unsigned int foreColor ;
		bool opaque ;
	public:
		Label ( ) ;
		virtual ~Label ( ) ;

		virtual void paint ( Graphic* g ) ;

		virtual void setText ( string text ) ;
		virtual string getText ( ) ;

		virtual void setFont ( Font* font ) ;
		virtual Font* getFont ( ) ;
		virtual void setBackColor ( int value ) ;
		virtual int getBackColor ( ) ;
		virtual int getDarkBackColor ( ) ;
		virtual int getLightBackColor ( ) ;
		virtual void setForeColor ( int value ) ;
		virtual int getForeColor ( ) ;

		virtual int getMinimumWidth ( ) ;
		virtual int getMinimumHeight ( ) ;
} ;

class Text : public Label {
	private:
		unsigned int cursor ;
		unsigned int cursorX ;
	public:
		Text ( ) ;
		virtual ~Text ( ) ;

		virtual void paint ( Graphic* g ) ;

		virtual void fireMouseDownEvent ( SDL_Event* e ) ;
		virtual void fireKeyDownEvent ( SDL_Event* e ) ;

		virtual int getMinimumWidth ( ) ;
		virtual int getMinimumHeight ( ) ;
} ;

class Button : public Label {
	private:
	public:
		Button ( ) ;
		virtual ~Button ( ) ;

		virtual void paint ( Graphic* g ) ;
		virtual void layout ( ) ;

		virtual void fireMouseDownEvent ( SDL_Event* e ) ;
		virtual void fireActionEvent ( SDL_Event* e ) ;

		virtual int getMinimumWidth ( ) ;
		virtual int getMinimumHeight ( ) ;
} ;

class Vertical : public Panel {
	private:
	public:
		Vertical ( ) ;

		virtual ~Vertical ( ) ;
		virtual void addComponent ( Component* c ) ;
		virtual void paint ( Graphic* g ) ;
		virtual void layout ( ) ;
} ;

class Horizontal : public Panel {
	private:
		vector < int > expandables ;
	public:
		Horizontal ( ) ;

		virtual ~Horizontal ( ) ;

		virtual void paint ( Graphic* g ) ;
		virtual void layout ( ) ;

		virtual void addComponent ( Component* c , bool expandable = false ) ;
} ;

class AppThread : public Thread {
	private:
		agb_library_sdl_object* data ;
	public:
		AppThread ( agb_library_sdl_object* data ) ;

		virtual ~AppThread ( ) ;
		virtual int run ( ) ;
		virtual unsigned char* load ( ) ;
		virtual int vm ( unsigned char* buffer ) ;
} ;

class AppPanel : public Panel {
	protected:
		AppThread* thread ;
		agb_library_sdl_object* data ;
	public:
		AppPanel ( ) ;
		virtual ~AppPanel ( ) ;

		virtual void init ( ) ;
		virtual void paint ( Graphic* g ) ;
		virtual void open ( ) ;

		virtual void fireMouseDownEvent ( SDL_Event* ) ;
		virtual void fireMouseUpEvent ( SDL_Event* ) ;
		virtual void fireKeyDownEvent ( SDL_Event* ) ;
		virtual void fireKeyUpEvent ( SDL_Event* ) ;
} ;

class AppFrame : public Frame {
	protected:
		AppPanel appPanel ;
	public:
		AppFrame ( ) ;
		virtual ~AppFrame ( ) ;

		virtual void openPage ( ) ;
} ;

#endif
