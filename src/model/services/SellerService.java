package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao(); // declara uma dependencia, uma chamada do SellerDao
	// utiliza o DaoFactory para injetar a dependencia usando esse padrão.

	public List<Seller> findAll(){ // vai no banco de dados e busca os departamentos.
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) { // método para verificar se vamos inserir um departamento no Banco de dados ou atualizar
		if(obj.getId() == null) {
			dao.insert(obj); // se for nulo, ele insere
		} else {
			dao.update(obj); // se o dep ja estiver id, ja tem no BD. Ai voce atualiza
		}
	}
	public void remove(Seller obj) { // remove um departamento do Banco de dados
		dao.deleteById(obj.getId());
	}
	
}
