package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class CadastroClientesController extends SistemaController{

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
	public void Voltar() {
		carregarTela("Sistema.fxml");
	}
	
	@FXML
	public void ProcessarEstoque() {
		carregarTela("ProcessarEstoque.fxml");
	}
}
