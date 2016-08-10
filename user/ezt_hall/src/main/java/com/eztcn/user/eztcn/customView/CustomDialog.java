package com.eztcn.user.eztcn.customView;

import com.eztcn.user.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * @author LX
 *  自定义对话框。。
 * 2016-3-16下午2:04:56
 */

public class CustomDialog  extends AlertDialog{
	
	private Context context;
	private String hint_tx_str;
	private String sure_tx;
	private String cancel_tx;
	private TextView hint_tx;
	private Button sure_bt;
	private Button cancel_bt;

	public CustomDialog(Context context,String hint_tx,String Sure_str,String Cancel_str) {
		super(context);
		this.context=context;
		this.hint_tx_str=hint_tx;
		this.sure_tx=Sure_str;
		this.cancel_tx=Cancel_str;
		View view=setDialogCustomView();
		super.setContentView(view);
	}
	public CustomDialog(Context context) {
		super(context);
		this.context=context;
		CustomDialog.this.show();
		View view=setDialogCustomView();
		super.setContentView(view);
	}

	private View setDialogCustomView() {
		
		View view=LayoutInflater.from(context).inflate(R.layout.item_custom_dialog30, null);
		hint_tx=(TextView) view.findViewById(R.id.hint_tx);
		sure_bt=(Button) view.findViewById(R.id.sure);
		cancel_bt=(Button) view.findViewById(R.id.cancel);
		
		if(null!=hint_tx_str&&hint_tx_str.length()!=0){
			hint_tx.setText(hint_tx_str);
			}
		if(null!=sure_tx&&sure_tx.length()!=0){
			sure_bt.setText(sure_tx);
		}
		if(null!=cancel_tx&&cancel_tx.length()!=0){
			cancel_bt.setText(cancel_tx);
		}
		return view;
	}
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}
	@Override
	public void setOnKeyListener(OnKeyListener onKeyListener) {
		super.setOnKeyListener(onKeyListener);
	}
	
	public void setOnPositiveListener(View.OnClickListener onClickListener) {
		sure_bt.setOnClickListener(onClickListener); 
	}
	
	 public void setOnNegativeListener(View.OnClickListener listener){ 
	        cancel_bt.setOnClickListener(listener); 
	        
	    }

}
