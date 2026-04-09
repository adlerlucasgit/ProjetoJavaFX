package application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.Conexao;
import javafx.scene.control.Alert;

public class MovimentacaoEstoqueModel {
	private int id;
	private int idProd;
	private String nomeProd;
	private String data;
	private int quantidade;
	private String tipo;
	
	public MovimentacaoEstoqueModel(int id, int idProd, String nomeProd, String data, int quantidade, String tipo) {
		this.id = id;
		this.idProd = idProd;
		this.nomeProd = nomeProd;
		this.data = data;
		this.quantidade = quantidade;
		this.tipo = tipo;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getIdFormatado() {
	    return String.format("%06d", this.id);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdProd() {
		return this.idProd;
	}
	
	public String getIdFormatadoProd() {
	    return String.format("%06d", this.idProd);
	}
	
	public void setIdProd(int idProd) {
		this.idProd = idProd;
	}
	
	public String getNomeProd() {
		return this.nomeProd;
	}
	
	public void setNomeProd(String nomeProd) {
		this.nomeProd = nomeProd;
	}
	
	public String getData() {
		return this.data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public int getQuantidade() {
		return this.quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public void InsereMovimentacao() {
		try(Connection conn = Conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("INSERT INTO movimentacaoEstoque "
						+ "(idProd,dataHora,quantidade,tipo) VALUES (?,NOW(),?,?)");){
			int tipo = 0;
			if(this.tipo.equals("Saída")) {
				tipo = 1;
			}
			consulta.setInt(1, this.idProd);
			consulta.setInt(2, this.quantidade);
			consulta.setInt(3, tipo);
			consulta.executeUpdate();
			
			Alert mensagem = new Alert(Alert.AlertType.CONFIRMATION);
			mensagem.setContentText("Estoque processado");
			mensagem.showAndWait();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<MovimentacaoEstoqueModel> HistoricoMovimentacao(int idProd, LocalDate dataInicio, LocalDate dataFim) {
	    List<MovimentacaoEstoqueModel> movimentacao = new ArrayList<MovimentacaoEstoqueModel>();

	    String sql = "SELECT " +
	                 "DATE_FORMAT(m.dataHora, '%d/%m/%Y %H:%i:%s') AS data, " +
	                 "m.id, " +
	                 "m.idProd, " +
	                 "p.nome, " +
	                 "m.quantidade, " +
	                 "CASE " +
	                 "    WHEN m.tipo = 0 THEN 'Entrada' " +
	                 "    WHEN m.tipo = 1 THEN 'Saída' " +
	                 "    ELSE 'Não Informado' " +
	                 "END AS tipo " +
	                 "FROM produto p " +
	                 "INNER JOIN movimentacaoEstoque m ON p.id = m.idProd " +
	                 "WHERE p.id = ? " +
	                 "AND m.dataHora >= ? AND m.dataHora < ?";

	    try (Connection conn = Conexao.getConnection();
	         PreparedStatement consulta = conn.prepareStatement(sql)) {
	    	
	    	if(idProd == 0) {
	    		consulta.setNull(1, java.sql.Types.INTEGER);
	    	}else {
		        consulta.setInt(1, idProd);
	    	}
	    	
	        consulta.setTimestamp(2, java.sql.Timestamp.valueOf(dataInicio.atStartOfDay()));
	        consulta.setTimestamp(3, java.sql.Timestamp.valueOf(dataFim.plusDays(1).atStartOfDay()));
	        
	        ResultSet resultado = consulta.executeQuery();

	        while (resultado.next()) {
	            MovimentacaoEstoqueModel m = new MovimentacaoEstoqueModel(
	                    resultado.getInt("id"),
	                    resultado.getInt("idProd"),
	                    resultado.getString("nome"),
	                    resultado.getString("data"),
	                    resultado.getInt("quantidade"),
	                    resultado.getString("tipo"));
	            setId(resultado.getInt("id"));
	            setNomeProd(resultado.getString("nome"));
	            setIdProd(resultado.getInt("idProd"));
	            setTipo(resultado.getString("tipo"));
	            setData(resultado.getString("data"));
	            setQuantidade(resultado.getInt("quantidade"));
	            movimentacao.add(m);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return movimentacao;
	}

}
