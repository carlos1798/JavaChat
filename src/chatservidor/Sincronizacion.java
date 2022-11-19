package chatservidor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sincronizacion {
	
	static CopyOnWriteArrayList<Cliente> listaUsu = new CopyOnWriteArrayList<Cliente>();

		public static  synchronized void getListaSincronizada() {
		Iterator<Cliente> it = listaUsu.iterator();
		while(it.hasNext()) {
			System.out.print(it.next());
		}

	}
	public static synchronized void addCliente(Cliente cliente) {


			listaUsu.add(cliente);

	}	
}
