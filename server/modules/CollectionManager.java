package laba8.laba8.server.modules;

import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.CollectionIsEmptyException;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.InvalidObjectFieldException;
import laba8.laba8.common.exeptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CollectionManager {
    private ConcurrentLinkedDeque<Organization> organizationCollection;

    private final DatabaseManager databaseManager;

    public CollectionManager(DatabaseManager databaseManager) throws SQLException, ConnectionErrorException {
        this.databaseManager = databaseManager;
        loadCollection();
    }


    public ConcurrentLinkedDeque<Organization> getOrganizationCollection() {
        return organizationCollection;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return organizationCollection.getClass().getName();
    }


    public void insertAt(Integer index, Organization el) throws IndexOutOfBoundsException, ConnectionErrorException, SQLException, UserNotFoundException {
        Connection connection = DatabaseManager.getConnection();
        try {
            databaseManager.addOrganization(connection, el);
            LinkedList<Organization> list = new LinkedList<>(organizationCollection);
            list.add(index, el);
            this.organizationCollection = new ConcurrentLinkedDeque<>(list);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Index must be in [0; " + organizationCollection.size() + "]");
        }
        connection.close();
    }

    public String getAllAnnualTurnovers() {
        return organizationCollection.stream().reduce("", (s, p) -> s + (p.getAnnualTurnover() == null ? "" : p.getAnnualTurnover() + "\n"), String::concat);
    }


    public String getCollectionInfo() {
        return "Type: " + collectionType() + "\n" + "Size: " + collectionSize() + "\n" + "Elements type: Organization";
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return organizationCollection.size();
    }

    public void reorder() {
        Object[] array = organizationCollection.toArray();
        Organization[] reversedArray = new Organization[array.length];
        for (int i = 0; i < array.length; i++) {
            reversedArray[i] = (Organization) array[array.length - 1 - i];
        }
        this.organizationCollection = new ConcurrentLinkedDeque<>(Arrays.asList(reversedArray));
    }


    public Organization getLast() {
        return organizationCollection.stream().reduce((first, second) -> second).orElse(null);
    }

    /**
     * @param id ID of the organization.
     * @return An organization by his ID or null if marine isn't found.
     */
    public Organization getById(Integer id) {
        return organizationCollection.stream().filter(organization -> organization.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * @return Organization, who has min employeesCount.
     * @throws CollectionIsEmptyException If collection is empty.
     */
    public Integer minByEmployeesCount() throws CollectionIsEmptyException {
        if (organizationCollection.isEmpty()) throw new CollectionIsEmptyException();

        return organizationCollection.stream().map(Organization::getEmployeesCount)
                .min(Integer::compareTo).get();
    }


    /**
     * @return Collection content or corresponding string if collection is empty.
     */
    public String showCollection() {
        if (organizationCollection.isEmpty()) return "Collection is empty!";
        return organizationCollection.stream().reduce("", (sum, m) -> sum + (m + "\n\n"), (sum1, sum2) -> sum1 + sum2).trim();
    }


    /**
     * Adds a new organization to collection.
     *
     * @param organization A marine to add.
     */
    public void addToCollection(Organization organization) throws ConnectionErrorException, SQLException, UserNotFoundException {
        Connection connection = DatabaseManager.getConnection();

        organizationCollection.add(databaseManager.addOrganization(connection, organization));
        connection.close();
    }

    /**
     * Removes organization from collection.
     *
     * @param organization An organization to remove.
     */
    public void removeFromCollection(Organization organization) throws ConnectionErrorException, SQLException {
        Connection connection = DatabaseManager.getConnection();
        databaseManager.removeOrganization(connection, organization);
        organizationCollection.remove(organization);
        connection.close();
    }

    /**
     * Clears the collection.
     */
    public void clearCollection(User user) throws SQLException, ConnectionErrorException, UserNotFoundException {
        Connection connection = DatabaseManager.getConnection();
        databaseManager.clearOrganization(connection, user);
        organizationCollection.removeIf(el -> el.getCreatedBy().checkUser(user.getUsername(), user.getPassword()));
        connection.close();
    }

    public double getAvgOfAnnualTurnover() {
        return organizationCollection.stream()
                .reduce(0.0, (sum, p) -> sum + (p.getAnnualTurnover() == null ? 0 : p.getAnnualTurnover()), Double::sum) / (organizationCollection.stream()
                .reduce(0, (count, p) -> count + (p.getAnnualTurnover() == null ? 0 : 1), Integer::sum));
    }

    /**
     * Loads the collection from file.
     */
    public void loadCollection() throws ConnectionErrorException, SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            organizationCollection = new ConcurrentLinkedDeque<>();
            ResultSet resultSet = databaseManager.getOrganizationRows(connection);
            while (resultSet.next()) {
                Organization organization = databaseManager.getOrganizationObjectFromResultSetRow(connection, resultSet);
                organizationCollection.add(organization);
            }
        } catch (InvalidObjectFieldException e) {
            ResponseOutputer.append("LoadingCollectionException");
        }
    }
}