package com.ppoo.tads.repository;

import com.ppoo.tads.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroRepositoryMemoria implements LivroRepository {

    private static LivroRepositoryMemoria instancia; // instância única
    private List<Livro> livros = new ArrayList<>();
    private long nextId = 1; // Para gerar IDs automáticos

    // Construtor privado para impedir criação externa
    private LivroRepositoryMemoria() {}

    // Método para acessar a instância única
    public static LivroRepositoryMemoria getInstance() {
        if (instancia == null) {
            instancia = new LivroRepositoryMemoria();
        }
        return instancia;
    }

    @Override
    public void salvar(Livro livro) {
        if (livro.getId() == null) {
            // Novo livro: atribui ID e adiciona à lista
            livro.setId(nextId++);
            livros.add(livro);
        } else {
            // Livro existente: atualiza registro
            update(livro);
        }
    }

    @Override
    public Livro buscarPorId(Long id) {
        return livros.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Livro livro) {
        for (int i = 0; i < livros.size(); i++) {
            if (livros.get(i).getId().equals(livro.getId())) {
                livros.set(i, livro);
                return;
            }
        }
    }

    @Override
    public void delete(Livro livro) {
        livros.removeIf(l -> l.getId().equals(livro.getId()));
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(livros); // devolve cópia para segurança
    }
}
