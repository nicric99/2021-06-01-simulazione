package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;



public class Model {
	private int durata=36;
	private Integer N;
	private Genes sorgente;
	// variabile risultato
	private Map<Integer,Genes> result;

	public void inizializza(Integer n, Genes gene) {
		this.sorgente=gene;
		this.N=n;
		this.result= new HashMap<>();
		for(int i=0; i<n;i++) {
			this.result.put(i, gene);
		}
	}
	
	private  Graph<Genes, DefaultWeightedEdge> grafo;
	private Map<String,Genes> idMap;
	private GenesDao dao;
	public Model() {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap=new HashMap<>();
		dao=new GenesDao();
	}
	public void creaGrafo() {
		dao.getAllGenes(idMap);
		Graphs.addAllVertices(this.grafo, dao.getVertici(idMap));
		for(Adiacenza a: dao.getArchi(idMap)) {
			if(this.grafo.vertexSet().contains(a.getG1()) && this.grafo.vertexSet().contains(a.getG2())) {
				Graphs.addEdge(this.grafo,a.getG1(), a.getG2(), a.getPeso());
			}
		}
		
	}
	public Integer getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public Integer getNArchi(){
		return this.grafo.edgeSet().size();
	}
	public Set<Genes> getVertici(){
		return this.grafo.vertexSet();
	}
	public List<GenePeso> getAdiacenti(Genes gene) {
		List<GenePeso> result= new ArrayList<>();
		for(Genes g: Graphs.neighborListOf(this.grafo, gene)) {
			DefaultWeightedEdge e= this.grafo.getEdge(gene, g);
			Double peso= this.grafo.getEdgeWeight(e);
			result.add(new GenePeso(g,peso));
			
		}
		Collections.sort(result);
		Collections.reverse(result);
		return result;
		
	}
	public void simula(Integer n,Genes gene) {
		inizializza(n,gene);
	}
	
	
	public void run() {
		int counter=0;
		while(counter<36) {
				for(int i=0; i<N;i++) {
						int num = (int)(Math.random()*100)+1;
							if(num<=70) {
								Genes nuovo= getNuovoGene(this.result.get(i));
								if(nuovo!=null) {
								this.result.remove(i);
								this.result.put(i, nuovo);
								}
							}
				}
				counter++;
		}
	}
	public Genes getNuovoGene(Genes gene) {
		int totale= this.getNVicini(gene);
		Double somma=this.getPesoVicini(gene);
		Map<Genes,Integer> parziali= new HashMap<>();
		int tot=0;
		
		for(Genes g: Graphs.neighborListOf(this.grafo, gene)) {
			DefaultWeightedEdge e= this.grafo.getEdge(gene, g);
			double peso=this.grafo.getEdgeWeight(e);
			double nparziale= (peso/somma)*100;
			tot+=nparziale;
			parziali.put(g,tot);
		}
		// ora che ho parziali mi genero un numero
		int numero =(int) ((Math.random()*100)+1);
		int precedente=0;
		for(Genes g:parziali.keySet()) {
			if(parziali.get(g)>=numero && numero>=precedente) {
				precedente= parziali.get(g)+1;
				return g;
			}
		}
		return null;
	}
	
	
	public Double getPesoVicini(Genes gene) {
		Double somma= (double) 0;
		for(Genes g: Graphs.neighborListOf(this.grafo,gene)) {
			DefaultWeightedEdge e= this.grafo.getEdge(gene, g);
			double peso=this.grafo.getEdgeWeight(e);
			somma+=peso;
		}
		return somma;
	}
	public Integer getNVicini(Genes gene) {
		return Graphs.neighborListOf(this.grafo, gene).size();
	}
	public Map<Integer,Genes> getRisultato(){
		return this.result;
	}
}
