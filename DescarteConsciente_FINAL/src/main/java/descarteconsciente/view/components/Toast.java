package descarteconsciente.view.components;

import javax.swing.*;
import java.awt.*;

/**
 * Classe simples para mostrar mensagens do sistema.
 */
public class Toast {

    public static final int SUCESSO = 0;
    public static final int ERRO = 1;
    public static final int AVISO = 2;
    public static final int INFO = 3;

    public static void show(Window parent, String msg, int tipo) {
        int messageType = JOptionPane.INFORMATION_MESSAGE;
        if (tipo == ERRO) {
            messageType = JOptionPane.ERROR_MESSAGE;
        } else if (tipo == AVISO) {
            messageType = JOptionPane.WARNING_MESSAGE;
        }

        JOptionPane.showMessageDialog(parent, msg, "Mensagem", messageType);
    }

    public static void sucesso(Window parent, String msg) {
        show(parent, msg, SUCESSO);
    }

    public static void erro(Window parent, String msg) {
        show(parent, msg, ERRO);
    }

    public static void aviso(Window parent, String msg) {
        show(parent, msg, AVISO);
    }

    public static void info(Window parent, String msg) {
        show(parent, msg, INFO);
    }
}
