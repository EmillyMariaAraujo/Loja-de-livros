package com.ppoo.tads.controller.cmd;

import com.ppoo.tads.model.Carrinho;
import com.ppoo.tads.model.ItemCarrinho;

public class AlterarQuantidadeCmd implements Command {
    private final Carrinho carrinho = Carrinho.getInstance();
    private final Long id; private final int nova; private int antiga;

    public AlterarQuantidadeCmd(Long id, int nova){ this.id=id; this.nova=nova; }

    @Override public void execute(){
        for (ItemCarrinho i : carrinho.getItens()) {
            if (java.util.Objects.equals(i.getLivro().getId(), id)) { antiga = i.getQuantidade(); break; }
        }
        carrinho.alterarQtd(id, nova);
    }
    @Override public void undo(){ carrinho.alterarQtd(id, antiga); }
    @Override public String name(){ return "Alterar quantidade"; }
}
