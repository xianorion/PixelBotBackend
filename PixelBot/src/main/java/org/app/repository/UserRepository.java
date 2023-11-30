package org.app.repository;

import org.app.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    //@Query("SELECT id FROM sys.user u WHERE u.username= :username")
    Iterable<User> findByUsername(String username);

    List<User> findByEmail(String email);
}
