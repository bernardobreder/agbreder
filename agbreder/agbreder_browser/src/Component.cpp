#include "agbreder.h"

Component::Component ( ) {
	this->setX ( 0 ) ;
	this->setY ( 0 ) ;
	this->setHeight ( 16 ) ;
	this->setWidth ( 50 ) ;
	this->setPreferenceWidth ( - 1 ) ;
	this->setPreferenceHeight ( - 1 ) ;
	this->setPadding ( 0 ) ;
}

Component::~Component ( ) {
}

void Component::init ( ) {
}

void Component::paint ( Graphic* g ) {
}

void Component::fireMouseDownEvent ( SDL_Event* event ) {
}

void Component::fireMouseUpEvent ( SDL_Event* event ) {
}

void Component::fireKeyDownEvent ( SDL_Event* event ) {
}

void Component::fireKeyUpEvent ( SDL_Event* event ) {
}

Panel* Component::getParent ( ) {
	return this->parent ;
}

Frame* Component::getFrame ( ) {
	Panel* parent = this->getParent ( ) ;
	while ( parent && ! dynamic_cast < Frame* > ( parent ) ) {
		parent = parent->getParent ( ) ;
	}
	return ( Frame* ) parent ;
}

void Component::setParent ( Panel* value ) {
	this->parent = value ;
}

int Component::getWidth ( ) {
	return this->width ;
}

int Component::getHeight ( ) {
	return this->height ;
}

int Component::getPreferenceWidth ( ) {
	return this->preferenceWidth ;
}

int Component::getPreferenceHeight ( ) {
	return this->preferenceHeight ;
}

int Component::getX ( ) {
	return this->x ;
}

int Component::getY ( ) {
	return this->y ;
}

int Component::getAbsoluteX ( ) {
	return 0 ;
}

int Component::getAbsoluteY ( ) {
	return 0 ;
}

void Component::setWidth ( int value ) {
	this->width = value ;
}

void Component::setHeight ( int value ) {
	this->height = value ;
}

void Component::setPreferenceWidth ( int value ) {
	this->preferenceWidth = value ;
}

void Component::setPreferenceHeight ( int value ) {
	this->preferenceHeight = value ;
}

void Component::setX ( int value ) {
	this->x = value ;
}

void Component::setY ( int value ) {
	this->y = value ;
}

int Component::getPadding ( ) {
	return this->padding ;
}

void Component::setPadding ( int value ) {
	this->padding = value ;
}

int Component::getMinimumWidth ( ) {
	return 10 ;
}

int Component::getMinimumHeight ( ) {
	return 10 ;
}
