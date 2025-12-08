package Modelo.exceptions;

public class ExcepcionInventarioVacio extends RuntimeException {
    public ExcepcionInventarioVacio() { super(); }
    public ExcepcionInventarioVacio(String message) { super(message); }
}


