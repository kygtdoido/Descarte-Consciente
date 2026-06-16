package descarteconsciente.dao;

import descarteconsciente.DatabaseConnection;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Acesso à tabela sobre_conteudo.
 * Permite que o admin edite o conteúdo da tela "Sobre" pelo próprio sistema.
 */
public class SobreDAO {

    /** Retorna todos os campos da seção Sobre como mapa secao → conteudo. */
    public Map<String, String> buscarTodos() {
        Map<String, String> map = new LinkedHashMap<>();
        String sql = "SELECT secao, conteudo FROM sobre_conteudo ORDER BY id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getString("secao"), rs.getString("conteudo"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    /** Atualiza o conteúdo de uma seção específica. */
    public boolean atualizar(String secao, String conteudo) {
        String sql = "UPDATE sobre_conteudo SET conteudo = ? WHERE secao = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, conteudo);
            ps.setString(2, secao);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}