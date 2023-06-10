package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class listener {

	public static void main(String[] args) {
		Socket client;
    	DataInputStream in;
    	DataOutputStream out=null;
    	int userId=0;
		try 
	    {   Scanner reader=new Scanner(System.in);

//	    	while(true) {
				client = new Socket(InetAddress.getLocalHost(), 3434);
				in = new DataInputStream(client.getInputStream());
				out = new DataOutputStream(client.getOutputStream());
	    		
				StringBuffer s=new StringBuffer(reader.nextLine());
				System.out.println(s.toString());
	    		out.writeUTF(s.toString());
	    		System.out.println(in.readUTF());
	    		while(true) {
					System.out.println(in.readUTF());
		    	}
	    } catch (IOException e){}
	    
	    try 
        {   out.writeUTF(""+userId);
	    	out.flush();
        } 
        catch (IOException e){}
	}
}
