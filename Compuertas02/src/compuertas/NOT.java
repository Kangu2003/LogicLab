package compuertas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

public class NOT extends Compuerta {
    private static final Color COLOR_RELLENO = new Color(255, 150, 150, 200); // Rojo suave semi-transparente
    private static final Color COLOR_CONTORNO = Color.BLACK;
    private static final int TAMANO_CIRCULO = 10;

    public NOT(int x, int y) {
        super(x, y, 50, 40); // solo pasamos x, y, ancho, alto
        actualizarPuntosConexion();
    }
    // Puntos de conexion Not
    @Override
    public void actualizarPuntosConexion() {
        puntosConexionEntrada.clear();
        puntosConexionEntrada.add(new Point(x, y + alto / 2));
        puntoConexionSalida.setLocation(x + ancho, y + alto / 2);
    }
// Dibujo de la compuerta NOT
    @Override
    public void Dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Suavizado
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Triángulo NOT
        int[] xPoints = {x, x, x + ancho - TAMANO_CIRCULO};
        int[] yPoints = {y, y + alto, y + alto / 2};

        // Líneas de entrada/salida
        g2d.setColor(COLOR_CONTORNO);
        g2d.draw(new Line2D.Double(x - 15, y + alto / 2, x, y + alto / 2));           // entrada
        g2d.draw(new Line2D.Double(x + ancho, y + alto / 2, x + ancho + 15, y + alto / 2)); // salida

        // Relleno triángulo
        g2d.setColor(COLOR_RELLENO);
        g2d.fillPolygon(xPoints, yPoints, 3);

        // Contorno triángulo
        g2d.setColor(seleccionada ? Color.RED : COLOR_CONTORNO);
        g2d.drawPolygon(xPoints, yPoints, 3);

        // Círculo de negación
        int circuloX = x + ancho - TAMANO_CIRCULO;
        int circuloY = y + alto / 2 - TAMANO_CIRCULO / 2;

        g2d.setColor(Color.WHITE);
        g2d.fillOval(circuloX, circuloY, TAMANO_CIRCULO, TAMANO_CIRCULO);
        g2d.setColor(seleccionada ? Color.RED : COLOR_CONTORNO);
        g2d.drawOval(circuloX, circuloY, TAMANO_CIRCULO, TAMANO_CIRCULO);

        // Puntos de conexión
        dibujarPuntoConexion(g2d, x, y + alto / 2);             // entrada
        dibujarPuntoConexion(g2d, x + ancho, y + alto / 2);     // salida

        g2d.dispose();
    }
// Metodo para dibujar compuerta not en MiPanel 
    private void dibujarPuntoConexion(Graphics2D g2d, int px, int py) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(px - 4, py - 4, 8, 8);

        g2d.setColor(seleccionada ? Color.RED : COLOR_CONTORNO);
        g2d.drawOval(px - 4, py - 4, 8, 8);

        g2d.fillOval(px - 2, py - 2, 4, 4);
    }
// Evaluacion de la compuerta NOT
    @Override
    public boolean evaluarSalida() {
        if (conexionesEntrada.size() > 0 && conexionesEntrada.get(0) != null) {
            return !conexionesEntrada.get(0).getValor();
        }
        return true; // Por defecto: TRUE si no hay entrada
    }
}
