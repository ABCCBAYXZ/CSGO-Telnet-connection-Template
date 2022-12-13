package CSGO;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;


public class Conductor {

	private Telnet connection;
	private CSGOReader csr;
	private CSGOWriter csw;

	Conductor() throws IOException, InterruptedException {
		connection = new Telnet();
		if (connection.getSocket() != null) {
			csr = new CSGOReader(new BufferedReader(new InputStreamReader(connection.getSocket().getInputStream())));
			csw = new CSGOWriter(new DataOutputStream(connection.getSocket().getOutputStream()));
		}
	}
	
	private void skipLines(int i) throws IOException {
		while (i-- > 0) 
			getCsr().read();
	}

	//use this to check console output bytes vs your Text bytes
	@SuppressWarnings("unused")
	private void debug(String line, String textToCheck) {
		System.out.println("line bytes:" + Arrays.toString(line.getBytes()));
		System.out.println("TextToCheck bytes:" + Arrays.toString(textToCheck.getBytes()));
	}
	
	private void close() {
		getCsw().close();
		getConnection().close();
		getCsr().close();
		
	}
	
	public Telnet getConnection() {
		return connection;
	}

	public void setConnection(Telnet connection) {
		this.connection = connection;
	}

	public CSGOReader getCsr() {
		return csr;
	}

	public void setCsr(CSGOReader csr) {
		this.csr = csr;
	}

	public CSGOWriter getCsw() {
		return csw;
	}

	public void setCsw(CSGOWriter csw) {
		this.csw = csw;
	}

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException{

		//opens CS:GO
		URI uri = new URI("steam://run/730");
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(uri);
		}

		//opens Telnet connection and reader/writer
		Conductor conductor = new Conductor();
		String line = "";
		
		//writes connected to CSGO console if telnet connection is up
		if (conductor.getCsr() != null) 
			conductor.getCsw().writeConsole("Connected!");
			

		//reads every line of CS:GO Console
		while (conductor.getCsr() != null && (line = conductor.getCsr().read()) != null) {
			
				if(conductor.getCsr().isHello(line)) {
					conductor.getCsw().writeConsole("Hello World");
					conductor.skipLines(1);
				}
				
				if(conductor.getCsr().isStop(line)) {
					conductor.getCsw().writeConsole("disconnected");
					conductor.close();
				}
				
		}

	}


}
