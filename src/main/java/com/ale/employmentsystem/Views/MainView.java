package com.ale.employmentsystem.Views;

import com.ale.employmentsystem.Entities.Employee;
import com.ale.employmentsystem.Exceptions.EmployeeEmptyFieldsException;
import com.ale.employmentsystem.Exceptions.EmployeeInvalidDataException;
import com.ale.employmentsystem.Services.EmployeeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;

import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final EmployeeService employeeService;
    private List<Employee> employeeList;
    private Grid<Employee> employeesGrid;
    private FormLayout form;
    private Button save, delete;
    private HorizontalLayout saveDeleteButtons;

    @Autowired
    public MainView(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.employeeList = this.employeeService.getEmployees();
        this.employeesGrid = new Grid<>(Employee.class, false);
        this.form = new FormLayout();
        this.save = new Button("Save");
        this.delete = new Button("Delete");
        this.saveDeleteButtons = new HorizontalLayout(save, delete);

        createGrid();
        displayAppName();
        displayMenu();

    }

    private void createGrid() {
        employeesGrid.addColumn(Employee::getId).setHeader("Id");
        employeesGrid.addColumn(Employee::getFirstName).setHeader("First name");
        employeesGrid.addColumn(Employee::getMiddleName).setHeader("Middle name");
        employeesGrid.addColumn(Employee::getLastName).setHeader("Last name");
        employeesGrid.addColumn(Employee::getBirthDate).setHeader("Birth date");
        employeesGrid.addColumn(Employee::getPosition).setHeader("Position");
        employeesGrid.setItems(employeeList);
    }

    private void displayAppName() {
        TextField center = new TextField();
        center.setReadOnly(true);
        center.setWidth("400px");
        center.setValue("Employment Management System app :)");
        center.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        add(center);
    }

    private void displayMenu() {
        MenuBar menuBar = new MenuBar();
        MenuItem getEmployeesButton = menuBar.addItem("Get employees");
        MenuItem addEmployee = menuBar.addItem("Add new employee");

        addEmployee.addClickListener(clickEvent -> {
            try {
                addNewEmployee(getEmployeesButton, addEmployee);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        getEmployeesButton.addClickListener(clickEvent -> {
            displayEmployees(getEmployeesButton, addEmployee);
        });
        add(menuBar);
    }

    private void displayEmployees(MenuItem getEmployeesButton, MenuItem addEmployee) {
        remove(this.form);
        remove(this.saveDeleteButtons);
        getEmployeesButton.setEnabled(false);
        addEmployee.setEnabled(true);
        add(employeesGrid);
    }

    private void addNewEmployee(MenuItem getEmployeesButton, MenuItem addEmployee) throws ParseException {
        remove(employeesGrid);
        getEmployeesButton.setEnabled(true);
        addEmployee.setEnabled(false);
        createForm();
    }

    private void createForm() {
        Binder<Employee> binder = new Binder<>(Employee.class);

        this.form = new FormLayout();
        TextField tf1 = new TextField("firstName");
        tf1.setRequiredIndicatorVisible(true);
        form.add(tf1);

        TextField tf2 = new TextField("middleName");
        form.add(tf2);

        TextField tf3 = new TextField("lastName");
        tf3.setRequiredIndicatorVisible(true);
        form.add(tf3);

        DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
        singleFormatI18n.setDateFormat("yyyy-MM-dd");

        DatePicker tf4 = new DatePicker("Select a date:");
        tf4.setI18n(singleFormatI18n);
        form.add(tf4);

        TextField tf5 = new TextField("position");
        tf5.setRequiredIndicatorVisible(true);
        form.add(tf5);
        add(form);
        this.save = new Button("Save");
        this.delete = new Button("Delete");
        this.saveDeleteButtons = new HorizontalLayout(save, delete);
        add(saveDeleteButtons);

        save.addClickListener(event -> {
            try {
                binder.bind(tf1, Employee::getFirstName,
                        Employee::setFirstName);
                binder.bind(tf2, Employee::getMiddleName,
                        Employee::setMiddleName);
                binder.bind(tf3, Employee::getLastName,
                        Employee::setLastName);
                binder.forField(tf4).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                        .bind(Employee::getBirthDate, Employee::setBirthDate);
                binder.bind(tf5, Employee::getPosition,
                        Employee::setPosition);
                if (tf1.isEmpty() || tf3.isEmpty() || tf4.isEmpty() || tf5.isEmpty())
                    throw new EmployeeEmptyFieldsException();

                binder.setBean(new Employee(tf1.getValue(), tf2.getValue(), tf3.getValue(), new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(tf4.getValue())), tf5.getValue()));
                save(binder);
            } catch (ParseException e) {
                throw new EmployeeInvalidDataException();
            }
        });

    }

    private void save(Binder binder) throws ParseException {
        Employee employee = (Employee) binder.getBean();
        employeeService.addEmployee(employee);
        employeeList.add(employee);
        Notification successNotification = Notification.show("The employee has been added successfully!");
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        add(successNotification);
    }

}