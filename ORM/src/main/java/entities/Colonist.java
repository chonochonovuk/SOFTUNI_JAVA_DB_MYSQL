package entities;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.util.Date;

@Entity(name = "colonists")
public class Colonist {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "ucn")
    private String ucn;
    @Column(name = "birth_date")
    private Date birthDate;

    public Colonist() {
    }

    public Colonist(String firstName, String lastName, String ucn, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ucn = ucn;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }
}
