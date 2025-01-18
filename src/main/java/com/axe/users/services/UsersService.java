package com.axe.users.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.axe.common.enums.UserGroup;
import com.axe.users.User;
import com.axe.users.UserRepository;

@Service
public class UsersService {
    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> fetchUsersInGroup(UserGroup group) {
        logger.debug("Fetching users in group: {}", group);

        if (group == null) {
            return Collections.emptyList(); // Or throw IllegalArgumentException
        }
        Sort emailSort = Sort.by(Sort.Direction.ASC, "email");
        Specification<User> userGroupSpecification = UserRepository.Specs.withGroup(group)
                .and(UserRepository.Specs.isActive(true));
        return userRepository.findAll(userGroupSpecification, emailSort);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}