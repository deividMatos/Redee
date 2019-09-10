package br.com.ernestoborges.redee.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ernestoborges.redee.service.RedeeService;

/**
 * INFORMACOES DE SEGURANÇA COMO SENHA E PATH'S REAIS, FORAM REMOVIDOS POR QUESTAO DE SEGURANÇA, 
 * PROJETO ESTA APENAS PARA APRESENTAÇÃO DO MODO DE PROGRAMAÇÃO E PARA CONHECIMENTO DE TERCEIROS SOBRE MIM.
 */
@RestController
@RequestMapping(produces = "application/json")
public class PostController {

	/**
	 * Resposta de erro para caso seja buscado sem path definido (apenas: redee/)
	 * @return retorna mensagem com erro de path incompleto
	 */
	@GetMapping("/")
	public String index() {
		return "Por favor, coloque o path do post requerido e envie as informações corretamente!";
	}

	@Autowired
	private RedeeService service;

	/**
	 * Metodo de post para inserção de tese no sistema
	 * 
	 * @param is objeto recebido com tipagem Json
	 * @return retorna o resultado da inseção
	 * @throws Exception tratamento de possivel erro na conversao para JSONObject
	 */
	@PostMapping("/path/teste1")
	public String getOutput(InputStream is) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String retorno = br.readLine();
		JSONObject json = new JSONObject(retorno);
		// MANDA PARA CLASSE CONTROLLER QUE CONTEM AS REGRAS DE NEGÓCIO
		return service.trataTese(json);

	}

	/**
	 * Metodo de post para inserção de tag no sistema
	 * 
	 * @param is objeto recebido com tipagem Json
	 * @return retorna o resultado da inseção
	 * @since 2019 09 02
	 * @throws Exception tratamento de possivel erro na conversao para JSONObject
	 */
	@PostMapping("/path/teste2")
	public String setTag(InputStream is) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String retorno = br.readLine();
		JSONObject json = new JSONObject(retorno);
		// MANDA PARA CLASSE CONTROLLER QUE CONTEM AS REGRAS DE NEGÓCIO
		List<JSONObject> resposta = service.trataTag(json);
		return resposta.toString();
	}

}
