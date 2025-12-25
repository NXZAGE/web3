package com.itmo.nxzage.beans;

import java.io.Serializable;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

@Named
@ViewScoped
@Data
@NoArgsConstructor
public class GlobalBean implements Serializable {
    String formStatus = "pizda(nichego ne bilo)";
}
