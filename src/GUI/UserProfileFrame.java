package GUI;

import BLL.CustomerManager;
import BLL.UserManager;
import DAL.CustomerDAO;
import DAL.UserDAO;
import DTO.Customer;
import DTO.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class UserProfileFrame extends JFrame {
    private JTextField txtUsername, txtName, txtPhone,txtEmail,txtaddress;
    private JPasswordField txtPassword, txtChangePassword, txtValidatePassword;
    private JButton btnSave, btnCancel;

    public UserProfileFrame(User user) {

        setTitle("Thông tin khách hàng");
        setSize(800, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents(user);
        setupLayout();
        setVisible(true);

    }

    private void initComponents(User user) {
        CustomerDAO customerDAO = new CustomerDAO();
        Customer currentcustomer = customerDAO.getCustomerByName(user.getFullName());
        txtUsername = new JTextField( user.getUsername());
        txtName = new JTextField(currentcustomer.getName());
        txtPhone = new JTextField(currentcustomer.getPhone());
        txtEmail = new JTextField(currentcustomer.getEmail());
        txtaddress = new JTextField(currentcustomer.getAddress());
        txtUsername.setEditable(false);
        txtName.setEditable(false);

        txtPassword = new JPasswordField(20);
        txtPassword.setEchoChar('*');
        txtChangePassword = new JPasswordField(20);
        txtChangePassword.setEchoChar('*');
        txtValidatePassword = new JPasswordField(20);
        txtValidatePassword.setEchoChar('*');


        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        setupListeners(user, currentcustomer);
    }

    private void setupLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin người dùng"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);


        // Số điện thoại
        gbc.gridx = 3;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 4;
        formPanel.add(txtPhone, gbc);


        // Họ và tên
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);


        gbc.gridx = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 4;
        formPanel.add(txtEmail, gbc);

        // Địa chỉ
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtaddress, gbc);


        // Mật khẩu hiện tại
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Mật khẩu hiện tại:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        // Mật khẩu mới
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Mật khẩu mới:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtChangePassword, gbc);

        // Xác nhận mật khẩu
        gbc.gridx = 3;
        formPanel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        gbc.gridx = 4;
        formPanel.add(txtValidatePassword, gbc);


        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    private void setupListeners(User user, Customer currentCustomer) {
        btnSave.addActionListener(e -> {
            String enteredPassword = new String(txtPassword.getPassword());
            String newPassword = new String(txtChangePassword.getPassword());
            String confirmPassword = new String(txtValidatePassword.getPassword());
            String newPhone = txtPhone.getText().trim();
            String newEmail = txtEmail.getText().trim();
            String newAddress = txtaddress.getText().trim();

            if (newPhone.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ số điện thoại và email.");
                return;
            }
            if (!newEmail.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ.");
                return;
            }

// Kiểm tra số điện thoại là số và độ dài
            if (!newPhone.matches("^\\d{9,11}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải có từ 9 đến 11 chữ số.");
                return;
            }
            // Giả sử mật khẩu đang lưu trong User là plaintext (nếu mã hóa thì phải xử lý khác)
            if (!enteredPassword.equals(user.getPassword())) {
                JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng!");
                return;
            }

            if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp!");
                return;
            }

            // Update DB: CustomerDAO bạn cần có phương thức updateCustomerByPhone
            // Cập nhật thông tin khách hàng
            currentCustomer.setAddress(newAddress);
            currentCustomer.setPhone(newPhone);
            currentCustomer.setEmail(newEmail);
            CustomerManager customerManager = new CustomerManager();
           boolean success=customerManager.updateCustomer(currentCustomer);
            if (success) {
                if (!newPassword.isEmpty()) {
                    UserManager dto = new UserManager();
                    user.setPassword(newPassword);
                    dto.updateUser(user);
                }
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }


}
