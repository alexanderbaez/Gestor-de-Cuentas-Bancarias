package com.ab.banco.service;

import com.ab.banco.models.User;
import com.ab.banco.repository.UserRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepositoty userRepositoty;

    //Traemos todos los usuarios
    public List<User> getAllUsers() {
        List<User> users = userRepositoty.findAll();
        //verificamos
        if (users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay usuarios en la lista");
        }
        return users;
    }

    //buscamos por el id
    public User getUserById(Long userId){
        return userRepositoty.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    //creamos un usuario
    public User createUser(User user){
        return userRepositoty.save(user);
    }

    //eliminamos un usuario
    public void deleteUser(long id){
        if (!userRepositoty.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        userRepositoty.deleteById(id);
    }

    //modificamos un usuario
    public User updateUser(long id, User user){

        if (!userRepositoty.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        user.setId(id);
        return userRepositoty.save(user);
    }

    //verificamos si existe un usuario
    public boolean existsById(Long userId) {
        return userRepositoty.existsById(userId);
    }
}