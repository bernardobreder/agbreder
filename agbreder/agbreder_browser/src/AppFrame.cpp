#include "agbreder.h"

AppFrame::AppFrame ( ) :
		Frame ( ) {
	class LoadButton : public Button {
			virtual void fireActionEvent ( SDL_Event* e ) {
				AppFrame* frame = ( AppFrame* ) this->getFrame ( ) ;
				frame->openPage ( ) ;
			}
	} ;
	Vertical* v = new Vertical ( ) ;
	{
		Horizontal* h = new Horizontal ( ) ;
		h->addComponent ( new Text ( ) , true ) ;
		{
			Button* c = new LoadButton ( ) ;
			c->setText ( "Go" ) ;
			this->setButton ( c ) ;
			h->addComponent ( c ) ;
		}
		v->addComponent ( h ) ;
	}
	v->addComponent ( & appPanel ) ;
	this->setComponent ( v ) ;
}

AppFrame::~AppFrame ( ) {
}

void AppFrame::openPage ( ) {
	this->appPanel.open ( ) ;
}
