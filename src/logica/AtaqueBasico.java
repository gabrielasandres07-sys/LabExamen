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
public class AtaqueBasico implements Movimiento{
    private final int POTENCIA=19;
    
    @Override
    public void ejecutar(Pokemon atacante,Pokemon defensor){
        if (atacante.estaDebilitado()||defensor.estaDebilitado()) {
            return;
        }
        int daño=atacante.calcularDaño(defensor, POTENCIA);
        defensor.recibirDaño(daño);
        
    }
    @Override
    public String getNombre(){
        return "Ataque basico";
    }
}
