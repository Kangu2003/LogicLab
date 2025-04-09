package compuertas;

import java.awt.*;

public class Conexion {
    private Componente origen;
    private Componente destino;
    private int indiceEntrada;
    private boolean valor;

    public Conexion(Componente origen, Componente destino, int indiceEntrada) {
        this.origen = origen;
        this.destino = destino;
        this.indiceEntrada = indiceEntrada;
    }

    public void Dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point inicio = origen.getPuntoConexionSalida();
        Point fin = destino.getPuntosConexionEntrada().get(indiceEntrada);

        g2.setColor(valor ? Color.GREEN.darker() : Color.RED.darker());

        // Punto medio en X para formar la "Z" o "L"
        int midX = (inicio.x + fin.x) / 2;

        // Dibujar en tres tramos: horizontal - vertical - horizontal
        g2.drawLine(inicio.x, inicio.y, midX, inicio.y); // Horizontal desde origen
        g2.drawLine(midX, inicio.y, midX, fin.y);         // Vertical hasta la altura del destino
        g2.drawLine(midX, fin.y, fin.x, fin.y);           // Horizontal hasta destino
    }

    public boolean contiene(Point p) {
        // Chequeo simple: usar distancia mínima al rectángulo de la línea
        Point inicio = origen.getPuntoConexionSalida();
        Point fin = destino.getPuntosConexionEntrada().get(indiceEntrada);
        Rectangle area = new Rectangle(
                Math.min(inicio.x, fin.x) - 4,
                Math.min(inicio.y, fin.y) - 4,
                Math.abs(inicio.x - fin.x) + 8,
                Math.abs(inicio.y - fin.y) + 8
        );
        return area.contains(p);
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public boolean getValor() {
        return valor;
    }

    public Componente getOrigen() {
        return origen;
    }

    public Componente getDestino() {
        return destino;
    }

    public int getIndiceEntrada() {
        return indiceEntrada;
    }
}
