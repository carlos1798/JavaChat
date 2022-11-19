
package chatservidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Cliente{


	private String nombreUsuario;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	 Sincronizacion sincro;

	public Cliente(Socket socket , String nombreUsuario){
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.nombreUsuario = nombreUsuario;
		}catch (IOException ex) {

			cerrarTodo(socket,bufferedReader,bufferedWriter);
		}

	}
	public void enviarMensaje() {
		try {

			bufferedWriter.write(nombreUsuario);
			bufferedWriter.newLine();
			bufferedWriter.flush();

			Scanner sc = new Scanner(System.in);

			while(socket.isConnected()) {
				String mensaje = sc.nextLine();
				if(mensaje.equals("/usuarios")) {
					Sincronizacion.getListaSincronizada();

				}else {
					bufferedWriter.newLine();
					bufferedWriter.write(nombreUsuario+": "+mensaje);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
			}

		}catch(IOException ex) {

			cerrarTodo(socket,bufferedReader,bufferedWriter);
		}
	}
	public void escucharMensaje() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mensajeDelGrupo;
				while(socket.isConnected()) {
					try {

						mensajeDelGrupo = bufferedReader.readLine();

						System.out.print(mensajeDelGrupo);

					}catch(IOException ex) {
						cerrarTodo(socket,bufferedReader,bufferedWriter);
					}
				}

			}

		}).start();

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
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in); 
		System.out.println("Introduce el nombre de usuario que quieres usar");
		String nombreUsuario = sc.nextLine();
		Socket socket = new Socket("localhost",10520);
		Cliente cliente = new Cliente(socket,nombreUsuario);
		synchronized(Sincronizacion.class) {
			
		Sincronizacion.addCliente(cliente);	
		}
		cliente.escucharMensaje();
		cliente.enviarMensaje();
	}


}
