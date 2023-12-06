package org.app.service;

import org.app.repository.UserQueriesRepository;
import org.app.vo.UserQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueriesService {
    @Autowired
    UserQueriesRepository userQueriesRepository;

    public Optional<UserQueries> getQueryById(int id){
        return userQueriesRepository.findById(id);
    }

    public List<UserQueries> getQueryByUserId(int id){
        return userQueriesRepository.findByUserId(id);
    }

}
