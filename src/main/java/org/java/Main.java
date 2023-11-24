package org.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	
	private static final String url = "jdbc:mysql://localhost:3306/dc_nations";
	private static final String user = "root";
	private static final String pws = "";
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		System.out.print("Filtra tra i paesi inserendo delle lettere: ");
		
		String Filtro = in.nextLine();
		
		try (Connection con = DriverManager.getConnection(url, user, pws)) {  
			
			
			  final String SQL = "SELECT countries.country_id , countries.name ,regions.name ,continents.name "
					 +" FROM countries "
					 +" JOIN regions "
					 +" 	ON countries.region_id = regions.region_id "
					 +" JOIN continents"
					 +" 	ON regions.continent_id = continents.continent_id "
					 +"  WHERE countries.name LIKE ?"
					 + " ORDER BY countries.name  "
					 +" ; ";
			  
			  try (PreparedStatement ps = con.prepareStatement(SQL)){
				  
				  ps.setString(1,  "%" + Filtro + "%");
				  
			    try (ResultSet rs = ps.executeQuery()){
			    	
			    	while (rs.next()) {
			    		
			    		final int ID = rs.getInt(1);
			    		final String NOME = rs.getString(2);
			    		final String NOME_REGIONE = rs.getString(3);
			    		final String NOME_CONTINENTE = rs.getString(4);
			    		
			    		
			    		System.out.println( NOME + " " + ID +" "+   NOME_REGIONE +" " + NOME_CONTINENTE );
			    	}
			    }
			  }
			} catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
			}
	}
	
}
