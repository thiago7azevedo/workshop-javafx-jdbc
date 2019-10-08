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
	
	public void saveOrUpdate(Department obj) { // método para verificar se vamos inserir um departamento no Banco de dados ou atualizar
		if(obj.getId() == null) {
			dao.insert(obj); // se for nulo, ele insere
		} else {
			dao.update(obj); // se o dep ja estiver id, ja tem no BD. Ai voce atualiza
		}
	}
	
}
