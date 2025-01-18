package com.axe.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.axe.notifications.DTOs.NotificationDTO;
import com.axe.notifications.DTOs.NotificationUpdateDTO;
import com.axe.notifications.services.NotificationService;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/common")
@CrossOrigin(origins = { "http://localhost:4200", "http://axebuild.io", "https://axebuild.io" })
public class CommonController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    private final NotificationService service;

    public CommonController(NotificationService notificationService) {
        this.service = notificationService;
    }

    @GetMapping("notifications")
    public List<NotificationDTO> getNotifications(@RequestHeader(AUTHORIZATION) String header) {
        logger.info("Fetching user notifications");
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return service.fetchUserNotifications(userEmail,  25);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Server encountered an error while fetching notifications");
        }
    }

    @PostMapping("notifications")
    public NotificationDTO createNotification(@RequestHeader(AUTHORIZATION) String header,
            @RequestBody NotificationDTO notification) {
        logger.info("Creating a new notification: {}", notification);
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return service.createNotification(userEmail, notification);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Server encountered an error while creating notification");
        }
    }

    @PatchMapping("notifications")
    public NotificationDTO updateNotification(@RequestHeader(AUTHORIZATION) String header,
            @RequestBody NotificationUpdateDTO notificationUpdate) {
        logger.info("Updating a notification: {}", notificationUpdate);
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return service.updateNotification(userEmail, notificationUpdate);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Server encountered an error while updating notification");
        }
    }

    @DeleteMapping("notifications")
    public Boolean deleteNotification(@RequestHeader(AUTHORIZATION) String header,
            @RequestParam("id") Long notificationID) {
        logger.info("Deleting notification: {}", notificationID);
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return service.deleteNotification(userEmail, notificationID);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Server encountered an error while deleting notification with id: " + notificationID);
        }
    }

    @GetMapping("notifications/mark-all-as-read")
    public Boolean markAllNotificationsAsRead(@RequestHeader(AUTHORIZATION) String header) {
        logger.info("Marking all user notifications as read");
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return service.markAllNotificationsAsRead(userEmail);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Server encountered an error while marking all notifications as read");
        }
    }
    
    private String extractEmailFromAuthorizationHeader(final String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = header.substring(7);
        return JWT.decode(token).getClaim("email").asString();
    }
}
