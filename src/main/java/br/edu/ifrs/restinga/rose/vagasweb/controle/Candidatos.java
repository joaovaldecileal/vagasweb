/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.rose.vagasweb.controle;

import br.edu.ifrs.restinga.rose.vagasweb.dao.CandidatoDAO;
import br.edu.ifrs.restinga.rose.vagasweb.dao.VagaDAO;
import br.edu.ifrs.restinga.rose.vagasweb.erro.NaoEncontrado;
import br.edu.ifrs.restinga.rose.vagasweb.erro.Proibido;
import br.edu.ifrs.restinga.rose.vagasweb.erro.RequisicaoInvalida;
import br.edu.ifrs.restinga.rose.vagasweb.modelo.Candidato;
import br.edu.ifrs.restinga.rose.vagasweb.modelo.Vaga;
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
public class Candidatos {
    @Autowired
    CandidatoDAO candidatoDAO;
    @Autowired
    VagaDAO vagaDAO;
    
    public void verificaCandidato(Candidato candidato){
        if (candidato.getNome() == null || candidato.getNome().isEmpty()){
            throw new RequisicaoInvalida("digite um nome");
        }
        if (candidato.getEmail() == null || candidato.getEmail().isEmpty()){
            throw new RequisicaoInvalida("digite um e-mail");
        }
        if (candidato.getExperiencia() == null || candidato.getExperiencia().isEmpty()){
            throw new RequisicaoInvalida("selecione suas experiencias na área");
        }
    }
    @RequestMapping( path = "/candidatos/pesquisar/vagas/", method = RequestMethod.GET)
    public List <Candidato> pesquisarCandidatoVaga(@RequestParam(required = true) String cargo){
        if (cargo == null || cargo.isEmpty()) {
            throw new RequisicaoInvalida("digite algum cargo");
        }
        return candidatoDAO.listarCandidatoVaga(cargo);   
    }
    
    @RequestMapping(path = "/candidatos/", method = RequestMethod.GET)
    public Iterable<Candidato> listar() {
        return candidatoDAO.findAll();
    }
    
    @RequestMapping(path = "/candidatos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Candidato inserir(@RequestBody Candidato candidato) {
        candidato.setId(0);
        verificaCandidato(candidato);
        return candidatoDAO.save(candidato); 
    }
    
    @RequestMapping(path = "/candidatos/{id}", method = RequestMethod.GET)
    public Candidato recuperar(@PathVariable int id) {
        Optional<Candidato> findById = candidatoDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
    @RequestMapping(path = "/candidatos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Candidato candidato){
        if (candidatoDAO.existsById(id)){
            candidato.setId(id);
            verificaCandidato(candidato);
            candidatoDAO.save(candidato);
        }else{
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
    @RequestMapping(path = "/candidatos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id){
        List<Vaga> vagas = vagaDAO.findAll();
        vagas.stream().filter((listaVagas) -> (listaVagas.getCandidato().getId() == id)).forEachOrdered((listaVagas) -> {
            throw new Proibido("este candidato esta associado a uma vaga não pode ser excluido");
        });
        if (candidatoDAO.existsById(id)){
            candidatoDAO.deleteById(id);
        }else {
            throw new NaoEncontrado("Não encontrado");
        }
    } 
}
