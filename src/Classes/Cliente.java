package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cliente
{
    private int codigo;
    private String nome;
    private String cpf;
    private String fone;
    private String email;
    private String cep;
    private String cidade;
    private String endereco;

    public Cliente()
    {
    }

    public Cliente(int codigo)
    {
        this.codigo = codigo;
    }

    public Cliente(String nome, String cpf, String fone, String email, String cep, String cidade, String endereco)
    {
        this.nome = nome;
        this.cpf = cpf;
        this.fone = fone;
        this.email = email;
        this.cep = cep;
        this.cidade = cidade;
        this.endereco = endereco;
    }

    public Cliente(int codigo, String nome, String cpf, String fone, String email, String cep, String cidade, String endereco)
    {
        this.codigo = codigo;
        this.nome = nome;
        this.cpf = cpf;
        this.fone = fone;
        this.email = email;
        this.cep = cep;
        this.cidade = cidade;
        this.endereco = endereco;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getCpf()
    {
        return cpf;
    }

    public void setCpf(String cpf)
    {
        this.cpf = cpf;
    }

    public String getFone()
    {
        return fone;
    }

    public void setFone(String fone)
    {
        this.fone = fone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCep()
    {
        return cep;
    }

    public void setCep(String cep)
    {
        this.cep = cep;
    }

    public String getCidade()
    {
        return cidade;
    }

    public void setCidade(String cidade)
    {
        this.cidade = cidade;
    }

    public String getEndereco()
    {
        return endereco;
    }

    public void setEndereco(String endereco)
    {
        this.endereco = endereco;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Cliente(cli_codigo, cli_nome, cli_cpf, cli_fone, cli_email, cli_cep, cli_cidade, cli_endereco) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, codigo);
            statement.setString(2, nome);
            statement.setString(3, cpf);
            statement.setString(4, fone);
            statement.setString(5, email);
            statement.setString(6, cep);
            statement.setString(7, cidade);
            statement.setString(8, endereco);

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
        String sql = "UPDATE Cliente SET cli_nome = ?, cli_cpf = ?, cli_fone = ?, cli_email = ?, cli_cep = ?, cli_cidade = ?, cli_endereco = ? WHERE cli_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nome);
            statement.setString(2, cpf);
            statement.setString(3, fone);
            statement.setString(4, email);
            statement.setString(5, cep);
            statement.setString(6, cidade);
            statement.setString(7, endereco);
            statement.setInt(8, codigo);

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
        String sql = "DELETE FROM Cliente WHERE cli_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, codigo);

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
        String sql = "SELECT * FROM Cliente WHERE cli_nome LIKE '" + nome + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Cliente(rs.getInt("cli_codigo"), rs.getString("cli_nome"), rs.getString("cli_cpf"), rs.getString("cli_fone"), rs.getString("cli_email"), rs.getString("cli_cep"), rs.getString("cli_cidade"), rs.getString("cli_endereco")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public Object searchByCodigo()
    {
        Object list = null;
        String sql = "SELECT * FROM Cliente WHERE cli_codigo = " + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list = new Cliente(rs.getInt("cli_codigo"), rs.getString("cli_nome"), rs.getString("cli_cpf"), rs.getString("cli_fone"), rs.getString("cli_email"), rs.getString("cli_cep"), rs.getString("cli_cidade"), rs.getString("cli_endereco"));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByCpf()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Cliente WHERE cli_cpf LIKE '" + cpf + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Cliente(rs.getInt("cli_codigo"), rs.getString("cli_nome"), rs.getString("cli_cpf"), rs.getString("cli_fone"), rs.getString("cli_email"), rs.getString("cli_cep"), rs.getString("cli_cidade"), rs.getString("cli_endereco")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByCidade()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Cliente WHERE cli_cidade LIKE '" + cidade + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Cliente(rs.getInt("cli_codigo"), rs.getString("cli_nome"), rs.getString("cli_cpf"), rs.getString("cli_fone"), rs.getString("cli_email"), rs.getString("cli_cep"), rs.getString("cli_cidade"), rs.getString("cli_endereco")));
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
        String sql = "SELECT * FROM Cliente WHERE cli_fone LIKE '%" + fone + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Cliente(rs.getInt("cli_codigo"), rs.getString("cli_nome"), rs.getString("cli_cpf"), rs.getString("cli_fone"), rs.getString("cli_email"), rs.getString("cli_cep"), rs.getString("cli_cidade"), rs.getString("cli_endereco")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Cliente";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Cliente(rs.getInt("cli_codigo"), rs.getString("cli_nome"), rs.getString("cli_cpf"), rs.getString("cli_fone"), rs.getString("cli_email"), rs.getString("cli_cep"), rs.getString("cli_cidade"), rs.getString("cli_endereco")));
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
        return nome + " [" + cpf + "]";
    }
}
