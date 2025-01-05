package com.vrubayka.restapi_sample.controller;

import com.vrubayka.restapi_sample.model.User;
import com.vrubayka.restapi_sample.util.HibernateUtil;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user); // This can throw ConstraintViolationException or other exceptions
            transaction.commit();
            return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            // Catch duplicate email exception
            return new ResponseEntity<>("Duplicate email detected: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Let Spring handle the exception with the GlobalExceptionHandler
            throw new RuntimeException("Failed to create user.", e); // Re-throw exception for global handler
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            // In case any error occurs (e.g., DB issues), it will be handled globally
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
