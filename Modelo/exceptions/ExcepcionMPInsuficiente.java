package Modelo.exceptions;

public class ExcepcionMPInsuficiente extends RuntimeException {
    public ExcepcionMPInsuficiente() { super(); }
    public ExcepcionMPInsuficiente(String message) { super(message); }
}
