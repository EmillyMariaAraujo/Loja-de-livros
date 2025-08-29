package com.ppoo.tads.controller.cmd;

import com.ppoo.tads.model.Carrinho;
import com.ppoo.tads.model.ItemCarrinho;

public class AdicionarItemCmd implements Command {
    private final Carrinho carrinho = Carrinho.getInstance();
    private final ItemCarrinho item;

    public AdicionarItemCmd(ItemCarrinho i){ this.item=i; }

    @Override public void execute(){ carrinho.adicionar(item); }
    @Override public void undo(){ carrinho.removerPorId(item.getLivro().getId()); }
    @Override public String name(){ return "Adicionar " + item.getLivro().getTitulo(); }
}
