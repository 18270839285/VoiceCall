package huidu.com.voicecall.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.idl.face.platform.utils.DensityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import huidu.com.voicecall.R;
import huidu.com.voicecall.bean.AnchorType;
import huidu.com.voicecall.bean.Home;

/**
 * 创    建:  lt  2018/1/8--11:51
 * 作    用:  对话框工具类
 * 注意事项:
 */

public class DialogUtil {
    /**
     * 确定----取消
     */
    public static Dialog showTakePhoto(Context context, final View.OnClickListener takeListener,final View.OnClickListener selectListener) {
        View dialogView = View.inflate(context, R.layout.dialog_takephoto, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tv_take = (TextView) dialogView.findViewById(R.id.tv_take);
        TextView tv_select = (TextView) dialogView.findViewById(R.id.tv_select);
        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        tv_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (takeListener != null) {
                    takeListener.onClick(v);
                }
            }
        });
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (selectListener != null) {
                    selectListener.onClick(v);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                            dialog.dismiss();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
        );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = DensityUtils.dp2px(context, 300);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialog_up_down_animation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
    /**
     * 确定----取消
     */
    public static Dialog showServiceDialog(Context context,String phone, final View.OnClickListener mListener) {
        View dialogView = View.inflate(context, R.layout.dialog_service, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tv_call = (TextView) dialogView.findViewById(R.id.tv_call);
        TextView tv_phone = (TextView) dialogView.findViewById(R.id.tv_phone);
        ImageView iv_cancel = (ImageView) dialogView.findViewById(R.id.iv_cancel);
        tv_phone.setText("客服电话: "+phone);
        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mListener != null) {
                    mListener.onClick(v);
                }
            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                            dialog.dismiss();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
        );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = DensityUtils.dp2px(context, 300);
        dialogWindow.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
    /**
     * 确定----取消
     */
    public static Dialog showDialogConfirm(Context context, String title, String content,
                                           String leftBtn, final View.OnClickListener leftListener,
                                           String rightBtn, final View.OnClickListener rightListener, int Visibility) {
        View dialogView = View.inflate(context, R.layout.dialog_confirm, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView tvContent = (TextView) dialogView.findViewById(R.id.dialog_content);
        TextView dialog_left_btn = (TextView) dialogView.findViewById(R.id.dialog_left_btn);
        TextView dialog_right_btn = (TextView) dialogView.findViewById(R.id.dialog_right_btn);
        dialog_left_btn.setText(leftBtn);
        tvTitle.setText(title);
        tvContent.setText(content);
        dialog_right_btn.setText(rightBtn);
        dialog_right_btn.setVisibility(Visibility);
        dialog_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (rightListener != null) {
                    rightListener.onClick(v);
                }
            }
        });
        dialog_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (leftListener != null) {
                    leftListener.onClick(v);
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                            dialog.dismiss();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
        );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtils.dip2px(context, 300);
        dialogWindow.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 取消订单弹框
     * @param context
     * @param cancelListener
     * @param closeListener
     * @return
     */
    public static Dialog showOrderCancelDialog(Context context, final View.OnClickListener cancelListener,
                                             final View.OnClickListener closeListener) {
        View dialogView = View.inflate(context, R.layout.dialog_order_cancel, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        ImageView iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (closeListener != null) {
                    closeListener.onClick(v);
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                            dialog.dismiss();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
        );
        Window dialogWindow = dialog.getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = DensityUtils.dp2px(context, 300);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialog_up_down_animation);
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.setCanceledOnTouchOutside(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog showDialogConfirm1(Context context, String content,
                                            String leftBtn, final View.OnClickListener leftListener,
                                            String rightBtn, final View.OnClickListener rightListener, int Visibility) {
        View dialogView = View.inflate(context, R.layout.dialog_confirm_new, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        TextView tvContent = (TextView) dialogView.findViewById(R.id.dialog_content);
        TextView dialog_left_btn = (TextView) dialogView.findViewById(R.id.dialog_left_btn);
        TextView dialog_right_btn = (TextView) dialogView.findViewById(R.id.dialog_right_btn);
        dialog_left_btn.setText(leftBtn);
        tvContent.setText(content);
        dialog_right_btn.setText(rightBtn);
        dialog_right_btn.setVisibility(Visibility);
        dialog_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (rightListener != null) {
                    rightListener.onClick(v);
                }
            }
        });
        dialog_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (leftListener != null) {
                    leftListener.onClick(v);
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                            dialog.dismiss();
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }
        );
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtils.dip2px(context, 300);
        dialogWindow.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

//    /**
//     * 主界面弹窗图片
//     * @param context
//     * @param path
//     * @param ivPicListener
//     * @param leftListener
//     * @return
//     */
//    public static Dialog showHomeDialog(Context context, String path,
//                                        final View.OnClickListener ivPicListener,
//                                        final View.OnClickListener leftListener) {
//        View dialogView = View.inflate(context, R.layout.dialog_home_ad, null);
//        final Dialog dialog = new Dialog(context, R.style.dialog);
//        dialog.setContentView(dialogView);
//        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.iv_close);
//        ImageView ivPic = (ImageView) dialogView.findViewById(R.id.iv_pic);
//        Glide.with(context).load(path).into(ivPic);
//        ivPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (ivPicListener != null) {
//                    ivPicListener.onClick(v);
//                }
//            }
//        });
//        ivClose.setOnClickListener(new View.OnClickListener() {//关闭弹窗
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (leftListener != null) {
//                    leftListener.onClick(v);
//                }
//            }
//        });
//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = DensityUtils.dp2px(context, 300);
//        dialogWindow.setGravity(Gravity.CENTER);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            dialog.create();
//        }
//        dialog.setCanceledOnTouchOutside(false);
//        return dialog;
//    }


    public interface onItemClick {
        void onClickItem(String string);
    }


    /**
     * 选择主播类型
     *
     * @return
     * @paramADBean
     */
    public static void selectAnchorType(Activity activity, final List<AnchorType> typeList, final onItemClick listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity,
                R.style.DialogStyleBottom).create();
        alertDialog.show();
        final Window window = alertDialog.getWindow();
        window.setContentView(R.layout.layout_anchor_type);
        window.setLayout(
                window.getContext().getResources().getDisplayMetrics().widthPixels,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_up_down_animation);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        BaseQuickAdapter adapter = new BaseQuickAdapter<AnchorType, BaseViewHolder>(R.layout.item_anchor_type, typeList) {
            @Override
            protected void convert(BaseViewHolder helper, final AnchorType item) {
                final TextView textView = helper.getView(R.id.tv_title);
                textView.setText(item.getType_name());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickItem(textView.getText().toString());
                        alertDialog.dismiss();
                    }
                });
            }
        };

//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                listener.onItemClick(adapter, view, position);
//                alertDialog.dismiss();
//            }
//        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 提示框，确定
     *
     * @param activity
     * @param msg
     * @param listener
     */
    public static void showDialogConfim(final Activity activity, String title, String msg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
