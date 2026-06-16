package descarteconsciente.view;

import descarteconsciente.model.PontoDeColeta;
import descarteconsciente.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapa simples dos pontos de coleta.
 * Usa somente Swing e as coordenadas cadastradas no banco.
 */
public class MapaPanel extends JPanel {

    private List<PontoDeColeta> pontos = new ArrayList<PontoDeColeta>();
    private PainelDesenho painelDesenho;

    public MapaPanel() {
        initComponents();
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Cores.BACKGROUND);

        JLabel titulo = new JLabel("Mapa dos pontos de coleta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(12, 16, 8, 16));
        add(titulo, BorderLayout.NORTH);

        painelDesenho = new PainelDesenho();
        painelDesenho.setBorder(new EmptyBorder(10, 16, 16, 16));
        add(painelDesenho, BorderLayout.CENTER);
    }

    public void setDados(List<PontoDeColeta> novosPontos) {
        pontos.clear();
        if (novosPontos != null) {
            pontos.addAll(novosPontos);
        }
        painelDesenho.repaint();
    }

    private class PainelDesenho extends JPanel {

        public PainelDesenho() {
            setBackground(new Color(238, 243, 235));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int largura = getWidth();
            int altura = getHeight();
            desenharRuas(g, largura, altura);

            if (pontos.isEmpty()) {
                g.setColor(Cores.TEXT_SECONDARY);
                g.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                g.drawString("Nenhum ponto encontrado.", 25, 35);
                return;
            }

            double menorLatitude = pontos.get(0).getLatitude();
            double maiorLatitude = menorLatitude;
            double menorLongitude = pontos.get(0).getLongitude();
            double maiorLongitude = menorLongitude;

            for (PontoDeColeta ponto : pontos) {
                if (ponto.getLatitude() < menorLatitude) {
                    menorLatitude = ponto.getLatitude();
                }
                if (ponto.getLatitude() > maiorLatitude) {
                    maiorLatitude = ponto.getLatitude();
                }
                if (ponto.getLongitude() < menorLongitude) {
                    menorLongitude = ponto.getLongitude();
                }
                if (ponto.getLongitude() > maiorLongitude) {
                    maiorLongitude = ponto.getLongitude();
                }
            }

            double diferencaLatitude = maiorLatitude - menorLatitude;
            double diferencaLongitude = maiorLongitude - menorLongitude;
            if (diferencaLatitude == 0) {
                diferencaLatitude = 1;
            }
            if (diferencaLongitude == 0) {
                diferencaLongitude = 1;
            }

            int margem = 70;
            for (PontoDeColeta ponto : pontos) {
                int x = margem + (int) ((ponto.getLongitude() - menorLongitude)
                        / diferencaLongitude * (largura - margem * 2));
                int y = margem + (int) ((maiorLatitude - ponto.getLatitude())
                        / diferencaLatitude * (altura - margem * 2));
                desenharPonto(g, ponto, x, y);
            }
        }

        private void desenharRuas(Graphics g, int largura, int altura) {
            g.setColor(Color.WHITE);
            for (int y = 45; y < altura; y += 70) {
                g.fillRect(0, y, largura, 18);
            }
            for (int x = 55; x < largura; x += 110) {
                g.fillRect(x, 0, 20, altura);
            }

            g.setColor(new Color(214, 224, 210));
            g.drawLine(0, altura, largura, 0);
            g.drawLine(0, altura / 2, largura, altura / 3);
        }

        private void desenharPonto(Graphics g, PontoDeColeta ponto, int x, int y) {
            g.setColor(Cores.PRIMARY_DARK);
            g.fillOval(x - 12, y - 12, 24, 24);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics medidas = g.getFontMetrics();
            String letra = ponto.getIcone();
            g.drawString(letra, x - medidas.stringWidth(letra) / 2, y + 5);

            String nome = ponto.getNome();
            g.setFont(new Font("Segoe UI", Font.BOLD, 12));
            medidas = g.getFontMetrics();
            int textoX = x - medidas.stringWidth(nome) / 2;
            if (textoX < 5) {
                textoX = 5;
            }
            if (textoX + medidas.stringWidth(nome) > getWidth() - 5) {
                textoX = getWidth() - medidas.stringWidth(nome) - 5;
            }

            g.setColor(new Color(255, 255, 255, 220));
            g.fillRect(textoX - 4, y + 15, medidas.stringWidth(nome) + 8, 20);
            g.setColor(Cores.TEXT_PRIMARY);
            g.drawString(nome, textoX, y + 30);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        designTitulo = new JLabel("Mapa dos pontos de coleta", SwingConstants.CENTER);
        designMapa = new JPanel();
        designMapa.setBackground(new Color(238, 243, 235));
        designPontos = new JLabel("Drogaria Saúde       Farmácia Bem+       Posto Municipal",
                SwingConstants.CENTER);
        designMapa.setLayout(new BorderLayout());
        designMapa.add(designPontos, BorderLayout.CENTER);
        setLayout(null);
        add(designTitulo);
        designTitulo.setBounds(20, 15, 850, 40);
        add(designMapa);
        designMapa.setBounds(20, 70, 850, 400);
    }

    private JLabel designTitulo;
    private JPanel designMapa;
    private JLabel designPontos;
}
