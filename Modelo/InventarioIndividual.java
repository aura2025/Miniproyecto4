package Modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class InventarioIndividual {
    private Map<Item, Integer> items;
    private final int capacidadMaxima;
    private final String propietario; 
    
    public InventarioIndividual(String propietario) {
        this(propietario, 5);
    }
    
    public InventarioIndividual(String propietario, int capacidadMaxima) {
        this.propietario = propietario;
        this.capacidadMaxima = Math.max(5, capacidadMaxima);
        this.items = new HashMap<>();
    }
    
    public boolean agregarItem(Item item, int cantidad) {
        if (item == null || cantidad <= 0) return false;
        
        int cantidadActual = items.getOrDefault(item, 0);
        int espaciosUsados = calcularEspaciosUsados();
        
        if (cantidadActual == 0 && espaciosUsados >= capacidadMaxima) {
            return false; 
        }
        
        items.put(item, cantidadActual + cantidad);
        return true;
    }
    
    public void usarItem(Item item) {
        if (item == null) {
            throw new Modelo.exceptions.ExcepcionItemInvalido("Ítem nulo");
        }
        
        Integer cantidad = items.getOrDefault(item, 0);
        if (cantidad <= 0) {
            throw new Modelo.exceptions.ExcepcionItemInvalido(
                "No hay unidades de: " + item.getNombre() + " en el inventario de " + propietario
            );
        }
        
        items.put(item, cantidad - 1);
        if (items.get(item) == 0) {
            items.remove(item);
        }
    }
    
    public boolean tieneItem(Item item) {
        return items.containsKey(item) && items.get(item) > 0;
    }
    
    public int getCantidad(Item item) {
        return items.getOrDefault(item, 0);
    }
    
    public Item getItemPorIndice(int indice) {
        if (indice < 1 || indice > items.size()) {
            return null;
        }
        
        int currentIndex = 1;
        for (Item item : items.keySet()) {
            if (currentIndex == indice) {
                return item;
            }
            currentIndex++;
        }
        return null;
    }
    
    public int getTamanio() {
        return items.size();
    }
    
    public boolean estaVacio() {
        return items.isEmpty();
    }
    
    public int calcularEspaciosUsados() {
        return items.size(); 
    }
    
    public int getEspaciosDisponibles() {
        return capacidadMaxima - calcularEspaciosUsados();
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public String getPropietario() {
        return propietario;
    }
    
    public boolean estaLleno() {
        return calcularEspaciosUsados() >= capacidadMaxima;
    }
    
    public boolean transferirItem(Item item, int cantidad, InventarioIndividual destino) {
        if (item == null || destino == null || cantidad <= 0) {
            return false;
        }
        
        if (!tieneItem(item) || getCantidad(item) < cantidad) {
            return false; 
        }
        
        if (!destino.tieneItem(item) && destino.estaLleno()) {
            return false; 
        }

        try {
            for (int i = 0; i < cantidad; i++) {
                usarItem(item);
            }
            boolean agregado = destino.agregarItem(item, cantidad);
            
            if (!agregado) {
                agregarItem(item, cantidad);
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<String> obtenerListaItems() {
        List<String> lista = new Vector<>();
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            lista.add(entry.getKey().getNombre() + " x" + entry.getValue());
        }
        return lista;
    }
    
    public Map<Item, Integer> obtenerItemsMap() {
        return new HashMap<>(items);
    }
    
    public void setItemsMap(Map<Item, Integer> itemsMap) {
        if (itemsMap != null) {
            this.items = new HashMap<>(itemsMap);
        }
    }
    
    public void limpiar() {
        items.clear();
    }
    
    public InventarioIndividual copiar() {
        InventarioIndividual copia = new InventarioIndividual(this.propietario, this.capacidadMaxima);
        copia.items = new HashMap<>(this.items);
        return copia;
    }
    
    @Override
    public String toString() {
        if (items.isEmpty()) {
            return propietario + " - Inventario vacío";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(propietario).append(" - Inventario (")
          .append(calcularEspaciosUsados()).append("/")
          .append(capacidadMaxima).append("):\n");
        
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            sb.append("  • ").append(entry.getKey().getNombre())
              .append(" x").append(entry.getValue()).append("\n");
        }
        
        return sb.toString();
    }
}