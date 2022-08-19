package com.underdogCounty.underdogCountyProject.domain.application.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {
    @Id @GeneratedValue
    private Long id;
    @Column(length = 20,nullable = false)
    private String name;
    @Column(length = 50,nullable = false)
    private String email;
    @Column(length = 20,nullable = false)
    private String phonenumber;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Builder
    public Application(Long id, String name, String email, String phonenumber, String contents){
        this.id=id;
        this.name=name;
        this.email=email;
        this.phonenumber=phonenumber;
        this.contents=contents;
    }
}
