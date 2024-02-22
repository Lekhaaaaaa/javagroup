package team1;

import java.io.Serializable;
import java.sql.Date;


public class Employee implements Serializable {
 private int id;
 private String name;
 private double salary;
 private Date dateOfBirth;
 private String email;
 private String phoneNumber;
 private static final long serialVersionUID = -7857751693407005352L;


 public Employee(int id, String name, double salary) {
     this.id = id;
     this.name = name;
     this.salary = salary;
 }

 public int getId() {
     return id;
 }

 public String getName() {
     return name;
 }

 public double getSalary() {
     return salary;
 }
 public Date getDateOfBirth() {
     return dateOfBirth;
 }

 public void setDateOfBirth(Date dateOfBirth) {
     this.dateOfBirth = dateOfBirth;
 }

 public String getEmail() {
     return email;
 }

 public void setEmail(String email) {
     this.email = email;
 }

 public String getPhoneNumber() {
     return phoneNumber;
 }

 public void setPhoneNumber(String phoneNumber) {
     this.phoneNumber = phoneNumber;
 }
 public void calculateDesignation(String designation) {
		// TODO Auto-generated method stub
		
	}

 @Override
 public String toString() {
	 return "Employee ID: " + id + "\nName: " + name + "\nSalary: " + salary +
             "\nDate of Birth: " + dateOfBirth + "\nEmail: " + email + "\nPhone Number: " + phoneNumber;
 }
}

