package Compuertas;

import Logica.Componente;
import Logica.Compuerta;
import Logica.Conexion;
import java.awt.*;
import java.awt.geom.*;

public class NAND extends Compuerta {
    private static final int RADIO_CIRCULO = 10;
    private static final Color COLOR_RELLENO = new Color(0, 0, 139, 180); // Azul rey semi-transparente

    public NAND(int x, int y) {
        super(x, y, 60, 40); // Tamaño estándar
    }

    @Override
    public void actualizarPuntosConexion() {
    puntosConexionEntrada.clear();
    puntosConexionEntrada.add(new Point(x - 15, y + alto / 3));
    puntosConexionEntrada.add(new Point(x - 15, y + 2 * alto / 3));
    puntoConexionSalida.setLocation(x + ancho + RADIO_CIRCULO + 15, y + alto / 2); 
       
    }

    @Override
    public void Dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Cuerpo AND semicircular + recta
        GeneralPath forma = new GeneralPath();
        forma.moveTo(x, y);
        forma.lineTo(x + ancho / 2, y);
        forma.quadTo(x + ancho, y, x + ancho, y + alto / 2);
        forma.quadTo(x + ancho, y + alto, x + ancho / 2, y + alto);
        forma.lineTo(x, y + alto);
        forma.closePath();

        // Relleno
        g2d.setColor(COLOR_RELLENO);
        g2d.fill(forma);

        // Contorno
        g2d.setColor(seleccionada ? Color.RED : Color.BLACK);
        g2d.draw(forma);

        // Círculo de negación
        int cx = x + ancho;
        int cy = y + alto / 2 - RADIO_CIRCULO / 2;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(cx, cy, RADIO_CIRCULO, RADIO_CIRCULO);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(cx, cy, RADIO_CIRCULO, RADIO_CIRCULO);

        // Línea de salida (después del círculo)
        g2d.drawLine(x + ancho + RADIO_CIRCULO, y + alto / 2, x + ancho + RADIO_CIRCULO + 15, y + alto / 2);

        // Líneas de entrada
        g2d.drawLine(x - 15, y + alto / 3, x, y + alto / 3);
        g2d.drawLine(x - 15, y + 2 * alto / 3, x, y + 2 * alto / 3);

    // Punto final de cada línea de entrada (x, misma Y)
        dibujarPuntoConexion(g2d, x - 15, y + alto / 3);               // izquierda superior
        dibujarPuntoConexion(g2d,x - 15, y + 2 * alto / 3 );           // izquierda inferior
        dibujarPuntoConexion(g2d, x + ancho + RADIO_CIRCULO + 15, y + alto / 2); 
        
        g2d.dispose();
    }

   private void dibujarPuntoConexion(Graphics2D g, int x, int y) {
        // Sombra sutil
        g.setColor(new Color(0, 0, 0, 70));
        g.fillOval(x - 2, y - 2, 6, 6);
        
        // Punto principal
        g.setColor(Color.RED);
        g.fillOval(x - 3, y - 3, 6, 6);
        
        // Brillo para efecto 3D
        g.setColor(new Color(255, 200, 200));
        g.fillOval(x - 2, y - 2, 2, 2);
    }
@Override
public Componente clonar() {
    NAND clon = new NAND(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    return clon;
}

    @Override
    public boolean evaluarSalida() {
        for (Conexion conexion : conexionesEntrada) {
            if (conexion == null || !conexion.getValor()) {
                return true; // Una falsa hace que NAND sea verdadera
            }
        }
        return false; // Todas verdaderas son false
    }
}
