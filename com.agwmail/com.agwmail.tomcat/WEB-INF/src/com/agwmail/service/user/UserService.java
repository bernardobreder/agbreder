package com.agwmail.service.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import breder.util.sql.DB;
import breder.util.sql.SqlTupla;

import com.agwmail.model.user.User;
import com.agwmail.model.user.UserContext;
import com.agwmail.model.user.UserManager;
import com.agwmail.service.IService;
import com.agwmail.service.ServiceLocator;

/**
 * Serviço de Usuário
 * 
 * 
 * @author Bernardo Breder
 */
public class UserService implements IService {

  /**
   * Autentica um usuário
   * 
   * @param username
   * @param password
   * @return código do usuario
   * @throws IOException
   * @throws SQLException
   */
  public Integer login(String username, String password) throws IOException,
    SQLException {
    Integer userId =
      DB.getInstance().readId(
        "SELECT id FROM agwmail$user WHERE username = ? and password = ?",
        username, password);
    if (userId != null) {
      UserContext context = new UserContext();
      context.setUser(ServiceLocator.user.get(userId));
      UserManager.getInstance().setUserContext(context);
    }
    return userId;
  }

  /**
   * Retira a autenticação
   * 
   * @throws IOException
   * @throws SQLException
   */
  public void logout() throws IOException, SQLException {
    UserManager.getInstance().removeUserContext();
  }

  /**
   * Cria uma conta
   * 
   * @param username
   * @param email
   * @param password
   * @return código do usuário
   * @throws IOException
   * @throws SQLException
   */
  public Integer create(String username, String email, String password)
    throws IOException, SQLException {
    DB.getInstance().write(
      "INSERT INTO agwmail$user " + "(username, email, password) VALUES "
        + "(?, ?, ?)", username, email, password);
    return DB.getInstance().getSequence("agwmail$user");
  }

  /**
   * Autentica um usuário
   * 
   * @param id
   * @return código do usuario
   * @throws IOException
   * @throws SQLException
   */
  public User get(int id) throws IOException, SQLException {
    List<SqlTupla> rows =
      DB.getInstance().readTupla(
        "SELECT id, username, email " + "FROM agwmail$user WHERE id = ?", id);
    if (rows.size() == 0) {
      return null;
    }
    SqlTupla row = rows.get(0);
    return buildUser(row);
  }

  /**
   * Autentica um usuário
   * 
   * @param username
   * @return código do usuario
   * @throws IOException
   * @throws SQLException
   */
  public User get(String username) throws IOException, SQLException {
    List<SqlTupla> rows =
      DB.getInstance().readTupla(
        "SELECT id, username, email " + "FROM agwmail$user WHERE username = ?",
        username);
    if (rows.size() == 0) {
      return null;
    }
    SqlTupla row = rows.get(0);
    return buildUser(row);
  }

  /**
   * Constroi um usuário
   * 
   * @param row
   * @return usuario
   */
  private User buildUser(SqlTupla row) {
    User user = new User();
    user.setId(row.getInt());
    user.setUsername(row.getString());
    user.setEmail(row.getString());
    return user;
  }

}
