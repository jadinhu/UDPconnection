/**
 * Server.cs
 * Created by: Jadson Almeida [jadson.sistemas@gmail.com]
 * Created on: 08/04/19 (dd/mm/yy)
 * Updated on: 09/04/19 (dd/mm/yy)
 */
package udp;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import window.WindowDisplay;

/**
 * UDP server controller
 * 
 * @author Jadson
 */
public class ConnectionHandler implements Runnable {

	// endereço de IP (ex: 213.168.55.06)
	String address = "localhost";
	// socket de conexao de datagrama (canal)
	DatagramSocket socket;
	// classe com informações de IP
	InetAddress ipAddress;
	// porta para enviar mensagens
	int sendPort;
	// porta para receber mensagens
	int receivePort;
	// thread para ouvir constantemente a chegada de mensagens sem travar o app
	Thread thread;
	// classe interna de interface visual
	WindowDisplay window;
	// classes responsavel por enviar e receber mensagens
	ShareData shareData;

	// construtor
	public ConnectionHandler(String address, int sendPort, int receivePort) {
		this.address = address;
		this.sendPort = sendPort;
		this.receivePort = receivePort;
	}

	// inicio do funcionamento da conexao
	public void Start() {
		Setup();
		thread = new Thread(this);
		thread.run();
	}

	// método que configura e inicia conexão 
	void Setup() {
		try {
			// configura o endereço de ip
			ipAddress = InetAddress.getByName(address);
			// cria um soquete de datagrama com o ip e a porta que recebe msgs
			socket = new DatagramSocket(new InetSocketAddress(ipAddress, receivePort));
			// metodo que finaliza a thread quando o app é fechado
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					socket.close();
					window.AddLineToLabel("Socket server fechado!");
				}
			});
			// criação da classe de envio e recebimento de msgs
			shareData = new ShareData(socket, ipAddress, sendPort, receivePort);
			// criação da classe de interface visual
			window = new WindowDisplay("SERVER", shareData, this);
			// atualiza quem é a classe de interface visual 
			shareData.UpdateWindow(window);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	// roda a thread com o metodo que escuta o recebimento de mensagens
	@Override
	public void run() {
		shareData.Receive();
	}

	// metodo de finalização do ap
	public void Close() {
		socket.close();
		Runtime.getRuntime().halt(0);
	}
}