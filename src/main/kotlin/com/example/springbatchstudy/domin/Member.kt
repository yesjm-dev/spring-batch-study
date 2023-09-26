package com.example.springbatchstudy.domin

import jakarta.persistence.*

@Table
@Entity(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    var name: String,
    var age: Int
)