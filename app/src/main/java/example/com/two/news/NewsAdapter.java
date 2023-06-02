package example.com.two.news;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import example.com.two.R;

public class NewsAdapter extends ArrayAdapter<News> {
    private int item_layout_id;
    public NewsAdapter(Context context, int resource, List objects) {
        super(context, resource,objects);
        item_layout_id=resource;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        final  ViewHolder holder;
        if(convertView==null){//回收站为空\
            /**
             * LayoutInflater.from()得到布局填充器对象
             * getContext()获取当前上下文
             * inflate() 加载填充布局
             */
            view= LayoutInflater.from(getContext())
                    .inflate(item_layout_id,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);

        }else {//显示后续的列表项
            view=convertView;
            holder= (ViewHolder) view.getTag();
        }
        News itemData=getItem(position);
        holder.title.setText(itemData.getTitle());
        holder.date.setText(itemData.getDate());
        holder.author_name.setText(itemData.getAuthor_name());
        if(!TextUtils.isEmpty(itemData.getThumbnail_pic_s())){
            new ImageTask(new ImageTask.CallBack() {
                @Override
                public void getResults(Bitmap result) {
                    holder.thumbnail_pic_s.setImageBitmap(result);


                }
            }).execute(itemData.getThumbnail_pic_s()) ;  //执行异步任务
        }else{
            holder.thumbnail_pic_s.setVisibility(View.GONE);
        }
        return view;
    }
    class  ViewHolder{
        TextView title;
        TextView date;
        TextView author_name;
        ImageView thumbnail_pic_s;

        public ViewHolder(View view) {
            title=(TextView) view.findViewById(R.id.title);
            date=(TextView)view.findViewById(R.id.date);
            author_name=(TextView)view.findViewById(R.id.author_name);
            thumbnail_pic_s=(ImageView)view.findViewById(R.id.imgsrc);
        }
    }
}
