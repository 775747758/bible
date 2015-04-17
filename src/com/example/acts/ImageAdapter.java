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
		// ���ͼƬ��
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

	// ���ɴ��е�Ӱ��ͼƬ
	public void createReflectedBitmap() {

		int ReflectionGap = 4;// ԭͼƬ�ڵ�Ӱ֮��ľ���
		BitmapFactory.Options opts = new Options();
		opts.inJustDecodeBounds = true;
		WindowManager wm = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		int wwidth = wm.getDefaultDisplay().getWidth();
		int wheight = wm.getDefaultDisplay().getHeight();
		int index = 0;

		for (int imageId : imageIds) {
			// ԴͼƬ
			Bitmap resourceBitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
			int width1 = resourceBitmap.getWidth();
			int height1 = resourceBitmap.getHeight();
			// ����˶�ű���

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
			// ���ɵ�ӰͼƬ
			// Bitmap source ԴͼƬ
			// x,y ���ɵ�ӰͼƬ����ʼλ�� ���Ͻ�
			// width,height ͼƬ�Ŀ��
			// Matrix m ���� ����ͼƬ����ʽ ����Ӱ��
			Matrix matrix = new Matrix();
			// xˮƽ��ת y��ֱ��ת 1 ���� -1��ת
			matrix.setScale(1, -1);
			Bitmap refrectionBitmap = Bitmap.createBitmap(resourceBitmap1, 0, height / 2, width, height / 2, matrix, false);
			// ���е�Ӱ��ͼƬ
			Bitmap bitmap = Bitmap.createBitmap(width, height + height / 2, Config.ARGB_8888);
			// ��������
			Canvas canvas = new Canvas(bitmap);
			// ����ԴͼƬ
			canvas.drawBitmap(resourceBitmap1, 0, 0, null);
			// ���� ԭͼƬ�ڵ�Ӱ֮��ľ���
			Paint defaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + ReflectionGap, defaultPaint);
			// ���Ƶ�ӰͼƬ
			canvas.drawBitmap(refrectionBitmap, 0, height + ReflectionGap, null);

			// ps�� ���� ����
			Paint paint = new Paint();
			// ����
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// ����
			// 0x70ffffff, 0x00ffffff
			/*
			 * //���Խ������ shader��ɫ�� //��λͼ��Y����שģʽ TileMode����һ�������֣� CLAMP
			 * �������Ⱦ������ԭʼ�߽緶Χ���Ḵ�Ʒ�Χ�ڱ�ԵȾɫ�� REPEAT �������������ظ���Ⱦ��ͼƬ��ƽ�̡� MIRROR
			 * �������������ظ���Ⱦ��ͼƬ�������REPEAT �ظ���ʽ��һ���������Ծ���ʽƽ�̡�
			 */
			LinearGradient shader = new LinearGradient(0, height, 0, bitmap.getHeight(), 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// ��ɫ�� ����������ɫ ��ɫ��
			paint.setShader(shader);

			canvas.drawRect(0, height, width, bitmap.getHeight(), paint);

			// ����ͼƬ
			ImageView imageView = new ImageView(context);
			// imageView.setImageBitmap(resourceBitmap);
			BitmapDrawable bd = new BitmapDrawable(bitmap);
			bd.setAntiAlias(true);// ����ͼƬ���Ч�� ƽ��
			imageView.setImageDrawable(bd);
			// ����ͼƬ�Ĵ�С
			imageView.setLayoutParams(new GalleryFlow.LayoutParams(480, 426));
			images[index++] = imageView;

		}

	}

}
