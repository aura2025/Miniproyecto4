package Modelo;

public class EstadoEfecto {
    private final Estado tipo;
    private int turnos;
    private final int valorDebilitacion;

    public EstadoEfecto(Estado tipo, int turnos) {
        this(tipo, turnos, 0);
    }

    public EstadoEfecto(Estado tipo, int turnos, int valorDebilitacion) {
        this.tipo = tipo;
        this.turnos = turnos;
        this.valorDebilitacion = valorDebilitacion;
    }

    public Estado getTipo() { return tipo; }
    public int getTurnos() { return turnos; }
    public int getValorDebilitacion() { return valorDebilitacion; }

    public void reducirTurno() { if (turnos>0) turnos--; }
    public boolean haExpirado() { return turnos <= 0; }
}
