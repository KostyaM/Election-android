import Tools.DatabaseConnectionConfigs;

import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import javax.xml.crypto.Data;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by admin on 24.04.2018.
 */
public class Controllers {
    MainController mainController;
    VoteController voteController;
    InfoController infoController;




    public Controllers() {
      mainController=new MainController();
      voteController=new VoteController();
      infoController=new InfoController();
    }

    public MainController getMainController() {
        return mainController;
    }

    public VoteController getVoteController() {
        return voteController;
    }

    public InfoController getInfoController() {
        return infoController;
    }
}
