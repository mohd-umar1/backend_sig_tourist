package com.tourist.safety.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="Restricted_Zone")
public class Restricted_zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private Double latitude;

    private Double longitude;
}
