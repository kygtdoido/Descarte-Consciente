package descarteconsciente.view;

import descarteconsciente.SessaoAtual;
import descarteconsciente.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.SwingUtilities;
/**
 * Janela principal do sistema após o login.
 *
 * Estrutura:
 *   NORTH  → cabeçalho com nome do app e botão sair
 *   CENTER → área de conteúdo trocada pelo bottom-nav (CardLayout)
 *   SOUTH  → barra de navegação inferior (Locais | Lembretes | Sobre)
 */
public class MainFrame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // resto do código...



    private static final String TAB_LOCAIS    = "locais";
    private static final String TAB_LEMBRETES = "lembretes";
    private static final String TAB_SOBRE     = "sobre";

    private final CardLayout   contentLayout = new CardLayout();
    private final JPanel       contentPanel  = new JPanel(contentLayout);

    private ListaPontosPanel   listaPontosPanel;
    private LembretesPanel     lembretesPanel;

    private JButton btnLocais, btnLembretes, btnSobre;

   
public MainFrame() {
    buildUI();

    setTitle("Descarte Consciente");
    setSize(900, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
}
    // ============================================================
    // Construção
    // ============================================================

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Cores.BACKGROUND);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        root.add(buildBottomNav(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ---- Cabeçalho ----

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, Cores.PRIMARY_DARK,
                        getWidth(), 0, Cores.PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 64));
        header.setBorder(new EmptyBorder(0, 18, 0, 18));

        // Esquerda: ícone + nome
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        JLabel icon = new JLabel("\uD83D\uDC8A");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        JLabel title = new JLabel("Descarte Consciente");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        left.add(icon);
        left.add(title);
        header.add(left, BorderLayout.WEST);

        // Centro: boas-vindas
        String nome = SessaoAtual.getUsuario() != null
                ? "Olá, " + primeiroNome(SessaoAtual.getUsuario().getNome()) + "!"
                : "Olá!";
        JLabel welcome = new JLabel(nome, SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        welcome.setForeground(Cores.PRIMARY_LIGHT);
        header.add(welcome, BorderLayout.CENTER);

        // Direita: botão sair
        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSair.setForeground(Color.WHITE);
        btnSair.setBackground(new Color(255, 255, 255, 40));
        btnSair.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,120), 1, true));
        btnSair.setFocusPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.setPreferredSize(new Dimension(70, 32));
        btnSair.addActionListener(e -> sair());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnSair);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ---- Conteúdo central ----

    private JPanel buildContent() {
        listaPontosPanel = new ListaPontosPanel();
        lembretesPanel   = new LembretesPanel();

        contentPanel.setBackground(Cores.BACKGROUND);
        contentPanel.add(listaPontosPanel,    TAB_LOCAIS);
        contentPanel.add(lembretesPanel,      TAB_LEMBRETES);
        contentPanel.add(new SobrePanel(),    TAB_SOBRE);
        return contentPanel;
    }

    // ---- Barra de navegação inferior ----

    private JPanel buildBottomNav() {
        JPanel nav = new JPanel(new GridLayout(1, 3));
        nav.setBackground(Color.WHITE);
        nav.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.BORDER));
        nav.setPreferredSize(new Dimension(0, 58));

        btnLocais    = navButton("\uD83D\uDCCD", "Locais");    // 📍
        btnLembretes = navButton("\uD83D\uDD14", "Lembretes"); // 🔔
        btnSobre     = navButton("\u2139",        "Sobre");    // ℹ

        btnLocais.addActionListener(e -> switchTab(TAB_LOCAIS));
        btnLembretes.addActionListener(e -> switchTab(TAB_LEMBRETES));
        btnSobre.addActionListener(e -> switchTab(TAB_SOBRE));

        nav.add(btnLocais);
        nav.add(btnLembretes);
        nav.add(btnSobre);

        setNavActive(btnLocais);  // aba inicial
        return nav;
    }

    private JButton navButton(String emoji, String label) {
        JButton b = new JButton("<html><center>"
                + "<span style='font-size:16px'>" + emoji + "</span>"
                + "<br><span style='font-size:10px'>" + label + "</span>"
                + "</center></html>");
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        b.setForeground(Cores.TEXT_SECONDARY);
        return b;
    }

    private void switchTab(String tab) {
        contentLayout.show(contentPanel, tab);
        setNavActive(switch (tab) {
            case TAB_LOCAIS    -> btnLocais;
            case TAB_LEMBRETES -> { lembretesPanel.recarregar(); yield btnLembretes; }
            case TAB_SOBRE     -> btnSobre;
            default            -> btnLocais;
        });
    }

    private void setNavActive(JButton active) {
        for (JButton b : new JButton[]{btnLocais, btnLembretes, btnSobre}) {
            b.setForeground(Cores.TEXT_SECONDARY);
            b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        }
        active.setForeground(Cores.PRIMARY);
        active.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
    }

    // ============================================================
    // Ações
    // ============================================================

    private void sair() {
        int ok = JOptionPane.showConfirmDialog(this,
                "Deseja sair da sua conta?", "Confirmar saída",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            SessaoAtual.encerrar();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    // ============================================================
    // Helpers
    // ============================================================

    private String primeiroNome(String nomeCompleto) {
        if (nomeCompleto == null) return "";
        String[] parts = nomeCompleto.trim().split("\\s+");
        return parts[0];
    }
}