package Modelo.exceptions;

public class ExcepcionItemInvalido extends RuntimeException {
    public ExcepcionItemInvalido() { super(); }
    public ExcepcionItemInvalido(String message) { super(message); }
}
