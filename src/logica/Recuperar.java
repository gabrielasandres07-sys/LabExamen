/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

/**
 *
 * @author segio
 */
import modelo.Pokemon;
public class Recuperar implements Movimiento{
    @Override
    public void ejecutar(Pokemon atacante,Pokemon defensor){
        if (atacante.estaDebilitado()) {
            return;
        }
        atacante.recuperarPS();
    }
    @Override
    public String getNombre(){
        return "Recuperar";
    }
}
