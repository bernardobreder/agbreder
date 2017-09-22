package breder.io;

import java.io.IOException;

public interface RandomInputStream extends RandomStream {

	public int read(byte[] bytes) throws IOException;

	public int read() throws IOException;

}
