package Controladoras;

import Classes.Cliente;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrCliente
{
    private static CtrCliente instance = null;
    
    private CtrCliente()
    {
        
    }
    
    public static CtrCliente instancia()
    {
        if(instance == null)
            instance = new CtrCliente();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public Object getField(Object obj, String campo)
    {
        String field = "";
        
        if(campo.compareTo("codigo") == 0)
            field = "" + ((Cliente)obj).getCodigo();
        else if(campo.compareTo("nome") == 0)
            field = ((Cliente)obj).getNome();
        else if(campo.compareTo("cpf") == 0)
            field = ((Cliente)obj).getCpf();
        else if(campo.compareTo("fone") == 0)
            field = ((Cliente)obj).getFone();
        else if(campo.compareTo("email") == 0)
            field = ((Cliente)obj).getEmail();
        else if(campo.compareTo("cep") == 0)
            field = ((Cliente)obj).getCep();
        else if(campo.compareTo("cidade") == 0)
            field = ((Cliente)obj).getCidade();
        else if(campo.compareTo("endereco") == 0)
            field = ((Cliente)obj).getEndereco();
        
        return field;
    }
    
    public boolean insert(String nome, String cpf, String fone, String email, String cep, String cidade, String endereco)
    {
        boolean flag;
        Cliente obj = new Cliente(Banco.getConexao().getMaxPK("Cliente", "cli_codigo") + 1, nome, cpf, fone, email, cep, cidade, endereco);
        
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
                    throw new SQLException("Erro ao inserir Cliente!");
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
    
    public boolean update(int codigo,String nome, String cpf, String fone, String email, String cep, String cidade, String endereco)
    {
        boolean flag;
        Cliente obj = new Cliente(codigo, nome, cpf, fone, email, cep, cidade, endereco);
        
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
                    throw new SQLException("Erro ao alterar Cliente!");
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
        Cliente obj = new Cliente(codigo);
        
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
                    throw new SQLException("Erro ao excluir Cliente!");
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
        Cliente obj = new Cliente();
        
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

                    case 1://cpf
                        obj.setCpf(filtro);
                        list = obj.searchByCpf();
                    break;
                    
                    case 2://cidade
                        obj.setCidade(filtro);
                        list = obj.searchByCidade();
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
                list = new Cliente().searchAll();
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
