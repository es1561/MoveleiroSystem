package Controladoras;

import Classes.Categoria;
import Classes.Material;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrMaterial
{
    private static CtrMaterial instance = null;
    
    private CtrMaterial()
    {
        
    }
    
    public static CtrMaterial instancia()
    {
        if(instance == null)
            instance = new CtrMaterial();
            
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
                field = "" + ((Material)obj).getCodigo();
            else if(campo.compareTo("nome") == 0)
                field = ((Material)obj).getNome();
            else if(campo.compareTo("desc") == 0)
                field = ((Material)obj).getDesc();
            else if(campo.compareTo("valor") == 0)
                field = "" + ((Material)obj).getValor();
            else if(campo.compareTo("estoque") == 0)
                field = "" + ((Material)obj).getEstoque();
            else if(campo.compareTo("estoque_min") == 0)
                field = "" + ((Material)obj).getEstoque_min();
            else if(campo.compareTo("categoria") == 0)
                field = ((Material)obj).getCategoriaDesc();
            else if(campo.compareTo("tipo") == 0)
                field = ((Material)obj).getTipoDesc();
            else if(campo.compareTo("obj_categoria") == 0)
                field = ((Material)obj).getCategoria();
        }
        
        return field;
    }
    
    public boolean insert(String nome, String desc, double valor, int estoque, int estoque_min, Object tipo)
    {
        boolean flag;
        Material obj = new Material(Banco.getConexao().getMaxPK("Material", "mat_codigo") + 1, nome, valor, desc, estoque, estoque_min, tipo);
        
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
                    throw new SQLException("Erro ao inserir Material!");
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
    
    public boolean update(int codigo, String nome, String desc, double valor, int estoque, int estoque_min, Object tipo)
    {
        boolean flag;
        Material obj = new Material(codigo, nome, valor, desc, estoque, estoque_min, tipo);
        
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
                    throw new SQLException("Erro ao alterar Material!");
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
    
    public boolean delete(int codigo)
    {
        boolean flag;
        Material obj = new Material(codigo);
        
        try
        {
            if(Banco.isConectado())
            {
                flag = obj.delete();
                
                if(flag)
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao excluir Material!");
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
    
    public ObservableList<Object> searchByFilter(String filtro, int tipo)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        Material obj = new Material();
        
        try
        {
            if(Banco.isConectado())
            {
                switch(tipo)
                {
                    case 0://nome
                        obj.setNome(filtro);
                        list = obj.searchByNome();
                    break;

                    case 1://categoria
                        obj.setDesc(filtro);
                        list = obj.searchByCategoria();
                    break;

                    case 2://tipo
                        obj.setDesc(filtro);
                        list = obj.searchByTipo();
                    break;
                }
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
    
    public Object searchByCodigo(int codigo)
    {
        Object obj = null;
        
        try
        {
            if(Banco.isConectado())
                obj = new Material(codigo).searchByCodigo();
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
                list = new Material().searchAll();
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
