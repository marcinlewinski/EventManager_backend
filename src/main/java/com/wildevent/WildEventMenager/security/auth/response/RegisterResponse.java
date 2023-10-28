package com.wildevent.WildEventMenager.security.auth.response;

import com.wildevent.WildEventMenager.location.model.Location;
import com.wildevent.WildEventMenager.role.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String name;
    private String phone;
    private String email;
    private Set<Role> roles;
    private List<Location> locations;
}
