package Components.Admin;

import Components.Dialogs.CountDisplayDialog;
import Components.User.User;
import Components.Dialogs.CountDisplayDialog;
import Components.User.UserGroup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class AdminControlPanel {

    private JTextArea userIdTextArea;
    private JTextArea groupIdTextArea;
    private UUID selectedUserID;
    private String selectedGroupId;
    private int totalUsers;
    private int totalGroups;
    private JFrame frame;

    public AdminControlPanel() {
        totalUsers = 0;
        totalGroups = 1;
        // Ensure GUI creation on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            createAndShowGui();
        });
    }

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
        UserGroup twitRoot = new UserGroup("root");
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
        twitRoot.addUserToGroup(new User("john"));
        twitRoot.addUserToGroup(new User("bob"));
        twitRoot.addUserToGroup(new User("steve"));
        twitRoot.addUserToGroup(new User("oopstu"));
        twitRoot.addUserToGroup(new User("oopstu2"));
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(twitRoot);
        addGroupNodes(root, twitRoot);

        // Create the tree model and set it to the tree
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        JTree tree = new JTree(treeModel);

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
                } else if(nodeInfo instanceof UserGroup) {
                    UserGroup group = (UserGroup) nodeInfo;
                    groupIdTextArea.setText("Group ID: " + group.getId());
                    userIdTextArea.setText("User ID: N/A");
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

    private void addGroupNodes(DefaultMutableTreeNode parent, UserGroup group) {
        for(User user : group.getUsers().values()) {
            DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
            parent.add(userNode);
            totalUsers += 1;
        }

        for(UserGroup subGroup : group.getGroups().values()) {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(subGroup);
            parent.add(groupNode);
            addGroupNodes(groupNode, subGroup);
            totalGroups += 1;
        }
    }

    private JPanel createRightSide() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 10 pixel inset
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        
        // Adding components to the right panel
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
        
        JButton addUserButton = new JButton("Add User");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        rightPanel.add(addUserButton, gbc);
        
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
        
        JButton addGroupButton = new JButton("Add Group");
        gbc.gridx = 1;
        gbc.gridy = 1;
        rightPanel.add(addGroupButton, gbc);
        
        JButton openUserViewButton = new JButton("Open User View");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        rightPanel.add(openUserViewButton, gbc);
        
        JButton showUserTotalButton = new JButton("Show User Total");
        showUserTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserTotalDialog();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        rightPanel.add(showUserTotalButton, gbc);
        
        JButton showGroupTotalButton = new JButton("Show Group Total");
        showGroupTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGroupTotalDialog();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        rightPanel.add(showGroupTotalButton, gbc);
        
        JButton showMessagesTotalButton = new JButton("Show Messages Total");
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(showMessagesTotalButton, gbc);
        
        JButton showPositivePercentageButton = new JButton("Show Positive Percentage");
        gbc.gridx = 1;
        gbc.gridy = 4;
        rightPanel.add(showPositivePercentageButton, gbc);
        
        return rightPanel;
    }

    private void showUserTotalDialog() {
        String message = "Total Users: " + totalUsers;
        CountDisplayDialog dialog = new CountDisplayDialog(frame, "User Total", message);
        dialog.setVisible(true);
    }

    private void showGroupTotalDialog() {
        String message = "Total Groups: " + totalGroups;
        CountDisplayDialog dialog = new CountDisplayDialog(frame, "Group Total", message);
        dialog.setVisible(true);
    }
}