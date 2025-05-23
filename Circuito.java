package Logica;

import Componentes.Switch;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa un circuito lógico que puede generar su tabla de verdad.
 */
public class Circuito {
    // Lista de switches (entradas) del circuito
    private List<Switch> switches;

    // Objeto que representa la salida principal del circuito (por ejemplo, el LED o la última compuerta)
    private ComponenteSalida salida;

    public Circuito(List<Switch> switches, ComponenteSalida salida) {
        this.switches = switches;
        this.salida = salida;
    }

    /**
     * Genera la tabla de verdad como un String.
     * Cada fila muestra las entradas y la salida evaluada.
     * @return La tabla de verdad en formato de texto.
     */
    public String generarTablaVerdad() {
        StringBuilder tabla = new StringBuilder();

        int n = switches.size();
        int filas = (int) Math.pow(2, n);

        // Crear encabezados
        for (Switch s : switches) {
            tabla.append(s.getNombre()).append("\t");
        }
        tabla.append("| Salida\n");
        tabla.append("-".repeat(n * 8 + 10)).append("\n");

        // Generar cada combinación
        for (int i = 0; i < filas; i++) {
            // Asignar valores binarios a los switches
            for (int j = 0; j < n; j++) {
                int valor = (i >> (n - j - 1)) & 1;  // bit j de i
                switches.get(j).setEstado(valor == 1);
            }

            // Evaluar la salida del circuito
            boolean salidaEvaluada = salida.evaluar();

            // Agregar fila a la tabla: entradas y salida
            for (Switch s : switches) {
                tabla.append(s.getEstado() ? "1" : "0").append("\t");
            }
            tabla.append("| ").append(salidaEvaluada ? "1" : "0").append("\n");
        }

        return tabla.toString();
    }
}

