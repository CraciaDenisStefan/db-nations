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
			    	System.out.printf("%-5s %-20s %-20s %-20s%n", "ID", "Nome", "Nome Regione", "Nome Continente");
			    	while (rs.next()) {
			    		
			    		final int ID = rs.getInt(1);
			    		final String NOME = rs.getString(2);
			    		final String NOME_REGIONE = rs.getString(3);
			    		final String NOME_CONTINENTE = rs.getString(4);
			    		
			    		
			    		System.out.printf("%-5d %-20s %-20s %-20s%n", ID, NOME, NOME_REGIONE, NOME_CONTINENTE);
		                    
			    	}
			    	
			   	 System.out.print("Inserisci l'ID di un paese: ");
                 int countryID = in.nextInt();

                 String nomePaese = getNomePaese(con, countryID);

                 System.out.println("Dettagli per il paese: " + nomePaese);
                 ricercaLingue(con, countryID);
                 ricercaStatistiche(con, countryID);
                 
			    }
			  }
			} catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
			}
	}
	
	
	private static void ricercaLingue(Connection con, int countryID) {
        try {
            final String selezionaLinguaSQL = "SELECT language"
					 +" FROM country_languages "
					 +" JOIN languages "
					 +" 	ON country_languages.language_id = languages.language_id "
					 +"  WHERE country_languages.country_id = ?"
					 +" ; ";
            try (PreparedStatement psLingue = con.prepareStatement(selezionaLinguaSQL)) {

                psLingue.setInt(1, countryID);

                try (ResultSet rsLingue = psLingue.executeQuery()) {

                	  System.out.print("Lingue:");
                	  
                	  
                    while (rsLingue.next()) {
                    	
                    	final String LINGUA = rsLingue.getString(1);
                    	
                        System.out.print( LINGUA + " ");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca delle lingue: " + e.getMessage());
        }
    }
	
	private static void ricercaStatistiche(Connection con, int countryID) {
        try {
            final String selezionaStatisticheSQL = "SELECT country_stats.year, country_stats.population, country_stats.gdp "
					 +" FROM country_stats "
					 +"  WHERE country_id = ?"
					 +" ORDER BY year DESC LIMIT 1;"
					 +" ; ";
            
            try (PreparedStatement psStatistiche = con.prepareStatement(selezionaStatisticheSQL)) {

            	psStatistiche.setInt(1, countryID);

                try (ResultSet rsStatistiche = psStatistiche.executeQuery()) {

                	 System.out.println("\nStatistiche più recenti: ");

                     while (rsStatistiche.next()) {
                    	 
                    	 final int ANNO = rsStatistiche.getInt(1);
                    	 final String POPOLAZIONE = rsStatistiche.getString(2);
                    	 final String GDP = rsStatistiche.getString(3);
                    	 
                         System.out.println("Anno: " + ANNO
                                 + "\nPopolazione: " + POPOLAZIONE
                                 + "\nGDP: " + GDP );
                     }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca delle statistiche: " + e.getMessage());
        }
    }
	
	private static String getNomePaese(Connection con, int countryID) {
	    try {
	        final String selezionaNomePaeseSQL = "SELECT name "
	        		+ "FROM countries"
	        		+ " WHERE country_id = ?";
	        
	        try (PreparedStatement psNomePaese = con.prepareStatement(selezionaNomePaeseSQL)) {
	        	
	        	psNomePaese.setInt(1, countryID);

	            try (ResultSet rsNomePaese = psNomePaese.executeQuery()) {
	                if (rsNomePaese.next()) {
	                    return rsNomePaese.getString(1);
	                }
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Errore durante il recupero del nome del paese: " + e.getMessage());
	    }
	    return ""; 
	}
}
