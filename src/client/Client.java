package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;

import utils.FilesReader;


public class Client extends Thread{
	Socket cs;
	OutputStream outputStream;
	InputStream inputStream;

    public void run() {
    
		try {
			cs= new Socket();
			InetSocketAddress sa=new InetSocketAddress("127.0.0.1",9999);
			cs.connect(sa);
			outputStream = cs.getOutputStream();
			inputStream = cs.getInputStream();
			DataOutputStream dos =
					new DataOutputStream(outputStream);
			DataInputStream dis=
					new DataInputStream(inputStream);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			FilesReader fr=new FilesReader("C:\\Users\\Mahag\\workspace\\TCP-synchronizer\\src\\resources");
			Map<String,Long> files=fr.getFilesList();
			for(Entry<String, Long> file:files.entrySet()){
				//dos.writeChars(file.getKey());
				dos.writeLong(file.getValue());
				System.out.println("op");
			}
			while(true){
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("exceptie client");
			e.printStackTrace();
		}
    	
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Client c=new Client();
			c.start();
		}

}
