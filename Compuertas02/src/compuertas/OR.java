package compuertas;

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
        super(x, y, 60, 40); // dimensiones típicas para compuerta OR
        actualizarPuntosConexion();
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

        // Forma de la compuerta OR
        GeneralPath forma = new GeneralPath();

        forma.moveTo(x + 10, y); // Punto superior
        forma.quadTo(x + 5, y + alto / 2, x + 10, y + alto); // Curva izquierda
        forma.quadTo(x + ancho - 15, y + alto, x + ancho - 5, y + alto / 2); // Curva inferior derecha
        forma.quadTo(x + ancho - 15, y, x + 10, y); // Curva superior derecha

        // Relleno
        g2d.setColor(new Color(200, 255, 200, 180)); // verde claro translúcido
        g2d.fill(forma);

        // Contorno
        g2d.setColor(seleccionada ? Color.RED : colorContorno);
        g2d.draw(forma);

        // Líneas de entrada
        g2d.setColor(Color.BLACK);
        g2d.draw(new Line2D.Double(x - 15, y + alto / 3, x + 7, y + alto / 3));
        g2d.draw(new Line2D.Double(x - 15, y + 2 * alto / 3, x + 7, y + 2 * alto / 3));

        // Línea de salida
        g2d.draw(new Line2D.Double(x + ancho - 5, y + alto / 2, x + ancho + 15, y + alto / 2));

        // Puntos de conexión
        dibujarPuntoConexion(g2d, x, y + alto / 3);
        dibujarPuntoConexion(g2d, x, y + 2 * alto / 3);
        dibujarPuntoConexion(g2d, x + ancho, y + alto / 2);

        g2d.dispose();
    }

    private void dibujarPuntoConexion(Graphics2D g2d, int px, int py) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(px - 4, py - 4, 8, 8);

        g2d.setColor(seleccionada ? Color.RED : Color.BLACK);
        g2d.drawOval(px - 4, py - 4, 8, 8);

        g2d.fillOval(px - 2, py - 2, 4, 4);
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

    public void setColorContorno(Color color) {
        this.colorContorno = color;
    }
}
