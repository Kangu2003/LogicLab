package Pantron_Factory;

import Pantron_Factory.TipoComponente;
import Componentes.*;
import Compuertas.*;
import Logica.Componente;

public class ComponenteFactory {

    public static Componente crearComponente(TipoComponente tipo, int x, int y) {
        return switch (tipo) {
            case AND -> new AND(x, y);
            case OR -> new OR(x, y);
            case NOT -> new NOT(x, y);
            case Xor -> new Xor(x, y);
            case XNOR -> new XNOR(x, y);
            case NAND -> new NAND(x, y);
            case NOR -> new NOR(x, y);
            case SWITCH -> new Switch(x, y);
            case LED -> new LED(x, y);
        };
    }
}

