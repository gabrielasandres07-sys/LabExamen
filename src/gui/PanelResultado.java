/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author USER
 */


import logica.Batalla;
import logica.EstadoBatalla;
import modelo.Entrenador;
import modelo.Equipo;
import modelo.Pokemon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelResultado extends JPanel {

    private static final Color FONDO = new Color(30, 60, 114);
    private static final Color AMARILLO = new Color(255, 203, 5);

    private final JLabel lblTitulo = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblSubtitulo = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblResumen = new JLabel(" ", SwingConstants.CENTER);
    private final JPanel filaJugador = crearFila();
    private final JPanel filaRival = crearFila();
    private final JLabel lblJugador = crearEtiquetaFila();
    private final JLabel lblRival = crearEtiquetaFila();

    public PanelResultado(VentanaPrincipal ventana) {
        setLayout(new GridBagLayout());
        setBackground(FONDO);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblSubtitulo.setForeground(Color.WHITE);
        lblResumen.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblResumen.setForeground(Color.WHITE);

        JButton btnNueva = new JButton("Nueva partida");
        JButton btnSalir = new JButton("Salir");
        btnNueva.setFont(btnNueva.getFont().deriveFont(Font.BOLD, 15f));
        btnSalir.setFont(btnSalir.getFont().deriveFont(Font.BOLD, 15f));
        btnNueva.addActionListener(e -> ventana.mostrar(VentanaPrincipal.EQUIPO));
        btnSalir.addActionListener(e -> ventana.salir());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botones.setOpaque(false);
        botones.add(btnNueva);
        botones.add(btnSalir);

        JComponent[] componentes = {lblTitulo, lblSubtitulo, lblResumen,
                lblJugador, filaJugador, lblRival, filaRival, botones};
        int[] espacios = {4, 20, 20, 4, 14, 4, 25, 0};
        for (int i = 0; i < componentes.length; i++) {
            componentes[i].setAlignmentX(CENTER_ALIGNMENT);
            centro.add(componentes[i]);
            centro.add(Box.createVerticalStrut(espacios[i]));
        }

        add(centro);
    }

    private static JPanel crearFila() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        fila.setOpaque(false);
        return fila;
    }

    private static JLabel crearEtiquetaFila() {
        JLabel lbl = new JLabel(" ", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(AMARILLO);
        return lbl;
    }

    public void mostrar(Entrenador entrenador, Batalla batalla) {
        String nombre = entrenador.getNombre();
        EstadoBatalla estado = batalla.getEstado();

        if (estado == EstadoBatalla.VICTORIA) {
            lblTitulo.setText("¡Victoria, " + nombre + "!");
            lblTitulo.setForeground(new Color(120, 230, 120));
            lblSubtitulo.setText("Derrotaste al equipo del Rival CPU.");
        } else if (estado == EstadoBatalla.DERROTA) {
            lblTitulo.setText("Derrota, " + nombre);
            lblTitulo.setForeground(new Color(255, 120, 110));
            lblSubtitulo.setText("El Rival CPU ganó esta vez. ¡Inténtalo de nuevo!");
        } else {
            lblTitulo.setText("¡Empate!");
            lblTitulo.setForeground(AMARILLO);
            lblSubtitulo.setText("Se alcanzó el límite de rondas con los mismos PS, " + nombre + ".");
        }

        Equipo jugador = batalla.getEquipoJugador();
        Equipo rival = batalla.getEquipoCPU();

        lblResumen.setText("<html><div style='text-align:center'>"
                + "Rondas jugadas: <b>" + batalla.getRonda() + "</b><br>"
                + "Pokémon debilitados — " + nombre + ": <b>" + debilitados(jugador) + "</b>"
                + "  |  Rival CPU: <b>" + debilitados(rival) + "</b><br>"
                + "Quedaron en pie — " + nombre + ": <b>" + enPie(jugador) + "</b><br>"
                + "Rival CPU: <b>" + enPie(rival) + "</b>"
                + "</div></html>");

        lblJugador.setText("Equipo de " + nombre);
        lblRival.setText("Equipo del Rival CPU");
        llenarFila(filaJugador, jugador);
        llenarFila(filaRival, rival);
        revalidate();
        repaint();
    }

    private void llenarFila(JPanel fila, Equipo equipo) {
        fila.removeAll();
        for (Pokemon p : equipo.getIntegrantes()) {
            ImageIcon icono = Imagenes.pokemon(p.getNombre(), p.getTipo(), 56);
            JLabel lbl = new JLabel(icono);
            if (p.estaDebilitado()) {
                lbl.setIcon(new ImageIcon(GrayFilter.createDisabledImage(icono.getImage())));
            }
            lbl.setToolTipText(p.getNombre() + " — PS " + p.getPsActuales() + "/" + p.getPsMaximos());
            fila.add(lbl);
        }
    }

    private int debilitados(Equipo equipo) {
        int total = 0;
        for (Pokemon p : equipo.getIntegrantes()) {
            if (p.estaDebilitado()) {
                total++;
            }
        }
        return total;
    }

    private String enPie(Equipo equipo) {
        StringBuilder sb = new StringBuilder();
        for (Pokemon p : equipo.getIntegrantes()) {
            if (!p.estaDebilitado()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(p.getNombre()).append(" (").append(p.getPsActuales()).append(" PS)");
            }
        }
        return sb.length() == 0 ? "ninguno" : sb.toString();
    }
}
