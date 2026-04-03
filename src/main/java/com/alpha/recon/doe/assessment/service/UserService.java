package com.alpha.recon.doe.assessment.service;

import com.alpha.recon.doe.assessment.domain.User;
import com.alpha.recon.doe.assessment.repository.UserMapper;
import com.alpha.recon.doe.assessment.repository.UserRepository;
import com.alpha.recon.doe.assessment.repository.domain.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<User> findAll() {
        return this.userRepository.findAll().stream().map(userMapper::map).collect(Collectors.toList());
    }

    public User findById(final Long id) {
        final UserDto userDto = userRepository.findById(id).orElseThrow(() -> this.notFound(id));
        return this.userMapper.map(userDto);
    }

    public User create(final User user) {
        final UserDto userDto = this.userMapper.map(user);
        userDto.setCreatedAt(LocalDateTime.now());

        return this.userMapper.map(userRepository.save(userDto));
    }

    public User update(final Long id, final User user) {
        UserDto existing = userRepository.findById(id).orElseThrow(() -> this.notFound(id));

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setNote(user.getNote());

        return this.userMapper.map(userRepository.save(existing));
    }

    public void delete(final Long id) {
        if (!userRepository.existsById(id)) {
            throw this.notFound(id);
        }

        userRepository.deleteById(id);
    }

    private EntityNotFoundException notFound(final Long id) {
        return new EntityNotFoundException("User not found with id [" + id + "]");
    }
}
