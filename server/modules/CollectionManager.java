package laba6.server.modules;

import laba6.common.data.Organization;
import laba6.common.exeptions.CollectionIsEmptyException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Stack;

public class CollectionManager {
    private Stack<Organization> organizationCollection;
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private CollectionFileManager collectionFileManager;

    public CollectionManager(CollectionFileManager collectionFileManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.collectionFileManager = collectionFileManager;

        loadCollection();
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return organizationCollection.getClass().getName();
    }


    public void insertAt(Integer index, Organization el){
        organizationCollection.insertElementAt(el, index);
    }

    public String getAllAnnualTurnovers(){
        return organizationCollection.stream().reduce("", (s, p)->s += String.valueOf(p.getAnnualTurnover()) + "\n", String::concat);
    }


    public String getCollectionInfo(){
        return "Type: " + collectionType() + "\n" + "Size: " + collectionSize() + "\n" + "Elements type: Organization" + "\n" + "Last save time: " + lastSaveTime;
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

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public Organization getFirst() {
        return organizationCollection.stream().findFirst().orElse(null);
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
     * @param organizationToFind An organization whose value will be found.
     * @return An organization by his value or null if organization isn't found.
     */
    public Organization getByValue(Organization organizationToFind) {
        return organizationCollection.stream().filter(marine -> marine.equals(organizationToFind)).findFirst().orElse(null);
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
        return organizationCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }




    /**
     * Adds a new organization to collection.
     *
     * @param organization A marine to add.
     */
    public void addToCollection(Organization organization) {
        organizationCollection.add(organization);
    }

    /**
     * Removes organization from collection.
     *
     * @param organization An organization to remove.
     */
    public void removeFromCollection(Organization organization) {
        organizationCollection.remove(organization);
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        organizationCollection.clear();
    }

    /**
     * Saves the collection to file.
     */
    public void saveCollection() {
        collectionFileManager.writeCollection(organizationCollection);
        lastSaveTime = LocalDateTime.now();
    }

    public double getAvgOfAnnualTurnover(){
        return organizationCollection.stream()
                .reduce(0.0, (sum, p) -> sum += p.getAnnualTurnover(), Double::sum)/collectionSize();
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection() {
        organizationCollection = collectionFileManager.readCollection();
        lastInitTime = LocalDateTime.now();
    }
}