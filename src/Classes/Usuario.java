package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Usuario
{
    private String login;
    private String senha;
    private int nivel;
    private String nome;
    private String fone;

    public Usuario()
    {
        
    }

    public Usuario(String nome)
    {
        this.nome = nome;
    }

    public Usuario(int nivel)
    {
        this.nivel = nivel;
    }

    public Usuario(String login, String senha)
    {
        this.login = login;
        this.senha = senha;
    }

    public Usuario(String login, String senha, String nome, String fone, int nivel)
    {
        this.login = login;
        this.senha = senha;
        this.nivel = nivel;
        this.nome = nome;
        this.fone = fone;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

    public int getNivel()
    {
        return nivel;
    }

    public void setNivel(int nivel)
    {
        this.nivel = nivel;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getFone()
    {
        return fone;
    }

    public void setFone(String fone)
    {
        this.fone = fone;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Usuario(usu_login, usu_senha, usu_nome, usu_fone, usu_nivel) ";
        String values = "VALUES(?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setString(1, login);
            statement.setString(2, senha);
            statement.setString(3, nome);
            statement.setString(4, fone);
            statement.setInt(5, nivel);

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean update()
    {
        String sql = "UPDATE Usuario SET usu_senha = ?, usu_nome = ?, usu_fone = ?, usu_nivel = ? WHERE usu_login = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, senha);
            statement.setString(2, nome);
            statement.setString(3, fone);
            statement.setInt(4, nivel);
            statement.setString(5, login); 
            
            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }
        
        return false;
    }
    
    public boolean delete()
    {
        String sql = "DELETE FROM Usuario WHERE usu_login = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, login);
            
            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }
        
        return false;
    }
    
    public ObservableList<Object> searchByNome()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Usuario WHERE usu_nome LIKE '" + nome + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Usuario(rs.getString("usu_login"), rs.getString("usu_senha"), rs.getString("usu_nome"), rs.getString("usu_fone"), rs.getInt("usu_nivel")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByLogin()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Usuario WHERE usu_login = '" + login + "'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Usuario(rs.getString("usu_login"), rs.getString("usu_senha"), rs.getString("usu_nome"), rs.getString("usu_fone"), rs.getInt("usu_nivel")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByFone()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Usuario WHERE usu_fone LIKE '%" + fone + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Usuario(rs.getString("usu_login"), rs.getString("usu_senha"), rs.getString("usu_nome"), rs.getString("usu_fone"), rs.getInt("usu_nivel")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public int countAdmnistrador()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Usuario WHERE usu_nivel = 3";
        int count = 0;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            count = rs.last() ? rs.getRow() : 0;
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return count;
    }
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Usuario";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Usuario(rs.getString("usu_login"), rs.getString("usu_senha"), rs.getString("usu_nome"), rs.getString("usu_fone"), rs.getInt("usu_nivel")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }

    @Override
    public String toString()
    {
        String str = "";
        
        switch(nivel)
        {
            case 1: str = "Usuario"; break;
            case 2: str = "Gerente"; break;
            case 3: str = "ADM"; break;
        }
        
        return nome + " [" + str + "]";
    }
}

/*
usu_login VARCHAR(20) NOT NULL,
usu_fone VARCHAR(20) NOT NULL,
usu_senha VARCHAR(20) NOT NULL,
usu_nivel INTEGER NOT NULL,
usu_nome VARCHAR(50) NOT NULL,
*/