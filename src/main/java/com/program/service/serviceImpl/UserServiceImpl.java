package com.program.service.serviceImpl;


import com.program.exception.UserException;
import com.program.model.role.ERole;
import com.program.model.role.Role;
import com.program.model.User;
import com.program.payload.request.AssignRequest;
import com.program.payload.request.RegisterRequest;
import com.program.repository.RoleRepository;
import com.program.repository.UserRepository;
import com.program.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String ADMIN="ROLE_ADMIN";
    private static final String USER="ROLE_TEACHER";

    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public User changeRoleToAdmin(User user) throws UserException {
//        Role adminRole = roleRepository.findByRole(ADMIN);
//        user.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
        return userRepository.save(user);
    }


    @Override
    public List<User> findAll() throws UserException {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRoleTeacher() throws UserException {
        List<User> users = userRepository.findAll();
        users.removeIf(user -> user.isAdmin() && user.isObserver());
        return users;
    }

    @Override
    public User createUser(User user) throws UserException {
        user.setPassword(user.getPassword());
//        Role userRole = roleRepository.findByRole(USER);
//        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }
    @Override
    public User getUserByEmail(String email) throws UserException {
        return userRepository.findByEmail(email);
    }

    @Override
    public User isUserEmailPresent(String email) throws UserException {
        if(userRepository.findByEmail(email) != null){
            return userRepository.findByEmail(email);
        }
        else
            throw new UserException("This user email doesn't exist!");
    }

    @Override
    public Boolean existsByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        return user != null;

    }

    @Override
    public User getUserById(Long id) throws UserException {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(Long id, User user) throws UserException {

        Optional<User> opt = userRepository.findById(id);

        if (opt.isPresent())
        {
            return userRepository.save(user);
        }else {
            throw new UserException("User with given id is not present........");
        }
    }

    @Override
    public void registerUser(RegisterRequest registerRequest) throws UserException {

        User user = new User(registerRequest.getEmail(),
                registerRequest.getUsername(),
                encoder.encode(registerRequest.getPassword()));

        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            throw new UserException("Role is not found! Please indicate role!");
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "Admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "Observer" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_OBSERVER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    case "Teacher" -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) throws UserException {
        User user = userRepository.getOne(id);
        userRepository.delete(user);
    }


}
