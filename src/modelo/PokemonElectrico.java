/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kendr
 */
public class PokemonElectrico extends Pokemon {
     public PokemonElectrico(String nombre, int ps, int ataque,int defensa, int velocidad) {
        super(nombre, TipoPokemon.ELECTRICO, ps, ataque, defensa, velocidad);
    }

    @Override
    public void usarHabilidadEspecial(Pokemon objetivo) {
        if (objetivo == null || objetivo.estaDebilitado()||getUsosHabilidad()<=0 || estaDebilitado()) {
            return;
        }

        int daño = calcularDaño(objetivo, 17);
        //25% de probabilidad de critic
        if (Math.random()<0.25) {
            daño = (int) Math.round(daño*1.5);
        }
        objetivo.recibirDaño(daño);
        reducirUsosHabilidad();
    }
}
