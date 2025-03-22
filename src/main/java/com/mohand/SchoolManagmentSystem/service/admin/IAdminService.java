package com.mohand.SchoolManagmentSystem.service.admin;

import com.mohand.SchoolManagmentSystem.model.Admin;

public interface IAdminService {
    Admin readAdminById(Long id);
    Admin save(Admin admin);
    Admin getByEmail(String email);
    boolean checkIfExistByEmail(String email);
}
