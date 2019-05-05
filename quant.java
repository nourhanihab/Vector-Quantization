import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class quant {

	public static int[][] getmyimage(String filePath) {
		int width = 0;
		int height = 0;

		File file = new File(filePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		width = image.getWidth();
		System.out.println("Image width -- " + width);
		height = image.getHeight();
		System.out.println("Image height -- " + height);
		int[][] pixels = new int[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int alpha = (rgb >> 24) & 0xff;
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb >> 0) & 0xff;

				pixels[y][x] = r;
			}
		}

		return pixels;
	}

	public static void quantize(String path, int width1, int height1, int vectorsincodebook) throws IOException {

		Vector<Double> vec = new Vector<>();
		int[][] arr = getmyimage(path);
		File file = new File(path);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int width = image.getWidth();
		int height = image.getHeight();
		/*
		 * for (int i = 0; i <width; i++) { for (int j=0; j<height; j++) {
		 * vec.add((double) arr[i][j]); System.out.println(arr[i][j]); } } for (int i=0
		 * ; i <vec.size(); i++) { System.out.println(vec.get(i));
		 * 
		 * }/* vec.add((double) 1); vec.add((double) 2); vec.add((double) 7);
		 * vec.add((double) 9); vec.add((double) 4); vec.add((double) 11);
		 * vec.add((double) 3); vec.add((double) 4); vec.add((double) 6);
		 * vec.add((double) 6); vec.add((double) 12); vec.add((double) 12);
		 * vec.add((double) 4); vec.add((double) 9); vec.add((double) 15);
		 * vec.add((double) 14); vec.add((double) 9); vec.add((double) 9);
		 * vec.add((double) 10); vec.add((double) 10); vec.add((double) 20);
		 * vec.add((double) 18); vec.add((double) 8); vec.add((double) 8);
		 * vec.add((double) 4); vec.add((double) 3); vec.add((double) 17);
		 * vec.add((double) 16); vec.add((double) 1); vec.add((double) 4);
		 * vec.add((double) 4); vec.add((double) 5); vec.add((double) 18);
		 * vec.add((double) 18); vec.add((double) 5); vec.add((double) 6);
		 */

		if ((width % width1) == 0 && (height % height1) == 0) {
			System.out.println("Input can be compressed!");

		} else {
			System.out.println("Please enter a valid vector size!!");
			return;
		}

		Vector<Vector<Double>> vector2D = new Vector<Vector<Double>>();
		Vector<Vector<Double>> totalAverages = new Vector<Vector<Double>>();
		for (int i = 0; i < height; i += height1) {
			for (int j = 0; j < width; j += width1) {
				vector2D.add(new Vector<>());
				for (int x = i; x < i + height1; x++) {
					for (int y = j; y < j + width1; y++) {
						vector2D.lastElement().add((double) arr[x][y]);
					}
				}
			}
		}
		/*
		 * for (int i = 0; i<vector2D.size(); i++) {
		 * System.out.println(vector2D.get(i)); }
		 */

		Vector<Double> averages = new Vector<>();
		Double sum = (double) 0;
		for (int j = 0; j < vector2D.get(1).size(); j++) {
			for (int i = 0; i < vector2D.size(); i++) {
				sum += vector2D.get(i).get(j);
			}
			averages.add((double) Math.round(sum / vector2D.get(1).size()));
			sum = (double) 0;

		}
		totalAverages.add(averages);
			
			System.out.println("------ the initial vectors ------");
		  for (int i = 0; i <totalAverages.size(); i++) {
		  System.out.println(totalAverages.get(i)); }
		 

		while (totalAverages.size() < vectorsincodebook) {
			FileWriter myff = new FileWriter("averages.txt");
			BufferedWriter mywrite = new BufferedWriter(myff);

			System.out.println("---------------------------------");

			// System.out.println("avvvvv!!!" + average);

			int mysize = totalAverages.size();
			Vector<Double> roundu = new Vector<>();
			Vector<Double> roundd = new Vector<>();
			for (int i = 0; i < mysize; i++) {
				for (int j = 0; j < totalAverages.get(0).size(); j++) {
					roundu.add(totalAverages.get(i).get(j) + 1);
					roundd.add(totalAverages.get(i).get(j) - 1);

				}
				
				totalAverages.add(roundd);
				totalAverages.set(i, roundu);
			}

			for (int i = 0; i < totalAverages.size(); i++) {
				mywrite.write(totalAverages.get(i)+" ");

			}
			Vector<Vector<Splitted>> mylevels = new Vector<>();
			for (int i = 0; i < totalAverages.size(); i++) {
				Vector<Splitted>  s = new Vector<>();
				mylevels.add(s);

			}
			
			for (int i = 0; i < vector2D.size(); i++) {
				int myind = minval(vector2D.get(i), totalAverages);
				System.out.println(myind + " +++++++ ");
				for (int j= 0; j <vector2D.get(i).size(); j++) {
					mylevels.get(myind).get(j).total+=vector2D.get(i).get(j);
					mylevels.get(myind).get(j).counter+=1;					
					
			}
				}

			totalAverages.clear();
			System.out.println("!!!!! mylevel sive" + mylevels.size());
			for (int i = 0; i < mylevels.size(); i++) 
			for (int j=0 ; j<mylevels.get(i).size(); j++){{
				System.out.println("total  " + mylevels.get(i).get(j).total + "     " + mylevels.get(i).get(j).counter);
				Double t = (double) Math.round(mylevels.get(i).get(j).total / mylevels.get(i).get(j).counter);
				averages.add(t);
			}
			}
			totalAverages.sort(null);
			for (int i = 0; i < totalAverages.size(); i++) {
				System.out.println(totalAverages.get(i));
				//mywrite.write(totalAverages.get(i)+ " ");
			}

		} 
		
		

	}
	
	public static int minval(Vector<Double> vector, Vector<Vector<Double>> totalAverages) {
		int index = 0;
		Vector<Double> diff = new Vector<>();
		//Vector<Double> in = new Vector<>();
		double min_diff = 0;
		Double sum =(double) 0;
		for (int i = 0; i < totalAverages.size(); i++) {
			for (int j=0 ; j < vector.size(); j++) {
			sum +=Math.abs(vector.get(j) - totalAverages.get(i).get(j));
			System.out.println(sum);
			diff.add(sum);
			//in.add((double) i);
			sum=(double) 0;
			//diff.add(sum);
			}
			//diff.add(sum);
			//sum=(double) 0;
			
			
		}
		
		//System.out.println(sum);
		//diff.add(sum);
		min_diff = diff.get(0);
		for (int i = 1; i < diff.size(); i++) {
			if (diff.get(i) < min_diff) {
				min_diff = diff.get(i);
				index = i;
			}

		}
		//System.out.println(index);
		return index;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		quantize("myimg.jpg", 2, 2, 2);

	}

}
