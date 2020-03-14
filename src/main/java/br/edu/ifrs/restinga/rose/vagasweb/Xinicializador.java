/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.rose.vagasweb;

import static br.edu.ifrs.restinga.rose.vagasweb.controle.Usuarios.PASSWORD_ENCODER;
import br.edu.ifrs.restinga.rose.vagasweb.dao.UsuarioDAO;
import br.edu.ifrs.restinga.rose.vagasweb.modelo.Usuario;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Xinicializador {
    @Autowired
    UsuarioDAO usuarioDAO;
    // Executa o método logo após a aplicação spring inicializar por completo 
    @PostConstruct
    public void init() {
        Usuario usuarioRoot = usuarioDAO.findByLogin("rose");
        if (usuarioRoot == null) {
            usuarioRoot = new Usuario();
            usuarioRoot.setNome("rose");
            usuarioRoot.setEmail("rose@rose");
            usuarioRoot.setLogin("rose");
            usuarioRoot.setSenha(PASSWORD_ENCODER.encode("12345"));
            usuarioRoot.setPermissoes(Arrays.asList("administrador" ));
            usuarioDAO.save(usuarioRoot);
        }
    }
}
