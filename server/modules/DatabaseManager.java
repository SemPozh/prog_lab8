package laba6.server.modules;

import laba6.common.data.*;
import laba6.common.exeptions.IncorrectAuthFileException;
import laba6.common.exeptions.InvalidObjectFieldException;
import laba6.common.exeptions.UserAlreadyExistsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Scanner;



public class DatabaseManager {
    private String username;
    private String password;

    private String authFileName;

    private String url;
    private Connection connection;
    private final String JDBC_DRIVER = "org.postgresql.Driver";

    public DatabaseManager(String url, String authFileName){
        this.url = url;
        this.authFileName = authFileName;
    }


    public String getUsername() throws IncorrectAuthFileException {
        try {
            Scanner scanner = new Scanner(new File(authFileName));
            this.username = scanner.nextLine().split(":")[0];
            scanner.close();
            return username;
        } catch (FileNotFoundException e) {
            throw new IncorrectAuthFileException("Auth file not found!");
        } catch (ArrayIndexOutOfBoundsException e){
            throw new IncorrectAuthFileException("Incorrect auth File!");
        }
    }

    public String getPassword() throws IncorrectAuthFileException {
        try {
            Scanner scanner = new Scanner(new File(authFileName));
            this.password = scanner.nextLine().split(":")[1];
            scanner.close();
            return password;
        } catch (FileNotFoundException e) {
            throw new IncorrectAuthFileException("Auth file not found!");
        } catch (ArrayIndexOutOfBoundsException e){
            throw new IncorrectAuthFileException("Incorrect auth File!");
        }
    }

    public Connection connectToDatabase() throws IncorrectAuthFileException, SQLException {
        this.connection = DriverManager.getConnection(url, getUsername(), getPassword());
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public OrganizationType getOrganizationTypeById(Integer id){
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT type FROM Organization_Type WHERE id="+id);
            if (resultSet.next()){
                return OrganizationType.fromString(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Address getAddressById(Integer id){
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT zip_code FROM Address WHERE id="+id);
            if (resultSet.next()){
                return new Address(resultSet.getString("zip_code"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUserById(Integer id){
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT username, password FROM Users WHERE id="+id);
            if (resultSet.next()){
                return new User(id, resultSet.getString("username"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public Organization getOrganizationObjectFromResultSetRow(ResultSet resultSet){
        try {

            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Integer x = resultSet.getInt("x_coordinate");
            double y = resultSet.getDouble("y_coordinate");
            ZonedDateTime creationDate = resultSet.getTimestamp("creation_date").toInstant().atZone(ZoneId.systemDefault());
            Integer annualTurnover = resultSet.getInt("annual_turnover");
            Integer employeesCount = resultSet.getInt("employees_count");
            OrganizationType organizationType = getOrganizationTypeById(resultSet.getInt("organization_type_id"));
            Address address = getAddressById(resultSet.getInt("address_id"));
            User createdBy = getUserById(resultSet.getInt("user_id"));
            Organization organization = new Organization(id, name, new Coordinates(x, y), creationDate, employeesCount, organizationType, createdBy);

            organization.setAnnualTurnover(annualTurnover);
            organization.setOfficialAddress(address);
            return organization;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidObjectFieldException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User authorizeUser(String username, String password){
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Users WHERE username="+username+"pasword="+password);
            if (resultSet.next()){
                return new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User createUser(String username, String password) throws UserAlreadyExistsException{
        try {
            int result = connection.createStatement().executeUpdate("INSERT INTO Users VALUES ("+ "'"+username+"'" +"," + "'"+password+"'" + ")");
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Users WHERE username="+"'"+username+"'");
            return new User(resultSet.getInt("id"), username, password);
        } catch (SQLException e) {
            System.out.print(e);
            throw new UserAlreadyExistsException("User with this username already exists");
        }
    }
}
