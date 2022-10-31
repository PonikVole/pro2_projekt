package models.gui;

import models.Message;
import models.chatClients.ChatClient;
import models.chatClients.InMemoryChatClient;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ChatClient chatClient;
    private JTextArea chatTextArea = new JTextArea();

    private JTextField message;

    public MainFrame(int width, int height, ChatClient chatClient) {
        this.chatClient = chatClient;
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("KYS");
        initGui();
        setVisible(true);
    }

    private void initGui() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(initLoginPanel(), BorderLayout.NORTH);
        mainPanel.add(initChatPanel(), BorderLayout.CENTER);
        mainPanel.add(initMessagePanel(), BorderLayout.SOUTH);
        mainPanel.add(initLoggedUsersPanel(), BorderLayout.EAST);

        add(mainPanel);
    }

    private JPanel initLoginPanel() {
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        loginPanel.add(new Label("Username"));
        JTextField usernameField = new JTextField("", 30);
        loginPanel.add(usernameField);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String userName = usernameField.getText();
            System.out.println("Login clicked: " + userName);
            if (userName.length() < 1) {
                JOptionPane.showMessageDialog(null, "Enter your username", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (chatClient.isAuthenticated()) {
                chatClient.logout();
                loginButton.setText("Login");
                usernameField.setEditable(true);
                message.setEnabled(false);
                chatTextArea.setEnabled(false);
            } else {
                chatClient.login(userName);
                loginButton.setText("Logout");
                usernameField.setEditable(false);
                message.setEnabled(true);
                chatTextArea.setEnabled(true);
            }
        });
        loginPanel.add(loginButton);

        return loginPanel;
    }

    private JPanel initChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.X_AXIS));

        chatTextArea.setEditable(false);
        chatTextArea.setEnabled(false);
        chatPanel.add(new JScrollPane(chatTextArea));

        chatClient.addActionListener(e -> {
            if(e.getID() == ChatClient.MESSAGES_CHANGED) refreshMessages();
        });

        return chatPanel;
    }

    private JPanel initMessagePanel() {
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        message = new JTextField("", 50);
        message.setEnabled(false);
        messagePanel.add(message);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String messageText = message.getText();
            System.out.println("Send clicked: " + messageText);

            if (messageText.length() > 1 && chatClient.isAuthenticated()) {
                chatClient.sendMessage(messageText);
                message.setText("");
            }
        });
        messagePanel.add(sendButton);

        return messagePanel;
    }

    private JPanel initLoggedUsersPanel() {
        JPanel loggedUsersPanel = new JPanel();

        /*Object[][] data = {
                {0, 0},
                {1, 1},
                {2, 2},
                {"a", "a"},
        };

        String[] colNames = { "Col1", "Col2"};*/

        //JTable loggedUsersTable = new JTable(data, colNames);

        LoggedUsersTableModel loggedUsersTableModel = new LoggedUsersTableModel(chatClient);

        JTable loggedUsersTable = new JTable(loggedUsersTableModel);
        JScrollPane scrollPane = new JScrollPane(loggedUsersTable);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        loggedUsersPanel.add(scrollPane);

        chatClient.addActionListener(e -> {
            if(e.getID() == ChatClient.LOGGED_USERS_CHANGED) loggedUsersTableModel.fireTableDataChanged();
        });

        return loggedUsersPanel;
    }

    private void refreshMessages() {
        if(!chatClient.isAuthenticated()) return;
        chatTextArea.setText("");
        for (Message msg:chatClient.getMessages()) {
            chatTextArea.append(msg + "\n");
        }
    }
}
