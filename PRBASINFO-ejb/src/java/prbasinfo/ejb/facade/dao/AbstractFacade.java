/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.ejb.facade.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Admin
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void crear(T entity) {
        getEntityManager().persist(entity);
    }

    public void editar(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public void eliminarGenerico(String ventidad, String vcampo, Object vvalor) throws Exception {
        String sql = " delete  from " + ventidad + " e where e." + vcampo + "=:vvalor";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor", vvalor);
        q.executeUpdate();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public int contarPorCampo(String ventidad, String vcampo1, Object vvalor1) throws Exception {
        String sql = "select count(e) from " + ventidad + " e where e." + vcampo1 + "=:vvalor1";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1);
        return ((Long) q.getSingleResult()).intValue();
    }

        public List<T> listarPorCampos(String ventidad, Map<String, Object> campos, String order) throws Exception {
        // System.out.println("llamar buscar listar campos: ");
        StringBuilder sql = new StringBuilder("select e from " + ventidad + " e ");
        if (!campos.isEmpty()) {
            sql.append("where ");
            int numCampos = 0;
            for (Map.Entry<String, Object> entry : campos.entrySet()) {
                numCampos++;
                if (numCampos > 1) {
                    sql.append(" and ");
                }
                sql.append("e." + entry.getKey() + "=:v" + entry.getKey().replace(".", ""));
                // System.out.println("valores: " + entry.getKey() + " : " + entry.getValue());
            }
            if (!order.equals("")) {
                sql.append(" order by " + order);
            }
            //  System.out.println("sql: " + sql.toString());
            Query q = getEntityManager().createQuery(sql.toString());
            for (Map.Entry<String, Object> entry : campos.entrySet()) {
                q.setParameter("v" + entry.getKey().replace(".", ""), entry.getValue());
            }

            List<T> resultado = q.getResultList();
            if (resultado.size() > 0) {
                return resultado;
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }
        
 

    public List<T> listarOrdenada(String ventidad, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e order by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        return q.getResultList();

    }

    public List<T> listarOrdenadaNotIn(String ventidad, String vcampoNotIn, Object vvalorNotIn, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampoNotIn + " not in (:vvalorNotIn) order  by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalorNotIn", vvalorNotIn);
        return q.getResultList();

    }

    public List<T> listarPor2CamposInOrdenada(String ventidad, String vCampoWhere, Object vvalorWhere, String vcampoIn, Object vvalorIn1, Object vvalorIn2, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vCampoWhere + "=:vvalor1 and " + vcampoIn + "  in (:vvalorIn1,:vvalorIn2) order  by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalorWhere);
        q.setParameter("vvalorIn1", vvalorIn1);
        q.setParameter("vvalorIn2", vvalorIn2);
        return q.getResultList();
    }

    public T buscarPorCampo(String ventidad, String vcampo1, Object vvalor1) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + "=:vvalor1";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1);
        List<T> resultado = q.getResultList();
        if (resultado.size() > 0) {
            return (T) resultado.get(0);
        } else {
            return null;
        }
    }

    public T buscarPor2Campos(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + "=:vvalor1 and e." + vcampo2 + "=:vvalor2";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2);
        List<T> resultado = q.getResultList();
        if (resultado.size() > 0) {
            return (T) resultado.get(0);
        } else {
            return null;
        }
    }

    public T buscarPor3Campos(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2, String vcampo3, Object vvalor3) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + "=:vvalor1 and e." + vcampo2 + "=:vvalor2 and e." + vcampo3 + "=:vvalor3";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2).setParameter("vvalor3", vvalor3);
        List<T> resultado = q.getResultList();
        if (resultado.size() > 0) {
            return (T) resultado.get(0);
        } else {
            return null;
        }
    }

    public T buscarPorPartes(String ventidad, String vcampo1, Object vvalor1) throws Exception {

        String sql = "select e from " + ventidad + " e where e." + vcampo1 + " like :vvalor1";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", "%" + vvalor1 + "%");
        return (T) q.getResultList().get(0);
    }

    public boolean existePorCampo(String ventidad, String vcampo1, Object vvalor1) throws Exception {
        String sql = "select count(e) from " + ventidad + " e where e." + vcampo1 + "=:vvalor1";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1);
        Long num = (Long) q.getSingleResult();
        if (num.intValue() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean existePor2Campos(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2) throws Exception {
        String sql = "select count(e) from " + ventidad + " e where e." + vcampo1 + "=:vvalor1 and e." + vcampo2 + "=:vvalor2";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2);
        Long num = (Long) q.getSingleResult();
        if (num.intValue() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean existePor3Campos(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2, String vcampo3, Object vvalor3) throws Exception {
        String sql = "select count(e) from " + ventidad + " e where e." + vcampo1 + "=:vvalor1 and e." + vcampo2 + "=:vvalor2 and e." + vcampo3 + "=:vvalor3";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2).setParameter("vvalor3", vvalor3);
        Long num = (Long) q.getSingleResult();
        if (num.intValue() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public List<T> listarPor2CamposOrdenada(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + " =:vvalor1 and e." + vcampo2 + "=:vvalor2 order by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2);
        return q.getResultList();

    }

    public List<T> listarPor2CamposyCampoNotNullOrdenada(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2, String campoNNull, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + " =:vvalor1 and e." + vcampo2 + "=:vvalor2 and e." + campoNNull + " is not null order by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2);
        return q.getResultList();

    }

    public List<T> listarPor3CamposOrdenada(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2, String vcampo3, Object vvalor3, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + " =:vvalor1 and e." + vcampo2 + "=:vvalor2 and e." + vcampo3 + "=:vvalor3 order by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2).setParameter("vvalor3", vvalor3);
        return q.getResultList();

    }

    public List<T> listarPor4CamposOrdenada(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2, String vcampo3, Object vvalor3, String vcampo4, Object vvalor4, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + " =:vvalor1 and e." + vcampo2 + "=:vvalor2 and e." + vcampo3 + "=:vvalor3 and e." + vcampo4 + "=:vvalor4 order by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2).setParameter("vvalor3", vvalor3).setParameter("vvalor4", vvalor4);
        return q.getResultList();

    }

    public List<T> listarPorRangoFecha(String ventidad, String vcampo1, Object vvalor1, Object vvalor2, String vcampoOrd, String vforma) throws Exception {
        String sql = "select e from " + ventidad + " e where e." + vcampo1 + " between :vvalor1 and :vvalor2 order by e." + vcampoOrd + " " + vforma;
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2);
        return q.getResultList();

    }

    public void eliminarPor3Campos(String ventidad, String vcampo1, Object vvalor1, String vcampo2, Object vvalor2, String vcampo3, Object vvalor3) throws Exception {
        String sql = "delete from " + ventidad + " e where e." + vcampo1 + " =:vvalor1 and e." + vcampo2 + "=:vvalor2 and e." + vcampo3 + "=:vvalor3";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor1", vvalor1).setParameter("vvalor2", vvalor2).setParameter("vvalor3", vvalor3);
        q.executeUpdate();

    }

    public void modificarGenerico(String ventidad, String vcampo, Integer cod_preg, String vcampo2, Object vvalorCondicion, String vcampo3, Object vvalorFlujo) throws Exception {
        String sql = " update " + ventidad + " set " + vcampo2 + "=:vvalor1, " + vcampo3 + "=:vvalor2 where " + vcampo + "=:vvalor3";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("vvalor2", vvalorFlujo);
        q.setParameter("vvalor1", vvalorCondicion);
        q.setParameter("vvalor3", cod_preg);
        q.executeUpdate();
    }
}
