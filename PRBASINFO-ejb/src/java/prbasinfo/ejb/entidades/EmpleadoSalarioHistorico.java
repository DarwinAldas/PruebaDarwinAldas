/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.ejb.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "empleado_salario_historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpleadoSalarioHistorico.findAll", query = "SELECT e FROM EmpleadoSalarioHistorico e")
    , @NamedQuery(name = "EmpleadoSalarioHistorico.findByIdHistorial", query = "SELECT e FROM EmpleadoSalarioHistorico e WHERE e.idHistorial = :idHistorial")
    , @NamedQuery(name = "EmpleadoSalarioHistorico.findByHisDescripcion", query = "SELECT e FROM EmpleadoSalarioHistorico e WHERE e.hisDescripcion = :hisDescripcion")
    , @NamedQuery(name = "EmpleadoSalarioHistorico.findByHisSalario", query = "SELECT e FROM EmpleadoSalarioHistorico e WHERE e.hisSalario = :hisSalario")
    , @NamedQuery(name = "EmpleadoSalarioHistorico.findByHisFechaRegistro", query = "SELECT e FROM EmpleadoSalarioHistorico e WHERE e.hisFechaRegistro = :hisFechaRegistro")
    , @NamedQuery(name = "EmpleadoSalarioHistorico.findByEstadoLogico", query = "SELECT e FROM EmpleadoSalarioHistorico e WHERE e.estadoLogico = :estadoLogico")})
public class EmpleadoSalarioHistorico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_historial")
    private Integer idHistorial;
    @Size(max = 2147483647)
    @Column(name = "his_descripcion")
    private String hisDescripcion;
    @Column(name = "his_salario")
    private BigInteger hisSalario;
    @Column(name = "his_fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hisFechaRegistro;
    @Column(name = "estado_logico")
    private Boolean estadoLogico;
    @JoinColumn(name = "cod_empleado", referencedColumnName = "id_empleado")
    @ManyToOne
    private Empleado codEmpleado;

    public EmpleadoSalarioHistorico() {
    }

    public EmpleadoSalarioHistorico(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public String getHisDescripcion() {
        return hisDescripcion;
    }

    public void setHisDescripcion(String hisDescripcion) {
        this.hisDescripcion = hisDescripcion;
    }

    public BigInteger getHisSalario() {
        return hisSalario;
    }

    public void setHisSalario(BigInteger hisSalario) {
        this.hisSalario = hisSalario;
    }

    public Date getHisFechaRegistro() {
        return hisFechaRegistro;
    }

    public void setHisFechaRegistro(Date hisFechaRegistro) {
        this.hisFechaRegistro = hisFechaRegistro;
    }

    public Boolean getEstadoLogico() {
        return estadoLogico;
    }

    public void setEstadoLogico(Boolean estadoLogico) {
        this.estadoLogico = estadoLogico;
    }

    public Empleado getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(Empleado codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorial != null ? idHistorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadoSalarioHistorico)) {
            return false;
        }
        EmpleadoSalarioHistorico other = (EmpleadoSalarioHistorico) object;
        if ((this.idHistorial == null && other.idHistorial != null) || (this.idHistorial != null && !this.idHistorial.equals(other.idHistorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prbasinfo.ejb.entidades.EmpleadoSalarioHistorico[ idHistorial=" + idHistorial + " ]";
    }
    
}
