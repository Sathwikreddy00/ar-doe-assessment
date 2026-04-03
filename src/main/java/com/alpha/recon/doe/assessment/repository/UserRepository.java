package com.alpha.recon.doe.assessment.repository;

import com.alpha.recon.doe.assessment.repository.domain.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Long> {
}
