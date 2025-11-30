package Modelo;

import java.util.HashMap;
import java.util.Map;

public class Inventario {
    private Map<Item, Integer> items;
    
    public Inventario() {
        this.items = new HashMap<>();
    }
    
    public void agregarItem(Item item, int cantidad) {
        items.put(item, items.getOrDefault(item, 0) + cantidad);
    }
    
    public boolean usarItem(Item item) {
        if (items.containsKey(item) && items.get(item) > 0) {
            items.put(item, items.get(item) - 1);
            if (items.get(item) == 0) {
                items.remove(item);
            }
            return true;
        }
        return false;
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
}