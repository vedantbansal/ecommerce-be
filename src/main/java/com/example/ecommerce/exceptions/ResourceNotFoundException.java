package com.example.ecommerce.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String field;
    String fieldName;
    String resourceName;
    Long fieldId;

    public ResourceNotFoundException(){
    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName){
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.field = field;
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId){
        super(String.format("%s not found with %s: %s", resourceName, field, fieldId));
        this.field = field;
        this.resourceName = resourceName;
        this.fieldId = fieldId;
    }

}
