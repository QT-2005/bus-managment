package GUI;

import BLL.BusManager;
import DTO.Bus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BusManagementFrame extends JFrame {
    private JTextField txtBusNumber;
    private JComboBox<String> cmbBusType;
    private JTextField txtCapacity;
    private JComboBox<String> cmbStatus;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable tblBuses;
    private DefaultTableModel busTableModel;

    private BusManager busManager;
    private Bus selectedBus;

    public BusManagementFrame() {
        busManager = new BusManager();

        initComponents();
        setupLayout();
        setupListeners();
        loadBuses();

        setTitle("Bus Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        txtBusNumber = new JTextField(20);

        cmbBusType = new JComboBox<>();
        cmbBusType.addItem("AC");
        cmbBusType.addItem("NON-AC");
        cmbBusType.addItem("SLEEPER");
        cmbBusType.addItem("SEMI-SLEEPER");
        cmbBusType.addItem("LUXURY");

        txtCapacity = new JTextField(20);

        cmbStatus = new JComboBox<>();
        cmbStatus.addItem("ACTIVE");
        cmbStatus.addItem("MAINTENANCE");
        cmbStatus.addItem("INACTIVE");

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        // Setup table
        String[] columns = {"ID", "Bus Number", "Bus Type", "Capacity", "Status"};
        busTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblBuses = new JTable(busTableModel);
        tblBuses.getTableHeader().setReorderingAllowed(false);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Bus Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Bus Number
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Bus Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtBusNumber, gbc);

        // Bus Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Bus Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(cmbBusType, gbc);

        // Capacity
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Capacity:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtCapacity, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(cmbStatus, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Bus List"));
        tablePanel.add(new JScrollPane(tblBuses), BorderLayout.CENTER);

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupListeners() {
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBus();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBus();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBus();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        tblBuses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblBuses.getSelectedRow();
                if (selectedRow >= 0) {
                    int busId = Integer.parseInt(tblBuses.getValueAt(selectedRow, 0).toString());
                    selectedBus = busManager.getBusById(busId);
                    if (selectedBus != null) {
                        txtBusNumber.setText(selectedBus.getBusNumber());
                        cmbBusType.setSelectedItem(selectedBus.getBusType());
                        txtCapacity.setText(String.valueOf(selectedBus.getCapacity()));
                        cmbStatus.setSelectedItem(selectedBus.getStatus());
                    }
                }
            }
        });
    }

    private void loadBuses() {
        // Clear table
        busTableModel.setRowCount(0);

        // Get all buses
        List<Bus> buses = busManager.getAllBuses();

        // Populate table
        for (Bus bus : buses) {
            Object[] row = {
                    bus.getId(),
                    bus.getBusNumber(),
                    bus.getBusType(),
                    bus.getCapacity(),
                    bus.getStatus()
            };

            busTableModel.addRow(row);
        }
    }

    private void addBus() {
        // Validate inputs
        if (txtBusNumber.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter bus number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(txtCapacity.getText().trim());
            if (capacity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid capacity", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create bus
        Bus bus = new Bus();
        bus.setBusNumber(txtBusNumber.getText().trim());
        bus.setBusType((String) cmbBusType.getSelectedItem());
        bus.setCapacity(capacity);
        bus.setStatus((String) cmbStatus.getSelectedItem());

        if (busManager.addBus(bus)) {
            JOptionPane.showMessageDialog(this, "Bus added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuses();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add bus", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBus() {
        if (selectedBus == null) {
            JOptionPane.showMessageDialog(this, "Please select a bus to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate inputs
        if (txtBusNumber.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter bus number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(txtCapacity.getText().trim());
            if (capacity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid capacity", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update bus
        selectedBus.setBusNumber(txtBusNumber.getText().trim());
        selectedBus.setBusType((String) cmbBusType.getSelectedItem());
        selectedBus.setCapacity(capacity);
        selectedBus.setStatus((String) cmbStatus.getSelectedItem());

        if (busManager.updateBus(selectedBus)) {
            JOptionPane.showMessageDialog(this, "Bus updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuses();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update bus", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBus() {
        if (selectedBus == null) {
            JOptionPane.showMessageDialog(this, "Please select a bus to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this bus?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (busManager.deleteBus(selectedBus.getId())) {
                JOptionPane.showMessageDialog(this, "Bus deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadBuses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bus", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtBusNumber.setText("");
        cmbBusType.setSelectedIndex(0);
        txtCapacity.setText("");
        cmbStatus.setSelectedIndex(0);
        selectedBus = null;
    }
}