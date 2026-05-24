package descarteconsciente.dao;

import descarteconsciente.DatabaseConnection;
import descarteconsciente.model.Lembrete;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso ao banco de dados para a entidade Lembrete.
 */
public class LembreteDAO {

    /** Salva um novo lembrete para o usuário. */
    public boolean salvar(Lembrete lembrete) {
        String sql = "INSERT INTO lembretes (usuario_id, medicamento, data_validade) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lembrete.getUsuarioId());
            ps.setString(2, lembrete.getMedicamento());
            ps.setDate(3, Date.valueOf(lembrete.getDataValidade()));
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Lista todos os lembretes de um usuário, ordenados pela data de validade. */
    public List<Lembrete> listarPorUsuario(int usuarioId) {
        List<Lembrete> lista = new ArrayList<>();
        String sql = "SELECT * FROM lembretes WHERE usuario_id = ? ORDER BY data_validade";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /** Remove um lembrete pelo ID. */
    public boolean remover(int id) {
        String sql = "DELETE FROM lembretes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---- Mapeamento ----

    private Lembrete mapear(ResultSet rs) throws SQLException {
        Lembrete l = new Lembrete();
        l.setId(rs.getInt("id"));
        l.setUsuarioId(rs.getInt("usuario_id"));
        l.setMedicamento(rs.getString("medicamento"));
        l.setDataValidade(rs.getDate("data_validade").toLocalDate());
        l.setNotificado(rs.getBoolean("notificado"));
        return l;
    }
}