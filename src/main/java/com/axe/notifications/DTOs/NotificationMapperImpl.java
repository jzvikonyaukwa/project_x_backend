package com.axe.notifications.DTOs;

import com.axe.notifications.Notification; 

public final class NotificationMapperImpl {

    public NotificationDTO fromNotification(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setDescription( notification.getDescriptionText() );
        notificationDTO.setIcon( notification.getIconName() );
        notificationDTO.setImage( notification.getImageUrl() );
        notificationDTO.setLink( notification.getLinkUrl() );
        notificationDTO.setTime( notification.getCreatedAt() );
        notificationDTO.setTitle( notification.getTitleText() );
        if ( notification.getId() != null ) {
            notificationDTO.setId( String.valueOf( notification.getId() ) );
        }
        notificationDTO.setUseRouter( notification.getUseRouter() );
        if ( notification.getRead() != null ) {
            notificationDTO.setRead( notification.getRead() );
        }

        return notificationDTO;
    }

    public Notification fromNotificationDTO(NotificationDTO notification) {
        if ( notification == null ) {
            return null;
        }

        Notification.NotificationBuilder notification1 = Notification.builder();

        notification1.descriptionText( notification.getDescription() );
        notification1.iconName( notification.getIcon() );
        notification1.imageUrl( notification.getImage() );
        notification1.linkUrl( notification.getLink() );
        notification1.createdAt( notification.getTime() );
        notification1.titleText( notification.getTitle() );
        if ( notification.getId() != null ) {
            notification1.id( Long.parseLong( notification.getId() ) );
        }
        notification1.useRouter( notification.getUseRouter() );
        notification1.read( notification.isRead() );

        return notification1.build();
    }
}
