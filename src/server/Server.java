package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {


	private ServerSocket serverSocket;
	public Server(){
		try {
			serverSocket=new ServerSocket();
			InetSocketAddress sa=new InetSocketAddress("127.0.0.1",9999);
			serverSocket.bind(sa,5);
			startServer();
			System.out.println("Server started");
			
		} catch (IOException e) {
			System.out.println("can't create server socket");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startServer(){
		Socket cs=null;
		while(true){
			try {
				cs=serverSocket.accept();
				if(cs!=null){
					FileSync fs=new FileSync(cs);
					fs.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args) {
		Server s=new Server();
		s.startServer();
	}

}
