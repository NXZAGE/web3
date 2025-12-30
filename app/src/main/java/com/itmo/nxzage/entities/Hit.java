package com.itmo.nxzage.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Hit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;
    @DecimalMin("-2.0") @DecimalMax("2.0")
    private double x;
    @NotNull @DecimalMin("-3.0") @DecimalMax("5.0")
    private double y;
    @NotNull @DecimalMin("1.0") @DecimalMax("4.0")
    private double r;
    @NotNull
    private boolean hit;
    @NotNull @PositiveOrZero
    @JsonIgnore
    private long exectime;
    @NotNull @PastOrPresent
    @JsonIgnore
    private LocalDateTime timestamp;
}
