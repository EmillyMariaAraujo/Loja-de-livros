package com.ppoo.tads.view;

import com.ppoo.tads.controller.LivroController;
import com.ppoo.tads.model.Livro;
import com.ppoo.tads.model.Genero;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class LivroView extends JFrame {

    private final LivroController controller;

    private JTextField tituloField;
    private JTextField autorField;
    private JTextField editoraField;
    private JComboBox<Genero> generoBox;
    private JTextField valorField;
    private DefaultListModel<Livro> listModel;
    private JList<Livro> listaLivros;

    public LivroView(LivroController controller) {
        this.controller = controller;
        initUI();
        atualizarLista();
    }

    private void initUI() {
        setTitle("Gestão de Livros");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(5, 2));

        formPanel.add(new JLabel("Título:"));
        tituloField = new JTextField();
        formPanel.add(tituloField);

        formPanel.add(new JLabel("Autor:"));
        autorField = new JTextField();
        formPanel.add(autorField);

        formPanel.add(new JLabel("Editora:"));
        editoraField = new JTextField();
        formPanel.add(editoraField);

        formPanel.add(new JLabel("Gênero:"));
        generoBox = new JComboBox<>(Genero.values());
        formPanel.add(generoBox);

        formPanel.add(new JLabel("Valor (R$):"));
        valorField = new JTextField();
        formPanel.add(valorField);

        JPanel buttonPanel = new JPanel();
        JButton salvarBtn = new JButton("Salvar");
        JButton editarBtn = new JButton("Editar");
        JButton excluirBtn = new JButton("Excluir");
        JButton listarBtn = new JButton("Listar");

        buttonPanel.add(salvarBtn);
        buttonPanel.add(editarBtn);
        buttonPanel.add(excluirBtn);
        buttonPanel.add(listarBtn);

        // Lista de livros
        listModel = new DefaultListModel<>();
        listaLivros = new JList<>(listModel);
        listaLivros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaLivros.addListSelectionListener(e -> carregarLivroSelecionado());

        // Renderer personalizado
        listaLivros.setCellRenderer((list, livro, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setOpaque(true);
            String text = String.format("%s | %s | %s | %s | R$ %.2f",
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getEditora(),
                    livro.getGenero(),
                    livro.getValor());
            label.setText(text);
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            return label;
        });

        getContentPane().add(formPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(new JScrollPane(listaLivros), BorderLayout.SOUTH);

        // Ações
        salvarBtn.addActionListener(e -> salvarLivro());
        editarBtn.addActionListener(e -> editarLivro());
        excluirBtn.addActionListener(e -> excluirLivro());
        listarBtn.addActionListener(e -> atualizarLista());
    }

    private boolean validarCampos() {
        if (tituloField.getText().isBlank() || autorField.getText().isBlank()
                || editoraField.getText().isBlank() || valorField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
            return false;
        }
        return true;
    }

    private void salvarLivro() {
        if (!validarCampos()) return;

        try {
            Livro livro = new Livro();
            livro.setTitulo(tituloField.getText());
            livro.setAutor(autorField.getText());
            livro.setEditora(editoraField.getText());
            livro.setGenero((Genero) generoBox.getSelectedItem());

            String valorFormatado = valorField.getText().replace(",", ".");
            livro.setValor(new BigDecimal(valorFormatado));

            controller.salvarLivro(livro);
            atualizarLista();
            limparCampos();
            JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números, ex: 39,90 ou 39.90");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage());
        }
    }

    private void editarLivro() {
        Livro selecionado = listaLivros.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.");
            return;
        }

        if (!validarCampos()) return;

        try {
            selecionado.setTitulo(tituloField.getText());
            selecionado.setAutor(autorField.getText());
            selecionado.setEditora(editoraField.getText());
            selecionado.setGenero((Genero) generoBox.getSelectedItem());

            String valorFormatado = valorField.getText().replace(",", ".");
            selecionado.setValor(new BigDecimal(valorFormatado));

            controller.atualizarLivro(selecionado); // Atualiza o livro existente
            atualizarLista(); // Atualiza a lista sem adicionar duplicados
            limparCampos();
            JOptionPane.showMessageDialog(this, "Livro editado com sucesso!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números, ex: 39,90 ou 39.90");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage());
        }
    }

    private void excluirLivro() {
        Livro selecionado = listaLivros.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.");
            return;
        }
        controller.removerLivro(selecionado);
        atualizarLista();
        limparCampos();
        JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
    }

    private void atualizarLista() {
        listModel.clear();
        for (Livro l : controller.listarLivros()) {
            listModel.addElement(l);
        }
    }

    private void carregarLivroSelecionado() {
        Livro selecionado = listaLivros.getSelectedValue();
        if (selecionado != null) {
            tituloField.setText(selecionado.getTitulo());
            autorField.setText(selecionado.getAutor());
            editoraField.setText(selecionado.getEditora());
            generoBox.setSelectedItem(selecionado.getGenero());
            valorField.setText(selecionado.getValor().toString());
        }
    }

    private void limparCampos() {
        tituloField.setText("");
        autorField.setText("");
        editoraField.setText("");
        generoBox.setSelectedIndex(0);
        valorField.setText("");
        listaLivros.clearSelection();
    }
}
