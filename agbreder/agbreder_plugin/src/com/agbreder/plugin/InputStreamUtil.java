package com.agbreder.plugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.agbreder.plugin.util.InterruptedThread;

public class InputStreamUtil {

	public static byte[] getBytes(InputStream input, long timeout, Runnable run) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (timeout > 0) {
			new InterruptedThread(timeout, run).start();
		}
		InputStreamUtil.copyStream(input, output);
		return output.toByteArray();
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] bytes = new byte[1024];
		while (true) {
			int n = input.read(bytes);
			if (n == -1)
				break;
			output.write(bytes, 0, n);
		}
	}

}
