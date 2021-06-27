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
}
