package com.project.userlistnew.repository;

import com.project.userlistnew.model.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class AutoRepository {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    class AutoRowMapper implements RowMapper<Auto> {

        @Override
        public Auto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Auto auto = new Auto();
            auto.setId(rs.getInt("AUTO_ID"));
            auto.setName(rs.getString("AUTO_NAME"));
            auto.setUrl(rs.getString("AUTO_IMG"));
            return auto;
        }
    }

    public List<Auto> getAllAuto() {
        List<Auto> autos = namedParameterJdbcTemplate.getJdbcOperations()
                .query("SELECT * FROM AUTO", new AutoRowMapper());
        return autos;
    }

    public List<Auto> getAuto (String name) {
        String auto = "SELECT USER_TABLE.USER_NAME, AUTO.AUTO_ID, AUTO.AUTO_NAME, AUTO.AUTO_IMG " +
                "FROM USER_TABLE " +
                "INNER JOIN OWNERS " +
                "ON USER_TABLE.USER_ID = OWNERS.USER_ID " +
                "INNER JOIN AUTO " +
                "ON AUTO.AUTO_ID = OWNERS.AUTO_ID " +
                "WHERE USER_TABLE.USER_NAME = '" + name + "'";

        List<Auto> autos = namedParameterJdbcTemplate.getJdbcOperations()
                .query(auto, new AutoRowMapper());
        return autos;
    }

    public void addCar(Integer id) {
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("AUTO_ID", id);
        namedParameters.addValue("USER_ID", UserRepository.currentUser.getId());
        namedParameterJdbcTemplate.update("INSERT INTO OWNERS (AUTO_ID, USER_ID) VALUES (:AUTO_ID, :USER_ID)", namedParameters);
    }

    public void removeCar(Integer id) {
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("AUTO_ID", id);
        namedParameters.addValue("USER_ID", UserRepository.currentUser.getId());
        namedParameterJdbcTemplate.update("DELETE FROM OWNERS WHERE AUTO_ID = :AUTO_ID AND USER_ID = :USER_ID", namedParameters);
    }
}
