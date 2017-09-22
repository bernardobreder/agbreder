#include "agb.h"
#include "agbi.h"
#include "sqlite3.h"
#include "xdelta3/xdelta3.h"

static int test(int argc, char* argv[]) {
	agb_test();
	return EXIT_SUCCESS;
}

static int vm(int argc, char* argv[]) {
	char* filename = "binary.agbc";
	if (argc > 1) {
		filename = argv[1];
	}
	wchar_t* mainClass = L"Main";
	if (argc > 2) {
		mainClass = agb_wchar_encode((unsigned char*) argv[2], strlen(argv[2]), 0);
	}
	unsigned char* buffer = agb_file_read((const char*) filename, 0);
	if (!buffer) {
		printf("Not correct file : %s\n", filename);
		return EXIT_FAILURE;
	}
	agb_vm_t* vm = agb_vm_new(buffer);
	free(buffer);
	if (!vm) {
		return EXIT_FAILURE;
	}
	agb_thread_t* thread = agb_thread_new(vm);
	if (!thread) {
		return EXIT_FAILURE;
	}
	agb_method_t* method = agb_method_classname(vm, mainClass, L"main()", 1);
	if (!method) {
		return EXIT_FAILURE;
	}
	int pc = agb_method_pc(method);
	int flag = agb_vm_execute(thread, pc);
	if (flag != EXIT_SUCCESS) {
		return flag;
	}
	agb_vm_free(vm);
	return EXIT_SUCCESS;
}

void diff() {
	FILE* InFile;
	FILE* SrcFile;
	FILE* OutFile;
	int r;
	char *input = "../bp2pb.diff/eclipse-64.zip";
	char *source = "../bp2pb.diff/eclipse-32.zip";
	const char *output = "encoded.testdata";
	const char *decoded = "decoded.testdata";
	/* Encode */
	InFile = fopen(input, "rb");
	SrcFile = fopen(source, "rb");
	OutFile = fopen(output, "wb");
	r = code(1, InFile, SrcFile, OutFile, 0x1000);
	fclose(OutFile);
	fclose(SrcFile);
	fclose(InFile);
	if (r) {
		fprintf(stderr, "Encode error: %d\n", r);
		exit(1);
	}
	/* Decode */
	InFile = fopen(output, "rb");
	SrcFile = fopen(source, "rb");
	OutFile = fopen(decoded, "wb");
	r = code(0, InFile, SrcFile, OutFile, 0x1000);
	fclose(OutFile);
	fclose(SrcFile);
	fclose(InFile);
	if (r) {
		fprintf(stderr, "Decode error: %d\n", r);
		exit(1);
	}
	exit(0);
}

int main(int argc, char *argv[]) {
	setlocale(LC_ALL, "");
	setbuf(stdout, 0);
	if (argc > 1 && !strcmp(argv[0], "-test")) {
		return test(argc, argv);
	} else {
		return vm(argc, argv);
	}
}
