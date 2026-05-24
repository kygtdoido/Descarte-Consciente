package descarteconsciente.view;

import descarteconsciente.model.PontoDeColeta;
import descarteconsciente.util.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel que exibe um mapa estilizado com os pontos de coleta.
 * Não depende de serviços externos – tudo é desenhado via Java2D.
 */
public class MapaPanel extends JPanel {

    private List<PontoDeColeta> pontos = new ArrayList<>();
    private PontoDeColeta       selecionado = null;

    // Área geográfica coberta (lat/lon dos dados de exemplo em SP)
    private static final double LAT_MIN  = -23.560;
    private static final double LAT_MAX  = -23.540;
    private static final double LON_MIN  = -46.645;
    private static final double LON_MAX  = -46.625;

    // Paleta dos marcadores por tipo
    private static final Color COR_FARMACIA   = new Color(46, 125, 50);
    private static final Color COR_DROGARIA   = new Color(21, 101, 192);
    private static final Color COR_POSTO      = new Color(183, 28, 28);
    private static final Color COR_MUNICIPAL  = new Color(130, 119, 23);

    public MapaPanel() {
        setBackground(new Color(232, 242, 220));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PontoDeColeta clicado = pontoNoPonto(e.getX(), e.getY());
                selecionado = clicado;
                repaint();
                if (clicado != null) mostrarPopup(clicado, e.getX(), e.getY());
            }
        });
        setToolTipText("");  // ativa tooltip dinâmico
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        PontoDeColeta p = pontoNoPonto(e.getX(), e.getY());
        return p == null ? null : p.getNome() + " – " + p.getTipoLabel();
    }

    public void setDados(List<PontoDeColeta> pontos) {
        this.pontos = pontos == null ? new ArrayList<>() : pontos;
        selecionado = null;
        repaint();
    }

    // ============================================================
    // Pintura
    // ============================================================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        desenharFundo(g2, w, h);
        desenharRuas(g2, w, h);
        desenharPontos(g2, w, h);
        desenharLegenda(g2, w, h);
        if (selecionado != null) desenharCardSelecionado(g2, w, h);
    }

    private void desenharFundo(Graphics2D g2, int w, int h) {
        // fundo tipo "tile" de mapa
        g2.setColor(new Color(238, 245, 220));
        g2.fillRect(0, 0, w, h);

        // blocos de quarteirões
        g2.setColor(new Color(200, 215, 185));
        int bw = w / 6, bh = h / 5;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                if ((row + col) % 2 == 0)
                    g2.fillRect(col * bw + 10, row * bh + 10, bw - 20, bh - 20);
            }
        }
    }

    private void desenharRuas(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(255, 255, 255, 200));

        // Vias principais (mais largas)
        g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(0, h / 4,     w,     h / 4);
        g2.drawLine(0, h / 2,     w,     h / 2);
        g2.drawLine(0, 3 * h / 4, w, 3 * h / 4);
        g2.drawLine(w / 5, 0,     w / 5, h);
        g2.drawLine(2 * w / 5, 0, 2 * w / 5, h);
        g2.drawLine(3 * w / 5, 0, 3 * w / 5, h);
        g2.drawLine(4 * w / 5, 0, 4 * w / 5, h);

        // Vias secundárias
        g2.setColor(new Color(255, 255, 255, 130));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(0, h / 8,       w, h / 8);
        g2.drawLine(0, 3 * h / 8,   w, 3 * h / 8);
        g2.drawLine(0, 5 * h / 8,   w, 5 * h / 8);
        g2.drawLine(0, 7 * h / 8,   w, 7 * h / 8);
        g2.drawLine(w / 10, 0, w / 10, h);
        g2.drawLine(3 * w / 10, 0, 3 * w / 10, h);

        // Nomes de ruas (decorativo)
        g2.setColor(new Color(100, 110, 80, 160));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2.drawString("Av. Principal", 10, h / 2 - 4);
        g2.drawString("Rua das Flores", 10, h / 4 - 4);
        g2.drawString("Rua Verde", 10, 3 * h / 4 - 4);
    }

    private void desenharPontos(Graphics2D g2, int w, int h) {
        if (pontos == null) return;
        for (PontoDeColeta p : pontos) {
            Point pt = latLonParaPonto(p.getLatitude(), p.getLongitude(), w, h);
            desenharMarcador(g2, pt.x, pt.y, corPorTipo(p.getTipo()),
                    p.getIcone(), p == selecionado);
        }
    }

    private void desenharMarcador(Graphics2D g2, int x, int y,
                                   Color cor, String emoji, boolean selected) {
        int r = selected ? 20 : 14;

        // Sombra
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval(x - r + 2, y - r + 2, r * 2, r * 2);

        // Círculo preenchido
        g2.setColor(selected ? cor.brighter() : cor);
        g2.fillOval(x - r, y - r, r * 2, r * 2);

        // Borda
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(selected ? 3 : 2));
        g2.drawOval(x - r, y - r, r * 2, r * 2);

        // Emoji
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, selected ? 14 : 10));
        FontMetrics fm = g2.getFontMetrics();
        int ex = x - fm.stringWidth(emoji) / 2;
        int ey = y + fm.getAscent() / 2 - 2;
        g2.setColor(Color.WHITE);
        g2.drawString(emoji, ex, ey);

        // Indicador de seleção
        if (selected) {
            g2.setColor(new Color(255, 255, 0, 180));
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    0, new float[]{4, 4}, 0));
            g2.drawOval(x - r - 5, y - r - 5, (r + 5) * 2, (r + 5) * 2);
        }
    }

    private void desenharLegenda(Graphics2D g2, int w, int h) {
        int px = 12, py = h - 110;
        int lh = 22, bw = 180;

        g2.setColor(new Color(255, 255, 255, 210));
        g2.fillRoundRect(px, py, bw, 102, 12, 12);
        g2.setColor(Cores.BORDER);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(px, py, bw, 102, 12, 12);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(Cores.TEXT_SECONDARY);
        g2.drawString("LEGENDA", px + 8, py + 15);

        String[][] itens = {
            {"💊", "Farmácia", String.valueOf(COR_FARMACIA.getRGB())},
            {"💊", "Drogaria", String.valueOf(COR_DROGARIA.getRGB())},
            {"🏥", "Posto de Saúde", String.valueOf(COR_POSTO.getRGB())},
            {"♻",  "Ponto Municipal", String.valueOf(COR_MUNICIPAL.getRGB())}
        };
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        for (int i = 0; i < itens.length; i++) {
            int iy = py + 28 + i * lh;
            // bolinha colorida
            g2.setColor(new Color(Integer.parseInt(itens[i][2])));
            g2.fillOval(px + 8, iy - 8, 12, 12);
            g2.setColor(Cores.TEXT_PRIMARY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.drawString(itens[i][1], px + 26, iy + 1);
        }
    }

    private void desenharCardSelecionado(Graphics2D g2, int w, int h) {
        PontoDeColeta p = selecionado;
        Point pt = latLonParaPonto(p.getLatitude(), p.getLongitude(), w, h);

        int cw = 220, ch = 80;
        int cx = Math.min(pt.x + 20, w - cw - 10);
        int cy = Math.max(pt.y - ch - 10, 10);

        g2.setColor(new Color(255, 255, 255, 235));
        g2.fillRoundRect(cx, cy, cw, ch, 12, 12);
        g2.setColor(Cores.PRIMARY);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(cx, cy, cw, ch, 12, 12);

        g2.setColor(Cores.TEXT_PRIMARY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.drawString(p.getNome(), cx + 10, cy + 20);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(Cores.TEXT_SECONDARY);
        g2.drawString(p.getEnderecoCompleto(), cx + 10, cy + 38);

        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
        g2.drawString("\u23F0 " + p.getHorariosFormatados(), cx + 10, cy + 56);

        // Botão "Como chegar"
        g2.setColor(Cores.PRIMARY);
        g2.fillRoundRect(cx + 10, cy + 62, cw - 20, 14, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        g2.drawString("Como chegar", cx + cw / 2 - 30, cy + 72);
    }

    // ============================================================
    // Helpers
    // ============================================================

    /** Converte lat/lon para pixel no painel. */
    private Point latLonParaPonto(double lat, double lon, int w, int h) {
        double px = (lon - LON_MIN) / (LON_MAX - LON_MIN) * (w - 40) + 20;
        double py = (LAT_MAX - lat) / (LAT_MAX - LAT_MIN) * (h - 40) + 20;
        // fallback: evitar pontos fora da área visível
        if (lat == 0 && lon == 0) { px = w / 2.0; py = h / 2.0; }
        return new Point((int) px, (int) py);
    }

    /** Verifica se o clique foi sobre algum marcador. */
    private PontoDeColeta pontoNoPonto(int mx, int my) {
        if (pontos == null) return null;
        int w = getWidth(), h = getHeight();
        for (PontoDeColeta p : pontos) {
            Point pt = latLonParaPonto(p.getLatitude(), p.getLongitude(), w, h);
            if (Math.hypot(mx - pt.x, my - pt.y) <= 18) return p;
        }
        return null;
    }

    private Color corPorTipo(String tipo) {
        return switch (tipo) {
            case PontoDeColeta.FARMACIA        -> COR_FARMACIA;
            case PontoDeColeta.DROGARIA        -> COR_DROGARIA;
            case PontoDeColeta.POSTO_SAUDE     -> COR_POSTO;
            case PontoDeColeta.PONTO_MUNICIPAL -> COR_MUNICIPAL;
            default                            -> Cores.PRIMARY;
        };
    }

    private void mostrarPopup(PontoDeColeta p, int x, int y) {
        // já exibe o card inline via desenharCardSelecionado
    }
}