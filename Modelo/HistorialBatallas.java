package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


public class HistorialBatallas {
    private final Queue<String> eventos = new LinkedList<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void clear() {
        eventos.clear();
    }

    public void nuevoEvento(String texto) {
        String ts = LocalDateTime.now().format(fmt);
        eventos.offer(ts + " - " + texto);
    }

    public void registrarAtaque(String atacante, String categoria, String objetivo, int danio) {
        String cat = (categoria == null || categoria.isEmpty()) ? "" : " (" + categoria + ")";
        nuevoEvento(atacante + cat + " ataca a " + objetivo + ": " + danio + " daño");
    }

    public void registrarPoder(String usuario, String poder, String objetivo) {
        nuevoEvento(usuario + " usa poder " + poder + " sobre " + objetivo);
    }

    public void registrarItem(String usuario, String item, String objetivo) {
        nuevoEvento(usuario + " usa ítem " + item + " sobre " + objetivo);
    }

    public void registrarMensaje(String mensaje) {
        nuevoEvento(mensaje);
    }

    public List<String> getEventos() {
        return Collections.unmodifiableList(new Vector<>(eventos));
    }
    
    public Queue<String> getEventosQueue() {
        return new LinkedList<>(eventos);
    }
    
    public String obtenerPrimerEvento() {
        return eventos.peek();
    }
    
    public String obtenerYEliminarPrimerEvento() {
        return eventos.poll();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String e : eventos) {
            sb.append(e).append(System.lineSeparator());
        }
        return sb.toString();
    }

  
     
    public Path guardarEnArchivo(String resultado) throws IOException {
        String token = (resultado == null || resultado.isEmpty()) ? "batalla" : resultado.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_-]", "");
        String nombre = "historial_" + token + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        Path dir = Paths.get("historiales");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        Path ruta = dir.resolve(nombre);
        Files.write(ruta, eventos, StandardCharsets.UTF_8);
        return ruta;
    }
}
