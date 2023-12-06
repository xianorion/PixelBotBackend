package org.app.repository;

import org.app.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //@Query("SELECT id FROM sys.user u WHERE u.username= :username")
    Iterable<User> findByUsername(String username);

    List<User> findByEmail(String email);

    /*
    nativeQuery = true => Native query refers to actual sql queries (referring to actual database objects).
    These queries are the sql statements which can be directly executed in database using a database client.

    The main disadvantage of native queries is complexity of result binding
     */
  @Query(value = "Select u FROM User u WHERE u.age > :ageGiven", nativeQuery = false) //uppercase User is used due to the Entity name being uppercase/equal to the casing of the classname eby default.
    //@Query(value = "Select * FROM user u WHERE u.age > :ageGiven", nativeQuery = true)
    List<User> findUsersOfAge(@Param("ageGiven") int age);
}
