package descarteconsciente.dao;

import descarteconsciente.DatabaseConnection;
import descarteconsciente.model.PontoDeColeta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso ao banco de dados para a entidade PontoDeColeta.
 */
public class PontoColetaDAO {

    // ---- Leitura ----

    /** Retorna todos os pontos ativos. */
    public List<PontoDeColeta> listarTodos() {
        return buscar("", "TODOS");
    }

    /**
     * Busca pontos filtrando por texto (nome/bairro/cidade) e tipo.
     *
     * @param filtroTexto   trecho de texto para nome, bairro ou cidade (pode ser vazio)
     * @param filtroTipo    "TODOS" ou um dos valores de PontoDeColeta (FARMACIA, etc.)
     */
    public List<PontoDeColeta> buscar(String filtroTexto, String filtroTipo) {
        List<PontoDeColeta> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM pontos_coleta WHERE ativo = 1");

        boolean temTexto = filtroTexto != null && !filtroTexto.isBlank();
        boolean temTipo  = filtroTipo != null && !filtroTipo.isBlank() && !"TODOS".equals(filtroTipo);

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
            System.out.println("Buscando pontos...");
            ResultSet rs = ps.executeQuery();

while (rs.next()) {
    System.out.println(rs.getString("nome"));
    lista.add(mapear(rs));
}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ---- Mapeamento ResultSet → Objeto ----

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