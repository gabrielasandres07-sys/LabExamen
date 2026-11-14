/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package labexamen;

/**
 *
 * @author USER
 */


import gui.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LabExamen {

    public static void main(String[] args) {
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");

        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}