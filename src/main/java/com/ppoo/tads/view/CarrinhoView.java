package com.ppoo.tads.view;

import com.ppoo.tads.controller.cmd.AdicionarItemCmd;
import com.ppoo.tads.controller.cmd.AlterarQuantidadeCmd;
import com.ppoo.tads.controller.cmd.CommandManager;
import com.ppoo.tads.controller.cmd.RemoverItemCmd;
import com.ppoo.tads.model.Carrinho;
import com.ppoo.tads.model.CarrinhoListener;
import com.ppoo.tads.model.ItemCarrinho;
import com.ppoo.tads.model.Livro;
import com.ppoo.tads.repository.LivroRepository;
import com.ppoo.tads.repository.LivroRepositoryMemoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CarrinhoView extends JPanel implements CarrinhoListener {
    private static final long serialVersionUID = 1L;

    private final Carrinho carrinho = Carrinho.getInstance();
    private final CommandManager commands = new CommandManager();

    // usa singleton do repositório em memória
    private final LivroRepository livroRepo = LivroRepositoryMemoria.getInstance();

    private final DefaultTableModel model = new DefaultTableModel(
        new Object[]{"ID","Título","Qtd","Preço","Subtotal"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int r, int c) { return c == 2; } // só Qtd editável
        @Override public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> Long.class;      // ID
                case 1 -> String.class;    // Título
                case 2 -> Integer.class;   // Quantidade
                case 3 -> String.class;    // Preço formatado
                case 4 -> String.class;    // Subtotal formatado
                default -> Object.class;
            };
        }
    };

    private final JTable tabela = new JTable(model);
    private final JLabel lblTotal = new JLabel("Total: R$ 0,00");
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));

    // evita loop de eventos ao atualizar a tabela
    private boolean isUpdating = false;

    public CarrinhoView() {
        carrinho.addListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Adicionar item");
        JButton btnRem = new JButton("Remover");
        JButton btnUndo = new JButton("Undo");
        JButton btnRedo = new JButton("Redo");
        JButton btnFechar = new JButton("Fechar venda");
        topo.add(btnAdd); topo.add(btnRem); topo.add(btnUndo); topo.add(btnRedo); topo.add(btnFechar);
        add(topo, BorderLayout.NORTH);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(lblTotal);
        add(rodape, BorderLayout.SOUTH);

        // Adicionar item via diálogo (preço vem de Livro.valor e pode ser sobrescrito)
        btnAdd.addActionListener(e -> {
            LivroSelectorDialog dlg =
                new LivroSelectorDialog(SwingUtilities.getWindowAncestor(this), livroRepo);
            dlg.setVisible(true);
            Livro lv = dlg.getSelecionado();
            if (lv != null) {
                ItemCarrinho item = new ItemCarrinho(lv, dlg.getQuantidade(), dlg.getPrecoUnit());
                commands.run(new AdicionarItemCmd(item));
            }
        });

        // Remover item selecionado
        btnRem.addActionListener(e -> {
            int idx = tabela.getSelectedRow();
            if (idx >= 0) {
                Object idCell = model.getValueAt(idx, 0);
                Long id = (idCell instanceof Long) ? (Long) idCell : Long.valueOf(String.valueOf(idCell));
                ItemCarrinho it = carrinho.getItens().stream()
                        .filter(i -> java.util.Objects.equals(i.getLivro().getId(), id))
                        .findFirst().orElse(null);
                if (it != null) commands.run(new RemoverItemCmd(it));
            }
        });

        // Undo/Redo
        btnUndo.addActionListener(e -> commands.undo());
        btnRedo.addActionListener(e -> commands.redo());

        // Editar quantidade direto na tabela → vira Command
        tabela.getModel().addTableModelListener(ev -> {
            if (isUpdating) return;
            if (ev.getColumn() == 2 && ev.getFirstRow() >= 0) {
                try {
                    Object idCell = model.getValueAt(ev.getFirstRow(), 0);
                    Long id = (idCell instanceof Long) ? (Long) idCell : Long.valueOf(String.valueOf(idCell));
                    Object qtdCell = model.getValueAt(ev.getFirstRow(), 2);
                    int novaQtd = (qtdCell instanceof Integer)
                            ? (Integer) qtdCell
                            : Integer.parseInt(String.valueOf(qtdCell));
                    commands.run(new AlterarQuantidadeCmd(id, novaQtd));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantidade inválida.");
                    onCarrinhoAtualizado(); // re-render para restaurar o valor anterior
                }
            }
        });

        // Fechar venda: exibe resumo e limpa carrinho
        btnFechar.addActionListener(e -> {
            if (carrinho.getItens().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Carrinho vazio.");
                return;
            }
            StringBuilder sb = new StringBuilder("Itens da venda:\n");
            for (ItemCarrinho i : carrinho.getItens()) {
                BigDecimal sub = i.getPrecoUnit().multiply(BigDecimal.valueOf(i.getQuantidade()));
                sb.append(String.format("- %s (x%d) = %s%n",
                        i.getLivro().getTitulo(), i.getQuantidade(), moeda.format(sub)));
            }
            sb.append("\nTOTAL: ").append(moeda.format(carrinho.total()));
            JOptionPane.showMessageDialog(this, sb.toString(), "Resumo da Venda",
                    JOptionPane.INFORMATION_MESSAGE);
            carrinho.limpar();
        });

        // primeira render
        onCarrinhoAtualizado();
    }

    @Override
    public void onCarrinhoAtualizado() {
        isUpdating = true;
        try {
            model.setRowCount(0);
            for (ItemCarrinho i : carrinho.getItens()) {
                BigDecimal subtotal = i.getPrecoUnit().multiply(BigDecimal.valueOf(i.getQuantidade()));
                model.addRow(new Object[]{
                    i.getLivro().getId(),
                    i.getLivro().getTitulo(),
                    i.getQuantidade(),
                    moeda.format(i.getPrecoUnit()),
                    moeda.format(subtotal)
                });
            }
            lblTotal.setText("Total: " + moeda.format(carrinho.total()));
        } finally {
            isUpdating = false;
        }
    }
}
