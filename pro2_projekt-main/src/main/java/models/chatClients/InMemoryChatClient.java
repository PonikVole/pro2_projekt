package models.chatClients;

import models.Message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;
    private List<ActionListener> listeners = new ArrayList<>();

    public InMemoryChatClient() {
        this.loggedUsers = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println("new message - " + text);
        raiseEventMessagesChanged();
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessage(Message.USER_LOGGED_IN, loggedUser);
        System.out.println("new logged in - " + userName);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);
        loggedUser = null;
        System.out.println("user logged out");
        raiseEventLoggedUsersChanged();
    }

    @Override
    public boolean isAuthenticated() {
        return loggedUser != null;
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    private void raiseEventLoggedUsersChanged() {
        for (ActionListener listener : listeners) {
            listener.actionPerformed(new ActionEvent(this, LOGGED_USERS_CHANGED, "usersChanger"));
        }
    }

    private void raiseEventMessagesChanged() {
        for (ActionListener listener : listeners) {
            listener.actionPerformed(new ActionEvent(this, MESSAGES_CHANGED, "messagesChanged"));
        }
    }

    public void addSystemMessage(int type, String userName) {
        messages.add(new Message(type, userName));
        raiseEventMessagesChanged();
    }
}
