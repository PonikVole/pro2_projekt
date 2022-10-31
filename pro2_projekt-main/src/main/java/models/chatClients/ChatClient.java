package models.chatClients;

import models.Message;

import java.awt.event.ActionListener;
import java.util.List;

public interface ChatClient {
    int LOGGED_USERS_CHANGED = 1, MESSAGES_CHANGED = 2;
    void sendMessage(String text);
    void login(String userName);
    void logout();
    boolean isAuthenticated();

    List<String> getLoggedUsers();
    List<Message> getMessages();

    void addActionListener(ActionListener listener);
}
