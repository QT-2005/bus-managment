package GUI;

import BLL.BusManager;
import BLL.RouteManager;
import DTO.Bus;
import DTO.Route;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    private String role; // NEW: store role ("Admin" or "User")

    public RouteManagementFrame(String role) {
        this.role = role;
        routeManager = new RouteManager();
        busManager = new BusManager();

        initComponents();
        loadBuses();
        setupLayout();
        setupListeners();
        loadRoutes();
        applyRoleBasedAccess(); // NEW

        setTitle("Route Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void applyRoleBasedAccess() {
        if ("User".equalsIgnoreCase(role)) {
            btnAdd.setVisible(false);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);
            btnClear.setVisible(false);
            cmbBus.setEnabled(false); // disable combo box for user
        }
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

        String[] columns = { "ID", "Source", "Destination", "Distance (km)", "Duration (min)", "Bus", "Departure Time",
                "Base Price" };
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

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Route Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] labels = { "Source:", "Destination:", "Distance (km):", "Duration (min):", "Bus:",
                "Departure Time (HH:MM):", "Base Price:" };
        JTextField[] fields = { txtSource, txtDestination, txtDistance, txtDuration, null, txtDepartureTime,
                txtBasePrice };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            if (i == 4) {
                formPanel.add(cmbBus, gbc);
            } else {
                formPanel.add(fields[i], gbc);
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Route List"));
        tablePanel.add(new JScrollPane(tblRoutes), BorderLayout.CENTER);

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void setupListeners() {
        btnAdd.addActionListener(e -> addRoute());
        btnUpdate.addActionListener(e -> updateRoute());
        btnDelete.addActionListener(e -> deleteRoute());
        btnClear.addActionListener(e -> clearForm());

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
        routeTableModel.setRowCount(0);
        List<Route> routes = routeManager.getAllRoutes();

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
        if (!validateForm())
            return;

        try {
            Route route = new Route();
            route.setSource(txtSource.getText().trim());
            route.setDestination(txtDestination.getText().trim());
            route.setDistance(Double.parseDouble(txtDistance.getText().trim()));
            route.setDuration(Integer.parseInt(txtDuration.getText().trim()));
            route.setBusId(((Bus) cmbBus.getSelectedItem()).getId());
            route.setDepartureTime(parseTime(txtDepartureTime.getText().trim())); // ✔ Xử lý trong try
            route.setBasePrice(Double.parseDouble(txtBasePrice.getText().trim()));

            if (routeManager.addRoute(route)) {
                JOptionPane.showMessageDialog(this, "Route added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadRoutes();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add route", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid departure time format (HH:mm)", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoute() {
        if (selectedRoute == null) {
            JOptionPane.showMessageDialog(this, "Please select a route to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validateForm())
            return;

        try {
            selectedRoute.setSource(txtSource.getText().trim());
            selectedRoute.setDestination(txtDestination.getText().trim());
            selectedRoute.setDistance(Double.parseDouble(txtDistance.getText().trim()));
            selectedRoute.setDuration(Integer.parseInt(txtDuration.getText().trim()));
            selectedRoute.setBusId(((Bus) cmbBus.getSelectedItem()).getId());
            selectedRoute.setDepartureTime(parseTime(txtDepartureTime.getText().trim())); // ✔ Xử lý trong try
            selectedRoute.setBasePrice(Double.parseDouble(txtBasePrice.getText().trim()));

            if (routeManager.updateRoute(selectedRoute)) {
                JOptionPane.showMessageDialog(this, "Route updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadRoutes();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update route", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid departure time format (HH:mm)", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoute() {
        if (selectedRoute == null) {
            JOptionPane.showMessageDialog(this, "Please select a route to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this route?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (routeManager.deleteRoute(selectedRoute.getId())) {
                JOptionPane.showMessageDialog(this, "Route deleted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
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
        if (cmbBus.getItemCount() > 0)
            cmbBus.setSelectedIndex(0);
        txtDepartureTime.setText("");
        txtBasePrice.setText("");
        selectedRoute = null;
    }

    private boolean validateForm() {
        if (txtSource.getText().trim().isEmpty() || txtDestination.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Source and Destination are required.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double d = Double.parseDouble(txtDistance.getText().trim());
            if (d <= 0)
                throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid distance", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int dur = Integer.parseInt(txtDuration.getText().trim());
            if (dur <= 0)
                throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid duration", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cmbBus.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a bus", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            parseTime(txtDepartureTime.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid departure time. Use HH:mm", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double price = Double.parseDouble(txtBasePrice.getText().trim());
            if (price <= 0)
                throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid base price", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private Time parseTime(String timeStr) throws ParseException {
        return new Time(new SimpleDateFormat("HH:mm").parse(timeStr).getTime());
    }
}
