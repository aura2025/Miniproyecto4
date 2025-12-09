package Vista;

import Modelo.Personaje;
import java.util.Scanner;
import java.util.List;
import Modelo.PersonajeJugable;
import Modelo.Monstruo;

public class VistaTerminal implements Vista {
    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenuAcciones(Personaje p) {
        String nombre = (p != null && p.getNombre() != null) ? p.getNombre() : "<sin nombre>";
        int hp = (p != null) ? p.getHP() : 0;
        int hpMax = (p != null) ? p.getHPMaximo() : 0;
    
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║   ACCIONES - " + nombre + " (HP " + hp + "/" + hpMax + ")");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  1) Atacar                                   ║");
        System.out.println("║  2) Poder especial                           ║");
        System.out.println("║  3) Defender                                 ║");
        System.out.println("║  4) Usar ítem                                ║");
        System.out.println("║  5) Guardar partida                          ║");
        System.out.println("║  6) Cargar partida                           ║");
        System.out.println("║ ──────────────────────────────────────────── ║");
        System.out.println("║  7) DESHACER última acción                   ║");
        System.out.println("║  8) REHACER acción deshecha                  ║");
        System.out.println("║  9) Ver historial de acciones                ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.print("Selecciona una opción (1-9): ");

    String line = scanner.nextLine();
    int opcion = Integer.parseInt(line.trim());
    if (opcion < 1 || opcion > 9) opcion = 1;
    return opcion;
}
    public void mostrarEstado(String atacanteNombre, int atacanteHP, String defensorNombre, int defensorHP) {
        System.out.println("\n=== Estado (resumen) ===");
        if (atacanteNombre != null && !atacanteNombre.isEmpty()) {
            System.out.println(atacanteNombre + " - HP: " + atacanteHP);
        } else {
            System.out.println("Atacante: <no disponible>");
        }
        if (defensorNombre != null && !defensorNombre.isEmpty()) {
            System.out.println(defensorNombre + " - HP: " + defensorHP);
        } else {
            System.out.println("Defensor: <no disponible>");
        }
    }

    public void mostrarMensaje(String msg) {
        System.out.println(msg == null ? "" : msg);
    }

    public void mostrarAtaque(String actor, String categoria, String objetivo, int danio) {
        String cat = (categoria == null || categoria.isEmpty()) ? "" : " (" + categoria + ")";
        System.out.println((actor == null ? "<desconocido>" : actor) + cat + " ataca a " + (objetivo == null ? "<desconocido>" : objetivo) + " e inflige " + danio + " de daño.");
    }

    public void mostrarTurnoPerdido(String sujeto) {
        System.out.println((sujeto == null ? "<sujeto>" : sujeto) + " pierde el turno.");
    }

    public void mostrarEstadoCambio(String sujeto, String estado, int duracion) {
        System.out.println((sujeto == null ? "<sujeto>" : sujeto) + " ahora está " + (estado == null ? "<estado>" : estado) + " por " + duracion + " turno(s).");
    }
    
    @Override
    public void mostrarTurnoActual(int numeroTurno, String nombrePersonaje) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        TURNO ACTUAL                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║  Turno #" + numeroTurno + " - " + nombrePersonaje);
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    public void pedirEnter() {
        System.out.println("Presiona ENTER para continuar...");
        scanner.nextLine();
    }


    public void mostrarBanner(String texto) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          " + texto + "          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    public void mostrarEstadoInicial(List<PersonajeJugable> heroes, List<Monstruo> monstruos) {
        System.out.println("\n┌─────────────────── HÉROES ───────────────────┐");
        for (PersonajeJugable h : heroes) {
            System.out.println("│ " + h.getNombre() + " - HP: " + h.getHP() + 
                " | Atk: " + h.getAtaque() + " | Def: " + h.getDefensa() + 
                " | Vel: " + h.getVelocidad() + " | Arma: " + (h.getTipoPersonaje()!=null?"":""));
        }
        System.out.println("└──────────────────────────────────────────────┘");
        
        System.out.println("\n┌─────────────────── ENEMIGOS ─────────────────┐");
        for (Monstruo m : monstruos) {
            System.out.println("│ " + m.getNombre() + " (" + m.getTipoMostruo() + ") - HP: " + m.getHP() + 
                " | Atk: " + m.getAtaque() + " | Def: " + m.getDefensa() + 
                " | Vel: " + m.getVelocidad());
        }
        System.out.println("└──────────────────────────────────────────────┘\n");
    }

    public void mostrarOrdenTurnos(List<Personaje> orden) {
        System.out.println("\nOrden de acción:");
        for (Personaje p : orden) {
            if (p.getHP() > 0) {
                System.out.println(" - " + p.getNombre() + " (Vel " + p.getVelocidad() + ")");
            }
        }
    }

    public void mostrarInfoHeroe(PersonajeJugable pj) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  " + pj.getNombre() + " - TURNO");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║  HP: " + pj.getHP() + "/" + pj.getHPMaximo() + " | MP: " + pj.getMP() + "/" + pj.getMPMaximo());
        System.out.println("║  Ataque: " + pj.getAtaque() + " | Defensa: " + pj.getDefensa() + " | Velocidad: " + pj.getVelocidad());
        System.out.println("║  Estado: " + pj.getEstado());
        if (pj.getEstadoEfecto() != null) {
            System.out.println("║    └─ Dura " + pj.getEstadoEfecto().getTurnos() + " turno(s) más");
        }
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    public int leerOpcionSegura(int min, int max) {
        int opcion = -1;
        while (opcion < min || opcion > max) {
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                if (opcion < min || opcion > max) System.out.print(" Opción inválida. Intenta de nuevo: ");
            } else {
                scanner.next();
                System.out.print(" Entrada inválida. Ingresa un número: ");
            }
        }
        scanner.nextLine(); 
        return opcion;
    }

    public int seleccionarItem(List<String> nombresItems) {
        if (nombresItems == null || nombresItems.isEmpty()) return -1;
        System.out.println("\nInventario:");
        System.out.println("0) Cancelar");
        for (int i = 0; i < nombresItems.size(); i++) {
            System.out.println((i+1) + ") " + nombresItems.get(i));
        }
        System.out.print("\nSelecciona un item (0 para cancelar): ");
        int sel = leerOpcionSegura(0, nombresItems.size());
        return sel == 0 ? -1 : sel - 1;
    }

    public int seleccionarObjetivoMonstruos(List<String> nombresMonstruos) {
        if (nombresMonstruos == null || nombresMonstruos.isEmpty()) return -1;
        System.out.println("\nSelecciona enemigo:");
        for (int i = 0; i < nombresMonstruos.size(); i++) {
            System.out.println((i+1) + ") " + nombresMonstruos.get(i));
        }
        System.out.print("\nSelecciona objetivo: ");
        int sel = leerOpcionSegura(1, nombresMonstruos.size());
        return sel - 1;
    }

    public int seleccionarObjetivoHeroes(List<String> nombresHeroes) {
        if (nombresHeroes == null || nombresHeroes.isEmpty()) return -1;
        System.out.println("\nSelecciona héroe objetivo:");
        for (int i = 0; i < nombresHeroes.size(); i++) {
            System.out.println((i+1) + ") " + nombresHeroes.get(i));
        }
        System.out.print("\nSelecciona objetivo: ");
        int sel = leerOpcionSegura(1, nombresHeroes.size());
        return sel - 1;
    }
    
    @Override
    public int mostrarMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       MENÚ PRINCIPAL DEL GREMIO        ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1) Iniciar Batalla                    ║");
        System.out.println("║  2) Atención en el Gremio              ║");
        System.out.println("║  3) Salir                              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Selecciona una opción (1-3): ");
        return leerOpcionSegura(1, 3);
    }
    
    @Override
    public int mostrarMenuGremio() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      ATENCIÓN EN EL GREMIO (FIFO)     ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1) Agregar aventurero a la cola      ║");
        System.out.println("║  2) Atender siguiente aventurero      ║");
        System.out.println("║  3) Ver fila actual                   ║");
        System.out.println("║  4) Volver al menú principal          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Selecciona una opción (1-4): ");
        return leerOpcionSegura(1, 4);
    }
    
    @Override
    public int seleccionarAventurero(List<PersonajeJugable> heroes) {
        System.out.println("\n=== Selecciona el aventurero a agregar ===");
        for (int i = 0; i < heroes.size(); i++) {
            PersonajeJugable h = heroes.get(i);
            System.out.println((i+1) + ". " + h.getNombre() + " (Poder: " + h.getPoderEspecial() + ")");
        }
        System.out.println((heroes.size() + 1) + ". Cancelar");
        System.out.print("\nSelecciona (1-" + (heroes.size() + 1) + "): ");
        return leerOpcionSegura(1, heroes.size() + 1);
    }
    
    @Override
    public void mostrarAventureroAtendido(PersonajeJugable aventurero, int restantes) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    ATENDIENDO AVENTURERO (FIFO)       ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n✓ Aventurero atendido: " + aventurero.getNombre());
        System.out.println("  - Poder Especial: " + aventurero.getPoderEspecial());
        System.out.println("  - HP: " + aventurero.getHP() + "/" + aventurero.getHPMaximo());
        System.out.println("  - MP: " + aventurero.getMP() + "/" + aventurero.getMPMaximo());
        System.out.println("\nAventureros restantes en cola: " + restantes);
    }
    
    @Override
    public void mostrarFilaGremio(String filaTexto) {
        System.out.println("\n" + filaTexto);
    }
    @Override
    public void mostrarInventarioHeroe(PersonajeJugable heroe) {
      
        throw new UnsupportedOperationException("Unimplemented method 'mostrarInventarioHeroe'");
    }
}
