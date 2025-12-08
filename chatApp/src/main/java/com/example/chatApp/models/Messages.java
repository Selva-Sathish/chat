package com.example.chatApp.models;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.chatApp.enums.MessageType;

@Document(collection = "chat")
public class Messages {
    
    private Long conversationID;

    @Field(name = "sender_id")
    private Long senderID;

    private String content;
    
    private MessageType type;
    
    @Field(name = "is_read")
    private boolean isRead;

    @Field(name = "created_at")
    private LocalDateTime createdAt;
}
