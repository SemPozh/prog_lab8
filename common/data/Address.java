package laba6.common.data;

public class Address {
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
