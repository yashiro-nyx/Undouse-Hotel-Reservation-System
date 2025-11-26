package undouse_hotel.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import undouse_hotel.model.BookingModel;
import undouse_hotel.model.RoomModel;
import undouse_hotel.model.AdminModel;
import undouse_hotel.model.UserModel;
import undouse_hotel.view.HomePanelView;
import undouse_hotel.view.RoomsPanelView;
import undouse_hotel.view.AboutPanelView;
import undouse_hotel.view.FAQView;
import undouse_hotel.view.LoginView;
import undouse_hotel.view.SignupView;
import undouse_hotel.view.UserProfileView;
import undouse_hotel.model.GuestModel;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import undouse_hotel.controller.AdminController;
import undouse_hotel.model.AdminModel;


public class MainController implements IController {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    private BookingModel bookingModel;
    private RoomModel roomModel;
    private AdminModel adminModel;
    private UserModel userModel;
    
    private BookingController bookingController;
    private AdminController adminController;
    private HomePanelView homePanelView;
    private RoomsPanelView roomsPanelView;
    private AboutPanelView aboutPanelView;
    private GuestModel guestModel;
    
    private JLabel userMenuLabel;
    
    private static final Color DARK_PLUM = new Color(70, 0, 50);
    private static final Color LIGHT_GOLD = new Color(245, 220, 180);
    private static final Color PLUM = new Color(74, 20, 56);
    private static final Color MENU_HOVER = new Color(255, 200, 150);
    
    public MainController() {
        this.bookingModel = new BookingModel();
        this.roomModel = new RoomModel();
        this.adminModel = new AdminModel();
        this.userModel = UserModel.getInstance();
        this.guestModel = new GuestModel(adminModel);
        
        adminModel.setRoomModel(roomModel);
    }
    
    @Override
    public void initialize() {
        frame = new JFrame("Resort - Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        
        createMenuBar();
        
        setupAdminHotkey(); 
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        homePanelView = new HomePanelView(null, frame);
        roomsPanelView = new RoomsPanelView(roomModel, frame, cardLayout, mainPanel);
        roomsPanelView.setAdminModel(adminModel);
        aboutPanelView = new AboutPanelView();
        
        bookingController = new BookingController(bookingModel, roomModel, frame, cardLayout, 
                mainPanel, homePanelView, 
                adminModel);

        adminController = new AdminController(frame, adminModel);

        homePanelView.setBookingController(bookingController);
        homePanelView.setRoomsPanelView(roomsPanelView);

        roomModel.addUpdateListener(() -> {
            SwingUtilities.invokeLater(() -> {
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp.getName() != null && comp.getName().equals("Rooms")) {
                        mainPanel.remove(comp);
                        break;
                    }
                }
                
                roomsPanelView = new RoomsPanelView(roomModel, frame, cardLayout, mainPanel);
                roomsPanelView.setAdminModel(adminModel);
                mainPanel.add(roomsPanelView.createPanel(), roomsPanelView.getPanelName());
                mainPanel.revalidate();
                mainPanel.repaint();
            });
        });
        
        mainPanel.add(homePanelView.createPanel(), homePanelView.getPanelName());
        mainPanel.add(roomsPanelView.createPanel(), roomsPanelView.getPanelName());
        mainPanel.add(aboutPanelView.createPanel(), aboutPanelView.getPanelName());
        
        JPanel glassPane = new JPanel(null);
        glassPane.setOpaque(false);
        
        JButton helpButton = FAQView.createFloatingHelpButton(frame);
        glassPane.add(helpButton);
        
        frame.setGlassPane(glassPane);
        frame.getGlassPane().setVisible(true);
        
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                Insets insets = frame.getInsets();
                int x = frame.getWidth() - helpButton.getWidth() - 30 - insets.right;
                int y = frame.getHeight() - helpButton.getHeight() - 30 - insets.bottom - insets.top;
                helpButton.setBounds(x, y, 60, 60);
            }
        });
        
        SwingUtilities.invokeLater(() -> {
            Insets insets = frame.getInsets();
            int x = frame.getWidth() - 90 - insets.right;
            int y = frame.getHeight() - 90 - insets.bottom - insets.top;
            helpButton.setBounds(x, y, 60, 60);
        });
        
        frame.add(mainPanel, BorderLayout.CENTER);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        setupAdminHotkey();
    }
    
    private void setupAdminHotkey() {
        System.out.println("Setting up admin hotkey (Ctrl+Shift+A)...");
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        
        KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()) + 
                                     " | Ctrl: " + e.isControlDown() + 
                                     " | Shift: " + e.isShiftDown());
                    
                    if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_A) {
                        System.out.println("Admin hotkey detected! Opening admin panel...");
                        SwingUtilities.invokeLater(() -> {
                            try {
                                adminController.showAdminPanel();
                            } catch (Exception ex) {
                                System.err.println("Error opening admin panel: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        });
                        return true;
                    }
                }
                return false;
            }
        };
        
        manager.addKeyEventDispatcher(keyEventDispatcher);
        System.out.println("Admin hotkey setup complete!");
    }
    
    @Override
    public void handleSearch() {
        bookingController.handleSearch();
    }
    
    @Override
    public void updateView() {
        homePanelView.updatePanel();
        roomsPanelView.updatePanel();
        aboutPanelView.updatePanel();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BorderLayout());
        menuBar.setBackground(PLUM); 
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 30);
        gbc.anchor = GridBagConstraints.WEST;
        
        ImageIcon logoIcon = new ImageIcon("images/logo.jpg");
        Image logoImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        headerPanel.add(logoLabel, gbc);
        
        gbc.insets = new Insets(0, 5, 0, 30);
        
        JLabel titleLabel = new JLabel("Undouse Hotel");
        titleLabel.setFont(new Font("Serif", Font.ITALIC, 30));
        titleLabel.setForeground(Color.WHITE); 
        headerPanel.add(titleLabel, gbc);
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        menuPanel.setOpaque(false);
        
        String[] menuItems = {"Home", "Rooms", "About Us"};
        for (String item : menuItems) {
            JLabel menuLabel = createMenuLabel(item);
            menuPanel.add(menuLabel);
        }
        
        JLabel faqLabel = createFAQMenuLabel();
        menuPanel.add(faqLabel);

        userMenuLabel = createUserProfileIcon();
        menuPanel.add(userMenuLabel);
        
        headerPanel.add(menuPanel, gbc);
        menuBar.add(headerPanel, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);
    }

    private JLabel createMenuLabel(String name) {
        JLabel label = new JLabel(name);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(MENU_HOVER); 
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(Color.WHITE);
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(mainPanel, name);
                
                if (name.equals("Home")) {
                    SwingUtilities.invokeLater(() -> {
                        homePanelView.scrollToTop();
                    });
                } else if (name.equals("About Us")) {
                    SwingUtilities.invokeLater(() -> {
                        aboutPanelView.scrollToTop();
                    });
                }
            }
        });
        return label;
    }
    
    private JLabel createFAQMenuLabel() {
        JLabel label = new JLabel("FAQs");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(MENU_HOVER); 
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(Color.WHITE);
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showFAQPanel();
            }
        });
        return label;
    }
    
    private void showFAQPanel() {
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp.getName() != null && comp.getName().equals("FAQ")) {
                mainPanel.remove(comp);
                break;
            }
        }
        
        JPanel faqPanel = FAQView.createFAQPanel();
        faqPanel.setName("FAQ");
        mainPanel.add(faqPanel, "FAQ");
        
        cardLayout.show(mainPanel, "FAQ");
    }
    
    private JLabel createUserProfileIcon() {
        UserModel.User currentUser = userModel.getCurrentUser();
        
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                UserModel.User user = userModel.getCurrentUser();
                
                if (user != null) {
                    g2d.setColor(new Color(230, 180, 120)); 
                } else {
                    g2d.setColor(new Color(200, 200, 200));
                }
                g2d.fillOval(2, 2, 46, 46);
                                g2d.setColor(Color.WHITE);
                
                if (user != null) {
                    // Draw user initials
                    g2d.setFont(new Font("Georgia", Font.BOLD, 18));
                    String initials = getUserInitials(user);
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = 25 - fm.stringWidth(initials) / 2;
                    int y = 32;
                    g2d.drawString(initials, x, y);
                } else {
                    
                    g2d.setFont(new Font("SansSerif", Font.PLAIN, 28));
                    String icon = "👤";
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = 25 - fm.stringWidth(icon) / 2;
                    int y = 33;
                    g2d.drawString(icon, x, y);
                }
            }
        };
        
        iconLabel.setPreferredSize(new Dimension(50, 50));
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconLabel.setToolTipText(currentUser != null ? currentUser.getFullName() : "Login");
        
        iconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (userModel.isUserLoggedIn()) {
                    showUserProfile();
                } else {
                    showMandatoryLogin();
                }
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                iconLabel.setBorder(BorderFactory.createLineBorder(MENU_HOVER, 2));
                iconLabel.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                iconLabel.setBorder(null);
                iconLabel.repaint();
            }
        });
        
        return iconLabel;
    }
    
    private String getUserInitials(UserModel.User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String initials = "";
        
        if (firstName != null && !firstName.isEmpty()) {
            initials += firstName.charAt(0);
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials += lastName.charAt(0);
        }
        
        return initials.toUpperCase();
    }
    
    private void showUserProfile() {
        UserProfileView userProfileView = new UserProfileView(frame, adminModel, cardLayout, mainPanel, () -> {
            cardLayout.show(mainPanel, "Home");
            SwingUtilities.invokeLater(() -> {
                homePanelView.scrollToTop();
            });
            refreshMenuBar();
        });
        
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp.getName() != null && comp.getName().equals("User Profile")) {
                mainPanel.remove(comp);
                break;
            }
        }
        
        JPanel profilePanel = userProfileView.createPanel();
        profilePanel.setName("User Profile");
        mainPanel.add(profilePanel, "User Profile");
        
        cardLayout.show(mainPanel, "User Profile");
    }
    
    private void showLoginPrompt() {
        LoginView loginView = new LoginView(frame);
        loginView.setVisible(true);
        
        if (loginView.isLoginSuccessful()) {
            refreshMenuBar();
        } else {
            showMandatoryLogin();
        }
    }

    private void showMandatoryLogin() {
        LoginView loginView = new LoginView(frame, true);
        loginView.setVisible(true);
        
        if (loginView.isLoginSuccessful()) {
            refreshMenuBar();
        } else {
            System.exit(0);
        }
    }
    
    public void refreshMenuBar() {
        frame.setJMenuBar(null);
        createMenuBar();
        setupAdminHotkey();
        frame.revalidate();
        frame.repaint();
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public GuestModel getGuestModel() {
        return guestModel;
    }
    
    public BookingController getBookingController() {
        return bookingController;
    }
    
    public AdminController getAdminController() {
        return adminController;
    }
    
    public RoomModel getRoomModel() {
        return roomModel;
    }
    
    public BookingModel getBookingModel() {
        return bookingModel;
    }
    
    public AdminModel getAdminModel() {
        return adminModel;
    }
    
    public CardLayout getCardLayout() {
        return cardLayout;
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
}