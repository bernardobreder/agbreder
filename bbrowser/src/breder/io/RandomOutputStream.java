package breder.io;

import java.io.IOException;

public interface RandomOutputStream extends RandomStream {

	public void write(char c) throws IOException;

	public void write(byte[] bytes) throws IOException;

}
