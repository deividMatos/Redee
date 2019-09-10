package br.com.ernestoborges.redee.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ernestoborges.redee.model.ModeloTO;
import br.com.ernestoborges.redee.model.PastaTag;
import br.com.ernestoborges.redee.model.Valores;
import br.com.ernestoborges.redee.repository.RedeeRepository;

/**
 * Classe que terá as regras de negocios após a recepção do post
 */
@Service
public class RedeeService {

	@Autowired
	private RedeeRepository repository;

	/**
	 * Metodo que aplicará todas regras de negocios predefinidas na solicitacao de
	 * cadastro de tags
	 * 
	 * @param solicitacao Recebera o objeto JSONObject como parametro para definir
	 *                    quais as tags que serão inseridas
	 * @since 2019 09 02
	 * @return retorna uma lista com os json de cada tag da lista
	 * @throws Exception captura de possiveis erros na conversao para json
	 */
	public List<JSONObject> trataTag(JSONObject solicitacao) throws Exception {
		ObjectMapper m = new ObjectMapper();
		PastaTag tag = m.readValue(solicitacao.toString(), PastaTag.class);
		List<JSONObject> listRetorno = new ArrayList<>();
		StringBuilder json = new StringBuilder();

		// VERIFICA SE TAG PASTA VEIO PREENCHIDA
		if (StringUtils.isNotEmpty(tag.getPasta().trim())) {

			// FARA VERIFICACAO ENTRE TODAS AS TAGS ENVIADAS PARA ESTA PASTA
			for (Valores v : tag.getValores()) {
				json = new StringBuilder();
				json.append(" {");
				json.append(" \"pasta\":\"" + tag.getPasta() + "\" ,");
				json.append(" \"tag\":\"" + v.getTag() + "\" ,");

				// VERIFICA SE HA CAMPOS IMPORTANTES SEM VALORES DEFINIDOS
				if (StringUtils.isNotEmpty(v.getTag().trim()) && StringUtils.isNotEmpty(v.getValor().trim())) {
					// VERIFICA SE EXISTE A TAG E TRAS O SEQUENCIAL, CASO NAO EXISTA TRAZ ZERO
					int sequencial = repository.verificaJaExiste(tag, v);
					// VERIFICA TAGS OBRIGATORIAS, POIS ESTAS NAO PODEM SER ALTERADAS OU INSERIDAS,
					// ESTAS JA ESTAO CONTIDAS NO BANCO
					if (v.getTag().equals("ficha") || v.getTag().equals("numero_processo") || v.getTag().equals("pasta")
							|| v.getTag().equals("acao") || v.getTag().equals("vara") || v.getTag().equals("juizo")
							|| v.getTag().equals("comarca") || v.getTag().equals("uf")
							|| v.getTag().equals("valor_causa") || v.getTag().equals("honorarios_periciais")
							|| v.getTag().equals("valor_pag_acordo") || v.getTag().equals("valor_pag_condenacao")
							|| v.getTag().equals("valor_pag_administrativo")
							|| v.getTag().equals("data_pag_administrativo") || v.getTag().equals("reu")
							|| v.getTag().equals("cpf_cnpj_reu") || v.getTag().equals("autor1")
							|| v.getTag().equals("cpf_cnpj_autor1") || v.getTag().equals("categoria_do_veiculo")
							|| v.getTag().equals("data_sinistro") || v.getTag().equals("natureza_sinistro")
							|| v.getTag().equals("numero_sinistro") || v.getTag().equals("data_distribuicao")
							|| v.getTag().equals("processo_virtual")) {

						json.append("\"obs\":\" Tag fixa já existe com essa nomenclatura. \"");
						json.append(" }");
						listRetorno.add(new JSONObject(json));
						System.out.println(json);

					} else {
						// SE NAO FOR TAG OBRIGATORIA, CRIAR OU ALTERAR SE JA EXISTIR
						if (repository.verificaJaExiste(tag, v) > 0) {
							System.out.println("alter with sucess");
//							 repository.alteraTag(sequencial, v);
							json.append("\"obs\":\" Item alterado com sucesso. \"");
							json.append(" }");
							listRetorno.add(new JSONObject(json.toString()));
							System.out.println(json.toString());
						} else {
							System.out.println("insert with sucess");
//							 repository.insertTag(tag, v);
							json.append("\"obs\":\"Item Inserido com sucesso.\"");
							json.append(" }");
							JSONObject teste = new JSONObject(json.toString());
							listRetorno.add(teste);
//							listRetorno.add(new JSONObject(json));
							System.out.println(json.toString());
						}
					}
				} else {
					json.append(" {");
					json.append("{\"pasta\":\"" + tag.getPasta() + "\" ,");
					json.append("\"tag\":\"" + v.getTag() + "\" ,");
					json.append("\"obs\":\" Campos importantes estão com valor Null ou vazio. \"");
					json.append(" }");
					listRetorno.add(new JSONObject(json.toString()));
					System.out.println(json.toString());
				}
			}
		} else {
			json.append(" {");
			json.append("{\"pasta\":\"" + tag.getPasta() + "\" ,");
			json.append("\"obs\":\" Campos importantes estão com valor Null ou vazio. \"");
			json.append(" }");
			listRetorno.add(new JSONObject(json.toString()));

			System.out.println(json.toString());
		}

		return listRetorno;

	}

	/**
	 * Metodo fara o tratamento da tese e dara o retorno em string de acordo com o
	 * que ocorrer
	 * 
	 * @param json parametro que define o objeto json que sera tratado
	 * @return retornara uma String com o resultado do tratamento
	 * @since 2019 09 02
	 */
	public String trataTese(JSONObject json) {

		try {
			ObjectMapper m = new ObjectMapper();
			ModeloTO modelo = m.readValue(json.toString(), ModeloTO.class);
			if (modelo.getTextos() != null) {
				for (String s : modelo.getTextos()) {
					repository.insertTese(modelo, s);
				}
			}
		} catch (Exception e) {
			ModeloTO modelo = new ModeloTO();
			repository.insertTese(modelo, "Erro: " + e.getMessage());
			return "Erro Inserção de tese: " + e.getMessage();
		}
		return "Inserção feita com sucesso!";

	}
}
