package descarteconsciente;

import descarteconsciente.model.Usuario;

/**
 * Guarda o usuário autenticado durante a sessão.
 * Classe utilitária – não pode ser instanciada.
 */
public final class SessaoAtual {

    private static Usuario usuarioLogado = null;

    private SessaoAtual() {}

    public static void iniciar(Usuario u)   { usuarioLogado = u; }
    public static Usuario getUsuario()      { return usuarioLogado; }
    public static boolean isLogado()        { return usuarioLogado != null; }
    public static void encerrar()           { usuarioLogado = null; }
}