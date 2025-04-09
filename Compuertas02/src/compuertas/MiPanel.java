package compuertas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MiPanel extends JPanel {
    private final List<Componente> componentes = new ArrayList<>();
    private final List<Conexion> conexiones = new ArrayList<>();
    private Componente componenteSeleccionado = null;
    private ConexionTemporal conexionTemporal = null;
    private Componente componentePendiente = null;
    private int offsetX, offsetY;

    public MiPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEtchedBorder());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (componentePendiente != null) {
                    componentePendiente.setPosicion(e.getX(), e.getY());
                    agregarComponente(componentePendiente);
                    componentePendiente = null;
                    setCursor(Cursor.getDefaultCursor()); // Restaurar cursor normal
                    repaint();
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    manejarClickIzquierdo(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    eliminarComponenteOConexion(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && conexionTemporal != null) {
                    completarConexion(e);
                }
                conexionTemporal = null;
                componenteSeleccionado = null;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cambiarEstadoComponente(e);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (componenteSeleccionado != null && SwingUtilities.isLeftMouseButton(e)) {
                    componenteSeleccionado.setPosicion(e.getX() - offsetX, e.getY() - offsetY);
                    repaint();
                } else if (conexionTemporal != null) {
                    conexionTemporal.setFin(e.getPoint());
                    repaint();
                }
            }
        });
    }

    public void seleccionarCompuerta(Componente c) {
        this.componentePendiente = c;
        setCursor(crearCursorPersonalizado(c));
    }

    private Cursor crearCursorPersonalizado(Componente componente) {
    int width = 64, height = 64;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();

    // Fondo transparente
    g2d.setComposite(AlphaComposite.Clear);
    g2d.fillRect(0, 0, width, height);
    g2d.setComposite(AlphaComposite.SrcOver);

    // Habilitar antialiasing
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    // Posición temporal centrada en la miniatura
    int offsetX = 12, offsetY = 12;
    componente.setPosicion(offsetX, offsetY);

    // Dibujar el componente en la miniatura
    componente.Dibujar(g2d);
    g2d.dispose();

    // Punto activo del cursor: donde se hará clic
    Point hotspot = new Point(offsetX + 5, offsetY + 5);

    return Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, "CursorComponente");
}


    private void manejarClickIzquierdo(MouseEvent e) {
        for (Componente c : componentes) {
            if (distancia(e.getPoint(), c.getPuntoConexionSalida()) < 10) {
                conexionTemporal = new ConexionTemporal(c, e.getPoint());
                return;
            }
        }

        for (Componente c : componentes) {
            if (c.contiene(e.getX(), e.getY())) {
                componenteSeleccionado = c;
                c.setSeleccionada(true);
                offsetX = e.getX() - c.getX();
                offsetY = e.getY() - c.getY();
                return;
            }
        }
    }

    private void completarConexion(MouseEvent e) {
        for (Componente c : componentes) {
            for (int i = 0; i < c.getPuntosConexionEntrada().size(); i++) {
                if (distancia(e.getPoint(), c.getPuntosConexionEntrada().get(i)) < 10) {
    // Verificar si ya hay una conexión en esa entrada
    for (Conexion con : conexiones) {
        if (con.getDestino() == c && con.getIndiceEntrada() == i) {
            // Entrada ya ocupada, no hacer nada
            return;
        }
    }

    Conexion nuevaConexion = new Conexion(conexionTemporal.getComponenteInicio(), c, i);

    if (c instanceof Compuerta) {
        ((Compuerta)c).agregarConexionEntrada(nuevaConexion);
    } else if (c instanceof LED) {
        ((LED)c).agregarConexionEntrada(nuevaConexion);
    }

    conexiones.add(nuevaConexion);
    repaint();
    actualizarEstadoConexiones();
    return;
}

            }
        }
    }

    private void eliminarComponenteOConexion(MouseEvent e) {
        for (int i = conexiones.size() - 1; i >= 0; i--) {
            if (conexiones.get(i).contiene(e.getPoint())) {
                Componente destino = conexiones.get(i).getDestino();
                if (destino instanceof Compuerta) {
                    ((Compuerta)destino).eliminarConexionEntrada(conexiones.get(i));
                } else if (destino instanceof LED) {
                    ((LED)destino).eliminarConexionEntrada(conexiones.get(i));
                }
                conexiones.remove(i);
                repaint();
                
                return;
            }
        }

        for (int i = componentes.size() - 1; i >= 0; i--) {
            if (componentes.get(i).contiene(e.getX(), e.getY())) {
                eliminarConexionesRelacionadas(componentes.get(i));
                componentes.remove(i);
                repaint();
                return;
            }
        }
    }

    private void cambiarEstadoComponente(MouseEvent e) {
        for (Componente c : componentes) {
            if (c instanceof Switch && c.contiene(e.getX(), e.getY())) {
                ((Switch) c).cambiarEstado();
                actualizarEstadoConexiones();
                repaint();
                return;
            }
        }
    }

   private void actualizarEstadoConexiones() {
    // Evaluar salidas
    for (Componente c : componentes) {
        if (c instanceof Compuerta || c instanceof Switch) {
            c.evaluarSalida();
        }
    }

    // Asignar valores a conexiones
    for (Conexion conexion : conexiones) {
        conexion.setValor(conexion.getOrigen().evaluarSalida());
    }

    // Re-evaluar compuertas y LEDs
    for (Componente c : componentes) {
        if (c instanceof Compuerta || c instanceof LED) {
            c.evaluarSalida();
        }
    }
}


    private void eliminarConexionesRelacionadas(Componente c) {
        conexiones.removeIf(con -> con.getOrigen() == c || con.getDestino() == c);
    }

    private double distancia(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public void agregarComponente(Componente c) {
        componentes.add(c);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Conexion c : conexiones) {
            c.Dibujar(g);
        }
        if (conexionTemporal != null) {
            conexionTemporal.Dibujar(g);
        }
        for (Componente c : componentes) {
            c.Dibujar(g);
        }
    }

    void seleccionarComponente(LED led) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
