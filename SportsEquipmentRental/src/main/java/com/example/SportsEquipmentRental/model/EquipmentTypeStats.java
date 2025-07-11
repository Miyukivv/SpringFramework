package com.example.SportsEquipmentRental.model;

import com.example.SportsEquipmentRental.model.Equipment;
import com.example.SportsEquipmentRental.model.TypeOfEquipment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EquipmentTypeStats {
    private TypeOfEquipment type;
    private long totalCount;
    private long rentedCount;
    private long availableCount;
    private List<Equipment> rentedItems;
}