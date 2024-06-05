package laba7.common.data;

import java.util.HashMap;

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

    public static Integer getId(OrganizationType organizationType){
        HashMap<OrganizationType, Integer> hashMap = new HashMap<>();
        hashMap.put(COMMERCIAL, 1);
        hashMap.put(PUBLIC, 2);
        hashMap.put(GOVERNMENT, 3);
        hashMap.put(TRUST, 4);
        hashMap.put(OJSC, 5);

        return hashMap.get(organizationType);
    }

    OrganizationType(String name){
        this.name = name;
    }
}