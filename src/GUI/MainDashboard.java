package GUI;

import DTO.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JButton btnTicketBooking;
    private JButton btnCustomerManagement;
    private JButton btnBusManagement;
    private JButton btnRouteManagement;
    private JButton btnReports;
    private JButton btnUserManagement;
    private JButton btnLogout;

    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setupLayout();
        setupListeners();

        setTitle("Bus Ticket Management System - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        btnTicketBooking = new JButton("Ticket Booking");
        btnCustomerManagement = new JButton("Customer Management");
        btnBusManagement = new JButton("Bus Management");
        btnRouteManagement = new JButton("Route Management");
        btnReports = new JButton("Reports");
        btnUserManagement = new JButton("User Management");
        btnLogout = new JButton("Logout");

        // Disable user management for non-admin users
        if (!"ADMIN".equals(currentUser.getRole())) {
            btnUserManagement.setEnabled(false);
        }
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Bus Ticket Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblUserInfo = new JLabel("Welcome, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        userInfoPanel.add(lblUserInfo);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);

        // Menu Panel
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        menuPanel.add(btnTicketBooking);
        menuPanel.add(btnCustomerManagement);
        menuPanel.add(btnBusManagement);
        menuPanel.add(btnRouteManagement);
        menuPanel.add(btnReports);
        menuPanel.add(btnUserManagement);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(btnLogout);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupListeners() {
        btnTicketBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TicketBookingFrame ticketFrame = new TicketBookingFrame();
                ticketFrame.setVisible(true);
            }
        });

        btnCustomerManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerManagementFrame customerFrame = new CustomerManagementFrame();
                customerFrame.setVisible(true);
            }
        });

        btnBusManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BusManagementFrame busFrame = new BusManagementFrame();
                busFrame.setVisible(true);
            }
        });

        btnRouteManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RouteManagementFrame routeFrame = new RouteManagementFrame();
                routeFrame.setVisible(true);
            }
        });

        btnReports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainDashboard.this,
                        "Reports feature is coming soon!",
                        "Under Development",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnUserManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ADMIN".equals(currentUser.getRole())) {
                    UserManagementFrame userFrame = new UserManagementFrame();
                    userFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(MainDashboard.this,
                            "You don't have permission to access this feature",
                            "Access Denied", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(MainDashboard.this,
                        "Are you sure you want to logout?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    Login loginFrame = new Login();
                    loginFrame.setVisible(true);
                    dispose();
                }
            }
        });
    }
}