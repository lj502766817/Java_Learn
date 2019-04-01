package com.lijia.configstyle.model;

import lombok.Data;

@Data
public class Student {

    private Long id;

    private String name;

    private Integer age;

    public Student() {
    }

    public Student(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

}