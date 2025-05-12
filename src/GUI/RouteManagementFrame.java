package GUI;

import BLL.BusManager;
import BLL.RouteManager;
import DTO.Bus;
import DTO.Route;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RouteManagementFrame extends JFrame {
    private JTextField txtSource;
    private JTextField txtDestination;
    private JTextField txtDistance;
    private JTextField txtDuration;
    private JComboBox<Bus> cmbBus;
    private JTextField txtDepartureTime;
    private JTextField txtBasePrice;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable tblRoutes;
    private DefaultTableModel routeTableModel;

    private RouteManager routeManager;
    private BusManager busManager;
    private Route selectedRoute;

    public RouteManagementFrame() {
        routeManager = new RouteManager();
        busManager = new BusManager();

        initComponents();
        loadBuses();
        setupLayout();
        setupListeners();
        loadRoutes();

        setTitle("Route Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        txtSource = new JTextField(20);
        txtDestination = new JTextField(20);
        txtDistance = new JTextField(20);
        txtDuration = new JTextField(20);
        cmbBus = new JComboBox<>();
        txtDepartureTime = new JTextField(20);
        txtBasePrice = new JTextField(20);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        // Setup table
        String[] columns = {"ID", "Source", "Destination", "Distance (km)", "Duration (min)", "Bus", "Departure Time", "Base Price"};
        routeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblRoutes = new JTable(routeTableModel);
        tblRoutes.getTableHeader().setReorderingAllowed(false);
    }

    private void loadBuses() {
        List<Bus> buses = busManager.getAllBuses();
        cmbBus.removeAllItems();

        for (Bus bus : buses) {
            if ("ACTIVE".equals(bus.getStatus())) {
                cmbBus.addItem(bus);
            }
        }
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Route Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Source
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Source:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtSource, gbc);

        // Destination
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Destination:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtDestination, gbc);

        // Distance
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Distance (km):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtDistance, gbc);

        // Duration
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Duration (min):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtDuration, gbc);

        // Bus
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Bus:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(cmbBus, gbc);

        // Departure Time
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Departure Time (HH:MM):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(txtDepartureTime, gbc);

        // Base Price
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Base Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(txtBasePrice, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Route List"));
        tablePanel.add(new JScrollPane(tblRoutes), BorderLayout.CENTER);

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupListeners() {
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoute();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoute();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoute();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        tblRoutes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblRoutes.getSelectedRow();
                if (selectedRow >= 0) {
                    int routeId = Integer.parseInt(tblRoutes.getValueAt(selectedRow, 0).toString());
                    selectedRoute = routeManager.getRouteById(routeId);
                    if (selectedRoute != null) {
                        txtSource.setText(selectedRoute.getSource());
                        txtDestination.setText(selectedRoute.getDestination());
                        txtDistance.setText(String.valueOf(selectedRoute.getDistance()));
                        txtDuration.setText(String.valueOf(selectedRoute.getDuration()));

                        // Find and select the bus
                        for (int i = 0; i < cmbBus.getItemCount(); i++) {
                            Bus bus = cmbBus.getItemAt(i);
                            if (bus.getId() == selectedRoute.getBusId()) {
                                cmbBus.setSelectedIndex(i);
                                break;
                            }
                        }

                        txtDepartureTime.setText(selectedRoute.getDepartureTime().toString());
                        txtBasePrice.setText(String.valueOf(selectedRoute.getBasePrice()));
                    }
                }
            }
        });
    }

    private void loadRoutes() {
        // Clear table
        routeTableModel.setRowCount(0);

        // Get all routes
        List<Route> routes = routeManager.getAllRoutes();

        // Populate table
        for (Route route : routes) {
            Bus bus = busManager.getBusById(route.getBusId());
            String busStr = bus != null ? bus.getBusNumber() + " (" + bus.getBusType() + ")" : "Unknown";

            Object[] row = {
                    route.getId(),
                    route.getSource(),
                    route.getDestination(),
                    route.getDistance(),
                    route.getDuration(),
                    busStr,
                    route.getDepartureTime(),
                    route.getBasePrice()
            };

            routeTableModel.addRow(row);
        }
    }

    private void addRoute() {
        // Validate inputs
        if (txtSource.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter source", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtDestination.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter destination", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double distance;
        try {
            distance = Double.parseDouble(txtDistance.getText().trim());
            if (distance <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid distance", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(txtDuration.getText().trim());
            if (duration <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid duration", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cmbBus.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a bus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Time departureTime;
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            departureTime = new Time(timeFormat.parse(txtDepartureTime.getText().trim()).getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid departure time (HH:MM)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double basePrice;
        try {
            basePrice = Double.parseDouble(txtBasePrice.getText().trim());
            if (basePrice <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid base price", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create route
        Route route = new Route();
        route.setSource(txtSource.getText().trim());
        route.setDestination(txtDestination.getText().trim());
        route.setDistance(distance);
        route.setDuration(duration);
        route.setBusId(((Bus) cmbBus.getSelectedItem()).getId());
        route.setDepartureTime(departureTime);
        route.setBasePrice(basePrice);

        if (routeManager.addRoute(route)) {
            JOptionPane.showMessageDialog(this, "Route added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadRoutes();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add route", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoute() {
        if (selectedRoute == null) {
            JOptionPane.showMessageDialog(this, "Please select a route to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate inputs
        if (txtSource.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter source", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtDestination.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter destination", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double distance;
        try {
            distance = Double.parseDouble(txtDistance.getText().trim());
            if (distance <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid distance", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(txtDuration.getText().trim());
            if (duration <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid duration", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cmbBus.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a bus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Time departureTime;
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            departureTime = new Time(timeFormat.parse(txtDepartureTime.getText().trim()).getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid departure time (HH:MM)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double basePrice;
        try {
            basePrice = Double.parseDouble(txtBasePrice.getText().trim());
            if (basePrice <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid base price", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update route
        selectedRoute.setSource(txtSource.getText().trim());
        selectedRoute.setDestination(txtDestination.getText().trim());
        selectedRoute.setDistance(distance);
        selectedRoute.setDuration(duration);
        selectedRoute.setBusId(((Bus) cmbBus.getSelectedItem()).getId());
        selectedRoute.setDepartureTime(departureTime);
        selectedRoute.setBasePrice(basePrice);

        if (routeManager.updateRoute(selectedRoute)) {
            JOptionPane.showMessageDialog(this, "Route updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadRoutes();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update route", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoute() {
        if (selectedRoute == null) {
            JOptionPane.showMessageDialog(this, "Please select a route to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this route?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (routeManager.deleteRoute(selectedRoute.getId())) {
                JOptionPane.showMessageDialog(this, "Route deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadRoutes();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete route", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtSource.setText("");
        txtDestination.setText("");
        txtDistance.setText("");
        txtDuration.setText("");
        if (cmbBus.getItemCount() > 0) {
            cmbBus.setSelectedIndex(0);
        }
        txtDepartureTime.setText("");
        txtBasePrice.setText("");
        selectedRoute = null;
    }
}