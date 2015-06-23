package com.xinkaishi.apple.xinweidian.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinkaishi.apple.xinweidian.Adapter.Adapter_goods_orders_down;
import com.xinkaishi.apple.xinweidian.DAO.AddressDAO;
import com.xinkaishi.apple.xinweidian.DAO.ImgDAO;
import com.xinkaishi.apple.xinweidian.DAO.ShoppingcartDAO;
import com.xinkaishi.apple.xinweidian.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Confirm_ordersActivity extends ActionBarActivity {
    private RelativeLayout rl_confirm_orders_receiving; //收货信息设置
    private LinearLayout ll_confirm_orders_defaultIfm; //无收货信息时的默认页面
    private HashMap<String, Object> receiceIFM;//数据库中的默认收货地址
    private TextView tv_confirm_orders_yunfei, tv_confirm_orders_allprice;//运费  应付总价
    private TextView tv_confirm_orders_submit;//提交订单
    private ListView lv_confirm_orders_list;
    private int yunfei;//运费
    private float allprice;
    private ShoppingcartDAO shoppingcartDAO;

    //姓名，手机，收货地址
    private TextView tv_confirm_orders_name, tv_confirm_orders_tel, tv_confirm_orders_address;

    private AddressDAO addDAO;
    private ImgDAO imgDAO;

    private ArrayList<HashMap<String, Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_orders);

        // 显示导航按钮
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initIntent();
        initView();//初始化控件
        initAdapter();//list适配

        setonclick();//监听
    }

    private void initIntent() {
        Bundle bundle = this.getIntent().getExtras();
        ArrayList bundlelist = bundle.getParcelableArrayList("list");
        list = new ArrayList<HashMap<String, Object>>();
        list= (ArrayList<HashMap<String, Object>>)bundlelist.get(0);
        Log.e("数据", list.get(0).get("name") + "" + list.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_mainback:
                // 返回应用的主activity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            // 导航返回键
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        addDAO.closeDB();
        imgDAO.closeDB();
        shoppingcartDAO.closeDB();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        initMain();//收货信息初始化 刷新

        super.onResume();
    }

    private void initView() {
        rl_confirm_orders_receiving = (RelativeLayout)findViewById(R.id.rl_confirm_orders_receiving);
        ll_confirm_orders_defaultIfm = (LinearLayout)findViewById(R.id.ll_confirm_orders_defaultIfm);
        tv_confirm_orders_name = (TextView)findViewById(R.id.tv_confirm_orders_name);
        tv_confirm_orders_tel = (TextView)findViewById(R.id.tv_confirm_orders_tel);
        tv_confirm_orders_address = (TextView)findViewById(R.id.tv_confirm_orders_address);
        tv_confirm_orders_submit = (TextView)findViewById(R.id.tv_confirm_orders_submit);
        tv_confirm_orders_yunfei = (TextView)findViewById(R.id.tv_confirm_orders_yunfei);
        tv_confirm_orders_allprice = (TextView)findViewById(R.id.tv_confirm_orders_allprice);
        lv_confirm_orders_list = (ListView)findViewById(R.id.lv_confirm_orders_list);
        receiceIFM = new HashMap<String, Object>();
        shoppingcartDAO = new ShoppingcartDAO(this);
        addDAO = new AddressDAO(this);
        imgDAO = new ImgDAO(this);
    }

    private void initMain() {
        receiceIFM = addDAO.getDefaultAddress();

        if(receiceIFM.get("name") == null){
            Log.e("eee", "默认收货地址为空");
            ll_confirm_orders_defaultIfm.setVisibility(View.VISIBLE);
        }else{
            ll_confirm_orders_defaultIfm.setVisibility(View.GONE);
            tv_confirm_orders_name.setText(receiceIFM.get("name").toString());
            tv_confirm_orders_tel.setText(receiceIFM.get("tel").toString());
            tv_confirm_orders_address.setText(receiceIFM.get("address").toString());
        }
    }

    private void initAdapter() {
        allprice = 0;
        Adapter_goods_orders_down adapter = new Adapter_goods_orders_down(Confirm_ordersActivity.this, list,
                R.layout.layout_goods_order_down, imgDAO, addDAO);
        lv_confirm_orders_list.setAdapter(adapter);

        for(int a = 0; a < list.size(); a ++){
            allprice = allprice + (float)list.get(a).get("price_in") * (Integer)list.get(a).get("num");
        }
        allprice = allprice + yunfei;
        tv_confirm_orders_allprice.setText(String.format("%.2f", allprice));
        //todo 运费本地算
//        tv_confirm_orders_yunfei.setText("");
    }

    private void setonclick() {
        rl_confirm_orders_receiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Confirm_ordersActivity.this, SetReceiveIfmActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pic_left_in, R.anim.pic_left_out);
            }
        });

        tv_confirm_orders_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(receiceIFM.get("name") == null) {
                    //todo 请先设置收货地址
                    return;
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");//获取当前时间 设置日期格式
                //todo post给服务器订单信息  返回交易号等 交易号等也要传递
                ArrayList bundlelist = new ArrayList();
                bundlelist.add(list);
                Bundle bundle = new Bundle();

                bundle.putParcelableArrayList("list", bundlelist);
                bundle.putString("time", df.format(new Date()));//传递的全部价格
                bundle.putFloat("allprice", allprice);//传递的全部价格
                bundle.putString("order", "9999999999999");//返回的交易单号
                Intent intent = new Intent(Confirm_ordersActivity.this, Payment_Activity.class);
                intent.putExtras(bundle);

                shoppingcartDAO.delete(list);//数据库删除选中的商品
                // 提交成功后两个页面
                Confirm_ordersActivity.this.finish();
                Shopping_cartActivity.instance.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.pic_left_in, R.anim.pic_left_out);
            }
        });
    }

}
