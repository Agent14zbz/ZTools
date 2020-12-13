package demoTest;

import geometry.ZLine;
import geometry.ZPoint;
import math.ZGeoMath;
import processing.core.PApplet;
import wblut.geom.WB_GeometryOp2D;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.processing.WB_Render2D;

import java.util.List;

/**
 * 测试多边形等分、沿多边形边找点、多边形边线offset
 *
 * @author ZHANG Bai-zhou zhangbz
 * @project shopping_mall
 * @date 2020/10/24
 * @time 17:14
 */
public class TestPolySplit extends PApplet {
    public void settings() {
        size(1000, 1000, P2D);
    }

    WB_Render2D render;
    WB_Polygon poly;
    WB_PolyLine pl;

    ZPoint test = new ZPoint(600, 100);
    ZPoint[] besides = new ZPoint[2];
    List<ZPoint> split;

    int count = 0;
    int index;
    ZLine offset;
    double step;

    public void setup() {
        render = new WB_Render2D(this);

        WB_Point[] pts1 = new WB_Point[6];
        WB_Point[] pts2 = new WB_Point[5];
        pts2[0] = pts1[0] = new WB_Point(100, 100);
        pts2[1] = pts1[1] = new WB_Point(700, 100);
        pts2[2] = pts1[2] = new WB_Point(800, 400);
        pts2[3] = pts1[3] = new WB_Point(500, 800);
        pts2[4] = pts1[4] = new WB_Point(100, 600);
        pts1[5] = new WB_Point(100, 100);
        poly = new WB_Polygon(pts1);
        pl = new WB_PolyLine(pts2);
        besides = ZGeoMath.pointsOnEdgeByDist(test, poly, 450);

        step = 50;
        split = ZGeoMath.splitWB_PolyLineEdgeByThreshold(pl, 90, 84);
        println("split: " + split.size());

        for (ZPoint p : split) {
            println(WB_GeometryOp2D.contains2D(p.toWB_Point(), poly));
        }
        index = count % poly.getNumberSegments();
        println(index);
        offset = ZGeoMath.offsetWB_PolygonSegment(poly, index, 30);
    }

    public void draw() {
        background(255);
        noFill();
        strokeWeight(1);
//        render.drawPolygonEdges2D(poly);
        render.drawPolyLine2D(pl);
        pushStyle();
        fill(255, 0, 0);
        render.drawPoint2D(poly.getSegment(index).getOrigin(), 10);
        fill(0, 0, 255);
        render.drawPoint2D(poly.getSegment(index).getEndpoint(), 10);

        fill(0, 255, 0);
        test.displayAsPoint(this);
        for (ZPoint p : besides) {
            p.displayAsPoint(this);
        }
        popStyle();
        strokeWeight(4);
        render.drawSegment2D(poly.getSegment(index));
        offset.display(this);

        for (ZPoint p : split) {
            p.displayAsPoint(this);
        }
    }

    public void mouseClicked() {
        count++;
        index = count % poly.getNumberSegments();
        offset = ZGeoMath.offsetWB_PolygonSegment(poly, index, 30);
    }

    public void mouseDragged() {
        step = mouseX;
        split = ZGeoMath.splitWB_PolyLineEdgeByStep(poly, step);
        println("split: " + split.size());
    }
}