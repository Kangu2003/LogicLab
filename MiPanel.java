package Logica;

import Componentes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MiPanel extends JPanel {
    private final List<Componente> componentes = new ArrayList<>();
    private final List<Conexion> conexiones = new ArrayList<>();
    private Componente componenteSeleccionado = null;
    private ConexionTemporal conexionTemporal = null;
    private Componente componentePendiente = null;
    private int offsetX, offsetY;
    private boolean mensajeErrorMostrado = false;

    private final Stack<CircuitoEstado> historial = new Stack<>();
    private final Stack<CircuitoEstado> futuro = new Stack<>();
    
    // Variables para controlar el zoom
    private double zoomFactor = 1.0;
    private final double MIN_ZOOM = 0.5;
    private final double MAX_ZOOM = 3.0;
    private final double ZOOM_INCREMENT = 0.1;
    
    // Variables para controlar el modo panorámico
    private boolean modoPanoramico = false;
    private Point inicioArrastre = null;
    private int panX = 0;
    private int panY = 0;
    private final Cursor CURSOR_AGARRAR = new Cursor(Cursor.HAND_CURSOR);
    private final Cursor CURSOR_ARRASTRANDO = new Cursor(Cursor.MOVE_CURSOR);
    
    // Variable para el menú contextual
    private Componente componenteMenuContextual = null;

    public MiPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEtchedBorder());
        setPreferredSize(new Dimension(800, 600)); 
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Si está en modo panorámico, iniciar el arrastre
                if (modoPanoramico && SwingUtilities.isLeftMouseButton(e)) {
                    inicioArrastre = e.getPoint();
                    setCursor(CURSOR_ARRASTRANDO);
                    return;
                }
                
                if (componentePendiente != null) {
                    Point pointTransformed = transformarPuntoInverso(e.getPoint());
                    componentePendiente.setPosicion((int)pointTransformed.getX(), (int)pointTransformed.getY());
                    agregarComponente(componentePendiente);
                    componentePendiente = null;
                    setCursor(Cursor.getDefaultCursor());
                    repaint();
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    manejarClickIzquierdo(e);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    manejarClickDerecho(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Si está en modo panorámico, finalizar el arrastre
                if (modoPanoramico && SwingUtilities.isLeftMouseButton(e)) {
                    inicioArrastre = null;
                    setCursor(CURSOR_AGARRAR);
                    return;
                }
                
                if (SwingUtilities.isLeftMouseButton(e) && conexionTemporal != null) {
                    boolean conexionExitosa = completarConexion(e);
                    
                    if (!conexionExitosa && !mensajeErrorMostrado) {
                        JOptionPane.showMessageDialog(MiPanel.this, 
                            "Conexión incompleta. Debes soltar sobre una entrada válida.", 
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                    mensajeErrorMostrado = false;
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
                // Si está en modo panorámico, realizar el desplazamiento
                if (modoPanoramico && inicioArrastre != null) {
                    int deltaX = e.getX() - inicioArrastre.x;
                    int deltaY = e.getY() - inicioArrastre.y;
                    
                    panX += deltaX;
                    panY += deltaY;
                    
                    inicioArrastre = e.getPoint();
                    repaint();
                    return;
                }
                
                Point pointTransformed = transformarPuntoInverso(e.getPoint());
                
                if (componenteSeleccionado != null && SwingUtilities.isLeftMouseButton(e)) {
                    componenteSeleccionado.setPosicion((int)pointTransformed.getX() - offsetX, (int)pointTransformed.getY() - offsetY);
                    repaint();
                } else if (conexionTemporal != null) {
                    conexionTemporal.setFin(pointTransformed);
                    repaint();
                }
            }
        });
        
        // rueda del ratón
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotacion = e.getWheelRotation();
                if (rotacion < 0) {
                    aumentarZoom();
                } else {
                    disminuirZoom();
                }
            }
        });
    }
    
    private void manejarClickDerecho(MouseEvent e) {
        // Si estamos en modo panorámico, no mostrar menú contextual
        if (modoPanoramico) {
            return;
        }
        
        Point pointTransformed = transformarPuntoInverso(e.getPoint());
        
        // Buscar si hay un componente en la posición del clic
        Componente componenteEncontrado = null;
        for (Componente c : componentes) {
            if (c.contiene((int)pointTransformed.getX(), (int)pointTransformed.getY())) {
                componenteEncontrado = c;
                break;
            }
        }
        
        if (componenteEncontrado != null) {
            // Mostrar menú contextual para el componente
            mostrarMenuContextual(e, componenteEncontrado);
        } else {
            // Comportamiento original: eliminar conexión
            eliminarConexion(e);
        }
    }
    
    private void mostrarMenuContextual(MouseEvent e, Componente componente) {
        componenteMenuContextual = componente;
        
        JPopupMenu menuContextual = new JPopupMenu();
        
        // Opción para clonar
        JMenuItem clonarItem = new JMenuItem("Clonar");
        clonarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clonarComponente(componenteMenuContextual);
            }
        });
        menuContextual.add(clonarItem);
        
        // Separador
        menuContextual.addSeparator();
        
        // Opción para eliminar
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                eliminarComponente(componenteMenuContextual);
            }
        });
        menuContextual.add(eliminarItem);
        
        // Mostrar el menú en la posición del mouse
        menuContextual.show(this, e.getX(), e.getY());
    }
    
    private void clonarComponente(Componente componente) {
        if (componente == null) return;
        
        try {
            // Usar el patrón Prototype para clonar el componente
            Componente componenteClonado = componente.clonar();
            
            // Posicionar el clon ligeramente desplazado del original
            componenteClonado.setPosicion(
                componente.getX() + 20, 
                componente.getY() + 20
            );
            
            // Guardar estado para deshacer
            historial.push(guardarEstadoActual());
            futuro.clear();
            
            // Agregar el componente clonado
            componentes.add(componenteClonado);
            simularCircuito();
            repaint();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al clonar el componente: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarComponente(Componente componente) {
        if (componente == null) return;
        
        // Guardar estado para deshacer
        historial.push(guardarEstadoActual());
        futuro.clear();
        
        // Eliminar conexiones relacionadas
        eliminarConexionesRelacionadas(componente);
        
        // Eliminar el componente
        componentes.remove(componente);
        
        simularCircuito();
        repaint();
    }
    
    private void eliminarConexion(MouseEvent e) {
        Point pointTransformed = transformarPuntoInverso(e.getPoint());
        
        historial.push(guardarEstadoActual());
        futuro.clear();

        for (int i = conexiones.size() - 1; i >= 0; i--) {
            if (conexiones.get(i).contiene(pointTransformed)) {
                Conexion conexion = conexiones.get(i);
                Componente destino = conexion.getDestino();

                if (destino instanceof Compuerta compuerta) {
                    compuerta.eliminarConexionEntrada(conexion);
                } else if (destino instanceof LED led) {
                    led.eliminarConexionEntrada(conexion);
                }

                conexiones.remove(i);
                simularCircuito();
                repaint();
                return;
            }
        }
    }
    
    //  para controlar el modo panorámico
    public void setModoPanoramico(boolean activo) {
        this.modoPanoramico = activo;
        if (activo) {
            setCursor(CURSOR_AGARRAR);
        } else {
            setCursor(Cursor.getDefaultCursor());
        }

    }
    
    public boolean isModoPanoramico() {
        return modoPanoramico;
    }
    
    public void toggleModoPanoramico() {
        setModoPanoramico(!modoPanoramico);
    }
   
    // Métodos para controlar el zoom
    public void aumentarZoom() {
        if (zoomFactor < MAX_ZOOM) {
            zoomFactor += ZOOM_INCREMENT;
            repaint();
        }
    }

    public void disminuirZoom() {
        if (zoomFactor > MIN_ZOOM) {
            zoomFactor -= ZOOM_INCREMENT;
            repaint();
        }
    }

    public void restablecerZoom() {
        zoomFactor = 1.0;
        repaint();
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    // Métodos auxiliares para transformar coordenadas
    private Point transformarPuntoInverso(Point original) {
        // Transformar el punto de coordenadas de pantalla a coordenadas del modelo
        // considerando el zoom y el desplazamiento panorámico
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double x = centerX + (original.x - centerX - panX) / zoomFactor;
        double y = centerY + (original.y - centerY - panY) / zoomFactor;
        return new Point((int)x, (int)y);
    }

    public String getNombreCircuito() {
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
        if (scrollPane != null) {
            JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, scrollPane);
            if (tabbedPane != null) {
                int index = tabbedPane.indexOfComponent(scrollPane);
                if (index != -1) {
                    return tabbedPane.getTitleAt(index);
                }
            }
        }
        return "Circuito";
    }

    public void setNombreCircuito(String nombre) {
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
        if (scrollPane != null) {
            JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, scrollPane);
            if (tabbedPane != null) {
                int index = tabbedPane.indexOfComponent(scrollPane);
                if (index != -1) {
                    tabbedPane.setTitleAt(index, nombre);
                }
            }
        }
    }

    public void seleccionarCompuerta(Componente c) {
        this.componentePendiente = c;
        setCursor(crearCursorPersonalizado(c));
    }

    private Cursor crearCursorPersonalizado(Componente componente) {
        int width = 64, height = 64;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);
        g2d.setComposite(AlphaComposite.SrcOver);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int offsetX = 12, offsetY = 12;
        componente.setPosicion(offsetX, offsetY);
        componente.Dibujar(g2d);
        g2d.dispose();

        Point hotspot = new Point(offsetX + 5, offsetY + 5);
        return Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, "CursorComponente");
    }

    private void manejarClickIzquierdo(MouseEvent e) {
        Point pointTransformed = transformarPuntoInverso(e.getPoint());
        
        // Si estamos en modo panorámico, no manipular componentes
        if (modoPanoramico) {
            return;
        }
        
        for (Componente c : componentes) {
            if (distancia(pointTransformed, c.getPuntoConexionSalida()) < 10) {
                conexionTemporal = new ConexionTemporal(c, pointTransformed);
                return;
            }
        }

        for (Componente c : componentes) {
            if (c.contiene((int)pointTransformed.getX(), (int)pointTransformed.getY())) {
                componenteSeleccionado = c;
                c.setSeleccionada(true);
                offsetX = (int)pointTransformed.getX() - c.getX();
                offsetY = (int)pointTransformed.getY() - c.getY();
                return;
            }
        }
    }

    private boolean completarConexion(MouseEvent e) {
        try {
            // Si estamos en modo panorámico, no permitir conexiones
            if (modoPanoramico) {
                return false;
            }
            
            Point pointTransformed = transformarPuntoInverso(e.getPoint());
            
            for (Componente c : componentes) {
                for (int i = 0; i < c.getPuntosConexionEntrada().size(); i++) {
                    if (distancia(pointTransformed, c.getPuntosConexionEntrada().get(i)) < 10) {

                        // Conexión a sí mismo
                        if (conexionTemporal.getComponenteInicio() == c) {
                            JOptionPane.showMessageDialog(this, 
                                "No puedes conectar un componente a sí mismo.",  "Error de conexión", JOptionPane.ERROR_MESSAGE);
                            mensajeErrorMostrado = true;
                            return false;  
                        }

                        // Entrada ocupada
                        for (Conexion con : conexiones) {
                            if (con.getDestino() == c && con.getIndiceEntrada() == i) {
                                // marcar que ya se mostró un error
                                JOptionPane.showMessageDialog(this, 
                                    "Esta entrada ya está ocupada.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
                                mensajeErrorMostrado = true;
                                return false;  
                            }
                        }
                        historial.push(guardarEstadoActual());
                        futuro.clear();
                        Conexion nuevaConexion = new Conexion(conexionTemporal.getComponenteInicio(), c, i);

                        if (c instanceof Compuerta) {
                            ((Compuerta) c).agregarConexionEntrada(nuevaConexion);
                        } else if (c instanceof LED) {
                            ((LED) c).agregarConexionEntrada(nuevaConexion);
                        }

                        conexiones.add(nuevaConexion);
                        simularCircuito();
                        repaint();
                        actualizarEstadoConexiones();

                        return true;  // Conectado correctamente
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            mensajeErrorMostrado = true;
            return false;  
        }

        return false;  // No se hizo la conexión 
    }

    private void eliminarComponenteOConexion(MouseEvent e) {
        // Si estamos en modo panorámico, no permitir eliminaciones
        if (modoPanoramico) {
            return;
        }
        
        Point pointTransformed = transformarPuntoInverso(e.getPoint());
        
        historial.push(guardarEstadoActual());
        futuro.clear();

        for (int i = conexiones.size() - 1; i >= 0; i--) {
            if (conexiones.get(i).contiene(pointTransformed)) {
                Conexion conexion = conexiones.get(i);
                Componente destino = conexion.getDestino();

                if (destino instanceof Compuerta compuerta) {
                    compuerta.eliminarConexionEntrada(conexion);
                } else if (destino instanceof LED led) {
                    led.eliminarConexionEntrada(conexion);
                }

                conexiones.remove(i);
                simularCircuito();
                repaint();
                return;
            }
        }

        for (int i = componentes.size() - 1; i >= 0; i--) {
            Componente componente = componentes.get(i);
            if (componente.contiene((int)pointTransformed.getX(), (int)pointTransformed.getY())) {
                eliminarConexionesRelacionadas(componente);
                componentes.remove(i);
                simularCircuito();
                repaint();
                return;
            }
        }
    }

    private void cambiarEstadoComponente(MouseEvent e) {
        // Si estamos en modo panorámico, no cambiar el estado de los componentes
        if (modoPanoramico) {
            return;
        }
        
        Point pointTransformed = transformarPuntoInverso(e.getPoint());
        
        for (Componente c : componentes) {
            if (c instanceof Switch sw && sw.contiene((int)pointTransformed.getX(), (int)pointTransformed.getY())) {
                sw.cambiarEstado();
                actualizarEstadoConexiones();
                simularCircuito();
                repaint();
                return;
            }
        }
    }

    private void actualizarEstadoConexiones() {
        for (Componente c : componentes) {
            if (c instanceof Compuerta || c instanceof Switch) {
                c.evaluarSalida(); // Evaluar la salida de cada compuerta
            }
        }

        for (Conexion conexion : conexiones) {
            conexion.setValor(conexion.getOrigen().evaluarSalida()); // Actualizar el valor de la conexión
        }

        for (Componente c : componentes) {
            if (c instanceof Compuerta || c instanceof LED) {
                c.evaluarSalida(); // Evaluar nuevamente para LEDs
            }
        }
    }

    private void eliminarConexionesRelacionadas(Componente c) {
        conexiones.removeIf(con -> {
            boolean relacionada = con.getOrigen() == c || con.getDestino() == c;
            if (relacionada) {
                if (con.getDestino() instanceof Compuerta compuerta) {
                    compuerta.eliminarConexionEntrada(con);
                } else if (con.getDestino() instanceof LED led) {
                    led.eliminarConexionEntrada(con);
                }
            }
            return relacionada;
        });
    }

    public void limpiarCircuito() {
        componentes.clear();
        conexiones.clear();
        repaint();
    }

    private CircuitoEstado guardarEstadoActual() {
        List<Componente> copiaComponentes = new ArrayList<>();
        List<Conexion> copiaConexiones = new ArrayList<>();

        for (Componente c : componentes) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(c);
                try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
                    copiaComponentes.add((Componente) in.readObject());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        copiaConexiones.addAll(conexiones);
        return new CircuitoEstado(copiaComponentes, copiaConexiones);
    }

    private void restaurarEstado(CircuitoEstado estado) {
        componentes.clear();
        conexiones.clear();
        componentes.addAll(estado.componentes);
        conexiones.addAll(estado.conexiones);
    }

    private static class CircuitoEstado implements Serializable {
        List<Componente> componentes;
        List<Conexion> conexiones;

        CircuitoEstado(List<Componente> comp, List<Conexion> con) {
            this.componentes = comp;
            this.conexiones = con;
        }
    }

    public void deshacer() {
        if (!historial.isEmpty()) {
            CircuitoEstado estadoAnterior = historial.pop();
            futuro.push(guardarEstadoActual());
            restaurarEstado(estadoAnterior);
            simularCircuito();
            repaint();
        }
    }

    public void rehacer() {
        if (!futuro.isEmpty()) {
            CircuitoEstado estadoRecuperado = futuro.pop();
            historial.push(guardarEstadoActual());
            restaurarEstado(estadoRecuperado);
            simularCircuito();
            repaint();
        }
    }

    private double distancia(Point a, Point b) {
        return a.distance(b);
    }

    public void agregarComponente(Componente c) {
        historial.push(guardarEstadoActual());
        futuro.clear();
        componentes.add(c);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        // Guardamos la transformación original
        AffineTransform transformOriginal = g2d.getTransform();
        
        // Aplicamos el zoom y el desplazamiento panorámico desde el centro del panel
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g2d.translate(centerX + panX, centerY + panY);
        g2d.scale(zoomFactor, zoomFactor);
        g2d.translate(-centerX, -centerY);
        
        // Dibujamos una cuadrícula de fondo cuando hay zoom
        if (zoomFactor > 1.0) {
            dibujarCuadricula(g2d);
        }
        
        // Dibujamos los componentes con el zoom y panning aplicados
        for (Conexion c : conexiones) c.Dibujar(g);
        if (conexionTemporal != null) conexionTemporal.Dibujar(g);
        for (Componente c : componentes) c.Dibujar(g);
        
        // Restauramos la transformación original
        g2d.setTransform(transformOriginal);
        
        // Dibujamos indicadores de navegación si estamos en modo panorámico o con zoom
        if (modoPanoramico || zoomFactor != 1.0) {
            dibujarIndicadoresNavegacion(g2d);
        }
    }
    
    //  dibujar una cuadrícula de fondo
    private void dibujarCuadricula(Graphics2D g2d) {
        g2d.setColor(new Color(230, 230, 230)); // Gris claro
        int tamañoCuadricula = 20; // Tamaño de la cuadrícula
        
        // Calcular los límites basados en el tamaño del panel y el zoom
        int ancho = getWidth();
        int alto = getHeight();
        int inicioX = -ancho;
        int finX = ancho * 2;
        int inicioY = -alto;
        int finY = alto * 2;
        
        // Dibujar líneas horizontales
        for (int y = inicioY; y <= finY; y += tamañoCuadricula) {
            g2d.drawLine(inicioX, y, finX, y);
        }
        
        // Dibujar líneas verticales
        for (int x = inicioX; x <= finX; x += tamañoCuadricula) {
            g2d.drawLine(x, inicioY, x, finY);
        }
    }
    
    //  dibujar indicador de navegacion
    private void dibujarIndicadoresNavegacion(Graphics2D g2d) {
        int ancho = getWidth();
        int alto = getHeight();
        int margen = 10;
        
        // Dibujar un indicador de zoom
        g2d.setColor(new Color(0, 0, 0, 128)); // Negro semi-transparente
        g2d.fillRoundRect(margen, alto - 40 - margen, 120, 40, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
  
        String modoTexto = modoPanoramico ? "MODO PANORÁMICO" : "MODO NORMAL";
        g2d.drawString(modoTexto, margen + 10, alto - 25 - margen);
        g2d.drawString("Zoom: " + String.format("%.1f", zoomFactor) + "x", margen + 10, alto - 10 - margen);
        
    }

    public void guardarCircuito(File archivo) {
        if (componentes.isEmpty() && conexiones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay nada que guardar. El circuito está vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(componentes);
            out.writeObject(conexiones);
            JOptionPane.showMessageDialog(this, "Circuito guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void cargarCircuito(File archivo) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            componentes.clear();
            conexiones.clear();

            List<Componente> cargados = (List<Componente>) in.readObject();
            List<Conexion> conexionesCargadas = (List<Conexion>) in.readObject();

            componentes.addAll(cargados);
            conexiones.addAll(conexionesCargadas);

            simularCircuito();
            repaint();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void simularCircuito() {
        for (Componente c : componentes) {
            if (c instanceof Switch || c instanceof Compuerta) {
                boolean salida = c.evaluarSalida();
                for (Conexion con : conexiones) {
                    if (con.getOrigen() == c) {
                        con.setValor(salida);
                    }
                }
            }
        }

        for (int i = 0; i < 3; i++) { 
            for (Componente c : componentes) {
                if (c instanceof Compuerta || c instanceof LED) {
                    c.evaluarSalida();
                    for (Conexion con : conexiones) {
                        if (con.getOrigen() == c) {
                            con.setValor(c.evaluarSalida());
                        }
                    }
                }
            }
        }
        for (Componente c : componentes) {
            if (c instanceof LED led) {
                led.evaluarSalida();
            }
        }
        repaint();
    }
}