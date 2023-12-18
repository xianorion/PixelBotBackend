package org.app.api;

import org.apache.commons.collections.IteratorUtils;
import org.app.Exceptions.UserNotFoundException;
import org.app.service.UserService;
import org.app.utils.ValidatorUtil;
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
    public @ResponseBody ResponseEntity<User> addUser(@RequestParam String username, @RequestParam String email, @RequestParam String preferredVoice, @RequestParam int age) {
        User userSaved = null;
        try {
            userSaved =userService.save(new User(username, age, preferredVoice, email));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        HashMap<String, String> message = new HashMap<>();
        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(userSaved);
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
        if(!ValidatorUtil.isValidEmail(newEmail)){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, newEmail+" is not a valid email", new Exception("Update Failed: Not a valid email"));
        }
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
    public @ResponseBody ResponseEntity<Optional<List<User>>> getUsersAboveAge(@RequestParam("age") int age){
        Optional<List<User>> users = Optional.empty();

        try{
            users = Optional.of(userService.getUsersAboveAge(age));
        }
        catch(UserNotFoundException unf){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_NO_CONTENT, unf.getMessage(), unf);
        }
        catch (Exception e){
            throw new ResponseStatusException(
                    HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_OK).body(users);
    }

}
