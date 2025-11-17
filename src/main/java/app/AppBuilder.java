package app;

import data_Access.InMemoryWageDataAccess;
import use_case.WageInteractor;
import use_case.WageUserDataAccessInterface;
import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WagePresenter;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;
import entity.Employee;
import view.manage_employees_wage.ManageWagesView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class AppBuilder {

