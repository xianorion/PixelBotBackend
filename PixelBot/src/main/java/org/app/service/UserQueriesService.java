package org.app.service;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.OutputFormat;
import org.app.repository.UserQueriesRepository;
import org.app.utils.FileUtil;
import org.app.vo.User;
import org.app.vo.UserQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserQueriesService {
    @Autowired
    UserQueriesRepository userQueriesRepository;
    @Autowired
    AWSPollyService awsPollyService;

    public Optional<UserQueries> getQueryById(int id){
        return userQueriesRepository.findById(id);
    }

    public List<UserQueries> getQueryByUserId(int id){
        return userQueriesRepository.findByUserId(id);
    }

    public byte[] getTextToSpeech(String txt, int userId, String voice) throws IOException {
        LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS);
        String uniqueTime = FileUtil.formatDateForFile(time);

        //save audio file reference to DB
        String filename = "vf_"+userId+"_"+uniqueTime+".mp3";

        //set up user query to save to db
        UserQueries uq =  new UserQueries();
        uq.setQuery(txt);
        User u =  new User();
        u.setId(userId);
        uq.setUser(u);
        uq.setS3_filename(filename);
        uq.setDate_initiated(Date.from(time.toInstant(ZoneOffset.UTC)));

        //save to db
        userQueriesRepository.save(uq);

        //get the audio stream
        byte[] speech = awsPollyService.synthesize(txt, filename, voice, OutputFormat.Mp3);
        return speech;
    }

}
