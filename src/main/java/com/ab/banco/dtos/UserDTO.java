package com.ab.banco.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private String telefono;
    private String email;
}
