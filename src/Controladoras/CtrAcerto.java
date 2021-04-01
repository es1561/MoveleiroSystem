package Controladoras;

import Classes.Acerto;
import Classes.Caixa;
import Classes.Fornecedor;
import Classes.ItemAcerto;
import Classes.Material;
import Classes.Pagamento;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import moveleirosystem.FXMLDocumentController;

public class CtrAcerto
{
    private static CtrAcerto instance = null;
    
    private CtrAcerto()
    {
        
    }
    
    public static CtrAcerto instancia()
    {
        if(instance == null)
            instance = new CtrAcerto();
            
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
                field = ((Acerto)obj).getCodigo();
            else if(campo.compareTo("data") == 0)
                field = ((Acerto)obj).getData();
            else if(campo.compareTo("usuario") == 0)
                field = ((Acerto)obj).getUser().getLogin();
            else if(campo.compareTo("obs") == 0)
                field = ((Acerto)obj).getObs();
            else if(campo.compareTo("text") == 0)
                field = ((Acerto)obj).toText();
        }
        
        return field;
    }
    
    public Object makeItem(Object mat, int quant)
    {
        return new ItemAcerto((Material)mat, quant);
    }
    
    public boolean find(Object obj, ObservableList<Object> itens)
    {
        boolean flag = false;
        ItemAcerto mat = (ItemAcerto)obj;
        
        for(int i = 0; !flag && i < itens.size(); i++)
        {
            ItemAcerto item = (ItemAcerto)itens.get(i);
            
            if(item.getMaterial().getCodigo() == mat.getMaterial().getCodigo())
                flag = true;
        }
        
        return flag;
    }
    
    public void add(Object obj, ObservableList<Object> itens)
    {
        boolean flag = true;
        ItemAcerto mat = (ItemAcerto)obj;
        
        for(int i = 0; flag && i < itens.size(); i++)
        {
            ItemAcerto item = (ItemAcerto)itens.get(i);
            
            if(item.getMaterial().getCodigo() == mat.getMaterial().getCodigo())
            {
                flag = false;
                item.setQuant(item.getQuant() + mat.getQuant());
            }
        }
        
        if(flag)
            itens.add(obj);
    }

    public ObservableList<Object> getItens(Object obj)
    {
        return ((Acerto)obj).getItens();
    }
    
    public double totalParcelas(ObservableList<Object> itens)
    {
        double total = 0;
        
        for (Object item : itens)
        {
            total += ((Pagamento)item).getValor();
        }
        
        return total;
    }
    
    public boolean insert(String obs, ObservableList<Object> itens)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Acerto obj = new Acerto(Date.valueOf(LocalDate.now()), (Usuario)FXMLDocumentController.USER, obs);
                obj.setItens(itens);

                flag = obj.insert();

                if(flag)
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao registrar Acerto!");
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
    
    public boolean update(int codigo, String obs, ObservableList<Object> itens)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Acerto obj = new Acerto(codigo, obs);
                obj.setItens(itens);

                flag = obj.update();

                if(flag)
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao alterar Aquisição!");
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
        
        try
        {
            if(Banco.isConectado())
            {                   
                Acerto obj = new Acerto(codigo);

                flag = obj.delete();

                if(flag)
                    Banco.getConexao().getConnection().commit();
                else
                {
                    Banco.getConexao().getConnection().rollback();
                    throw new SQLException("Erro ao deletar Aquisição!");
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
    
    public ObservableList<Object> searchAll()
    {
        ObservableList<Object> list = FXCollections.observableArrayList();
        
        try
        {
            if(Banco.isConectado())
                list = new Acerto().searchAll();
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
