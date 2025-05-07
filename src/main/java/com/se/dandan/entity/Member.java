package com.se.dandan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String userId;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private int wordCount;

}
