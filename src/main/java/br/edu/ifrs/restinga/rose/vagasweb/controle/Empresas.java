/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.rose.vagasweb.controle;

import br.edu.ifrs.restinga.rose.vagasweb.dao.EmpresaDAO;
import br.edu.ifrs.restinga.rose.vagasweb.dao.VagaDAO;
import br.edu.ifrs.restinga.rose.vagasweb.erro.NaoEncontrado;
import br.edu.ifrs.restinga.rose.vagasweb.erro.Proibido;
import br.edu.ifrs.restinga.rose.vagasweb.erro.RequisicaoInvalida;
import br.edu.ifrs.restinga.rose.vagasweb.modelo.Empresa;
import br.edu.ifrs.restinga.rose.vagasweb.modelo.Vaga;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rose
 */
@RestController
@RequestMapping(path = "/api")
public class Empresas {
    @Autowired
    EmpresaDAO empresaDao;
    @Autowired
    VagaDAO vagaDAO;
    
    public void verificaEmpresa(Empresa empresa){
        if (empresa.getNome() == null || empresa.getNome().isEmpty()) {
            throw new RequisicaoInvalida("digite um nome");
        }
         if (empresa.getCnpj() == null || empresa.getCnpj().isEmpty()) {
            throw new RequisicaoInvalida("digite um cnpj");
        }
        if (empresa.getEndereco() == null || empresa.getEndereco().isEmpty()) {
            throw new RequisicaoInvalida("digite um endereço");
        }
        if (empresa.getTelefone() == null || empresa.getTelefone().isEmpty()) {
            throw new RequisicaoInvalida("digite um telefone");
        }
        if (empresa.getEmail() == null || empresa.getEmail().isEmpty()) {
            throw new RequisicaoInvalida("digite um email");
        }
    }
    
    @RequestMapping(path = "/empresas/", method = RequestMethod.GET)
    public Iterable<Empresa> listar() {
        return empresaDao.findAll();
    }
 
    @RequestMapping(path = "/empresas/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Empresa inserir(@RequestBody Empresa empresa) {
        empresa.setId(0);
        verificaEmpresa(empresa);
        return empresaDao.save(empresa); 
    }
    
    @RequestMapping(path = "/empresas/{id}", method = RequestMethod.GET)
    public Empresa recuperar(@PathVariable int id) {
        Optional<Empresa> findById = empresaDao.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
    @RequestMapping(path = "/empresas/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Empresa empresa){
        if (empresaDao.existsById(id)){
            empresa.setId(id);
            verificaEmpresa(empresa);
            empresaDao.save(empresa);
        }else{
            throw new NaoEncontrado("Não encontrado");
        }
    }
    
    @RequestMapping(path = "/empresas/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id){
         List<Vaga> vagas = vagaDAO.findAll();
         vagas.stream().filter((listaVaga) -> (listaVaga.getEmpresa().getId() == id)).forEachOrdered((_item) -> {
             throw new Proibido("esta empresa esta associado a uma vaga não pode ser excluido");
        });
        if (empresaDao.existsById(id)){
            empresaDao.deleteById(id);
        }else {
            throw new NaoEncontrado("Não encontrado");
        }
    }
}
