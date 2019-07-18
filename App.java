package app;

import udp.ConnectionHandler;

public class App {
	
	public static void main(String[] args) {
		ConnectionHandler c = new ConnectionHandler("localhost", 9878, 9876);
		c.Start();
	}
}
