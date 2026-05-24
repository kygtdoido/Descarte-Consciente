package descarteconsciente.model;

import java.time.LocalDateTime;

/**
 * Entidade que representa um usuário cadastrado no sistema.
 */
public class Usuario {

    private int           id;
    private String        nome;
    private String        email;
    private String        senha;         // armazenada como hash SHA-256
    private LocalDateTime dataCriacao;

    public Usuario() {}

    public Usuario(String nome, String email, String senha) {
        this.nome  = nome;
        this.email = email;
        this.senha = senha;
    }

    // ---- Getters / Setters ----

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getNome()                     { return nome; }
    public void setNome(String nome)            { this.nome = nome; }

    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }

    public String getSenha()                    { return senha; }
    public void setSenha(String senha)          { this.senha = senha; }

    public LocalDateTime getDataCriacao()               { return dataCriacao; }
    public void setDataCriacao(LocalDateTime d)         { this.dataCriacao = d; }

    @Override
    public String toString() {
        return nome + " <" + email + ">";
    }
}