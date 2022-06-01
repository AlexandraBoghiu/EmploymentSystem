package com.ale.employmentsystem.Exceptions;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class EmployeeInvalidDataException extends RuntimeException{
    public EmployeeInvalidDataException() {
        super("Invalid input fields for the employee." );
        Notification notification = Notification.show("The employee has not been added! Please check the input fields again.");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
