package models;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

public class Message {

    public static final int USER_LOGGED_IN = 1, USER_LOGGED_OUT = 2;
    public static final String AUTHOR_SYSTEM = "System";
    @Expose(serialize = true, deserialize = true)
    private String author, text;
    @Expose(serialize = true, deserialize = true)
    private LocalDateTime created;

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        this.created = LocalDateTime.now();
    }

    public Message(int type, String userName) {
        this.author = AUTHOR_SYSTEM;
        this.created = LocalDateTime.now();
        this.text = userName + switch (type){
            case USER_LOGGED_IN -> " has joined the chat.";
            case USER_LOGGED_OUT -> " has left the chat.";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        if (author.toUpperCase().equals(AUTHOR_SYSTEM.toUpperCase()))
            return text + "\n";
        String s = author + " ["+ created +"]\n";
        s += text+"\n";
        return s;
        /*
        if (author.equals(AUTHOR_SYSTEM)) return "(" + created.getHour() + ":" + created.getMinute() + ") " + text;
        return author + " (" + created.getHour() + ":" + created.getMinute() + ")" + "\n    " + text;
        */
    }
}
