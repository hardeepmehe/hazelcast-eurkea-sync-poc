package com.htec.user.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getUserName(@RequestParam String id) {
        return userService.getUserNameById(id);
    }

    @PutMapping
    public String setUserName(@RequestParam String id, @RequestParam String userName) {
        return userService.setUserName(userName, id);
    }

}
