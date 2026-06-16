package descarteconsciente.view;

import descarteconsciente.DatabaseConnection;
import descarteconsciente.SessaoAtual;
import descarteconsciente.util.Cores;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

/**
 * Painel inicial (Dashboard).
 *
 * CORREÇÕES APLICADAS:
 *  1. GridLayout(1, 4, ...) → GridLayout(1, 6, ...) — evita cards invisíveis.
 *  2. Dois novos cards: "Usuários comuns" e "Administradores" (req. 4.d do professor).
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        initComponents();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(Color.WHITE);

        conteudo.add(criarHero());
        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(criarStatsRow());
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(criarSecaoPorQue());
        conteudo.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------ Hero
    private JPanel criarHero() {
        JPanel hero = new JPanel(new GridLayout(2, 1, 0, 10));
        hero.setBackground(new Color(27, 94, 32));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        hero.setPreferredSize(new Dimension(0, 160));
        hero.setBorder(new EmptyBorder(30, 40, 30, 40));

        String nome = SessaoAtual.getUsuario() != null
                ? SessaoAtual.getUsuario().getNome()
                : "Usu\u00E1rio";

        JLabel lblTitulo = new JLabel("Bem-vindo, " + nome + "! \uD83C\uDF31", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(
                "Encontre locais para descartar medicamentos vencidos corretamente.", JLabel.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(200, 230, 201));

        hero.add(lblTitulo);
        hero.add(lblSub);
        return hero;
    }

    // -------------------------------------------------------------- Stats Row
    private JPanel criarStatsRow() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(0, 16, 0, 16));

        // ✅ CORRIGIDO: era GridLayout(1, 4, 14, 0) — agora GridLayout(1, 6, 10, 0)
        JPanel row = new JPanel(new GridLayout(1, 6, 10, 0));
        row.setBackground(Color.WHITE);

        // --- Cards existentes (pontos de coleta) ---
        row.add(statCard("\uD83C\uDFE5",
                contarQuery("SELECT COUNT(*) FROM pontos_coleta"),
                "Pontos de coleta", new Color(56, 142, 60)));

        row.add(statCard("\uD83C\uDFE8",
                contarQuery("SELECT COUNT(*) FROM pontos_coleta WHERE tipo = 'POSTO_SAUDE'"),
                "Postos de Sa\u00FAde", new Color(198, 40, 40)));

        row.add(statCard("\uD83D\uDC8A",
                contarQuery("SELECT COUNT(*) FROM pontos_coleta WHERE tipo IN ('FARMACIA','DROGARIA')"),
                "Farm\u00E1cias/Drogarias", new Color(21, 101, 192)));

        row.add(statCard("\u267B",
                contarQuery("SELECT COUNT(*) FROM pontos_coleta WHERE tipo = 'PONTO_MUNICIPAL'"),
                "Pontos Municipais", new Color(245, 127, 23)));

        // ✅ NOVOS: total de usuários por categoria — exigido pelo professor (item 4.d)
        row.add(statCard("\uD83D\uDC64",
                contarUsuarios(false),
                "Usu\u00E1rios comuns", new Color(21, 101, 192)));

        row.add(statCard("\uD83D\uDD11",
                contarUsuarios(true),
                "Administradores", new Color(74, 20, 140)));

        wrapper.add(row, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel statCard(String icone, int valor, String rotulo, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(14, 8, 14, 8)));

        // Barra colorida no topo
        JPanel barra = new JPanel();
        barra.setBackground(cor);
        barra.setPreferredSize(new Dimension(0, 5));
        card.add(barra, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(3, 1, 0, 4));
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(8, 0, 0, 0));

        JLabel lblIcone = new JLabel(icone, JLabel.CENTER);
        lblIcone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel lblValor = new JLabel(String.valueOf(valor), JLabel.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(cor);

        JLabel lblRotulo = new JLabel(rotulo, JLabel.CENTER);
        lblRotulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRotulo.setForeground(new Color(100, 100, 100));

        centro.add(lblIcone);
        centro.add(lblValor);
        centro.add(lblRotulo);
        card.add(centro, BorderLayout.CENTER);

        return card;
    }

    // --------------------------------------------------------------- Por quê
    private JPanel criarSecaoPorQue() {
        JPanel section = new JPanel(new BorderLayout(0, 10));
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(0, 24, 24, 24));

        JLabel titulo = new JLabel("Por que descartar corretamente?");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(27, 94, 32));
        section.add(titulo, BorderLayout.NORTH);

        JTextArea texto = new JTextArea(
                "Medicamentos descartados incorretamente no lixo comum ou na pia contaminam o solo, "
                + "rios e len\u00E7\u00F3is fre\u00E1ticos, afetando a sa\u00FAde humana e animal.\n\n"
                + "\uD83C\uDF31 Use os pontos de coleta cadastrados neste sistema \u2013 \u00E9 gratuito, "
                + "f\u00E1cil e faz toda a diferen\u00E7a para o meio ambiente e para a comunidade.\n\n"
                + "\uD83C\uDF0D Este projeto est\u00E1 alinhado com a ODS\u00A03 \u2013 Sa\u00FAde e "
                + "Bem-Estar da ONU, que busca garantir sa\u00FAde e bem-estar para todos.");
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setEditable(false);
        texto.setBackground(Color.WHITE);
        texto.setForeground(new Color(60, 60, 60));
        texto.setBorder(null);
        section.add(texto, BorderLayout.CENTER);

        return section;
    }

    // ---------------------------------------------------------------- Helpers
    private int contarQuery(String sql) {
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int contarUsuarios(boolean isAdmin) {
        return contarQuery("SELECT COUNT(*) FROM usuarios WHERE is_admin = " + (isAdmin ? 1 : 0));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        designTitulo = new JLabel("Dashboard - Resumo do Sistema", SwingConstants.CENTER);
        designTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        designTitulo.setForeground(Color.WHITE);
        designTitulo.setOpaque(true);
        designTitulo.setBackground(Cores.PRIMARY_DARK);
        designCards = new JLabel("Pontos de coleta     Farmácias     Usuários     Administradores",
                SwingConstants.CENTER);
        designCards.setFont(new Font("Segoe UI", Font.BOLD, 16));
        setLayout(null);
        add(designTitulo);
        designTitulo.setBounds(0, 0, 900, 130);
        add(designCards);
        designCards.setBounds(40, 180, 820, 100);
    }

    private JLabel designTitulo;
    private JLabel designCards;
}
