package client;

import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JFrame {

    JTextField user, fullname, email, phone, birth;
    JPasswordField pass;
    JComboBox<String> gender;

    public RegisterForm() {

        setTitle("Register");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel title = new JLabel("APP CHAT CÙNG BẠN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ===== INPUT PANEL =====
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        inputPanel.setBackground(new Color(240, 255, 240));

        inputPanel.add(new JLabel("Tên tài khoản:"));
        user = new JTextField();
        inputPanel.add(user);

        inputPanel.add(new JLabel("Mật khẩu:"));
        pass = new JPasswordField();
        inputPanel.add(pass);

        inputPanel.add(new JLabel("Họ và tên:"));
        fullname = new JTextField();
        inputPanel.add(fullname);

        inputPanel.add(new JLabel("Email:"));
        email = new JTextField();
        inputPanel.add(email);

        inputPanel.add(new JLabel("Số điện thoại:"));
        phone = new JTextField();
        inputPanel.add(phone);

        inputPanel.add(new JLabel("Giới tính:"));
        gender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        inputPanel.add(gender);

        inputPanel.add(new JLabel("Ngày sinh (yyyy-mm-dd):"));
        birth = new JTextField();
        inputPanel.add(birth);

        add(inputPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 255, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btn = new JButton("Đăng ký");
        btn.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(btn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== EVENT =====
        btn.addActionListener(e -> {

            try {

                String u = user.getText().trim();
                String p = new String(pass.getPassword()).trim();
                String fn = fullname.getText().trim();
                String em = email.getText().trim();
                String ph = phone.getText().trim();
                String g = gender.getSelectedItem().toString();
                String b = birth.getText().trim();

                if (u.isEmpty() || p.isEmpty() || fn.isEmpty() || em.isEmpty() || ph.isEmpty() || b.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                // gửi dữ liệu lên server
                Client.dos.writeUTF(
                        "REGISTER|" + u + "|" + p + "|" + fn + "|" + em + "|" + ph + "|" + g + "|" + b
                );

                String res = Client.dis.readUTF();

                if (res.equals("REGISTER_SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi kết nối server!");
            }

        });

        setVisible(true);
    }
}
