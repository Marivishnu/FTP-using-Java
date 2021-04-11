import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.border.LineBorder;



class GUI implements ActionListener,MouseListener
{
	JButton b1,b2,EXIT;
    JFrame  f1;
    JTextField tf1,tf2,tf3;

    JPanel p1,p2,p11,p5,p3,p4,mainpanel1,mainpanel2;
    JLabel l1,l2;	
	String[] list ;
    Socket s;
    InputStream in=null;
    OutputStream  out=null;
    JOptionPane op1;
    JComboBox cb;
	JDialog d;
	JTextField tf4;
	JLabel message;
	JLabel[] label= new JLabel[1000];
	JButton getAction;
	JPanel output, inner1, inner2,inner3;
	
	DataInputStream din;
	static DataOutputStream dout;
	
	String path;
	
	GUI()
	{
		message = new JLabel();
		
		String tasks[] = {"list", "Download a file", "Upload a file", "Set the file as read only", "Delete a file", "Rename a file", "Make Directory", "File or Directory", "Get last modified information", "Set last modified information"};
		f1=new JFrame("GUI Services");
		cb = new JComboBox(tasks);
		f1.setLayout(new GridLayout(1,1));
		
        b1 = new JButton("CONNECT");
		b2 = new JButton("CLEAR ALL");
		EXIT = new JButton("EXIT");
		
        p1=new JPanel();
        p2=new JPanel();
		p4=new JPanel();
        op1 = new JOptionPane();
		b1.addActionListener(this);
		b2.addActionListener(this);
		EXIT.addActionListener(this);

		tf1 = new JTextField(20);
		tf2 = new JTextField(20);
		
		tf1.setText("localhost");
		tf2.setText("2001");
		p5=new JPanel();
		p11 = new JPanel();

		JPanel temp2 = new JPanel();
		temp2.setLayout(new GridBagLayout());
		JLabel ll = new JLabel("FTP");
		temp2.add(ll);
		ll.setForeground(Color.white);
		ll.setFont(new Font("Roboto", Font.PLAIN, 30));
		

		mainpanel1=new JPanel();
		mainpanel2=new JPanel();
		mainpanel2.setBackground(Color.decode("#6B38FF"));
		l1 = new JLabel("Server Name");
		l2 = new JLabel(" Port number");
		message.setForeground(Color.WHITE);
		
		p1.add(l1);
		p1.add(tf1);
		p11.add(l2);
		p11.add(tf2);
		p5.add(b1);
		p5.add(b2);
		p5.add(EXIT);
		
		mainpanel1.setBackground(Color.decode("#6B38FF"));
		mainpanel1.setLayout(new GridLayout(5,1));
		mainpanel1.add(temp2);
		temp2.setBackground(Color.decode("#6B38FF"));
		mainpanel1.add(p1);
		mainpanel1.add(p11);
		mainpanel1.add(p5);
		
		
		JLabel l4 = new JLabel("Enter path/filename");
		tf4 = new JTextField(30);
		p3 = new JPanel();
		p3.add(l4);
		p3.add(tf4);
		JLabel l5 = new JLabel("Select the Function");
		p4.add(l5);
		p4.add(cb);
		l4.setFont(new Font("Roboto", Font.PLAIN, 15));
		l5.setFont(new Font("Roboto", Font.PLAIN, 15));
		mainpanel2.setLayout(new GridLayout(5,1));
		
		
		
		inner1 = new JPanel();
		inner2 = new JPanel();

		inner2.setBackground(Color.white);

		mainpanel2.add(new JLabel());
		mainpanel2.add(p3);
		mainpanel2.add(p4);
		
		inner2.setBorder(new LineBorder(Color.BLACK));


		
		
		getAction = new JButton("Submit");
		getAction.addActionListener(this);
		
	
		p2.add(getAction);
		mainpanel2.add(p2);

		p3.setBackground(Color.WHITE);
		p2.setBackground(Color.WHITE);
		p4.setBackground(Color.WHITE);

		JPanel temp = new JPanel();
		temp.add(message);
		temp.setLayout(new GridBagLayout());
		temp.setBackground(Color.decode("#6B38FF"));
		message.setText("OUTPUT");
		mainpanel2.add(temp);

		f1.add(mainpanel1);
		f1.setUndecorated(true);
		f1. getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#FFDF00")));
		f1.setVisible(true);
		f1.setSize(400,300);
		f1.setLocationRelativeTo(null);
		f1.setDefaultCloseOperation(f1.EXIT_ON_CLOSE);
		
		b1.setBackground(Color.decode("#FFDF00"));
		b2.setBackground(Color.decode("#FFDF00"));
		EXIT.setBackground(Color.decode("#FFDF00"));
		getAction.setBackground(Color.decode("#FFDF00"));

		l1.setFont(new Font("Roboto", Font.PLAIN, 15));
		l2.setFont(new Font("Roboto", Font.PLAIN, 15));
		
		message.setFont(new Font("Roboto", Font.PLAIN, 15));
		cb.setBackground(Color.WHITE);

	}
	
	public void download(String path) {
		
		
		try {
			String servermessage = din.readUTF();
			System.out.println("servermessage: " + servermessage);
			inner2.removeAll();
			inner2.repaint(); inner2.repaint(); inner2.revalidate();
			String name = path.substring(path.lastIndexOf("\\") + 1, path.length());
			
			if (servermessage.equals("File not found!!! From Server")) 
			{
				JOptionPane.showMessageDialog(f1, "File not found", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			} else if(servermessage.equals("Path should contain a file name!! File not found")) {
				JOptionPane.showMessageDialog(f1, servermessage, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			String s3= JOptionPane.showInputDialog("Enter destination path");
			
			JLabel l = new JLabel();
				if(s3.isEmpty())
				{
					JOptionPane.showMessageDialog(f1, "Invalid Path!!", "Error!!", JOptionPane.ERROR_MESSAGE);	
				}
				else
				{
					File file = new File(s3 + name);	
					if (file.createNewFile()) {
						
						/*
						FileWriter fw = new FileWriter(s3 + name);
						fw.write(servermessage);
						fw.close();
						*/

						int bytes = 0;
				        FileOutputStream fileOutputStream = new FileOutputStream(s3 + name);
				        
				        long size = din.readLong();     // read file size
				        System.out.println("File size: " + size);
				        byte[] buffer = new byte[4*1024];
				        while (size > 0 && (bytes = din.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
				            fileOutputStream.write(buffer,0,bytes);
				            size -= bytes;      // read upto file size
				        }
				        fileOutputStream.close();
						l.setText("File downloaded successfully at "+s3);
						l.setFont(new Font("Roboto", Font.PLAIN, 15));
						l.setOpaque(true);
						inner2.repaint(); inner2.revalidate();
						inner2.add(l);
			
				
					}
					else {
						
						JOptionPane.showMessageDialog(f1, "File already exists!!!", "Error!!", JOptionPane.ERROR_MESSAGE);
					}
				}
			
			}	
			
		 catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	
	}
	
	public void upload(String path) {
		
		try {
			
			File x=new File(path);
			
			inner2.removeAll();
			inner2.repaint(); inner2.revalidate();
			JLabel l = new JLabel();
			
			if(x.exists())
			{	
				
				
				String message=din.readUTF();
				if(!message.equals(""))
				{
					JOptionPane.showMessageDialog(f1, "File Already exists in server", "Error!!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				//extracting information from file byte by byte and appending it to a string
				DataInputStream din2=new DataInputStream(new FileInputStream(path));
				
				/*
				byte b;
				String s2 = "";
				while((b=(byte)din2.read())!=-1)
				{
					s2=s2+(char)b;
				}
				
				//sending extracted info as string to server
				System.out.println("Uploading the file!!");
				dout.writeUTF(s2);
				*/
				
				int bytes = 0;
				File file = new File(path);
				FileInputStream fileInputStream = new FileInputStream(file);
				
				// send file size
				dout.writeLong(file.length());  
				// break file into chunks
				byte[] buffer = new byte[4*1024];
				while ((bytes=fileInputStream.read(buffer))!=-1){
					dout.write(buffer,0,bytes);
					dout.flush();
				}
				fileInputStream.close();
				
				String msg = din.readUTF();
				l = new JLabel(msg);
				inner2.repaint(); inner2.revalidate();
				inner2.add(l);
				l.setFont(new Font("Roboto", Font.PLAIN, 15));
			}
			else
				{
					JOptionPane.showMessageDialog(f1, "File does not exists in your machine", "Error!!!", JOptionPane.ERROR_MESSAGE);
					
				}
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void list() {
		
		
		
		try {
			String servermessage = din.readUTF();
			inner2.removeAll();
			inner2.repaint(); inner2.revalidate();

			if (servermessage.equals("Bad comment!! Can't list the file name")) {

				JOptionPane.showMessageDialog(f1, "File can't be listed", "Error!!!", JOptionPane.ERROR_MESSAGE);
				 servermessage = din.readUTF();
			
				return;
			}
			list= servermessage.split("\n");
			inner2.setLayout(new GridLayout(list.length,1));
			if (list.length<=10)
				inner2.setLayout(new GridLayout(10,1));
			
			for (int i=0;i<list.length;i++)
			{	
				label[i]=new JLabel(list[i]);
			}
			for (int i=0;i<list.length;i++)
			{	
				label[i].addMouseListener(this);
			}
			

			if (list.length > 10)
			{	for(int i=0;i<list.length;i++)
				{
				label[i].setFont(new Font("Roboto", Font.PLAIN, 12));
				}
			}
			else
			{
				for(int i=0;i<list.length;i++)
				{
				label[i].setFont(new Font("Roboto", Font.PLAIN, 16));
				}
			}
			tf4.setText(path);
			for (int i=0;i<list.length;i++)
			{	
				inner2.add(label[i]);
			}
			
			
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void readOnly() {
		
		inner2.removeAll();
		
		try {
			String msg = din.readUTF();
			JLabel l = new JLabel();
			l.setText(msg);
			inner2.repaint(); inner2.revalidate();
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void delete() {
		inner2.removeAll();
		
		try {
			String msg = din.readUTF();
			JLabel l = new JLabel();
			l.setText(msg);
			inner2.repaint(); inner2.revalidate();
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void glastupdate_c() {
		inner2.removeAll();
		
		try {
			String msg = din.readUTF();
			inner2.repaint(); inner2.revalidate();
			JLabel l = new JLabel();
			l.setText("The given file was last updated on :"+msg);
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void slastupdate_c() {
		inner2.removeAll();
		
		try {
			String msg = din.readUTF();
			inner2.repaint(); inner2.revalidate();
			JLabel l = new JLabel();
			l.setText(msg);
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void exenot() {
		inner2.removeAll();
		
		try {
			String msg = din.readUTF();
			inner2.repaint(); inner2.revalidate();
			JLabel l = new JLabel();
			l.setText(msg);
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void makedir() {
		inner2.removeAll();
		
		try {
			String msg = din.readUTF();
			inner2.repaint(); inner2.revalidate();
			JLabel l = new JLabel();
			l.setText(msg);
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void fileordir() {
		inner2.removeAll();
		
		try {
			String msg = din.readUTF();
			inner2.repaint(); inner2.revalidate();
			JLabel l = new JLabel();
			l.setText(msg);
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void renamefile() {
		inner2.removeAll();
		
		try {
			String s3= JOptionPane.showInputDialog("Enter a new name for the file");
			dout.writeUTF(s3);
			String msg = din.readUTF();
			inner2.repaint(); inner2.revalidate();
			JLabel l = new JLabel();
			l.setText(msg);
			inner2.add(l);
			l.setFont(new Font("Roboto", Font.PLAIN, 15));
		} catch (Exception E) {
			JOptionPane.showMessageDialog(f1, E, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
	      	
		if(e.getSource()==b1)
		{
			try{
			  
			  
			  s = new Socket(InetAddress.getByName(tf1.getText()),Integer.parseInt(tf2.getText()));
			  din = new DataInputStream(s.getInputStream());
			  dout = new DataOutputStream(s.getOutputStream());
			  
			  getAction.setSize(new Dimension(200,50));
			  d=new JDialog(f1,"File Services");
			  d.setSize(500,500);
			  d.setLocationRelativeTo(null);
			  d.setLayout(new GridLayout(2,1));
			  d.add(mainpanel2);
			  d.add(BorderLayout.CENTER, new JScrollPane(inner2));
			  
			  d.addWindowListener(new WindowAdapter() {
				  public void windowClosing(WindowEvent WE) {
					try {
						dout.writeUTF("exit");
							
					} catch(Exception E) {
				
					}
				  }
			  });
			  d.setVisible(true);
			  getAction.setEnabled(true);
			  
		  }
		  catch (Exception hh)
		  {	
			  
			  

			  JOptionPane.showMessageDialog(f1,"Server not in online","Error!",JOptionPane.ERROR_MESSAGE);
			  System.out.println("Connection not established with server");
			  
			  getAction.setEnabled(false);
			  
		  }
	  }
	      	if(e.getSource()==b2)
	      	{
	      		tf1.setText("");
	      		tf2.setText("");
	      		tf3.setText("");
	      		
				try {
					s.close();
				}
				catch (Exception ex) {}
				getAction.setEnabled(false);
				
	      	}
			
			if(e.getSource()==EXIT)
	      	{
				try {
					
					f1.dispose();
					System.exit(0);
				} catch(Exception E) {
					f1.dispose();
					System.exit(0);
				}
				
	      	}

			if(e.getSource() == getAction) 
			{
				try 
				{
					String action = cb.getSelectedItem().toString();
					path = tf4.getText();
					
					
					dout.writeUTF(action);
					System.out.println("action sent to server");
					
					din.readUTF();
					
					System.out.println("Control returned from server");
					dout.writeUTF(path);
					System.out.println("path sent to server");
					
				
					if (action.equals("Download a file")) 
					{
						download(path);
					} 
					else if (action.equals("list")) 
					{
						list();
					}
					else if (action.equals("Upload a file")) 
					{
						upload(path);
					} 
					else if (action.equals("Set the file as read only")) 
					{
						readOnly();
					}
					else if (action.equals("Delete a file")) 
					{
						delete();
					} 
					else if(action.equals("Get last modified information"))
					{
						glastupdate_c();
					} 
					else if(action.equals("Set last modified information"))
					{
						slastupdate_c();
					} 
					else if(action.equals("Executable or not"))
					{
						exenot();
					} 
					else if(action.equals("Make Directory"))
					{
						makedir();
					} 
					else if(action.equals("File or Directory"))
					{
						fileordir();
					} 
					else if(action.equals("Rename a file"))
					{
						renamefile();
					} 
				}
				catch (Exception ex) 
				{
					JOptionPane.showMessageDialog(f1, "Exception!!", "Exception", JOptionPane.ERROR_MESSAGE);
				}	
				
			}
	      	
	}
	 public void mouseClicked(MouseEvent  m) {  
		
		try
		{
			for (int i=0;i<list.length;i++)
			{
				if(m.getSource()==label[i])
				{
					path = tf4.getText();
					int a=path.length();
					if(path.lastIndexOf('\\')==a-1)
						path = path+list[i];
					else
						path = path+"\\"+list[i];
				
					String action="list";
					din = new DataInputStream(s.getInputStream());
					dout = new DataOutputStream(s.getOutputStream());
					dout.writeUTF(action);
					System.out.println("action sent to server");
					
					din.readUTF();
					
					System.out.println("Control returned from server");
					dout.writeUTF(path);
					System.out.println("path sent to server");
					list();
				}
					
			}
		
		
		}catch(Exception ex){}
    }  
    public void mouseEntered(MouseEvent e) {  
     
    }  
    public void mouseExited(MouseEvent e) {  
      }
    public void mousePressed(MouseEvent e) {  
        
    }  
    public void mouseReleased(MouseEvent e) {  
        
    }


	public static void main(String[] args) 
	{
	
		GUI g = new GUI();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() { 
			  
				try {
					dout.writeUTF("exit");
				}catch(Exception E) {
					System.out.println("Exception");
				}
			}
		});
	}
}
