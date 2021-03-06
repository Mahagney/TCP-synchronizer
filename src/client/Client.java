package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import utils.FilesReader;

public class Client extends Thread {
	Socket cs;
	private String pathToDir; //= "C:\\Users\\Mahag\\workspace\\TCP-synchronizer\\src\\resources\\client";
	OutputStream outputStream;
	InputStream inputStream;
	Date lastSync;
	private SimpleDateFormat simpleDateFormat;

	public Client(String path, String adr,int port) {
		pathToDir = path;
		cs = new Socket();
		InetSocketAddress sa = new InetSocketAddress(adr, port);
		simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
		try {
			lastSync = simpleDateFormat.parse("15/05/2018 - 00:00:00");
			cs.connect(sa);
			outputStream = cs.getOutputStream();
			inputStream = cs.getInputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {

		try {

			DataOutputStream dos = new DataOutputStream(outputStream);
			DataInputStream dis = new DataInputStream(inputStream);

			FilesReader fr = new FilesReader(pathToDir);
			Map<String, Long> files = fr.getFilesList();
			System.out.println(simpleDateFormat.format(lastSync));
			dos.writeUTF(simpleDateFormat.format(lastSync));
			dos.writeInt(files.size());
			System.out.println("size" + files.size());
			for (Entry<String, Long> file : files.entrySet()) {
				dos.writeUTF(file.getKey());
				dos.writeLong(file.getValue());
				System.out.println(file.getKey() + "\n" + file.getValue());
			}
			receiveFiles(dis, dos);
			dos.close();
			dis.close();
			String date = simpleDateFormat.format(new Date());
			try {
				lastSync = simpleDateFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("exceptie client");
			e.printStackTrace();
		}

	}

	private void receiveFiles(DataInputStream dis, DataOutputStream dos) throws IOException {
		int n = dis.readInt();
		int i;
		for (i = 0; i < n; i++) {
			String path = dis.readUTF();
			int action = dis.readInt();
			System.out.println(path + "\n" + action);
			if (action == 1) {
				receiveFile(dis, dos, pathToDir + path);
			} else {
				File file = new File(pathToDir + path);
				System.out.println(file.getAbsolutePath() + action + " file was deleted " + file.delete());
			}
		}

	}

	private void receiveFile(DataInputStream dis, DataOutputStream dos, String path) throws IOException {
		// TODO Auto-generated method stub
		long sz = dis.readLong();
		System.out.println("File Size: " + sz + " B");

		byte buffer[] = new byte[1024];
		System.out.println("Receving file..");
		File f = new File(path);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(f);
		int bytesRead;
		while (sz > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, sz))) != -1) {
			fos.write(buffer, 0, bytesRead);
			sz -= bytesRead;
		}

		System.out.println("Completed");
		fos.close();

	}

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("invalid arguments \n 1=server address\n2=path to dir\n3=port");
			return;
		}
		int i=Integer.parseInt(args[2]);
		Client c = new Client(args[0], args[1],i);
		c.start();
	}

}
