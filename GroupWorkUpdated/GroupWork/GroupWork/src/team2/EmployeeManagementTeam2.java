package team2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import CustomException.IdNotFound;
import team1.CalculateDesignation;
import team1.Employee;
import team1.EmployeeManagement;
import team1.Menu;

public class EmployeeManagementTeam2 implements EmployeeManagement {
    private List<Employee> employees;
    private String filename;

    public EmployeeManagementTeam2(String filename) {
        this.filename = filename;
        employees = loadEmployeesFromFile();
    }

    public boolean isExists(int employeeId) 
    {
    	for(Employee employee: employees) {
    		if (employee.getId()==employeeId)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    @Override
    public void addEmployee(Employee employee) {
    	
    	    // Calculate the designation and set it in the Employee object
    	    String designation = CalculateDesignation.calculateDesignation(employee);
    	    employee.calculateDesignation(designation);
    	    employees.add(employee);
    	    saveEmployeesToFile();    	
    }

    @Override
    public void deleteEmployee(int employeeId) {
        employees.removeIf(employee -> employee.getId() == employeeId);
        saveEmployeesToFile();
    }

    
    @Override
    public void updateEmployee(int employeeId, Employee newEmployee) throws IdNotFound {
        boolean employeeUpdated = false;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employeeId) {
                employees.set(i, newEmployee);
                saveEmployeesToFile();
                employeeUpdated = true;
                break;
            }
        }

       if (!employeeUpdated) {
            throw new IdNotFound("Employee with ID " + employeeId + " not found.");
        }
    }

    @Override
    public List<Employee> listEmployees() {
        for (Employee employee : employees) {
            System.out.println(employee);
          
        }
		return employees;
    }

    // Implement serialization methods
    private void saveEmployeesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Employee> loadEmployeesFromFile() {
        List<Employee> loadedEmployees = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            loadedEmployees = (List<Employee>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If the file doesn't exist or there's an issue reading it, return an empty list.
            e.printStackTrace();
        }
        return loadedEmployees;
    }
}
