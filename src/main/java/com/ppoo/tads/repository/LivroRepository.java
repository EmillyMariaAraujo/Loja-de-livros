package com.ppoo.tads.repository;

import com.ppoo.tads.model.Livro;

public interface LivroRepository {
    void salvar(Livro livro);
    Livro buscarPorId(Long id);
    void update(Livro livro);
    void delete(Livro livro);
}
