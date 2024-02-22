package team1;

import java.util.List;
import java.sql.Date;

//import customexceptions.DuplicateEmployeeIdException;

import CustomException.IdNotFound;

public interface EmployeeManagement {
  public  void addEmployee(Employee employee);
  public  void deleteEmployee(int employeeId);
   public void updateEmployee(int employeeId, Employee newEmployee) throws IdNotFound;
    List<Employee> listEmployees();
}
