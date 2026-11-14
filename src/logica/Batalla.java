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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modelo.Equipo;
import modelo.Pokemon;
import modelo.Catalogo;

public class Batalla {
    private Equipo equipoJugador;
    private Equipo equipoCPU;   
    private int activoJugador;
    private int activoCPU;
    private int ronda;
    private Movimiento ataque;
    private Movimiento habilidad;
    private Movimiento recuperar;
    private Random random;
    private List<String> historial;
    private EstadoBatalla estado;
    private static final int MAX_RONDAS = 50;
    
    public Batalla(Equipo jugador) {
        if (jugador == null || jugador.estaVacio()) {
            throw new IllegalArgumentException("El jugador necesita un equipo.");
        }
        equipoJugador = copiarEquipo(jugador);
        equipoCPU = crearEquipoCPU(jugador.getCantidad());
        activoJugador = 0;
        activoCPU = 0;
        ronda = 1;
        ataque = new AtaqueBasico();
        habilidad = new HabilidadEspecial();
        recuperar = new Recuperar();
        random = new Random();
        historial = new ArrayList<>();
        estado = EstadoBatalla.ESPERANDO_ACCION;
        registrar("Comienza la batalla Pokemon.");
        registrar("Ronda 1");
    }

    private Equipo copiarEquipo(Equipo original) {
        Equipo copia = new Equipo();
        for (int i = 0; i < original.getCantidad(); i++) {
            Pokemon pokemon = original.obtenerPokemon(i);
            if (pokemon == null || pokemon.estaDebilitado()|| !copia.agregarPokemon(pokemon)) {
                throw new IllegalArgumentException("El equipo contiene Pokemon invalidos.");
            }
        }

        return copia;
    }

    private Equipo crearEquipoCPU(int cantidad) {
        Equipo equipo = new Equipo();
        Catalogo catalogo = new Catalogo();
        Random aleatorio = new Random();
        while (equipo.getCantidad() < cantidad) {
            int posicion = aleatorio.nextInt(catalogo.getCantidad());
            Pokemon pokemon = catalogo.obtenerPokemon(posicion);
            equipo.agregarPokemon(pokemon);
        }

        return equipo;
    }

    public void jugarRonda(Accion accion, int cambio) {
        if (estado != EstadoBatalla.ESPERANDO_ACCION) {
            throw new IllegalStateException(
                    "No puedes jugar en este momento.");
        }
        if (!accionValida(equipoJugador, activoJugador,accion, cambio)) {
            throw new IllegalArgumentException("La accion no es valida.");
        }
        estado = EstadoBatalla.RESOLVIENDO_RONDA;
        Accion accionCPU = elegirAccionCPU();
        int cambioCPU = -1;
        if (accionCPU == Accion.CAMBIAR) {
            cambioCPU = elegirCambioCPU();
        }
        Pokemon jugador = getPokemonJugador();
        Pokemon cpu = getPokemonCPU();
        boolean jugadorPrimero;

        if (accion == Accion.CAMBIAR&& accionCPU != Accion.CAMBIAR) {
            jugadorPrimero = true;
        } else if (accionCPU == Accion.CAMBIAR&& accion != Accion.CAMBIAR) {
            jugadorPrimero = false;
        } else {
            jugadorPrimero = jugador.getVelocidad()>= cpu.getVelocidad();
        }

        if (jugadorPrimero) {
            ejecutarAccion(true, accion, cambio);
            if (!batallaTerminada()) {
                ejecutarAccion(false, accionCPU, cambioCPU);
            }
        } else {
            ejecutarAccion(false, accionCPU, cambioCPU);
            if (!batallaTerminada()) {
                ejecutarAccion(true, accion, cambio);
            }
        }
        finalizarRonda();
    }

    private boolean accionValida(Equipo equipo,int activo, Accion accion, int cambio) {

        if (accion == null) {
            return false;
        }
        Pokemon pokemon = equipo.obtenerPokemon(activo);
        if (pokemon == null || pokemon.estaDebilitado()) {
            return false;
        }

        switch (accion) {
            case ATACAR:
                return true;
            case HABILIDAD:
                return pokemon.getUsosHabilidad() > 0;
            case RECUPERAR:
                return pokemon.getUsosRecuperacion() > 0 && pokemon.getPsActuales() < pokemon.getPsMaximos();
            case CAMBIAR:
                return cambio >= 0&& cambio < equipo.getCantidad()&& cambio != activo&& !equipo.obtenerPokemon(cambio).estaDebilitado();

            default:
                return false;
        }
    }

    private Accion elegirAccionCPU() {
        ArrayList<Accion> opciones = new ArrayList<>();
        for (Accion accion : Accion.values()) {
            if (accion == Accion.CAMBIAR) {
                if (elegirCambioCPU() != -1) {
                    opciones.add(accion);
                }
            } else if (accionValida(equipoCPU,activoCPU, accion, -1)) {
                opciones.add(accion);
            }
        }
        return opciones.get(random.nextInt(opciones.size()));
    }

    private int elegirCambioCPU() {
        ArrayList<Integer> disponibles = new ArrayList<>();
        for (int i = 0; i < equipoCPU.getCantidad(); i++) {
            if (i != activoCPU&& !equipoCPU.obtenerPokemon(i).estaDebilitado()) {
                disponibles.add(i);
            }
        }
        if (disponibles.isEmpty()) {
            return -1;
        }

        return disponibles.get(random.nextInt(disponibles.size()));
    }

    private void ejecutarAccion(boolean esJugador,Accion accion, int cambio) {
        Equipo equipo = esJugador? equipoJugador : equipoCPU;
        Pokemon atacante = esJugador? getPokemonJugador() : getPokemonCPU();

        if (atacante.estaDebilitado()) {
            registrar(atacante.getNombre()+ " esta debilitado y pierde su turno.");
            return;
        }

        String quien = esJugador ? "Jugador" : "CPU";
        if (accion == Accion.CAMBIAR) {
            if (esJugador) {
                activoJugador = cambio;
            } else {
                activoCPU = cambio;
            }

            registrar(quien + " cambio a "+ equipo.obtenerPokemon(cambio).getNombre());
            return;
        }

        Pokemon defensor = esJugador? getPokemonCPU() : getPokemonJugador();

        if (defensor.estaDebilitado()) {
            return;
        }

        switch (accion) {
            case ATACAR:
                registrar(atacante.getNombre()+ " uso ataque basico.");
                realizarAtaque(ataque, atacante, defensor);
                break;

            case HABILIDAD:
                registrar(atacante.getNombre()+ " uso su habilidad especial.");
                realizarAtaque(habilidad, atacante, defensor);
                break;

            case RECUPERAR:
                int antes = atacante.getPsActuales();
                recuperar.ejecutar(atacante, defensor);
                int recuperados = atacante.getPsActuales()- antes;
                registrar(atacante.getNombre()+ " recupero " + recuperados + " PS.");
                break;
                
            default:
                break;
        }
    }

    private void realizarAtaque(Movimiento movimiento,Pokemon atacante, Pokemon defensor) {
        int antes = defensor.getPsActuales();
        movimiento.ejecutar(atacante, defensor);
        int danio = antes - defensor.getPsActuales();
        registrar(defensor.getNombre()+ " perdio " + danio + " PS.");

        double multiplicador = atacante.getTipo().obtenerMultiplicador(defensor.getTipo());
        if (multiplicador == 2.0) {
            registrar("Es muy eficaz.");
        } else if (multiplicador == 0.5) {
            registrar("No es muy eficaz.");
        }
        if (defensor.estaDebilitado()) {
            registrar(defensor.getNombre()+ " fue debilitado.");
        }
    }

    private void finalizarRonda() {
        if (batallaTerminada()) {
            comprobarGanador();
            return;
        }
        if (getPokemonCPU().estaDebilitado()) {
            activoCPU = buscarDisponible(equipoCPU, 0);
            registrar("La CPU envio a "+ getPokemonCPU().getNombre() + ".");
        }
        if (ronda >= MAX_RONDAS) {
            comprobarGanador();
            return;
        }

        ronda++;

        if (getPokemonJugador().estaDebilitado()) {
            estado = EstadoBatalla.ELEGIR_REEMPLAZO;
            registrar("Debes elegir otro Pokemon.");
        } else {
            estado = EstadoBatalla.ESPERANDO_ACCION;
        }
        registrar("Ronda " + ronda);
    }

    public void elegirReemplazo(int posicion) {
        if (estado != EstadoBatalla.ELEGIR_REEMPLAZO) {
            throw new IllegalStateException("No necesitas cambiar Pokemon.");
        }

        if (posicion < 0|| posicion >= equipoJugador.getCantidad()|| posicion == activoJugador|| equipoJugador.obtenerPokemon(posicion).estaDebilitado()) {

            throw new IllegalArgumentException("El Pokemon no esta disponible.");
        }

        activoJugador = posicion;
        registrar("El jugador envio a "+ getPokemonJugador().getNombre() + ".");
        estado = EstadoBatalla.ESPERANDO_ACCION;
    }

    private int buscarDisponible(Equipo equipo, int posicion) {
        if (posicion >= equipo.getCantidad()) {
            return -1;
        }

        if (!equipo.obtenerPokemon(posicion).estaDebilitado()) {
            return posicion;
        }
        return buscarDisponible(equipo, posicion + 1);
    }

    private boolean batallaTerminada() {

        return buscarDisponible(equipoJugador, 0)==-1|| buscarDisponible(equipoCPU, 0)==-1;
    }

    private int sumarPS(Equipo equipo, int posicion) {

        if (posicion >= equipo.getCantidad()) {
            return 0;
        }

        return equipo.obtenerPokemon(posicion).getPsActuales()+ sumarPS(equipo, posicion + 1);
    }

    private void comprobarGanador() {

        int psJugador = sumarPS(equipoJugador, 0);
        int psCPU = sumarPS(equipoCPU, 0);
        if (psJugador > psCPU) {
            estado = EstadoBatalla.VICTORIA;
            registrar("Ganaste la batalla.");
        } else if (psCPU > psJugador) {
            estado = EstadoBatalla.DERROTA;
            registrar("La CPU gano la batalla.");
        } else {
            estado = EstadoBatalla.EMPATE;
            registrar("La batalla termino en empate.");
        }
    }

    private void registrar(String mensaje) {
        historial.add(mensaje);
    }

    public Pokemon getPokemonJugador() {
        return equipoJugador.obtenerPokemon(activoJugador);
    }

    public Pokemon getPokemonCPU() {
        return equipoCPU.obtenerPokemon(activoCPU);
    }

    public Equipo getEquipoJugador() {
        return equipoJugador;
    }

    public Equipo getEquipoCPU() {
        return equipoCPU;
    }

    public int getActivoJugador() {
        return activoJugador;
    }

    public int getActivoCPU() {
        return activoCPU;
    }

    public int getRonda() {
        return ronda;
    }

    public EstadoBatalla getEstado() {
        return estado;
    }

    public ArrayList<String> getHistorial() {
        return new ArrayList<>(historial);
    }

    public int getPSJugador() {
        return sumarPS(equipoJugador, 0);
    }

    public int getPSCPU() {
        return sumarPS(equipoCPU, 0);
    }
}
