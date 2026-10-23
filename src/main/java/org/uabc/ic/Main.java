package org.uabc.ic;

import org.uabc.ic.Control.ControlCatalogo;
import org.uabc.ic.Vista.CatalogoView;
import org.uabc.ic.modelo.CatalogoMelodias;
import org.uabc.ic.modelo.Melodia;

public class Main {
    public static void main(String[] args) {
        CatalogoMelodias modelo = new CatalogoMelodias();
        CatalogoView vista = new CatalogoView();

        // Ejemplo de cacnciones
        modelo.registrarMelodia(new Melodia("Bohemian Rhapsody", "Freddie Mercury", 1975, "Rock", "Queen", 19.99, "mp3"));
        modelo.registrarMelodia(new Melodia("Billie Jean", "Michael Jackson", 1982, "Pop", "Michael Jackson", 15.50, "wav"));
        modelo.registrarMelodia(new Melodia("Hotel California", "Don Felder", 1976, "Rock", "Eagles", 17.00, "mp3"));

        ControlCatalogo control = new ControlCatalogo(vista, modelo);
        control.iniciar();
    }
}