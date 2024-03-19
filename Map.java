import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//east 50.864239, 5.762940
//north 50.911792, 5.705905
//west 50.847681, 5.638854
//south 50.803814, 5.716786
//east-west diference 0.124086
//north-south difference 0.107978

public class Map extends JFrame {

    private double x1, y1, x2, y2;
    private JPanel drawPanel;
    private Image image;

    public Map() {
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Map");
        this.setLayout(null);

        ImageIcon imageIcon = new ImageIcon("Maastricht.png");
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
        JButton button = new JButton("Submit");
        JTextField textfield1 = new JTextField(5);
        JTextField textfield2 = new JTextField(5);
        JTextField textfield3 = new JTextField(5);
        JTextField textfield4 = new JTextField(5);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    y1 = Double.parseDouble(textfield1.getText());
                    x1 = Double.parseDouble(textfield2.getText());
                    y2 = Double.parseDouble(textfield3.getText());
                    x2 = Double.parseDouble(textfield4.getText());
                    drawPanel.repaint(); 
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Map.this, "Please provide correct numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        controlPanel.add(textfield1); 
        controlPanel.add(textfield2);
        controlPanel.add(textfield3);
        controlPanel.add(textfield4);
        controlPanel.add(button);
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
                g2.drawLine( (int) Math.round(((x1-5.638854)/0.124086)*396),(int) Math.round(((50.911792-y1)/0.107978)*545), (int) Math.round(((x2-5.638854)/0.124086)*396),(int) Math.round(((50.911792-y2)/0.107978)*545));
            }
        };

        return drawPanel;
    }

    public static void main(String[] args) {
        new Map();
    }
}
