package team1;

import team1.Employee;

public class CalculateDesignation {
    public static String calculateDesignation(Employee employee) {
        double salary = employee.getSalary();

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
}
