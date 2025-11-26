package undouse_hotel;

import javax.swing.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import undouse_hotel.controller.MainController;
import undouse_hotel.controller.AdminController;
import undouse_hotel.model.AdminModel;
import undouse_hotel.view.SplashScreen;
import undouse_hotel.view.LoginView;

public class UndouseHotelApp {
    private static JFrame mainApplicationFrame;
    
    public static void main(String[] args) {
       
        setupGlobalAdminHotkey();
        
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplashAndLoad(() -> {
    
                showLoginScreen();
            }); 
        });
    }
    
    private static void setupGlobalAdminHotkey() {
        System.out.println("Setting up global admin hotkey (Ctrl+Shift+A)...");
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
            new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
 
                    if (e.getID() == KeyEvent.KEY_PRESSED &&
                        e.isControlDown() && 
                        e.isShiftDown() && 
                        e.getKeyCode() == KeyEvent.VK_A) {
                        
                        System.out.println("Admin hotkey detected! Opening admin panel...");
                        
                        SwingUtilities.invokeLater(() -> {
                            JFrame parentFrame = findActiveFrame();
                            
                            if (parentFrame == null) {

                                parentFrame = new JFrame();
                                parentFrame.setUndecorated(true);
                                parentFrame.setSize(0, 0);
                                parentFrame.setLocationRelativeTo(null);
                                parentFrame.setVisible(true);
                            }
                            
                            AdminModel adminModel = new AdminModel();
                            AdminController adminController = new AdminController(parentFrame, adminModel);
                            adminController.showAdminPanel();
                        });
                        
                        return true; 
                    }
                    return false;
                }
            }
        );
        
        System.out.println("Global admin hotkey setup complete!");
    }
    
    private static JFrame findActiveFrame() {

        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame && window.isVisible()) {
                return (JFrame) window;
            }
        }
        return null;
    }
    
    private static void showLoginScreen() {
   
        JFrame tempFrame = new JFrame();
        tempFrame.setUndecorated(true);
        tempFrame.setSize(0, 0);
        tempFrame.setLocationRelativeTo(null);
        tempFrame.setVisible(true);
    
        LoginView loginView = new LoginView(tempFrame, true); 
        loginView.setVisible(true);
       
        if (loginView.isLoginSuccessful()) {
      
            tempFrame.dispose();
            
            initializeMainApplication();
        } else {
           
            tempFrame.dispose();
            System.exit(0);
        }
    }
    
    private static void initializeMainApplication() {
       
        MainController mainController = new MainController();
        mainController.initialize();
        
        mainApplicationFrame = findMainApplicationFrame();
    }
    
    private static JFrame findMainApplicationFrame() {
   
        for (Window window : Window.getWindows()) {
            if (window instanceof JFrame && window.isVisible()) {
                JFrame frame = (JFrame) window;
               
                if (frame.getTitle() != null && !frame.getTitle().contains("Admin") && 
                    !frame.getTitle().contains("Login")) {
                    return frame;
                }
            }
        }
        return null;
    }
    public static void restartApplication() {
        System.out.println("Restarting application...");
        
        Timer timer = new Timer(100, e -> {
  
            closeAllWindows();
            
            showLoginScreen();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private static void closeAllWindows() {

        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window.isVisible()) {
                window.dispose();
            }
        }
    }
}