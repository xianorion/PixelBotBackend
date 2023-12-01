package org.app.api;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import org.app.vo.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("hello world")));
    }

    @Test
    public void getUser()  throws Exception {
        MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/getUsers").accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200,response.getResponse().getStatus());
        String content =response.getResponse().getContentAsString();
        String dataReturned = new JSONObject(content).get("data").toString();
        Gson g = new Gson();
        User[] userList = g.fromJson(dataReturned, User[].class);
        assertTrue(userList.length > 0);
    }

    @Test
    public void getUserByEmailTestHasOneUserReturned() throws Exception {
        MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/getUsersByEmail").param("email","mackk@gmail.com").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, response.getResponse().getStatus());
        String content = response.getResponse().getContentAsString();
        String dataReturned = new JSONObject(content).get("data").toString();
        Gson g = new Gson();
        User[] userList = g.fromJson(dataReturned, User[].class);
        assertEquals(1, userList.length);
    }

    @Test
    public void getUserByEmailTestHasNoUserReturned() throws Exception {
        MvcResult response = mvc.perform(MockMvcRequestBuilders.get("/getUsersByEmail").param("email","none").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, response.getResponse().getStatus());
        String content = response.getResponse().getContentAsString();
        String dataReturned = new JSONObject(content).get("data").toString();
        Gson g = new Gson();
        User[] userList = g.fromJson(dataReturned, User[].class);
        assertEquals(0, userList.length);
    }
}