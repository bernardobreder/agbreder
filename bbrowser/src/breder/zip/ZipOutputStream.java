package breder.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

public class ZipOutputStream extends OutputStream {

	public ByteArrayOutputStream output = new ByteArrayOutputStream();
	private final OutputStream foutput;

	public ZipOutputStream(OutputStream foutput) {
		this.foutput = foutput;

	}

	@Override
	public void write(int b) throws IOException {
		this.output.write(b);
	}

	@Override
	public void close() throws IOException {
		java.util.zip.ZipOutputStream zipOutput = new java.util.zip.ZipOutputStream(foutput);
		zipOutput.putNextEntry(new ZipEntry("root"));
		zipOutput.write(output.toByteArray());
		zipOutput.closeEntry();
		zipOutput.close();
	}

	public static void main(String[] args) throws IOException {
		byte[] bytes = "Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder Bernardo Breder está testando o compressor de texto, mas eu não sei o que fazer.".getBytes("utf-8");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(output);
		out.write(bytes);
		out.close();
		System.out.println(bytes.length);
		System.out.println("To");
		System.out.println(output.toByteArray().length);
	}

}
