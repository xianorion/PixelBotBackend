package org.app.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import org.app.service.UserService;
import org.app.vo.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.net.HttpURLConnection;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Autowired
    private MockMvc mvc;

    @Mock
    UserService userService;


    @Before
    public void init(){

//        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getUser()  throws Exception {
        MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/getUsers").accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200,response.getResponse().getStatus());
        MockHttpServletResponse content = response.getResponse();
        String dataReturned = content.getContentAsString();
        Gson g = new Gson();
        User[] userList = g.fromJson(dataReturned, User[].class);
        assertTrue(userList.length > 0);
    }

    @Test
    public void getUsersReturnsSuccessfulWhenUsersAreReceivedWithoutError() throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/getUsers").accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void getUsersReturns400lWhenGetUsersReturnsError() throws Exception{

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulated exception")).when(userService).getAllUsers();
        Executable ex = () -> userController.getUsers();

        assertThrows(ResponseStatusException.class, ex);
    }

    @Test
    public void getUserByNameReturns200WhenRequestCompletesWithNoError() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/getUsersByName").param("username", "Mack").accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void getUserByNameReturns400WhenRequestSentWithNoName() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/getUsersByName").accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(400,mvcResult.getResponse().getStatus());
    }

    @Test
    public void getUserByEmailTestHasOneUserReturned() throws Exception {
        MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/getUsersByEmail").param("email","mackk@gmail.com").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, response.getResponse().getStatus());
        MockHttpServletResponse content = response.getResponse();
        String dataReturned = content.getContentAsString();
        Gson g = new Gson();
        User[] userList = g.fromJson(dataReturned, User[].class);
        assertEquals(1, userList.length);
    }

    @Test
    public void getUserByEmailTestHasNoUserReturned() throws Exception {
        MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/getUsersByEmail").param("email","none").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, response.getResponse().getStatus());
        MockHttpServletResponse content = response.getResponse();
        String dataReturned = content.getContentAsString();
        Gson g = new Gson();
        User[] userList = g.fromJson(dataReturned, User[].class);
        assertEquals(0, userList.length);
    }

    @Test
    public void getUserByEmailWithBadRequestWhenEmailInputIsEmptyAndReturnsError() throws Exception {
        MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/getUsersByEmail").accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(400, response.getResponse().getStatus());

    }

    @Test
    public void deleteUserByEmailReturnsStatus200WhenSuccessful() throws Exception{
        int userId = -1200;
        //mock return
        doNothing().when(userService).deleteById(userId);
        //get response
        MvcResult response =  mvc.perform(MockMvcRequestBuilders.delete("/deleteUser").param("id", String.valueOf(userId))).andReturn();
        ResponseEntity re = userController.removeUserById(userId);

        assertEquals(200, response.getResponse().getStatus());

    }

    @Test
    public void deleteUserByEmailReturnsStatus400WhenFails(){
        int userId = -1200;
        //mock return
        doThrow(ResponseStatusException.class).when(userService).deleteById(userId);
        Executable executable = () -> userController.removeUserById(userId);
        assertThrows(ResponseStatusException.class, executable);
    }

    @Test
    @Transactional
    @Rollback
    public void addUserToDBReturn200ResponseWhenSuccessful() throws Exception {
        String username = "testUser";
        String email = "test@gmail.com";
        String preferredVoice = "Brian";
        int age = 23;


        MvcResult response = mvc.perform(MockMvcRequestBuilders.post("/addUser")
                .param("email", email)
                .param("username", username)
                .param("preferredVoice", preferredVoice)
                .param("age", String.valueOf(age))).andReturn();

        assertEquals(200, response.getResponse().getStatus());

    }

    @Test
    @Transactional
    @Rollback
    public void addUserToDBReturn400ResponseWhenFailure() throws Exception {
        String username = "testUser";
        String email = "test@gmail.com";
        String preferredVoice = "Brian";
        int age = 23;
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulated exception")).when(userService).save(userCaptor.capture());


        Executable executable = () -> userController.addUser(username,email, preferredVoice,age);
        assertThrows(ResponseStatusException.class, executable);
    }

    //getUserAboveName tests

    @Test
    public void getUserAboveAgeReturnsStatus200WhenUsersAreFound() throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/getUserAboveAge").param("age", "0").accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void getUserAboveAgeReturnsStatusNoContentWhenNoUsersAreFound() throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/getUserAboveAge").param("age", "1000000").accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, mvcResult.getResponse().getStatus());
    }

    @Test
    public void getUserAboveAgeReturnsStatus200WhenInvalidAgeInputIsGiven() throws Exception{
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/getUserAboveAge").accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, mvcResult.getResponse().getStatus());
    }


    //updateEmail tests

    public int addTestUserAndGetId() throws Exception{
        String email = "test@hello.net";
        String username = "immaTestUser";
        String age = "20";
        String preferredVoice= "david";

        MvcResult rs = mvc.perform(MockMvcRequestBuilders.post("/addUser").param("username",username)
                .param("email", email)
                .param("preferredVoice", preferredVoice)
                .param("age", age).accept(MediaType.APPLICATION_JSON)).andReturn();
        Gson gson = new Gson();
        User userMade = gson.fromJson(rs.getResponse().getContentAsString(), User.class);
        return userMade.getId();
    }

    @Transactional
    @Rollback
    @Test
    public void updateEmailReturnsStatus200WhenEmailSuccessfullyUpdated() throws Exception{
        String newEmail = "testEmail@Test.com";
        String userId = String.valueOf(addTestUserAndGetId());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/updateEmail").param("id", userId).param("email", newEmail).accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

    }

    @Transactional
    @Rollback
    @Test
    public void updateEmailReturnsStatus400WhenEmailGivenIsInvalid() throws Exception{
        String userId = String.valueOf(addTestUserAndGetId());
        String email = "009";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/updateEmail").param("id", userId).param("email", email).accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Transactional
    @Rollback
    @Test
    public void updateEmailReturnsStatus400WhenNoEmailIsProvided() throws Exception{
        String userId = String.valueOf(addTestUserAndGetId());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/updateEmail").param("id", userId).accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }


    //update preferred voice test
    @Transactional
    @Rollback
    @Test
    public void updatePreferredVoiceReturnsStatus200WhenEmailSuccessfullyUpdated() throws Exception{
        String preferredVoice = "testEmail@Test.com";
        String userId = String.valueOf(addTestUserAndGetId());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/updatePreferredVoice").param("id", userId).param("preferredVoice", preferredVoice).accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

    }
    @Transactional
    @Rollback
    @Test
    public void updatePreferredVoiceReturnsStatus400WhenNoVoiceIsProvided() throws Exception{
        String userId = String.valueOf(addTestUserAndGetId());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/updatePreferredVoice").param("id", userId).accept(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }
}