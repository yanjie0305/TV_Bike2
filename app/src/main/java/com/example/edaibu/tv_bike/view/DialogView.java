package com.example.edaibu.tv_bike.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.edaibu.tv_bike.R;


/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DialogView extends Dialog implements View.OnClickListener {

    private TextView tvContent,tvConfirm,tvCancle;
    private String confirm,cancle;
    private CharSequence content;
    private android.view.View.OnClickListener listener_yes;
    private android.view.View.OnClickListener listener_no;

    public DialogView(Context mContext, CharSequence content, String confirm, String cancle, android.view.View.OnClickListener listenerYes, android.view.View.OnClickListener listenerNo){
        super(mContext);
        this.content=content;
        this.confirm=confirm;
        this.cancle=cancle;
        this.listener_yes=listenerYes;
        this.listener_no=listenerNo;
    }

    public DialogView(DialogView dialogView, Context mContext, CharSequence content, String confirm, String cancle, android.view.View.OnClickListener listenerYes, android.view.View.OnClickListener listenerNo){
        super(mContext);
        if(null!=dialogView){
            dialogView.dismiss();
        }
        this.content=content;
        this.confirm=confirm;
        this.cancle=cancle;
        this.listener_yes=listenerYes;
        this.listener_no=listenerNo;
    }


    public DialogView(Context mContext, CharSequence content, String confirm, android.view.View.OnClickListener listenerYes){
        super(mContext);
        this.content=content;
        this.confirm=confirm;
        this.listener_yes=listenerYes;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        Window dialogWindow = getWindow();
//        dialogWindow.setWindowAnimations(R.style.mystyle);  //添加动画
        // 不可以用“返回键”取消
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false) ;
        initView();
    }


    /**
     * 初始化控件
     */
    private void initView(){
        tvContent=(TextView)findViewById(R.id.tv_dialog_content);
        tvConfirm=(TextView)findViewById(R.id.tv_confirm);
        tvCancle=(TextView)findViewById(R.id.tv_cancle);
        tvContent.setText(content);
        tvConfirm.setText(confirm);
        tvCancle.setText(cancle);
        if(cancle==null){
            tvCancle.setVisibility(View.GONE);
        }
        if(listener_yes!=null){
            tvConfirm.setOnClickListener(listener_yes);
        }else{
            tvConfirm.setOnClickListener(this);
        }
        if(listener_no!=null){
            tvCancle.setOnClickListener(listener_no);
        }else{
            tvCancle.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                dismiss();
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
            default:
                break;
        }
    }
}
