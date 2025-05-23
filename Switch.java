package Componentes;

import Logica.Componente;
import Logica.Conexion;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class Switch extends Componente {
    
    private boolean activado;
    private final List<Conexion> conexionesSalida;

    public Switch(int x, int y) {
        super(x, y, 40, 20); // Tamaño ajustado
        this.activado = false;
        this.conexionesSalida = new ArrayList<>();
        actualizarPuntosConexion();
    }

    @Override
    public void actualizarPuntosConexion() {
        puntosConexionEntrada.clear(); // Switch no tiene entradas
        puntoConexionSalida.setLocation(x + ancho, y + alto / 2); // Punto de salida
    }

    @Override
    public void Dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo del switch
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(x, y, ancho, alto, 15, 15);

        // Dibujar círculo del switch
        g2.setColor(activado ? Color.GREEN : Color.RED);
        int posX = activado ? x + ancho - 18 : x + 2;
        g2.fillOval(posX, y + 2, 16, 16);

        // Borde del switch
        g2.setColor(seleccionada ? Color.RED : Color.DARK_GRAY);
        g2.drawRoundRect(x, y, ancho, alto, 15, 15);
        
        // Dibujar punto de conexión de salida
        g2.setColor(Color.red);
        g2.fillOval(puntoConexionSalida.x - 3, puntoConexionSalida.y - 3, 6, 6);

        g2.dispose();
    }
    

    public void cambiarEstado() {
        activado = !activado;
    }

    public boolean estaActivado() {
        return activado;
    }
    
    @Override
    public boolean evaluarSalida() {
        return activado;
    }
    
    @Override
    public List<Conexion> getConexionesEntrada() {
        return new ArrayList<>(); // Switch no tiene conexiones de entrada
    }
    
    public List<Conexion> getConexionesSalida() {
        return conexionesSalida;
    }
    
   
    public void agregarConexionSalida(Conexion conexion) {
        conexionesSalida.add(conexion);
    }
    
    public void eliminarConexionSalida(Conexion conexion) {
        conexionesSalida.remove(conexion);
    }

    public boolean isEncendido() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setEncendido(boolean encendido) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
public Componente clonar() {
    Switch clon = new Switch(this.x, this.y);
    clon.setSeleccionada(this.seleccionada);
    clon.activado = this.activado;
    return clon;
}

}