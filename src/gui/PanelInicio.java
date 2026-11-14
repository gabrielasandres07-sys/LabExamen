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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.URL;


public class PanelInicio extends JPanel {

    private final VentanaPrincipal ventana;
    private final JTextField txtNombre = new JTextField(15);
    private final JLabel lblError = new JLabel(" ");

    public PanelInicio(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setLayout(new GridBagLayout());          
        setBackground(new Color(30, 60, 114));   
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        
        JLabel lblLogo = crearLogo();
        lblLogo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("BATALLAS POKÉMON");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblTitulo.setForeground(new Color(255, 203, 5));   
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Simulador de batallas por turnos");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSub.setForeground(Color.WHITE);
        lblSub.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblNombre = new JLabel("Nombre del entrenador:");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);

        txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtNombre.setMaximumSize(new Dimension(260, 36));
        txtNombre.setHorizontalAlignment(JTextField.CENTER);
        txtNombre.setAlignmentX(CENTER_ALIGNMENT);
        txtNombre.setToolTipText("Entre 3 y 10 caracteres: letras, números y espacios");

        lblError.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblError.setForeground(new Color(255, 110, 110));  
        lblError.setAlignmentX(CENTER_ALIGNMENT);

    
        JButton btnInstrucciones = new JButton("Instrucciones");
        JButton btnContinuar = new JButton("Continuar");
        JButton btnSalir = new JButton("Salir");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        botones.setOpaque(false);
        botones.add(btnInstrucciones);
        botones.add(btnContinuar);
        botones.add(btnSalir);
        botones.setAlignmentX(CENTER_ALIGNMENT);

      
        btnContinuar.addActionListener(e -> continuar());
        txtNombre.addActionListener(e -> continuar());     // Enter = Continuar
        btnInstrucciones.addActionListener(e -> mostrarInstrucciones());
        btnSalir.addActionListener(e -> ventana.salir());

     
        txtNombre.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { lblError.setText(" "); }
            public void removeUpdate(DocumentEvent e)  { lblError.setText(" "); }
            public void changedUpdate(DocumentEvent e) { }
        });

      
        centro.add(lblLogo);
        centro.add(Box.createVerticalStrut(10));
        centro.add(lblTitulo);
        centro.add(lblSub);
        centro.add(Box.createVerticalStrut(40));
        centro.add(lblNombre);
        centro.add(Box.createVerticalStrut(8));
        centro.add(txtNombre);
        centro.add(Box.createVerticalStrut(6));
        centro.add(lblError);
        centro.add(Box.createVerticalStrut(20));
        centro.add(botones);

        add(centro);
    }

    private void continuar() {
        String texto = txtNombre.getText();
        String error = Entrenador.validarNombre(texto);

        if (error != null) {
            lblError.setText(error);
            txtNombre.requestFocus();
            return;                         
        }

        ventana.setEntrenador(new Entrenador(texto));
        ventana.mostrar(VentanaPrincipal.EQUIPO);
    }

    private void mostrarInstrucciones() {
        String reglas =
            "CÓMO JUGAR\n\n" +
            "1. Escribe tu nombre de entrenador (3 a 10 caracteres).\n" +
            "2. Arma tu equipo con 1 a 6 Pokémon del catálogo\n" +
            "    (máximo 2 copias del mismo Pokémon).\n" +
            "3. Combate contra el Rival CPU por rondas.\n\n" +
            "ACCIONES EN CADA RONDA\n" +
            "•  Atacar: ataque básico (potencia 10).\n" +
            "•  Habilidad especial: más daño, 3 usos por batalla.\n" +
            "•  Recuperar: cura 25% de los PS, 2 usos por batalla.\n" +
            "•  Cambiar: cambia tu Pokémon activo (siempre va primero).\n\n" +
            "TIPOS\n" +
            "Fuego > Planta > Agua > Fuego.   Eléctrico > Agua.\n" +
            "Un ataque muy eficaz hace el doble de daño.\n\n" +
            "FIN DE LA BATALLA\n" +
            "Gana quien deje al rival sin Pokémon en pie.\n" +
            "Si se llega a 50 rondas, gana quien tenga más PS en total.";

        JOptionPane.showMessageDialog(this, reglas, "Instrucciones",
                JOptionPane.INFORMATION_MESSAGE);
    }

    
    private JLabel crearLogo() {
        URL url = getClass().getResource("/recursos/logo.png");
        if (url != null) {
            ImageIcon icono = new ImageIcon(url);
            Image escalada = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(escalada));
        }
        JLabel alternativo = new JLabel("◓");
        alternativo.setFont(new Font("SansSerif", Font.BOLD, 90));
        alternativo.setForeground(Color.WHITE);
        return alternativo;
    }

  
    public void reiniciar() {
        lblError.setText(" ");
    }
}