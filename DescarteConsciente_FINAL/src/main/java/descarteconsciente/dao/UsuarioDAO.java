package descarteconsciente.dao;

import descarteconsciente.DatabaseConnection;
import descarteconsciente.model.Usuario;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

public class UsuarioDAO {

    public static String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(64);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public boolean cadastrar(Usuario u) {
        if (emailExiste(u.getEmail())) return false;
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail().trim().toLowerCase());
            ps.setString(3, hashSenha(u.getSenha()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public Usuario login(String email, String senha) {
        String sql = "SELECT id,nome,email,is_admin FROM usuarios WHERE email=? AND senha=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, hashSenha(senha));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setAdmin(rs.getBoolean("is_admin"));
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    public boolean atualizar(Usuario u) {
    String sql = "UPDATE usuarios SET nome=?, email=? WHERE id=?";
    try (Connection c = DatabaseConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, u.getNome());
        ps.setString(2, u.getEmail().trim().toLowerCase());
        ps.setInt(3, u.getId());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) { e.printStackTrace(); return false; }
}

public boolean excluir(int id) {
    String sql = "DELETE FROM usuarios WHERE id=? AND is_admin=0";
    try (Connection c = DatabaseConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) { e.printStackTrace(); return false; }
}
}