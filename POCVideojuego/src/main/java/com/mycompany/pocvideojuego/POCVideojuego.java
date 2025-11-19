package com.mycompany.pocvideojuego;

import com.mycompany.pocvideojuego.view.GUIPrincipal;

/**
 * Clase principal que arranca la aplicación.
 */
public class POCVideojuego {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            GUIPrincipal gui = new GUIPrincipal();
            gui.setVisible(true);
        });
    }
}
