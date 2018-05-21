package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import utils.FilesReader;
//1 resend file
//2 delete file

public class FileSync extends Thread {
	private OutputStream outputStream;
	private InputStream inputStream;
	private Socket cs;
	private Map<String, Long> serverfiles = new HashMap<String, Long>();
	private Map<String, Long> clientFiles = new HashMap<String, Long>();
	private String pathToDir;
	private Map<String, Integer> toSendFiles = new HashMap<String, Integer>();
	private FilesReader fr ;

	public FileSync(Socket cs1, String path) {
		this.cs = cs1;
		pathToDir=path;
		fr= new FilesReader(pathToDir);
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
		try {
			String date = dis.readUTF();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
			Date lastSync = formatter.parse(date);
			int size = dis.readInt();
			String path;
			long dim;
			for (int i = 0; i < size; i++) {
				path = dis.readUTF();
				dim = dis.readLong();
				clientFiles.put(path, dim);
			}
			System.out.println(" am de citit " + clientFiles.size() + " fisiere");

			serverfiles = fr.getFilesList();
			for (Entry<String, Long> file : clientFiles.entrySet()) {
				String filePath = file.getKey();
				if (serverfiles.containsKey(filePath)) {
					if (serverfiles.get(filePath) != file.getValue()) {
						toSendFiles.put(filePath, 1);
						serverfiles.remove(filePath);
					} else {
						File f = new File(filePath);
						Long lastModified = f.lastModified();
						Date lm = new Date(lastModified);
						if (lm.after(lastSync)) {
							toSendFiles.put(filePath, 1);
						}
						serverfiles.remove(filePath);
					}
				} else {
					toSendFiles.put(filePath, 2);
				}
			}
			for (Entry<String, Long> file : serverfiles.entrySet()) {
				toSendFiles.put(file.getKey(), 1);
			}
			toSendFiles.size();
			System.out.println(toSendFiles);
			sendResultToClient(dos, dis, toSendFiles);
			dos.close();
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendResultToClient(DataOutputStream dos, DataInputStream dis, Map<String, Integer> toSendFiles)
			throws IOException {
		dos.writeInt(toSendFiles.size());
		for (Entry<String, Integer> file : toSendFiles.entrySet()) {
			dos.writeUTF(file.getKey());
			Integer i = file.getValue();
			dos.writeInt(i);
			if (i == 1) {
				sendFile(dos, dis, file.getKey());
			}
		}
	}

	private void sendFile(DataOutputStream dout, DataInputStream dis, String path) throws IOException {
		// TODO Auto-generated method stub

		File f = new File(pathToDir + path);
		FileInputStream fin = new FileInputStream(f);
		long sz = f.length();

		byte b[] = new byte[1024];

		int read;

		dout.writeLong(sz);

		while (sz > 0 && (read = fin.read(b, 0, (int) Math.min(b.length, sz))) != -1) {
			dout.write(b, 0, read);
			sz -= read;
		}
		fin.close();

	}

}
