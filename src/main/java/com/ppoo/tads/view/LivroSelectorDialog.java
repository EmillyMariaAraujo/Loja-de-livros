package com.ppoo.tads.view;

import com.ppoo.tads.model.Livro;
import com.ppoo.tads.repository.LivroRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

public class LivroSelectorDialog extends JDialog {
    private static final long serialVersionUID = 1L; // <<< remove o warning

    private Livro selecionado;
    private int quantidade = 1;
    private BigDecimal precoUnit = new BigDecimal("0.00");

    private final DefaultTableModel model = new DefaultTableModel(
        new Object[]{"ID","Título","Autor","Valor"}, 0) {
        private static final long serialVersionUID = 1L; // <<< remove o warning da classe anônima
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    private final JTable tabela = new JTable(model);
    private final JSpinner spQtd = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JFormattedTextField txtPreco =
            new JFormattedTextField(NumberFormat.getNumberInstance());

    private List<Livro> livros; // guardamos a lista para recuperar o próprio objeto

    public LivroSelectorDialog(Window owner, LivroRepository repo) {
        super(owner, "Selecionar Livro", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        // Usa o método do teu repo (listarTodos)
        livros = repo.listarTodos();
        NumberFormat moeda = NumberFormat.getCurrencyInstance();

        for (Livro l : livros) {
            String valorFmt = (l.getValor() == null) ? "R$ 0,00" : moeda.format(l.getValor());
            model.addRow(new Object[]{ l.getId(), l.getTitulo(), l.getAutor(), valorFmt });
        }

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Lateral: quantidade e preço
        JPanel right = new JPanel(new GridLayout(0, 1, 6, 6));
        right.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        right.add(new JLabel("Quantidade:"));
        right.add(spQtd);
        right.add(new JLabel("Preço unitário (R$):"));
        txtPreco.setValue(0.00);
        right.add(txtPreco);
        add(right, BorderLayout.EAST);

        // Atualiza o campo de preço quando o usuário troca a linha
        tabela.getSelectionModel().addListSelectionListener(e -> {
            int idx = tabela.getSelectedRow();
            if (!e.getValueIsAdjusting() && idx >= 0) {
                Livro l = livros.get(idx);
                txtPreco.setValue(l.getValor() == null ? 0.00 : l.getValor());
            }
        });

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Adicionar");
        JButton btnCancel = new JButton("Cancelar");
        botoes.add(btnCancel); botoes.add(btnOk);
        add(botoes, BorderLayout.SOUTH);

        btnOk.addActionListener(e -> {
            int idx = tabela.getSelectedRow();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um livro na tabela.");
                return;
            }
            quantidade = (Integer) spQtd.getValue();
            try {
                // aceita "1.23" ou "1,23"
                precoUnit = new BigDecimal(txtPreco.getValue().toString().replace(",", "."));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido.");
                return;
            }
            selecionado = livros.get(idx); 
            dispose();
        });

        btnCancel.addActionListener(e -> { selecionado = null; dispose(); });

        setSize(760, 460);
        setLocationRelativeTo(owner);

        // Seleciona a primeira linha por padrão e preenche o preço, se houver itens
        if (!livros.isEmpty()) {
            tabela.setRowSelectionInterval(0, 0);
            txtPreco.setValue(livros.get(0).getValor() == null ? 0.00 : livros.get(0).getValor());
        }
    }

    public Livro getSelecionado() { return selecionado; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnit() { return precoUnit; }
}
