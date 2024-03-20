package UI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.*;


public class HomePage extends javax.swing.JFrame {

    
    
    /**
     * Creates new form HomePage
     */
    public HomePage() {
        initComponents();
        setLocationRelativeTo(null);
        logoBar.setBackground(new Color(0, 28, 61, 200));
        populars.setBackground(new Color(0, 28, 61, 170));
        
        ImageIcon firstImage = new ImageIcon(getClass().getResource("/img/maastricht-underground.jpg"));
        ImageIcon secondImage = new ImageIcon(getClass().getResource("/img/maastricht-mecc.png"));
        ImageIcon thirdImage = new ImageIcon(getClass().getResource("/img/maastricht-fse.jpeg"));
        
        bodyPopulars.add(new Place("DAILY", "UNDERGROUND", "up to 15 people", firstImage));
        bodyPopulars.add(new Place("WEEKDAYS", "MECC", "8 am until 5 pm", secondImage));
        bodyPopulars.add(new Place("WEEKDAYS", "FSE", "8 am until 8 pm", thirdImage));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        rootPanel = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                try {
                    InputStream inStream = getClass().getResourceAsStream("/img/maastricht-background.jpg");
                    if (inStream != null) {
                    BufferedImage buffIm = ImageIO.read(inStream);
                    if (buffIm != null) {
                        ImageIcon im = new ImageIcon(buffIm);
                        Image i = im.getImage();
                        g.drawImage(i, 0, 0, this.getWidth(), this.getHeight(), this);
                    } else {
                        System.err.println("Failed to read image data.");
                    }
                } else {
                    System.err.println("Image resource not found.");
                }
            } catch (IOException e) {
                System.err.println("Error loading image: " + e.getMessage());
                e.printStackTrace();
            }
        }

        };
        logoBar = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        aboutUS = new javax.swing.JPanel();
        aboutUsText = new javax.swing.JLabel();
        iconsPanel = new javax.swing.JPanel();
        home = new javax.swing.JLabel();
        maap = new javax.swing.JLabel();
        close = new javax.swing.JLabel();
        populars = new javax.swing.JPanel();
        popularsText = new javax.swing.JLabel();
        bodyPopulars = new javax.swing.JPanel();

        jLabel7.setText("jLabel7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        logoBar.setBackground(new java.awt.Color(0, 28, 61));

        logo.setFont(new java.awt.Font("Imprint MT Shadow", 0, 36)); 
        logo.setForeground(new java.awt.Color(255, 255, 255));
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/maapstricht-logo.png"))); 
        logo.setText("MAAPSTRICHT");

        aboutUS.setBackground(new java.awt.Color(0, 28, 61));
        aboutUS.setOpaque(false);

        aboutUsText.setFont(new java.awt.Font("Dubai Medium", 0, 18));
        aboutUsText.setForeground(new java.awt.Color(255, 255, 255));
        aboutUsText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aboutUsText.setText("ABOUT US");
        aboutUsText.addMouseListener(new MouseListener(){
            private boolean pageOpened = false;
            public void mouseReleased(MouseEvent e){
                if (!pageOpened) {
                    pageOpened = true;
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            new InfoPage().setVisible(true);
                        }
                    });
                    setVisible(false);
                }
            }
            public void mousePressed(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseClicked(MouseEvent e){
                if (!pageOpened) {
                    pageOpened = true;
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            new InfoPage().setVisible(true);
                        }
                    });
                    setVisible(false);
                }
            }
        });

        javax.swing.GroupLayout aboutUSLayout = new javax.swing.GroupLayout(aboutUS);
        aboutUS.setLayout(aboutUSLayout);
        aboutUSLayout.setHorizontalGroup(
            aboutUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutUSLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(aboutUsText, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        aboutUSLayout.setVerticalGroup(
            aboutUSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aboutUsText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout logoBarLayout = new javax.swing.GroupLayout(logoBar);
        logoBar.setLayout(logoBarLayout);
        logoBarLayout.setHorizontalGroup(
            logoBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoBarLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aboutUS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        logoBarLayout.setVerticalGroup(
            logoBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aboutUS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(logoBarLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(logo)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        iconsPanel.setBackground(new java.awt.Color(255, 255, 255));
        iconsPanel.setLayout(new java.awt.GridLayout(1, 0));

        home.setBackground(new java.awt.Color(243, 148, 37));
        home.setFont(new java.awt.Font("Dubai Medium", 0, 18)); 
        home.setForeground(new java.awt.Color(255, 255, 255));
        home.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/home-white-icon.png"))); 
        home.setText("   HOME");
        home.setOpaque(true);
        iconsPanel.add(home);

        maap.setFont(new java.awt.Font("Dubai Medium", 0, 18)); 
        maap.setForeground(new java.awt.Color(243, 148, 37));
        maap.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/map-icon.png"))); 
        maap.setText("   MAAP");
        iconsPanel.add(maap);
        maap.addMouseListener(new MouseListener(){
            private boolean pageOpened = false;
            public void mouseReleased(MouseEvent e){
                if (!pageOpened) {
                    pageOpened = true;
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            new MapPage().setVisible(true);
                        }
                    });
                    setVisible(false);
                }
            }
            public void mousePressed(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseClicked(MouseEvent e){
                if (!pageOpened) {
                    pageOpened = true;
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            new MapPage().setVisible(true);
                        }
                    });
                    setVisible(false);
                }
            }
        });

        close.setFont(new java.awt.Font("Dubai Medium", 0, 18)); 
        close.setForeground(new java.awt.Color(243, 148, 37));
        close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/close-icon.png"))); 
        close.setText("  CLOSE");
        iconsPanel.add(close);
        close.addMouseListener(new MouseListener(){
            private boolean pageOpened = false;
            public void mouseReleased(MouseEvent e){
                setVisible(false);
            }
            public void mousePressed(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseClicked(MouseEvent e){
                setVisible(false);
            }
        });

        populars.setBackground(new java.awt.Color(0, 28, 61));

        popularsText.setFont(new java.awt.Font("Dubai", 1, 36)); 
        popularsText.setForeground(new java.awt.Color(255, 255, 255));
        popularsText.setText("Visit these popular places");

        bodyPopulars.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout popularsLayout = new javax.swing.GroupLayout(populars);
        populars.setLayout(popularsLayout);
        popularsLayout.setHorizontalGroup(
            popularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popularsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(popularsText, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(bodyPopulars, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        popularsLayout.setVerticalGroup(
            popularsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popularsLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(popularsText, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bodyPopulars, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logoBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addGap(360, 360, 360)
                        .addComponent(iconsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addGap(332, 332, 332)
                .addComponent(populars, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(335, Short.MAX_VALUE))
        );
        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(populars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(iconsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aboutUS;
    private javax.swing.JLabel aboutUsText;
    private javax.swing.JPanel bodyPopulars;
    private javax.swing.JLabel close;
    private javax.swing.JLabel home;
    private javax.swing.JPanel iconsPanel;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel logo;
    private javax.swing.JPanel logoBar;
    private javax.swing.JLabel maap;
    private javax.swing.JPanel populars;
    private javax.swing.JLabel popularsText;
    private javax.swing.JPanel rootPanel;
    // End of variables declaration//GEN-END:variables
}
