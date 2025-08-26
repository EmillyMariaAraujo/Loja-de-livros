package com.ppoo.tads.repository;

import com.ppoo.tads.model.Livro;
import java.util.List;

public interface LivroRepository {
    void salvar(Livro livro);
    Livro buscarPorId(Long id);
    void update(Livro livro);
    void delete(Livro livro);
    List<Livro> listarTodos();
}
