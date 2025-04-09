package compuertas;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public abstract class Componente {
    protected int x, y;
    protected final int ancho, alto;
    protected boolean seleccionada;
    protected List<Point> puntosConexionEntrada;
    protected Point puntoConexionSalida;
    
    public Componente(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.puntosConexionEntrada = new ArrayList<>();
        this.puntoConexionSalida = new Point();
        actualizarPuntosConexion();
    }

    public abstract void Dibujar(Graphics g);
    
    public void actualizarPuntosConexion() {
        puntosConexionEntrada.clear();
        // Las clases derivadas implementarán la lógica específica
    }
    
    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
        actualizarPuntosConexion();
    }
    
    public boolean contiene(int px, int py) {
        return px >= x && px <= x + ancho && py >= y && py <= y + alto;
    }
    
    public Point getPuntoConexionSalida() {
        return puntoConexionSalida;
    }
    
    public List<Point> getPuntosConexionEntrada() {
        return puntosConexionEntrada;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    
    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }
    
    public boolean isSeleccionada() {
        return seleccionada;
    }
    
    // Método para evaluar la salida lógica del componente
    public abstract boolean evaluarSalida();
    
    // Método para obtener las conexiones de entrada
    public List<Conexion> getConexionesEntrada() {
        return new ArrayList<>(); // La implementación real se hará en cada subclase
    }

    void agregarConexionSalida(Conexion nuevaConexion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}