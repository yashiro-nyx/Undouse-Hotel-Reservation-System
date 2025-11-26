package undouse_hotel.controller;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import undouse_hotel.model.BookingModel;
import undouse_hotel.model.RoomModel;
import undouse_hotel.model.UserModel;
import undouse_hotel.view.DialogHelper;
import undouse_hotel.view.RoomSelectionView;
import undouse_hotel.view.HomePanelView;
import undouse_hotel.view.LoginView;
import undouse_hotel.view.SignupView;
import undouse_hotel.model.AdminModel;
import undouse_hotel.model.GuestModel;


public class BookingController implements IController {
    private BookingModel bookingModel;
    private RoomModel roomModel;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HomePanelView homePanelView;
    private RoomSelectionView currentRoomSelectionView;
    private AdminModel adminModel;
    private GuestModel guestModel;
    
    
    public BookingController(BookingModel bookingModel, RoomModel roomModel, JFrame frame, 
            CardLayout cardLayout, JPanel mainPanel, HomePanelView homePanelView,
            AdminModel adminModel) {
        this.bookingModel = bookingModel;
        this.roomModel = roomModel;
        this.frame = frame;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.homePanelView = homePanelView;
        this.adminModel = adminModel;
        this.guestModel = new GuestModel(adminModel);
    }
    
    @Override
    public void initialize() {
        bookingModel.resetBooking();
        roomModel.clearSelectedRooms();
    }
    
    @Override
    public void handleSearch() {
        UserModel userModel = UserModel.getInstance();
        if (!userModel.isUserLoggedIn()) {
            showLoginRequiredDialog();
            return;
        }
        
        String checkIn = bookingModel.getCheckInDate();
        String checkOut = bookingModel.getCheckOutDate();
        int totalGuests = bookingModel.getTotalGuests();
        
        boolean datesMissing = checkIn.isEmpty() || checkOut.isEmpty();
        boolean guestsMissing = totalGuests == 0;
        boolean roomsMissing = bookingModel.getNumRooms() == 0;
        
        if (datesMissing && (guestsMissing || roomsMissing)) {
            DialogHelper.showElegantWarningDialog(frame, "Incomplete Selection", 
                "Please select valid dates, guests, and rooms.");
            return;
        } else if (datesMissing) {
            DialogHelper.showElegantWarningDialog(frame, "Missing Dates", 
                "Please select both check-in and check-out dates.");
            return;
        } else if (guestsMissing || roomsMissing) {
            DialogHelper.showElegantWarningDialog(frame, "Missing Guest Info", 
                "Please select valid number of guests and rooms.");
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
        try {
            SimpleDateFormat parser = new SimpleDateFormat("MMMM dd, yyyy");
            Date checkInRaw = parser.parse(checkIn);
            Date checkOutRaw = parser.parse(checkOut);
            String checkInFormatted = formatter.format(checkInRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            String checkOutFormatted = formatter.format(checkOutRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            bookingModel.setCheckInDateFormatted(checkInFormatted);
            bookingModel.setCheckOutDateFormatted(checkOutFormatted);
        } catch (ParseException ex) {
            ex.printStackTrace();
            DialogHelper.showElegantWarningDialog(frame, "Invalid Date Format", 
                "Please reselect your check-in and check-out dates.");
            return;
        }
        
        roomModel.clearSelectedRooms();
        bookingModel.resetBooking();
        
        showRoomSelectionPanel(totalGuests);
    }
    
    private void showLoginRequiredDialog() {
        JDialog loginDialog = new JDialog(frame, "Login Required", true);
        loginDialog.setSize(450, 280);
        loginDialog.setLocationRelativeTo(frame);
        loginDialog.setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel iconLabel = new JLabel("🔒");
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Login Required");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 0, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("<html><center>You need to login or create an account<br>to make a reservation.</center></html>");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(90, 0, 60));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                loginButton.setBackground(new Color(70, 0, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                loginButton.setBackground(new Color(90, 0, 60));
            }
        });
        
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(230, 180, 120));
        signupButton.setForeground(new Color(90, 0, 60));
        signupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signupButton.setFocusPainted(false);
        signupButton.setPreferredSize(new Dimension(120, 40));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        signupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                signupButton.setBackground(new Color(210, 160, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                signupButton.setBackground(new Color(230, 180, 120));
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.DARK_GRAY);
        cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                cancelButton.setBackground(Color.LIGHT_GRAY);
            }
        });
        
        loginButton.addActionListener(e -> {
            loginDialog.dispose();
            showLoginDialog();
        });
        
        signupButton.addActionListener(e -> {
            loginDialog.dispose();
            showSignupDialog();
        });
        
        cancelButton.addActionListener(e -> loginDialog.dispose());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(iconLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(buttonPanel);
        
        loginDialog.add(mainPanel);
        loginDialog.setVisible(true);
    }
    
    private void showLoginDialog() {
        LoginView loginView = new LoginView(frame, true);
        loginView.setVisible(true);
        
        if (loginView.isLoginSuccessful()) {
            updateParentFrameMenuBar();
            handleSearch();
        }
    }

    private void showSignupDialog() {
        SignupView signupView = new SignupView(frame, true);
        signupView.setVisible(true);
        
        if (signupView.isSignupSuccessful()) {
            updateParentFrameMenuBar();
            handleSearch();
        } else {
            showLoginDialog();
        }
    }
    
    private void updateParentFrameMenuBar() {
        SwingUtilities.invokeLater(() -> {
            frame.revalidate();
            frame.repaint();
        });
    }
    
    private void showRoomSelectionPanel(int totalGuests) {
    
        currentRoomSelectionView = new RoomSelectionView(
            this, frame, totalGuests, "Room Selection", adminModel, guestModel
        );
        
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp.getName() != null && comp.getName().equals("Room Selection")) {
                mainPanel.remove(comp);
            }
        }
        
        mainPanel.add(currentRoomSelectionView.createPanel(), "Room Selection");
        cardLayout.show(mainPanel, "Room Selection");
    }
    
    @Override
    public void updateView() {
  
    }
    
    public void proceedToNextRoom() {
        if (bookingModel.getCurrentRoomIndex() < bookingModel.getNumRooms()) {
            bookingModel.incrementRoomIndex();
            updateRoomSelectionDisplay();
        }
    }
    
    public void goToPreviousRoom() {
        if (bookingModel.getCurrentRoomIndex() > 1) {
            bookingModel.decrementRoomIndex();
            updateRoomSelectionDisplay();
        }
    }
    
    public void goToHome() {
        cardLayout.show(mainPanel, "Home");
        
        SwingUtilities.invokeLater(() -> {
            if (homePanelView != null) {
                homePanelView.scrollToTop();
            }
        });
    }
    
    public void navigateToRooms() {
        cardLayout.show(mainPanel, "Rooms");
    }

    public void updateRoomSelectionDisplay() {
        int totalGuests = bookingModel.getTotalGuests();
        
        currentRoomSelectionView = new RoomSelectionView(
            this, frame, totalGuests, "Room Selection", adminModel, guestModel
        );
        
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp.getName() != null && comp.getName().equals("Room Selection")) {
                mainPanel.remove(comp);
            }
        }
        
        mainPanel.add(currentRoomSelectionView.createPanel(), "Room Selection");
        cardLayout.show(mainPanel, "Room Selection");
    }
    
    public BookingModel getBookingModel() {
        return bookingModel;
    }
    
    public RoomModel getRoomModel() {
        return roomModel;
    }
    
    public CardLayout getCardLayout() {
        return cardLayout;
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    public AdminModel getAdminModel() {
        return adminModel;
    }
    
    public GuestModel getGuestModel() {
        return guestModel;
    }
}