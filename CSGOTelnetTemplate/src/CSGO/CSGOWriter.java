package CSGO;

import java.io.DataOutputStream;
import java.io.IOException;

public class CSGOWriter {
	private DataOutputStream writer;
	private String command;

	public CSGOWriter(DataOutputStream _writer) {
		writer = _writer;
	}

	public void writeConsole(String text) throws IOException, InterruptedException {
		command = "echo ";
		write(text);

	}

	public void writeSameManyTimes(int i, String text) throws IOException {
		
		for(int y = 0; y < i; y++) 
			write(text);

	}
	
	private void write(String text) throws IOException {

		command += text;
		command = (command + "\n").replace("\0xFF", "\0xFF\0xFF");

		byte[] arr = command.getBytes("UTF8");

		writer.write(arr, 0, arr.length);
		writer.flush();
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
