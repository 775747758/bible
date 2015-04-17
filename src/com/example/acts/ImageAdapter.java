package com.example.acts;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context context;
	private int[] imageIds;
	private ImageView[] images;

	public ImageAdapter(Context context, int[] imageIds) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imageIds = imageIds;
		// 存放图片的
		images = new ImageView[imageIds.length];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return images[position];
	}

	// 生成带有倒影的图片
	public void createReflectedBitmap() {

		int ReflectionGap = 4;// 原图片于倒影之间的距离
		BitmapFactory.Options opts = new Options();
		opts.inJustDecodeBounds = true;
		WindowManager wm = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		int wwidth = wm.getDefaultDisplay().getWidth();
		int wheight = wm.getDefaultDisplay().getHeight();
		int index = 0;

		for (int imageId : imageIds) {
			// 源图片
			Bitmap resourceBitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
			int width1 = resourceBitmap.getWidth();
			int height1 = resourceBitmap.getHeight();
			// 计算硕放比列

			int scaleX = width1 / wwidth;
			int scaleY = height1 / wheight;
			int scale = 1;
			if (scaleX > scaleY && scaleY >= 1) {
				scale = scaleX + 20;
			}
			if (scaleY > scaleX && scaleX >= 1) {
				scale = scaleY + 20;
			}
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = 5;
			Bitmap resourceBitmap1 = BitmapFactory.decodeResource(context.getResources(), imageId, opts);

			int width = resourceBitmap1.getWidth();
			int height = resourceBitmap1.getHeight();
			// 生成倒影图片
			// Bitmap source 源图片
			// x,y 生成倒影图片的起始位置 左上角
			// width,height 图片的宽高
			// Matrix m 用来 设置图片的样式 （倒影）
			Matrix matrix = new Matrix();
			// x水平翻转 y垂直翻转 1 正常 -1翻转
			matrix.setScale(1, -1);
			Bitmap refrectionBitmap = Bitmap.createBitmap(resourceBitmap1, 0, height / 2, width, height / 2, matrix, false);
			// 带有倒影的图片
			Bitmap bitmap = Bitmap.createBitmap(width, height + height / 2, Config.ARGB_8888);
			// 创建画布
			Canvas canvas = new Canvas(bitmap);
			// 绘制源图片
			canvas.drawBitmap(resourceBitmap1, 0, 0, null);
			// 绘制 原图片于倒影之间的距离
			Paint defaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + ReflectionGap, defaultPaint);
			// 绘制倒影图片
			canvas.drawBitmap(refrectionBitmap, 0, height + ReflectionGap, null);

			// ps中 渐变 遮罩
			Paint paint = new Paint();
			// 遮罩
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// 渐变
			// 0x70ffffff, 0x00ffffff
			/*
			 * //线性渐变填充 shader着色器 //在位图上Y方向花砖模式 TileMode：（一共有三种） CLAMP
			 * ：如果渲染器超出原始边界范围，会复制范围内边缘染色。 REPEAT ：横向和纵向的重复渲染器图片，平铺。 MIRROR
			 * ：横向和纵向的重复渲染器图片，这个和REPEAT 重复方式不一样，他是以镜像方式平铺。
			 */
			LinearGradient shader = new LinearGradient(0, height, 0, bitmap.getHeight(), 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// 着色器 用来绘制颜色 上色的
			paint.setShader(shader);

			canvas.drawRect(0, height, width, bitmap.getHeight(), paint);

			// 加入图片
			ImageView imageView = new ImageView(context);
			// imageView.setImageBitmap(resourceBitmap);
			BitmapDrawable bd = new BitmapDrawable(bitmap);
			bd.setAntiAlias(true);// 消除图片锯齿效果 平滑
			imageView.setImageDrawable(bd);
			// 设置图片的大小
			imageView.setLayoutParams(new GalleryFlow.LayoutParams(480, 426));
			images[index++] = imageView;

		}

	}

}
