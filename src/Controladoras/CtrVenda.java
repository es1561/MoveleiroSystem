package Controladoras;

import Classes.Venda;
import Classes.Caixa;
import Classes.Cliente;
import Classes.ItemVenda;
import Classes.Material;
import Classes.Recebimento;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import moveleirosystem.FXMLDocumentController;

public class CtrVenda
{
    private static CtrVenda instance = null;
    
    private CtrVenda()
    {
        
    }
    
    public static CtrVenda instancia()
    {
        if(instance == null)
            instance = new CtrVenda();
            
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
                field = ((Venda)obj).getCodigo();
            else if(campo.compareTo("data") == 0)
                field = ((Venda)obj).getData();
            else if(campo.compareTo("cliente") == 0)
                field = ((Venda)obj).getCliente().toString();
            else if(campo.compareTo("obj_cli") == 0)
                field = ((Venda)obj).getCliente();
            else if(campo.compareTo("parc") == 0)
                field = ((Venda)obj).getParcelas();
            else if(campo.compareTo("total") == 0)
                field = ((Venda)obj).getTotal();
            else if(campo.compareTo("usuario") == 0)
                field = ((Venda)obj).getUser().getLogin();
            else if(campo.compareTo("text") == 0)
                field = ((Venda)obj).toText();
        }
        
        return field;
    }
    
    public Object makeItem(Object mat, int quant, double valor)
    {
        return new ItemVenda((Material)mat, quant, valor);
    }
    
    public boolean find(Object obj, ObservableList<Object> itens)
    {
        boolean flag = false;
        ItemVenda mat = (ItemVenda)obj;
        
        for(int i = 0; !flag && i < itens.size(); i++)
        {
            ItemVenda item = (ItemVenda)itens.get(i);
            
            if(item.getMaterial().getCodigo() == mat.getMaterial().getCodigo())
                flag = true;
        }
        
        return flag;
    }
    
    public void add(Object obj, ObservableList<Object> itens)
    {
        boolean flag = true;
        ItemVenda mat = (ItemVenda)obj;
        
        for(int i = 0; flag && i < itens.size(); i++)
        {
            ItemVenda item = (ItemVenda)itens.get(i);
            
            if(item.getMaterial().getCodigo() == mat.getMaterial().getCodigo() && item.getValor() == mat.getValor())
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
        return ((Venda)obj).getItens();
    }
    
    public double total(ObservableList<Object> itens)
    {
        double total = 0;
        
        for (Object item : itens)
        {
            total += ((ItemVenda)item).getTotal();
        }
        
        return total;
    }
    
    public double totalParcelas(ObservableList<Object> itens)
    {
        double total = 0;
        
        for (Object item : itens)
        {
            total += ((Recebimento)item).getValor();
        }
        
        return total;
    }
    
    public boolean insert(Object cliente, int parc, Double total, ObservableList<Object> itens, ObservableList<Object> parcelas)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {               
                    Venda obj = new Venda(Banco.getConexao().getMaxPK("Venda", "ven_codigo") + 1 ,Date.valueOf(LocalDate.now()), total, parc, (Usuario)FXMLDocumentController.USER, (Cliente)cliente);
                    obj.setItens(itens);
                    
                    flag = obj.insert();

                    for (int i = 0; flag && i < parcelas.size(); i++)
                    {
                        Recebimento par = (Recebimento)parcelas.get(i);
                        
                        par.setVenda(obj);
                        flag = par.insert();
                    }
                    
                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        
                        if(Banco.getConexao().getMensagemErro().isEmpty())
                            throw new SQLException("Erro ao registrar Venda!");
                    }
                }
                else
                    throw new SQLException("Caixa Fechado...");
                
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
    
    public boolean update(int codigo, Object cliente, int parc, Double total, ObservableList<Object> itens, ObservableList<Object> parcelas)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {               
                    if(new Recebimento(new Venda(codigo)).canEdit())
                    {
                        Venda obj = new Venda(codigo, Date.valueOf(LocalDate.now()), total, parc, (Usuario)FXMLDocumentController.USER, (Cliente)cliente);
                        obj.setItens(itens);

                        flag = obj.update();

                        flag = new Recebimento(obj).clearItens();
                        
                        for (int i = 0; flag && i < parcelas.size(); i++)
                        {
                            Recebimento par = (Recebimento)parcelas.get(i);

                            par.setVenda(obj);
                            flag = par.insert();
                        }
                        
                        if(flag)
                            Banco.getConexao().getConnection().commit();
                        else
                        {
                            Banco.getConexao().getConnection().rollback();
                            throw new SQLException("Erro ao alterar Venda!");
                        }
                    }
                    else
                        throw new SQLException("Venda em Andamento...");
                }
                else
                    throw new SQLException("Caixa Fechado...");
                
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
                if(new Recebimento(new Venda(codigo)).canEdit())
                {               
                    Venda obj = new Venda(codigo);
                    
                    flag = obj.delete();

                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Erro ao excluir Venda!");
                    }
                }
                else
                    throw new SQLException("Venda em Andamento...");
                
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
                list = new Venda().searchAll();
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
