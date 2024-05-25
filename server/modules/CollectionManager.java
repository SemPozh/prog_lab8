package laba6.server.modules;

import laba6.common.data.Organization;
import laba6.common.data.User;
import laba6.common.exeptions.CollectionIsEmptyException;
import laba6.common.exeptions.InvalidObjectFieldException;
import laba6.common.exeptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionManager {
    private Stack<Organization> organizationCollection;
    private LocalDateTime lastSaveTime;

    private final DatabaseManager databaseManager;
    public CollectionManager(DatabaseManager databaseManager) {
        this.lastSaveTime = null;
        this.databaseManager = databaseManager;

        loadCollection();
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public  DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return organizationCollection.getClass().getName();
    }


    public void insertAt(Integer index, Organization el) throws IndexOutOfBoundsException{
        try {
            databaseManager.addOrganization(el);
            organizationCollection.insertElementAt(el, index);
        } catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundsException("Index must be in [0; "+organizationCollection.size()+"]");
        } catch (UserNotFoundException e) {
            ResponseOutputer.appendln("User in your organization doesn't exist");
        }
    }

    public String getAllAnnualTurnovers(){
        return organizationCollection.stream().reduce("", (s, p)-> s + (p.getAnnualTurnover() == null ? "" : p.getAnnualTurnover() + "\n"), String::concat);
    }


    public String getCollectionInfo(){
        return "Type: " + collectionType() + "\n" + "Size: " + collectionSize() + "\n" + "Elements type: Organization";
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return organizationCollection.size();
    }

    public void reorder(){
        Collections.reverse(organizationCollection);
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
    public void addToCollection(Organization organization) {
        try {
            organizationCollection.add(databaseManager.addOrganization(organization));
        } catch (UserNotFoundException e) {
            ResponseOutputer.appendln("User in your organization not found!");
        }
    }

    /**
     * Removes organization from collection.
     *
     * @param organization An organization to remove.
     */
    public void removeFromCollection(Organization organization) {
        databaseManager.removeOrganization(organization);
        organizationCollection.remove(organization);
    }

    /**
     * Clears the collection.
     */
    public void clearCollection(User user) throws SQLException {
        databaseManager.clearOrganization(user);
        organizationCollection.clear();
    }

    /**
     * Saves the collection to file.
     */
//    public void saveCollection() {
//        collectionFileManager.writeCollection(organizationCollection);
//        lastSaveTime = LocalDateTime.now();
//    }

    public double getAvgOfAnnualTurnover(){
        return organizationCollection.stream()
                .reduce(0.0, (sum, p) -> sum + (p.getAnnualTurnover() == null ? 0 : p.getAnnualTurnover()), Double::sum)/(organizationCollection.stream()
                .reduce(0, (count, p) -> count + (p.getAnnualTurnover() == null ? 0 : 1), Integer::sum));
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection() {
        organizationCollection = new Stack<>();
        try {
            Statement statement = databaseManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Organization");
            while (resultSet.next()){
                Organization organization = databaseManager.getOrganizationObjectFromResultSetRow(resultSet);
                organizationCollection.add(organization);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidObjectFieldException e) {
            ResponseOutputer.appendln("Unexpected error while loading collection!");
        }
    }
}