package descarteconsciente.model;

import java.time.LocalDateTime;

public class Usuario {
    private int           id;
    private String        nome;
    private String        email;
    private String        senha;
    private boolean       admin;
    private LocalDateTime dataCriacao;

    public Usuario() {}
    public Usuario(String nome, String email, String senha) {
        this.nome = nome; this.email = email; this.senha = senha;
    }

    public int     getId()                      { return id; }
    public void    setId(int id)                { this.id = id; }
    public String  getNome()                    { return nome; }
    public void    setNome(String n)            { this.nome = n; }
    public String  getEmail()                   { return email; }
    public void    setEmail(String e)           { this.email = e; }
    public String  getSenha()                   { return senha; }
    public void    setSenha(String s)           { this.senha = s; }
    public boolean isAdmin()                    { return admin; }
    public void    setAdmin(boolean a)          { this.admin = a; }
    public LocalDateTime getDataCriacao()       { return dataCriacao; }
    public void    setDataCriacao(LocalDateTime d){ this.dataCriacao = d; }

    @Override public String toString()          { return nome + " <" + email + ">"; }
}