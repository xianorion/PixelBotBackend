package org.app.api;

import com.amazonaws.services.polly.model.OutputFormat;
import com.google.gson.Gson;
import org.app.service.AWSPollyService;
import org.app.service.UserQueriesService;
import org.app.vo.UserQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/*
Controller vs RestController?
The return for a RestController is always ResponseBody
 */
@RestController  //swap to restController and fix return values as
@RequestMapping(path = "userquery")
public class UserQueriesController {

    @Autowired
    UserQueriesService userQueriesService;

    @GetMapping("/getById")
    @CrossOrigin(value = "http://localhost:3000")
    public @ResponseBody ResponseEntity<UserQueries> getUserQueryById(@RequestParam("id") int id){
        Optional<UserQueries> uq =null;
        try{

            uq = userQueriesService.getQueryById(id);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(uq.isPresent()?uq.get():null);
    }


    @GetMapping("/getByUserId")
    @CrossOrigin(value = "http://localhost:3000")
    public @ResponseBody ResponseEntity<List<UserQueries>> getQueriesByUser(@RequestParam("userId") int userId){
        List<UserQueries> uqs =null;
        try{

            uqs = userQueriesService.getQueryByUserId(userId);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(uqs);
    }


    /*
    Need to fix return as maybe a file to play instead of input stream
    update deprecated aws s3 calls
     */
    @GetMapping("/getSpeech")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<byte[]> getSpeechFromText(@RequestParam("text") String text, @RequestParam("userId") int userId, @RequestParam("voice") String voice) throws IOException {
        byte[] audio =null;
        try{

            audio =userQueriesService.getTextToSpeech(text,userId, voice);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(audio);
    }

}
