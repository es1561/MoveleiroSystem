package Controladoras;

import Classes.Categoria;
import Classes.TipoMaterial;
import JDBC.Banco;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrTipoMaterial
{
    private static CtrTipoMaterial instance = null;
    
    private CtrTipoMaterial()
    {
        
    }
    
    public static CtrTipoMaterial instancia()
    {
        if(instance == null)
            instance = new CtrTipoMaterial();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public String getField(Object obj, String campo)
    {
        String field = "";
        
        if(campo.compareTo("codigo") == 0)
            field = "" + ((TipoMaterial)obj).getCodigo();
        else if(campo.compareTo("desc") == 0)
            field = ((TipoMaterial)obj).getDesc();
        else if(campo.compareTo("categoria") == 0)
            field = ((TipoMaterial)obj).getCategoria().getDesc();
        
        return field;
    }
    
    public boolean insert(String desc, int categoria)
    {
        boolean flag;
        TipoMaterial obj = new TipoMaterial(Banco.getConexao().getMaxPK("TipoMaterial", "tipo_mat_codigo") + 1, desc, (Categoria) CtrCategoria.instancia().searchByCodigo(categoria));
        
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
                    throw new SQLException("Erro ao inserir Tipo de Material!");
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
    
    public boolean update(int codigo, String desc, int categoria)
    {
        boolean flag;
        TipoMaterial obj = new TipoMaterial(codigo, desc, (Categoria) CtrCategoria.instancia().searchByCodigo(categoria));
        
        try
        {
            if(Banco.isConectado())
            {
                if(obj.update())
                        Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao alterar Tipo de Material!");
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
        TipoMaterial obj = new TipoMaterial(codigo);
        
        try
        {
            if(Banco.isConectado())
            {
                if(obj.delete())
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao excluir Tipo de Material!");
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
                obj = new TipoMaterial(codigo).searchByCodigo();
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
    
    public ObservableList<Object> searchByCategoria(int categoria)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new TipoMaterial((Categoria) CtrCategoria.instancia().searchByCodigo(categoria)).searchbyCategoria();
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
                list = new TipoMaterial().searchAll();
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
