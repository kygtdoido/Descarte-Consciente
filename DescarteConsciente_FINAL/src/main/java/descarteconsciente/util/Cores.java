package descarteconsciente.util;

import java.awt.Color;

/**
 * Paleta de cores do sistema Descarte Consciente.
 * Tema verde – ODS 3: Saúde e Bem-Estar.
 */
public class Cores {

    // Verdes principais
    public static final Color PRIMARY       = new Color(46, 125, 50);   // #2E7D32
    public static final Color PRIMARY_DARK  = new Color(27,  94, 32);   // #1B5E20
    public static final Color PRIMARY_LIGHT = new Color(200, 230, 201); // #C8E6C9
    public static final Color ACCENT        = new Color(56, 142, 60);   // #388E3C

    // Fundos
    public static final Color BACKGROUND    = new Color(245, 250, 245);
    public static final Color CARD_BG       = Color.WHITE;

    // Texto
    public static final Color TEXT_PRIMARY   = new Color(30,  30,  30);
    public static final Color TEXT_SECONDARY = new Color(100, 100, 100);

    // Bordas / divisórias
    public static final Color BORDER        = new Color(220, 220, 220);

    // Status
    public static final Color ERROR         = new Color(198,  40,  40);  // vermelho
    public static final Color WARNING       = new Color(230,  81,   0);  // laranja
    public static final Color SUCCESS       = new Color(46,  125,  50);  // verde

    private Cores() {}
}