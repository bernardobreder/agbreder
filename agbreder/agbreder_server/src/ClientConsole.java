import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.bp2pb.server.AGBrederClient;

public class ClientConsole {

	public static void main(String[] args) throws IOException {
		AGBrederClient client = new AGBrederClient();
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(System.in));
		for (;;) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			System.out.println(client.send(line).length);
		}
		client.close();
	}

}
