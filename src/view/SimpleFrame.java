package view;

import javax.swing.JFrame;

/**
 * SimpleFrame permet d'avoir les methodes de gestion de la fenetre
 */
public class SimpleFrame extends JFrame {
    public SimpleFrame() {
        this.setLocation(200, 200);
        // pour quitter l'application quand on ferme la fenetre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Makes the frame visible.
     */
    public void showIt() {
        this.pack();
        this.setVisible(true);
    }

    /**
     * Makes the frame visible and sets the title text.
     *
     * @param title : the title of the window
     */
    public void showIt(String title) {
        if (title != null) {
            this.setTitle(title);
            this.pack();
            this.setVisible(true);
        } else {
            System.out.println("showIt: title cannot be null");
        }
    }

    /**
     * Makes the frame visible and sets the title text
     * and the position of the window.
     *
     * @param title : the title of the window
     * @param x     : x position
     * @param y     : y position
     */
    public void showIt(String title, int x, int y) {
        if ((title != null) && (x >= 0) && (y >= 0)) {
            this.setTitle(title);
            this.setLocation(x, y);
            this.pack();
            this.setVisible(true);
        } else {
            System.out.println("showIt : invalid parameters");
        }
    }

    /**
     * Makes the frame invisible.
     */
    public void hideIt() {
        this.setVisible(false);
    }
}
