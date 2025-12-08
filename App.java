import Controlador.Controlador;
import Vista.VistaTerminal;
import Vista.VistaGui;
import Vista.Vista;
public class App {
    public static void main(String[] args) {
        VistaTerminal vista = new VistaTerminal();
       // VistaGui vista = new VistaGui();

        Controlador controlador = new Controlador(vista);
        controlador.iniciar();
    }
}