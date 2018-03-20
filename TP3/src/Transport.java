import java.io.*;
import java.net.*;

public class Transport implements ITransport {
	
	protected Socket socket;
	protected OutputStream os;
	protected InputStream is;
	
	public Transport() {
	}
	
	public void openClient(String host, int port) throws IOException {
		socket = new Socket(host, port);
		
		if(socket != null) {
			os = socket.getOutputStream();
			is = socket.getInputStream();
		}
	}

	public void open(Socket socket) throws IOException {
		this.socket = socket;
		os = socket.getOutputStream();
		is = socket.getInputStream();
	}

	public void close() throws IOException {
		if(os != null) {
			os.close();	
		}
		if(is != null) {
			is.close();
		}
		if(socket != null) {
			socket.close();
		}
	}

}
