package james.toolbox.jlogger.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.wetrack.salesassist.R;

import james.toolbox.jlogger.JLogger;

/**
 * Created by zhanghong on 15/2/28.
 */
public class LogView extends Activity{

    Button clearButton;
    ListView listView;
    LogListAdapter logListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_list);

        clearButton = (Button)findViewById(R.id.clearButton);
        if(clearButton != null){
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JLogger.clear();
                }
            });
        }

        listView = (ListView)findViewById(R.id.listView);
        logListAdapter = new LogListAdapter(this);
        listView.setAdapter(logListAdapter);
    }
}
