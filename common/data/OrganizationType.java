package laba7.common.data;

public enum OrganizationType {
    COMMERCIAL("Commercial"),
    PUBLIC("Public"),
    GOVERNMENT("Government"),
    TRUST("Trust"),
    OJSC("OJSC");

    private final String name;
    public static OrganizationType fromString(String value) throws IllegalArgumentException {
        if (value != null) {
            for (OrganizationType ot : OrganizationType.values()) {
                if (value.equalsIgnoreCase(ot.name)) {
                    return ot;
                }
            }
        }
        throw new IllegalArgumentException("There are no such organization type!");
    }

    OrganizationType(String name){
        this.name = name;
    }
}