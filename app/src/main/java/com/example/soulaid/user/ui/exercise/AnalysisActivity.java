package com.example.soulaid.user.ui.exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.adapter.ResultShowAdapter;
import com.example.soulaid.entity.Scale;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AnalysisActivity extends AppCompatActivity {

    private int questionNumber;
    private Scale scale;
    private TextView name,analysis;
    private LineChart chart;
    private RecyclerView recyclerView;
    private HashMap<String,Integer> result=new HashMap<>();
    private ResultShowAdapter adapter;

    private ArrayList<String> k=new ArrayList<>();
    private ArrayList<Integer> v=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        init();
    }
    private void init(){
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        //获取scale
        scale = (Scale) bundle.getSerializable("scale");

        questionNumber=bundle.getInt("questionNumber");

        //获取result
        Set<String> keySet=bundle.keySet();  //问题出这在里，bundle.keySet()中第一行为"scale"  解决方法:增加if语句判断（第42行）
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()){
            String key = iter.next();
            if(!key.equals("scale")&&!key.equals("questionNumber")){
                result.put(key,bundle.getInt(key));
            }

        }
        //初始化控件
        name=findViewById(R.id.name);
        name.setText(scale.getName());

        analysis=findViewById(R.id.analysis);
        analysis.setText(scale.getAnswerAnalysis());

        recyclerView=findViewById(R.id.recycleView);
        LinearLayoutManager manager = new LinearLayoutManager(AnalysisActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter=new ResultShowAdapter(AnalysisActivity.this,result);
        recyclerView.setAdapter(adapter);

        //生成曲线图
        getChart();
    }

    //该函数用于生成心理健康曲线图
    private void getChart(){
        chart=findViewById(R.id.chart);

        //设置折线上的点
        List<Entry> list=new ArrayList<>();
        for (int i=0;i<v.size();i++){
            list.add(new Entry(i,(float)v.get(i)));
        }

        //将所有点汇成一条折现
        LineDataSet lineDataSet=new LineDataSet(list,"心理健康折线");

        //增加一条折现
        LineData lineData=new LineData();
        lineData.addDataSet(lineDataSet);

        //将折现添加到折线图上
        chart.setData(lineData);

        //修改x轴
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //设置x轴位置
        xAxis.setLabelCount(k.size(),false);  //设置x轴刻度数量
        xAxis.setGranularity(1f);   //这行代码非常关键，解决了和x轴不对应的问题，解决了x轴标签重复的问题
        xAxis.setValueFormatter(new ValueFormatter() {  //设置x轴刻度值
            @Override
            public String getFormattedValue(float value) {
                return k.get((int)value);
            }
        });

        //修改y轴
        float min;
        float max;

        if(scale.getName().equals("人际关系综合诊断量表")){   //人际关系综合诊断表从0开始，其他从一开始
            min=0f;
            max=((float) questionNumber*(scale.getAnswerNumber()-1)/result.size());
        }else {
            min=((float) questionNumber/result.size());
            max=((float) questionNumber*(scale.getAnswerNumber())/result.size());
        }

        YAxis yAxisRight=chart.getAxisRight();
        YAxis yAxisLeft=chart.getAxisLeft();
        yAxisRight.setEnabled(false);  //隐藏右侧y轴
        yAxisLeft.setAxisMinimum(min); //设置左侧y轴最小值
        yAxisLeft.setAxisMaximum(max); //设置左侧y轴最大值

        //隐藏描述
        Description description=new Description();
        description.setEnabled(false);
        chart.setDescription(description);

        //刷新
        chart.invalidate();

    }

    public void setK(ArrayList<String> k) {
        this.k = k;
    }

    public void setV(ArrayList<Integer> v) {
        this.v = v;
    }
}
