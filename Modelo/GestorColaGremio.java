package Modelo;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Sistema de turnos para atención en el gremio.
 * Utiliza estructura FIFO (Queue) donde el primero en llegar es el primero en ser atendido.
 * Sistema independiente del combate.
 */
public class GestorColaGremio {
    
    private final Queue<PersonajeJugable> colaGremio;
    
    public GestorColaGremio() {
        this.colaGremio = new LinkedList<>();
    }
    
 
    public void agregarAventurero(PersonajeJugable aventurero) {
        if (aventurero != null) {
            colaGremio.offer(aventurero);
        }
    }
    
   
    public PersonajeJugable atenderSiguiente() {
        return colaGremio.poll();
    }
    
    
    public PersonajeJugable verSiguiente() {
        return colaGremio.peek();
    }
    
   
    public boolean hayAventureros() {
        return !colaGremio.isEmpty();
    }
    
   
    
     
    public int cantidadEnCola() {
        return colaGremio.size();
    }
    
    
   
    public List<PersonajeJugable> obtenerFilaActual() {
        return new Vector<>(colaGremio);
    }
    
    
    public void limpiarCola() {
        colaGremio.clear();
    }
    
    
    public String mostrarCola() {
        if (colaGremio.isEmpty()) {
            return "La cola del gremio está vacía.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Cola del Gremio ===\n");
        sb.append("Aventureros en espera: ").append(colaGremio.size()).append("\n\n");
        
        int posicion = 1;
        for (PersonajeJugable aventurero : colaGremio) {
            sb.append(posicion).append(". ");
            sb.append(aventurero.getNombre());
            sb.append(" (HP: ").append(aventurero.getHP());
            sb.append("/").append(aventurero.getHPMaximo());
            sb.append(", Poder: ").append(aventurero.getPoderEspecial()).append(")");
            sb.append("\n");
            posicion++;
        }
        
        return sb.toString();
    }
}
