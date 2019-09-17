package model.DAO;

import db.DB;
import model.DAO.impl.SellerDaoJdbc;

public class DaoFactory {
	
public static SellerDAO createSellerDAO() {
	
	return new SellerDaoJdbc(DB.getConnection());
}
}
