package Components.Admin;

import Components.User.User;
import Components.User.UserGroup;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class AdminControlPanel {
    public AdminControlPanel() {
        // Ensure GUI creation on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            createAndShowGui();
        });
    }

    private void createAndShowGui() {
        // Create the frame
        JFrame frame = new JFrame("MiniTwitter");
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
        }

        for(UserGroup subGroup : group.getGroups().values()) {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(subGroup);
            parent.add(groupNode);
            addGroupNodes(groupNode, subGroup);
        }
    }

    private JPanel createRightSide() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 10 pixel inset
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components
        
        // Adding components to the right panel
        JTextArea userIdTextArea = new JTextArea("TextArea - User Id");
        userIdTextArea.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(userIdTextArea, gbc);
        
        JButton addUserButton = new JButton("Button - Add User");
        gbc.gridx = 1;
        gbc.gridy = 0;
        rightPanel.add(addUserButton, gbc);
        
        JTextArea groupIdTextArea = new JTextArea("TextArea - Group Id");
        groupIdTextArea.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        rightPanel.add(groupIdTextArea, gbc);
        
        JButton addGroupButton = new JButton("Button - Add Group");
        gbc.gridx = 1;
        gbc.gridy = 1;
        rightPanel.add(addGroupButton, gbc);
        
        JButton openUserViewButton = new JButton("Button - Open User View");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        rightPanel.add(openUserViewButton, gbc);
        
        JButton showUserTotalButton = new JButton("Button - Show User Total");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        rightPanel.add(showUserTotalButton, gbc);
        
        JButton showGroupTotalButton = new JButton("Button - Show Group Total");
        gbc.gridx = 1;
        gbc.gridy = 3;
        rightPanel.add(showGroupTotalButton, gbc);
        
        JButton showMessagesTotalButton = new JButton("Button - Show Messages Total");
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(showMessagesTotalButton, gbc);
        
        JButton showPositivePercentageButton = new JButton("Button - Show Positive Percentage");
        gbc.gridx = 1;
        gbc.gridy = 4;
        rightPanel.add(showPositivePercentageButton, gbc);
        
        return rightPanel;
    }
}