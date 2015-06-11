package com.xinkaishi.apple.xinweidian.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.xinkaishi.apple.xinweidian.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：Xinweidian
 * 类描述：
 * 创建人：apple
 * 创建时间：15/6/5 上午11:33
 * 修改人：apple
 * 修改时间：15/6/5 上午11:33
 * 修改备注：
 */
public class Adapter_goods_orders_detail extends BaseExpandableListAdapter{
    private Context context;
    private HashMap<String, Object> hm;
    public Adapter_goods_orders_detail(Context context, HashMap<String, Object> hm){
        this.context = context;
        this.hm = hm;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //如果有超过两个商品，则显示两行
        ArrayList<HashMap<String, Object>> childlist = (ArrayList<HashMap<String, Object>>)hm.get("child");

        return childlist.size();
    }

    @Override
    public HashMap<String, Object> getGroup(int groupPosition) {
        return hm;
    }

    @Override
    public HashMap<String, Object> getChild(int groupPosition, int childPosition) {
        ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>)hm.get("child");
        return list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_detail_ordergroup, null);
            holder.tv_ordergroup_jiaoyihao = (TextView)convertView.findViewById(R.id.tv_ordergroup_jiaoyihao);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setClickable(true);

        holder.tv_ordergroup_jiaoyihao.setText(hm.get("transaction").toString());
        //todo 数据适配
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(true){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_detail_orderchild, null);
            holder.tv_goodscenter_title = (TextView)convertView.findViewById(R.id.tv_goodscenter_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_goodscenter_title.setText(getChild(groupPosition, childPosition).get("name").toString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder{
        // 交易号  商品名称  订单号  规格  进价  数量
        TextView tv_ordergroup_jiaoyihao, tv_goodscenter_title;
    }
}
