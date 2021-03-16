package Classes;

import Controladoras.CtrPagamento;
import JDBC.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Aquisicao
{
    private String codigo;
    private Date data;
    private double total;
    private int parcelas;
    private Usuario user;
    private Fornecedor forn;
    private ObservableList<Object> itens;

    public Aquisicao()
    {
    }

    public Aquisicao(String codigo)
    {
        this.codigo = codigo;
    }

    public Aquisicao(Date data, double total, int parcelas, Usuario user, Fornecedor forn)
    {
        this.data = data;
        this.total = total;
        this.parcelas = parcelas;
        this.user = user;
        this.forn = forn;
    }

    public Aquisicao(String codigo, Date data, double total, int parcelas, Usuario user, Fornecedor forn)
    {
        this.codigo = codigo;
        this.data = data;
        this.total = total;
        this.parcelas = parcelas;
        this.user = user;
        this.forn = forn;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public Date getData()
    {
        return data;
    }

    public double getTotal()
    {
        return total;
    }

    public int getParcelas()
    {
        return parcelas;
    }

    public Usuario getUser()
    {
        return user;
    }

    public Fornecedor getForn()
    {
        return forn;
    }
    
    public ObservableList<Object> getItens()
    {
        return itens;
    }
    
    public void setItens(ObservableList<Object> itens)
    {
        this.itens = itens;
    }
    
    private boolean insertSelf()
    {
        String sql = "INSERT INTO Aquisicao(aqui_codigo, aqui_data, aqui_total, aqui_parcelas, usu_login, for_cod) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setString(1, codigo);
            statement.setDate(2, data);
            statement.setDouble(3, total);
            statement.setInt(4, parcelas);
            statement.setString(5, user.getLogin());
            statement.setInt(6, forn.getCodigo());

            return statement.executeUpdate() > 0;
        }
        catch(SQLException ex)
        {
            Banco.getConexao().setMessagemErro(ex.getMessage());
        }

        return false;
    }
    
    public boolean insert()
    {
        boolean flag = insertSelf();
        
        if(flag)
        {
            for (int i = 0; flag && i < itens.size(); i++)
            {
                ItemAquisicao item = (ItemAquisicao)itens.get(i);
                
                item.setAquisicao(this);
                flag = item.insert();
            }
        }
        
        return flag;
    }
    
    private boolean updateSelf()
    {
        String sql = "UPDATE Aquisicao SET aqui_total = ?, aqui_parcelas = ?, usu_login = ?, for_cod = ? WHERE aqui_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setDouble(1, total);
            statement.setInt(2, parcelas);
            statement.setString(3, user.getLogin());
            statement.setInt(4, forn.getCodigo());
            statement.setString(5, codigo);

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
        boolean flag = updateSelf();
        
        if(flag)
        {
            if(new Pagamento(this).canEdit())
            {
                flag = new ItemAquisicao(this).clearItens();

                if(flag)
                {
                    for (int i = 0; flag && i < itens.size(); i++)
                    {
                        ItemAquisicao item = (ItemAquisicao)itens.get(i);

                        item.setAquisicao(this);
                        flag = item.insert();
                    }
                }
            }
        }
        
        return flag;
    }
    
    private boolean deleteSelf()
    {
        String sql = "DELETE FROM Aquisicao WHERE aqui_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, codigo);
            
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
        boolean flag = new ItemAquisicao(this).clearItens();
        boolean flag2 = new Pagamento(this).clearItens();
        
        if(flag && flag2)
            flag = deleteSelf();
        
        return flag;
    }
    
    public Object searchByCodigo()
    {
        Object obj = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Aquisicao WHERE aqui_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, codigo);
            
            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
            {
                Usuario _user = (Usuario) new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0);
                Fornecedor _forn = (Fornecedor) new Fornecedor(rs.getInt("for_cod")).searchByCodigo();
                Aquisicao aqui = new Aquisicao(rs.getString("aqui_codigo"), rs.getDate("aqui_data"), rs.getDouble("aqui_total"), rs.getInt("aqui_parcelas"), _user, _forn);
                ObservableList<Object> itens = new ItemAquisicao(aqui).searchByAquisicao();
                
                aqui.setItens(itens);
                obj = aqui;
            }
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
        String sql = "SELECT * FROM Aquisicao";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Usuario _user = (Usuario) new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0);
                Fornecedor _forn = (Fornecedor) new Fornecedor(rs.getInt("for_cod")).searchByCodigo();
                Aquisicao aqui = new Aquisicao(rs.getString("aqui_codigo"), rs.getDate("aqui_data"), rs.getDouble("aqui_total"), rs.getInt("aqui_parcelas"), _user, _forn);
                ObservableList<Object> itens = new ItemAquisicao(aqui).searchByAquisicao();
                
                aqui.setItens(itens);
                list.add(aqui);
            }
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
        return "" + codigo;
    }
    
    public String toText()
    {
        String str = "Aquisição - " + codigo + " com " + forn.getNome() + "\n" +
                     "Efetuado por [" + user.getLogin() + "]\n no valor total de R$ " + total + "\n\nItens da Aquisição\n";
        
        for (Object item : itens)
        {
            ItemAquisicao obj = (ItemAquisicao)item;
            
            str += obj.toString() + " [" + obj.getValor() + "]\n";
        }
        
        return str;
    }
}
