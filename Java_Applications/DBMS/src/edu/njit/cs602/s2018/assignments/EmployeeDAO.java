package edu.njit.cs602.s2018.assignments;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//Written and submitted by Safia Hareema// 

public class EmployeeDAO extends AbstractEmployeeDAO {

	HashMap<Integer, Employee> employees = new HashMap<>();
	List<Integer> empsAdded = new ArrayList<>();

	protected EmployeeDAO(Connection conn) {
		super(conn);
	}

	@Override
	/**
	 * Add department info to database
	 * 
	 * @param dept
	 *            department info to add
	 * @throws DepartmentException
	 *             if department already exists or department info is invalid
	 */
	public void addDepartment(Department dept) throws DepartmentException {
		String sql = "INSERT INTO department(dept_id, dept_name, manager_id) VALUES(?,?,?)";
		String sql2 = "SELECT FROM department WHERE dept_id = ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql2);
			ps.setInt(1, dept.getDeptId());
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				int depId = res.getInt(1);
				System.out.println("emp id: " + depId);
				throw new DepartmentException("Department already exists.");
			}
			ps = conn.prepareStatement(sql);
			ps.setInt(1, dept.getDeptId());
			ps.setString(2, dept.getDeptName());
			ps.setInt(3, dept.getManagerId());
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DepartmentException("Department invalid");
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	/**
	 * Add employees and if deptId is not null, add department id to employee
	 * info
	 * 
	 * @param employees
	 * @param deptId
	 * @throws EmployeeException
	 *             if one or more employees cannot be added
	 */
	public void addEmployees(List<Employee> employees, Integer deptId) throws EmployeeException {
		String sql = "INSERT INTO employee(employee_id, first_name, last_name, employment_date, dept_id, annual_salary) VALUES(?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (Employee emp : employees) {
				this.employees.put(emp.getEmployeeId(), emp);
				empsAdded.add(emp.getEmployeeId());
				ps.setInt(1, emp.getEmployeeId());
				ps.setString(2, emp.getFirstName());
				ps.setString(3, emp.getLastName());
				ps.setDate(4, new java.sql.Date(emp.getEmploymentDate().getTime()));
				if (deptId == null) {
					ps.setNull(5, java.sql.Types.INTEGER);
				} else {
					ps.setInt(5, deptId);
				}
				ps.setDouble(6, emp.getAnnualSalary());

				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			empsAdded.clear();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			revertEmpsAdded();
			throw new EmployeeException("One or more employees could not be added. Update reverted.");

		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Removes employees just added to list if rollback called in addEmployees method 
	 * 
	 * @param none 
	 * @return void 
	 */
	private void revertEmpsAdded() {
		for (int i : empsAdded) {
			this.employees.remove(i);
		}
	}

	@Override
	/**
	 * Update employee info
	 * 
	 * @param employee
	 * @throws EmployeeException
	 *             if employee is not valid
	 */
	public void updateEmployee(Employee employee) throws EmployeeException {
		String sql2 = "UPDATE employee SET first_name = ?, last_name = ?, employment_date = ?, dept_id = ?, annual_salary = ? WHERE employee_id = ?";
		if (isValidEmp(employee)) {
			PreparedStatement ps = null;
			this.employees.remove(employee.getEmployeeId());
			this.employees.put(employee.getEmployeeId(), employee);
			try {
				ps = conn.prepareStatement(sql2);
				ps.setString(1, employee.getFirstName());
				ps.setString(2, employee.getLastName());
				ps.setDate(3, new java.sql.Date(employee.getEmploymentDate().getTime()));
				ps.setInt(4, employee.getDept().getDeptId());
				ps.setDouble(5, employee.getAnnualSalary());
				ps.setInt(6, employee.getEmployeeId());

				ps.executeUpdate();
				conn.commit();
				System.out.println("Employee updated!");
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw new EmployeeException("Employeee not valid.");
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new EmployeeException("Employee not valid.");
		}

	}

	private boolean isValidEmp(Employee emp) {
		String sql = "SELECT FROM employee WHERE employee_id = ?";
		ResultSet res = null;
		PreparedStatement ps = null;
		boolean valid = true;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, emp.getEmployeeId());
			res = ps.executeQuery();
			if (res.next()) {
				valid = true;
			} else {
				valid = false;
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				res.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valid;
	}

	@Override
	/**
	 * Delete employee info
	 * 
	 * @param employee
	 * @throws EmployeeException
	 *             if employee is not valid
	 */
	public void deleteEmployee(Employee employee) throws EmployeeException {
		String sql = "DELETE FROM employee WHERE employee_id = ?";
		PreparedStatement ps = null;
		if (isValidEmp(employee)) {
			this.employees.remove(employee.getEmployeeId());
			try {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, employee.getEmployeeId());
				ps.executeUpdate();
				conn.commit();
				System.out.print("Employee deleted");
				ps.close();
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw new EmployeeException("Employee not valid");
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else {
			throw new EmployeeException("Employee not valid.");
		}

	}

	@Override
	/**
	 * Get employees for given employee ids
	 * 
	 * @param employeeIds
	 * @return list of employees for which employee ids exist
	 */
	public List<Employee> getEmployees(Set<Integer> employeeIds) {
		List<Employee> result = new ArrayList<>();
		for (int empID : employeeIds) {
			if (this.employees.containsKey(empID)) {
				result.add(this.employees.get(empID));
			}
		}
		return result;
	}

	@Override
	/**
	 * Set average salaries in the given departments
	 * 
	 * @param departments
	 */
	public void setAverageSalaries(List<Department> departments) {
		String sql = "SELECT AVG(annual_salary) FROM employee e, department d WHERE d.dept_id = e.dept_id AND d.dept_id = ?";
		ResultSet res = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (Department d : departments) {
				ps.setInt(1, d.getDeptId());
				res = ps.executeQuery();
				while (res.next()) {
					d.setAverageSalary(res.getDouble(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	/**
	 * Get list of all superiors (manager, manager of manager etc)
	 * 
	 * @param employee
	 *            employee for which dept info exists
	 * @return list of superiors (first in list is immediate supervisor)
	 */
	public List<Employee> getSuperiors(Employee employee) {
		List<Employee> superiors = new LinkedList<>();
		String sql = "SELECT d.manager_id FROM employee e, department d WHERE d.dept_id = e.dept_id AND e.employee_id = ?";
		int empID = employee.getEmployeeId(), supID = -1;
		ResultSet res = null;
		boolean hasSuperior = true;
		PreparedStatement ps = null;
		try {
			while (hasSuperior) {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, empID);
				res = ps.executeQuery();
				while (res.next()) {
					supID = res.getInt(1);
				}
				if (supID == empID) {
					hasSuperior = false;
				} else {
					superiors.add(this.employees.get(supID));
					empID = supID;
				}
			}
			return superiors;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
