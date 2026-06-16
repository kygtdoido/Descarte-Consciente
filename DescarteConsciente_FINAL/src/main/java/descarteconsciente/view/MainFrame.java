package descarteconsciente.view;

import descarteconsciente.SessaoAtual;
import descarteconsciente.util.Cores;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Janela principal criada como JFrame Form do NetBeans.
 */
public class MainFrame extends javax.swing.JFrame {

    private static final String TAB_DASHBOARD = "dashboard";
    private static final String TAB_LOCAIS = "locais";
    private static final String TAB_LEMBRETES = "lembretes";
    private static final String TAB_SOBRE = "sobre";
    private static final String TAB_ADMIN = "admin";

    private CardLayout contentLayout;
    private LembretesPanel lembretesPanel;

    public MainFrame() {
        initComponents();
        configurarTela();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }

    private void configurarTela() {
        String nome = "Olá!";
        if (SessaoAtual.getUsuario() != null) {
            nome = "Olá, " + primeiroNome(SessaoAtual.getUsuario().getNome()) + "!";
        }
        lblBoasVindas.setText(nome);

        contentLayout = new CardLayout();
        pnlConteudo.setLayout(contentLayout);

        lembretesPanel = new LembretesPanel();
        pnlConteudo.add(new DashboardPanel(), TAB_DASHBOARD);
        pnlConteudo.add(new ListaPontosPanel(), TAB_LOCAIS);
        pnlConteudo.add(lembretesPanel, TAB_LEMBRETES);
        pnlConteudo.add(new SobrePanel(), TAB_SOBRE);

        boolean administrador = SessaoAtual.getUsuario() != null
                && SessaoAtual.getUsuario().isAdmin();
        if (administrador) {
            pnlConteudo.add(new AdminPanel(), TAB_ADMIN);
        }
        configurarBotoes(administrador);
        mudarTela(TAB_DASHBOARD, btnInicio);
    }

    private void configurarBotoes(boolean administrador) {
        JButton[] botoes = {btnInicio, btnLocais, btnLembretes, btnSobre, btnAdmin};
        int quantidade = administrador ? 5 : 4;
        int largura = 1000 / quantidade;

        for (int i = 0; i < quantidade; i++) {
            botoes[i].setBounds(i * largura, 0, largura, 58);
        }
        btnAdmin.setVisible(administrador);
    }

    private void mudarTela(String nomeTela, JButton botaoAtivo) {
        contentLayout.show(pnlConteudo, nomeTela);
        JButton[] botoes = {btnInicio, btnLocais, btnLembretes, btnSobre, btnAdmin};
        for (JButton botao : botoes) {
            botao.setForeground(Cores.TEXT_SECONDARY);
            botao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
        botaoAtivo.setForeground(Cores.PRIMARY);
        botaoAtivo.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private String primeiroNome(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.trim().isEmpty()) {
            return "";
        }
        String[] partes = nomeCompleto.trim().split("\\s+");
        return partes[0];
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCabecalho = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblBoasVindas = new javax.swing.JLabel();
        btnSair = new javax.swing.JButton();
        pnlConteudo = new javax.swing.JPanel();
        pnlNavegacao = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnLocais = new javax.swing.JButton();
        btnLembretes = new javax.swing.JButton();
        btnSobre = new javax.swing.JButton();
        btnAdmin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Descarte Consciente");
        setResizable(false);
        getContentPane().setLayout(null);

        pnlCabecalho.setBackground(Cores.PRIMARY_DARK);
        pnlCabecalho.setLayout(null);

        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setText("Descarte Consciente");
        pnlCabecalho.add(lblTitulo);
        lblTitulo.setBounds(25, 15, 260, 35);

        lblBoasVindas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBoasVindas.setForeground(Color.WHITE);
        lblBoasVindas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBoasVindas.setText("Olá, Administrador!");
        pnlCabecalho.add(lblBoasVindas);
        lblBoasVindas.setBounds(350, 15, 300, 35);

        btnSair.setBackground(Cores.PRIMARY);
        btnSair.setForeground(Color.WHITE);
        btnSair.setText("Sair");
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
        pnlCabecalho.add(btnSair);
        btnSair.setBounds(895, 15, 80, 34);

        getContentPane().add(pnlCabecalho);
        pnlCabecalho.setBounds(0, 0, 1000, 64);

        pnlConteudo.setBackground(new Color(245, 247, 245));
        getContentPane().add(pnlConteudo);
        pnlConteudo.setBounds(0, 64, 1000, 528);

        pnlNavegacao.setBackground(Color.WHITE);
        pnlNavegacao.setLayout(null);

        prepararBotao(btnInicio, "Início");
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });
        pnlNavegacao.add(btnInicio);
        btnInicio.setBounds(0, 0, 200, 58);

        prepararBotao(btnLocais, "Locais");
        btnLocais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocaisActionPerformed(evt);
            }
        });
        pnlNavegacao.add(btnLocais);
        btnLocais.setBounds(200, 0, 200, 58);

        prepararBotao(btnLembretes, "Lembretes");
        btnLembretes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLembretesActionPerformed(evt);
            }
        });
        pnlNavegacao.add(btnLembretes);
        btnLembretes.setBounds(400, 0, 200, 58);

        prepararBotao(btnSobre, "Sobre");
        btnSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSobreActionPerformed(evt);
            }
        });
        pnlNavegacao.add(btnSobre);
        btnSobre.setBounds(600, 0, 200, 58);

        prepararBotao(btnAdmin, "Admin");
        btnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminActionPerformed(evt);
            }
        });
        pnlNavegacao.add(btnAdmin);
        btnAdmin.setBounds(800, 0, 200, 58);

        getContentPane().add(pnlNavegacao);
        pnlNavegacao.setBounds(0, 592, 1000, 58);

        setSize(new java.awt.Dimension(1016, 689));
    }// </editor-fold>//GEN-END:initComponents

    private void prepararBotao(JButton botao, String texto) {
        botao.setText(texto);
        botao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        botao.setForeground(Cores.TEXT_SECONDARY);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFocusPainted(false);
    }

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        mudarTela(TAB_DASHBOARD, btnInicio);
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnLocaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocaisActionPerformed
        mudarTela(TAB_LOCAIS, btnLocais);
    }//GEN-LAST:event_btnLocaisActionPerformed

    private void btnLembretesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLembretesActionPerformed
        lembretesPanel.recarregar();
        mudarTela(TAB_LEMBRETES, btnLembretes);
    }//GEN-LAST:event_btnLembretesActionPerformed

    private void btnSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSobreActionPerformed
        mudarTela(TAB_SOBRE, btnSobre);
    }//GEN-LAST:event_btnSobreActionPerformed

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed
        mudarTela(TAB_ADMIN, btnAdmin);
    }//GEN-LAST:event_btnAdminActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja sair da sua conta?", "Confirmar saída",
                JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            SessaoAtual.encerrar();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }//GEN-LAST:event_btnSairActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnLembretes;
    private javax.swing.JButton btnLocais;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnSobre;
    private javax.swing.JLabel lblBoasVindas;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlCabecalho;
    private javax.swing.JPanel pnlConteudo;
    private javax.swing.JPanel pnlNavegacao;
    // End of variables declaration//GEN-END:variables
}
