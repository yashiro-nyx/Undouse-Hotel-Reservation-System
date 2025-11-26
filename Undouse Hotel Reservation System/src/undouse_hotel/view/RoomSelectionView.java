package undouse_hotel.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import undouse_hotel.model.BookingModel;
import undouse_hotel.model.RoomModel;
import undouse_hotel.controller.BookingController;
import undouse_hotel.model.AdminModel;
import undouse_hotel.model.GuestModel;

public class RoomSelectionView extends BasePanelView {
    private BookingController controller;
    private JFrame parentFrame;
    private JButton proceedButton;
    private JPanel bookingSummarySidebar;
    private int totalGuests;
    private String panelName;
    private Map<String, JButton> roomButtonMap;
    private AdminModel adminModel;
    private GuestModel guestModel;
    
    public RoomSelectionView(BookingController controller, JFrame parentFrame, int totalGuests, 
            String panelName, AdminModel adminModel, GuestModel guestModel) {
this.controller = controller;
this.parentFrame = parentFrame;
this.totalGuests = totalGuests;
this.panelName = panelName;
this.roomButtonMap = new HashMap<>();
this.adminModel = adminModel;      
this.guestModel = guestModel;      
}
    
    @Override
    public JPanel createPanel() {
        BookingModel bookingModel = controller.getBookingModel();
        RoomModel roomModel = controller.getRoomModel();
        
        JPanel filteredPanel = new JPanel();
        filteredPanel.setLayout(new BoxLayout(filteredPanel, BoxLayout.Y_AXIS));
        filteredPanel.setBackground(Color.WHITE);
        
        JLabel header = new JLabel("Available Rooms for " + totalGuests + " Guest(s)", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        filteredPanel.add(header);
        
        JLabel selectingLabel = new JLabel("Select Room " + bookingModel.getCurrentRoomIndex(), SwingConstants.CENTER);
        selectingLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        selectingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        selectingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        filteredPanel.add(selectingLabel);
        
        String[][] rooms = roomModel.getAllRooms();
        
        boolean found = false;
        for (String[] room : rooms) {
            String type = room[0];
            String price = room[1];
            String desc = room[3];
            int capacity = roomModel.getRoomCapacity(type);
            
            if (totalGuests <= capacity) {
                for (int i = 1; i <= 3; i++) {
                    JPanel roomCard = createRoomCard(type + " " + i, price, desc, roomModel, bookingModel);
                    filteredPanel.add(roomCard);
                    filteredPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                }
                found = true;
            }
        }
        
        if (!found) {
            JLabel noMatch = new JLabel("No rooms available for " + totalGuests + " guest(s).", SwingConstants.CENTER);
            noMatch.setFont(new Font("SansSerif", Font.ITALIC, 18));
            noMatch.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
            noMatch.setAlignmentX(Component.CENTER_ALIGNMENT);
            filteredPanel.add(noMatch);
        }
        
        JPanel bottomPanel = createBottomPanel(bookingModel, roomModel);
        
        JScrollPane scrollPane = new JScrollPane(filteredPanel);
        styleScrollBar(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        wrapper.add(bottomPanel, BorderLayout.SOUTH);
        
        bookingSummarySidebar = new JPanel();
        bookingSummarySidebar.setLayout(new BoxLayout(bookingSummarySidebar, BoxLayout.Y_AXIS));
        bookingSummarySidebar.setPreferredSize(new Dimension(300, 0));
        bookingSummarySidebar.setBackground(new Color(255, 248, 240));
        bookingSummarySidebar.setBorder(BorderFactory.createTitledBorder("Selected Rooms"));
        bookingSummarySidebar.add(Box.createVerticalStrut(10));
        refreshBookingSummary();
        
        JPanel contentWithSidebar = new JPanel(new BorderLayout());
        contentWithSidebar.add(wrapper, BorderLayout.CENTER);
        contentWithSidebar.add(new JScrollPane(bookingSummarySidebar), BorderLayout.EAST);
        
        JPanel fullPanel = new JPanel(new BorderLayout());
        fullPanel.add(createBookingSummaryPanel(bookingModel), BorderLayout.NORTH);
        fullPanel.setName(panelName);
        fullPanel.add(contentWithSidebar, BorderLayout.CENTER);
        
        this.panel = fullPanel;
        return fullPanel;
    }
    
    private JPanel createRoomCard(String roomTitle, String price, String description, 
                                  RoomModel roomModel, BookingModel bookingModel) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(900, 200));
        card.setMaximumSize(new Dimension(900, 200));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(250, 250, 255));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        String normalizedTitle = roomTitle.replaceAll("\\s+", "_").toLowerCase();
        ImageIcon icon = new ImageIcon("images/" + normalizedTitle + ".jpg");
        Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));
        imageLabel.setPreferredSize(new Dimension(200, 200));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("<html><h2 style='color:#5A003C; margin:0;'>" + roomTitle + "</h2><i>" + description + "</i></html>");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(new JLabel("📶 Free WiFi"));
        centerPanel.add(new JLabel("❌ Free cancellation"));
        centerPanel.add(new JLabel("🍳 Good breakfast included"));
        centerPanel.add(new JLabel("💳 Pay nothing until check-in"));
        
        JLabel seeDetails = new JLabel("<html><u>See details</u></html>");
        seeDetails.setForeground(Color.BLUE.darker());
        seeDetails.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seeDetails.setAlignmentX(Component.LEFT_ALIGNMENT);
        seeDetails.setFont(new Font("SansSerif", Font.PLAIN, 13));
        seeDetails.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showRoomDetailsDialog(roomTitle, price, description);
            }
        });
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(seeDetails);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(250, 250, 250));
        rightPanel.setPreferredSize(new Dimension(250, 200));
        rightPanel.add(new JLabel("<html><font size='6' color='black'><b>" + price + "</b></font></html>"));
        
        JButton bookButton = createStyledButton("Book now", PRIMARY_BG, PRIMARY_HOVER, PRIMARY_TEXT);
        bookButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.setPreferredSize(new Dimension(140, 45));
        bookButton.setMaximumSize(new Dimension(140, 45));
        bookButton.setOpaque(true);
        
        String buttonKey = bookingModel.getCurrentRoomIndex() + "_" + roomTitle;
        roomButtonMap.put(buttonKey, bookButton);
        
        bookButton.addActionListener(e -> {
            int currentIndex = bookingModel.getCurrentRoomIndex();
            int basePrice = Integer.parseInt(price.replace("₱", "").replace(",", ""));
            double taxes = DialogHelper.computeTaxes(basePrice);
            
            RoomModel.RoomSelection selection = new RoomModel.RoomSelection(
                roomTitle, description, basePrice, taxes
            );
            roomModel.addSelectedRoom(currentIndex, selection);
            
            updateBookButtonStates(bookingModel, roomModel);
            refreshBookingSummary();
            if (proceedButton != null) {
                proceedButton.setEnabled(true);
            }
        });
        
        if (roomModel.hasSelectedRoom(bookingModel.getCurrentRoomIndex())) {
            RoomModel.RoomSelection selected = roomModel.getSelectedRoom(bookingModel.getCurrentRoomIndex());
            if (selected != null && selected.title.equals(roomTitle)) {
                bookButton.setBackground(new Color(90, 0, 60));
                bookButton.setForeground(Color.WHITE);
                bookButton.setEnabled(false);
            }
        }
        
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(bookButton);
        
        card.add(imageLabel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void updateBookButtonStates(BookingModel bookingModel, RoomModel roomModel) {
        roomButtonMap.forEach((key, button) -> {
            String[] parts = key.split("_", 2);
            int roomIndex = Integer.parseInt(parts[0]);
            String roomTitle = parts[1];
            
            if (roomIndex == bookingModel.getCurrentRoomIndex()) {
                RoomModel.RoomSelection selected = roomModel.getSelectedRoom(roomIndex);
                if (selected != null && selected.title.equals(roomTitle)) {
                    button.setBackground(new Color(90, 0, 60));
                    button.setForeground(Color.WHITE);
                    button.setEnabled(false);
                } else {
                    button.setBackground(PRIMARY_BG);
                    button.setForeground(PRIMARY_TEXT);
                    button.setEnabled(true);
                }
            }
        });
    }
    
    private JPanel createBottomPanel(BookingModel bookingModel, RoomModel roomModel) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton backButton;
        if (bookingModel.getCurrentRoomIndex() == 1) {
            backButton = createStyledButton("← Back to Homepage", PRIMARY_BG, PRIMARY_HOVER, PRIMARY_TEXT);
            backButton.addActionListener(e -> controller.goToHome());
        } else {
            backButton = createStyledButton("← Back", PRIMARY_BG, PRIMARY_HOVER, PRIMARY_TEXT);
            backButton.addActionListener(e -> controller.goToPreviousRoom());
        }
        bottomPanel.add(backButton, BorderLayout.WEST);
        
        proceedButton = createStyledButton(
            (bookingModel.getCurrentRoomIndex() < bookingModel.getNumRooms() 
                ? "Proceed to Room " + (bookingModel.getCurrentRoomIndex() + 1) 
                : "Checkout"),
            PRIMARY_BG, PRIMARY_HOVER, PRIMARY_TEXT
        );
        proceedButton.setEnabled(roomModel.hasSelectedRoom(bookingModel.getCurrentRoomIndex()));
        
        proceedButton.addActionListener(e -> {
            if (bookingModel.getCurrentRoomIndex() < bookingModel.getNumRooms()) {
                controller.proceedToNextRoom();
            } else {
                boolean confirmed = DialogHelper.showCustomConfirmation(parentFrame, 
                    "Confirm Checkout", "Proceed to payment?");
                if (confirmed) {
                    showPaymentWindow(roomModel, bookingModel);
                }
            }
        });
        
        bottomPanel.add(proceedButton, BorderLayout.EAST);
        return bottomPanel;
    }
    
    private void refreshBookingSummary() {
        if (bookingSummarySidebar == null) return;
        
        bookingSummarySidebar.removeAll();
        BookingModel bookingModel = controller.getBookingModel();
        RoomModel roomModel = controller.getRoomModel();
        
        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        Font detailFont = new Font("SansSerif", Font.PLAIN, 11);
        Font boldFont = new Font("SansSerif", Font.BOLD, 11);
        
        double total = 0;
        for (int i = 1; i <= bookingModel.getNumRooms(); i++) {
            RoomModel.RoomSelection room = roomModel.getSelectedRoom(i);
            if (room != null) {
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
                card.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));
                
                JLabel roomNum = new JLabel("ROOM " + i);
                roomNum.setFont(boldFont);
                
                JLabel name = new JLabel("<html>" + room.title + "<br>₱" + DialogHelper.formatPrice(room.basePrice) + "</html>");
                name.setFont(titleFont);
                name.setMaximumSize(new Dimension(260, 40));
                
                JLabel desc = new JLabel("<html><i>" + room.description + "</i></html>");
                desc.setFont(new Font("SansSerif", Font.PLAIN, 10));
                desc.setMaximumSize(new Dimension(260, 50));
                
                JLabel tax = new JLabel("Tax & fees: ₱" + DialogHelper.formatPrice(room.taxes));
                tax.setFont(detailFont);
                
                card.add(roomNum);
                card.add(Box.createVerticalStrut(3));
                card.add(name);
                card.add(desc);
                card.add(Box.createVerticalStrut(3));
                card.add(tax);
                
                JButton removeButton = new JButton("Remove");
                removeButton.setFont(new Font("SansSerif", Font.PLAIN, 10));
                removeButton.setBackground(Color.RED);
                removeButton.setForeground(Color.WHITE);
                removeButton.setFocusPainted(false);
                removeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                removeButton.setMaximumSize(new Dimension(80, 25));
                
                int indexToRemove = i;
                removeButton.addActionListener(e -> {
                    int result = JOptionPane.showConfirmDialog(parentFrame,
                        "Are you sure you want to remove Room " + indexToRemove + "?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        roomModel.removeSelectedRoom(indexToRemove);
                        updateBookButtonStates(controller.getBookingModel(), roomModel);
                        refreshBookingSummary();
                        if (proceedButton != null) {
                            proceedButton.setEnabled(roomModel.hasSelectedRoom(controller.getBookingModel().getCurrentRoomIndex()));
                        }
                        if (!roomModel.hasAnySelectedRooms()) {
                            controller.goToHome();
                        }
                    }
                });
                
                card.add(Box.createVerticalStrut(5));
                card.add(removeButton);
                
                bookingSummarySidebar.add(card);
                bookingSummarySidebar.add(Box.createVerticalStrut(8));
                
                total += room.basePrice + room.taxes;
            }
        }
        
        JLabel totalLabel = new JLabel("<html><b>Total:<br>₱" + DialogHelper.formatPrice(total) + "</b></html>");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookingSummarySidebar.add(totalLabel);
        bookingSummarySidebar.add(Box.createVerticalGlue());
        
        bookingSummarySidebar.revalidate();
        bookingSummarySidebar.repaint();
    }
    
    private JPanel createBookingSummaryPanel(BookingModel bookingModel) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        wrapper.setBackground(Color.WHITE);
        
        JPanel container = new JPanel(new GridLayout(1, 3));
        container.setPreferredSize(new Dimension(800, 80));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        Font titleFont = new Font("SansSerif", Font.BOLD, 14);
        Font detailFont = new Font("SansSerif", Font.PLAIN, 13);
        Color labelColor = new Color(120, 80, 30);
        
        JPanel guestsPanel = createSummaryItem("👥 Guests", 
            bookingModel.getNumAdults() + " adults, " + bookingModel.getNumChildren() + " children", 
            titleFont, detailFont, labelColor);
        JPanel checkinPanel = createSummaryItem("📅 Check-in", 
            bookingModel.getCheckInDateFormatted(), titleFont, detailFont, labelColor);
        JPanel checkoutPanel = createSummaryItem("📅 Check-out", 
            bookingModel.getCheckOutDateFormatted(), titleFont, detailFont, labelColor);
        
        container.add(guestsPanel);
        container.add(checkinPanel);
        container.add(checkoutPanel);
        
        wrapper.add(container);
        return wrapper;
    }
    
    private JPanel createSummaryItem(String title, String detail, Font titleFont, Font detailFont, Color titleColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(800 / 3, 80));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));
        
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        contentPanel.setOpaque(false);
        
        JLabel textLabel = new JLabel("<html><div style='line-height:1.2;'><span style='color:rgb(" + 
            titleColor.getRed() + "," + titleColor.getGreen() + "," + titleColor.getBlue() + 
            "); font-weight:bold;'>" + title + "</span><br>" + detail + "</div></html>");
        textLabel.setFont(detailFont);
        
        contentPanel.add(textLabel);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showRoomDetailsDialog(String roomTitle, String price, String description) {
        JOptionPane.showMessageDialog(parentFrame, 
            "Room: " + roomTitle + "\nPrice: " + price + "\n\n" + description,
            "Room Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showPaymentWindow(RoomModel roomModel, BookingModel bookingModel) {
        StringBuilder summary = new StringBuilder();
        double total = 0;
        for (int i = 1; i <= bookingModel.getNumRooms(); i++) {
            RoomModel.RoomSelection room = roomModel.getSelectedRoom(i);
            if (room != null) {
                summary.append("Room ").append(i).append(": ").append(room.title).append("\n");
                total += room.basePrice + room.taxes;
            }
        }
        
        PaymentView paymentView = new PaymentView(
            parentFrame, 
            "Multiple Rooms", 
            "₱" + DialogHelper.formatPrice(total),
            bookingModel.getCheckInDateFormatted(), 
            bookingModel.getCheckOutDateFormatted(),
            bookingModel.getTotalGuests(), 
            bookingModel.getNumRooms(),
            () -> {
                controller.goToHome();
            },
            adminModel,     
            guestModel     
        );
    }
    
    @Override
    public void updatePanel() {
        refreshBookingSummary();
        if (panel != null) {
            panel.revalidate();
            panel.repaint();
        }
    }
    
    @Override
    public String getPanelName() {
        return panelName;
    }
}