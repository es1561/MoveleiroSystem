package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Fornecedor
{
    private int codigo;
    private String nome;
    private String cnpj;
    private String fone;
    private String email;
    private String contato;

    public Fornecedor()
    {
    }

    public Fornecedor(int codigo)
    {
        this.codigo = codigo;
    }

    public Fornecedor(String nome, String cnpj, String fone, String contato)
    {
        this.nome = nome;
        this.cnpj = cnpj;
        this.fone = fone;
        this.contato = contato;
    }

    public Fornecedor(int codigo, String nome, String cnpj, String fone, String email, String contato)
    {
        this.codigo = codigo;
        this.nome = nome;
        this.cnpj = cnpj;
        this.fone = fone;
        this.email = email;
        this.contato = contato;
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

    public String getCnpj()
    {
        return cnpj;
    }

    public void setCnpj(String cnpj)
    {
        this.cnpj = cnpj;
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

    public String getContato()
    {
        return contato;
    }

    public void setContato(String contato)
    {
        this.contato = contato;
    }
    
    public boolean insert()
    {
        String sql = "INSERT INTO Fornecedores(for_cod, for_nome, for_cnpj, for_fone, for_email, for_contato) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, codigo);
            statement.setString(2, nome);
            statement.setString(3, cnpj);
            statement.setString(4, fone);
            statement.setString(5, email);
            statement.setString(6, contato);

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
        String sql = "UPDATE Fornecedores SET for_nome = ?, for_cnpj = ?, for_fone = ?, for_email = ?, for_contato = ? WHERE for_cod = ? ";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            
            statement.setString(1, nome);
            statement.setString(2, cnpj);
            statement.setString(3, fone);
            statement.setString(4, email);
            statement.setString(5, contato);
            statement.setInt(6, codigo);

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
        String sql = "DELETE FROM Fornecedores WHERE for_cod = ? ";
        
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
        String sql = "SELECT * FROM Fornecedores WHERE for_nome LIKE '" + nome + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Fornecedor(rs.getInt("for_cod"), rs.getString("for_nome"), rs.getString("for_cnpj"), rs.getString("for_fone"), rs.getString("for_email"), rs.getString("for_contato")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
    
    public ObservableList<Object> searchByContato()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Fornecedores WHERE for_contato LIKE '" + contato + "%'";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
                list.add(new Fornecedor(rs.getInt("for_cod"), rs.getString("for_nome"), rs.getString("for_cnpj"), rs.getString("for_fone"), rs.getString("for_email"), rs.getString("for_contato")));
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
        String sql = "SELECT * FROM Fornecedores";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new Fornecedor(rs.getInt("for_cod"), rs.getString("for_nome"), rs.getString("for_cnpj"), rs.getString("for_fone"), rs.getString("for_email"), rs.getString("for_contato")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
}
