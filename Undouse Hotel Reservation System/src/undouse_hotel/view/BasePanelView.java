package undouse_hotel.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public abstract class BasePanelView implements IPanelView {
    protected static final Color PRIMARY_BG = new Color(230, 180, 120);
    protected static final Color PRIMARY_HOVER = new Color(140, 180, 185);
    protected static final Color PRIMARY_TEXT = new Color(60, 30, 10);
    protected static final Color PLUM = new Color(90, 0, 60);
    protected static final Color GOLD = new Color(230, 180, 120);
    
    protected static final Color SCROLLBAR_THUMB = new Color(150, 150, 150);
    protected static final Color SCROLLBAR_THUMB_HOVER = new Color(120, 120, 120);
    protected static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);
    
    protected JPanel panel;
    
    protected JButton createStyledButton(String text, Color normalBg, Color hoverBg, Color textColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(textColor);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBackground(normalBg);
        addHoverEffect(button, hoverBg, normalBg);
        return button;
    }
    
    protected void addHoverEffect(JButton button, Color hoverBackground, Color normalBackground) {
        button.setBackground(normalBackground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBackground);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalBackground);
            }
        });
    }
    
    protected void styleScrollBar(JScrollPane scrollPane) {
        JScrollBar[] scrollBars = {scrollPane.getVerticalScrollBar(), scrollPane.getHorizontalScrollBar()};
        
        for (JScrollBar bar : scrollBars) {
            bar.setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = SCROLLBAR_THUMB;
                    this.thumbDarkShadowColor = SCROLLBAR_THUMB;
                    this.thumbHighlightColor = SCROLLBAR_THUMB;
                    this.thumbLightShadowColor = SCROLLBAR_THUMB;
                    this.trackColor = SCROLLBAR_TRACK;
                    this.trackHighlightColor = SCROLLBAR_TRACK;
                }
                
                @Override
                protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                    if (thumbBounds.isEmpty() || !bar.isEnabled()) {
                        return;
                    }
                    
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    Color thumbColor = isThumbRollover() ? SCROLLBAR_THUMB_HOVER : SCROLLBAR_THUMB;
                    g2.setColor(thumbColor);
                    
                    g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                                   thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
                    
                    g2.dispose();
                }
                
                @Override
                protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(SCROLLBAR_TRACK);
                    g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                    g2.dispose();
                }
                
                @Override
                protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }
                
                @Override
                protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }
                
                private JButton createZeroButton() {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(0, 0));
                    button.setMinimumSize(new Dimension(0, 0));
                    button.setMaximumSize(new Dimension(0, 0));
                    return button;
                }
            });
            
            bar.setPreferredSize(new Dimension(12, 12));
            bar.setUnitIncrement(16);
        }
    }
    
    protected JPanel createCounterPanel(String labelText, int[] value) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(50, 30));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JButton minusButton = createStyledButton("-", PRIMARY_BG, PRIMARY_HOVER, PRIMARY_TEXT);
        minusButton.setPreferredSize(new Dimension(40, 30));
        minusButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        
        JLabel countLabel = new JLabel(String.valueOf(value[0]), SwingConstants.CENTER);
        countLabel.setPreferredSize(new Dimension(30, 30));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton plusButton = createStyledButton("+", PRIMARY_BG, PRIMARY_HOVER, PRIMARY_TEXT);
        plusButton.setPreferredSize(new Dimension(50, 30));
        plusButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        
        minusButton.addActionListener(e -> {
            if (value[0] > 0) {
                value[0]--;
                countLabel.setText(String.valueOf(value[0]));
            }
        });
        
        plusButton.addActionListener(e -> {
            value[0]++;
            countLabel.setText(String.valueOf(value[0]));
        });
        
        panel.add(label);
        panel.add(minusButton);
        panel.add(countLabel);
        panel.add(plusButton);
        return panel;
    }
    
    protected JSeparator createDivider() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.BLACK);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 3));
        return separator;
    }

    protected static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
    	private int cornerRadius;
        
        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        }
    }
}