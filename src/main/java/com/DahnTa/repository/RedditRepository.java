package com.DahnTa.repository;

import com.DahnTa.entity.Reddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedditRepository extends JpaRepository<Reddit, Long> {

}
