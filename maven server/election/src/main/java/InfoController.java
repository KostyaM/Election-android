import Tools.DatabaseConnectionConfigs;
import Tools.DatabaseTableConfig;
import Tools.JSONConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

/**
 * Created by admin on 01.05.2018.
 */
public class InfoController {
    String subject,question;
    public Route JSON = (Request request, Response response) -> {
        getData();
        String responceData="{ \n" +
                "\t \"subject\":\"" +subject+"\", \n "+
                "\t \"question\":\"" +question+"\" \n "+
                "}";

        response.status(200);
        response.type("application/json");
        return responceData;


    };
    public void getData(){
        ConfigVoteDataReader cvdr=new ConfigVoteDataReader();
        cvdr.run();

        subject=cvdr.getSubject();
       question=cvdr.getQuestion();

        };

    public Route getJSON() {
        return JSON;
    }
}

