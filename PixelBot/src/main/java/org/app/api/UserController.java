package org.app.api;

import com.google.gson.Gson;
import org.app.repository.UserRepository;
import org.app.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    @CrossOrigin(origins = "http://localhost:3000")
    public Map<String, String> index(){
        HashMap<String,String> message =  new HashMap<>();
        message.put("body", "hello world");
        return message;
    }

    @GetMapping("/user")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody HashMap<String,String> getUser(@RequestParam int id){
        HashMap<String,String> message =  new HashMap<>();
        Gson gsonConverter =  new Gson();
        String error = null;
        Optional<User> users = null;
        try{
            users = userRepository.findById(id);

        }catch (Exception e){
            error = e.getMessage();
        }

        message.put("response", error==null?"200":"400");
        message.put("data", users.isPresent()?gsonConverter.toJson(users.get()):"");
        message.put("msg", error);
        return message;

    }

    @GetMapping("/getUsers")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody HashMap<String,String> getUsers(){
        HashMap<String,String> message =  new HashMap<>();
        Gson gsonConverter =  new Gson();
        String error = null;
        Iterable<User> users = null;
        try{
            users = userRepository.findAll();

        }catch (Exception e){
            error = e.getMessage();
        }

        message.put("response", error==null?"200":"400");
        message.put("data", gsonConverter.toJson(users));
        message.put("msg", error);
        return message;
    }
    @GetMapping("/getUsersByName")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody HashMap<String,String> getUsersByName(@RequestParam String username){
        HashMap<String,String> message =  new HashMap<>();
        Gson gsonConverter =  new Gson();
        String error = null;
        Iterable<User> users = null;
        try{
            users = userRepository.findByUsername(username);

        }catch (Exception e){
            error = e.getMessage();
        }

        message.put("response", error==null?"200":"400");
        message.put("data", gsonConverter.toJson(users));
        message.put("msg", error);
        return message;
    }

    @GetMapping("/getUsersByEmail")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody HashMap<String,String> getUsersByEmail(@RequestParam String email){
        HashMap<String,String> message =  new HashMap<>();
        Gson gsonConverter =  new Gson();
        String error = null;
        List<User> users = null;
        try{
            users = userRepository.findByEmail(email);
            if(users.size() > 1){
                error = "Multiple users found with email: "+ email;
            }

        }catch (Exception e){
            error = e.getMessage();
        }

        message.put("response", error==null?"200":"400");
        message.put("data", gsonConverter.toJson(users));
        message.put("msg", error);
        return message;
    }

    @PostMapping("/addUser")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody Map<String, String>  addUser(@RequestParam String username, @RequestParam String email, @RequestParam String preferredVoice, @RequestParam int age){
        User newUser = new User();
        String error = null;
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setAge(age);
        newUser.setPreferredVoice(preferredVoice);
        try{
            userRepository.save(newUser);
        }catch(Exception e){
            error = e.getMessage();
        }

        HashMap<String,String> message =  new HashMap<>();
        message.put("response", error==null?"200":"400");
        message.put("msg", error);
        return message;
    }

    @DeleteMapping("/deleteUser")
    @CrossOrigin(origins="http://localhost:3000")
    public @ResponseBody HashMap<String, String> removeUserById(int id){
        HashMap<String, String> res = new HashMap<>();
        try{
            userRepository.deleteById(id);
            res.put("msg", "Success");
            res.put("status", "200");
        }catch(Exception e){
            res.put("msg", e.getMessage());
            res.put("status", "400");
        }

        return res;
    }

    @PutMapping("/updateEmail")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody HashMap<String,String> updateUserEmail(int id, @RequestParam("email") String  newEmail){
        HashMap<String, String> res = new HashMap<>();
        Optional<User> userFound = userRepository.findById(id);
        if(!userFound.isEmpty()){
            User userObj = userFound.get();
            userObj.setEmail(newEmail);
            userRepository.save(userObj);
            res.put("msg", "Success");
            res.put("status", "200");
        }else{
            res.put("msg", "No user found");
            res.put("status", "400");
        }
        return res;
    }

    @PutMapping("/updatePreferredVoice")
    @CrossOrigin(origins = "http://localhost:3000")
    public @ResponseBody HashMap<String,String> updateUserPreferredVoice(int id, @RequestParam("preferredVoice") String  newVoice){
        HashMap<String, String> res = new HashMap<>();
        Optional<User> userFound = userRepository.findById(id);
        if(!userFound.isEmpty()){
            User userObj = userFound.get();
            userObj.setPreferredVoice(newVoice);
            userRepository.save(userObj);
            res.put("msg", "Success");
            res.put("status", "200");
        }else{
            res.put("msg", "No user found");
            res.put("status", "400");
        }
        return res;
    }

}
