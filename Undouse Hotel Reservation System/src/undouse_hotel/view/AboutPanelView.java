package undouse_hotel.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AboutPanelView {
    private String panelName = "About Us";
    private JScrollPane scrollPane;
    private List<JPanel> animatedCards = new ArrayList<>();
    private Timer animationTimer;
    
    private static final Color DEEP_PLUM = new Color(74, 20, 56);
    private static final Color RICH_GOLD = new Color(212, 175, 55);
    private static final Color CREAM_WHITE = new Color(253, 248, 243);
    private static final Color LIGHT_GOLD = new Color(245, 238, 220);
    private static final Color ACCENT_PURPLE = new Color(123, 71, 154);
    private static final Color DARK_CHARCOAL = new Color(45, 45, 45);
    private static final Color MEDIUM_GRAY = new Color(120, 120, 120);
    private static final Color LIGHT_GRAY = new Color(248, 248, 248);
    private static final Color GLOW_GOLD = new Color(255, 215, 0, 100);
    
    public JPanel createPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setName(panelName);
        mainPanel.setBackground(Color.WHITE);
        
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
        
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(245, 245, 245));
                for (int x = 0; x < getWidth(); x += 50) {
                    for (int y = 0; y < getHeight(); y += 50) {
                        g2d.drawLine(x, 0, x, getHeight());
                        g2d.drawLine(0, y, getWidth(), y);
                    }
                }
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(createHeroSection());
        contentPanel.add(createWelcomeSection());
        contentPanel.add(createStorySection());
        contentPanel.add(createMissionVisionSection());
        contentPanel.add(createFeaturesSection());
        contentPanel.add(createStatisticsSection());
        contentPanel.add(createTeamSection());
        contentPanel.add(createTestimonialsSection());
        contentPanel.add(createContactSection());
        
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        
        ScrollBarStyleHelper.styleScrollPane(scrollPane);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (mainPanel.isShowing()) {
                    startAnimations();
                } else {
                    stopAnimations();
                }
            }
        });
        
        return mainPanel;
    }
    
    private JPanel createHeroSection() {
        JPanel heroPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint mainGradient = new GradientPaint(
                    0, 0, DEEP_PLUM, 
                    getWidth(), getHeight(), new Color(94, 40, 84)
                );
                g2d.setPaint(mainGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                RadialGradientPaint goldGlow = new RadialGradientPaint(
                    new Point(getWidth()/2, getHeight()/3),
                    getWidth()/2,
                    new float[]{0.0f, 0.8f},
                    new Color[]{new Color(212, 175, 55, 50), new Color(212, 175, 55, 0)}
                );
                g2d.setPaint(goldGlow);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < 5; i++) {
                    int size = 100 + i * 50;
                    g2d.drawOval(getWidth()/2 - size/2, getHeight()/2 - size/2, size, size);
                }
            }
        };
        heroPanel.setPreferredSize(new Dimension(800, 500));
        
        JPanel glassPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));
        glassPanel.setPreferredSize(new Dimension(600, 300));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("UNDOUSE HOTEL");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 62));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel animatedSeparator = new JPanel() {
            private float animation = 0f;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = (int) (getWidth() * animation);
                g2d.setColor(RICH_GOLD);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine((getWidth() - width)/2, getHeight()/2, (getWidth() + width)/2, getHeight()/2);
            }
            
            public void setAnimation(float value) {
                this.animation = value;
                repaint();
            }
        };
        animatedSeparator.setPreferredSize(new Dimension(300, 10));
        animatedSeparator.setMaximumSize(new Dimension(300, 10));
        animatedSeparator.setOpaque(false);
        animatedSeparator.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Where Timeless Elegance Meets Modern Luxury");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        subtitleLabel.setForeground(LIGHT_GOLD);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        textPanel.add(animatedSeparator);
        textPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        textPanel.add(subtitleLabel);
        
        glassPanel.add(textPanel);
        heroPanel.add(glassPanel, BorderLayout.CENTER);
        
        return heroPanel;
    }
    
    private JPanel createWelcomeSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 120, 100, 120));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("A Legacy of Excellence");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 42));
        titleLabel.setForeground(DEEP_PLUM);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel separatorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        separatorPanel.setBackground(Color.WHITE);
        separatorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel crownIcon = new JLabel("👑");
        crownIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(100, 3));
        separator.setBackground(RICH_GOLD);
        separator.setForeground(RICH_GOLD);
        
        separatorPanel.add(crownIcon);
        separatorPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        separatorPanel.add(separator);
        
        JTextArea welcomeText = new JTextArea();
        welcomeText.setText("For over a decade, Undouse Hotel has stood as a beacon of sophistication and unparalleled " +
                "service in the heart of Quezon City. Our commitment to excellence has earned us the trust of " +
                "discerning travelers and numerous industry accolades, cementing our position as a premier " +
                "destination for those who appreciate the finer things in life.\n\n" +
                "Every aspect of our hotel reflects our dedication to creating memorable experiences - from " +
                "the meticulously designed interiors to the personalized service that anticipates your every need.");
        welcomeText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeText.setForeground(DARK_CHARCOAL);
        welcomeText.setLineWrap(true);
        welcomeText.setWrapStyleWord(true);
        welcomeText.setEditable(false);
        welcomeText.setBackground(Color.WHITE);
        welcomeText.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        welcomeText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(separatorPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        contentPanel.add(welcomeText);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStorySection() {
        JPanel panel = new JPanel(new BorderLayout(60, 0));
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 120, 100, 120));
        
        JPanel timelinePanel = createTimelinePanel();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(LIGHT_GRAY);
        
        JLabel titleLabel = new JLabel("Our Journey Through Time");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 36));
        titleLabel.setForeground(DEEP_PLUM);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(80, 3));
        separator.setMaximumSize(new Dimension(80, 3));
        separator.setBackground(RICH_GOLD);
        separator.setForeground(RICH_GOLD);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea storyText = new JTextArea();
        storyText.setText("Our story began in 2010 with a simple yet powerful vision: to create an urban sanctuary " +
                "where modern luxury and timeless elegance converge. What started as a single boutique property " +
                "has blossomed into a celebrated hospitality brand, renowned for its unwavering commitment to " +
                "excellence and innovation.\n\n" +
                "Throughout our journey, we have remained true to our founding principles while continuously " +
                "evolving to meet the changing needs of our guests. Each milestone represents not just growth, " +
                "but our dedication to pushing the boundaries of luxury hospitality.");
        storyText.setFont(new Font("SansSerif", Font.PLAIN, 15));
        storyText.setForeground(DARK_CHARCOAL);
        storyText.setLineWrap(true);
        storyText.setWrapStyleWord(true);
        storyText.setEditable(false);
        storyText.setBackground(LIGHT_GRAY);
        storyText.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        storyText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel achievementsPanel = createAchievementsPanel();
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(separator);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(storyText);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(achievementsPanel);
        
        panel.add(timelinePanel, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTimelinePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(300, 400));
        
        String[][] timelineEvents = {
            {"2023", "Global Luxury Hotel Award", "🏆"},
            {"2020", "Premium Service Certification", "⭐"},
            {"2017", "International Expansion", "🌍"},
            {"2015", "Spa & Wellness Center", "💆"},
            {"2013", "Fine Dining Restaurant", "🍽️"},
            {"2010", "Grand Opening", "🎉"}
        };
        
        for (String[] event : timelineEvents) {
            panel.add(createTimelineEvent(event[0], event[1], event[2]));
            if (!event[0].equals("2010")) {
                panel.add(createTimelineConnector());
            }
        }
        
        return panel;
    }
    
    private JPanel createTimelineEvent(String year, String event, String icon) {
        JPanel eventPanel = new JPanel(new BorderLayout(15, 0));
        eventPanel.setBackground(LIGHT_GRAY);
        eventPanel.setMaximumSize(new Dimension(280, 60));
        
        JLabel yearLabel = new JLabel(year);
        yearLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        yearLabel.setForeground(DEEP_PLUM);
        yearLabel.setPreferredSize(new Dimension(60, 20));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(LIGHT_GRAY);
        
        JLabel eventLabel = new JLabel(event);
        eventLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        eventLabel.setForeground(DARK_CHARCOAL);
        
        contentPanel.add(eventLabel);
        
        eventPanel.add(yearLabel, BorderLayout.WEST);
        eventPanel.add(iconLabel, BorderLayout.CENTER);
        eventPanel.add(contentPanel, BorderLayout.EAST);
        
        return eventPanel;
    }
    
    private JPanel createTimelineConnector() {
        JPanel connector = new JPanel();
        connector.setPreferredSize(new Dimension(2, 20));
        connector.setMaximumSize(new Dimension(2, 20));
        connector.setBackground(RICH_GOLD);
        connector.setAlignmentX(Component.LEFT_ALIGNMENT);
        connector.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        return connector;
    }
    
    private JPanel createAchievementsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(LIGHT_GRAY);
        panel.setMaximumSize(new Dimension(500, 80));
        
        String[][] achievements = {
        };
        
        for (String[] achievement : achievements) {
            panel.add(createAchievementCard(achievement[0], achievement[1]));
        }
        
        return panel;
    }
    
    private JPanel createAchievementCard(String value, String label) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GOLD, 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Georgia", Font.BOLD, 20));
        valueLabel.setForeground(DEEP_PLUM);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelLabel.setForeground(MEDIUM_GRAY);
        labelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(labelLabel);
        
        return card;
    }
    
    private JPanel createMissionVisionSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));
        
        JLabel titleLabel = new JLabel("Our Commitment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(DEEP_PLUM);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        cardsPanel.setBackground(Color.WHITE);
        
        JPanel missionCard = createCommitmentCard(
            "OUR MISSION",
            "To redefine luxury hospitality by creating exceptional, personalized experiences " +
            "that anticipate our guests' needs and exceed their expectations. We are committed " +
            "to maintaining the highest standards of service, comfort, and attention to detail " +
            "in every interaction.",
            DEEP_PLUM,
            "🎯"
        );
        
        JPanel visionCard = createCommitmentCard(
            "OUR VISION",
            "To be globally recognized as the benchmark for luxury hospitality, where innovation " +
            "complements tradition and every guest departure marks the beginning of a lifelong " +
            "relationship. We envision expanding our legacy while maintaining the intimate, " +
            "personalized service that defines our brand.",
            ACCENT_PURPLE,
            "🔭"
        );
        
        cardsPanel.add(missionCard);
        cardsPanel.add(visionCard);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(cardsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCommitmentCard(String title, String content, Color accentColor, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, LIGHT_GOLD),
            BorderFactory.createEmptyBorder(40, 30, 40, 30)
        ));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setForeground(accentColor);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(accentColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setForeground(DARK_CHARCOAL);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(null);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(textArea, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createFeaturesSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(LIGHT_GRAY);
        
        JLabel titleLabel = new JLabel("Our Distinguishing Features");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(DEEP_PLUM);
        titlePanel.add(titleLabel);
        
        panel.add(titlePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Features Grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 25, 25));
        gridPanel.setBackground(LIGHT_GRAY);
        
        String[][] features = {
            {"Luxury Accommodations", "Elegantly appointed rooms and suites with premium amenities, stunning views, and sophisticated design elements", "🏨"},
            {"Culinary Excellence", "Multiple award-winning restaurants offering international cuisine and local delicacies crafted by master chefs", "🍽️"},
            {"Business Facilities", "State-of-the-art meeting rooms, conference facilities, and comprehensive business center services", "💼"},
            {"Wellness & Recreation", "Swimming pool, luxury spa, fitness center, and curated recreational activities for holistic wellbeing", "🏊"},
            {"Prime Location", "Strategic location in the heart of the city with convenient access to business districts and attractions", "📍"},
            {"Personalized Service", "24/7 concierge service and dedicated staff providing attentive, personalized guest experiences", "⭐"}
        };
        
        for (String[] feature : features) {
            gridPanel.add(createFeatureCard(feature[0], feature[1], feature[2]));
        }
        
        panel.add(gridPanel);
        
        return panel;
    }
    
    private JPanel createFeatureCard(String title, String description, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setForeground(RICH_GOLD);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(DEEP_PLUM);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        JTextArea descText = new JTextArea(description);
        descText.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descText.setForeground(MEDIUM_GRAY);
        descText.setLineWrap(true);
        descText.setWrapStyleWord(true);
        descText.setEditable(false);
        descText.setBackground(Color.WHITE);
        descText.setBorder(null);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(descText, BorderLayout.CENTER);
       
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(253, 248, 243));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(RICH_GOLD, 2),
                    BorderFactory.createEmptyBorder(24, 24, 24, 24)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
            }
        });
        
        animatedCards.add(card);
        return card;
    }
    
    private JPanel createStatisticsSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 100, 80, 100));
        
        JLabel titleLabel = new JLabel("Excellence in Numbers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(DEEP_PLUM);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 30, 0));
        statsPanel.setBackground(Color.WHITE);
        
        String[][] statistics = {
            {"10,000+", "Happy Guests", "👥"},
            {"95%", "Guest Satisfaction", "⭐"},
            {"50+", "Awards Won", "🏆"},
            {"24/7", "Customer Support", "🛎️"}
        };
        
        for (String[] stat : statistics) {
            statsPanel.add(createStatCard(stat[0], stat[1], stat[2]));
        }
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatCard(String number, String label, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        numberLabel.setForeground(DEEP_PLUM);
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(label);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descLabel.setForeground(MEDIUM_GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(numberLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(descLabel);
        
        return card;
    }
    
    private JPanel createTeamSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 120, 80, 120));
        
        JLabel titleLabel = new JLabel("Meet Our Leadership", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 36));
        titleLabel.setForeground(DEEP_PLUM);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JPanel teamPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        teamPanel.setBackground(Color.WHITE);
        
        String[][] teamMembers = {
            {"Jervin Paul Romualdo", "General Manager", "👑", "15+ years in luxury hospitality"},
            {"Gwen Del Castillo", "Head Chef", "👨‍🍳", "Michelin-star trained culinary expert"},
            {"Arianne Kaye Tupaen", "Guest Relations", "💝", "Dedicated to exceptional service"},
            {"Adrian Estrecho", "Operations", "⚙️", "Ensuring seamless experiences"},
            {"Joana Sostea", "Wellness Director", "💆", "Holistic guest wellbeing"},
        };
        
        for (String[] member : teamMembers) {
            teamPanel.add(createTeamCard(member[0], member[1], member[2], member[3]));
        }
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(teamPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTeamCard(String name, String role, String icon, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(DEEP_PLUM);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roleLabel.setForeground(RICH_GOLD);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(50, 1));
        separator.setMaximumSize(new Dimension(50, 1));
        separator.setBackground(LIGHT_GOLD);
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descArea.setForeground(MEDIUM_GRAY);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(roleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(separator);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(descArea);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(253, 248, 243));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(RICH_GOLD, 2),
                    BorderFactory.createEmptyBorder(24, 19, 24, 19)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    BorderFactory.createEmptyBorder(25, 20, 25, 20)
                ));
            }
        });
        
        animatedCards.add(card);
        return card;
    }
    
    private JPanel createTestimonialsSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 120, 80, 120));
        
        JLabel titleLabel = new JLabel("Guest Experiences", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 36));
        titleLabel.setForeground(DEEP_PLUM);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JPanel testimonialsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        testimonialsPanel.setBackground(LIGHT_GRAY);
        
        String[][] testimonials = {
            {"⭐⭐⭐⭐⭐", "\"Absolutely impeccable service! The attention to detail and personalized care made our anniversary unforgettable.\"", "- Sophia M., Luxury Traveler"},
            {"⭐⭐⭐⭐⭐", "\"The perfect blend of modern luxury and warm hospitality. Undouse Hotel sets the standard for excellence.\"", "- Thomas R., Business Executive"},
            {"⭐⭐⭐⭐⭐", "\"From the stunning accommodations to the world-class dining, every moment exceeded our expectations.\"", "- The Johnson Family"},
            {"⭐⭐⭐⭐⭐", "\"The team at Undouse Hotel anticipates your every need. Truly exceptional service in a breathtaking setting.\"", "- Elena D., Frequent Guest"}
        };
        
        for (String[] testimonial : testimonials) {
            testimonialsPanel.add(createTestimonialCard(testimonial[0], testimonial[1], testimonial[2]));
        }
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(testimonialsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTestimonialCard(String stars, String quote, String author) {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GOLD, 1),
            BorderFactory.createEmptyBorder(30, 25, 30, 25)
        ));
        
        JLabel starsLabel = new JLabel(stars);
        starsLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        starsLabel.setForeground(RICH_GOLD);
        starsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JTextArea quoteArea = new JTextArea(quote);
        quoteArea.setFont(new Font("SansSerif", Font.ITALIC, 14));
        quoteArea.setForeground(DARK_CHARCOAL);
        quoteArea.setLineWrap(true);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setEditable(false);
        quoteArea.setBackground(Color.WHITE);
        quoteArea.setBorder(null);
        
        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        authorLabel.setForeground(MEDIUM_GRAY);
        authorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        card.add(starsLabel, BorderLayout.NORTH);
        card.add(quoteArea, BorderLayout.CENTER);
        card.add(authorLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createContactSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DEEP_PLUM);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(DEEP_PLUM);
        
        JLabel titleLabel = new JLabel("Experience Undouse Hotel");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Where exceptional service meets unforgettable experiences");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(LIGHT_GOLD);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));
        
        JPanel contactInfoPanel = new JPanel();
        contactInfoPanel.setLayout(new BoxLayout(contactInfoPanel, BoxLayout.Y_AXIS));
        contactInfoPanel.setBackground(DEEP_PLUM);
        contactInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[][] contactInfo = {
            {"📍", "New Seaside Drive, Quezon City, Philippines"},
            {"📞", "+63 991 664 9798"},
            {"✉️", "info@undousehotel.com"},
            {"🌐", "www.undousehotel.com"}
        };
        
        for (String[] info : contactInfo) {
            JPanel contactLine = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            contactLine.setBackground(DEEP_PLUM);
            
            JLabel iconLabel = new JLabel(info[0]);
            iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            iconLabel.setForeground(RICH_GOLD);
            
            JLabel textLabel = new JLabel(info[1]);
            textLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textLabel.setForeground(Color.WHITE);
            
            contactLine.add(iconLabel);
            contactLine.add(textLabel);
            contactInfoPanel.add(contactLine);
            
            if (!info[0].equals("🌐")) {
                contactInfoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        
        contentPanel.add(titleLabel);
        contentPanel.add(subtitleLabel);
        contentPanel.add(contactInfoPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void startAnimations() {
        if (animationTimer != null && animationTimer.isRunning()) {
            return;
        }
        
        animationTimer = new Timer(100, e -> {
            if (!animatedCards.isEmpty()) {
          
                EventQueue.invokeLater(() -> {
                    for (JPanel card : animatedCards) {
                      
                        if (card.isShowing() && card.getParent() != null) {
                            try {
                                
                                Point position = card.getLocationOnScreen();
                                if (position != null) {
                                    
                                    Rectangle cardBounds = card.getBounds();
                                    Rectangle viewportBounds = scrollPane.getViewport().getViewRect();
                                    
                                    if (viewportBounds.intersects(cardBounds)) {
                                        card.repaint();
                                    }
                                }
                            } catch (IllegalComponentStateException ex) {
                    
                            }
                        }
                    }
                });
            }
        });
        animationTimer.start();
    }
    
    private void stopAnimations() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
    
    public void scrollToTop() {
        if (scrollPane != null) {
            SwingUtilities.invokeLater(() -> {
                scrollPane.getVerticalScrollBar().setValue(0);
            });
        }
    }
    
    public String getPanelName() {
        return panelName;
    }
    
    public void updatePanel() {
        scrollToTop();
    }
    
    public void dispose() {
        stopAnimations();
        if (animatedCards != null) {
            animatedCards.clear();
        }
    }
    
    private static class RoundedPanel extends JPanel {
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