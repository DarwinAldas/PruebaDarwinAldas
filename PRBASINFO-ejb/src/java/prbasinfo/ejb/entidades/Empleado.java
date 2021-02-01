/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prbasinfo.ejb.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e")
    , @NamedQuery(name = "Empleado.findByIdEmpleado", query = "SELECT e FROM Empleado e WHERE e.idEmpleado = :idEmpleado")
    , @NamedQuery(name = "Empleado.findByEmpIdentificador", query = "SELECT e FROM Empleado e WHERE e.empIdentificador = :empIdentificador")
    , @NamedQuery(name = "Empleado.findByEmpNombre", query = "SELECT e FROM Empleado e WHERE e.empNombre = :empNombre")
    , @NamedQuery(name = "Empleado.findByEmpApellido", query = "SELECT e FROM Empleado e WHERE e.empApellido = :empApellido")
    , @NamedQuery(name = "Empleado.findByEmpSexo", query = "SELECT e FROM Empleado e WHERE e.empSexo = :empSexo")
    , @NamedQuery(name = "Empleado.findByEmpFechaNac", query = "SELECT e FROM Empleado e WHERE e.empFechaNac = :empFechaNac")
    , @NamedQuery(name = "Empleado.findByEmpSalario", query = "SELECT e FROM Empleado e WHERE e.empSalario = :empSalario")
    , @NamedQuery(name = "Empleado.findByEmpDireccion", query = "SELECT e FROM Empleado e WHERE e.empDireccion = :empDireccion")
    , @NamedQuery(name = "Empleado.findByEmpFonoContacto", query = "SELECT e FROM Empleado e WHERE e.empFonoContacto = :empFonoContacto")
    , @NamedQuery(name = "Empleado.findByEmpCorreoElectronico", query = "SELECT e FROM Empleado e WHERE e.empCorreoElectronico = :empCorreoElectronico")
    , @NamedQuery(name = "Empleado.findByEmpFechaRegistro", query = "SELECT e FROM Empleado e WHERE e.empFechaRegistro = :empFechaRegistro")
    , @NamedQuery(name = "Empleado.findByEmpFechaActualiza", query = "SELECT e FROM Empleado e WHERE e.empFechaActualiza = :empFechaActualiza")
    , @NamedQuery(name = "Empleado.findByEstadoLogico", query = "SELECT e FROM Empleado e WHERE e.estadoLogico = :estadoLogico")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empleado")
    private Integer idEmpleado;
    @Size(max = 25)
    @Column(name = "emp_identificador")
    private String empIdentificador;
    @Size(max = 120)
    @Column(name = "emp_nombre")
    private String empNombre;
    @Size(max = 120)
    @Column(name = "emp_apellido")
    private String empApellido;
    @Size(max = 1)
    @Column(name = "emp_sexo")
    private String empSexo;
    @Column(name = "emp_fecha_nac")
    @Temporal(TemporalType.DATE)
    private Date empFechaNac;
    @Column(name = "emp_salario")
    private BigInteger empSalario;
    @Size(max = 2147483647)
    @Column(name = "emp_direccion")
    private String empDireccion;
    @Size(max = 50)
    @Column(name = "emp_fono_contacto")
    private String empFonoContacto;
    @Size(max = 50)
    @Column(name = "emp_correo_electronico")
    private String empCorreoElectronico;
    @Column(name = "emp_fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date empFechaRegistro;
    @Column(name = "emp_fecha_actualiza")
    @Temporal(TemporalType.TIMESTAMP)
    private Date empFechaActualiza;
    @Column(name = "estado_logico")
    private Boolean estadoLogico;
    @OneToMany(mappedBy = "codEmpleado")
    private Collection<EmpleadoSalarioHistorico> empleadoSalarioHistoricoCollection;
    @JoinColumn(name = "cod_cargo", referencedColumnName = "id_cargo")
    @ManyToOne
    private Cargo codCargo;
    @JoinColumn(name = "cod_departamento", referencedColumnName = "id_departamento")
    @ManyToOne
    private Departamento codDepartamento;
    @OneToMany(mappedBy = "codEmpleadoPadre")
    private Collection<Empleado> empleadoCollection;
    @JoinColumn(name = "cod_empleado_padre", referencedColumnName = "id_empleado")
    @ManyToOne
    private Empleado codEmpleadoPadre;

    public Empleado() {
    }

    public Empleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getEmpIdentificador() {
        return empIdentificador;
    }

    public void setEmpIdentificador(String empIdentificador) {
        this.empIdentificador = empIdentificador;
    }

    public String getEmpNombre() {
        return empNombre;
    }

    public void setEmpNombre(String empNombre) {
        this.empNombre = empNombre;
    }

    public String getEmpApellido() {
        return empApellido;
    }

    public void setEmpApellido(String empApellido) {
        this.empApellido = empApellido;
    }

    public String getEmpSexo() {
        return empSexo;
    }

    public void setEmpSexo(String empSexo) {
        this.empSexo = empSexo;
    }

    public Date getEmpFechaNac() {
        return empFechaNac;
    }

    public void setEmpFechaNac(Date empFechaNac) {
        this.empFechaNac = empFechaNac;
    }

    public BigInteger getEmpSalario() {
        return empSalario;
    }

    public void setEmpSalario(BigInteger empSalario) {
        this.empSalario = empSalario;
    }

    public String getEmpDireccion() {
        return empDireccion;
    }

    public void setEmpDireccion(String empDireccion) {
        this.empDireccion = empDireccion;
    }

    public String getEmpFonoContacto() {
        return empFonoContacto;
    }

    public void setEmpFonoContacto(String empFonoContacto) {
        this.empFonoContacto = empFonoContacto;
    }

    public String getEmpCorreoElectronico() {
        return empCorreoElectronico;
    }

    public void setEmpCorreoElectronico(String empCorreoElectronico) {
        this.empCorreoElectronico = empCorreoElectronico;
    }

    public Date getEmpFechaRegistro() {
        return empFechaRegistro;
    }

    public void setEmpFechaRegistro(Date empFechaRegistro) {
        this.empFechaRegistro = empFechaRegistro;
    }

    public Date getEmpFechaActualiza() {
        return empFechaActualiza;
    }

    public void setEmpFechaActualiza(Date empFechaActualiza) {
        this.empFechaActualiza = empFechaActualiza;
    }

    public Boolean getEstadoLogico() {
        return estadoLogico;
    }

    public void setEstadoLogico(Boolean estadoLogico) {
        this.estadoLogico = estadoLogico;
    }

    @XmlTransient
    public Collection<EmpleadoSalarioHistorico> getEmpleadoSalarioHistoricoCollection() {
        return empleadoSalarioHistoricoCollection;
    }

    public void setEmpleadoSalarioHistoricoCollection(Collection<EmpleadoSalarioHistorico> empleadoSalarioHistoricoCollection) {
        this.empleadoSalarioHistoricoCollection = empleadoSalarioHistoricoCollection;
    }

    public Cargo getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(Cargo codCargo) {
        this.codCargo = codCargo;
    }

    public Departamento getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(Departamento codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    @XmlTransient
    public Collection<Empleado> getEmpleadoCollection() {
        return empleadoCollection;
    }

    public void setEmpleadoCollection(Collection<Empleado> empleadoCollection) {
        this.empleadoCollection = empleadoCollection;
    }

    public Empleado getCodEmpleadoPadre() {
        return codEmpleadoPadre;
    }

    public void setCodEmpleadoPadre(Empleado codEmpleadoPadre) {
        this.codEmpleadoPadre = codEmpleadoPadre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpleado != null ? idEmpleado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.idEmpleado == null && other.idEmpleado != null) || (this.idEmpleado != null && !this.idEmpleado.equals(other.idEmpleado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prbasinfo.ejb.entidades.Empleado[ idEmpleado=" + idEmpleado + " ]";
    }
    
}
