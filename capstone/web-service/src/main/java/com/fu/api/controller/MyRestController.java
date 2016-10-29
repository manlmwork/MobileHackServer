package com.fu.api.controller;

import com.fu.database.dao.EmployeeDao;
import com.fu.database.entity.Employee;
import com.fu.notification.service.FCMService;
import com.fu.vision.service.VisionService;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manlm on 8/13/2016.
 */
@RestController
public class MyRestController {

    private final EmployeeDao employeeDao;

    private final FCMService fcmService;

    private final VisionService visionService;

    /**
     * Constructor
     *
     * @param employeeDao
     * @param fcmService
     * @param visionService
     */
    @Autowired
    public MyRestController(EmployeeDao employeeDao, FCMService fcmService, VisionService visionService) {
        this.employeeDao = employeeDao;
        this.fcmService = fcmService;
        this.visionService = visionService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<Employee>> listUser(@RequestParam(name = "username") String username) {
        List<Employee> users = new ArrayList<>();
        users.add(employeeDao.getByUsername(username));
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String send() {
        fcmService.sendNotificationMessage("my title", "my message", "/topics/news");
        return "ok";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(@RequestParam(name = "filePath") String filePath) {
        try {
            Path path = Paths.get(filePath);

            byte[] data = Files.readAllBytes(path);
            List<EntityAnnotation> entityAnnotations = visionService.detectLogo(data, 3);
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
