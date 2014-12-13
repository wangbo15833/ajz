package com.v_info.ajzclientv3.widget;

import com.v_info.ajzclientv3.BaseActivity;
import com.v_info.ajzclientv3.R;

import android.app.Activity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyToast extends Activity {
	public static final int LOADING = 1;
	public static final int ERR404 = 2;
	public static final int CONERR = 3;
	public static final int PSWWRONG = 4;
	public static final int USERNAMEWRONG = 5;
	public static final int USERSTAWRONG = 6;

	public static final int SIGIN = 7;
	public static final int SIGNSUC = 8;
	public static final int SIGFAL = 9;
	public static final int TEST = 10;

	public static final int KICK = 11;
	public static final int NEWCHKFAIL = 12;

	public static Toast toast;
	public static String content;
	public static int imageRes;

	public static void showMyToast(BaseActivity a, int tag) {
		showMyToast((Activity) a, tag);
	}

	public static void showMyToast(Activity a, int tag) {
		switch (tag) {
		case LOADING:
			setInfo("正在连接服务器", R.drawable.nearby);
			break;
		case ERR404:
			setInfo("服务器不存在此资源", R.drawable.ic_favorites_cancel_fource);
			break;
		case CONERR:
			setInfo("网络连接异常,请检查您的网络状态和服务器地址",
					R.drawable.ic_favorites_cancel_fource);
			break;
		case PSWWRONG:
			setInfo("密码不正确", R.drawable.ic_favorites_cancel_fource);
			break;
		case USERNAMEWRONG:
			setInfo("用户名不存在", R.drawable.ic_favorites_cancel_fource);
			break;
		case USERSTAWRONG:
			setInfo("用户处于禁用状态，请与管理员联系", R.drawable.ic_favorites_cancel_fource);
			break;
		case SIGIN:
			setInfo("签到中...", R.drawable.nearby);
			break;
		case SIGNSUC:
			setInfo("签到成功", R.drawable.lt_gps_overlay_popup_rightbt);
			break;
		case SIGFAL:
			setInfo("签到失败", R.drawable.ic_favorites_cancel_fource);
			break;
		case TEST:
			setInfo("测试模式", R.drawable.ic_favorites_cancel_fource);
			break;
		case KICK:
			setInfo("您的账号已从其它手机客户端登录", R.drawable.ic_favorites_cancel_fource);
			break;
		case NEWCHKFAIL:
			setInfo("新建检查失败", R.drawable.ic_favorites_cancel_fource);
			break;
		default:
			break;
		}
		// public static Toast makeText(Context context,java.lang.CharSequence
		// text,int duration)
		// context 当前的activity或application
		// text 要显示的内容
		// duration 要显示的时长
		toast = Toast.makeText(a, content, Toast.LENGTH_SHORT);
		// 设置消息要显示的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(a);
		imageCodeProject.setImageResource(imageRes);
		// AnimationDrawable ad=(AnimationDrawable)
		// imageCodeProject.getBackground();
		toastView.addView(imageCodeProject, 0);
		// ad.setOneShot(false);
		// ad.start();
		toast.show();
	}

	public static void showMyToast(Activity a, String s, int r) {

		setInfo(s, r);
		toast = Toast.makeText(a, content, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(a);
		imageCodeProject.setImageResource(imageRes);
		// 把图片视图加到toastView中
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	private static void setInfo(String s, int i) {
		content = s;
		imageRes = i;
	}
}
