package Controlador;

import Modelo.*;
import Vista.*;
import java.util.*;

public class Controlador {

    private List<PersonajeJugable> heroes;
    private List<Monstruo> monstruos;
    private Inventario inventario;
    private Vista vista;
    private Random random = new Random();

    public Controlador(Vista vista) {
        this.vista = vista;
        inicializarDatos();
    }

    
    private void inicializarDatos() {
        heroes = new ArrayList<>();
        monstruos = new ArrayList<>();
        inventario = new Inventario();

      
        inventario.agregarItem(Item.HIERBA, 5);
        inventario.agregarItem(Item.MEGA_HIERBA, 2);
        inventario.agregarItem(Item.RECUP_MP, 3);
        inventario.agregarItem(Item.ANTIDOTO, 3);
        inventario.agregarItem(Item.ESTIMULANTE, 2);
        inventario.agregarItem(Item.PANACEA, 1);
        inventario.agregarItem(Item.POCION_FUERZA, 2);
        inventario.agregarItem(Item.POCION_DEFENSA, 2);
        inventario.agregarItem(Item.POCION_VELOCIDAD, 2);

        
        PersonajeJugable aiden = new PersonajeJugable(
            TipoPersonaje.Jugable, "Heroe", 500, 50, 80, 90,
            Estado.NORMAL, Arma.ESPADA, 200, PoderEspecial.ENVENENAR
        );

        PersonajeJugable jessica = new PersonajeJugable(
            TipoPersonaje.Jugable, "Jessica", 400, 40, 90, 70,
            Estado.NORMAL, Arma.BASTON, 250, PoderEspecial.CONGELAR
        );

        PersonajeJugable angelo = new PersonajeJugable(
            TipoPersonaje.Jugable, "Angelo", 450, 45, 85, 60,
            Estado.NORMAL, Arma.ARCO, 180, PoderEspecial.DEBILITAR
        );

        PersonajeJugable yangus = new PersonajeJugable(
            TipoPersonaje.Jugable, "Yangus", 600, 70, 70, 50,
            Estado.NORMAL, Arma.HACHA, 150, PoderEspecial.ATURDIR
        );

        heroes.add(aiden);
        heroes.add(jessica);
        heroes.add(angelo);
        heroes.add(yangus);

 
    Monstruo m1 = new Monstruo(TipoPersonaje.Monstruo, "Slime Gigante", 300, 40, 60, 85, Estado.NORMAL, TipoMonstruo.AGRESIVO);
    Monstruo m2 = new Monstruo(TipoPersonaje.Monstruo, "Golem de Piedra", 350, 60, 55, 65, Estado.NORMAL, TipoMonstruo.DEFENSIVO);
    Monstruo m3 = new Monstruo(TipoPersonaje.Monstruo, "Caballero Oscuro", 280, 45, 65, 55, Estado.NORMAL, TipoMonstruo.BALANCEADO);
    Monstruo m4 = new Monstruo(TipoPersonaje.Monstruo, "Dragón Menor", 400, 50, 75, 45, Estado.NORMAL, TipoMonstruo.DEFENSIVO);

        monstruos.add(m1);
        monstruos.add(m2);
        monstruos.add(m3);
        monstruos.add(m4);

        vista.mostrarEstadoInicial(heroes, monstruos);
    }

    
    public void iniciarBatalla() {
        vista.mostrarBanner("¡Comienza la batalla!");
        vista.pedirEnter();

        while (hayVivos(heroes) && hayVivos(monstruos)) {
            List<Personaje> ordenTurnos = generarOrdenTurnos();
            vista.mostrarOrdenTurnos(ordenTurnos);

            for (Personaje p : ordenTurnos) {
                if (p.getHP() <= 0) continue; 
                if (!hayVivos(heroes) || !hayVivos(monstruos)) break;

                if (p instanceof PersonajeJugable) {
                    turnoHeroe((PersonajeJugable)p);
                } else if (p instanceof Monstruo) {
                    turnoMonstruo((Monstruo)p);
                }
            }
        }

        if (hayVivos(heroes)) {
            vista.mostrarBanner("¡Victoria! Los héroes ganaron.");
        } else {
            vista.mostrarBanner("Derrota... los monstruos prevalecieron.");
        }
    }


    public void iniciar() {
        iniciarBatalla();
    }


    private void turnoHeroe(PersonajeJugable heroe) {
        if (heroe.getHP() <= 0) return;

     
        heroe.actualizarBuffs();
        boolean accionCompleta = false;
        while (!accionCompleta) {
            vista.mostrarInfoHeroe(heroe);
            int accion = vista.mostrarMenuAcciones(heroe);

            switch (accion) {
                case 1: 
                    List<Monstruo> vivosAtk = new ArrayList<>();
                    List<String> nombresAtk = new ArrayList<>();
                    for (Monstruo m : monstruos) if (m.getHP() > 0) {
                        vivosAtk.add(m);
                        nombresAtk.add(m.getNombre() + " - HP: " + m.getHP() + "/" + m.getHPMaximo());
                    }
                    int objetivoIdx = vista.seleccionarObjetivoMonstruos(nombresAtk);
                    if (objetivoIdx >= 0 && objetivoIdx < vivosAtk.size()) {
                        atacar(heroe, vivosAtk.get(objetivoIdx));
                        accionCompleta = true;
                    } else {
                        vista.mostrarMensaje("Ataque cancelado, selecciona otra acción.");
                    }
                    break;
                case 2: 
                    List<Monstruo> vivosPow = new ArrayList<>();
                    List<String> nombresPow = new ArrayList<>();
                    for (Monstruo m : monstruos) if (m.getHP() > 0) {
                        vivosPow.add(m);
                        nombresPow.add(m.getNombre() + " - HP: " + m.getHP() + "/" + m.getHPMaximo());
                    }
                    int idxObj = vista.seleccionarObjetivoMonstruos(nombresPow);
                    if (idxObj >= 0 && idxObj < vivosPow.size()) {
                        Monstruo objetivoM = vivosPow.get(idxObj);
                        if (heroe.getMP() >= 50) {
                          
                            if (heroe.getPoderEspecial() == PoderEspecial.DEBILITAR && objetivoM.getEstado() == Estado.DEBILITADO) {
                                vista.mostrarMensaje(objetivoM.getNombre() + " ya está DEBILITADO.");
                            } else {
                                heroe.usarPoderEspecial(objetivoM);
                         
                                vista.mostrarMensaje(heroe.getNombre() + " usa " + heroe.getPoderEspecial() + " sobre " + objetivoM.getNombre());
                           
                                EstadoEfecto ef = objetivoM.getEstadoEfecto();
                                if (ef != null) {
                                    if (ef.getTipo() == Estado.DEBILITADO) {
                                        vista.mostrarEstadoCambio(objetivoM.getNombre(), ef.getTipo().toString() + " (ataque -" + ef.getValorDebilitacion() + ")", ef.getTurnos());
                                    } else {
                                        vista.mostrarEstadoCambio(objetivoM.getNombre(), ef.getTipo().toString(), ef.getTurnos());
                                    }
                                }
                                accionCompleta = true;
                            }
                        } else {
                            vista.mostrarMensaje("No hay suficiente MP para el poder especial. Elige otra acción.");
                        }
                    } else {
                        vista.mostrarMensaje("Uso de poder especial cancelado, selecciona otra acción.");
                    }
                    break;
                case 3: 
                    heroe.defender();
                    vista.mostrarMensaje(heroe.getNombre() + " se defiende 🛡️");
                    accionCompleta = true;
                    break;
                case 4: 
                    if (inventario == null || inventario.estaVacio()) {
                        vista.mostrarMensaje("No hay ítems disponibles.");
                        break;
                    }
                    List<String> nombresItems = new ArrayList<>();
                    for (int i = 1; i <= inventario.getTamanio(); i++) {
                        Item it = inventario.getItemPorIndice(i);
                        if (it != null) nombresItems.add(it.getNombre() + " x" + inventario.getCantidad(it));
                    }
                    int selItem = vista.seleccionarItem(nombresItems);
                    if (selItem >= 0) {
                        Item item = inventario.getItemPorIndice(selItem + 1);
                        if (item != null) {
                           
                            List<PersonajeJugable> vivosH = new ArrayList<>();
                            List<String> nombresH = new ArrayList<>();
                            for (PersonajeJugable h : heroes) if (h.getHP() > 0) {
                                vivosH.add(h);
                                nombresH.add(h.getNombre() + " - HP: " + h.getHP() + "/" + h.getHPMaximo());
                            }
                            int idxHeroe = vista.seleccionarObjetivoHeroes(nombresH);
                            if (idxHeroe >= 0 && idxHeroe < vivosH.size()) {
                                PersonajeJugable objetivoHeroe = vivosH.get(idxHeroe);
                                int hpAntesItem = objetivoHeroe.getHP();
                                int mpAntesItem = objetivoHeroe.getMP();
                                Estado estadoAntesItem = objetivoHeroe.getEstado();

                                heroe.usarItem(item, objetivoHeroe);
                                inventario.usarItem(item);
                                vista.mostrarMensaje(heroe.getNombre() + " usa " + item.getNombre() + " en " + objetivoHeroe.getNombre());

                                switch (item.getTipo()) {
                                    case CURACION: {
                                        int hpDespues = objetivoHeroe.getHP();
                                        int rec = Math.max(0, hpDespues - hpAntesItem);
                                        vista.mostrarMensaje(objetivoHeroe.getNombre() + " recupera " + rec + " HP. HP actual: " + hpDespues + "/" + objetivoHeroe.getHPMaximo());
                                        break;
                                    }
                                    case RECUPERACION_MP: {
                                        int mpDespues = objetivoHeroe.getMP();
                                        int rec = Math.max(0, mpDespues - mpAntesItem);
                                        vista.mostrarMensaje(objetivoHeroe.getNombre() + " recupera " + rec + " MP. MP actual: " + mpDespues + "/" + objetivoHeroe.getMPMaximo());
                                        break;
                                    }
                                    case CURACION_ESTADO: {
                                        Estado estadoDespues = objetivoHeroe.getEstado();
                                        if (estadoAntesItem != Estado.NORMAL && estadoDespues == Estado.NORMAL) {
                                            vista.mostrarMensaje(objetivoHeroe.getNombre() + " se recupera de " + estadoAntesItem);
                                        } else {
                                            vista.mostrarMensaje(objetivoHeroe.getNombre() + " no estaba en el estado requerido.");
                                        }
                                        break;
                                    }
                                    case BUFF: {
                                        vista.mostrarMensaje(objetivoHeroe.getNombre() + " recibe efecto de " + item.getNombre());
                                        break;
                                    }
                                    default: break;
                                }

                                accionCompleta = true;
                            } else {
                                vista.mostrarMensaje("Uso de ítem cancelado, selecciona otra acción.");
                            }
                        } else {
                            vista.mostrarMensaje("Ítem inválido, selecciona otra acción.");
                        }
                    } else {
                        vista.mostrarMensaje("No se seleccionó ningún ítem. Elige otra acción.");
                    }
                    break;
                default:
                    vista.mostrarMensaje("Opción inválida. Intenta de nuevo.");
                    break;
            }
        }
        vista.pedirEnter();
    }

    private void turnoMonstruo(Monstruo m) {
        if (m.getHP() <= 0) return;
        PersonajeJugable objetivo = seleccionarHeroeAleatorio();
        Estado estadoAntes = m.getEstado();
        int hpAntes = objetivo != null ? objetivo.getHP() : 0;

        
        if (objetivo != null) {
            String estadoActual = m.getEstado() != null ? m.getEstado().toString() : "";
            int hpMonstruoAntes = m.getHP();
            m.tomarTurno(objetivo, 1);

            int hpDespues = objetivo.getHP();
            int danio = Math.max(0, hpAntes - hpDespues);
            if (danio > 0) {
                vista.mostrarAtaque(m.getNombre() + " (" + estadoActual + ")", m.getTipoMostruo() != null ? m.getTipoMostruo().toString() : "", objetivo.getNombre(), danio);
            } else if (estadoAntes == Estado.ATURDIDO || estadoAntes == Estado.CONGELADO) {
                vista.mostrarTurnoPerdido(m.getNombre() + " (" + estadoActual + ")");
            }


            int hpMonstruoDespues = m.getHP();
            if (hpMonstruoDespues < hpMonstruoAntes) {
                int danoMon = hpMonstruoAntes - hpMonstruoDespues;
                EstadoEfecto efe = m.getEstadoEfecto();
                String causa = "efecto";
                if (efe != null) {
                    causa = efe.getTipo() == Estado.ENVENENADO ? "VENENO" : efe.getTipo().toString();
                }
                vista.mostrarMensaje(m.getNombre() + " (" + estadoActual + ") sufre " + danoMon + " de daño por " + causa + ".");
                if (efe != null) {
                    vista.mostrarEstadoCambio(m.getNombre(), efe.getTipo().toString(), efe.getTurnos());
                }
                if (hpMonstruoDespues <= 0) {
                    vista.mostrarMensaje(m.getNombre() + " ha sido derrotado por los efectos.");
                }
            }

            vista.mostrarEstado(m.getNombre(), m.getHP(), objetivo != null ? objetivo.getNombre() : "", objetivo != null ? objetivo.getHP() : 0);
        } else {
     
            int hpMonstruoAntes = m.getHP();
            m.tomarTurno(null, 0);
            int hpMonstruoDespues = m.getHP();
            if (hpMonstruoDespues < hpMonstruoAntes) {
                int danoMon = hpMonstruoAntes - hpMonstruoDespues;
                String estadoActual = m.getEstado() != null ? m.getEstado().toString() : "";
                EstadoEfecto efe = m.getEstadoEfecto();
                String causa = "efecto";
                if (efe != null) causa = efe.getTipo() == Estado.ENVENENADO ? "VENENO" : efe.getTipo().toString();
                vista.mostrarMensaje(m.getNombre() + " (" + estadoActual + ") sufre " + danoMon + " de daño por " + causa + ".");
            }
            vista.mostrarEstado(m.getNombre(), m.getHP(), "", 0);
        }

        vista.pedirEnter();
    }

    private void atacar(Personaje atacante, Personaje defensor) {
       
        int hpAntesDef = defensor != null ? defensor.getHP() : 0;
        int defAntesDef = defensor != null ? defensor.getDefensa() : 0;
        int velAntesDef = defensor != null ? defensor.getVelocidad() : 0;
        
        int defAntesAt = atacante != null ? atacante.getDefensa() : 0;
        int velAntesAt = atacante != null ? atacante.getVelocidad() : 0;
        Integer mpAntesAt = null;
        if (atacante instanceof PersonajeJugable) mpAntesAt = ((PersonajeJugable) atacante).getMP();

        String categoria = "";

        
        if (atacante instanceof PersonajeJugable) {
            ((PersonajeJugable) atacante).atacar(defensor);
        } else {
            int daño = Math.max(0, atacante.getAtaque() - (defensor != null ? defensor.getDefensa() : 0) + random.nextInt(6));
            if (defensor != null) defensor.setHP(defensor.getHP() - daño);
        }

        if (atacante instanceof Monstruo) {
            Monstruo mm = (Monstruo) atacante;
            categoria = mm.getTipoMostruo() != null ? mm.getTipoMostruo().toString() : "";
        }


        int hpDespuesDef = defensor != null ? defensor.getHP() : 0;
        int defDespuesDef = defensor != null ? defensor.getDefensa() : 0;
        int velDespuesDef = defensor != null ? defensor.getVelocidad() : 0;
        
        int defDespuesAt = atacante != null ? atacante.getDefensa() : 0;
        int velDespuesAt = atacante != null ? atacante.getVelocidad() : 0;
        Integer mpDespuesAt = null;
        if (atacante instanceof PersonajeJugable) mpDespuesAt = ((PersonajeJugable) atacante).getMP();

        int danoReal = Math.max(0, hpAntesDef - hpDespuesDef);

   
        vista.mostrarAtaque(atacante.getNombre(), categoria, defensor != null ? defensor.getNombre() : "<desconocido>", danoReal);
        vista.mostrarEstado(atacante.getNombre(), atacante.getHP(), defensor != null ? defensor.getNombre() : "", defensor != null ? defensor.getHP() : 0);


        if (mpAntesAt != null && mpDespuesAt != null && !mpAntesAt.equals(mpDespuesAt)) {
            int diff = mpDespuesAt - mpAntesAt;
            if (diff > 0) vista.mostrarMensaje(atacante.getNombre() + " recupera " + diff + " MP.");
            else if (diff < 0) vista.mostrarMensaje(atacante.getNombre() + " gasta " + (-diff) + " MP.");
        }

        if (defAntesDef != defDespuesDef) {
            int diff = defDespuesDef - defAntesDef;
            if (diff > 0) vista.mostrarMensaje(defensor.getNombre() + " gana " + diff + " de defensa.");
            else vista.mostrarMensaje(defensor.getNombre() + " pierde " + (-diff) + " de defensa.");
        }

        if (velAntesDef != velDespuesDef) {
            int diff = velDespuesDef - velAntesDef;
            if (diff > 0) vista.mostrarMensaje(defensor.getNombre() + " gana " + diff + " de velocidad.");
            else vista.mostrarMensaje(defensor.getNombre() + " pierde " + (-diff) + " de velocidad.");
        }

        if (defAntesAt != defDespuesAt) {
            int diff = defDespuesAt - defAntesAt;
            if (diff > 0) vista.mostrarMensaje(atacante.getNombre() + " gana " + diff + " de defensa.");
            else vista.mostrarMensaje(atacante.getNombre() + " pierde " + (-diff) + " de defensa.");
        }

        if (velAntesAt != velDespuesAt) {
            int diff = velDespuesAt - velAntesAt;
            if (diff > 0) vista.mostrarMensaje(atacante.getNombre() + " gana " + diff + " de velocidad.");
            else vista.mostrarMensaje(atacante.getNombre() + " pierde " + (-diff) + " de velocidad.");
        }
    }

    private PersonajeJugable seleccionarHeroeAleatorio() {
        List<PersonajeJugable> vivos = new ArrayList<>();
        for (PersonajeJugable h : heroes) if (h.getHP() > 0) vivos.add(h);
        if (vivos.isEmpty()) return null;
        return vivos.get(random.nextInt(vivos.size()));
    }

    private List<Personaje> generarOrdenTurnos() {
        List<Personaje> todos = new ArrayList<>();
        todos.addAll(heroes);
        todos.addAll(monstruos);
        todos.sort((a, b) -> b.getVelocidad() - a.getVelocidad());
        return todos;
    }

    private boolean hayVivos(List<? extends Personaje> lista) {
        return lista.stream().anyMatch(p -> p.getHP() > 0);
    }
}