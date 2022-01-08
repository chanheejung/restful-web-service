package com.example.restfulwebservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*** JPA Service 구현을 위한 Controller, Repository 생성 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
