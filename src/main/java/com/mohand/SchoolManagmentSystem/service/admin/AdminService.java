package com.mohand.SchoolManagmentSystem.service.admin;

import com.mohand.SchoolManagmentSystem.exception.user.account.AccountNotFoundException;
import com.mohand.SchoolManagmentSystem.model.user.Admin;
import com.mohand.SchoolManagmentSystem.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

    private final AdminRepository adminRepository;


    @Override
    public Admin readAdminById(Long id) {
        return adminRepository
                .findById(id)
                .orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin getByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public boolean checkIfExistByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}
