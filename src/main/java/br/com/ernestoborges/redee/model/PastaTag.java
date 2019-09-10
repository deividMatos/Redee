package br.com.ernestoborges.redee.model;

import java.util.List;

public class PastaTag {

	private String pasta;
	private List<Valores> valores;

	public String getPasta() {
		return pasta;
	}

	public void setPasta(String pasta) {
		this.pasta = pasta;
	}

	public List<Valores> getValores() {
		return valores;
	}

	public void setValores(List<Valores> valores) {
		this.valores = valores;
	}

}
