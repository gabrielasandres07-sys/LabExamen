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
public abstract class Pokemon implements Serializable, Cloneable {
    
    private String nombre;
    private TipoPokemon tipo;
    private int nivel;
    private int psMaximos;
    private int psActuales;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int usosHabilidad;
    private int usosRecuperacion;

    public Pokemon(String nombre, TipoPokemon tipo, int psMaximos, int ataque, int defensa, int velocidad) {
        this.nombre=nombre;
        this.tipo=tipo;
        this.nivel=5;
        this.psMaximos=psMaximos;
        this.psActuales=psMaximos;
        this.ataque=ataque;
        this.defensa=defensa;
        this.velocidad=velocidad;
        this.usosHabilidad=3;
        this.usosRecuperacion=2;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoPokemon getTipo() {
        return tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public int getPsMaximos() {
        return psMaximos;
    }

    public int getPsActuales() {
        return psActuales;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public int getUsosHabilidad() {
        return usosHabilidad;
    }

    public int getUsosRecuperacion() {
        return usosRecuperacion;
    }

    public boolean estaDebilitado() {
        return psActuales<=0;
    }

    public void recibirDaño(int cantidad) {

        psActuales= psActuales-cantidad;
        if (psActuales<0) {
            psActuales=0;
        }
    }

    public int calcularDaño(Pokemon objetivo, int potencia) {

        double multiplicador=tipo.obtenerMultiplicador(objetivo.getTipo());

        double resultado=(ataque+nivel+potencia-objetivo.getDefensa()/2.0)*multiplicador;
        return Math.max(1, (int) Math.round(resultado));
    }

    public void reducirUsosHabilidad(){
        //redjcir los pps del movimiento
        if (usosHabilidad>0) {
            usosHabilidad--;
        }
    }

    public boolean recuperarPS() {
        if (usosRecuperacion<=0||psActuales==psMaximos||estaDebilitado()) {
            return false;
        }
        int cantidad=(int)(psMaximos*0.25);
        psActuales=psActuales+cantidad;

        if (psActuales>psMaximos) {
            psActuales=psMaximos;
        }
        usosRecuperacion--;
        return true;
    }

    public abstract void usarHabilidadEspecial(Pokemon objetivo);

    @Override
    public Pokemon clone() {
        try {
            return (Pokemon) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
    public void recuperarCantidad(int cantidad) {

    if (cantidad > 0 && !estaDebilitado()) {

        psActuales = psActuales + cantidad;

        if (psActuales > psMaximos) {
            psActuales = psMaximos;
        }
    }
}

    @Override
    public String toString() {
        return nombre+" - "+tipo+" - PS: "+psActuales+"/"+psMaximos;
    }
    
}
