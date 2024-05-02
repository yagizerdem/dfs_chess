import enigma.console.TextAttributes;
import enigma.console.TextWindow;
import enigma.core.Enigma;
import enigma.event.TextMouseListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EnigmaConfig {

    public static enigma.console.Console cn = Enigma.getConsole("chess", 40, 20, 16);
    public static TextWindow cnt = cn.getTextWindow();
    public static TextAttributes RED = new TextAttributes(Color.red, Color.black); // foreground, background color

    public static TextAttributes BLUE = new TextAttributes(Color.blue, Color.black);
    public static TextAttributes GOLD = new TextAttributes(Color.ORANGE, Color.black);
    public static TextAttributes WHITE = new TextAttributes(Color.WHITE, Color.black);
    public TextMouseListener tmlis;
    public static KeyListener klis;

    public EnigmaConfig() {
        cn.getTextWindow();

        // ------ Standard code for mouse and keyboard ------ Do not change
        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (SD.keypr == 0) {
                    SD.keypr = 1;
                    SD.rkey = e.getKeyCode();
                    SD.rkeymod = e.getModifiersEx();
                    if (SD.rkey == KeyEvent.VK_CAPS_LOCK) {
                        if (SD.capslock == 0)
                            SD.capslock = 1;
                        else
                            SD.capslock = 0;
                    }
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);
        // --------------------------------------------------------------------------

        cnt.setCursorType(1); // 1 -> cursor visible
        cn.setTextAttributes(WHITE);
    } // editor config


}