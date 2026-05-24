package descarteconsciente.view;

import descarteconsciente.dao.PontoColetaDAO;
import descarteconsciente.model.PontoDeColeta;
import descarteconsciente.util.Cores;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Painel de Locais: barra de busca, filtro por tipo, e abas Lista/Mapa.
 */
public class ListaPontosPanel extends JPanel {

    private final PontoColetaDAO dao = new PontoColetaDAO();

    // Controles de busca / filtro
    private JTextField    campoBusca;
    private JComboBox<String> cmbTipo;

    // Área de conteúdo (lista ou mapa)
    private final CardLayout   viewLayout = new CardLayout();
    private final JPanel       viewPanel  = new JPanel(viewLayout);

    // Lista
    private JPanel listaContainer;

    // Mapa
    private MapaPanel mapaPanel;

    // Botões de aba
    private JButton btnLista, btnMapa;

    public ListaPontosPanel() {
        setLayout(new BorderLayout());
        setBackground(Cores.BACKGROUND);
        add(buildTopBar(),   BorderLayout.NORTH);
        add(buildViewArea(), BorderLayout.CENTER);
        buscar();  // carrega tudo na abertura
    }

    // ============================================================
    // Barra superior (busca + filtro + abas)
    // ============================================================

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(0, 8));
        bar.setBackground(Color.WHITE);
        bar.setBorder(new EmptyBorder(12, 16, 0, 16));

        // Linha 1: campo de busca
        bar.add(buildSearchRow(), BorderLayout.NORTH);

        // Linha 2: filtro de tipo + abas lista/mapa
        JPanel row2 = new JPanel(new BorderLayout(10, 0));
        row2.setBackground(Color.WHITE);
        row2.setBorder(new EmptyBorder(8, 0, 0, 0));
        row2.add(buildFiltroTipo(), BorderLayout.WEST);
        row2.add(buildTabButtons(), BorderLayout.EAST);
        bar.add(row2, BorderLayout.CENTER);

        // Linha divisória
        JPanel separator = new JPanel();
        separator.setBackground(Cores.BORDER);
        separator.setPreferredSize(new Dimension(0, 1));
        bar.add(separator, BorderLayout.SOUTH);

        return bar;
    }

    private JPanel buildSearchRow() {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(Color.WHITE);

        // Ícone lupa
        JLabel lupa = new JLabel(" \uD83D\uDD0D "); // 🔍
        lupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lupa.setForeground(Cores.TEXT_SECONDARY);

        campoBusca = new JTextField();
        campoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.BORDER, 1, true),
                new EmptyBorder(6, 8, 6, 8)));
        campoBusca.setToolTipText("Buscar por nome, bairro ou cidade");
        campoBusca.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { buscar(); }
        });

        JPanel fieldWrap = new JPanel(new BorderLayout());
        fieldWrap.setBackground(Color.WHITE);
        fieldWrap.setBorder(BorderFactory.createLineBorder(Cores.BORDER, 1, true));
        fieldWrap.add(lupa, BorderLayout.WEST);
        fieldWrap.add(campoBusca, BorderLayout.CENTER);

        row.add(fieldWrap, BorderLayout.CENTER);
        return row;
    }

    private JComboBox<String> buildFiltroTipo() {
        String[] opcoes = {"Todos os tipos", "Farmácias", "Drogarias",
                           "UBS / Postos de Saúde", "Pontos Municipais"};
        cmbTipo = new JComboBox<>(opcoes);
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTipo.setPreferredSize(new Dimension(200, 34));
        cmbTipo.addActionListener(e -> buscar());
        return cmbTipo;
    }

    private JPanel buildTabButtons() {
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tabs.setBackground(Color.WHITE);

        btnLista = tabButton("Lista");
        btnMapa  = tabButton("Mapa");

        btnLista.addActionListener(e -> {
            viewLayout.show(viewPanel, "lista");
            setTabActive(btnLista, btnMapa);
        });
        btnMapa.addActionListener(e -> {
            viewLayout.show(viewPanel, "mapa");
            setTabActive(btnMapa, btnLista);
            mapaPanel.repaint();
        });

        setTabActive(btnLista, btnMapa);
        tabs.add(btnLista);
        tabs.add(btnMapa);
        return tabs;
    }

    private JButton tabButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(80, 34));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void setTabActive(JButton active, JButton inactive) {
        active.setBackground(Cores.PRIMARY);
        active.setForeground(Color.WHITE);
        active.setBorder(BorderFactory.createLineBorder(Cores.PRIMARY, 1));
        inactive.setBackground(Color.WHITE);
        inactive.setForeground(Cores.PRIMARY);
        inactive.setBorder(BorderFactory.createLineBorder(Cores.PRIMARY, 1));
    }

    // ============================================================
    // Área de conteúdo
    // ============================================================

    private JPanel buildViewArea() {
        // Lista
        listaContainer = new JPanel();
        listaContainer.setLayout(new BoxLayout(listaContainer, BoxLayout.Y_AXIS));
        listaContainer.setBackground(Cores.BACKGROUND);

        JScrollPane scroll = new JScrollPane(listaContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(Cores.BACKGROUND);

        // Mapa
        mapaPanel = new MapaPanel();

        viewPanel.add(scroll,    "lista");
        viewPanel.add(mapaPanel, "mapa");
        return viewPanel;
    }

    // ============================================================
    // Busca e renderização dos cards
    // ============================================================

    public void buscar() {
        String texto = campoBusca == null ? "" : campoBusca.getText().trim();
        String tipo  = tipoSelecionado();
        List<PontoDeColeta> pontos = dao.buscar(texto, tipo);

        listaContainer.removeAll();

        if (pontos.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum ponto encontrado.", SwingConstants.CENTER);
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            vazio.setForeground(Cores.TEXT_SECONDARY);
            vazio.setBorder(new EmptyBorder(40, 0, 0, 0));
            listaContainer.add(vazio);
        } else {
            for (PontoDeColeta p : pontos) {
                listaContainer.add(buildCard(p));
                listaContainer.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        listaContainer.revalidate();
        listaContainer.repaint();

        if (mapaPanel != null) mapaPanel.setDados(pontos);
    }

    private String tipoSelecionado() {
        if (cmbTipo == null) return "TODOS";
        return switch (cmbTipo.getSelectedIndex()) {
            case 1 -> PontoDeColeta.FARMACIA;
            case 2 -> PontoDeColeta.DROGARIA;
            case 3 -> PontoDeColeta.POSTO_SAUDE;
            case 4 -> PontoDeColeta.PONTO_MUNICIPAL;
            default -> "TODOS";
        };
    }

    // ---- Card de um ponto ----

    private JPanel buildCard(PontoDeColeta p) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 4, 0, 0, Cores.PRIMARY),
                new EmptyBorder(12, 14, 12, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));

        // Ícone
        JLabel icon = new JLabel(p.getIcone());
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        icon.setPreferredSize(new Dimension(44, 44));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(icon, BorderLayout.WEST);

        // Info central
        JPanel info = new JPanel();
        info.setBackground(Color.WHITE);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nome = new JLabel(p.getNome());
        nome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nome.setForeground(Cores.TEXT_PRIMARY);

        JLabel end = new JLabel(p.getEnderecoCompleto());
        end.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        end.setForeground(Cores.TEXT_SECONDARY);

        JLabel hora = new JLabel("\u23F0 " + p.getHorariosFormatados()); // ⏰
        hora.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
        hora.setForeground(Cores.TEXT_SECONDARY);

        // Badges de tipos aceitos
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        badges.setBackground(Color.WHITE);
        if (p.isAceitaComprimidos())      badges.add(badge("Comprimidos",  new Color(200,230,201)));
        if (p.isAceitaLiquidos())         badges.add(badge("Líquidos",     new Color(187,222,251)));
        if (p.isAceitaControlados())      badges.add(badge("Controlados",  new Color(255,224,178)));
        if (p.isAceitaPerfurocortantes()) badges.add(badge("Perfurocort.", new Color(255,204,188)));

        info.add(nome);
        info.add(Box.createRigidArea(new Dimension(0, 2)));
        info.add(end);
        info.add(hora);
        info.add(badges);
        card.add(info, BorderLayout.CENTER);

        // Seta direita
        JLabel arrow = new JLabel("›");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 22));
        arrow.setForeground(Cores.PRIMARY);
        card.add(arrow, BorderLayout.EAST);

        // Hover
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 248, 240));
                info.setBackground(new Color(240, 248, 240));
                badges.setBackground(new Color(240, 248, 240));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                info.setBackground(Color.WHITE);
                badges.setBackground(Color.WHITE);
            }
            @Override public void mouseClicked(MouseEvent e) {
                mostrarDetalhes(p);
            }
        });
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Wrapper com margens horizontais
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Cores.BACKGROUND);
        wrapper.setBorder(new EmptyBorder(0, 12, 0, 12));
        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel badge(String texto, Color bg) {
        JLabel b = new JLabel(texto);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        b.setOpaque(true);
        b.setBackground(bg);
        b.setForeground(Cores.TEXT_PRIMARY);
        b.setBorder(new EmptyBorder(2, 6, 2, 6));
        return b;
    }

    // ---- Diálogo de detalhes ----

    private void mostrarDetalhes(PontoDeColeta p) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='width:280px;font-family:Segoe UI'>");
        sb.append("<h3 style='color:#1B5E20'>").append(p.getNome()).append("</h3>");
        sb.append("<b>Tipo:</b> ").append(p.getTipoLabel()).append("<br>");
        sb.append("<b>Endereço:</b> ").append(p.getEnderecoCompleto()).append("<br>");
        if (p.getCep() != null)      sb.append("<b>CEP:</b> ").append(p.getCep()).append("<br>");
        if (p.getTelefone() != null) sb.append("<b>Telefone:</b> ").append(p.getTelefone()).append("<br>");
        sb.append("<br><b>Horários:</b><br>");
        if (p.getHorarioSegSex() != null) sb.append("Seg–Sex: ").append(p.getHorarioSegSex()).append("<br>");
        if (p.getHorarioSab()    != null) sb.append("Sábado: ").append(p.getHorarioSab()).append("<br>");
        if (p.getHorarioDom()    != null) sb.append("Domingo: ").append(p.getHorarioDom()).append("<br>");
        sb.append("<br><b>Aceita:</b> ");
        if (p.isAceitaComprimidos())       sb.append("Comprimidos  ");
        if (p.isAceitaLiquidos())          sb.append("Líquidos  ");
        if (p.isAceitaControlados())       sb.append("Controlados  ");
        if (p.isAceitaPerfurocortantes())  sb.append("Perfurocortantes");
        sb.append("</body></html>");

        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                new JLabel(sb.toString()),
                p.getNome(),
                JOptionPane.INFORMATION_MESSAGE);
    }
}