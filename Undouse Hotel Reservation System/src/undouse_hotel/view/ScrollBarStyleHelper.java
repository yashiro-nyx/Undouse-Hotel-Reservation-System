package undouse_hotel.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;


public class ScrollBarStyleHelper {
    
    private static final Color SCROLLBAR_THUMB = new Color(150, 150, 150);
    private static final Color SCROLLBAR_THUMB_HOVER = new Color(120, 120, 120);
    private static final Color SCROLLBAR_TRACK = new Color(240, 240, 240);
    

    public static void styleScrollPane(JScrollPane scrollPane) {
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
}