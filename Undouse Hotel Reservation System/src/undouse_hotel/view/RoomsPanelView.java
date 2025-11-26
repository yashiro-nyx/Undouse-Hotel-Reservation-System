package undouse_hotel.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import undouse_hotel.model.RoomModel;
import undouse_hotel.model.AdminModel;

public class RoomsPanelView extends BasePanelView {
    private static final long serialVersionUID = 1L;
    private RoomModel roomModel;
    private AdminModel adminModel;
    private Map<String, JLabel> roomSectionLabels;
    private JScrollPane roomsScrollPane;
    private JFrame parentFrame;
    private JPanel roomCardsPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel categoryFilterPanel;
    private String selectedCategory = "All Rooms";
    private Map<String, JPanel> categoryButtons;
    private static final Color DARK_PLUM = new Color(114, 47, 55);
    
    public RoomsPanelView(RoomModel roomModel, JFrame parentFrame, CardLayout cardLayout, JPanel mainPanel) {
        this.roomModel = roomModel;
        this.parentFrame = parentFrame;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.roomSectionLabels = new HashMap<>();
        this.categoryButtons = new HashMap<>();
    }
    
    public void setAdminModel(AdminModel adminModel) {
        this.adminModel = adminModel;
    }
    
    @Override
    public JPanel createPanel() {
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(Color.WHITE);
        
        categoryFilterPanel = createCategoryFilterPanel();
        mainWrapper.add(categoryFilterPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel headerLabel = new JLabel("Available Rooms", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setForeground(DARK_PLUM);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        roomCardsPanel = new JPanel();
        roomCardsPanel.setBackground(Color.WHITE);
        roomCardsPanel.setLayout(new BoxLayout(roomCardsPanel, BoxLayout.Y_AXIS));
        
        populateRoomCards();
        
        JScrollPane scrollPane = new JScrollPane(roomCardsPanel);
        styleScrollBar(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        roomsScrollPane = scrollPane;
        mainWrapper.add(contentPanel, BorderLayout.CENTER);
        
        this.panel = mainWrapper;
        return mainWrapper;
    }
    
    private JPanel createCategoryFilterPanel() {
        JPanel mainFilterPanel = new JPanel(new BorderLayout());
        mainFilterPanel.setBackground(new Color(250, 245, 250));
        mainFilterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JPanel filterContent = new JPanel();
        filterContent.setLayout(new BoxLayout(filterContent, BoxLayout.Y_AXIS));
        filterContent.setBackground(new Color(250, 245, 250));
        filterContent.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel titleLabel = new JLabel("Browse Room Categories");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(DARK_PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel categoriesWrapper = new JPanel(new BorderLayout());
        categoriesWrapper.setBackground(new Color(250, 245, 250));
        
        JPanel categoriesContainer = new JPanel();
        categoriesContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        categoriesContainer.setBackground(new Color(250, 245, 250));
        categoriesContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        String[][] categories = {
            {"All Rooms", "🏨", "View all available rooms"},
            {"Classic Room", "🛏️", "Cozy and comfortable"},
            {"Standard Deluxe Room", "⭐", "Enhanced comfort features"},
            {"Premier Deluxe Room", "✨", "Premium luxury experience"},
            {"Executive Suite", "💼", "Business class accommodation"},
            {"Family Suite", "👨‍👩‍👧‍👦", "Spacious family living"},
            {"Presidential Suite", "👑", "Ultimate luxury experience"}
        };
        
        for (String[] category : categories) {
            JPanel categoryCard = createCategoryCard(category[0], category[1], category[2]);
            categoriesContainer.add(categoryCard);
            categoryButtons.put(category[0], categoryCard);
        }
        
        JScrollPane categoriesScrollPane = new JScrollPane(categoriesContainer);
        categoriesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        categoriesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        categoriesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        categoriesScrollPane.getViewport().setBackground(new Color(250, 245, 250));
        styleScrollBar(categoriesScrollPane);
        categoriesScrollPane.setPreferredSize(new Dimension(800, 140));
        
        categoriesWrapper.add(categoriesScrollPane, BorderLayout.CENTER);
        
        filterContent.add(titleLabel);
        filterContent.add(categoriesWrapper);
        
        mainFilterPanel.add(filterContent, BorderLayout.CENTER);
        
        setCategorySelection("All Rooms");
        
        return mainFilterPanel;
    }
    
    private JPanel createCategoryCard(String categoryName, String icon, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(150, 110));
        card.setMinimumSize(new Dimension(150, 110));
        card.setMaximumSize(new Dimension(150, 110));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 8, 12, 8)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel("<html><center>" + categoryName + "</center></html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        nameLabel.setForeground(DARK_PLUM);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><center><small>" + description + "</small></center></html>");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 9));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(descLabel);
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setCategorySelection(categoryName);
                filterRoomsByCategory(categoryName);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selectedCategory.equals(categoryName)) {
                    card.setBackground(new Color(245, 240, 255));
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(147, 51, 234), 2),
                        BorderFactory.createEmptyBorder(11, 7, 11, 7)
                    ));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!selectedCategory.equals(categoryName)) {
                    card.setBackground(Color.WHITE);
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(12, 8, 12, 8)
                    ));
                }
            }
        });
        
        return card;
    }
    
    private void setCategorySelection(String category) {
        for (Map.Entry<String, JPanel> entry : categoryButtons.entrySet()) {
            JPanel card = entry.getValue();
            if (entry.getKey().equals(category)) {
                card.setBackground(new Color(230, 220, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(147, 51, 234), 3),
                    BorderFactory.createEmptyBorder(10, 6, 10, 6)
                ));
                
                Component[] components = card.getComponents();
                for (Component comp : components) {
                    if (comp instanceof JLabel) {
                        JLabel label = (JLabel) comp;
                        if (label.getText().contains(category)) {
                            label.setForeground(DARK_PLUM);
                        }
                    }
                }
            } else {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(12, 8, 12, 8)
                ));
            }
        }
        selectedCategory = category;
    }
    
    private void filterRoomsByCategory(String category) {
        populateRoomCards();
        
        SwingUtilities.invokeLater(() -> {
            if (roomsScrollPane != null) {
                roomsScrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }
    
    private void populateRoomCards() {
        roomCardsPanel.removeAll();
        roomSectionLabels.clear();
        
        if (adminModel == null) {
            JLabel errorLabel = new JLabel("Admin data not initialized", SwingConstants.CENTER);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            errorLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            roomCardsPanel.add(errorLabel);
            roomCardsPanel.revalidate();
            roomCardsPanel.repaint();
            return;
        }
        
        java.util.List<AdminModel.RoomUnit> allRooms = adminModel.getAllRoomUnits();
        
        if (allRooms.isEmpty()) {
            JLabel noRoomsLabel = new JLabel("No rooms available", SwingConstants.CENTER);
            noRoomsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noRoomsLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            roomCardsPanel.add(noRoomsLabel);
            roomCardsPanel.revalidate();
            roomCardsPanel.repaint();
            return;
        }
        
        java.util.List<AdminModel.RoomUnit> filteredRooms = new ArrayList<>();
        for (AdminModel.RoomUnit room : allRooms) {
            if (selectedCategory.equals("All Rooms") || room.getRoomType().equals(selectedCategory)) {
                filteredRooms.add(room);
            }
        }
        
        if (filteredRooms.isEmpty()) {
            JLabel noMatchLabel = new JLabel(
                "<html><div style='text-align:center; color:#666; font-size:16px;'>" +
                "No " + selectedCategory + " available<br>" +
                "<small style='font-size:12px;'>Please check back later or try another category</small></div></html>", 
                SwingConstants.CENTER
            );
            noMatchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noMatchLabel.setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));
            roomCardsPanel.add(noMatchLabel);
            roomCardsPanel.revalidate();
            roomCardsPanel.repaint();
            return;
        }
        
        Map<String, java.util.List<AdminModel.RoomUnit>> roomsByType = new LinkedHashMap<>();
        for (AdminModel.RoomUnit room : filteredRooms) {
            roomsByType.computeIfAbsent(room.getRoomType(), k -> new ArrayList<>()).add(room);
        }
        
        for (Map.Entry<String, java.util.List<AdminModel.RoomUnit>> entry : roomsByType.entrySet()) {
            String type = entry.getKey();
            java.util.List<AdminModel.RoomUnit> roomUnits = entry.getValue();
            
            JPanel sectionPanel = new JPanel(new BorderLayout());
            sectionPanel.setBackground(Color.WHITE);
            sectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            JLabel sectionLabel = new JLabel(type, SwingConstants.LEFT);
            roomSectionLabels.put(type, sectionLabel);
            sectionLabel.setFont(new Font("Georgia", Font.BOLD, 20));
            sectionLabel.setForeground(DARK_PLUM);
            sectionLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 180, 120)));
            
            sectionPanel.add(sectionLabel, BorderLayout.CENTER);
            
            roomCardsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            roomCardsPanel.add(sectionPanel);
            roomCardsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            for (AdminModel.RoomUnit unit : roomUnits) {
                roomCardsPanel.add(createRoomCard(unit));
                roomCardsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }
        
        roomCardsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        roomCardsPanel.revalidate();
        roomCardsPanel.repaint();
    }

    private JPanel createRoomCard(AdminModel.RoomUnit room) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(900, 200));
        card.setMaximumSize(new Dimension(900, 200));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(10, 10));
        
        Color borderColor = getStatusColor(room.getStatus());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 2, true),
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
        
        JComponent imageComponent = createImageComponent(room.getImagePath(), room.getRoomType(), room.getRoomId());
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("<html><h2 style='color:#722F37; margin:0;'>" + 
            room.getRoomId() + " - " + room.getRoomType() + "</h2><i>" + 
            room.getDescription() + "</i></html>");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(titleLabel);
        
        centerPanel.add(new JLabel("📌 Free WiFi"));
        centerPanel.add(new JLabel("❌ Free cancellation"));
        centerPanel.add(new JLabel("🍽 Good breakfast included"));
        centerPanel.add(new JLabel("💳 Pay nothing until check-in"));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel statusBadge = createStatusBadge(room.getStatus());
        statusPanel.add(statusBadge);
        centerPanel.add(statusPanel);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(250, 250, 250));
        rightPanel.setPreferredSize(new Dimension(250, 200));
        
        String priceDisplay = "₱" + String.format("%,d", room.getBasePrice());
        rightPanel.add(new JLabel("<html><font size='6' color='black'><b>" + priceDisplay + "</b></font></html>"));
        
        JButton bookButton = createStyledButton("Book now", PRIMARY_BG, PRIMARY_HOVER, PRIMARY_TEXT);
        bookButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.setPreferredSize(new Dimension(140, 45));
        bookButton.setMaximumSize(new Dimension(140, 45));
        bookButton.setOpaque(true);
        
        if (!room.getStatus().equals("Available")) {
            bookButton.setEnabled(false);
            bookButton.setBackground(Color.GRAY);
            bookButton.setText("Unavailable");
        } else {
            bookButton.addActionListener(e -> {
                navigateToHome();
            });
        }
        
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(bookButton);
        
        JButton detailsButton = createStyledButton("See Details", Color.WHITE, new Color(240, 240, 240), DARK_PLUM);
        detailsButton.setBorder(BorderFactory.createLineBorder(DARK_PLUM, 2));
        detailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailsButton.setPreferredSize(new Dimension(140, 45));
        detailsButton.setMaximumSize(new Dimension(140, 45));
        detailsButton.setOpaque(true);
        detailsButton.addActionListener(e -> {
            showRoomDetailsDialog(room);
        });
        
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(detailsButton);
        
        card.add(imageComponent, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void navigateToHome() {
        if (cardLayout != null && mainPanel != null) {
            cardLayout.show(mainPanel, "Home");
            
            SwingUtilities.invokeLater(() -> {
                for (Component comp : mainPanel.getComponents()) {
                    if (comp.getName() != null && comp.getName().equals("Home")) {
                        if (comp instanceof JPanel) {
                            for (Component child : ((JPanel) comp).getComponents()) {
                                if (child instanceof JScrollPane) {
                                    JScrollPane scrollPane = (JScrollPane) child;
                                    scrollPane.getVerticalScrollBar().setValue(0);
                                    scrollPane.getViewport().setViewPosition(new java.awt.Point(0, 0));
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }
    }
    
    private String extractRoomNumber(String roomId) {
        try {
            String[] parts = roomId.split("-");
            if (parts.length > 1) {
                return String.valueOf(Integer.parseInt(parts[1]));
            }
        } catch (Exception e) {
            System.out.println("Error extracting room number: " + e.getMessage());
        }
        return "1";
    }
    
    private JComponent createImageComponent(String imagePath, String roomType, String roomId) {
        ImageIcon icon = null;
        
        if (imagePath != null && !imagePath.isEmpty()) {
            icon = loadImage(imagePath);
        }
        
        if (icon == null || icon.getIconWidth() <= 0) {
            String roomTypeNormalized = roomType.replaceAll("\\s+", "_").toLowerCase();
            String roomNumber = extractRoomNumber(roomId);
            
            String[] possiblePaths = {
                "images/" + roomTypeNormalized + "_" + roomNumber + ".jpg",
                "images/" + roomTypeNormalized + ".jpg",
                "images/" + roomId + ".jpg",
                "images/room_placeholder.jpg"
            };
            
            for (String path : possiblePaths) {
                icon = loadImage(path);
                if (icon != null && icon.getIconWidth() > 0) {
                    break;
                }
            }
        }
        
        if (icon != null && icon.getIconWidth() > 0) {
            Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setPreferredSize(new Dimension(200, 200));
            return imageLabel;
        } else {
            JPanel placeholder = createImagePlaceholder(roomType);
            return placeholder;
        }
    }
    
    private JPanel createImagePlaceholder(String roomType) {
        JPanel placeholder = new JPanel();
        placeholder.setBackground(new Color(200, 200, 200));
        placeholder.setPreferredSize(new Dimension(200, 200));
        placeholder.setLayout(new BorderLayout());
        
        JLabel label = new JLabel("<html><center>" + roomType + "<br><small>No Image</small></center></html>");
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.add(label, BorderLayout.CENTER);
        
        return placeholder;
    }
    
    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel(" " + getStatusIcon(status) + " " + status + " ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 12));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        switch (status) {
            case "Available":
                badge.setBackground(new Color(200, 255, 200));
                badge.setForeground(new Color(0, 100, 0));
                break;
            case "Occupied":
                badge.setBackground(new Color(255, 230, 200));
                badge.setForeground(new Color(150, 75, 0));
                break;
            case "Maintenance":
                badge.setBackground(new Color(255, 255, 200));
                badge.setForeground(new Color(150, 150, 0));
                break;
            case "Out of Service":
                badge.setBackground(new Color(255, 200, 200));
                badge.setForeground(new Color(150, 0, 0));
                break;
            default:
                badge.setBackground(Color.LIGHT_GRAY);
                badge.setForeground(Color.DARK_GRAY);
        }
        
        return badge;
    }
    
    private String getStatusIcon(String status) {
        switch (status) {
            case "Available": return "✓";
            case "Occupied": return "🔒";
            case "Maintenance": return "🔧";
            case "Out of Service": return "⚠";
            default: return "•";
        }
    }
    
    private Color getStatusColor(String status) {
        switch (status) {
            case "Available": return new Color(0, 150, 0);
            case "Occupied": return new Color(200, 100, 0);
            case "Maintenance": return new Color(200, 200, 0);
            case "Out of Service": return new Color(200, 0, 0);
            default: return new Color(200, 200, 200);
        }
    }
    
    private ImageIcon loadImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }
        
        File imageFile = new File(imagePath);
        
        if (imageFile.exists()) {
            return new ImageIcon(imagePath);
        }
        
        String fileName = new File(imagePath).getName();
        String localImagePath = "images/" + fileName;
        File localImageFile = new File(localImagePath);
        
        if (localImageFile.exists()) {
            return new ImageIcon(localImagePath);
        }
        
        return null;
    }
    
    private void showRoomDetailsDialog(AdminModel.RoomUnit room) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JDialog dialog = new JDialog(parentFrame, room.getRoomId() + " Details", true);
                    dialog.setSize(1000, 700);
                    dialog.setLocationRelativeTo(parentFrame);
                    dialog.setResizable(false);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    
                    JPanel mainContainer = new JPanel(new BorderLayout());
                    mainContainer.setBackground(new Color(240, 248, 255));
                    
                    JPanel headerPanel = new JPanel(new BorderLayout());
                    headerPanel.setBackground(new Color(90, 37, 60));
                    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
                    
                    JLabel headerTitle = new JLabel(room.getRoomType());
                    headerTitle.setFont(new Font("Georgia", Font.BOLD, 26));
                    headerTitle.setForeground(Color.WHITE);
                    
                    JLabel headerSubtitle = new JLabel(room.getRoomId());
                    headerSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    headerSubtitle.setForeground(new Color(200, 230, 255));
                    
                    JPanel headerTextPanel = new JPanel();
                    headerTextPanel.setLayout(new BoxLayout(headerTextPanel, BoxLayout.Y_AXIS));
                    headerTextPanel.setBackground(new Color(90, 37, 60));
                    headerTextPanel.add(headerTitle);
                    headerTextPanel.add(Box.createVerticalStrut(5));
                    headerTextPanel.add(headerSubtitle);
                    
                    headerPanel.add(headerTextPanel, BorderLayout.WEST);
                    
                    JLabel statusBadge = createStatusBadge(room.getStatus());
                    headerPanel.add(statusBadge, BorderLayout.EAST);
                    
                    mainContainer.add(headerPanel, BorderLayout.NORTH);
                    
                    JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
                    contentPanel.setBackground(Color.WHITE);
                    contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
                    
                    JPanel imagePanel = new JPanel(new BorderLayout());
                    imagePanel.setBackground(Color.WHITE);
                    imagePanel.setPreferredSize(new Dimension(450, 550));
                    
                    EnhancedImageCarousel carousel = new EnhancedImageCarousel(room, 450, 400);
                    imagePanel.add(carousel.getPanel(), BorderLayout.CENTER);
                    
                    JPanel detailsContainer = new JPanel(new BorderLayout());
                    detailsContainer.setBackground(Color.WHITE);
                    detailsContainer.setPreferredSize(new Dimension(450, 550));
                    
                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                    detailsPanel.setBackground(Color.WHITE);
                    detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
                    
                    JLabel descHeader = new JLabel("Description");
                    descHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
                    descHeader.setForeground(new Color(90, 37, 60));
                    descHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
                    detailsPanel.add(descHeader);
                    detailsPanel.add(Box.createVerticalStrut(8));
                    
                    JLabel desc = new JLabel("<html><p style='width:380px;'>" + room.getDescription() + "</p></html>");
                    desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    desc.setForeground(new Color(60, 60, 60));
                    desc.setAlignmentX(Component.LEFT_ALIGNMENT);
                    detailsPanel.add(desc);
                    detailsPanel.add(Box.createVerticalStrut(20));
                    
                    JLabel detailsHeader = new JLabel("Room Details");
                    detailsHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
                    detailsHeader.setForeground(new Color(90, 37, 60));
                    detailsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
                    detailsPanel.add(detailsHeader);
                    detailsPanel.add(Box.createVerticalStrut(10));
                    
                    String[][] details = {
                        {"🏷️", "Room ID", room.getRoomId()},
                        {"👥", "Capacity", room.getCapacity() + " guests"},
                        {"📍", "Location", room.getLocation()},
                        {"💰", "Price per night", "₱" + String.format("%,d", room.getBasePrice())}
                    };
                    
                    for (String[] detail : details) {
                        JPanel detailRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
                        detailRow.setBackground(Color.WHITE);
                        detailRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                        detailRow.setMaximumSize(new Dimension(400, 35));
                        
                        JLabel iconLabel = new JLabel(detail[0] + " ");
                        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
                        
                        JLabel keyLabel = new JLabel(detail[1] + ": ");
                        keyLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
                        keyLabel.setForeground(new Color(80, 80, 80));
                        
                        JLabel valueLabel = new JLabel(detail[2]);
                        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
                        valueLabel.setForeground(new Color(40, 40, 40));
                        
                        detailRow.add(iconLabel);
                        detailRow.add(keyLabel);
                        detailRow.add(valueLabel);
                        
                        detailsPanel.add(detailRow);
                    }
                    
                    detailsPanel.add(Box.createVerticalStrut(20));
                    
                    JLabel amenitiesHeader = new JLabel("Amenities");
                    amenitiesHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
                    amenitiesHeader.setForeground(new Color(90, 37, 60));
                    amenitiesHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
                    detailsPanel.add(amenitiesHeader);
                    detailsPanel.add(Box.createVerticalStrut(10));
                    
                    String[] amenities = {
                        "📌 Free High-Speed WiFi",
                        "❌ Free Cancellation",
                        "🍽️ Breakfast Included",
                        "💳 Pay at Check-in",
                        "🏊 Pool Access",
                        "🧖 Spa Services"
                    };
                    
                    for (String amenity : amenities) {
                        JLabel amenityLabel = new JLabel(amenity);
                        amenityLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
                        amenityLabel.setForeground(new Color(60, 60, 60));
                        amenityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        amenityLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
                        detailsPanel.add(amenityLabel);
                    }
                    
                    detailsPanel.add(Box.createVerticalGlue());
                    
                    // Book button at bottom
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    buttonPanel.setBackground(Color.WHITE);
                    buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    JButton bookButton = createStyledButton("Book This Room", new Color(90, 37, 60), new Color(70, 27, 45), Color.WHITE);
                    bookButton.setPreferredSize(new Dimension(200, 45));
                    bookButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                    bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    if (room.getStatus().equals("Available")) {
                        bookButton.addActionListener(e -> {
                            dialog.dispose();
                            navigateToHome();
                        });
                    } else {
                        bookButton.setEnabled(false);
                        bookButton.setBackground(Color.GRAY);
                        bookButton.setText("Currently Unavailable");
                    }
                    
                    buttonPanel.add(bookButton);
                    detailsPanel.add(buttonPanel);
                    
                    JScrollPane scrollPane = new JScrollPane(detailsPanel);
                    scrollPane.setBorder(null);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    scrollPane.getViewport().setBackground(Color.WHITE);
                    styleScrollBar(scrollPane);
                    
                    detailsContainer.add(scrollPane, BorderLayout.CENTER);
                    
                    contentPanel.add(imagePanel, BorderLayout.WEST);
                    contentPanel.add(detailsContainer, BorderLayout.CENTER);
                    
                    mainContainer.add(contentPanel, BorderLayout.CENTER);
                    
                    dialog.add(mainContainer);
                    dialog.setVisible(true);
                    
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private class EnhancedImageCarousel {
        private JPanel panel;
        private JLabel imageLabel;
        private JLabel counterLabel;
        private java.util.List<String> imagePaths;
        private int currentIndex = 0;
        
        public EnhancedImageCarousel(AdminModel.RoomUnit room) {
            this(room, 500, 550);
        }
        
        public EnhancedImageCarousel(AdminModel.RoomUnit room, int width, int height) {
            panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
            	    BorderFactory.createLineBorder(new Color(90, 37, 60), 2),
            	    BorderFactory.createLineBorder(Color.WHITE, 5)
            	));
            
            imagePaths = new ArrayList<>();
            
            java.util.List<String> roomImagePaths = room.getImagePaths();
            if (roomImagePaths != null && !roomImagePaths.isEmpty()) {
                imagePaths.addAll(roomImagePaths);
                System.out.println("Loaded " + imagePaths.size() + " admin-uploaded images for room: " + room.getRoomId());
            }
            
            if (imagePaths.isEmpty() && room.getImagePath() != null && !room.getImagePath().isEmpty()) {
                imagePaths.add(room.getImagePath());
                System.out.println("Loaded single image path for room: " + room.getRoomId());
            }
            
            if (imagePaths.isEmpty()) {
                String normalizedTitle = room.getRoomType().replaceAll("\\s+", "_").toLowerCase();
                String roomNumber = extractRoomNumber(room.getRoomId());
                
                String[] defaultPaths = {
                    "images/" + normalizedTitle + "_" + roomNumber + ".jpg",
                    "images/" + normalizedTitle + "_" + roomNumber + "a.jpg",
                    "images/" + normalizedTitle + "_" + roomNumber + "b.jpg",
                    "images/" + normalizedTitle + "_" + roomNumber + "c.jpg",
                    "images/" + normalizedTitle + ".jpg",
                    "images/" + room.getRoomId() + ".jpg",
                    "images/room_placeholder.jpg"
                };
                
                for (String path : defaultPaths) {
                    if (!imagePaths.contains(path)) {
                        imagePaths.add(path);
                    }
                }
                System.out.println("Using default images for room: " + room.getRoomId());
            }
            
            imagePaths = filterExistingImages(imagePaths);
            
            System.out.println("Final image paths for " + room.getRoomId() + ": " + imagePaths);
            
            if (imagePaths.isEmpty()) {
                imagePaths.add("placeholder");
                System.out.println("No images found, using placeholder for room: " + room.getRoomId());
            }
            
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setBackground(new Color(245, 250, 255));
            imageLabel.setOpaque(true);
            imageLabel.setPreferredSize(new Dimension(width, height));
            
            JPanel imageContainer = new JPanel(new GridBagLayout());
            imageContainer.setBackground(new Color(245, 250, 255));
            imageContainer.add(imageLabel);
            
            panel.add(imageContainer, BorderLayout.CENTER);
            
            if (imagePaths.size() > 1) {
                JPanel navPanel = new JPanel(new BorderLayout());
                navPanel.setBackground(new Color(90, 37, 60));
                navPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
                
                JButton prevButton = new JButton("◄ Previous");
                prevButton.setFont(new Font("SansSerif", Font.BOLD, 12));
                prevButton.setBackground(Color.WHITE);
                prevButton.setForeground(new Color(90, 37, 60));
                prevButton.setFocusPainted(false);
                prevButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                prevButton.addActionListener(e -> showPreviousImage());
                
                counterLabel = new JLabel("1 / " + imagePaths.size());
                counterLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
                counterLabel.setForeground(Color.WHITE);
                counterLabel.setHorizontalAlignment(SwingConstants.CENTER);
                
                JButton nextButton = new JButton("Next ►");
                nextButton.setFont(new Font("SansSerif", Font.BOLD, 12));
                nextButton.setBackground(Color.WHITE);
                nextButton.setForeground(new Color(90, 37, 60));
                nextButton.setFocusPainted(false);
                nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                nextButton.addActionListener(e -> showNextImage());
                
                navPanel.add(prevButton, BorderLayout.WEST);
                navPanel.add(counterLabel, BorderLayout.CENTER);
                navPanel.add(nextButton, BorderLayout.EAST);
                
                panel.add(navPanel, BorderLayout.SOUTH);
            } else if (imagePaths.size() == 1) {
                JLabel singleImageLabel = new JLabel("1 image available");
                singleImageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                singleImageLabel.setForeground(Color.WHITE);
                singleImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                singleImageLabel.setBackground(new Color(0, 119, 182));
                singleImageLabel.setOpaque(true);
                singleImageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                panel.add(singleImageLabel, BorderLayout.SOUTH);
            }
            
            showImage(0, width, height);
        }
        
        private void showImage(int index) {
            showImage(index, 500, 550);
        }
        
        private java.util.List<String> filterExistingImages(java.util.List<String> paths) {
            java.util.List<String> existing = new ArrayList<>();
            for (String path : paths) {
                if ("placeholder".equals(path)) {
                    existing.add(path);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        existing.add(path);
                        System.out.println("Image exists: " + path);
                    } else {
                        System.out.println("Image not found: " + path);
                        if (!path.startsWith("images/") && !path.contains("/")) {
                            String imagesPath = "images/" + path;
                            File imagesFile = new File(imagesPath);
                            if (imagesFile.exists()) {
                                existing.add(imagesPath);
                                System.out.println("Found image in images folder: " + imagesPath);
                            }
                        }
                    }
                }
            }
            return existing;
        }
        
        private void showImage(int index, int maxWidth, int maxHeight) {
            if (index < 0 || index >= imagePaths.size()) {
                return;
            }
            
            currentIndex = index;
            String imagePath = imagePaths.get(index);
            
            System.out.println("Showing image " + (index + 1) + "/" + imagePaths.size() + ": " + imagePath);
            
            if ("placeholder".equals(imagePath)) {
                showPlaceholderImage();
            } else {
                ImageIcon icon = loadImage(imagePath);
                if (icon != null && icon.getIconWidth() > 0) {
                    // Calculate scaling to fit within bounds without stretching
                    int imgWidth = icon.getIconWidth();
                    int imgHeight = icon.getIconHeight();
                    
                    double widthRatio = (double) maxWidth / imgWidth;
                    double heightRatio = (double) maxHeight / imgHeight;
                    double scale = Math.min(widthRatio, heightRatio);
                    
                    int scaledWidth = (int) (imgWidth * scale);
                    int scaledHeight = (int) (imgHeight * scale);
                    
                    Image scaled = icon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaled));
                    imageLabel.setBackground(new Color(245, 250, 255));
                    imageLabel.setText("");
                } else {
                    System.out.println("Failed to load image: " + imagePath);
                    showPlaceholderImage();
                }
            }
            
            if (counterLabel != null) {
                counterLabel.setText((currentIndex + 1) + " / " + imagePaths.size());
            }
        }
        
        private void showPlaceholderImage() {
            imageLabel.setIcon(null);
            imageLabel.setText("<html><div style='text-align:center; color:#0077B6;'>" +
                              "📷<br><br>No Image Available<br>" +
                              "<small style='color:#666;'>Admin can upload images in Room Management</small></div></html>");
            imageLabel.setForeground(new Color(0, 119, 182));
            imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            imageLabel.setBackground(new Color(245, 250, 255));
        }
        
        private void showNextImage() {
            if (currentIndex < imagePaths.size() - 1) {
                showImage(currentIndex + 1);
            } else {
                showImage(0);
            }
        }
        
        private void showPreviousImage() {
            if (currentIndex > 0) {
                showImage(currentIndex - 1);
            } else {
                showImage(imagePaths.size() - 1);
            }
        }
        
        public JPanel getPanel() {
            return panel;
        }
    }
    
    public void showRoomDetailsFromHome(String roomType) {
        if (adminModel == null) return;
        for (AdminModel.RoomUnit room : adminModel.getAllRoomUnits()) {
            if (room.getRoomType().toLowerCase().contains(roomType.toLowerCase())) {
                showRoomDetailsDialog(room);
                return;
            }
        }
    }
    
    public void scrollToRoom(String roomType) {
        if (roomsScrollPane == null) return;
        JLabel label = roomSectionLabels.get(roomType);
        if (label != null) {
            SwingUtilities.invokeLater(() -> {
                Rectangle bounds = label.getBounds();
                if (bounds != null) {
                    roomsScrollPane.getViewport().scrollRectToVisible(bounds);
                }
            });
        }
    }
    
    public JScrollPane getRoomsScrollPane() {
        return roomsScrollPane;
    }
    
    public JLabel getRoomSectionLabel(String roomType) {
        return roomSectionLabels.get(roomType);
    }
    
    @Override
    public void updatePanel() {
        if (panel != null) {
            populateRoomCards();
            panel.revalidate();
            panel.repaint();
        }
    }
    
    @Override
    public String getPanelName() {
        return "Rooms";
    }
}