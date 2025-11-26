package undouse_hotel.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

import undouse_hotel.controller.AdminController;
import undouse_hotel.model.AdminModel;
import undouse_hotel.model.UserModel;

public class LoginView extends JDialog {
    private static final Color PLUM = new Color(90, 0, 60);
    private static final Color GOLD = new Color(230, 180, 120);
    private static final Color LIGHT_BG = new Color(255, 248, 240);
    private static final Color DARK_GRAY = new Color(60, 60, 60);
    private static final Color LIGHT_GRAY = new Color(240, 240, 240);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color ERROR_RED = new Color(231, 76, 60);
    private static final Color WARNING_ORANGE = new Color(230, 126, 34);
    
    private JTextField emailField;
    private JPasswordField passwordField;
    private JFrame parentFrame;
    private UserModel userModel;
    private boolean loginSuccessful = false;
    private JCheckBox rememberMeCheckbox;
    private Preferences prefs;
    private boolean mandatoryLogin;

    public LoginView(JFrame parent) {
        this(parent, false);
    }
    
    public LoginView(JFrame parent, boolean mandatoryLogin) {
        super(parent, "Login - Undouse Hotel", true);
        this.parentFrame = parent;
        this.userModel = UserModel.getInstance();
        this.prefs = Preferences.userNodeForPackage(LoginView.class);
        this.mandatoryLogin = mandatoryLogin;
        
        setUndecorated(true);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setLocationRelativeTo(parent);
        if (mandatoryLogin) {
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int choice = JOptionPane.showConfirmDialog(
                        LoginView.this,
                        "You must login to continue.\nDo you want to exit the application?",
                        "Login Required",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    if (choice == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            });
        } else {
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        }
        
        initializeComponents();
        setupAdminHotkey();
        loadRememberedCredentials();
    }
    
    private void initializeComponents() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(250, 250, 252));
        
        JPanel brandingPanel = createBrandingPanel();
        
        JPanel loginPanel = createLoginPanel();
        
        mainContainer.add(brandingPanel, BorderLayout.WEST);
        mainContainer.add(loginPanel, BorderLayout.CENTER);
        
        add(mainContainer);
    }
    
    private JPanel createBrandingPanel() {
        JPanel brandingPanel = new JPanel(new BorderLayout());
        brandingPanel.setBackground(PLUM);
        brandingPanel.setPreferredSize(new Dimension(500, getHeight()));
        brandingPanel.setBorder(BorderFactory.createEmptyBorder(80, 60, 80, 60));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PLUM);
        
        ImageIcon originalLogo = new ImageIcon("images/logo.png");
        Image scaledLogo = originalLogo.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        JLabel hotelName = new JLabel("UNDOUSE HOTEL");
        hotelName.setFont(new Font("Segoe UI", Font.BOLD, 36));
        hotelName.setForeground(Color.WHITE);
        hotelName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel tagline = new JLabel("Luxury Redefined");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(GOLD);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setBorder(BorderFactory.createEmptyBorder(10, 0, 60, 0));
        
        String[] features = {
            "• Premium Accommodations",
            "• World-Class Service",
            "• Unforgettable Experiences",
            "• 24/7 Customer Support"
        };
        
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setBackground(PLUM);
        featuresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            featureLabel.setForeground(Color.WHITE);
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            featureLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            featuresPanel.add(featureLabel);
        }
        
        contentPanel.add(logoLabel);
        contentPanel.add(hotelName);
        contentPanel.add(tagline);
        contentPanel.add(featuresPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        if (mandatoryLogin) {
            JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            exitPanel.setBackground(PLUM);
            exitPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            
            JButton exitButton = new JButton("Exit Application");
            exitButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            exitButton.setForeground(Color.WHITE);
            exitButton.setBackground(new Color(120, 0, 80));
            exitButton.setFocusPainted(false);
            exitButton.setBorder(BorderFactory.createLineBorder(new Color(150, 0, 100), 1));
            exitButton.setPreferredSize(new Dimension(140, 40));
            exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            exitButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    exitButton.setBackground(new Color(140, 0, 100));
                }
                public void mouseExited(MouseEvent e) {
                    exitButton.setBackground(new Color(120, 0, 80));
                }
            });
            
            exitButton.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            });
            
            exitPanel.add(exitButton);
            brandingPanel.add(exitPanel, BorderLayout.SOUTH);
        }
        
        brandingPanel.add(contentPanel, BorderLayout.CENTER);
        
        return brandingPanel;
    }
    
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 40, 8, 40);
        
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(Color.WHITE);
        formContainer.setPreferredSize(new Dimension(450, 600));
        
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(DARK_GRAY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel instructionLabel = new JLabel(mandatoryLogin ? "Please login to continue" : "Sign in to your account");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        instructionLabel.setForeground(new Color(120, 120, 120));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JPanel emailPanel = createInputPanel("EMAIL ADDRESS", emailField = new JTextField(26));
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setPreferredSize(new Dimension(400, 55));
        emailField.setMinimumSize(new Dimension(400, 55));
        emailField.setMaximumSize(new Dimension(400, 55));
        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel passwordPanel = createInputPanel("PASSWORD", passwordField = new JPasswordField(26));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(400, 55));
        passwordField.setMinimumSize(new Dimension(400, 55));
        passwordField.setMaximumSize(new Dimension(400, 55));
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setMaximumSize(new Dimension(400, 50));
        
        rememberMeCheckbox = new JCheckBox("Remember me");
        rememberMeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rememberMeCheckbox.setForeground(DARK_GRAY);
        rememberMeCheckbox.setBackground(Color.WHITE);
        
        JLabel forgotPassword = new JLabel("Forgot Password?");
        forgotPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        forgotPassword.setForeground(PLUM);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showForgotPasswordDialog();
            }
            public void mouseEntered(MouseEvent e) {
                forgotPassword.setForeground(new Color(70, 0, 50));
            }
            public void mouseExited(MouseEvent e) {
                forgotPassword.setForeground(PLUM);
            }
        });
        
        optionsPanel.add(rememberMeCheckbox, BorderLayout.WEST);
        optionsPanel.add(forgotPassword, BorderLayout.EAST);
        
        JButton loginButton = new JButton("SIGN IN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(PLUM);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(400, 55));
        loginButton.setMinimumSize(new Dimension(400, 55));
        loginButton.setMaximumSize(new Dimension(400, 55));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(70, 0, 50));
                loginButton.setBorder(BorderFactory.createLineBorder(new Color(70, 0, 50), 2));
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(PLUM);
                loginButton.setBorder(BorderFactory.createLineBorder(PLUM, 2));
            }
        });
        
        loginButton.addActionListener(e -> handleLogin());
        
        KeyAdapter enterKeyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        emailField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
        signupPanel.setBackground(Color.WHITE);
        signupPanel.setMaximumSize(new Dimension(400, 50));
        
        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noAccountLabel.setForeground(new Color(120, 120, 120));
        
        JLabel signupLink = new JLabel("Create account");
        signupLink.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signupLink.setForeground(PLUM);
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openSignupDialog();
            }
            public void mouseEntered(MouseEvent e) {
                signupLink.setForeground(new Color(70, 0, 50));
            }
            public void mouseExited(MouseEvent e) {
                signupLink.setForeground(PLUM);
            }
        });
        
        signupPanel.add(noAccountLabel);
        signupPanel.add(signupLink);
        
        formContainer.add(welcomeLabel);
        formContainer.add(instructionLabel);
        formContainer.add(Box.createVerticalStrut(20));
        formContainer.add(emailPanel);
        formContainer.add(Box.createVerticalStrut(25));
        formContainer.add(passwordPanel);
        formContainer.add(Box.createVerticalStrut(20));
        formContainer.add(optionsPanel);
        formContainer.add(Box.createVerticalStrut(30));
        formContainer.add(loginButton);
        formContainer.add(Box.createVerticalStrut(30));
        formContainer.add(signupPanel);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        loginPanel.add(formContainer, gbc);
        
        return loginPanel;
    }
    
    private JPanel createInputPanel(String labelText, JComponent inputField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(400, 80));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(DARK_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        if (inputField instanceof JPasswordField) {
            JPasswordField passField = (JPasswordField) inputField;
            
            JPanel passwordWrapper = new JPanel(new BorderLayout(0, 0));
            passwordWrapper.setBackground(Color.WHITE);
            passwordWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            passwordWrapper.setPreferredSize(new Dimension(400, 55));
            passwordWrapper.setMinimumSize(new Dimension(400, 55));
            passwordWrapper.setMaximumSize(new Dimension(400, 55));
            
            passField.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 5));
            
            passwordWrapper.add(passField, BorderLayout.CENTER);
            passwordWrapper.add(createPasswordToggleButton(passField), BorderLayout.EAST);
            
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setBackground(Color.WHITE);
            wrapperPanel.add(label, BorderLayout.NORTH);
            wrapperPanel.add(passwordWrapper, BorderLayout.CENTER);
            
            panel.add(wrapperPanel, BorderLayout.CENTER);
        } else {
            inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
            
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setBackground(Color.WHITE);
            wrapperPanel.add(label, BorderLayout.NORTH);
            wrapperPanel.add(inputField, BorderLayout.CENTER);
            
            panel.add(wrapperPanel, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JButton createPasswordToggleButton(JPasswordField passwordField) {
        JButton toggleButton = new JButton("👁");
        toggleButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        toggleButton.setForeground(new Color(120, 120, 120));
        toggleButton.setBackground(Color.WHITE);
        toggleButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.setToolTipText("Show password");
        toggleButton.setPreferredSize(new Dimension(40, 55));
        
        toggleButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == (char) 0) {
                passwordField.setEchoChar('•');
                toggleButton.setText("👁");
                toggleButton.setToolTipText("Show password");
            } else {
                passwordField.setEchoChar((char) 0);
                toggleButton.setText("🙈");
                toggleButton.setToolTipText("Hide password");
            }
        });
        
        toggleButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                toggleButton.setForeground(PLUM);
            }
            public void mouseExited(MouseEvent e) {
                toggleButton.setForeground(new Color(120, 120, 120));
            }
        });
        
        return toggleButton;
    }
    
    private void loadRememberedCredentials() {
        String rememberedEmail = prefs.get("remembered_email", "");
        boolean rememberMe = prefs.getBoolean("remember_me", false);
        
        if (rememberMe && !rememberedEmail.isEmpty()) {
            emailField.setText(rememberedEmail);
            rememberMeCheckbox.setSelected(true);
            passwordField.requestFocus();
        }
    }
    
    private void saveRememberedCredentials() {
        if (rememberMeCheckbox.isSelected()) {
            prefs.put("remembered_email", emailField.getText().trim());
            prefs.putBoolean("remember_me", true);
        } else {
            prefs.remove("remembered_email");
            prefs.putBoolean("remember_me", false);
        }
    }
    
    private void showAdminLogin() {
        
        JDialog currentLoginDialog = this;
        
       
        currentLoginDialog.setVisible(false);
        
        
        if (parentFrame != null) {
            parentFrame.setVisible(false);
        }
        
        AdminModel adminModel = new AdminModel();
        AdminController adminController = new AdminController(parentFrame, adminModel);
        
        JDialog adminWrapper = new JDialog(currentLoginDialog, "", true);
        adminWrapper.setUndecorated(true);
        adminWrapper.setSize(1, 1);
        adminWrapper.setLocationRelativeTo(null);
        
        SwingUtilities.invokeLater(() -> {
            adminController.showAdminPanel();
            
            adminWrapper.dispose();
            
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            
            if (mandatoryLogin) {
                currentLoginDialog.setVisible(true);
            } else {
                currentLoginDialog.setVisible(true);
            }
        });
        
        adminWrapper.setVisible(true);
    }
    
    private void showForgotPasswordDialog() {
        JDialog forgotDialog = new JDialog(this, "Reset Password", true);
        forgotDialog.setSize(550, 400);
        forgotDialog.setLocationRelativeTo(this);
        forgotDialog.setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("Reset Your Password");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("Enter your email address to receive a password reset code");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel emailFieldContainer = new JPanel();
        emailFieldContainer.setLayout(new BoxLayout(emailFieldContainer, BoxLayout.Y_AXIS));
        emailFieldContainer.setBackground(Color.WHITE);
        emailFieldContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailFieldContainer.setMaximumSize(new Dimension(470, 80));
        
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(PLUM);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField resetEmailField = new JTextField();
        resetEmailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        resetEmailField.setPreferredSize(new Dimension(470, 50));
        resetEmailField.setMaximumSize(new Dimension(470, 50));
        resetEmailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetEmailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        if (!emailField.getText().trim().isEmpty()) {
            resetEmailField.setText(emailField.getText().trim());
        }
        
        emailFieldContainer.add(emailLabel);
        emailFieldContainer.add(Box.createVerticalStrut(8));
        emailFieldContainer.add(resetEmailField);
        
        JButton sendCodeButton = new JButton("Send Reset Code");
        sendCodeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendCodeButton.setForeground(Color.WHITE);
        sendCodeButton.setBackground(PLUM);
        sendCodeButton.setFocusPainted(false);
        sendCodeButton.setBorderPainted(false);
        sendCodeButton.setPreferredSize(new Dimension(470, 55));
        sendCodeButton.setMaximumSize(new Dimension(470, 55));
        sendCodeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendCodeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        sendCodeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                sendCodeButton.setBackground(new Color(70, 0, 50));
            }
            public void mouseExited(MouseEvent e) {
                sendCodeButton.setBackground(PLUM);
            }
        });
        
        sendCodeButton.addActionListener(e -> {
            String email = resetEmailField.getText().trim();
            if (email.isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(forgotDialog,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            sendCodeButton.setEnabled(false);
            sendCodeButton.setText("Sending...");
            
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                private String token;
                
                @Override
                protected Boolean doInBackground() throws Exception {
                    token = userModel.generatePasswordResetToken(email);
                    if (token != null) {
                        return undouse_hotel.service.EmailService.sendPasswordResetEmail(
                            email,
                            userModel.getUserByEmail(email).getFirstName(),
                            token
                        );
                    }
                    return false;
                }
                
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        sendCodeButton.setEnabled(true);
                        sendCodeButton.setText("Send Reset Code");
                        
                        if (success) {
                            JOptionPane.showMessageDialog(forgotDialog,
                                "A password reset code has been sent to your email.\nPlease check your inbox and spam folder.",
                                "Email Sent",
                                JOptionPane.INFORMATION_MESSAGE);
                            showResetCodeDialog(forgotDialog, email, token);
                        } else {
                            JOptionPane.showMessageDialog(forgotDialog,
                                "No account found with this email address.",
                                "Email Not Found",
                                JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        sendCodeButton.setEnabled(true);
                        sendCodeButton.setText("Send Reset Code");
                        JOptionPane.showMessageDialog(forgotDialog,
                            "Failed to send reset code. Please check your internet connection and try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();
        });
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(instructionLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(emailFieldContainer);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(sendCodeButton);
        
        forgotDialog.add(mainPanel);
        forgotDialog.setVisible(true);
    }
    
    private void showResetCodeDialog(JDialog parentDialog, String email, String token) {
        parentDialog.dispose();
        
        JDialog codeDialog = new JDialog(this, "Enter Reset Code", true);
        codeDialog.setSize(600, 600);
        codeDialog.setLocationRelativeTo(this);
        codeDialog.setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("Enter Reset Code");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel instructionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        instructionPanel.setBackground(Color.WHITE);
        instructionPanel.setMaximumSize(new Dimension(520, 60));
        
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>A reset code has been sent to your email.<br>Please check your inbox and spam folder, then enter the code below.</div></html>");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        instructionPanel.add(instructionLabel);
        
        JPanel codeFieldContainer = new JPanel();
        codeFieldContainer.setLayout(new BoxLayout(codeFieldContainer, BoxLayout.Y_AXIS));
        codeFieldContainer.setBackground(Color.WHITE);
        codeFieldContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        codeFieldContainer.setMaximumSize(new Dimension(520, 80));
        
        JLabel enterCodeLabel = new JLabel("Enter Reset Code");
        enterCodeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        enterCodeLabel.setForeground(PLUM);
        enterCodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField codeField = new JTextField();
        codeField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        codeField.setPreferredSize(new Dimension(520, 50));
        codeField.setMaximumSize(new Dimension(520, 50));
        codeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        codeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        codeFieldContainer.add(enterCodeLabel);
        codeFieldContainer.add(Box.createVerticalStrut(8));
        codeFieldContainer.add(codeField);
        
        JPanel passwordFieldContainer = new JPanel();
        passwordFieldContainer.setLayout(new BoxLayout(passwordFieldContainer, BoxLayout.Y_AXIS));
        passwordFieldContainer.setBackground(Color.WHITE);
        passwordFieldContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordFieldContainer.setMaximumSize(new Dimension(520, 80));
        
        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newPasswordLabel.setForeground(PLUM);
        newPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        newPasswordField.setPreferredSize(new Dimension(520, 50));
        newPasswordField.setMaximumSize(new Dimension(520, 50));
        newPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        passwordFieldContainer.add(newPasswordLabel);
        passwordFieldContainer.add(Box.createVerticalStrut(8));
        passwordFieldContainer.add(newPasswordField);
        
        JPanel strengthContainer = new JPanel();
        strengthContainer.setLayout(new BoxLayout(strengthContainer, BoxLayout.Y_AXIS));
        strengthContainer.setBackground(Color.WHITE);
        strengthContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        strengthContainer.setMaximumSize(new Dimension(520, 40));
        
        JPanel strengthPanel = new JPanel();
        strengthPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
        strengthPanel.setBackground(Color.WHITE);
        strengthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel strengthTextLabel = new JLabel("Strength: ");
        strengthTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        strengthTextLabel.setForeground(new Color(100, 100, 100));
        
        JProgressBar passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setForeground(ERROR_RED);
        passwordStrengthBar.setBackground(new Color(240, 240, 240));
        passwordStrengthBar.setPreferredSize(new Dimension(150, 12));
        passwordStrengthBar.setStringPainted(false);
        
        JLabel passwordStrengthLabel = new JLabel("Weak");
        passwordStrengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordStrengthLabel.setForeground(ERROR_RED);
        
        strengthPanel.add(strengthTextLabel);
        strengthPanel.add(passwordStrengthBar);
        strengthPanel.add(Box.createHorizontalStrut(10));
        strengthPanel.add(passwordStrengthLabel);
        
        strengthContainer.add(strengthPanel);
        
        newPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String password = new String(newPasswordField.getPassword());
                updatePasswordStrength(password, passwordStrengthBar, passwordStrengthLabel);
            }
        });
        
        JButton resetButton = new JButton("Reset Password");
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(PLUM);
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);
        resetButton.setPreferredSize(new Dimension(520, 55));
        resetButton.setMaximumSize(new Dimension(520, 55));
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        resetButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                resetButton.setBackground(new Color(70, 0, 50));
            }
            public void mouseExited(MouseEvent e) {
                resetButton.setBackground(PLUM);
            }
        });
        
        resetButton.addActionListener(e -> {
            String enteredCode = codeField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            
            if (enteredCode.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(codeDialog,
                    "Please enter both the reset code and new password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(codeDialog,
                    "New password must be at least 6 characters long.",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (enteredCode.equals(token)) {
                boolean success = userModel.resetPassword(email, token, newPassword);
                if (success) {
                    JOptionPane.showMessageDialog(codeDialog,
                        "Password reset successfully! You can now login with your new password.",
                        "Password Reset",
                        JOptionPane.INFORMATION_MESSAGE);
                    codeDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(codeDialog,
                        "Failed to reset password. Please try again.",
                        "Reset Failed",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(codeDialog,
                    "Invalid reset code. Please check your email and try again.",
                    "Invalid Code",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(instructionPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(codeFieldContainer);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(passwordFieldContainer);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(strengthContainer);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel requirementsPanel = new JPanel();
        requirementsPanel.setLayout(new BoxLayout(requirementsPanel, BoxLayout.Y_AXIS));
        requirementsPanel.setBackground(Color.WHITE);
        requirementsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        requirementsPanel.setMaximumSize(new Dimension(520, 120));
        requirementsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel reqTitle = new JLabel("Password Requirements:");
        reqTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reqTitle.setForeground(PLUM);
        reqTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] requirements = {
            "• At least 6 characters long",
            "• Mix of letters, numbers, and symbols recommended",
            "• Avoid common words or personal information"
        };

        requirementsPanel.add(reqTitle);
        requirementsPanel.add(Box.createVerticalStrut(6));

        for (String req : requirements) {
            JLabel reqLabel = new JLabel(req);
            reqLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            reqLabel.setForeground(new Color(100, 100, 100));
            reqLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            requirementsPanel.add(reqLabel);
            requirementsPanel.add(Box.createVerticalStrut(2));
        }

        mainPanel.add(requirementsPanel);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(resetButton);
        
        codeDialog.add(mainPanel);
        codeDialog.setVisible(true);
    }
    
    private void updatePasswordStrength(String password, JProgressBar progressBar, JLabel strengthLabel) {
        if (password.isEmpty()) {
            progressBar.setValue(0);
            strengthLabel.setText("Weak");
            strengthLabel.setForeground(ERROR_RED);
            progressBar.setForeground(ERROR_RED);
            return;
        }
        
        int strength = 0;
        
        if (password.length() >= 8) strength += 25;
        if (password.length() >= 12) strength += 15;
        
        if (password.matches(".*[A-Z].*")) strength += 20;
        if (password.matches(".*[a-z].*")) strength += 20;
        if (password.matches(".*\\d.*")) strength += 20;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength += 20;
        
        progressBar.setValue(strength);
        
        if (strength < 40) {
            strengthLabel.setText("Weak");
            strengthLabel.setForeground(ERROR_RED);
            progressBar.setForeground(ERROR_RED);
        } else if (strength < 70) {
            strengthLabel.setText("Fair");
            strengthLabel.setForeground(WARNING_ORANGE);
            progressBar.setForeground(WARNING_ORANGE);
        } else {
            strengthLabel.setText("Strong");
            strengthLabel.setForeground(SUCCESS_GREEN);
            progressBar.setForeground(SUCCESS_GREEN);
        }
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both email and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (userModel.authenticateUser(email, password)) {
            saveRememberedCredentials();
            loginSuccessful = true;
            JOptionPane.showMessageDialog(this,
                "Welcome back, " + userModel.getCurrentUser().getFirstName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
            if (parentFrame != null) {
                parentFrame.setVisible(true);
                parentFrame.toFront();
            }
            
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid email or password. Please try again.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    private void openSignupDialog() {
        dispose();
        SignupView signupView = new SignupView(parentFrame, mandatoryLogin);
        signupView.setVisible(true);
        
        if (signupView.isSignupSuccessful()) {
            loginSuccessful = true;
        } else if (mandatoryLogin) {
            SwingUtilities.invokeLater(() -> {
                LoginView newLogin = new LoginView(parentFrame, true);
                newLogin.setVisible(true);
                if (!newLogin.isLoginSuccessful()) {
                    System.exit(0);
                }
            });
        }
    }
    
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
    
    public static boolean showLoginDialog(JFrame parent) {
        LoginView loginView = new LoginView(parent);
        loginView.setVisible(true);
        return loginView.isLoginSuccessful();
    }
    
    private void setupAdminHotkey() {
      
        JPanel mainPanel = (JPanel) this.getContentPane().getComponent(0);
        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();
        
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_A) {
                    showAdminLogin();
                }
            }
        });
    }
}