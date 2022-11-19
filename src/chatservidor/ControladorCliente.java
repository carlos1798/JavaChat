/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatservidor;

import java.util.ArrayList;
import java.io.*;
import java.net.Socket;


public class ControladorCliente implements Runnable {
	
	public static ArrayList<ControladorCliente> controladoresCliente = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String nombreUsuarioCliente;


	
	 public ControladorCliente(Socket socket) {
		 try {
		this.socket = socket;
		this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.nombreUsuarioCliente = bufferedReader.readLine();


		controladoresCliente.add(this);
		broadcastMensaje("SERVER: "+ nombreUsuarioCliente + " ha entrado en el servidor");
		
		 }catch(IOException ex) {
			 cerrarTodo(socket,bufferedReader,bufferedWriter);
		 }
	}

	@Override
	public void run() {
		String mensajeCliente;

		while(socket.isConnected()) {
			try {
				mensajeCliente = bufferedReader.readLine();
				broadcastMensaje(mensajeCliente);
			}catch(IOException ex) {
				cerrarTodo(socket,bufferedReader,bufferedWriter);
				break;
		
				
			}
		}
		

		
	}

	
	public void broadcastMensaje(String mensajeAEnviar) {
		for(ControladorCliente controladorCliente: controladoresCliente) {
			try {
				
				if(!controladorCliente.nombreUsuarioCliente.equals(nombreUsuarioCliente)) {
					controladorCliente.bufferedWriter.newLine();
					controladorCliente.bufferedWriter.write(mensajeAEnviar+" ");
					controladorCliente.bufferedWriter.newLine();
					controladorCliente.bufferedWriter.flush();				
					
					
				}}catch (IOException ex) {
				cerrarTodo(socket,bufferedReader,bufferedWriter);
				}
				
				
			}
			
		}
		
	public void eliminarControladorCliente() {
		controladoresCliente.remove(this);
		broadcastMensaje("SERVER: "+nombreUsuarioCliente+ "ha dejado el chat!");
	}
	public void cerrarTodo(Socket socket, BufferedReader bufferedReader,BufferedWriter bufferedWriter) {

		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
				if(socket != null) {
				socket.close();
			}
					}catch(IOException ex) {
						ex.printStackTrace();
					}

	}
public String getNombreUsuario() {
	return nombreUsuarioCliente;
}
}
	