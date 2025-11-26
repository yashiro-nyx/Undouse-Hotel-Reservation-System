package undouse_hotel.view;    

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import undouse_hotel.model.AdminModel;
import undouse_hotel.model.AdminModel.BookingRecord;
import undouse_hotel.model.AdminModel.RoomStatistics;
import undouse_hotel.model.GuestModel;
import undouse_hotel.model.UserModel;
import java.util.Map;
import undouse_hotel.model.AdminModel.RoomAvailabilityStats;
import java.io.File;

public class AdminPanelView {
	private static final Color PLUM = new Color(74, 20, 56); // Deep Violet/Plum
	private static final Color DARK_PLUM = new Color(70, 0, 50);
	private static final Color LIGHT_PLUM = new Color(180, 140, 190); // Accent color
	private static final Color GOLD = new Color(230, 190, 100);
	private static final Color LIGHT_GOLD = new Color(245, 220, 180);
	private static final Color LIGHT_BG = new Color(255, 250, 245); // Cream white
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color SUCCESS_GREEN = new Color(34, 139, 34);
    private static final Color WARNING_ORANGE = new Color(255, 140, 0);
    private static final Color INFO_BLUE = new Color(30, 144, 255);
    private static final Color DANGER_RED = new Color(220, 53, 69);
    private static final Color ERROR_RED = new Color(220, 53, 69);
   
    private JFrame parentFrame;
    private AdminModel adminModel;
    private GuestModel guestModel;
    private UserModel userModel;
    private JDialog adminDialog;
    private JTabbedPane tabbedPane;

    public AdminPanelView(JFrame parentFrame, AdminModel adminModel) {
        this.parentFrame = parentFrame;
        this.adminModel = adminModel;
        this.guestModel = new GuestModel(adminModel);
        this.userModel = UserModel.getInstance();
    }
    
    public void showPanel() {
        try {
            adminDialog = new JDialog(parentFrame, "Admin Dashboard - Undouse Hotel", true);
            
            adminDialog.setUndecorated(true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            adminDialog.setSize(screenSize);
            adminDialog.setLocationRelativeTo(parentFrame);
            adminDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel mainPanel = new GradientPanel();
            mainPanel.setLayout(new BorderLayout());
            
            JPanel headerPanel = createModernHeaderPanel();
            
            tabbedPane = createModernTabbedPane();
            
            tabbedPane.addTab("Dashboard", createEnhancedDashboardPanel());
            tabbedPane.addTab("Bookings", createEnhancedBookingHistoryPanel());
            tabbedPane.addTab("Rooms", createEnhancedRoomStatsPanel());
            tabbedPane.addTab("Guests", createEnhancedGuestRecordsPanel());
            tabbedPane.addTab("Settings", createEnhancedSettingsPanel());
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(tabbedPane, BorderLayout.CENTER);
            
            adminDialog.add(mainPanel);
            adminDialog.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, 
                "Error opening admin dashboard: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createModernHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PLUM);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(100, 80));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Manage your hotel operations efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_GOLD);
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Administrator");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = createModernButton("Logout", GOLD, DARK_PLUM);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.addActionListener(e -> {
            adminDialog.dispose();
            undouse_hotel.UndouseHotelApp.restartApplication();
        });
        userPanel.add(userLabel);
        userPanel.add(logoutButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JTabbedPane createModernTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT) {
            @Override
            public void setSelectedIndex(int index) {
                super.setSelectedIndex(index);
                updateTabAppearances();
            }
        };
        
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(PLUM);
        
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, 
                    int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    g2d.setColor(PLUM);
                    g2d.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 10, 10);
                } else {
                    g2d.setColor(LIGHT_BG);
                    g2d.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 10, 10);
                }
            }
            
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, 
                    int x, int y, int w, int h, boolean isSelected) {

            }
            
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(220, 220, 220));
                g2d.setStroke(new BasicStroke(1));
                int width = tabPane.getWidth();
                int height = tabPane.getHeight();
                g2d.drawRect(0, 0, width-1, height-1);
            }
            
            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
                                   int tabIndex, String title, Rectangle textRect, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    g2d.setColor(Color.WHITE); 
                } else {
                    g2d.setColor(PLUM); 
                }
                
                int x = textRect.x;
                int y = textRect.y + metrics.getAscent();
                g2d.setFont(font);
                g2d.drawString(title, x, y);
            }
        });
        
        return tabbedPane;
    }
    
    private void updateTabAppearances() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component tabComponent = tabbedPane.getTabComponentAt(i);
            if (tabComponent != null) {
                tabComponent.repaint();
            }
        }
    }
    
    private JPanel createEnhancedDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBackground(LIGHT_BG);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(LIGHT_BG);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        cardsPanel.add(createModernStatCard("Total Revenue", 
            String.format("₱%,.2f", adminModel.getTotalRevenue()),
            INFO_BLUE, ""));
        
        cardsPanel.add(createModernStatCard("Total Bookings", 
            String.valueOf(adminModel.getTotalBookings()),
            SUCCESS_GREEN, ""));
        
        cardsPanel.add(createModernStatCard("Popular Room", 
            adminModel.getMostPopularRoom(),
            WARNING_ORANGE, ""));
        
        cardsPanel.add(createModernStatCard("Active Guests", 
            String.valueOf(userModel.getAllUsers().size()),
            new Color(138, 43, 226), ""));
        
        dashboardPanel.add(cardsPanel, BorderLayout.NORTH);
        dashboardPanel.add(createEnhancedRecentBookingsPanel(), BorderLayout.CENTER);
        
        return dashboardPanel;
    }
    
    private JPanel createModernStatCard(String title, String value, Color accentColor, String icon) {
        JPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setForeground(accentColor);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        headerPanel.add(iconLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(titleLabel);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createEnhancedRecentBookingsPanel() {
        JPanel panel = new RoundedPanel(15);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Recent Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PLUM);
        
        JButton viewAllButton = createModernButton("View All", PLUM, DARK_PLUM);
        viewAllButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewAllButton.setPreferredSize(new Dimension(120, 35));
        viewAllButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(viewAllButton, BorderLayout.EAST);
        
        List<BookingRecord> bookings = adminModel.getAllBookings();
        int recentCount = Math.min(5, bookings.size());
        List<BookingRecord> recentBookings = bookings.subList(
            Math.max(0, bookings.size() - recentCount), bookings.size()
        );
        
        String[] columns = {"Receipt", "Guest", "Room", "Check-in", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        for (BookingRecord booking : recentBookings) {
            model.addRow(new Object[]{
                booking.getReceiptNumber(),
                censorName(booking.getGuestName()),  // ✅ CENSORED
                booking.getRoomType(),
                booking.getCheckInDate(),
                String.format("₱%,.2f", booking.getTotalAmount()),
                "Completed"
            });
        }
        
        JTable table = createStyledTable(model);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1 && i != 2) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        styleScrollPane(scrollPane);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    

    private JPanel createEnhancedBookingHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Booking History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PLUM);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(LIGHT_BG);
        
        JButton refreshButton = createModernButton("Refresh", PLUM, DARK_PLUM);
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JButton exportButton = createModernButton("Export CSV", SUCCESS_GREEN, new Color(0, 100, 0));
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        actionPanel.add(refreshButton);
        actionPanel.add(exportButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);
        
        String[] columns = {"Receipt", "Booking Date", "Guest", "Email", "Room", "Check-in", "Check-out", "Payment", "Amount", "Status", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { 
                return column == 10; // Only Action column is editable
            }
        };
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        List<BookingRecord> allBookings = adminModel.getAllBookings();
        for (BookingRecord booking : allBookings) {
            String status = booking.getStatus() != null ? booking.getStatus() : "Pending";
            model.addRow(new Object[]{
                booking.getReceiptNumber(),
                sdf.format(booking.getBookingDate()),
                censorName(booking.getGuestName()),  // ✅ CENSORED
                censorEmail(booking.getEmail() != null ? booking.getEmail() : "N/A"),  // ✅ CENSORED
                booking.getRoomType(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getPaymentMethod(),
                String.format("₱%,.2f", booking.getTotalAmount()),
                status,
                "Change Status"
            });
        }
        
     JTable table = createStyledTable(model);

     table.setRowHeight(45);

     TableColumn actionColumn = table.getColumnModel().getColumn(10);
     actionColumn.setCellRenderer(new ButtonRenderer());
     actionColumn.setCellEditor(new ButtonEditor(new JCheckBox(), table, model));
     actionColumn.setPreferredWidth(140);
     actionColumn.setMinWidth(140);
     actionColumn.setMaxWidth(160);     
     table.getColumnModel().getColumn(9).setCellRenderer(new StatusCellRenderer());

     DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
     centerRenderer.setHorizontalAlignment(JLabel.CENTER);
     int[] centerColumns = {0, 1, 4, 5, 6, 7};  
     for (int col : centerColumns) {
         table.getColumnModel().getColumn(col).setCellRenderer(centerRenderer);
     }

     table.addMouseListener(new MouseAdapter() {
    	    @Override
    	    public void mouseClicked(MouseEvent e) {
    	        int column = table.columnAtPoint(e.getPoint());
    	        int row = table.rowAtPoint(e.getPoint());
    	        
    	        if (column == 10 && row >= 0) {
    	            
    	            table.editCellAt(row, column);
    	        }
    	    }
    	});

    	JScrollPane scrollPane = new JScrollPane(table);
    	styleScrollPane(scrollPane);
     
        refreshButton.addActionListener(e -> {
            model.setRowCount(0); 
            SimpleDateFormat refreshSdf = new SimpleDateFormat("MMM dd, yyyy");
            List<BookingRecord> refreshedBookings = adminModel.getAllBookings();
            for (BookingRecord booking : refreshedBookings) {
                String status = booking.getStatus() != null ? booking.getStatus() : "Pending";
                model.addRow(new Object[]{
                    booking.getReceiptNumber(),
                    refreshSdf.format(booking.getBookingDate()),
                    censorName(booking.getGuestName()),  
                    censorEmail(booking.getEmail() != null ? booking.getEmail() : "N/A"), 
                    booking.getRoomType(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getPaymentMethod(),
                    String.format("₱%,.2f", booking.getTotalAmount()),
                    status,
                    "Change Status"
                });
            }
            JOptionPane.showMessageDialog(panel, "Booking data refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        exportButton.addActionListener(e -> exportBookingsToCSV(table));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEnhancedRoomStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JLabel titleLabel = new JLabel("Room Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PLUM);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String[] columns = {
            "Room Type", 
            "Available", 
            "Occupied", 
            "Total",
            "Occupancy %",
            "Bookings", 
            "Revenue", 
            "Avg/Booking"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { 
                return false;
            }
        };
        
        Map<String, AdminModel.RoomAvailabilityStats> availStats = adminModel.getRoomAvailabilityStatistics();
        Map<String, AdminModel.RoomStatistics> roomStats = adminModel.getRoomStatistics();
        
        for (Map.Entry<String, AdminModel.RoomAvailabilityStats> entry : availStats.entrySet()) {
            String roomType = entry.getKey();
            AdminModel.RoomAvailabilityStats availStat = entry.getValue();
            AdminModel.RoomStatistics roomStat = roomStats.get(roomType);
            
            int totalBookings = (roomStat != null) ? roomStat.getBookingCount() : 0;
            double totalRevenue = (roomStat != null) ? roomStat.getTotalRevenue() : 0.0;
            double avgRevenue = totalBookings > 0 ? totalRevenue / totalBookings : 0;
            
            model.addRow(new Object[]{
                roomType,
                availStat.getAvailableRooms(),
                availStat.getOccupiedRooms(),
                availStat.getTotalRooms(),
                String.format("%.1f%%", availStat.getOccupancyRate()),
                totalBookings,
                String.format("₱%,.2f", totalRevenue),
                String.format("₱%,.2f", avgRevenue)
            });
        }
        
        JTable table = createStyledTable(model);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        styleScrollPane(scrollPane);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEnhancedGuestRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Guest Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PLUM);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(LIGHT_BG);
        
        JButton refreshButton = createModernButton("Refresh", PLUM, DARK_PLUM);
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JButton exportButton = createModernButton("Export List", SUCCESS_GREEN, new Color(0, 100, 0));
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        String[] columns = {"Name", "Email", "Phone", "City", "Country", "Registered", "Last Login", "Bookings"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        List<UserModel.User> users = userModel.getAllUsers();
        
        if (users.isEmpty()) {
            model.addRow(new Object[]{"Sample User", "sample@email.com", "555-1234", "Manila", "Philippines", "Jan 01, 2025", "Jan 15, 2025", 0});
        } else {
            for (UserModel.User user : users) {
                model.addRow(new Object[]{
                    censorName(user.getFullName()),  
                    censorEmail(user.getEmail()),    
                    censorPhone(user.getPhone()),    
                    user.getCity(),
                    user.getCountry(),
                    user.getFormattedRegistrationDate(),
                    user.getFormattedLastLogin(),
                    user.getTotalBookings()
                });
            }
        }
        
        JTable table = createStyledTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(table);
        styleScrollPane(scrollPane);
        
        refreshButton.addActionListener(e -> {
            model.setRowCount(0);
            List<UserModel.User> refreshedUsers = userModel.getAllUsers();
            
            if (refreshedUsers.isEmpty()) {
                model.addRow(new Object[]{"No users registered", "", "", "", "", "", "", 0});
            } else {
                for (UserModel.User user : refreshedUsers) {
                    model.addRow(new Object[]{
                        censorName(user.getFullName()), 
                        censorEmail(user.getEmail()),    
                        censorPhone(user.getPhone()),    
                        user.getCity(),
                        user.getCountry(),
                        user.getFormattedRegistrationDate(),
                        user.getFormattedLastLogin(),
                        user.getTotalBookings()
                    });
                }
            }
            JOptionPane.showMessageDialog(panel, "User data refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        exportButton.addActionListener(e -> exportUsersToCSV(table));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createEnhancedSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("Manage system configuration and preferences");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(40));
        
        panel.add(createModernSettingOption("Security", "Update admin credentials and username", this::showSecurityDialog));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createModernSettingOption("Export All Data", "Export all booking records to CSV file", this::exportAllData));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createModernSettingOption("System Backup", "Create complete backup of system data", this::createSystemBackup));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createModernSettingOption("Room Management", "View and manage room inventory", this::showRoomManagementDialog));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createModernSettingOption("Clear Sample Data", "Remove all sample/demo booking records", () -> clearSampleData()));
        panel.add(Box.createVerticalGlue());
        return panel;
    }    

    private JPanel createModernSettingOption(String title, String description, Runnable action) {
        JPanel optionPanel = new RoundedPanel(10);
        optionPanel.setLayout(new BorderLayout(15, 0));
        optionPanel.setBackground(CARD_BG);
        optionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        optionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        optionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        optionPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                optionPanel.setBackground(new Color(248, 248, 248));
                optionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PLUM, 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                optionPanel.setBackground(CARD_BG);
                optionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
        });
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PLUM);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(120, 120, 120));
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);
        
        JButton actionButton = createModernButton("Configure", PLUM, DARK_PLUM);
        actionButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        actionButton.addActionListener(e -> action.run());
        
        optionPanel.add(textPanel, BorderLayout.CENTER);
        optionPanel.add(actionButton, BorderLayout.EAST);
        
        return optionPanel;
    }
    
    
    private void showSecurityDialog() {
        JDialog dialog = new JDialog(adminDialog, "Admin Security Settings", true);
        
        dialog.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setSize(screenSize);
        dialog.setLocationRelativeTo(adminDialog);
        dialog.getContentPane().setBackground(LIGHT_BG);
        
        JPanel mainPanel = new RoundedPanel(10);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        
        JLabel titleLabel = new JLabel("Security Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Update your admin credentials");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        JLabel currentUserLabel = new JLabel("Current Username:");
        currentUserLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        currentUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField currentUserField = new JTextField(adminModel.getAdminUsername());
        currentUserField.setEditable(false);
        currentUserField.setBackground(new Color(245, 245, 245));
        styleTextField(currentUserField);
        currentUserField.setMaximumSize(new Dimension(400, 40));
        currentUserField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(currentUserLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(currentUserField);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JLabel newUserLabel = new JLabel("New Username:");
        newUserLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField newUserField = new JTextField(20);
        styleTextField(newUserField);
        newUserField.setMaximumSize(new Dimension(400, 40));
        newUserField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(newUserLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(newUserField);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JLabel currentPassLabel = new JLabel("Current Password:");
        currentPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        currentPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPasswordField currentPassField = createStyledPasswordField();
        currentPassField.setMaximumSize(new Dimension(400, 40));
        currentPassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(currentPassLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(currentPassField);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPasswordField newPassField = createStyledPasswordField();
        newPassField.setMaximumSize(new Dimension(400, 40));
        newPassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel newPassPanel = createPasswordFieldWithEye(newPassField);
        
        mainPanel.add(newPassLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(newPassPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JLabel confirmPassLabel = new JLabel("Confirm New Password:");
        confirmPassLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPasswordField confirmPassField = createStyledPasswordField();
        confirmPassField.setMaximumSize(new Dimension(400, 40));
        confirmPassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel confirmPassPanel = createPasswordFieldWithEye(confirmPassField);
        
        mainPanel.add(confirmPassLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(confirmPassPanel);
        mainPanel.add(Box.createVerticalStrut(25));
        
        JPanel strengthPanel = createPasswordStrengthPanel(newPassField, confirmPassField);
        strengthPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(strengthPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        JPanel requirementsPanel = createPasswordRequirementsPanel();
        requirementsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(requirementsPanel);
        mainPanel.add(Box.createVerticalStrut(25));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(500, 60));
        
        JButton updateButton = createModernButton("Update Security", PLUM, DARK_PLUM);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setPreferredSize(new Dimension(180, 45));
        
        JButton cancelButton = createModernButton("Cancel", Color.GRAY, Color.DARK_GRAY);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(120, 45));
        
        JButton closeButton = createModernButton("Close", INFO_BLUE, new Color(0, 100, 200));
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setPreferredSize(new Dimension(120, 45));
        closeButton.addActionListener(e -> dialog.dispose());
        
        updateButton.addActionListener(e -> {
            String currentUsername = adminModel.getAdminUsername();
            String newUsername = newUserField.getText().trim();
            String currentPassword = new String(currentPassField.getPassword());
            String newPassword = new String(newPassField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());
            
            boolean usernameChanged = !newUsername.isEmpty() && !newUsername.equals(currentUsername);
            boolean passwordChanged = !newPassword.isEmpty();
            
            if (!usernameChanged && !passwordChanged) {
                JOptionPane.showMessageDialog(dialog, 
                    "No changes detected. Please enter new username or password.", 
                    "No Changes", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if (!adminModel.authenticate(currentUsername, currentPassword)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Current password is incorrect.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (passwordChanged) {
                if (newPassword.length() < 8) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Password must be at least 8 characters.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "New passwords do not match.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!isStrongPassword(newPassword)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Password does not meet strength requirements.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (usernameChanged) {
                if (!adminModel.changeUsername(newUsername)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Failed to update username.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (passwordChanged) {
                if (!adminModel.changePassword(currentPassword, newPassword)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Failed to update password.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            JOptionPane.showMessageDialog(dialog, 
                "Security settings updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            newUserField.setText("");
            currentPassField.setText("");
            newPassField.setText("");
            confirmPassField.setText("");
        });
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel);
        
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(LIGHT_BG);
        centerWrapper.add(mainPanel);
        
        dialog.add(centerWrapper);
        dialog.setVisible(true);
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(400, 40));
        field.setBackground(new Color(250, 250, 250));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }
    
    private JPanel createPasswordFieldWithEye(JPasswordField passwordField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 40));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton eyeButton = new JButton("👁");
        eyeButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        eyeButton.setPreferredSize(new Dimension(50, 40));
        eyeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        eyeButton.setBackground(new Color(250, 250, 250));
        eyeButton.setFocusPainted(false);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        eyeButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == '\u0000') {
                passwordField.setEchoChar('•');
            } else {
                passwordField.setEchoChar('\u0000');
            }
        });
        
        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(eyeButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createPasswordStrengthPanel(JPasswordField passwordField, JPasswordField confirmField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(400, 60));
        
        JPanel strengthBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        strengthBarPanel.setOpaque(false);
        
        JLabel strengthTextLabel = new JLabel("Password Strength: ");
        strengthTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        strengthTextLabel.setForeground(new Color(100, 100, 100));
        
        JProgressBar passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setForeground(ERROR_RED);
        passwordStrengthBar.setBackground(new Color(240, 240, 240));
        passwordStrengthBar.setPreferredSize(new Dimension(120, 10));
        passwordStrengthBar.setStringPainted(false);
        
        JLabel passwordStrengthLabel = new JLabel("Weak");
        passwordStrengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordStrengthLabel.setForeground(ERROR_RED);
        
        strengthBarPanel.add(strengthTextLabel);
        strengthBarPanel.add(passwordStrengthBar);
        strengthBarPanel.add(Box.createHorizontalStrut(10));
        strengthBarPanel.add(passwordStrengthLabel);
        
        JLabel passwordMatchLabel = new JLabel("Passwords do not match");
        passwordMatchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordMatchLabel.setForeground(ERROR_RED);
        passwordMatchLabel.setVisible(false);
        
        JPanel matchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        matchPanel.setOpaque(false);
        matchPanel.add(passwordMatchLabel);
        
        panel.add(strengthBarPanel);
        panel.add(matchPanel);
        
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePasswordStrength(passwordField, passwordStrengthBar, passwordStrengthLabel);
                checkPasswordMatch(passwordField, confirmField, passwordMatchLabel);
            }
        });
        
        confirmField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkPasswordMatch(passwordField, confirmField, passwordMatchLabel);
            }
        });
        
        return panel;
    }
    
    private void updatePasswordStrength(JPasswordField passwordField, JProgressBar strengthBar, JLabel strengthLabel) {
        String password = new String(passwordField.getPassword());
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
            strengthBar.setForeground(new Color(241, 196, 15));
            strengthLabel.setText("Good");
            strengthLabel.setForeground(new Color(241, 196, 15));
        } else {
            strengthBar.setForeground(SUCCESS_GREEN);
            strengthLabel.setText("Strong");
            strengthLabel.setForeground(SUCCESS_GREEN);
        }
    }
    
    private void checkPasswordMatch(JPasswordField passwordField, JPasswordField confirmField, JLabel matchLabel) {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmField.getPassword());
        
        if (confirmPassword.isEmpty()) {
            matchLabel.setVisible(false);
        } else if (password.equals(confirmPassword)) {
            matchLabel.setText("✓ Passwords match");
            matchLabel.setForeground(SUCCESS_GREEN);
            matchLabel.setVisible(true);
        } else {
            matchLabel.setText("✗ Passwords do not match");
            matchLabel.setForeground(ERROR_RED);
            matchLabel.setVisible(true);
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
    
    private JPanel createPasswordRequirementsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(400, 160));
        
        JLabel reqTitle = new JLabel("Password Requirements:");
        reqTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        reqTitle.setForeground(PLUM);
        reqTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] requirements = {
            "• At least 8 characters",
            "• At least one uppercase letter (A-Z)",
            "• At least one lowercase letter (a-z)",
            "• At least one number (0-9)",
            "• At least one special character (!@#$%^&* etc.)"
        };
        
        panel.add(reqTitle);
        panel.add(Box.createVerticalStrut(8));
        
        for (String req : requirements) {
            JLabel label = new JLabel(req);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(new Color(120, 120, 120));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        }
        
        return panel;
    }
    
    
    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        
        return button;
    }
    
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(true);
                }
                
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                }
                
                return c;
            }
        };
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setSelectionForeground(PLUM);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PLUM);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        return table;
    }
    
    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUnitIncrement(16);
        vertical.setBackground(LIGHT_BG);
        
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    private class StatusRenderer extends DefaultTableCellRenderer {
        public StatusRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null && value.toString().contains("✅")) {
                setBackground(new Color(220, 255, 220));
                setForeground(SUCCESS_GREEN);
                setFont(getFont().deriveFont(Font.BOLD));
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
    }
    
    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        
        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        }
    }
    
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color color1 = new Color(255, 248, 240);
            Color color2 = new Color(245, 238, 230);
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(new Color(250, 250, 250));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    private void exportAllData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("📊 Export All Booking Data");
        fileChooser.setSelectedFile(new File("UndouseHotel_AllBookings_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(adminDialog) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Receipt Number,Booking Date,Guest Name,Guest Email,Room Type,Check-in,Check-out,Payment Method,Amount");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (BookingRecord booking : adminModel.getAllBookings()) {
                    writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%.2f%n",
                        booking.getReceiptNumber(),
                        sdf.format(booking.getBookingDate()),
                        booking.getGuestName(),
                        booking.getEmail() != null ? booking.getEmail() : "N/A",
                        booking.getRoomType(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getPaymentMethod(),
                        booking.getTotalAmount()
                    );
                }
                JOptionPane.showMessageDialog(adminDialog, "Data exported successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(adminDialog, "Error exporting data: " + ex.getMessage(), "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportBookingsToCSV(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Bookings");
        fileChooser.setSelectedFile(new File("Bookings_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(adminDialog) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (int i = 0; i < table.getColumnCount(); i++) {
                    writer.print(table.getColumnName(i));
                    if (i < table.getColumnCount() - 1) writer.print(",");
                }
                writer.println();
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        writer.print(table.getValueAt(i, j));
                        if (j < table.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }
                JOptionPane.showMessageDialog(adminDialog, "Bookings exported successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(adminDialog, "Error exporting: " + ex.getMessage(), "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportUsersToCSV(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export User Records");
        fileChooser.setSelectedFile(new File("RegisteredUsers_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(adminDialog) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (int i = 0; i < table.getColumnCount(); i++) {
                    writer.print(table.getColumnName(i));
                    if (i < table.getColumnCount() - 1) writer.print(",");
                }
                writer.println();
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        writer.print(table.getValueAt(i, j));
                        if (j < table.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }
                JOptionPane.showMessageDialog(adminDialog, "User records exported successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(adminDialog, "Error exporting: " + ex.getMessage(), "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createSystemBackup() {
        int confirm = JOptionPane.showConfirmDialog(adminDialog,
            "💾 Create a complete system backup?\nThis will backup all booking records and guest information.",
            "System Backup Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("💾 Save Backup File");
            fileChooser.setSelectedFile(new File("UndouseHotel_Backup_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
            
            if (fileChooser.showSaveDialog(adminDialog) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    writer.println("=== Undouse Hotel SYSTEM BACKUP ===");
                    writer.println("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    writer.println("Total Bookings: " + adminModel.getTotalBookings());
                    writer.println("Total Revenue: ₱" + String.format("%,.2f", adminModel.getTotalRevenue()));
                    writer.println();
                    
                    for (BookingRecord booking : adminModel.getAllBookings()) {
                        writer.println("Receipt: " + booking.getReceiptNumber());
                        writer.println("Guest: " + booking.getGuestName());
                        writer.println("Amount: ₱" + String.format("%.2f", booking.getTotalAmount()));
                        writer.println("---");
                    }
                    
                    JOptionPane.showMessageDialog(adminDialog, "System backup completed successfully!", "Backup Complete", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(adminDialog, "Backup failed: " + ex.getMessage(), "Backup Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showRoomManagementDialog() {
        JDialog dialog = new JDialog(adminDialog, "Room Inventory Management", true);
        dialog.setSize(1000, 650);
        dialog.setLocationRelativeTo(adminDialog);
        dialog.getContentPane().setBackground(LIGHT_BG);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(LIGHT_BG);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_BG);
        
        JLabel titleLabel = new JLabel("Room Inventory Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PLUM);
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        statusPanel.setBackground(LIGHT_BG);
        
        Map<String, Integer> statusCount = adminModel.getRoomCountByStatus();
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            JLabel statusLabel = new JLabel(entry.getKey() + ": " + entry.getValue());
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            statusPanel.add(statusLabel);
        }
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statusPanel, BorderLayout.EAST);
        
        String[] columns = {"Room ID", "Type", "Capacity", "Price", "Status", "Location", "Last Updated"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        List<AdminModel.RoomUnit> rooms = adminModel.getAllRoomUnits();
        for (AdminModel.RoomUnit room : rooms) {
            model.addRow(new Object[]{
                room.getRoomId(),
                room.getRoomType(),
                room.getCapacity() + " guests",
                "₱" + String.format("%,d", room.getBasePrice()),
                room.getStatus(),
                room.getLocation(),
                sdf.format(room.getLastUpdated())
            });
        }
        
        JTable table = createStyledTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        styleScrollPane(scrollPane);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(LIGHT_BG);
        
        JButton addButton = createModernButton("Add Room", new Color(0, 150, 0), new Color(0, 120, 0));
        addButton.addActionListener(e -> showAddRoomDialog(dialog, model));
        
        JButton editButton = createModernButton("Edit Room", INFO_BLUE, new Color(0, 100, 200));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String roomId = (String) table.getValueAt(selectedRow, 0);
                showEditRoomDialog(dialog, model, roomId, selectedRow);
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a room to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton statusButton = createModernButton("Change Status", WARNING_ORANGE, new Color(200, 100, 0));
        statusButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String roomId = (String) table.getValueAt(selectedRow, 0);
                showChangeStatusDialog(dialog, model, roomId, selectedRow);
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a room to change status.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton deleteButton = createModernButton("🗑Delete Room", DANGER_RED, new Color(180, 0, 0));
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String roomId = (String) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Delete room " + roomId + "? This cannot be undone.",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (adminModel.deleteRoomUnit(roomId)) {
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(dialog, "Room deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a room to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton closeButton = createModernButton("Close", Color.GRAY, Color.DARK_GRAY);
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(statusButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(LIGHT_BG);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel infoLabel = new JLabel("Select a room and use buttons below to manage");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoPanel.add(infoLabel);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(LIGHT_BG);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void showAddRoomDialog(JDialog parentDialog, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(parentDialog, "Add New Room", true);
        dialog.setSize(600, 850);
        dialog.setLocationRelativeTo(parentDialog);
        dialog.getContentPane().setBackground(LIGHT_BG);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel panel = new RoundedPanel(10);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel roomIdLabel = new JLabel("Room ID:");
        roomIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(roomIdLabel, gbc);
        
        gbc.gridx = 1;
        JTextField roomIdField = new JTextField(15);
        styleTextField(roomIdField);
        panel.add(roomIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(typeLabel, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "Classic Room", "Standard Deluxe Room", "Premier Deluxe Room", 
            "Executive Suite", "Family Suite", "Presidential Suite"
        });
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel capacityLabel = new JLabel("Capacity:");
        capacityLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(capacityLabel, gbc);
        
        gbc.gridx = 1;
        JTextField capacityField = new JTextField(15);
        styleTextField(capacityField);
        panel.add(capacityField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel priceLabel = new JLabel("Base Price:");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        JTextField priceField = new JTextField(15);
        styleTextField(priceField);
        panel.add(priceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{
            "Available", "Occupied", "Maintenance", "Out of Service"
        });
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(locationLabel, gbc);
        
        gbc.gridx = 1;
        JTextField locationField = new JTextField(15);
        styleTextField(locationField);
        panel.add(locationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(descLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextArea descriptionArea = new JTextArea(3, 15);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(200, 60));
        panel.add(descScroll, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel imageLabel = new JLabel("Room Images:");
        imageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(imageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel imageUploadPanel = new JPanel(new BorderLayout(10, 10));
        imageUploadPanel.setBackground(Color.WHITE);
        
        final java.util.List<String> selectedImagePaths = new ArrayList<>();
        JLabel imagePreviewLabel = new JLabel("📷 No Images Selected");
        imagePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 120));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        imagePreviewLabel.setBackground(new Color(245, 245, 245));
        imagePreviewLabel.setOpaque(true);
        
        Runnable updateImagePreview = () -> {
            if (!selectedImagePaths.isEmpty()) {
                try {
                    String firstImagePath = selectedImagePaths.get(0);
                    ImageIcon icon = new ImageIcon(firstImagePath);
                    if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                        Image img = icon.getImage().getScaledInstance(180, 100, Image.SCALE_SMOOTH);
                        imagePreviewLabel.setIcon(new ImageIcon(img));
                        imagePreviewLabel.setText("<html><center>Main Image<br><small>" + selectedImagePaths.size() + " image(s) uploaded</small></center></html>");
                    } else {
                        imagePreviewLabel.setIcon(null);
                        imagePreviewLabel.setText("📷 Invalid Image");
                    }
                } catch (Exception ex) {
                    imagePreviewLabel.setIcon(null);
                    imagePreviewLabel.setText("📷 Load Error");
                }
            } else {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("📷 No Images Selected");
            }
        };
        
        JButton browseImageButton = createModernButton("Upload Images", INFO_BLUE, new Color(0, 100, 200));
        browseImageButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        browseImageButton.setPreferredSize(new Dimension(200, 35));
        
        browseImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Room Images");
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif", "bmp"));
            
            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                
                try {
                    String imagesDir = "images/";
                    File imagesFolder = new File(imagesDir);
                    if (!imagesFolder.exists()) {
                        imagesFolder.mkdirs();
                    }
                    
                    String roomId = roomIdField.getText().trim();
                    if (roomId.isEmpty()) {
                        roomId = "new_room";
                    }
                    
                    for (File selectedFile : selectedFiles) {
                      
                        String fileExtension = getFileExtension(selectedFile.getName());
                        String newFileName = roomId + "_" + System.currentTimeMillis() + "_" + selectedImagePaths.size() + fileExtension;
                        File destinationFile = new File(imagesDir + newFileName);
                        
                        java.nio.file.Files.copy(
                            selectedFile.toPath(), 
                            destinationFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                        );
                        selectedImagePaths.add(destinationFile.getPath());
                    }
                    
                    updateImagePreview.run();
                    
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ " + selectedFiles.length + " image(s) uploaded successfully!\nThey will appear in room details view.", 
                        "Upload Success", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, 
                        "❌ Failed to upload images: " + ex.getMessage(), 
                        "Upload Error", JOptionPane.ERROR_MESSAGE);
                    imagePreviewLabel.setText("❌ Upload Failed");
                }
            }
        });
        
        JLabel imageHelpLabel = new JLabel("<html><small><i>Uploaded images will appear in room details view for customers</i></small></html>");
        imageHelpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        imageHelpLabel.setForeground(new Color(100, 100, 100));
        
        JPanel imageButtonPanel = new JPanel();
        imageButtonPanel.setLayout(new BoxLayout(imageButtonPanel, BoxLayout.Y_AXIS));
        imageButtonPanel.setBackground(Color.WHITE);
        imageButtonPanel.add(browseImageButton);
        imageButtonPanel.add(Box.createVerticalStrut(5));
        imageButtonPanel.add(imageHelpLabel);
        
        imageUploadPanel.add(imagePreviewLabel, BorderLayout.NORTH);
        imageUploadPanel.add(imageButtonPanel, BorderLayout.SOUTH);
        panel.add(imageUploadPanel, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createModernButton("💾 Save Room", SUCCESS_GREEN, new Color(0, 120, 0));
        saveButton.addActionListener(e -> {
            try {
                String roomId = roomIdField.getText().trim();
                String roomType = (String) typeCombo.getSelectedItem();
                String capacity = capacityField.getText().trim();
                String priceText = priceField.getText().trim();
                String status = (String) statusCombo.getSelectedItem();
                String location = locationField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                if (roomId.isEmpty() || capacity.isEmpty() || priceText.isEmpty() || 
                    location.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please fill all required fields.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (roomId.length() < 2) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Room ID must be at least 2 characters long.", "Invalid Room ID", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int basePrice;
                try {
                    basePrice = Integer.parseInt(priceText);
                    if (basePrice <= 0) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Price must be greater than 0.", "Invalid Price", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please enter a valid number for price.", "Invalid Price", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (adminModel.getRoomUnitById(roomId) != null) {
                    JOptionPane.showMessageDialog(dialog, 
                        "❌ Room ID already exists! Please choose a different Room ID.", 
                        "Duplicate Room ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                AdminModel.RoomUnit newRoom = new AdminModel.RoomUnit(roomId, roomType, capacity, basePrice, status, location, description);
                newRoom.setImagePaths(selectedImagePaths); // Store ALL images
                
                if (adminModel.addRoomUnit(newRoom)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                    tableModel.addRow(new Object[]{
                        roomId, roomType, capacity + " guests", 
                        "₱" + String.format("%,d", basePrice), 
                        status, location, sdf.format(new Date())
                    });
                    
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ Room added successfully!\n" + 
                        (selectedImagePaths.isEmpty() ? "No images uploaded." : selectedImagePaths.size() + " image(s) uploaded and will appear in room details."), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "❌ Failed to add room. Room ID might already exist.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Invalid price format! Please enter a valid number.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Error adding room: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = createModernButton("Cancel", Color.GRAY, Color.DARK_GRAY);
        cancelButton.addActionListener(e -> {
    
            if (!selectedImagePaths.isEmpty()) {
                for (String imagePath : selectedImagePaths) {
                    File uploadedFile = new File(imagePath);
                    if (uploadedFile.exists() && uploadedFile.getName().startsWith("new_room_")) {
                        try {
                            uploadedFile.delete();
                        } catch (Exception ex) {
                            System.err.println("Could not delete temporary image file: " + ex.getMessage());
                        }
                    }
                }
            }
            dialog.dispose();
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 8, 8, 8);
        panel.add(buttonPanel, gbc);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void showEditRoomDialog(JDialog parentDialog, DefaultTableModel tableModel, String roomId, int row) {
        AdminModel.RoomUnit room = adminModel.getRoomUnitById(roomId);
        if (room == null) return;
        
        JDialog dialog = new JDialog(parentDialog, "Edit Room - " + roomId, true);
        dialog.setSize(600, 850);
        dialog.setLocationRelativeTo(parentDialog);
        dialog.getContentPane().setBackground(LIGHT_BG);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel panel = new RoundedPanel(10);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel roomIdLabel = new JLabel("Room ID:");
        roomIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(roomIdLabel, gbc);
        
        gbc.gridx = 1;
        JLabel roomIdValue = new JLabel(room.getRoomId());
        roomIdValue.setFont(new Font("Segoe UI", Font.BOLD, 13));
        roomIdValue.setForeground(PLUM);
        panel.add(roomIdValue, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(typeLabel, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "Classic Room", "Standard Deluxe Room", "Premier Deluxe Room", 
            "Executive Suite", "Family Suite", "Presidential Suite"
        });
        typeCombo.setSelectedItem(room.getRoomType());
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel capacityLabel = new JLabel("Capacity:");
        capacityLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(capacityLabel, gbc);
        
        gbc.gridx = 1;
        JTextField capacityField = new JTextField(room.getCapacity(), 15);
        styleTextField(capacityField);
        panel.add(capacityField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel priceLabel = new JLabel("Base Price:");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        JTextField priceField = new JTextField(String.valueOf(room.getBasePrice()), 15);
        styleTextField(priceField);
        panel.add(priceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{
            "Available", "Occupied", "Maintenance", "Out of Service"
        });
        statusCombo.setSelectedItem(room.getStatus());
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(locationLabel, gbc);
        
        gbc.gridx = 1;
        JTextField locationField = new JTextField(room.getLocation(), 15);
        styleTextField(locationField);
        panel.add(locationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(descLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextArea descriptionArea = new JTextArea(room.getDescription(), 3, 15);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(200, 60));
        panel.add(descScroll, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel imageLabel = new JLabel("Room Images:");
        imageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(imageLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel imageUploadPanel = new JPanel(new BorderLayout(10, 10));
        imageUploadPanel.setBackground(Color.WHITE);
        
        final java.util.List<String> selectedImagePaths = new ArrayList<>();
        
        java.util.List<String> existingImages = room.getImagePaths();
        if (existingImages != null && !existingImages.isEmpty()) {
            selectedImagePaths.addAll(existingImages);
        }
        
        JLabel imagePreviewLabel = new JLabel();
        imagePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 120));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        imagePreviewLabel.setBackground(new Color(245, 245, 245));
        imagePreviewLabel.setOpaque(true);
        
        Runnable updateImagePreview = () -> {
            if (!selectedImagePaths.isEmpty()) {
                try {
                    String firstImagePath = selectedImagePaths.get(0);
                    ImageIcon icon = new ImageIcon(firstImagePath);
                    if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                        Image img = icon.getImage().getScaledInstance(180, 100, Image.SCALE_SMOOTH);
                        imagePreviewLabel.setIcon(new ImageIcon(img));
                        imagePreviewLabel.setText("<html><center>Main Image<br><small>" + selectedImagePaths.size() + " image(s) selected</small></center></html>");
                    } else {
                        imagePreviewLabel.setIcon(null);
                        imagePreviewLabel.setText("📷 Invalid Image");
                    }
                } catch (Exception ex) {
                    imagePreviewLabel.setIcon(null);
                    imagePreviewLabel.setText("📷 Load Error");
                }
            } else {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("📷 No Images Selected");
            }
        };
        
        updateImagePreview.run();
        
        JPanel imageButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        imageButtonPanel.setBackground(Color.WHITE);
        
        JButton browseImageButton = createModernButton("Upload More", INFO_BLUE, new Color(0, 100, 200));
        browseImageButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        browseImageButton.setPreferredSize(new Dimension(120, 32));
        
        JButton removeImageButton = createModernButton("Remove All", DANGER_RED, new Color(180, 0, 0));
        removeImageButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        removeImageButton.setPreferredSize(new Dimension(100, 32));
        
        browseImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Room Images");
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif", "bmp"));
            
            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                
                try {
                    
                    String imagesDir = "images/";
                    File imagesFolder = new File(imagesDir);
                    if (!imagesFolder.exists()) {
                        imagesFolder.mkdirs();
                    }
                    
                    for (File selectedFile : selectedFiles) {
                        
                        String fileExtension = getFileExtension(selectedFile.getName());
                        String newFileName = roomId + "_" + System.currentTimeMillis() + "_" + selectedImagePaths.size() + fileExtension;
                        File destinationFile = new File(imagesDir + newFileName);
                        
                        java.nio.file.Files.copy(
                            selectedFile.toPath(), 
                            destinationFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                        );
                        
                        selectedImagePaths.add(destinationFile.getPath());
                    }
                    
                    updateImagePreview.run();
                    
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ " + selectedFiles.length + " image(s) uploaded successfully!\nThey will appear in room details view.", 
                        "Upload Success", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, 
                        "❌ Failed to upload images: " + ex.getMessage(), 
                        "Upload Error", JOptionPane.ERROR_MESSAGE);
                    imagePreviewLabel.setText("❌ Upload Failed");
                }
            }
        });
        
        removeImageButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "Remove all uploaded images for this room?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
               
                for (String imagePath : selectedImagePaths) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists() && imageFile.getName().startsWith(roomId + "_")) {
                        try {
                            imageFile.delete();
                        } catch (Exception ex) {
                            System.err.println("Could not delete image file: " + ex.getMessage());
                        }
                    }
                }
                selectedImagePaths.clear();
                updateImagePreview.run();
                JOptionPane.showMessageDialog(dialog, 
                    "All images removed from this room.", 
                    "Images Removed", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JLabel imageHelpLabel = new JLabel("<html><small><i>Uploaded images will appear in room details view for customers</i></small></html>");
        imageHelpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        imageHelpLabel.setForeground(new Color(100, 100, 100));
        
        JPanel imageControlsPanel = new JPanel();
        imageControlsPanel.setLayout(new BoxLayout(imageControlsPanel, BoxLayout.Y_AXIS));
        imageControlsPanel.setBackground(Color.WHITE);
        
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonsRow.setBackground(Color.WHITE);
        buttonsRow.add(browseImageButton);
        buttonsRow.add(removeImageButton);
        
        imageControlsPanel.add(buttonsRow);
        imageControlsPanel.add(Box.createVerticalStrut(5));
        imageControlsPanel.add(imageHelpLabel);
        
        imageUploadPanel.add(imagePreviewLabel, BorderLayout.NORTH);
        imageUploadPanel.add(imageControlsPanel, BorderLayout.SOUTH);
        panel.add(imageUploadPanel, gbc);
        
        if (existingImages != null && !existingImages.isEmpty()) {
            gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel currentImageInfo = new JLabel("<html><small><i>Current: " + existingImages.size() + " image(s) - All will appear in room details</i></small></html>");
            currentImageInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            currentImageInfo.setForeground(new Color(100, 100, 100));
            panel.add(currentImageInfo, gbc);
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createModernButton("💾 Save Changes", SUCCESS_GREEN, new Color(0, 120, 0));
        saveButton.addActionListener(e -> {
            try {
                String roomType = (String) typeCombo.getSelectedItem();
                String capacity = capacityField.getText().trim();
                int basePrice = Integer.parseInt(priceField.getText().trim());
                String status = (String) statusCombo.getSelectedItem();
                String location = locationField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                if (capacity.isEmpty() || location.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (basePrice <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Price must be greater than 0.", "Invalid Price", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                AdminModel.RoomUnit updatedRoom = new AdminModel.RoomUnit(roomId, roomType, capacity, basePrice, status, location, description);
                updatedRoom.setImagePaths(selectedImagePaths); 
                
                if (adminModel.updateRoomUnit(roomId, updatedRoom)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                    tableModel.setValueAt(roomType, row, 1);
                    tableModel.setValueAt(capacity + " guests", row, 2);
                    tableModel.setValueAt("₱" + String.format("%,d", basePrice), row, 3);
                    tableModel.setValueAt(status, row, 4);
                    tableModel.setValueAt(location, row, 5);
                    tableModel.setValueAt(sdf.format(new Date()), row, 6);
                    
                    String imageMessage = selectedImagePaths.isEmpty() ? 
                        "No images set for this room." : 
                        selectedImagePaths.size() + " image(s) will appear in room details.";
                    
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ Room updated successfully!\n" + imageMessage + "\nChanges will reflect in the Rooms panel.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "❌ Failed to update room. Please try again.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Invalid price format! Please enter a valid number.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Error updating room: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = createModernButton("Cancel", Color.GRAY, Color.DARK_GRAY);
        cancelButton.addActionListener(e -> {
            
            if (!selectedImagePaths.isEmpty()) {
                for (String imagePath : selectedImagePaths) {
                    File uploadedFile = new File(imagePath);
                    
                    if (uploadedFile.exists() && 
                        (existingImages == null || !existingImages.contains(imagePath))) {
                        try {
                            uploadedFile.delete();
                        } catch (Exception ex) {
                            System.err.println("Could not delete temporary image file: " + ex.getMessage());
                        }
                    }
                }
            }
            dialog.dispose();
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 8, 8, 8);
        panel.add(buttonPanel, gbc);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileName.substring(lastDot).toLowerCase();
    }

    private void showChangeStatusDialog(JDialog parentDialog, DefaultTableModel tableModel, 
                                        String roomId, int row) {
        AdminModel.RoomUnit room = adminModel.getRoomUnitById(roomId);
        if (room == null) return;
        
        JDialog dialog = new JDialog(parentDialog, "Change Room Status", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(parentDialog);
        dialog.getContentPane().setBackground(LIGHT_BG);
        
        JPanel panel = new RoundedPanel(10);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel titleLabel = new JLabel("Change Status for Room: " + roomId);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel currentLabel = new JLabel("Current Status: " + room.getStatus());
        currentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JLabel newLabel = new JLabel("Select New Status:");
        newLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        newLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] statuses = {"Available", "Occupied", "Maintenance", "Out of Service"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem(room.getStatus());
        statusCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusCombo.setMaximumSize(new Dimension(200, 30));
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton updateButton = createModernButton("Update Status", PLUM, DARK_PLUM);
        updateButton.setPreferredSize(new Dimension(140, 35));
        
        JButton cancelButton = createModernButton("Cancel", Color.GRAY, Color.DARK_GRAY);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        
        updateButton.addActionListener(e -> {
            String newStatus = (String) statusCombo.getSelectedItem();
            if (adminModel.updateRoomStatus(roomId, newStatus)) {
                tableModel.setValueAt(newStatus, row, 4);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                tableModel.setValueAt(sdf.format(new Date()), row, 6);
                JOptionPane.showMessageDialog(dialog, 
                    "Room status updated to: " + newStatus, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update room status.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        panel.add(titleLabel);
        panel.add(currentLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(newLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(statusCombo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(buttonPanel);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    class ButtonRenderer implements TableCellRenderer {
        private JPanel panel;
        private JButton button;

        public ButtonRenderer() {
            panel = new JPanel(new GridBagLayout());
            
            button = new JButton("Change Status");
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setForeground(Color.WHITE);
            button.setBackground(INFO_BLUE);
            button.setPreferredSize(new Dimension(115, 28));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            
            panel.add(button);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            
            return panel;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton button;
        private boolean isPushed;
        private JTable table;
        private DefaultTableModel tableModel;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel model) {
            super(checkBox);
            this.table = table;
            this.tableModel = model;
            
            panel = new JPanel(new GridBagLayout());
            
            button = new JButton("Change Status");
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setForeground(Color.WHITE);
            button.setBackground(INFO_BLUE);
            button.setPreferredSize(new Dimension(115, 28));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            
            button.addActionListener(e -> fireEditingStopped());
            
            panel.add(button);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.currentRow = row;
            isPushed = true;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                SwingUtilities.invokeLater(() -> {
                    String receiptNumber = (String) tableModel.getValueAt(currentRow, 0);
                    String currentStatus = (String) tableModel.getValueAt(currentRow, 9);
                    showStatusChangeDialog(receiptNumber, currentStatus, currentRow);
                });
            }
            isPushed = false;
            return "Change Status";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        private void showStatusChangeDialog(String receiptNumber, String currentStatus, int row) {
            AdminModel.BookingRecord booking = adminModel.getBookingByReceipt(receiptNumber);
            if (booking == null) return;
            
            JDialog dialog = new JDialog(adminDialog, "Change Booking Status", true);
            dialog.setSize(400, 280);
            dialog.setLocationRelativeTo(adminDialog);
            dialog.getContentPane().setBackground(LIGHT_BG);
            
            JPanel panel = new RoundedPanel(10);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
            
            JLabel titleLabel = new JLabel("Change Status for Receipt: " + receiptNumber);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(PLUM);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel currentLabel = new JLabel("Current Status: " + currentStatus);
            currentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            currentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            
            JLabel newLabel = new JLabel("Select New Status:");
            newLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            newLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            String[] statuses = {"Pending", "Confirmed", "Completed", "Cancelled"};
            JComboBox<String> statusCombo = new JComboBox<>(statuses);
            statusCombo.setSelectedItem(currentStatus);
            statusCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
            statusCombo.setMaximumSize(new Dimension(200, 30));
            statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton updateButton = createModernButton("Update Status", PLUM, DARK_PLUM);
            updateButton.setPreferredSize(new Dimension(140, 35));
            
            JButton cancelButton = createModernButton("Cancel", Color.GRAY, Color.DARK_GRAY);
            cancelButton.setPreferredSize(new Dimension(100, 35));
            
            updateButton.addActionListener(e -> {
                String newStatus = (String) statusCombo.getSelectedItem();
                if (adminModel.updateBookingStatus(receiptNumber, newStatus)) {
                    tableModel.setValueAt(newStatus, row, 9);
                    JOptionPane.showMessageDialog(dialog, 
                        "Status updated to: " + newStatus, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Failed to update status.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(updateButton);
            buttonPanel.add(cancelButton);
            
            panel.add(titleLabel);
            panel.add(currentLabel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(newLabel);
            panel.add(Box.createVerticalStrut(5));
            panel.add(statusCombo);
            panel.add(Box.createVerticalStrut(15));
            panel.add(buttonPanel);
            
            dialog.add(panel);
            dialog.setVisible(true);
        }
    }
    
    private String censorName(String name) {
        if (name == null || name.length() <= 3) return name;
        
        String[] parts = name.split(" ");
        StringBuilder censored = new StringBuilder();
        
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.length() <= 2) {
                censored.append(part);
            } else if (part.length() == 3) {
                censored.append(part.charAt(0)).append("*").append(part.charAt(2));
            } else {
                censored.append(part.substring(0, 2));
                for (int j = 2; j < part.length() - 1; j++) {
                    censored.append("*");
                }
                censored.append(part.charAt(part.length() - 1));
            }
            
            if (i < parts.length - 1) {
                censored.append(" ");
            }
        }
        
        return censored.toString();
    }
    
    private String censorEmail(String email) {
        if (email == null || email.equals("N/A") || !email.contains("@")) return email;
        
        String[] parts = email.split("@");
        if (parts.length != 2) return email;
        
        String username = parts[0];
        String domain = parts[1];
        
        String censoredUsername;
        if (username.length() <= 2) {
            censoredUsername = username;
        } else if (username.length() <= 4) {
            censoredUsername = username.charAt(0) + "**" + username.charAt(username.length() - 1);
        } else {
            censoredUsername = username.substring(0, 2);
            for (int i = 2; i < username.length() - 1; i++) {
                censoredUsername += "*";
            }
            censoredUsername += username.charAt(username.length() - 1);
        }
        
        String censoredDomain;
        if (domain.contains(".")) {
            String[] domainParts = domain.split("\\.");
            String domainName = domainParts[0];
            String extension = domainParts[domainParts.length - 1];
            
            if (domainName.length() <= 2) {
                censoredDomain = domainName + "." + extension;
            } else {
                String censored = domainName.substring(0, 2);
                for (int i = 2; i < domainName.length(); i++) {
                    censored += "*";
                }
                censoredDomain = censored + "." + extension;
            }
        } else {
            censoredDomain = domain;
        }
        
        return censoredUsername + "@" + censoredDomain;
    }
    
    private String censorPhone(String phone) {
        if (phone == null || phone.equals("N/A")) return phone;
        
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        
        if (digitsOnly.length() <= 4) {
            return phone;
        }
        
        StringBuilder censored = new StringBuilder();
        int digitIndex = 0;
        int totalDigits = digitsOnly.length();
        
        int showStart = 3;
        int showEnd = 2;
        
        for (int i = 0; i < phone.length(); i++) {
            char c = phone.charAt(i);
            if (Character.isDigit(c)) {
                if (digitIndex < showStart || digitIndex >= totalDigits - showEnd) {
                    censored.append(c);
                } else {
                    censored.append("*");
                }
                digitIndex++;
            } else {
                censored.append(c);
            }
        }
        
        return censored.toString();
    }

    class StatusCellRenderer extends DefaultTableCellRenderer {
        public StatusCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                setFont(getFont().deriveFont(Font.BOLD));
                
                switch (status) {
                    case "Pending":
                        setBackground(new Color(255, 243, 205));
                        setForeground(WARNING_ORANGE);
                        break;
                    case "Confirmed":
                        setBackground(new Color(209, 250, 229));
                        setForeground(SUCCESS_GREEN);
                        break;
                    case "Completed":
                        setBackground(new Color(219, 234, 254));
                        setForeground(new Color(59, 130, 246));
                        break;
                    case "Cancelled":
                        setBackground(new Color(254, 226, 226));
                        setForeground(DANGER_RED);
                        break;
                    default:
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                }
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
    }

    private void clearSampleData() {
        int totalBookings = adminModel.getTotalBookings();
        int confirm = JOptionPane.showConfirmDialog(adminDialog,
            "⚠️ WARNING: This will permanently delete all sample data!\n\nCurrent: " + totalBookings + " bookings\nThis CANNOT be undone!\n\nAre you sure?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String verification = JOptionPane.showInputDialog(adminDialog, 
                "Type DELETE to confirm:", "Final Confirmation", JOptionPane.WARNING_MESSAGE);
            if ("DELETE".equals(verification)) {
                adminModel.clearAllBookings();
                JOptionPane.showMessageDialog(adminDialog,
                    "✅ Sample data cleared successfully!\nSystem is ready for live data.",
                    "Data Cleared", JOptionPane.INFORMATION_MESSAGE);
                adminDialog.dispose();
            }
        }
    }
}