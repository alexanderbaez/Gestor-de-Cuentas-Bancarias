package com.ab.banco.service.interfaces;

import com.ab.banco.persistence.models.User;
import java.util.List;

public interface IUserService {
    //Traemos todos los usuarios
    public List<User> getAllUsers();

    //buscamos por el id
    public User getUserById(Long userId);

    //creamos un usuario
    public User createUser(User user);

    //eliminamos un usuario
    public void deleteUser(long id);

    //modificamos un usuario
    public User updateUser(long id, User user);

    //verificamos si existe un usuario
    public boolean existsById(Long userId);
}

