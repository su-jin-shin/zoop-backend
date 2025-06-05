package com.example.demo.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "complex")
@Getter
@Setter
public class Complex {

    @Id
    @GeneratedValue
    @Column(name =  "complex_id")
    private Long id;

    private Long complex_no;  //실제 단지 번호

    private String complex_name;   //단지 이름


}
