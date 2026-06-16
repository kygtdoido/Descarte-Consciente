package descarteconsciente.dao;

import descarteconsciente.DatabaseConnection;
import descarteconsciente.model.PontoDeColeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso ao banco de dados para a entidade PontoDeColeta.
 * Suporta leitura (busca/listar) e escrita (inserir/atualizar/excluir) — exclusivo admin.
 */
public class PontoColetaDAO {

    // ── Leitura ──────────────────────────────────────────────────

    /** Retorna todos os pontos ativos. */
    public List<PontoDeColeta> listarTodos() {
        return buscar("", "TODOS");
    }

    /** Retorna TODOS os pontos (ativos e inativos) — uso exclusivo do admin. */
    public List<PontoDeColeta> listarTodosAdmin() {
        List<PontoDeColeta> lista = new ArrayList<>();
        String sql = "SELECT * FROM pontos_coleta ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /**
     * Busca pontos filtrando por texto (nome/bairro/cidade) e tipo.
     */
    public List<PontoDeColeta> buscar(String filtroTexto, String filtroTipo) {
        List<PontoDeColeta> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM pontos_coleta WHERE ativo = 1");

        boolean temTexto = filtroTexto != null && !filtroTexto.trim().isEmpty();
        boolean temTipo  = filtroTipo  != null && !filtroTipo.trim().isEmpty() && !"TODOS".equals(filtroTipo);

        if (temTexto) sql.append(" AND (nome LIKE ? OR bairro LIKE ? OR cidade LIKE ?)");
        if (temTipo)  sql.append(" AND tipo = ?");
        sql.append(" ORDER BY nome");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (temTexto) {
                String like = "%" + filtroTexto.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            if (temTipo) ps.setString(idx, filtroTipo);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // ── Escrita (admin) ───────────────────────────────────────────

    /**
     * Insere um novo ponto de coleta no banco.
     * @return true se inserido com sucesso.
     */
    public boolean inserir(PontoDeColeta p, int adminId) {
        String sql = "INSERT INTO pontos_coleta "
                + "(nome,tipo,rua,numero,bairro,cidade,cep,telefone,"
                + "horario_seg_sex,horario_sab,horario_dom,"
                + "aceita_controlados,aceita_liquidos,aceita_comprimidos,aceita_perfurocortantes,"
                + "latitude,longitude,ativo,criado_por) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            preencherPs(ps, p);
            ps.setInt(19, adminId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Atualiza os dados de um ponto existente.
     * @return true se atualizado com sucesso.
     */
    public boolean atualizar(PontoDeColeta p) {
        String sql = "UPDATE pontos_coleta SET "
                + "nome=?,tipo=?,rua=?,numero=?,bairro=?,cidade=?,cep=?,telefone=?,"
                + "horario_seg_sex=?,horario_sab=?,horario_dom=?,"
                + "aceita_controlados=?,aceita_liquidos=?,aceita_comprimidos=?,aceita_perfurocortantes=?,"
                + "latitude=?,longitude=?,ativo=? "
                + "WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            preencherPs(ps, p);
            ps.setBoolean(18, p.isAtivo());
            ps.setInt(19, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Remove (desativa) um ponto de coleta pelo id.
     * Usamos soft-delete (ativo = 0) para preservar o histórico.
     */
    public boolean excluir(int id) {
        String sql = "UPDATE pontos_coleta SET ativo = 0 WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    /**
     * Remove permanentemente um ponto do banco.
     */
    public boolean excluirPermanente(int id) {
        String sql = "DELETE FROM pontos_coleta WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Helpers privados ──────────────────────────────────────────

    /** Preenche as primeiras 18 posições do PreparedStatement com os dados do ponto. */
    private void preencherPs(PreparedStatement ps, PontoDeColeta p) throws SQLException {
        ps.setString(1,  p.getNome());
        ps.setString(2,  p.getTipo());
        ps.setString(3,  p.getRua());
        ps.setString(4,  p.getNumero());
        ps.setString(5,  p.getBairro());
        ps.setString(6,  p.getCidade());
        ps.setString(7,  p.getCep());
        ps.setString(8,  p.getTelefone());
        ps.setString(9,  p.getHorarioSegSex());
        ps.setString(10, p.getHorarioSab());
        ps.setString(11, p.getHorarioDom());
        ps.setBoolean(12, p.isAceitaControlados());
        ps.setBoolean(13, p.isAceitaLiquidos());
        ps.setBoolean(14, p.isAceitaComprimidos());
        ps.setBoolean(15, p.isAceitaPerfurocortantes());
        ps.setDouble(16, p.getLatitude());
        ps.setDouble(17, p.getLongitude());
        ps.setBoolean(18, p.isAtivo());
    }

    private PontoDeColeta mapear(ResultSet rs) throws SQLException {
        PontoDeColeta p = new PontoDeColeta();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setTipo(rs.getString("tipo"));
        p.setRua(rs.getString("rua"));
        p.setNumero(rs.getString("numero"));
        p.setBairro(rs.getString("bairro"));
        p.setCidade(rs.getString("cidade"));
        p.setCep(rs.getString("cep"));
        p.setTelefone(rs.getString("telefone"));
        p.setHorarioSegSex(rs.getString("horario_seg_sex"));
        p.setHorarioSab(rs.getString("horario_sab"));
        p.setHorarioDom(rs.getString("horario_dom"));
        p.setAceitaControlados(rs.getBoolean("aceita_controlados"));
        p.setAceitaLiquidos(rs.getBoolean("aceita_liquidos"));
        p.setAceitaComprimidos(rs.getBoolean("aceita_comprimidos"));
        p.setAceitaPerfurocortantes(rs.getBoolean("aceita_perfurocortantes"));
        p.setLatitude(rs.getDouble("latitude"));
        p.setLongitude(rs.getDouble("longitude"));
        p.setAtivo(rs.getBoolean("ativo"));
        return p;
    }
}
