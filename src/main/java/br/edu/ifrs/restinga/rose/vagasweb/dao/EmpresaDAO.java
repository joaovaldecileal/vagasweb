/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.rose.vagasweb.dao;

import br.edu.ifrs.restinga.rose.vagasweb.modelo.Empresa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rose
 */
@Repository
public interface EmpresaDAO extends CrudRepository<Empresa, Integer> {
    
}
