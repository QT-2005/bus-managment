package GUI;

import DAL.CustomerDAO;
import DTO.Customer;
import DTO.User;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import javax.swing.ImageIcon;


public class MainDashboard extends JFrame {
    private final User currentUser;
    private JButton btnTicketBooking;
    private JButton btnCustomerManagement;
    private JButton btnBusManagement;
    private JButton btnRouteManagement;

    private JButton btnUserManagement;
    private JButton btnLogout;
    private JPanel contentPanel;
    private final Color sidebarColor = new Color(0, 102, 204);
    private final Color buttonHoverColor = new Color(0, 82, 164);
    private Image headerBackground;

    public MainDashboard(User user) {
        this.currentUser = user;
        loadHeaderBackground();
        initComponents();
        setupLayout();
        setupListeners(user);


        setTitle("Bus Ticket Management System - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    private void loadHeaderBackground() {
        try {
            headerBackground = new ImageIcon(Objects.requireNonNull(
                    getClass().getResource("/ImageBackground/tittle.png"))).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            headerBackground = null;
        }
    }

    private void initComponents() {
        btnTicketBooking = createMenuButton("Ticket Booking", "📃");
        btnCustomerManagement = createMenuButton("Customer", "👥");
        btnBusManagement = createMenuButton("Bus", "🚌");
        btnRouteManagement = createMenuButton("Route", "🗺️");
        btnUserManagement = createMenuButton("User", "👤");
        btnLogout = createMenuButton("Logout", "🚪");

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Ẩn các nút nếu không phải admin
        if (!"ADMIN".equals(currentUser.getRole())) {
            btnCustomerManagement.setVisible(false);
            btnBusManagement.setVisible(false);

        }
    }

    private JButton createMenuButton(String text, String icon) {
        JButton button = new JButton(icon + "  " + text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        button.setForeground(Color.WHITE);
        button.setBackground(sidebarColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(0, 15, 0, 0));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonHoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(sidebarColor);
            }
        });

        return button;
    }

    private void setupLayout() {
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout());

        // Header Panel with background image
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (headerBackground != null) {
                    g.drawImage(headerBackground, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(242, 227, 57)); // Fallback color
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Create a panel to hold both title and user info (vertical layout)
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false); // Make it transparent
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Title label
        JLabel lblTitle = new JLabel("Bus Ticket Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // User info panel
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Changed to LEFT
        userInfoPanel.setOpaque(false);
        JLabel lblUserInfo = new JLabel(String.format(
                "Welcome, %s (%s)",
                currentUser.getFullName(),
                currentUser.getRole()));
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUserInfo.setForeground(Color.WHITE); // Changed to WHITE to match background
        userInfoPanel.add(lblUserInfo);
        userInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components to leftPanel with spacing
        leftPanel.add(lblTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add small vertical gap
        leftPanel.add(userInfoPanel);

        // Add leftPanel to header (west side)
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Rest of the code remains exactly the same...
        // Sidebar Panel
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(sidebarColor);
        sidebarPanel.setPreferredSize(new Dimension(260, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add buttons to sidebar
        addToSidebar(sidebarPanel, btnTicketBooking);
        addToSidebar(sidebarPanel, btnCustomerManagement);
        addToSidebar(sidebarPanel, btnBusManagement);
        addToSidebar(sidebarPanel, btnRouteManagement);

        addToSidebar(sidebarPanel, btnUserManagement);

        // Add logout button at bottom
        sidebarPanel.add(Box.createVerticalGlue());
        addToSidebar(sidebarPanel, btnLogout);

        // Welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(240, 248, 255));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel welcomeLabel = new JLabel("Welcome to the Bus Ticket Management System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Assemble main container
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(sidebarPanel, BorderLayout.WEST);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainContainer);
    }

    private void addToSidebar(JPanel sidebar, JButton button) {
        button.setMaximumSize(new Dimension(260, 48));
        button.setPreferredSize(new Dimension(260, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void setupListeners(User user) {
        btnTicketBooking.addActionListener(e -> {
            new TicketBookingFrame().setVisible(true);
        });

        btnCustomerManagement.addActionListener(e -> {
            new CustomerManagementFrame().setVisible(true);
        });

        btnBusManagement.addActionListener(e -> {
            new BusManagementFrame().setVisible(true);
        });

        btnRouteManagement.addActionListener(e -> {
            new RouteManagementFrame(currentUser.getRole()).setVisible(true);
        });

        btnUserManagement.addActionListener(e -> {
            if ("ADMIN".equals(currentUser.getRole())) {
                new UserManagementFrame().setVisible(true);
            } else {
               new UserProfileFrame(user).setVisible(true);
            }
        });

        btnLogout.addActionListener(e -> {
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION)) {
                new Login().setVisible(true);
                dispose();
            }
        });
    }
}