package descarteconsciente.view;

import descarteconsciente.SessaoAtual;
import descarteconsciente.dao.UsuarioDAO;
import descarteconsciente.model.Usuario;
import descarteconsciente.util.Cores;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Tela de login criada como JFrame Form do NetBeans.
 */
public class LoginFrame extends javax.swing.JFrame {

    private final UsuarioDAO dao = new UsuarioDAO();

    public LoginFrame() {
        initComponents();
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        btnEntrar = new javax.swing.JButton();
        btnCadastrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Descarte Consciente");
        setResizable(false);
        getContentPane().setLayout(null);

        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Cores.PRIMARY_DARK);
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Descarte Consciente");
        getContentPane().add(lblTitulo);
        lblTitulo.setBounds(45, 55, 320, 40);

        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Cores.TEXT_SECONDARY);
        lblSubtitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubtitulo.setText("Saúde e bem-estar");
        getContentPane().add(lblSubtitulo);
        lblSubtitulo.setBounds(45, 95, 320, 25);

        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setText("E-mail");
        getContentPane().add(lblEmail);
        lblEmail.setBounds(65, 170, 280, 25);

        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        getContentPane().add(txtEmail);
        txtEmail.setBounds(65, 195, 280, 42);

        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSenha.setText("Senha");
        getContentPane().add(lblSenha);
        lblSenha.setBounds(65, 255, 280, 25);

        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        getContentPane().add(txtSenha);
        txtSenha.setBounds(65, 280, 280, 42);

        btnEntrar.setBackground(Cores.PRIMARY);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setText("Entrar");
        btnEntrar.setFocusPainted(false);
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEntrar);
        btnEntrar.setBounds(65, 350, 280, 48);

        btnCadastrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCadastrar.setForeground(Cores.PRIMARY);
        btnCadastrar.setText("Cadastrar-se");
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setContentAreaFilled(false);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCadastrar);
        btnCadastrar.setBounds(145, 425, 120, 30);

        setSize(new java.awt.Dimension(430, 540));
    }// </editor-fold>//GEN-END:initComponents

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
        fazerLogin();
    }//GEN-LAST:event_btnEntrarActionPerformed

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        mostrarCadastro();
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void fazerLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Preencha e-mail e senha.");
            return;
        }

        Usuario usuario = dao.login(email, senha);
        if (usuario == null) {
            mostrarErro("E-mail ou senha incorretos.");
            return;
        }

        SessaoAtual.iniciar(usuario);
        dispose();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private void mostrarCadastro() {
        JTextField nome = new JTextField();
        JTextField email = new JTextField();
        JPasswordField senha = new JPasswordField();
        JPasswordField confirmar = new JPasswordField();

        JPanel painel = new JPanel(new GridLayout(0, 1, 4, 4));
        painel.add(new JLabel("Nome completo"));
        painel.add(nome);
        painel.add(new JLabel("E-mail"));
        painel.add(email);
        painel.add(new JLabel("Senha"));
        painel.add(senha);
        painel.add(new JLabel("Confirmar senha"));
        painel.add(confirmar);

        int resposta = JOptionPane.showConfirmDialog(this, painel,
                "Criar conta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resposta != JOptionPane.OK_OPTION) {
            return;
        }

        String nomeTexto = nome.getText().trim();
        String emailTexto = email.getText().trim();
        String senhaTexto = new String(senha.getPassword());
        String confirmarTexto = new String(confirmar.getPassword());

        if (nomeTexto.isEmpty() || emailTexto.isEmpty() || senhaTexto.isEmpty()) {
            mostrarErro("Preencha todos os campos.");
            return;
        }
        if (!senhaTexto.equals(confirmarTexto)) {
            mostrarErro("As senhas não coincidem.");
            return;
        }
        if (senhaTexto.length() < 6) {
            mostrarErro("A senha deve ter ao menos 6 caracteres.");
            return;
        }
        if (!emailTexto.contains("@")) {
            mostrarErro("Informe um e-mail válido.");
            return;
        }

        Usuario usuario = new Usuario(nomeTexto, emailTexto, senhaTexto);
        if (dao.cadastrar(usuario)) {
            JOptionPane.showMessageDialog(this, "Conta criada com sucesso.");
        } else {
            mostrarErro("Este e-mail já está cadastrado.");
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção",
                JOptionPane.WARNING_MESSAGE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnEntrar;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables
}
