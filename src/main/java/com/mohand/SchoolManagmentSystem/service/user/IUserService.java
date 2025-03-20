package com.mohand.SchoolManagmentSystem.service.user;

import com.mohand.SchoolManagmentSystem.model.User;

public interface IUserService {
    boolean checkIfUserExist(String email);
    User getByEmail(String email);
    User saveUser(User user);
}
