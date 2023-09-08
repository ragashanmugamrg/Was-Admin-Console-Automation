package cbaconnect;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.SimpleTimeZone;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.filechooser.FileSystemView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import static javax.swing.GroupLayout.Alignment.*;    
public class Connect implements ActionListener,ItemListener,KeyListener{
    static JTextField button;
    static JComboBox<String> envbox;
    static JButton cancle;
    private static String password;
    static JFrame frame;
    static String pathofFile;
    static JTextField passField;
    static JProgressBar progressBar;
    static TextArea area = null;
    static Timer timer = null;
    static boolean isfilepresent= false; 
    static JLabel operation = new JLabel("Operation");
    static long start1 = System.currentTimeMillis();
    static JLabel time = null;
    public static void main(String[] args) { 
        frame = new JFrame("CBA Deployement"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        JPanel layoutpanel = new JPanel();
        
        layoutpanel.setSize(600, 210);
        //layoutpanel.setBackground(Color.gray);
        //layoutpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        GroupLayout groupLayout = new GroupLayout(layoutpanel); 
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        
        JLayeredPane pane = new JLayeredPane();
        
        String arr[] = {"UAT","DEV"};
        JLabel clickMe = new JLabel("CBA File Path :"); 
        button = new JTextField();
		JButton browse = new JButton("Browse");
		Connect event = new Connect();
        browse.addActionListener(event);
        JLabel environment = new JLabel("Choose The Deploying Environment :");
        envbox = new JComboBox<String>(arr);
        envbox.addItemListener(event);
        JLabel Username = new JLabel("User Name :");
        JLabel Password = new JLabel("PassWord :");
        JTextField Userfield = new JTextField("nmehta");
        Userfield.setEditable(false);
        passField = new JTextField();
        passField.addKeyListener(event);
        JLabel progress = new JLabel("Progressing :");
        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        
        cancle = new JButton("Cancle");
        cancle.addActionListener(event);
        
        JButton upload = new JButton("Upload");
        upload.addActionListener(event);
        JButton deploy = new JButton("Deploye");
        deploy.addActionListener(event);
        JButton validate = new JButton("Validate");
        
        time = new JLabel("Time");
        
        timer = new Timer(100, event);
        
        area = new TextArea("Waiting for the Host .......");
        
        groupLayout.setHorizontalGroup( 
                    groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup() 
                                .addComponent(clickMe)
                                .addComponent(environment)
                                .addComponent(Username)
                                .addComponent(Password)
                                .addComponent(progress)
                                .addComponent(progressBar, LEADING, 1, 2, 200)
                                .addComponent(cancle)
//                                .addGroup(groupLayout.createParallelGroup(TRAILING)
//                                		.addComponent(area))
                                .addComponent(operation))
                                
                                .addGroup(groupLayout.createParallelGroup()
                                .addComponent(button)
                                .addComponent(envbox)
                                .addComponent(Userfield)
                                .addComponent(passField)
                                .addComponent(time)
                                .addComponent(time)
                                .addComponent(upload))                               
                                .addGroup(groupLayout.createParallelGroup()
                                .addComponent(browse)
                                .addComponent(deploy))
                                
        );
        groupLayout.setVerticalGroup( 
                     groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(BASELINE) 
                                .addComponent(clickMe) 
                                .addComponent(button)
                                .addComponent(browse))
                                .addGroup(groupLayout.createParallelGroup(BASELINE)
                                .addComponent(environment)
                                .addComponent(envbox))
                                .addGroup(groupLayout.createParallelGroup(BASELINE)
                                .addComponent(Username)
                                .addComponent(Userfield))
                                .addGroup(groupLayout.createParallelGroup(BASELINE)
                                .addComponent(Password)
                                .addComponent(passField))
                                .addGroup(groupLayout.createParallelGroup(BASELINE)
                                .addComponent(progress))
                                .addGroup(groupLayout.createParallelGroup(BASELINE)
                                .addComponent(progressBar)
                                .addComponent(time))
                                .addGroup(groupLayout.createParallelGroup()
                                .addComponent(cancle)
                                //.addComponent(validate)
                                .addComponent(upload)
                                .addComponent(deploy))
                                .addGroup(groupLayout.createParallelGroup()
                                .addComponent(operation)
                                .addComponent(time))
//                                .addGroup(groupLayout.createParallelGroup(BASELINE)
//                                .addComponent(area))
         );
        layoutpanel.setLayout(groupLayout);
        
        pane.add(layoutpanel);
        
        JPanel mainpanel1 = new JPanel();
        area.setPreferredSize(new Dimension(590,200));
        area.setBackground(Color.black);
        area.setForeground(Color.green);
        area.setFont( new Font("Serif", Font.PLAIN, 14));
        area.setSize(800, 100);
        mainpanel1.add(area);
        //mainpanel.setPreferredSize(new Dimension(400,200));
        //mainpanel.setBounds(10, 20, 700, 200);
        mainpanel1.setSize(new Dimension(600,150));
        mainpanel1.setBounds(5, 215, 595, 190);
        //mainpanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        //mainpanel1.setBackground(Color.GRAY);
        //frame.add(mainpanel1);
        //frame.add(mainpanel2);
        //frame.setBackground(Color.magenta);
//        group.setAutoCreateContainerGaps(true);
//        contentPanel.setLayout(group);
//        group.setHorizontalGroup(group.createSequentialGroup()
//        					.addGroup(group.createParallelGroup()
//        							.addComponent(area)));
//        group.setVerticalGroup(group.createSequentialGroup()
//        		.addGroup(group.createParallelGroup(BASELINE)
//        				.addComponent(area)));
        pane.add(mainpanel1);
        //pane.add(mainpanel2);
        frame.add(pane);
        //frame.setBackground(Color.BLACK);
        frame.pack();
        frame.setSize(620, 450);
        frame.setResizable(false);
        frame.setVisible(true);
        
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        // TODO Auto-generated method stub
        String flow = event.getActionCommand();
        System.out.println(flow);
        
        if(flow.equals("Browse")){
            System.out.println(flow);
            area.append("\n"+flow+"\n");
            JFileChooser jf = new JFileChooser(FileSystemView.getFileSystemView()); 
            // calling the showOpenDialog method to display the save dialog on the frame 
            int r = jf.showOpenDialog(null); 
            // if the user selects a file 
            if (r == JFileChooser.APPROVE_OPTION) 
            { 
                // setting the label as the path of the selected file 
                System.out.println(jf.getSelectedFile().getAbsolutePath());
                area.append(jf.getSelectedFile().getAbsolutePath()+"\n");
                button.setText(jf.getSelectedFile().getAbsolutePath());
                pathofFile = jf.getSelectedFile().getAbsolutePath().toString();
            } 
            // if the user canceled the operation 
            else { 
            	System.out.println("The user cancelled the operation");
            	area.append("The user cancelled the operation"+"\n");
            }
        }else if(flow.equals("Deploye")){
            System.out.println(event.getActionCommand().getBytes().toString());
            area.append(event.getActionCommand().getBytes().toString()+"\n");
            String password = passField.getText();
            
            progressBar.setValue(0);
            deployeEnvironment(password);
            progressBar.setValue(100);
            long end1 = System.currentTimeMillis();
        	float elapsedTime = (end1 - start1)/1000F;
        	String totaltime = String.valueOf(elapsedTime);
        	time.setText(totaltime);
        	System.out.println("calcuted time:  "+elapsedTime+"s");
            
        }else if(flow.equals("Cancle")) {
        	frame.dispose();
        }else if(flow.equals("Upload")) {
        	progressinglevel();
        	String password = passField.getText();
        	fileUpLoadsftp(password);
        	long end1 = System.nanoTime();
        	float elapsedTime = (end1 - start1)/1000F;
        	String totaltime = String.valueOf(elapsedTime);
        	time.setText(totaltime);
        	System.out.println("calcuted time:  "+elapsedTime+"s");
        }
    }
    
    
    private void fileUpLoadsftp(String password2) {
    	String host = null; // Replace with your server's IP address or hostname
    	if(envbox.getSelectedItem().toString().equalsIgnoreCase("DEV")) {
    		host = "172.20.162.122";
    	}else if(envbox.getSelectedItem().toString().equalsIgnoreCase("UAT")){
    		host = "172.20.162.123";
    	}    	
        String username = "nmehta"; // Replace with your SSH username
       
        System.out.println(password);
        String localFilePath = pathofFile;
        String remoteFilePath = "/opt/IBM/WebSphere/CBA_Deploy/cba";
        if(password2==null) {
        	operation.setText("File Uploade Failed!");
        	JOptionPane.showMessageDialog(null,"Password Can't be null","Message",
                    JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        if(localFilePath==null) {
        	operation.setText("File Uploade Failed!");
        	JOptionPane.showMessageDialog(null,"The File Path is Empty","Message",
                    JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        password = password2;// Replace with your SSH password (or use public key authentication)
        System.out.println(localFilePath);
        area.append("localFilePath :"+localFilePath+"\n");
        area.append("remoteFilePath :"+remoteFilePath+"\n");
        System.out.println(host);
        System.out.println(username);
        System.out.println(remoteFilePath);
        System.out.println(password);
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, 22); // Assuming SSH is running on port 22
            isfilepresent = true;
            // If you have a private key, use the following line instead of password-based authentication
            // jsch.addIdentity("path_to_your_private_key");

            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // Disable host key checking for simplicity (not recommended in production)

            System.out.println("Connecting to " + host + "...");
            area.append("Connecting to " + host + "..."+"\n");
            session.connect();

            System.out.println("Connected!");
            area.append("Connected!"+"\n");
            operation.setText("Connected!");
            // Now, you can execute remote commands. For example:
            executeRemoteCommand(session, "pwd");    // Print the current working directory
            
            ChannelSftp channelSftp = (ChannelSftp)session.openChannel("sftp");
            channelSftp.connect();
            
            System.out.println("Uploading file...");
            area.append("Uploading file..."+"\n");
            channelSftp.put(localFilePath, remoteFilePath);

            System.out.println("File uploaded successfully!");
            area.append("File uploaded successfully!"+"\n");
            operation.setText("File Uploade Successfully!");
            channelSftp.disconnect();
            session.disconnect();
            System.out.println("Disconnected.");
            area.append("Disconnected."+"\n");
        } catch (JSchException e) {
//        	JOptionPane.showMessageDialog(null,"Password Can't be null","Message",
//                    JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        } catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
    
	private void deployeEnvironment(String Usrpassword) {
		String host = null; // Replace with your server's IP address or hostname
		if(envbox.getSelectedItem().toString().equalsIgnoreCase("DEV")) {
			host = "172.20.162.122";
		}else if(envbox.getSelectedItem().toString().equalsIgnoreCase("UAT")){
			host = "172.20.162.123";
		}

		String username = "nmehta"; // Replace with your SSH username
		password = Usrpassword;// Replace with your SSH password (or use public key authentication)
		System.out.println(password);
		String localFilePath = pathofFile;
		String remoteFilePath = "/opt/IBM/WebSphere/CBA_Deploy/cba";
		if(password.isEmpty()) {
			JOptionPane.showMessageDialog(null,"Password Can't be null","Message",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		System.out.println(localFilePath);
		area.append("localFilePath :"+localFilePath+"\n");
		area.append("remoteFilePath :"+remoteFilePath+"\n");
		System.out.println(remoteFilePath);
		System.out.println(password);
		if(isfilepresent) {
			try {
				JSch jsch = new JSch();
				Session session = jsch.getSession(username, host, 22); // Assuming SSH is running on port 22

				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no"); // Disable host key checking for simplicity (not recommended in production)

				System.out.println("Connecting to " + host + "...");
				area.append("Connecting to " + host + "..."+"\n");

				session.connect();
				System.out.println("Connected!");
				area.append("Connected!"+"\n");
				operation.setText("Connected!");

				executeRemoteCommand(session, "pwd");                       
				executeRemoteCommand(session, "cd /opt/IBM/WebSphere/CBA_Deploy && sh cbacrud.sh"); 
				operation.setText("Deployement Completed");
				area.append("Deployement Completed"+"\n");
				session.disconnect();

				System.out.println("Disconnected.");
				area.append("Disconnected."+"\n");
			} catch (JSchException e) {
				//        	JOptionPane.showMessageDialog(null,"Password Can't be null","Message",
				//                    JOptionPane.INFORMATION_MESSAGE);
				e.printStackTrace();
			}
		}else {
			JOptionPane.showMessageDialog(null,"The File is not Present","Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
    
    private static void executeRemoteCommand(Session session, String command) throws JSchException {
    	Channel channel = session.openChannel("exec");
        try {
			InputStream in = channel.getInputStream();
			((ChannelExec) channel).setCommand(command);
			((ChannelExec) channel).setPty(true);

		    OutputStream out=channel.getOutputStream();
		    ((ChannelExec)channel).setErrStream(System.err);
			System.out.println("cmd "+command);
			channel.connect();
			
			try {
				out.write((password+"\n").getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		    out.flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line= reader.readLine())!=null) {
				area.append(line+"\n");
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}        
        channel.disconnect();
    }
	
    
	@Override
    public void itemStateChanged(ItemEvent event) {
        // TODO Auto-generated method stub
        if(event.getSource() == envbox){
            System.out.println(envbox.getSelectedItem().toString());
        }
    }
    @Override
    public void keyPressed(KeyEvent keypressed) {
        // TODO Auto-generated method stub
//        printit(keypressed);
    }
    @Override
    public void keyReleased(KeyEvent keyreleased) {
        // TODO Auto-generated method stub
        printit(keyreleased);
    }
    @Override
    public void keyTyped(KeyEvent kettyped) {
        // TODO Auto-generated method stub
    }
    private void printit(KeyEvent keypressed) {
    	
        char keycode = keypressed.getKeyChar();
        String str = "";
        str = str + keycode;
        if(keypressed.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        	StringBuffer sb = new StringBuffer(str);
        	sb.deleteCharAt(str.length()-1);
        }
        System.out.print(str);
    }
    
    public static void progressinglevel() {
    	int i=0;
    	while(i<=100){    
    		progressBar.setValue(i);    
    		i=i+1;    
    	}    
    }
    
    
}