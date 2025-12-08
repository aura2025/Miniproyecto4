package Modelo;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class Partida {

    public static class PartidaData {
        public List<PersonajeJugable> heroes;
        public List<Monstruo> monstruos;
        public Inventario inventario;
        public HistorialBatallas historial;
        public String proximoActorTipo;
        public String proximoActorNombre;
        public java.util.List<String> ordenGuardada;
    }

    public static void guardar(List<PersonajeJugable> heroes, List<Monstruo> monstruos, Inventario inventario, HistorialBatallas historial, String proximoActorTipo, String proximoActorNombre, java.util.List<Personaje> ordenActual, Path path) throws IOException {
        List<String> lines = new Vector<>();
        lines.add("VERSION:1");
        lines.add("CURRENT:" + (proximoActorTipo == null ? "" : proximoActorTipo) + "|" + (proximoActorNombre == null ? "" : proximoActorNombre));
        lines.add("HEROES_COUNT:" + heroes.size());
        for (PersonajeJugable h : heroes) {
            StringBuilder b = new StringBuilder();
            b.append("HERO|");
            b.append(escape(h.getNombre())).append("|");
            b.append(h.getHP()).append("|").append(h.getHPMaximo()).append("|");
            b.append(h.getDefensa()).append("|").append(h.getAtaque()).append("|");
            b.append(h.getVelocidad()).append("|");
            b.append(h.getMP()).append("|");
            b.append(h.getMPMaximo()).append("|");
            b.append(h.getTipoPersonaje() == null ? "" : h.getTipoPersonaje().toString()).append("|");
            b.append(h.getPoderEspecial() == null ? "" : h.getPoderEspecial().toString()).append("|");
            b.append(h.getEstado() == null ? "" : h.getEstado().toString()).append("|");
            // estadoEfecto
            EstadoEfecto ef = h.getEstadoEfecto();
            if (ef != null) b.append(ef.getTipo()).append(",").append(ef.getTurnos()).append(",").append(ef.getValorDebilitacion());
            b.append("|");
            // buffs: count and serialized list tipo,valor,turnos separated by ';'
            java.util.List<Buff> buffs = h.getBuffs();
            b.append(buffs == null ? 0 : buffs.size()).append("|");
            if (buffs != null && !buffs.isEmpty()) {
                StringBuilder sbuff = new StringBuilder();
                for (Buff bf : buffs) {
                    if (sbuff.length() > 0) sbuff.append(";");
                    sbuff.append(bf.getTipo()).append(",").append(bf.getValor()).append(",").append(bf.getTurnos());
                }
                b.append(escape(sbuff.toString()));
            }
            lines.add(b.toString());
        }

        lines.add("MONSTROS_COUNT:" + monstruos.size());
        for (Monstruo m : monstruos) {
            StringBuilder b = new StringBuilder();
            b.append("MONSTRO_FULL|");
            b.append(escape(m.getNombre())).append("|");
            b.append(m.getHP()).append("|").append(m.getHPMaximo()).append("|");
            b.append(m.getDefensa()).append("|").append(m.getAtaque()).append("|");
            b.append(m.getVelocidad()).append("|");
            int mpField = 0;
            int mpMaxField = 0;
            b.append(mpField).append("|").append(mpMaxField).append("|");
            b.append(m.getEstado() == null ? "" : m.getEstado().toString()).append("|");
            EstadoEfecto mef = m.getEstadoEfecto();
            if (mef != null) b.append(mef.getTipo()).append(",").append(mef.getTurnos()).append(",").append(mef.getValorDebilitacion());
            b.append("|");
            b.append(m.getTipoMostruo() == null ? "" : m.getTipoMostruo().toString());
            lines.add(b.toString());
        }

        // inventario compartido
        lines.add("INVENTARIO_COUNT:" + inventario.getTamanio());
        for (int i = 1; i <= inventario.getTamanio(); i++) {
            Item it = inventario.getItemPorIndice(i);
            if (it == null) continue;
            lines.add("ITEM|" + it.toString() + "|" + inventario.getCantidad(it));
        }

        // ═══════════════════════════════════════════════════════════════════════════
        // NUEVO: INVENTARIOS INDIVIDUALES DE HÉROES
        // ═══════════════════════════════════════════════════════════════════════════
        lines.add("INVENTARIOS_INDIVIDUALES_COUNT:" + heroes.size());
        for (PersonajeJugable h : heroes) {
            StringBuilder invLine = new StringBuilder();
            invLine.append("INVENTARIO_INDIVIDUAL|");
            invLine.append(escape(h.getNombre())).append("|");
            
            InventarioIndividual inv = h.getInventarioIndividual();
            if (inv != null && !inv.estaVacio()) {
                invLine.append(inv.getCapacidadMaxima()).append("|");
                
                // Serializar ítems: ITEM_NAME:CANTIDAD;ITEM_NAME:CANTIDAD;...
                java.util.Map<Item, Integer> itemsMap = inv.obtenerItemsMap();
                StringBuilder itemsStr = new StringBuilder();
                for (java.util.Map.Entry<Item, Integer> entry : itemsMap.entrySet()) {
                    if (itemsStr.length() > 0) itemsStr.append(";");
                    itemsStr.append(entry.getKey().toString()).append(":").append(entry.getValue());
                }
                invLine.append(escape(itemsStr.toString()));
            } else {
                invLine.append("5|"); // Capacidad por defecto, sin ítems
            }
            
            lines.add(invLine.toString());
        }

        // historial
        List<String> eventos = historial.getEventos();
        lines.add("HISTORIAL_COUNT:" + eventos.size());
        for (String ev : eventos) {
            lines.add("HISTORIAL|" + escape(ev));
        }

        // orden de turnos guardada (opcional)
        if (ordenActual != null) {
            lines.add("ORDER_COUNT:" + ordenActual.size());
            for (Personaje p : ordenActual) {
                if (p instanceof PersonajeJugable) {
                    lines.add("ORDER|HERO|" + escape(p.getNombre()));
                } else if (p instanceof Monstruo) {
                    lines.add("ORDER|MONSTRO|" + escape(p.getNombre()));
                }
            }
        }

        // velocidades actuales (heroes + monstruos) para restaurar exactamente el turno
        List<String> velLines = new Vector<>();
        for (PersonajeJugable h : heroes) {
            velLines.add("VEL|HERO|" + escape(h.getNombre()) + "|" + h.getVelocidad());
        }
        for (Monstruo m : monstruos) {
            velLines.add("VEL|MONSTRO|" + escape(m.getNombre()) + "|" + m.getVelocidad());
        }
        if (!velLines.isEmpty()) {
            lines.add("VEL_COUNT:" + velLines.size());
            lines.addAll(velLines);
        }

        Files.createDirectories(path.getParent());
        Files.write(path, lines, java.nio.charset.StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static PartidaData cargar(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);
        PartidaData data = new PartidaData();
        data.heroes = new Vector<>();
        data.monstruos = new Vector<>();
        data.inventario = new Inventario();
        data.historial = new HistorialBatallas();

        int idx = 0;
        if (idx >= lines.size()) return data;
        // VERSION
        String v = lines.get(idx++);
        // CURRENT
        if (idx >= lines.size()) return data;
        String cur = lines.get(idx++);
        if (cur.startsWith("CURRENT:")) {
            String rest = cur.substring("CURRENT:".length());
            String[] parts = rest.split("\\|", -1);
            data.proximoActorTipo = parts.length > 0 ? parts[0] : "";
            data.proximoActorNombre = parts.length > 1 ? parts[1] : "";
        }

        // HEROES
        if (idx >= lines.size()) return data;
        String hc = lines.get(idx++);
        int heroesCount = 0;
        if (hc.startsWith("HEROES_COUNT:")) heroesCount = Integer.parseInt(hc.substring("HEROES_COUNT:".length()));
        for (int i = 0; i < heroesCount && idx < lines.size(); i++) {
            String l = lines.get(idx++);
            if (!l.startsWith("HERO|")) continue;
            String[] f = l.substring(5).split("\\|", -1);
            String nombre = unescape(f[0]).trim();
            int hp = parseIntSafe(f,1,100);
            int hpMax = parseIntSafe(f,2, hp);
            int defensa = parseIntSafe(f,3,0);
            int ataque = parseIntSafe(f,4,0);
            int velocidad = parseIntSafe(f,5,0);
            int mp = parseIntSafe(f,6,0);
            int mpMax = parseIntSafe(f,7,mp);
            PersonajeJugable pj = new PersonajeJugable(TipoPersonaje.Jugable, nombre, hpMax, defensa, ataque, velocidad, Estado.NORMAL, Arma.ESPADA, mpMax, null);
            pj.setHP(hp);
            pj.setMP(mp);
            if (f.length > 9 && f[9] != null && !f[9].isEmpty()) {
                try { pj.setPoderEspecial(PoderEspecial.valueOf(f[9])); } catch (Exception e) {}
            }
            if (f.length > 10 && f[10] != null && !f[10].isEmpty()) {
                try { pj.setEstado(Estado.valueOf(f[10])); } catch (Exception e) {}
            }
            // estadoEfecto
            if (f.length > 11 && f[11] != null && !f[11].isEmpty()) {
                try {
                    String[] efp = f[11].split(",", -1);
                    if (efp.length >= 2) {
                        Estado et = Estado.valueOf(efp[0]);
                        int turnos = Integer.parseInt(efp[1]);
                        int val = efp.length > 2 ? parseIntSafe(efp,2,0) : 0;
                        pj.setEstado(et);
                        pj.setEstadoConDebilitacion(val, turnos);
                    }
                } catch (Exception e) {}
            }
            // buffs
            if (f.length > 12 && f[12] != null && !f[12].isEmpty()) {
                int bcount = parseIntSafe(f,12,0);
                if (bcount > 0 && f.length > 13 && f[13] != null && !f[13].isEmpty()) {
                    String sb = unescape(f[13]);
                    String[] parts = sb.split(";", -1);
                    java.util.List<Buff> buffs = new java.util.Vector<>();
                    for (String part : parts) {
                        if (part == null || part.isEmpty()) continue;
                        String[] bf = part.split(",", -1);
                        try {
                            TipoBuff tb = TipoBuff.valueOf(bf[0]);
                            int val = parseIntSafe(bf,1,0);
                            int turns = parseIntSafe(bf,2,0);
                            buffs.add(new Buff(tb, val, turns));
                        } catch (Exception e) { }
                    }
                    pj.setBuffs(buffs);
                }
            }
            data.heroes.add(pj);
        }

        // MONSTRUOS
        if (idx >= lines.size()) return data;
        String mc = lines.get(idx++);
        int monCount = 0;
        if (mc.startsWith("MONSTROS_COUNT:")) monCount = Integer.parseInt(mc.substring("MONSTROS_COUNT:".length()));
        for (int i = 0; i < monCount && idx < lines.size(); i++) {
            String l = lines.get(idx++);
            if (l.startsWith("MONSTRO_FULL|")) {
                String[] f = l.substring("MONSTRO_FULL|".length()).split("\\|", -1);
                String nombre = f.length > 0 ? unescape(f[0]).trim() : "";
                int hp = parseIntSafe(f,1,100);
                int hpMax = parseIntSafe(f,2,hp);
                int defensa = parseIntSafe(f,3,0);
                int ataque = parseIntSafe(f,4,0);
                int velocidad = parseIntSafe(f,5,0);
                Monstruo m = new Monstruo(TipoPersonaje.Monstruo, nombre, hpMax, defensa, ataque, velocidad, Estado.NORMAL, null);
                m.setHP(hp);
                if (f.length > 8 && f[8] != null && !f[8].isEmpty()) {
                    try { m.setEstado(Estado.valueOf(f[8])); } catch (Exception e) {}
                }
                if (f.length > 9 && f[9] != null && !f[9].isEmpty()) {
                    try {
                        String[] me = f[9].split(",", -1);
                        if (me.length >= 2) {
                            Estado et = Estado.valueOf(me[0]);
                            int turns = parseIntSafe(me,1,0);
                            int val = me.length > 2 ? parseIntSafe(me,2,0) : 0;
                            if (et == Estado.DEBILITADO) {
                                m.setEstadoConDebilitacion(val, turns);
                            } else {
                                m.setEstadoConDuracion(et, turns);
                            }
                        }
                    } catch (Exception e) {}
                }
                if (f.length > 10 && f[10] != null && !f[10].isEmpty()) {
                    try { m.setTipoMostruo(TipoMonstruo.valueOf(f[10])); } catch (Exception e) {}
                }
                data.monstruos.add(m);
            } else if (l.startsWith("MONSTRO|")) {
                String[] f = l.substring(7).split("\\|", -1);
                String nombre = unescape(f[0]).trim();
                int hp = parseIntSafe(f,1,100);
                int hpMax = parseIntSafe(f,2,hp);
                int defensa = parseIntSafe(f,3,0);
                int ataque = parseIntSafe(f,4,0);
                int velocidad = parseIntSafe(f,5,0);
                Monstruo m = new Monstruo(TipoPersonaje.Monstruo, nombre, hpMax, defensa, ataque, velocidad, Estado.NORMAL, null);
                m.setHP(hp);
                if (f.length > 6 && f[6] != null && !f[6].isEmpty()) {
                    try { m.setEstado(Estado.valueOf(f[6])); } catch (Exception e) {}
                }
                if (f.length > 7 && f[7] != null && !f[7].isEmpty()) {
                    try {
                        String[] me = f[7].split(",", -1);
                        if (me.length >= 2) {
                            Estado et = Estado.valueOf(me[0]);
                            int turns = parseIntSafe(me,1,0);
                            int val = me.length > 2 ? parseIntSafe(me,2,0) : 0;
                            if (et == Estado.DEBILITADO) {
                                m.setEstadoConDebilitacion(val, turns);
                            } else {
                                m.setEstadoConDuracion(et, turns);
                            }
                        }
                    } catch (Exception e) {}
                }
                if (f.length > 8 && f[8] != null && !f[8].isEmpty()) {
                    try { m.setTipoMostruo(TipoMonstruo.valueOf(f[8])); } catch (Exception e) {}
                }
                data.monstruos.add(m);
            }
        }

        // INVENTARIO COMPARTIDO
        if (idx < lines.size()) {
            String invc = lines.get(idx++);
            if (invc.startsWith("INVENTARIO_COUNT:")) {
                int tamanio = Integer.parseInt(invc.substring("INVENTARIO_COUNT:".length()));
                for (int i = 0; i < tamanio && idx < lines.size(); i++) {
                    String l = lines.get(idx++);
                    if (!l.startsWith("ITEM|")) continue;
                    String[] f = l.substring(5).split("\\|", -1);
                    try {
                        Item it = Item.valueOf(f[0]);
                        int cant = Integer.parseInt(f[1]);
                        data.inventario.agregarItem(it, cant);
                    } catch (Exception e) { }
                }
            }
        }

        // ═══════════════════════════════════════════════════════════════════════════
        // NUEVO: INVENTARIOS INDIVIDUALES
        // ═══════════════════════════════════════════════════════════════════════════
        if (idx < lines.size()) {
            String maybeInv = lines.get(idx);
            if (maybeInv != null && maybeInv.startsWith("INVENTARIOS_INDIVIDUALES_COUNT:")) {
                String invCountLine = lines.get(idx++);
                int invCount = Integer.parseInt(invCountLine.substring("INVENTARIOS_INDIVIDUALES_COUNT:".length()));
                
                java.util.Map<String, InventarioIndividual> inventariosPorNombre = new java.util.HashMap<>();
                
                for (int i = 0; i < invCount && idx < lines.size(); i++) {
                    String l = lines.get(idx++);
                    if (!l.startsWith("INVENTARIO_INDIVIDUAL|")) continue;
                    
                    String[] parts = l.substring("INVENTARIO_INDIVIDUAL|".length()).split("\\|", -1);
                    if (parts.length < 2) continue;
                    
                    String nombreHeroe = unescape(parts[0]).trim();
                    int capacidad = parts.length > 1 ? parseIntSafe(parts, 1, 5) : 5;
                    
                    InventarioIndividual inv = new InventarioIndividual(nombreHeroe, capacidad);
                    
                    // Deserializar ítems
                    if (parts.length > 2 && parts[2] != null && !parts[2].isEmpty()) {
                        String itemsStr = unescape(parts[2]);
                        String[] itemsPairs = itemsStr.split(";");
                        
                        for (String pair : itemsPairs) {
                            if (pair == null || pair.isEmpty()) continue;
                            String[] itemData = pair.split(":");
                            if (itemData.length < 2) continue;
                            
                            try {
                                Item item = Item.valueOf(itemData[0]);
                                int cantidad = Integer.parseInt(itemData[1]);
                                inv.agregarItem(item, cantidad);
                            } catch (Exception e) {
                                // Ítem inválido, ignorar
                            }
                        }
                    }
                    
                    inventariosPorNombre.put(nombreHeroe, inv);
                }
                
                // Asignar inventarios a héroes
                for (PersonajeJugable h : data.heroes) {
                    InventarioIndividual inv = inventariosPorNombre.get(h.getNombre());
                    if (inv != null) {
                        h.setInventarioIndividual(inv);
                    } else {
                        // Si no hay inventario guardado, crear uno vacío
                        h.setInventarioIndividual(new InventarioIndividual(h.getNombre(), 5));
                    }
                }
            }
        }

        // Si no se encontró la sección de inventarios, crear inventarios vacíos para todos
        if (data.heroes != null) {
            for (PersonajeJugable h : data.heroes) {
                if (h.getInventarioIndividual() == null) {
                    h.setInventarioIndividual(new InventarioIndividual(h.getNombre(), 5));
                }
            }
        }

        // HISTORIAL
        if (idx < lines.size()) {
            String hc2 = lines.get(idx++);
            if (hc2.startsWith("HISTORIAL_COUNT:")) {
                int hcount = Integer.parseInt(hc2.substring("HISTORIAL_COUNT:".length()));
                for (int i = 0; i < hcount && idx < lines.size(); i++) {
                    String l = lines.get(idx++);
                    if (!l.startsWith("HISTORIAL|")) continue;
                    data.historial.nuevoEvento(unescape(l.substring("HISTORIAL|".length())));
                }
            }
        }

        // ORDER (optional)
        if (idx < lines.size()) {
            String maybe = lines.get(idx);
            if (maybe.startsWith("ORDER_COUNT:")) {
                String oc = lines.get(idx++);
                int ocount = Integer.parseInt(oc.substring("ORDER_COUNT:".length()));
                data.ordenGuardada = new java.util.Vector<>();
                for (int i = 0; i < ocount && idx < lines.size(); i++) {
                    String l = lines.get(idx++);
                    if (!l.startsWith("ORDER|")) continue;
                    String rest = l.substring("ORDER|".length());
                    String[] op = rest.split("\\|", -1);
                    if (op.length >= 2) {
                        String tipo = op[0];
                        String nombreEsc = op[1];
                        String nombre = unescape(nombreEsc).trim();
                        data.ordenGuardada.add(tipo + "|" + nombre);
                    } else {
                        data.ordenGuardada.add(rest);
                    }
                }
            }
        }

        // VEL (optional)
        if (idx < lines.size()) {
            String maybeVel = lines.get(idx);
            if (maybeVel != null && maybeVel.startsWith("VEL_COUNT:")) {
                String vc = lines.get(idx++);
                int vcount = Integer.parseInt(vc.substring("VEL_COUNT:".length()));
                java.util.Map<String, Integer> velMap = new java.util.HashMap<>();
                for (int i = 0; i < vcount && idx < lines.size(); i++) {
                    String l = lines.get(idx++);
                    if (!l.startsWith("VEL|")) continue;
                    String[] parts = l.substring("VEL|".length()).split("\\|", -1);
                    if (parts.length >= 3) {
                        String tipo = parts[0];
                        String nombre = unescape(parts[1]).trim();
                        int vel = parseIntSafe(parts,2,0);
                        velMap.put(tipo + "|" + nombre, vel);
                    }
                }
                if (velMap.size() > 0) {
                    for (PersonajeJugable h : data.heroes) {
                        String key = "HERO|" + (h.getNombre() == null ? "" : h.getNombre());
                        Integer vv = velMap.get(key);
                        if (vv != null) {
                            int vHero = vv.intValue();
                            try { h.setVelocidad(vHero); h.setVelocidadBase(vHero); } catch (Exception e) {}
                        }
                    }
                    for (Monstruo m : data.monstruos) {
                        String key = "MONSTRO|" + (m.getNombre() == null ? "" : m.getNombre());
                        Integer vv2 = velMap.get(key);
                        if (vv2 != null) {
                            int vMon = vv2.intValue();
                            try { m.setVelocidad(vMon); } catch (Exception e) {}
                        }
                    }
                }
            }
        }

        if (data.monstruos != null) {
            java.util.List<String> monsterNames = new java.util.Vector<>();
            if (data.ordenGuardada != null) {
                for (String e : data.ordenGuardada) {
                    try {
                        String[] p = e.split("\\|", -1);
                        if (p.length >= 2 && p[0].toUpperCase().contains("MONST")) {
                            String nm = p[1];
                            if (nm != null && !nm.trim().isEmpty()) monsterNames.add(nm.trim());
                        }
                    } catch (Exception ex) {}
                }
            }

            for (Monstruo m : data.monstruos) {
                if (m.getNombre() == null || m.getNombre().trim().isEmpty()) {
                    String candidate = null;
                    for (int i = 0; i < monsterNames.size(); i++) {
                        String nm = monsterNames.get(i);
                        boolean used = false;
                        for (Monstruo mm : data.monstruos) {
                            if (mm.getNombre() != null && mm.getNombre().equals(nm)) { used = true; break; }
                        }
                        if (used) continue;
                        candidate = nm; monsterNames.remove(i); break;
                    }
                    if (candidate != null) {
                        try { m.setNombre(candidate); } catch (Exception ex) {}
                    } else {
                        try {
                            if (m.getTipoMostruo() != null) m.setNombre(m.getTipoMostruo().toString());
                            else m.setNombre("Monstruo");
                        } catch (Exception ex) { }
                    }
                }
            }
        }

        return data;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("|", "\\|").replace("\n", "\\n");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n").replace("\\|", "|").replace("\\\\", "\\");
    }

    private static int parseIntSafe(String[] arr, int idx, int def) {
        if (arr.length <= idx) return def;
        try { return Integer.parseInt(arr[idx]); } catch (Exception e) { return def; }
    }
}