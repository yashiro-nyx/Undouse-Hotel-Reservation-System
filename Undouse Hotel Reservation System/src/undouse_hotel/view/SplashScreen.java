package undouse_hotel.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SplashScreen extends JWindow {
    private static final long serialVersionUID = 1L;
    private static final Color PLUM = new Color(90, 0, 60);
    private static final Color GOLD = new Color(230, 180, 120);
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public SplashScreen() {
        createSplashScreen();
    }
    
    private void createSplashScreen() {
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, PLUM,
                    0, getHeight(), new Color(60, 0, 40)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                g2.dispose();
            }
        };
        
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        content.setPreferredSize(new Dimension(600, 450));
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("images/logo.jpg");
            Image scaledImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("🏨");
            logoLabel.setFont(new Font("Serif", Font.PLAIN, 80));
            logoLabel.setForeground(GOLD);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);        

        JLabel hotelNameLabel = new JLabel("Undouse Hotel");
        hotelNameLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 48));
        hotelNameLabel.setForeground(Color.WHITE);
        hotelNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel taglineLabel = new JLabel("Resort Reservation System");
        taglineLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        taglineLabel.setForeground(GOLD);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topPanel.add(logoLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(hotelNameLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(taglineLabel);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));  
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(GOLD);
        progressBar.setBackground(new Color(255, 255, 255, 50));
        progressBar.setPreferredSize(new Dimension(500, 30)); 
        progressBar.setMaximumSize(new Dimension(500, 30)); 
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setBorderPainted(false);
        progressBar.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalStrut(15)); 
        centerPanel.add(statusLabel);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        
        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(255, 255, 255, 150));
        
        JLabel copyrightLabel = new JLabel("© 2025 Undouse Hotel. All rights reserved.");
        copyrightLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        copyrightLabel.setForeground(new Color(255, 255, 255, 120));
        
        bottomPanel.add(versionLabel);
        bottomPanel.add(new JLabel("  |  "));
        bottomPanel.add(copyrightLabel);
        
        content.add(topPanel, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(content);
        pack();
        setLocationRelativeTo(null);
        
        try {
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        } catch (Exception e) {
        }
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    public void showSplashAndLoad(Runnable onComplete) {
        setVisible(true);
        
        new Thread(() -> {
            try {
                updateProgress(0, "Initializing system...");
                Thread.sleep(500);
                
                updateProgress(20, "Loading models...");
                Thread.sleep(400);
                
                updateProgress(40, "Setting up controllers...");
                Thread.sleep(400);
                
                updateProgress(60, "Preparing user interface...");
                Thread.sleep(400);
                
                updateProgress(80, "Loading resources...");
                Thread.sleep(400);
                
                updateProgress(100, "Starting application...");
                Thread.sleep(300);
                
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    dispose();
                    
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateProgress(int progress, String status) {
        SwingUtilities.invokeLater(() -> {
            setProgress(progress);
            setStatus(status);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplashAndLoad(() -> {
                System.out.println("Application loaded!");
            });
        });
    }
}