import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LineTransport extends Transport implements ILineTransport {
	
	protected BufferedWriter writer;
	protected BufferedReader reader;
	
	public LineTransport(String host, int port) throws IOException {
		super();
		super.openClient(host, port);
		if(socket != null){
			reader = new BufferedReader(new InputStreamReader(is));
			writer = new BufferedWriter(new OutputStreamWriter(os));
		}
	}

	public void send(String line) throws IOException {
		if(writer != null) {
			writer.write(line + "\n");
			writer.flush();
		}
	}

	public String receive() throws IOException {
		if(reader != null) {
			String ret = reader.readLine();
			return ret;
		}
		return null;
	}
	
	public void close() throws IOException {
		reader.close();	
		writer.close();
		super.close();
	}
	
	
	public static void main(String[] args) {
		try {
			LineTransport lineTransport = new LineTransport("127.0.0.1", 5000);
			lineTransport.send("test");
			System.out.println(lineTransport.receive());
			lineTransport.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
