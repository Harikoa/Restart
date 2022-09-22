package com.meyvn.restart_mobile;

public class MessagesList {

    private String name, email, lastMessage, profilePic;
    private int unseenMessages;

    public MessagesList(String name, String email, String lastMessage, String profilePic, int unseenMessages) {
        this.name = name;
        this.email = email;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }
}
