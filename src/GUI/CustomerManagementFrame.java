package GUI;

import BLL.CustomerManager;
import DTO.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerManagementFrame extends JFrame {
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable tblCustomers;
    private DefaultTableModel customerTableModel;

    private CustomerManager customerManager;
    private Customer selectedCustomer;

    public CustomerManagementFrame() {
        customerManager = new CustomerManager();

        initComponents();
        setupLayout();
        setupListeners();
        loadCustomers();

        setTitle("Customer Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        txtName = new JTextField(20);
        txtPhone = new JTextField(20);
        txtEmail = new JTextField(20);
        txtAddress = new JTextField(20);
        txtSearch = new JTextField(20);

        btnSearch = new JButton("Search");
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        // Setup table
        String[] columns = {"ID", "Name", "Phone", "Email", "Address"};
        customerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCustomers = new JTable(customerTableModel);
        tblCustomers.getTableHeader().setReorderingAllowed(false);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtName, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtPhone, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtEmail, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtAddress, gbc);

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
        tablePanel.setBorder(BorderFactory.createTitledBorder("Customer List"));
        tablePanel.add(new JScrollPane(tblCustomers), BorderLayout.CENTER);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupListeners() {
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomers();
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomer();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        tblCustomers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblCustomers.getSelectedRow();
                if (selectedRow >= 0) {
                    int customerId = Integer.parseInt(tblCustomers.getValueAt(selectedRow, 0).toString());
                    selectedCustomer = customerManager.getCustomerById(customerId);
                    if (selectedCustomer != null) {
                        txtName.setText(selectedCustomer.getName());
                        txtPhone.setText(selectedCustomer.getPhone());
                        txtEmail.setText(selectedCustomer.getEmail());
                        txtAddress.setText(selectedCustomer.getAddress());
                    }
                }
            }
        });
    }

    private void loadCustomers() {
        // Clear table
        customerTableModel.setRowCount(0);

        // Get all customers
        List<Customer> customers = customerManager.getAllCustomers();

        // Populate table
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress()
            };

            customerTableModel.addRow(row);
        }
    }

    private void searchCustomers() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadCustomers();
            return;
        }

        // Clear table
        customerTableModel.setRowCount(0);

        // Search customers
        List<Customer> customers = customerManager.searchCustomers(keyword);

        // Populate table
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress()
            };

            customerTableModel.addRow(row);
        }
    }

    private void addCustomer() {
        // Validate inputs
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create customer
        Customer customer = new Customer();
        customer.setName(txtName.getText().trim());
        customer.setPhone(txtPhone.getText().trim());
        customer.setEmail(txtEmail.getText().trim());
        customer.setAddress(txtAddress.getText().trim());

        if (customerManager.addCustomer(customer)) {
            JOptionPane.showMessageDialog(this, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadCustomers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add customer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate inputs
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update customer
        selectedCustomer.setName(txtName.getText().trim());
        selectedCustomer.setPhone(txtPhone.getText().trim());
        selectedCustomer.setEmail(txtEmail.getText().trim());
        selectedCustomer.setAddress(txtAddress.getText().trim());

        if (customerManager.updateCustomer(selectedCustomer)) {
            JOptionPane.showMessageDialog(this, "Customer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadCustomers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update customer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this customer?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (customerManager.deleteCustomer(selectedCustomer.getId())) {
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadCustomers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete customer", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        selectedCustomer = null;
    }
}