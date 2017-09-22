#include "agb.h"
#include "agbi.h"
#include <SDL/SDL.h>
#include <SDL/SDL_net.h>

agb_object_t* agb_library_net_request(agb_thread_t* thread, wchar_t* host, unsigned short port, wchar_t* request) {
	if (SDL_Init(SDL_INIT_EVERYTHING) < 0) {
		return 0;
	}
	if (SDLNet_Init() < 0) {
		return 0;
	}
	if (!host) {
		host = L"localhost";
	}
	char hostChars[wcslen(host) + 1];
	agb_wchar_copy_to_chars(host, hostChars);
	if (port <= 0) {
		port = 9889;
	}
	IPaddress address;
	if (SDLNet_ResolveHost(&address, hostChars, port) != 0) {
		return 0;
	}
	TCPsocket socket = SDLNet_TCP_Open(&address);
	if (!socket) {
		return 0;
	}
	if (!request) {
		request = L"";
	}
	int requestSize = wcslen(request);
	char requestChars[requestSize + 1];
	agb_wchar_copy_to_chars(request, requestChars);
	if (SDLNet_TCP_Send(socket, requestChars, requestSize + 1) != requestSize + 1) {
		SDLNet_TCP_Close(socket);
		return 0;
	}
	long max = 1024;
	long size = 0;
	unsigned char* data = agb_memory_alloc(max * sizeof(unsigned char));
	if (!data) {
		SDLNet_TCP_Close(socket);
		return 0;
	}
	for (;;) {
		if (SDLNet_TCP_Recv(socket, data + size, 1) == 0) {
			break;
		}
		if (++size == max) {
			max *= 2;
			unsigned char* aux = agb_memory_realloc(data, max * sizeof(unsigned char));
			if (!aux) {
				agb_memory_free(data);
				SDLNet_TCP_Close(socket);
				return 0;
			}
			data = aux;
		}
	}
	data[size] = 0;
	SDLNet_TCP_Close(socket);
	return agb_library_bytes_new(thread, data, size);
}
