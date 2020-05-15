package entities;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "age")
    private int age;
    @Column(name = "registration_date")
    private Date registrationDate;
    @Column(name = "email")
    private String email;
    @Column(name = "salary")
    private BigDecimal salary;

    public User() {
    }

    public User(String username, String password, int age, Date registrationDate,String email,BigDecimal salary) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.registrationDate = registrationDate;
        this.email = email;
        this.salary = salary;
    }
}
