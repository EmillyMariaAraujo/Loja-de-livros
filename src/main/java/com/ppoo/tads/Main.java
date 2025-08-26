package com.ppoo.tads;

import com.ppoo.tads.controller.LivroController;
import com.ppoo.tads.repository.LivroRepositoryMemoria;
import com.ppoo.tads.view.LivroView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Usa a instância única do Repository
            LivroController controller = new LivroController(LivroRepositoryMemoria.getInstance());
            LivroView view = new LivroView(controller);
            view.setVisible(true);
        });
    }
}
