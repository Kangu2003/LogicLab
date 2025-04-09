package compuertas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D; 
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

public class AND extends Compuerta {
    private static final int CURVA_ANCHO = 30;
    private Color colorRelleno = new Color(70, 130, 180); // Azul para AND
    private Color colorContorno = Color.BLACK;
    
    public AND(int x, int y) {
        super(x, y, 60, 40);
    }
    
    @Override
    public void actualizarPuntosConexion() {
        puntosConexionEntrada.clear();
        puntosConexionEntrada.add(new Point(x, y + alto / 3));
        puntosConexionEntrada.add(new Point(x, y + 2 * alto / 3));
        puntoConexionSalida.setLocation(x + ancho, y + alto / 2);
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
        
       
        g2d.fillOval(x - 3, y + alto / 3 - 3, 6, 6);
        g2d.fillOval(x - 3, y + 2 * alto / 3 - 3, 6, 6);
        g2d.fillOval(x + ancho - 3, y + alto / 2 - 3, 6, 6);
        
        g2d.dispose();
    }
    
    @Override
    public boolean evaluarSalida() {
        boolean resultado = true;
        
        // Si no hay conexiones de entrada, el resultado es falso
        if (conexionesEntrada.isEmpty()) {
            return false;
        }
        
        // Para una puerta AND, todas las entradas deben ser verdaderas
        for (Conexion conexion : conexionesEntrada) {
            if (conexion == null || !conexion.getValor()) {
                resultado = false;
                break;
            }
        }
        return resultado;
    }
    
    public void setColorRelleno(Color color) {
        this.colorRelleno = color;
    }
    
    public void setColorContorno(Color color) {
        this.colorContorno = color;
    }
}