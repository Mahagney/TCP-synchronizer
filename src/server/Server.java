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
			
		} catch (IOException e) {
			System.out.println("can't create server socket");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startServer(String path){
		Socket cs=null;
		while(true){
			try {
				cs=serverSocket.accept();
				if(cs!=null){
					FileSync fs=new FileSync(cs,path);
					fs.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args) {
		if(args.length<1){
			System.out.println("invalid arguments \n1=path to dir");
		}
		Server s=new Server();
		s.startServer(args[0]);
	}

}
