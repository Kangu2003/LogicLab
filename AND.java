package Compuertas;


import Logica.Componente;
import Logica.Compuerta;
import Logica.Conexion;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D; 
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

public class AND extends Compuerta {
    private static final int CURVA_ANCHO = 30;
    private Color colorRelleno = new Color(70, 130, 180);
    private Color colorContorno = Color.BLACK;
    
    public AND(int x, int y) {
        super(x, y, 60, 40);
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
        forma.moveTo(x, y);
        forma.lineTo(x + ancho / 2, y);
        forma.quadTo(x + ancho, y, x + ancho, y + alto / 2);
        forma.quadTo(x + ancho, y + alto, x + ancho / 2, y + alto);
        forma.lineTo(x, y + alto);
        forma.closePath();
        
       
        g2d.setColor(new Color(150, 200, 255, 180));
        g2d.fill(forma);
        
        
        g2d.setColor(seleccionada ? Color.RED : colorContorno);
        g2d.draw(forma);
        
        
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(new Line2D.Double(x - 15, y + alto / 3, x, y + alto / 3));
        g2d.draw(new Line2D.Double(x - 15, y + 2 * alto / 3, x, y + 2 * alto / 3));
        
        
        g2d.draw(new Line2D.Double(x + ancho, y + alto / 2, x + ancho + 15, y + alto / 2));
        
       
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
    
    if (conexionesEntrada.size() < 2) {
        return false;
    }

    for (int i = 0; i < 2; i++) {
        Conexion conexion = conexionesEntrada.get(i);
        if (conexion == null || !conexion.getValor()) {
            return false; // Si alguna entrada es falsa o nula
        }
    }

    return true; // Ambas entradas son verdaderas
}

@Override
public Componente clonar() {
    AND clon = new AND(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    return clon;
}


    
    public void setColorRelleno(Color color) {
        this.colorRelleno = color;
    }
    
    public void setColorContorno(Color color) {
        this.colorContorno = color;
    }
}