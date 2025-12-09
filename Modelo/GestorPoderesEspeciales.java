package Modelo;


public class GestorPoderesEspeciales {
    private static final ArbolPoderesEspeciales arbolPoderes;
    private static final GestorPoderesEspeciales instancia;

    static {
        arbolPoderes = new ArbolPoderesEspeciales();
        instancia = new GestorPoderesEspeciales();
    }

  
    private GestorPoderesEspeciales() {
    }

    
    public static GestorPoderesEspeciales obtenerInstancia() {
        return instancia;
    }

    public static ArbolPoderesEspeciales obtenerArbol() {
        return arbolPoderes;
    }

   
    public NodoPoderEspecial obtenerInformacionPoder(PoderEspecial poder) {
        return arbolPoderes.obtenerInfoPoder(poder);
    }

   
    public int obtenerCostoPoder(PoderEspecial poder) {
        NodoPoderEspecial nodo = arbolPoderes.buscarPoder(poder);
        return nodo != null ? nodo.getCosto() : -1;
    }

 
    public int obtenerDuracionPoder(PoderEspecial poder) {
        NodoPoderEspecial nodo = arbolPoderes.buscarPoder(poder);
        return nodo != null ? nodo.getDuracion() : -1;
    }

    public Estado obtenerEstadoPoder(PoderEspecial poder) {
        NodoPoderEspecial nodo = arbolPoderes.buscarPoder(poder);
        return nodo != null ? nodo.getEstado() : null;
    }

    public boolean existePoder(PoderEspecial poder) {
        return arbolPoderes.buscarPoder(poder) != null;
    }

   
    public String obtenerDescripcionPoder(PoderEspecial poder) {
        NodoPoderEspecial nodo = arbolPoderes.buscarPoder(poder);
        if (nodo == null) {
            return "Poder no encontrado";
        }
        return nodo.toString();
    }

    
    public void imprimirArbolPoderes() {
        System.out.println(arbolPoderes.imprimirArbol());
    }

    
    public String obtenerEstadisticasArbol() {
        return "Nodos totales: " + arbolPoderes.contarNodos() + 
               " | Altura: " + arbolPoderes.calcularAltura() +
               " | Poderes disponibles: " + (arbolPoderes.contarNodos() - 1);
    }
}
