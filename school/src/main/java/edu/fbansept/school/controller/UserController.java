package edu.fbansept.school.controller;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.school.dao.UserDao;
import edu.fbansept.school.model.User;
import edu.fbansept.school.security.JwtUtils;
import edu.fbansept.school.security.MyUserDetails;
import edu.fbansept.school.security.MyUserDetailsService;
import edu.fbansept.school.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/")
    public String home() {
        return "Le serveur marche bien, mais il n'y a rien a voir sur cette rout : D";
    }

    @PostMapping("/connection")
    public ResponseEntity<String> connection(@RequestBody User user) {
        try {


            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()
                    ));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        MyUserDetails userDetails = (MyUserDetails) myUserDetailsService
                .loadUserByUsername(user.getEmail());

        return new ResponseEntity<>(jwtUtils.generateJwt(userDetails), HttpStatus.OK);
    }

    @GetMapping("/users")
    @JsonView(UserView.class)
    public List<User> getAllUser() {
        return userDao.findAll();
    }


    @GetMapping("/user/{id}")
    @JsonView(UserView.class)
    public ResponseEntity<User> getUserByID(@PathVariable int id) {
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.NOT_FOUND);
    }


    @PostMapping("/admin/user")
    public ResponseEntity<User> saveUser(@RequestBody User user) {

        //Si l'utilisateur fourni n'a pas toutes les informations
        if (user == null || user.getEmail().equals("") || user.getPassword().equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Si l'id est fourni
        if (user.getId() != null) {
            Optional<User> userDatabase = userDao.findById(user.getId());
            //Si il n'existe pas dans la base de donnée
            if (userDatabase.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            //Si l'id est fourni et l'utilisateur existe, c'est donc un update
            user.setPassword(userDatabase.get().getPassword());
            System.out.println(user.getPassword());
            userDao.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        //Si l'id n'est pas fourni c'est donc un create
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }

    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        userDao.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
