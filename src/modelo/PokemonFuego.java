/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kendr
 */
public class PokemonFuego extends Pokemon {
    public PokemonFuego(String nombre, int ps, int ataque,int defensa, int velocidad) {

        super(nombre, TipoPokemon.FUEGO, ps, ataque, defensa, velocidad);
    }

    @Override
    public void usarHabilidadEspecial(Pokemon objetivo) {

        if (objetivo==null || objetivo.estaDebilitado()||getUsosHabilidad()<=0 || estaDebilitado()) {
            return;
        }

        int daño = calcularDaño(objetivo, 20);
        objetivo.recibirDaño(daño);
        reducirUsosHabilidad();
    }
}
