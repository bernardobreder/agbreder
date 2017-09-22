#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/stat.h>
#include "xdelta3/xdelta3.h"

int code(int encode, FILE* InFile, FILE* SrcFile, FILE* OutFile, int BufSize) {
	int r, ret;
	struct stat statbuf;
	xd3_stream stream;
	xd3_config config;
	xd3_source source;
	void* Input_Buf;
	int Input_Buf_Read;
	if (BufSize < XD3_ALLOCSIZE) {
		BufSize = XD3_ALLOCSIZE;
	}
	memset(&stream, 0, sizeof (stream));
	memset(&source, 0, sizeof (source));
	xd3_init_config(&config, XD3_ADLER32);
	config.winsize = BufSize;
	xd3_config_stream(&stream, &config);
	if (SrcFile) {
		r = fstat(fileno(SrcFile), &statbuf);
		if (r) {
			return r;
		}
		source.blksize = BufSize;
		source.curblk = malloc(source.blksize);
		r = fseek(SrcFile, 0, SEEK_SET);
		if (r)
			return r;
		source.onblk = fread((void*) source.curblk, 1, source.blksize, SrcFile);
		source.curblkno = 0;
		xd3_set_source(&stream, &source);
	}
	Input_Buf = malloc(BufSize);
	fseek(InFile, 0, SEEK_SET);
	do {
		Input_Buf_Read = fread(Input_Buf, 1, BufSize, InFile);
		if (Input_Buf_Read < BufSize) {
			xd3_set_flags(&stream, XD3_FLUSH | stream.flags);
		}
		xd3_avail_input(&stream, Input_Buf, Input_Buf_Read);
		process: if (encode) {
			ret = xd3_encode_input(&stream);
		} else {
			ret = xd3_decode_input(&stream);
		}
		switch (ret) {
		case XD3_INPUT: {
			continue;
		}
		case XD3_OUTPUT: {
			r = fwrite(stream.next_out, 1, stream.avail_out, OutFile);
			if (r != (int) stream.avail_out)
				return r;
			xd3_consume_output(&stream);
			goto process;
		}
		case XD3_GETSRCBLK: {
			if (SrcFile) {
				r = fseek(SrcFile, source.blksize * source.getblkno, SEEK_SET);
				if (r)
					return r;
				source.onblk = fread((void*) source.curblk, 1, source.blksize, SrcFile);
				source.curblkno = source.getblkno;
			}
			goto process;
		}
		case XD3_GOTHEADER: {
			goto process;
		}
		case XD3_WINSTART: {
			goto process;
		}
		case XD3_WINFINISH: {
			goto process;
		}
		default: {
			return ret;
		}
		}
	} while (Input_Buf_Read == BufSize);

	free(Input_Buf);

	free((void*) source.curblk);
	xd3_close_stream(&stream);
	xd3_free_stream(&stream);

	return 0;

}
;
