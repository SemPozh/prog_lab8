package laba6.server.modules;

import laba6.client.validators.*;
import laba6.common.data.Address;
import laba6.common.data.Coordinates;
import laba6.common.data.Organization;
import laba6.common.exeptions.InvalidObjectFieldException;

import java.time.format.DateTimeFormatter;
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
        Integer annualTurnover = annualTurnoverValidator.validate(arr[5]);
        Organization organization;
        organization = new Organization(idValidator.validate(arr[0]), nameValidator.validate(arr[1]), coordinates, creationDateValidator.validate(arr[4]), annualTurnover, employeesCountValidator.validate(arr[6]), organizationTypeValidator.validate(arr[7]), address);
        return organization;
    }

    public static String toCSV(Stack<Organization> collection){
        StringBuilder fileStr = new StringBuilder();
        for (Organization element : collection){
            fileStr.append(element.getId().toString());
            fileStr.append(",");
            fileStr.append(element.getName());
            fileStr.append(",");
            fileStr.append(element.getCoordinates().getX());
            fileStr.append(",");
            if (element.getCoordinates().getY() != Double.MAX_VALUE){
                fileStr.append(element.getCoordinates().getY());
            }
            fileStr.append(",");
            fileStr.append(element.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            fileStr.append(",");
            if (element.getAnnualTurnover() != null){
                fileStr.append(element.getAnnualTurnover());
            }
            fileStr.append(",");
            fileStr.append(element.getEmployeesCount());
            fileStr.append(",");
            fileStr.append(element.getType().name());
            fileStr.append(",");
            if (element.getOfficialAddress() != null){
                fileStr.append(element.getOfficialAddress().getZipCode());
            }
            fileStr.append("\n");
        }
        return fileStr.toString();
    }
}