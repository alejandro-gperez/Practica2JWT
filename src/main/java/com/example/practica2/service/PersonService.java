package com.example.practica2.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.practica2.dto.LoginRequestDTO;
import com.example.practica2.dto.LoginResponseDTO;
import com.example.practica2.entity.Person;
import com.example.practica2.repository.PersonRepository;
import com.example.practica2.security.JwtService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class PersonService {

    PersonRepository personRepository;
    JwtService jwtService;

    public PersonService(PersonRepository personRepository, JwtService jwtService) {
        this.personRepository = personRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/noauth/login")
    public ResponseEntity<?> getLogin(@RequestBody(required = false) LoginRequestDTO credenciales) {

        if (credenciales == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debes colocar un username y password");
        }

        String username = credenciales.getUserName();
        String password = credenciales.getPassword();

        if ((username == null || username.isBlank()) || (password == null || password.isBlank())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debes colocar un username y password");
        }

        username = username.strip();

        Optional<Person> person = personRepository.findById(username);

        if (person.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username o contraseña incorrectos");

        } else {
            if (person.get().getPassword().equals(password)) {
                LoginResponseDTO respuesta = new LoginResponseDTO();

                respuesta.setToken(jwtService.generateToken(username));
                return ResponseEntity.ok(respuesta);

            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Username o contraseña incorrectos");

            }
        }
    }

    @GetMapping("/auth/hola")
    public String getHola() {
        return "holaMundo";
    }
    
}
