package Components.Admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Components.Dialogs.AddGroupDialog;
import Components.Dialogs.AddUserDialog;
import Components.Dialogs.CountDisplayDialog;
import Components.Dialogs.IDVerifyDialog;
import Components.Dialogs.LastUpdatedDialog;
import Components.Dialogs.UserViewDialog;
import Components.User.User;
import Components.User.UserGroup;
import Components.Util.CustomTreeCellRenderer;

/**
 * Admin Control Panel 
 * @Author Emma Gutierrez
 * 
 * GUI for the main Control Panel for the entire program. Calls different 
 * dialog options for most button presses. Others use method calls that 
 * search/sort through various objects.
 */
public class AdminControlPanel {

    private JTextArea userIdTextArea;
    private JTextArea groupIdTextArea;
    private UUID selectedUserID;
    private String selectedGroupId;
    private int totalUsers;
    private int totalGroups;
    private int totalMessages;
    private JFrame frame;
    private DefaultTreeModel treeModel;
    private UserGroup twitRoot;
    private DefaultMutableTreeNode root;
    private JButton openUserViewButton;
    private Map<UUID, User> users;

    /**
     * Default constructor, initializes total Users to 0, groups to 1. 
     * Initializes the GUI event.
     */
    public AdminControlPanel() {
        totalUsers = 0;
        totalGroups = 1;
        users = new HashMap<>();
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    // ------------------------------ LEFT SIDE PANEL/INIT DEFAULTS ------------------------------ //

    /**
     * draws the main window and populates the leftside tree.
     */
    private void createAndShowGui() {
        // Create the frame
        frame = new JFrame("MiniTwitter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Create a panel to hold the tree view
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.setPreferredSize(new Dimension(320, 0)); // 40% of 800px width
        treePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 10 pixel inset

        // Create the root node and populate default data.
        twitRoot = new UserGroup("root");
        UserGroup cs3560 = new UserGroup("CS3560");
        UserGroup cs3560Session01 = new UserGroup("CS3560Session01");
        cs3560.addUserToGroup(new User("stu1"));
        cs3560.addUserToGroup(new User("stu2"));
        cs3560.addUserToGroup(new User("stu3"));
        cs3560.addUserToGroup(new User("stu4"));
        cs3560Session01.addUserToGroup(new User("stu8"));
        cs3560Session01.addUserToGroup(new User("stu9"));
        cs3560Session01.addUserToGroup(new User("stu10"));
        cs3560.addUserGroupToGroup(cs3560Session01);
        twitRoot.addUserGroupToGroup(cs3560);
        User john = new User("john");
        User bob = new User("bob");
        twitRoot.addUserToGroup(john);
        twitRoot.addUserToGroup(bob);
        twitRoot.addUserToGroup(new User("steve"));
        twitRoot.addUserToGroup(new User("oopstu"));
        twitRoot.addUserToGroup(new User("oopstu2"));
        twitRoot.getUsers().get(john.getId()).addFollowing(bob);
        root = new DefaultMutableTreeNode(twitRoot);
        addGroupNodes(root, twitRoot);

        // Create the tree model and set it to the tree
        treeModel = new DefaultTreeModel(root);
        JTree tree = new JTree(treeModel);

        tree.setCellRenderer(new CustomTreeCellRenderer());

        // Add a tree selection listener to detect selection changes
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if(selectedNode == null) {
                    return;
                }

                // Check if the selected node represents a User or UserGroup
                Object nodeInfo = selectedNode.getUserObject();
                if(nodeInfo instanceof User) {
                    User user = (User) nodeInfo;
                    userIdTextArea.setText("User ID: " + user.getId());
                    groupIdTextArea.setText("Group ID: N/A");
                    openUserViewButton.setEnabled(true);
        
                    for (ActionListener al : openUserViewButton.getActionListeners()) {
                        openUserViewButton.removeActionListener(al);
                    }

                    openUserViewButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new UserViewDialog(frame, user, users).setVisible(true);
                        }
                    });
                } else if(nodeInfo instanceof UserGroup) {
                    UserGroup group = (UserGroup) nodeInfo;
                    groupIdTextArea.setText("Group ID: " + group.getId());
                    userIdTextArea.setText("User ID: N/A");
                    openUserViewButton.setEnabled(false);
                }
            }
        });

        // Add the tree to a scroll pane
        JScrollPane treeScrollPane = new JScrollPane(tree);
        treePanel.add(treeScrollPane, BorderLayout.CENTER);

        // Add the tree panel to the left side of the frame
        frame.getContentPane().add(treePanel, BorderLayout.WEST);

        frame.getContentPane().add(createRightSide(), BorderLayout.CENTER);

        // Set the frame's visibility to true
        frame.setVisible(true);
    }

    // ------------------------------ RIGHT SIDE PANEL ------------------------------ //

    /**
     * All code for the right side of the main window, creates a grid bag,
     * and has listeners for most content.
     * 
     * @return JPanel object that correlates to the entire right half of the window.
     */
    private JPanel createRightSide() {

        // ---------- PANEL ---------- //

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 10 pixel inset
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        
        // ---------- PANEL COMPONENTS/BUTTONS ---------- //

        // User ID text Area
        userIdTextArea = new JTextArea();
        userIdTextArea.setPreferredSize(new Dimension(150, 20));
        userIdTextArea.setEditable(false);
        userIdTextArea.setWrapStyleWord(true);
        userIdTextArea.setLineWrap(true);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridwidth = 1; // Spans 1 Column
        rightPanel.add(userIdTextArea, gbc);
        
        // Add User button
        JButton addUserButton = new JButton("Add User");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        rightPanel.add(addUserButton, gbc);
        
        // Group ID Text Area
        groupIdTextArea = new JTextArea();
        groupIdTextArea.setText("Group ID: N/A");
        userIdTextArea.setText("User ID: N/A");
        groupIdTextArea.setPreferredSize(new Dimension(150, 20));
        groupIdTextArea.setEditable(false);
        groupIdTextArea.setWrapStyleWord(true);
        groupIdTextArea.setLineWrap(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridwidth = 1; // Spans 1 Column
        rightPanel.add(groupIdTextArea, gbc);
        
        // Add Group Button
        JButton addGroupButton = new JButton("Add Group");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        rightPanel.add(addGroupButton, gbc);

        // User/Group ID Verify Button
        JButton verifyIDButton = new JButton("User/Group ID Verify");
        verifyIDButton.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(verifyIDButton, gbc);
        
        // User View Button
        openUserViewButton = new JButton("Open User View");
        openUserViewButton.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        rightPanel.add(openUserViewButton, gbc);
        
        // User Total Button 
        JButton showUserTotalButton = new JButton("Show User Total");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        rightPanel.add(showUserTotalButton, gbc);
        
        // Group Total Button
        JButton showGroupTotalButton = new JButton("Show Group Total");
        gbc.gridx = 1;
        gbc.gridy = 3;
        rightPanel.add(showGroupTotalButton, gbc);
        
        // Total Messages Button
        JButton showMessagesTotalButton = new JButton("Show Messages Total");
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(showMessagesTotalButton, gbc);
        
        // Positive Messages Button
        JButton showPositivePercentageButton = new JButton("Show Positive Percentage");
        gbc.gridx = 1;
        gbc.gridy = 4;
        rightPanel.add(showPositivePercentageButton, gbc);

        // Las Updated Button
        JButton showLastUpdatedButton = new JButton("Last updated");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        rightPanel.add(showLastUpdatedButton, gbc);

        // ---------- LISTENERS ---------- //

        // User Total Listener
        showUserTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserTotalDialog();
            }
        });

        // Group Total Listener
        showGroupTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGroupTotalDialog();
            }
        });

        showMessagesTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessageTotalDialog();
            }
        });

        // Add User Listener
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUserListener();
            }
        });

        // Add Group Listener
        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGroupListener();
            }
        });

        // Add action listener to User/Group ID Verify button
        verifyIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IDVerifyDialog dialog = new IDVerifyDialog(frame, users);
                dialog.setVisible(true);
            }
        });

        // Show Positive Percentage Listener
        showPositivePercentageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPositivePercentageDialog();
            }
        });

        // Add action listener to Last Updated button
        showLastUpdatedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User lastUpdatedUser = null;
                for (User user : users.values()) {
                    if (lastUpdatedUser == null || user.getUpdatedTime() > lastUpdatedUser.getUpdatedTime()) {
                        lastUpdatedUser = user;
                    }
                }
                LastUpdatedDialog lastUpdatedDialog = new LastUpdatedDialog(frame, lastUpdatedUser);
                lastUpdatedDialog.setVisible(true);
            }
        });

        return rightPanel;
    }

    // ------------------------------ HELPER METHODS ------------------------------ //

    /**
     * Helper for initialization of the default values. Used to populate the initial tree and counts.
     */
    private void addGroupNodes(DefaultMutableTreeNode parent, UserGroup group) {
        for(User user : group.getUsers().values()) {
            DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
            parent.add(userNode);
            users.put(user.getId(), user);
            totalUsers += 1;
        }

        for(UserGroup subGroup : group.getGroups().values()) {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(subGroup);
            parent.add(groupNode);
            addGroupNodes(groupNode, subGroup);
            totalGroups += 1;
        }
    }

    /**
     * Listener Helper for the add user button. Adds user to the right position.
     */
    private void addUserListener() {
        AddUserDialog dialog = new AddUserDialog(frame, twitRoot);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String userName = dialog.getUserName();
            UserGroup selectedGroup = dialog.getSelectedGroup();
            if (userName != null && !userName.trim().isEmpty() && selectedGroup != null) {
                User newUser = new User(userName.trim());
                selectedGroup.addUserToGroup(newUser);
                users.put(newUser.getId(), newUser); 
                DefaultMutableTreeNode groupNode = findTreeNode(root, selectedGroup);
                if (groupNode != null) {
                    groupNode.add(new DefaultMutableTreeNode(newUser));
                    treeModel.reload(groupNode);
                    totalUsers += 1;
                }
            }
        }
    }

    /**
     * Listener Helper for the add group button. Adds group to the right position.
     */
    private void addGroupListener() {
        AddGroupDialog dialog = new AddGroupDialog(frame, twitRoot);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String groupName = dialog.getGroupName();
            UserGroup selectedParentGroup = dialog.getSelectedParentGroup();
            if (groupName != null && !groupName.trim().isEmpty() && selectedParentGroup != null) {
                UserGroup newGroup = new UserGroup(groupName.trim());
                selectedParentGroup.addUserGroupToGroup(newGroup);
                DefaultMutableTreeNode parentGroupNode = findTreeNode(root, selectedParentGroup);
                if (parentGroupNode != null) {
                    parentGroupNode.add(new DefaultMutableTreeNode(newGroup));
                    treeModel.reload(parentGroupNode);
                    totalGroups += 1;
                }
            }
        }
    }

    /**
     * Used to return a given position in the tree displayed in the GUI based off of a passed in
     * root node and group node.
     * 
     * @param root Starting position node
     * @param group Destination node
     * @return Returns target node location.
     */
    private DefaultMutableTreeNode findTreeNode(DefaultMutableTreeNode root, UserGroup group) {
        if (root.getUserObject() == group) {
            return root;
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) root.getChildAt(i);
            DefaultMutableTreeNode result = findTreeNode(childNode, group);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Method to count how many items are in each user's news feed.
     * @return A map where key is the user ID and value is the count of news feed items.
     */
    private int countNewsFeedItems() {
        int count = 0;
        for (User user : users.values()) {
            count += user.getNewsFeed().size(); 
        }
        return count;
    }

    /**
     * Calculates how many messages are considered "Positive" based on a pre-defined list of 
     * positive words.
     * 
     * @return Calculated Percentage of amount of positive messages.
     */
    private double calculatePositivePercentage() {
        String[] positiveWords = {"Haha", "lol", "pog", "love", "like", "amazing", "good"};
        Pattern positivePattern = Pattern.compile(String.join("|", positiveWords), Pattern.CASE_INSENSITIVE);

        int totalMessages = 0;
        int positiveMessages = 0;

        for (User user : users.values()) {
            for (String message : user.getNewsFeed()) {
                totalMessages++;
                if (positivePattern.matcher(message).find()) {
                    positiveMessages++;
                }
            }
        }

        return totalMessages == 0 ? 0 : (double) positiveMessages / totalMessages * 100;
    }

    /**
     * Helper method for showing total users.
     */
    private void showUserTotalDialog() {
        String message = "Total Users: " + totalUsers;
        CountDisplayDialog dialog = new CountDisplayDialog(frame, "User Total", message);
        dialog.setVisible(true);
    }

    /**
     * Helper method for showing total groups.
     */
    private void showGroupTotalDialog() {
        String message = "Total Groups: " + totalGroups;
        CountDisplayDialog dialog = new CountDisplayDialog(frame, "Group Total", message);
        dialog.setVisible(true);
    }

    /**
     * Helper method for total messages
     */
    private void showMessageTotalDialog() {
        String message = "Total Messages: " + countNewsFeedItems();
        CountDisplayDialog dialog = new CountDisplayDialog(frame, "Message Total", message);
        dialog.setVisible(true);
    }

    /**
     * Helper method for Positive Percentage
     */
    private void showPositivePercentageDialog() {
        double total = calculatePositivePercentage();
        String str = new DecimalFormat("#.0#").format(total);
        CountDisplayDialog dialog = new CountDisplayDialog(frame,
         "Message Positive Percentage", "Positive Percentage: " + Double.toString(total) + "%");
        dialog.setVisible(true);
    }
}