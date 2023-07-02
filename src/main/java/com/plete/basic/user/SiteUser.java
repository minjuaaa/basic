package com.plete.basic.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)  //user와 email에 같은값 못넣음
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
}