/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.rose.vagasweb.controle;

import br.edu.ifrs.restinga.rose.vagasweb.erro.NaoEncontrado;
import br.edu.ifrs.restinga.rose.vagasweb.erro.RequisicaoInvalida;
import br.edu.ifrs.restinga.rose.vagasweb.modelo.Vaga;
import br.edu.ifrs.restinga.rose.vagasweb.dao.VagaDAO;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rose
 */
@RestController
@RequestMapping(path = "/api")
public class Vagas {
    @Autowired
    VagaDAO vagaDAO;
    
    
    public void verificaVaga(Vaga vaga){
      
        if (vaga.getCargo() == null || vaga.getCargo().isEmpty()){
            throw new RequisicaoInvalida("digite cargo");
        }
        if (vaga.getCidade() == null || vaga.getCidade().isEmpty()){
            throw new RequisicaoInvalida("digite uma cidade");
        }
        if (vaga.getEstado() == null || vaga.getEstado().isEmpty()){
            throw new RequisicaoInvalida("digite um estado");
        }
        if (vaga.getDescricao() == null || vaga.getDescricao().isEmpty()){
            throw new RequisicaoInvalida("digite uma descrição ");
        }
        if (vaga.getEmpresa() == null) {
           throw new RequisicaoInvalida("escolha uma empresa ");
        }
        if (vaga.getUsuario() == null) {
            throw new RequisicaoInvalida("escolha um usuario "); 
        }
    }
    
    public Date insere(){
        return new Date(System.currentTimeMillis());
    }
    
    @RequestMapping( path = "/vagas/pesquisar/empresas/", method = RequestMethod.GET)
    public List <Vaga> pesquisarVagaEmpresa(@RequestParam(required = true) String nome){
        if (nome == null || nome.isEmpty()) {
            throw new RequisicaoInvalida("digite o nome de uma empresa");
        }
        return vagaDAO.listarVagaEmpresa(nome);   
    }
    
    @RequestMapping(path = "/vagas/", method = RequestMethod.GET)
    public Iterable<Vaga> listar() {
        return vagaDAO.findAll();
    }
    
    @RequestMapping(path = "/vagas/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Vaga inserir(@RequestBody Vaga vaga) {
        vaga.setId(0);
        vaga.setDataInclusao(insere());
        verificaVaga(vaga);
        return vagaDAO.save(vaga); 
    }
    
    @RequestMapping(path = "/vagas/{id}", method = RequestMethod.GET)
    public Vaga recuperar(@PathVariable int id) {
        Optional<Vaga> findById = vagaDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
    @RequestMapping(path = "/vagas/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Vaga vaga){
       
            if (vagaDAO.existsById(id)){
            vaga.setId(id);
            verificaVaga(vaga);
            vagaDAO.save(vaga);
        }else{
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
    @RequestMapping(path = "/vagas/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id){
        if (vagaDAO.existsById(id)){
           vagaDAO.deleteById(id);
        }else {
            throw new NaoEncontrado("Não encontrado");
        }
    }
}
