package com.axe.users;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.axe.common.enums.UserGroup;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> //
        , JpaSpecificationExecutor<User> {

    interface Specs {
        /**
         * Filter Notification by ID.
         * 
         * @param id The ID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<User> withId(Number id) {
            return (root, query, builder) -> builder.equal(root.get("id"), id);
        }

        /**
         * Filter Notification by status.
         * 
         * @param status The status to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<User> hasBeenRead(Boolean status) {
            return (root, query, builder) -> builder.equal(root.get("read"), status);
        }

        /**
         * Filter Notification by user email.
         * 
         * @param id The email to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<User> withEmail(String email) {
            return (root, query, builder) -> builder.equal(root.get("email"), email);
        }

        /**
         * Filter User by active status.
         *
         * @param isActive The active status to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<User> isActive(Boolean isActive) {
            return (root, query, builder) -> isActive ? builder.isTrue(root.get("isActive"))
                    : builder.isFalse(root.get("isActive"));
        }

        /**
         * Filter User by user group.
         *
         * @param group The user group to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<User> withGroup(UserGroup group) {
            return (root, query, builder) -> builder.equal(root.get("groups"), group);
        }
    }

    Optional<User> findByEmail(String email);
}
