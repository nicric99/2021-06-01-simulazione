package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public void getAllGenes(Map<String,Genes> idMap){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
				idMap.put(res.getString("GeneID"), genes);
			}
			res.close();
			st.close();
			conn.close();
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	public List<Genes> getVertici(Map<String,Genes> idMap) {
		String sql = "SELECT DISTINCT(g.GeneID) AS gene "
				+ "FROM genes g "
				+ "WHERE g.Essential='Essential' ";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(idMap.get(res.getString("gene")));
				
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}
	public List<Adiacenza> getArchi(Map<String,Genes> idMap){
		String sql = "SELECT i.GeneID1 AS id1,i.GeneID2 AS id2, g1.Chromosome AS cro1,g2.Chromosome AS cro2 ,i.Expression_Corr AS peso "
				+ "FROM interactions i, genes g1, genes g2 "
				+ "WHERE i.GeneID1 <> i.GeneID2 AND i.GeneID1=g1.GeneID AND i.GeneID2=g2.GeneID "
				+ "GROUP BY i.GeneID1,i.GeneID2,g1.Chromosome,g2.Chromosome,i.Expression_Corr ";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getString("id1")) && idMap.containsKey(res.getString("id2"))) {
				int c1=res.getInt("cro1");
				int c2=res.getInt("cro2");
				Double peso;
				if(c1-c2 == 0) {
				peso= Math.abs(res.getDouble("peso"))*2;
				}else {
				peso= Math.abs(res.getDouble("peso"));
				}
				result.add(new Adiacenza(idMap.get(res.getString("id1")),idMap.get(res.getString("id2")),peso));
			}
				
		}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}
	


	
}
