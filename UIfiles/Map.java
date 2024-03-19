package UIfiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ZipCode.*;

//east 50.864239, 5.762940
//north 50.911792, 5.705905
//west 50.847681, 5.638854
//south 50.803814, 5.716786
//east-west diference 0.124086
//north-south difference 0.107978

public class Map extends JFrame {

    private String start, end;
    public static double zipStartX, zipStartY, zipEndX, zipEndY;
    private JPanel drawPanel;
    private Image image;

    public Map() {
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("UIfiles.Map");
        this.setLayout(null);

        ImageIcon imageIcon = new ImageIcon("UIfiles/Maastricht.png");
        image = imageIcon.getImage();

        JPanel controlPanel = createControlPanel();
        controlPanel.setBounds(0, 0, 1000, 100);
        this.add(controlPanel);

        drawPanel = createDrawPanel();
        drawPanel.setBounds(300, 150, 396, 545);
        this.add(drawPanel);

        this.setVisible(true);
    }


    private JPanel createControlPanel() {

        JPanel controlPanel = new JPanel(); 
        JButton submitButton = new JButton("Submit");

        JTextField zipStartField = new JTextField(5);
        JTextField zipEndField = new JTextField(5);


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    start = zipStartField.getText().toUpperCase();
                    end = zipEndField.getText().toUpperCase();
                    Coordinates.extractCoordinates(start, end);

                    drawPanel.repaint();
            }
        });

        controlPanel.add(zipStartField);
        controlPanel.add(zipEndField);

        controlPanel.add(submitButton);
        return controlPanel;
    }

    private JPanel createDrawPanel() {
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                if (image != null) {
                    g2.drawImage(image, 0, 0,396,545, this);
                }
                g2.setStroke(new BasicStroke(3));
                g2.setColor(Color.RED);
                g2.drawLine(Coordinates.startLineX(), Coordinates.startLineY(), Coordinates.endLineX(), Coordinates.endtLineY());
            }
        };

        return drawPanel;
    }

    public static void main(String[] args) {
        new Map();
    }
}
