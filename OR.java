package Compuertas;

import Logica.Componente;
import Logica.Compuerta;
import Logica.Conexion;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class OR extends Compuerta {
    private Color colorContorno = Color.BLACK;

    public OR(int x, int y) {
        super(x, y, 60, 40); 
        actualizarPuntosConexion();
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

      
        GeneralPath forma = new GeneralPath();

        forma.moveTo(x + 10, y); 
        forma.quadTo(x + 5, y + alto / 2, x + 10, y + alto); 
        forma.quadTo(x + ancho - 15, y + alto, x + ancho - 5, y + alto / 2); 
        forma.quadTo(x + ancho - 15, y, x + 10, y); 

       
        g2d.setColor(new Color(200, 255, 200, 180)); 
        g2d.fill(forma);

        
        g2d.setColor(seleccionada ? Color.RED : colorContorno);
        g2d.draw(forma);

        g2d.setColor(Color.BLACK);
        g2d.draw(new Line2D.Double(x - 15, y + alto / 3, x + 7, y + alto / 3));
        g2d.draw(new Line2D.Double(x - 15, y + 2 * alto / 3, x + 7, y + 2 * alto / 3));

        
        g2d.draw(new Line2D.Double(x + ancho - 5, y + alto / 2, x + ancho + 15, y + alto / 2));

       
        dibujarPuntoConexion(g2d, x - 15, y + alto / 3);             
        dibujarPuntoConexion(g2d, x - 15, y + 2 * alto / 3);           
        dibujarPuntoConexion(g2d, x + ancho + 15, y + alto / 2); 

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
                return true; 
            }
        }
        return false; 
    }
    @Override
public Componente clonar() {
    OR clon = new OR(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    return clon;
}


    public void setColorContorno(Color color) {
        this.colorContorno = color;
    }
}
