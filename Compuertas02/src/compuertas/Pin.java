package compuertas;

import java.awt.*;

public class Pin {
    private int x, y;
    private boolean conectado;
    private Pin conexion;

    public Pin(int x, int y) {
        this.x = x;
        this.y = y;
        this.conectado = false;
        this.conexion = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean estaConectado() { return conectado; }
    public Pin getConexion() { return conexion; }

    public void conectar(Pin otro) {
        this.conexion = otro;
        this.conectado = true;
        otro.conexion = this;
        otro.conectado = true;
    }

    public void desconectar() {
        if (conexion != null) {
            conexion.conexion = null;
            conexion.conectado = false;
        }
        this.conexion = null;
        this.conectado = false;
    }

    public void dibujar(Graphics g) {
        g.setColor(conectado ? Color.GREEN : Color.RED);
        g.fillOval(x - 3, y - 3, 6, 6);
    }
}
