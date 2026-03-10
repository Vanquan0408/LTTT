package client;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChatUI extends JFrame {

    JTextArea chatArea;
    JTextField messageField;
    DefaultListModel<String> userModel;
    JList<String> userList;
    JLabel chatLabel;

    // Biến này để biết mình đang ở "phòng" nào
    String currentChatTarget = "Tất cả"; 
    
    // Map lưu lịch sử: Mỗi người một "thế giới" riêng
    // Key: "Tất cả" hoặc "Tên_User_A", "Tên_User_B"
    private Map<String, StringBuilder> chatLogs = new HashMap<>();

    public ChatUI() {
        setTitle("Zalo Clone - Chat Riêng Biệt");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Khởi tạo ngăn lưu trữ mặc định cho Chat Tổng
        chatLogs.put("Tất cả", new StringBuilder("--- Đây là phòng Chat Chung ---\n"));

        // --- GIAO DIỆN CHÍNH ---
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // --- DANH SÁCH BÊN PHẢI ---
        userModel = new DefaultListModel<>();
        userModel.addElement("Tất cả");
        userList = new JList<>(userModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setSelectedIndex(0);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, 0));
        sidePanel.setBorder(BorderFactory.createTitledBorder("Danh sách chat"));
        sidePanel.add(new JScrollPane(userList), BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        // --- THANH NHẬP LIỆU ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        chatLabel = new JLabel(" Đang chat với: Tất cả");
        chatLabel.setOpaque(true);
        chatLabel.setBackground(new Color(220, 230, 255));
        
        messageField = new JTextField();
        JButton sendBtn = new JButton("Gửi");

        JPanel inputArea = new JPanel(new BorderLayout());
        inputArea.add(messageField, BorderLayout.CENTER);
        inputArea.add(sendBtn, BorderLayout.EAST);

        bottomPanel.add(chatLabel, BorderLayout.NORTH);
        bottomPanel.add(inputArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- SỰ KIỆN CHUYỂN ĐỔI NGƯỜI CHAT (QUAN TRỌNG) ---
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = userList.getSelectedValue();
                if (selected != null && !selected.equals(currentChatTarget)) {
                    // 1. Cập nhật mục tiêu chat mới
                    currentChatTarget = selected;
                    chatLabel.setText(" Đang chat với: " + currentChatTarget);
                    
                    // 2. Xóa màn hình cũ, nạp lịch sử của đúng người này
                    refreshChatScreen();
                }
            }
        });

        // --- GỬI TIN ---
        sendBtn.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        // --- LUỒNG NHẬN TIN TỪ SERVER ---
        new Thread(this::listenToServer).start();

        refreshChatScreen();
        setVisible(true);
    }

    private void refreshChatScreen() {
        // Chỉ lấy nội dung của đúng người (Key) đang được chọn
        StringBuilder log = chatLogs.getOrDefault(currentChatTarget, new StringBuilder());
        chatArea.setText(log.toString());
    }

    private void listenToServer() {
        try {
            while (true) {
                String msg = Client.dis.readUTF();

                if (msg.startsWith("USERS|")) {
                    updateUserList(msg.substring(6));
                } 
                else if (msg.startsWith("PRIVATE_FROM|")) {
                    // Cấu trúc: PRIVATE_FROM|TênNgườiGửi|NộiDung
                    String[] parts = msg.split("\\|", 3);
                    String sender = parts[1];
                    String content = parts[2];
                    
                    // LƯU VÀO NGĂN KÉO CỦA NGƯỜI GỬI
                    appendToLog(sender, sender + ": " + content);
                } 
                else {
                    // Tin nhắn chung từ phòng Chat Tổng
                    appendToLog("Tất cả", msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm này quyết định tin nhắn sẽ được cất vào đâu
    private void appendToLog(String targetKey, String message) {
        // 1. Tìm hoặc tạo mới lịch sử cho người này
        chatLogs.putIfAbsent(targetKey, new StringBuilder("--- Chat với " + targetKey + " ---\n"));
        chatLogs.get(targetKey).append(message).append("\n");

        // 2. NẾU mình đang mở đúng cửa sổ của người đó, thì mới hiện lên màn hình
        if (currentChatTarget.equals(targetKey)) {
            chatArea.append(message + "\n");
            // Tự động cuộn xuống cuối
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }

    private void updateUserList(String data) {
        String[] users = data.split(",");
        // Giữ lại lựa chọn hiện tại để không bị nhảy khi có người online/offline
        String currentSelection = userList.getSelectedValue();
        
        userModel.clear();
        userModel.addElement("Tất cả");
        for (String u : users) {
            if (!u.isEmpty()) {
                userModel.addElement(u);
                chatLogs.putIfAbsent(u, new StringBuilder("--- Chat với " + u + " ---\n"));
            }
        }
        // Chọn lại người đang chat dở
        userList.setSelectedValue(currentSelection, true);
    }

    void sendMessage() {
        try {
            String msg = messageField.getText().trim();
            if (msg.isEmpty()) return;

            if (currentChatTarget.equals("Tất cả")) {
                Client.dos.writeUTF("MSG|" + msg);
            } else {
                // Gửi riêng cho người đang chọn
                Client.dos.writeUTF("PRIVATE|" + currentChatTarget + "|" + msg);
                // Tự lưu vào lịch sử của mình với người đó (hiện bên phải)
                appendToLog(currentChatTarget, "Tôi: " + msg);
            }
            messageField.setText("");
        } catch (Exception e) {}
    }
}