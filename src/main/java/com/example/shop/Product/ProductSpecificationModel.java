package com.example.shop.Product;

public class ProductSpecificationModel {
    private String SpecificationName;
    private String SpecificationValue;

    public ProductSpecificationModel(String specificationName, String specificationValue) {
        SpecificationName = specificationName;
        SpecificationValue = specificationValue;
    }

    public String getSpecificationName() {
        return SpecificationName;
    }

    public void setSpecificationName(String specificationName) {
        SpecificationName = specificationName;
    }

    public String getSpecificationValue() {
        return SpecificationValue;
    }

    public void setSpecificationValue(String specificationValue) {
        SpecificationValue = specificationValue;
    }
}
