package com.ppoo.tads.model;

import java.math.BigDecimal;
import java.util.*;

public class Carrinho {
    private static Carrinho INSTANCE;
    public static Carrinho getInstance(){ if(INSTANCE==null) INSTANCE=new Carrinho(); return INSTANCE; }
    private Carrinho(){}

    private final List<ItemCarrinho> itens = new ArrayList<>();
    private final List<CarrinhoListener> listeners = new ArrayList<>();

    public List<ItemCarrinho> getItens(){ return Collections.unmodifiableList(itens); }

    public void adicionar(ItemCarrinho item){ itens.add(item); notificar(); }

    public void removerPorId(Long id){
        itens.removeIf(i -> Objects.equals(i.getLivro().getId(), id));
        notificar();
    }

    public void alterarQtd(Long id, int novaQtd){
        for (ItemCarrinho i : itens) {
            if (Objects.equals(i.getLivro().getId(), id)) { i.setQuantidade(novaQtd); break; }
        }
        notificar();
    }

    public BigDecimal total(){
        return itens.stream()
            .map(i -> i.getPrecoUnit().multiply(BigDecimal.valueOf(i.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void limpar(){ itens.clear(); notificar(); }
    public void addListener(CarrinhoListener l){ listeners.add(l); }
    public void removeListener(CarrinhoListener l){ listeners.remove(l); }
    private void notificar(){ listeners.forEach(CarrinhoListener::onCarrinhoAtualizado); }
}
