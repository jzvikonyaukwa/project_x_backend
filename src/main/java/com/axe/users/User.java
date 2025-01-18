package com.axe.users;

import com.axe.common.enums.UserGroup;
import com.axe.notifications.Notification;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 50)
    @Column(name = "username")
    private String username;

    @Column(name = "fullname")
    private String fullname;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "assigned_groups")
    private UserGroup groups;
 
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @Version
    private Timestamp version;

    /**
     * Helper method to manage bidirectional relationship
     */
    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setUser(this);
    }

    /**
     * Helper method to manage bidirectional relationship
     */
    public void removeNotification(Notification notification) {
        notifications.remove(notification);
        notification.setUser(null);
    }
}
