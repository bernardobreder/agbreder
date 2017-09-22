#include "agb.h"
#include "agbi.h"

void agb_library_number_print(double value) {
	wchar_t num2str[64];
	int len;
	if (value == (int) value) {
		len = swprintf(num2str, 64, L"%d", (int) value);
	} else {
		len = swprintf(num2str, 64, L"%lf", value);
		wchar_t* aux = num2str + len - 1;
		while (*aux == '0') {
			aux--;
		}
		aux[1] = 0;
	}
	wprintf(L"%ls", num2str);
}
