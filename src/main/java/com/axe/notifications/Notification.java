package com.axe.notifications;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.axe.users.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id; 

    @Column(name = "icon")
    @Size(max = 255, message = "Icon length must not exceed 255 characters")
    private String iconName;
 
    @Column(name = "image")
    @Size(max = 255, message = "Image URL length must not exceed 255 characters")
    private String imageUrl;

    @Column(name = "title")
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title length must not exceed 255 characters")
    private String titleText;

    @Column(name = "description")
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description length must not exceed 1000 characters")
    private String descriptionText;

    @Column(name = "created_at")
    @NotNull(message = "Creation time is required")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "link")
    @Size(max = 255, message = "Link length must not exceed 255 characters")
    private String linkUrl;

    @Column(name = "should_use_router")
    @Builder.Default
    private Boolean useRouter = false;

    @Column(name = "has_been_read")
    @Builder.Default
    private Boolean read = false;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    private Timestamp version;
}
