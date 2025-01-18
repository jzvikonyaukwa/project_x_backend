package com.axe.notifications.DTOs;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private String id;
    private String icon;
    private String image;
    private String title;
    private String description;
    private LocalDateTime time;
    private String link;
    private Boolean useRouter;
    private boolean read;
}
