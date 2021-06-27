package it.polito.tdp.genes.model;

public class testModel {

	public static void main(String[] args) {
	Model model= new Model();
	model.creaGrafo();
	System.out.println(model.getNVertici()+"\n");
	System.out.println(model.getNArchi());

	}

}
