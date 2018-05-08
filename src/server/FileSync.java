package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FileSync extends Thread{
	OutputStream outputStream;
	InputStream inputStream;
	Socket cs;
	public FileSync(Socket cs){
		this.cs=cs;
		try {
			outputStream = cs.getOutputStream();
			inputStream = cs.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public void run() {
		DataOutputStream dos =
				new DataOutputStream(outputStream);
		DataInputStream dis=
				new DataInputStream(inputStream);
    	while(true){
				String read;
				try {
				//	read = dis.readLine();
			
				//System.out.println(" am citit "+ read);
				
				Double size = dis.readDouble();
				System.out.println(" am citit "+ size);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
				
			
    	
    }
}
