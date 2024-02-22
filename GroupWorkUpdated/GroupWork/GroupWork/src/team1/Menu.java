package team1;

import database.Database;
import java.util.List;
import java.util.Scanner;
import CustomException.IdNotFound;
import team2.EmployeeManagementTeam2; // Import team2
import team3.EmployeeManagementTeam3; // Import team3
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Menu {
    private static final int NULL = 0;

	public static void main(String[] args) {
		final Logger logger = LogManager.getLogger(Menu.class);
        Scanner scanner = new Scanner(System.in);
      //  int choice;

                while (true) {
                    System.out.println("Menu:");
                    System.out.println("1. Add Employee");
                    System.out.println("2. Delete Employee");
                    System.out.println("3. Update Employee");
                    System.out.println("4. List Employees");
                    System.out.println("5. List Employees with personal details");
                    System.out.println("6. Exit");
                    System.out.print("Enter your choice: ");

                    Database db = new Database();
                    EmployeeManagementTeam2 employeeManager1 = new EmployeeManagementTeam2("C:/Users/MSIS/Downloads/GroupWorkUpdated/GroupWork/GroupWork/employee_data.ser");
                    EmployeeManagementTeam3 employeeManager = new EmployeeManagementTeam3("C:/Users/MSIS/Downloads/GroupWorkUpdated/GroupWork/employee_data.txt");
                    
                    int subChoice = scanner.nextInt();
                    scanner.nextLine(); 

                    switch (subChoice) {
                        case 1:
                        	//Adds employee details based on the user input
                            System.out.print("Enter Employee ID: ");
                            int employeeId = scanner.nextInt();
                            if(employeeManager.isExists(employeeId) || employeeManager1.isExists(employeeId)) {
                            	System.out.println("Employee Id already exists");
                            }
                            else {
                            scanner.nextLine();
                            System.out.print("Enter Employee Name: ");
                            String employeeName = scanner.nextLine();
                            System.out.print("Enter Employee Salary: ");
                            double employeeSalary = scanner.nextDouble();
                            scanner.nextLine();
                            //newly added
                            System.out.println("Enter Date of Birth (yyyy-MM-dd):");
           	                String dateOfBirth = scanner.nextLine();
           	                System.out.println("Enter Email (in xyz@ems.com):");
           	                String email = scanner.nextLine();
           	                System.out.println("Enter Phone Number (+91):");
           	                String phoneNumber = scanner.nextLine();
                            Employee employee = new Employee(employeeId, employeeName, employeeSalary);
                            employeeManager.addEmployee(employee);
                            //ser
                            employeeManager1.addEmployee(employee);

                            String designation = CalculateDesignation.calculateDesignation(employee);
        	                System.out.println("Designation: " + designation);
        	                employee.setDateOfBirth(java.sql.Date.valueOf(dateOfBirth));
            	             employee.setEmail(email);
            	             employee.setPhoneNumber(phoneNumber);
            	             db.addEmployee(employee);

        	                logger.info("Added successfully");
        	                System.out.println("Employee added successfully.");
                       
                            }
                            break;
                        case 2:
                        	//Deletes the Employee details
                            System.out.print("Enter Employee ID to delete: ");
                            int idToDelete = scanner.nextInt();
                            employeeManager.deleteEmployee(idToDelete);
                            employeeManager1.deleteEmployee(idToDelete);
                            db.deleteEmployee(idToDelete);
                            break;
                        case 3:                        	
                        	//Update the employee details based on the id
                        	System.out.print("Enter the ID to update:");
		                     int idToUpdate = scanner.nextInt();
		                     scanner.nextLine(); 
		                     System.out.print("Enter new Name: ");
		                     String newName = scanner.nextLine();
		                     System.out.print("Enter new Salary: ");
		                     double newSalary = scanner.nextDouble();
		                     scanner.nextLine();
		                     System.out.println("Enter new Date of Birth (yyyy-MM-dd):");
		                     String newDateOfBirth = scanner.nextLine();
		                     System.out.println("Enter new Email:");
		                     String newEmail = scanner.nextLine();
		                     System.out.println("Enter new Phone Number:");
		                     String newPhoneNumber = scanner.nextLine();

		                     Employee updatedEmployee = new Employee(idToUpdate, newName, newSalary);
		                     updatedEmployee.setDateOfBirth(java.sql.Date.valueOf(newDateOfBirth));
		                     updatedEmployee.setEmail(newEmail);
		                     updatedEmployee.setPhoneNumber(newPhoneNumber);

		                     try {
		                    	 employeeManager.updateEmployee(idToUpdate, updatedEmployee); 
		                            employeeManager1.updateEmployee(idToUpdate, updatedEmployee); 
		                         db.updateEmployee(idToUpdate, updatedEmployee);
		                         System.out.println("Employee updated successfully.");
		                     } catch (IdNotFound e) {
		                         System.out.println("Error: " + e.getMessage());
		                         logger.error("Unable to update as the id is not found");
		                     }
		                     break;
                            
                        case 4:
                        	//list employee without personal details
                        	 System.out.println("Employees List:");
                        	 System.out.println("Data stored in database");
		                     List<Employee> employeesManagementOnly = db.listEmployees(false);
		                     for (Employee employee1 : employeesManagementOnly) {
		                         System.out.println(employee1);
		                     }
		                     System.out.println("Data stored in file");
		                     for (Employee employee1 : employeesManagementOnly) {
		                         System.out.println(employee1);
		                     }
		                     employeeManager.listEmployees();
		                     System.out.println("Data stored through serialization");
		                     employeeManager1.listEmployees();
		                     for (Employee employee1 : employeesManagementOnly) {
		                         System.out.println(employee1);
		                     }
		                     
		                     logger.info("Listed the employee details");
		                     break;
                            
                            
                        case 5:
                        	//list employee with personal details
		            		 System.out.println("Employees List with Personal Details:");
                        	 System.out.println("Data stored in database");

		                     List<Employee> employeesWithPersonalDetails = db.listEmployees(true);
		                     for (Employee employee1 : employeesWithPersonalDetails) {
		                         System.out.println(employee1);
		                     }
		                     System.out.println("Data stored in file");

		                     employeeManager.listEmployees();
		                     for (Employee employee1 : employeesWithPersonalDetails) {
		                         System.out.println(employee1);
		                     }
		                     System.out.println("Data stored through serialization");

		                     employeeManager1.listEmployees();
		                     for (Employee employee1 : employeesWithPersonalDetails) {
		                         System.out.println(employee1);
		                     }
		                     logger.info("Listed the employee personal details");
		                     break;
		            		 
                        
                        case 6:
                        	System.out.println("Terminated");
                        	logger.info("Exited");
                            System.exit(0);
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            logger.error("User has entered invalid choice");
                    }
                }
            } 
	}
         
        
    