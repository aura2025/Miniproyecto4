package Vista;

import Modelo.*;
import java.util.List;

public interface Vista {
    int mostrarMenuAcciones(Personaje p);
    void mostrarEstado(String atacanteNombre, int atacanteHP, String defensorNombre, int defensorHP);
    void mostrarMensaje(String msg);
    void pedirEnter();
    void mostrarBanner(String texto);
    void mostrarEstadoInicial(List<PersonajeJugable> heroes, List<Monstruo> monstruos);
    void mostrarOrdenTurnos(List<Personaje> orden);
    void mostrarInfoHeroe(PersonajeJugable pj);
    int leerOpcionSegura(int min, int max);
    int seleccionarItem(List<String> nombresItems);
    int seleccionarObjetivoMonstruos(List<String> nombresMonstruos);
    int seleccionarObjetivoHeroes(List<String> nombresHeroes);
    void mostrarAtaque(String actor, String categoria, String objetivo, int danio);
    void mostrarTurnoPerdido(String sujeto);
    void mostrarEstadoCambio(String sujeto, String estado, int duracion);
}
