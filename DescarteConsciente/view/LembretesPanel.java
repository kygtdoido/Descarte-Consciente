package descarteconsciente.view;

import descarteconsciente.SessaoAtual;
import descarteconsciente.dao.LembreteDAO;
import descarteconsciente.model.Lembrete;
import descarteconsciente.util.Cores;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Painel de lembretes de validade de medicamentos.
 * Permite adicionar, listar e remover lembretes do usuário logado.
 */
public class LembretesPanel extends JPanel {

    private static final DateTimeFormatter FMT_BR  = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final LembreteDAO dao = new LembreteDAO();

    // Form de adição
    private JTextField    txtMedicamento;
    private JTextField    txtDataValidade;

    // Lista
    private JPanel        listaContainer;
    private JScrollPane   scroll;

    public LembretesPanel() {
        setLayout(new BorderLayout());
        setBackground(Cores.BACKGROUND);
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildForm(),      BorderLayout.CENTER);
        recarregar();
    }

    // ============================================================
    // Construção
    // ============================================================

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Cores.PRIMARY_DARK,
                        getWidth(), 0, Cores.PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setPreferredSize(new Dimension(0, 52));
        p.setBorder(new EmptyBorder(0, 18, 0, 18));

        JLabel icon  = new JLabel("\uD83D\uDD14 ");  // 🔔
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        JLabel title = new JLabel("Lembretes de Validade");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        icon.setForeground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        left.setOpaque(false);
        left.add(icon);
        left.add(title);
        p.add(left, BorderLayout.WEST);
        return p;
    }

    private JPanel buildForm() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Cores.BACKGROUND);

        // Card de adição
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, Cores.BORDER),
                new EmptyBorder(16, 20, 16, 20)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(4, 4, 4, 4);

        // Título do form
        g.gridx = 0; g.gridy = 0; g.gridwidth = 3; g.weightx = 1;
        JLabel formTitle = new JLabel("Adicionar lembrete");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(Cores.PRIMARY_DARK);
        formCard.add(formTitle, g);

        // Campo medicamento
        g.gridwidth = 2; g.gridy = 1; g.weightx = 0.65;
        txtMedicamento = styledField("Nome do medicamento");
        formCard.add(labeledField("Medicamento", txtMedicamento), g);

        // Campo data
        g.gridx = 2; g.gridwidth = 1; g.weightx = 0.35;
        txtDataValidade = styledField("DD/MM/AAAA");
        formCard.add(labeledField("Validade (DD/MM/AAAA)", txtDataValidade), g);

        // Botão adicionar
        g.gridx = 0; g.gridy = 2; g.gridwidth = 3; g.weightx = 1;
        g.insets = new Insets(10, 4, 4, 4);
        JButton btnAdd = primaryButton("+ Adicionar Lembrete");
        btnAdd.addActionListener(e -> adicionarLembrete());
        formCard.add(btnAdd, g);

        root.add(formCard, BorderLayout.NORTH);

        // Lista de lembretes
        listaContainer = new JPanel();
        listaContainer.setLayout(new BoxLayout(listaContainer, BoxLayout.Y_AXIS));
        listaContainer.setBackground(Cores.BACKGROUND);

        scroll = new JScrollPane(listaContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        return root;
    }

    // ============================================================
    // Lógica
    // ============================================================

    public void recarregar() {
        if (listaContainer == null) return;
        listaContainer.removeAll();

        if (!SessaoAtual.isLogado()) {
            listaContainer.add(msgLabel("Faça login para ver seus lembretes."));
            listaContainer.revalidate();
            return;
        }

        List<Lembrete> lista = dao.listarPorUsuario(SessaoAtual.getUsuario().getId());

        if (lista.isEmpty()) {
            listaContainer.add(msgLabel("Nenhum lembrete cadastrado."));
        } else {
            // Separar: vencidos / atenção / ok
            for (Lembrete l : lista) {
                listaContainer.add(buildItemLembrete(l));
                listaContainer.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        listaContainer.revalidate();
        listaContainer.repaint();
    }

    private void adicionarLembrete() {
        if (!SessaoAtual.isLogado()) {
            JOptionPane.showMessageDialog(this, "Você precisa estar logado.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String med   = txtMedicamento.getText().trim();
        String data  = txtDataValidade.getText().trim();

        if (med.isEmpty() || data.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha o nome do medicamento e a data de validade.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate validade;
        try {
            validade = LocalDate.parse(data, FMT_BR);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use DD/MM/AAAA.",
                    "Data inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Lembrete l = new Lembrete(SessaoAtual.getUsuario().getId(), med, validade);
        if (dao.salvar(l)) {
            txtMedicamento.setText("");
            txtDataValidade.setText("");
            recarregar();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar lembrete. Verifique a conexão.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // Cards de lembrete
    // ============================================================

    private JPanel buildItemLembrete(Lembrete l) {
        Color borda;
        Color bgStatus;
        if      (l.isVencido())      { borda = Cores.ERROR;   bgStatus = new Color(255, 235, 238); }
        else if (l.isVenceHoje())    { borda = Cores.WARNING; bgStatus = new Color(255, 248, 225); }
        else if (l.isVenceEmBreve()) { borda = Cores.WARNING; bgStatus = new Color(255, 253, 231); }
        else                         { borda = Cores.PRIMARY; bgStatus = new Color(232, 245, 233); }

        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 4, 0, 0, borda),
                new EmptyBorder(12, 14, 12, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Ícone medicamento
        JLabel icon = new JLabel("\uD83D\uDC8A"); // 💊
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        card.add(icon, BorderLayout.WEST);

        // Info
        JPanel info = new JPanel();
        info.setBackground(Color.WHITE);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nome = new JLabel(l.getMedicamento());
        nome.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel data = new JLabel("Validade: " + l.getDataValidade().format(FMT_BR));
        data.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        data.setForeground(Cores.TEXT_SECONDARY);

        JLabel status = new JLabel(l.getStatusLabel());
        status.setFont(new Font("Segoe UI", Font.BOLD, 11));
        status.setOpaque(true);
        status.setBackground(bgStatus);
        status.setForeground(borda);
        status.setBorder(new EmptyBorder(2, 6, 2, 6));

        info.add(nome);
        info.add(Box.createRigidArea(new Dimension(0, 2)));
        info.add(data);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(status);
        card.add(info, BorderLayout.CENTER);

        // Botão remover
        JButton btnRem = new JButton("×");
        btnRem.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRem.setForeground(Cores.ERROR);
        btnRem.setBorderPainted(false);
        btnRem.setContentAreaFilled(false);
        btnRem.setFocusPainted(false);
        btnRem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRem.setToolTipText("Remover lembrete");
        btnRem.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Remover lembrete de \"" + l.getMedicamento() + "\"?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                dao.remover(l.getId());
                recarregar();
            }
        });
        card.add(btnRem, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Cores.BACKGROUND);
        wrapper.setBorder(new EmptyBorder(0, 12, 0, 12));
        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    // ============================================================
    // Helpers de UI
    // ============================================================

    private JPanel labeledField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(Cores.TEXT_SECONDARY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(0, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.BORDER, 1, true),
                new EmptyBorder(4, 10, 4, 10)));
        f.setToolTipText(placeholder);
        return f;
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? Cores.PRIMARY_DARK : Cores.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(0, 38));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JLabel msgLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        l.setForeground(Cores.TEXT_SECONDARY);
        l.setBorder(new EmptyBorder(40, 0, 0, 0));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
}