package Components.Dialogs;

import javax.swing.*;

import Components.User.UserGroup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUserDialog extends JDialog {

    private JTextField userNameField;
    private JComboBox<UserGroup> groupComboBox;
    private boolean confirmed;

    public AddUserDialog(JFrame parent, UserGroup rootGroup) {
        super(parent, "Add User", true); // true makes it modal

        setLayout(new BorderLayout());

        userNameField = new JTextField(15);
        groupComboBox = new JComboBox<>();
        populateGroupComboBox(rootGroup);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("User Name:"));
        inputPanel.add(userNameField);
        inputPanel.add(new JLabel("Select Group:"));
        inputPanel.add(groupComboBox);

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
        groupComboBox.addItem(group);
        for (UserGroup subGroup : group.getGroups().values()) {
            populateGroupComboBox(subGroup);
        }
    }

    public String getUserName() {
        return userNameField.getText();
    }

    public UserGroup getSelectedGroup() {
        return (UserGroup) groupComboBox.getSelectedItem();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
