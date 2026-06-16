package descarteconsciente.view;

import descarteconsciente.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Tela Sobre com informacoes do projeto.
 */
public class SobrePanel extends JPanel {

    public SobrePanel() {
        initComponents();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Cores.BACKGROUND);

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(Cores.BACKGROUND);
        conteudo.setBorder(new EmptyBorder(20, 20, 20, 20));

        conteudo.add(cabecalho());
        conteudo.add(espaco());
        conteudo.add(secao("Objetivo", textoObjetivo()));
        conteudo.add(espaco());
        conteudo.add(secao("ODS relacionadas", textoOds()));
        conteudo.add(espaco());
        conteudo.add(secao("Tecnologias utilizadas", textoTecnologias()));
        conteudo.add(espaco());
        conteudo.add(secao("Equipe", "Pedro, Gabriel, Kevyn, Juan, Nicole e Isac"));

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel cabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Cores.PRIMARY_DARK);
        painel.setBorder(new EmptyBorder(20, 24, 20, 24));
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel titulo = new JLabel("Descarte Consciente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Sistema de Gestao de Pontos de Coleta de Medicamentos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(Color.WHITE);

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        painel.add(textos, BorderLayout.CENTER);

        return painel;
    }

    private JPanel secao(String titulo, String texto) {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Cores.BORDER, 1),
                new EmptyBorder(14, 16, 14, 16)));
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(Cores.PRIMARY_DARK);

        JTextArea area = new JTextArea(texto);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setOpaque(false);

        painel.add(lblTitulo, BorderLayout.NORTH);
        painel.add(area, BorderLayout.CENTER);
        return painel;
    }

    private String textoObjetivo() {
        return "Facilitar o acesso da populacao a pontos de descarte de medicamentos vencidos.\n"
                + "Evitar o descarte incorreto no lixo comum, solo ou agua.\n"
                + "Permitir que administradores cadastrem e atualizem pontos de coleta.";
    }

    private String textoOds() {
        return "ODS 3 - Saude e Bem-Estar.\n"
                + "ODS 6 - Agua potavel e saneamento.\n"
                + "ODS 12 - Consumo e producao responsaveis.";
    }

    private String textoTecnologias() {
        return "Java, Swing, MySQL, JDBC, Maven, NetBeans e padrao DAO.";
    }

    private Component espaco() {
        return Box.createRigidArea(new Dimension(0, 12));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        designTitulo = new JLabel("Descarte Consciente", SwingConstants.CENTER);
        designTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        designTitulo.setForeground(Color.WHITE);
        designTitulo.setOpaque(true);
        designTitulo.setBackground(Cores.PRIMARY_DARK);
        designTexto = new JTextArea("Objetivo\n\nODS relacionadas\n\nTecnologias utilizadas\n\nEquipe");
        designTexto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        designTexto.setEditable(false);
        setLayout(null);
        add(designTitulo);
        designTitulo.setBounds(20, 20, 850, 90);
        add(designTexto);
        designTexto.setBounds(40, 140, 810, 320);
    }

    private JLabel designTitulo;
    private JTextArea designTexto;
}
