package Controladoras;

import Classes.Categoria;
import JDBC.Banco;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrCategoria
{
    private static CtrCategoria instance = null;
    
    private CtrCategoria()
    {
        
    }
    
    public static CtrCategoria instancia()
    {
        if(instance == null)
            instance = new CtrCategoria();
            
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
                field = "" + ((Categoria)obj).getCodigo();
            else if(campo.compareTo("desc") == 0)
                field = ((Categoria)obj).getDesc();
        }
        
        return field;
    }
    
    public boolean insert(String desc)
    {
        boolean flag;
        Categoria obj = new Categoria(Banco.getConexao().getMaxPK("Categoria", "cat_codigo") + 1, desc);
        
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
                    throw new SQLException("Erro ao inserir Categoria!");
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
    
    public boolean update(int codigo, String desc)
    {
        boolean flag;
        Categoria obj = new Categoria(codigo, desc);
        
        try
        {
            if(Banco.isConectado())
            {
                if(obj.update())
                        Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao alterar Categoria!");
                }
                
                flag = true;
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
    
    public boolean delete(int codigo)
    {
        boolean flag;
        Categoria obj = new Categoria(codigo);
        
        try
        {
            if(Banco.isConectado())
            {
                if(obj.delete())
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao excluir Categoria!");
                }
                
                flag = true;
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
    
    public Object searchByCodigo(int codigo)
    {
        Object obj = null;
        
        try
        {
            if(Banco.isConectado())
                obj = new Categoria(codigo).searchByCodigo();
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
                list = new Categoria().searchAll();
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
