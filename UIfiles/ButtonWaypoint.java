package UIfiles;

import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonWaypoint extends JButton {

    public ButtonWaypoint() {

        setContentAreaFilled(false);
        setIcon(new ImageIcon(getClass().getResource("Images\mapPin.png")));
        setSize(new Dimension(25, 25));
    }
}
