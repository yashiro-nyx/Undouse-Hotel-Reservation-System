package undouse_hotel.view;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import undouse_hotel.controller.BookingController;

public class HomePanelView extends BasePanelView {
    private BookingController controller;
    private JPanel centerPanel;
    private JLabel dateLabel;
    private JLabel guestLabel;
    private JFrame parentFrame;
    private RoomsPanelView roomsPanelView;
    private JScrollPane homeScrollPane;
    
    protected static final Color DARK_PLUM = new Color(70, 0, 50);
    protected static final Color LIGHT_GOLD = new Color(245, 220, 180);
    protected static final Color CREAM_WHITE = new Color(255, 250, 245);
    protected static final Color SOFT_GRAY = new Color(248, 248, 250);
    protected static final Color ACCENT_COLOR = new Color(180, 140, 190);
    
    protected static final Color PLUM = new Color(74, 20, 56); // Deep Violet/Plum
    
    public HomePanelView(BookingController controller, JFrame parentFrame) {
        this.controller = controller;
        this.parentFrame = parentFrame;
    }
    
    public void setBookingController(BookingController controller) {
        this.controller = controller;
    }
    
    public void setRoomsPanelView(RoomsPanelView roomsPanelView) {
        this.roomsPanelView = roomsPanelView;
    }
    
    @Override
    public JPanel createPanel() {
        JPanel background = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, CREAM_WHITE, 
                    getWidth(), getHeight(), new Color(250, 245, 240)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        styleScrollBar(scrollPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        homeScrollPane = scrollPane;
        
        centerPanel.add(createBookingSection());
        centerPanel.add(createDivider());
        centerPanel.add(createWarmMessagePanel());
        centerPanel.add(createDivider());
        centerPanel.add(createRoomRecommendations());
        centerPanel.add(createDivider());
        centerPanel.add(createAmenitiesSection());
        centerPanel.add(createDivider());
        centerPanel.add(createTestimonialsSection());
        centerPanel.add(createDivider());
        centerPanel.add(createContactInformation());
        
        background.add(scrollPane, BorderLayout.CENTER);
        this.panel = background;
        
        scrollToTop();
        
        return background;
    }
    
    public void scrollToTop() {
        if (homeScrollPane != null) {
            SwingUtilities.invokeLater(() -> {
                homeScrollPane.getVerticalScrollBar().setValue(0);
                homeScrollPane.getViewport().setViewPosition(new java.awt.Point(0, 0));
            });
        }
    }
    
    @Override
    public void updatePanel() {
        if (panel != null) {
            panel.revalidate();
            panel.repaint();
        }
        scrollToTop();
    }
    
    @Override
    public String getPanelName() {
        return "Home";
    }
    
    private JPanel createBookingSection() {
        JPanel bookingBackground = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;
            Image bookingBg = new ImageIcon("images/okada_bg.jpg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bookingBg, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bookingBackground.setPreferredSize(new Dimension(900, 550));
        bookingBackground.setOpaque(false);
        
        JPanel bookingPanelWrapper = new JPanel(new GridBagLayout());
        bookingPanelWrapper.setOpaque(false);
        
        JPanel bookingPanelContainer = new JPanel();
        bookingPanelContainer.setLayout(new BoxLayout(bookingPanelContainer, BoxLayout.Y_AXIS));
        bookingPanelContainer.setOpaque(false);
        
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);
        welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel welcomeLine1 = new JLabel("Welcome to");
        welcomeLine1.setFont(new Font("Georgia", Font.PLAIN, 36));
        welcomeLine1.setForeground(LIGHT_GOLD);
        welcomeLine1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel welcomeLine2 = new JLabel("Undouse Hotel");
        welcomeLine2.setFont(new Font("Georgia", Font.BOLD, 48));
        welcomeLine2.setForeground(Color.WHITE);
        welcomeLine2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomePanel.add(welcomeLine1);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(welcomeLine2);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 40, 0));
        
        bookingPanelContainer.add(welcomePanel);
        
        JPanel bookingPanel = new RoundedPanel(40);
        bookingPanel.setBackground(new Color(74, 20, 56, 230)); 
        bookingPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));     
        
        dateLabel = new JLabel("Date: Select Date Range 📅");
        guestLabel = new JLabel("Guests: 1 Room, 1 Adult, 0 Child ▼");
        JButton bookNowButton = createStyledButton("Search", LIGHT_GOLD, new Color(230, 190, 100), new Color(60, 30, 10));
        bookNowButton.setPreferredSize(new Dimension(150, 50));
        
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        guestLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        dateLabel.setForeground(Color.WHITE);
        guestLabel.setForeground(Color.WHITE);
        dateLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guestLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        dateLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showDateSelectionDialog();
            }
        });
        
        guestLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showGuestSelectionDialog();
            }
        });
        
        bookNowButton.addActionListener(e -> controller.handleSearch());
        
        bookingPanel.add(dateLabel);
        bookingPanel.add(guestLabel);
        bookingPanel.add(bookNowButton);
        bookingPanelContainer.add(bookingPanel);
        bookingPanelWrapper.add(bookingPanelContainer);
        bookingBackground.add(bookingPanelWrapper, BorderLayout.CENTER);
        
        return bookingBackground;
    }
    
    private JPanel createFormFieldPanel(String iconLabel, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBackground(new Color(255, 255, 255, 150));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setPreferredSize(new Dimension(180, 70));
        
        JLabel icon = new JLabel(iconLabel);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        icon.setForeground(DARK_PLUM);
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(icon);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel value = new JLabel(placeholder);
        value.setFont(new Font("SansSerif", Font.BOLD, 16));
        value.setForeground(Color.BLACK);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(value);
        
        return panel;
    }
    
    private void showDateSelectionDialog() {
        JDialog dateDialog = new JDialog(parentFrame, "Select Your Stay Dates", true);
        dateDialog.setSize(650, 420);
        dateDialog.setLayout(new BorderLayout());
        dateDialog.setUndecorated(false);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PLUM);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel titleLabel = new JLabel("Plan Your Perfect Getaway");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Select your check-in and check-out dates");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_GOLD);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(CREAM_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(15, 10, 15, 10);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0.0;
        JLabel checkInLabel = new JLabel("Check-in Date:");
        checkInLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        checkInLabel.setForeground(DARK_PLUM);
        contentPanel.add(checkInLabel, gc);
        
        gc.gridx = 1;
        gc.weightx = 1.0;
        JDateChooser checkInDate = new JDateChooser();
        checkInDate.setPreferredSize(new Dimension(350, 50));
        checkInDate.setMinimumSize(new Dimension(350, 50));
        checkInDate.setMinSelectableDate(new Date());
        checkInDate.setFont(new Font("SansSerif", Font.PLAIN, 15));
        checkInDate.setDateFormatString("MMMM dd, yyyy");
        checkInDate.getJCalendar().setTodayButtonVisible(true);
        // Make the text field inside JDateChooser bigger
        JTextField dateField = (JTextField) checkInDate.getDateEditor().getUiComponent();
        dateField.setPreferredSize(new Dimension(310, 45));
        dateField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        dateField.setColumns(20);
        contentPanel.add(checkInDate, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0.0;
        JLabel checkOutLabel = new JLabel("Check-out Date:");
        checkOutLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        checkOutLabel.setForeground(DARK_PLUM);
        contentPanel.add(checkOutLabel, gc);
        
        gc.gridx = 1;
        gc.weightx = 1.0;
        JDateChooser checkOutDate = new JDateChooser();
        checkOutDate.setPreferredSize(new Dimension(350, 50));
        checkOutDate.setMinimumSize(new Dimension(350, 50));
        checkOutDate.setMinSelectableDate(new Date());
        checkOutDate.setFont(new Font("SansSerif", Font.PLAIN, 15));
        checkOutDate.setDateFormatString("MMMM dd, yyyy");
        checkOutDate.getJCalendar().setTodayButtonVisible(true);
        
        JTextField dateFieldOut = (JTextField) checkOutDate.getDateEditor().getUiComponent();
        dateFieldOut.setPreferredSize(new Dimension(310, 45));
        dateFieldOut.setFont(new Font("SansSerif", Font.PLAIN, 15));
        dateFieldOut.setColumns(20);
        contentPanel.add(checkOutDate, gc);
        
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        JLabel infoLabel = new JLabel("💡 Check-out must be at least one day after check-in");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(infoLabel, gc);
        
        checkInDate.addPropertyChangeListener("date", evt -> {
            Date selectedCheckIn = checkInDate.getDate();
            if (selectedCheckIn != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(selectedCheckIn);
                cal.add(Calendar.DATE, 1);
                checkOutDate.setMinSelectableDate(cal.getTime());
                
                Date selectedCheckOut = checkOutDate.getDate();
                if (selectedCheckOut != null && !selectedCheckOut.after(selectedCheckIn)) {
                    checkOutDate.setDate(null);
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(CREAM_WHITE);
        
        JButton confirmButton = createStyledButton("✓ Confirm Dates", LIGHT_GOLD, new Color(255, 200, 150), new Color(60, 30, 10));
        confirmButton.setPreferredSize(new Dimension(180, 45));
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 45));
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setBackground(new Color(220, 220, 220));
        cancelButton.setForeground(new Color(80, 80, 80));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> dateDialog.dispose());
        
        confirmButton.addActionListener(ae -> {
            if (checkInDate.getDate() != null && checkOutDate.getDate() != null) {
                if (!checkOutDate.getDate().after(checkInDate.getDate())) {
                    DialogHelper.showElegantWarningDialog(parentFrame, "Invalid Date Range", 
                        "Check-out date must be at least one day after check-in date.");
                    return;
                }
                
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                String checkInStr = sdf.format(checkInDate.getDate());
                String checkOutStr = sdf.format(checkOutDate.getDate());
                controller.getBookingModel().setCheckInDate(checkInStr);
                controller.getBookingModel().setCheckOutDate(checkOutStr);
                dateLabel.setText("Date: " + checkInStr + " - " + checkOutStr);
                dateDialog.dispose();
            } else {
                DialogHelper.showElegantWarningDialog(parentFrame, "Missing Dates", 
                    "Please select both check-in and check-out dates.");
            }
        });
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        dateDialog.add(headerPanel, BorderLayout.NORTH);
        dateDialog.add(contentPanel, BorderLayout.CENTER);
        dateDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dateDialog.setLocationRelativeTo(parentFrame);
        dateDialog.setVisible(true);
    }
    
    private void showGuestSelectionDialog() {
        JDialog guestDialog = new JDialog(parentFrame, "Select Number of Guests", true);
        guestDialog.setSize(520, 600);
        guestDialog.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PLUM);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel titleLabel = new JLabel("Who's Coming Along?");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Tell us about your travel party");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_GOLD);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CREAM_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        
        int[] numRooms = {controller.getBookingModel().getNumRooms()};
        int[] numAdults = {controller.getBookingModel().getNumAdults()};
        int[] numChildren = {controller.getBookingModel().getNumChildren()};
        
        JPanel roomPanel = createResortCounterPanel("Rooms", numRooms, "Number of rooms needed");
        JPanel adultPanel = createResortCounterPanel("Adults", numAdults, "Guests 18 years and older");
        JPanel childrenPanel = createResortCounterPanel("Children", numChildren, "Guests under 18 years");
        
        contentPanel.add(roomPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(adultPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(childrenPanel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(CREAM_WHITE);
        
        JButton confirmButton = createStyledButton("✓ Confirm Selection", LIGHT_GOLD, new Color(255, 200, 150), new Color(60, 30, 10));
        confirmButton.setPreferredSize(new Dimension(200, 45));
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 45));
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setBackground(new Color(220, 220, 220));
        cancelButton.setForeground(new Color(80, 80, 80));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> guestDialog.dispose());
        
        confirmButton.addActionListener(ae -> {
            controller.getBookingModel().setNumRooms(numRooms[0]);
            controller.getBookingModel().setNumAdults(numAdults[0]);
            controller.getBookingModel().setNumChildren(numChildren[0]);
            guestLabel.setText(numRooms[0] + " Room(s), " + numAdults[0] + 
                             " Adult(s), " + numChildren[0] + " Child(ren)");
            guestDialog.dispose();
        });
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        guestDialog.add(headerPanel, BorderLayout.NORTH);
        guestDialog.add(contentPanel, BorderLayout.CENTER);
        guestDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        guestDialog.setLocationRelativeTo(parentFrame);
        guestDialog.setVisible(true);
    }
    
    private JPanel createResortCounterPanel(String labelText, int[] counter, String description) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 230), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        mainPanel.setMaximumSize(new Dimension(440, 130));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(labelText);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(DARK_PLUM);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setForeground(new Color(120, 120, 120));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        labelPanel.add(nameLabel);
        labelPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        labelPanel.add(descLabel);
        
        JPanel counterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        counterPanel.setOpaque(false);
        
        JButton minusButton = new JButton("−");
        minusButton.setPreferredSize(new Dimension(50, 45));
        minusButton.setBackground(LIGHT_GOLD);
        minusButton.setForeground(new Color(60, 30, 10));
        minusButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        minusButton.setFocusPainted(false);
        minusButton.setBorderPainted(true);
        minusButton.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 150), 2));
        minusButton.setOpaque(true);
        
        JLabel countLabel = new JLabel(String.valueOf(counter[0]));
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        countLabel.setForeground(DARK_PLUM);
        countLabel.setPreferredSize(new Dimension(50, 45));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton plusButton = new JButton("+");
        plusButton.setPreferredSize(new Dimension(50, 45));
        plusButton.setBackground(LIGHT_GOLD);
        plusButton.setForeground(new Color(60, 30, 10));
        plusButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        plusButton.setFocusPainted(false);
        plusButton.setBorderPainted(true);
        plusButton.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 150), 2));
        plusButton.setOpaque(true);
        
        minusButton.addActionListener(e -> {
            if (counter[0] > 1) {
                counter[0]--;
                countLabel.setText(String.valueOf(counter[0]));
            }
        });
        
        plusButton.addActionListener(e -> {
            counter[0]++;
            countLabel.setText(String.valueOf(counter[0]));
        });
        
        counterPanel.add(minusButton);
        counterPanel.add(countLabel);
        counterPanel.add(plusButton);
        
        topPanel.add(labelPanel, BorderLayout.WEST);
        topPanel.add(counterPanel, BorderLayout.EAST);
        
        mainPanel.add(topPanel);
        
        return mainPanel;
    }
    
    private JPanel createWarmMessagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.setLayout(new BorderLayout());
        
        JLabel line1 = new JLabel(
            "Experience comfort and luxury like never before. Whether you're here for business, leisure, or a quick getaway,"
        );
        line1.setFont(new Font("Georgia", Font.PLAIN, 22));
        line1.setForeground(PLUM);
        line1.setHorizontalAlignment(SwingConstants.CENTER);
        line1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel line2 = new JLabel(
            "Undouse Hotel ensures an unforgettable stay with world-class amenities and personalized service tailored just for you."
        );
        line2.setFont(new Font("Georgia", Font.PLAIN, 22));
        line2.setForeground(PLUM);
        line2.setHorizontalAlignment(SwingConstants.CENTER);
        line2.setBorder(BorderFactory.createEmptyBorder(10, 20, 25, 20));
        
        panel.add(line1, BorderLayout.NORTH);
        panel.add(line2, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRoomRecommendations() {
        JPanel roomPanel = new JPanel();
        roomPanel.setOpaque(false);
        roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("TOP CHOICE ROOMS");
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(PLUM);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        roomPanel.add(title);
        
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(1300, 550));
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Dimension cardContainerSize = new Dimension(366, 550);
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.setPreferredSize(cardContainerSize);
        left.add(createRoomRecommendationCard("Family Suite", "deluxe.jpg", 
            "Designed for comfort and togetherness, the Family Suite offers generous space with two deluxe beds."));
        
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setOpaque(false);
        center.setPreferredSize(cardContainerSize);
        center.add(createRoomRecommendationCard("Premier Deluxe Room", "suite.jpg", 
            "A refined blend of luxury and comfort, ideal for couples or small families."));
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.setPreferredSize(cardContainerSize);
        right.add(createRoomRecommendationCard("Classic Room", "classic.jpg", 
            "Ideal for solo travelers, this compact and cozy room features modern décor."));
        
        wrapper.add(left);
        wrapper.add(center);
        wrapper.add(right);
        
        roomPanel.add(wrapper);
        return roomPanel;
    }
    
    private JPanel createRoomRecommendationCard(String roomType, String imageFile, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(340, 520));
        card.setMaximumSize(new Dimension(340, 520));
        card.setBackground(new Color(255, 255, 255, 230));
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        
        ImageIcon originalIcon = new ImageIcon("images/" + imageFile);
        Image scaledImage = originalIcon.getImage().getScaledInstance(320, 220, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        JLabel roomLabel = new JLabel(roomType);
        roomLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        roomLabel.setForeground(PLUM);
        roomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setFocusable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setMaximumSize(new Dimension(300, 90));
        
        JButton viewDetailsButton = new JButton("View Details");
        addHoverEffect(viewDetailsButton, new Color(255, 200, 150), new Color(255, 223, 186));
        viewDetailsButton.setBackground(new Color(255, 223, 186));
        viewDetailsButton.setPreferredSize(new Dimension(200, 45));
        viewDetailsButton.setMaximumSize(new Dimension(200, 45));
        viewDetailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewDetailsButton.setForeground(new Color(60, 30, 10));
        viewDetailsButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setOpaque(true);
        viewDetailsButton.setBorderPainted(false);
        
        viewDetailsButton.addActionListener(e -> {
            if (roomsPanelView != null && controller != null) {
                controller.navigateToRooms();
                
                SwingUtilities.invokeLater(() -> {
                    roomsPanelView.scrollToRoom(roomType);
                    Timer timer = new Timer(300, evt -> {
                        roomsPanelView.showRoomDetailsFromHome(roomType);
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
            }
        });
        
        card.add(imageLabel);
        card.add(roomLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descArea);
        card.add(Box.createVerticalGlue());
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(viewDetailsButton);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return card;
    }
    
    private JPanel createTestimonialsSection() {
        JPanel testimonialsPanel = new JPanel();
        testimonialsPanel.setOpaque(false);
        testimonialsPanel.setLayout(new BoxLayout(testimonialsPanel, BoxLayout.Y_AXIS));
        testimonialsPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        JLabel title = new JLabel("WHAT OUR GUESTS SAY");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(PLUM);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        testimonialsPanel.add(title);
        
        JPanel cardsWrapper = new JPanel();
        cardsWrapper.setLayout(new BoxLayout(cardsWrapper, BoxLayout.X_AXIS));
        cardsWrapper.setOpaque(false);
        cardsWrapper.setMaximumSize(new Dimension(1200, 320));
        cardsWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel testimonial1 = createTestimonialCard(
            "⭐⭐⭐⭐⭐",
            "\"The booking process was incredibly smooth! I found the perfect room in minutes, and the confirmation was instant. The system is user-friendly and made planning my stay effortless. Highly recommend!\"",
            "- Sarah Martinez",
            "Booked: December 2024"
        );
        
        JPanel testimonial2 = createTestimonialCard(
            "⭐⭐⭐⭐⭐",
            "\"Best experience I've had! The website is intuitive, payment was secure, and I received all the information I needed immediately. Customer service was excellent throughout.\"",
            "- Michael Chen",
            "Booked: January 2025"
        );
        
        JPanel testimonial3 = createTestimonialCard(
            "⭐⭐⭐⭐⭐",
            "\"Amazing reservation system! I was able to compare different room types, see real-time availability, and book everything within minutes. The entire process was transparent and hassle-free!\"",
            "- Emily Rodriguez",
            "Booked: October 2024"
        );
        
        cardsWrapper.add(Box.createHorizontalGlue());
        cardsWrapper.add(testimonial1);
        cardsWrapper.add(Box.createRigidArea(new Dimension(25, 0)));
        cardsWrapper.add(testimonial2);
        cardsWrapper.add(Box.createRigidArea(new Dimension(25, 0)));
        cardsWrapper.add(testimonial3);
        cardsWrapper.add(Box.createHorizontalGlue());
        
        testimonialsPanel.add(cardsWrapper);
        
        return testimonialsPanel;
    }
    
    private JPanel createTestimonialCard(String stars, String quote, String author, String date) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 280));
        card.setMaximumSize(new Dimension(360, 280));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        
        JLabel starsLabel = new JLabel(stars);
        starsLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        starsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        starsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JTextArea quoteArea = new JTextArea(quote);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setLineWrap(true);
        quoteArea.setEditable(false);
        quoteArea.setFocusable(false);
        quoteArea.setOpaque(false);
        quoteArea.setFont(new Font("Georgia", Font.ITALIC, 15));
        quoteArea.setForeground(new Color(60, 60, 60));
        quoteArea.setMaximumSize(new Dimension(310, 130));
        quoteArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        authorLabel.setForeground(PLUM);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(120, 120, 120));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(starsLabel);
        card.add(quoteArea);
        card.add(Box.createVerticalGlue());
        card.add(authorLabel);
        card.add(dateLabel);
        
        return card;
    }
    
    private JPanel createAmenitiesSection() {
        JPanel amenitiesPanel = new JPanel();
        amenitiesPanel.setBackground(new Color(248, 248, 250));
        amenitiesPanel.setLayout(new BoxLayout(amenitiesPanel, BoxLayout.Y_AXIS));
        amenitiesPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        
        JLabel title = new JLabel("RESORT AMENITIES");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(PLUM);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 0));
        amenitiesPanel.add(title);
        
        JPanel amenitiesGrid = new JPanel(new GridLayout(2, 3, 30, 30));
        amenitiesGrid.setOpaque(false);
        amenitiesGrid.setMaximumSize(new Dimension(1100, 320));
        amenitiesGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        amenitiesGrid.add(createAmenityCard("🔍", "Easy Search", "Intuitive search interface to find your perfect room quickly"));
        amenitiesGrid.add(createAmenityCard("⚡", "Instant Booking", "Real-time availability with immediate confirmation"));
        amenitiesGrid.add(createAmenityCard("💳", "Secure Payment", "Safe and encrypted payment processing system"));
        amenitiesGrid.add(createAmenityCard("📱", "Mobile Friendly", "Seamless booking experience on any device"));
        amenitiesGrid.add(createAmenityCard("🔔", "Notifications", "Instant booking confirmations and updates"));
        amenitiesGrid.add(createAmenityCard("📊", "Room Comparison", "Compare different room types and prices easily"));
        amenitiesPanel.add(amenitiesGrid);
        
        return amenitiesPanel;
    }
    
    private JPanel createAmenityCard(String icon, String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        card.setPreferredSize(new Dimension(340, 140));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));
        
        JTextArea descArea = new JTextArea(description);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setFocusable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descArea.setForeground(new Color(80, 80, 80));
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setMaximumSize(new Dimension(300, 50));
        
        card.add(iconLabel);
        card.add(titleLabel);
        card.add(descArea);
        
        return card;
    }
    
    private JPanel createContactInformation() {
        JPanel contactPanel = new JPanel(new BorderLayout());
        contactPanel.setBackground(PLUM);
        contactPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);
        
        ImageIcon rawIcon = new ImageIcon("images/logo.jpg");
        Image scaled = rawIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaled));
        
        JLabel name = new JLabel("Unoduse Hotel");
        name.setFont(new Font("Serif", Font.BOLD, 32));
        name.setForeground(Color.WHITE);
        
        leftPanel.add(logo);
        leftPanel.add(name);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(createVerticalContactBlock("📍 Address", "New Seaside Drive, Quezon City"));
        rightPanel.add(createVerticalContactBlock("✉️ Email", "info@undousehotel.com"));
        rightPanel.add(createVerticalContactBlock("📞 Contact Number", "+639916649798"));
        
        contactPanel.add(leftPanel, BorderLayout.WEST);
        contactPanel.add(rightPanel, BorderLayout.EAST);
        
        return contactPanel;
    }
    
    private JPanel createVerticalContactBlock(String labelText, String detailText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel value = new JLabel(detailText);
        value.setFont(new Font("SansSerif", Font.PLAIN, 14));
        value.setForeground(Color.WHITE);
        value.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        value.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(value);
        
        return panel;
    }
}