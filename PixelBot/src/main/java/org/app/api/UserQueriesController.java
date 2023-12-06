package org.app.api;

import com.google.gson.Gson;
import org.app.service.UserQueriesService;
import org.app.vo.UserQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "userquery")
public class UserQueriesController {

    @Autowired
    UserQueriesService userQueriesService;


    @GetMapping("/getById")
    @CrossOrigin(value = "http://localhost:3000")
    public @ResponseBody HashMap<String, String> getUserQueryById(@RequestParam("id") int id){
        Gson gson = new Gson();
        HashMap<String, String> res = new HashMap<>();

        try{
            Optional<UserQueries> uq = userQueriesService.getQueryById(id);

            res.put("msg", uq.isPresent()?gson.toJson(uq.get()): "no user found with id: "+id);
            res.put("status", "200");
        }catch(Exception e){
            res.put("msg", e.getMessage());
            res.put("status", "400");
        }

        return res;
    }


    @GetMapping("/getByUserId")
    @CrossOrigin(value = "http://localhost:3000")
    public @ResponseBody HashMap<String, String> getQueriesByUser(@RequestParam("userId") int userId){
        Gson gson = new Gson();
        HashMap<String, String> res = new HashMap<>();

        try{

            List<UserQueries> uqs = userQueriesService.getQueryByUserId(userId);
            res.put("msg", gson.toJson(uqs));
            res.put("status", "200");
        }catch(Exception e){
            res.put("msg", e.getMessage());
            res.put("status", "400");
        }

        return res;
    }
}
