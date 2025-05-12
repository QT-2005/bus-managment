package GUI;

import BLL.CustomerManager;
import BLL.RouteManager;
import BLL.TicketManager;
import DTO.Customer;
import DTO.Route;
import DTO.Ticket;
import Utils.DateUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class TicketBookingFrame extends JFrame {
    private JComboBox<Route> cmbRoutes;
    private JTextField txtCustomerName;
    private JTextField txtCustomerPhone;
    private JTextField txtCustomerEmail;
    private JTextField txtTravelDate;
    private JComboBox<String> cmbSeatNumber;
    private JTextField txtPrice;
    private JButton btnSearchCustomer;
    private JButton btnBookTicket;
    private JButton btnCancel;
    private JTable tblTickets;
    private DefaultTableModel ticketTableModel;

    private RouteManager routeManager;
    private CustomerManager customerManager;
    private TicketManager ticketManager;
    private Customer selectedCustomer;

    public TicketBookingFrame() {
        routeManager = new RouteManager();
        customerManager = new CustomerManager();
        ticketManager = new TicketManager();

        initComponents();
        loadRoutes();
        setupLayout();
        setupListeners();
        loadTickets();

        setTitle("Ticket Booking");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        cmbRoutes = new JComboBox<>();
        txtCustomerName = new JTextField(20);
        txtCustomerPhone = new JTextField(20);
        txtCustomerEmail = new JTextField(20);
        txtTravelDate = new JTextField(10);
        cmbSeatNumber = new JComboBox<>();
        txtPrice = new JTextField(10);
        txtPrice.setEditable(false);

        // Add some sample seat numbers
        for (int i = 1; i <= 40; i++) {
            cmbSeatNumber.addItem("Seat " + i);
        }

        btnSearchCustomer = new JButton("Search Customer");
        btnBookTicket = new JButton("Book Ticket");
        btnCancel = new JButton("Cancel");

        // Setup table
        String[] columns = {"Ticket ID", "Route", "Customer", "Seat", "Price", "Booking Date", "Travel Date", "Status"};
        ticketTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTickets = new JTable(ticketTableModel);
        tblTickets.getTableHeader().setReorderingAllowed(false);
    }

    private void loadRoutes() {
        List<Route> routes = routeManager.getAllRoutes();
        cmbRoutes.removeAllItems();

        for (Route route : routes) {
            cmbRoutes.addItem(route);
        }

        if (cmbRoutes.getItemCount() > 0) {
            cmbRoutes.setSelectedIndex(0);
            updatePrice();
        }
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Ticket Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Route selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Route:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(cmbRoutes, gbc);

        // Customer information
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Customer Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtCustomerName, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        formPanel.add(btnSearchCustomer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(txtCustomerPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(txtCustomerEmail, gbc);

        // Travel details
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Travel Date (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(txtTravelDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Seat Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(cmbSeatNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(txtPrice, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnBookTicket);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        formPanel.add(buttonPanel, gbc);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Recent Tickets"));
        tablePanel.add(new JScrollPane(tblTickets), BorderLayout.CENTER);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupListeners() {
        cmbRoutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePrice();
            }
        });

        btnSearchCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomer();
            }
        });

        btnBookTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookTicket();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void updatePrice() {
        if (cmbRoutes.getSelectedItem() != null) {
            Route selectedRoute = (Route) cmbRoutes.getSelectedItem();
            txtPrice.setText(String.valueOf(selectedRoute.getBasePrice()));
        }
    }

    private void searchCustomer() {
        String searchTerm = txtCustomerName.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a customer name to search", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Customer> customers = customerManager.searchCustomers(searchTerm);

        if (customers.isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Customer not found. Would you like to add a new customer?",
                    "Customer Not Found",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                CustomerManagementFrame customerFrame = new CustomerManagementFrame();
                customerFrame.setVisible(true);
            }
        } else if (customers.size() == 1) {
            selectedCustomer = customers.get(0);
            txtCustomerName.setText(selectedCustomer.getName());
            txtCustomerPhone.setText(selectedCustomer.getPhone());
            txtCustomerEmail.setText(selectedCustomer.getEmail());
        } else {
            // Multiple customers found, show selection dialog
            Object[] options = customers.toArray();
            Customer selected = (Customer) JOptionPane.showInputDialog(
                    this,
                    "Multiple customers found. Please select one:",
                    "Select Customer",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (selected != null) {
                selectedCustomer = selected;
                txtCustomerName.setText(selectedCustomer.getName());
                txtCustomerPhone.setText(selectedCustomer.getPhone());
                txtCustomerEmail.setText(selectedCustomer.getEmail());
            }
        }
    }

    private void bookTicket() {
        // Validate inputs
        if (cmbRoutes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a route", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtCustomerName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtTravelDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter travel date", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse travel date
        Date travelDate = DateUtils.parseDate(txtTravelDate.getText().trim());
        if (travelDate == null) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create or get customer
        if (selectedCustomer == null) {
            selectedCustomer = new Customer();
            selectedCustomer.setName(txtCustomerName.getText().trim());
            selectedCustomer.setPhone(txtCustomerPhone.getText().trim());
            selectedCustomer.setEmail(txtCustomerEmail.getText().trim());
            selectedCustomer.setAddress(""); // Default empty address

            if (!customerManager.addCustomer(selectedCustomer)) {
                JOptionPane.showMessageDialog(this, "Failed to add new customer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Create ticket
        Route selectedRoute = (Route) cmbRoutes.getSelectedItem();
        String seatNumber = (String) cmbSeatNumber.getSelectedItem();
        double price = Double.parseDouble(txtPrice.getText().trim());

        Ticket ticket = new Ticket();
        ticket.setRouteId(selectedRoute.getId());
        ticket.setCustomerId(selectedCustomer.getId());
        ticket.setSeatNumber(seatNumber);
        ticket.setPrice(price);
        ticket.setBookingDate(new Date()); // Current date
        ticket.setTravelDate(travelDate);
        ticket.setStatus("BOOKED");

        if (ticketManager.addTicket(ticket)) {
            JOptionPane.showMessageDialog(this, "Ticket booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadTickets();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to book ticket", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        if (cmbRoutes.getItemCount() > 0) {
            cmbRoutes.setSelectedIndex(0);
        }
        txtCustomerName.setText("");
        txtCustomerPhone.setText("");
        txtCustomerEmail.setText("");
        txtTravelDate.setText("");
        if (cmbSeatNumber.getItemCount() > 0) {
            cmbSeatNumber.setSelectedIndex(0);
        }
        updatePrice();
        selectedCustomer = null;
    }

    private void loadTickets() {
        // Clear table
        ticketTableModel.setRowCount(0);

        // Get all tickets
        List<Ticket> tickets = ticketManager.getAllTickets();

        // Populate table
        for (Ticket ticket : tickets) {
            Route route = routeManager.getRouteById(ticket.getRouteId());
            Customer customer = customerManager.getCustomerById(ticket.getCustomerId());

            String routeStr = route != null ? route.getSource() + " to " + route.getDestination() : "Unknown";
            String customerStr = customer != null ? customer.getName() : "Unknown";

            Object[] row = {
                    ticket.getId(),
                    routeStr,
                    customerStr,
                    ticket.getSeatNumber(),
                    ticket.getPrice(),
                    DateUtils.formatDate(ticket.getBookingDate()),
                    DateUtils.formatDate(ticket.getTravelDate()),
                    ticket.getStatus()
            };

            ticketTableModel.addRow(row);
        }
    }
}