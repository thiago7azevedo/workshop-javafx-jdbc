package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao(); // declara uma dependencia, uma chamada do DepartmentDao
	// utiliza o DaoFactory para injetar a dependencia usando esse padrão.

	public List<Department> findAll(){ // vai no banco de dados e busca os departamentos.
		return dao.findAll();
	}
	
}
