package model.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.DAO.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJdbc implements SellerDAO {

	private Connection conn;

public SellerDaoJdbc(Connection conn) {
	this.conn = conn;

}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
		"Select Seller.*, Department.name as DepName from Seller "
		+"Inner join department on seller.DepartmentId = Department.id "
		+"Where seller.id = ?");
			st.setInt(1,id);
			System.out.println(st);
			System.out.println("***************");
			rs = st.executeQuery();
			System.out.println(rs);
			if(rs.next()) {
				
				Department dep = instantiateDepartment(rs); // metodo de simplifica��o e reuso
				Seller obj = instantiateSeller(rs,dep); // metodo de simplifica��o e reuso
				return obj;
				
			}else {
				return null;
			}
			
					
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			
		}
	
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj  = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException{
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentID"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+" ORDER BY Name");
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId")); // o map ir� ver se j� existe departamento se tiver vai evitar nova instancia��o e usar o existente
				if(dep == null) {
					dep = instantiateDepartment(rs); // metodo de simplifica��o e reuso
					map.put(rs.getInt("DepartmentId"), dep);
				} 
				Seller obj = instantiateSeller(rs,dep); // metodo de simplifica��o e reuso
				
				list.add(obj);
			} 	return list;
			
					
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"where departmentid = ? "
					+" ORDER BY Name");
		
			st.setInt(1,department.getId());
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId")); // o map ir� ver se j� existe departamento se tiver vai evitar nova instancia��o e usar o existente
				if(dep == null) {
					dep = instantiateDepartment(rs); // metodo de simplifica��o e reuso
					map.put(rs.getInt("DepartmentId"), dep);
				} 
				Seller obj = instantiateSeller(rs,dep); // metodo de simplifica��o e reuso
				
				list.add(obj);
			} 	return list;
			
					
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			
		}
	
		
	}

}
