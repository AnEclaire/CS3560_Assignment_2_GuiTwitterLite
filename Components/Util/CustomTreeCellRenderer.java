package Components.Util;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

import Components.User.User;
import Components.User.UserGroup;

import java.awt.*;
import java.net.URL;
import javax.swing.tree.DefaultMutableTreeNode;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

    private Icon userIcon;
    private Icon groupIcon;

    public CustomTreeCellRenderer() {
        userIcon = createScaledIcon("/Icons/user.png", 16, 16);
        groupIcon = createScaledIcon("/Icons/group.png", 16, 16);
    }

    private Icon createScaledIcon(String path, int width, int height) {
        URL iconURL = getClass().getResource(path);
        if (iconURL != null) {
            ImageIcon originalIcon = new ImageIcon(iconURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if (userObject instanceof User) {
                setIcon(userIcon);
            } else if (userObject instanceof UserGroup) {
                setIcon(groupIcon);
            }
        }

        return this;
    }
}
