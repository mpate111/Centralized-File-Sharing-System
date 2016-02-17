

import java.io.*;
import java.net.*;


public class IndexServer implements Runnable {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new IndexServer(args[0]);

	}

	
	ServerSocket server_sock = null;
	Socket client_socket = null;
	
	
	public void run(){
		System.out.println("Trying to connect");
		InetAddress ip =null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("The Ip of Server is :"+ip.getHostAddress());
		while(true){
			try{
				
				client_socket = server_sock.accept();
				
				System.out.println("The connection from"+client_socket.getInetAddress()+"   "+client_socket.getPort());				
				
				new Connect(client_socket);
				}
			catch(IOException e){
				System.out.println("The connection failed");
				e.printStackTrace();
				}
		}
			}
	
	public IndexServer(String portNo){
		try{
			server_sock = new ServerSocket(Integer.parseInt(portNo));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("The port no is:"+server_sock.getLocalPort());
		new Thread(this).start();
		
	}

	
}
