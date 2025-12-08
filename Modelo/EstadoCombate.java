package Modelo;

import java.util.Vector;
import java.util.List;

public class EstadoCombate {
    
    private final String descripcionAccion;
    private final List<DatosPersonaje> estadoHeroes;
    private final List<DatosPersonaje> estadoMonstruos;
    private final InventarioSnapshot inventarioSnapshot;
    private final int numeroTurno;
    
    public EstadoCombate(String descripcion, 
                         List<PersonajeJugable> heroes, 
                         List<Monstruo> monstruos,
                         Inventario inventario,
                         int turno) {
        this.descripcionAccion = descripcion;
        this.numeroTurno = turno;
        
        this.estadoHeroes = new Vector<>();
        for (PersonajeJugable h : heroes) {
            this.estadoHeroes.add(new DatosPersonaje(h));
        }
        
        this.estadoMonstruos = new Vector<>();
        for (Monstruo m : monstruos) {
            this.estadoMonstruos.add(new DatosPersonaje(m));
        }
        
        this.inventarioSnapshot = new InventarioSnapshot(inventario);
    }
    
    public String getDescripcionAccion() {
        return descripcionAccion;
    }
    
    public int getNumeroTurno() {
        return numeroTurno;
    }
    
    public List<DatosPersonaje> getEstadoHeroes() {
        return new Vector<>(estadoHeroes);
    }
    
    public List<DatosPersonaje> getEstadoMonstruos() {
        return new Vector<>(estadoMonstruos);
    }
    
    public InventarioSnapshot getInventarioSnapshot() {
        return inventarioSnapshot;
    }
    
    @Override
    public String toString() {
        return "Turno " + numeroTurno + ": " + descripcionAccion;
    }
    
    public static class DatosPersonaje {
        private final String nombre;
        private final int hp;
        private final int hpMaximo;
        private final int mp;
        private final int mpMaximo;
        private final int ataque;
        private final int defensa;
        private final int velocidad;
        private final Estado estado;
        private final boolean esHeroe;
        
      
        private final PoderEspecial poderEspecial;
        private final List<BuffSnapshot> buffs;
        private final InventarioIndividualSnapshot inventarioIndividual; // NUEVO
        
       
        private final TipoMonstruo tipoMonstruo;
        private final EstadoEfectoSnapshot estadoEfecto;
        
        public DatosPersonaje(PersonajeJugable heroe) {
            this.nombre = heroe.getNombre();
            this.hp = heroe.getHP();
            this.hpMaximo = heroe.getHPMaximo();
            this.mp = heroe.getMP();
            this.mpMaximo = heroe.getMPMaximo();
            this.ataque = heroe.getAtaque();
            this.defensa = heroe.getDefensa();
            this.velocidad = heroe.getVelocidad();
            this.estado = heroe.getEstado();
            this.poderEspecial = heroe.getPoderEspecial();
            this.esHeroe = true;
            this.tipoMonstruo = null;
            this.estadoEfecto = null;
            
          
            this.buffs = new Vector<>();
            if (heroe.getBuffs() != null) {
                for (Buff b : heroe.getBuffs()) {
                    this.buffs.add(new BuffSnapshot(b));
                }
            }
            
    
            if (heroe.getInventarioIndividual() != null) {
                this.inventarioIndividual = new InventarioIndividualSnapshot(
                    heroe.getInventarioIndividual()
                );
            } else {
                this.inventarioIndividual = null;
            }
        }
        
        public DatosPersonaje(Monstruo monstruo) {
            this.nombre = monstruo.getNombre();
            this.hp = monstruo.getHP();
            this.hpMaximo = monstruo.getHPMaximo();
            this.mp = 0;
            this.mpMaximo = 0;
            this.ataque = monstruo.getAtaque();
            this.defensa = monstruo.getDefensa();
            this.velocidad = monstruo.getVelocidad();
            this.estado = monstruo.getEstado();
            this.tipoMonstruo = monstruo.getTipoMostruo();
            this.esHeroe = false;
            this.poderEspecial = null;
            this.buffs = null;
            this.inventarioIndividual = null; 
            
         
            if (monstruo.getEstadoEfecto() != null) {
                this.estadoEfecto = new EstadoEfectoSnapshot(monstruo.getEstadoEfecto());
            } else {
                this.estadoEfecto = null;
            }
        }
        
        public String getNombre() { return nombre; }
        public int getHp() { return hp; }
        public int getHpMaximo() { return hpMaximo; }
        public int getMp() { return mp; }
        public int getMpMaximo() { return mpMaximo; }
        public int getAtaque() { return ataque; }
        public int getDefensa() { return defensa; }
        public int getVelocidad() { return velocidad; }
        public Estado getEstado() { return estado; }
        public boolean isEsHeroe() { return esHeroe; }
        public PoderEspecial getPoderEspecial() { return poderEspecial; }
        public List<BuffSnapshot> getBuffs() { return buffs; }
        public TipoMonstruo getTipoMonstruo() { return tipoMonstruo; }
        public EstadoEfectoSnapshot getEstadoEfecto() { return estadoEfecto; }
        public InventarioIndividualSnapshot getInventarioIndividual() { return inventarioIndividual; }
    }
    
    public static class BuffSnapshot {
        private final TipoBuff tipo;
        private final int valor;
        private final int turnos;
        
        public BuffSnapshot(Buff buff) {
            this.tipo = buff.getTipo();
            this.valor = buff.getValor();
            this.turnos = buff.getTurnos();
        }
        
        public Buff toBuff() {
            return new Buff(tipo, valor, turnos);
        }
    }
    
    public static class EstadoEfectoSnapshot {
        private final Estado tipo;
        private final int turnos;
        private final int valorDebilitacion;
        
        public EstadoEfectoSnapshot(EstadoEfecto efecto) {
            this.tipo = efecto.getTipo();
            this.turnos = efecto.getTurnos();
            this.valorDebilitacion = efecto.getValorDebilitacion();
        }
        
        public Estado getTipo() { return tipo; }
        public int getTurnos() { return turnos; }
        public int getValorDebilitacion() { return valorDebilitacion; }
    }
    
    public static class InventarioSnapshot {
        private final java.util.Map<Item, Integer> items;
        
        public InventarioSnapshot(Inventario inventario) {
            this.items = new java.util.HashMap<>();
         
            for (int i = 1; i <= inventario.getTamanio(); i++) {
                Item item = inventario.getItemPorIndice(i);
                if (item != null) {
                    int cantidad = inventario.getCantidad(item);
                    this.items.put(item, cantidad);
                }
            }
        }
        
        public java.util.Map<Item, Integer> getItems() {
            return new java.util.HashMap<>(items);
        }
    }
    
   
    public static class InventarioIndividualSnapshot {
        private final String propietario;
        private final int capacidadMaxima;
        private final java.util.Map<Item, Integer> items;
        
        public InventarioIndividualSnapshot(InventarioIndividual inventario) {
            this.propietario = inventario.getPropietario();
            this.capacidadMaxima = inventario.getCapacidadMaxima();
            this.items = new java.util.HashMap<>(inventario.obtenerItemsMap());
        }
        
        public String getPropietario() { return propietario; }
        public int getCapacidadMaxima() { return capacidadMaxima; }
        public java.util.Map<Item, Integer> getItems() { 
            return new java.util.HashMap<>(items); 
        }
        
     
        public InventarioIndividual restaurar() {
            InventarioIndividual inventario = new InventarioIndividual(
                propietario, capacidadMaxima
            );
            inventario.setItemsMap(items);
            return inventario;
        }
    }
}