package Components.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserGroup implements Identifiable {

    private final UUID gId;
    private final String name;
    private boolean inGroup;
    private Map<UUID, User> users;
    private Map<String, UserGroup> userGroups;

    public UserGroup(String name) {
        this.name = name;
        this.gId = generateId();
        this.users = new HashMap<>();
        this.userGroups = new HashMap<>();
    }

    /**
     * Method generates a UUID for a user then ensures it does not already
     * exist in the global list of users.
     * 
     * @return A validated UUID. Does not exist in list of current users.
     */
    public UUID generateId() {
        UUID uuid = UUID.randomUUID();
        // TODO: code to check global UUIDS

        return uuid;
    }

    /**
     * 
     * @param user
     * @return
     */
    public boolean addUserToGroup(User user) {
        if(users.containsKey(user.getId())) {
            return false;
        }
        users.put(user.getId(), user);
        return true;
    }

     /**
     * 
     * @param user
     * @return
     */
    public boolean addUserGroupToGroup(UserGroup userGroup) {
        if(userGroups.containsKey(userGroup.getName())) {
            return false;
        }
        userGroups.put(userGroup.getName(), userGroup);
        return true;
    }

    public UUID getId() {
        return gId;
    }

    public String getName() {
        return this.name;
    }

    public void setInGroup(boolean inGroup) {
        this.inGroup = inGroup;
    }

    public boolean getInGroup() {
       return inGroup;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public Map<String, UserGroup> getGroups() {
        return this.userGroups;
    }

    public Map<UUID, User> getUsers() {
        return this.users;
    }
}
