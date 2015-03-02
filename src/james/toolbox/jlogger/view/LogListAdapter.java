package james.toolbox.jlogger.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wetrack.salesassist.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import james.toolbox.jlogger.JLogger;

/**
 * Created by zhanghong on 15/2/28.
 */
public class LogListAdapter extends BaseAdapter{
    private Context context;
    List<JLogger.Record> list;
    DataSetObserver dataSetObserver;

    final int DATA_CHANGE = 1;
    final int DATA_INVALIDATE = 2;

    public LogListAdapter(Context context){
        this.context = context;
        final Handler handler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case DATA_CHANGE:
                        readLog();
                        notifyDataSetChanged();
                        break;
                    case DATA_INVALIDATE:
                        if(list != null)
                            list.clear();
                        notifyDataSetInvalidated();
                        break;
                }
                return false;
            }
        });
        readLog();
        dataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                handler.sendEmptyMessage(DATA_CHANGE);
            }

            @Override
            public void onInvalidated() {
                handler.sendEmptyMessage(DATA_INVALIDATE);
            }
        };

        JLogger.registerDatasetObserver(dataSetObserver);
    }

//    public LogListAdapter(){
//
//    }

    private void readLog(){
        if(list == null || list.size() == 0){
            list = JLogger.readFromStart();
        }else{
            list.addAll(JLogger.readContinue());
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        JLogger.Record record = list == null ? null : list.get(position);
        return record == null ? 0 : record.time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.log_list_item, null);
            holder = new ViewHolder();
            holder.message = (TextView)convertView.findViewById(R.id.messageText);
            holder.time = (TextView)convertView.findViewById(R.id.timeText);
            holder.type = (TextView)convertView.findViewById(R.id.typeText);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        if(holder != null){
            JLogger.Record record = (JLogger.Record)getItem(position);
            if(record != null){
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                    Date date = new Date(record.time);
                    String timeText = simpleDateFormat.format(date);
                    holder.time.setText(timeText);
                }catch (Exception e){
                    holder.time.setText("非法时间" + record.time);
                }
                holder.message.setText(record.message);
                switch (record.type){
                    case JLogger.TYPE_INFO:
                        holder.type.setTextColor(context.getResources().getColor(R.color.green));
                        holder.type.setText("info");
                        break;
                    case JLogger.TYPE_DEBUG:
                        holder.type.setTextColor(context.getResources().getColor(R.color.blue));
                        holder.type.setText("debug");
                        break;
                    case JLogger.TYPE_WARN:
                        holder.type.setTextColor(context.getResources().getColor(R.color.orange));
                        holder.type.setText("warn");
                        break;
                    case JLogger.TYPE_ERROR:
                        holder.type.setTextColor(context.getResources().getColor(R.color.default_red));
                        holder.type.setText("error");
                        break;
                    default:
                        break;
                }
            }
        }
        return convertView;
    }

    class ViewHolder{
        TextView time;
        TextView type;
        TextView message;
    }
}
