package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import window.WindowDisplay;

public class ShareData {
	// soquete de datagrama (canal)
	DatagramSocket socket;
	// classe de configuração de IP
	InetAddress ipAddress;
	// porta para envio de msgs
	int sendPort;
	// porta para recebimento de msgs
	int receivePort;
	// classe de interface visual
	WindowDisplay window;
	
	// construtor que recebe os dados da classe ConnectionHandler
	public ShareData(DatagramSocket socket, InetAddress ipAddress, int sendPort, int receivePort) {
		this.socket = socket;
		this.ipAddress = ipAddress;
		this.sendPort = sendPort;
		this.receivePort = receivePort;
	}
	
	// envia mensagem
	public void Send(String message) {
		// acrescenta o remetente no final da msg
		message += " / FROM " + ipAddress.getHostAddress() + " / " + sendPort;
		// criacao de array de bytes para enviar a msg (toda transferencia é feita em bytes)
		byte[] sendData = new byte[1024];
		// transforma a msg em bytes
		sendData = message.getBytes();
		// cria um pacote de datagrama para enviar a msg
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, sendPort);
		try {
			// envia a msgs através do socket
			socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// metodo que constantemente ouve se uma msg nova chegou
	public void Receive() {
		try {
			// array para receber a msg
			byte[] receiveData = new byte[1024];
			// conta as tentativas de recebimento (só visual)
			int counter = 0;
			while (true) {
				// cria um pacote de datagrama para receber a msg
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length, ipAddress, receivePort);
				// adiciona na interface o texto sobre a tentativa de recebimento
				window.AddLineToLabel(
						"Tentativa " + ++counter + ". Esperando por datagrama UDP na porta " + receivePort);
				// começa a ouvir mensagens a serem recebidas
				socket.receive(receivePacket);
				// informa que uma msg foi recebida
				window.AddLineToLabel("recebido...");
				// transforma os bytes do pacote de datagrama, em uma String
				String sentence = new String(receivePacket.getData());
				// adiciona a String acima na interface
				window.AddLineToLabel("RECEIVED: " + sentence);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// atualiza a classe de interface visual
	public void UpdateWindow(WindowDisplay window) {
		this.window = window;
	}
}
