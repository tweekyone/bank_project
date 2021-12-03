package com.epam.clientinterface.domain;

import com.epam.clientinterface.entity.Role;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
@Setter
public class RoleAuth extends Role implements GrantedAuthority {

    public RoleAuth(Long id, @NotBlank @Size(min = 4) String authority) {
        super(id, authority);
    }

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

}
