/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatservidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlos
 */
public class Server {

	private ServerSocket serverSocket;

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void startServer() {

		try {
			while (!serverSocket.isClosed()) {

				Socket socket = serverSocket.accept();
				System.out.println("Un nuevo usuario se a conectado " + socket);
				System.out.println("");
				ControladorCliente controladorCliente = new ControladorCliente(socket);
				Thread hilo = new Thread(controladorCliente);
				hilo.start();

			}
		} catch (IOException ex) {

		}
	}

	public void cerrarServerSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(10520);
		Server server = new Server(serverSocket);

		server.startServer();
	}
}
