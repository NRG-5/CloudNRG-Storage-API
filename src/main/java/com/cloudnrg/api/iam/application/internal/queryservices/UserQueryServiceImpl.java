package com.cloudnrg.api.iam.application.internal.queryservices;

import com.cloudnrg.api.iam.domain.model.aggregates.User;
import com.cloudnrg.api.iam.domain.model.queries.GetUserByIdQuery;
import com.cloudnrg.api.iam.domain.services.UserQueryService;
import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findUserById(query.userId());
    }
}
