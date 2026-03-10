package client;

import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JFrame {

    JTextField user;
    JPasswordField pass;

    public RegisterForm() {
        setTitle("Register");
        setSize(350, 280); // Tăng nhẹ chiều cao để cân đối hơn
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); // Sử dụng BorderLayout làm chủ đạo

        // ===== 1. TIÊU ĐỀ (NORTH) =====
        JLabel title = new JLabel("APP CHAT CÙNG BẠN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ===== 2. PANEL NHẬP LIỆU (CENTER) =====
        // Đổi thành 2 hàng 2 cột thay vì 3 hàng
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        inputPanel.setBackground(new Color(240, 255, 240));

        inputPanel.add(new JLabel("Tên tài khoản:"));
        user = new JTextField();
        inputPanel.add(user);

        inputPanel.add(new JLabel("Mật khẩu:"));
        pass = new JPasswordField();
        inputPanel.add(pass);

        add(inputPanel, BorderLayout.CENTER);

        // ===== 3. PANEL NÚT BẤM (SOUTH) =====
        // FlowLayout mặc định sẽ căn giữa các thành phần bên trong nó
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 255, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton btn = new JButton("Đăng ký");
        btn.setPreferredSize(new Dimension(120, 35)); // Chỉnh kích thước nút cho đẹp
        buttonPanel.add(btn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== XỬ LÝ SỰ KIỆN =====
        btn.addActionListener(e -> {
            try {
                String u = user.getText();
                String p = new String(pass.getPassword());

                if (u.isEmpty() || p.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                // Giả sử Client.dos và Client.dis đã được khởi tạo
                Client.dos.writeUTF("REGISTER|" + u + "|" + p);
                String res = Client.dis.readUTF();

                if (res.equals("REGISTER_SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Đăng ký thành công");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "User đã tồn tại");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi kết nối server!");
            }
        });

        setVisible(true);
    }
}