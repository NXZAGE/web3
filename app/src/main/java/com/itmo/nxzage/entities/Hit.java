package com.itmo.nxzage.entities;

import java.time.LocalDateTime;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @DecimalMin("-2.0") @DecimalMax("2.0")
    double x;
    @NotNull @DecimalMin("-3.0") @DecimalMax("5.0")
    double y;
    @NotNull @DecimalMin("1.0") @DecimalMax("4.0")
    double r;
    @NotNull
    boolean hit;
    @NotNull @PositiveOrZero
    long exectime;
    @NotNull @PastOrPresent
    LocalDateTime timestamp;
}
