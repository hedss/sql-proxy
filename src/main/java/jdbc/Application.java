package jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {

    @Autowired
    private Connector connector;

    @RequestMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> test() {
        Connection conn = new Connection();
        conn.setType("sqlserver");
        conn.setHost("localhost");
        conn.setPort("1433");
        conn.setUsername("sa");
        conn.setPassword("Grafana.123");
        conn.setDatabase("master");
        return connect(conn);
    }

    @RequestMapping("/query")
    public List query(@RequestParam String sql) {
        JdbcTemplate template = new JdbcTemplate(connector.getDataSource());
        return template.queryForList(sql);
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> connect(@RequestBody Connection conn) {
        try {
            connector.connect(conn);
            return new ResponseEntity<>("Connected", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
