package com.axe.notifications.services;

import com.axe.common.DAO.NotificationDaoImpl;
import com.axe.common.enums.UserGroup;
import com.axe.notifications.Notification;
import com.axe.notifications.NotificationRepository;
import com.axe.notifications.DTOs.NotificationDTO;
import com.axe.notifications.DTOs.NotificationMapperImpl;
import com.axe.notifications.DTOs.NotificationUpdateDTO;
import com.axe.users.User;
import com.axe.users.services.UsersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationDaoImpl notificationDao;
    private final UsersService usersService;

    public NotificationService(NotificationRepository notificationRepository,
            UsersService usersService,
            @Lazy NotificationDaoImpl notificationDao) {
        this.notificationRepository = notificationRepository;
        this.notificationDao = notificationDao;
        this.usersService = usersService;
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> fetchUserNotifications(final String userEmail, int limit) {
        logger.debug("Fetching {} user notifications for: {}", limit, userEmail);
        try {
            // @formatter:off
            String expected = "SELECT t0.*, t1.name, t1.BUDDY_ID " + 
            "FROM mtm.MEMBERS AS t0  INNER JOIN mtm.USERS AS t1  ON (t1.UID = t0.id)";
            // @formatter:on
            final List<Notification> matchingRecords = notificationDao.fetchUserNotifications(userEmail, limit);

            NotificationMapperImpl mapper = new NotificationMapperImpl();

            return matchingRecords.stream()
                    .map(o -> mapper.fromNotification(o))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
            throw new RuntimeException("Unexpected error:" + e.getLocalizedMessage());
        }
    }

    @Transactional
    public Boolean markAllNotificationsAsRead(final String userEmail) {
        logger.debug("Marking all notifications as read for: {}", userEmail);
        try {
            return notificationDao.markAllNotificationsAsRead(userEmail);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
            throw new RuntimeException("Unexpected error:" + e.getLocalizedMessage());
        }
    }

    @Transactional
    public Boolean deleteNotification(final String userEmail, Long notificationID) {
        logger.debug("Deleting notification with id: {} for: {}", notificationID, userEmail);
        try {
            return notificationDao.deleteNotification(userEmail, notificationID);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
            throw new RuntimeException("Unexpected error:" + e.getLocalizedMessage());
        }
    }

    @Transactional
    public NotificationDTO updateNotification(final String userEmail, NotificationUpdateDTO notificationUpdate) {
        logger.debug("Updating notification: {} for: {}", notificationUpdate, userEmail);
        try {
            NotificationMapperImpl mapper = new NotificationMapperImpl();
            Notification updatedNotification = mapper.fromNotificationDTO(notificationUpdate.getNotification());

            Notification storedNotification = notificationDao.updateNotification(userEmail, updatedNotification);

            return mapper.fromNotification(storedNotification);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
            throw new RuntimeException("Unexpected error:" + e.getLocalizedMessage());
        }
    }

    @Transactional
    public NotificationDTO createNotification(final String userEmail, NotificationDTO notification) {
        logger.debug("Creating notification: {} for: {}", notification, userEmail);
        try {
            NotificationMapperImpl mapper = new NotificationMapperImpl();
            Notification storedNotification = notificationDao.createNotification(userEmail, notification);
            
            return mapper.fromNotification(storedNotification);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
            throw new RuntimeException("Unexpected error:" + e.getLocalizedMessage());
        }
    }

    @Transactional
    public void sendNotificationToActiveSuperUsers(String titleText, String descriptionText) {
        logger.debug("Sending notification to active super users");
        try {
            sendNotificationToActiveSuperUsers(titleText, descriptionText, null, null);

        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
            throw new RuntimeException("Unexpected error:" + e.getLocalizedMessage());
        }
    }

    @Transactional
    public void sendNotificationToActiveSuperUsers(String titleText, String descriptionText, String linkUrl,
            String iconName) {
        logger.debug("Sending notification to active super users");
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            List<User> activeSuperUsers = usersService.fetchUsersInGroup(UserGroup.superUserGroup);
            logger.debug("Active super users: {}", activeSuperUsers);
            for (User user : activeSuperUsers) {
                logger.debug("Sending notification to: {}", user.getEmail());
                Notification notification = Notification.builder().titleText(titleText).descriptionText(descriptionText)
                        .createdAt(currentTime)
                        .read(false)
                        .user(user)
                        .useRouter(false)
                        .build();

                if (iconName != null) {
                    notification.setIconName(iconName);
                }

                if (linkUrl != null) {
                    notification.setLinkUrl(linkUrl);
                    notification.setUseRouter(true);
                }

                user.addNotification(notification);
                notificationRepository.save(notification);
            }
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
            throw new RuntimeException("Unexpected error:" + e.getLocalizedMessage());
        }
    }
}
