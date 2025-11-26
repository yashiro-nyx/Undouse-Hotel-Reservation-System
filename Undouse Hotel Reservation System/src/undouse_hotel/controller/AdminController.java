package undouse_hotel.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import undouse_hotel.view.AdminPanelView;
import undouse_hotel.model.AdminModel;

public class AdminController {
    private JFrame parentFrame;
    private AdminModel adminModel;
    private AdminPanelView adminPanelView;
    
    private static final Color PLUM = new Color(90, 0, 60);
    private static final Color GOLD = new Color(230, 180, 120);
    private static final Color LIGHT_BG = new Color(255, 248, 240);
    private static final Color DARK_PLUM = new Color(70, 0, 50);

    public AdminController(JFrame parentFrame, AdminModel adminModel) {
        this.parentFrame = parentFrame;
        this.adminModel = adminModel;
    }

    public void showAdminPanel() {
        showAdminLogin();
    }

    private void showAdminLogin() {
        JDialog loginDialog = new JDialog(parentFrame, "Admin Authentication - Undouse Hotel", true);
        
        loginDialog.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        loginDialog.setSize(screenSize);
        loginDialog.setLocationRelativeTo(parentFrame);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        JPanel headerPanel = createHeaderPanel();
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(LIGHT_BG);
        
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(50, 60, 50, 60)
        ));
        
        formContainer.setPreferredSize(new Dimension(500, 500)); 
        
        JLabel titleLabel = new JLabel("Administrator Access");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Hotel Management System");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        formContainer.add(Box.createVerticalStrut(20));
        formContainer.add(titleLabel);
        formContainer.add(Box.createVerticalStrut(10));
        formContainer.add(subtitleLabel);
        formContainer.add(Box.createVerticalStrut(50));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        usernameLabel.setForeground(PLUM);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameField.setMaximumSize(new Dimension(350, 50));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        passwordLabel.setForeground(PLUM);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setMaximumSize(new Dimension(350, 50));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(25)); 
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(30)); 

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(350, 60));

        JButton loginButton = createStyledButton("LOGIN", PLUM, DARK_PLUM, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(150, 50));
        loginButton.setMaximumSize(new Dimension(150, 50));
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton cancelButton = createStyledButton("CANCEL", Color.WHITE, LIGHT_BG, Color.GRAY);
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.setMaximumSize(new Dimension(150, 50));
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalGlue());

        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(10));

        JLabel securityLabel = new JLabel("Authorized personnel only • All activities are logged");
        securityLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        securityLabel.setForeground(new Color(150, 150, 150));
        securityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(securityLabel);

        formContainer.add(formPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(formContainer, gbc);
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(LIGHT_BG);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 40, 0));
        
        JButton closeButton = new JButton("Return to User Login");
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        closeButton.setForeground(Color.GRAY);
        closeButton.setBackground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                closeButton.setBackground(new Color(250, 250, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                closeButton.setBackground(Color.WHITE);
            }
        });
        
        footerPanel.add(closeButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        loginDialog.add(mainPanel);
        
        loginButton.addActionListener(e -> handleLogin(loginDialog, usernameField, passwordField));
        cancelButton.addActionListener(e -> loginDialog.dispose());
        closeButton.addActionListener(e -> loginDialog.dispose());

        KeyListener enterKey = new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) loginButton.doClick();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        };
        usernameField.addKeyListener(enterKey);
        passwordField.addKeyListener(enterKey);
        
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());

        loginDialog.setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PLUM);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        headerPanel.setPreferredSize(new Dimension(100, 100));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Undouse Hotel");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Administrator Portal");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(subtitleLabel);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        try {
            ImageIcon logoIcon = new ImageIcon("images/logo.png");
            if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image logoImage = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
                rightPanel.add(logoLabel);
            }
        } catch (Exception e) {
           
        }
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void handleLogin(JDialog loginDialog, JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.", usernameField, passwordField);
            return;
        }

        if (adminModel.authenticate(username, password)) {
            loginDialog.dispose();
            showAdminDashboard();
        } else {
            showError("Invalid username or password. Please try again.", usernameField, passwordField);
        }
    }
    
    private void showError(String message, JTextField usernameField, JPasswordField passwordField) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "Authentication Failed", 
            JOptionPane.ERROR_MESSAGE);
        
        passwordField.setText("");
        usernameField.requestFocus();
        showErrorAnimation(usernameField, passwordField);
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void showErrorAnimation(JTextField usernameField, JPasswordField passwordField) {
        Color errorColor = new Color(255, 100, 100);
        Color originalColor = new Color(200, 200, 200);
        
        Timer timer = new Timer(100, null);
        timer.setRepeats(true);
        final int[] count = {0};
        
        timer.addActionListener(e -> {
            if (count[0] < 4) {
                Color borderColor = (count[0] % 2 == 0) ? errorColor : originalColor;
                
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                count[0]++;
            } else {
                timer.stop();
               
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(originalColor, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(originalColor, 2),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        timer.start();
    }

    private void showAdminDashboard() {
        adminPanelView = new AdminPanelView(parentFrame, adminModel);
        adminPanelView.showPanel();
    }
}