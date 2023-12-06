package org.app.repository;

import org.app.vo.UserQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQueriesRepository extends JpaRepository<UserQueries, Integer> {

    @Query(value ="select * from user_queries where user_id = :userId", nativeQuery = true)
    public List<UserQueries> findByUserId(@Param("userId") int userId);
}
