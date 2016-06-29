package fabian.de.palaver;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage>{

    private List<ChatMessage> chatMessages;
    private PalaverApplication app;
    private Context context;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages, PalaverApplication app) {
        super(context, R.layout.chat_message);
        this.chatMessages = chatMessages;
        this.app = app;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ChatMessage message = chatMessages.get(position);
        View rowView = convertView;

        if(rowView == null) {
            rowView = inflater.inflate(R.layout.chat_message, parent, false);
        }

        TextView dateTextView = (TextView) rowView.findViewById(R.id.chat_message_date_text_view);
        TextView messageTextView = (TextView) rowView.findViewById(R.id.message_text_view);

        dateTextView.setText(message.getDate());
        boolean textMessage = message.getMimetype().equals("text/plain");
        if(textMessage) messageTextView.setText(message.getMessageText());
        else messageTextView.setText(message.from() + " " + ChatActivity.LOCATION_SHARED);

        LinearLayout innerLayout = (LinearLayout) rowView.findViewById(R.id.bubble_inner_layout);
        LinearLayout outerLayout = (LinearLayout) rowView.findViewById(R.id.bubble_parent_layout);


        if(message.from().equalsIgnoreCase(app.getUserName())){
            innerLayout.setBackgroundResource(R.drawable.bubble_right);
            outerLayout.setGravity(Gravity.END);
        }
        else{
            innerLayout.setBackgroundResource(R.drawable.bubble_left);
            outerLayout.setGravity(Gravity.START);
        }

        return rowView;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public void add(ChatMessage object) {
        chatMessages.add(object);
    }
}
