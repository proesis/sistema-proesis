package br.com.proesis.avaliacoes.view;

import br.com.proesis.avaliacoes.entity.EntityManageable;
import br.com.softop.boletim.util.exception.BusinessException;
import br.com.proesis.avaliacoes.view.AbstractManager.PersistenceAction;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.hibernate.Session;

/**
 *
 * @author Danilo Souza Almeida
 */
public abstract class GenericCrudBean<E extends EntityManageable> extends AbstractManager {
    
    private static final long serialVersionUID = 1L;
    
    public static enum CurrentState {
        EDIT,
        INSERT,
        SEARCH
    }
    
    private Class<E> classEntity;
    private E entity;
    private List<E> entitys;
    
    private CurrentState currentState;

    public GenericCrudBean(){
        try {
            entity = getClassEntity().newInstance();
            entitys = new ArrayList<E>();
            setCurrentState(CurrentState.SEARCH);
        } catch (InstantiationException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void newRegistre(ActionEvent actionEvent){
        try {
            entity = getClassEntity().newInstance();
            setCurrentState(CurrentState.INSERT);
        } catch (InstantiationException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void saveWithoutReturnToSearch(ActionEvent actionEvent){
        try {
            save();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Salvo com sucesso!"));
        } catch (BusinessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu ao tentar salvar o objeto!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void save() throws BusinessException, Exception{
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    entity.setSession(s);
                    entity.save();
                }
            });
            if(entitys == null){
                entitys = new ArrayList<E>();
            }
            if(getEntitys().contains(entity)){
                getEntitys().remove(entity);
            }
            getEntitys().add(0, entity);
            entity = getClassEntity().newInstance();
    }
    
    public void save(ActionEvent actionEvent){
        try {
            save();
            setCurrentState(CurrentState.SEARCH);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Salvo com sucesso!"));
        } catch (BusinessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu ao tentar salvar o objeto!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void delete(ActionEvent actionEvent){
        try {
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    entity.setSession(s);
                    entity.remove();
                }
            });
            getEntitys().remove(getEntity());
            newRegistre(actionEvent);
            search(actionEvent);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Removido com sucesso!!"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu um erro na aplicação!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void edit(E entity){
        this.entity = entity;
        setCurrentState(CurrentState.EDIT);
    }
    
    public void search(ActionEvent event){
        try {
            if(currentState.equals(CurrentState.SEARCH)){
                entitys = doInTransaction(new PersistenceAction<List<E>>() {
                    @Override
                    public List<E> execute(Session s) throws BusinessException {
                        return s.createCriteria(getClassEntity()).list();
                    }
                });
                if(entitys == null || entitys.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Nenhum dado encontrado!"));
                }
            } else {
                    entity = getClassEntity().newInstance();
                    setCurrentState(CurrentState.SEARCH);
            }
        } catch (BusinessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu um erro na aplicação!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Getters and setters
    public String getCurrentStateName() {
        return currentState.name();
    }
    public CurrentState getCurrentState() {
        return currentState;
    }
    public void setCurrentState(CurrentState currentState) {
        this.currentState = currentState;
    }

    public E getEntity() {
        return entity;
    }
    public void setEntity(E entity) {
        this.entity = entity;
    }

    public List<E> getEntitys() {
        return entitys;
    }
    public void setEntitys(List<E> entitys) {
        this.entitys = entitys;
    }

    public Class<E> getClassEntity() {
        classEntity = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return classEntity;
    }
    
}
