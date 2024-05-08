package laba6.common.data;

import laba6.common.exeptions.InvalidObjectFieldException;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Organization implements Comparable, Serializable {
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private java.time.ZonedDateTime creationDate;
    private Integer annualTurnover;
    private Integer employeesCount;
    private OrganizationType type;
    private Address officialAddress = null;

    public void setId() {
        this.id = generateID();
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setName(String name) throws InvalidObjectFieldException {
        if (!name.isEmpty()){
            this.name = name;
        } else {
            throw new InvalidObjectFieldException("Organization name can't be empty");
        }
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate() {
        this.creationDate = generateDateTime();
    }

    public void setCreationDate(ZonedDateTime creationDate){
        this.creationDate = creationDate;
    }

    public void setAnnualTurnover(Integer annualTurnover) throws InvalidObjectFieldException {
        if (annualTurnover!=null){
            if (annualTurnover > 0){
                this.annualTurnover = annualTurnover;
            } else {
                throw new InvalidObjectFieldException("Field annual turnover must be positive");
            }
        }
    }

    public void setEmployeesCount(Integer employeesCount) throws InvalidObjectFieldException {
        if (employeesCount > 0){
            this.employeesCount = employeesCount;
        } else {
            throw new InvalidObjectFieldException("Field employees count must be positive");
        }

    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    public Organization(String name, Coordinates coordinates, Integer annualTurnover, Integer employeesCount, OrganizationType organizationType, Address officialAddress) throws InvalidObjectFieldException {
        setId();
        setCreationDate();
        setName(name);
        setCoordinates(coordinates);
        setAnnualTurnover(annualTurnover);
        setEmployeesCount(employeesCount);
        setType(organizationType);
        setOfficialAddress(officialAddress);
    }

    public Organization(String name, Coordinates coordinates, Integer employeesCount, OrganizationType organizationType, Address officialAddress) throws InvalidObjectFieldException {
        setId();
        setCreationDate();
        setName(name);
        setCoordinates(coordinates);
        setEmployeesCount(employeesCount);
        setType(organizationType);
        setOfficialAddress(officialAddress);
    }

    public Organization(String name, Coordinates coordinates, Integer annualTurnover, Integer employeesCount, OrganizationType organizationType) throws InvalidObjectFieldException {
        setId();
        setCreationDate();
        setName(name);
        setCoordinates(coordinates);
        setAnnualTurnover(annualTurnover);
        setEmployeesCount(employeesCount);
        setType(organizationType);
    }

    public Organization(String name, Coordinates coordinates, Integer employeesCount, OrganizationType organizationType) throws InvalidObjectFieldException {
        setId();
        setCreationDate();
        setName(name);
        setCoordinates(coordinates);
        setEmployeesCount(employeesCount);
        setType(organizationType);
    }

    public Organization(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer annualTurnover, Integer employeesCount, OrganizationType organizationType, Address officialAddress) throws InvalidObjectFieldException {
        setId(id);
        setCreationDate(creationDate);
        setName(name);
        setCoordinates(coordinates);
        setAnnualTurnover(annualTurnover);
        setEmployeesCount(employeesCount);
        setType(organizationType);
        setOfficialAddress(officialAddress);
    }

    public Organization(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer employeesCount, OrganizationType organizationType, Address officialAddress) throws InvalidObjectFieldException {
        setId(id);
        setCreationDate(creationDate);
        setName(name);
        setCoordinates(coordinates);
        setEmployeesCount(employeesCount);
        setType(organizationType);
        setOfficialAddress(officialAddress);
    }

    public Organization(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer annualTurnover, Integer employeesCount, OrganizationType organizationType) throws InvalidObjectFieldException {
        setId(id);
        setCreationDate(creationDate);
        setName(name);
        setCoordinates(coordinates);
        setAnnualTurnover(annualTurnover);
        setEmployeesCount(employeesCount);
        setType(organizationType);
    }

    public Organization(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer employeesCount, OrganizationType organizationType) throws InvalidObjectFieldException {
        setId(id);
        setCreationDate(creationDate);
        setName(name);
        setCoordinates(coordinates);
        setEmployeesCount(employeesCount);
        setType(organizationType);
    }



    private Integer generateID(){
        return Math.abs(generateDateTime().hashCode() * 150 - 500);
    }

    private ZonedDateTime generateDateTime(){
        return ZonedDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getAnnualTurnover() {
        return annualTurnover;
    }

    public Integer getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    @Override
    public String toString() {
        return "ID: "+ getId() + ", NAME: " + getName() + ", COORDINATES: (" + getCoordinates().getX() + "; " + getCoordinates().getY() + ")" + ", CREATION DATE: " + getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    /** Comparing by distance between organization and ITMO University*/
    @Override
    public int compareTo(Object o) {
        try {
            Coordinates ITMO = new Coordinates(60,30.310029);
            return (int) (Math.round(Math.sqrt(Math.pow(ITMO.getX() - getCoordinates().getX(), 2) + Math.pow(ITMO.getY() - getCoordinates().getY(), 2))) - (Math.sqrt(Math.pow(ITMO.getX() - ((Organization) o).getCoordinates().getX(), 2) + Math.pow(ITMO.getY() - ((Organization) o).getCoordinates().getY(), 2))));
        } catch (InvalidObjectFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
