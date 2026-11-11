package com.example.agenda;

public class Contacte {
	private String nom;
	private String cognom;
	private String adreca;
	private String telefon;
	private String email;
	private String data_naixement;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCognom() {
		return cognom;
	}

	public void setCognom(String cognom) {
		this.cognom = cognom;
	}

	public String getAdreca() {
		return adreca;
	}

	public void setAdreca(String adreca) {
		this.adreca = adreca;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getData_naixement() {
		return data_naixement;
	}

	public void setData_naixement(String data_naixement) {
		this.data_naixement = data_naixement;
	}

	public Contacte(String nom, String cognom, String adreca, String telefon, String email, String data_naixement) {
		this.nom = nom;
		this.cognom = cognom;
		this.adreca = adreca;
		this.telefon = telefon;
		this.email = email;
		this.data_naixement = data_naixement;
	}
}