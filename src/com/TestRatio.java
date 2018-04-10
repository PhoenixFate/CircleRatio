package com;

import java.util.ArrayList;
import java.util.List;

//中心点
class Point {
	private double x;
	private double y;

	public Point() {
		super();
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}

// 圆
class Circle {
	private Point center;
	private double r;

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}
}

public class TestRatio {
	public static void main(String[] args) {

		// 新建4个圆
		Circle c1 = new Circle();
		Circle c2 = new Circle();
		Circle c3 = new Circle();
		Circle c4 = new Circle();

		// 4个圆的中心点
		Point p1 = new Point(11, 5);
		Point p2 = new Point(1, 2);
		Point p3 = new Point(5, 10);
		Point p4 = new Point(2, 5);
		c1.setCenter(p1);
		c2.setCenter(p2);
		c3.setCenter(p3);
		c4.setCenter(p4);

		// 设置4个圆的半径
		c1.setR(2.0);
		c2.setR(2.0);
		c3.setR(3.0);
		c4.setR(3.0);

		// 区域的长度和宽度
		double length = 10;
		double width = 10;

		// 将4个圆放入一个数组中
		List<Circle> list = new ArrayList<Circle>();
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		
		double ratio=getRatio(list, width, length);
		System.out.println("覆盖率:" + ratio * 100 + "%");

	}

	/*
	 * list为一群圆的数组 
	 * width为区域的宽度 
	 * length为区域的长度
	 */
	static double getRatio(List<Circle> list, double width, double length) {
		// minusArea为需要减去的面积的累和
		double minusArea = 0;
		// circleArea为圆面积之和
		double circleArea = 0;
		// temp1为两圆重叠的面积
		double temp1 = 0;
		// temp2为圆与长方形不重叠的面积
		double temp2 = 0;
		// 循环两两遍历是否有交集
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				// 最后一个元素与第一个元素是否有交集
				temp1 = circleArea(list.get(i), list.get(0));
			} else {
				// 第i个圆与第i+1个圆是否有交集
				temp1 = circleArea(list.get(i), list.get(i + 1));
			}
			System.out.println("两圆相交的面积：" + temp1);
			temp2 = rectangleArea(list.get(i), width, length);
			System.out.println("圆与长方形不重叠的面积：" + temp2);
			minusArea += temp1 + temp2;
			circleArea += list.get(i).getR() * list.get(i).getR() * Math.PI;
		}
		System.out.println("需要减去的总面积：minusArea:" + minusArea);
		System.out.println("所有圆的总面积：circleArea:" + circleArea);
		// ratio为覆盖率
		// 覆盖率为圆的面积之和减去相交的面积，减去矩形之外的面积，然后和矩形之间的比率
		// 覆盖率的公式:(J1+J2+J3-y1-y2-y3-c1-c2)/(M*N);
		double ratio = (circleArea - minusArea) / (width * length);
		//System.out.println("覆盖率:" + ratio * 100 + "%");

		return ratio;
	}

	// 两圆圆心点距离
	static double distance(Point a, Point b) {
		return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()));
	}

	// 两圆相交的重叠面积
	static double circleArea(Circle c1, Circle c2) {
		// 两圆内交或包含
		if ((distance(c1.getCenter(), c2.getCenter()) + Math.min(c1.getR(), c2.getR())) <= Math.max(c1.getR(),
				c2.getR()))
			return Math.min(c1.getR(), c2.getR()) * Math.min(c1.getR(), c2.getR()) * Math.PI;

		// 两圆外交或相离
		else if (distance(c1.getCenter(), c2.getCenter()) >= (c1.getR() + c2.getR()))
			return 0;

		// 两圆相交
		else {
			// 两圆心之间的距离
			double length = distance(c1.getCenter(), c2.getCenter());
			// 两扇形圆心角
			double d1 = 2 * Math
					.acos((c1.getR() * c1.getR() + length * length - c2.getR() * c2.getR()) / (2 * c1.getR() * length));
			double d2 = 2 * Math
					.acos((c2.getR() * c2.getR() + length * length - c1.getR() * c1.getR()) / (2 * c2.getR() * length));
			// 根据圆心角求扇形面积，减去三角形面积，得到相交部分面积
			//// 扇形面积：S=PI*r*r*θ/(2*PI) 三角形面积：S=1/2*a*c*sin(B)
			double area1 = c1.getR() * c1.getR() * d1 / 2 - c1.getR() * c1.getR() * Math.sin(d1) / 2;
			double area2 = c2.getR() * c2.getR() * d2 / 2 - c2.getR() * c2.getR() * Math.sin(d2) / 2;
			double area = area1 + area2;
			return area;
		}
	}

	// 圆与长方形相交的不重叠面积
	static double rectangleArea(Circle c1, double width, double length) {
		double length2 = 0;
		double d12 = 0;
		double area12 = 0;
		double smallRectangle = 0;

		// 圆的上边超过长方形
		if ((c1.getCenter().getY() + c1.getR() > width)) {
			length2 = 2 * (c1.getR() - (c1.getCenter().getY() + c1.getR() - width));
			area12 = smallArea(c1, d12, area12, length2);
			if (c1.getCenter().getY() > width) {
				area12 = c1.getR() * c1.getR() * Math.PI - area12;
			}
		}
		// 圆的下边超过长方形
		if ((c1.getCenter().getY() - c1.getR()) < 0) {
			length2 = 2 * Math.abs(c1.getCenter().getY());
			area12 = smallArea(c1, d12, area12, length2);
			if (c1.getCenter().getY() < 0) {
				area12 = c1.getR() * c1.getR() * Math.PI - area12;
			}
		}
		// 圆的的右侧超过长方形
		if ((c1.getCenter().getX() + c1.getR()) > width) {
			length2 = 2 * Math.abs(width - c1.getCenter().getX());
			area12 = smallArea(c1, d12, area12, length2);
			if (c1.getCenter().getX() > width) {
				area12 = c1.getR() * c1.getR() * Math.PI - area12;
			}
		}
		// 圆的左侧超过长方形
		if ((c1.getCenter().getX() - c1.getR()) < 0) {
			length2 = 2 * Math.abs(c1.getCenter().getX());
			area12 = smallArea(c1, d12, area12, length2);
			if (c1.getCenter().getX() < 0) {
				area12 = c1.getR() * c1.getR() * Math.PI - area12;
			}
		}
		// 特殊情况：圆在四个角落上
		if ((c1.getCenter().getX() + c1.getR()) > length && (c1.getCenter().getY() + c1.getR()) > width) {
			smallRectangle = 0.785 * (c1.getCenter().getX() + c1.getR() - length)
					* (c1.getCenter().getY() + c1.getR() - width);
		}
		if ((c1.getCenter().getX() + c1.getR()) > length && (c1.getCenter().getY() - c1.getR()) < 0) {
			smallRectangle = 0.785 * (c1.getCenter().getX() + c1.getR() - length) * (c1.getR() - c1.getCenter().getY());
		}
		if ((c1.getCenter().getX() - c1.getR()) < 0 && (c1.getCenter().getY() + c1.getR()) > width) {
			smallRectangle = 0.785 * (c1.getR() - c1.getCenter().getX()) * (c1.getCenter().getY() + c1.getR() - width);
		}
		if ((c1.getCenter().getX() - c1.getR()) < 0 && (c1.getCenter().getY() - c1.getR()) < 0) {
			smallRectangle = 0.785 * (c1.getR() - c1.getCenter().getX()) * (c1.getR() - c1.getCenter().getY());
		}
		return area12 - smallRectangle;
	}

	// 封装不重叠面积计算
	static double smallArea(Circle c1, double d12, double area12, double length2) {
		// 先算出圆心角
		// d12=2*Math.acos((length2*length2)/(2*c1.getR()*length2));
		d12 = 2 * Math.acos((length2) / (2 * c1.getR()));
		// 面积=扇形面积-三角形面积
		double area13 = c1.getR() * c1.getR() * d12 / 2 - c1.getR() * c1.getR() * Math.sin(d12) / 2;
		area13 += area12;// 不重叠面积可能涉及四面，所以需要累加
		return area13;
	}

}
