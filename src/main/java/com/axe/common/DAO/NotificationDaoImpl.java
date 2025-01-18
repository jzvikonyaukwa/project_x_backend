package com.axe.common.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.axe.notifications.Notification;
import com.axe.notifications.DTOs.NotificationDTO;
import com.axe.users.User;
import com.axe.utilities.ClientVisibleException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import java.sql.Timestamp;
import java.util.List;
import java.util.Collections;

@Lazy
@Repository
public class NotificationDaoImpl implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(NotificationDaoImpl.class);

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;
    private final UsersDaoImpl usersDao;

    public NotificationDaoImpl(final EntityManager entityManager$, final TransactionTemplate transactionTemplate$,
            @Lazy final UsersDaoImpl usersDao$) {
        entityManager = entityManager$;
        transactionTemplate = transactionTemplate$;
        usersDao = usersDao$;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Boolean deleteNotification(String email, Long notificationID) {
        logger.debug("Deleting notification with id: {} for: {}", notificationID, email);
        try {
            // update notification using criteria API
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // Create ParameterExpression objects
            ParameterExpression<Boolean> readParameter = cb.parameter(Boolean.class, "isRead");
            ParameterExpression<String> emailParameter = cb.parameter(String.class, "targetEmail");

            CriteriaUpdate<Notification> notificationUpdate = cb.createCriteriaUpdate(Notification.class);
            Root<Notification> root = notificationUpdate.from(Notification.class);

            Subquery<User> userSubquery = notificationUpdate.subquery(User.class);
            Root<User> userRoot = userSubquery.from(User.class);

            notificationUpdate.set(root.<Boolean>get("read"), readParameter);
            notificationUpdate.set(root.<Timestamp>get("acknowledgedAt"), cb.currentTimestamp());
            notificationUpdate.set(root.<Timestamp>get("version"), cb.currentTimestamp());

            // Specify the condition (where id = ?)
            notificationUpdate.where(
                    cb.and(
                            root.get("user").in(
                                    userSubquery.select(userSubquery.from(User.class))
                                            .where(
                                                    cb.and(
                                                            cb.isTrue(userRoot.get("isActive")),
                                                            cb.equal(userRoot.get("email"), emailParameter)))),
                            cb.equal(root.get("id"), notificationID)));

            Query query = entityManager.createQuery(notificationUpdate);
            query.setParameter("isRead", true);
            query.setParameter("targetEmail", email);

            int rowsAffected = query.executeUpdate();
            logger.debug("Rows affected: {}", rowsAffected);

            return rowsAffected > 0;
        } catch (DataAccessException e) {
            logger.error("Data access error deleting notification: {}", e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Failed to deleting notification due to data access error",
                    e.getMostSpecificCause()) {
            };
        } catch (Exception e) {
            logger.error("Unexpected error delete notification: {}", e.getLocalizedMessage());
            throw new RuntimeException("Failed to delete notification due to an unexpected error", e) {
            };
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Boolean markAllNotificationsAsRead(String userEmail) {
        logger.debug("Marking all notifications as read for: {}", userEmail);
        try {
            // update notification using criteria API
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // Create ParameterExpression objects
            ParameterExpression<Boolean> readParameter = cb.parameter(Boolean.class, "isRead");
            ParameterExpression<String> emailParameter = cb.parameter(String.class, "targetEmail");

            CriteriaUpdate<Notification> notificationUpdate = cb.createCriteriaUpdate(Notification.class);
            Root<Notification> root = notificationUpdate.from(Notification.class);

            Subquery<User> userSubquery = notificationUpdate.subquery(User.class);
            Root<User> userRoot = userSubquery.from(User.class);

            notificationUpdate.set(root.<Boolean>get("read"), readParameter);
            notificationUpdate.set(root.<Timestamp>get("version"), cb.currentTimestamp());

            // Specify the condition (where id = ?)
            notificationUpdate.where(
                    root.get("user").in(
                            userSubquery.select(userRoot)
                                    .where(
                                            cb.and(
                                                    cb.isTrue(userRoot.get("isActive")),
                                                    cb.equal(userRoot.get("email"), emailParameter)))));

            Query query = entityManager.createQuery(notificationUpdate);
            query.setParameter("isRead", true);
            query.setParameter("targetEmail", userEmail);

            int rowsAffected = query.executeUpdate();
            logger.debug("Rows affected: {}", rowsAffected);

            return rowsAffected > 0;
        } catch (DataAccessException e) {
            logger.error("Data access error updating notification: {}", e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Failed to update notification due to data access error",
                    e.getMostSpecificCause());
        } catch (Exception e) {
            logger.error("Unexpected error updating notification: {}", e.getLocalizedMessage());
            throw new RuntimeException("Failed to update notification due to an unexpected error", e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Notification> fetchUserNotifications(String userEmail, int limit) {
        logger.debug("Fetching {} user notifications for: {}", limit, userEmail);
        try {
            // fetch notification using criteria API
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // Create ParameterExpression objects
            ParameterExpression<Boolean> readParameter = cb.parameter(Boolean.class, "isRead");
            ParameterExpression<String> emailParameter = cb.parameter(String.class, "targetEmail");

            // create a count query
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Notification> countRoot = countQuery.from(Notification.class);
            Join<Notification, User> countUserJoin = countRoot.join("user");

            CriteriaQuery<Notification> notificationQuery = cb.createQuery(Notification.class);
            Root<Notification> root = notificationQuery.from(Notification.class);
            Join<Notification, User> userJoin = root.join("user");

            countQuery.select(cb.count(countRoot));
            countQuery.where(
                    cb.and(
                            cb.and(
                                    cb.equal(countUserJoin.get("email"), emailParameter),
                                    cb.isTrue(countUserJoin.get("isActive"))),
                            cb.or(
                                    cb.equal(countRoot.get("read"), readParameter),
                                    cb.isNull(countRoot.get("acknowledgedAt")))));

            TypedQuery<Long> countTypedQuery = entityManager.createQuery(countQuery);
            countTypedQuery.setParameter(readParameter, false);
            countTypedQuery.setParameter(emailParameter, userEmail);
            countTypedQuery.setLockMode(LockModeType.NONE);

            Long rowsFound = countTypedQuery.getSingleResult();
            logger.debug("Rows found: {}", rowsFound);
            if (rowsFound == 0) {
                return Collections.emptyList();
            }

            // Specify the condition (where id = ?)
            notificationQuery.where(
                    cb.and(
                            cb.and(
                                    cb.equal(userJoin.get("email"), emailParameter),
                                    cb.isTrue(userJoin.get("isActive"))),
                            cb.or(
                                    cb.equal(root.get("read"), readParameter),
                                    cb.isNull(root.get("acknowledgedAt")))));

            notificationQuery.orderBy(
                    cb.desc(root.get("createdAt")),
                    cb.asc(root.get("titleText")),
                    cb.desc(root.get("id")));

            TypedQuery<Notification> query = entityManager.createQuery(notificationQuery);
            query.setParameter(readParameter, false);
            query.setParameter(emailParameter, userEmail);
            query.setLockMode(LockModeType.NONE);
            query.setMaxResults(limit);

            return query.getResultList();
        } catch (DataAccessException e) {
            logger.error("Data access error fetching notification: {}", e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Failed to fetching notifications due to data access error",
                    e.getMostSpecificCause());
        } catch (Exception e) {
            logger.error("Unexpected error fetching notification: {}", e.getLocalizedMessage());
            throw new RuntimeException("Failed to fetching notifications due to an unexpected error", e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Notification updateNotification(String userEmail, Notification notificationUpdate) {
        logger.debug("Updating notification: {} for: {}", notificationUpdate, userEmail);
        try {
            Notification storedNotification = entityManager.find(Notification.class, notificationUpdate.getId(),
                    LockModeType.PESSIMISTIC_WRITE);
            if (storedNotification == null) {
                logger.error("Notification with id: {} not found for user: {}", notificationUpdate.getId(), userEmail);
                throw new ClientVisibleException("Notification '%d' not found".formatted(notificationUpdate.getId()));
            }

            if (notificationUpdate.getTitleText() != null) {
                storedNotification.setTitleText(notificationUpdate.getTitleText());
            }

            if (notificationUpdate.getDescriptionText() != null) {
                storedNotification.setDescriptionText(notificationUpdate.getDescriptionText());
            }

            storedNotification.setIconName(notificationUpdate.getIconName());
            storedNotification.setImageUrl(notificationUpdate.getImageUrl());

            storedNotification.setLinkUrl(notificationUpdate.getLinkUrl());
            storedNotification.setUseRouter(notificationUpdate.getUseRouter());

            storedNotification.setRead(notificationUpdate.getRead());
            storedNotification.setAcknowledgedAt(notificationUpdate.getAcknowledgedAt());
            // if (!updatedNotification.getRead()) {
            // storedNotification.setAcknowledgedAt(null);
            // }

            return entityManager.merge(storedNotification);
        } catch (DataAccessException e) {
            logger.error("Data access error updating notification with id: {} for user: {}. Error: {}",
                    notificationUpdate.getId(), userEmail, e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error while updating notifications",
                    e.getMostSpecificCause());
        } catch (Exception e) {
            logger.error("Unexpected error updating notification with id: {} for user: {}. Error: {}",
                    notificationUpdate.getId(), userEmail, e.getLocalizedMessage());
            throw new RuntimeException("Failed to updating notifications due to an unexpected error", e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Notification createNotification(String userEmail, NotificationDTO notification) {
        logger.debug("Creating notification: {} for: {}", notification, userEmail);
        try {
            Notification newNotification = Notification.builder()
                    .titleText(notification.getTitle())
                    .descriptionText(notification.getDescription())
                    .iconName(notification.getIcon())
                    .imageUrl(notification.getImage())
                    .linkUrl(notification.getLink())
                    .useRouter(notification.getUseRouter())
                    .read(false)
                    .build();

            User user = usersDao.fetchUserByEmail(userEmail);
            if (user == null) {
                logger.error("User with email: {} not found", userEmail);
                throw new ClientVisibleException("User '%s' not found".formatted(userEmail));
            }

            newNotification.setUser(user);
            entityManager.persist(newNotification);
            return newNotification;
        } catch (DataAccessException e) {
            logger.error("Data access error creating notification: {}", e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Failed to create notification due to data access error",
                    e.getMostSpecificCause());
        } catch (Exception e) {
            logger.error("Unexpected error creating notification: {}", e.getLocalizedMessage());
            throw new RuntimeException("Failed to create notification due to an unexpected error", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("Initializing NotificationDaoImpl");
    }

    @Override
    public void destroy() throws Exception {
        logger.debug("Destroying NotificationDaoImpl");
    }

}
