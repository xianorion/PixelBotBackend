package org.app.api;

import com.google.gson.Gson;
import org.apache.commons.collections.IteratorUtils;
import org.app.repository.UserRepository;
import org.app.service.UserService;
import org.app.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.HttpURLConnection;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:3000")
    public Map<String, String> index(){
        HashMap<String,String> message =  new HashMap<>();
        message.put("body", "hello world");
        return message;
    }

    @GetMapping("/user")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody ResponseEntity<User> getUser(@RequestParam int id){
        Optional<User> users = null;
        try{
            users = userService.getUserById(id);

        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }


        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(users.isPresent()?users.get():null);

    }

    @GetMapping("/getUsers")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody ResponseEntity<List<User>> getUsers(){
        List<User> users = null;
        try{
            users = userService.getAllUsers();

        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(users);
    }
    @GetMapping("/getUsersByName")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody ResponseEntity<List<User>> getUsersByName(@RequestParam String username){
        HashMap<String,String> message =  new HashMap<>();
        Gson gsonConverter =  new Gson();
        String error = null;
        Iterable<User> users = null;
        try{
            users = userService.findByUsername(username);

        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(IteratorUtils.toList(users.iterator()));
    }

    @GetMapping("/getUsersByEmail")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody ResponseEntity<List<User>> getUsersByEmail(@RequestParam String email){
        List<User> users = null;
        try{
            users = userService.findByEmail(email);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(users);
    }

    @PostMapping("/addUser")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody ResponseEntity<Map<String, String>>  addUser(@RequestParam String username, @RequestParam String email, @RequestParam String preferredVoice, @RequestParam int age) {
        User newUser = new User();
        String error = null;
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setAge(age);
        newUser.setPreferredVoice(preferredVoice);
        try {
            userService.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        HashMap<String, String> message = new HashMap<>();
        message.put("response", error == null ? "200" : "400");
        message.put("msg", error);
        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(message);
    }

    @DeleteMapping("/deleteUser")
    @CrossOrigin(origins="http://localhost:3000")
    public @ResponseBody ResponseEntity<HashMap<String, String>> removeUserById(int id){
        HashMap<String, String> res = new HashMap<>();
        try{
            userService.deleteById(id);
            res.put("msg", "Success");
            res.put("status", "200");
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(res);
    }

    @PutMapping("/updateEmail")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody ResponseEntity<HashMap<String,String>> updateUserEmail(int id, @RequestParam("email") String  newEmail){
        HashMap<String, String> res = new HashMap<>();
        Optional<User> userFound = userService.getUserById(id);
        if(!userFound.isEmpty()){
            User userObj = userFound.get();
            userObj.setEmail(newEmail);
            userService.save(userObj);
            res.put("msg", "Success");
            res.put("status", "200");
        }else{
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, "No User Found", new Exception("Update Failed: No User Found"));
        }
        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(res);
    }

    @PutMapping("/updatePreferredVoice")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody ResponseEntity<HashMap<String,String>> updateUserPreferredVoice(int id, @RequestParam("preferredVoice") String  newVoice){
        HashMap<String, String> res = new HashMap<>();
        Optional<User> userFound = userService.getUserById(id);
        if(!userFound.isEmpty()){
            User userObj = userFound.get();
            userObj.setPreferredVoice(newVoice);
            userService.save(userObj);
            res.put("msg", "Success");
            res.put("status", "200");
        }else{
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, "No User Found", new Exception("Update Failed: No User Found"));
        }
        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(res);
    }

    @GetMapping("/getUserAboveAge")
    @CrossOrigin(origins="http://localhost:3000/")
    public @ResponseBody ResponseEntity<List<User>> getUsersAboveAge(@RequestParam("age") int age){
        List<User> users = null;
        try{
            users = userService.getUsersAboveAge(age);
            if(users.isEmpty()){
                throw new ResponseStatusException(HttpURLConnection.HTTP_NO_CONTENT, "No users above "+age+" found", new Exception("No users above "+age+" found"));
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }
        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(users);

    }

}
