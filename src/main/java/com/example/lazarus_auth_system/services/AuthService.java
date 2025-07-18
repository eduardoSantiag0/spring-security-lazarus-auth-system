package com.example.lazarus_auth_system.services;

import com.example.lazarus_auth_system.infra.persistance.MissionRepository;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.infra.persistance.UserRepository;
import com.example.lazarus_auth_system.securiy.dtos.ChangeLoginDTO;
import com.example.lazarus_auth_system.securiy.dtos.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }


    public Optional<UserEntity> loadUserById(Long id) throws UsernameNotFoundException {
        return userRepository.findById(id);
    }

    public ResponseEntity<String> saveUser(RegisterDTO data) {
        if (this.userRepository.findByUsername(data.username()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists."); //409
        }

        if (this.missionRepository.findByMissionCode(data.missionCode()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mission code does not exist."); //404
        }

        UserEntity newUser = userFactory.createNewUser(data);

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered: " + newUser.getUsername());
    }


    public ResponseEntity<?> changeCredentials(ChangeLoginDTO resetPasswordDTO, UserEntity user) {

        if (!passwordEncoder.matches(resetPasswordDTO.currentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong password"); // 400
        }

        userService.updateUserCredentials(resetPasswordDTO, user);

        return ResponseEntity.ok("Credentials updated successfully");

    }


    public void updateRefreshToken(UserEntity user, String newRefreshToken) {
        user.setRefresh_token(newRefreshToken);
        userRepository.save(user);
    }
}
