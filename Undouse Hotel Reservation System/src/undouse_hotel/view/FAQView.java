package undouse_hotel.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FAQView {
    private static final Color PLUM = new Color(90, 0, 60);
    private static final Color GOLD = new Color(230, 180, 120);
    private static final Color LIGHT_BG = new Color(255, 248, 240);
    private static final Color ACCENT_PURPLE = new Color(147, 51, 234);
    private static final Color LIGHT_PURPLE = new Color(245, 240, 255);
    
    public static void showFAQDialog(JFrame parentFrame) {
        JDialog faqDialog = new JDialog(parentFrame, "Frequently Asked Questions", true);
        faqDialog.setSize(850, 700);
        faqDialog.setLocationRelativeTo(parentFrame);
        faqDialog.setResizable(true);
        
        JPanel mainPanel = createFAQPanel();
        faqDialog.add(mainPanel);
        
        faqDialog.setVisible(true);
    }
    
    public static JPanel createFAQPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Enhanced Header
        JPanel headerPanel = createEnhancedHeader();
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        
        // FAQ Content with improved styling
        JPanel faqContent = createFAQContent();
        
        JScrollPane scrollPane = new JScrollPane(faqContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        ScrollBarStyleHelper.styleScrollPane(scrollPane);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private static JPanel createEnhancedHeader() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, PLUM,
                    getWidth(), getHeight(), new Color(120, 0, 80)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        headerPanel.setPreferredSize(new Dimension(0, 180));
        
        JLabel titleLabel = new JLabel("💡 Frequently Asked Questions");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Everything you need to know about Undouse Hotel");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel decorationPanel = new JPanel();
        decorationPanel.setOpaque(false);
        decorationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        decorationPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel decoration = new JLabel("✦ ✦ ✦");
        decoration.setFont(new Font("SansSerif", Font.PLAIN, 18));
        decoration.setForeground(GOLD);
        
        decorationPanel.add(decoration);
        
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        headerPanel.add(decorationPanel);
        headerPanel.add(Box.createVerticalGlue());
        
        return headerPanel;
    }
    
    private static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBackground(Color.WHITE);
        searchContainer.setPreferredSize(new Dimension(600, 50));
        searchContainer.setMaximumSize(new Dimension(600, 50));
        searchContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 5)
        ));
        
        JTextField searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBackground(Color.WHITE);
        
        searchField.putClientProperty("JTextField.placeholderText", "Search FAQs...");
        
        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchButton.setBackground(GOLD);
        searchButton.setForeground(PLUM);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchContainer.add(searchField, BorderLayout.CENTER);
        searchContainer.add(searchButton, BorderLayout.EAST);
        
        searchPanel.add(searchContainer);
        
        return searchPanel;
    }
    
    private static JPanel createFAQContent() {
        JPanel faqContent = new JPanel();
        faqContent.setLayout(new BoxLayout(faqContent, BoxLayout.Y_AXIS));
        faqContent.setBackground(Color.WHITE);
        faqContent.setBorder(BorderFactory.createEmptyBorder(20, 30, 40, 30));
        
        addFAQCategory(faqContent, "🏨 General Information");
        addEnhancedFAQItem(faqContent, "What is Undouse Hotel?", 
            "Undouse Hotel is a luxury hotel reservation system that allows you to book rooms online with ease. " +
            "We offer a variety of room types to suit your needs, from Classic Rooms to Presidential Suites.",
            "🏛️");
        
        addEnhancedFAQItem(faqContent, "What room types are available?", 
            "We offer 6 room types:\n" +
            "• Classic Room (2 guests)\n" +
            "• Standard Deluxe Room (2-4 guests)\n" +
            "• Premier Deluxe Room (4 guests)\n" +
            "• Executive Suite (5 guests)\n" +
            "• Family Suite (8-10 guests)\n" +
            "• Presidential Suite (10-15 guests)",
            "🛏️");
        
        addFAQCategory(faqContent, "📅 Booking Process");
        addEnhancedFAQItem(faqContent, "How do I make a booking?", 
            "1. Select your check-in and check-out dates from the Home page\n" +
            "2. Choose the number of guests and rooms\n" +
            "3. Click 'Search' to view available rooms\n" +
            "4. Select your preferred room(s)\n" +
            "5. Complete the payment form with accurate details\n" +
            "6. Confirm your booking and receive your receipt",
            "📝");
        
        addEnhancedFAQItem(faqContent, "Can I book multiple rooms?", 
            "Yes! You can book multiple rooms in a single reservation. Simply select the number of rooms " +
            "when choosing your guests, and you'll be able to select a room for each one.",
            "🔢");
        
        addEnhancedFAQItem(faqContent, "What information do I need to complete a booking?", 
            "You'll need:\n" +
            "• Personal details (name, email, phone)\n" +
            "• Complete address\n" +
            "• Payment method and account details\n" +
            "• Exact payment amount matching your booking total",
            "📋");
        
        addFAQCategory(faqContent, "💳 Payment");
        addEnhancedFAQItem(faqContent, "What payment methods do you accept?", 
            "We accept the following payment methods:\n" +
            "• GCash\n" +
            "• BDO\n" +
            "• PayMaya\n" +
            "• BPI\n" +
            "• Other bank transfers",
            "💵");
        
        addEnhancedFAQItem(faqContent, "Why must I enter the exact amount?", 
            "For security and accuracy, our system requires you to enter the exact booking amount. " +
            "This ensures that the payment matches your reservation total and helps prevent errors. " +
            "The required amount is displayed in the summary section on the right side of the payment form.",
            "🎯");
        
        addEnhancedFAQItem(faqContent, "Is my payment secure?", 
            "Yes, all payment information is processed securely. We require payment confirmation through " +
            "your chosen payment method's reference number to verify the transaction.",
            "🔒");
        
        addFAQCategory(faqContent, "🔄 Cancellation & Changes");
        addEnhancedFAQItem(faqContent, "Can I cancel my reservation?", 
            "This is a Book-and-Buy arrangement. Reservations are non-cancellable and non-refundable, " +
            "but may be rebooked subject to rate differences. Rebooking must be done at least 3 days before arrival.",
            "❌");
        
        addEnhancedFAQItem(faqContent, "What happens if I don't show up?", 
            "No-shows will result in full payment forfeiture. Please contact us in advance if you need " +
            "to rebook your reservation.",
            "⏰");
        
        addEnhancedFAQItem(faqContent, "Can I modify my booking dates?", 
            "Yes, you can rebook your reservation at least 3 days prior to your arrival date. " +
            "Rate differences may apply based on the new dates selected.",
            "📅");
        
        addFAQCategory(faqContent, "ℹ️ Additional Information");
        addEnhancedFAQItem(faqContent, "What amenities are included?", 
            "All rooms include:\n" +
            "• Free WiFi\n" +
            "• Free cancellation (with rebooking option)\n" +
            "• Good breakfast included\n" +
            "• Pay nothing until check-in\n" +
            "• Air conditioning\n" +
            "• Flat-screen TV\n" +
            "• Ensuite bathroom",
            "⭐");
        
        addEnhancedFAQItem(faqContent, "How can I contact customer support?", 
            "You can reach us at:\n" +
            "📧 Email: info@undousehotel.com\n" +
            "📞 Phone: +639916649798\n" +
            "📍 Address: New Seaside Drive, Quezon City",
            "📞");
        
        addEnhancedFAQItem(faqContent, "Where can I view room details?", 
            "You can view detailed room information in two ways:\n" +
            "1. From the Home page, click 'View Details' on any featured room\n" +
            "2. Navigate to the 'Rooms' page from the menu to browse all available rooms",
            "🔍");
        
        return faqContent;
    }
    
    private static void addFAQCategory(JPanel parent, String categoryName) {
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBackground(LIGHT_PURPLE);
        categoryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(25, 0, 15, 0),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        categoryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        categoryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        
        JLabel categoryLabel = new JLabel(categoryName);
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        categoryLabel.setForeground(ACCENT_PURPLE);
        
        categoryPanel.add(categoryLabel, BorderLayout.WEST);
        parent.add(categoryPanel);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private static void addEnhancedFAQItem(JPanel parent, String question, String answer, String icon) {
        JPanel faqPanel = new JPanel();
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));
        faqPanel.setBackground(Color.WHITE);
        faqPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        faqPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        questionPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JPanel questionContent = new JPanel(new BorderLayout(15, 0));
        questionContent.setBackground(Color.WHITE);
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        iconLabel.setPreferredSize(new Dimension(30, 30));
        
        JLabel questionLabel = new JLabel("<html><div style='font-size:14px; font-weight:bold; color:" + 
            colorToHex(PLUM) + ";'>" + question + "</div></html>");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel expandLabel = new JLabel("▶");
        expandLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        expandLabel.setForeground(ACCENT_PURPLE);
        expandLabel.setPreferredSize(new Dimension(20, 20));

        questionContent.add(iconLabel, BorderLayout.WEST);
        questionContent.add(questionLabel, BorderLayout.CENTER);
        questionContent.add(expandLabel, BorderLayout.EAST);
        
        questionPanel.add(questionContent, BorderLayout.CENTER);
        
        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBackground(new Color(250, 250, 255));
        answerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 240)),
            BorderFactory.createEmptyBorder(20, 45, 20, 20)  
        ));
        answerPanel.setVisible(false);
        
        JTextArea answerText = new JTextArea(answer);
        answerText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        answerText.setForeground(new Color(60, 60, 60));
        answerText.setLineWrap(true);
        answerText.setWrapStyleWord(true);
        answerText.setEditable(false);
        answerText.setOpaque(false);
        answerText.setFocusable(false);
        answerText.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        answerPanel.add(answerText, BorderLayout.CENTER);
        
        questionPanel.addMouseListener(new MouseAdapter() {
            private boolean isExpanded = false;
            
            @Override
            public void mouseClicked(MouseEvent e) {
                isExpanded = !isExpanded;
                answerPanel.setVisible(isExpanded);
                expandLabel.setText(isExpanded ? "▼" : "▶");
                questionPanel.setBackground(isExpanded ? new Color(248, 248, 255) : Color.WHITE);
                faqPanel.revalidate();
                
                SwingUtilities.invokeLater(() -> {
                    JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, faqPanel);
                    if (viewport != null && isExpanded && faqPanel.isShowing()) {
                        Rectangle bounds = faqPanel.getBounds();
                        viewport.scrollRectToVisible(bounds);
                    }
                });
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isExpanded) {
                    questionPanel.setBackground(new Color(248, 248, 255));
                    questionLabel.setForeground(new Color(120, 0, 80));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isExpanded) {
                    questionPanel.setBackground(Color.WHITE);
                    questionLabel.setForeground(PLUM);
                }
            }
        });
        
        faqPanel.add(questionPanel);
        faqPanel.add(answerPanel);
        
        parent.add(faqPanel);
        parent.add(Box.createRigidArea(new Dimension(0, 8)));
    }
    
    private static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public static JButton createFloatingHelpButton(JFrame parentFrame) {
        JButton helpButton = new JButton("?") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillOval(3, 5, 54, 54);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(70, 0, 50));
                } else if (getModel().isRollover()) {
                    
                    GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(120, 0, 80),
                        0, 60, new Color(90, 0, 60)
                    );
                    g2.setPaint(gradient);
                } else {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, PLUM,
                        0, 60, new Color(70, 0, 50)
                    );
                    g2.setPaint(gradient);
                }
                g2.fillOval(0, 0, 56, 56);
                
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(1, 1, 54, 54);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (60 - fm.stringWidth(text)) / 2;
                int y = (60 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 60);
            }
        };
        
        helpButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        helpButton.setForeground(Color.WHITE);
        helpButton.setBackground(PLUM);
        helpButton.setFocusPainted(false);
        helpButton.setBorderPainted(false);
        helpButton.setContentAreaFilled(false);
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpButton.setToolTipText("<html><b>Need Help?</b><br>Click for FAQs</html>");
        
        helpButton.addActionListener(e -> showFAQDialog(parentFrame));
        
        return helpButton;
    }
    
    public static JButton createModernCloseButton() {
        JButton closeButton = new JButton("✕") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(220, 53, 69));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(220, 53, 69));
                } else {
                    g2.setColor(new Color(108, 117, 125));
                }
                
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeButton.setForeground(Color.WHITE);
        closeButton.setPreferredSize(new Dimension(30, 30));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return closeButton;
    }
}