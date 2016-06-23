package co.com.indibyte.truelink.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.com.indibyte.truelink.R;
import co.com.indibyte.truelink.model.ItemSlideMenu;

/**
 * Created by juandiaz on 21/06/16.
 */
public class SlidingMenuAdapter extends BaseAdapter {

    private Context context;
    private List<ItemSlideMenu> lstItem;
    private int position;

    public SlidingMenuAdapter(List<ItemSlideMenu> lstItem, Context context) {
        this.context = context;
        this.lstItem = lstItem;

    }

    @Override
    public int getCount() {

        return lstItem.size();
    }

    @Override
    public Object getItem(int position) {

        return lstItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(context,  R.layout.item_sliding_menu, null);
        ImageView img = (ImageView)v.findViewById(R.id.ic_lupa3);
        TextView tv = (TextView)v.findViewById(R.id.item_title);

        ItemSlideMenu item = lstItem.get(position);
        img.setImageResource(item.getImgId());
        tv.setText(item.getTitle());
        return v;
    }
}
