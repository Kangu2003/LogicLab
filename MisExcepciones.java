package Logica;

public class MisExcepciones {

    public static class EntradaOcupadaException extends Exception {
        public EntradaOcupadaException(String mensaje) {
            super(mensaje);
        }
    }

    public static class ConexionASiMismoException extends Exception {
        public ConexionASiMismoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class MaxEntradasException extends Exception {
        public MaxEntradasException(String mensaje) {
            super(mensaje);
        }
    }
    
    public static class ConexionIncompletaException extends Exception {
    public ConexionIncompletaException(String mensaje) {
        super(mensaje);
    }
}

}
