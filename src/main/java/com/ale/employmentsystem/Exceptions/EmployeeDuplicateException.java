package com.ale.employmentsystem.Exceptions;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class EmployeeDuplicateException extends RuntimeException{
    public EmployeeDuplicateException() {
        super("Duplicate employee." );
        Notification notification = Notification.show("The employee exists already.");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
