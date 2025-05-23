package Compuertas;

import Logica.Componente;
import Logica.Compuerta;
import Logica.Conexion;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class NOR extends Compuerta {
    private static final int RADIO_CIRCULO = 8;

    public NOR(int x, int y) {
        super(x, y, 60, 40); // Ancho y alto estándar
    }

    @Override
   public void actualizarPuntosConexion() {
    puntosConexionEntrada.clear();
    puntosConexionEntrada.add(new Point(x - 15, y + alto / 3));
    puntosConexionEntrada.add(new Point(x - 15, y + 2 * alto / 3));
    puntoConexionSalida.setLocation(x + ancho + 15, y + alto / 2);
}

    @Override
    public void Dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Forma curva principal tipo OR
        GeneralPath forma = new GeneralPath();
        forma.moveTo(x + 10, y);
        forma.quadTo(x + 5, y + alto / 2, x + 10, y + alto);
        forma.quadTo(x + ancho - 15, y + alto, x + ancho - 5, y + alto / 2);
        forma.quadTo(x + ancho - 15, y, x + 10, y);
        forma.closePath();

        g2d.setColor(new Color(34, 139, 34, 180)); 
        g2d.fill(forma);
        g2d.setColor(seleccionada ? Color.RED : Color.BLACK);
        g2d.draw(forma);

   

        // Círculo de negación (salida)
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + ancho - 2, y + alto / 2 - RADIO_CIRCULO / 2, RADIO_CIRCULO, RADIO_CIRCULO);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x + ancho - 2, y + alto / 2 - RADIO_CIRCULO / 2, RADIO_CIRCULO, RADIO_CIRCULO);

        // Líneas de entrada
        g2d.drawLine(x - 15, y + alto / 3, x + 7, y + alto / 3);
        g2d.drawLine(x - 15, y + 2 * alto / 3, x + 7, y + 2 * alto / 3);

        // Línea de salida
        g2d.drawLine(x + ancho + RADIO_CIRCULO, y + alto / 2, x + ancho + RADIO_CIRCULO + 15, y + alto / 2);
        
       // Punto final de cada línea de entrada (x, misma Y)
        dibujarPuntoConexion(g2d, x - 15, y + alto / 3);             
        dibujarPuntoConexion(g2d, x - 15, y + 2 * alto / 3);           
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
    public boolean evaluarSalida() {
        for (Conexion conexion : conexionesEntrada) {
            if (conexion != null && conexion.getValor()) {
                return false; // Si alguna es verdadera, el resultado NOR es falso
            }
        }
        return true;
    }
    
    @Override
public Componente clonar() {
    NOR clon = new NOR(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    return clon;
}

}
