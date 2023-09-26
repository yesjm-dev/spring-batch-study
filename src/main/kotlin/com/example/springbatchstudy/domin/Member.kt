package com.example.springbatchstudy.domin

import jakarta.persistence.*

@Table(name = "Member")
@Entity
class Member(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "name")
    var name: String,
    @Column(name = "age")
    var age: Int
)