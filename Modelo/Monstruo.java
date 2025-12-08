package Modelo;

public class Monstruo implements Personaje {
    private int hp;
    private int hpMaximo;
    private int defensa;
    private int defensaOriginal; 
    private int ataque;
    private int velocidad;
    private Estado estado;
    private EstadoEfecto estadoEfecto;
    private String nombre;
    private TipoMonstruo tipoMonstruo;
    private TipoPersonaje tipoPersonaje;
    private boolean estaDefendiendo;

    public Monstruo(TipoPersonaje tipoPersonaje, String nombre, int hp, int defensa, 
                    int ataque, int velocidad, Estado estado, TipoMonstruo tipoMonstruo) {
        this.hp = hp;
        this.hpMaximo = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.defensaOriginal = defensa; 
        this.velocidad = velocidad;
        this.estado = estado;
        this.estadoEfecto = null;
        this.tipoMonstruo = tipoMonstruo;
        this.nombre = nombre;
        this.tipoPersonaje = tipoPersonaje;
        this.estaDefendiendo = false;
        
    }

    @Override
    public int getHP() {
        return hp;
    }
    
    @Override
    public int getDefensa() {
        return defensa;
    }
    
    @Override
    public int getAtaque() {
        return ataque;
    }
    
    @Override
    public int getVelocidad() {
        return velocidad;
    }
   
    @Override
    public Estado getEstado() {
        return estado;
    }

    @Override
    public void setHP(int hp) {
        this.hp = Math.max(0, Math.min(hp, this.hpMaximo));
    }
    
    @Override
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    @Override
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    
    @Override
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    
    @Override
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public void setTipoMostruo(TipoMonstruo tipoMostruo) {
        this.tipoMonstruo = tipoMostruo;
    }
    
    public TipoMonstruo getTipoMostruo() {
        return tipoMonstruo;
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public TipoPersonaje getTipoPersonaje() {
        return tipoPersonaje;
    }

    @Override
    public void SetTipoPersonaje(TipoPersonaje tipoPersonaje) {
        this.tipoPersonaje = tipoPersonaje;
    }
    
    @Override
    public void tomarTurno(Personaje objetivo, int opcion) {
        if (hp <= 0) return;
    
        if (this.estado == Estado.ENVENENADO) {
            int danioPorVeneno = 15;
            setHP(this.hp - danioPorVeneno);
            
        }
       
        if (this.estado == Estado.ATURDIDO) {
            actualizarEstados();
            return;
        }
        if (this.estado == Estado.CONGELADO) {
            actualizarEstados();
            return;
        }
           this.defensa = (int)(this.defensa * 1.8);
        if (estaDefendiendo) {
            this.defensa = defensaOriginal;
            estaDefendiendo = false;
        }

        if (debeDefender()) {
            defender();
        } else {
            atacar(objetivo);
        }

     
        actualizarEstados();
    }

    private boolean debeDefender() {
        if (tipoMonstruo == TipoMonstruo.DEFENSIVO && hp < 100) {
            return Math.random() < 0.4;
        }
        if (tipoMonstruo == TipoMonstruo.BALANCEADO && hp < 80) {
            return Math.random() < 0.2;
        }
        if (tipoMonstruo == TipoMonstruo.AGRESIVO) {
            return Math.random() < 0.05;
        }
        return false;
    }

    @Override
    public void atacar(Personaje objetivo) {
        int ataqueFinal;
        int danio;

        switch (tipoMonstruo) {
            case AGRESIVO:
                ataqueFinal = (int)(this.ataque * 1.20);
                danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
                objetivo.setHP(objetivo.getHP() - danio);
                break;

            case DEFENSIVO:
                ataqueFinal = this.ataque;
                danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
                objetivo.setHP(objetivo.getHP() - danio);
                objetivo.setDefensa(Math.max(objetivo.getDefensa() - 5, 0));
                break;

            case BALANCEADO:
                ataqueFinal = this.ataque;
                danio = Math.max(ataqueFinal - objetivo.getDefensa(), 0);
                objetivo.setHP(objetivo.getHP() - danio);
                objetivo.setVelocidad(Math.max(objetivo.getVelocidad() - 5, 0));
                break;
        }
    }

    @Override
    public void defender() {
        defensaOriginal = this.defensa;
        switch (tipoMonstruo) {
            case DEFENSIVO:
                this.defensa = (int)(this.defensa * 1.8 );
                break;
            case BALANCEADO:
                this.defensa = (int)(this.defensa * 1.5);
                break;
            case AGRESIVO:
                this.defensa = (int)(this.defensa * 1.3);
                break;
        }
        
    estaDefendiendo = true;
   
    }

   
    public void setEstadoConDuracion(Estado estado, int turnos) {
        this.estado = estado == null ? Estado.NORMAL : estado;
        if (this.estado == Estado.NORMAL) this.estadoEfecto = null;
        else this.estadoEfecto = new EstadoEfecto(this.estado, Math.max(0, turnos));
    }

    public void setEstadoConDebilitacion(int valorDebilitacion, int turnos) {
        this.estado = Estado.DEBILITADO;
        this.estadoEfecto = new EstadoEfecto(Estado.DEBILITADO, Math.max(0, turnos), valorDebilitacion);
    }

    public EstadoEfecto getEstadoEfecto() { return estadoEfecto; }

    public void actualizarEstados() {
        if (estadoEfecto != null) {
          
            estadoEfecto.reducirTurno();
            if (estadoEfecto.haExpirado()) {
                Estado prev = estadoEfecto.getTipo();
                if (prev == Estado.DEBILITADO) {
                    this.ataque += estadoEfecto.getValorDebilitacion();
                   
                }
                this.estado = Estado.NORMAL;
                this.estadoEfecto = null;
            }
        }
    }

    @Override
    public void usarItem(Item item, Personaje objetivo) {
    
    }

    @Override
    public int getHPMaximo() {
        return hpMaximo;
    }

    @Override
    public int getMPMaximo() {
        return 0;
    }
}
