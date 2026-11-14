/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author USER
 */
import modelo.TipoPokemon;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Imagenes {

    private static final Map<String, ImageIcon> CACHE = new HashMap<>();

    private Imagenes() {
    }

    public static ImageIcon pokemon(String nombre, TipoPokemon tipo, int tamanio) {
        String clave = nombre.toLowerCase() + "@" + tamanio;
        ImageIcon icono = CACHE.get(clave);
        if (icono == null) {
            URL url = Imagenes.class.getResource("/recursos/" + nombre.toLowerCase() + ".png");
            if (url != null) {
                icono = ajustar(new ImageIcon(url).getImage(), tamanio);
            } else {
                icono = generica(nombre, tipo, tamanio);
            }
            CACHE.put(clave, icono);
        }
        return icono;
    }

    private static ImageIcon ajustar(Image original, int tamanio) {
        int ancho = original.getWidth(null);
        int alto = original.getHeight(null);
        double escala = Math.min((double) tamanio / ancho, (double) tamanio / alto);
        int nuevoAncho = Math.max(1, (int) Math.round(ancho * escala));
        int nuevoAlto = Math.max(1, (int) Math.round(alto * escala));

        BufferedImage actual = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g0 = actual.createGraphics();
        g0.drawImage(original, 0, 0, null);
        g0.dispose();

        while (ancho / 2 >= nuevoAncho && alto / 2 >= nuevoAlto) {
            ancho /= 2;
            alto /= 2;
            actual = redimensionar(actual, ancho, alto);
        }
        actual = redimensionar(actual, nuevoAncho, nuevoAlto);

        BufferedImage lienzo = new BufferedImage(tamanio, tamanio, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = lienzo.createGraphics();
        g.drawImage(actual, (tamanio - nuevoAncho) / 2, (tamanio - nuevoAlto) / 2, null);
        g.dispose();
        return new ImageIcon(lienzo);
    }

    private static BufferedImage redimensionar(BufferedImage img, int ancho, int alto) {
        BufferedImage destino = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = destino.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(img, 0, 0, ancho, alto, null);
        g.dispose();
        return destino;
    }

    private static ImageIcon generica(String nombre, TipoPokemon tipo, int tamanio) {
        BufferedImage img = new BufferedImage(tamanio, tamanio, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int margen = tamanio / 10;
        int diametro = tamanio - 2 * margen;
        g.setColor(colorTipo(tipo));
        g.fillOval(margen, margen, diametro, diametro);
        g.setColor(colorTipo(tipo).darker());
        g.setStroke(new BasicStroke(Math.max(2f, tamanio / 40f)));
        g.drawOval(margen, margen, diametro, diametro);

        String inicial = nombre.isEmpty() ? "?" : nombre.substring(0, 1).toUpperCase();
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, tamanio / 2));
        FontMetrics fm = g.getFontMetrics();
        int x = (tamanio - fm.stringWidth(inicial)) / 2;
        int y = (tamanio - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(inicial, x, y);
        g.dispose();
        return new ImageIcon(img);
    }

    public static Color colorTipo(TipoPokemon tipo) {
        if (tipo == null) {
            return Color.GRAY;
        }
        switch (tipo) {
            case FUEGO:
                return new Color(235, 95, 45);
            case AGUA:
                return new Color(55, 140, 230);
            case PLANTA:
                return new Color(70, 170, 80);
            default:
                return new Color(230, 180, 20);
        }
    }

    public static String nombreTipo(TipoPokemon tipo) {
        if (tipo == null) {
            return "";
        }
        switch (tipo) {
            case FUEGO:
                return "Fuego";
            case AGUA:
                return "Agua";
            case PLANTA:
                return "Planta";
            default:
                return "Eléctrico";
        }
    }

    public static String descripcionHabilidad(TipoPokemon tipo) {
        if (tipo == null) {
            return "";
        }
        switch (tipo) {
            case FUEGO:
                return "Potencia 20, daño elevado";
            case AGUA:
                return "Potencia 18 y recupera 10% de PS";
            case PLANTA:
                return "Potencia 18 y absorbe la mitad del daño";
            default:
                return "Potencia 17 y 25% de golpe crítico";
        }
    }
}
