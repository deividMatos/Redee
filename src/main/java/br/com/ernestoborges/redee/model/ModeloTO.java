package br.com.ernestoborges.redee.model;

import java.util.List;

public class ModeloTO {

	private String pasta;
	private String modelo;
	private List<String> textos;
	private String conjuntoTextos;

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public List<String> getTextos() {
		return textos;
	}

	public void setTextos(List<String> textos) {
		this.textos = textos;
	}

	public String getPasta() {
		return pasta;
	}

	public void setPasta(String pasta) {
		this.pasta = pasta;
	}

	public String getConjuntoTextos() {
		return conjuntoTextos;
	}

	public void setConjuntoTextos(String conjuntoTextos) {
		this.conjuntoTextos = conjuntoTextos;
	}

}
