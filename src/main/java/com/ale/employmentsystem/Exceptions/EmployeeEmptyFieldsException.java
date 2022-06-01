package com.ale.employmentsystem.Exceptions;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class EmployeeEmptyFieldsException extends RuntimeException{
    public EmployeeEmptyFieldsException() {
        super("Empty fields for employee." );
        Notification notification = Notification.show("The employee has not been added! Some required input fields are empty.");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
