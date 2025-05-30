package Componentes;

import Logica.Componente;
import Logica.Conexion;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;

public final class LED extends Componente {
  
    private boolean encendido;
    private Color colorApagado;
    private Color colorEncendido;
    private List<Conexion> conexionesEntrada;

    public LED(int x, int y) {
        super(x, y, 20, 32);
        this.encendido = false;
        this.colorApagado = new Color(220, 220, 220, 180); // Transparente cuando apagado
        this.colorEncendido = new Color(255, 50, 50);      // Rojo por defecto
        this.conexionesEntrada = new ArrayList<>();
        actualizarPuntosConexion();
    }

    @Override
    public void actualizarPuntosConexion() {
        puntosConexionEntrada.clear();
        puntosConexionEntrada.add(new Point(x + ancho / 2, y + alto + 10));

    }

    //  Usamos directamente la lista de conexiones de entrada real
    public List<Conexion> getEntradas() {
        return conexionesEntrada;
    }

    //  Cambia el estado manualmente (si es necesario)
    public void setEstado(boolean estado) {
        this.encendido = estado;
    }

    public void setEncendido(boolean estado) {
        this.encendido = estado;
    }

    public boolean isEncendido() {
        return encendido;
    }

    public void setColorEncendido(Color color) {
        this.colorEncendido = color;
    }

    @Override
    public void Dibujar(Graphics g) {
        evaluarSalida();

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1.5f));

        int ledWidth = ancho;
        int ledHeight = alto - 10;
        int arcDiameter = ledWidth;

        // Patas del LED
        int pinWidth = 2;
        g2d.setColor(new Color(192, 192, 192));
        g2d.fillRect(x + (ledWidth - pinWidth) / 2, y + ledHeight, pinWidth, 10);
        g2d.setColor(Color.BLACK);
        g2d.draw(new Line2D.Double(x + ledWidth / 2, y + alto, x + ledWidth / 2, y + alto + 10));
        dibujarPuntoConexion(g2d,  x + ledWidth / 2, y + alto + 10);

        if (encendido) {
            g2d.setColor(new Color(colorEncendido.getRed(), colorEncendido.getGreen(), colorEncendido.getBlue(), 70));
            g2d.fillOval(x - 3, y - 3, ledWidth + 6, arcDiameter + 6);
        }

        g2d.setColor(encendido ? colorEncendido : colorApagado);
        g2d.fillRect(x, y + arcDiameter / 2, ledWidth, ledHeight - arcDiameter / 2);
        g2d.fillArc(x, y, ledWidth, arcDiameter, 0, 180);

        g2d.setColor(seleccionada ? Color.RED : Color.BLACK);
        g2d.drawLine(x, y + arcDiameter / 2, x, y + ledHeight);
        g2d.drawLine(x + ledWidth, y + arcDiameter / 2, x + ledWidth, y + ledHeight);
        g2d.drawLine(x, y + ledHeight, x + ledWidth, y + ledHeight);
        g2d.drawArc(x, y, ledWidth, arcDiameter, 0, 180);

        if (encendido) {
            g2d.setColor(new Color(255, 255, 255, 160));
            g2d.fillOval(x + ledWidth / 4, y + ledWidth / 6, ledWidth / 3, ledWidth / 3);
        }

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
    if (!conexionesEntrada.isEmpty() && conexionesEntrada.get(0) != null) {
        encendido = conexionesEntrada.get(0).getValor();
    } else {
        encendido = false; // Se apaga si no tiene entradas
    }
    return encendido;
}

    @Override
    public List<Conexion> getConexionesEntrada() {
        return conexionesEntrada;
    }

    public void agregarConexionEntrada(Conexion conexion) {
        conexionesEntrada.add(conexion);
    }

    public void eliminarConexionEntrada(Conexion conexion) {
        conexionesEntrada.remove(conexion);
    }
    
    @Override
public Componente clonar() {
    LED clon = new LED(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    clon.setEncendido(this.encendido);
    clon.setColorEncendido(this.getColorEncendido());
    return clon;
}

    private Color getColorEncendido() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
