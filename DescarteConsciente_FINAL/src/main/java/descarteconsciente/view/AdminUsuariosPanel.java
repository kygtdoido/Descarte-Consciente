package descarteconsciente.view;

import descarteconsciente.DatabaseConnection;
import descarteconsciente.dao.UsuarioDAO;
import descarteconsciente.model.Usuario;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * Painel de administração de usuários — CRUD completo (Create via cadastro, Read, Update, Delete).
 *
 * ARQUIVO NOVO — criado para satisfazer o requisito de 3 CRUDs completos.
 * Integrar como segunda aba dentro de AdminPanel.java.
 *
 * Funcionalidades:
 *   - Lista todos os usuários (READ)
 *   - Editar nome e e-mail (UPDATE) → chama UsuarioDAO.atualizar()
 *   - Excluir usuário não-admin (DELETE) → chama UsuarioDAO.excluir()
 *   - Proteção: admins não podem ser excluídos por esta tela
 */
public class AdminUsuariosPanel extends JPanel {

    private final UsuarioDAO dao = new UsuarioDAO();
    private DefaultTableModel modelo;
    private JTable tabela;
    private JLabel lblStatus;

    public AdminUsuariosPanel() {
        initComponents();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(criarTabela(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarUsuarios();
    }

    // ---------------------------------------------------------------- Tabela
    private JScrollPane criarTabela() {
        String[] colunas = {"ID", "Nome", "Email", "Administrador"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }

            @Override
            public Class<?> getColumnClass(int c) {
                return c == 3 ? Boolean.class : String.class;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(34);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setSelectionBackground(new Color(200, 230, 201));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setGridColor(new Color(220, 220, 220));
        tabela.setShowGrid(true);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Cabeçalho
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 245, 245));
        header.setReorderingAllowed(false);

        // Larguras
        tabela.getColumnModel().getColumn(0).setPreferredWidth(55);
        tabela.getColumnModel().getColumn(0).setMaxWidth(70);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(260);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(3).setMaxWidth(130);

        // Renderizador: destaca linha verde para admins
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) {
                    try {
                        int mr = t.convertRowIndexToModel(r);
                        boolean isAdmin = (Boolean) modelo.getValueAt(mr, 3);
                        comp.setBackground(isAdmin ? new Color(232, 245, 233) : Color.WHITE);
                    } catch (Exception ex) {
                        comp.setBackground(Color.WHITE);
                    }
                }
                return comp;
            }
        });

        // Renderizador para coluna Boolean (checkbox com mesma lógica de cor)
        tabela.setDefaultRenderer(Boolean.class, new TableCellRenderer() {
            private final JCheckBox cb = new JCheckBox();
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                cb.setSelected(val != null && (Boolean) val);
                cb.setHorizontalAlignment(JLabel.CENTER);
                try {
                    int mr = t.convertRowIndexToModel(r);
                    boolean isAdmin = (Boolean) modelo.getValueAt(mr, 3);
                    cb.setBackground(sel ? t.getSelectionBackground()
                            : (isAdmin ? new Color(232, 245, 233) : Color.WHITE));
                } catch (Exception ex) {
                    cb.setBackground(Color.WHITE);
                }
                return cb;
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(null);
        return scroll;
    }

    // ---------------------------------------------------------------- Rodapé
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        botoes.setBackground(Color.WHITE);

        JButton btnEditar    = criarBotao("\u270F  Editar",    new Color(21, 101, 192));
        JButton btnExcluir   = criarBotao("\uD83D\uDDD1  Excluir",   new Color(198, 40, 40));
        JButton btnAtualizar = criarBotao("\u21BA  Atualizar", new Color(69, 90, 100));

        btnEditar.addActionListener(e -> editarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());
        btnAtualizar.addActionListener(e -> carregarUsuarios());

        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);

        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setBorder(new EmptyBorder(0, 0, 0, 12));

        rodape.add(botoes,    BorderLayout.WEST);
        rodape.add(lblStatus, BorderLayout.EAST);
        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 36));
        return btn;
    }

    // ---------------------------------------------------------------- Dados
    private void carregarUsuarios() {
        modelo.setRowCount(0);
        String sql = "SELECT id, nome, email, is_admin FROM usuarios ORDER BY id";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getBoolean("is_admin")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar usu\u00E1rios.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        lblStatus.setText(modelo.getRowCount() + " usu\u00E1rio(s) encontrado(s)");
    }

    // ---------------------------------------------------------------- UPDATE
    private void editarUsuario() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usu\u00E1rio para editar.", "Aten\u00E7\u00E3o", JOptionPane.WARNING_MESSAGE);
            return;
        }
        row = tabela.convertRowIndexToModel(row);

        int    id         = (int)    modelo.getValueAt(row, 0);
        String nomeAtual  = (String) modelo.getValueAt(row, 1);
        String emailAtual = (String) modelo.getValueAt(row, 2);

        JTextField campoNome  = new JTextField(nomeAtual);
        JTextField campoEmail = new JTextField(emailAtual);
        campoNome.setPreferredSize(new Dimension(280, 32));
        campoEmail.setPreferredSize(new Dimension(280, 32));

        JPanel painel = new JPanel(new GridLayout(4, 1, 0, 6));
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("Email:"));
        painel.add(campoEmail);

        int res = JOptionPane.showConfirmDialog(
                this, painel,
                "Editar Usu\u00E1rio #" + id,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        String novoNome  = campoNome.getText().trim();
        String novoEmail = campoEmail.getText().trim();

        if (novoNome.isEmpty() || novoEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos.", "Aten\u00E7\u00E3o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setId(id);
        u.setNome(novoNome);
        u.setEmail(novoEmail);

        if (dao.atualizar(u)) {
            JOptionPane.showMessageDialog(this,
                    "Usu\u00E1rio atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar. O e-mail pode j\u00E1 estar em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------------- DELETE
    private void excluirUsuario() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usu\u00E1rio para excluir.", "Aten\u00E7\u00E3o", JOptionPane.WARNING_MESSAGE);
            return;
        }
        row = tabela.convertRowIndexToModel(row);

        boolean isAdmin = (Boolean) modelo.getValueAt(row, 3);
        if (isAdmin) {
            JOptionPane.showMessageDialog(this,
                    "Administradores n\u00E3o podem ser exclu\u00EDdos por esta tela.", "Aten\u00E7\u00E3o", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int    id   = (int)    modelo.getValueAt(row, 0);
        String nome = (String) modelo.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Excluir o usu\u00E1rio \"" + nome + "\" (ID " + id + ")?\nEsta a\u00E7\u00E3o n\u00E3o pode ser desfeita.",
                "Confirmar Exclus\u00E3o",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.excluir(id)) {
                JOptionPane.showMessageDialog(this,
                        "Usu\u00E1rio exclu\u00EDdo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir usu\u00E1rio.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        designTitulo = new JLabel("Administração de Usuários", SwingConstants.CENTER);
        designTabela = new JTable(new Object[][]{}, new String[]{"ID", "Nome", "E-mail", "Administrador"});
        designScroll = new JScrollPane(designTabela);
        designEditar = new JButton("Editar");
        designExcluir = new JButton("Excluir");
        setLayout(null);
        add(designTitulo);
        designTitulo.setBounds(20, 15, 850, 40);
        add(designScroll);
        designScroll.setBounds(20, 70, 850, 330);
        add(designEditar);
        designEditar.setBounds(280, 425, 140, 38);
        add(designExcluir);
        designExcluir.setBounds(480, 425, 140, 38);
    }

    private JLabel designTitulo;
    private JTable designTabela;
    private JScrollPane designScroll;
    private JButton designEditar;
    private JButton designExcluir;
}
