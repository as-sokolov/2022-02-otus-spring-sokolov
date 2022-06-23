package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Genre getById(long id) {
        try {
            Map<String, Object> params = Collections.singletonMap("id", id);
            return namedParameterJdbcOperations.queryForObject(
                    "select id, name from genres where id = :id", params, new GenreMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.debug("Запись с id {} не найдена", id);
            return  null;
        }
    }

    @Override
    public List<Genre> getAll() {
        try {
            return namedParameterJdbcOperations.query("select id, name from genres", new GenreMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.debug("Записей не найдено");
            return  new ArrayList<>();
        }

    }

    @Override
    public Long insert(Genre genre) {
        KeyHolder kh = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("insert into genres (name) values (:name)",
                new MapSqlParameterSource().addValue("name", genre.getName()), kh);
        return kh.getKey() == null ? null : kh.getKeyAs(Long.class);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from genres where id = :id", params
        );
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }
    }
}
