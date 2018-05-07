
import Tools.JSONConverter;
import spark.Request;
import spark.Response;
import spark.Route;



/**
 * Created by admin on 01.05.2018.
 */
public class InfoController {
    String subject,question;
    public Route JSON = (Request request, Response response) -> {



        getData();
        InfoItem data=new InfoItem(subject,question);


        response.status(200);
        response.type("application/json");
        System.out.println(new JSONConverter(data).dataToJson());
        return new JSONConverter(data).dataToJson();


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

