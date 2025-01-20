package jp.co.benesse.web.repository;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class webCustomerMenuRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<BookDTO> findAllBooks() {
        String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/sql/BookRepository/findAllBooks.sql")),
                StandardCharsets.UTF_8);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BookDTO.class));
    }

    public int countStock(String bookID) {
        String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/sql/BookRepository/countStock.sql")),
                StandardCharsets.UTF_8);
        return jdbcTemplate.queryForObject(sql, new Object[] { bookID }, Integer.class);
    }
}
