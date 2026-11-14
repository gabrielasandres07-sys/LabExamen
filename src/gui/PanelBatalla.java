/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author USER
 */


import logica.Accion;
import logica.Batalla;
import logica.EstadoBatalla;
import modelo.Entrenador;
import modelo.Equipo;
import modelo.Pokemon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelBatalla extends JPanel {

    private static final Color FONDO = new Color(236, 241, 250);
    private static final Color AZUL = new Color(30, 60, 114);

    private final VentanaPrincipal ventana;
    private Entrenador entrenador;
    private Batalla batalla;
    private int mensajesMostrados;

    private final JLabel lblRonda = new JLabel();
    private final JLabel lblEstado = new JLabel();
    private final LadoPokemon ladoJugador = new LadoPokemon();
    private final LadoPokemon ladoRival = new LadoPokemon();
    private final JTextArea areaLog = new JTextArea(7, 40);

    private final JButton btnAtacar = new JButton("Atacar");
    private final JButton btnHabilidad = new JButton("Habilidad especial");
    private final JButton btnRecuperar = new JButton("Recuperar");
    private final JButton btnCambiar = new JButton("Cambiar Pokémon");

    public PanelBatalla(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(12, 15, 12, 15));
        setBackground(FONDO);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCampo(), BorderLayout.CENTER);
        add(crearZonaInferior(), BorderLayout.SOUTH);

        btnAtacar.addActionListener(e -> accion(Accion.ATACAR));
        btnHabilidad.addActionListener(e -> accion(Accion.HABILIDAD));
        btnRecuperar.addActionListener(e -> accion(Accion.RECUPERAR));
        btnCambiar.addActionListener(e -> accion(Accion.CAMBIAR));
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        lblRonda.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblRonda.setForeground(AZUL);
        lblEstado.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblEstado.setForeground(AZUL);
        panel.add(lblRonda, BorderLayout.WEST);
        panel.add(lblEstado, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearCampo() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        panel.add(ladoJugador);
        panel.add(ladoRival);
        return panel;
    }

    private JPanel crearZonaInferior() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createTitledBorder("Mensajes de la batalla"));

        JPanel botones = new JPanel(new GridLayout(1, 4, 10, 0));
        botones.setOpaque(false);
        for (JButton b : new JButton[]{btnAtacar, btnHabilidad, btnRecuperar, btnCambiar}) {
            b.setFont(b.getFont().deriveFont(Font.BOLD, 14f));
            b.setPreferredSize(new Dimension(150, 40));
            botones.add(b);
        }

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    public void iniciar(Entrenador entrenador, Batalla batalla) {
        this.entrenador = entrenador;
        this.batalla = batalla;
        this.mensajesMostrados = 0;
        areaLog.setText("");
        log(entrenador.getNombre() + " vs Rival CPU");
        log(entrenador.getNombre() + " envía a " + batalla.getPokemonJugador().getNombre() + ".");
        log("Rival CPU envía a " + batalla.getPokemonCPU().getNombre() + ".");
        mostrarMensajesNuevos();
        refrescar();
    }

    private void accion(Accion accion) {
        if (batalla.getEstado() != EstadoBatalla.ESPERANDO_ACCION) {
            return;
        }

        int indiceCambio = -1;
        if (accion == Accion.CAMBIAR) {
            indiceCambio = elegirPokemon("¿A qué Pokémon quieres cambiar?", true);
            if (indiceCambio < 0) {
                return;
            }
        }

        habilitarAcciones(false);
        lblEstado.setText("Resolviendo ronda...");
        try {
            batalla.jugarRonda(accion, indiceCambio);
        } catch (RuntimeException ex) {
            aviso(ex.getMessage());
        }
        mostrarMensajesNuevos();
        despuesDeRonda();
    }

    private void despuesDeRonda() {
        refrescar();
        EstadoBatalla estado = batalla.getEstado();

        if (estado == EstadoBatalla.ELEGIR_REEMPLAZO) {
            SwingUtilities.invokeLater(this::pedirReemplazo);
        } else if (terminada(estado)) {
            habilitarAcciones(false);
            Timer timer = new Timer(1800, e -> ventana.mostrarResultado(batalla));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void pedirReemplazo() {
        while (batalla.getEstado() == EstadoBatalla.ELEGIR_REEMPLAZO && !opcionesDeCambio().isEmpty()) {
            int indice = elegirPokemon("¡Tu Pokémon se debilitó! Elige al siguiente:", false);
            if (indice < 0) {
                continue;
            }
            try {
                batalla.elegirReemplazo(indice);
            } catch (RuntimeException ex) {
                aviso(ex.getMessage());
            }
        }
        mostrarMensajesNuevos();
        despuesDeRonda();
    }

    private int elegirPokemon(String mensaje, boolean cancelable) {
        List<Opcion> opciones = opcionesDeCambio();
        if (opciones.isEmpty()) {
            if (cancelable) {
                aviso("No tienes otros Pokémon disponibles.");
            }
            return -1;
        }

        JComboBox<Opcion> combo = new JComboBox<>(opciones.toArray(new Opcion[0]));
        Object[] contenido = {mensaje, combo};
        int r = JOptionPane.showOptionDialog(this, contenido, "Elegir Pokémon",
                cancelable ? JOptionPane.OK_CANCEL_OPTION : JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (r != JOptionPane.OK_OPTION) {
            return -1;
        }
        return ((Opcion) combo.getSelectedItem()).indice;
    }

    private List<Opcion> opcionesDeCambio() {
        List<Opcion> opciones = new ArrayList<>();
        Equipo equipo = batalla.getEquipoJugador();
        Pokemon activo = batalla.getPokemonJugador();
        for (int i = 0; i < equipo.getCantidad(); i++) {
            Pokemon p = equipo.obtenerPokemon(i);
            if (p != activo && !p.estaDebilitado()) {
                opciones.add(new Opcion(i, p.getNombre() + "  (PS " + p.getPsActuales()
                        + "/" + p.getPsMaximos() + ")"));
            }
        }
        return opciones;
    }

    private void refrescar() {
        lblRonda.setText("Ronda " + batalla.getRonda());
        lblEstado.setText(textoEstado(batalla.getEstado()));
        ladoJugador.mostrar(entrenador.getNombre(), batalla.getPokemonJugador(), batalla.getEquipoJugador());
        ladoRival.mostrar("Rival CPU", batalla.getPokemonCPU(), batalla.getEquipoCPU());
        habilitarAcciones(batalla.getEstado() == EstadoBatalla.ESPERANDO_ACCION);
    }

    private void habilitarAcciones(boolean turno) {
        Pokemon p = batalla.getPokemonJugador();
        boolean vivo = p != null && !p.estaDebilitado();

        btnAtacar.setEnabled(turno && vivo);
        btnHabilidad.setEnabled(turno && vivo && p.getUsosHabilidad() > 0);
        btnRecuperar.setEnabled(turno && vivo && p.getUsosRecuperacion() > 0
                && p.getPsActuales() < p.getPsMaximos());
        btnCambiar.setEnabled(turno && !opcionesDeCambio().isEmpty());

        if (p != null) {
            btnHabilidad.setText("Habilidad (" + p.getUsosHabilidad() + ")");
            btnRecuperar.setText("Recuperar (" + p.getUsosRecuperacion() + ")");
        }
    }

    private String textoEstado(EstadoBatalla estado) {
        switch (estado) {
            case ESPERANDO_ACCION:
                return "Turno de " + entrenador.getNombre();
            case RESOLVIENDO_RONDA:
                return "Resolviendo ronda...";
            case ELEGIR_REEMPLAZO:
                return "Elige un reemplazo";
            case VICTORIA:
                return "¡Victoria!";
            case DERROTA:
                return "Derrota";
            case EMPATE:
                return "Empate";
            default:
                return "Preparando...";
        }
    }

    private boolean terminada(EstadoBatalla estado) {
        return estado == EstadoBatalla.VICTORIA
                || estado == EstadoBatalla.DERROTA
                || estado == EstadoBatalla.EMPATE;
    }

    private void mostrarMensajesNuevos() {
        List<String> historial = batalla.getHistorial();
        for (int i = mensajesMostrados; i < historial.size(); i++) {
            String m = historial.get(i);
            log(m.startsWith("Ronda ") ? "\n── " + m + " ──" : m);
        }
        mensajesMostrados = historial.size();
    }

    private void log(String texto) {
        areaLog.append(texto + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private static class Opcion {
        final int indice;
        final String texto;

        Opcion(int indice, String texto) {
            this.indice = indice;
            this.texto = texto;
        }

        @Override
        public String toString() {
            return texto;
        }
    }

    private static class LadoPokemon extends JPanel {

        private final JLabel lblEntrenador = new JLabel(" ", SwingConstants.CENTER);
        private final JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
        private final JLabel lblNombre = new JLabel(" ", SwingConstants.CENTER);
        private final JProgressBar barraPs = new JProgressBar();
        private final JLabel lblUsos = new JLabel(" ", SwingConstants.CENTER);
        private final JLabel lblEquipo = new JLabel(" ", SwingConstants.CENTER);

        LadoPokemon() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AZUL, 2, true),
                    new EmptyBorder(8, 12, 8, 12)));

            lblEntrenador.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblEntrenador.setForeground(AZUL);
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblUsos.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblEquipo.setFont(new Font("SansSerif", Font.PLAIN, 16));

            barraPs.setStringPainted(true);
            barraPs.setFont(new Font("SansSerif", Font.BOLD, 13));
            barraPs.setMaximumSize(new Dimension(260, 22));

            for (JComponent c : new JComponent[]{lblEntrenador, lblImagen, lblNombre, barraPs, lblUsos, lblEquipo}) {
                c.setAlignmentX(CENTER_ALIGNMENT);
                add(c);
                add(Box.createVerticalStrut(4));
            }
        }

        void mostrar(String nombreEntrenador, Pokemon p, Equipo equipo) {
            lblEntrenador.setText(nombreEntrenador);
            if (p == null) {
                return;
            }
            lblImagen.setIcon(Imagenes.pokemon(p.getNombre(), p.getTipo(), 130));
            lblImagen.setEnabled(!p.estaDebilitado());
            lblNombre.setText(p.getNombre() + "   Nv. " + p.getNivel());

            barraPs.setMaximum(p.getPsMaximos());
            barraPs.setValue(p.getPsActuales());
            barraPs.setString("PS " + p.getPsActuales() + " / " + p.getPsMaximos());
            double proporcion = (double) p.getPsActuales() / p.getPsMaximos();
            if (proporcion > 0.5) {
                barraPs.setForeground(new Color(60, 170, 70));
            } else if (proporcion > 0.2) {
                barraPs.setForeground(new Color(230, 170, 20));
            } else {
                barraPs.setForeground(new Color(210, 50, 40));
            }

            lblUsos.setText("Habilidad: " + p.getUsosHabilidad() + "/3   ·   Recuperar: "
                    + p.getUsosRecuperacion() + "/2");

            StringBuilder bolitas = new StringBuilder("Equipo: ");
            for (Pokemon miembro : equipo.getIntegrantes()) {
                bolitas.append(miembro.estaDebilitado() ? "○ " : "● ");
            }
            lblEquipo.setText(bolitas.toString().trim());
        }
    }
}