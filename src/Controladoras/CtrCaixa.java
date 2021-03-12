package Controladoras;

import Classes.Caixa;
import Classes.Despesa;
import Classes.Movimento;
import Classes.TipoDespesa;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import moveleirosystem.FXMLDocumentController;

public class CtrCaixa
{
    private static CtrCaixa instance = null;
    
    private CtrCaixa()
    {
        
    }
    
    public static CtrCaixa instancia()
    {
        if(instance == null)
            instance = new CtrCaixa();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public String getField(Object obj, String campo)
    {
        String field = "";
        
        if(obj != null)
        {
            if(campo.compareTo("codigo") == 0)
                field = "" + ((Caixa)obj).getCodigo();
            else if(campo.compareTo("data") == 0)
                field = ((Caixa)obj).getData().toString();
            else if(campo.compareTo("hora_a") == 0)
                field = ((Caixa)obj).getHora_a();
            else if(campo.compareTo("hora_f") == 0)
                field = ((Caixa)obj).getHora_f();
            else if(campo.compareTo("valor_a") == 0)
                field = "" + ((Caixa)obj).getValor_a();
            else if(campo.compareTo("valor_f") == 0)
                field = "" + ((Caixa)obj).getValor_f();
            else if(campo.compareTo("usuario_a") == 0)
                field = ((Caixa)obj).getUser_a().getLogin();
            else if(campo.compareTo("usuario_f") == 0)
                field = ((Caixa)obj).getUser_f().getLogin();
            else if(campo.compareTo("saldo") == 0)
                field = "" + ((Caixa)obj).getSaldo();
        }
        
        return field;
    }
    
    public ObservableList<Object> getList(Object obj)
    {
        return ((Caixa)obj).getList();
    }
    
    public String getItemText(Object obj)
    {
        return ((Movimento)obj).toText();
    }
    
    public boolean isOpen(String codigo)
    {
        return ((Caixa)new Caixa(codigo).searchByCodigo()).isOpen();
    }
    
    public boolean insert(double valor_a)
    {
        boolean flag;
        int dia = Clock.systemDefaultZone().instant().atZone(ZoneId.systemDefault()).getDayOfMonth();
        int mes = Clock.systemDefaultZone().instant().atZone(ZoneId.systemDefault()).getMonthValue();
        int hora = Clock.systemDefaultZone().instant().atZone(ZoneId.systemDefault()).getHour();
        int min = Clock.systemDefaultZone().instant().atZone(ZoneId.systemDefault()).getMinute();
        String codigo = (dia < 10 ? ("0" + dia) : dia) + "" + (mes < 10 ? ("0" + mes) : mes);
        String hm = (hora < 10 ? ("0" + hora) : hora) + "" + (min < 10 ? ("0" + min) : min);
        Caixa obj = new Caixa(codigo, Date.valueOf(LocalDate.now()), (Usuario)(FXMLDocumentController.USER), hm, valor_a);
        
        try
        {
            if(Banco.isConectado())
            {
                flag = obj.insert();
                
                if(flag)
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao Abrir Caixa!");
                }
            }
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            flag = false;
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return flag;
    }
    
    public boolean update(Object caixa, double valor_f)
    {
        boolean flag;
        Caixa obj = (Caixa)caixa;
        
        obj.setValor_f(valor_f);
        
        try
        {
            if(Banco.isConectado())
            {
                flag = obj.update();
                
                if(flag)
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao Fechar Caixa!");
                }
            }
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            flag = false;
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return flag;
    }
    
    public boolean close(Object caixa)
    {
        boolean flag;
        Caixa obj = (Caixa)caixa;
        
        int hora = Clock.systemDefaultZone().instant().atZone(ZoneId.systemDefault()).getHour();
        int min = Clock.systemDefaultZone().instant().atZone(ZoneId.systemDefault()).getMinute();
        String hm = (hora < 10 ? ("0" + hora) : hora) + "" + (min < 10 ? ("0" + min) : min);
        
        obj.setUser_f((Usuario)FXMLDocumentController.USER);
        obj.setHora_f(hm);
        
        try
        {
            if(Banco.isConectado())
            {
                flag = obj.close();
                
                if(flag)
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao Fechar Caixa!");
                }
            }
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            flag = false;
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return flag;
    }
    
    public boolean movimentar(int tipo, double valor)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {
                    Caixa caixa = (Caixa)(oc);                
                    Despesa obj = new Despesa(new TipoDespesa(tipo), valor, Date.valueOf(LocalDate.now()), caixa, (Usuario)FXMLDocumentController.USER);

                    flag = obj.insert();

                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        
                        if(tipo == 1)
                            throw new SQLException("Erro ao realizar Suprimento!");
                        else
                            throw new SQLException("Erro ao realizar Sangria!");
                    }
                }
                else
                    throw new SQLException("Caixa Fechado...");
                
            }
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            flag = false;
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return flag;
    }
    
    public ObservableList<Object> searchMovimentos()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
            {
                
            }
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public Object searchByToday()
    {
        Caixa obj = null;
        
        try
        {
            if(Banco.isConectado())
                obj = (Caixa)new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public Object searchByData(Date data)
    {
        Object obj = null;
        
        try
        {
            if(Banco.isConectado())
                obj = new Caixa(data).searchByData();
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Caixa().searchAll();
            else
                throw new SQLException("Banco Off-Line...");
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
}
