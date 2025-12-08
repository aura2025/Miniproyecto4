package Modelo;

public enum Item {
    ANTIDOTO(ItemType.CURACION_ESTADO, "Antídoto", 0),
    ESTIMULANTE(ItemType.CURACION_ESTADO, "Estimulante", 0),
    PANACEA(ItemType.CURACION_ESTADO, "Panacea", 0),
    POCION_FUERZA(ItemType.BUFF, "Poción de fuerza", 20),
    POCION_DEFENSA(ItemType.BUFF, "Poción de defensa", 20),
    POCION_VELOCIDAD(ItemType.BUFF, "Poción de velocidad", 15),
    HIERBA(ItemType.CURACION, "Hierba curativa", 50),
    MEGA_HIERBA(ItemType.CURACION, "Mega Hierba", 200),
    RECUP_MP(ItemType.RECUPERACION_MP, "Recuperador de MP", 30);

    public enum ItemType { CURACION, RECUPERACION_MP, CURACION_ESTADO, BUFF }

    private final ItemType tipo;
    private final String nombre;
    private final int valor;

    Item(ItemType tipo, String nombre, int valor) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.valor = valor;
    }

    public ItemType getTipo() { return tipo; }
    public String getNombre() { return nombre; }
    public int getValor() { return valor; }
    public String getDescripcion() {
        String tipoStr = tipo == ItemType.CURACION ? "Curación" :
                tipo == ItemType.RECUPERACION_MP ? "Recupera MP" :
                tipo == ItemType.CURACION_ESTADO ? "Curación de estado" :
                "Buff";
        return nombre + " (" + tipoStr + ", valor: " + valor + ")";
    }
}
