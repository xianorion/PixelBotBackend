package org.app.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import org.app.service.UserService;
import org.app.vo.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.junit.jupiter.MockitoExtension;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

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
        MockitoAnnotations.initMocks(this);
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
    public void getUserByEmailWithBadRequestInputReturnsError() throws Exception {
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
}