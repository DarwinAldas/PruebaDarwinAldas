/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.ejb.facade.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import prbasinfo.ejb.entidades.EmpleadoSalarioHistorico;

/**
 *
 * @author Admin
 */
@Stateless
public class EmpSalHistoricoFacade extends AbstractFacade<EmpleadoSalarioHistorico> {

    @PersistenceContext(unitName = "PRBASINFO-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpSalHistoricoFacade() {
        super(EmpleadoSalarioHistorico.class);
    }
    
}
