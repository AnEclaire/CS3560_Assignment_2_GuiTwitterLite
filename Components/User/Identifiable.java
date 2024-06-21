package Components.User;

import java.util.UUID;

interface Identifiable {
    UUID getId();
    UUID generateId();
    void setInGroup(boolean inGroup);
    boolean getInGroup();
    String getName();
}
