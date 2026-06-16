package descarteconsciente;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão única com o banco de dados MySQL.
 *
 * CONFIGURAÇÃO:
 *   Altere DB_USER e DB_PASSWORD conforme seu ambiente MySQL.
 *   Por padrão usa localhost:3306 e banco "descarte_consciente".
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/descarte_consciente"
                                         + "?useSSL=false&serverTimezone=America/Sao_Paulo"
                                         + "&allowPublicKeyRetrieval=true";
    private static final String DB_USER  = "root";       // ← altere se necessário
    private static final String DB_PASS  = "Sua_Senha_aqui";           // ← altere se necessário

    private static Connection conn = null;

    /** Retorna a conexão ativa, abrindo-a se necessário. */
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
        }
        return conn;
    }

    /** Fecha a conexão com segurança. */
    public static void fechar() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}