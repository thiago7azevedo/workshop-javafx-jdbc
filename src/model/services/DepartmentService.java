package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {

	public List<Department> findAll(){ // ainda não imporeta dados do Banco de dados, faz um MOCK que são dados não verdadeiros ainda
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(1, "Eletronics"));
		return list;
	}
	
}
