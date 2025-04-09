package compuertas;
import java.awt.*;
import java.awt.geom.*;

public class Xor extends Compuerta {
    private static final int CURVA_ANCHO = 10;
    private Color colorRelleno = new Color(255, 255, 100); // Color amarillo suave
    private Color colorContorno = Color.BLACK;
    
    public Xor(int x, int y) {
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
        
        // La curva adicional que distingue XOR de OR
        GeneralPath curvaExtra = new GeneralPath();
        curvaExtra.moveTo(x - 5, y + alto / 6);
        curvaExtra.quadTo(x + 5, y + alto / 2, x - 5, y + 5 * alto / 6);
        g2d.setColor(seleccionada ? Color.RED : colorContorno);
        g2d.draw(curvaExtra);
        
        // La forma principal (igual que OR)
        GeneralPath forma = new GeneralPath();
        forma.moveTo(x + 5, y);
        forma.quadTo(x + ancho / 2, y - CURVA_ANCHO / 2, x + ancho, y + alto / 2);
        forma.quadTo(x + ancho / 2, y + alto + CURVA_ANCHO / 2, x + 5, y + alto);
        forma.quadTo(x + ancho / 4, y + alto / 2, x + 5, y);
        
        // Relleno mejorado con gradiente suave
        GradientPaint gradiente = new GradientPaint(
            x, y, new Color(255, 255, 200),
            x + ancho, y + alto, new Color(255, 240, 100),
            false
        );
        g2d.setPaint(gradiente);
        g2d.fill(forma);
        
        // Borde de la forma principal
        g2d.setColor(seleccionada ? Color.RED : colorContorno);
        g2d.draw(forma);
        
        // Curva del borde (igual que OR)
        GeneralPath curvaBorde = new GeneralPath();
        curvaBorde.moveTo(x, y + alto / 6);
        curvaBorde.quadTo(x + 10, y + alto / 2, x, y + 5 * alto / 6);
        g2d.draw(curvaBorde);
        
        // Las líneas de entrada y salida
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(new Line2D.Double(x - 15, y + alto / 3, x, y + alto / 3));
        g2d.draw(new Line2D.Double(x - 15, y + 2 * alto / 3, x, y + 2 * alto / 3));
        g2d.draw(new Line2D.Double(x + ancho, y + alto / 2, x + ancho + 15, y + alto / 2));
        
        // Mejoras en los puntos de conexión
        // Puntos con efecto de brillo
        dibujarPuntoConexion(g2d, x, y + alto / 3);
        dibujarPuntoConexion(g2d, x, y + 2 * alto / 3);
        dibujarPuntoConexion(g2d, x + ancho, y + alto / 2);
        
        g2d.dispose();
    }
    
    // Método para dibujar puntos de conexión con mejor apariencia
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
        boolean a = false, b = false;
        if (conexionesEntrada.size() >= 2) {
            a = conexionesEntrada.get(0) != null && conexionesEntrada.get(0).getValor();
            b = conexionesEntrada.get(1) != null && conexionesEntrada.get(1).getValor();
        }
        return a ^ b;
    }
}