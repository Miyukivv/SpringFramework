package com.example.SportsEquipmentRental.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(columnDefinition = "NUMERIC")
    private double price_per_hour;

    private String name;
    private String brand;
    private String size;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_equipment")
    private  TypeOfEquipment typeOfEquipment;
    private String condition;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group")
    private AgeGroup ageGroup;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;


}
