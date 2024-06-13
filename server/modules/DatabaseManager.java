package laba8.laba8.server.modules;

import laba8.laba8.common.data.*;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.InvalidObjectFieldException;
import laba8.laba8.common.exeptions.UserAlreadyExistsException;
import laba8.laba8.common.exeptions.UserNotFoundException;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;


public class DatabaseManager {

    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER_CLASS = "driver.class.name";

    private static BasicDataSource dataSource;


    public DatabaseManager(String authFileName) throws ConnectionErrorException {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(authFileName)));
        } catch (IOException e) {
            throw new ConnectionErrorException();
        }
        dataSource = new BasicDataSource();
        dataSource.setUrl(properties.getProperty(DB_URL));
        dataSource.setUsername(properties.getProperty(DB_USERNAME));
        dataSource.setPassword(properties.getProperty(DB_PASSWORD));

    }


    public ResultSet getOrganizationRows(Connection connection) throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM Organization");
    }


    public OrganizationType getOrganizationTypeById(Connection connection, Integer id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT type FROM Organization_Type WHERE id=?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                OrganizationType organizationType = OrganizationType.fromString(resultSet.getString("type"));
                resultSet.close();
                st.close();
                return organizationType;
            }
            resultSet.close();
            st.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Address getAddressById(Connection connection, Integer id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT zip_code FROM Address WHERE id=?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                Address address = new Address(resultSet.getString("zip_code"));
                resultSet.close();
                st.close();
                return address;
            }
            st.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUserById(Connection connection, Integer id) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT username, password FROM Users WHERE id=?");
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                User user = new User(id, resultSet.getString("username"), resultSet.getString("password"));
                resultSet.close();
                st.close();
                return user;
            }
            st.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Organization getOrganizationObjectFromResultSetRow(Connection connection, ResultSet resultSet) throws InvalidObjectFieldException {
        try {

            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Integer x = resultSet.getInt("x_coordinate");
            double y = resultSet.getDouble("y_coordinate");
            ZonedDateTime creationDate = resultSet.getTimestamp("creation_date").toInstant().atZone(ZoneId.systemDefault());
            Integer annualTurnover = resultSet.getInt("annual_turnover");
            if (resultSet.wasNull()){
                annualTurnover = null;
            }
            Integer employeesCount = resultSet.getInt("employees_count");
            OrganizationType organizationType = getOrganizationTypeById(connection, resultSet.getInt("organization_type_id"));
            Address address = getAddressById(connection, resultSet.getInt("address_id"));
            if (resultSet.wasNull()){
                address = null;
            }
            User createdBy = getUserById(connection, resultSet.getInt("user_id"));
            Organization organization = new Organization(id, name, new Coordinates(x, y), creationDate, employeesCount, organizationType, createdBy);

            organization.setAnnualTurnover(annualTurnover);
            organization.setOfficialAddress(address);

            return organization;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void authorizeUser(Connection connection, String username, String password) throws UserNotFoundException, SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM Users WHERE username=? AND password=?");
        st.setString(1, username);
        st.setString(2, PasswordHasher.hashPassword(password));
        ResultSet resultSet = st.executeQuery();
        if (resultSet.next()) {
            new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
        } else {
            st.close();
            resultSet.close();
            throw new UserNotFoundException();
        }
        st.close();
        resultSet.close();
    }

    public void createUser(Connection connection, String username, String password) throws UserAlreadyExistsException {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, PasswordHasher.hashPassword(password));
            int count = ps.executeUpdate();
            System.out.println(PasswordHasher.hashPassword(password));
            ps.close();
        } catch (SQLException e) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
    }

    public User getUserByName(Connection connection, String username) throws UserNotFoundException, SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users WHERE username=?");
        ps.setString(1, username);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            User user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"));
            ps.close();
            resultSet.close();
            return user;
        }
        ps.close();
        resultSet.close();
        throw new UserNotFoundException();
    }


    public Organization addOrganization(Connection connection, Organization organization) throws UserNotFoundException {
        try {
            Integer address_id = addAddress(connection, organization.getOfficialAddress());
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
            ps.setInt(9, getUserByName(connection, organization.getCreatedBy().getUsername()).getId());
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            organization.setId(ps.getGeneratedKeys().getInt(1));
            ps.close();
            generatedKeys.close();
            return organization;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Integer addAddress(Connection connection, Address address) throws SQLException {
        if (address == null) {
            return null;
        } else {
            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO Address (zip_code) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, address.getZipCode());
            ps1.executeUpdate();
            ResultSet generatedKeys = ps1.getGeneratedKeys();
            generatedKeys.next();
            Integer address_id = generatedKeys.getInt(1);
            generatedKeys.close();
            ps1.close();
            return address_id;
        }
    }

    public void clearOrganization(Connection connection, User user) throws SQLException, UserNotFoundException {
        PreparedStatement st = connection.prepareStatement("DELETE FROM Organization WHERE user_id=?");
        st.setInt(1, getUserByName(connection, user.getUsername()).getId());
        st.executeUpdate();
        st.close();

    }

    public void removeOrganization(Connection connection, Organization organization) {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE  FROM Organization WHERE id=?");
            st.setInt(1, organization.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOrganization(Connection connection, Organization oldOrganization, Organization newOrganization) {
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
                st.setInt(7, addAddress(connection, newOrganization.getOfficialAddress()));
            } else {
                st.setNull(7, Types.INTEGER);
            }
            st.setInt(8, oldOrganization.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void closeConnection() throws SQLException {
        dataSource.close();
    }


    public static Connection getConnection() throws ConnectionErrorException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionErrorException();
        }
    }
}
