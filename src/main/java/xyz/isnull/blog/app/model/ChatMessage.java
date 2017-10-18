package xyz.isnull.blog.app.model;

import lombok.Data;

@Data
public class ChatMessage {

    private String nickname;

    private String content;

    private String sendTime;

}

