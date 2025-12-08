package Modelo;

import java.util.Vector;
import java.util.List;

public class ArbolPoderesEspeciales {
    private NodoPoderEspecial raiz;

   
    public ArbolPoderesEspeciales() {
     
        raiz = new NodoPoderEspecial("Poderes Especiales");

      
        NodoPoderEspecial envenenar = new NodoPoderEspecial(
            "Envenenar", PoderEspecial.ENVENENAR, 50, 3, Estado.ENVENENADO
        );
        
        NodoPoderEspecial aturdir = new NodoPoderEspecial(
            "Aturdir", PoderEspecial.ATURDIR, 50, 1, Estado.ATURDIDO
        );
        
        NodoPoderEspecial debilitar = new NodoPoderEspecial(
            "Debilitar", PoderEspecial.DEBILITAR, 50, 3, Estado.DEBILITADO
        );
        
        NodoPoderEspecial congelar = new NodoPoderEspecial(
            "Congelar", PoderEspecial.CONGELAR, 50, 1, Estado.CONGELADO
        );

        
        raiz.setIzquierda(envenenar);
        raiz.setDerecha(debilitar);
        
        envenenar.setIzquierda(aturdir);
        envenenar.setDerecha(congelar);
    }

   
    public NodoPoderEspecial getRaiz() {
        return raiz;
    }

    
    public NodoPoderEspecial buscarPoder(PoderEspecial poder) {
        return buscarPoderRecursivo(raiz, poder);
    }

   
    private NodoPoderEspecial buscarPoderRecursivo(NodoPoderEspecial nodo, PoderEspecial poder) {
        if (nodo == null) {
            return null;
        }

        
        if (nodo.getPoderEspecial() == poder) {
            return nodo;
        }

       
        NodoPoderEspecial resultadoIzq = buscarPoderRecursivo(nodo.getIzquierda(), poder);
        if (resultadoIzq != null) {
            return resultadoIzq;
        }

        return buscarPoderRecursivo(nodo.getDerecha(), poder);
    }

  
    public List<NodoPoderEspecial> obtenerTodosPoderes() {
        Vector<NodoPoderEspecial> poderes = new Vector<>();
        recorridoInOrden(raiz, poderes);
        return poderes;
    }

    private void recorridoInOrden(NodoPoderEspecial nodo, Vector<NodoPoderEspecial> resultado) {
        if (nodo == null) {
            return;
        }

        recorridoInOrden(nodo.getIzquierda(), resultado);
        
      
        if (nodo.esHoja()) {
            resultado.add(nodo);
        }
        
        recorridoInOrden(nodo.getDerecha(), resultado);
    }

    
    public List<NodoPoderEspecial> obtenerPoderesPreOrden() {
        Vector<NodoPoderEspecial> poderes = new Vector<>();
        recorridoPreOrden(raiz, poderes);
        return poderes;
    }

   
    private void recorridoPreOrden(NodoPoderEspecial nodo, Vector<NodoPoderEspecial> resultado) {
        if (nodo == null) {
            return;
        }

        if (nodo.esHoja()) {
            resultado.add(nodo);
        }
        
        recorridoPreOrden(nodo.getIzquierda(), resultado);
        recorridoPreOrden(nodo.getDerecha(), resultado);
    }

   
    public NodoPoderEspecial obtenerInfoPoder(PoderEspecial poder) {
        return buscarPoder(poder);
    }

   
    public int calcularAltura() {
        return calcularAlturaRecursiva(raiz);
    }

   
    private int calcularAlturaRecursiva(NodoPoderEspecial nodo) {
        if (nodo == null) {
            return 0;
        }

        int alturaIzq = calcularAlturaRecursiva(nodo.getIzquierda());
        int alturaDer = calcularAlturaRecursiva(nodo.getDerecha());

        return 1 + Math.max(alturaIzq, alturaDer);
    }

  
    public int contarNodos() {
        return contarNodosRecursivo(raiz);
    }

    private int contarNodosRecursivo(NodoPoderEspecial nodo) {
        if (nodo == null) {
            return 0;
        }

        return 1 + contarNodosRecursivo(nodo.getIzquierda()) + 
               contarNodosRecursivo(nodo.getDerecha());
    }

    
    public String imprimirArbol() {
        StringBuilder sb = new StringBuilder();
        imprimirArbolRecursivo(raiz, "", sb, true);
        return sb.toString();
    }

    
    private void imprimirArbolRecursivo(NodoPoderEspecial nodo, String prefijo, 
                                        StringBuilder sb, boolean esUltimo) {
        if (nodo == null) {
            return;
        }

        sb.append(prefijo);
        sb.append(esUltimo ? "└── " : "├── ");
        sb.append(nodo.getNombre()).append("\n");

        String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");

        NodoPoderEspecial izq = nodo.getIzquierda();
        NodoPoderEspecial der = nodo.getDerecha();

        if (izq != null || der != null) {
            if (izq != null) {
                imprimirArbolRecursivo(izq, nuevoPrefijo, sb, der == null);
            }
            if (der != null) {
                imprimirArbolRecursivo(der, nuevoPrefijo, sb, true);
            }
        }
    }

    @Override
    public String toString() {
        return "ArbolPoderesEspeciales con " + contarNodos() + " nodos y altura " + calcularAltura();
    }
}
