package Controladoras;

import Classes.Cliente;
import Classes.Fornecedor;
import JDBC.Banco;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrFornecedor
{
    private static CtrFornecedor instance = null;
    
    private CtrFornecedor()
    {
        
    }
    
    public static CtrFornecedor instancia()
    {
        if(instance == null)
            instance = new CtrFornecedor();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public boolean insert(String nome, String cnpj, String fone, String email, String contato)
    {
        boolean flag;
        Fornecedor obj = new Fornecedor(Banco.getConexao().getMaxPK("Fornecedores", "for_cod") + 1, nome, cnpj, fone, email, contato);
        
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
                    System.out.println(Banco.getConexao().getMensagemErro());
                    throw new SQLException("Erro ao inserir Fornecedor!");
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
    
    public boolean update(int codigo, String nome, String cnpj, String fone, String email, String contato)
    {
        boolean flag;
        Fornecedor obj = new Fornecedor(codigo, nome, cnpj, fone, email, contato);
        
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
                    System.out.println(Banco.getConexao().getMensagemErro());
                    throw new SQLException("Erro ao alterar Fornecedor!");
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
        Fornecedor obj = new Fornecedor(codigo);
        
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
                    System.out.println(Banco.getConexao().getMensagemErro());
                    throw new SQLException("Erro ao excluir Fornecedor!");
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
    
    public Object getField(Object obj, String campo)
    {
        String field = "";
        
        if(campo.compareTo("codigo") == 0)
            field = "" + ((Fornecedor)obj).getCodigo();
        else if(campo.compareTo("nome") == 0)
            field = ((Fornecedor)obj).getNome();
        else if(campo.compareTo("cnpj") == 0)
            field = ((Fornecedor)obj).getCnpj();
        else if(campo.compareTo("fone") == 0)
            field = ((Fornecedor)obj).getFone();
        else if(campo.compareTo("email") == 0)
            field = ((Fornecedor)obj).getEmail();
        else if(campo.compareTo("contato") == 0)
            field = ((Fornecedor)obj).getContato();
        
        return field;
    }
    
    public ObservableList<Object> searchByFilter(String filtro, int tipo)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        Fornecedor obj = new Fornecedor();
        
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

                    case 1://contato
                        obj.setContato(filtro);
                        list = obj.searchByContato();
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
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Fornecedor().searchAll();
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
