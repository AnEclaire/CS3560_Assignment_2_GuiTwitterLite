package Components.Dialogs;

import Components.User.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.*;

/**
 * @author Emma Gutierrez
 * 
 * User View Dialog class file. Responsible for the entire user view GUI Dialog.
 * Uses a grid for all the buttons and text areas/inputs. Updates different values 
 * as certain interactions like posts and follows are processed.
 */
public class UserViewDialog extends JDialog {

    private User user;
    private List<User> followingUsers;
    private DefaultListModel<String> followingListModel;
    private DefaultListModel<String> newsFeedListModel;
    private JLabel lastUpdatedLabel;

    public UserViewDialog(JFrame parentFrame, User user, Map<UUID, User> users) {
        super(parentFrame, "User View: " + user.getName(), true);
        setSize(400, 600);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.user = user;
        this.followingUsers = new ArrayList<>();
        this.followingListModel = new DefaultListModel<>();
        this.newsFeedListModel = new DefaultListModel<>();

        // Initialize followingListModel with existing followed users' names
        for (User followedUser : user.getFollowing().values()) {
            followingUsers.add(followedUser);
            followingListModel.addElement(followedUser.getName());
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 10 pixel inset
        gbc.fill = GridBagConstraints.BOTH;

        // Formatting timestamps
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdAt = sdf.format(new Date(user.getCreatedTime()));
        String lastUpdated = sdf.format(new Date(user.getUpdatedTime()));

        // Created At Label
        JLabel createdAtLabel = new JLabel("Created at: " + createdAt);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.05;
        gbc.gridwidth = 3;
        add(createdAtLabel, gbc);

        // Last Updated Label
        lastUpdatedLabel = new JLabel("Last Updated: " + lastUpdated);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.05;
        gbc.gridwidth = 3;
        add(lastUpdatedLabel, gbc);

        // User ID TextArea
        JTextArea userIdTextArea = new JTextArea();
        userIdTextArea.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.05;
        gbc.gridwidth = 2;
        add(userIdTextArea, gbc);

        // Follow User Button
        JButton followUserButton = new JButton("Follow User");
        followUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userIdInput = userIdTextArea.getText().trim();
                try {
                    UUID uuid = UUID.fromString(userIdInput);
                    User followedUser = users.get(uuid);

                    if (followedUser != null) {
                        followingUsers.add(followedUser);
                        followedUser.addFollower(user);
                        user.addFollowing(followedUser);
                        followingListModel.addElement(followedUser.getName()); // Update list model

                        // Update the followed user's followers list
                        followedUser.addFollowing(user); 
                    } else {
                        JOptionPane.showMessageDialog(UserViewDialog.this, "User not found.");
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(UserViewDialog.this, "Invalid UUID format.");
                }
                userIdTextArea.setText(""); // Clear text area after adding user
                updateNewsFeed();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.gridwidth = 1;
        add(followUserButton, gbc);

        // Currently Following Title
        JLabel currentlyFollowingLabel = new JLabel("Currently Following");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.05;
        gbc.gridwidth = 3;
        add(currentlyFollowingLabel, gbc);

        // List of users this user follows
        JList<String> followingList = new JList<>(followingListModel);
        JScrollPane followingScrollPane = new JScrollPane(followingList);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.gridwidth = 3;
        add(followingScrollPane, gbc);

        // Input Text Area Box
        JTextArea messageTextArea = new JTextArea();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.weighty = 0.05;
        gbc.gridwidth = 2;
        add(messageTextArea, gbc);

        // Post Message Button
        JButton postMessageButton = new JButton("Post");
        for (ActionListener al : postMessageButton.getActionListeners()) {
            postMessageButton.removeActionListener(al);
        }
        postMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageTextArea.getText().trim();
                if (!message.isEmpty()) {
                    user.addPost(message);
                    updateNewsFeed();
                    updateLastUpdatedTime();
                    messageTextArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(UserViewDialog.this, "Message cannot be empty.");
                }
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.weighty = 0.05;
        gbc.gridwidth = 1;
        add(postMessageButton, gbc);

        // News Feed Title
        JLabel newsFeedLabel = new JLabel("News Feed");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 0.05;
        gbc.gridwidth = 3;
        add(newsFeedLabel, gbc);

        // Newsfeed list 
        JList<String> newsFeedList = new JList<>(newsFeedListModel);
        JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedList);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.gridwidth = 3;
        add(newsFeedScrollPane, gbc);

        updateNewsFeed();
    }

    /**
     * Populates newsfeed with personal posts then followed users posts. Does not currently
     * keep track of post times/sort them as such.
     */
    private void updateNewsFeed() {
        newsFeedListModel.clear();
        // Add user's own posts
        for (String post : user.getNewsFeed()) {
            newsFeedListModel.addElement(user.getName() + ": " + post);
        }
        // Add posts from followed users
        for (User followedUser : followingUsers) {
            for (String post : followedUser.getNewsFeed()) {
                newsFeedListModel.addElement(followedUser.getName() + ": " + post);
            }
        }
    }

    /**
     * Updates the "Last Updated" label with the current time.
     */
    private void updateLastUpdatedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lastUpdated = sdf.format(new Date(user.getUpdatedTime()));
        lastUpdatedLabel.setText("Last Updated: " + lastUpdated);
    }
}