package fi.academy.dao.jdbc;

import fi.academy.ToDo;
import fi.academy.dao.ToDoDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("jdbc")
public
class ToDoDaoJdbcImpl implements ToDoDao {
    private
    Connection con;

    public  ToDoDaoJdbcImpl() throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tododo",
                "postgres", "daviddavid");
    }
    @Override
    public
    List<ToDo> haeKaikki() {
        String sql = "SELECT * FROM todot";
        List<ToDo> haetut = new ArrayList<>(  );
        try (PreparedStatement pstmt = con.prepareStatement( sql )){
            for (ResultSet rs = pstmt.executeQuery(); rs.next();){

                haetut.add( handleTodo( rs ) );
            }
        } catch (SQLException e){
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return haetut;
    }

    @Override
    public
    Optional<ToDo> haeIdlla(int id) {
        String sql = "SELECT * FROM todot WHERE id=?";
        try (PreparedStatement pstmt = con.prepareStatement( sql )) {
            pstmt.setInt( 1, id );
            for (ResultSet rs = pstmt.executeQuery(); rs.next(); ) {
                return Optional.of( handleTodo( rs ) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }
    @Override
    public int editTodo(int id, ToDo todo){
        String sql = "UPDATE todot SET uppgift =?, fardig = ? WHERE id = ?";
        try(PreparedStatement pstmt = con.prepareStatement( sql )){
            pstmt.setString( 1, todo.getUppgift() );
            pstmt.setBoolean( 2, todo.isFardig() );
            pstmt.setInt( 3, id );
            return pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;

    }
    @Override
    public
    int lisaa(ToDo todo) {
        int avain = -1;
        String sql = "INSERT INTO todot(uppgift, fardig) VALUES (?,?)";
        try (PreparedStatement pstmt1 = con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS )) {
            pstmt1.setString( 1, todo.getUppgift() );
            pstmt1.setBoolean( 2, todo.isFardig() );
            pstmt1.execute();
            ResultSet avaimet = pstmt1.getGeneratedKeys();
            while (avaimet.next()) {
                avain = avaimet.getInt( 1 );
                todo.setId( avain );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avain;
    }
    @Override
    public
    ToDo poista(int id) {
        ToDo poistettu = new ToDo();
        String sel = "SELECT * FROM todot WHERE id = ?";
        try (PreparedStatement prs = con.prepareStatement(sel)) {
            prs.setInt(1, id);
            prs.execute();
            ResultSet rs = prs.executeQuery();
            while (rs.next()) {
                poistettu.setId(rs.getInt("id"));
                poistettu.setUppgift(rs.getString("todo"));
                poistettu.setFardig( rs.getBoolean("fardig") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "DELETE FROM todot WHERE id = ?";
        try (PreparedStatement pr = con.prepareStatement(sql)) {
            pr.setInt(1, id);
            pr.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poistettu;
    }

    public ToDo handleTodo(ResultSet rs) throws SQLException{
        ToDo todo = new ToDo(  );
        todo.setId( rs.getInt( 1 ) );
        todo.setUppgift( rs.getString( 2 ) );
        todo.setFardig( rs.getBoolean( 3 ) );
        return todo;
    }

}
