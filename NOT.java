package Compuertas;

import Logica.Componente;
import Logica.Compuerta;
import Logica.Conexion;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.List;

public class NOT extends Compuerta {
    private static final Color COLOR_RELLENO = new Color(255, 150, 150, 200); 
    private static final Color COLOR_CONTORNO = Color.BLACK;
    private static final int TAMANO_CIRCULO = 10;
    private boolean valorEntrada = false;  // Valor predeterminado para entrada

    public NOT(int x, int y) {
        super(x, y, 50, 40); 
        actualizarPuntosConexion();
    }

    @Override
    public void actualizarPuntosConexion() {
        puntosConexionEntrada.clear();
        puntosConexionEntrada.add(new Point(x - 15, y + alto / 2));
        puntoConexionSalida.setLocation(x + ancho + 15, y + alto / 2);
    }

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
        dibujarPuntoConexion(g2d, x - 15, y + alto / 2);             // entrada
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
        // Actualizar el valor de entrada basado en conexiones
        actualizarValorEntrada();
        // Aplicar la función NOT al valor de entrada
        return !valorEntrada;
    }
    
    /**
     * Actualiza el valor de entrada basado en las conexiones existentes
     */
    private void actualizarValorEntrada() {
        valorEntrada = false; // Valor predeterminado si no hay conexión
        
        if (!conexionesEntrada.isEmpty()) {
            Conexion conexion = conexionesEntrada.get(0);
            if (conexion != null) {
                valorEntrada = conexion.getValor();
            }
        }
    }
    @Override
public Componente clonar() {
    NOT clon = new NOT(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    return clon;
}

    @Override
    public List<Conexion> getConexionesEntrada() {
        return conexionesEntrada;
    }
}