package Components.Dialogs;

import javax.swing.*;

import Components.User.UserGroup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddGroupDialog extends JDialog {

    private JTextField groupNameField;
    private JComboBox<UserGroup> parentGroupComboBox;
    private boolean confirmed;

    public AddGroupDialog(JFrame parent, UserGroup rootGroup) {
        super(parent, "Add Group", true); // true makes it modal

        setLayout(new BorderLayout());

        groupNameField = new JTextField(15);
        parentGroupComboBox = new JComboBox<>();
        populateGroupComboBox(rootGroup);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Group Name:"));
        inputPanel.add(groupNameField);
        inputPanel.add(new JLabel("Select Parent Group:"));
        inputPanel.add(parentGroupComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                dispose(); // Close the dialog
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose(); // Close the dialog
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void populateGroupComboBox(UserGroup group) {
        parentGroupComboBox.addItem(group);
        for (UserGroup subGroup : group.getGroups().values()) {
            populateGroupComboBox(subGroup);
        }
    }

    public String getGroupName() {
        return groupNameField.getText();
    }

    public UserGroup getSelectedParentGroup() {
        return (UserGroup) parentGroupComboBox.getSelectedItem();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
