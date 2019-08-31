package com.htec.user.userservice;


import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/cache")
public class HazelCastManagementController {

    @Autowired
    HazelcastInstance hazelcastInstance;


    @RequestMapping("/remove")
    public CommandResponse remove(@RequestParam(value = "key") String key) {
        IMap<String, String> map = hazelcastInstance.getMap("users");
        String value = map.remove(key);
        return new CommandResponse(value);
    }

    @RequestMapping("/size")
    public CommandResponse size() {
        IMap<String, String> map = hazelcastInstance.getMap("users");
        int size = map.size();
        return new CommandResponse(Integer.toString(size));
    }


    @RequestMapping("/clear")
    public CommandResponse clear() {

        IMap<String, String> map = hazelcastInstance.getMap("users");
        map.clear();
        return new CommandResponse("Map cleared");
    }
}
