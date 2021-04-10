import java.io.*;
import java.net.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

class Server {
	
	ServerSocket ss;
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	
	Server() {
		
		try {
			ss = new ServerSocket(2001);
			System.out.println("Server Created!!");
			
		} catch (Exception E) {
			System.out.println(E);
		}
	}
	
	public void sendErrorMessage(String str) {
		try {
			System.out.println(str);
			dout.writeUTF(str);
		} catch (Exception E) {
			System.out.println(E);
		}
	}
	
	public void list(String path) {
		
		try {
			File file = new File(path);
			if (!file.isDirectory()) {
				sendErrorMessage("Bad comment!! Can't list the file name");
			}
			
			String[] fileNames = file.list();
			String lists = new String();
			//Attaching all file names in one string variable
			
			for (int i = 0; i < fileNames.length; i++)
				lists += fileNames[i] + "\n";
			
			System.out.println("Action - List");
			dout.writeUTF(lists);
			System.out.println(lists);
			System.out.println("List sent!!!");
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage("Invalid path!! Ending in a file name");
		}
	}
	
	public void download(String path) {
		System.out.println(path);
		try {
			File file = new File(path);
			String s1 = "";
			if (file.exists()) {
				
				
				if (file.isFile())
				{
					
				
				
					DataInputStream d = new DataInputStream(new FileInputStream(file));
					byte b;

					int bytes = 0;
					dout.writeUTF(s1);

					FileInputStream fileInputStream = new FileInputStream(file);
					dout.writeLong(file.length());

					byte[] buffer = new byte[4*1024];
			        while ((bytes=fileInputStream.read(buffer))!=-1){

			            dout.write(buffer,0,bytes);
						System.out.println(bytes + " bytes sent");
			            dout.flush();
			        }
			        fileInputStream.close();
			        System.out.println("File sent");
					
					/*	
					while((b = (byte)d.read()) != -1) {
						s1 = s1 + (char)b;
					}
					dout.writeUTF(s1);
					System.out.println("File sent to client");
					*/
				} 
				else if (file.isDirectory()) {
					dout.writeUTF("Path should contain a file name!! File not found");
					return;
				}
				
			}
			else {
				sendErrorMessage("File not found!!! From Server");
				return;
			}
			System.out.println("Action - Download");
			
			
			
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage("File not found!!! From Server");
		}
	}
	
	public void upload(String path) {
		try {
			String name=path.substring(path.lastIndexOf("\\")+1);
			File x=new File(name);

			//Checking if file already exists in Server
			if(x.exists())
			{ 
				
				dout.writeUTF("File Already Exists");
				return;
			}
			
			
			dout.writeUTF("");
			
			/*
			//extracting contents of the file and writing it in the new created file
			String s2=din.readUTF();
			//File file = new File(name);
			if (x.createNewFile()) {
				FileWriter fw = new FileWriter(name);
				fw.write(s2);
				fw.close();
				//System.out.println("Download completed!!!");
			}
			*/
			
			int bytes = 0;
			FileOutputStream fileOutputStream = new FileOutputStream(name);
			
			long size = din.readLong();     // read file size
			byte[] buffer = new byte[4*1024];
			while (size > 0 && (bytes = din.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
				fileOutputStream.write(buffer,0,bytes);
				size -= bytes;      // read upto file size
			}
			fileOutputStream.close();
			
			System.out.println("File uploaded successfully");
			sendErrorMessage("File uploaded successfully");
		} catch (Exception E) {
			sendErrorMessage(E.toString());
		}
	}
	
	public void readOnly (String path) {
		
		
		try {
			
			File file = new File(path);
			if (file.exists()) {
				

				if (file.isFile()) {
					file.setReadable(true); //set readable
					file.setWritable(false); //remove write permission
					file.setExecutable(false); //remove exe permission
					dout.writeUTF("Set the file as readable successfully!!!");
				} else
					dout.writeUTF("Path should contain a file name!! File not found");
					
			} else {
				dout.writeUTF("Path not found");
			}
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}
	
	public void delete(String path) {
		
		try {
			String str2="File deleted Successfully";
			File x=new File(path);
			if(x.exists())
			{	
				if(x.delete())
				{
				dout.writeUTF(str2);
				} else {
					dout.writeUTF("Can't delete the file");
				}
			}
			else
			{	
				str2=" File not exist";
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF(str2);
			}
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}
	public void glastupdate_s(String path) {
		
		try {
			String str; 
			File file=new File(path);
			if(file.exists())
			{	
				//Get the last updated time of file
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
				str= sdf.format(file.lastModified());
		
				// sending the time to client
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(str);
				System.out.println("Last updated time sent!!!");
			}
			
			else
			{	
				str=" File not exist";
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF(str);
			}
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}
	public void slastupdate_s(String path) {
		
		try {
			String str; 
			File file=new File(path);
			if(file.exists())
			{	
				//setting last updated time of file 
				file.setLastModified(new Date().getTime());
	
				str= "Last updated time has been changed to current time!!!";
		
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(str);
				System.out.println("Last updated time changed!!!");
			}
			
			else
			{	
				str=" File not exist";
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF(str);
			}
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}
	public void exenot(String path) {
		
		try { 
			File file=new File(path);
			if (file.exists()) 
			{
				//check file is executable or not
				if (file.canExecute()) 
					dout.writeUTF("File is executable");
				else
					dout.writeUTF("File is not executable");
			} 
			else
			{
				dout.writeUTF("File not found");
			}
			
			
			
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}
	public void makedir(String path) {
		
		try {
			File file=new File(path);
			if (file.mkdirs()) 
			{
				dout.writeUTF("Directory created successfully!!!");
			} 
			else 
			{
				dout.writeUTF("Directory not created!!!");
			}
			
			
			
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}
	public void fileordir(String path) {
		
		try {
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			File x=new File(path);
			if (x.exists()) 
			{
				if (x.isFile())
					dout.writeUTF("It's a file");
				if (x.isDirectory())
					dout.writeUTF("It's a directory");
			
			}
			else {
				dout.writeUTF("Path not found");
			}
			
			} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}
	public void renamefile(String path) {
		
		try {
			File x=new File(path);
			din = new DataInputStream(s.getInputStream());
			String s3=din.readUTF();
			int index=path.lastIndexOf('\\');
			String s5=path.substring(0,index+1);
			File y=new File((s5.concat(s3)).concat(path.substring(path.lastIndexOf('.'))));
			if(!y.exists())
			{
				if(x.renameTo(y))
					dout.writeUTF("File renamed Successfully!!!");
			}
			else
				dout.writeUTF("File with this new name already exist");
			
			
			
		} catch (Exception E) {
			System.out.println(E);
			sendErrorMessage(E.toString());
		}
	}

	public void acceptConnection() {
		try {
			s = ss.accept();
			System.out.println("Connected to Client");
		} catch (Exception E) {
			System.out.println(E);
		}
	}
 	
	public void service() {
		
		acceptConnection();
		while(true) {
			
			String action;
			String clientMessage;
			try {
				din = new DataInputStream(s.getInputStream());
				dout = new DataOutputStream(s.getOutputStream());
				
				action = din.readUTF();
				
				System.out.println("Received action: " + action);
				
				if (action.equals("exit")) {
					acceptConnection();
					continue;
				}

				dout.writeUTF("");
				clientMessage = din.readUTF();
				System.out.println("Received clientMessage: " + clientMessage);
				switch (action) {
				
				case "list":
									list(clientMessage);
									break;
				case "Download a file":
									download(clientMessage);
									break;
				case "Upload a file":
									upload(clientMessage);
									break;
				case "Set the file as read only":
									readOnly(clientMessage);
									break;
				case "Delete a file":
									delete(clientMessage);
									break;
				case "Get last modified information":
									glastupdate_s(clientMessage);
									break;
				case "Set last modified information":
									slastupdate_s(clientMessage);
									break;	
				case "Executable or not":
									exenot(clientMessage);
									break;
				case "Make Directory":
									makedir(clientMessage);
									break;	
				case "File or Directory":
									fileordir(clientMessage);
									break;						
				case "Rename a file":
									renamefile(clientMessage);
									break;	
				}
				
			}catch (Exception E) {
				System.out.println(E);
			}
			
			
			
		}
		
	}
	
	public static void main(String[] args){
		
		Server s1 = new Server();
		s1.service();
		
	}
}