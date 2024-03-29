package Controladoras;

import Classes.Caixa;
import Classes.Despesa;
import Classes.TipoDespesa;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import moveleirosystem.FXMLDocumentController;

public class CtrDespesa
{
    private static CtrDespesa instance = null;
    
    private CtrDespesa()
    {
        
    }
    
    public static CtrDespesa instancia()
    {
        if(instance == null)
            instance = new CtrDespesa();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public Object getField(Object obj, String campo)
    {
        Object field = "";
        
        if(obj != null)
        {
            if(campo.compareTo("codigo") == 0)
                field = ((Despesa)obj).getCodigo();
            else if(campo.compareTo("tipo") == 0)
                field = ((Despesa)obj).getTipo();
            else if(campo.compareTo("data") == 0)
                field = ((Despesa)obj).getData();
            else if(campo.compareTo("valor") == 0)
                field = "" + ((Despesa)obj).getValor();
            else if(campo.compareTo("caixa") == 0)
                field = ((Despesa)obj).getCaixa();
            else if(campo.compareTo("usuario") == 0)
                field = ((Despesa)obj).getUser().getLogin();
            
        }
        
        return field;
    }
    
    public boolean insert(Object tipo, Double valor, String obs)
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
                    Despesa obj = new Despesa((TipoDespesa)tipo, valor, Date.valueOf(LocalDate.now()), caixa, (Usuario)FXMLDocumentController.USER, obs);

                    if(((TipoDespesa)tipo).getCodigo() > 1 && (caixa.getSaldo() - valor) >= 0)
                    {
                        flag = obj.insert();

                        if(flag)
                            Banco.getConexao().getConnection().commit();
                        else
                        {
                            Banco.getConexao().getConnection().rollback();
                            throw new SQLException("Erro ao inserir Material!");
                        }
                    }
                    else
                        throw new SQLException("Fundos insuficiente...");
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

    public boolean delete(int codigo, Object cx)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Caixa caixa = (Caixa)((Caixa)cx).searchByCodigo();
                
                if(caixa.isOpen())
                {   
                    Despesa obj = new Despesa(codigo);

                    flag = obj.delete();

                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Erro ao inserir Material!");
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
    
    public ObservableList<Object> searchByCaixa(Object caixa)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Despesa((Caixa)caixa).searchByCaixa();
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
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Despesa().searchAll();
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
