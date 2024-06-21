package Components.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emma Gutierrez
 * Class if a representation of a User for the MiniTwitter program.
 */
public class User implements Identifiable {
    private final UUID id;
    private final String name;
    private boolean inGroup;
    private Map<UUID, User> followers;
    private Map<UUID, User> following;
    private List<String> newsFeed;

    /**
     * Default constructor
     */
    public User(String name) {
        this.name = name;
        this.inGroup = false;
        this.id = generateId();
        this.followers = new HashMap<>();
        this.following = new HashMap<>();
        this.newsFeed = new ArrayList<>();
    }

    /**
     * Function takes in a {@link User} object, checks if it exists in followers, and adds
     * it to the list if it does not. Returns boolean for either outcome.
     * 
     * @param follower Passed in {@link User} object.
     * @return Conditional boolean. Returns true if successfully added, false if user exists.
     */
    public boolean addFollower(User follower) {
        if(followers.containsKey(follower.getId())) {
            return false;
        }
        followers.put(follower.getId(), follower);
        return true;
    }

    /**
     * Function takes in a {@link User} object, checks if it exists in following, and adds
     * it to the list if it does not. Returns boolean for either outcome.
     * 
     * @param toFollow Passed in {@link User} object.
     * @return Conditional boolean. Returns true if successfully added, false if user exists.
     */
    public boolean addFollowing(User toFollow) {
        if(following.containsKey(toFollow.getId())) {
            return false;
        }
        following.put(toFollow.getId() ,toFollow);
        return true;
    }

    /**
     * Adds a string representation of a post to the newsFeed list.
     * @param post Passed in string representation of a post.
     */
    public void addPost(String post) {
        newsFeed.add(post);
    }  
    
    /**
     * Function returns the entire List of newsfeed posts.
     * @return NewsFeed list container of posts.
     */
    public List<String> getNewsFeed() {
        return newsFeed;
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

    public void setInGroup(boolean inGroup) {
        this.inGroup = inGroup;
    }

    public boolean getInGroup() {
        return this.inGroup;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
