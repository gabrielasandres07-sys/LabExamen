/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kendr
 */
import java.util.ArrayList;

public class Catalogo {
    private ArrayList<Pokemon> catalogo;
    public Catalogo() {

        catalogo = new ArrayList<>();

        catalogo.add(new PokemonFuego("Charmander", 90, 14, 8, 11));
        catalogo.add(new PokemonFuego("Vulpix", 85, 13, 9, 12));
        catalogo.add(new PokemonAgua("Squirtle", 100, 11, 12, 9));
        catalogo.add(new PokemonAgua("Psyduck", 95, 12, 9, 10));
        catalogo.add(new PokemonPlanta("Bulbasaur", 100, 12, 10, 8));
        catalogo.add(new PokemonPlanta("Oddish", 90, 13, 8, 7));
        catalogo.add(new PokemonElectrico("Pikachu", 80, 14, 7, 15));
        catalogo.add(new PokemonElectrico("Voltorb", 75, 12, 9, 16));
    }

    public int getCantidad() {
        return catalogo.size();
    }

    public Pokemon obtenerPokemon(int posicion) {
        if (posicion<0||posicion>=catalogo.size()) {
            return null;
        }

        return catalogo.get(posicion).clone();
    }

    public ArrayList<Pokemon> obtenerCatalogo() {

        ArrayList<Pokemon> copia=new ArrayList<>();

        for (Pokemon pokemon:catalogo) {
            copia.add(pokemon.clone());
        }
        return copia;
    }    
}
