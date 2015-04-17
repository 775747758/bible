package com.example.acts;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {

	public GalleryFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setStaticTransformationsEnabled(true);
	}

	public GalleryFlow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setStaticTransformationsEnabled(true);
	}

	private Camera camera = new Camera();

	private int maxRotateAngle = 50;// �����ת�Ƕ�
	private int maxZoom = -250;// �������ֵ

	private int CurrentOfGallery;

	// ���galleryչʾͼƬ�����ĵ�
	public int getCurrentOfGallery() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	// ���ͼƬ�����ĵ�
	public int getCurrentOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		CurrentOfGallery = getCurrentOfGallery();

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// TODO Auto-generated method stub

		// �õ�ͼƬ�����ĵ�
		int currentOfChild = getCurrentOfView(child);
		int height = child.getLayoutParams().height;
		int width = child.getLayoutParams().width;
		int rotateAngle = 0;// ��ת�Ƕ�

		t.clear();
		// ͼƬ���εķ����ʽ
		t.setTransformationType(Transformation.TYPE_MATRIX);

		if (currentOfChild == CurrentOfGallery) {
			// ����
			transformationBitmap((ImageView) child, t, 0);
		} else {
			// ��������λ��
			rotateAngle = (int) ((float) (CurrentOfGallery - currentOfChild) / width * maxRotateAngle);
			if (Math.abs(rotateAngle) > maxRotateAngle) {
				rotateAngle = rotateAngle < 0 ? -maxRotateAngle : maxRotateAngle;
			}

			transformationBitmap((ImageView) child, t, rotateAngle);

		}
		return true;
	}

	// ͼƬ����
	private void transformationBitmap(ImageView child, Transformation t, int rotateAngle) {
		// TODO Auto-generated method stub

		camera.save();// ����

		Matrix imageMatrix = t.getMatrix();
		int rotate = Math.abs(rotateAngle);
		int imageWidth = child.getWidth();
		int imageHeight = child.getHeight();

		// z�� ���� ͼƬ��� xˮƽ�ƶ� y��ֱ�ƶ�
		camera.translate(0.0f, 0.0f, 100.0f);

		if (rotate < maxRotateAngle) {
			float zoom = (float) ((rotate * 1.5) + maxZoom);
			camera.translate(0.0f, 0.0f, zoom);
			child.setAlpha((int) (255 - rotate * 2.5));
		}

		// ͼƬ��չʾ���� ���д�ֱ�Ƕȵ���ת
		camera.rotateY(rotateAngle);

		camera.getMatrix(imageMatrix);

		// Preconcats matrix�൱���ҳ˾���
		// Postconcats matrix�൱����˾���
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		/*
		 * �����д�����˼���ܾͲ���ô�����ˣ���˵������������д��룬����һ��ʲô����� Ĭ������£��������Զ�������Ͻ�Ϊ���ģ���������Ļ���
		 * ������Ч���ͱ���˿ɼ��������������Ͻǿ�ʼ���������½���������Ȼ�������������ġ�
		 * ��������ǰ���õ���halfWidth��halfHeight���õ��ˣ����ﱣ���˿ɼ������һ���Ⱥ͸߶ȣ�Ҳ�����е㣬
		 * ʹ�����������������󣬾ͻ�ı䶯������ʼλ�ã�����Ĭ���Ǵ����½ǿ�ʼ����ģ�
		 * ʹ��matrix.preTranslate(-halfWidth, -halfHeight) �Ͱ���ɢ���Ƶ����м䣬
		 * ͬ������������ʼ��Ϊ���Ͻǣ�ʹ��matrix.postTranslate(halfWidth,
		 * halfHeight)�Ͱ���ʼ���Ƶ����м䣬 ������ʵ������������Ч���ˡ�
		 */

		camera.restore();// ��ԭ

	}

}
