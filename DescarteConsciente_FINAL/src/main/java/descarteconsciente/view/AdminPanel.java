package descarteconsciente.view;

import descarteconsciente.util.Cores;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Painel unificado de administração.
 *
 * Agrupa em abas:
 *   • "Pontos de Coleta"  → AdminPontosPanel   (CRUD 1)
 *   • "Usuários"          → AdminUsuariosPanel  (CRUD 2 + CREATE via Criar Conta no login)
 *
 * Isso satisfaz o requisito de ter ao menos 3 CRUDs visíveis:
 *   CRUD 1 – Pontos de Coleta (admin)
 *   CRUD 2 – Usuários         (admin)
 *   CRUD 3 – Lembretes        (usuário)
 */
public class AdminPanel extends JPanel {

    public AdminPanel() {
        initComponents();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ---- Cabeçalho ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Cores.PRIMARY_DARK);
        header.setPreferredSize(new Dimension(0, 56));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel titulo = new JLabel("⚙  Administração do Sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Gerencie pontos de coleta e usuários");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(Cores.PRIMARY_LIGHT);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(sub);
        header.add(textos, BorderLayout.CENTER);

        // ---- Abas ----
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(Cores.TEXT_PRIMARY);

        tabs.addTab("📍  Pontos de Coleta", new AdminPontosPanel());
        tabs.addTab("👤  Usuários",         new AdminUsuariosPanel());

        // Estilo da aba selecionada
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = Cores.PRIMARY;
                lightHighlight = Cores.PRIMARY_LIGHT;
                shadow = Cores.BORDER;
                darkShadow = Cores.BORDER;
                focus = Cores.PRIMARY;
            }
        });

        add(header, BorderLayout.NORTH);
        add(tabs,   BorderLayout.CENTER);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        designTitulo = new JLabel("Administração do Sistema", SwingConstants.CENTER);
        designAbas = new JTabbedPane();
        designAbas.addTab("Pontos de Coleta", new JPanel());
        designAbas.addTab("Usuários", new JPanel());
        setLayout(null);
        add(designTitulo);
        designTitulo.setBounds(20, 15, 850, 45);
        add(designAbas);
        designAbas.setBounds(20, 75, 850, 400);
    }

    private JLabel designTitulo;
    private JTabbedPane designAbas;
}
