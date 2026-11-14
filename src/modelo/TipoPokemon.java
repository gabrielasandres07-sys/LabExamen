/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kendr
 */
public enum TipoPokemon {
    FUEGO,
    AGUA,
    PLANTA,
    ELECTRICO;

    public double obtenerMultiplicador(TipoPokemon defensor) {
        double[][] tabla = {
            // fuego, agua, planta, elec
            {0.5, 0.5, 2.0, 1.0}, // fuego
            {2.0, 0.5, 0.5, 1.0}, // agua
            {0.5, 2.0, 0.5, 1.0}, // planta
            {1.0, 2.0, 0.5, 0.5}  // electrico
            //0.5 es no muy eficaz o resiste
                //1 es daño normal
                //2 es muy eficaz
        };

        return tabla[this.ordinal()][defensor.ordinal()];
    }
}
