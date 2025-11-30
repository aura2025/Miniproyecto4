package Modelo;


public interface Personaje {
    public int getHP();
    public void setHP(int hp);
    public int getDefensa();
    public void setDefensa(int defensa);
    public int getAtaque();
    public void setAtaque(int ataque);
    public int getVelocidad();
    public void setVelocidad(int velocidad);
    public Estado getEstado();
    public void setEstado(Estado estado);
    public String getNombre();
    public void setNombre(String nombre);
    public TipoPersonaje getTipoPersonaje();
    public void SetTipoPersonaje(TipoPersonaje tipoPersonaje);
    void tomarTurno(Personaje objetivo, int opcion);
    void atacar(Personaje objetivo);
    void defender();
    void usarItem(Item item, Personaje objetivo);
    int getHPMaximo();
    int getMPMaximo();
}