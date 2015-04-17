package www.orange.utils;

import android.app.Activity;
import android.content.Context;
import android.text.NoCopySpan.Concrete;

import com.orange.biblefood.Activity_BibleFood;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class UMShare {

	private UMSocialService mController;
	private Activity context;

	public UMShare(Activity context) {
		this.context = context;
		InitUmengShare();
	}

	

	private void InitUmengShare() {

		// 首先在您的Activity中添加如下成员变量
		mController = UMServiceFactory.getUMSocialService("");
		
		//微信
		UMWXHandler wxHandler = new UMWXHandler(context,"wx147b83155f980c1e","47fd6b2d592df322b9e4e0804debff52");
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context,"wx147b83155f980c1e");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// qq分享http://weibo.com/775747758
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1101251907", "qoCgRXiSyNX1FQ1q");
		qqSsoHandler.addToSocialSDK();
		

		// qq空间
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, "1101251907", "qoCgRXiSyNX1FQ1q");
		qZoneSsoHandler.addToSocialSDK();

		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 添加人人网SSO授权功能
		// APPID:201874
		// API Key:28401c0964f04a72a14c812d6132fcef
		// Secret:3bf66e42db1e4fa9829b955cc300b737
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(context, "201874", "28401c0964f04a72a14c812d6132fcef", "3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();

		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
	}
	public UMSocialService getController()
	{
		return mController;
	}
	
	
	public void share(String title,String content,String target,Activity activity)
	{
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle(title);
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(target);
		mController.setShareMedia(qqShareContent);

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setTitle(title);
		qzone.setShareContent(content);
		qzone.setTargetUrl(target);
		mController.setShareMedia(qzone);
		
		
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		//设置title
		weixinContent.setTitle(title);
		//设置分享内容跳转URL
		weixinContent.setTargetUrl(target);
		weixinContent.setShareContent(content);
		//设置分享图片
		//weixinContent.setShareImage(localImage);
		//mController.setShareMedia(weixinContent);
		mController.setShareMedia(weixinContent);
		
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		//设置朋友圈title
		circleMedia.setTitle(title);
		circleMedia.setTargetUrl(target);
		mController.setShareMedia(circleMedia);
		
		

		mController.setShareMedia(qqShareContent);
		mController.setShareContent(title+"\r\n"+content);
		// umShare.getController().setAppWebSite("http://weibo.com/775747758");
		mController.openShare(activity, false);
	}
	
	
	

}
