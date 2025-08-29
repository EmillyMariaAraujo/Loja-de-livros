package com.ppoo.tads.controller;

import java.util.List;

import com.ppoo.tads.model.Livro;
import com.ppoo.tads.repository.LivroRepository;

public class LivroController {

    private final LivroRepository repository;

    public LivroController(LivroRepository repository) {
        this.repository = repository;
    }

    // Validação mínima antes de salvar
    public void salvarLivro(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("O título do livro é obrigatório.");
        }
        if (livro.getAutor() == null || livro.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("O autor do livro é obrigatório.");
        }
        if (livro.getEditora() == null || livro.getEditora().trim().isEmpty()) {
            throw new IllegalArgumentException("A editora do livro é obrigatória.");
        }
        if (livro.getValor() == null || livro.getValor().doubleValue() <= 0) {
            throw new IllegalArgumentException("O valor do livro deve ser maior que zero.");
        }

        repository.salvar(livro);
    }

    public Livro buscarLivro(Long id) {
        return repository.buscarPorId(id);
    }

    public void atualizarLivro(Livro livro) {
        if (livro.getId() == null) {
            throw new IllegalArgumentException("Livro sem ID não pode ser atualizado.");
        }
        salvarLivro(livro); // reaproveita validação
        repository.update(livro);
    }

    public void removerLivro(Livro livro) {
        if (livro.getId() != null) {
            repository.delete(livro);
        }
    }

    public List<Livro> listarLivros() {
        return repository.listarTodos();
    }
}
