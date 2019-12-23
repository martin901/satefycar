package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.UserPrincipal;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user, boolean encryptPassword) {
        if (encryptPassword) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with id %d does not exist", id)));
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User updateById(Long id, User updatedUser) {
        User userToUpdate = getById(id);

        updatedUser.setId(userToUpdate.getId());
        if (updatedUser.getName().getFirstName() == null) {
            updatedUser.getName().setFirstName(userToUpdate.getName().getFirstName());
        }
        if (updatedUser.getName().getLastName() == null) {
            updatedUser.getName().setLastName(userToUpdate.getName().getLastName());
        }
        if (userToUpdate.getAddress() == null) {
            updatedUser.setAddress(userToUpdate.getAddress());
        }
        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(userToUpdate.getEmail());
        }
        if (updatedUser.getPhone() == null) {
            updatedUser.setPhone(userToUpdate.getPhone());
        }
        if (updatedUser.getUsername() == null) {
            updatedUser.setUsername(userToUpdate.getUsername());
        }
        if (updatedUser.getPassword() == null) {
            updatedUser.setPassword(userToUpdate.getPassword());
        }
        if (updatedUser.getRole() == null) {
            updatedUser.setRole(userToUpdate.getRole());
        }

        return userRepository.save(updatedUser);
    }

    @Override
    public User getCurrentUser() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return getByUsername(principal.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(SpringSecurityMessageSource.getAccessor().getMessage("AbstractUserDetailsAuthenticationProvider.UserUnknown", "Incorrect username"));
        }

        return new UserPrincipal(user);
    }
}
