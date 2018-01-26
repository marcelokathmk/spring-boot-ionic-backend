package com.mkath.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mkath.cursomc.domain.Cidade;

@Repository
public interface CidadeRespository extends JpaRepository<Cidade, Integer>{

	@Transactional(readOnly=true)
	@Query("Select obj FROM Cidade obj Where obj.estado.id = :estadoID Order By obj.nome")
	public List<Cidade> findCidades(@Param("estadoID") Integer idEstado);
}
