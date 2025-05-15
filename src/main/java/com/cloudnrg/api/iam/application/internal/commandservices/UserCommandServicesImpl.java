package com.cloudnrg.api.iam.application.internal.commandservices;

import com.cloudnrg.api.iam.domain.model.aggregates.User;
import com.cloudnrg.api.iam.domain.model.commands.CreateUserCommand;
import com.cloudnrg.api.iam.domain.services.UserCommandService;
import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServicesImpl implements UserCommandService {

    private final UserRepository userRepository;

    public UserCommandServicesImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(CreateUserCommand command) {

        var newUser = new User(
                command.username(),
                command.email(),
                command.passwordHash()
        );

        try {

            var savedUser = userRepository.save(newUser);
            savedUser.userCreation();
            return Optional.of(savedUser);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }




    }
}
