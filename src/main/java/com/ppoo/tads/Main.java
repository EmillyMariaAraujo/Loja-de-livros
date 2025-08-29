package com.ppoo.tads;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.ppoo.tads.controller.LivroController;
import com.ppoo.tads.repository.LivroRepositoryMemoria;
import com.ppoo.tads.view.CarrinhoView;
import com.ppoo.tads.view.LivroView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame launcher = new JFrame("Launcher - Loja de Livros");
            launcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));

            JButton btnCrud = new JButton("Abrir CRUD de Livros");
            btnCrud.addActionListener(e -> {
                LivroController controller = new LivroController(LivroRepositoryMemoria.getInstance());
                LivroView view = new LivroView(controller);
                view.setVisible(true);
            });

            JButton btnCarrinho = new JButton("Abrir Carrinho");
            btnCarrinho.addActionListener(e -> {
                JFrame f = new JFrame("Carrinho - Loja de Livros");
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setContentPane(new CarrinhoView()); 
                f.setSize(900, 520);
                f.setLocationRelativeTo(launcher);
                f.setVisible(true);
            });

            panel.add(btnCrud);
            panel.add(btnCarrinho);
            launcher.setContentPane(panel);
            launcher.setSize(380, 120);
            launcher.setLocationRelativeTo(null);
            launcher.setVisible(true);
        });
    }
}
