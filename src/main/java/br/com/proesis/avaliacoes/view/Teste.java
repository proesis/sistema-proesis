package br.com.proesis.avaliacoes.view;

import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Danilo Souza Almeida
 */
@ManagedBean
@RequestScoped
public class Teste {
    
    public void testar(){
        int duracao = 3000;
        long tempo = Calendar.getInstance().getTimeInMillis()+duracao;
        while(Calendar.getInstance().getTimeInMillis() < tempo){
            //so espera mais nada
        }
    }
    
}
