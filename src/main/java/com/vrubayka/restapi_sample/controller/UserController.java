package com.vrubayka.restapi_sample.controller;

import com.vrubayka.restapi_sample.model.User;
import com.vrubayka.restapi_sample.util.ErrorResponse;
import com.vrubayka.restapi_sample.util.HibernateUtil;

import java.sql.SQLException;
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
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user); // This can throw ConstraintViolationException or other exceptions
            transaction.commit();
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            // Access the underlying SQLException
            SQLException sqlException = e.getSQLException();

            // Check if it's a unique constraint violation based on SQLState or error code
            if ("23505".equals(sqlException.getSQLState())) {
                // Handle the duplicate email violation
                ErrorResponse errorResponse = new ErrorResponse("Duplicate email detected", null);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Handle other types of constraint violations
            ErrorResponse errorResponse = new ErrorResponse("Constraint violation: " + e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other exceptions
            ErrorResponse errorResponse = new ErrorResponse("Failed to create user.", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User existingUser = session.get(User.class, id);

            if (existingUser == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // Update fields
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());

            session.merge(existingUser);
            transaction.commit();

            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            session.remove(user);
            transaction.commit();

            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
