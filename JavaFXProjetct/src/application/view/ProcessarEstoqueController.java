package application.view;

import javafx.fxml.FXML;

public class ProcessarEstoqueController extends SistemaController {

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
