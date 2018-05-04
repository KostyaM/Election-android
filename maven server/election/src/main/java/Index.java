import Tools.DatabaseConnectionConfigs;
import Tools.Path;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.Scanner;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

/**
 * Created by admin on 24.04.2018.
 */
public class Index {
    public static void main(String[] args) {

        Scanner in=new Scanner(System.in);
        port(4567);


        Flyway flyway = new Flyway();

        // Point it to the database
        flyway.setDataSource("jdbc:postgresql://" + DatabaseConnectionConfigs.dbHost + ":" + DatabaseConnectionConfigs.port + "/" + DatabaseConnectionConfigs.dbName, DatabaseConnectionConfigs.username, DatabaseConnectionConfigs.password, null);

        // Start the migration
        flyway.migrate();


        get(Path.Index, new Controllers().getMainController().getHTML());
        get(Path.Vote, new Controllers().getVoteController().getHTML());
        get(Path.IndexAndroid,new Controllers().getMainController().getJSON());
        post(Path.IndexAndroid,new Controllers().getMainController().getJSON());
        get(Path.VoteAndroid,new Controllers().getVoteController().getJSON());
        get(Path.androidInfo,new Controllers().getInfoController().getJSON());
    }
}
