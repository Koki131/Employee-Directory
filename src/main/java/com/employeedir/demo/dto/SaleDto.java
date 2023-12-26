package com.employeedir.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public class SaleDto {


    private int id;
    private LocalDate date;
    private String fullName;

    private BigDecimal amount;


    public SaleDto() {

    }

    public SaleDto(int id, String fullName, BigDecimal amount, LocalDate date) {
        this.id = id;
        this.fullName = fullName;
        this.amount = amount;
        this.date = date;

    }

    public SaleDto(LocalDate date, String fullName, BigDecimal amount) {
        this.date = date;
        this.fullName = fullName;
        this.amount = amount;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }




    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Sales [id=" + id + ", date=" + date + ", amount=" + amount + "]";
    }


}




