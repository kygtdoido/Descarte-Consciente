package descarteconsciente.dao;

import descarteconsciente.DatabaseConnection;
import descarteconsciente.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

/**
 * Acesso ao banco de dados para a entidade Usuario.
 */
public class UsuarioDAO {

    // ---- Hash de senha SHA-256 ----

    public static String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(64);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

    // ---- Operações CRUD ----

    /**
     * Insere um novo usuário.
     * @return true se criado com sucesso; false se o e-mail já existe ou erro.
     */
    public boolean cadastrar(Usuario usuario) {
        if (emailExiste(usuario.getEmail())) return false;

        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail().trim().toLowerCase());
            ps.setString(3, hashSenha(usuario.getSenha()));
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Autentica o usuário pelo e-mail e senha.
     * @return o objeto Usuario preenchido, ou null se inválido.
     */
    public Usuario login(String email, String senha) {
        String sql = "SELECT id, nome, email FROM usuarios WHERE email = ? AND senha = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, hashSenha(senha));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Verifica se já existe um usuário com o e-mail informado. */
    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}