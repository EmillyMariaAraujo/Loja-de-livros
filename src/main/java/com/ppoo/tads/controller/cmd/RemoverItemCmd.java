package com.ppoo.tads.controller.cmd;

import com.ppoo.tads.model.Carrinho;
import com.ppoo.tads.model.ItemCarrinho;

public class RemoverItemCmd implements Command {
    private final Carrinho carrinho = Carrinho.getInstance();
    private final ItemCarrinho itemBackup;

    public RemoverItemCmd(ItemCarrinho item){ this.itemBackup=item; }

    @Override public void execute(){ carrinho.removerPorId(itemBackup.getLivro().getId()); }
    @Override public void undo(){ carrinho.adicionar(itemBackup); }
    @Override public String name(){ return "Remover " + itemBackup.getLivro().getTitulo(); }
}
