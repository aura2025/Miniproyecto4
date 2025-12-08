package Modelo;

public class Buff {
    private final TipoBuff tipo;
    private final int valor;
    private int turnos;

    public Buff(TipoBuff tipo, int valor, int turnos) {
        this.tipo = tipo;
        this.valor = valor;
        this.turnos = turnos;
    }

    public TipoBuff getTipo() { return tipo; }
    public int getValor() { return valor; }
    public int getTurnos() { return turnos; }

    public void reducirTurno() { if (turnos>0) turnos--; }
    public boolean haExpirado() { return turnos <= 0; }
}
