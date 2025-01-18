package com.axe.notifications.DTOs;

import lombok.Data; 

@Data
public class NotificationUpdateDTO {
    private String id;
    private NotificationDTO notification;
}
