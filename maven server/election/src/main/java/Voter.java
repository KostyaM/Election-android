/**
 * Created by admin on 24.04.2018.
 */
class Voter {
    String fio;
    String candidate;

    public Voter(String fio, String candidate) {
        this.fio = fio;
        this.candidate = candidate;
    }

    public Voter() {
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public boolean isValid(){
        if(fio!=null && !fio.equals("")&&candidate!=null && !candidate.equals(""))return true;
        return false;
    }
}
