package laba6.common.data;

public enum OrganizationType {
    COMMERCIAL("Commercial"),
    PUBLIC("Public"),
    GOVERNMENT("Government"),
    TRUST("Trust"),
    OJSC("OJSC");

    private final String name;

    OrganizationType(String name){
        this.name = name;
    }
}