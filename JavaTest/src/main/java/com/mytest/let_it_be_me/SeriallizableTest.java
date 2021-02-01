package com.mytest.let_it_be_me;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;

public class SeriallizableTest {
    @Test
    public void test_deserializable() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(new Person());
        byte[] personToByteArray = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(personToByteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Person s = (Person) objectInputStream.readObject();

        System.out.println("s: " + s);
    }

    @Test
    public void json_to_object_used_or_not_constructor() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person p = new Person();
        p.setAge(10);
        p.setName("张三");
        p.setAnimal(new Animal().setWeight(200));
        String toJson = objectMapper.writeValueAsString(p);
        Person toObject = objectMapper.readValue(toJson, Person.class);
        System.out.println(toJson);
    }
}

class Person implements Serializable {
    private String name;
    private Integer age;
    private Animal animal;

    Person() {
        System.out.println("Person构造方法被调用");
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Person setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Person setAnimal(Animal animal) {
        this.animal = animal;
        return this;
    }
}

class Animal implements Serializable {
    private Integer weight;

    public Integer getWeight() {
        return weight;
    }

    public Animal setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }
}
