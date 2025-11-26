package undouse_hotel.view;

import javax.swing.*;
import java.awt.*;

public class DialogHelper {
    private static final Color PLUM = new Color(90, 0, 60);
    private static final Color GOLD = new Color(230, 180, 120);
    
    public static void showElegantWarningDialog(JFrame frame, String title, String message) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD, 2),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel msgLabel = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>");
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msgLabel.setForeground(new Color(50, 50, 50));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        msgLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton okButton = new JButton("OK");
        okButton.setFocusPainted(false);
        okButton.setBackground(GOLD);
        okButton.setForeground(PLUM);
        okButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> dialog.dispose());
        
        panel.add(titleLabel);
        panel.add(msgLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(okButton);
        
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
    public static boolean showCustomConfirmation(JFrame frame, String title, String message) {
        final boolean[] userConfirmed = {false};
        
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD, 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        JButton yesBtn = new JButton("Yes");
        yesBtn.setBackground(GOLD);
        yesBtn.setForeground(PLUM);
        yesBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        yesBtn.setFocusPainted(false);
        yesBtn.addActionListener(e -> {
            userConfirmed[0] = true;
            dialog.dispose();
        });
        
        JButton noBtn = new JButton("No");
        noBtn.setBackground(Color.LIGHT_GRAY);
        noBtn.setForeground(Color.DARK_GRAY);
        noBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        noBtn.setFocusPainted(false);
        noBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(yesBtn);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(noBtn);
        
        panel.add(titleLabel);
        panel.add(messageLabel);
        panel.add(buttonPanel);
        
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        
        return userConfirmed[0];
    }
    
    public static double computeTaxes(int basePrice) {
        return Math.round(basePrice * 0.23 * 100.0) / 100.0;
    }
    
    public static String formatPrice(double amount) {
        return String.format("%,.2f", amount);
    }
}