package descarteconsciente.util;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;

/**
 * Borda com cantos arredondados para campos e botões.
 */
public class RoundedBorder extends AbstractBorder {

    private final Color color;
    private final int   radius;
    private final int   thickness;

    public RoundedBorder(Color color, int radius, int thickness) {
        this.color     = color;
        this.radius    = radius;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.draw(new RoundRectangle2D.Double(
                x + thickness / 2.0,
                y + thickness / 2.0,
                width  - thickness,
                height - thickness,
                radius, radius));
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int pad = radius / 2 + thickness;
        return new Insets(pad, pad, pad, pad);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        int pad = radius / 2 + thickness;
        insets.set(pad, pad, pad, pad);
        return insets;
    }
}