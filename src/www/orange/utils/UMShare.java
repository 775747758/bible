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

		// ����������Activity��������³�Ա����
		mController = UMServiceFactory.getUMSocialService("");
		
		//΢��
		UMWXHandler wxHandler = new UMWXHandler(context,"wx147b83155f980c1e","47fd6b2d592df322b9e4e0804debff52");
		wxHandler.addToSocialSDK();

		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(context,"wx147b83155f980c1e");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// qq����http://weibo.com/775747758
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1101251907", "qoCgRXiSyNX1FQ1q");
		qqSsoHandler.addToSocialSDK();
		

		// qq�ռ�
		// ����1Ϊ��ǰActivity������2Ϊ��������QQ���������APP ID������3Ϊ��������QQ���������APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, "1101251907", "qoCgRXiSyNX1FQ1q");
		qZoneSsoHandler.addToSocialSDK();

		// ��������SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// ���������SSO��Ȩ����
		// APPID:201874
		// API Key:28401c0964f04a72a14c812d6132fcef
		// Secret:3bf66e42db1e4fa9829b955cc300b737
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(context, "201874", "28401c0964f04a72a14c812d6132fcef", "3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// ��Ӷ���
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();

		// ���email
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
		//����title
		weixinContent.setTitle(title);
		//���÷���������תURL
		weixinContent.setTargetUrl(target);
		weixinContent.setShareContent(content);
		//���÷���ͼƬ
		//weixinContent.setShareImage(localImage);
		//mController.setShareMedia(weixinContent);
		mController.setShareMedia(weixinContent);
		
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		//��������Ȧtitle
		circleMedia.setTitle(title);
		circleMedia.setTargetUrl(target);
		mController.setShareMedia(circleMedia);
		
		

		mController.setShareMedia(qqShareContent);
		mController.setShareContent(title+"\r\n"+content);
		// umShare.getController().setAppWebSite("http://weibo.com/775747758");
		mController.openShare(activity, false);
	}
	
	
	

}
