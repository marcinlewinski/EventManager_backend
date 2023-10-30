package com.wildevent.WildEventMenager.security.auth.response;

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
    private String id;
    private String name;
    private String phone;
    private String email;
    private Set<String> roles;
    private List<String> locations;
}
