package Logica;

import java.awt.*;

public class ConexionTemporal {
    private Componente componenteInicio;
    private Point fin;

    public ConexionTemporal(Componente componenteInicio, Point fin) {
        this.componenteInicio = componenteInicio;
        this.fin = fin;
    }

    public void Dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point inicio = componenteInicio.getPuntoConexionSalida();
        int midX = (inicio.x + fin.x) / 2;

        g2.drawLine(inicio.x, inicio.y, midX, inicio.y);
        g2.drawLine(midX, inicio.y, midX, fin.y);
        g2.drawLine(midX, fin.y, fin.x, fin.y);
    }

    public void setFin(Point fin) {
        this.fin = fin;
    }

    public Componente getComponenteInicio() {
        return componenteInicio;
    }
}