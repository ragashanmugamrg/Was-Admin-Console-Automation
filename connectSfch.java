package cbaconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class connectSfch   
{  
	public static void main(String[] args) {
		 	String host = "192.168.56.111"; // Replace with your server's IP address or hostname
	        String username = "mdmadmin"; // Replace with your SSH username
	        String password = "mdmadmin";// Replace with your SSH password (or use public key authentication)
	        
	        String localFilePath = "D:\\BBMComposites.cba_1.0.0.202305041036.cba";
	        String remoteFilePath = "/home/nmehta";

	        try {
	            JSch jsch = new JSch();
	            Session session = jsch.getSession(username, host, 22); // Assuming SSH is running on port 22

	            // If you have a private key, use the following line instead of password-based authentication
	            // jsch.addIdentity("path_to_your_private_key");

	            session.setPassword(password);
	            session.setConfig("StrictHostKeyChecking", "no"); // Disable host key checking for simplicity (not recommended in production)

	            System.out.println("Connecting to " + host + "...");
	            session.connect();

	            System.out.println("Connected!");

	            // Now, you can execute remote commands. For example:
//	            executeRemoteCommand(session, "ls -l"); // List files in the home directory
	            executeRemoteCommand(session, "pwd");    // Print the current working directory
	            
	            ChannelSftp channelSftp = (ChannelSftp)session.openChannel("sftp");
	            channelSftp.connect();
	            
	            System.out.println("Uploading file...");
	            //channelSftp.put(localFilePath, remoteFilePath);

	            System.out.println("File uploaded successfully!");
	            
	            System.out.println(channelSftp.pwd());
	            executeRemoteCommand(session, "cd /home/mdmadmin && sh getoldcba.sh");
	            executeRemoteCommand(session, "pwd");
	            
	            channelSftp.disconnect();
	            session.disconnect();
	            System.out.println("Disconnected.");
	        } catch (JSchException e) {
	            e.printStackTrace();
	        } catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			
			out.write(("mdmadmin"+"\n").getBytes());
		    out.flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line= reader.readLine())!=null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        channel.disconnect();
//        ((ChannelExec) channel).setCommand(command);
//        channel.setInputStream(null);
//        ((ChannelExec) channel).setErrStream(System.err);

//        try (InputStream in = channel.getInputStream()) {
//            channel.connect();
//            byte[] tmp = new byte[1024];
//            while (true) {
//                while (in.available() > 0) {
//                    int i = in.read(tmp, 0, 1024);
//                    if (i < 0) break;
//                    System.out.print(new String(tmp, 0, i));
//                }
//                if (channel.isClosed()) {
//                    if (in.available() > 0) continue;
//                    System.out.println("exit-status: " + channel.getExitStatus());
//                    break;
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            channel.disconnect();
//        }
    }
	
	
}  