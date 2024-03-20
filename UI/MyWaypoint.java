package UIfiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class MyWaypoint extends DefaultWaypoint {
    private JButton button;


    public JButton getButton() {

        return button;
    }

    public void setButton(JButton button) {

        this.button = button;
    }

    public MyWaypoint( EventWaypoint event, GeoPosition coords) {

        super(coords);
        initButton(event);
    }

    private void initButton(EventWaypoint event) {

        button = new ButtonWaypoint();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                event.selected(MyWaypoint.this);
            }
        });
    }
}
