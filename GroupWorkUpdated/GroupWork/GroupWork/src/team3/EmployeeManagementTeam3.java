package team3;

import team1.Employee;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import CustomException.IdNotFound;
import team1.EmployeeManagement;

public class EmployeeManagementTeam3 implements EmployeeManagement {
    private List<Employee> employees;
    private String filename;

    public EmployeeManagementTeam3(String filename) {
        this.filename = filename;
        employees = loadEmployeesFromFile();
    }

    // Implement methods to add, delete, update, and list employees
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
   
    public void addEmployee(Employee employee) {
        employees.add(employee);
        //if(employee.getId() == (employees.isExists()) 
        saveEmployeesToFile();
    }

   
    public void deleteEmployee(int employeeId) {
        employees.removeIf(employee -> employee.getId() == employeeId);
        saveEmployeesToFile();
    }

 
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
            throw new IdNotFound("Employee ID not found.");
        }
        
    }

   
    public List<Employee> listEmployees() {
        for (Employee employee : employees) {
            System.out.println(employee);
          
        }
		return employees;
    }    

    // Implement file operations to read and write employee data
    public void saveEmployeesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (Employee employee : employees) {
                writer.write(employee.getId() + "," + employee.getName() + "," + employee.getSalary());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Employee> loadEmployeesFromFile() {
        List<Employee> loadedEmployees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double salary = Double.parseDouble(parts[2]);
                    loadedEmployees.add(new Employee(id, name, salary));
                }
            }
        } catch (IOException e) {
            // If the file doesn't exist or there's an issue reading it, return an empty list.
            e.printStackTrace();
        }
        return loadedEmployees;
        
    }
    
}
