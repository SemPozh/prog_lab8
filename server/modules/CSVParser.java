package laba6.server.modules;

import laba6.client.validators.*;
import laba6.common.data.Address;
import laba6.common.data.Coordinates;
import laba6.common.data.Organization;
import laba6.common.exeptions.InvalidObjectFieldException;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Stack;

/** Class to parse CSV data */
public class CSVParser {
    public static Organization parseCSVString(String csvString) throws InvalidObjectFieldException {
        String[] arr = new String[9];
        String[] data = csvString.split(",");

        for (int i=0; i<8; i++){
            arr[i] = data[i].trim();
        }
        if (data.length == 9){
            arr[8] = data[8].trim();
        } else{
            arr[8] = "";
        }
        IDValidator idValidator = new IDValidator();
        NameValidator nameValidator = new NameValidator();
        XCoordinateValidator xCoordinateValidator = new XCoordinateValidator();
        YCoordinateValidator yCoordinateValidator = new YCoordinateValidator();
        CreationDateValidator creationDateValidator = new CreationDateValidator();
        AnnualTurnoverValidator annualTurnoverValidator = new AnnualTurnoverValidator();
        EmployeeCountValidator employeesCountValidator = new EmployeeCountValidator();
        OrganizationTypeValidator organizationTypeValidator = new OrganizationTypeValidator();
        ZipCodeValidator zipCodeValidator = new ZipCodeValidator();

        Coordinates coordinates = new Coordinates(xCoordinateValidator.validate(arr[2]), yCoordinateValidator.validate(arr[3]));
        Address address = zipCodeValidator.validate(arr[8]);
        Integer annualTurnover = annualTurnoverValidator.validate(arr[3]);
        Organization organization;
        organization = new Organization(idValidator.validate(arr[0]), nameValidator.validate(arr[1]), coordinates, creationDateValidator.validate(arr[4]), annualTurnover, employeesCountValidator.validate(arr[6]), organizationTypeValidator.validate(arr[7]), address);
        return organization;
    }

    public static String toCSV(Stack<Organization> collection){
        String fileStr = "";
        for (Organization element : collection){
            fileStr += element.getId().toString();
            fileStr += ",";
            fileStr += element.getName();
            fileStr += ",";
            fileStr += element.getCoordinates().getX();
            fileStr += ",";
            if (element.getCoordinates().getY() != Double.MAX_VALUE){
                fileStr += element.getCoordinates().getY();
            }
            fileStr += ",";
            fileStr += element.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            fileStr += ",";
            if (element.getAnnualTurnover() != null){
                fileStr += element.getAnnualTurnover();
            }
            fileStr += ",";
            fileStr += element.getEmployeesCount();
            fileStr += ",";
            fileStr += element.getType().name();
            fileStr += ",";
            if (element.getOfficialAddress() != null){
                fileStr += element.getOfficialAddress().getZipCode();
            }
            fileStr += "\n";
        }
        return fileStr;
    }
}