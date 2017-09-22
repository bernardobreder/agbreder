package breder.io;

import java.io.IOException;

public interface RandomStream {

	public int mark() throws IOException;

	public void mark(int pos) throws IOException;

	public void close() throws IOException;

	public void reset() throws IOException;

}
