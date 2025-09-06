package com.cmms.plant.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantId implements Serializable {
    private String companyId;
    private String plantId;
}
