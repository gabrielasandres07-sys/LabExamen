/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USER
 */

import java.io.Serializable;

public class Entrenador implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MIN_CARACTERES = 3;
    public static final int MAX_CARACTERES = 10;

    private final String nombre;
    private Equipo equipo;

    public Entrenador(String nombre) {
        String error = validarNombre(nombre);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }
        this.nombre = nombre.trim();
        this.equipo = new Equipo();
    }

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre no puede estar vacío ni tener solo espacios.";
        }
        String limpio = nombre.trim();

        if (limpio.length() < MIN_CARACTERES || limpio.length() > MAX_CARACTERES) {
            return "El nombre debe tener entre " + MIN_CARACTERES + " y " + MAX_CARACTERES
                    + " caracteres (tiene " + limpio.length() + ").";
        }
        if (!limpio.matches("[\\p{L}\\d ]+")) {
            return "El nombre solo puede contener letras, números y espacios.";
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}