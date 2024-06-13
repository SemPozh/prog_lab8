package laba8.laba8.common.data;

import java.io.Serializable;

public class Address implements Serializable {
    private String zipCode;

    public Address(String zipCode){
        setZipCode(zipCode);
    }

    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    public String getZipCode(){
        return this.zipCode;
    }
}
