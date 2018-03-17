package com.project.userlistnew.repository;

import com.project.userlistnew.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class UserRepository {

    static public User currentUser = new User(null, null, null, null, null);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {

            User user = new User();
            user.setId(rs.getInt("USER_ID"));
            user.setName(rs.getString("USER_NAME"));
            user.setSurname(rs.getString("USER_SURNAME"));
            user.setPassword(rs.getString("USER_PASS"));
            user.setDescription(rs.getString("USER_DESC"));
            user.setRole(rs.getInt("ROLE_ID"));
            return user;
        }
    }

    public List<User> getAllUsers() {

        List<User> users = namedParameterJdbcTemplate.getJdbcOperations()
                .query("SELECT * FROM USER_TABLE", new UserRowMapper());
        return users;
    }

    public void removeUser(Integer id) {

        namedParameterJdbcTemplate.getJdbcOperations().update("DELETE FROM OWNERS WHERE USER_ID = ?", id);
        namedParameterJdbcTemplate.getJdbcOperations().update("DELETE FROM USER_TABLE WHERE USER_ID = ?", id);
    }

    public boolean addUser (String name, String surname, String password, String description, Integer role)
    {

        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("USER_NAME", name);
        int result = namedParameterJdbcTemplate.queryForObject("SELECT COUNT(USER_NAME) FROM USER_TABLE WHERE USER_NAME = :USER_NAME", namedParameters, Integer.class);

        if (result == 0) {

            namedParameters.addValue("USER_SURNAME", surname);
            namedParameters.addValue("USER_PASS", password);
            namedParameters.addValue("USER_DESC", description);
            namedParameters.addValue("ROLE_ID", role);

            namedParameterJdbcTemplate.update("INSERT INTO USER_TABLE (USER_NAME, USER_SURNAME, USER_PASS, USER_DESC, ROLE_ID) VALUES (:USER_NAME, :USER_SURNAME, :USER_PASS, :USER_DESC, :ROLE_ID)", namedParameters);
            return true;
        }
        return false;
    }

    public String signInUser(String name, String password)
    {

        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("USER_NAME", name);
        namedParameters.addValue("USER_PASS", password);

        User user = namedParameterJdbcTemplate.queryForObject("SELECT * FROM USER_TABLE WHERE (USER_NAME = :USER_NAME) AND (USER_PASS = :USER_PASS)", namedParameters, new UserRowMapper());

        currentUser.setId(user.getId());

        if(user.getRole().equals(1))
            return "user";
        if(user.getRole().equals(2))
            return "admin";
        else
            return "none";
    }
}
