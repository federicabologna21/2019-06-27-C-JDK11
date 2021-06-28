package it.polito.tdp.crimes.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private SimpleWeightedGraph <String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	public Model() {
		dao = new EventsDao();
		
	}
	
	public List<String> getCategorie (){
		return dao.getCategorie();
	}
	
	public List<Integer> getGiorno(){
		return dao.getGiorno();
	}
	
	public void creaGrafo (String categoria, int giorno) {
		grafo = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(grafo, dao.getVertici(categoria, giorno));
		
		// aggiungo gli archi 
		for (Adiacenza a : dao.getAdiacenze(categoria, giorno)) {
			if(this.grafo.containsVertex(a.getTipo1()) && this.grafo.containsVertex(a.getTipo2())) {
				DefaultWeightedEdge e = this.grafo.getEdge(a.getTipo1(), a.getTipo2());
				
				if (e == null) {
					if (a.getPeso() !=0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
				
					}
				}
			}
		}
	}

	public int getNumVertici() {
		if (this.grafo!=null) {
			return this.grafo.vertexSet().size();
		}
		return 0;
		
	}
	
	public int getNumArchi() {
		if (this.grafo!=null) {
			return this.grafo.edgeSet().size();
		}
		return 0;
		
	}
	
	public double getPesoMediano() {
		double pesoMediano = 0;
		double pesoMax = 0;
		double pesoMin = 0;
		
		for (DefaultWeightedEdge e: this.grafo.edgeSet()) {
			double peso = this.grafo.getEdgeWeight(e);
			
			if (peso > pesoMax ) {
				pesoMax = peso;
			}
			else if (peso < pesoMin ) {
				pesoMin = peso;
			}
		}
		
		pesoMediano = (pesoMin+pesoMax)/2;
		return pesoMediano;
	}
	
	public List<Adiacenza> getArchiPesoInfMediano(){
		
		List<Adiacenza> result = new LinkedList<Adiacenza>();
		
		for (DefaultWeightedEdge e: this.grafo.edgeSet()) {
			double peso = this.grafo.getEdgeWeight(e);
			
			if (peso < this.getPesoMediano()) {
				
				String t1 = this.grafo.getEdgeSource(e);
				String t2 = this.grafo.getEdgeTarget(e);
				Adiacenza a = new Adiacenza (t1, t2, peso);
				result.add(a);
			}
		}
		return result;
		
	}
}
