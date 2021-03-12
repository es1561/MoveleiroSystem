package moveleirosystem;

import Controladoras.CtrUsuario;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FXMLDocumentController implements Initializable
{
    public static FXMLDocumentController APP;
    public static VBox PANE;
    public static Object USER;
    
    @FXML
    private Menu menu_acesso;
    @FXML
    private MenuItem mitem_a_sair;
    @FXML
    private Menu menu_gerenciar;
    @FXML
    private MenuItem mitem_g_cliente;
    @FXML
    private MenuItem mitem_g_fornecedor;
    @FXML
    private MenuItem mitem_g_material;
    @FXML
    private Menu menu_operacao;
    @FXML
    private MenuItem mitem_o_acerto;
    @FXML
    private MenuItem mitem_o_aquisicao;
    @FXML
    private MenuItem mitem_o_busca;
    @FXML
    private MenuItem mitem_o_pagamento;
    @FXML
    private MenuItem mitem_o_recebimento;
    @FXML
    private MenuItem mitem_o_venda;
    @FXML
    private Menu menu_relatorio;
    @FXML
    private VBox vbox_center;
    @FXML
    private MenuItem mitem_a_backup;
    @FXML
    private MenuItem mitem_g_caixa;
    @FXML
    private MenuItem mitem_g_usuario;
    @FXML
    private MenuItem mitem_g_despesa;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if(CtrUsuario.instancia().searchAll().isEmpty())
            loadNode("FXMLTelaConfi.fxml");
        else
            loadNode("FXMLLogin.fxml");
        
        APP = this;
        PANE = vbox_center;
        disableMenu(true);
    }    

    private void loadNode(String fxml)
    {
        try
        {   
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/Telas/" + fxml));
            vbox_center.getChildren().clear();
            vbox_center.getChildren().add(newLoadedPane);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private void disableMenu(boolean flag)
    {
        menu_acesso.setDisable(flag);
        menu_gerenciar.setDisable(flag);
        menu_operacao.setDisable(flag);
        menu_relatorio.setDisable(flag);
    }
    
    private void disableAcesso(boolean flag)
    {
        mitem_a_backup.setDisable(flag);
        mitem_a_sair.setDisable(flag);
    }
    
    private void disableGerenciar(boolean flag)
    {
        mitem_g_caixa.setDisable(flag);
        mitem_g_cliente.setDisable(flag);
        mitem_g_usuario.setDisable(flag);
        mitem_g_fornecedor.setDisable(flag);
        mitem_g_material.setDisable(flag);
    }
    
    private void disableOperacao(boolean flag)
    {
        mitem_o_acerto.setDisable(flag);
        mitem_o_aquisicao.setDisable(flag);
        mitem_o_busca.setDisable(flag);
        mitem_o_pagamento.setDisable(flag);
        mitem_o_recebimento.setDisable(flag);
        mitem_o_venda.setDisable(flag);
    }
    
    private void disableRelatorio(boolean flag)
    {
        
    }
    
    public void disableLogin(int nivel)
    {
        switch(nivel)
        {
            case 1://usuario
                disableMenu(false);
                disableAcesso(true);
                mitem_a_sair.setDisable(false);
                disableGerenciar(true);
                mitem_g_cliente.setDisable(false);
                mitem_g_material.setDisable(false);
                mitem_g_despesa.setDisable(false);
                disableOperacao(false);
                mitem_o_aquisicao.setDisable(true);
                disableRelatorio(true);
            break;
            
            case 2://gerente
                disableMenu(false);
                disableAcesso(true);
                mitem_a_sair.setDisable(false);
                disableGerenciar(false);
                mitem_g_usuario.setDisable(true);
                mitem_g_fornecedor.setDisable(true);
                disableOperacao(false);
                disableRelatorio(false);
            break;
            
            case 3://ADM
                disableMenu(false);
                disableAcesso(false);
                disableGerenciar(false);
                disableOperacao(false);
                disableRelatorio(false);
            break;
        }
        
        MoveleiroSystem.STAGE.setTitle("Moveleiro System - Usuario: " + CtrUsuario.instancia().getField(USER, "nome") + "[" + CtrUsuario.instancia().getField(USER, "acesso") + "]");
        vbox_center.getChildren().clear();
    }
    
    @FXML
    private void ClickMenuItemSair(ActionEvent event)
    {
        loadNode("FXMLLogin.fxml");
        MoveleiroSystem.STAGE.setTitle("Moveleiro System");
        USER = null;
    }

    @FXML
    private void ClickGerenciarCliente(ActionEvent event)
    {
        loadNode("FXMLCliente.fxml");
    }

    @FXML
    private void ClickGerenciarDespesa(ActionEvent event)
    {
        loadNode("FXMLDespesa.fxml");
    }
    
    @FXML
    private void ClickGerenciarFornecedor(ActionEvent event)
    {
        loadNode("FXMLFornecedor.fxml");
    }

    @FXML
    private void ClickGerenciarMaterial(ActionEvent event)
    {
        loadNode("FXMLMaterial.fxml");
    }

    @FXML
    private void ClickOperacaoAcerto(ActionEvent event)
    {
    }

    @FXML
    private void ClickOperacaoAquisicao(ActionEvent event)
    {
        loadNode("FXMLAquisicao.fxml");
    }

    @FXML
    private void ClickOperacaoBusca(ActionEvent event)
    {
    }

    @FXML
    private void ClickOperacaoPagamento(ActionEvent event)
    {
        loadNode("FXMLPagamento.fxml");
    }

    @FXML
    private void ClickOperacaoRecebimento(ActionEvent event)
    {
    }

    @FXML
    private void ClickOperacaoVenda(ActionEvent event)
    {
    }

    @FXML
    private void ClickMenuItemBackup(ActionEvent event)
    {
    }

    @FXML
    private void ClickGerenciarCaixa(ActionEvent event)
    {
        loadNode("FXMLCaixa.fxml");
    }

    @FXML
    private void ClickGerenciarUsuario(ActionEvent event)
    {
        loadNode("FXMLUsuario.fxml");
    }

    
    
}
