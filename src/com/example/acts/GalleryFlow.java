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

	private int maxRotateAngle = 50;// 最大旋转角度
	private int maxZoom = -250;// 最大缩放值

	private int CurrentOfGallery;

	// 获得gallery展示图片的中心点
	public int getCurrentOfGallery() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	// 获得图片的中心点
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

		// 得到图片的中心点
		int currentOfChild = getCurrentOfView(child);
		int height = child.getLayoutParams().height;
		int width = child.getLayoutParams().width;
		int rotateAngle = 0;// 旋转角度

		t.clear();
		// 图片变形的风格样式
		t.setTransformationType(Transformation.TYPE_MATRIX);

		if (currentOfChild == CurrentOfGallery) {
			// 中心
			transformationBitmap((ImageView) child, t, 0);
		} else {
			// 不是中心位置
			rotateAngle = (int) ((float) (CurrentOfGallery - currentOfChild) / width * maxRotateAngle);
			if (Math.abs(rotateAngle) > maxRotateAngle) {
				rotateAngle = rotateAngle < 0 ? -maxRotateAngle : maxRotateAngle;
			}

			transformationBitmap((ImageView) child, t, rotateAngle);

		}
		return true;
	}

	// 图片变形
	private void transformationBitmap(ImageView child, Transformation t, int rotateAngle) {
		// TODO Auto-generated method stub

		camera.save();// 保存

		Matrix imageMatrix = t.getMatrix();
		int rotate = Math.abs(rotateAngle);
		int imageWidth = child.getWidth();
		int imageHeight = child.getHeight();

		// z轴 正数 图片变大 x水平移动 y垂直移动
		camera.translate(0.0f, 0.0f, 100.0f);

		if (rotate < maxRotateAngle) {
			float zoom = (float) ((rotate * 1.5) + maxZoom);
			camera.translate(0.0f, 0.0f, zoom);
			child.setAlpha((int) (255 - rotate * 2.5));
		}

		// 图片向展示中心 进行垂直角度的旋转
		camera.rotateY(rotateAngle);

		camera.getMatrix(imageMatrix);

		// Preconcats matrix相当于右乘矩阵
		// Postconcats matrix相当于左乘矩阵。
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		/*
		 * 这两行代码意思可能就不那么明显了，先说如果不加这两行代码，会是一个什么情况， 默认情况下，动画是以对象的左上角为起点的，如果这样的话，
		 * 动画的效果就变成了可见对象在它的左上角开始，逐渐向右下角扩大，这显然不是我们期望的。
		 * 所以我们前面用到的halfWidth，halfHeight就用到了，这里保存了可见对象的一半宽度和高度，也就是中点，
		 * 使用上面这两个方法后，就会改变动画的起始位置，动画默认是从右下角开始扩大的，
		 * 使用matrix.preTranslate(-halfWidth, -halfHeight) 就把扩散点移到了中间，
		 * 同样，动画的起始点为左上角，使用matrix.postTranslate(halfWidth,
		 * halfHeight)就把起始点移到了中间， 这样就实现我们期望的效果了。
		 */

		camera.restore();// 还原

	}

}
