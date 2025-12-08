package Modelo.exceptions;

public class ExcepcionEntidadMuerta extends RuntimeException {
    public ExcepcionEntidadMuerta() { super(); }
    public ExcepcionEntidadMuerta(String message) { super(message); }
}

