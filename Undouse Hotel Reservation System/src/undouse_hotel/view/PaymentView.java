package undouse_hotel.view;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.util.List;
import undouse_hotel.model.UserModel;
import undouse_hotel.model.AdminModel;
import undouse_hotel.model.GuestModel;
import undouse_hotel.model.DiscountModel;

public class PaymentView extends JDialog {
	private static final Color PRIMARY = new Color(74, 20, 56); 
	private static final Color SUCCESS = new Color(25, 135, 84);
	private static final Color DANGER = new Color(220, 53, 69);
	private static final Color DARK = new Color(70, 0, 50); 
	private static final Color LIGHT_BG = new Color(255, 250, 245); 
	private static final Color WHITE = Color.WHITE;
	private static final Color BORDER = new Color(222, 226, 230);
	private static final Color TEXT_MUTED = new Color(108, 117, 125);
	private static final Color LIGHT_GOLD = new Color(245, 220, 180);
	private static final Color GOLD = new Color(230, 190, 100);
	private static final Color ACCENT_COLOR = new Color(180, 140, 190);
	private static final Color DARK_PLUM = new Color(70, 0, 50);
    
    private JTextField firstNameField, lastNameField, emailField, mobileField;
    private JComboBox<String> countryCombo, paymentMethodCombo;
    private JTextField addr1Field, addr2Field, cityField, zipField;
    private JTextField referenceField, acctNameField;
    private JCheckBox privacyCheck, bookingCheck;
    private JButton submitBtn;
    private JScrollPane scrollPane;
    
    private String roomType, price, checkInDate, checkOutDate;
    private int guestCount, roomCount;
    private double exactAmount;
    private UserModel userModel;
    private AdminModel adminModel;
    private GuestModel guestModel;
    private Runnable onCompletion;
    
    public PaymentView(JFrame parent, String roomType, String price, String checkInDate, String checkOutDate,
            int guestCount, int roomCount, Runnable onCompletion, AdminModel adminModel, GuestModel guestModel) {
        super(parent, "Secure Checkout", true);
        this.roomType = roomType;
        this.price = price;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestCount = guestCount;
        this.roomCount = roomCount;
        this.userModel = UserModel.getInstance();
        this.adminModel = adminModel;
        this.guestModel = guestModel;
        this.onCompletion = onCompletion;

        String cleanPrice = price.replace("₱", "").replace(",", "").trim();
        this.exactAmount = Double.parseDouble(cleanPrice);
        this.price = String.format("%,.2f", exactAmount);

        initializeComponents();
    }
    
    private void initializeComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(LIGHT_BG);
        
        mainContainer.add(createTopBar(), BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout(20, 0));
        contentWrapper.setBackground(LIGHT_BG);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JPanel leftPanel = createFormPanel();
        scrollPane = new JScrollPane(leftPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(LIGHT_BG);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Set scroll to top initially
        SwingUtilities.invokeLater(() -> {
            JViewport viewport = scrollPane.getViewport();
            viewport.setViewPosition(new Point(0, 0));
        });

        contentWrapper.add(scrollPane, BorderLayout.CENTER);
        contentWrapper.add(createSummaryPanel(), BorderLayout.EAST);
        
        mainContainer.add(contentWrapper, BorderLayout.CENTER);
        add(mainContainer);

        populateUserData();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(25, 60, 25, 60)
        ));
        
        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftSide.setBackground(WHITE);
        
        JPanel textContainer = new JPanel();
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.Y_AXIS));
        textContainer.setBackground(WHITE);
        
        JLabel title = new JLabel("Secure Checkout");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel breadcrumb = new JLabel("Home > Rooms > Booking > Payment");
        breadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        breadcrumb.setForeground(TEXT_MUTED);
        breadcrumb.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textContainer.add(title);
        textContainer.add(Box.createVerticalStrut(8));
        textContainer.add(breadcrumb);
        
        leftSide.add(textContainer);
        
        JLabel security = new JLabel("🔒 Secure Payment");
        security.setFont(new Font("Segoe UI", Font.BOLD, 15));
        security.setForeground(SUCCESS);
        
        topBar.add(leftSide, BorderLayout.WEST);
        topBar.add(security, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createFormPanel() {
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setBackground(LIGHT_BG);
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(LIGHT_BG);
        container.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        
        container.add(createProgressBar());
        container.add(Box.createVerticalStrut(40));
        container.add(createContactSection());
        container.add(Box.createVerticalStrut(30));
        container.add(createAddressSection());
        container.add(Box.createVerticalStrut(30));
        container.add(createPaymentSection());
        container.add(Box.createVerticalStrut(30));
        container.add(createPoliciesSection());
        container.add(Box.createVerticalStrut(30));
        container.add(createDiscountSection());  
        container.add(createAgreementSection());
        container.add(Box.createVerticalStrut(30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        outerWrapper.add(container, gbc);
        
        return outerWrapper;
    }
    
    private JPanel createProgressBar() {
        JPanel progress = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        progress.setBackground(LIGHT_BG);
        progress.setMaximumSize(new Dimension(800, 60));
        
        String[] steps = {"1. Select Room", "2. Guest Info", "3. Payment", "4. Confirmation"};
        
        for (int i = 0; i < steps.length; i++) {
            JPanel step = new JPanel();
            step.setLayout(new BoxLayout(step, BoxLayout.X_AXIS));
            step.setBackground(LIGHT_BG);
            
            JLabel circle = new JLabel(" " + (i + 1) + " ");
            circle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            circle.setOpaque(true);
            circle.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            
            if (i <= 2) {
                circle.setBackground(PRIMARY);
                circle.setForeground(WHITE);
            } else {
                circle.setBackground(WHITE);
                circle.setForeground(TEXT_MUTED);
                circle.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 2),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
            
            JLabel label = new JLabel("  " + steps[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(i <= 2 ? DARK : TEXT_MUTED);
            
            step.add(circle);
            step.add(label);
            progress.add(step);
        }
        
        return progress;
    }
    
    private JPanel createSection(String title) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        section.setMaximumSize(new Dimension(800, 2000));
        section.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(20));
        
        return section;
    }
    
    private JPanel createContactSection() {
        JPanel section = createSection("Contact Information");
        
        firstNameField = createTextField();
        lastNameField = createTextField();
        emailField = createTextField();
        mobileField = createTextField();
        
        addValidationListeners(firstNameField);
        addValidationListeners(lastNameField);
        addValidationListeners(emailField);
        addNumericFilter(mobileField);
        
        mobileField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                validateMobileField();
                validateForm();
            }
        });
        
        JPanel mobileWrapper = new JPanel(new BorderLayout(0, 0));
        mobileWrapper.setBackground(WHITE);
        mobileWrapper.setMaximumSize(new Dimension(700, 45));
        mobileWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel prefix = new JLabel(" +63 ");
        prefix.setFont(new Font("Segoe UI", Font.BOLD, 14));
        prefix.setForeground(PRIMARY);
        prefix.setOpaque(true);
        prefix.setBackground(new Color(240, 240, 240));
        prefix.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        prefix.setPreferredSize(new Dimension(60, 45));
        
        mobileWrapper.add(prefix, BorderLayout.WEST);
        mobileWrapper.add(mobileField, BorderLayout.CENTER);
        
        JPanel nameFieldsContainer = new JPanel();
        nameFieldsContainer.setLayout(new BoxLayout(nameFieldsContainer, BoxLayout.X_AXIS));
        nameFieldsContainer.setBackground(WHITE);
        nameFieldsContainer.setMaximumSize(new Dimension(700, 70));
        nameFieldsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel firstNamePanel = createFieldGroup("First Name", firstNameField, 340);
        JPanel lastNamePanel = createFieldGroup("Last Name", lastNameField, 340);
        
        nameFieldsContainer.add(firstNamePanel);
        nameFieldsContainer.add(Box.createHorizontalStrut(15));
        nameFieldsContainer.add(lastNamePanel);
        
        section.add(nameFieldsContainer);
        section.add(Box.createVerticalStrut(20));
        section.add(createFieldGroup("Email Address", emailField, 700));
        section.add(Box.createVerticalStrut(20));
        section.add(createFieldGroup("Mobile Phone", mobileWrapper, 700));
        
        return section;
    }

    private JPanel createDiscountSection() {
        JPanel section = createSection("Discount Information");
        
        String[] discountOptions = {
            "No Discount",
            "Senior Citizen (RA 9994) - 10% + VAT Exempt",
            "Person with Disability (RA 10754) - 10% + VAT Exempt",
            "Child (Below 12 years) -5%"
        };
        
        JComboBox<String> discountCombo = createComboBox(discountOptions);
        discountCombo.setName("discountTypeCombo");
        
        section.add(createFieldGroup("Discount Type", discountCombo, 700));
        section.add(Box.createVerticalStrut(20));
        
        JTextField idNumberField = createTextField();
        idNumberField.setName("idNumberField");
        JPanel idFieldGroup = createFieldGroup("ID Number (Required for Senior/PWD)", idNumberField, 700);
        idFieldGroup.setVisible(false);
        idFieldGroup.setName("idFieldGroup");
        
        section.add(idFieldGroup);
        section.add(Box.createVerticalStrut(20));
        
        JTextField ageField = createTextField();
        ageField.setName("ageField");
        addNumericFilter(ageField);
        JPanel ageFieldGroup = createFieldGroup("Age (Required for Child discount)", ageField, 700);
        ageFieldGroup.setVisible(false);
        ageFieldGroup.setName("ageFieldGroup");
        
        section.add(ageFieldGroup);
        section.add(Box.createVerticalStrut(20));
        
        JPanel discountBreakdownPanel = new JPanel();
        discountBreakdownPanel.setLayout(new BoxLayout(discountBreakdownPanel, BoxLayout.Y_AXIS));
        discountBreakdownPanel.setBackground(new Color(240, 248, 255));
        discountBreakdownPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        discountBreakdownPanel.setMaximumSize(new Dimension(700, 200));
        discountBreakdownPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        discountBreakdownPanel.setVisible(false);
        discountBreakdownPanel.setName("discountBreakdownPanel");
        
        JLabel breakdownTitle = new JLabel("💰 Discount Breakdown");
        breakdownTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        breakdownTitle.setForeground(PRIMARY);
        breakdownTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea breakdownText = new JTextArea();
        breakdownText.setName("breakdownText");
        breakdownText.setEditable(false);
        breakdownText.setOpaque(false);
        breakdownText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        breakdownText.setForeground(DARK);
        breakdownText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        discountBreakdownPanel.add(breakdownTitle);
        discountBreakdownPanel.add(Box.createVerticalStrut(10));
        discountBreakdownPanel.add(breakdownText);
        
        section.add(discountBreakdownPanel);
        
        discountCombo.addActionListener(e -> {
            int selectedIndex = discountCombo.getSelectedIndex();
            
            boolean showIdField = (selectedIndex == 1 || selectedIndex == 2);
            idFieldGroup.setVisible(showIdField);
            
            boolean showAgeField = (selectedIndex == 3);
            ageFieldGroup.setVisible(showAgeField);
            
            discountBreakdownPanel.setVisible(false);
            
            section.revalidate();
            section.repaint();
            validateForm();
        });
        
        idNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculateAndDisplayDiscount(discountCombo, idNumberField, ageField, 
                                           discountBreakdownPanel, breakdownText);
                validateForm();
            }
        });
        
        ageField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                calculateAndDisplayDiscount(discountCombo, idNumberField, ageField, 
                                           discountBreakdownPanel, breakdownText);
                validateForm();
            }
        });
        
        return section;
    }

    private void calculateAndDisplayDiscount(JComboBox<String> discountCombo, 
            JTextField idNumberField, 
            JTextField ageField,
            JPanel breakdownPanel, 
            JTextArea breakdownText) {
int selectedIndex = discountCombo.getSelectedIndex();
DiscountModel.DiscountType discountType = DiscountModel.DiscountType.NONE;

switch (selectedIndex) {
case 1:
discountType = DiscountModel.DiscountType.SENIOR_CITIZEN;
break;
case 2:
discountType = DiscountModel.DiscountType.PWD;
break;
case 3:
discountType = DiscountModel.DiscountType.CHILD;
break;
}

if (discountType == DiscountModel.DiscountType.NONE) {
breakdownPanel.setVisible(false);
return;
}

String idNumber = idNumberField.getText().trim();
int age = 0;
try {
age = ageField.getText().trim().isEmpty() ? 0 : Integer.parseInt(ageField.getText().trim());
} catch (NumberFormatException e) {
age = 0;
}

if (!DiscountModel.validateDiscountEligibility(discountType, idNumber, age)) {
breakdownPanel.setVisible(false);
return;
}

// Parse the original price from the price string to avoid using modified exactAmount
String cleanPrice = price.replace("₱", "").replace(",", "").trim();
double originalPrice = Double.parseDouble(cleanPrice);

DiscountModel.DiscountCalculation calculation = 
DiscountModel.calculateDiscount(originalPrice, discountType);

breakdownText.setText(calculation.getFormattedBreakdown());
breakdownPanel.setVisible(true);
}

    @SuppressWarnings("unchecked")
    private <T extends Component> T findComponentByName(Container container, String name) {
        for (Component comp : container.getComponents()) {
            if (name.equals(comp.getName())) {
                return (T) comp;
            }
            if (comp instanceof Container) {
                T found = findComponentByName((Container) comp, name);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private JPanel createAddressSection() {
        JPanel section = createSection("Billing Address");
        
        countryCombo = createComboBox(new String[]{"-- Select Country --", "Philippines"});
        countryCombo.addActionListener(e -> validateForm());
        
        addr1Field = createTextField();
        addr2Field = createTextField();
        cityField = createTextField();
        zipField = createTextField();
        
        addValidationListeners(addr1Field);
        addValidationListeners(addr2Field);
        addValidationListeners(cityField);
        addValidationListeners(zipField);
        addNumericFilter(zipField);
        
        section.add(createFieldGroup("Country", countryCombo, 700));
        section.add(Box.createVerticalStrut(20));
        section.add(createFieldGroup("Address Line 1", addr1Field, 700));
        section.add(Box.createVerticalStrut(20));
        section.add(createFieldGroup("Address Line 2", addr2Field, 700));
        section.add(Box.createVerticalStrut(20));
        
        // City and Zip code in two columns
        JPanel cityZipContainer = new JPanel();
        cityZipContainer.setLayout(new BoxLayout(cityZipContainer, BoxLayout.X_AXIS));
        cityZipContainer.setBackground(WHITE);
        cityZipContainer.setMaximumSize(new Dimension(700, 70));
        cityZipContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel cityPanel = createFieldGroup("City", cityField, 340);
        JPanel zipPanel = createFieldGroup("Zip Code", zipField, 340);
        
        cityZipContainer.add(cityPanel);
        cityZipContainer.add(Box.createHorizontalStrut(15));
        cityZipContainer.add(zipPanel);
        
        section.add(cityZipContainer);
        
        return section;
    }
    
    private JPanel createPaymentSection() {
        JPanel section = createSection("Payment Details");
        
        paymentMethodCombo = createComboBox(new String[]{
            "-- Select Payment Method --", "Gcash", "BDO", "Paymaya", "BPI",
        });
        paymentMethodCombo.addActionListener(e -> validateForm());
        
        referenceField = createTextField();
        acctNameField = createTextField();
        
        addValidationListeners(referenceField);
        addValidationListeners(acctNameField);
        addNumericFilter(referenceField);
        
        section.add(createFieldGroup("Payment Method", paymentMethodCombo, 700));
        section.add(Box.createVerticalStrut(20));
        section.add(createFieldGroup("Account Number / Reference", referenceField, 700));
        section.add(Box.createVerticalStrut(20));
        section.add(createFieldGroup("Account Name", acctNameField, 700));
        
        return section;
    }
    
    
    private JPanel createPoliciesSection() {
        JPanel section = createSection("Terms & Policies");
        
        JPanel alert = new JPanel();
        alert.setLayout(new BoxLayout(alert, BoxLayout.Y_AXIS));
        alert.setBackground(new Color(255, 243, 205));
        alert.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        alert.setMaximumSize(new Dimension(700, 180));
        alert.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel alertTitle = new JLabel("Important Information");
        alertTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        alertTitle.setForeground(DARK);
        alertTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea text = new JTextArea(
            "• Book-and-Buy arrangement with full payment required\n" +
            "• Non-refundable but may be rebooked (subject to rate difference)\n" +
            "• Rebooking must be done at least 3 days before arrival\n" +
            "• Full payment forfeited for no-show"
        );
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setOpaque(false);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        text.setForeground(DARK);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        alert.add(alertTitle);
        alert.add(Box.createVerticalStrut(12));
        alert.add(text);
        
        section.add(alert);
        
        return section;
    }
    
    private JPanel createAgreementSection() {
        JPanel section = createSection("Final Steps");
        
        privacyCheck = new JCheckBox("I agree to the Privacy Policy and Terms of Service");
        bookingCheck = new JCheckBox("I understand and accept the Booking Conditions");
        
        for (JCheckBox check : new JCheckBox[]{privacyCheck, bookingCheck}) {
            check.setBackground(WHITE);
            check.setForeground(DARK);
            check.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            check.setAlignmentX(Component.LEFT_ALIGNMENT);
            check.addActionListener(e -> validateForm());
        }
        
        submitBtn = new JButton("COMPLETE BOOKING");
        submitBtn.setBackground(PRIMARY);
        submitBtn.setForeground(WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitBtn.setPreferredSize(new Dimension(300, 55));
        submitBtn.setMaximumSize(new Dimension(300, 55));
        submitBtn.setEnabled(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setBorder(BorderFactory.createEmptyBorder());
        submitBtn.addActionListener(e -> handleSubmit());
        
        submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
            	if (submitBtn.isEnabled()) submitBtn.setBackground(DARK_PLUM);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (submitBtn.isEnabled()) submitBtn.setBackground(PRIMARY);
            }
        });
        
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnWrapper.setBackground(WHITE);
        btnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnWrapper.add(submitBtn);
        
        section.add(privacyCheck);
        section.add(Box.createVerticalStrut(15));
        section.add(bookingCheck);
        section.add(Box.createVerticalStrut(30));
        section.add(btnWrapper);
        
        return section;
    }
    
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(700, 45));
        combo.setMaximumSize(new Dimension(700, 45));
        combo.setBackground(WHITE);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return combo;
    }
    
    private JPanel createFieldGroup(String label, Component field) {
        return createFieldGroup(label, field, 700);
    }
    
    private JPanel createFieldGroup(String label, Component field, int width) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setBackground(WHITE);
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        group.setMaximumSize(new Dimension(width, 80));
        group.setPreferredSize(new Dimension(width, 80));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(DARK);
        labelComp.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        group.add(labelComp);
        group.add(Box.createVerticalStrut(8));
        
        if (field instanceof JTextField) {
            field.setPreferredSize(new Dimension(width, 45));
            field.setMaximumSize(new Dimension(width, 45));
            field.setMinimumSize(new Dimension(width, 45));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(width, 45));
            field.setMaximumSize(new Dimension(width, 45));
            field.setMinimumSize(new Dimension(width, 45));
        } else if (field instanceof JPanel) {
            field.setPreferredSize(new Dimension(width, 45));
            field.setMaximumSize(new Dimension(width, 45));
            field.setMinimumSize(new Dimension(width, 45));
        }
        
        ((JComponent) field).setAlignmentX(Component.LEFT_ALIGNMENT);
        group.add(field);
        
        return group;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setMaximumSize(new Dimension(700, 45));
        field.setPreferredSize(new Dimension(700, 45));
        field.setMinimumSize(new Dimension(700, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY, 2),
                    BorderFactory.createEmptyBorder(9, 14, 9, 14)
                ));
            }
            public void focusLost(FocusEvent e) {
                validateField(field);
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JPanel createSummaryPanel() {
        JPanel summary = new JPanel();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBackground(WHITE);
        summary.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER),
            BorderFactory.createEmptyBorder(40, 30, 40, 30)
        ));
        summary.setPreferredSize(new Dimension(380, getHeight()));
        
        JLabel title = new JLabel("Order Summary");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel roomCard = new JPanel();
        roomCard.setLayout(new BoxLayout(roomCard, BoxLayout.Y_AXIS));
        roomCard.setBackground(LIGHT_BG);
        roomCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        roomCard.setMaximumSize(new Dimension(340, 120));
        roomCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel roomTitle = new JLabel(roomType);
        roomTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        roomTitle.setForeground(PRIMARY);
        roomTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel roomDesc = new JLabel("Premium Resort Experience");
        roomDesc.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        roomDesc.setForeground(TEXT_MUTED);
        roomDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        roomCard.add(roomTitle);
        roomCard.add(Box.createVerticalStrut(5));
        roomCard.add(roomDesc);
        
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBackground(LIGHT_BG);
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        detailsCard.setMaximumSize(new Dimension(340, 160));
        detailsCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        detailsCard.add(createSummaryRow("Check-in", checkInDate, TEXT_MUTED, DARK));
        detailsCard.add(Box.createVerticalStrut(12));
        detailsCard.add(createSummaryRow("Check-out", checkOutDate, TEXT_MUTED, DARK));
        detailsCard.add(Box.createVerticalStrut(12));
        detailsCard.add(createSummaryRow("Capacity", guestCount + " guest(s) • " + roomCount + " room(s)", TEXT_MUTED, DARK));
        
        JPanel totalCard = new JPanel();
        totalCard.setLayout(new BoxLayout(totalCard, BoxLayout.Y_AXIS));
        totalCard.setBackground(PRIMARY);
        totalCard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        totalCard.setMaximumSize(new Dimension(340, 100));
        totalCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel totalLabel = new JLabel("Total Amount");
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        totalLabel.setForeground(new Color(220, 230, 255));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel amount = new JLabel("₱ " + price);
        amount.setFont(new Font("Segoe UI", Font.BOLD, 28));
        amount.setForeground(WHITE);
        amount.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        totalCard.add(totalLabel);
        totalCard.add(Box.createVerticalStrut(5));
        totalCard.add(amount);
        
        JButton backBtn = new JButton("← BACK TO ROOMS");
        backBtn.setBackground(new Color(73, 80, 87));
        backBtn.setForeground(WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setMaximumSize(new Dimension(340, 45));
        backBtn.setPreferredSize(new Dimension(340, 45));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setBorder(BorderFactory.createEmptyBorder());
        backBtn.addActionListener(e -> dispose());
        
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backBtn.setBackground(new Color(52, 58, 64));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                backBtn.setBackground(new Color(73, 80, 87));
            }
        });
        
        summary.add(title);
        summary.add(Box.createVerticalStrut(25));
        summary.add(roomCard);
        summary.add(Box.createVerticalStrut(20));
        summary.add(detailsCard);
        summary.add(Box.createVerticalStrut(20));
        summary.add(totalCard);
        summary.add(Box.createVerticalGlue());
        summary.add(backBtn);
        
        return summary;
    }
    
    private JPanel createSummaryRow(String label, String value, Color labelColor, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(LIGHT_BG);
        row.setMaximumSize(new Dimension(300, 20));
        
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(labelColor);
        
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 13));
        v.setForeground(valueColor);
        
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        
        return row;
    }
    
    private void populateUserData() {
        UserModel.User currentUser = userModel.getCurrentUser();
        if (currentUser == null) return;
        
        try {
            if (firstNameField != null && currentUser.getFirstName() != null) {
                firstNameField.setText(currentUser.getFirstName());
            }
            if (lastNameField != null && currentUser.getLastName() != null) {
                lastNameField.setText(currentUser.getLastName());
            }
            if (emailField != null && currentUser.getEmail() != null) {
                emailField.setText(currentUser.getEmail());
                emailField.setEditable(false);
                emailField.setBackground(LIGHT_BG);
            }
            if (mobileField != null && currentUser.getPhone() != null) {
                String phone = currentUser.getPhone();
                if (phone.startsWith("+63")) phone = phone.substring(3).trim();
                mobileField.setText(phone);
            }
            if (addr1Field != null && currentUser.getAddress() != null) {
                String address = currentUser.getAddress();
                if (address.contains(",")) {
                    String[] parts = address.split(",", 2);
                    addr1Field.setText(parts[0].trim());
                    if (addr2Field != null && parts.length > 1) {
                        addr2Field.setText(parts[1].trim());
                    }
                } else {
                    addr1Field.setText(address);
                }
            }
            if (cityField != null && currentUser.getCity() != null) {
                cityField.setText(currentUser.getCity());
            }
            if (countryCombo != null && currentUser.getCountry() != null) {
                String userCountry = currentUser.getCountry();
                for (int i = 0; i < countryCombo.getItemCount(); i++) {
                    if (countryCombo.getItemAt(i).equalsIgnoreCase(userCountry)) {
                        countryCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            if (acctNameField != null && currentUser.getFullName() != null) {
                acctNameField.setText(currentUser.getFullName());
            }
            
            SwingUtilities.invokeLater(() -> {
                validateAllFields();
                validateForm();
                // Scroll to top after populating data
                if (scrollPane != null) {
                    scrollPane.getViewport().setViewPosition(new Point(0, 0));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void validateAllFields() {
        if (firstNameField != null) validateField(firstNameField);
        if (lastNameField != null) validateField(lastNameField);
        if (emailField != null) validateField(emailField);
        if (mobileField != null) validateMobileField();
        if (addr1Field != null) validateField(addr1Field);
        if (addr2Field != null) validateField(addr2Field);
        if (cityField != null) validateField(cityField);
        if (zipField != null) validateField(zipField);
        if (referenceField != null) validateField(referenceField);
        if (acctNameField != null) validateField(acctNameField);
    }
    
    private void addValidationListeners(JTextField field) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                validateField(field);
                validateForm();
            }
        });
    }

    private void addNumericFilter(JTextField field) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != java.awt.event.KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
    }
    
    private void validateField(JTextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER, 2),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
            ));
        } else if (field == emailField && !isValidEmail(text)) {
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER, 2),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
            ));
        } else {
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SUCCESS, 2),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
            ));
        }
    }
    
    private void validateMobileField() {
        String mobile = mobileField.getText().trim();
        
        if (mobile.isEmpty()) {
            mobileField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER, 2),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
            ));
            return;
        }
        
        if (mobile.startsWith("0")) {
            if (mobile.length() > 1 && mobile.charAt(1) == '9') {
                SwingUtilities.invokeLater(() -> mobileField.setText(mobile.substring(1)));
                return;
            } else {
                mobileField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(DANGER, 2),
                    BorderFactory.createEmptyBorder(9, 14, 9, 14)
                ));
                return;
            }
        }
        
        if (!mobile.startsWith("9") || mobile.length() != 10) {
            mobileField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER, 2),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
            ));
            return;
        }
        
        mobileField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS, 2),
            BorderFactory.createEmptyBorder(9, 14, 9, 14)
        ));
    }
    
    private boolean isFormValid() {
        String mobile = mobileField.getText().trim();
        boolean mobileValid = !mobile.isEmpty() && mobile.startsWith("9") && mobile.length() == 10;
        
        JComboBox<String> discountCombo = findComponentByName(getContentPane(), "discountTypeCombo");
        JTextField idNumberField = findComponentByName(getContentPane(), "idNumberField");
        JTextField ageField = findComponentByName(getContentPane(), "ageField");
        
        boolean discountValid = true;
        if (discountCombo != null) {
            int selectedIndex = discountCombo.getSelectedIndex();
            
            if (selectedIndex == 1 || selectedIndex == 2) {
               
                discountValid = idNumberField != null && !idNumberField.getText().trim().isEmpty();
            } else if (selectedIndex == 3) {
                
                try {
                    int age = Integer.parseInt(ageField.getText().trim());
                    discountValid = age >= 0 && age < 12;
                } catch (NumberFormatException e) {
                    discountValid = false;
                }
            }
        }
        
        return !firstNameField.getText().trim().isEmpty() &&
               !lastNameField.getText().trim().isEmpty() &&
               isValidEmail(emailField.getText().trim()) &&
               mobileValid &&
               countryCombo.getSelectedIndex() > 0 &&
               !addr1Field.getText().trim().isEmpty() &&
               !addr2Field.getText().trim().isEmpty() &&
               !cityField.getText().trim().isEmpty() &&
               !zipField.getText().trim().isEmpty() &&
               paymentMethodCombo.getSelectedIndex() > 0 &&
               !referenceField.getText().trim().isEmpty() &&
               !acctNameField.getText().trim().isEmpty() &&
               discountValid &&
               privacyCheck.isSelected() &&
               bookingCheck.isSelected();
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    private void validateForm() {
        boolean isValid = isFormValid();
        submitBtn.setEnabled(isValid);
        submitBtn.setBackground(isValid ? PRIMARY : new Color(173, 181, 189));
    }
    
    private void handleSubmit() {
        if (!isFormValid()) {
            validateAllFields();
            JOptionPane.showMessageDialog(this, "Please fill in all required fields correctly.", 
                "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            return;
        }
        showConfirmationDialog();
    }
    
    private void showConfirmationDialog() {
        JDialog dialog = new JDialog(this, "Confirm Booking", true);
        dialog.setSize(600, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createLineBorder(PRIMARY, 3));
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel title = new JLabel("Confirm Your Booking");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Please review the details below");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(220, 230, 255));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 25, 40));
        
        content.add(createDetailRow("Guest", firstNameField.getText() + " " + lastNameField.getText()));
        content.add(Box.createVerticalStrut(10));
        content.add(createDetailRow("Email", emailField.getText()));
        content.add(Box.createVerticalStrut(10));
        String mobile = mobileField.getText().trim();
        if (!mobile.startsWith("9")) mobile = mobile.replaceFirst("^0+", "");
        content.add(createDetailRow("Mobile", "+63 " + mobile));
        content.add(Box.createVerticalStrut(20));
        content.add(createDetailRow("Room", roomType));
        content.add(Box.createVerticalStrut(10));
        content.add(createDetailRow("Check-in", checkInDate));
        content.add(Box.createVerticalStrut(10));
        content.add(createDetailRow("Check-out", checkOutDate));
        content.add(Box.createVerticalStrut(10));
        content.add(createDetailRow("Capacity", guestCount + " guest(s) • " + roomCount + " room(s)"));
        content.add(Box.createVerticalStrut(10));
        content.add(createDetailRow("Payment", (String) paymentMethodCombo.getSelectedItem()));
        content.add(Box.createVerticalStrut(25));
        
        JPanel totalBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        totalBox.setBackground(new Color(240, 248, 255));
        totalBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        totalBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        
        JLabel totalLbl = new JLabel("Total Amount");
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalLbl.setForeground(TEXT_MUTED);
        
        JLabel amtLbl = new JLabel("₱ " + price);
        amtLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        amtLbl.setForeground(PRIMARY);
        
        totalBox.add(totalLbl);
        totalBox.add(amtLbl);
        content.add(totalBox);
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttons.setBackground(WHITE);
        
        JButton confirmBtn = new JButton("CONFIRM BOOKING");
        confirmBtn.setBackground(PRIMARY);
        confirmBtn.setForeground(WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmBtn.setPreferredSize(new Dimension(200, 45));
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmBtn.setBorder(BorderFactory.createEmptyBorder());
        confirmBtn.addActionListener(e -> {
            dialog.dispose();
            showReceipt();
        });
        
        confirmBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
            	confirmBtn.setBackground(DARK_PLUM);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                confirmBtn.setBackground(PRIMARY);
            }
        });
        
        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setBackground(LIGHT_BG);
        cancelBtn.setForeground(DARK);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(130, 45));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                cancelBtn.setBackground(new Color(233, 236, 239));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                cancelBtn.setBackground(LIGHT_BG);
            }
        });
        
        buttons.add(confirmBtn);
        buttons.add(cancelBtn);
        
        main.add(header, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);
        
        dialog.add(main);
        dialog.setVisible(true);
    }
    
    private JPanel createDetailRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setBackground(WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        l.setForeground(TEXT_MUTED);
        l.setPreferredSize(new Dimension(100, 25));
        
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 13));
        v.setForeground(DARK);
        
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        
        return row;
    }
    
    private void showReceipt() {
        String receiptNumber = "REC-" + System.currentTimeMillis();
        String guestFullName = firstNameField.getText() + " " + lastNameField.getText();
        String guestEmail = emailField.getText().trim();
        
        UserModel.User currentUser = userModel.getCurrentUser();
        if (currentUser != null) {
            currentUser.incrementBookings();
            userModel.saveCurrentUser();
        }
        
        GuestModel.GuestRecord guestRecord = new GuestModel.GuestRecord(
            "", firstNameField.getText().trim(), lastNameField.getText().trim(),
            emailField.getText().trim(), "", mobileField.getText().trim(),
            addr1Field.getText().trim(), addr2Field.getText().trim(),
            cityField.getText().trim(), zipField.getText().trim(),
            (String) countryCombo.getSelectedItem()
        );
        guestModel.saveGuestData(guestRecord);
        
        AdminModel.BookingRecord bookingRecord = new AdminModel.BookingRecord(
            receiptNumber, guestFullName, emailField.getText().trim(),
            roomType, checkInDate, checkOutDate, exactAmount, new Date(),
            (String) paymentMethodCombo.getSelectedItem()
        );
        adminModel.addBooking(bookingRecord);
        
        GuestModel.BookingPaymentRecord paymentRecord = new GuestModel.BookingPaymentRecord(
            receiptNumber, emailField.getText().trim(), guestFullName,
            roomType, roomCount, guestCount, checkInDate, checkOutDate,
            (String) paymentMethodCombo.getSelectedItem(),
            acctNameField.getText().trim(), referenceField.getText().trim(), exactAmount
        );
        guestModel.saveBookingPayment(paymentRecord);
        
        if (adminModel != null) {
            List<AdminModel.RoomUnit> allRooms = adminModel.getAllRoomUnits();
            int roomsToOccupy = roomCount;
            
            for (AdminModel.RoomUnit room : allRooms) {
                if (roomsToOccupy <= 0) break;
                if (room.getRoomType().equals(roomType) && room.getStatus().equals("Available")) {
                    adminModel.updateRoomStatus(room.getRoomId(), "Occupied");
                    roomsToOccupy--;
                }
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            boolean emailSent = undouse_hotel.service.EmailService.sendBookingConfirmation(
                guestEmail,
                guestFullName,
                receiptNumber,
                roomType,
                checkInDate,
                checkOutDate,
                exactAmount,
                (String) paymentMethodCombo.getSelectedItem(),
                roomCount,
                guestCount
            );
            
            if (emailSent) {
                System.out.println("✅ Confirmation email sent to: " + guestEmail);
            } else {
                System.err.println("⚠️ Warning: Could not send confirmation email");
            }
        });
        
        JDialog receiptDialog = new JDialog(this, "Booking Confirmed", true);
        receiptDialog.setSize(650, 750); 
        receiptDialog.setLocationRelativeTo(this);
        receiptDialog.setUndecorated(true);
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createLineBorder(SUCCESS, 3));
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(SUCCESS);
        header.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel title = new JLabel("Booking Confirmed!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel receipt = new JLabel("Receipt #" + receiptNumber);
        receipt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        receipt.setForeground(new Color(230, 255, 240));
        receipt.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(receipt);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 25, 40));
        
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(WHITE);
        
        scrollContent.add(createDetailRow("Guest", guestFullName));
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(createDetailRow("Email", emailField.getText()));
        scrollContent.add(Box.createVerticalStrut(10));
        String mobile = mobileField.getText().trim();
        if (!mobile.startsWith("9")) mobile = mobile.replaceFirst("^0+", "");
        scrollContent.add(createDetailRow("Mobile", "+63 " + mobile));
        scrollContent.add(Box.createVerticalStrut(20));
        scrollContent.add(createDetailRow("Room", roomType));
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(createDetailRow("Check-in", checkInDate));
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(createDetailRow("Check-out", checkOutDate));
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(createDetailRow("Guests", guestCount + " guest(s)"));
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(createDetailRow("Rooms", roomCount + " room(s)"));
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(createDetailRow("Payment", (String) paymentMethodCombo.getSelectedItem()));
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(createDetailRow("Reference", referenceField.getText()));
        scrollContent.add(Box.createVerticalStrut(25));
        
        JPanel totalBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        totalBox.setBackground(new Color(240, 255, 245));
        totalBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS, 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        totalBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        
        JLabel totalLbl = new JLabel("Amount Paid");
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalLbl.setForeground(TEXT_MUTED);
        
        JLabel amtLbl = new JLabel("₱ " + price);
        amtLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        amtLbl.setForeground(SUCCESS);
        
        totalBox.add(totalLbl);
        totalBox.add(amtLbl);
        scrollContent.add(totalBox);
        scrollContent.add(Box.createVerticalStrut(20));
        
        JPanel infoBox = new JPanel(new BorderLayout());
        infoBox.setBackground(new Color(240, 248, 255));
        infoBox.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        JPanel infoContent = new JPanel();
        infoContent.setLayout(new BoxLayout(infoContent, BoxLayout.Y_AXIS));
        infoContent.setBackground(new Color(240, 248, 255));

        JLabel infoTitle = new JLabel("Important");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoTitle.setForeground(PRIMARY);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea infoText = new JTextArea(
            "Confirmation sent to " + emailField.getText() + ". Present this receipt at check-in."
        );
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setEditable(false);
        infoText.setOpaque(false);
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoText.setForeground(TEXT_MUTED);
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoText.setPreferredSize(new Dimension(500, 40));
        infoText.setMaximumSize(new Dimension(500, 40));

        infoContent.add(infoTitle);
        infoContent.add(Box.createVerticalStrut(5));
        infoContent.add(infoText);
        
        infoBox.add(infoContent, BorderLayout.WEST);

        scrollContent.add(infoBox);
        
        JScrollPane contentScroll = new JScrollPane(scrollContent);
        contentScroll.setBorder(null);
        contentScroll.getVerticalScrollBar().setUnitIncrement(16);
        contentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        content.add(contentScroll);
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttons.setBackground(WHITE);
        
        JButton doneBtn = new JButton("DONE");
        doneBtn.setBackground(SUCCESS);
        doneBtn.setForeground(WHITE);
        doneBtn.setFocusPainted(false);
        doneBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        doneBtn.setPreferredSize(new Dimension(160, 45));
        doneBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        doneBtn.setBorder(BorderFactory.createEmptyBorder());
        doneBtn.addActionListener(e -> {
            receiptDialog.dispose();
            dispose();
            if (onCompletion != null) onCompletion.run();
        });
        
        doneBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
            	doneBtn.setBackground(new Color(20, 108, 67));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                doneBtn.setBackground(SUCCESS);
            }
        });
        
        buttons.add(doneBtn);
        
        main.add(header, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);
        
        receiptDialog.add(main);
        receiptDialog.setVisible(true);
    }
}