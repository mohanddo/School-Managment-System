package com.mohand.SchoolManagmentSystem.service.user;

import com.mohand.SchoolManagmentSystem.model.User;
import com.mohand.SchoolManagmentSystem.request.password.ChangePasswordRequest;

import java.security.Principal;

public interface IUserService {
    boolean checkIfExist(String email);
    User getByEmail(String email);
    User save(User user);
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
    void createPasswordResetTokenForStudent(String email, String token);
    User getUserByToken(String token);
}
