package descarteconsciente.view;

import descarteconsciente.SessaoAtual;
import descarteconsciente.dao.UsuarioDAO;
import descarteconsciente.model.Usuario;
import descarteconsciente.util.Cores;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Tela de login e cadastro do sistema Descarte Consciente.
 * Usa CardLayout para alternar entre o painel de login e o de cadastro.
 */
public class LoginFrame extends JFrame {

    // ---- Componentes de login ----
    private JTextField    txtEmail;
    private JPasswordField txtSenha;

    // ---- Componentes de cadastro ----
    private JTextField    txtNomeCad;
    private JTextField    txtEmailCad;
    private JPasswordField txtSenhaCad;
    private JPasswordField txtConfSenha;

    private final CardLayout    cardLayout = new CardLayout();
    private final JPanel        cardPanel  = new JPanel(cardLayout);
    private final UsuarioDAO    dao        = new UsuarioDAO();

    public LoginFrame() {
        super("Descarte Consciente");
        buildUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(430, 600);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // ============================================================
    // Construção da UI
    // ============================================================

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.add(buildHeader(), BorderLayout.NORTH);

        cardPanel.add(buildLoginPanel(),    "login");
        cardPanel.add(buildCadastroPanel(), "cadastro");
        root.add(cardPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    // ---- Cabeçalho verde ----

    private JPanel buildHeader() {
        JPanel header = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, Cores.PRIMARY_DARK,
                        getWidth(), getHeight(), Cores.PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(430, 190));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(6, 0, 4, 0);

        // Ícone
        JLabel icon = new JLabel("\uD83D\uDC8A"); // 💊
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        header.add(icon, gbc);

        // Título
        JLabel title = new JLabel("Descarte Consciente");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, gbc);

        // Subtítulo
        JLabel sub = new JLabel("ODS 3  ·  Saúde e Bem-Estar");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(Cores.PRIMARY_LIGHT);
        gbc.insets = new Insets(0, 0, 8, 0);
        header.add(sub, gbc);

        return header;
    }

    // ---- Painel de Login ----

    private JPanel buildLoginPanel() {
        JPanel p = whitePanelWithPadding(30);
        GridBagConstraints gbc = baseGbc();

        // Título do form
        gbc.insets = new Insets(10, 0, 18, 0);
        p.add(label("Bem-vindo!", 20, Font.BOLD, Cores.PRIMARY_DARK), gbc);

        // E-mail
        addFieldLabel(p, gbc, "E-mail");
        txtEmail = new JTextField();
        stylizeField(txtEmail);
        p.add(wrapField(txtEmail), gbc);

        // Senha
        addFieldLabel(p, gbc, "Senha");
        txtSenha = new JPasswordField();
        stylizePasswordField(txtSenha);
        p.add(buildPasswordRow(txtSenha), gbc);

        // Botão entrar
        gbc.insets = new Insets(18, 0, 10, 0);
        JButton btnEntrar = primaryButton("ENTRAR");
        btnEntrar.addActionListener(e -> fazerLogin());
        p.add(btnEntrar, gbc);

        // Divisor
        gbc.insets = new Insets(4, 0, 4, 0);
        p.add(new JSeparator(), gbc);

        // Link criar conta
        gbc.insets = new Insets(10, 0, 4, 0);
        p.add(buildLinkRow("Não tem conta?", "Criar conta",
                e -> cardLayout.show(cardPanel, "cadastro")), gbc);

        return p;
    }

    // ---- Painel de Cadastro ----

    private JPanel buildCadastroPanel() {
        JPanel p = whitePanelWithPadding(30);
        GridBagConstraints gbc = baseGbc();

        // Linha topo: voltar + título
        gbc.insets = new Insets(4, 0, 12, 0);
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(Color.WHITE);
        topRow.add(linkButton("← Voltar", e -> cardLayout.show(cardPanel, "login")), BorderLayout.WEST);
        JLabel t = new JLabel("  Criar Conta");
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setForeground(Cores.PRIMARY_DARK);
        topRow.add(t, BorderLayout.CENTER);
        p.add(topRow, gbc);

        // Campos
        addFieldLabel(p, gbc, "Nome completo");
        txtNomeCad = new JTextField();
        stylizeField(txtNomeCad);
        p.add(wrapField(txtNomeCad), gbc);

        addFieldLabel(p, gbc, "E-mail");
        txtEmailCad = new JTextField();
        stylizeField(txtEmailCad);
        p.add(wrapField(txtEmailCad), gbc);

        addFieldLabel(p, gbc, "Senha");
        txtSenhaCad = new JPasswordField();
        stylizePasswordField(txtSenhaCad);
        p.add(buildPasswordRow(txtSenhaCad), gbc);

        addFieldLabel(p, gbc, "Confirmar senha");
        txtConfSenha = new JPasswordField();
        stylizePasswordField(txtConfSenha);
        p.add(buildPasswordRow(txtConfSenha), gbc);

        // Botão cadastrar
        gbc.insets = new Insets(18, 0, 10, 0);
        JButton btnCad = primaryButton("CRIAR CONTA");
        btnCad.addActionListener(e -> fazerCadastro());
        p.add(btnCad, gbc);

        // Link entrar
        gbc.insets = new Insets(4, 0, 4, 0);
        p.add(new JSeparator(), gbc);
        gbc.insets = new Insets(10, 0, 4, 0);
        p.add(buildLinkRow("Já tem conta?", "Entrar",
                e -> cardLayout.show(cardPanel, "login")), gbc);

        return p;
    }

    // ============================================================
    // Ações
    // ============================================================

    private void fazerLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Preencha e-mail e senha.");
            return;
        }

        Usuario u = dao.login(email, senha);
        if (u == null) {
            mostrarErro("E-mail ou senha incorretos.");
            return;
        }

        SessaoAtual.iniciar(u);
        dispose();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    private void fazerCadastro() {
        String nome   = txtNomeCad.getText().trim();
        String email  = txtEmailCad.getText().trim();
        String senha  = new String(txtSenhaCad.getPassword());
        String conf   = new String(txtConfSenha.getPassword());

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Preencha todos os campos.");
            return;
        }
        if (!senha.equals(conf)) {
            mostrarErro("As senhas não coincidem.");
            return;
        }
        if (senha.length() < 6) {
            mostrarErro("A senha deve ter ao menos 6 caracteres.");
            return;
        }
        if (!email.contains("@")) {
            mostrarErro("Informe um e-mail válido.");
            return;
        }

        Usuario u = new Usuario(nome, email, senha);
        if (dao.cadastrar(u)) {
            JOptionPane.showMessageDialog(this,
                    "Conta criada com sucesso!\nFaça login para continuar.",
                    "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cardPanel, "login");
        } else {
            mostrarErro("Este e-mail já está cadastrado.");
        }
    }

    // ============================================================
    // Helpers de UI
    // ============================================================

    private JPanel whitePanelWithPadding(int pad) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(pad, pad, pad, pad));
        return p;
    }

    private GridBagConstraints baseGbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.fill      = GridBagConstraints.HORIZONTAL;
        g.gridwidth = GridBagConstraints.REMAINDER;
        g.weightx   = 1.0;
        g.insets    = new Insets(3, 0, 3, 0);
        return g;
    }

    private JLabel label(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        return l;
    }

    private void addFieldLabel(JPanel p, GridBagConstraints gbc, String text) {
        gbc.insets = new Insets(8, 0, 2, 0);
        p.add(label(text, 13, Font.PLAIN, Cores.TEXT_SECONDARY), gbc);
        gbc.insets = new Insets(0, 0, 2, 0);
    }

    private void stylizeField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 40));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)));
    }

    private void stylizePasswordField(JPasswordField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 40));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)));
    }

    /** Envolve um campo em um painel com altura definida. */
    private JPanel wrapField(JComponent c) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.setPreferredSize(new Dimension(0, 42));
        wrap.add(c, BorderLayout.CENTER);
        return wrap;
    }

    /** Campo de senha com botão mostrar/ocultar. */
    private JPanel buildPasswordRow(JPasswordField pf) {
        JPanel row = new JPanel(new BorderLayout(4, 0));
        row.setBackground(Color.WHITE);
        row.setPreferredSize(new Dimension(0, 42));
        row.add(pf, BorderLayout.CENTER);

        JButton eye = new JButton("\uD83D\uDC41"); // 👁
        eye.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        eye.setFocusable(false);
        eye.setToolTipText("Mostrar / ocultar senha");
        eye.setPreferredSize(new Dimension(42, 40));
        eye.setBackground(new Color(245, 245, 245));
        eye.setBorder(BorderFactory.createLineBorder(Cores.BORDER, 1, true));
        eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eye.addActionListener(e -> {
            if (pf.getEchoChar() == '\u2022' || pf.getEchoChar() == '*') {
                pf.setEchoChar((char) 0);
            } else {
                pf.setEchoChar('\u2022');
            }
        });

        row.add(eye, BorderLayout.EAST);
        return row;
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? Cores.PRIMARY_DARK : Cores.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(0, 44));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton linkButton(String text, ActionListener action) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Cores.PRIMARY);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(action);
        return b;
    }

    private JPanel buildLinkRow(String textLabel, String linkText, ActionListener action) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        row.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(textLabel);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(Cores.TEXT_SECONDARY);
        row.add(lbl);
        row.add(linkButton(linkText, action));
        return row;
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
}