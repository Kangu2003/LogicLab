package Compuertas;

import Logica.Componente;
import Logica.Compuerta;
import java.awt.*;
import java.awt.geom.*;


public class XNOR extends Compuerta {
     private static final int CURVA_ANCHO = 10;
    private Color colorRelleno = new Color(204, 153, 0); // Amarillo oscuro
    private Color colorContorno = Color.BLACK;
    private static final int RADIO_CIRCULO = 10;
    
    
    public XNOR(int x, int y) {
        super(x, y, 60, 40);
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
        
        GeneralPath forma = new GeneralPath();
        forma.moveTo(x + 5, y);
        forma.quadTo(x + ancho / 2, y - CURVA_ANCHO / 2, x + ancho, y + alto / 2);
        forma.quadTo(x + ancho / 2, y + alto + CURVA_ANCHO / 2, x + 5, y + alto);
        forma.quadTo(x + ancho / 4, y + alto / 2, x + 5, y);
        
      
        GradientPaint gradiente = new GradientPaint(
            x, y, new Color(255, 255, 200),
            x + ancho, y + alto, new Color(204, 153, 0),
            false
        );
        g2d.setPaint(gradiente);
        g2d.fill(forma);
        
       
        g2d.setColor(seleccionada ? Color.RED : colorContorno);
        g2d.draw(forma);
        
      
        GeneralPath curvaBorde = new GeneralPath();
        curvaBorde.moveTo(x, y + alto / 6);
        curvaBorde.quadTo(x + 10, y + alto / 2, x, y + 5 * alto / 6);
        g2d.draw(curvaBorde);
        
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(new Line2D.Double(x - 15, y + alto / 3, x, y + alto / 3));
        g2d.draw(new Line2D.Double(x - 15, y + 2 * alto / 3, x, y + 2 * alto / 3));
       g2d.drawLine(x + ancho + RADIO_CIRCULO, y + alto / 2, x + ancho + RADIO_CIRCULO + 15, y + alto / 2);
        
        
           // Círculo de negación
        int cx = x + ancho;
        int cy = y + alto / 2 - RADIO_CIRCULO / 2;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(cx, cy, RADIO_CIRCULO, RADIO_CIRCULO);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(cx, cy, RADIO_CIRCULO, RADIO_CIRCULO);
        
      
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
        if (conexionesEntrada.size() >= 2) {
            boolean a = conexionesEntrada.get(0) != null && conexionesEntrada.get(0).getValor();
            boolean b = conexionesEntrada.get(1) != null && conexionesEntrada.get(1).getValor();
            return !(a ^ b); // XNOR lógico
        }
        return false;
    }
    
    @Override
public Componente clonar() {
    XNOR clon = new XNOR(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    return clon;
}

}
