package Modelo;

import Modelo.*;
import java.util.Stack;
import java.util.List;

public class GestorDeshacerRehacer {
    
    private final Stack<EstadoCombate> pilaDeshacer;
    
    private final Stack<EstadoCombate> pilaRehacer;
    
    private final int LIMITE_HISTORIAL = 10;
    
    public GestorDeshacerRehacer() {
        this.pilaDeshacer = new Stack<>();
        this.pilaRehacer = new Stack<>();
    }
    
    public void guardarEstado(String descripcion,
                              List<PersonajeJugable> heroes,
                              List<Monstruo> monstruos,
                              Inventario inventario,
                              int turno) {
        
        EstadoCombate estado = new EstadoCombate(
            descripcion, heroes, monstruos, inventario, turno
        );
        
        pilaDeshacer.push(estado);
        
        pilaRehacer.clear();
        
        if (pilaDeshacer.size() > LIMITE_HISTORIAL) {
            pilaDeshacer.remove(0);
        }
    }
    
    public EstadoCombate deshacer() {
        if (!puedoDeshacer()) {
            return null;
        }
        
        EstadoCombate estadoADeshacer = pilaDeshacer.pop();
        
        pilaRehacer.push(estadoADeshacer);
        
        if (!pilaDeshacer.isEmpty()) {
            EstadoCombate estadoAnterior = pilaDeshacer.peek();
            return estadoAnterior; 
        }
        
        return null;
    }
    
    public EstadoCombate rehacer() {
        if (!puedoRehacer()) {
            return null;
        }
        
        EstadoCombate estadoRehecho = pilaRehacer.pop();
        
        pilaDeshacer.push(estadoRehecho);
        
        return estadoRehecho;
    }
    
    public boolean puedoDeshacer() {
        return pilaDeshacer.size() >= 1;
    }
    
    public boolean puedoRehacer() {
        return !pilaRehacer.isEmpty();
    }
    
    public java.util.List<String> obtenerHistorialDeshacer(int cantidad) {
        java.util.List<String> historial = new java.util.Vector<>();
        
        int limite = Math.min(cantidad, pilaDeshacer.size());
        
        for (int i = pilaDeshacer.size() - 1; i >= pilaDeshacer.size() - limite && i >= 0; i--) {
            historial.add(pilaDeshacer.get(i).toString());
        }
        
        return historial;
    }
    
    public java.util.List<String> obtenerHistorialRehacer(int cantidad) {
        java.util.List<String> historial = new java.util.Vector<>();
        
        int limite = Math.min(cantidad, pilaRehacer.size());
        
        for (int i = pilaRehacer.size() - 1; i >= pilaRehacer.size() - limite && i >= 0; i--) {
            historial.add(pilaRehacer.get(i).toString());
        }
        
        return historial;
    }
    
    public void limpiarHistorial() {
        pilaDeshacer.clear();
        pilaRehacer.clear();
    }
    
    public String obtenerEstadisticas() {
        return String.format(
            "Historial: %d acciones guardadas | %d acciones para rehacer | Límite: %d",
            pilaDeshacer.size(),
            pilaRehacer.size(),
            LIMITE_HISTORIAL
        );
    }
    
    public void restaurarEstado(EstadoCombate estado,
                                List<PersonajeJugable> heroes,
                                List<Monstruo> monstruos,
                                Inventario inventario) {
        
        if (estado == null) return;
        
        List<EstadoCombate.DatosPersonaje> datosHeroes = estado.getEstadoHeroes();
        for (int i = 0; i < heroes.size() && i < datosHeroes.size(); i++) {
            restaurarHeroe(heroes.get(i), datosHeroes.get(i));
        }
        
        List<EstadoCombate.DatosPersonaje> datosMonstruos = estado.getEstadoMonstruos();
        for (int i = 0; i < monstruos.size() && i < datosMonstruos.size(); i++) {
            restaurarMonstruo(monstruos.get(i), datosMonstruos.get(i));
        }
        
        restaurarInventario(inventario, estado.getInventarioSnapshot());
    }
    
    private void restaurarHeroe(PersonajeJugable heroe, EstadoCombate.DatosPersonaje datos) {
        heroe.setHP(datos.getHp());
        heroe.setMP(datos.getMp());
        heroe.setAtaque(datos.getAtaque());
        heroe.setDefensa(datos.getDefensa());
        heroe.setVelocidad(datos.getVelocidad());
        heroe.setEstado(datos.getEstado());
        
        if (datos.getBuffs() != null) {
            java.util.List<Buff> buffsRestaurados = new java.util.Vector<>();
            for (EstadoCombate.BuffSnapshot bs : datos.getBuffs()) {
                buffsRestaurados.add(bs.toBuff());
            }
            heroe.setBuffs(buffsRestaurados);
        } else {
            heroe.setBuffs(new java.util.Vector<>());
        }

        if (datos.getInventarioIndividual() != null) {
            InventarioIndividual inventarioRestaurado = datos.getInventarioIndividual().restaurar();
            heroe.setInventarioIndividual(inventarioRestaurado);
        } else {
            heroe.setInventarioIndividual(new InventarioIndividual(heroe.getNombre(), 5));
        }
}
    
    private void restaurarMonstruo(Monstruo monstruo, EstadoCombate.DatosPersonaje datos) {
        monstruo.setHP(datos.getHp());
        monstruo.setAtaque(datos.getAtaque());
        monstruo.setDefensa(datos.getDefensa());
        monstruo.setVelocidad(datos.getVelocidad());
        monstruo.setEstado(datos.getEstado());
        
        if (datos.getEstadoEfecto() != null) {
            EstadoCombate.EstadoEfectoSnapshot ef = datos.getEstadoEfecto();
            if (ef.getTipo() == Estado.DEBILITADO) {
                monstruo.setEstadoConDebilitacion(ef.getValorDebilitacion(), ef.getTurnos());
            } else {
                monstruo.setEstadoConDuracion(ef.getTipo(), ef.getTurnos());
            }
        }
    }
    
    private void restaurarInventario(Inventario inventario, EstadoCombate.InventarioSnapshot snapshot) {
 
        java.util.Map<Item, Integer> itemsSnapshot = snapshot.getItems();
        
        for (Item item : Item.values()) {
            int cantidadActual = inventario.getCantidad(item);
            int cantidadDeseada = itemsSnapshot.getOrDefault(item, 0);
            
            int diferencia = cantidadDeseada - cantidadActual;
            
            if (diferencia > 0) {
                inventario.agregarItem(item, diferencia);
            } else if (diferencia < 0) {
                for (int i = 0; i < Math.abs(diferencia); i++) {
                    try {
                        inventario.usarItem(item);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}