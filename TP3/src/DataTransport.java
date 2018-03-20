import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataTransport extends Transport implements IDataTransport {
	
	public DataTransport(String host, int port) {
		super();
		try {
			super.openClient(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int send(InputStream in) throws IOException {
		if(os != null) {
			byte buffer[] = new byte[1024];
			int len = in.read(buffer);
			int count = len;
			
			while(len != -1) {
				os.write(buffer,0,len);
				os.flush();
				len = in.read(buffer);
				count+=len;
			}
			
			return count+1;
		}
		
		return 0;
	}
	
	public int receive(OutputStream out) throws IOException {
		if(is != null) {
			byte buffer[] = new byte[1024];
			int len = is.read(buffer);
			int count = len;
			while(len != -1) {
				out.write(buffer,0,len);
				out.flush();
				len = is.read(buffer);
				count+=len;
			}
			return count+1;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		if(args.length != 4) {
			System.out.println("Usage: DataClient host port [in/out] file");
			return;
		}
		try{
			int nbByte = 0;
			DataTransport dataTransport = new DataTransport(args[0], Integer.parseInt(args[1]));
			if("out".equals(args[2])) {
				nbByte = dataTransport.send(new BufferedInputStream(new FileInputStream(args[3])));
				System.out.println("Number of byte sent: " + nbByte);
			}else if("in".equals(args[2])) {
				nbByte = dataTransport.receive(new BufferedOutputStream(new FileOutputStream(args[3])));
				System.out.println("Number of byte received: " + nbByte);
			}			
			dataTransport.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
