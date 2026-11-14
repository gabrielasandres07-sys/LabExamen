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
public class HabilidadEspecial implements Movimiento{
    @Override
    public void ejecutar(Pokemon atacante,Pokemon defensor){
        if (atacante.estaDebilitado()||defensor.estaDebilitado()){
            return;
        }
        if (atacante.getUsosHabilidad()<=0) {
            return;
        }
        atacante.usarHabilidadEspecial(defensor);
        atacante.reducirUsosHabilidad();
    }
    @Override
    public String getNombre(){
        return "Habilidad especial";
    }
    
}
