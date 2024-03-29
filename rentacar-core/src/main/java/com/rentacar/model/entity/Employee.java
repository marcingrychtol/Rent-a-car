package com.rentacar.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String secondName;
    private String phoneNumber;
    private String email;
    private Boolean cancelled = false;
//    private BigDecimal salary;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(
//            name = "employee_task",
//            joinColumns = @JoinColumn(name = "taskId"),
//            inverseJoinColumns = @JoinColumn(name = "employeeId")
//    )
//    private List<Task> tasks;
//
//    public List<Task> getTasks() {
//        if (tasks == null){
//            tasks = new ArrayList<>();
//        }
//        return tasks;
//    }

}
