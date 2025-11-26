package undouse_hotel.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import undouse_hotel.model.UserModel;

public class SignupView extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Color PLUM = new Color(90, 0, 60);
    private static final Color GOLD = new Color(230, 180, 120);
    private static final Color LIGHT_BG = new Color(255, 248, 240);
    private static final Color SUCCESS_GREEN = new Color(0, 150, 0);
    private static final Color ERROR_RED = new Color(200, 0, 0);
    private static final Color WARNING_ORANGE = new Color(255, 140, 0);
    private static final Color DARK_GRAY = new Color(60, 60, 60);
    private static final Color LIGHT_GRAY = new Color(240, 240, 240);
    
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField cityField;
    private JComboBox<String> countryCombo;
    private JCheckBox termsCheckBox;
    
    private JLabel passwordStrengthLabel;
    private JProgressBar passwordStrengthBar;
    private JLabel passwordMatchLabel;
    private JButton signupButton;
    private JButton nextButton;
    private JButton backButton;
    
    private JFrame parentFrame;
    private UserModel userModel;
    private boolean signupSuccessful = false;
    private boolean mandatoryLogin;
    
    private JPanel progressPanel;
    private JLabel[] stepLabels;
    private JLabel[] stepIcons;
    private String[] steps = {"Personal", "Account", "Review"};
    private int currentStep = 0;
    
    private JLabel reviewNameLabel;
    private JLabel reviewEmailLabel;
    private JLabel reviewPhoneLabel;
    private JLabel reviewAddressLabel;
    private JLabel reviewCityLabel;
    private JLabel reviewCountryLabel;
    
    private JPanel stepsContainer;
    
    public SignupView(JFrame parent) {
        this(parent, false);
    }
    
    public SignupView(JFrame parent, boolean mandatoryLogin) {
        super(parent, "Sign Up - Undouse Hotel", true);
        this.parentFrame = parent;
        this.userModel = UserModel.getInstance();
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
                    showExitConfirmation();
                }
            });
        } else {
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        }
        
        initializeComponents();
    }
    
    private void showExitConfirmation() {
        int choice = JOptionPane.showConfirmDialog(
            SignupView.this,
            "<html><div style='text-align: center;'>Do you want to return to login?<br><small>Your progress will be saved</small></div></html>",
            "Cancel Signup",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
    
    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        JPanel brandingPanel = createBrandingPanel();
        
        JPanel formPanel = createFormPanel();
        
        mainPanel.add(brandingPanel, BorderLayout.WEST);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        setupValidationListeners();
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
        
        JLabel tagline = new JLabel("Join Our Family");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(GOLD);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setBorder(BorderFactory.createEmptyBorder(10, 0, 60, 0));
        
        
        String[] benefits = {
            "Exclusive Member Rates",
            "Faster Check-in Process",
            "Personalized Service",
            "Special Offers & Deals",
            "Easy Booking Management",
            "24/7 Customer Support"
        };
        
        JPanel benefitsPanel = new JPanel();
        benefitsPanel.setLayout(new BoxLayout(benefitsPanel, BoxLayout.Y_AXIS));
        benefitsPanel.setBackground(PLUM);
        benefitsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        for (String benefit : benefits) {
            JLabel benefitLabel = new JLabel(benefit);
            benefitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            benefitLabel.setForeground(Color.WHITE);
            benefitLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            benefitLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            benefitsPanel.add(benefitLabel);
        }
        
        contentPanel.add(logoLabel);
        contentPanel.add(hotelName);
        contentPanel.add(tagline);
        contentPanel.add(benefitsPanel);
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
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        
        JPanel headerPanel = createHeaderPanel();
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        
        stepsContainer = new JPanel(new CardLayout());
        stepsContainer.setBackground(Color.WHITE);
        
                JPanel step1Panel = createPersonalInfoStep();
        
        JPanel step2Panel = createAccountSecurityStep();
        
        JPanel step3Panel = createReviewStep();
        
        stepsContainer.add(step1Panel, "step1");
        stepsContainer.add(step2Panel, "step2");
        stepsContainer.add(step3Panel, "step3");
        
        contentPanel.add(stepsContainer, BorderLayout.CENTER);
        
        JPanel navPanel = createNavigationPanel();
        
        formPanel.add(headerPanel, BorderLayout.NORTH);
        formPanel.add(contentPanel, BorderLayout.CENTER);
        formPanel.add(navPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(DARK_GRAY);
        
        JLabel subtitleLabel = new JLabel("Join Undouse Hotel in just a few steps");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        
        titlePanel.add(titleLabel);
        
        progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        stepLabels = new JLabel[3];
        stepIcons = new JLabel[3];
        
        for (int i = 0; i < 3; i++) {
            JPanel stepPanel = new JPanel();
            stepPanel.setLayout(new BoxLayout(stepPanel, BoxLayout.Y_AXIS));
            stepPanel.setBackground(Color.WHITE);
            stepPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
            
            JLabel stepIcon = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            stepIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
            stepIcon.setForeground(i == 0 ? Color.WHITE : new Color(180, 180, 180));
            stepIcon.setOpaque(true);
            stepIcon.setBackground(i == 0 ? PLUM : new Color(240, 240, 240));
            stepIcon.setBorder(BorderFactory.createLineBorder(i == 0 ? PLUM : new Color(220, 220, 220), 2));
            stepIcon.setPreferredSize(new Dimension(40, 40));
            stepIcon.setMinimumSize(new Dimension(40, 40));
            stepIcon.setMaximumSize(new Dimension(40, 40));
            stepIcon.setHorizontalAlignment(SwingConstants.CENTER);
            stepIcon.setVerticalAlignment(SwingConstants.CENTER);
            
                        JLabel stepLabel = new JLabel(steps[i], SwingConstants.CENTER);
            stepLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            stepLabel.setForeground(i == 0 ? PLUM : new Color(180, 180, 180));
            
            stepPanel.add(stepIcon);
            stepPanel.add(Box.createVerticalStrut(8));
            stepPanel.add(stepLabel);
            
            stepLabels[i] = stepLabel;
            stepIcons[i] = stepIcon;
            
            progressPanel.add(stepPanel);
            
            if (i < 2) {
                JLabel connector = new JLabel("──────");
                connector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                connector.setForeground(new Color(220, 220, 220));
                connector.setVerticalAlignment(SwingConstants.CENTER);
                progressPanel.add(connector);
            }
        }
        
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(progressPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createPersonalInfoStep() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel stepTitle = new JLabel("Personal Information");
        stepTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        stepTitle.setForeground(PLUM);
        stepTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        stepTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel stepDesc = new JLabel("Tell us about yourself");
        stepDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stepDesc.setForeground(new Color(120, 120, 120));
        stepDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        stepDesc.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(Color.WHITE);
        formGrid.setMaximumSize(new Dimension(1000, 500));
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
                gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        formGrid.add(createFormLabel("First Name *"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
        firstNameField = createFormField();
        formGrid.add(firstNameField, gbc);
        
                gbc.gridx = 0; gbc.gridy = 1;
        formGrid.add(createFormLabel("Last Name *"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        lastNameField = createFormField();
        formGrid.add(lastNameField, gbc);
        
                gbc.gridx = 0; gbc.gridy = 2;
        formGrid.add(createFormLabel("Email Address *"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        emailField = createFormField();
        formGrid.add(emailField, gbc);
        
                gbc.gridx = 0; gbc.gridy = 3;
        formGrid.add(createFormLabel("Phone Number *"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        phoneField = createFormField();
        phoneField.setToolTipText("Must be 11 digits (e.g., 09123456789)");
        formGrid.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formGrid.add(createFormLabel("Street Address *"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        addressField = createFormField();
        formGrid.add(addressField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formGrid.add(createFormLabel("City *"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        cityField = createFormField();
        formGrid.add(cityField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formGrid.add(createFormLabel("Country *"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 6;
        countryCombo = createCountryCombo();
        countryCombo.setSelectedItem("Philippines");
        formGrid.add(countryCombo, gbc);
        
        panel.add(stepTitle);
        panel.add(stepDesc);
        panel.add(formGrid);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createAccountSecurityStep() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel stepTitle = new JLabel("Account Security");
        stepTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        stepTitle.setForeground(PLUM);
        stepTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel stepDesc = new JLabel("Create a secure password for your account");
        stepDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stepDesc.setForeground(new Color(120, 120, 120));
        stepDesc.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel requirementsContainer = createRequirementsContainer();

        JPanel formContainer = createPasswordFormContainer();

        contentPanel.add(requirementsContainer);
        contentPanel.add(formContainer);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(stepTitle, BorderLayout.NORTH);
        titlePanel.add(stepDesc, BorderLayout.CENTER);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRequirementsContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(248, 248, 248));
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        container.setMaximumSize(new Dimension(400, 500));

        JLabel title = new JLabel("Password Requirements");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PLUM);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

                JLabel desc = new JLabel("<html>Your password must meet the following criteria to ensure account security:</html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(new Color(100, 100, 100));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

                String[] requirements = {
            "At least 8 characters long",
            "One uppercase letter (A-Z)",
            "One lowercase letter (a-z)", 
            "One number (0-9)",
            "One special character (!@#$%^&*)"
        };

        JPanel reqListPanel = new JPanel();
        reqListPanel.setLayout(new BoxLayout(reqListPanel, BoxLayout.Y_AXIS));
        reqListPanel.setBackground(new Color(248, 248, 248));
        reqListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String req : requirements) {
            JPanel reqItem = createRequirementItem(req);
            reqListPanel.add(reqItem);
            reqListPanel.add(Box.createVerticalStrut(8));
        }

                JLabel tipsTitle = new JLabel("Security Tips:");
        tipsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tipsTitle.setForeground(PLUM);
        tipsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        tipsTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        String[] tips = {
            "Avoid using personal information",
            "Don't reuse passwords from other sites",
            "Consider using a passphrase",
            "Update your password regularly"
        };

        JPanel tipsPanel = new JPanel();
        tipsPanel.setLayout(new BoxLayout(tipsPanel, BoxLayout.Y_AXIS));
        tipsPanel.setBackground(new Color(248, 248, 248));
        tipsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String tip : tips) {
            JLabel tipLabel = new JLabel(tip);
            tipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tipLabel.setForeground(new Color(120, 120, 120));
            tipLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            tipsPanel.add(tipLabel);
        }

        container.add(title);
        container.add(desc);
        container.add(reqListPanel);
        container.add(Box.createVerticalGlue());
        container.add(tipsTitle);
        container.add(tipsPanel);

        return container;
    }

    private JPanel createRequirementItem(String requirement) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(new Color(248, 248, 248));
        panel.setMaximumSize(new Dimension(350, 25));

        JLabel text = new JLabel("- " + requirement);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setForeground(new Color(100, 100, 100));

        panel.add(text);

        return panel;
    }

    private JPanel createPasswordFormContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setMaximumSize(new Dimension(500, 500));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel passwordFieldPanel = new JPanel(new BorderLayout());
        passwordFieldPanel.setBackground(Color.WHITE);
        passwordFieldPanel.setMaximumSize(new Dimension(500, 90));
        
        passwordFieldPanel.add(createFormLabel("Password *"), BorderLayout.NORTH);

        JPanel passwordWrapper = new JPanel(new BorderLayout(0, 0));
     	passwordWrapper.setBackground(Color.WHITE);
     	passwordWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
     	passwordWrapper.setPreferredSize(new Dimension(400, 45));
     	passwordWrapper.setMaximumSize(new Dimension(400, 45));

     	passwordField = createPasswordField();
     	passwordField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 5));
     	passwordWrapper.add(passwordField, BorderLayout.CENTER);
     	passwordWrapper.add(createPasswordToggleButton(passwordField), BorderLayout.EAST);

     	passwordFieldPanel.add(passwordWrapper, BorderLayout.CENTER);

        JPanel strengthContainer = new JPanel(new BorderLayout());
        strengthContainer.setBackground(Color.WHITE);
        strengthContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel strengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        strengthPanel.setBackground(Color.WHITE);
        
        passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setForeground(ERROR_RED);
        passwordStrengthBar.setBackground(LIGHT_GRAY);
        passwordStrengthBar.setPreferredSize(new Dimension(150, 12));
        passwordStrengthBar.setStringPainted(false);
        
        passwordStrengthLabel = new JLabel("Enter a password");
        passwordStrengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordStrengthLabel.setForeground(new Color(150, 150, 150));
        
        strengthPanel.add(new JLabel("Strength:"));
        strengthPanel.add(passwordStrengthBar);
        strengthPanel.add(Box.createHorizontalStrut(10));
        strengthPanel.add(passwordStrengthLabel);
        
        strengthContainer.add(strengthPanel, BorderLayout.WEST);

        JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
        confirmPasswordPanel.setBackground(Color.WHITE);
        confirmPasswordPanel.setMaximumSize(new Dimension(500, 90));
        confirmPasswordPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        
        confirmPasswordPanel.add(createFormLabel("Confirm Password *"), BorderLayout.NORTH);

        JPanel confirmPasswordWrapper = new JPanel(new BorderLayout(0, 0));
     	confirmPasswordWrapper.setBackground(Color.WHITE);
     	confirmPasswordWrapper.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
     	confirmPasswordWrapper.setPreferredSize(new Dimension(400, 45));
     	confirmPasswordWrapper.setMaximumSize(new Dimension(400, 45));

     	confirmPasswordField = createPasswordField();
     	confirmPasswordField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 5));
     	confirmPasswordWrapper.add(confirmPasswordField, BorderLayout.CENTER);
     	confirmPasswordWrapper.add(createPasswordToggleButton(confirmPasswordField), BorderLayout.EAST);

     	confirmPasswordPanel.add(confirmPasswordWrapper, BorderLayout.CENTER);

        passwordMatchLabel = new JLabel("Passwords will be checked here");
        passwordMatchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordMatchLabel.setForeground(new Color(150, 150, 150));
        passwordMatchLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        container.add(passwordFieldPanel);
        container.add(strengthContainer);
        container.add(confirmPasswordPanel);
        container.add(passwordMatchLabel);
        container.add(Box.createVerticalGlue());

        return container;
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
        toggleButton.setPreferredSize(new Dimension(40, 45));
        
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
    
    private JPanel createReviewStep() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel stepTitle = new JLabel("Review Information");
        stepTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        stepTitle.setForeground(PLUM);
        stepTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        stepTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel stepDesc = new JLabel("Please review your information before creating your account");
        stepDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stepDesc.setForeground(new Color(120, 120, 120));
        stepDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        stepDesc.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setBackground(new Color(250, 250, 250));
        reviewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        reviewPanel.setMaximumSize(new Dimension(600, 350));
        reviewPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel namePanel = createReviewRow("Name:", "");
        JPanel emailPanel = createReviewRow("Email:", "");
        JPanel phonePanel = createReviewRow("Phone:", "");
        JPanel addressPanel = createReviewRow("Address:", "");
        JPanel cityPanel = createReviewRow("City:", "");
        JPanel countryPanel = createReviewRow("Country:", "");
        
        reviewPanel.add(namePanel);
        reviewPanel.add(createSeparator());
        reviewPanel.add(emailPanel);
        reviewPanel.add(createSeparator());
        reviewPanel.add(phonePanel);
        reviewPanel.add(createSeparator());
        reviewPanel.add(addressPanel);
        reviewPanel.add(createSeparator());
        reviewPanel.add(cityPanel);
        reviewPanel.add(createSeparator());
        reviewPanel.add(countryPanel);
        
        JPanel termsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        termsPanel.setBackground(Color.WHITE);
        termsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        termsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        termsPanel.setMaximumSize(new Dimension(600, 60));
        
        termsCheckBox = new JCheckBox();
        termsCheckBox.setBackground(Color.WHITE);
        
        JLabel termsText = new JLabel("I agree to the ");
        termsText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel termsLink = new JLabel("Terms and Conditions");
        termsLink.setFont(new Font("Segoe UI", Font.BOLD, 14));
        termsLink.setForeground(GOLD);
        termsLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        termsLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showTermsDialog();
            }
            public void mouseEntered(MouseEvent e) {
                termsLink.setForeground(new Color(200, 150, 100));
            }
            public void mouseExited(MouseEvent e) {
                termsLink.setForeground(GOLD);
            }
        });
        
        termsPanel.add(termsCheckBox);
        termsPanel.add(termsText);
        termsPanel.add(termsLink);
        
        panel.add(stepTitle);
        panel.add(stepDesc);
        panel.add(Box.createVerticalStrut(15));
        panel.add(reviewPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(termsPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBackground(new Color(220, 220, 220));
        separator.setForeground(new Color(220, 220, 220));
        separator.setMaximumSize(new Dimension(600, 1));
        return separator;
    }
    
    private JPanel createReviewRow(String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(new Color(250, 250, 250));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rowPanel.setMaximumSize(new Dimension(600, 40));
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComponent.setForeground(DARK_GRAY);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComponent.setForeground(DARK_GRAY);
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.EAST);
        
                if (label.equals("Name:")) {
            reviewNameLabel = valueComponent;
        } else if (label.equals("Email:")) {
            reviewEmailLabel = valueComponent;
        } else if (label.equals("Phone:")) {
            reviewPhoneLabel = valueComponent;
        } else if (label.equals("Address:")) {
            reviewAddressLabel = valueComponent;
        } else if (label.equals("City:")) {
            reviewCityLabel = valueComponent;
        } else if (label.equals("Country:")) {
            reviewCountryLabel = valueComponent;
        }
        
        return rowPanel;
    }
    
    private void updateReviewInformation() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String country = (String) countryCombo.getSelectedItem();
        
        reviewNameLabel.setText(firstName.isEmpty() && lastName.isEmpty() ? "Not provided" : firstName + " " + lastName);
        reviewEmailLabel.setText(email.isEmpty() ? "Not provided" : email);
        reviewPhoneLabel.setText(phone.isEmpty() ? "Not provided" : phone);
        reviewAddressLabel.setText(address.isEmpty() ? "Not provided" : address);
        reviewCityLabel.setText(city.isEmpty() ? "Not provided" : city);
        reviewCountryLabel.setText("Select Country".equals(country) ? "Not provided" : country);
    }
    
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backToLoginButton.setForeground(Color.GRAY);
        backToLoginButton.setBackground(Color.WHITE);
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.setBorderPainted(true);
        backToLoginButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        backToLoginButton.setPreferredSize(new Dimension(160, 50));
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginButton.addActionListener(e -> confirmBackToLogin());
        
        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setForeground(PLUM);
        backButton.setBackground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(true);
        backButton.setBorder(BorderFactory.createLineBorder(PLUM, 2));
        backButton.setPreferredSize(new Dimension(120, 50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setVisible(false); 
        backButton.addActionListener(e -> previousStep());
        
        nextButton = new JButton("Next Step");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBackground(PLUM);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setPreferredSize(new Dimension(140, 50));
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(e -> nextStep());
        
        signupButton = new JButton("CREATE ACCOUNT");
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(PLUM);
        signupButton.setFocusPainted(false);
        signupButton.setBorderPainted(false);
        signupButton.setPreferredSize(new Dimension(200, 50));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.setVisible(false);
        signupButton.addActionListener(e -> handleSignup());
        
        addButtonHoverEffects(nextButton);
        addButtonHoverEffects(signupButton);
        
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Color.WHITE);
            }
        });
        
        backToLoginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backToLoginButton.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(MouseEvent e) {
                backToLoginButton.setBackground(Color.WHITE);
            }
        });
        
        buttonPanel.add(backToLoginButton);
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(signupButton);
        
        navPanel.add(buttonPanel, BorderLayout.CENTER);
        
        return navPanel;
    }
    
    private void addButtonHoverEffects(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(70, 0, 50));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(PLUM);
                }
            }
        });
    }
    
    private void confirmBackToLogin() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to go back to the login page? Unsaved information will be lost.",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            backToLogin();
        }
    }
    
    private void nextStep() {
        if (currentStep < 2) {
            if (!isCurrentStepValid()) {
                JOptionPane.showMessageDialog(this,
                    "Please complete all required fields correctly before proceeding to the next step.",
                    "Incomplete Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            currentStep++;
            updateProgressIndicator();
            updateNavigationButtons();
            CardLayout cl = (CardLayout) stepsContainer.getLayout();
            cl.show(stepsContainer, "step" + (currentStep + 1));
            
            if (currentStep == 2) {
                updateReviewInformation();
            }
        }
    }
    
    private void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateProgressIndicator();
            updateNavigationButtons();
            CardLayout cl = (CardLayout) stepsContainer.getLayout();
            cl.show(stepsContainer, "step" + (currentStep + 1));
        }
    }
    
    private void updateNavigationButtons() {
        backButton.setVisible(currentStep > 0);
        
        if (currentStep == 2) {
            nextButton.setVisible(false);
            signupButton.setVisible(true);
            updateSubmitButton();
        } else {
            nextButton.setVisible(true);
            signupButton.setVisible(false);
            updateNextButtonState();
        }
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(DARK_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return label;
    }
    
    private JTextField createFormField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(DARK_GRAY);
        field.setCaretColor(DARK_GRAY);
        field.setPreferredSize(new Dimension(400, 45));
        field.setMinimumSize(new Dimension(400, 45));
        field.setMaximumSize(new Dimension(400, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(DARK_GRAY);
        field.setCaretColor(DARK_GRAY);
        field.setPreferredSize(new Dimension(400, 45));
        field.setMinimumSize(new Dimension(400, 45));
        field.setMaximumSize(new Dimension(400, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setEchoChar('•');
        return field;
    }
    
    private JComboBox<String> createCountryCombo() {
        JComboBox<String> combo = new JComboBox<>(new String[]{
            "Select Country", "Philippines", "United States", "Japan", 
            "South Korea", "Singapore", "Malaysia", "Thailand", "Other"
        });
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        combo.setForeground(DARK_GRAY);
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(400, 45));
        combo.setMinimumSize(new Dimension(400, 45));
        combo.setMaximumSize(new Dimension(400, 45));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return combo;
    }
    
    private JPanel createRequirementsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel title = new JLabel("Password Requirements:");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(PLUM);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] requirements = {
            "At least 8 characters long",
            "One uppercase letter (A-Z)",
            "One lowercase letter (a-z)", 
            "One number (0-9)",
            "One special character (!@#$%^&*)"
        };
        
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        
        for (String req : requirements) {
            JLabel reqLabel = new JLabel(req);
            reqLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            reqLabel.setForeground(new Color(120, 120, 120));
            reqLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(reqLabel);
        }
        
        return panel;
    }
    
    private boolean isCurrentStepValid() {
        switch (currentStep) {
            case 0:  
                return isPersonalInfoValid();
            case 1: 
                return isAccountSecurityValid();
            default:
                return true;
        }
    }
    
    private boolean isPersonalInfoValid() {
        if (firstNameField.getText().trim().isEmpty()) return false;
        if (lastNameField.getText().trim().isEmpty()) return false;
        if (!isEmailValid(emailField.getText().trim())) return false;
        if (!isPhoneValid(phoneField.getText().trim())) return false;
        if (addressField.getText().trim().isEmpty()) return false;
        if (cityField.getText().trim().isEmpty()) return false;
        if ("Select Country".equals(countryCombo.getSelectedItem())) return false;
        
        return true;
    }
    
    private boolean isAccountSecurityValid() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        return isStrongPassword(password) && 
               password.equals(confirmPassword) && 
               !password.isEmpty();
    }
    
    private boolean isEmailValid(String email) {
        if (email.isEmpty()) return false;
        return email.toLowerCase().contains("@gmail.com");
    }
    
    private boolean isPhoneValid(String phone) {
        if (phone.isEmpty()) return false;
        return phone.matches("\\d{11}");
    }
    
    private void updateProgressIndicator() {
        for (int i = 0; i < 3; i++) {
            boolean isActive = i == currentStep;
            boolean isCompleted = i < currentStep;
            
            stepIcons[i].setForeground(isActive || isCompleted ? Color.WHITE : new Color(180, 180, 180));
            stepIcons[i].setBackground(isActive || isCompleted ? PLUM : new Color(240, 240, 240));
            stepIcons[i].setBorder(BorderFactory.createLineBorder(isActive || isCompleted ? PLUM : new Color(220, 220, 220), 2));
            
            stepLabels[i].setForeground(isActive || isCompleted ? PLUM : new Color(180, 180, 180));
        }
    }
    
    private void setupValidationListeners() {
        FocusAdapter focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateField(e.getComponent());
                updateNextButtonState();
                updateSubmitButton();
            }
        };
        
        KeyAdapter keyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateField(e.getComponent());
                updateNextButtonState();
                updateSubmitButton();
            }
        };
        
        Component[] fields = {firstNameField, lastNameField, emailField, phoneField, 
                             addressField, cityField, passwordField, confirmPasswordField};
        for (Component field : fields) {
            field.addFocusListener(focusListener);
            field.addKeyListener(keyListener);
        }
        
        countryCombo.addActionListener(e -> {
            validateField(countryCombo);
            updateNextButtonState();
            updateSubmitButton();
        });
        
        termsCheckBox.addActionListener(e -> {
            updateSubmitButton();
        });
        
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePasswordStrength();
                checkPasswordMatch();
                updateNextButtonState();
                updateSubmitButton();
            }
        });
        
        confirmPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkPasswordMatch();
                updateNextButtonState();
                updateSubmitButton();
            }
        });
        
        updateNextButtonState();
        updateSubmitButton();
        updateNavigationButtons();
    }
    
    private void validateField(Component component) {
        boolean isValid = true;
        String value = "";
        
        if (component instanceof JTextField) {
            value = ((JTextField) component).getText().trim();
            if (component == emailField) {
                isValid = isEmailValid(value);
            } else if (component == phoneField) {
                isValid = isPhoneValid(value);
            } else {
                isValid = !value.isEmpty();
            }
        } else if (component instanceof JPasswordField) {
            value = new String(((JPasswordField) component).getPassword());
            if (component == passwordField) {
                isValid = isStrongPassword(value);
            } else if (component == confirmPasswordField) {
                String password = new String(passwordField.getPassword());
                isValid = value.equals(password) && !value.isEmpty();
            } else {
                isValid = !value.isEmpty();
            }
        } else if (component instanceof JComboBox) {
            value = (String) ((JComboBox<?>) component).getSelectedItem();
            isValid = !"Select Country".equals(value);
        }
        
        Color borderColor = isValid ? new Color(220, 220, 220) : ERROR_RED;
        
        if (component instanceof JTextField) {
            ((JTextField) component).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
        } else if (component instanceof JPasswordField) {
            ((JPasswordField) component).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
        } else if (component instanceof JComboBox) {
            ((JComboBox<?>) component).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
    }
    
    private void updateNextButtonState() {
        boolean canProceed = isCurrentStepValid();
        nextButton.setEnabled(canProceed);
        nextButton.setBackground(canProceed ? PLUM : new Color(180, 180, 180));
    }
    
    private void updatePasswordStrength() {
        String password = new String(passwordField.getPassword());
        int strength = calculatePasswordStrength(password);
        
        passwordStrengthBar.setValue(strength);
        
        if (strength < 25) {
            passwordStrengthBar.setForeground(ERROR_RED);
            passwordStrengthLabel.setText("Weak");
            passwordStrengthLabel.setForeground(ERROR_RED);
        } else if (strength < 50) {
            passwordStrengthBar.setForeground(WARNING_ORANGE);
            passwordStrengthLabel.setText("Fair");
            passwordStrengthLabel.setForeground(WARNING_ORANGE);
        } else if (strength < 75) {
            passwordStrengthBar.setForeground(new Color(255, 200, 0));
            passwordStrengthLabel.setText("Good");
            passwordStrengthLabel.setForeground(new Color(255, 200, 0));
        } else {
            passwordStrengthBar.setForeground(SUCCESS_GREEN);
            passwordStrengthLabel.setText("Strong");
            passwordStrengthLabel.setForeground(SUCCESS_GREEN);
        }
    }
    
    private int calculatePasswordStrength(String password) {
        if (password.isEmpty()) return 0;
        
        int strength = 0;
        
        if (password.length() >= 8) strength += 10;
        if (password.length() >= 12) strength += 10;
        if (password.length() >= 16) strength += 10;
        
        if (password.matches(".*[A-Z].*")) strength += 15; 
        if (password.matches(".*[a-z].*")) strength += 15; 
        if (password.matches(".*[0-9].*")) strength += 15; 
        if (password.matches(".*[!@#$%^&*].*")) strength += 15; 
        
                if (password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")) strength += 10;
        if (password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*")) strength += 10;
        
        return Math.min(strength, 100);
    }
    
    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[0-9].*") &&
               password.matches(".*[!@#$%^&*].*");
    }
    
    private void checkPasswordMatch() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (password.isEmpty() && confirmPassword.isEmpty()) {
            passwordMatchLabel.setText("Passwords will be checked here");
            passwordMatchLabel.setForeground(new Color(150, 150, 150));
        } else if (confirmPassword.isEmpty()) {
            passwordMatchLabel.setText("Please confirm your password");
            passwordMatchLabel.setForeground(WARNING_ORANGE);
        } else if (password.equals(confirmPassword)) {
            passwordMatchLabel.setText("Passwords match");
            passwordMatchLabel.setForeground(SUCCESS_GREEN);
        } else {
            passwordMatchLabel.setText("Passwords do not match");
            passwordMatchLabel.setForeground(ERROR_RED);
        }
    }
    
    private void updateSubmitButton() {
        boolean allValid = areAllFieldsValid();
        signupButton.setEnabled(allValid);
        signupButton.setBackground(allValid ? PLUM : new Color(180, 180, 180));
    }
    
    private boolean areAllFieldsValid() {
        return isPersonalInfoValid() && isAccountSecurityValid() && termsCheckBox.isSelected();
    }
    
    private void showTermsDialog() {
        JDialog termsDialog = new JDialog(this, "Terms and Conditions", true);
        termsDialog.setSize(700, 600);
        termsDialog.setLocationRelativeTo(this);
        termsDialog.setResizable(false);
        
        JPanel termsPanel = new JPanel(new BorderLayout());
        termsPanel.setBackground(Color.WHITE);
        termsPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel title = new JLabel("Terms and Conditions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PLUM);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JTextArea termsText = new JTextArea();
        termsText.setEditable(false);
        termsText.setLineWrap(true);
        termsText.setWrapStyleWord(true);
        termsText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        termsText.setBackground(LIGHT_BG);
        termsText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        termsText.setText(
            "UNDOUSE HOTEL - TERMS AND CONDITIONS\n\n" +
            "Last Updated: October 2025\n\n" +
            "1. ACCEPTANCE OF TERMS\n" +
            "By creating an account with Undouse Hotel, you agree to be bound by these Terms and Conditions. " +
            "If you do not agree to these terms, please do not create an account.\n\n" +
            "2. ACCOUNT REGISTRATION\n" +
            "- You must provide accurate, current, and complete information during registration\n" +
            "- You are responsible for maintaining the confidentiality of your account credentials\n" +
            "- You must notify us immediately of any unauthorized use of your account\n" +
            "- You must be at least 18 years old to create an account\n\n" +
            "3. BOOKING AND RESERVATIONS\n" +
            "- All bookings are subject to room availability\n" +
            "- Prices are subject to change without notice\n" +
            "- Full payment is required at the time of booking\n" +
            "- Booking confirmation will be sent to your registered email address\n\n" +
            "4. CANCELLATION POLICY\n" +
            "- Cancellations must be made at least 48 hours before check-in date\n" +
            "- Late cancellations or no-shows may result in charges\n" +
            "- Refunds will be processed within 7-14 business days\n" +
            "- Special promotional rates may have different cancellation terms\n\n" +
            "5. PAYMENT TERMS\n" +
            "- We accept various payment methods including credit cards, GCash, and bank transfers\n" +
            "- All prices are in Philippine Peso (₱)\n" +
            "- Additional charges may apply for extra services\n" +
            "- Payment information is encrypted and securely processed\n\n" +
            "6. GUEST CONDUCT\n" +
            "- Guests must respect hotel property and other guests\n" +
            "- Smoking is only allowed in designated areas\n" +
            "- Pets are not allowed unless specified\n" +
            "- The hotel reserves the right to refuse service to guests who violate policies\n\n" +
            "7. PRIVACY POLICY\n" +
            "- We collect and store personal information as described in our Privacy Policy\n" +
            "- Your information will not be shared with third parties without consent\n" +
            "- We use cookies to enhance your browsing experience\n" +
            "- You have the right to request deletion of your personal data\n\n" +
            "8. LIABILITY\n" +
            "- The hotel is not responsible for loss or damage to personal belongings\n" +
            "- Guests are responsible for any damage caused to hotel property\n" +
            "- The hotel's liability is limited to the amount paid for the reservation\n\n" +
            "9. MODIFICATIONS\n" +
            "- We reserve the right to modify these terms at any time\n" +
            "- Continued use of our services constitutes acceptance of modified terms\n" +
            "- Major changes will be communicated via email\n\n" +
            "10. CONTACT INFORMATION\n" +
            "For questions about these terms, contact us at:\n" +
            "Email: info@undousehotel.com\n" +
            "Phone: +639916649798\n" +
            "Address: New Seaside Drive, Quezon City, Philippines\n\n" +
            "By checking the box, you acknowledge that you have read, understood, and agree to be bound by these Terms and Conditions."
        );
        
        JScrollPane termsScroll = new JScrollPane(termsText);
        termsScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        termsScroll.setBackground(Color.WHITE);
        
        SwingUtilities.invokeLater(() -> {
            termsScroll.getVerticalScrollBar().setValue(0);
        });
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.setBackground(PLUM);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(120, 40));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> termsDialog.dispose());
        
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (closeButton.isEnabled()) {
                    closeButton.setBackground(new Color(70, 0, 50));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (closeButton.isEnabled()) {
                    closeButton.setBackground(PLUM);
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(closeButton);
        
        termsPanel.add(title, BorderLayout.NORTH);
        termsPanel.add(termsScroll, BorderLayout.CENTER);
        termsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        termsDialog.add(termsPanel);
        termsDialog.setVisible(true);
    }
    
    private void handleSignup() {
        if (!areAllFieldsValid()) {
            JOptionPane.showMessageDialog(this,
                "Please complete all required fields correctly before signing up.",
                "Incomplete Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String country = (String) countryCombo.getSelectedItem();
        String password = new String(passwordField.getPassword());
        
        boolean registered = userModel.registerUser(email, password, firstName, lastName, 
                                                     phone, address, city, country);
        
        if (registered) {
            userModel.authenticateUser(email, password);
            signupSuccessful = true;
            
            JOptionPane.showMessageDialog(this,
                "Account created successfully!\nWelcome to Undouse Hotel, " + firstName + "!",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "An account with this email already exists.\nPlease use a different email or login.",
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void backToLogin() {
        dispose();
        LoginView loginView = new LoginView(parentFrame, mandatoryLogin);
        loginView.setVisible(true);
    }
    
    public boolean isSignupSuccessful() {
        return signupSuccessful;
    }
}