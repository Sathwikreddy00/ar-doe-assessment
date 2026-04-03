package com.alpha.recon.doe.assessment.repository;

import com.alpha.recon.doe.assessment.domain.User;
import com.alpha.recon.doe.assessment.repository.domain.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User map(final UserDto dto) {
        final User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setNote(dto.getNote());
        user.setCreatedAt(dto.getCreatedAt());

        return user;
    }

    public UserDto map(final User user) {
        final UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setNote(user.getNote());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }
}
