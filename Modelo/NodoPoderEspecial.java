package Modelo;
public class NodoPoderEspecial {
    private String nombre;                  
    private int costo;                       
    private int duracion;                    
    private Estado estado;                   
    private PoderEspecial poderEspecial;      
    private NodoPoderEspecial izquierda;     
    private NodoPoderEspecial derecha;        

  
    public NodoPoderEspecial(String nombre) {
        this.nombre = nombre;
        this.costo = -1;
        this.duracion = -1;
        this.estado = null;
        this.poderEspecial = null;
        this.izquierda = null;
        this.derecha = null;
    }

   
   
    public NodoPoderEspecial(String nombre, PoderEspecial poderEspecial, int costo, int duracion, Estado estado) {
        this.nombre = nombre;
        this.poderEspecial = poderEspecial;
        this.costo = costo;
        this.duracion = duracion;
        this.estado = estado;
        this.izquierda = null;
        this.derecha = null;
    }


    public String getNombre() {
        return nombre;
    }

    public int getCosto() {
        return costo;
    }

    public int getDuracion() {
        return duracion;
    }

    public Estado getEstado() {
        return estado;
    }

    public PoderEspecial getPoderEspecial() {
        return poderEspecial;
    }

    public NodoPoderEspecial getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(NodoPoderEspecial izquierda) {
        this.izquierda = izquierda;
    }

    public NodoPoderEspecial getDerecha() {
        return derecha;
    }

    public void setDerecha(NodoPoderEspecial derecha) {
        this.derecha = derecha;
    }

    
    public boolean esHoja() {
        return this.poderEspecial != null;
    }


    public boolean esRaiz() {
        return this.poderEspecial == null && this.costo == -1;
    }

    @Override
    public String toString() {
        if (esRaiz()) {
            return nombre;
        }
        return nombre + " [Costo: " + costo + " MP, Duración: " + duracion + " turnos, Estado: " + estado + "]";
    }
}
