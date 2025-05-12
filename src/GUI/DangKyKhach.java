package GUI;

import BLL.CustomerManager;
import BLL.UserManager;
import DTO.Customer;
import DTO.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;


public class DangKyKhach extends JFrame {

    public DangKyKhach() {
        setTitle("Đăng Ký Khách Hàng");
        setSize(430, 705);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel bg = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/ImageBackground/backgroundDky.png"))));
        bg.setBounds(0, 0, 422, 690);
        add(bg);
        bg.setLayout(null);

        int leftX1 = 20;
        int leftX2 = 225;
        int fieldWidth = 180;
        int labelHeight = 25;
        int fieldHeight = 35;
        int spacingY = 90;
        int errorSpacing = 15;
        int baseY = 100;

        JLabel titleLabel = new JLabel("Đăng Ký Khách Hàng", JLabel.CENTER);
        titleLabel.setBounds(120, 30, 300, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        bg.add(titleLabel);

// ==== SĐT ====
        JLabel sdtLabel = new JLabel("SĐT:");
        sdtLabel.setBounds(leftX1, baseY+30, 100, labelHeight);
        sdtLabel.setForeground(Color.WHITE);
        bg.add(sdtLabel);

        JTextField sdtField = new JTextField();
        sdtField.setBounds(leftX1, baseY + labelHeight+30, fieldWidth, fieldHeight);
        bg.add(sdtField);

        JLabel sdtError = new JLabel("SĐT không hợp lệ");
        sdtError.setBounds(leftX1, baseY +30+ labelHeight + fieldHeight + 2, fieldWidth, errorSpacing);
        sdtError.setForeground(Color.PINK);
        sdtError.setFont(new Font("Arial", Font.ITALIC, 10));
        sdtError.setVisible(false);
        bg.add(sdtError);

// ==== Họ tên ====
        int nameY = baseY + spacingY + fieldHeight+30;
        JLabel nameLabel = new JLabel("Họ Tên:");
        nameLabel.setBounds(leftX1, nameY, 100, labelHeight);
        nameLabel.setForeground(Color.WHITE);
        bg.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(leftX1, nameY + labelHeight, fieldWidth, fieldHeight);
        bg.add(nameField);

        JLabel nameError = new JLabel("Họ tên không hợp lệ");
        nameError.setBounds(leftX1, nameY + labelHeight + fieldHeight + 2, fieldWidth, errorSpacing);
        nameError.setForeground(Color.PINK);
        nameError.setFont(new Font("Arial", Font.ITALIC, 10));
        nameError.setVisible(false);
        bg.add(nameError);

// ==== Email ====
        int emailY = nameY + spacingY + fieldHeight;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(leftX1, emailY, 100, labelHeight);
        emailLabel.setForeground(Color.WHITE);
        bg.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(leftX1, emailY + labelHeight, fieldWidth, fieldHeight);
        bg.add(emailField);

        JLabel emailError = new JLabel("Email không hợp lệ");
        emailError.setBounds(leftX1, emailY + labelHeight + fieldHeight + 2, fieldWidth, errorSpacing);
        emailError.setForeground(Color.PINK);
        emailError.setFont(new Font("Arial", Font.ITALIC, 10));
        emailError.setVisible(false);
        bg.add(emailError);

// ==== Tài khoản ====
        JLabel tkLabel = new JLabel("Tài Khoản:");
        tkLabel.setBounds(leftX2, baseY, 100, labelHeight);
        tkLabel.setForeground(Color.WHITE);
        bg.add(tkLabel);

        JTextField tkField = new JTextField();
        tkField.setBounds(leftX2, baseY + labelHeight, fieldWidth, fieldHeight);
        bg.add(tkField);

        JLabel tkError = new JLabel("Tài khoản không hợp lệ");
        tkError.setBounds(leftX2, baseY + labelHeight + fieldHeight + 2, fieldWidth, errorSpacing);
        tkError.setForeground(Color.PINK);
        tkError.setFont(new Font("Arial", Font.ITALIC, 10));
        tkError.setVisible(false);
        bg.add(tkError);

// ==== Mật khẩu ====
        int mkY = baseY + spacingY + fieldHeight;
        JLabel mkLabel = new JLabel("Mật khẩu:");
        mkLabel.setBounds(leftX2, mkY, 100, labelHeight);
        mkLabel.setForeground(Color.WHITE);
        bg.add(mkLabel);

        JPasswordField mkField = new JPasswordField();
        mkField.setBounds(leftX2, mkY + labelHeight, fieldWidth, fieldHeight);
        bg.add(mkField);

        JLabel mkHint = new JLabel("Từ 6 đến 50 ký tự");
        mkHint.setBounds(leftX2, mkY + labelHeight + fieldHeight + 2, fieldWidth, errorSpacing);
        mkHint.setForeground(Color.WHITE);
        mkHint.setFont(new Font("Arial", Font.ITALIC, 10));
        bg.add(mkHint);

        JLabel mkError = new JLabel("Mật khẩu không hợp lệ");
        mkError.setBounds(leftX2, mkY + labelHeight + fieldHeight + errorSpacing + 2, fieldWidth, errorSpacing);
        mkError.setForeground(Color.PINK);
        mkError.setFont(new Font("Arial", Font.ITALIC, 10));
        mkError.setVisible(false);
        bg.add(mkError);

// ==== Nhập lại mật khẩu ====
        int mk2Y = mkY + spacingY + fieldHeight;
        JLabel mk2Label = new JLabel("Nhập lại mật khẩu:");
        mk2Label.setBounds(leftX2, mk2Y, 150, labelHeight);
        mk2Label.setForeground(Color.WHITE);
        bg.add(mk2Label);

        JPasswordField mk2Field = new JPasswordField();
        mk2Field.setBounds(leftX2, mk2Y + labelHeight, fieldWidth, fieldHeight);
        bg.add(mk2Field);

        JLabel mk2Error = new JLabel("Mật khẩu nhập lại không khớp");
        mk2Error.setBounds(leftX2, mk2Y + labelHeight + fieldHeight + 2, fieldWidth + 20, errorSpacing);
        mk2Error.setForeground(Color.PINK);
        mk2Error.setFont(new Font("Arial", Font.ITALIC, 10));
        mk2Error.setVisible(false);
        bg.add(mk2Error);

        mk2Field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    thucHienDangKy(sdtField, nameField, emailField, tkField, mkField, mk2Field,
                            sdtError, nameError, emailError, tkError, mkError, mk2Error);
                }
            }
        });

// ==== Line phân cách ====
        JPanel line = new JPanel();
        line.setBounds(211, baseY - 10, 1, 350);
        line.setBackground(new Color(160, 100, 255));
        bg.add(line);

// ==== Nút Đăng Ký ====
        JButton btnDangKy = new JButton("Đăng Ký");
        btnDangKy.setBounds(125, baseY + 420, 160, 40);
        btnDangKy.setBackground(new Color(255, 233, 84));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFont(new Font("Arial", Font.BOLD, 16));
        bg.add(btnDangKy);

        btnDangKy.addActionListener(e -> thucHienDangKy(sdtField, nameField, emailField, tkField, mkField, mk2Field,
                sdtError, nameError, emailError, tkError, mkError, mk2Error));


// ==== Link đăng nhập ====
        JLabel loginLabel = new JLabel("<html><u>Đăng nhập</u></html>");
        loginLabel.setBounds(150, baseY + 470, 120, 30);
        loginLabel.setForeground(new Color(0, 255, 255));
        loginLabel.setFont(new Font("Arial",Font.BOLD,16));
        loginLabel.setHorizontalAlignment(JLabel.CENTER);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bg.add(loginLabel);
        loginLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dispose();
                new Login().setVisible(true);
            }

            public void mouseEntered(MouseEvent evt) {
                loginLabel.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent evt) {
                loginLabel.setForeground(Color.CYAN);
            }
        });

        setVisible(true);

    }

    private void thucHienDangKy(JTextField sdt, JTextField ten, JTextField email,
                                JTextField tk, JPasswordField mk1, JPasswordField mk2,
                                JLabel sdtError, JLabel tenError, JLabel emailError,
                                JLabel tkError, JLabel mkError, JLabel mk2Error) {
        boolean hopLe = true;

        sdtError.setVisible(false);
        tenError.setVisible(false);
        emailError.setVisible(false);
        tkError.setVisible(false);
        mkError.setVisible(false);
        mk2Error.setVisible(false);

        String soDienThoai = sdt.getText().trim();
        String hoTen = ten.getText().trim();
        String emailStr = email.getText().trim();
        String taiKhoan = tk.getText().trim();
        String matKhau = new String(mk1.getPassword());
        String nhapLai = new String(mk2.getPassword());

        if (!soDienThoai.matches("^\\d{9,11}$")) {
            sdtError.setVisible(true);
            hopLe = false;
        }

        if (!emailStr.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            emailError.setVisible(true);
            hopLe = false;
        }

        if (hoTen.isEmpty()) {
            tenError.setVisible(true);
            hopLe = false;
        }

        if (taiKhoan.isEmpty()) {
            tkError.setVisible(true);
            hopLe = false;
        }

        if (matKhau.length() < 6 || matKhau.length() > 50) {
            mkError.setVisible(true);
            hopLe = false;
        }

        if (!matKhau.equals(nhapLai)) {
            mk2Error.setVisible(true);
            hopLe = false;
        }

        if (hopLe) {
            try {
                // Kiểm tra tài khoản đã tồn tại chưa
                UserManager userManager = new UserManager();
                if (userManager.isUsernameExists(taiKhoan)) {
                    JOptionPane.showMessageDialog(null, "Tài khoản đã tồn tại!");
                } else {
                    // Thêm vào bảng users
                    User users = new User(taiKhoan,matKhau,hoTen,"Staff",true);
                    userManager.addUser(users);

                    // Thêm vào bảng customers
                    CustomerManager cus=new CustomerManager();
                    Customer newcustomer=new Customer(hoTen,soDienThoai,emailStr,"");
                    cus.addCustomer(newcustomer);

                    JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                   dispose();
                   new Login().setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

    }



    public static void main(String[] args) {
        new DangKyKhach();
    }
}
