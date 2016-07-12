package com.ten.dao.implementation;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.ten.dao.implementation.DaoConstants;


/**
 * The major class containing feature extraction,
 * frame difference and employs twin-comparison
 * algorithm. Provides first frame numbers for
 * the detected shots.
 * @author Jay
 * @version 2.0
 */
public class VideoShotDesign extends JFrame
{
	private static final long serialVersionUID = 1L;
	private FFmpegFrameGrabber frameGrabber;
	URL mediaURL;
	File url;
	ArrayList<Integer> cutSet = new ArrayList<Integer>();
	ArrayList<Integer> FsList = new ArrayList<Integer>();
	ArrayList<Integer> FeList = new ArrayList<Integer>();
	ArrayList<Integer> mergeList = new ArrayList<Integer>();
	double[] intensityBins = new double[25];
	double[][] intensityMatrix = new double[4000][25];
	double[] sd = new double[3999];

	double[] imageSize = new double[28];
	double tb, ts, tor;
	int Fs, Fe;
	int imageCount;
	JButton[] buttons;	
	JPanel panel;
	
	
	public ArrayList<Integer> getMergeList() {
		return mergeList;
	}
	/**
	 * This method starts FFmpeg frame grabber and extracts individual frames
	 * for the duration specified for the video, 1000-5000 frame duration in this case.
	 * It can be changed to work on the entire video or part of the video.
	 * @param videoFileName
	 */
	public void detectFrameHandler(String videoFileName)
	{
		intensityBins = new double[26];
		intensityMatrix = new double[5000][26];
		imageCount = 1000;
		url = new File(DaoConstants.FILES_DIR + videoFileName);
		//File file = new File("bin/video/sample.mp4");
		System.out.println("URL:" + url);
		frameGrabber = new FFmpegFrameGrabber(url); 
		
		try 
		{
			frameGrabber.start();
			
			for(int i = 1000; i < 5000; i++)
			{
				frameGrabber.setFrameNumber(i);
				
				//Have to use FrameConverter.
				Java2DFrameConverter conversion = new Java2DFrameConverter();
				Frame f = frameGrabber.grabFrame();
				BufferedImage bi = new BufferedImage(frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), BufferedImage.TYPE_INT_RGB);
				bi = (BufferedImage) conversion.getBufferedImage(f);
				readFrames(bi);
			}
			//writeIntensity();
			readIntensityFile();
			frameGrabber.stop();
		}
		catch(Exception e)
		{
			System.out.println("Exception inside Detect Frame Handler");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is called for every frame captured.
	 * It calculates Intensity color histogram bins for
	 * all the image frames.
	 * @param img
	 */
	public void readFrames(BufferedImage img)
	{		
		int red, blue, green, height, width;
		try
		{
			// The line that reads the image file
			//BufferedImage img = ImageIO.read(getClass().getResource("frames/" + imageCount + ".png"));
			width = img.getWidth();
			height = img.getHeight();
			
			//Initializing the bins for both techniques.
			for (int i = 1 ; i <= 25; i++)
				intensityBins[i] = 0;
			
			//Grabbing the Image RGB values and passing them 
			//for getting intensities and color codes.
			for(int x = 0; x < width; x++)
			{
				for(int y = 0; y < height; y++)
				{
					red = new Color(img.getRGB(x, y)).getRed();
					blue = new Color(img.getRGB(x, y)).getBlue();
					green = new Color(img.getRGB(x, y)).getGreen();
					getIntensity(red, blue , green);
				}
			}
			for(int i = 1; i <= 25; i++)
			{	
				intensityMatrix[imageCount][i] = intensityBins[i];
				//System.out.print("intensityMatrix[" + imageCount + "] [" + i + "] = " + intensityMatrix[imageCount][i]);
			}
			imageCount++;
			System.out.println();
		} 
		catch (Exception e)
		{
			System.out.println("Error occurred when reading the Frames.");
		}
	}
	
	/**
	 * Additional method for storing Intensity values for a video.
	 */
	/*
	public void writeIntensity()
	{
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		try
		{
			String intensityValue;
			File file = new File(DaoConstants.FRAMES_DIR + "intensity.txt");
			System.out.println("Inside writeIntensity!!!");
			// if file doesn't exists, then create it
			if (!file.exists())
				file.createNewFile();
			
			fw = new FileWriter(file);//file.getAbsoluteFile());
			bw = new BufferedWriter(fw); 
			for(int i = 1000; i <= 4999; i++)
			{
				for(int j = 1; j <= 25; j++)
				{
					intensityValue = (j==25)? Integer.toString((int)intensityMatrix[i][j]) : Integer.toString((int)intensityMatrix[i][j])+",";
					bw.write(intensityValue);	 
				}				
				bw.newLine();
			}
		}
		catch (IOException io)
		{
			System.out.println("IO Exception occurred when reading the file." + io);
		}
		catch(Exception e)
		{
			System.out.println("Error occurred when reading the file." + e);		 
		}
		finally
		{
			try
			{
				bw.close();
				fw.close();
			}
			catch (IOException io)
			{
				System.out.println("IO Exception occurred when reading the file." + io);
			}
			catch(Exception e)
			{
				System.out.println("Error occurred when reading the file." + e);		 
			}
		}
	}*/
	
	/**
	 * This method continues to process intensity matrix having 
	 * the histogram bin values for each image. The contents of 
	 * the matrix are processed and stored in a two 
	 * dimensional array called intensityMatrix and computes the
	 * SD values (frame-frame difference)in sd array.
	 */
	public void readIntensityFile()
	{
		double dist = 0;
		Scanner read;
		File temp;
		BufferedWriter bw = null;
		StringTokenizer token;
		String line1 = "";
		double line = 0;
		int binIndex = 0, lineNumber = 0;
		//Additional logic to store SD values if required.
		/*try{
			temp = new File(DaoConstants.FRAMES_DIR + "intensity.txt");
			
			//InputStream stream = VideoShotDesign.class.getResourceAsStream("bin/intensity.txt");
			read = new Scanner(temp);
			System.out.println("Inside Read intensityFile...");
			while(read.hasNextLine() && lineNumber <= 3999)
			{
				line1 = read.nextLine();
				token = new StringTokenizer(line1, ",");
				while(token.hasMoreTokens() && binIndex < 25)
				{
					intensityMatrix[lineNumber][binIndex] = (double) Integer.parseInt(token.nextToken());
					binIndex++;
				}
				binIndex = 0;
				lineNumber++;	
			}

		}
		catch(Exception e)
		{
			System.out.println("Exception occured : " + e);
		}*/
		try
		{
			System.out.println("Inside calculating and Writing SD");
			temp = new File(DaoConstants.FRAMES_DIR + "sd.txt");
			if (!temp.exists())
				temp.createNewFile();

			bw = new BufferedWriter(new FileWriter(temp));
		
			for(int i = 0; i < 3999; i++)
			{
				dist = 0;
				for(int j = 0; j < 25; j++)
				{
					dist += Math.abs(intensityMatrix[i][j] - intensityMatrix[i+1][j]); 
				}
				sd[i] = dist;
				System.out.println("SD[" + i + "] = " + sd[i]);
			}
			
			for(int i = 0; i < 3999; i++)
			{
				bw.write(Double.toString(sd[i]));
				bw.newLine();
			}
			
		}	
		catch(IOException ioe)
		{
			System.out.println("Cannot write to sd.txt");
		}
		finally
		{
			try
			{
				bw.flush();
				bw.close();
			}	
			catch(IOException ioe)
			{
				System.out.println("Cannot write to sd.txt");
			}
		}
		
		
		/**
		 * Pre-computed SD values using the above code which is commented.
		 * Pre-computed for efficiency.
		 * Generally any .mp4 video can be processed. 
		 */
		/*
		try{
			//InputStream stream = VideoShotDesign.class.getResourceAsStream("bin/sd.txt");
			temp = new File(DaoConstants.FRAMES_DIR + "sd.txt");
			if(!temp.exists())
				temp.createNewFile();
			System.out.println("SD txt file created");
			read = new Scanner(temp);
			while(read.hasNext() && binIndex < sd.length)
			{
				line = Double.parseDouble(read.nextLine());
				sd[binIndex++] = line;
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception occurred: " + e);
		}
		*/
		setThresholds(); 
		System.out.println("Tb: " + tb);
		System.out.println("Ts: " + ts);
		System.out.println("Tor: " + tor);
		performSceneDetection();
	}
	
	/**
	 * This method deals with setting the cut
	 * and gradual transition thresholds.
	 */
	public void setThresholds()
	{
		double sum = 0;
		double avg = 0;
		double stddev = 0;
		double meanDiff = 0;
		double meanSum = 0;
		for(int i = 0; i < 3999; i++) //Can change the number of frames (duration) of video to be processed.
			sum += sd[i];
		avg = sum/3999;				//3999 consecutive differences i.e. SD values
		
		for(int i = 0; i < 3999; i++)
		{	
			meanDiff = 0.0;
			if(sd[i] == 0.0)
				continue;
			meanDiff = Math.pow((sd[i] - avg), 2);
			meanSum += meanDiff;
		}	
		stddev = Math.sqrt(meanSum/3999);
		
		tb = avg + stddev * 11;
		ts = avg * 2;
		tor = 2;
	}
	
	public void performSceneDetection()
	{
		cutSet = new ArrayList<Integer>();
		for(int i = 0; i < sd.length; i++)
		{
			if (sd[i] >= tb)
                cutSet.add(i + 1001);
		}
		findGradualTransition();
		mergeCutAndFs();
		createThumbs();
	}
	
	/** 
	 * Display or Store the image frames in web content or somewhere.
	 * Newer logic in video_presentation.jsp for extracting image thumbnails
	 * from the HTML5 video element on the client side itself.
	 */
	public void createThumbs()
	{
		try
		{
			frameGrabber = new FFmpegFrameGrabber(url);
			File outImage = null; 
			frameGrabber.start();	
			for(Integer i : mergeList)
			{
				frameGrabber.setFrameNumber(i.intValue());
				outImage = new File(DaoConstants.THUMBS_DIR + i + ".png");
				//Have to use FrameConverter.
				Java2DFrameConverter conversion = new Java2DFrameConverter();
				Frame f = frameGrabber.grabFrame();
				BufferedImage bi = new BufferedImage(frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), BufferedImage.TYPE_INT_RGB);
				bi = (BufferedImage) conversion.getBufferedImage(f);
				ImageIO.write(bi, "png", outImage);
			}
		}	
		catch(Exception e)
		{
			System.out.println("Error in creating Thumbnails!!!");
			e.printStackTrace();
		}
	}
	
	public void findGradualTransition()
	{
		for (int i = 0; i < sd.length; i++)
        {
            if ((ts <= sd[i]) && (sd[i] < tb))
            {
                Fs = i;
                Fe = checkFe(i);
                i = Fe;
                confirmFsFe(Fs, Fe);
            }
        }
	}
	
	int checkFe(int index)
    {
        boolean check = false;
        while (index < sd.length - 1)
        {
            if(((sd[index + 1] < ts) && (sd[index + 2] < ts)) || (cutSet.contains(index + 1001)))
            {
                check = true;
            }
            if(check)
                break;
            else if (!check) { index++; }
        }
        return index;
    }
	
	void confirmFsFe(int fs, int fe)
    {
        double sum = 0.0;
        for (int i = fs; i <= fe; i++)
            sum += sd[i];
        if (sum >= tb)
        {
            FsList.add(fs + 1001);
            FeList.add(fe + 1001);
        }
    }
	
	public void mergeCutAndFs()
	{
		mergeList = new ArrayList<Integer>();
		mergeList.addAll(cutSet);
		mergeList.addAll(FsList);
		
		Collections.sort(mergeList);

		
		for(Integer i : mergeList)
			System.out.println("mergeList: " + i);
		
		System.out.println("Lists Ce " + cutSet);
		System.out.println("Lists Fs+1 " + FsList);
	}
		
	/**
	* The method calculating the intensity by classifying pixel values
	* in each of the 25 intensity bins in which they will be placed.
	*/ 
	
	public void getIntensity(int red, int blue, int green)
	{
		int index = 0;
		double pixelIntensity;

		index = 0;
		pixelIntensity = (0.299*red) + (0.587*green) + (0.114*blue);
		
		if (pixelIntensity >= 250)
		index = 25;
		else if (pixelIntensity>= 0)
		index = (int)pixelIntensity/10 +1;	
		
		intensityBins[index]++;					
	}
	
}