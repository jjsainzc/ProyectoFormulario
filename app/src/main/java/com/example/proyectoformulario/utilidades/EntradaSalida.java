package com.example.proyectoformulario.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EntradaSalida {
	public  static <T> void  escribirArchivoObjeto( File file, List<T> lista) throws FileNotFoundException, IOException {
		
		ObjectOutputStream oos;

		oos = new ObjectOutputStream(new FileOutputStream(file));
		
		for (T obj : lista) {
			oos.writeObject(obj);
		}
		oos.flush();
		oos.close();
	}
	
	public static <T> List<T> leerArchivoObjeto(File file) throws IOException, ClassNotFoundException {
		List<T> lista = new ArrayList();
		T obj;
		
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois;

		ois = new ObjectInputStream(fis);

		while (fis.available() > 0) {
			obj = (T) ois.readObject();
			lista.add(obj);
		}
		ois.close();
		
		return lista;
	}
	
	
	
	
}
