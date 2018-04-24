import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class Main {

//	private final static double latitude1 = 593837.0603774042;
//	private final static double longitude1 = 175508.8209644216;
//	private final static double latitude2 = 593836.6696454831;
//	private final static double longitude2 = 175508.9958579471;
	private final static double x0 = 59.66618805, y0 = 17.91346357;
	
	public static void main(String[] args) {

		//double [] cord1 = getCoordinates(latitude1,longitude1), cord2 = getCoordinates(latitude2, longitude2);
        
        //initialise();
        
		initialiseXY();
		
//		double[] xy = translate_coordinates(x0, y0, 59.65854929, 17.92451849);
//		System.out.println(xy[0] + " , " + xy[1]);
		
//		System.out.println(cord1[0] + " - " + cord1[1]);
//		System.out.println(cord2[0] + " - " + cord2[1]);
//        System.out.println(distance(cord1[0], cord1[1], cord2[0], cord2[1]));
        
	}
	
	private static double[] translate_coordinates(double slat, double slon, double olat, double olon) {
		
		double xx, yy, r, ct, st, angle, rotation_angle_degs = 0, xoffset_mtrs = 0, yoffset_mtrs = 0, pxpos_mtrs, pypos_mtrs;
		
		angle = deg2rad(rotation_angle_degs);

		xx = (slon - olon) * METERS_DEGLON(olat);
		yy = (slat - olat) * METERS_DEGLAT(olat);

		r = Math.sqrt(xx * xx + yy * yy);

		if (r > 0) {
			ct = xx / r;
			st = yy / r;
			xx = r * ((ct * Math.cos(angle)) + (st * Math.sin(angle)));
			yy = r * ((st * Math.cos(angle)) - (ct * Math.sin(angle)));
		}
		pxpos_mtrs = xx + xoffset_mtrs;
		pypos_mtrs = yy + yoffset_mtrs;

		return (new double[] { pxpos_mtrs, pypos_mtrs });
	}

	private static double METERS_DEGLON(double x) {
		double d2r = deg2rad(x);
		return ((111415.13 * Math.cos(d2r)) - (94.55 * Math.cos(3.0 * d2r)) + (0.12 * Math.cos(5.0 * d2r)));
	}

	private static double METERS_DEGLAT(double x) {
		double d2r = deg2rad(x);
		return (111132.09 - (566.05 * Math.cos(2.0 * d2r)) + (1.20 * Math.cos(4.0 * d2r)) - (0.002 * Math.cos(6.0 * d2r)));
	}
	
	private static void initialiseXY() {
		Workbook w;
		try {
			w = Workbook.getWorkbook(new File("Nodes.xls"));
			Sheet s3 = w.getSheet(3);

			for (int i = 1; i < s3.getRows(); i++) {
				double[] xy = translate_coordinates(x0, y0, Double.parseDouble(s3.getCell(0, i).getContents()), Double.parseDouble(s3.getCell(1, i).getContents()));
				System.out.println(Math.abs(xy[0]) + "	" + xy[1]);
			}

		} catch (BiffException | IOException e) {
		}
	}
	
	private static void initialise() {
		try {
			Workbook w = Workbook.getWorkbook(new File("Nodes.xls"));
			Sheet s0 = w.getSheet(0), s1 = w.getSheet(1);

			for (int i = 2; i < s0.getRows(); i++) { //s1

				//System.out.println(Integer.parseInt(s1.getCell(0, i).getContents()) + " - " + Integer.parseInt(s1.getCell(1, i).getContents()));

				//int e1 = Integer.parseInt(s1.getCell(0, i).getContents()) + 2, e2 = Integer.parseInt(s1.getCell(1, i).getContents()) + 2;
				//double[] cord1 = getCoordinates(Double.parseDouble(s0.getCell(1, e1).getContents()), Double.parseDouble(s0.getCell(2, e1).getContents())), cord2 = getCoordinates(Double.parseDouble(s0.getCell(1, e2).getContents()), Double.parseDouble(s0.getCell(2, e2).getContents()));
				
				
				double[] cord = getCoordinates(Double.parseDouble(s0.getCell(1, i).getContents()), Double.parseDouble(s0.getCell(2, i).getContents()));
				
				System.out.println(cord[0] + ", " + cord[1]);
				
				//System.out.println(distance(cord1[0], cord1[1], cord2[0], cord2[1]));
			}
			
//			for (int i = 2; i < s.getRows(); i++)
//				System.out.println((s.getCell(2, i)).getContents());
				//System.out.println(( (NumberCell) s.getCell(2, i)).getValue());
				//nodes.add(new Vertex(Integer.parseInt(s.getCell(0, i).getContents()), new double[] { Double.parseDouble(s.getCell(1, i).getContents()), Double.parseDouble(s.getCell(2, i).getContents()),Double.parseDouble(s.getCell(3, i).getContents()), Double.parseDouble(s.getCell(4, i).getContents()) }));
		} catch (BiffException | IOException e) {
		}
	}
	
	private static double[] getCoordinates(double latitude, double longitude) {
		double lat, lon, reminder;

        reminder = latitude;
        lat = (reminder - reminder % 10000) / 10000;
        reminder -= 10000*lat;
        double lat_min = (reminder - reminder % 100) / 100;
        reminder -= 100 * lat_min;
        lat += lat_min / 60 + reminder / 3600;

        reminder = longitude;
        lon = (reminder - reminder % 10000) / 10000;
        reminder -= 10000 * lon;
        double lon_min = (reminder - reminder % 100) / 100; ;
        reminder -= 100 * lon_min;
        lon += lon_min / 60 + reminder / 3600;
        
        return new double[] {lat, lon};
	}

	private static double distance(double lat1, double lon1, double lat2, double lon2) {
		return Math.acos(Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(lon1 - lon2))) * 6371000;
	}
	
	private static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}
	
	private double euclideanDist(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
} 
