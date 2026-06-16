package descarteconsciente.view;

import descarteconsciente.SessaoAtual;
import descarteconsciente.dao.PontoColetaDAO;
import descarteconsciente.model.PontoDeColeta;
import descarteconsciente.util.Cores;
import descarteconsciente.view.components.Toast;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Painel administrativo para gerenciamento de Pontos de Coleta.
 * Disponível apenas para usuários com is_admin = true.
 */
public class AdminPontosPanel extends JPanel {

    private final PontoColetaDAO dao = new PontoColetaDAO();

    private JTable             tabela;
    private DefaultTableModel  modelo;
    private JLabel             labelContador;
    private List<PontoDeColeta> pontosCached;

    private static final Color ADMIN_DARK  = new Color(27, 94, 32);
    private static final Color BTN_NOVO    = new Color(46, 125, 50);
    private static final Color BTN_EDITAR  = new Color(21, 101, 192);
    private static final Color BTN_EXCLUIR = new Color(183, 28, 28);

    public AdminPontosPanel() {
        initComponents();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Cores.BACKGROUND);
        add(buildHeader(),      BorderLayout.NORTH);
        add(buildTableArea(),   BorderLayout.CENTER);
        add(buildBotoesBarra(), BorderLayout.SOUTH);
        recarregar();
    }

    // ── Construção da UI ──────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(ADMIN_DARK);
        h.setPreferredSize(new Dimension(0, 56));
        h.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel icon  = new JLabel("🛠️");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel title = new JLabel("  Administração de Pontos de Coleta");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Gerencie, crie e edite pontos de descarte");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(Cores.PRIMARY_LIGHT);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        left.setOpaque(false);
        left.add(icon);
        left.add(title);

        h.add(left, BorderLayout.WEST);
        h.add(sub,  BorderLayout.EAST);
        return h;
    }

    private JPanel buildTableArea() {
        String[] colunas = {"ID", "Nome", "Tipo", "Cidade", "Seg-Sex", "Controlados", "Líquidos", "Ativo"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return (c == 5 || c == 6 || c == 7) ? Boolean.class : String.class;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(36);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setSelectionBackground(Cores.PRIMARY_LIGHT);
        tabela.setSelectionForeground(Cores.TEXT_PRIMARY);
        tabela.setGridColor(new Color(235, 235, 235));
        tabela.setShowVerticalLines(false);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(new Color(245, 250, 245));
        tabela.getTableHeader().setForeground(Cores.PRIMARY_DARK);
        tabela.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.PRIMARY_LIGHT));

        int[] widths = {40, 220, 120, 110, 100, 90, 70, 50};
        for (int i = 0; i < widths.length; i++)
            tabela.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(248, 252, 248));
                ((JLabel) comp).setBorder(new EmptyBorder(0, 8, 0, 8));
                return comp;
            }
        });

        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editarSelecionado();
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Cores.BORDER));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Cores.BACKGROUND);
        panel.setBorder(new EmptyBorder(12, 16, 0, 16));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildBotoesBarra() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.BORDER),
                new EmptyBorder(4, 12, 4, 12)));

        JButton btnNovo      = actionButton("➕  Novo Ponto",  BTN_NOVO);
        JButton btnEditar    = actionButton("✏️  Editar",      BTN_EDITAR);
        JButton btnExcluir   = actionButton("🗑️  Desativar",  BTN_EXCLUIR);
        JButton btnAtualizar = actionButton("🔄  Atualizar",   new Color(80, 80, 80));

        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());
        btnAtualizar.addActionListener(e -> recarregar());

        bar.add(btnNovo);
        bar.add(btnEditar);
        bar.add(btnExcluir);
        bar.add(btnAtualizar);
        bar.add(Box.createHorizontalStrut(16));

        labelContador = new JLabel();
        labelContador.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        labelContador.setForeground(Cores.TEXT_SECONDARY);
        bar.add(labelContador);

        return bar;
    }

    private JButton actionButton(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(Color.WHITE);
        b.setBackground(cor);
        b.setPreferredSize(new Dimension(130, 34));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ── Ações ─────────────────────────────────────────────────────

    public void recarregar() {
        pontosCached = dao.listarTodosAdmin();
        modelo.setRowCount(0);
        for (PontoDeColeta p : pontosCached) {
            modelo.addRow(new Object[]{
                p.getId(), p.getNome(), p.getTipoLabel(), p.getCidade(),
                p.getHorarioSegSex(), p.isAceitaControlados(), p.isAceitaLiquidos(), p.isAtivo()
            });
        }
        if (labelContador != null)
            labelContador.setText(pontosCached.size() + " ponto(s) encontrado(s)");
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um ponto na tabela para editar.",
                    "Nenhuma seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        abrirFormulario(pontosCached.get(row));
    }

    private void excluirSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um ponto para desativar.",
                    "Nenhuma seleção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        PontoDeColeta p = pontosCached.get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "<html>Desativar o ponto <b>" + p.getNome() + "</b>?<br>" +
                "Ele não aparecerá mais para os usuários, mas ficará no banco.</html>",
                "Confirmar desativação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION && dao.excluir(p.getId())) {
            Toast.aviso(SwingUtilities.getWindowAncestor(this), "Ponto desativado com sucesso.");
            recarregar();
        }
    }

    // ── Formulário de criação / edição ────────────────────────────

    private void abrirFormulario(PontoDeColeta ponto) {
        boolean editando = ponto != null;
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
                editando ? "Editar Ponto de Coleta" : "Novo Ponto de Coleta",
                Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setSize(540, 620);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField fNome   = campo(editando ? ponto.getNome()    : "");
        JTextField fRua    = campo(editando ? ponto.getRua()     : "");
        JTextField fNumero = campo(editando ? ponto.getNumero()  : "");
        JTextField fBairro = campo(editando ? ponto.getBairro()  : "");
        JTextField fCidade = campo(editando ? ponto.getCidade()  : "");
        JTextField fCep    = campo(editando ? ponto.getCep()     : "");
        JTextField fTel    = campo(editando ? ponto.getTelefone(): "");
        JTextField fSegSex = campo(editando ? ponto.getHorarioSegSex() : "");
        JTextField fSab    = campo(editando ? ponto.getHorarioSab()    : "");
        JTextField fDom    = campo(editando ? ponto.getHorarioDom()    : "");
        JTextField fLat    = campo(editando ? String.valueOf(ponto.getLatitude())  : "0.0");
        JTextField fLon    = campo(editando ? String.valueOf(ponto.getLongitude()) : "0.0");

        String[] tipos = {PontoDeColeta.FARMACIA, PontoDeColeta.DROGARIA,
                          PontoDeColeta.POSTO_SAUDE, PontoDeColeta.PONTO_MUNICIPAL};
        JComboBox<String> cmbTipo = new JComboBox<>(tipos);
        if (editando) cmbTipo.setSelectedItem(ponto.getTipo());

        JCheckBox chkCtrl  = new JCheckBox("Controlados",   editando && ponto.isAceitaControlados());
        JCheckBox chkLiq   = new JCheckBox("Líquidos",      editando && ponto.isAceitaLiquidos());
        JCheckBox chkComp  = new JCheckBox("Comprimidos",   !editando || ponto.isAceitaComprimidos());
        JCheckBox chkPerf  = new JCheckBox("Perfurocort.",  editando && ponto.isAceitaPerfurocortantes());
        JCheckBox chkAtivo = new JCheckBox("Ativo",         !editando || ponto.isAtivo());
        for (JCheckBox c : new JCheckBox[]{chkCtrl, chkLiq, chkComp, chkPerf, chkAtivo})
            c.setBackground(Color.WHITE);

        Object[][] linhas = {
            {"Nome *", fNome},    {"Tipo *", cmbTipo},
            {"Rua", fRua},        {"Número", fNumero},
            {"Bairro", fBairro},  {"Cidade", fCidade},
            {"CEP", fCep},        {"Telefone", fTel},
            {"Seg–Sex", fSegSex}, {"Sábado", fSab},
            {"Domingo", fDom},    {"Latitude", fLat},
            {"Longitude", fLon}
        };

        int row = 0;
        for (Object[] linha : linhas) {
            gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
            JLabel lbl = new JLabel((String) linha[0]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(Cores.TEXT_SECONDARY);
            form.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            form.add((Component) linha[1], gbc);
            row++;
        }

        // Linha "Aceita"
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lblAceita = new JLabel("Aceita");
        lblAceita.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAceita.setForeground(Cores.TEXT_SECONDARY);
        form.add(lblAceita, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JPanel aceitas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        aceitas.setBackground(Color.WHITE);
        aceitas.add(chkComp); aceitas.add(chkLiq); aceitas.add(chkCtrl); aceitas.add(chkPerf);
        form.add(aceitas, gbc);
        row++;

        // Linha "Status"
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lblStatus = new JLabel("Status");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(Cores.TEXT_SECONDARY);
        form.add(lblStatus, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(chkAtivo, gbc);
        row++;

        // Botões
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 5, 5, 5);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.addActionListener(e -> dlg.dispose());

        JButton btnSalvar = actionButton(editando ? "💾  Salvar Alterações" : "➕  Criar Ponto", BTN_NOVO);
        btnSalvar.setPreferredSize(new Dimension(180, 36));
        btnSalvar.addActionListener(e -> {
            String nome = fNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "O campo Nome é obrigatório.",
                        "Campo obrigatório", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PontoDeColeta p2 = editando ? ponto : new PontoDeColeta();
            p2.setNome(nome);
            p2.setTipo((String) cmbTipo.getSelectedItem());
            p2.setRua(fRua.getText().trim());
            p2.setNumero(fNumero.getText().trim());
            p2.setBairro(fBairro.getText().trim());
            p2.setCidade(fCidade.getText().trim());
            p2.setCep(fCep.getText().trim());
            p2.setTelefone(fTel.getText().trim());
            p2.setHorarioSegSex(fSegSex.getText().trim());
            p2.setHorarioSab(fSab.getText().trim());
            p2.setHorarioDom(fDom.getText().trim());
            p2.setAceitaControlados(chkCtrl.isSelected());
            p2.setAceitaLiquidos(chkLiq.isSelected());
            p2.setAceitaComprimidos(chkComp.isSelected());
            p2.setAceitaPerfurocortantes(chkPerf.isSelected());
            p2.setAtivo(chkAtivo.isSelected());
            try { p2.setLatitude(Double.parseDouble(fLat.getText())); }  catch (Exception ex) { p2.setLatitude(0); }
            try { p2.setLongitude(Double.parseDouble(fLon.getText())); } catch (Exception ex) { p2.setLongitude(0); }

            boolean ok = editando
                    ? dao.atualizar(p2)
                    : dao.inserir(p2, SessaoAtual.getUsuario().getId());

            if (ok) {
                dlg.dispose();
                recarregar();
                Toast.sucesso(SwingUtilities.getWindowAncestor(AdminPontosPanel.this),
                        editando ? "Ponto atualizado com sucesso!" : "Novo ponto criado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(dlg,
                        "Erro ao salvar. Verifique se o nome já existe no banco.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(btnCancelar);
        btnPanel.add(btnSalvar);
        form.add(btnPanel, gbc);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        dlg.add(scroll);
        dlg.setVisible(true);
    }

    private JTextField campo(String valor) {
        JTextField f = new JTextField(valor);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.BORDER, 1, true),
                new EmptyBorder(5, 8, 5, 8)));
        return f;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        designTitulo = new JLabel("Administração de Pontos de Coleta", SwingConstants.CENTER);
        designTabela = new JTable(new Object[][]{}, new String[]{"ID", "Nome", "Tipo", "Cidade", "Ativo"});
        designScroll = new JScrollPane(designTabela);
        designNovo = new JButton("Novo");
        designEditar = new JButton("Editar");
        designExcluir = new JButton("Excluir");
        setLayout(null);
        add(designTitulo);
        designTitulo.setBounds(20, 15, 850, 40);
        add(designScroll);
        designScroll.setBounds(20, 70, 850, 330);
        add(designNovo);
        designNovo.setBounds(180, 425, 130, 38);
        add(designEditar);
        designEditar.setBounds(380, 425, 130, 38);
        add(designExcluir);
        designExcluir.setBounds(580, 425, 130, 38);
    }

    private JLabel designTitulo;
    private JTable designTabela;
    private JScrollPane designScroll;
    private JButton designNovo;
    private JButton designEditar;
    private JButton designExcluir;
}
