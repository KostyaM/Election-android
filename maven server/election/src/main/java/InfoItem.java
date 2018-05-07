/**
 * Created by admin on 07.05.2018.
 */
public class InfoItem {
    String subject, question;

    public InfoItem() {
    }

    public InfoItem(String subject, String question) {
        this.subject = subject;
        this.question = question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
