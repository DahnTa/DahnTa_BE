package com.DahnTa.repository;

import com.DahnTa.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserAccount(String userAccount);
}
