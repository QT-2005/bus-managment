package GUI;

import BLL.UserManager;
import DTO.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lbErrorMessage;
    private UserManager userManager;

    public Login() {
        setTitle("Bus Ticket Management System - Login");
        setSize(430, 705);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        userManager = new UserManager();

        // Ảnh nền
        JLabel bg = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/ImageBackground/LoginEdit.png"))));
        bg.setBounds(0, 0, 422, 690);
        add(bg);
        bg.setLayout(null);

        // Tiêu đề
        JLabel title = new JLabel("Đăng Nhập Tài Khoản", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBounds(40, 60, 340, 40);
        bg.add(title);

        // Slogan
        JLabel slogan = new JLabel("Đồng hành cùng bạn trên mọi hành trình", SwingConstants.CENTER);
        slogan.setForeground(Color.WHITE);
        slogan.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        slogan.setBounds(40, 230, 340, 20);
        bg.add(slogan);

        // Tên đăng nhập
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(95, 290, 270, 40);
        bg.add(txtUsername);

        // Mật khẩu
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(95, 350, 270, 40);
        bg.add(txtPassword);

        // Nút đăng nhập
        JButton btnLogin = new JButton("Đăng Nhập");
        btnLogin.setBackground(new Color(242, 227, 57));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(40, 415, 343, 40);
        bg.add(btnLogin);

        // Lỗi đăng nhập
        lbErrorMessage = new JLabel("", SwingConstants.CENTER);
        lbErrorMessage.setForeground(Color.PINK);
        lbErrorMessage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbErrorMessage.setBounds(40, 470, 340, 30);
        lbErrorMessage.setVisible(false);
        bg.add(lbErrorMessage);

        // Logo
        JLabel logo = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/ImageBackground/Logo-Car2.png"))));
        logo.setBounds(125, 510, 170, 100);
        bg.add(logo);

        // Đăng ký
        JLabel lblRegister = new JLabel("Chưa có tài khoản? Đăng ký", SwingConstants.CENTER);
        lblRegister.setForeground(Color.CYAN);
        lblRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRegister.setBounds(90, 615, 240, 25);
        bg.add(lblRegister);

        // Sự kiện
        btnLogin.addActionListener(e -> login());

        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        lblRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dispose();
                new DangKyKhach().setVisible(true);
            }

            public void mouseEntered(MouseEvent evt) {
                lblRegister.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent evt) {
                lblRegister.setForeground(Color.CYAN);
            }
        });
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lbErrorMessage.setText("Vui lòng nhập tài khoản và mật khẩu");
            lbErrorMessage.setVisible(true);
            return;
        }

        User user = userManager.authenticate(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            new MainDashboard(user).setVisible(true);
            dispose();
        } else {
            lbErrorMessage.setText("Tài khoản hoặc mật khẩu không đúng");
            lbErrorMessage.setVisible(true);
            txtPassword.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
