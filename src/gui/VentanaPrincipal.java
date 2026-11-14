/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author USER
 */


import modelo.Entrenador;
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

    private Entrenador entrenador;

    public VentanaPrincipal() {
        super("Batallas Pokémon");

        contenedor.add(new PanelInicio(this), INICIO);
        contenedor.add(crearTemporal("Pantalla de EQUIPO"), EQUIPO);
        contenedor.add(crearTemporal("Pantalla de BATALLA"), BATALLA);
        contenedor.add(crearTemporal("Pantalla de RESULTADO"), RESULTADO);

        add(contenedor);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });

        setSize(900, 600);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        mostrar(INICIO);
    }

    public void mostrar(String pantalla) {
        cards.show(contenedor, pantalla);
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

    private JPanel crearTemporal(String texto) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        p.add(lbl, BorderLayout.CENTER);

        JButton siguiente = new JButton("Siguiente pantalla →");
        siguiente.addActionListener(e -> cards.next(contenedor));
        p.add(siguiente, BorderLayout.SOUTH);
        return p;
    }


}