/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.rose.vagasweb.dao;

import br.edu.ifrs.restinga.rose.vagasweb.modelo.Vaga;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rose
 */
@Repository
public interface VagaDAO extends CrudRepository<Vaga, Integer> {
     @Query(nativeQuery = true, value = "select*from vaga "
             + "inner join empresa on empresa.id = vaga.empresa_id "
             + "where nome = :nome")
    List <Vaga> listarVagaEmpresa(@Param("nome") String nome);
    @Override
    List <Vaga> findAll();
    
}
