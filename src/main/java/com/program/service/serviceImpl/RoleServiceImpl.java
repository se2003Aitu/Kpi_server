package com.program.service.serviceImpl;

import com.program.exception.RoleException;
import com.program.model.role.Role;
import com.program.repository.RoleRepository;
import com.program.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) throws RoleException {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() throws RoleException {
        return roleRepository.findAll();
    }
}
