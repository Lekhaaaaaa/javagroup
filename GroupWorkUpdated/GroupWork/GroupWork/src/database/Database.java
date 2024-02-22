package database;

import java.sql.Date;


import team1.EmployeeManagement;
import team1.Employee;
import team1.Menu;


import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import CustomException.IdNotFound;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;



public class Database implements EmployeeManagement{
		   final Logger logger = LogManager.getLogger(Menu.class);
		   private static Connection connection;
		   private Properties properties;
		   public Database() {
		       initializeDatabaseConnection();
		   }
		   //connecting to the database and the information is in db.properties file
		   private void initializeDatabaseConnection() {
			   Properties properties = new Properties();
		       try {		           
		           //properties.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
		    	   FileInputStream input = new FileInputStream("C:/Users/MSIS/Downloads/GroupWorkUpdated/GroupWork/employee_db.properties");
		           properties.load(input);
		           String connectionUrl = "jdbc:sqlserver://172.16.51.64;" + "databaseName=231047010;encrypt=true;trustServerCertificate=true";
		           String username = properties.getProperty("username");
		           String password = properties.getProperty("password");
		           connection = DriverManager.getConnection(connectionUrl, username, password);
		          // System.out.println("Successfully connected to the database");
		           logger.info("Connection to the database is successful");
		       } catch (Exception e) {
		           e.printStackTrace();
		       }
		   }
	
	
	
	@Override
	public void addEmployee(Employee employee) {
	    try {
	        // Begin transaction
	        connection.setAutoCommit(false);

	        // Insert into Employeemanage table
	        String insertEmployeeSql = "INSERT INTO Employeemanage(employee_id, employee_name, employee_Sal, employee_des) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement employeeStatement = connection.prepareStatement(insertEmployeeSql)) {
	            employeeStatement.setInt(1, employee.getId());
	            employeeStatement.setString(2, employee.getName());
	            employeeStatement.setDouble(3, employee.getSalary());
	            employeeStatement.setString(4, calculateDesignation(employee.getSalary()));
	            int rowsAffected = employeeStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Employee added successfully to the Employeemanage table");
	                logger.info("Employee data added successfully");
	            } else {
	                System.out.println("Failed to add employee to the Employeemanage table");
	            }
	        }

	        // Insert into Personal_details table
	        String insertPersonalDetailsSql = "INSERT INTO Personal_details(employee_id, date_of_birth, email, phone_number) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement personalDetailsStatement = connection.prepareStatement(insertPersonalDetailsSql)) {
	            // Additional methods in the Employee class to retrieve dateOfBirth, email, and phoneNumber
	            personalDetailsStatement.setInt(1, employee.getId());
	            personalDetailsStatement.setDate(2, employee.getDateOfBirth()); 
	            personalDetailsStatement.setString(3, employee.getEmail()); 
	            personalDetailsStatement.setString(4, employee.getPhoneNumber());
	            int rowsAffected = personalDetailsStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Personal details added successfully to the Personal_details table");
	                logger.info("Personal details have been added");
	            } else {
	                System.out.println("Failed to add personal details to the Personal_details table");
	                logger.error("Facing issues adding personal details");
	            }
	        }

	        // Commit the transaction
	        connection.commit();
	    } catch (SQLException e) {
	        // Rollback the transaction in case of any exception
	        try {
	            connection.rollback();
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        // Set auto-commit back to true
	        try {
	            connection.setAutoCommit(true);
	        } catch (SQLException autoCommitException) {
	            autoCommitException.printStackTrace();
	        }
	    }
	}

		
	//Delete Employee from the employee manage table
	@Override
	public void deleteEmployee(int employeeId) {
		   try {
	           String sql = "DELETE FROM Employeemanage WHERE employee_id = ?";
	           try (PreparedStatement statement = connection.prepareStatement(sql)) {
	               statement.setInt(1, employeeId);
	               int rowsAffected = statement.executeUpdate();
	               if (rowsAffected > 0) {
	                   System.out.println("Employee deleted successfully from the database");
	                   logger.info("Employee with the mentioned id has been deleted");
	               } else {
	                   System.out.println("Failed to delete employee from the database");
	                   logger.error("Employee with the id could not be found");
	               }
	           }
	       } catch (SQLException e) {
	           e.printStackTrace();
	       }
	   }
	
	@Override
	
	public void updateEmployee(int employeeId, Employee newEmployee) throws IdNotFound {
	    try {
	        connection.setAutoCommit(false);  // Begin transaction

	        // Update Employeemanage table
	        String updateEmployeeSql = "UPDATE Employeemanage SET employee_name = ?, employee_Sal = ?, employee_des = ? WHERE employee_id = ?";
	        try (PreparedStatement employeeStatement = connection.prepareStatement(updateEmployeeSql)) {
	            employeeStatement.setString(1, newEmployee.getName());
	            employeeStatement.setDouble(2, newEmployee.getSalary());
	            employeeStatement.setString(3, calculateDesignation(newEmployee.getSalary()));
	            employeeStatement.setInt(4, employeeId);
	            int rowsAffected = employeeStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Employee updated successfully in the Employeemanage table");
	            } else {
	                throw new IdNotFound("Employee ID not found.");
	            }
	        }

	        // Update Personal_details table
	        String updatePersonalDetailsSql = "UPDATE Personal_details SET date_of_birth = ?, email = ?, phone_number = ? WHERE employee_id = ?";
	        try (PreparedStatement personalDetailsStatement = connection.prepareStatement(updatePersonalDetailsSql)) {
	            personalDetailsStatement.setDate(1, newEmployee.getDateOfBirth());
	            personalDetailsStatement.setString(2, newEmployee.getEmail());
	            personalDetailsStatement.setString(3, newEmployee.getPhoneNumber());
	            personalDetailsStatement.setInt(4, employeeId);
	            int rowsAffected = personalDetailsStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Personal details updated successfully in the Personal_details table");
	            } else {
	                throw new IdNotFound("Employee ID not found in the Personal_details table.");
	            }
	        }

	        connection.commit();  // Commit the transaction
	    } catch (SQLException e) {
	        // Rollback the transaction in case of any exception
	        try {
	            connection.rollback();
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        // Set auto-commit back to true
	        try {
	            connection.setAutoCommit(true);
	        } catch (SQLException autoCommitException) {
	            autoCommitException.printStackTrace();
	        }
	    }
	}

	//lists personal details of employees
	public List<Employee> listEmployees(boolean includePersonalDetails) {
	    List<Employee> employees = new ArrayList<>();

	    try {
	        String sql;
	        if (includePersonalDetails) {
	            sql = "SELECT E.employee_id, E.employee_name, E.employee_Sal, E.employee_des, " +
	                  "P.date_of_birth, P.email, P.phone_number " +
	                  "FROM Employeemanage E " +
	                  "JOIN Personal_details P ON E.employee_id = P.employee_id";
	        } else {
	            sql = "SELECT * FROM Employeemanage";
	        }

	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            ResultSet rs = statement.executeQuery();
	            while (rs.next()) {
	                int id = rs.getInt("employee_id");
	                String name = rs.getString("employee_name");
	                double salary = rs.getDouble("employee_Sal");
	                String designation = rs.getString("employee_des");

	                Employee employee = new Employee(id, name, salary);

	                if (includePersonalDetails) {
	                    Date dateOfBirth = rs.getDate("date_of_birth");
	                    String email = rs.getString("email");
	                    String phoneNumber = rs.getString("phone_number");

	                    employee.setDateOfBirth(dateOfBirth);
	                    employee.setEmail(email);
	                    employee.setPhoneNumber(phoneNumber);
	                }

	                employees.add(employee);
	                logger.info("Personal details have been retrieved");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return employees;
	}


	   private String calculateDesignation(double salary) {
	       // Logic to determine the designation based on the salary
		   if (salary >= 80000) {
	            return "Senior Manager";
	        } else if (salary >= 60000) {
	            return "Manager";
	        } else if (salary >= 40000) {
	            return "Team Lead";
	        } else {
	            return "Employee";
	        }
	   }
	
	@Override
	//Lists the Employee details
	public List<Employee> listEmployees() {
		// TODO Auto-generated method stub
		return null;
	}
	}

