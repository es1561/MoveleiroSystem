package Classes;

import Controladoras.CtrCategoria;
import JDBC.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TipoDespesa
{
    private int codigo;
    private String nome;

    public TipoDespesa()
    {
    }

    public TipoDespesa(int codigo)
    {
        this.codigo = codigo;
    }

    public TipoDespesa(String nome)
    {
        this.nome = nome;
    }

    public TipoDespesa(int codigo, String nome)
    {
        this.codigo = codigo;
        this.nome = nome;
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

    @Override
    public String toString()
    {
        return nome;
    }

    public Object searchByCodigo()
    {
        Object obj = FXCollections.observableArrayList();
        String sql = "SELECT * FROM TipoDespesa WHERE tipo_des_codigo = " + codigo;
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                obj = new TipoDespesa(rs.getInt("tipo_des_codigo"), rs.getString("tipo_des_nome"));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return obj;
    }
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM TipoDespesa WHERE tipo_des_codigo > 2";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
                list.add(new TipoDespesa(rs.getInt("tipo_des_codigo"), rs.getString("tipo_des_nome")));
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return list;
    }
}
