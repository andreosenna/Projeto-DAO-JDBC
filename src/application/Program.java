package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.DAO.DaoFactory;
import model.DAO.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		System.out.println("+========++=======   1 teste find by Id ++=====++=====+++=========");
		SellerDAO sellerDao = DaoFactory.createSellerDAO();
		Seller seller = sellerDao.findById(1);
		System.out.println(seller);
		///*
		System.out.println("+========++=======   2 teste find by Department ++=====++=====+++=========");
		Department dep = new Department(4,null);
		List<Seller> sel = sellerDao.findByDepartment(dep);
		for(Seller obj : sel) {
			System.out.println(obj);
		}
		System.out.println("+========++====    3 teste find All ===++=====++=====+++=========");
		sel = sellerDao.findAll();
		for(Seller obj : sel) {
			System.out.println(obj);
		}
		System.out.println("+========++====    4  Teste Inser��o de novo vendedor===++=====++=====+++=========");
		Seller seller1 = new Seller(null,"maria jose","maria@gmail.com",new Date(),3000.0, dep);
		sellerDao.insert(seller1);
		System.out.println(seller1.getId());
	//	*/
		System.out.println("+========++====    5  Teste Inser��o de novo vendedor===++=====++=====+++=========");
		seller1 = sellerDao.findById(1);
		seller1.setName("black crow");
		sellerDao.update(seller1);
		System.out.println("update ok!"+ seller1);
		
		System.out.println("+========++====    6  Teste Dele��o ===++=====++=====+++=========");
		System.out.println("Insera o id para dele��o");
		Scanner sc = new Scanner(System.in);
		int idDeletar;
		idDeletar = sc.nextInt();
		sellerDao.deleteById(idDeletar);
		System.out.println("dele��o completada");
		sc.close();
	}
	}


