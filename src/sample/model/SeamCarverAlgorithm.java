package sample.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SeamCarverAlgorithm {
    private BufferedImage input;
    private BufferedImage seam_carved_image;
    private ArrayList<Seam> seams;

    public SeamCarverAlgorithm(String input_path) throws IOException {
        this.seam_carved_image = null;

        try {
            File input_file = new File(input_path);
            this.input = ImageIO.read(input_file);
            seam_carved_image = copyImage(input);
        } catch (IOException e) {
            System.out.println(e);
            throw (e);
        }
        seams = new ArrayList<>();

    }
    public double[][] calculateEnergyMap(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int xpPixel = 0, xnPixel = 0, ypPixel = 0, ynPixel = 0;
        double[][] energyMap = new double[width][height];


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double xDistance, yDistance, totalEnergy;
                if (i != 0 && i != width - 1) {
                    xpPixel = image.getRGB(i - 1, j);
                    xnPixel = image.getRGB(i + 1, j);
                }

                xDistance = getEnergy(xpPixel, xnPixel);
                if (j != 0 && j != height - 1) {
                    ypPixel = image.getRGB(i, j - 1);
                    ynPixel = image.getRGB(i, j + 1);
                }

                yDistance = getEnergy(ypPixel, ynPixel);

                totalEnergy = xDistance + yDistance;
                energyMap[i][j] = totalEnergy;
            }
        }

        return energyMap;
    }
    public double getEnergy(int rgb1, int rgb2) {
        double blue1 = (rgb1) & 0xff;
        double green1 = (rgb1 >> 8) & 0xff;
        double red1 = (rgb1 >> 16) & 0xff;

        double blue2 = (rgb2) & 0xff;
        double green2 = (rgb2 >> 8) & 0xff;
        double red2 = (rgb2 >> 16) & 0xff;

        double energy = (red1 - red2) * (red1 - red2) + (green1 - green2) * (green1 - green2) + (blue1 - blue2) * (blue1 - blue2);

        return energy;


    }
    public void EnergyMapPrint() throws IOException {
        EnergyMapView(input);
    }
    public void EnergyMapView(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] energyMap = calculateEnergyMap(image);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(energyMap[i][j] + " ");
            }
            System.out.println("");
        }

    }
    public BufferedImage copyImage(BufferedImage img) {
        ColorModel color_model = img.getColorModel();
        boolean isAlphaPremultiplied = color_model.isAlphaPremultiplied();
        WritableRaster writable_raster = img.copyData(null);

        return new BufferedImage(color_model, writable_raster, isAlphaPremultiplied, null);

    }
    public Seam getHorizontalSeam(double[][] energyMap) {
        int width = energyMap.length;
        int height = energyMap[0].length;
        Seam seam = new Seam(width, "h");
        double[][] horizontal = new double[width][height];
        int[][] p = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double min_val;

                if (i == 0) {
                    horizontal[i][j] = energyMap[i][j];
                    p[i][j] = -1;
                    continue;
                } else if (j == 0) {
                    min_val = Math.min(horizontal[i - 1][j], horizontal[i - 1][j + 1]);
                    if (min_val == horizontal[i - 1][j]) {
                        p[i][j] = j;
                    } else {
                        p[i][j] = j + 1;
                    }
                } else if (j == height - 1) {
                    min_val = Math.min(horizontal[i - 1][j], horizontal[i - 1][j - 1]);
                    if (min_val == horizontal[i - 1][j]) {
                        p[i][j] = j;
                    } else {
                        p[i][j] = j - 1;
                    }
                } else {
                    min_val = Math.min(horizontal[i - 1][j], Math.min(horizontal[i - 1][j - 1], horizontal[i - 1][j + 1]));

                    if (min_val == horizontal[i - 1][j]) {
                        p[i][j] = j;
                    } else if (min_val == horizontal[i - 1][j - 1]) {
                        p[i][j] = j - 1;
                    } else {
                        p[i][j] = j + 1;
                    }

                }


                horizontal[i][j] = energyMap[i][j] + min_val;
            }
        }

        double min_energy = horizontal[width - 1][0];
        int min = 0;
        for (int j = 0; j < height; j++) {
            if (min_energy > horizontal[width - 1][j]) {
                min_energy = horizontal[width - 1][j];
                min = j;
            }
        }
        seam.setEnergy(min_energy);
        for (int i = width - 1; i >= 0; i--) {
            seam.setPixels(i, min);
            min = p[i][min];

        }

        return seam;
    }
    public Seam getVerticalSeam(double[][] energyMap) {
        int width = energyMap.length;
        int height = energyMap[0].length;

        Seam seam = new Seam(height, "v");


        double[][] vertical = new double[width][height];


        int[][] p = new int[width][height];


        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                double min_val;


                if (j == 0) {
                    vertical[i][j] = energyMap[i][j];
                    p[i][j] = -1;
                    continue;
                } else if (i == 0) {
                    min_val = Math.min(vertical[i][j - 1], vertical[i + 1][j - 1]);
                    if (min_val == vertical[i][j - 1]) {
                        p[i][j] = i;
                    } else {
                        p[i][j] = i + 1;
                    }
                } else if (i == width - 1) {
                    min_val = Math.min(vertical[i][j - 1], vertical[i - 1][j - 1]);
                    if (min_val == vertical[i][j - 1]) {
                        p[i][j] = i;
                    } else {
                        p[i][j] = i - 1;
                    }
                } else {
                    min_val = Math.min(vertical[i][j - 1], Math.min(vertical[i - 1][j - 1], vertical[i + 1][j - 1]));

                    if (min_val == vertical[i][j - 1]) {
                        p[i][j] = i;
                    } else if (min_val == vertical[i - 1][j - 1]) {
                        p[i][j] = i - 1;
                    } else {
                        p[i][j] = i + 1;
                    }


                }


                vertical[i][j] = energyMap[i][j] + min_val;
            }
        }


        double min_energy = vertical[0][height - 1];
        int min = 0;
        for (int i = 0; i < width; i++) {
            if (min_energy > vertical[i][height - 1]) {
                min_energy = vertical[i][height - 1];
                min = i;
            }
        }

        seam.setEnergy(min_energy);


        for (int j = height - 1; j >= 0; j--) {
            seam.setPixels(j, min);
            min = p[min][j];
        }

        return seam;
    }
    public void carve(int width, int height) {
        int carve_horizontal = input.getHeight() - height;
        int carve_vertical = input.getWidth() - width;

        int total_carve = carve_horizontal + carve_vertical;


        while (carve_horizontal > 0 || carve_vertical > 0) {
            Seam horizontal_seam;
            Seam vertical_seam;

            Progress(total_carve, carve_horizontal + carve_vertical);


            double[][] energy_table = calculateEnergyMap(seam_carved_image);


            if (carve_horizontal > 0 && carve_vertical > 0) {
                horizontal_seam = getHorizontalSeam(energy_table);
                vertical_seam = getVerticalSeam(energy_table);

                if (horizontal_seam.getEnergy() < vertical_seam.getEnergy()) {
                    seams.add(horizontal_seam);
                    removeSeam(horizontal_seam);
                    carve_horizontal--;
                } else {
                    seams.add(vertical_seam);
                    removeSeam(vertical_seam);
                    carve_vertical--;
                }
            } else if (carve_horizontal > 0) {
                horizontal_seam = getHorizontalSeam(energy_table);
                seams.add(horizontal_seam);
                removeSeam(horizontal_seam);
                carve_horizontal--;
            } else {
                vertical_seam = getVerticalSeam(energy_table);
                seams.add(vertical_seam);
                removeSeam(vertical_seam);
                carve_vertical--;
            }

        }

    }
    public void Progress(int t, int c) {
        System.out.println((t-c)+" Seam Removed From "+t);
    }
    public void removeSeam(Seam seam) {
        int width = seam_carved_image.getWidth();
        int height = seam_carved_image.getHeight();
        BufferedImage image_new;


        if (seam.getDir().equals("h")) {

            image_new = new BufferedImage(width, height - 1, BufferedImage.TYPE_INT_RGB);


            for (int i = 0; i < width; i++) {
                boolean Removable = false;
                for (int j = 0; j < height - 1; j++) {

                    if (seam.getPixels()[i] == j) {
                        Removable = true;
                    }
                    if (Removable)
                        image_new.setRGB(i, j, seam_carved_image.getRGB(i, j + 1));
                    else
                        image_new.setRGB(i, j, seam_carved_image.getRGB(i, j));
                }
            }
        } else {

            image_new = new BufferedImage(width - 1, height, BufferedImage.TYPE_INT_RGB);


            for (int j = 0; j < height; j++) {
                boolean Removable = false;
                for (int i = 0; i < width - 1; i++) {

                    if (seam.getPixels()[j] == i) {
                        Removable = true;
                    }
                    if (Removable) {
                        image_new.setRGB(i, j, seam_carved_image.getRGB(i + 1, j));
                    } else {
                        image_new.setRGB(i, j, seam_carved_image.getRGB(i, j));
                    }
                }
            }
        }


        seam_carved_image = image_new;
    }
    public void saveFinalImage(String file_path, String type) throws IOException {
        try {
            File output_file = new File(file_path);
            ImageIO.write(seam_carved_image, type, output_file);
            System.out.println("Carving completed....");
        } catch (IOException e) {
            System.out.println(e);
            throw (e);
        }
    }



}





























   /*public void saveSeamTable() throws IOException {
        int[][] seam_image_array = new int[input.getWidth()][input.getHeight()];

        int current_width = seam_carved_image.getWidth();
        int current_height = seam_carved_image.getHeight();


        for (int i = 0; i < current_width; i++)
            for (int j = 0; j < current_height; j++)
                seam_image_array[i][j] = seam_carved_image.getRGB(i, j);


        for (int s = seams.size() - 1; s >= 0; s--) {
            getProgress(seams.size(), s + 1);


            Seam seam = seams.get(s);
            if (seam.getDir().equals("h")) {
                for (int i = 0; i < current_width; i++) {
                    int coord = seam.getPixels()[i];
                    for (int j = current_height; j > coord; j--)
                        seam_image_array[i][j] = seam_image_array[i][j - 1];
                    seam_image_array[i][coord] = (0xffffff);

                }
                current_height++;
            } else {
                for (int j = 0; j < current_height; j++) {

                    int coord = seam.getPixels()[j];
                    for (int i = current_width; i > coord; i--)
                        seam_image_array[i][j] = seam_image_array[i - 1][j];
                    seam_image_array[coord][j] = (0xffffff);
                }

                current_width++;
            }

        }

        BufferedImage seam_image = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < seam_image.getWidth(); i++) {
            for (int j = 0; j < seam_image.getHeight(); j++) {
                seam_image.setRGB(i, j, seam_image_array[i][j]);
            }
        }

        try {
            File output_file = new File("seamTable.jpg");
            ImageIO.write(seam_image, "jpg", output_file);
            System.out.println("Seam table has been created.");
        } catch (IOException e) {
            System.out.println("Cannot create file for seam table.");
            throw (e);
        }

    }*/