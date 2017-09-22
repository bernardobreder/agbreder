package com.agbreder.server.service.source;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import breder.util.sql.DB;
import breder.util.sql.SqlTupla;
import breder.util.util.Base64;

import com.agbreder.server.service.IService;

/**
 * Serviço de Mensagem
 * 
 * 
 * @author Bernardo Breder
 */
public class SourceService implements IService {

  /**
   * Envia um email
   * 
   * @param userId
   * @param name
   * @param text
   * @throws IOException
   * @throws SQLException
   */
  public void publish(int userId, String name, String text) throws IOException,
    SQLException {
    Date date = new Date();
    String source = Base64.encodeString(text);
    List<String> names = this.getSourceNames(userId);
    if (!names.contains(name)) {
      DB.getInstance()
        .write(
          "INSERT INTO agbreder$source (user_id, date, name, src, bin) VALUES (?, ?, ?, ?, ?)",
          userId, date, name, source, source);
    }
    else {
      DB.getInstance()
        .write(
          "UPDATE agbreder$source SET date = ?, src = ?, bin = ? WHERE user_id = ? and name = ?",
          date, source, source, userId, name);
    }
  }

  /**
   * Envia um email
   * 
   * @param userId
   * @return id
   * @throws IOException
   * @throws SQLException
   */
  public List<String> getSources(int userId) throws IOException, SQLException {
    List<SqlTupla> rows =
      DB.getInstance().readTupla(
        "SELECT src FROM agbreder$source WHERE user_id = ?", userId);
    List<String> list = new ArrayList<String>(rows.size());
    for (int n = 0; n < rows.size(); n++) {
      SqlTupla row = rows.get(n);
      list.add(Base64.decodeString(row.getString()));
    }
    return list;
  }

  /**
   * Envia um email
   * 
   * @param userId
   * @return id
   * @throws IOException
   * @throws SQLException
   */
  public List<String> getSourceNames(int userId) throws IOException,
    SQLException {
    List<SqlTupla> rows =
      DB.getInstance().readTupla(
        "SELECT name FROM agbreder$source WHERE user_id = ?", userId);
    List<String> list = new ArrayList<String>(rows.size());
    for (int n = 0; n < rows.size(); n++) {
      SqlTupla row = rows.get(n);
      list.add(row.getString());
    }
    return list;
  }

}
