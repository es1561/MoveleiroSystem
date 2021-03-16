package Controladoras;

import Classes.Aquisicao;
import Classes.Caixa;
import Classes.Categoria;
import Classes.Despesa;
import Classes.Fornecedor;
import Classes.ItemAquisicao;
import Classes.Material;
import Classes.Pagamento;
import Classes.TipoDespesa;
import Classes.Usuario;
import JDBC.Banco;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import moveleirosystem.FXMLDocumentController;

public class CtrAquisicao
{
    private static CtrAquisicao instance = null;
    
    private CtrAquisicao()
    {
        
    }
    
    public static CtrAquisicao instancia()
    {
        if(instance == null)
            instance = new CtrAquisicao();
            
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
                field = ((Aquisicao)obj).getCodigo();
            else if(campo.compareTo("data") == 0)
                field = ((Aquisicao)obj).getData();
            else if(campo.compareTo("forn") == 0)
                field = ((Aquisicao)obj).getForn().toString();
            else if(campo.compareTo("obj_forn") == 0)
                field = ((Aquisicao)obj).getForn();
            else if(campo.compareTo("parc") == 0)
                field = ((Aquisicao)obj).getParcelas();
            else if(campo.compareTo("total") == 0)
                field = ((Aquisicao)obj).getTotal();
            else if(campo.compareTo("usuario") == 0)
                field = ((Aquisicao)obj).getUser().getLogin();
            else if(campo.compareTo("text") == 0)
                field = ((Aquisicao)obj).toText();
        }
        
        return field;
    }
    
    public Object makeItem(Object mat, int quant, double valor)
    {
        return new ItemAquisicao((Material)mat, quant, valor);
    }
    
    public boolean find(Object obj, ObservableList<Object> itens)
    {
        boolean flag = false;
        ItemAquisicao mat = (ItemAquisicao)obj;
        
        for(int i = 0; !flag && i < itens.size(); i++)
        {
            ItemAquisicao item = (ItemAquisicao)itens.get(i);
            
            if(item.getMaterial().getCodigo() == mat.getMaterial().getCodigo())
                flag = true;
        }
        
        return flag;
    }
    
    public void add(Object obj, ObservableList<Object> itens)
    {
        boolean flag = true;
        ItemAquisicao mat = (ItemAquisicao)obj;
        
        for(int i = 0; flag && i < itens.size(); i++)
        {
            ItemAquisicao item = (ItemAquisicao)itens.get(i);
            
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
        return ((Aquisicao)obj).getItens();
    }
    
    public double total(ObservableList<Object> itens)
    {
        double total = 0;
        
        for (Object item : itens)
        {
            total += ((ItemAquisicao)item).getTotal();
        }
        
        return total;
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
    
    public boolean insert(String codigo, Object forn, int parc, Double total, ObservableList<Object> itens, ObservableList<Object> parcelas)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {               
                    Aquisicao obj = new Aquisicao(codigo, Date.valueOf(LocalDate.now()), total, parc, (Usuario)FXMLDocumentController.USER, (Fornecedor)forn);
                    obj.setItens(itens);
                    
                    flag = obj.insert();

                    for (int i = 0; flag && i < parcelas.size(); i++)
                    {
                        Pagamento par = (Pagamento)parcelas.get(i);
                        
                        par.setAquisicao(obj);
                        flag = par.insert();
                    }
                    
                    if(flag)
                        Banco.getConexao().getConnection().commit();
                    else
                    {
                        Banco.getConexao().getConnection().rollback();
                        throw new SQLException("Erro ao registrar Aquisição!");
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
    
    public boolean update(String codigo, Object forn, int parc, Double total, ObservableList<Object> itens, ObservableList<Object> parcelas)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {
                Object oc = new Caixa(Date.valueOf(LocalDate.now())).searchByToday();
                
                if(oc != null)
                {               
                    if(new Pagamento(new Aquisicao(codigo)).canEdit())
                    {
                        Aquisicao obj = new Aquisicao(codigo, Date.valueOf(LocalDate.now()), total, parc, (Usuario)FXMLDocumentController.USER, (Fornecedor)forn);
                        obj.setItens(itens);

                        flag = obj.update();

                        flag = new Pagamento(obj).clearItens();
                        
                        for (int i = 0; flag && i < parcelas.size(); i++)
                        {
                            Pagamento par = (Pagamento)parcelas.get(i);

                            par.setAquisicao(obj);
                            flag = par.insert();
                        }
                        
                        if(flag)
                            Banco.getConexao().getConnection().commit();
                        else
                        {
                            Banco.getConexao().getConnection().rollback();
                            throw new SQLException("Erro ao alterar Aquisição!");
                        }
                    }
                    else
                        throw new SQLException("Aquisição em Andamento...");
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
    
    public boolean delete(String codigo)
    {
        boolean flag;        
        
        try
        {
            if(Banco.isConectado())
            {                   
                if(new Pagamento(new Aquisicao(codigo)).canEdit())
                {               
                    Aquisicao obj = new Aquisicao(codigo);
                    
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
                    throw new SQLException("Aquisição em Andamento...");
                
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
                list = new Aquisicao().searchAll();
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
