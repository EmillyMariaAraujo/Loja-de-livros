package com.ppoo.tads.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Livro {
    private Long id;
    private String titulo;
    private String autor;
    private String editora;
    private Genero genero;
    private BigDecimal valor; 

    public Livro() { }

    public Livro(Long id, String titulo, String autor, String editora, Genero genero, BigDecimal valor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.genero = genero;
        this.valor = valor;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    @Override
    public String toString() {
        return String.format("%s - %s - R$ %.2f", titulo, autor, valor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro)) return false;
        Livro livro = (Livro) o;
        return id != null && id.equals(livro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // Conversão para centavos (útil caso precise gravar como long no banco)
    public long getValorEmCentavos() {
        return valor == null ? 0L : valor.movePointRight(2).longValueExact();
    }

    public void setValorEmCentavos(long centavos) {
        this.valor = BigDecimal.valueOf(centavos, 2);
    }
}
