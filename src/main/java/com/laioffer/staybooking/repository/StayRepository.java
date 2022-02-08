package com.laioffer.staybooking.repository;

import com.laioffer.staybooking.entity.Stay;
import com.laioffer.staybooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
    List<Stay> findByHost(User user);
    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Long> ids, int guestNumber);
}
// JPA Repositories is used for Relational Database, and if method is meet the requirement of query
// syntax, it will be operated by JPA, no need to write our own function
//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
