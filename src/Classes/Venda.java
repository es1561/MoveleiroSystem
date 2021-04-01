package Classes;

import JDBC.Banco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Venda
{
    private int codigo;
    private Date data;
    private double total;
    private int parcelas;
    private Usuario user;
    private Cliente cliente;
    private ObservableList<Object> itens;

    public Venda()
    {
    }

    public Venda(int codigo)
    {
        this.codigo = codigo;
    }

    public Venda(Date data, double total, int parcelas, Usuario user, Cliente cliente)
    {
        this.data = data;
        this.total = total;
        this.parcelas = parcelas;
        this.user = user;
        this.cliente = cliente;
    }

    public Venda(int codigo, Date data, double total, int parcelas, Usuario user, Cliente cliente)
    {
        this.codigo = codigo;
        this.data = data;
        this.total = total;
        this.parcelas = parcelas;
        this.user = user;
        this.cliente = cliente;
    }

    public int getCodigo()
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

    public Cliente getCliente()
    {
        return cliente;
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
        String sql = "INSERT INTO Venda(ven_codigo, ven_data, ven_total, ven_parcelas, usu_login, cli_codigo) ";
        String values = "VALUES(?, ?, ?, ?, ?, ?)";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql + values);

            statement.setInt(1, codigo);
            statement.setDate(2, data);
            statement.setDouble(3, total);
            statement.setInt(4, parcelas);
            statement.setString(5, user.getLogin());
            statement.setInt(6, cliente.getCodigo());

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
                ItemVenda item = (ItemVenda)itens.get(i);
                
                item.setVenda(this);
                flag = item.insert();
            }
        }
        
        return flag;
    }
    
    private boolean updateSelf()
    {
        String sql = "UPDATE Venda SET ven_total = ?, ven_parcelas = ?, usu_login = ?, cli_codigo = ? WHERE ven_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setDouble(1, total);
            statement.setInt(2, parcelas);
            statement.setString(3, user.getLogin());
            statement.setInt(4, cliente.getCodigo());
            statement.setInt(5, codigo);

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
            if(new Recebimento(this).canEdit())
            {
                flag = new ItemVenda(this).clearItens();

                if(flag)
                {
                    for (int i = 0; flag && i < itens.size(); i++)
                    {
                        ItemVenda item = (ItemVenda)itens.get(i);

                        item.setVenda(this);
                        flag = item.insert();
                    }
                }
            }
        }
        
        return flag;
    }
    
    private boolean deleteSelf()
    {
        String sql = "DELETE FROM Venda WHERE ven_codigo = ?";
        
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
    
    public boolean delete()
    {
        boolean flag = new ItemVenda(this).clearItens();
        boolean flag2 = new Recebimento(this).clearItens();
        
        if(flag && flag2)
            flag = deleteSelf();
        
        return flag;
    }
    
    public Object searchByCodigo()
    {
        Object obj = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Venda WHERE ven_codigo = ?";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, codigo);
            
            ResultSet rs = statement.executeQuery();
            
            if(rs.next())
            {
                Usuario _user = (Usuario) new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0);
                Cliente _cliente = (Cliente) new Cliente(rs.getInt("cli_codigo")).searchByCodigo();
                Venda aqui = new Venda(rs.getInt("ven_codigo"), rs.getDate("ven_data"), rs.getDouble("ven_total"), rs.getInt("ven_parcelas"), _user, _cliente);
                ObservableList<Object> itens = new ItemVenda(aqui).searchByVenda();
                
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
        String sql = "SELECT * FROM Venda";
        
        try
        {
            Connection connection = Banco.getConexao().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            
            while(rs.next())
            {
                Usuario _user = (Usuario) new Usuario(rs.getString("usu_login"), "").searchByLogin().get(0);
                Cliente _cliente = (Cliente) new Cliente(rs.getInt("cli_codigo")).searchByCodigo();
                Venda aqui = new Venda(rs.getInt("ven_codigo"), rs.getDate("ven_data"), rs.getDouble("ven_total"), rs.getInt("ven_parcelas"), _user, _cliente);
                ObservableList<Object> itens = new ItemVenda(aqui).searchByVenda();
                
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
        String str = "Venda - " + codigo + " para " + cliente.getNome() + "\n" +
                     "Efetuada por [" + user.getLogin() + "]\n no valor total de R$ " + total + "\n\nItens da Venda\n";
        
        for (Object item : itens)
        {
            ItemVenda obj = (ItemVenda)item;
            
            str += obj.toString() + " [" + obj.getValor() + "]\n";
        }
        
        return str;
    }
}
