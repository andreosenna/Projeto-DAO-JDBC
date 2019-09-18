package model.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	PreparedStatement st = null;
	try {
		st = conn.prepareStatement(
				"Insert into seller "
				+"(Name,Email,Birthdate, BaseSalary, DepartmentId)"
				+"Values (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
		st.setString(1,obj.getName());
		st.setString(2,obj.getEmail());
		st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
		st.setDouble(4,obj.getSalary());
		st.setInt(5,obj.getDepartment().getId());
		int rowsAffected = st.executeUpdate();
		if(rowsAffected >0) {
			ResultSet rs = st.getGeneratedKeys();
			if(rs.next()) {// já que não tem id esse método irá recuperar o id do banco de dados e setar no objeto obj
				int id = rs.getInt(1);
				obj.setId(id);
		}
			
		}else {
		throw new DbException("Erro! nenhum valor afetado");	
		}
		
	}catch(SQLException e) {
		throw new DbException("erro ");
	}finally {
		DB.closeStatement(st);
		//DB.closeResultSet(rs);
	}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"Update seller "
					+"Set Name = ?, Email = ?, BirthDate = ? , BaseSalary = ?, DepartmentId = ? "
					+"Where Id = ?"
			);
			st.setString(1,obj.getName());
			st.setString(2,obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4,obj.getSalary());
			st.setInt(5,obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			st.executeUpdate();
		
					
		}catch(SQLException e) {
			throw new DbException("erro ");
		}finally {
			DB.closeStatement(st);
			//DB.closeResultSet(rs);
		}
			
		}
		
	

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
		st = conn.prepareStatement(
				"Delete from Seller "
				+"Where Id =  ?");
				st.setInt(1, id);
				int row = st.executeUpdate();
			if (row == 0) {
				System.out.println("valor não encontrado");
			}
		}catch(SQLException e){
			throw new DbException("deleçao não realizada id não encotrada ");
		}finally {
			DB.closeStatement(st);
		}
		
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
		"Select Seller.*, Department.Name as DepName from Seller "
		+"Inner join department on seller.DepartmentId = Department.id "
		+"Where seller.id = ?");
			st.setInt(1,id);
			rs = st.executeQuery();
			if(rs.next()) {
				
				Department dep = instantiateDepartment(rs); // metodo de simplificação e reuso
				Seller obj = instantiateSeller(rs,dep); // metodo de simplificação e reuso
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
				Department dep = map.get(rs.getInt("DepartmentId")); // o map irá ver se já existe departamento se tiver vai evitar nova instanciação e usar o existente
				if(dep == null) {
					dep = instantiateDepartment(rs); // metodo de simplificação e reuso
					map.put(rs.getInt("DepartmentId"), dep);
				} 
				Seller obj = instantiateSeller(rs,dep); // metodo de simplificação e reuso
				
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
				Department dep = map.get(rs.getInt("DepartmentId")); // o map irá ver se já existe departamento se tiver vai evitar nova instanciação e usar o existente
				if(dep == null) {
					dep = instantiateDepartment(rs); // metodo de simplificação e reuso
					map.put(rs.getInt("DepartmentId"), dep);
				} 
				Seller obj = instantiateSeller(rs,dep); // metodo de simplificação e reuso
				
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
