package com.epam.clientinterface.domain.dto;

import com.epam.clientinterface.domain.RoleAuth;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String email;
    private String name;
    private String surname;
    private Set<RoleAuth> roles;

}
