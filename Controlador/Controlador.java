package Controlador;

import Modelo.*;
import Vista.*;
import java.util.List;
import java.util.Vector;
import java.util.Random;

public class Controlador {

    private List<PersonajeJugable> heroes;
    private List<Monstruo> monstruos;
    private Inventario inventario;
    private HistorialBatallas historial;
    private Vista vista;
    private Random random = new Random();
    private List<Personaje> ordenSiguiente = null;

    private GestorDeshacerRehacer gestorDeshacer;
    private int contadorTurnos = 0;
    
    private GestorColaGremio gestorGremio;

    public Controlador(Vista vista) {
        this.vista = vista;
        inicializarDatos();
        this.gestorDeshacer = new GestorDeshacerRehacer();
        this.gestorGremio = new GestorColaGremio();
    }

    
    private void inicializarDatos() {
        heroes = new Vector<>();
        monstruos = new Vector<>();
        inventario = new Inventario(); 
        historial = new HistorialBatallas();

      
        inventario.agregarItem(Item.HIERBA, 10);
        inventario.agregarItem(Item.MEGA_HIERBA, 5);
        inventario.agregarItem(Item.RECUP_MP, 8);
        inventario.agregarItem(Item.ANTIDOTO, 6);
        inventario.agregarItem(Item.ESTIMULANTE, 4);
        inventario.agregarItem(Item.PANACEA, 3);
        inventario.agregarItem(Item.POCION_FUERZA, 4);
        inventario.agregarItem(Item.POCION_DEFENSA, 4);
        inventario.agregarItem(Item.POCION_VELOCIDAD, 4);

        
        PersonajeJugable heroe = new PersonajeJugable(
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

       
        heroe.agregarItemAInventario(Item.HIERBA, 2);
        heroe.agregarItemAInventario(Item.MEGA_HIERBA, 1);
        heroe.agregarItemAInventario(Item.RECUP_MP, 1);
        heroe.agregarItemAInventario(Item.ANTIDOTO, 1);
        heroe.agregarItemAInventario(Item.POCION_FUERZA, 1);
        
        
        jessica.agregarItemAInventario(Item.RECUP_MP, 2);
        jessica.agregarItemAInventario(Item.HIERBA, 1);
        jessica.agregarItemAInventario(Item.PANACEA, 1);
        jessica.agregarItemAInventario(Item.ESTIMULANTE, 1);
        jessica.agregarItemAInventario(Item.POCION_DEFENSA, 1);
    
        angelo.agregarItemAInventario(Item.HIERBA, 2);
        angelo.agregarItemAInventario(Item.POCION_VELOCIDAD, 1);
        angelo.agregarItemAInventario(Item.RECUP_MP, 1);
        angelo.agregarItemAInventario(Item.ANTIDOTO, 1);
        angelo.agregarItemAInventario(Item.ESTIMULANTE, 1);
        
        yangus.agregarItemAInventario(Item.MEGA_HIERBA, 2);
        yangus.agregarItemAInventario(Item.HIERBA, 1);
        yangus.agregarItemAInventario(Item.POCION_DEFENSA, 1);
        yangus.agregarItemAInventario(Item.ESTIMULANTE, 1);
        yangus.agregarItemAInventario(Item.POCION_FUERZA, 1);

        heroes.add(heroe);
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
        
  
        try {
            vista.mostrarMensaje("\n╔════════════════════════════════════════════════════════╗");
            vista.mostrarMensaje("║  INVENTARIOS INICIALES DE LOS AVENTUREROS             ║");
            vista.mostrarMensaje("╚════════════════════════════════════════════════════════╝");
            for (PersonajeJugable h : heroes) {
                vista.mostrarMensaje("\n" + h.mostrarInventario());
            }
            vista.mostrarMensaje("\n[Usa la opción 10 durante tu turno para gestionar tu inventario]");
        } catch (Exception e) {
            
        }
    }
    
    public void iniciarBatalla() {
        vista.mostrarBanner("¡Comienza la batalla!");
        historial.clear();
        historial.registrarMensaje("Comienza la batalla entre héroes y monstruos.");
        gestorDeshacer.limpiarHistorial();
        contadorTurnos = 0;
        vista.pedirEnter();

        while (hayVivos(heroes) && hayVivos(monstruos)) {
            contadorTurnos++;
            List<Personaje> ordenTurnos;
            if (ordenSiguiente != null) {
                ordenTurnos = ordenSiguiente;
                ordenSiguiente = null;
            } else {
                ordenTurnos = generarOrdenTurnos();
            }
            vista.mostrarOrdenTurnos(ordenTurnos);
            try {
                StringBuilder sb = new StringBuilder();
                for (Personaje p : ordenTurnos) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(p.getNombre());
                }
                historial.registrarMensaje("ORDEN_RONDA:" + sb.toString());
            } catch (Exception e) { }

            for (Personaje p : ordenTurnos) {
                if (ordenSiguiente != null) break;
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
            historial.registrarMensaje("Victoria de los héroes");
        } else {
            vista.mostrarBanner("Derrota... los monstruos prevalecieron.");
            historial.registrarMensaje("Victoria de los monstruos");
        }

        try {
            String textoHistorial = historial.toString();
            if (textoHistorial == null || textoHistorial.isEmpty()) textoHistorial = "(no hay eventos registrados)";
            vista.mostrarMensaje("\n--- Historial de Batalla ---\n" + textoHistorial + "--- Fin del Historial ---");
            String resultadoToken = hayVivos(heroes) ? "victoria_heroes" : "victoria_monstruos";
            try {
                java.nio.file.Path ruta = historial.guardarEnArchivo(resultadoToken);
                vista.mostrarMensaje("Historial guardado en: " + ruta.toAbsolutePath().toString());
            } catch (java.io.IOException ioex) {
                vista.mostrarMensaje("No se pudo guardar el historial: " + ioex.getMessage());
            }
        } catch (Exception ex) {
        }
        vista.pedirEnter();
    }


    public void iniciar() {
        boolean continuar = true;
        
        while (continuar) {
            int opcion = vista.mostrarMenuPrincipal();
            
            switch (opcion) {
                case 1:
                    iniciarBatalla();
                    break;
                case 2:
                    menuGremio();
                    break;
                case 3:
                    vista.mostrarMensaje("\n¡Hasta pronto, aventurero!");
                    continuar = false;
                    break;
            }
        }
    }

    public HistorialBatallas getHistorial() {
        return this.historial;
    }


    private void turnoHeroe(PersonajeJugable heroe) {
        if (heroe.getHP() <= 0) return;

        vista.mostrarTurnoActual(contadorTurnos, heroe.getNombre());
     
        heroe.actualizarBuffs();
        
        
        String descripcionInicioTurno = "Inicio del turno de " + heroe.getNombre();
        gestorDeshacer.guardarEstado(descripcionInicioTurno, heroes, monstruos, inventario, contadorTurnos);
        
        boolean accionCompleta = false;
        while (!accionCompleta) {
            vista.mostrarInfoHeroe(heroe);
            int accion = vista.mostrarMenuAcciones(heroe);

            switch (accion) {
                case 1: 
                    List<Monstruo> vivosAtk = new Vector<>();
                    List<String> nombresAtk = new Vector<>();
                    for (Monstruo m : monstruos) if (m.getHP() > 0) {
                        vivosAtk.add(m);
                        String display = (m.getNombre() == null || m.getNombre().trim().isEmpty()) ?
                            (m.getTipoMostruo() != null ? m.getTipoMostruo().toString() : "<Monstruo>") : m.getNombre();
                        nombresAtk.add(display + " - HP: " + m.getHP() + "/" + m.getHPMaximo());
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
                    GestorPoderesEspeciales gestorPoderes = GestorPoderesEspeciales.obtenerInstancia();
                    int costoPoder = gestorPoderes.obtenerCostoPoder(heroe.getPoderEspecial());
                    
                    List<Monstruo> vivosPow = new Vector<>();
                    List<String> nombresPow = new Vector<>();
                    for (Monstruo m : monstruos) if (m.getHP() > 0) {
                        vivosPow.add(m);
                        String display = (m.getNombre() == null || m.getNombre().trim().isEmpty()) ?
                            (m.getTipoMostruo() != null ? m.getTipoMostruo().toString() : "<Monstruo>") : m.getNombre();
                        nombresPow.add(display + " - HP: " + m.getHP() + "/" + m.getHPMaximo());
                    }
                    int idxObj = vista.seleccionarObjetivoMonstruos(nombresPow);
                    if (idxObj >= 0 && idxObj < vivosPow.size()) {
                        Monstruo objetivoM = vivosPow.get(idxObj);
                        if (heroe.getMP() >= costoPoder) {
                            if (heroe.getPoderEspecial() == PoderEspecial.DEBILITAR && objetivoM.getEstado() == Estado.DEBILITADO) {
                                vista.mostrarMensaje(objetivoM.getNombre() + " ya está DEBILITADO.");
                            } else {
                                try {
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
                                    try { vista.mostrarOrdenTurnos(generarOrdenTurnos()); } catch (Exception e) { }
                                    accionCompleta = true;
                                } catch (Modelo.exceptions.ExcepcionMPInsuficiente ex) {
                                    vista.mostrarMensaje("No hay suficiente MP para usar el poder.");
                                } catch (Modelo.exceptions.ExcepcionObjetivoInvalido ex) {
                                    vista.mostrarMensaje("Objetivo inválido para el poder especial.");
                                } catch (Modelo.exceptions.ExcepcionEntidadMuerta ex) {
                                    vista.mostrarMensaje("El objetivo ya está muerto.");
                                }
                                try {
                                    historial.registrarPoder(heroe.getNombre(), heroe.getPoderEspecial() != null ? heroe.getPoderEspecial().toString() : "<sin poder>", objetivoM.getNombre());
                                } catch (Exception ex) { }
                            }
                        } else {
                            vista.mostrarMensaje("No hay suficiente MP para el poder especial. Costo: " + costoPoder + " MP. MP actual: " + heroe.getMP());
                        }
                    } else {
                        vista.mostrarMensaje("Uso de poder especial cancelado, selecciona otra acción.");
                    }
                    break;
                    
                case 3: 
                    String descripcionDefensa = heroe.getNombre() + " Se va a defenderse";
                    gestorDeshacer.guardarEstado(descripcionDefensa, heroes, monstruos, inventario, contadorTurnos);

                    heroe.defender();
                    vista.mostrarMensaje(heroe.getNombre() + " se defiende ");
                    accionCompleta = true;
                    break;
                    
                case 4: 
                    String descripcionItem = heroe.getNombre() + " Va a usar un ítem";
                    gestorDeshacer.guardarEstado(descripcionItem, heroes, monstruos, inventario, contadorTurnos);

                    if (heroe.getInventarioIndividual() == null || heroe.getInventarioIndividual().estaVacio()) {
                        vista.mostrarMensaje("Tu inventario está vacío.");
                        vista.mostrarMensaje("Usa la opción 10 para tomar ítems del depósito común.");
                        break;
                    }
                    
                 
                    List<String> nombresItems = new Vector<>();
                    InventarioIndividual invIndividual = heroe.getInventarioIndividual();
                    for (int i = 1; i <= invIndividual.getTamanio(); i++) {
                        Item it = invIndividual.getItemPorIndice(i);
                        if (it != null) {
                            nombresItems.add(it.getNombre() + " x" + invIndividual.getCantidad(it));
                        }
                    }
                    
                    int selItem = vista.seleccionarItem(nombresItems);
                    if (selItem >= 0) {
                        Item item = invIndividual.getItemPorIndice(selItem + 1);
                        if (item != null) {
                            List<PersonajeJugable> vivosH = new Vector<>();
                            List<String> nombresH = new Vector<>();
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

                                try {
                                    
                                    heroe.usarItem(item, objetivoHeroe);
                                    vista.mostrarMensaje(heroe.getNombre() + " usa " + item.getNombre() + " en " + objetivoHeroe.getNombre());
                                    try { historial.registrarItem(heroe.getNombre(), item.getNombre(), objetivoHeroe.getNombre()); } catch (Exception e) {}
                                } catch (Modelo.exceptions.ExcepcionItemInvalido ex) {
                                    vista.mostrarMensaje("Ítem inválido o agotado: " + item.getNombre());
                                    break;
                                } catch (RuntimeException ex) {
                                    vista.mostrarMensaje("Error usando ítem: " + ex.getMessage());
                                    break;
                                }

                                try { vista.mostrarOrdenTurnos(generarOrdenTurnos()); } catch (Exception e) { }

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
                case 5: 
                    try {
                        java.nio.file.Path dir = java.nio.file.Paths.get("partidas");
                        java.nio.file.Files.createDirectories(dir);
                        String nombreArchivo = "partida_" + new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".txt";
                        java.nio.file.Path ruta = dir.resolve(nombreArchivo);
                        List<Personaje> todos = generarOrdenTurnos();
                        int idxStart = -1;
                        for (int i = 0; i < todos.size(); i++) if (todos.get(i) instanceof PersonajeJugable && todos.get(i).getNombre().equals(heroe.getNombre())) { idxStart = i; break; }
                        List<Personaje> ordenGuardar = null;
                        if (idxStart >= 0) {
                            ordenGuardar = new Vector<>();
                            for (int k = 0; k < todos.size(); k++) ordenGuardar.add(todos.get((idxStart + k) % todos.size()));
                        }
                        Modelo.Partida.guardar(heroes, monstruos, inventario, historial, "HEROE", heroe.getNombre(), ordenGuardar, ruta);
                        vista.mostrarMensaje("Partida guardada en: " + ruta.toAbsolutePath().toString());
                        historial.registrarMensaje("Partida guardada: " + ruta.getFileName().toString());
                        accionCompleta = true;
                    } catch (Exception ex) {
                        vista.mostrarMensaje("Error guardando la partida: " + ex.getMessage());
                    }
                    break;
                    
                case 6: 
                    try {
                        java.nio.file.Path dir = java.nio.file.Paths.get("partidas");
                        if (!java.nio.file.Files.exists(dir)) {
                            vista.mostrarMensaje("No hay partidas guardadas.");
                            break;
                        }
                        java.util.List<String> archivos = new java.util.Vector<>();
                        try (java.util.stream.Stream<java.nio.file.Path> s = java.nio.file.Files.list(dir)) {
                            s.filter(p -> p.getFileName().toString().endsWith(".txt")).forEach(p -> archivos.add(p.getFileName().toString()));
                        }
                        if (archivos.isEmpty()) { vista.mostrarMensaje("No hay partidas guardadas."); break; }
                        int sel = vista.seleccionarItem(archivos);
                        if (sel < 0) { vista.mostrarMensaje("Carga cancelada."); break; }
                        java.nio.file.Path ruta = dir.resolve(archivos.get(sel));
                        Modelo.Partida.PartidaData pd = Modelo.Partida.cargar(ruta);
                        this.heroes = pd.heroes;
                        this.monstruos = pd.monstruos;
                        this.inventario = pd.inventario;
                        this.historial = pd.historial;
                        vista.mostrarMensaje("Partida cargada: " + ruta.getFileName().toString());
                        try { vista.mostrarBanner("¡Partida reanudada!"); } catch (Exception e) { }
                        try { historial.registrarMensaje("Partida reanudada desde: " + ruta.getFileName().toString()); } catch (Exception e) { }
                        try {
                            vista.mostrarEstadoInicial(this.heroes, this.monstruos);
                        } catch (Exception e) { }
                        if (pd.ordenGuardada != null && !pd.ordenGuardada.isEmpty()) {
                            List<Personaje> restored = new Vector<>();
                            for (String entry : pd.ordenGuardada) {
                                try {
                                    String[] parts = entry.split("\\|", -1);
                                    if (parts.length < 2) continue;
                                    String tipo = parts[0];
                                    String nombre = parts[1];
                                    if (tipo.toUpperCase().contains("HERO")) {
                                        for (PersonajeJugable h : this.heroes) {
                                            if (h.getNombre().equals(nombre)) { restored.add(h); break; }
                                        }
                                    } else if (tipo.toUpperCase().contains("MONST")) {
                                        for (Monstruo mm : this.monstruos) {
                                            if (mm.getNombre().equals(nombre)) { restored.add(mm); break; }
                                        }
                                    }
                                } catch (Exception e) { }
                            }
                            if (!restored.isEmpty()) {
                                this.ordenSiguiente = restored;
                                try { vista.mostrarOrdenTurnos(this.ordenSiguiente); } catch (Exception e) { }
                            }
                        }
                        try {
                            for (PersonajeJugable h : this.heroes) {
                                if (h.getNombre().equals(heroe.getNombre())) { heroe = h; break; }
                            }
                        } catch (Exception e) { }
                        accionCompleta = true;
                    } catch (Exception ex) {
                        vista.mostrarMensaje("Error cargando la partida: " + ex.getMessage());
                    }
                    break;
                    
                case 7: 
                    if (gestorDeshacer.puedoDeshacer()) {
                        EstadoCombate estadoAnterior = gestorDeshacer.deshacer();
                        if (estadoAnterior != null) {
                            gestorDeshacer.restaurarEstado(estadoAnterior, heroes, monstruos, inventario);
                            vista.mostrarMensaje("✓ Acción deshecha: " + estadoAnterior.getDescripcionAccion());
                            vista.mostrarEstadoInicial(heroes, monstruos);
                            historial.registrarMensaje("DESHACER: " + estadoAnterior.getDescripcionAccion());
                        }
                    } else {
                        vista.mostrarMensaje("✗ No hay acciones para deshacer.");
                    }
                    break;
                    
                case 8: 
                    if (gestorDeshacer.puedoRehacer()) {
                        EstadoCombate estadoRehecho = gestorDeshacer.rehacer();
                        if (estadoRehecho != null) {
                            gestorDeshacer.restaurarEstado(estadoRehecho, heroes, monstruos, inventario);
                            vista.mostrarMensaje("✓ Acción rehecha: " + estadoRehecho.getDescripcionAccion());
                            vista.mostrarEstadoInicial(heroes, monstruos);
                            historial.registrarMensaje("REHACER: " + estadoRehecho.getDescripcionAccion());
                        }
                    } else {
                        vista.mostrarMensaje("✗ No hay acciones para rehacer.");
                    }
                    break;
                    
                case 9: 
                    List<String> historicoDeshacer = gestorDeshacer.obtenerHistorialDeshacer(5);
                    if (historicoDeshacer.isEmpty()) {
                        vista.mostrarMensaje("No hay historial de acciones aún.");
                    } else {
                        vista.mostrarMensaje("\n=== Últimas 5 acciones ===");
                        for (String accionHist : historicoDeshacer) {
                            vista.mostrarMensaje("  • " + accionHist);
                        }
                    }
                    vista.mostrarMensaje("\n" + gestorDeshacer.obtenerEstadisticas());
                    break;
                    
                case 10: 
                    menuGestionInventario(heroe);
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
        
        String nombreMonstruo = displayNombre(m);
        vista.mostrarTurnoActual(contadorTurnos, nombreMonstruo);
        
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
                vista.mostrarAtaque(displayNombre(m) + " (" + estadoActual + ")", m.getTipoMostruo() != null ? m.getTipoMostruo().toString() : "", displayNombre(objetivo), danio);
                try { historial.registrarAtaque(displayNombre(m), m.getTipoMostruo() != null ? m.getTipoMostruo().toString() : "", displayNombre(objetivo), danio); } catch (Exception e) {}
            } else if (estadoAntes == Estado.ATURDIDO || estadoAntes == Estado.CONGELADO) {
                vista.mostrarTurnoPerdido(displayNombre(m) + " (" + estadoActual + ")");
            }

            int hpMonstruoDespues = m.getHP();
            if (hpMonstruoDespues < hpMonstruoAntes) {
                int danoMon = hpMonstruoAntes - hpMonstruoDespues;
                EstadoEfecto efe = m.getEstadoEfecto();
                String causa = "efecto";
                if (efe != null) {
                    causa = efe.getTipo() == Estado.ENVENENADO ? "VENENO" : efe.getTipo().toString();
                }
                vista.mostrarMensaje(displayNombre(m) + " (" + estadoActual + ") sufre " + danoMon + " de daño por " + causa + ".");
                if (efe != null) {
                    vista.mostrarEstadoCambio(displayNombre(m), efe.getTipo().toString(), efe.getTurnos());
                }
                if (hpMonstruoDespues <= 0) {
                    vista.mostrarMensaje(displayNombre(m) + " ha sido derrotado por los efectos.");
                }
            }

            vista.mostrarEstado(displayNombre(m), m.getHP(), objetivo != null ? displayNombre(objetivo) : "", objetivo != null ? objetivo.getHP() : 0);
            try { vista.mostrarOrdenTurnos(generarOrdenTurnos()); } catch (Exception e) { }
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
            try { vista.mostrarOrdenTurnos(generarOrdenTurnos()); } catch (Exception e) { }
        }

        vista.pedirEnter();
    }
}
