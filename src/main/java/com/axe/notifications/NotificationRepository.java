package com.axe.notifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationRepository extends JpaRepository<Notification, Long> //
        , JpaSpecificationExecutor<Notification> {

    interface Specs {
        /**
         * Filter Notification by ID.
         * 
         * @param id The ID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Notification> withId(Number id) {
            return (root, query, builder) -> builder.equal(root.get("id"), id);
        }

        /**
         * Filter Notification by status.
         * 
         * @param status The status to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Notification> hasBeenRead(Boolean status) {
            return (root, query, builder) -> builder.equal(root.get("read"), status);
        }

        static Specification<Notification> isUnread() {
            return (root, query, builder) -> hasBeenRead(false).toPredicate(root, query, builder);
        }

        /**
         * Filter Notification by user email.
         * 
         * @param id The email to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Notification> withEmail(String email) {
            return (root, query, builder) -> builder.equal(root.get("user").get("email"), email);
        }

        // /**
        // * Filter Notification by clientID.
        // *
        // * @param id The clientID to filter by.
        // * @return A Specification object representing the filter criteria.
        // */
        // static Specification<Notification> withClientId(Number clientID) {
        // return (root, query, builder) ->
        // builder.equal(root.get("project").get("client").get("id"), clientID);
        // }
    }
}
