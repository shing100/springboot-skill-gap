package com.kingname.core.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime birthday;
    private LocalDateTime createdAt;

}
