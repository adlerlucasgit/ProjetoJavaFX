package application.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SistemaController {

    @FXML
    private MenuItem Clientes;

    @FXML
    private MenuItem ProcessarEstoque;

    @FXML
    private MenuItem Produtos;

    @FXML
    private MenuItem itemSair;
    
    @FXML
    private AnchorPane ap;

	public void carregarTela(String fxmlfile) {
		try {
			Parent fxml = FXMLLoader.load(getClass().getResource(fxmlfile));
			ap.getChildren().clear();
			ap.getChildren().add(fxml);
			
			AnchorPane.setTopAnchor(fxml,0.0);
			AnchorPane.setBottomAnchor(fxml,0.0);
			AnchorPane.setLeftAnchor(fxml,0.0);
			AnchorPane.setRightAnchor(fxml,0.0);
			Scene cena =  ap.getScene();
			
			if (cena!=null) {
				Stage stage = (Stage) cena.getWindow();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}	
		
	}
    
	@FXML
	private void initialize() {
		
	}
	
	@FXML
	public void Sair() {
		System.exit(0);
	}
	
	@FXML
	public void CadastroProduto() {
		carregarTela("CadastroProdutos.fxml");
	}
	
	@FXML
	public void CadastroCliente() {
		carregarTela("CadastroClientes.fxml");
	}
	
	@FXML
	public void ProcessarEstoque() {
		carregarTela("ProcessarEstoque.fxml");
	}
}
