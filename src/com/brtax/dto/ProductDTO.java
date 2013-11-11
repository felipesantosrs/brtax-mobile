package com.brtax.dto;

/**
 *
 * @author Felipe
 */
public class ProductDTO {
    
    String name; 
 
    
    double price; 
    
    double tax; 
    
    double priceFee;
    
    double valueTax;

    public double getValueTax() {
        return valueTax;
    }

    public void setValueTax(double valueTax) {
        this.valueTax = valueTax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getPriceFee() {
        return priceFee;
    }

    public void setPriceFee(double priceFee) {
        this.priceFee = priceFee;
    }
}