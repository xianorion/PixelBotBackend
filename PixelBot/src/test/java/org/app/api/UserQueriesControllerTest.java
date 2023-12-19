package org.app.api;


import com.amazonaws.services.s3control.model.AWSS3ControlException;
import org.app.service.UserQueriesService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserQueriesControllerTest {

    @InjectMocks
    UserQueriesController userQueriesController;

    @Mock
    UserQueriesService userQueriesService;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


   //getById
    @Test
    public void getByIdReturns200StatusWhenNoIssuesOccur() throws Exception{
        String id = "1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/userquery/getById").param("id",id).accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void getByIdReturns400StatusWhenNoIdIsSupplied() throws Exception{
        String id = "1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/userquery/getById").accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());

    }

    @Test
    public void getByIdReturns400StatusWhenErrorHappensDuringDBGetRequest()  {
        int id = 1;
        when(userQueriesService.getQueryById(id)).thenThrow(new JpaSystemException(new RuntimeException()));
        Executable ex = () -> userQueriesController.getUserQueryById(id) ;
        assertThrows(ResponseStatusException.class, ex);
    }

    //getByUserId

    @Test
    public void getByUserIdReturns200StatusWhenNoIssuesOccur() throws Exception{
        String id = "1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/userquery/getByUserId").param("userId",id).accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void getByUserIdReturns400StatusWhenNoIdIsSupplied() throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/userquery/getByUserId").accept(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());

    }

    @Test
    public void getByUserIdReturns400StatusWhenErrorHappensDuringDBGetRequest()  {
        int id = 1;
        when(userQueriesService.getQueryByUserId(id)).thenThrow(new JpaSystemException(new RuntimeException()));
        Executable ex = () -> userQueriesController.getQueriesByUser(id) ;
        assertThrows(ResponseStatusException.class, ex);
    }
    //getSpeech unit tests
    @Test
    public void getSpeechFromTextReturns200StatusWhenCallIsSuccessful() throws IOException {
        String text = "Hello world";
        int userId = 1;
        String voice = "David";
        byte[] audio = new byte[20];
        when(userQueriesService.getTextToSpeech(text, userId,voice)).thenReturn(audio);
        ResponseEntity<byte[]> returnedAudio =  userQueriesController.getSpeechFromText(text,userId,voice) ;
        assertEquals(audio, returnedAudio.getBody());
    }
    @Test
    public void getSpeechFromTextReturns400StatusWhenErrorHappensDuringGetTextToSpeechCall() throws IOException {
        String text = "Hello world";
        int userId = 1;
        String voice = "David";
        when(userQueriesService.getTextToSpeech(text, userId,voice)).thenThrow(new AWSS3ControlException("Error storing in S3 Bucket"));
        Executable ex = () -> userQueriesController.getSpeechFromText(text,userId,voice) ;
        assertThrows(ResponseStatusException.class, ex);
    }

}
