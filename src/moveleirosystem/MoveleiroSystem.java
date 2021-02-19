/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moveleirosystem;

import JDBC.Banco;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author mega_
 */
public class MoveleiroSystem extends Application
{
    
    public static Stage STAGE;

    @Override
    public void start(Stage stage) throws Exception
    {
        MoveleiroSystem.STAGE = stage;
        
        if (Banco.conectar())
        {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            
            Scene scene = new Scene(root);

            STAGE.setTitle("Moveleiro System");
            STAGE.setScene(scene);
            STAGE.centerOnScreen();
            STAGE.show();
        } 
        else
        {

            if (new Alert(Alert.AlertType.CONFIRMATION, "Banco não encontrado!!!\nDeseja Gerar um novo banco de dados?", ButtonType.YES, ButtonType.NO).showAndWait().get() == ButtonType.YES)
            {
                boolean f = true;
                f = Banco.criarBD("dbmoveleiro");
                f = f && Banco.criarTabelas("dbmoveleiro");

                if (!f)
                {
                    new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                } 
                else
                {
                    new Alert(Alert.AlertType.INFORMATION, "Base gerada com sucesso\nA aplicação precisa ser aberta novamente!!!", ButtonType.OK).showAndWait();
                }
            } 
            else
            {
                new Alert(Alert.AlertType.INFORMATION, "Aplicação será encerrada!!!", ButtonType.OK).showAndWait();
            }

        }

    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
