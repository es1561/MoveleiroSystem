package Controladoras;

import Classes.Usuario;
import JDBC.Banco;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CtrUsuario
{
    private static CtrUsuario instance = null;
    
    private CtrUsuario()
    {
        
    }
    
    public static CtrUsuario instancia()
    {
        if(instance == null)
            instance = new CtrUsuario();
            
        return instance;
    }
    
    public static void finaliza()
    {
        instance = null;
    }
    
    public String getField(Object obj, String campo)
    {
        String field = "";
        
        if(campo.compareTo("nome") == 0)
            field = ((Usuario)obj).getNome();
        else if(campo.compareTo("login") == 0)
            field = ((Usuario)obj).getLogin();
        else if(campo.compareTo("senha") == 0)
            field = ((Usuario)obj).getSenha();
        else if(campo.compareTo("fone") == 0)
            field = ((Usuario)obj).getFone();
        else if(campo.compareTo("nivel") == 0)
            field = "" + ((Usuario)obj).getNivel();
        else if(campo.compareTo("acesso") == 0)
        {
            switch(((Usuario)obj).getNivel())
            {
                case 1://funcionario
                    field = "Funcionario";
                break;
                
                case 2://gerente
                    field = "Gerente";
                break;
                
                case 3://ADM
                    field = "Administrador";
                break;
            }
        }
        
        return field;
    }
    
    public Object login(String login, String senha)
    {
        Object obj = new Usuario(login, senha).searchByLogin().get(0);
        Usuario user = obj != null ? ((Usuario) obj) : null;
        
        if(user != null && user.getSenha().compareTo(senha) != 0)
            obj = null;
        
        return obj;
    }
    
    public boolean insert(String login, String senha, String nome, String fone, int nivel)
    {
        boolean flag;
        Usuario obj = new Usuario(login, senha, nome, fone, nivel);
        
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
                    throw new SQLException("Erro ao inserir Usuario!");
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
    
    public boolean update(String login, String senha, String nome, String fone, int nivel)
    {
        boolean flag;
        Usuario obj = new Usuario(login, senha, nome, fone, nivel);
        
        try
        {
            if(Banco.isConectado())
            {
                if(obj.update())
                {
                    if(obj.countAdmnistrador() > 0)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Alteração do ultimo ADM negada!");
                    }
                }
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao alterar Usuario!");
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
    
    public boolean delete(String login)
    {
        boolean flag;
        Usuario obj = new Usuario(login, "");
        
        try
        {
            if(Banco.isConectado())
            {
                if(obj.delete())
                {
                    if(obj.countAdmnistrador() > 0)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Exclusão do ultimo ADM negada!");
                    }
                }
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao excluir Usuario!");
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
    
    public ObservableList<Object> searchByFilter(String filtro, int tipo)
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        Usuario obj = new Usuario();
        
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

                    case 1://login
                        obj.setLogin(filtro);
                        list = obj.searchByLogin();
                    break;

                    case 2://fone
                        obj.setFone(filtro);
                        list = obj.searchByFone();
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
                list = new Usuario().searchAll();
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
