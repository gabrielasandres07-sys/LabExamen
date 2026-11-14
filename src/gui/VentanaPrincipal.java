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
import modelo.Catalogo;
import modelo.Entrenador;
import modelo.GestorMemoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    public static final String INICIO    = "INICIO";
    public static final String EQUIPO    = "EQUIPO";
    public static final String BATALLA   = "BATALLA";
    public static final String RESULTADO = "RESULTADO";

    private final CardLayout cards = new CardLayout();
    private final JPanel contenedor = new JPanel(cards);

    private final Catalogo catalogo = new Catalogo();
    private final GestorMemoria memoria = new GestorMemoria();
    private Entrenador entrenador;

    private final PanelEquipo panelEquipo;
    private final PanelBatalla panelBatalla;
    private final PanelResultado panelResultado;

    public VentanaPrincipal() {
        super("Batallas Pokémon");

        panelEquipo = new PanelEquipo(this);
        panelBatalla = new PanelBatalla(this);
        panelResultado = new PanelResultado(this);

        contenedor.add(new PanelInicio(this), INICIO);
        contenedor.add(panelEquipo, EQUIPO);
        contenedor.add(panelBatalla, BATALLA);
        contenedor.add(panelResultado, RESULTADO);

        add(contenedor);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });

        setSize(1000, 680);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        mostrar(INICIO);
    }

    public void mostrar(String pantalla) {
        if (EQUIPO.equals(pantalla)) {
            panelEquipo.actualizar();
        }
        cards.show(contenedor, pantalla);
    }

    public void comenzarBatalla() {
        Batalla batalla = new Batalla(entrenador.getEquipo());
        panelBatalla.iniciar(entrenador, batalla);
        mostrar(BATALLA);
    }

    public void mostrarResultado(Batalla batalla) {
        panelResultado.mostrar(entrenador, batalla);
        mostrar(RESULTADO);
    }

    public void salir() {
        int r = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres salir del juego?",
                "Salir", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public GestorMemoria getMemoria() {
        return memoria;
    }
}
