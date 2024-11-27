    package com.ab.banco.controllers;

    import com.ab.banco.dtos.UserDTO;
    import com.ab.banco.mappers.UserMapper;
    import com.ab.banco.models.User;
    import com.ab.banco.service.AccountService;
    import com.ab.banco.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.stream.Collectors;

    @RestController
    @RequestMapping(value = "/users")
    public class UserController {

        @Autowired
        private UserService userService;
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private AccountService accountService;

        //traemos todos lo usuarios
        @CrossOrigin
        @GetMapping
        public ResponseEntity<List<UserDTO>> getUsers(){
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOS = users.stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
            return ResponseEntity.ok(userDTOS);
        }

        //buscamos por Id
       @CrossOrigin
       @GetMapping(value = "/{id}")
       public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){

            //buscamos el usuario
           User user = userService.getUserById(id);
           //convertimos de entidad a dto
           UserDTO userDTO = userMapper.userToUserDTO(user);
           return ResponseEntity.ok(userDTO);
       }

        //creamos un usuario
        @CrossOrigin
        @PostMapping
        public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
            //convertimos el dto en una entidad
            User user = userMapper.userDTOToUser(userDTO);
            //creamos el usuario
            User saveUser = userService.createUser(user);
            //ahora convertimos el usuario guardado de entidad a dto
            UserDTO savedUserDTO = userMapper.userToUserDTO(saveUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
        }
        //eliminamos un usuario
        @CrossOrigin
        @DeleteMapping(value = "/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable long id) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        //modificamos un usuario
        @CrossOrigin
        @PutMapping(value = "/{id}")
        public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserDTO userDTO){
            //convertimos el dto a entidad
            User user = userMapper.userDTOToUser(userDTO);
            //actualizamos usuario
            User updateUser = userService.updateUser(id,user);
            //convertimos de entidad a dto
            UserDTO updateUserDTO = userMapper.userToUserDTO(updateUser);
            return ResponseEntity.ok(updateUserDTO);
        }

    }