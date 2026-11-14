/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USER
 */
import java.io.*;


public class GestorMemoria {

    private byte[] copiaGuardada;
    public void guardar(Entrenador entrenador) throws IOException {
        if (entrenador == null) {
            throw new IllegalArgumentException("No hay entrenador para guardar.");
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream salida = new ObjectOutputStream(bytes)) {
            salida.writeObject(entrenador);
        }
        copiaGuardada = bytes.toByteArray();
    }

    public Entrenador restaurar() throws IOException, ClassNotFoundException {
        if (copiaGuardada == null) {
            throw new IllegalStateException("No hay ningún equipo guardado.");
        }

        try (ObjectInputStream entrada =
                     new ObjectInputStream(new ByteArrayInputStream(copiaGuardada))) {
            return (Entrenador) entrada.readObject();
        }
    }

    public boolean hayCopia() {
        return copiaGuardada != null;
    }
}
