package compuertas;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public abstract class Compuerta extends Componente {
    // Lista de conexiones de entrada
    protected List<Conexion> conexionesEntrada;
    
    public Compuerta(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.conexionesEntrada = new ArrayList<>();
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
    public List<Conexion> getEntradas() {
        List<Conexion> entradas = null;
    return entradas;
}

}