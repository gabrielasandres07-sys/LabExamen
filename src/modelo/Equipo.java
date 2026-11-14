/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kendr
 */
import java.io.Serializable;
import java.util.ArrayList;

public class Equipo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private ArrayList<Pokemon> integrantes;

    public Equipo() {
        integrantes = new ArrayList<>();
    }

    public boolean agregarPokemon(Pokemon pokemon) {

        if (pokemon == null) {
            return false;
        }

        if (integrantes.size() >= 6) {
            return false;
        }

        int copias = 0;

        for (Pokemon integrante : integrantes) {

            if (integrante.getNombre().equals(pokemon.getNombre())) {
                copias++;
            }
        }

        if (copias >= 2) {
            return false;
        }

        integrantes.add(pokemon.clone());

        return true;
    }

    public boolean quitarPokemon(int posicion) {

        if (posicion < 0 || posicion >= integrantes.size()) {
            return false;
        }

        integrantes.remove(posicion);

        return true;
    }

    public Pokemon obtenerPokemon(int posicion) {

        if (posicion < 0 || posicion >= integrantes.size()) {
            return null;
        }

        return integrantes.get(posicion);
    }

    public int getCantidad() {
        return integrantes.size();
    }

    public boolean estaVacio() {
        return integrantes.isEmpty();
    }

    public boolean estaLleno() {
        return integrantes.size() >= 6;
    }

    public ArrayList<Pokemon> getIntegrantes() {
        return new ArrayList<>(integrantes);
    }

    public void limpiar() {
        integrantes.clear();
    }

    @Override
    public String toString() {
        return "Equipo: " + integrantes.size() + "/6";
    }
}
