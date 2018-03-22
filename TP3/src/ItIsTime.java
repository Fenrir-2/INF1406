import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ItIsTime implements IItIsTime {
	
	protected String host;
	protected int port;
	protected LineTransport lineTransport;
	
	public ItIsTime(String host, int port) {
		this.host = host;
		this.port = port;
		lineTransport = null;
		try {
			lineTransport = new LineTransport(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String connect(String tp, String grp, String key) {
		
		String reply = "";
		try {
			lineTransport.send("10" + tp + grp + key);
			reply = lineTransport.receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if("5".equals(reply.substring(0,1))) {
			if("0".equals(reply.substring(1,2))) {
				System.out.println(reply + " identiﬁcation refused - wrong id");
				return null;
				
			}else if("1".equals(reply.substring(1,2))) {
				System.out.println(reply + "identiﬁcation refused - out of time");
				return null;
				
			}else if("5".equals(reply.substring(1,2))) {
				System.out.println(reply + "unexpected command");
				return null;
			}
		}
		
		if("20".equals(reply.substring(0, 2))) {
			return reply.substring(2);
		}
		
		
		
		return null;
	}

	public int send(String filename) {
		String reply = "";
		try {
			lineTransport.send("12");
			reply = lineTransport.receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if("52".equals(reply.substring(0, 2))) {
			System.out.println("52: error while opening connection");
			try {
				lineTransport.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}
		
		int dataPort = -1;
		
		if("22".equals(reply.substring(0, 2))) {
			dataPort = Integer.parseInt(reply.substring(2));
		}
		
		DataTransport dataTransport = null;
		try {
			dataTransport = new DataTransport(host, dataPort);
			dataTransport.send(new BufferedInputStream(new FileInputStream(filename)));
			dataTransport.close();
			lineTransport.send("13");
			reply = lineTransport.receive();
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			try {
				dataTransport.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if("53".equals(reply.substring(0, 2))) {
			System.out.println("53: error while reading ﬁle");
			return -1;
		}
		if("55".equals(reply.substring(0, 2))) {
			System.out.println("55:  unexpected command");
			return -1;
		}
		
		if("23".equals(reply.substring(0, 2))) {
			return Integer.parseInt(reply.substring(2));
		}
		
		return 0;
	}

	public void close() {
		String reply = "";
		try {
			lineTransport.send("14");
			reply = lineTransport.receive();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				lineTransport.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if("24".equals(reply.substring(0, 2))) {
			System.out.println("connection closed");
		}
		if("25".equals(reply.substring(0, 2))) {
			System.out.println("unexpected command");
		}

	}
	
	public static void main(String args[]) {
		if(args.length != 6) {
			System.out.println("Usage: ItIsTime host port tp grp key file");
			return;
		}
		
		ItIsTime itIsTime = new ItIsTime(args[0], Integer.parseInt(args[1]));		
		String login = itIsTime.connect(args[2], args[3], args[4]);
		if(login != null && !"".equals(login)) {
			System.out.println("You're logged in as: " + login);
			int receivedSize = itIsTime.send(args[5]);
			if(receivedSize != -1) {
				System.out.println("Received size:" + receivedSize);
			}
		}
		itIsTime.close();
	}
}
