package client;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class RegisterForm extends JFrame {

    JTextField user, fullname, email, phone, birth;
    JPasswordField pass;
    JComboBox<String> gender;

    JLabel userError, passError, emailError, phoneError, birthError;

    public RegisterForm() {

        setTitle("Register");
        setSize(450, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("APP CHAT CÙNG BẠN", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        inputPanel.setBackground(new Color(240, 255, 240));

        // USERNAME
        inputPanel.add(new JLabel("Tên tài khoản:", SwingConstants.RIGHT));
        user = new JTextField();
        inputPanel.add(user);

        inputPanel.add(new JLabel(""));
        userError = new JLabel(" ");
        userError.setForeground(Color.RED);
        inputPanel.add(userError);

        // PASSWORD
        inputPanel.add(new JLabel("Mật khẩu:", SwingConstants.RIGHT));
        pass = new JPasswordField();
        inputPanel.add(pass);

        inputPanel.add(new JLabel(""));
        passError = new JLabel(" ");
        passError.setForeground(Color.RED);
        inputPanel.add(passError);

        // FULLNAME
        inputPanel.add(new JLabel("Họ và tên:", SwingConstants.RIGHT));
        fullname = new JTextField();
        inputPanel.add(fullname);

        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));

        // EMAIL
        inputPanel.add(new JLabel("Email:", SwingConstants.RIGHT));
        email = new JTextField();
        inputPanel.add(email);

        inputPanel.add(new JLabel(""));
        emailError = new JLabel(" ");
        emailError.setForeground(Color.RED);
        inputPanel.add(emailError);

        // PHONE
        inputPanel.add(new JLabel("Số điện thoại:", SwingConstants.RIGHT));
        phone = new JTextField();
        inputPanel.add(phone);

        inputPanel.add(new JLabel(""));
        phoneError = new JLabel(" ");
        phoneError.setForeground(Color.RED);
        inputPanel.add(phoneError);

        // GENDER
        inputPanel.add(new JLabel("Giới tính:", SwingConstants.RIGHT));
        gender = new JComboBox<>(new String[]{"Nam", "Nữ"});
        inputPanel.add(gender);

        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));

        // BIRTH
        inputPanel.add(new JLabel("Ngày sinh (dd-mm-yyyy):", SwingConstants.RIGHT));
        birth = new JTextField();
        inputPanel.add(birth);

        inputPanel.add(new JLabel(""));
        birthError = new JLabel(" ");
        birthError.setForeground(Color.RED);
        inputPanel.add(birthError);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JButton btn = new JButton("Đăng ký");
        btn.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(btn);

        add(buttonPanel, BorderLayout.SOUTH);

        btn.addActionListener(e -> {

            try {

                userError.setText(" ");
                passError.setText(" ");
                emailError.setText(" ");
                phoneError.setText(" ");
                birthError.setText(" ");

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

                if (u.contains(" ")) {
                    userError.setText("Tên tài khoản không được chứa khoảng trắng");
                    return;
                }

                if (!u.matches("^[a-zA-ZÀ-ỹ0-9]{4,20}$")) {
                    userError.setText("Username phải 4-20 ký tự");
                    return;
                }

                if (!p.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{6,}$")) {
                    passError.setText("Password ≥6 ký tự, có chữ, số, ký tự đặc biệt");
                    return;
                }

                if (!em.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    emailError.setText("Email phải có dạng abc@gmail.com");
                    return;
                }

                if (!ph.matches("^0\\d{9}$")) {
                    phoneError.setText("SĐT phải 10 số và bắt đầu bằng 0");
                    return;
                }

                if (!b.matches("\\d{2}-\\d{2}-\\d{4}")) {
                    birthError.setText("Ngày sinh phải dạng dd-mm-yyyy");
                    return;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate birthDate = LocalDate.parse(b, formatter);
                int age = Period.between(birthDate, LocalDate.now()).getYears();

                if (age < 10 || age > 100) {
                    birthError.setText("Tuổi phải từ 10 - 100");
                    return;
                }

                DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String birthForDB = birthDate.format(output);

                Client.dos.writeUTF(
                        "REGISTER|" + u + "|" + p + "|" + fn + "|" + em + "|" + ph + "|" + g + "|" + birthForDB
                );

                String res = Client.dis.readUTF();

                if (res.equals("REGISTER_SUCCESS")) {

                    JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                    dispose();

                } else if (res.equals("USER_EXIST")) {

                    userError.setText("Tên tài khoản đã tồn tại");

                } else if (res.equals("EMAIL_EXIST")) {

                    emailError.setText("Email đã được sử dụng");

                } else if (res.equals("PHONE_EXIST")) {

                    phoneError.setText("SĐT đã được sử dụng");

                } else {

                    JOptionPane.showMessageDialog(this, "Đăng ký thất bại");

                }

            } catch (Exception ex) {

                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi kết nối server");

            }

        });

        setVisible(true);
    }
}
