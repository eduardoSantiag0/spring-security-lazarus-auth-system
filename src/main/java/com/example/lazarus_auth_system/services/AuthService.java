package com.example.lazarus_auth_system.services;

import com.example.lazarus_auth_system.dtos.RegisterDTO;
import com.example.lazarus_auth_system.dtos.ChangeLoginDTO;
import com.example.lazarus_auth_system.infra.persistance.MissionRepository;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.infra.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<String> saveUser(RegisterDTO data) {
        if (this.userRepository.findByUsername(data.username()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        if (this.missionRepository.findByMissionCode(data.missionCode()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mission code does not exist.");
        }

        UserEntity newUser = userFactory.createNewUser(data);

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered: " + newUser.getUsername());
    }

    public ResponseEntity<?> changeCredentials(ChangeLoginDTO resetPasswordDTO, UserEntity user) {
        // Checar se passsord inserida bate com da conta do user
        if (!passwordEncoder.matches(resetPasswordDTO.currentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong password");
        }

        // Atualizar conta
        if(resetPasswordDTO.newPassword().isPresent())
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.newPassword().get()));
        if(resetPasswordDTO.newUsername().isPresent())
            user.setUsername(resetPasswordDTO.newUsername().get());

        // Salvar
        userRepository.save(user);

        return ResponseEntity.ok("Credentials updated successfully");

    }

}
