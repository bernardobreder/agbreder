import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Inicializador do servidor
 * 
 * @author bernardobreder
 */
public class Main {

	public static final Map<String, byte[]> cache = new WeakHashMap<String, byte[]>(1024);

	/**
	 * Inicializador
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(9889);
		// SqlLiteConnectionTest conn = new SqlLiteConnectionTest();
		for (;;) {
			try {
				Socket socket = server.accept();
				new ClientThread(socket).start();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public static class ClientThread extends Thread {
		private final Socket socket;

		public ClientThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				DataOutputStream data = new DataOutputStream(socket.getOutputStream());
				File file = new File("../agbreder_bdk_test/bin/binary.agbc");
				if (file.exists()) {
					long size = file.length();
					try {
						OutputStream output = socket.getOutputStream();
						FileInputStream input = new FileInputStream(file);
						for (long n = 0; n < size; n++) {
							data.write(input.read());
						}
						output.close();
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					// File dir = new File("res");
				} finally {
					try {
						data.close();
					} catch (IOException e) {
					}
				}
			} catch (IOException e) {
			}
			// OutputStream output = socket.getOutputStream();
			// try {
			// {
			// System.out.print("Request : [");
			// InputStream input = this.socket.getInputStream();
			// for (int n; ((n = input.read()) > 0);) {
			// System.out.print((char) n);
			// }
			// System.out.println("]");
			// }
			// byte[] bytes = cache.get("");
			// if (bytes == null) {
			// InputStream input = new
			// FileInputStream("../agbreder_bdk/binary.agbc");
			// ByteArrayOutputStream output = new ByteArrayOutputStream();
			// for (int n; ((n = input.read()) != -1);) {
			// output.write((char) n);
			// }
			// input.close();
			// cache.put("", bytes = output.toByteArray());
			// }
			// OutputStream output = socket.getOutputStream();
			// output.write(bytes);
			// } catch (Exception e) {
			// e.printStackTrace();
			// } finally {
			// try {
			// socket.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

}
