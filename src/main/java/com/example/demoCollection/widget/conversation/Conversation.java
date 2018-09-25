package com.example.demoCollection.widget.conversation;

public class Conversation {

    public Conversation(int id, int icon, String msg, int msgType) {
        this.id = id;
        this.icon = icon;
        this.msg = msg;
        this.msgType = msgType;
    }

    public int id;
    public int icon;
    public String msg;
    public int msgType;
    public boolean isSelected;
}
