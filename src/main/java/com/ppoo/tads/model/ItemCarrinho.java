package com.ppoo.tads.model;

import java.math.BigDecimal;

public class ItemCarrinho {
    private final Livro livro;
    private int quantidade;
    private final BigDecimal precoUnit;

    public ItemCarrinho(Livro livro, int quantidade, BigDecimal precoUnit) {
        this.livro = livro; this.quantidade = quantidade; this.precoUnit = precoUnit;
    }
    public Livro getLivro() { return livro; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int q) { this.quantidade = q; }
    public BigDecimal getPrecoUnit() { return precoUnit; }
}
