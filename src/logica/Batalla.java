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
import modelo.Pokemon;

public class Batalla {
    private Pokemon[] equipoJugador;
    private Pokemon[] equipoCPU;
    private int activoJugador;
    private int activoCPU;
    private int ronda;
    private EstadoBatalla estado;
    private Movimiento ataqueBasico;
    private Movimiento habilidadEspecial;
    private Movimiento recuperar;
    private Random random;
    private List<String> historial;
    private static final int MAX_RONDAS = 50;
    
    public Batalla(Pokemon[] jugador, Pokemon[] cpu) {
        validarEquipo(jugador);
        validarEquipo(cpu);
        equipoJugador=copiarEquipo(jugador);
        equipoCPU=copiarEquipo(cpu);
        activoJugador=0;
        activoCPU=0;
        ronda=1;

        estado=EstadoBatalla.ESPERANDO_ACCION;
        ataqueBasico=new AtaqueBasico();
        habilidadEspecial=new HabilidadEspecial();
        recuperar=new Recuperar();
        random=new Random();
        historial=new ArrayList<>();
        agregarMensaje("Comienza la batalla.");
        agregarMensaje("Ronda 1");
    }

    private void validarEquipo(Pokemon[] equipo) {
        if (equipo == null || equipo.length < 1|| equipo.length > 6) {
            throw new IllegalArgumentException("El equipo debe tener entre 1 y 6 Pokemon.");
        }

        for (Pokemon pokemon : equipo) {
            if (pokemon == null || pokemon.estaDebilitado()) {
                throw new IllegalArgumentException("El equipo contiene un Pokemon invalido.");
            }
        }
    }

    private Pokemon[] copiarEquipo(Pokemon[] original) {
        Pokemon[] copia = new Pokemon[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i].clone();
            if (copia[i] == null) {
                throw new IllegalStateException("No se pudo copiar el Pokemon.");
            }
        }

        return copia;
    }
    public void jugarRonda(Accion accion, int posicionCambio) {
        if (estado != EstadoBatalla.ESPERANDO_ACCION) {
            throw new IllegalStateException("No se puede realizar una accion ahora.");
        }

        if (!accionValida(equipoJugador, activoJugador,accion, posicionCambio)) {
            throw new IllegalArgumentException("La accion seleccionada no es valida.");
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
            ejecutarAccion(true, accion, posicionCambio);

            if (!batallaTerminada()) {
                ejecutarAccion(false, accionCPU, cambioCPU);
            }
        } else {
            ejecutarAccion(false, accionCPU, cambioCPU);

            if (!batallaTerminada()) {
                ejecutarAccion(true, accion, posicionCambio);
            }
        }

        finalizarRonda();
    }

    private boolean accionValida(Pokemon[] equipo,
            int activo, Accion accion, int cambio) {
        if (accion == null) {
            return false;
        }
        Pokemon pokemon = equipo[activo];
        if (pokemon.estaDebilitado()) {
            return false;
        }
        switch (accion) {
            case ATACAR:
                return true;
            case HABILIDAD:
                return pokemon.getUsosHabilidad() > 0;
            case RECUPERAR:
                return pokemon.getUsosRecuperacion()>0&&pokemon.getPsActuales()<pokemon.getPsMaximos();
            case CAMBIAR:
                return cambio>=0&&cambio<equipo.length&& cambio!=activo&& !equipo[cambio].estaDebilitado();

            default:
                return false;
        }
    }

    private Accion elegirAccionCPU() {
        List<Accion> opciones = new ArrayList<>();
        for (Accion accion : Accion.values()) {
            if (accion == Accion.CAMBIAR) {
                if (elegirCambioCPU() != -1) {
                    opciones.add(accion);
                }
            } else if (accionValida(equipoCPU, activoCPU,
                    accion, -1)) {
                opciones.add(accion);
            }
        }

        return opciones.get(random.nextInt(opciones.size()));
    }

    private int elegirCambioCPU() {
        List<Integer> disponibles = new ArrayList<>();
        for (int i = 0; i < equipoCPU.length; i++) {
            if (i != activoCPU&& !equipoCPU[i].estaDebilitado()) {
                disponibles.add(i);
            }
        }

        if (disponibles.isEmpty()) {
            return -1;
        }

        return disponibles.get(random.nextInt(disponibles.size()));
    }

    private void ejecutarAccion(boolean esJugador,
            Accion accion, int cambio) {
        Pokemon[] equipo = esJugador? equipoJugador : equipoCPU;
        int activo = esJugador? activoJugador : activoCPU;
        Pokemon atacante = equipo[activo];

        if (atacante.estaDebilitado()) {
            agregarMensaje(atacante.getNombre()+ " esta debilitado y pierde su accion.");
            return;
        }

        Pokemon defensor = esJugador? getPokemonCPU() : getPokemonJugador();
        String nombre = atacante.getNombre();

        switch (accion) {

            case ATACAR:
                realizarAtaque(ataqueBasico,atacante,defensor);
                agregarMensaje(nombre + " uso ataque basico.");
                break;

            case HABILIDAD:
                realizarAtaque(habilidadEspecial,atacante,defensor);
                agregarMensaje(nombre+ " uso su habilidad especial.");
                break;

            case RECUPERAR:
                int antes = atacante.getPsActuales();
                recuperar.ejecutar(atacante, defensor);
                int recuperados = atacante.getPsActuales()-antes;
                agregarMensaje(nombre + " recupero "+ recuperados + " PS.");
                break;

            case CAMBIAR:
                if (esJugador) {
                    activoJugador = cambio;
                } else {
                    activoCPU = cambio;
                }

                agregarMensaje(nombre + " fue cambiado por "+ equipo[cambio].getNombre() + ".");
                break;
        }
    }

    private void realizarAtaque(Movimiento movimiento,
            Pokemon atacante, Pokemon defensor) {
        int psAntes = defensor.getPsActuales();
        movimiento.ejecutar(atacante, defensor);
        int danio = psAntes - defensor.getPsActuales();
        agregarMensaje(defensor.getNombre()+ " perdio " + danio + " PS.");
        double multiplicador = atacante.getTipo().obtenerMultiplicador(defensor.getTipo());

        if (multiplicador == 2.0) {
            agregarMensaje("¡Es muy eficaz!");
        } else if (multiplicador == 0.5) {
            agregarMensaje("No es muy eficaz...");
        }

        if (defensor.estaDebilitado()) {
            agregarMensaje(defensor.getNombre()+ " ha sido debilitado.");
        }
    }

    private void finalizarRonda() {
        if (batallaTerminada()) {
            comprobarGanador();
            return;
        }
        if (getPokemonCPU().estaDebilitado()) {
            int reemplazo = buscarDisponible(equipoCPU, 0);
            if (reemplazo != -1) {
                activoCPU = reemplazo;agregarMensaje("La CPU envio a "+ getPokemonCPU().getNombre() + ".");
            }
        }

        if (ronda >= MAX_RONDAS) {
            comprobarGanador();
            return;
        }

        ronda++;

        if (getPokemonJugador().estaDebilitado()) {
            estado = EstadoBatalla.ELEGIR_REEMPLAZO;
            agregarMensaje("Selecciona otro Pokemon.");
        } else {
            estado = EstadoBatalla.ESPERANDO_ACCION;
        }

        agregarMensaje("Ronda " + ronda);
    }

    public void elegirReemplazo(int posicion) {
        if (estado != EstadoBatalla.ELEGIR_REEMPLAZO) {
            throw new IllegalStateException("No necesitas elegir un reemplazo.");
        }

        if (posicion < 0|| posicion >= equipoJugador.length|| posicion == activoJugador|| equipoJugador[posicion].estaDebilitado()) {
            throw new IllegalArgumentException("Selecciona un Pokemon disponible.");
        }

        activoJugador = posicion;
        agregarMensaje("Entra al combate "+ getPokemonJugador().getNombre() + ".");
        estado=EstadoBatalla.ESPERANDO_ACCION;
    }

    private int buscarDisponible(Pokemon[] equipo,
            int posicion) {
        if (posicion >= equipo.length) {
            return -1;
        }
        if (!equipo[posicion].estaDebilitado()) {
            return posicion;
        }
        return buscarDisponible(equipo, posicion + 1);
    }

    private boolean batallaTerminada() {
        return buscarDisponible(equipoJugador, 0) == -1|| buscarDisponible(equipoCPU, 0) == -1;
    }

    private int sumarPS(Pokemon[] equipo, int posicion) {
        if (posicion >= equipo.length) {
            return 0;
        }
        return equipo[posicion].getPsActuales()+ sumarPS(equipo, posicion + 1);
    }

    private void comprobarGanador() {
        int psJugador = sumarPS(equipoJugador, 0);
        int psCPU = sumarPS(equipoCPU, 0);

        if (psJugador > psCPU) {
            estado = EstadoBatalla.VICTORIA;
            agregarMensaje("¡Ganaste la batalla!");

        } else if (psCPU > psJugador) {
            estado = EstadoBatalla.DERROTA;
            agregarMensaje("La CPU gano la batalla.");

        } else {
            estado = EstadoBatalla.EMPATE;
            agregarMensaje("La batalla termino en empate.");
        }
    }

    private void agregarMensaje(String mensaje) {
        historial.add(mensaje);
    }

    public Pokemon getPokemonJugador() {
        return equipoJugador[activoJugador];
    }

    public Pokemon getPokemonCPU() {
        return equipoCPU[activoCPU];
    }

    public Pokemon[] getEquipoJugador() {
        return equipoJugador.clone();
    }

    public Pokemon[] getEquipoCPU() {
        return equipoCPU.clone();
    }

    public int getRonda() {
        return ronda;
    }

    public EstadoBatalla getEstado() {
        return estado;
    }

    public List<String> getHistorial() {
        return new ArrayList<>(historial);
    }

    public int getPSJugador() {
        return sumarPS(equipoJugador, 0);
    }

    public int getPSCPU() {
        return sumarPS(equipoCPU, 0);
    }
}
