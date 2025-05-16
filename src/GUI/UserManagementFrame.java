package GUI;

import BLL.UserManager;
import DTO.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserManagementFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;
    private JComboBox<String> cmbRole;
    private JCheckBox chkActive;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable tblUsers;
    private DefaultTableModel userTableModel;

    private UserManager userManager;
    private User selectedUser;
    private boolean isNewPassword;

    public UserManagementFrame() {
        userManager = new UserManager();

        initComponents();
        setupLayout();
        setupListeners();
        loadUsers();

        setTitle("User Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtFullName = new JTextField(20);

        cmbRole = new JComboBox<>();
        cmbRole.addItem("ADMIN");
        cmbRole.addItem("USER");

        chkActive = new JCheckBox("Active");
        chkActive.setSelected(true);

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        // Setup table
        String[] columns = { "ID", "Username", "Full Name", "Role", "Active" };
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblUsers = new JTable(userTableModel);
        tblUsers.getTableHeader().setReorderingAllowed(false);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtPassword, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtFullName, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(cmbRole, gbc);

        // Active
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(chkActive, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("User List"));
        tablePanel.add(new JScrollPane(tblUsers), BorderLayout.CENTER);

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupListeners() {
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        tblUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblUsers.getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = Integer.parseInt(tblUsers.getValueAt(selectedRow, 0).toString());
                    selectedUser = userManager.getUserById(userId);
                    if (selectedUser != null) {
                        txtUsername.setText(selectedUser.getUsername());
                        txtPassword.setText(""); // Don't show password
                        isNewPassword = false;
                        txtFullName.setText(selectedUser.getFullName());
                        cmbRole.setSelectedItem(selectedUser.getRole());
                        chkActive.setSelected(selectedUser.isActive());
                    }
                }
            }
        });

        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                isNewPassword = true;
            }
        });
    }

    private void loadUsers() {
        // Clear table
        userTableModel.setRowCount(0);

        // Get all users
        List<User> users = userManager.getAllUsers();

        // Populate table
        for (User user : users) {
            Object[] row = {
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getRole(),
                    user.isActive() ? "Yes" : "No"
            };

            userTableModel.addRow(row);
        }
    }

    private void addUser() {
        // Validate inputs
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Please enter password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter full name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create user
        User user = new User();
        user.setUsername(txtUsername.getText().trim());
        user.setPassword(new String(txtPassword.getPassword()));
        user.setFullName(txtFullName.getText().trim());
        user.setRole((String) cmbRole.getSelectedItem());
        user.setActive(chkActive.isSelected());

        if (userManager.addUser(user)) {
            JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select a user to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate inputs
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter full name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update user
        selectedUser.setUsername(txtUsername.getText().trim());
        if (isNewPassword && txtPassword.getPassword().length > 0) {
            selectedUser.setPassword(new String(txtPassword.getPassword()));
        }
        selectedUser.setFullName(txtFullName.getText().trim());
        selectedUser.setRole((String) cmbRole.getSelectedItem());
        selectedUser.setActive(chkActive.isSelected());

        if (userManager.updateUser(selectedUser)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (userManager.deleteUser(selectedUser.getId())) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        cmbRole.setSelectedIndex(0);
        chkActive.setSelected(true);
        selectedUser = null;
        isNewPassword = true;
    }
}