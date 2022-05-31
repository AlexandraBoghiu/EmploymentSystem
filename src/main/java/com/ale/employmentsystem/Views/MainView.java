package com.ale.employmentsystem.Views;

import com.ale.employmentsystem.Entities.Employee;
import com.ale.employmentsystem.Services.EmployeeService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final EmployeeService employeeService;

    @Autowired
    public MainView(EmployeeService employeeService) {
        this.employeeService = employeeService;
        TextField center = new TextField();
        center.setReadOnly(true);
        center.setValue("Hello! :)");
        center.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        add(center);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        Button getEmployeesButton = new Button("Get employees");
        getEmployeesButton.addClickListener(clickEvent -> {
            displayEmployees(getEmployeesButton);
        });

        Button addEmployee = new Button("Add new employee");
        addEmployee.addClickListener(clickEvent -> {
            try {
                addNewEmployee(addEmployee);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        add(horizontalLayout);
        horizontalLayout.add(getEmployeesButton);
        horizontalLayout.add(addEmployee);

    }

    private void displayEmployees(Button getEmployeesButton) {
        List<Employee> employeeList = this.employeeService.getEmployees();
        Grid<Employee> employeesGrid = new Grid<>(Employee.class, false);

        employeesGrid.addColumn(Employee::getId).setHeader("Id");
        employeesGrid.addColumn(Employee::getFirstName).setHeader("First name");
        employeesGrid.addColumn(Employee::getMiddleName).setHeader("Middle name");
        employeesGrid.addColumn(Employee::getLastName).setHeader("Last name");
        employeesGrid.addColumn(Employee::getBirthDate).setHeader("Birth date");
        employeesGrid.addColumn(Employee::getPosition).setHeader("Position");
        employeesGrid.setItems(employeeList);
        add(employeesGrid);
        getEmployeesButton.setEnabled(false);

    }

    private void addNewEmployee(Button getEmployeesButton) throws ParseException {
        getEmployeesButton.setEnabled(true);
        createAddForm();
    }

    private void createAddForm() throws ParseException {
        Binder<Employee> binder = new Binder<>(Employee.class);

        FormLayout form = new FormLayout();
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

        Button save = new Button("Save");
        Button delete = new Button("Delete");

        HorizontalLayout saveDeleteButtons = new HorizontalLayout(save, delete);
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
                binder.setBean(new Employee(tf1.getValue(), tf2.getValue(), tf3.getValue(), new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(tf4.getValue())), tf5.getValue()));
                save(binder);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void save(Binder binder) throws ParseException {
        Employee employee = (Employee) binder.getBean();
        try {
            employeeService.addEmployee(employee);
        } catch (Exception e) {
            TextField error = new TextField();
            error.setReadOnly(true);
            error.setValue("Couldn't add employee");
            error.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        }
    }

}