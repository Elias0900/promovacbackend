package com.promovac.jolivoyage.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BilanVenteDetail {

    LocalDate getVenteTotal();

    BigDecimal getVenteFram();

    BigDecimal getVenteAutre();

    BigDecimal getVenteAssurance();

    BigDecimal getPourcentage();
    BigDecimal getPourcentageFram();
    BigDecimal getPourcentageAss();
    //BigDecimal getPourcentageAutre();

    BigDecimal getPrimeHauteFram();
    BigDecimal getPrimeHauteAssurance();
    BigDecimal getPrimeHauteAutre();
    BigDecimal getPrimeHauteTotal();

    BigDecimal getPrimeBasseFram();
    BigDecimal getPrimeBasseAssurance();
    BigDecimal getPrimeBasseAutre();
    BigDecimal getPrimeBasseTotal();


}
