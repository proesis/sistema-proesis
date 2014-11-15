package br.com.proesis.avaliacoes.view;

import br.com.proesis.avaliacoes.entity.Usuario;
import br.com.proesis.avaliacoes.util.StringHelper;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class GerenciarUsuarioBean extends GenericCrudBean<Usuario> {
 
    private String senha;

    @Override
    public void save(ActionEvent actionEvent) {
        if(!StringHelper.isEmpty(senha)){
            getEntity().setSenha(senha);
        }
        super.save(actionEvent);
    }
    
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
