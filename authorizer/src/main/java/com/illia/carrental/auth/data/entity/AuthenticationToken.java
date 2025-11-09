package com.illia.carrental.auth.data.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
    private Long id;

    @Column
    private String token;

    @Column
    private String seed;

    @Column
    private Long user_id;
}
