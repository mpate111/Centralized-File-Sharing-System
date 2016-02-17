

import java.net.Socket;
import java.util.*;
import java.io.*;

public class Connect implements Runnable {
	
	Socket client =null;
	int index ;
	String command;

	BufferedReader input ;
	PrintWriter output;
	String finalFileName = null;
	StringBuffer str_buf = new StringBuffer();
	ObjectOutputStream obj_output;
	ObjectInputStream obj_input;
	static Hashtable<String, List<String>> filesCollection = new Hashtable<String, List<String>>();
	ArrayList<String> FileList = new ArrayList<String>();
	String peerInfo;
	
	public Connect(Socket client) throws IOException
	{
	this.client = client;
    output = new PrintWriter(client.getOutputStream(),true);
    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
    obj_output = new ObjectOutputStream(client.getOutputStream());
    obj_input = new ObjectInputStream(client.getInputStream());
	new Thread(this).start();
	}
	
		// Hashtable function to store the value in Hashtable	
	public boolean filePool(String peerId, String filename) throws Exception {
		// add filenames and peerid to the registry (assign by return peerids to clients)
		
		List<String> check = new ArrayList<String>();
		List<String> insert = new ArrayList<String>();
		
		//check if file is already in filecollection
		check = filesCollection.get(filename);
		
		if(check == null || check.isEmpty())  //file not in registry
		{
			
			insert.add(peerId);
			filesCollection.put(filename, insert);
		}
		else //file exists
		{
			//test to see if peer is already listed, if not insert in list and put in filecollection
			Iterator<String> portNo = check.listIterator();
			
			while(portNo.hasNext())
			{
				String x = portNo.next();
				
				if(x.contains(peerId))  //filename and peer already exist return
				{
					return true;
				}
			}
			
			//filename exists, peer does not
			// map peerId to filename
			check.add(peerId);
			filesCollection.put(filename, check);
			
		}
	   
		return true;
	}



	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void run() {
		if(command == null){
//		srlz_obj.readSerializedObject(client);
		try{
		   
			Object obj =obj_input.readObject();
			FileList = (ArrayList<String>)obj;
			Object obj_2 = obj_input.readObject();
			peerInfo = (String) obj_2;
			for (String printList : FileList) {
			System.out.println("File registered to server "+printList);
			filePool(peerInfo,printList);
			}
			
			
		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		System.out.println("Connected to the server");
		}
		
		
			try {
				while(true){
					command = input.readLine();
					 if(command.contains("get")){
						String[] getFileCommand = command.split(" ");
						List<String> setOfPortNumber = new ArrayList<String>();
						System.out.println("\nFile name asked for"+getFileCommand[1]);
						String filename = getFileCommand[1];
						setOfPortNumber = filesCollection.get(filename);
						obj_output.writeObject(setOfPortNumber);												
						}// To get the port and Ip.
					 
					//refresh command
					else if(command.contains("refresh")){
						System.out.println("I am here got refresh commnd");	
						System.out.println("File list updated");
						
					}
					else if(command.contains("disconnect")){
						System.out.println("Peer  is now disconnected");
						Thread.currentThread().stop();
					}
					 
					else{
						output.println("Unknown Command");
					}
					 
					System.out.println(command);
					
					
				}
			
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
	}
	
}

	
