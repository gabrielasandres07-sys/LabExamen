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
import modelo.Equipo;
import modelo.Pokemon;
import modelo.PokemonAgua;
import modelo.PokemonElectrico;
import modelo.PokemonFuego;
import modelo.PokemonPlanta;
import modelo.TipoPokemon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelEquipo extends JPanel {

    private static final Color FONDO = new Color(236, 241, 250);
    private static final Color AZUL = new Color(30, 60, 114);
    private static final int MAXIMO_EQUIPO = 6;
    private static final int MAXIMO_COPIAS = 2;

    private final VentanaPrincipal ventana;
    private final List<Pokemon> personalizados = new ArrayList<>();

    private final DefaultListModel<Pokemon> modeloCatalogo = new DefaultListModel<>();
    private final DefaultListModel<Pokemon> modeloEquipo = new DefaultListModel<>();
    private final JList<Pokemon> listaCatalogo = new JList<>(modeloCatalogo);
    private final JList<Pokemon> listaEquipo = new JList<>(modeloEquipo);

    private final JLabel lblTitulo = new JLabel();
    private final JLabel lblContador = new JLabel();
    private final JLabel lblImagen = new JLabel();
    private final JLabel lblNombreFicha = new JLabel("Selecciona un Pokémon");
    private final JLabel lblDatosFicha = new JLabel();

    private final JButton btnAgregar = new JButton("Agregar →");
    private final JButton btnQuitar = new JButton("← Quitar");
    private final JButton btnCrear = new JButton("Crear Pokémon");
    private final JButton btnGuardar = new JButton("Guardar equipo");
    private final JButton btnRestaurar = new JButton("Restaurar equipo");
    private final JButton btnComenzar = new JButton("Comenzar batalla");
    private final JButton btnVolver = new JButton("← Volver al inicio");

    public PanelEquipo(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(FONDO);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearBotonesInferiores(), BorderLayout.SOUTH);

        listaCatalogo.setCellRenderer(new CeldaPokemon(false));
        listaEquipo.setCellRenderer(new CeldaPokemon(true));
        listaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEquipo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaCatalogo.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaCatalogo.getSelectedValue() != null) {
                listaEquipo.clearSelection();
                mostrarFicha(listaCatalogo.getSelectedValue());
            }
        });
        listaEquipo.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listaEquipo.getSelectedValue() != null) {
                listaCatalogo.clearSelection();
                mostrarFicha(listaEquipo.getSelectedValue());
            }
        });

        btnAgregar.addActionListener(e -> agregar());
        btnQuitar.addActionListener(e -> quitar());
        btnCrear.addActionListener(e -> crearPokemon());
        btnGuardar.addActionListener(e -> guardar());
        btnRestaurar.addActionListener(e -> restaurar());
        btnComenzar.addActionListener(e -> comenzar());
        btnVolver.addActionListener(e -> ventana.mostrar(VentanaPrincipal.INICIO));
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(AZUL);
        lblContador.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblContador.setForeground(AZUL);
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(lblContador, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new GridLayout(1, 3, 12, 0));
        centro.setOpaque(false);
        centro.add(crearColumnaLista("Catálogo disponible", listaCatalogo, btnAgregar, btnCrear));
        centro.add(crearFicha());
        centro.add(crearColumnaLista("Mi equipo", listaEquipo, btnQuitar, null));
        return centro;
    }

    private JPanel crearColumnaLista(String titulo, JList<Pokemon> lista, JButton boton1, JButton boton2) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.setBorder(crearBorde(titulo));
        panel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, boton2 == null ? 1 : 2, 8, 0));
        botones.setOpaque(false);
        botones.add(boton1);
        if (boton2 != null) {
            botones.add(boton2);
        }
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFicha() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(crearBorde("Ficha"));

        lblImagen.setAlignmentX(CENTER_ALIGNMENT);
        lblImagen.setPreferredSize(new Dimension(150, 150));
        lblNombreFicha.setAlignmentX(CENTER_ALIGNMENT);
        lblNombreFicha.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblDatosFicha.setAlignmentX(CENTER_ALIGNMENT);
        lblDatosFicha.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(Box.createVerticalStrut(8));
        panel.add(lblImagen);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblNombreFicha);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblDatosFicha);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearBotonesInferiores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        derecha.add(btnGuardar);
        derecha.add(btnRestaurar);
        derecha.add(btnComenzar);

        btnComenzar.setFont(btnComenzar.getFont().deriveFont(Font.BOLD, 14f));

        panel.add(btnVolver, BorderLayout.WEST);
        panel.add(derecha, BorderLayout.EAST);
        return panel;
    }

    private TitledBorder crearBorde(String titulo) {
        TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AZUL, 2, true), titulo);
        borde.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
        borde.setTitleColor(AZUL);
        return borde;
    }

    public void actualizar() {
        Entrenador entrenador = ventana.getEntrenador();
        lblTitulo.setText("Arma tu equipo, " + entrenador.getNombre());

        modeloCatalogo.clear();
        for (Pokemon p : ventana.getCatalogo().obtenerCatalogo()) {
            modeloCatalogo.addElement(p);
        }
        for (Pokemon p : personalizados) {
            modeloCatalogo.addElement(p);
        }
        actualizarEquipo();
    }

    private void actualizarEquipo() {
        Equipo equipo = equipo();
        modeloEquipo.clear();
        for (Pokemon p : equipo.getIntegrantes()) {
            modeloEquipo.addElement(p);
        }
        lblContador.setText("Equipo: " + equipo.getCantidad() + "/" + MAXIMO_EQUIPO);
        btnComenzar.setEnabled(!equipo.estaVacio());
        btnRestaurar.setEnabled(ventana.getMemoria().hayCopia());
    }

    private void mostrarFicha(Pokemon p) {
        lblImagen.setIcon(Imagenes.pokemon(p.getNombre(), p.getTipo(), 150));
        lblNombreFicha.setText(p.getNombre());
        lblNombreFicha.setForeground(Imagenes.colorTipo(p.getTipo()).darker());
        lblDatosFicha.setText("<html><table>"
                + fila("Tipo", Imagenes.nombreTipo(p.getTipo()))
                + fila("Nivel", String.valueOf(p.getNivel()))
                + fila("PS", p.getPsActuales() + " / " + p.getPsMaximos())
                + fila("Ataque", String.valueOf(p.getAtaque()))
                + fila("Defensa", String.valueOf(p.getDefensa()))
                + fila("Velocidad", String.valueOf(p.getVelocidad()))
                + fila("Habilidad", Imagenes.descripcionHabilidad(p.getTipo()))
                + "</table></html>");
    }

    private String fila(String etiqueta, String valor) {
        return "<tr><td>" + etiqueta + ":</td><td><b>" + valor + "</b></td></tr>";
    }

    private void agregar() {
        Pokemon seleccionado = listaCatalogo.getSelectedValue();
        if (seleccionado == null) {
            aviso("Selecciona un Pokémon del catálogo para agregarlo.");
            return;
        }
        Equipo equipo = equipo();
        if (equipo.estaLleno()) {
            aviso("El equipo está lleno (máximo " + MAXIMO_EQUIPO + " Pokémon).");
            return;
        }
        if (contarCopias(equipo, seleccionado.getNombre()) >= MAXIMO_COPIAS) {
            aviso("Ya tienes " + MAXIMO_COPIAS + " " + seleccionado.getNombre() + " en tu equipo.");
            return;
        }
        if (!equipo.agregarPokemon(seleccionado)) {
            aviso("No se pudo agregar a " + seleccionado.getNombre() + ".");
            return;
        }
        actualizarEquipo();
    }

    private int contarCopias(Equipo equipo, String nombre) {
        int copias = 0;
        for (Pokemon p : equipo.getIntegrantes()) {
            if (p.getNombre().equals(nombre)) {
                copias++;
            }
        }
        return copias;
    }

    private void quitar() {
        int indice = listaEquipo.getSelectedIndex();
        if (indice < 0) {
            aviso("Selecciona un Pokémon de Mi equipo para quitarlo.");
            return;
        }
        equipo().quitarPokemon(indice);
        actualizarEquipo();
        lblImagen.setIcon(null);
        lblNombreFicha.setText("Selecciona un Pokémon");
        lblDatosFicha.setText("");
    }

    private void crearPokemon() {
        JTextField txtNombre = new JTextField();
        JComboBox<TipoPokemon> cbTipo = new JComboBox<>(TipoPokemon.values());
        cbTipo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(Imagenes.nombreTipo((TipoPokemon) value));
                return this;
            }
        });

        Object[] formulario = {"Nombre:", txtNombre, "Tipo:", cbTipo};
        int r = JOptionPane.showConfirmDialog(this, formulario, "Crear Pokémon",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }

        String nombre = txtNombre.getText().trim();
        if (nombre.length() < 3 || nombre.length() > 12) {
            aviso("El nombre debe tener entre 3 y 12 caracteres.");
            return;
        }
        if (!nombre.matches("[\\p{L} ]+")) {
            aviso("El nombre solo puede contener letras y espacios.");
            return;
        }
        for (int i = 0; i < modeloCatalogo.size(); i++) {
            if (modeloCatalogo.get(i).getNombre().equalsIgnoreCase(nombre)) {
                aviso("Ya existe un Pokémon llamado " + nombre + " en el catálogo.");
                return;
            }
        }

        Pokemon nuevo = crearSegunTipo(nombre, (TipoPokemon) cbTipo.getSelectedItem());
        personalizados.add(nuevo);
        modeloCatalogo.addElement(nuevo);
        listaCatalogo.setSelectedValue(nuevo, true);
    }

    private Pokemon crearSegunTipo(String nombre, TipoPokemon tipo) {
        switch (tipo) {
            case FUEGO:
                return new PokemonFuego(nombre, 90, 13, 9, 10);
            case AGUA:
                return new PokemonAgua(nombre, 95, 12, 10, 9);
            case PLANTA:
                return new PokemonPlanta(nombre, 95, 12, 10, 8);
            default:
                return new PokemonElectrico(nombre, 80, 13, 8, 14);
        }
    }

    private void guardar() {
        if (equipo().estaVacio()) {
            aviso("Agrega al menos un Pokémon antes de guardar el equipo.");
            return;
        }
        try {
            ventana.getMemoria().guardar(ventana.getEntrenador());
            btnRestaurar.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Equipo guardado en memoria.",
                    "Guardar equipo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            aviso("No se pudo guardar el equipo: " + ex.getMessage());
        }
    }

    private void restaurar() {
        try {
            Entrenador guardado = ventana.getMemoria().restaurar();
            ventana.getEntrenador().setEquipo(guardado.getEquipo());
            actualizarEquipo();
            JOptionPane.showMessageDialog(this, "Equipo restaurado.",
                    "Restaurar equipo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            aviso("No se pudo restaurar el equipo: " + ex.getMessage());
        }
    }

    private void comenzar() {
        if (equipo().estaVacio()) {
            aviso("Necesitas al menos un Pokémon para comenzar la batalla.");
            return;
        }
        ventana.comenzarBatalla();
    }

    private Equipo equipo() {
        return ventana.getEntrenador().getEquipo();
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private static class CeldaPokemon extends DefaultListCellRenderer {

        private final boolean numerar;

        CeldaPokemon(boolean numerar) {
            this.numerar = numerar;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Pokemon p = (Pokemon) value;
            setIcon(Imagenes.pokemon(p.getNombre(), p.getTipo(), 36));
            String tipo = Imagenes.nombreTipo(p.getTipo());
            setText(numerar
                    ? (index + 1) + ". " + p.getNombre() + "  (" + tipo + ")"
                    : p.getNombre() + "  (" + tipo + ")");
            setFont(getFont().deriveFont(14f));
            setBorder(new EmptyBorder(4, 6, 4, 6));
            return this;
        }
    }
}
