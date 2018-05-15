package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.DateFormatter;

import utils.FilesReader;
//1 resend file
//2 delete file

public class FileSync extends Thread {
	private OutputStream outputStream;
	private InputStream inputStream;
	private Socket cs;
	private Map<String, Long> serverfiles = new HashMap<String, Long>();
	private Map<String, Long> clientFiles = new HashMap<String, Long>();
	private Map<String, Integer> toSendFiles = new HashMap<String, Integer>();
	private FilesReader fr=new FilesReader("C:\\Users\\Mahag\\workspace\\TCP-synchronizer\\src\\resources\\server");

	public FileSync(Socket cs) {
		this.cs = cs;
		try {
			outputStream = cs.getOutputStream();
			inputStream = cs.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		DataOutputStream dos = new DataOutputStream(outputStream);
		DataInputStream dis = new DataInputStream(inputStream);
		//while (true) {
			try {
				String date = dis.readUTF();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
				Date lastSync = new Date();
				int size = dis.readInt();
				String path;
				long dim;
				for (int i = 0; i < size; i++) {
					path = dis.readUTF();
					dim = dis.readLong();
					clientFiles.put(path, dim);
				}
				System.out.println(" am de citit " + clientFiles.size() + " fisiere");

				serverfiles=fr.getFilesList();
				for(Entry<String, Long> file:clientFiles.entrySet()){
					String filePath = file.getKey();
					if(serverfiles.containsKey(filePath)){
						if(serverfiles.get(filePath)!=file.getValue()){
							toSendFiles.put(filePath, 1);
							serverfiles.remove(filePath);
						}
						else{
							File f=new File(filePath);
							Long lastModified=f.lastModified();
						    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
						    Date lm= new Date(lastModified);
						    String format = dateFormat.format(lm);
						    Date lastMod= dateFormat.parse(format);
						    if(lm.after(lastSync)){
						    	toSendFiles.put(filePath, 1);
						    }
					    	serverfiles.remove(filePath);
						}
					}else{
						toSendFiles.put(filePath, 2);
					}
				}
				for(Entry<String, Long> file:serverfiles.entrySet()){
					toSendFiles.put(file.getKey(), 1);
				} 
				toSendFiles.size();
				System.out.println(toSendFiles);
				sendResultToClient(dos, toSendFiles);
				dos.close();
				dis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}

	}
	
	private void sendResultToClient(DataOutputStream dos,Map<String, Integer> toSendFiles) throws IOException{
		dos.writeInt(toSendFiles.size());
		for(Entry<String, Integer> file:toSendFiles.entrySet()){
			dos.writeUTF(file.getKey());
			dos.flush();
			Integer i=file.getValue();
			dos.writeInt(i);
			if(i==1){
				sendFile(dos,file.getKey());
			}
		}
	}

	private void sendFile(DataOutputStream dos, String key) {
		// TODO Auto-generated method stub
		
	}
}
