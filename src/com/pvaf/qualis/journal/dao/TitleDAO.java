/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvaf.qualis.journal.dao;

import com.pvaf.qualis.journal.service.DBLocator;
import com.pvaf.qualis.journal.entidades.Title;
import com.pvaf.qualis.journal.exceptions.ErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author marte
 */
public class TitleDAO {
    
    private final static Logger log = Logger.getLogger(TitleDAO.class);
    
    public static List<Title> getTitles(int idPubVenue) throws ErrorException {
        List<Title> listT = new ArrayList<>();
        int i=1;
        
        try(Connection conn = DBLocator.getConnection(); 
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM title WHERE id_pub_venue = ?")){
            ps.setInt(i++,idPubVenue);
            
            ResultSet rs = ps.executeQuery();
            Title title;
            while(rs.next()){
                title = new Title(rs.getInt("id_pub_venue"),rs.getString("title"));
                listT.add(title);
            }
            rs.close();
            
	}catch(SQLException e){
            log.error("Ocorreu uma exceção de SQL.", e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
	}
        
	return listT;
    }
    
    public static Integer checkIdPubVenue(Integer idPubVenue, String journalTitle) throws ErrorException{
        int idPubVenueAux = 0;
        Connection conn = null;
        
        try {
            conn = DBLocator.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT id_pub_venue FROM title WHERE id_pub_venue = ? AND title = ?");

            int i = 1;
            ps.setInt(i++, idPubVenue);
            ps.setString(i++, journalTitle);

            ResultSet title = ps.executeQuery();
            if (title.first()) {
                idPubVenueAux = title.getInt("id_pub_venue");
            }
            title.close();
            ps.close();
            
        } catch (SQLException e) {
            log.error("Ocorreu uma exceção de SQL.", e.fillInStackTrace());
            throw new ErrorException("Ocorreu um Erro Interno");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("Exceção ao fechar a conexão.", e.fillInStackTrace());
                    throw new ErrorException("Ocorreu um Erro Interno");
                }
            }
        }
        return idPubVenueAux;
    }
}
