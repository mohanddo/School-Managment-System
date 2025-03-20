package com.mohand.SchoolManagmentSystem.service.user;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.User;
import com.mohand.SchoolManagmentSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public boolean checkIfUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }


}
