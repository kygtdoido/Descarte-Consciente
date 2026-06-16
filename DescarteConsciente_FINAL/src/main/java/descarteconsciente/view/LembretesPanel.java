package descarteconsciente.view;

import descarteconsciente.SessaoAtual;
import descarteconsciente.dao.LembreteDAO;
import descarteconsciente.model.Lembrete;
import descarteconsciente.util.Cores;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Painel de lembretes de validade de medicamentos.
 *
 * ATUALIZAÇÃO: cada card agora tem botão ✏ (editar) ao lado do ✕ (remover).
 * O botão editar abre um dialog com os campos preenchidos e chama LembreteDAO.atualizar().
 */
public class LembretesPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final LembreteDAO dao = new LembreteDAO();
    private JPanel listaPanel;
    private JTextField txtMedicamento;
    private JTextField txtData;

    public LembretesPanel() {
        initComponents();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(criarFormulario(), BorderLayout.NORTH);

        listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(Color.WHITE);
        listaPanel.setBorder(new EmptyBorder(10, 16, 10, 16));

        JScrollPane scroll = new JScrollPane(listaPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        carregarLembretes();
    }

    // ---------------------------------------------------------------- Formulário
    private JPanel criarFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                new EmptyBorder(16, 16, 16, 16)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Título
        JLabel lblTitulo = new JLabel("Adicionar lembrete");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        form.add(lblTitulo, gbc);

        // Labels
        JLabel lblMed = new JLabel("Medicamento");
        lblMed.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.65;
        form.add(lblMed, gbc);

        JLabel lblData = new JLabel("Validade (DD/MM/AAAA)");
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.weightx = 0.35;
        form.add(lblData, gbc);

        // Campos
        txtMedicamento = new JTextField();
        txtMedicamento.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMedicamento.setPreferredSize(new Dimension(0, 36));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.65;
        form.add(txtMedicamento, gbc);

        txtData = new JTextField();
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtData.setPreferredSize(new Dimension(0, 36));
        gbc.gridx = 1; gbc.weightx = 0.35;
        form.add(txtData, gbc);

        // Botão adicionar
        JButton btnAdd = new JButton("+ Adicionar Lembrete");
        btnAdd.setBackground(Cores.PRIMARY);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(0, 40));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 1.0;
        form.add(btnAdd, gbc);

        btnAdd.addActionListener(e -> adicionarLembrete());
        txtData.addActionListener(e -> adicionarLembrete()); // Enter no campo de data também submete

        return form;
    }

    // ---------------------------------------------------------------- Ações
    private void adicionarLembrete() {
        String med     = txtMedicamento.getText().trim();
        String dataStr = txtData.getText().trim();

        if (med.isEmpty() || dataStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos.", "Aten\u00E7\u00E3o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate data = LocalDate.parse(dataStr, FMT);
            Lembrete l = new Lembrete();
            l.setUsuarioId(SessaoAtual.getUsuario().getId());
            l.setMedicamento(med);
            l.setDataValidade(data);

            if (dao.salvar(l)) {
                txtMedicamento.setText("");
                txtData.setText("");
                txtMedicamento.requestFocus();
                carregarLembretes();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar lembrete.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Data inv\u00E1lida. Use o formato DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Abre dialog de edição e chama LembreteDAO.atualizar(). */
    private void abrirDialogEditar(Lembrete l) {
        JTextField campoMed  = new JTextField(l.getMedicamento());
        JTextField campoData = new JTextField(l.getDataValidade().format(FMT));
        campoMed.setPreferredSize(new Dimension(300, 32));
        campoData.setPreferredSize(new Dimension(300, 32));

        JPanel painel = new JPanel(new GridLayout(4, 1, 0, 6));
        painel.add(new JLabel("Medicamento:"));
        painel.add(campoMed);
        painel.add(new JLabel("Validade (DD/MM/AAAA):"));
        painel.add(campoData);

        int res = JOptionPane.showConfirmDialog(
                this, painel,
                "Editar Lembrete",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        String novoMed  = campoMed.getText().trim();
        String novaData = campoData.getText().trim();

        if (novoMed.isEmpty() || novaData.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos.", "Aten\u00E7\u00E3o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            l.setMedicamento(novoMed);
            l.setDataValidade(LocalDate.parse(novaData, FMT));

            if (dao.atualizar(l)) {
                carregarLembretes();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao atualizar lembrete.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Data inv\u00E1lida. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerLembrete(int id) {
        int ok = JOptionPane.showConfirmDialog(
                this, "Remover este lembrete?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            dao.remover(id);
            carregarLembretes();
        }
    }

    // ---------------------------------------------------------------- Lista
    public void recarregar() {
        carregarLembretes();
    }

    private void carregarLembretes() {
        listaPanel.removeAll();
        List<Lembrete> lista = dao.listarPorUsuario(SessaoAtual.getUsuario().getId());

        if (lista.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum lembrete cadastrado.", JLabel.CENTER);
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            vazio.setForeground(Color.GRAY);
            vazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            vazio.setBorder(new EmptyBorder(30, 0, 30, 0));
            listaPanel.add(vazio);
        } else {
            for (Lembrete l : lista) {
                listaPanel.add(criarCard(l));
                listaPanel.add(Box.createVerticalStrut(8));
            }
        }

        listaPanel.revalidate();
        listaPanel.repaint();
    }

    private JPanel criarCard(Lembrete l) {
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), l.getDataValidade());
        Color corBorda = dias < 0 ? new Color(198, 40, 40)
                       : dias <= 30 ? new Color(255, 152, 0)
                       : new Color(56, 142, 60);

        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setBorder(new CompoundBorder(
                new MatteBorder(0, 5, 0, 0, corBorda),
                new EmptyBorder(12, 12, 12, 12)));

        // Ícone
        JLabel icone = new JLabel("\uD83D\uDC8A");
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        card.add(icone, BorderLayout.WEST);

        // Informações
        JPanel info = new JPanel(new GridLayout(3, 1, 0, 3));
        info.setOpaque(false);

        JLabel lblNome = new JLabel(l.getMedicamento());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblData = new JLabel("Validade: " + l.getDataValidade().format(FMT));
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblData.setForeground(Color.GRAY);

        JLabel badge = criarBadge(dias);

        info.add(lblNome);
        info.add(lblData);
        info.add(badge);
        card.add(info, BorderLayout.CENTER);

        // ✅ Botões: ✏ Editar + ✕ Remover
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        botoes.setOpaque(false);

        JButton btnEditar = new JButton("\u270F");   // ✏
        estilizarBotaoIcone(btnEditar, new Color(21, 101, 192), "Editar lembrete");
        btnEditar.addActionListener(e -> abrirDialogEditar(l));

        JButton btnRemover = new JButton("\u2715");  // ✕
        estilizarBotaoIcone(btnRemover, new Color(198, 40, 40), "Remover lembrete");
        btnRemover.addActionListener(e -> removerLembrete(l.getId()));

        botoes.add(btnEditar);
        botoes.add(btnRemover);
        card.add(botoes, BorderLayout.EAST);

        return card;
    }

    private JLabel criarBadge(long dias) {
        JLabel badge = new JLabel();
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(2, 8, 2, 8));

        if (dias < 0) {
            badge.setText("Vencido h\u00E1 " + Math.abs(dias) + " dia(s)");
            badge.setBackground(new Color(255, 205, 210));
            badge.setForeground(new Color(180, 0, 0));
        } else if (dias == 0) {
            badge.setText("Vence hoje!");
            badge.setBackground(new Color(255, 243, 205));
            badge.setForeground(new Color(130, 80, 0));
        } else if (dias <= 30) {
            badge.setText("Vence em " + dias + " dia(s)");
            badge.setBackground(new Color(255, 243, 205));
            badge.setForeground(new Color(130, 80, 0));
        } else {
            badge.setText("V\u00E1lido");
            badge.setBackground(new Color(200, 230, 200));
            badge.setForeground(new Color(20, 100, 20));
        }
        return badge;
    }

    private void estilizarBotaoIcone(JButton btn, Color cor, String tooltip) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(cor);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(32, 32));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        designTitulo = new JLabel("Adicionar lembrete");
        designMedicamento = new JTextField();
        designData = new JTextField("dd/MM/aaaa");
        designAdicionar = new JButton("Adicionar");
        designLista = new JList<String>(new String[]{"Medicamento - Data de validade"});
        setLayout(null);
        add(designTitulo);
        designTitulo.setBounds(25, 20, 220, 30);
        add(designMedicamento);
        designMedicamento.setBounds(25, 60, 300, 38);
        add(designData);
        designData.setBounds(345, 60, 180, 38);
        add(designAdicionar);
        designAdicionar.setBounds(545, 60, 120, 38);
        add(designLista);
        designLista.setBounds(25, 125, 840, 340);
    }

    private JLabel designTitulo;
    private JTextField designMedicamento;
    private JTextField designData;
    private JButton designAdicionar;
    private JList<String> designLista;
}
