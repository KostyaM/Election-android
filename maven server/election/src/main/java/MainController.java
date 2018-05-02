import Tools.DatabaseConnectionConfigs;
import Tools.DatabaseTableConfig;
import Tools.JSONConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonParseException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;


public class MainController {
    private static final int HTTP_BAD_REQUEST = 405;
    static String subject;
    static private Sql2o sql2o;
    static List <Candidate>options;

    public  Route JSON = (Request request, Response response) -> {
        getData();

        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(request.body().toString());
            Voter creation = mapper.readValue(request.body().toString(), Voter.class);
            if (creation.isValid()) {
                String fio = creation.getFio();

                String voteResult = creation.getCandidate();
                System.out.println(creation.getCandidate());
                try (Connection conn = sql2o.open()) {
                    conn.createQuery("INSERT INTO "+DatabaseTableConfig.tableName+" VALUES (:fio,:percent)").addParameter("fio", fio).addParameter("percent", voteResult).executeUpdate();
                    conn.close();
                    System.out.println("Insert result sucesessfully");
                }
            }


        } catch (JsonParseException jpe) {

        }catch (Exception e){
            System.out.println("Nothin to add");
            e.printStackTrace();
        }
        response.status(200);
        response.type("application/json");
        return new JSONConverter(options).dataToJson();


    };

    public void getData(){
        ConfigVoteDataReader cvdr=new ConfigVoteDataReader();
        cvdr.start();
        try {
            cvdr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subject=cvdr.getSubject();
        options=cvdr.getOutput();
        sql2o = new Sql2o("jdbc:postgresql://" + DatabaseConnectionConfigs.dbHost + ":" + DatabaseConnectionConfigs.port + "/" + DatabaseConnectionConfigs.dbName, DatabaseConnectionConfigs.username, DatabaseConnectionConfigs.password, new PostgresQuirks() {{
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
        });

    }
    public  Route HTML = (Request request, Response response) -> {
        getData();
        String fio=request.queryParams("fio");
        String voteResult=request.queryParams("voteResult");
        if( fio!=null &&!fio.equals("") && voteResult!=null){
            try (Connection conn = sql2o.open()) {
                conn.createQuery("INSERT INTO "+DatabaseTableConfig.tableName+" VALUES (:fio,:percent)").addParameter("fio", fio).addParameter("percent", voteResult).executeUpdate();
                conn.close();
                System.out.println("Insert result sucesessfully");
            }
        }else{
            System.out.println("Голос не принят " +fio+" "+voteResult);
        }


        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
        Configuration freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(MainController.class, "/"));
        freeMarkerEngine.setConfiguration(freeMarkerConfiguration);


        response.status(200);
        response.type("text/html");


        Map<String, Object> attributes = new HashMap<>();
        attributes.put("subject",subject);


        attributes.put("options", options);
        attributes.get("subject");
        System.out.println(options.get(1).getPercent());
        return freeMarkerEngine.render(new ModelAndView(attributes, "templates/index.ftl"));
    };

    public  Route getHTML() {
        return HTML;
    }

    public Route getJSON() {
        return JSON;
    }
}
