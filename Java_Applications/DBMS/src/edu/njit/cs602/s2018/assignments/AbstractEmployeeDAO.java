package edu.njit.cs602.s2018.assignments;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 */
public abstract class AbstractEmployeeDAO {

    protected final Connection conn;

    /**
     * Constructor
     * @param conn connection to use in all db operations of this DAO
     */
    protected AbstractEmployeeDAO(Connection conn) {
        this.conn = conn;
        try {
            conn.setAutoCommit(false);
            System.out.println("database connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add department info to database
     * @param dept department info to add
     * @throws DepartmentException if department already exists or department info is invalid
     */
    public abstract void addDepartment(Department dept) throws DepartmentException;

    /**
     * Add employees and if deptId is not null, add department id to employee info
     * @param employees
     * @param deptId
     * @throws EmployeeException if one or more employees cannot be added
     */
    public abstract void addEmployees(List<Employee> employees, Integer deptId) throws EmployeeException;

    /**
     * Update employee info
     * @param employee
     * @throws EmployeeException if employee is not valid
     */
    public abstract void updateEmployee(Employee employee) throws EmployeeException;

    /**
     * Delete employee info
     * @param employee
     * @throws EmployeeException if employee is not valid
     */
    public abstract void deleteEmployee(Employee employee) throws EmployeeException;

    /**
     * Get employees for given employee ids
     * @param employeeIds
     * @return list of employees for which employee ids exist
     */
    public abstract List<Employee> getEmployees(Set<Integer> employeeIds);


    /**
     * Set average salaries in the given departments
     * @param departments
     */
    public abstract void setAverageSalaries(List<Department> departments);

    /**
     * Get list of all superiors (manager, manager of manager etc)
     * @param employee employee for which dept info exists
     * @return list of superiors (first in list is immediate supervisor)
     */
    public abstract List<Employee> getSuperiors(Employee employee);

}
