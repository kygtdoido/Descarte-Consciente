package descarteconsciente.view;

import javax.swing.*;
import java.awt.*;

public class SobrePanel extends JPanel {

    public SobrePanel() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Tela Sobre", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}