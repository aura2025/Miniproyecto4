package Vista;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VistaGui extends JFrame implements Vista {

    private JTextArea areaLog;
    private JPanel heroesContainer;
    private JPanel monstruosContainer;
    private JButton btnAtacar, btnPoder, btnDefender, btnItem;

    
    private final Color CONTENT_BG = new Color(20, 20, 20);
    private final Color HERO_BG = new Color(10, 70, 10); 
    private final Color MONSTER_BG = new Color(70, 10, 10); 
    private final Color CARD_OVERLAY = new Color(60, 60, 60, 160); 
    private final Color BORDER_GRAY = new Color(80, 80, 80, 200); 

    
    private int opcionSeleccionada = -1;

    private java.util.List<PersonajeJugable> lastHeroes = null;
    private java.util.List<Monstruo> lastMonstruos = null;

    public VistaGui() {
        setTitle("Batalla RPG - MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

    getContentPane().setBackground(CONTENT_BG);


    heroesContainer = new JPanel();
    heroesContainer.setLayout(new BoxLayout(heroesContainer, BoxLayout.Y_AXIS));
    heroesContainer.setBackground(HERO_BG);
    heroesContainer.setOpaque(true);
    JPanel panelHeroes = new JPanel(new BorderLayout());
    panelHeroes.setBackground(HERO_BG);
    panelHeroes.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
    JLabel headerHeroes = new JLabel(" HÉROES ", SwingConstants.CENTER);
    headerHeroes.setForeground(Color.WHITE);
    headerHeroes.setOpaque(true);
    headerHeroes.setBackground(HERO_BG);
    panelHeroes.add(headerHeroes, BorderLayout.NORTH);
    panelHeroes.add(new JScrollPane(heroesContainer), BorderLayout.CENTER);

    panelHeroes.setPreferredSize(new Dimension(420, 0));
    add(panelHeroes, BorderLayout.WEST);

    monstruosContainer = new JPanel();
    monstruosContainer.setLayout(new BoxLayout(monstruosContainer, BoxLayout.Y_AXIS));
    monstruosContainer.setBackground(MONSTER_BG);
    monstruosContainer.setOpaque(true);
    JPanel panelMonstruos = new JPanel(new BorderLayout());
    panelMonstruos.setBackground(MONSTER_BG);

    panelMonstruos.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
    JLabel headerMonstruos = new JLabel(" MONSTRUOS ", SwingConstants.CENTER);
    headerMonstruos.setForeground(Color.WHITE);
    headerMonstruos.setOpaque(true);
    headerMonstruos.setBackground(MONSTER_BG);
    panelMonstruos.add(headerMonstruos, BorderLayout.NORTH);
    panelMonstruos.add(new JScrollPane(monstruosContainer), BorderLayout.CENTER);

    panelMonstruos.setPreferredSize(new Dimension(420, 0));
    add(panelMonstruos, BorderLayout.CENTER);

    JPanel panelAcciones = new JPanel(new GridLayout(4, 1, 5, 5));

    panelAcciones.setBackground(Color.BLACK);
        panelAcciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnAtacar = crearBoton(" Atacar");
        btnPoder = crearBoton(" Poder Especial");
        btnDefender = crearBoton(" Defender");
        btnItem = crearBoton(" Usar Ítem");

    panelAcciones.add(btnAtacar);
    panelAcciones.add(btnPoder);
    panelAcciones.add(btnDefender);
    panelAcciones.add(btnItem);


    panelAcciones.setPreferredSize(new Dimension(160, 0));
    add(panelAcciones, BorderLayout.EAST);


    areaLog = new JTextArea();
        areaLog.setEditable(false);

    areaLog.setBackground(Color.BLACK);
    areaLog.setForeground(Color.WHITE);
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setPreferredSize(new Dimension(1000, 200));
        add(scrollLog, BorderLayout.SOUTH);

   
        btnAtacar.addActionListener(e -> opcionSeleccionada = 1);
        btnPoder.addActionListener(e -> opcionSeleccionada = 2);
        btnDefender.addActionListener(e -> opcionSeleccionada = 3);
        btnItem.addActionListener(e -> opcionSeleccionada = 4);

        setVisible(true);
    }



    @Override
    public void mostrarBanner(String mensaje) {
        log("\n==============================\n" + mensaje + "\n==============================");
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        log(mensaje);
    }

    @Override
    public void mostrarEstadoInicial(List<PersonajeJugable> heroes, List<Monstruo> monstruos) {

        this.lastHeroes = heroes;
        this.lastMonstruos = monstruos;
        actualizarListas(heroes, monstruos);
        log("Estado inicial del combate cargado.");
    }

    @Override
    public void mostrarOrdenTurnos(List<Personaje> orden) {
        StringBuilder sb = new StringBuilder("Orden de turnos:\n");
        for (Personaje p : orden) {
            sb.append(" - ").append(p.getNombre()).append(" (Vel: ").append(p.getVelocidad()).append(")\n");
        }
        log(sb.toString());
    }

    @Override
    public int mostrarMenuAcciones(Personaje p) {

        if (p instanceof PersonajeJugable) {
            mostrarInfoHeroe((PersonajeJugable)p);
        } else if (p != null) {

            log("Turno de " + p.getNombre());
        }
       
        return leerOpcionSegura(1, 4);
    }


    @Override
    public void mostrarEstado(String atacanteNombre, int atacanteHP, String defensorNombre, int defensorHP) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[Estado] ");
        if (atacanteNombre != null && !atacanteNombre.isEmpty()) sb.append(atacanteNombre).append(" (HP: ").append(atacanteHP).append(")");
        if (defensorNombre != null && !defensorNombre.isEmpty()) sb.append(" -> ").append(defensorNombre).append(" (HP: ").append(defensorHP).append(")");
        log(sb.toString());
        if (lastHeroes != null && lastMonstruos != null) {
            actualizarListas(lastHeroes, lastMonstruos);
        }
    }

    @Override
    public void mostrarInfoHeroe(PersonajeJugable p) {
        log("\nTurno de " + p.getNombre() + " | Estado: " + p.getEstado());
    }

    @Override
    public void mostrarAtaque(String actor, String categoria, String objetivo, int danio) {
        String cat = (categoria == null || categoria.isEmpty()) ? "" : " (" + categoria + ")";
        safeLog((actor == null ? "<desconocido>" : actor) + cat + " ataca a " + (objetivo == null ? "<desconocido>" : objetivo) + " e inflige " + danio + " de daño.");
    }

    @Override
    public void mostrarTurnoPerdido(String sujeto) {
        safeLog((sujeto == null ? "<sujeto>" : sujeto) + " pierde el turno.");
    }

    @Override
    public void mostrarEstadoCambio(String sujeto, String estado, int duracion) {
        safeLog((sujeto == null ? "<sujeto>" : sujeto) + " ahora está " + (estado == null ? "<estado>" : estado) + " por " + duracion + " turno(s).");
    }

    @Override
    public int leerOpcionSegura(int min, int max) {
        opcionSeleccionada = -1;
        log("Esperando acción del jugador...");
        while (opcionSeleccionada == -1) {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
        return opcionSeleccionada;
    }

    @Override
    public int seleccionarItem(List<String> nombresItems) {
        if (nombresItems == null || nombresItems.isEmpty()) {
            log("Inventario vacío.");
            return -1;
        }
        String[] items = nombresItems.toArray(new String[0]);
        String elegido = (String) JOptionPane.showInputDialog(this, "Selecciona un ítem:", "Inventario", JOptionPane.PLAIN_MESSAGE, null, items, items[0]);
        if (elegido == null) return -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(elegido)) return i;
        }
        return -1;
    }

    @Override
    public int seleccionarObjetivoHeroes(List<String> nombresHeroes) {
        if (nombresHeroes == null || nombresHeroes.isEmpty()) return -1;
        String[] nombres = nombresHeroes.toArray(new String[0]);
        String elegido = (String) JOptionPane.showInputDialog(this, "Selecciona un héroe objetivo:", "Objetivo", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (elegido == null) return -1;
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equals(elegido)) return i;
        }
        return -1;
    }

    @Override
    public int seleccionarObjetivoMonstruos(List<String> nombresMonstruos) {
        if (nombresMonstruos == null || nombresMonstruos.isEmpty()) return -1;
        String[] nombres = nombresMonstruos.toArray(new String[0]);
        String elegido = (String) JOptionPane.showInputDialog(this, "Selecciona un enemigo:", "Ataque", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (elegido == null) return -1;
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equals(elegido)) return i;
        }
        return -1;
    }

    @Override
    public void pedirEnter() {
    }


    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Consolas", Font.BOLD, 14));
        btn.setBackground(new Color(70, 70, 70));
    btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void actualizarListas(List<PersonajeJugable> heroes, List<Monstruo> monstruos) {
     
        SwingUtilities.invokeLater(() -> {
            heroesContainer.removeAll();
            monstruosContainer.removeAll();

            for (PersonajeJugable h : heroes) {
                heroesContainer.add(crearPanelPersonaje(h));
                heroesContainer.add(Box.createRigidArea(new Dimension(0,6)));
            }

            for (Monstruo m : monstruos) {
                monstruosContainer.add(crearPanelMonstruo(m));
                monstruosContainer.add(Box.createRigidArea(new Dimension(0,6)));
            }

            heroesContainer.revalidate();
            heroesContainer.repaint();
            monstruosContainer.revalidate();
            monstruosContainer.repaint();
        });
    }

    private JPanel crearPanelPersonaje(PersonajeJugable p) {
    JPanel panel = new JPanel(new BorderLayout());

    panel.setBackground(CARD_OVERLAY);
    panel.setOpaque(true);
    panel.setBorder(BorderFactory.createLineBorder(BORDER_GRAY, 3));

    JLabel lblNombre = new JLabel(p.getNombre());
    lblNombre.setForeground(Color.WHITE);
    lblNombre.setOpaque(false);
        lblNombre.setFont(new Font("Consolas", Font.BOLD, 14));
        panel.add(lblNombre, BorderLayout.NORTH);

    JPanel centro = new JPanel();
    centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
    centro.setOpaque(false);

    JLabel hpLabel = new JLabel("HP: " + p.getHP() + " / " + p.getHPMaximo());
    hpLabel.setForeground(Color.WHITE);
    hpLabel.setOpaque(false);
    hpLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
    centro.add(hpLabel);

    JLabel mpLabel = new JLabel("MP: " + p.getMP() + " / " + p.getMPMaximo());
    mpLabel.setForeground(Color.WHITE);
    mpLabel.setOpaque(false);
    mpLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
    centro.add(mpLabel);

    JLabel estado = new JLabel("Estado: " + p.getEstado());
    estado.setForeground(Color.WHITE);
    estado.setOpaque(false);
        centro.add(estado);

        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelMonstruo(Monstruo m) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(CARD_OVERLAY);
    panel.setOpaque(true);
    panel.setBorder(BorderFactory.createLineBorder(BORDER_GRAY, 3));

    JLabel lblNombre = new JLabel(m.getNombre());
    lblNombre.setForeground(Color.WHITE);
    lblNombre.setOpaque(false);
        lblNombre.setFont(new Font("Consolas", Font.BOLD, 14));
        panel.add(lblNombre, BorderLayout.NORTH);

    JPanel centro = new JPanel();
    centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
    centro.setOpaque(false);

    JLabel hpLabel = new JLabel("HP: " + m.getHP() + " / " + m.getHPMaximo());
    hpLabel.setForeground(Color.WHITE);
    hpLabel.setOpaque(false);
    hpLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
    centro.add(hpLabel);

        JLabel estado = new JLabel("Estado: " + m.getEstado());
    estado.setForeground(Color.WHITE);
    estado.setOpaque(false);
        centro.add(estado);

        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private void log(String texto) {
        areaLog.append(texto + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

   
    private void safeLog(String texto) {
        if (SwingUtilities.isEventDispatchThread()) {
            log(texto);
        } else {
            SwingUtilities.invokeLater(() -> log(texto));
        }
    }
}