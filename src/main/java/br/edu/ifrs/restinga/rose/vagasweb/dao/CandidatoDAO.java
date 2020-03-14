/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.rose.vagasweb.dao;

import br.edu.ifrs.restinga.rose.vagasweb.modelo.Candidato;
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
public interface CandidatoDAO extends CrudRepository<Candidato, Integer> {
    @Query(nativeQuery = true, value = "select*from candidato "
            + "inner join  vaga on vaga.candidato_id =  candidato.id "
            + "where cargo = :cargo")
    List <Candidato> listarCandidatoVaga(@Param("cargo") String cargo);
    @Override
    List <Candidato> findAll();  
    
}
