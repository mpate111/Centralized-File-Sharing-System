
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;



public class Peer1 implements Runnable{
	String[] getportInformation ;
	ArrayList<String> fileNames = new ArrayList<String>(); 
	Socket client_socket;
	
	ObjectOutputStream obj_out;
	ObjectInputStream obj_in;
	BufferedReader in;
	PrintWriter out;
	String[] filename;
	String path ="/home/tony/Downloads";
	InetAddress ip;
	
	Hashtable<Integer,String> clientInfo = new Hashtable<Integer,String>();
	int serverPortNumber;

	public static void main(String[] args) throws Exception{
	
		new Peer1(args[0]);
		

	}
	
	
	// Method to get the files from directory. Path has to be set to get the files of the folder
  public void getPeerFiles(){
		
		File[] files = new File(path).listFiles();
		for (File file : files) {
			fileNames.add(file.getName());
		}
		System.out.println("Number of Files Registerd to the server - "+fileNames.size());
		System.out.println("To search for file give command: get <Filename>");
		System.out.println("To refresh type command: refresh");
		System.out.println("To disconnect type command: disconnect");
	}

	
	public Peer1(String port_number) throws NumberFormatException, Exception{
		 System.out.println("Enter the Ip adress you want to connect:");
		 @SuppressWarnings("resource")
		InetAddress ip= InetAddress.getLocalHost();
		 this.ip = ip;
		Scanner sc =new Scanner(System.in);
		String ipAdd = sc.nextLine();
		System.out.println("Enter the port number for peer server:");
	    Scanner scan =new Scanner(System.in);
	    // To get the port number for peer acting as server
		serverPortNumber = scan.nextInt();
		// Act as a client
		client_socket = new Socket(ipAdd, Integer.parseInt(port_number));
		getPeerFiles();	
		new Thread(this).start();//Start the server
		obj_out = new ObjectOutputStream(client_socket.getOutputStream());
		obj_out.writeObject(fileNames);	//to send file names to server
		obj_out.writeObject(serverPortNumber+":"+ip.getHostAddress());//to send the portnumber and Ip
		obj_in = new ObjectInputStream(client_socket.getInputStream());
		in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
		out = new PrintWriter(client_socket.getOutputStream(),true);
		usercommand();
	}
	
	// Method to get the command from user
	@SuppressWarnings("unchecked")
	public void usercommand() throws ClassNotFoundException, IOException{
		String usercommand;
		System.out.println("Enter the user command");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
			if((usercommand=sc.nextLine())!=null){
				if(usercommand.contains("get")){
					filename = usercommand.split(" ");
					out.println(usercommand);
					Object obj =obj_in.readObject();// to get the list of portId and Ip
					List<String> portList = new ArrayList<String>();
					portList = (List<String>) obj;

					try{
						//return no such files if list is empty
						if (portList.isEmpty()){
						System.out.println("No such file on system");
						}
						
					    else{
						//if peer list is not empty
					    for (String peerInfo : portList){
					    	int i = 0;
					    	getportInformation = peerInfo.split(":");
					    	clientInfo.put(Integer.parseInt(getportInformation[i]),getportInformation[i+1]);
					    	System.out.println("The Port Id and The iP address is "+peerInfo );
					    	i = i+2;
					      }
					    					    					    					    
						// To check if the file is already in peer
						 for (int i = 0; i < portList.size(); i++) {
							if(portList.contains(serverPortNumber+":"+ip.getHostAddress())){
								System.out.println("You already have this file in system");
								usercommand();
							}
							
						
						System.out.println("Enter the following command to download file: download <portNumber>");
						String command;
						@SuppressWarnings("resource")
						Scanner inputPort= new Scanner(System.in);
						command= inputPort.nextLine();
						String[] setPortInfo = command.split(" ");
						int serverPortNum = 0;
						try{
						serverPortNum = Integer.parseInt(setPortInfo[1]);
							}catch(Exception e){
								System.out.println("Make sure you enter the command correctly:");
								System.out.println("Get the file name again");
								usercommand();
							}
						
						System.out.println("Connecting to the port"+serverPortNum+" "+clientInfo.get(serverPortNum));				
						Socket client_t = new Socket(clientInfo.get(serverPortNum),serverPortNum);
						PrintWriter out_temp = new PrintWriter(client_socket.getOutputStream(),true);
						out_temp.println(setPortInfo[0]);
						byte[] bytearray = new byte[65000];
						InputStream temp_is = client_t.getInputStream();
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename[1]));
						int Byteread = temp_is.read(bytearray,0,bytearray.length);
						bos.write(bytearray, 0, Byteread);
						bos.close();
						client_t.close();
						}
						usercommand();
						}
					}//try
						catch(NullPointerException e){
						System.out.println("No such file on system");
						usercommand();
					}
					obj_in.close();
				}
				
				
				else if(usercommand.contains("refresh")){
					out.println(usercommand);
					
//					out.println("Got the refresh command");
//					out.println("File list updated");					
					usercommand();
				}
				else if(usercommand.contains("disconnect")){
					out.print(usercommand);
					System.exit(1);
				}
			}
			
	}	
		
	
	
//	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		Socket client;
		ServerSocket clientAsServer;	
		while(true){
		try {
			clientAsServer = new ServerSocket(serverPortNumber);
			System.out.println("Waiting.....");
			client = clientAsServer.accept();
			System.out.println("The collection from peer"+client.getInetAddress()+" "+client.getPort());
//			new ServerConnection(client);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		}
		
	

	
}
