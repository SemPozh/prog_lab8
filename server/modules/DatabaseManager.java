package laba6.server.modules;

import laba6.common.data.*;
import laba6.common.exeptions.IncorrectAuthFileException;
import laba6.common.exeptions.InvalidObjectFieldException;
import laba6.common.exeptions.UserAlreadyExistsException;
import laba6.common.exeptions.UserNotFoundException;
import laba6.server.commands.Registration;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
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

    public DatabaseManager(String url, String authFileName) {
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
        } catch (ArrayIndexOutOfBoundsException e) {
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
        } catch (ArrayIndexOutOfBoundsException e) {
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

    public OrganizationType getOrganizationTypeById(Integer id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT type FROM Organization_Type WHERE id=?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return OrganizationType.fromString(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Address getAddressById(Integer id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT zip_code FROM Address WHERE id=?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return new Address(resultSet.getString("zip_code"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUserById(Integer id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT username, password FROM Users WHERE id=?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return new User(id, resultSet.getString("username"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Organization getOrganizationObjectFromResultSetRow(ResultSet resultSet) throws InvalidObjectFieldException {
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
        }
    }

    public User authorizeUser(String username, String password) throws UserNotFoundException, SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM Users WHERE username=? AND password=?");
        st.setString(1, username);
        System.out.println(getUserByName(username).getPassword());
        System.out.println(PasswordHasher.hashPassword(password));
        st.setString(2, PasswordHasher.hashPassword(password));
        ResultSet resultSet = st.executeQuery();
        if (resultSet.next()) {
            return new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
        } else {
            throw new UserNotFoundException();
        }
    }

    public User createUser(String username, String password) throws UserAlreadyExistsException {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, PasswordHasher.hashPassword(password));
            ps.executeUpdate();
            return getUserByName(username);
        } catch (SQLException | UserNotFoundException e) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
    }

    public User getUserByName(String username) throws UserNotFoundException {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users WHERE username=?");
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
            }
            throw new UserNotFoundException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Organization addOrganization(Organization organization) throws UserNotFoundException {
        try {

            Integer address_id = addAddress(organization.getOfficialAddress());

            PreparedStatement ps = connection.prepareStatement("INSERT INTO Organization (name, x_coordinate, y_coordinate, creation_date, annual_turnover, employees_count, organization_type_id, address_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, organization.getName());
            ps.setInt(2, organization.getCoordinates().getX());
            ps.setDouble(3, organization.getCoordinates().getY());
            ps.setTimestamp(4, Timestamp.from(organization.getCreationDate().toInstant()));
            if (organization.getAnnualTurnover() == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, organization.getAnnualTurnover());

            }
            ps.setInt(6, organization.getEmployeesCount());
            ps.setInt(7, OrganizationType.getId(organization.getType()));
            if (address_id != null) {
                ps.setInt(8, address_id);
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setInt(9, getUserByName(organization.getCreatedBy().getUsername()).getId());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            organization.setId(ps.getGeneratedKeys().getInt(1));
            return organization;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Integer addAddress(Address address) throws SQLException {
        if (address == null) {
            return null;
        } else {
            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO Address (zip_code) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, address.getZipCode());
            ps1.executeUpdate();

            ResultSet generatedKeys = ps1.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getInt(1);
        }
    }

    public void clearOrganization(User user) throws SQLException {
        PreparedStatement st = connection.prepareStatement("DELETE FROM Organization WHERE user_id=?");
        st.setInt(1, user.getId());
        st.executeUpdate();
    }

    public void removeOrganization(Organization organization) {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE  FROM Organization WHERE id=?");
            st.setInt(1, organization.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOrganization(Organization oldOrganization, Organization newOrganization) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE Organization SET name=?, x_coordinate=?, y_coordinate=?, annual_turnover=?, employees_count=?, organization_type_id=?, address_id=? WHERE id=?");
            st.setString(1, newOrganization.getName());
            st.setInt(2, newOrganization.getCoordinates().getX());
            st.setDouble(3, newOrganization.getCoordinates().getY());
            if (newOrganization.getAnnualTurnover() != null) {
                st.setInt(4, newOrganization.getAnnualTurnover());
            } else {
                st.setNull(4, Types.INTEGER);
            }
            st.setInt(5, newOrganization.getEmployeesCount());
            st.setInt(6, OrganizationType.getId(newOrganization.getType()));
            if (newOrganization.getOfficialAddress() != null) {
                st.setInt(7, addAddress(newOrganization.getOfficialAddress()));
            } else {
                st.setNull(7, Types.INTEGER);
            }
            st.setInt(8, oldOrganization.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
