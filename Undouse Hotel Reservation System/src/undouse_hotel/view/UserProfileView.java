package undouse_hotel.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import undouse_hotel.model.UserModel;
import undouse_hotel.model.AdminModel;
import com.toedter.calendar.JDateChooser;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class UserProfileView extends BasePanelView {

    private static final Color DARK_PLUM = new Color(70, 0, 50);
    private static final Color PLUM = new Color(74, 20, 56); 
    private static final Color LIGHT_GOLD = new Color(245, 220, 180);
    private static final Color GOLD = new Color(230, 190, 100);
    private static final Color CREAM_WHITE = new Color(255, 250, 245);
    private static final Color SOFT_GRAY = new Color(248, 248, 250);
    private static final Color ACCENT_COLOR = new Color(180, 140, 190);
    private static final Color LIGHT_BG = CREAM_WHITE;
    private static final Color WHITE = Color.WHITE;
    private static final Color SUCCESS_GREEN = new Color(0, 150, 0);
    private static final Color ERROR_RED = new Color(200, 0, 0);
    private static final Color WARNING_ORANGE = new Color(255, 140, 0);
    
    
    private JFrame parentFrame;
    private UserModel userModel;
    private AdminModel adminModel;
    private UserModel.User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Runnable onBackCallback;
    private JPanel sidePanel;
    private String currentSection = "Profile";
    private JLabel nameLabel;
    private JLabel emailLabel;
    
    
    public UserProfileView(JFrame parent, AdminModel adminModel, CardLayout cardLayout, JPanel mainPanel, Runnable onBackCallback) {
        this.parentFrame = parent;
        this.userModel = UserModel.getInstance();
        this.adminModel = adminModel;
        this.currentUser = userModel.getCurrentUser();
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.onBackCallback = onBackCallback;
    }
    
    @Override
    public JPanel createPanel() {
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(WHITE);
        
        sidePanel = createSidePanel();
        
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(LIGHT_BG);
        
        contentPanel.add(createProfilePanel(), "Profile");
        contentPanel.add(createReservationsPanel(), "Reservations");
        contentPanel.add(createSecurityPanel(), "Security");
        
        CardLayout contentLayout = (CardLayout) contentPanel.getLayout();
        contentLayout.show(contentPanel, "Profile");
        
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(LIGHT_BG);
        centerWrapper.add(contentPanel, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(WHITE);
        centerPanel.add(sidePanel, BorderLayout.WEST);
        centerPanel.add(centerWrapper, BorderLayout.CENTER);
        background.add(centerPanel, BorderLayout.CENTER);
        
        this.panel = background;
        return background;
    }
    
    @Override
    public String getPanelName() {
        return "User Profile";
    }
        
    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 25, 30, 25)
        ));
        
        // User avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int panelWidth = getWidth();
                int circleDiameter = 110;
                int x = (panelWidth - circleDiameter) / 2;
                
                g2d.setColor(GOLD);
                g2d.fillOval(x, 10, circleDiameter, circleDiameter);
                
                g2d.setColor(WHITE);
                g2d.setFont(new Font("Georgia", Font.BOLD, 42));
                String initials = getInitials();
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (panelWidth - fm.stringWidth(initials)) / 2;
                int textY = 75;
                g2d.drawString(initials, textX, textY);
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(250, 130));
        avatarPanel.setMaximumSize(new Dimension(250, 130));
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // User name
        nameLabel = new JLabel(currentUser.getFullName());
        nameLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        nameLabel.setForeground(PLUM);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // User email
        emailLabel = new JLabel(currentUser.getEmail());
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(100, 100, 100));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with proper spacing
        panel.add(avatarPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(40));
        
        // Menu buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));
        
        buttonPanel.add(createMenuButton("👤 Profile", "Profile", true));
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(createMenuButton("📋 Reservations", "Reservations", false));
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(createMenuButton("🔐 Security", "Security", false));
        
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(40));
        
        // Logout button
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
        logoutPanel.setBackground(WHITE);
        logoutPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutPanel.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));
        
        logoutPanel.add(createLogoutButton());
        panel.add(logoutPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    
    private String getInitials() {
        String firstName = currentUser.getFirstName();
        String lastName = currentUser.getLastName();
        String initials = "";
        
        if (firstName != null && !firstName.isEmpty()) {
            initials += firstName.charAt(0);
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials += lastName.charAt(0);
        }
        
        return initials.toUpperCase();
    }
    
    private JButton createMenuButton(String text, String action, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(250, 45));
        button.setMaximumSize(new Dimension(250, 45));
        button.setMinimumSize(new Dimension(250, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setName(action);
        
        if (selected) {
            button.setBackground(PLUM);
            button.setForeground(WHITE);
            button.setOpaque(true);
        } else {
            button.setOpaque(false);
            button.setForeground(PLUM);
        }
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getBackground().equals(PLUM)) {
                    button.setOpaque(true);
                    button.setBackground(LIGHT_BG);
                    button.setContentAreaFilled(true);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!action.equals(currentSection)) {
                    button.setOpaque(false);
                    button.setBackground(null);
                    button.setContentAreaFilled(false);
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                switchContent(action);
                currentSection = action;
                updateMenuSelection(action);
            }
        });
        
        return button;
    }
    
    private JButton createLogoutButton() {
        JButton button = new JButton("🚪 Logout");
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(250, 45));
        button.setMaximumSize(new Dimension(250, 45));
        button.setMinimumSize(new Dimension(250, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setOpaque(false);
        button.setForeground(new Color(220, 53, 69));
        button.setName("Logout");
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setOpaque(true);
                button.setBackground(new Color(255, 240, 240));
                button.setContentAreaFilled(true);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setOpaque(false);
                button.setBackground(null);
                button.setContentAreaFilled(false);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                handleLogout();
            }
        });
        
        return button;
    }
    
    private void switchContent(String section) {
        currentSection = section;
        CardLayout layout = (CardLayout) contentPanel.getLayout();
        layout.show(contentPanel, section);
        updateMenuSelection(section);
    }
    
    private void updateMenuSelection(String selectedSection) {
        Component[] components = sidePanel.getComponents();
        
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] nestedComps = ((JPanel) comp).getComponents();
                for (Component nestedComp : nestedComps) {
                    if (nestedComp instanceof JButton) {
                        updateButtonSelection((JButton) nestedComp, selectedSection);
                    }
                }
            } else if (comp instanceof JButton) {
                updateButtonSelection((JButton) comp, selectedSection);
            }
        }
        
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    private void updateButtonSelection(JButton btn, String selectedSection) {
        String btnName = btn.getName();
        
        if (btnName == null || btnName.equals("Logout")) {
            return;
        }
        
        boolean isSelected = btnName.equals(selectedSection);
        
        if (isSelected) {
            btn.setOpaque(true);
            btn.setBackground(PLUM);
            btn.setForeground(WHITE);
        } else {
            btn.setOpaque(false);
            btn.setForeground(PLUM);
            btn.setBackground(null);
            btn.setContentAreaFilled(false);
        }
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Title
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleContainer.setBackground(LIGHT_BG);
        titleContainer.setMaximumSize(new Dimension(800, 50));
        titleContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Profile Information");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setForeground(PLUM);
        titleContainer.add(titleLabel);
        
        panel.add(titleContainer);
        panel.add(Box.createVerticalStrut(30));
        
        // Form panel - increased width
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));
        formPanel.setMaximumSize(new Dimension(900, 650)); // Increased width
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0; // Allow components to stretch
        
        // First Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("First Name *"), gbc);
        
        gbc.gridx = 1;
        JTextField firstNameField = createTextField(currentUser.getFirstName());
        firstNameField.setPreferredSize(new Dimension(300, 45)); // Larger size
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 2;
        formPanel.add(createLabel("Last Name *"), gbc);
        
        gbc.gridx = 3;
        JTextField lastNameField = createTextField(currentUser.getLastName());
        lastNameField.setPreferredSize(new Dimension(300, 45)); // Larger size
        formPanel.add(lastNameField, gbc);
        
        // Email (Read-only)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(createLabel("Email Address *"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        JTextField emailField = createTextField(currentUser.getEmail());
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        emailField.setPreferredSize(new Dimension(650, 45)); // Full width
        formPanel.add(emailField, gbc);
        
        // Phone Number
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(createLabel("Phone Number *"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        JTextField phoneField = createTextField(currentUser.getPhone());
        phoneField.setPreferredSize(new Dimension(650, 45)); // Full width
        formPanel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(createLabel("Street Address *"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        JTextArea addressArea = new JTextArea(currentUser.getAddress(), 3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setPreferredSize(new Dimension(650, 80)); // Full width
        formPanel.add(addressScroll, gbc);
        
        // City
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        formPanel.add(createLabel("City *"), gbc);
        
        gbc.gridx = 1;
        JTextField cityField = createTextField(currentUser.getCity());
        cityField.setPreferredSize(new Dimension(300, 45)); // Larger size
        formPanel.add(cityField, gbc);
        
        // Country
        gbc.gridx = 2;
        formPanel.add(createLabel("Country *"), gbc);
        
        gbc.gridx = 3;
        JTextField countryField = createTextField(currentUser.getCountry());
        countryField.setPreferredSize(new Dimension(300, 45)); // Larger size
        formPanel.add(countryField, gbc);
        
        // Update button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton updateButton = new JButton("Update Profile");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        updateButton.setBackground(PLUM);
        updateButton.setForeground(WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorderPainted(false);
        updateButton.setPreferredSize(new Dimension(200, 45));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateButton.setBackground(new Color(70, 0, 50));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                updateButton.setBackground(PLUM);
            }
        });
        
        updateButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressArea.getText().trim();
            String city = cityField.getText().trim();
            String country = countryField.getText().trim();
            
            if (userModel.updateUser(currentUser.getEmail(), firstName, lastName, phone, address, city, country)) {
                JOptionPane.showMessageDialog(panel, 
                    "Profile updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                currentUser = userModel.getCurrentUser();
                updateSidePanelInfo();
                updatePanel();
            } else {
                JOptionPane.showMessageDialog(panel, 
                    "Failed to update profile.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(updateButton, gbc);
        
        panel.add(formPanel);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private void updateSidePanelInfo() {
        if (nameLabel != null) {
            nameLabel.setText(currentUser.getFullName());
        }
        if (emailLabel != null) {
            emailLabel.setText(currentUser.getEmail());
        }
        sidePanel.revalidate();
        sidePanel.repaint();
    }
    
    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Title with Refresh button
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(LIGHT_BG);
        titleContainer.setMaximumSize(new Dimension(1200, 50));
        titleContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("My Reservations");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setForeground(PLUM);
        
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.setBackground(PLUM);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setPreferredSize(new Dimension(120, 35));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        titleContainer.add(titleLabel, BorderLayout.WEST);
        titleContainer.add(refreshButton, BorderLayout.EAST);
        
        panel.add(titleContainer);
        panel.add(Box.createVerticalStrut(10));
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(LIGHT_BG);
        statsPanel.setMaximumSize(new Dimension(1200, 100));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        List<AdminModel.BookingRecord> allBookings = adminModel.getAllBookings();
        int userBookings = 0;
        double totalSpent = 0;
        String userEmail = currentUser.getEmail();

        for (AdminModel.BookingRecord booking : allBookings) {
            if (booking.getEmail() != null && 
                booking.getEmail().equalsIgnoreCase(userEmail)) {
                userBookings++;
                totalSpent += booking.getTotalAmount();
            }
        }
        
        
        statsPanel.add(createStatCard("Total Bookings", String.valueOf(userBookings), PLUM));
        statsPanel.add(createStatCard("Total Spent", String.format("₱%,.2f", totalSpent), new Color(0, 128, 0)));
        statsPanel.add(createStatCard("Member Since", currentUser.getFormattedRegistrationDate(), GOLD));
        
        panel.add(statsPanel);
        panel.add(Box.createVerticalStrut(30));
        
        // Reservations list container
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        contentPanel.setMaximumSize(new Dimension(1200, Integer.MAX_VALUE));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Populate bookings
        populateBookings(contentPanel, allBookings, userEmail);
        
        // Refresh button action
        refreshButton.addActionListener(e -> {
            contentPanel.removeAll();
            List<AdminModel.BookingRecord> refreshedBookings = adminModel.getAllBookings();
            populateBookings(contentPanel, refreshedBookings, userEmail);
            contentPanel.revalidate();
            contentPanel.repaint();
            JOptionPane.showMessageDialog(panel, 
                "Reservations refreshed!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        panel.add(contentPanel);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        
        return card;
    }

    // Helper method to populate bookings
    private void populateBookings(JPanel contentPanel, List<AdminModel.BookingRecord> allBookings, String userEmail) {
        boolean hasBookings = false;
        
        // Create a list to store user's bookings
        List<AdminModel.BookingRecord> userBookings = new ArrayList<>();
        
        // Filter bookings for current user
        for (AdminModel.BookingRecord booking : allBookings) {
            if (booking.getEmail() != null && 
                booking.getEmail().equalsIgnoreCase(userEmail)) {
                userBookings.add(booking);
                hasBookings = true;
            }
        }
        
        // Sort bookings by booking date (newest first)
        userBookings.sort((b1, b2) -> {
            Date date1 = b1.getBookingDate();
            Date date2 = b2.getBookingDate();
            // Sort in descending order (newest first)
            return date2.compareTo(date1);
        });
        
        // Display sorted bookings
        for (AdminModel.BookingRecord booking : userBookings) {
            contentPanel.add(createBookingCardWithStatus(booking));
            contentPanel.add(Box.createVerticalStrut(15));
        }
        
        if (!hasBookings) {
            JLabel placeholderLabel = new JLabel("No reservations found.");
            placeholderLabel.setFont(new Font("Georgia", Font.ITALIC, 18));
            placeholderLabel.setForeground(Color.GRAY);
            placeholderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel instructionLabel = new JLabel("Your booking history will appear here after making a reservation.");
            instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            instructionLabel.setForeground(Color.GRAY);
            instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(WHITE);
            emptyPanel.add(Box.createVerticalGlue());
            emptyPanel.add(placeholderLabel);
            emptyPanel.add(Box.createVerticalStrut(10));
            emptyPanel.add(instructionLabel);
            emptyPanel.add(Box.createVerticalGlue());
            
            contentPanel.add(emptyPanel);
        }
    }

    private JPanel createBookingCardWithStatus(AdminModel.BookingRecord booking) {
        JPanel card = new JPanel(new BorderLayout(25, 0));
        card.setBackground(WHITE);
        
        // Status-based border color
        String status = booking.getStatus() != null ? booking.getStatus() : "Pending";
        Color borderColor = getStatusColor(status);
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 3),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        // Main content panel - LEFT ALIGNED
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Status badge
        JPanel statusRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusRow.setOpaque(false);
        statusRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel statusLabel = new JLabel(" " + status + " ");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(borderColor);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        
        statusRow.add(statusLabel);
        contentPanel.add(statusRow);
        contentPanel.add(Box.createVerticalStrut(12));
        
        // Room Type(s) - Parse and display
        String roomType = booking.getRoomType();
        List<String> roomList = parseRoomTypes(roomType);
        int roomCount = roomList.size();
        
        // Room information panel
        JPanel roomInfoPanel = new JPanel();
        roomInfoPanel.setLayout(new BoxLayout(roomInfoPanel, BoxLayout.Y_AXIS));
        roomInfoPanel.setOpaque(false);
        roomInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (roomCount > 1) {
            // Multiple rooms header
            JLabel mainRoomLabel = new JLabel("Multiple Rooms (" + roomCount + " rooms)");
            mainRoomLabel.setFont(new Font("Georgia", Font.BOLD, 20));
            mainRoomLabel.setForeground(PLUM);
            mainRoomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            roomInfoPanel.add(mainRoomLabel);
            roomInfoPanel.add(Box.createVerticalStrut(10));
            
            // Room list with bullets (showing first 2-3 rooms)
            int displayCount = Math.min(3, roomList.size());
            for (int i = 0; i < displayCount; i++) {
                JPanel roomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
                roomRow.setOpaque(false);
                roomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel bulletLabel = new JLabel("• ");
                bulletLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                bulletLabel.setForeground(GOLD);
                
                JLabel roomLabel = new JLabel(roomList.get(i));
                roomLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
                roomLabel.setForeground(new Color(60, 60, 60));
                
                roomRow.add(bulletLabel);
                roomRow.add(roomLabel);
                roomInfoPanel.add(roomRow);
            }
            
            // Show "and X more" if there are more than 3 rooms
            if (roomList.size() > 3) {
                JPanel moreRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
                moreRow.setOpaque(false);
                moreRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel moreLabel = new JLabel("...and " + (roomList.size() - 3) + " more");
                moreLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
                moreLabel.setForeground(new Color(100, 100, 100));
                
                moreRow.add(moreLabel);
                roomInfoPanel.add(moreRow);
            }
        } else if (roomCount == 1) {
            // Single room
            JLabel roomLabel = new JLabel(roomList.get(0));
            roomLabel.setFont(new Font("Georgia", Font.BOLD, 20));
            roomLabel.setForeground(PLUM);
            roomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            roomInfoPanel.add(roomLabel);
        } else {
            // No room info
            JLabel roomLabel = new JLabel("Room Information Not Available");
            roomLabel.setFont(new Font("Georgia", Font.ITALIC, 16));
            roomLabel.setForeground(Color.GRAY);
            roomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            roomInfoPanel.add(roomLabel);
        }
        
        contentPanel.add(roomInfoPanel);
        contentPanel.add(Box.createVerticalStrut(12));
        
        // Receipt and booking details - LEFT ALIGNED
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Receipt number
        JPanel receiptRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        receiptRow.setOpaque(false);
        receiptRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel receiptLabel = new JLabel("Receipt: " + booking.getReceiptNumber());
        receiptLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        receiptLabel.setForeground(new Color(80, 80, 80));
        receiptRow.add(receiptLabel);
        
        detailsPanel.add(receiptRow);
        detailsPanel.add(Box.createVerticalStrut(6));
        
        // Check-in and Check-out dates
        JPanel datesRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datesRow.setOpaque(false);
        datesRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel datesLabel = new JLabel("Check-in: " + booking.getCheckInDate() + "  •  Check-out: " + booking.getCheckOutDate());
        datesLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        datesLabel.setForeground(new Color(100, 100, 100));
        datesRow.add(datesLabel);
        
        detailsPanel.add(datesRow);
        detailsPanel.add(Box.createVerticalStrut(6));
        
        // Booking date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        JPanel bookingDateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bookingDateRow.setOpaque(false);
        bookingDateRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel bookingDateLabel = new JLabel("Booked on: " + sdf.format(booking.getBookingDate()));
        bookingDateLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        bookingDateLabel.setForeground(new Color(120, 120, 120));
        bookingDateRow.add(bookingDateLabel);
        
        detailsPanel.add(bookingDateRow);
        detailsPanel.add(Box.createVerticalStrut(6));
        
        // Payment method and amount
        JPanel paymentRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        paymentRow.setOpaque(false);
        paymentRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel paymentLabel = new JLabel("Payment: " + booking.getPaymentMethod() + "  •  ");
        paymentLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        paymentLabel.setForeground(new Color(100, 100, 100));
        
        JLabel amountLabel = new JLabel(String.format("₱%,.2f", booking.getTotalAmount()));
        amountLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        amountLabel.setForeground(new Color(0, 128, 0));
        
        paymentRow.add(paymentLabel);
        paymentRow.add(amountLabel);
        
        detailsPanel.add(paymentRow);
        
        contentPanel.add(detailsPanel);
        
        // Right panel - Action buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setOpaque(false);
        
        // Add vertical glue to center the buttons
        actionPanel.add(Box.createVerticalGlue());
        
        // See Details button - always visible
        JButton detailsButton = new JButton("📋 See Details");
        detailsButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        detailsButton.setBackground(PLUM);
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.setBorderPainted(false);
        detailsButton.setPreferredSize(new Dimension(150, 38));
        detailsButton.setMaximumSize(new Dimension(150, 38));
        detailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        detailsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                detailsButton.setBackground(new Color(0, 90, 140));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                detailsButton.setBackground(PLUM);
            }
        });
        
        detailsButton.addActionListener(e -> showBookingDetailsDialog(booking));
        
        actionPanel.add(detailsButton);
        actionPanel.add(Box.createVerticalStrut(10));
        
        // Cancel button - only show for Pending status
        if ("Pending".equalsIgnoreCase(status)) {
            JButton cancelButton = new JButton("Cancel Booking");
            cancelButton.setFont(new Font("SansSerif", Font.BOLD, 13));
            cancelButton.setBackground(new Color(220, 53, 69));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);
            cancelButton.setBorderPainted(false);
            cancelButton.setPreferredSize(new Dimension(150, 38));
            cancelButton.setMaximumSize(new Dimension(150, 38));
            cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            cancelButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cancelButton.setBackground(new Color(180, 40, 50));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    cancelButton.setBackground(new Color(220, 53, 69));
                }
            });
            
            cancelButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "Are you sure you want to cancel this booking?\n\n" +
                    "Receipt: " + booking.getReceiptNumber() + "\n" +
                    "Room: " + booking.getRoomType(),
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (adminModel.updateBookingStatus(booking.getReceiptNumber(), "Cancelled")) {
                        JOptionPane.showMessageDialog(
                            parentFrame,
                            "Booking cancelled successfully!",
                            "Cancellation Successful",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        updatePanel();
                    } else {
                        JOptionPane.showMessageDialog(
                            parentFrame,
                            "Failed to cancel booking. Please contact support.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            });
            
            actionPanel.add(cancelButton);
        } else {
            // Status indicator for non-pending bookings
            JLabel statusIndicator = new JLabel();
            statusIndicator.setFont(new Font("SansSerif", Font.BOLD, 15));
            statusIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            switch (status.toLowerCase()) {
                case "confirmed":
                    statusIndicator.setText("✓ Confirmed");
                    statusIndicator.setForeground(SUCCESS_GREEN);
                    break;
                case "completed":
                    statusIndicator.setText("✓ Completed");
                    statusIndicator.setForeground(new Color(59, 130, 246));
                    break;
                case "cancelled":
                    statusIndicator.setText("✗ Cancelled");
                    statusIndicator.setForeground(new Color(220, 53, 69));
                    break;
            }
            
            actionPanel.add(statusIndicator);
        }
        
        actionPanel.add(Box.createVerticalGlue());
        
        // Set card size
        int baseHeight = 200 + (Math.max(0, Math.min(roomCount, 3) - 1) * 25);
        card.setMaximumSize(new Dimension(1150, baseHeight));
        card.setPreferredSize(new Dimension(1150, baseHeight));
        
        // Add panels to card
        card.add(contentPanel, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);
        
        return card;
    }

    // NEW METHOD: Show detailed booking information in a popup dialog
    private void showBookingDetailsDialog(AdminModel.BookingRecord booking) {
        JDialog dialog = new JDialog(parentFrame, "Booking Details - " + booking.getReceiptNumber(), true);
        dialog.setLayout(new BorderLayout());
        
        // Main container
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(WHITE);
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PLUM);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel headerTitle = new JLabel("Booking Receipt");
        headerTitle.setFont(new Font("Georgia", Font.BOLD, 24));
        headerTitle.setForeground(WHITE);
        
        String status = booking.getStatus() != null ? booking.getStatus() : "Pending";
        JLabel statusBadge = new JLabel(" " + status + " ");
        statusBadge.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusBadge.setForeground(WHITE);
        statusBadge.setOpaque(true);
        statusBadge.setBackground(getStatusColor(status));
        statusBadge.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        headerPanel.add(headerTitle, BorderLayout.WEST);
        headerPanel.add(statusBadge, BorderLayout.EAST);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Receipt Number Section
        JPanel receiptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        receiptPanel.setBackground(WHITE);
        receiptPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel receiptLabel = new JLabel("Receipt Number: ");
        receiptLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        receiptLabel.setForeground(new Color(100, 100, 100));
        
        JLabel receiptValue = new JLabel(booking.getReceiptNumber());
        receiptValue.setFont(new Font("Courier New", Font.BOLD, 18));
        receiptValue.setForeground(PLUM);
        
        receiptPanel.add(receiptLabel);
        receiptPanel.add(receiptValue);
        contentPanel.add(receiptPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Guest Information Section
        contentPanel.add(createInfoSection("Guest Information", new String[][]{
            {"Name:", booking.getGuestName()},
            {"Email:", booking.getEmail()},
            {"Phone:", currentUser.getPhone()}
        }));
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Reservation Details Section
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        contentPanel.add(createInfoSection("Reservation Details", new String[][]{
            {"Check-in Date:", booking.getCheckInDate()},
            {"Check-out Date:", booking.getCheckOutDate()},
            {"Booking Date:", sdf.format(booking.getBookingDate())}
        }));
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Room Details Section
        List<String> roomList = parseRoomTypes(booking.getRoomType());
        String[][] roomInfo;
        
        if (roomList.size() > 1) {
            // Count each room type
            Map<String, Integer> roomCounts = new HashMap<>();
            for (String room : roomList) {
                roomCounts.put(room, roomCounts.getOrDefault(room, 0) + 1);
            }
            
            // Create room info array
            roomInfo = new String[roomCounts.size() + 1][2];
            roomInfo[0] = new String[]{"Total Rooms:", String.valueOf(roomList.size())};
            
            int idx = 1;
            for (Map.Entry<String, Integer> entry : roomCounts.entrySet()) {
                roomInfo[idx++] = new String[]{
                    "  • " + entry.getKey() + ":", 
                    "Qty: " + entry.getValue()
                };
            }
        } else {
            roomInfo = new String[][]{
                {"Room Type:", roomList.isEmpty() ? "N/A" : roomList.get(0)},
                {"Quantity:", "1"}
            };
        }
        
        contentPanel.add(createInfoSection("Room Details", roomInfo));
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Payment Information Section
        double totalAmount = booking.getTotalAmount();
        double subtotal = totalAmount / 1.12;
        double tax = totalAmount - subtotal;
        
        contentPanel.add(createInfoSection("Payment Information", new String[][]{
            {"Payment Method:", booking.getPaymentMethod()},
            {"Subtotal:", String.format("₱%,.2f", subtotal)},
            {"Tax (12%):", String.format("₱%,.2f", tax)},
            {"Total Amount:", String.format("₱%,.2f", totalAmount)}
        }));
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Close Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 0, 15, 0)
        ));
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeButton.setBackground(PLUM);
        closeButton.setForeground(WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(120, 40));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(0, 90, 140));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(PLUM);
            }
        });
        
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        // Add all panels to container
        container.add(headerPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add container to dialog
        dialog.add(container);
        dialog.setSize(700, 650);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    // Helper method to create information sections
    private JPanel createInfoSection(String title, String[][] data) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(WHITE);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Section Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(12));
        
        // Section Data
        for (String[] row : data) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
            rowPanel.setBackground(WHITE);
            rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel label = new JLabel(row[0]);
            label.setFont(new Font("SansSerif", Font.PLAIN, 13));
            label.setForeground(new Color(80, 80, 80));
            label.setPreferredSize(new Dimension(150, 20));
            
            JLabel value = new JLabel(row[1] != null ? row[1] : "N/A");
            
            // Special formatting for total amount
            if (row[0].contains("Total Amount")) {
                value.setFont(new Font("Georgia", Font.BOLD, 16));
                value.setForeground(SUCCESS_GREEN);
            } else {
                value.setFont(new Font("SansSerif", Font.BOLD, 13));
                value.setForeground(new Color(40, 40, 40));
            }
            
            rowPanel.add(label);
            rowPanel.add(value);
            section.add(rowPanel);
        }
        
        return section;
    }

    // Helper method to parse room types with multiple delimiter support
    private List<String> parseRoomTypes(String roomType) {
        List<String> rooms = new ArrayList<>();
        
        if (roomType == null || roomType.trim().isEmpty()) {
            return rooms;
        }
        
        // Handle multiplier format: "Classic Room (x3)" -> ["Classic Room", "Classic Room", "Classic Room"]
        if (roomType.contains("(x") && roomType.contains(")")) {
            try {
                int startIdx = roomType.indexOf("(x") + 2;
                int endIdx = roomType.indexOf(")", startIdx);
                int count = Integer.parseInt(roomType.substring(startIdx, endIdx).trim());
                String baseRoom = roomType.substring(0, roomType.indexOf("(x")).trim();
                
                for (int i = 0; i < count; i++) {
                    rooms.add(baseRoom);
                }
                return rooms;
            } catch (Exception e) {
                // If parsing fails, fall through to normal parsing
            }
        }
        
        // Try different delimiters
        String[] candidates = null;
        
        if (roomType.contains(",")) {
            candidates = roomType.split(",");
        } else if (roomType.contains(";")) {
            candidates = roomType.split(";");
        } else if (roomType.contains("|")) {
            candidates = roomType.split("\\|");
        } else if (roomType.contains("\n")) {
            candidates = roomType.split("\n");
        } else if (roomType.toLowerCase().contains(" and ")) {
            candidates = roomType.split("(?i)\\s+and\\s+");
        } else {
            // Single room
            candidates = new String[]{roomType};
        }
        
        // Clean up and add to list
        for (String room : candidates) {
            String cleaned = room.trim();
            if (!cleaned.isEmpty()) {
                rooms.add(cleaned);
            }
        }
        
        return rooms;
    }

    // Helper method to get status color
    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return WARNING_ORANGE;
            case "confirmed":
                return SUCCESS_GREEN;
            case "completed":
                return new Color(59, 130, 246); // Blue
            case "cancelled":
                return new Color(220, 53, 69); // Red
            default:
                return GOLD;
        }
    }
    
    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Title
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleContainer.setBackground(LIGHT_BG);
        titleContainer.setMaximumSize(new Dimension(800, 50)); // Increased width
        titleContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Security Settings");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setForeground(PLUM);
        titleContainer.add(titleLabel);
        
        panel.add(titleContainer);
        panel.add(Box.createVerticalStrut(30));
        
        // Change password form - increased width
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));
        formPanel.setMaximumSize(new Dimension(800, 500)); // Increased width
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.weightx = 1.0; // Allow stretching
 
     // Current Password
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Current Password *"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;

        JPanel currentPassWrapper = new JPanel(new BorderLayout(0, 0));
        currentPassWrapper.setBackground(WHITE);
        currentPassWrapper.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        currentPassWrapper.setPreferredSize(new Dimension(500, 45));
        currentPassWrapper.setMaximumSize(new Dimension(500, 45));
        currentPassWrapper.setMinimumSize(new Dimension(500, 45));

        JPasswordField currentPassField = new JPasswordField();
        currentPassField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        currentPassField.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        currentPassField.setBackground(WHITE);

        currentPassWrapper.add(currentPassField, BorderLayout.CENTER);
        currentPassWrapper.add(createPasswordToggleButton(currentPassField), BorderLayout.EAST);

        formPanel.add(currentPassWrapper, gbc);
        
     // New Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(createLabel("New Password *"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        
        // Create wrapper panel with border
        JPanel newPassWrapper = new JPanel(new BorderLayout(0, 0));
        newPassWrapper.setBackground(WHITE);
        newPassWrapper.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        newPassWrapper.setPreferredSize(new Dimension(500, 45));
        newPassWrapper.setMaximumSize(new Dimension(500, 45));
        
        // Password field without border
        JPasswordField newPassField = new JPasswordField();
        newPassField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        newPassField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        newPassField.setBackground(WHITE);
        
        // Add components
        newPassWrapper.add(newPassField, BorderLayout.CENTER);
        newPassWrapper.add(createPasswordToggleButton(newPassField), BorderLayout.EAST);
        
        formPanel.add(newPassWrapper, gbc);
        
        // Password strength indicator
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        JPanel strengthPanel = createPasswordStrengthPanel(newPassField);
        formPanel.add(strengthPanel, gbc);
        
     // Confirm Password
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(createLabel("Confirm Password *"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        
        // Create wrapper panel with border
        JPanel confirmPassWrapper = new JPanel(new BorderLayout(0, 0));
        confirmPassWrapper.setBackground(WHITE);
        confirmPassWrapper.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        confirmPassWrapper.setPreferredSize(new Dimension(500, 45));
        confirmPassWrapper.setMaximumSize(new Dimension(500, 45));
        
        // Password field without border
        JPasswordField confirmPassField = new JPasswordField();
        confirmPassField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        confirmPassField.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        confirmPassField.setBackground(WHITE);
        
        // Add components
        confirmPassWrapper.add(confirmPassField, BorderLayout.CENTER);
        confirmPassWrapper.add(createPasswordToggleButton(confirmPassField), BorderLayout.EAST);
        
        formPanel.add(confirmPassWrapper, gbc);
        
        // Password match indicator
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        JLabel passwordMatchLabel = new JLabel("✓ Passwords match");
        passwordMatchLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordMatchLabel.setForeground(SUCCESS_GREEN);
        passwordMatchLabel.setVisible(false);
        formPanel.add(passwordMatchLabel, gbc);
        
        // Password requirements
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        JPanel requirementsPanel = createPasswordRequirementsPanel();
        formPanel.add(requirementsPanel, gbc);
        
        // Update button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton updateButton = new JButton("Update Password");
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        updateButton.setBackground(PLUM);
        updateButton.setForeground(WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorderPainted(false);
        updateButton.setPreferredSize(new Dimension(200, 45));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateButton.setBackground(new Color(70, 0, 50));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                updateButton.setBackground(PLUM);
            }
        });
        
        updateButton.addActionListener(e -> {
            String current = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirm = new String(confirmPassField.getPassword());
            
            if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(panel, 
                    "Please fill in all password fields.", 
                    "Missing Information", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!isStrongPassword(newPass)) {
                JOptionPane.showMessageDialog(panel, 
                    "New password does not meet the requirements:\n\n" +
                    "• At least 8 characters\n" +
                    "• At least one uppercase letter (A-Z)\n" +
                    "• At least one lowercase letter (a-z)\n" +
                    "• At least one number (0-9)\n" +
                    "• At least one special character (!@#$%^&* etc.)", 
                    "Weak Password", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!newPass.equals(confirm)) {
                JOptionPane.showMessageDialog(panel, 
                    "New passwords do not match!", 
                    "Password Mismatch", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (userModel.updatePassword(currentUser.getEmail(), current, newPass)) {
                JOptionPane.showMessageDialog(panel, 
                    "Password updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                currentPassField.setText("");
                newPassField.setText("");
                confirmPassField.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, 
                    "Current password is incorrect.", 
                    "Authentication Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(updateButton, gbc);
        
        addPasswordValidationListeners(newPassField, confirmPassField, passwordMatchLabel);
        
        panel.add(formPanel);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private JPanel createPasswordStrengthPanel(JPasswordField passwordField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(WHITE);
        
        JLabel strengthTextLabel = new JLabel("Strength:");
        strengthTextLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JProgressBar passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setForeground(ERROR_RED);
        passwordStrengthBar.setBackground(new Color(240, 240, 240));
        passwordStrengthBar.setPreferredSize(new Dimension(120, 12)); // Slightly larger
        passwordStrengthBar.setStringPainted(false);
        
        JLabel passwordStrengthLabel = new JLabel("Weak");
        passwordStrengthLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        passwordStrengthLabel.setForeground(ERROR_RED);
        
        panel.add(strengthTextLabel);
        panel.add(passwordStrengthBar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(passwordStrengthLabel);
        
        passwordField.putClientProperty("strengthBar", passwordStrengthBar);
        passwordField.putClientProperty("strengthLabel", passwordStrengthLabel);
        
        return panel;
    }
    
    private JPanel createPasswordRequirementsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel reqTitle = new JLabel("Password Requirements:");
        reqTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        reqTitle.setForeground(PLUM);
        reqTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel req1 = new JLabel("• At least 8 characters");
        req1.setFont(new Font("SansSerif", Font.PLAIN, 12));
        req1.setForeground(Color.GRAY);
        req1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel req2 = new JLabel("• At least one uppercase letter (A-Z)");
        req2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        req2.setForeground(Color.GRAY);
        req2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel req3 = new JLabel("• At least one lowercase letter (a-z)");
        req3.setFont(new Font("SansSerif", Font.PLAIN, 12));
        req3.setForeground(Color.GRAY);
        req3.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel req4 = new JLabel("• At least one number (0-9)");
        req4.setFont(new Font("SansSerif", Font.PLAIN, 12));
        req4.setForeground(Color.GRAY);
        req4.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel req5 = new JLabel("• At least one special character (!@#$%^&* etc.)");
        req5.setFont(new Font("SansSerif", Font.PLAIN, 12));
        req5.setForeground(Color.GRAY);
        req5.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(reqTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(req1);
        panel.add(req2);
        panel.add(req3);
        panel.add(req4);
        panel.add(req5);
        
        return panel;
    }
    
    private void addPasswordValidationListeners(JPasswordField newPassField, JPasswordField confirmPassField, 
            JLabel matchLabel) {
        KeyAdapter validationListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String newPass = new String(newPassField.getPassword());
                String confirmPass = new String(confirmPassField.getPassword());

                updatePasswordStrength(newPassField, newPass);

                if (confirmPass.isEmpty()) {
                    matchLabel.setVisible(false);
                } else if (newPass.equals(confirmPass)) {
                    matchLabel.setText("✓ Passwords match");
                    matchLabel.setForeground(SUCCESS_GREEN);
                    matchLabel.setVisible(true);
                } else {
                    matchLabel.setText("✗ Passwords do not match");
                    matchLabel.setForeground(ERROR_RED);
                    matchLabel.setVisible(true);
                }
            }
        };

        newPassField.addKeyListener(validationListener);
        confirmPassField.addKeyListener(validationListener);
    }
    
    
    
    private void updatePasswordStrength(JPasswordField passwordField, String password) {
        JProgressBar strengthBar = (JProgressBar) passwordField.getClientProperty("strengthBar");
        JLabel strengthLabel = (JLabel) passwordField.getClientProperty("strengthLabel");
        
        if (strengthBar == null || strengthLabel == null) return;
        
        int strength = calculatePasswordStrength(password);
        strengthBar.setValue(strength);
        
        if (strength < 25) {
            strengthBar.setForeground(ERROR_RED);
            strengthLabel.setText("Weak");
            strengthLabel.setForeground(ERROR_RED);
        } else if (strength < 50) {
            strengthBar.setForeground(WARNING_ORANGE);
            strengthLabel.setText("Fair");
            strengthLabel.setForeground(WARNING_ORANGE);
        } else if (strength < 75) {
            strengthBar.setForeground(new Color(255, 200, 0));
            strengthLabel.setText("Good");
            strengthLabel.setForeground(new Color(255, 200, 0));
        } else {
            strengthBar.setForeground(SUCCESS_GREEN);
            strengthLabel.setText("Strong");
            strengthLabel.setForeground(SUCCESS_GREEN);
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
        if (password.matches(".*\\d.*")) strength += 15;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength += 15;
        
        if (password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")) strength += 10;
        if (password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength += 10;
        
        return Math.min(strength, 100);
    }
    
    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*\\d.*") &&
               password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
    
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(PLUM);
        return label;
    }
    
    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text, 20); // Increased columns
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 45)); // Larger default size
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(parentFrame,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            userModel.logout();
            JOptionPane.showMessageDialog(parentFrame,
                "You have been logged out successfully.",
                "Logout Successful",
                JOptionPane.INFORMATION_MESSAGE);
            showLoginScreenAfterLogout();
        }
    }

    private void showLoginScreenAfterLogout() {
        Window window = SwingUtilities.getWindowAncestor(panel);
        if (window != null) {
            window.dispose();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(parentFrame, true);
            loginView.setVisible(true);
            
            if (loginView.isLoginSuccessful()) {
                refreshApplicationAfterLogin();
            } else {
                System.exit(0);
            }
        });
    }

    private void refreshApplicationAfterLogin() {
        currentUser = userModel.getCurrentUser();
        
        updateSidePanelInfo();
        
        if (onBackCallback != null) {
            onBackCallback.run();
        }
    }

    // Add this helper method to create status badges
    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel(" " + status + " ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 12));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        switch (status) {
            case "Pending":
                badge.setBackground(new Color(255, 243, 205));
                badge.setForeground(WARNING_ORANGE);
                break;
            case "Confirmed":
                badge.setBackground(new Color(209, 250, 229));
                badge.setForeground(SUCCESS_GREEN);
                break;
            case "Completed":
                badge.setBackground(new Color(219, 234, 254));
                badge.setForeground(new Color(59, 130, 246));
                break;
            case "Cancelled":
                badge.setBackground(new Color(254, 226, 226));
                badge.setForeground(ERROR_RED);
                break;
            default:
                badge.setBackground(new Color(243, 244, 246));
                badge.setForeground(new Color(107, 114, 128));
        }
        
        return badge;
    }

    // Add this method to handle booking cancellation
    private void handleCancelBooking(AdminModel.BookingRecord booking) {
        int confirm = JOptionPane.showConfirmDialog(
            panel,
            "Are you sure you want to cancel this booking?\n\n" +
            "Receipt: " + booking.getReceiptNumber() + "\n" +
            "Room: " + booking.getRoomType() + "\n" +
            "Check-in: " + booking.getCheckInDate() + "\n\n" +
            "This action cannot be undone.",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (adminModel.updateBookingStatus(booking.getReceiptNumber(), "Cancelled")) {
                JOptionPane.showMessageDialog(
                    panel,
                    "Booking cancelled successfully!",
                    "Cancellation Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );
                updatePanel(); // Refresh the reservations panel
            } else {
                JOptionPane.showMessageDialog(
                    panel,
                    "Failed to cancel booking. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    @Override
    public void updatePanel() {
        if (panel != null) {
            currentUser = userModel.getCurrentUser();
            updateSidePanelInfo();
            
            JPanel newPanel = createPanel();
            
            Container parent = panel.getParent();
            if (parent != null) {
                parent.remove(panel);
                parent.add(newPanel, BorderLayout.CENTER);
                parent.revalidate();
                parent.repaint();
            }
            
            this.panel = newPanel;
            
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        }
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
        toggleButton.setMinimumSize(new Dimension(40, 45));
        toggleButton.setMaximumSize(new Dimension(40, 45));
        
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
            @Override
            public void mouseEntered(MouseEvent e) {
                toggleButton.setForeground(PLUM);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                toggleButton.setForeground(new Color(120, 120, 120));
            }
        });
        
        return toggleButton;
    }
}