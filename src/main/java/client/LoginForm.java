package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginForm extends JFrame {

    JTextField user;
    JPasswordField pass;

    public LoginForm(){

        setTitle("Chat Login");
        setSize(380,280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel title = new JLabel("APP CHAT CÙNG BẠN",SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,22));
        title.setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
        add(title,BorderLayout.NORTH);

        // ===== PANEL GIỮA =====
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(2,2,10,10));
        center.setBorder(BorderFactory.createEmptyBorder(20,40,10,40));

        center.add(new JLabel("Tên tài khoản"));
        user = new JTextField();
        center.add(user);

        center.add(new JLabel("Mật khẩu"));
        pass = new JPasswordField();
        center.add(pass);

        add(center,BorderLayout.CENTER);

        // ===== NÚT LOGIN Ở GIỮA =====
        JButton login = new JButton("ĐĂNG NHẬP");
        login.setFont(new Font("Arial",Font.BOLD,14));
        login.setBackground(new Color(70,130,180));
        login.setForeground(Color.WHITE);

        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.add(login);

        // ===== REGISTER LINK =====
        JLabel text = new JLabel("Nếu bạn chưa có tài khoản hãy ");
        JLabel register = new JLabel("Đăng ký tại đây");

        register.setForeground(Color.BLUE);
        register.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel bottom = new JPanel();
        bottom.setLayout(new GridLayout(2,1));

        bottom.add(loginPanel);

        JPanel registerPanel = new JPanel();
        registerPanel.add(text);
        registerPanel.add(register);

        bottom.add(registerPanel);

        add(bottom,BorderLayout.SOUTH);

        // ===== LOGIN ACTION =====
        login.addActionListener(e->{

            try{

                String u = user.getText();
                String p = new String(pass.getPassword());

                Client.dos.writeUTF("LOGIN|" + u + "|" + p);

                String res = Client.dis.readUTF();

                if(res.equals("LOGIN_SUCCESS")){

                    JOptionPane.showMessageDialog(this,"Đăng nhập thành công");

                    new ChatUI();

                    dispose();

                }else{

                    JOptionPane.showMessageDialog(this,"Sai tài khoản");

                }

            }catch(Exception ex){
                ex.printStackTrace();
            }

        });

        // ===== CLICK REGISTER =====
        register.addMouseListener(new MouseAdapter(){

            public void mouseClicked(MouseEvent e){

                new RegisterForm();

            }

        });

        setVisible(true);
    }

}