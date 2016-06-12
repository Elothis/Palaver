package fabian.de.palaver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private String from;
    private String to;
    private String date;
    private long timestamp;
    private String messageText;
    private String mimetype;

    public ChatMessage(String from, String to, Date date, String mimetype, String messageText){
        this.from = from;
        this.to = to;
        this.timestamp = date.getTime();
        DateFormat dfOutput = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
        this.date = dfOutput.format(date);
        this.messageText = messageText;
        this.mimetype = mimetype;
    }

    public String from() {
        return from;
    }

    public String to() {
        return to;
    }

    public String getDate() {
        return date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMimetype(){ return mimetype; }
}
