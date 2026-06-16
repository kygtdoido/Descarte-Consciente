package descarteconsciente.util;

import java.awt.Color;
import javax.swing.border.LineBorder;

/**
 * Borda simples usada em componentes do sistema.
 */
public class RoundedBorder extends LineBorder {

    public RoundedBorder(Color color, int radius, int thickness) {
        super(color, thickness);
    }
}
